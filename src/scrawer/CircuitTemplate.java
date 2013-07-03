package scrawer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import scrawer.adarpter.DataAdarpterContext;
import scrawer.adarpter.data.DataAdarpter;
import scrawer.impl.AbstractCircuit;
import util.Constants;
import util.FileUtil;
import util.Transfer;

public class CircuitTemplate extends AbstractCircuit {

	private HashMap<String, Param> captcha = new HashMap<String, Param>();

	public CircuitTemplate(String siteName, String rootUrl) {
		super(siteName, rootUrl);
	}

	/**
	 * @param siteName
	 *            站点名称
	 * @param rootUrl
	 *            根路径URL
	 * @param loginUrl
	 *            登录URL
	 * @param loginParams
	 *            登录参数
	 * @param redirects
	 *            跳转URL集合
	 * @param datasources
	 *            数据源集合
	 */
	public CircuitTemplate(String siteName, String code, String rootUrl,
			String loginUrl, List<Param> loginParams, List<Redirect> redirects,
			List<SiteDataSource> datasources) {
		super(siteName, code, rootUrl, loginUrl, loginParams, redirects,
				datasources);
	}

	public void createLoginCaptcha() {

		for (Param p : this.getLoginParams()) {
			String filePath = "";
			if (p.getType().equals(ParamType.captcha)) {
				try {

					filePath = Constants.captchaDirectory + this.getSiteName()
							+ "_login_" + p.getName() + "_captcha.jpg";

					FileOutputStream fos = FileUtil.fileOutputStream(FileUtil
							.createFile(filePath));
					createFile(fos);
					fos.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				captcha.put(p.getName(), p);
				systemInputCaptcha(filePath, p);
			}
		}
	}

	public void setCaptchaValue(String name, String value) {
		// System.out.println("验证码缓存中的内容  " + captcha.get(name));
		if (captcha.get(name) != null)
			captcha.get(name).setValue(value);
	}

	private void createFile(FileOutputStream fos) {

		this.refresh(fos);
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void systemInputCaptcha(String captchaPath, Param p) {
		System.out.print("请输入" + captchaPath + "的图片内容(Enter确认)：");
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		String captcha = "";
		try {
			captcha = stdin.readLine();
			stdin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setCaptchaValue(p.getName(), captcha);
	}

	public String post(String url, HashMap<String, String> params) {
		if (null == params)
			return this.post.setUrl(url).execute();
		else
			return this.post.setUrl(url).setParameters(params).execute();
	}

	public String get(String url) {
		return this.get.setUrl(url).execute();
	}

	public boolean executeDirectly() {
		
		System.out.println(this.getSiteName() + "_" + this.getCode() + "\t开始采集数据");
		HashMap<String, String> res = this.gatherData();
		for (String key : res.keySet()) {
			try {
				FileUtil.createFile(
						Constants.outPoutDirectory + this.getSiteName() + "_"
								+ key + "_output.html", this.charset.name(),
						res.get(key));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(HTMLUtil.delHTMLTag(res.get(key)));
		}
		DataAdarpter dap = DataAdarpterContext.getDataAdarpter(this
				.getSiteName());
		if (null == dap) {
			return true;
		}
		dap.setCt(this);
		if (res != null && res.size() > 0 && null != dap) {
			for (String key : res.keySet()) {
				
				dap.analyzer(key, res.get(key));
				System.out.println("导出 " + key + " 数据文件...");
			}
		}
		System.out.println("开始压缩上传文件!");
//		File f = new File(Constants.outPoutDirectory + this.getSiteName() + "/");
//		try {
//			Transfer.process(f, Constants.transferURL, this.getCode());
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("上传文件过程中出现异常！");
//		}
		System.out.println("上传文件成功！");
		return true;
	}

	public boolean wake() {
		System.out
				.println(this.getSiteName() + "_" + this.getCode() + "\t激活");

		this.createLoginCaptcha();
		this.login();

		return true;
	}

}
