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

public class Circuit extends AbstractCircuit {

	public Circuit(String siteName, String rootUrl) {
		super(siteName, rootUrl);
	}


	public Circuit(String siteName, String rootUrl, String loginUrl,
			List<Param> loginParams, List<Redirect> redirects,
			List<SiteDataSource> datasources) {
		super(siteName, "",rootUrl, loginUrl, loginParams, redirects, datasources);
		// TODO Auto-generated constructor stub
	}


	public static void main(String[] argv) {
		String siteName = "drugoogle.com";
		String rootUrl = "http://www.drugoogle.com/member/index.jspx";
		String loginUrl = "http://www.drugoogle.com/index/registerloginjson.jspx?";

		ArrayList<Param> list = new ArrayList<Param>();
		list.add(new Param().setName("username").setType(ParamType.text)
				.setValue("5444"));
		list.add(new Param().setName("password").setType(ParamType.text)
				.setValue("18605888311"));
		Param verifyCode = new Param()
				.setName("verifyCode")
				.setType(ParamType.captcha)
				.setSource("http://www.drugoogle.com/verifyCode/verifyCode.jsp");
		list.add(verifyCode);

		ArrayList<Redirect> redirects = new ArrayList<Redirect>();
		redirects.add(new Redirect().setUrl(
				"http://www.drugoogle.com/index/index.htm").setCallback(true));
		redirects
				.add(new Redirect()
						.setUrl("http://www.drugoogle.com/index/sms.jsp?randCode=000000000&url=http://www.drugoogle.com/index/index.htm&sUrl=http://www.drugoogle.com/index/index.htm")
						.setCallback(true));
		// redirects.add(new Redirect().setUrl("").setCallback(false));
		// redirects.add(new Redirect().setUrl("").setCallback(false));
		
		SiteDataSource sds = new SiteDataSource(
				"http://www.drugoogle.com/member/suppliers/medicineGoto/medicinegototab1.jspx?entryId=3&medicineId=0&storegeType=1&updateTime=20130603053820&month=&year=");
		ArrayList<SiteDataSource> dss = new ArrayList<SiteDataSource>();
		dss.add(sds);
		
		Circuit ct = new Circuit(siteName, rootUrl, loginUrl, list, redirects,dss);
		try {
			OutputStream ops = new FileOutputStream(new File("tst.jpg"));
			ct.refresh(ops);
			ops.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
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

		ct.login(list);

		

		ct.setDatasources(dss);
		ct.gatherData();
	}

}
