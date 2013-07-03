package test;

import harmo.Method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import scrawer.Param;
import scrawer.ParamType;
import scrawer.Redirect;
import scrawer.SiteDataSource;
import scrawer.impl.AbstractCircuit;

public class Circuit3 extends AbstractCircuit {
	public Circuit3(String siteName, String rootUrl) {
		super(siteName, rootUrl);
	}

	public Circuit3(String siteName, String rootUrl, String loginUrl,
			List<Param> loginParams, List<Redirect> redirects,
			List<SiteDataSource> datasources) {
		super(siteName, "",rootUrl, loginUrl, loginParams, redirects, datasources);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv) {
		String siteName = "http://218.2.111.134:8080";
		String rootUrl = "http://218.2.111.134:8080/CstQuery/Flow.aspx";
		String loginUrl = "http://218.2.111.134:8080/userLogin.aspx";

		ArrayList<Param> list = new ArrayList<Param>();
		list.add(new Param().setName("drpCompany").setType(ParamType.text)
				.setValue("1"));
		list.add(new Param().setName("txtUser").setType(ParamType.text)
				.setValue("10000501"));
		list.add(new Param().setName("txtPass").setType(ParamType.text)
				.setValue("10000501"));
		list.add(new Param()
				.setName("__VIEWSTATE")
				.setType(ParamType.text)
				.setValue(
						"/wEPDwUKMTkzNzE5ODUzMw9kFgICAw9kFgICAQ8QDxYCHgtfIURhdGFCb3VuZGdkEBUBHuWbveiNr+aOp+iCoeWNl+S6rOaciemZkOWFrOWPuBUBATEUKwMBZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQVidG5PawUIYnRuUmVzZXRQ2ieaTSMFuM95cgQW7E7BiQ+Qww=="));

		Param verifyCode = new Param().setName("txtVerify")
				.setType(ParamType.captcha)
				.setSource("http://218.2.111.134:8080/ValiateNum.ashx");
		list.add(verifyCode);

		list.add(new Param().setName("btnOk.x").setType(ParamType.text)
				.setValue("61"));
		list.add(new Param().setName("btnOk.y").setType(ParamType.text)
				.setValue("18"));

		ArrayList<Redirect> redirects = new ArrayList<Redirect>();
		redirects.add(new Redirect().setUrl(
				"http://218.2.111.134:8080/Default.aspx").setCallback(true));
		redirects.add(new Redirect().setUrl(
				"http://218.2.111.134:8080/top.aspx").setCallback(true));
		redirects.add(new Redirect().setUrl(
				"http://218.2.111.134:8080/right.aspx").setCallback(true));
		redirects.add(new Redirect().setUrl(
				"http://218.2.111.134:8080/left.aspx").setCallback(true));
		// redirects
		// .add(new Redirect()
		// .setUrl("http://www.drugoogle.com/index/sms.jsp?randCode=000000000&url=http://www.drugoogle.com/index/index.htm&sUrl=http://www.drugoogle.com/index/index.htm")
		// .setCallback(true));
		// redirects.add(new Redirect().setUrl("").setCallback(false));
		// redirects.add(new Redirect().setUrl("").setCallback(false));

		SiteDataSource sds = new SiteDataSource(
				"http://218.2.111.134:8080/CstQuery/LotQuery.aspx");
		sds.setMethod(Method.GET);
		List<Param> dsParam=new ArrayList<Param>();
//		dsParam.add(new Param().setName("__EVENTTARGET").setValue("gv$ctl13$ctl01").setType(ParamType.text));
//		dsParam.add(new Param().setName("gv$ctl01$HidRowIndex").setValue("-1").setType(ParamType.text));
//		dsParam.add(new Param().setName("gv$ctl13$netPage_input").setValue("1").setType(ParamType.text));
//		dsParam.add(new Param().setName("gv$ctl13$ctl01").setValue("10000").setType(ParamType.text));
		dsParam.add(new Param().setName("ASPxPopupControl1WS").setValue("0%3A0%3A-1%3A0%3A0%3A0%3A0%3A").setType(ParamType.text));
		sds.setParams(dsParam);
		ArrayList<SiteDataSource> dss = new ArrayList<SiteDataSource>();
		dss.add(sds);

		Circuit3 ct = new Circuit3(siteName, rootUrl, loginUrl, list,
				redirects, dss);
		try {
			OutputStream ops = new FileOutputStream(new File("tst.jpg"));
			ct.refresh(ops);
			ops.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		String captcha = "";
		try {
			captcha = stdin.readLine();
			stdin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		verifyCode.setValue(captcha);

		// company.setValue(captcha);

		ct.login();

		ct.gatherData();
	}
}
