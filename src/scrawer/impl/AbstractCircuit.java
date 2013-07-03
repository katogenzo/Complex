package scrawer.impl;

import harmo.Get;
import harmo.HttpMethodProcessor;
import harmo.Method;
import harmo.Post;
import harmo.Proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import scrawer.Param;
import scrawer.ParamType;
import scrawer.Redirect;
import scrawer.SiteDataSource;
import scrawer.circuit.CircuitInterface;
import util.FileUtil;
import util.MD5;

/**
 * 数据抓取流程抽象类
 * 
 * @author wangbibo
 * 
 */
public class AbstractCircuit implements CircuitInterface {

	/**
	 * 查询的网站名称
	 */
	private String siteName;
	/**
	 * 网站登录页面地址
	 */
	private String rootUrl;
	/**
	 * 网站登录路径
	 */
	private String loginUrl;
	/**
	 * 登陆时需要提交的参数列表
	 */
	private List<Param> loginParams;
	/**
	 * 登录后需要跳转的URL列表
	 */
	private List<Redirect> redirects;

	/**
	 * 在站点上需要访问的数据源
	 */
	private List<SiteDataSource> datasources;

	private HttpMethodProcessor processor;

	private int initStatus;

	private String contrastkey;

	protected Get get;

	protected Post post;

	protected Charset charset;

	protected String code;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private HashMap<String, String> innerParam;

	public AbstractCircuit(String siteName, String rootUrl) {
		initialize(siteName, rootUrl);
		validate();
	}

	public AbstractCircuit(String siteName,String code, String rootUrl, String loginUrl,
			List<Param> loginParams, List<Redirect> redirects,
			List<SiteDataSource> datasources) {
		initialize(siteName, rootUrl);
		this.code=code;
		this.loginUrl = loginUrl;
		this.loginParams = loginParams;
		this.redirects = redirects;
		this.datasources = datasources;
		validate();
		analyzParam();
	}

	private void initialize(String siteName, String rootUrl) {
		this.siteName = siteName;
		this.rootUrl = rootUrl;
		this.processor = HttpProcessorFactory.getInstance().makeProcessor(
				this.siteName);
		this.get = this.processor.get(rootUrl);
		this.post = this.processor.post(rootUrl);
		this.charset = this.processor.getCharset();
		this.innerParam = new HashMap<String, String>();
	}

