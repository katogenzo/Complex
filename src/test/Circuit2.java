package test;

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

public class Circuit2 extends AbstractCircuit{
	public Circuit2(String siteName, String rootUrl) {
		super(siteName, rootUrl);
	}


	public Circuit2(String siteName, String rootUrl, String loginUrl,
			List<Param> loginParams, List<Redirect> redirects,
			List<SiteDataSource> datasources) {
		super(siteName, "",rootUrl, loginUrl, loginParams, redirects, datasources);
		// TODO Auto-generated constructor stub
	}


	public static void main(String[] argv) {
		String siteName = "http://www.gkzj.com";
		String rootUrl = "http://www.gkzj.com:81/index.asp";
		String loginUrl = "http://www.gkzj.com:81/login1.asp";

		ArrayList<Param> list = new ArrayList<Param>();
		list.add(new Param().setName("username").setType(ParamType.text)
				.setValue("ZMSK"));
		list.add(new Param().setName("password").setType(ParamType.text)
				.setValue("821120"));
		Param company = new Param()
				.setName("company")
				.setType(ParamType.text).setValue("6");
		list.add(company);
		list.add(new Param().setName("Submit").setType(ParamType.text)
				.setValue("登录"));
		list.add(new Param().setName("tourl").setType(ParamType.text)
				.setValue("member.php"));
		
//		ArrayList<Redirect> redirects = new ArrayList<Redirect>();
//		redirects.add(new Redirect().setUrl(
//				"http://www.drugoogle.com/index/index.htm").setCallback(true));
//		redirects
//				.add(new Redirect()
//						.setUrl("http://www.drugoogle.com/index/sms.jsp?randCode=000000000&url=http://www.drugoogle.com/index/index.htm&sUrl=http://www.drugoogle.com/index/index.htm")
//						.setCallback(true));
		// redirects.add(new Redirect().setUrl("").setCallback(false));
		// redirects.add(new Redirect().setUrl("").setCallback(false));
		SiteDataSource sds = new SiteDataSource(
				"http://www.gkzj.com:81/kc.asp");
		ArrayList<SiteDataSource> dss = new ArrayList<SiteDataSource>();
		dss.add(sds);

//		ct.setDatasources(dss);
		
		
		Circuit2 ct = new Circuit2(siteName, rootUrl, loginUrl, list, null,dss);
//		try {
//			OutputStream ops = new FileOutputStream(new File("tst.jpg"));
//			ct.refresh(ops);
//			ops.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		BufferedReader stdin = new BufferedReader(new InputStreamReader(
//				System.in));
//		String captcha = "";
//		try {
//			captcha = stdin.readLine();
//			stdin.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		company.setValue(captcha);

		ct.login(list);

		
		ct.gatherData();
	}
}
