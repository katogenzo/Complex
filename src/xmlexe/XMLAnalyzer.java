package xmlexe;

import harmo.Method;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import scrawer.CircuitTemplate;
import scrawer.Param;
import scrawer.ParamType;
import scrawer.Redirect;
import scrawer.SiteDataSource;
import util.DateUtil;
import util.HTMLUtil;
import exception.ConfigException;
import exception.ExceptionMessage;

public class XMLAnalyzer {

	public static Hashtable<String, CircuitTemplate> circuits = new Hashtable<String, CircuitTemplate>();
	public static Hashtable<String,Param> sources=new Hashtable<String,Param>();
	public static Hashtable<String,Object> RAW_sources=new Hashtable<String,Object>();
	public static void init(String fileName) throws ConfigException {
		InputStream inputStream = ClassLoader
				.getSystemResourceAsStream(fileName);
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		analyzer(doc);
	}

	private static void analyzer(Document doc) throws ConfigException {

		@SuppressWarnings("unchecked")
		List<Element> source=doc.selectNodes("/circuits/sources/source");
		for(Element e:source){
			String name=null==e.selectSingleNode("./@name") ? "" : e
					.selectSingleNode("./@name").getStringValue();
			String value=null==e.selectSingleNode("./@value") ? "" : e
					.selectSingleNode("./@value").getStringValue();
			String type=null==e.selectSingleNode("./@type") ? "" : e
					.selectSingleNode("./@type").getStringValue();
			Param p=new Param().setName(name).setValue(value).setType(ParamType.valueOf(type));
			
			if(p.getType().equals(ParamType.date)){
				String pattern=null==e.selectSingleNode("./@pattern") ? "" : e
						.selectSingleNode("./@pattern").getStringValue();
				if(pattern.equals(""))
					pattern=Param.DEFAULT_DATE_PATTERN;
				try {
					Date d=DateUtil.getDateWithPatterValue(pattern, p.getValue());
					RAW_sources.put(p.getName(), d);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			sources.put(p.getName(), p);
		}

		@SuppressWarnings("unchecked")
		List<Element> list = doc.selectNodes("/circuits/circuit");
		for (Element e : list) {
			String siteName = null == e.selectSingleNode("./@site") ? "" : e
					.selectSingleNode("./@site").getStringValue();
			String rootUrl = null == e.selectSingleNode("./rootUrl/@url") ? ""
					: e.selectSingleNode("./rootUrl/@url").getStringValue();
			String code = null == e.selectSingleNode("./@code") ? "" : e
					.selectSingleNode("./@code").getStringValue();

			Node login = e.selectSingleNode("./loginUrl");
			String loginUrl = null == login.selectSingleNode("./@url") ? ""
					: login.selectSingleNode("./@url").getStringValue();

			loginUrl = HTMLUtil.replaceDLEC(loginUrl);
			@SuppressWarnings("unchecked")
			List<Element> loginParamSouce = login
					.selectNodes("./params//param");

			List<Param> params = new ArrayList<Param>();
			for (Element param : loginParamSouce) {
				String type = null == param.attributeValue("type") ? "" : param
						.attributeValue("type").trim();
				String name = null == param.attributeValue("name") ? "" : param
						.attributeValue("name").trim();
				String value = param.getText().trim();
				String sourceUrl = null == param.attributeValue("sourceUrl") ? ""
						: param.attributeValue("sourceUrl").trim();

				sourceUrl = HTMLUtil.replaceDLEC(sourceUrl);
				Param p = new Param();
				try {
					p.setName(name).setType(ParamType.valueOf(type))
							.setValue(value).setSource(sourceUrl);
				} catch (java.lang.IllegalArgumentException e2) {
					throw new ConfigException(
							ExceptionMessage.getExceptionMessage(type,
									ExceptionMessage.IllegalArgument));
				}
				params.add(p);
			}

			List<Redirect> redirects = new ArrayList<Redirect>();
			@SuppressWarnings("unchecked")
			List<Element> redirect = e.selectNodes("./redirects//redirect");
			for (Element rd : redirect) {
				boolean callback = Boolean.parseBoolean(rd.attributeValue(
						"callback").trim());
				String url = null == rd.attributeValue("url") ? "" : rd
						.attributeValue("url").trim();
				url = HTMLUtil.replaceDLEC(url);
				redirects.add(new Redirect().setCallback(callback).setUrl(url));
			}

			@SuppressWarnings("unchecked")
			List<Element> dss = e.selectNodes("./datasources//datasource");
			List<SiteDataSource> sdss = new ArrayList<SiteDataSource>();
			for (Element ds : dss) {
				String name = ds.attributeValue("name");
				Node urlnode = ds.selectSingleNode("./urltempalte");
				String urlTemplate = null == urlnode.selectSingleNode("./@url") ? ""
						: urlnode.selectSingleNode("./@url").getStringValue()
								.trim();

				urlTemplate = HTMLUtil.replaceDLEC(urlTemplate);

				String method = null == urlnode.selectSingleNode("./@method") ? ""
						: urlnode.selectSingleNode("./@method")
								.getStringValue().trim();
				Method m;
				try {
					m = Method.valueOf(method);
				} catch (java.lang.IllegalArgumentException e2) {
					throw new ConfigException(
							ExceptionMessage.getExceptionMessage(method,
									ExceptionMessage.IllegalArgument));
				}
				
				//读取数据源配置
				@SuppressWarnings("unchecked")
				List<Element> dsParamSource = ds.selectNodes("./params//param");

				List<Param> dsparams = new ArrayList<Param>();
				for (Element param : dsParamSource) {
					String ptype = null == param.attributeValue("type") ? ""
							: param.attributeValue("type").trim();
					String pname = null == param.attributeValue("name") ? ""
							: param.attributeValue("name").trim();
					String value = param.getText().trim();
					String sourceUrl = null == param.attributeValue("source") ? ""
							: param.attributeValue("source").trim();
					sourceUrl = HTMLUtil.replaceDLEC(sourceUrl);
					Param p = new Param();
					
					try {
						p.setName(pname).setType(ParamType.valueOf(ptype))
								.setValue(value).setSource(sourceUrl);
						if(p.getType().equals(ParamType.date)){
							String pattern = null == param.attributeValue("pattern") ? ""
									: param.attributeValue("pattern").trim();
							Object o=RAW_sources.get(p.getSource());
							if(null!=o){
								Date d=(Date) o;
								p.setValue(DateUtil.generatePatternDate(pattern, d));
							}
									
						}
						
					} catch (java.lang.IllegalArgumentException e1) {
						throw new ConfigException(
								ExceptionMessage.getExceptionMessage(ptype,
										ExceptionMessage.IllegalArgument));
					}
					dsparams.add(p);
				}

				SiteDataSource siteDs = new SiteDataSource(urlTemplate, params)
						.setMethod(m).setParams(dsparams).setName(name);
				sdss.add(siteDs);
			}

			CircuitTemplate ct = new CircuitTemplate(siteName, code, rootUrl,
					loginUrl, params, redirects, sdss);
			circuits.put(ct.getSiteName(), ct);
		}
	}

	private static void outPut() {

	}

	public static void execute(String siteName) {
		if (!circuits.containsKey(siteName))
			return;
		else {
			CircuitTemplate ct = circuits.get(siteName);
			ct.wake();
			ct.executeDirectly();
		}
	}

	public static void executeOnTime(Date time, String siteName) {
		CircuitTemplate ct =null;
		if (!circuits.containsKey(siteName)) {
			System.out.println("不存在\t" + siteName);
			return;
		} else {
			ct = circuits.get(siteName);

			for (Param p : ct.getLoginParams()) {
				if (p.getType().equals(ParamType.captcha)) {
					ct.wake();
				}
			}

		}

		while (true) {
			Date d = new Date();
			if (!(d.after(time))) {
				ct.alive();
			}else{
				ct.executeDirectly();
				break;
			}
			try {
				System.out.println("Waiting......");
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void executeOnTime(Date time) {
		for (CircuitTemplate ct : circuits.values()) {
			ct.wake();
		}
		Date d = new Date();
		while (!(d.after(time))) {
			for (CircuitTemplate ct : circuits.values()) {
				ct.alive();
			}
			d = new Date();
		}

		for (CircuitTemplate ct : circuits.values()) {
			ct.executeDirectly();
		}

	}

	public static void main(String[] argv) {
		try {
			XMLAnalyzer.init("site-config.xml");
		} catch (ConfigException e) {
			e.printStackTrace();
		}
//		Date start = null;
//
//		String startTime = "2013-06-29 10:00:00";
//		try {
//			start = DateUtil.getDateWithPatterValue("yyyy-MM-dd HH:mm:ss",
//					startTime);
//
//			XMLAnalyzer.executeOnTime(start, "福建同春药业股份有限公司");
//			 XMLAnalyzer.execute("江苏华晓医药物流有限公司");
//			 XMLAnalyzer.execute("南京医药药事服务有限公司");
//			 XMLAnalyzer.execute("宁波医药股份有限公司");
//			 XMLAnalyzer.execute("浙江省上虞市医药有限责任公司");
//			 XMLAnalyzer.execute("绍兴县华通医药有限公司");
			// for (CircuitTemplate ct : circuits.values()) {
			// ct.execute();
			// }
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