	@Override
	public OutputStream refresh(OutputStream ops) {

		if (this.initStatus > 0) {
			this.refresh();
			for (Param p : this.loginParams) {
				if (p.getType().equals(scrawer.ParamType.captcha)) {
					ops = this.get.setUrl(p.getSource()).executeAsStream(ops);
				}
			}
		} else {
			try {
				ops.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ops;
	}

	@Override
	public boolean refresh() {
		String res = null;
		if (this.initStatus > 0) {
			if (this.rootUrl.trim().length() > 1) {
				res = this.get.setUrl(this.rootUrl).execute();
				// System.out.println("登录前：\n" + res);
				// System.out.println(res.length());
				// System.out.println(MD5.toMD5String(res));

				this.contrastkey = MD5.toMD5String(res);
				fillInHtmlParam(res);
			}
		}
		return res == null;
	}

	@Override
	public boolean login(HashMap<String, String> parameters) {
		String res = null;
		if (null == this.contrastkey)
			this.refresh();
		if (this.initStatus > 0) {
			allcatParam(parameters);
			res = this.post.setUrl(this.loginUrl).setParameters(parameters)
					.execute();
			// HashMap<String, String> map = this.post.getHeader();
			// for (String key : map.keySet()) {
			// System.out.println(key + "\t" + map.get(key));
			// }
			// for (String key : parameters.keySet()) {
			// System.out.println(key + "\t" + parameters.get(key));
			// }

			fillInHtmlParam(res);
			System.out.println("登录后页面内容 ： " + res);
		}
		this.redirect();
		if (this.rootUrl.trim().length() > 1) {
			res = this.get.setUrl(this.rootUrl).execute();

			System.out.println("登录后：\n" + res);
			// if (null != res) {
			// System.out.println(res.length());
			// }
			System.out.println(MD5.toMD5String(res));
		}
		if (null == this.contrastkey)
			return false;
		else
			return this.contrastkey.equals(MD5.toMD5String(res));
	}

	@Override
	public boolean login(List<Param> parameters) {

		HashMap<String, String> params = new HashMap<String, String>();
		if (this.initStatus > 0) {
			// allocatParam();
			if (null != parameters && parameters.size() > 0) {
				for (Param p : parameters) {
					params.put(p.getName(), p.getValue());
				}
			}
		}
		return this.login(params);
	}

	public boolean login() {
		// allocatParam();
		return this.login(this.loginParams);
	}

	@Override
	public HashMap<String, String> gatherData() {
		HashMap<String, String> allData = new HashMap<String, String>();
		String res = null;
		// this.refresh();
		if (null != this.datasources && this.datasources.size() > 0) {
			for (SiteDataSource ds : datasources) {
				res = this.gatherData(ds);
				allData.put(ds.getName(), res);
			}
		}
		// System.out.println("获取结果" + res);
		// System.out.println("获取结果" + HTMLUtil.stripHtml(res));
		return allData;
	}

	private String gatherData(SiteDataSource ds) {
		String res = null;
		if (ds.getMethod().equals(Method.POST)) {
			res = this.get.setUrl(ds.generateUrl()).execute();
			fillInHtmlParam(res);
			List<Param> params = ds.getParams();
			if (null != params && params.size() > 0) {
				HashMap<String, String> parameters = new HashMap<String, String>();
				for (Param p : params) {
					parameters.put(p.getName(), p.getValue());
					// System.out.println(p.getName() + "\t" + p.getValue());
				}
				allcatParam(parameters);
				System.out.println("查询请求参数");
				for (String p : parameters.keySet()) {
					System.out.println(p + "\t" + parameters.get(p));
				}
				this.post.setParameters(parameters);
			}
			this.post.setUrl(ds.generateUrl());
			res = this.post.execute();
			fillInHtmlParam(res);
		} else {
			this.get.setUrl(ds.generateUrl());
			res = this.get.execute();
			fillInHtmlParam(res);
		}

		return res;
	}

	private boolean redirect() {
		if (null != this.redirects && this.redirects.size() > 0) {
			for (Redirect rd : this.redirects) {
				// if (rd.isCallback()) {
				String res = this.get.setUrl(rd.getUrl()).execute();
				fillInHtmlParam(res);
				// System.out.println("跳转后\t " + res);
				// } else
				// this.get.setUrl(rd.getUrl()).touch();

			}
		}
		return true;
	}

	private void fillInHtmlParam(String res) {
		if (null != res && this.innerParam.size() > 0) {
			Document doc = Jsoup.parse(res);
			for (String id : this.innerParam.keySet()) {
				// System.out.println("待获取内容 :" + id);
				// System.out.println("获取内容 :" + id + "\t"
				// + doc.getElementById(id));
				if (null != doc.getElementById(id)) {
					// System.out.println("页面中内容   " + id + "\t"
					// + doc.getElementById(id).attr("value"));
					this.innerParam.put(id, doc.getElementById(id)
							.attr("value"));
				}
			}
			// allocatParam();
		}
	}

	public List<SiteDataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<SiteDataSource> datasources) {
		this.datasources = datasources;
		validate();
		analyzParam();
	}

	public String getSiteName() {
		return this.siteName;
	}

	public String getRootUrl() {
		return this.rootUrl;
	}

	public String getLoginUrl() {
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
		validate();
	}

	public List<Param> getLoginParams() {
		return this.loginParams;
	}

	public void setLoginParams(List<Param> loginParams) {
		this.loginParams = loginParams;
		validate();
		analyzParam();
	}

	public List<Redirect> getRedirects() {
		return this.redirects;
	}

	public void setRedirects(List<Redirect> redirects) {
		this.redirects = redirects;
	}

	public Charset getCharset() {
		return this.charset;
	}

	private void validate() {
		this.initStatus = 0;
		if (null != this.loginUrl && null != this.loginParams) {
			this.initStatus = 1;
		}
		if (null != this.datasources) {
			this.initStatus = 2;
		}
	}

	private void allocatParam() {
		if (null != this.loginParams && this.loginParams.size() > 0) {
			for (Param p : this.loginParams) {
				if (p.getType().equals(ParamType.inhtmlById)) {
					p.setValue(innerParam.get(p.getName()));
				}
				// System.out.println(p.getName());
			}
		}

		if (null != this.datasources && this.datasources.size() > 0) {
			for (SiteDataSource ds : this.datasources) {
				if (null != ds.getParams() && ds.getParams().size() > 0) {
					for (Param p : ds.getParams()) {
						if (p.getType().equals(ParamType.inhtmlById)) {
							p.setValue(innerParam.get(p.getName()));
						}
					}
				}
			}
		}
	}

	private void allcatParam(HashMap<String, String> params) {
		// System.out.println("传入的参数params" + params);
		if (null != params && params.size() > 0) {
			// for (String key : this.innerParam.keySet()) {
			// System.out.println("Inner HTML 中的内容\t" + key + "\t"
			// + this.innerParam.get(key));
			// }

			for (String key : params.keySet()) {
				if (this.innerParam.containsKey(key)) {
					// System.out.println(key + "\t" +
					// this.innerParam.get(key));
					params.put(key, this.innerParam.get(key));
				}
			}
		}
	}

	private void analyzParam() {

		if (null != this.loginParams && this.loginParams.size() > 0) {
			for (Param p : this.loginParams) {
				if (p.getType().equals(ParamType.inhtmlById)) {
					if (this.innerParam.containsKey(p.getName())) {
						p.setValue(this.innerParam.get(p.getName()));
					} else
						this.innerParam.put(p.getName(), p.getValue());
				}
			}
		}
		if (null != this.datasources && this.datasources.size() > 0) {
			for (SiteDataSource ds : this.datasources) {
				if (null != ds.getParams() && ds.getParams().size() > 0) {
					for (Param p : ds.getParams()) {
						if (p.getType().equals(ParamType.inhtmlById)) {
							if (this.innerParam.containsKey(p.getName())) {
								p.setValue(this.innerParam.get(p.getName()));
							} else
								this.innerParam.put(p.getName(), p.getValue());
						}
					}
				}
			}
		}
	}
	
	public HashMap<String, String> alive(){
		System.out.println("keep alive......");
		return this.gatherData();
	}

}

class HttpProcessorFactory {
	private static HttpProcessorFactory instance = new HttpProcessorFactory();
	private static final String proxyConfig = "proxy.properties";
	private static Hashtable<String, HttpMethodProcessor> processorContext = new Hashtable<String, HttpMethodProcessor>();

	public static HttpProcessorFactory getInstance() {
		return instance;
	}

	public HttpMethodProcessor makeProcessor(String siteName) {

		Proxy proxy = getProxy(proxyConfig);
		HttpMethodProcessor processor;
		if (null != proxy) {
			processor = new HttpMethodProcessor(proxy);
		} else
			processor = new HttpMethodProcessor();

		processorContext.put(siteName, processor);
		return processor;
	}

	private Proxy getProxy(String proxyConfig) {
		HashMap<String, String> map = FileUtil.getPropertiesConfig(proxyConfig);
		if (!map.containsKey("proxy") || map.get("proxy").trim().length() < 1) {
			return null;
		} else {
			return new Proxy(map.get("protocal").trim(), map.get("proxy")
					.trim(), Integer.parseInt(map.get("port").trim()), map.get(
					"username").trim(), map.get("password").trim());
		}
	}
}
