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

public class Circuit5 extends AbstractCircuit {
	public Circuit5(String siteName, String rootUrl) {
		super(siteName, rootUrl);
	}

	public Circuit5(String siteName, String rootUrl, String loginUrl,
			List<Param> loginParams, List<Redirect> redirects,
			List<SiteDataSource> datasources) {
		super(siteName, "",rootUrl, loginUrl, loginParams, redirects, datasources);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] argv) {
		String siteName = "南京医药药事服务有限公司";
		String rootUrl = "http://222.190.117.5:8886/elink11/system/main.html";
		String loginUrl = "http://222.190.117.5:8886/elink11/initAction/login.do";

		ArrayList<Param> list = new ArrayList<Param>();
		list.add(new Param().setName("submit").setType(ParamType.text)
				.setValue("登  陆"));
		list.add(new Param().setName("user").setType(ParamType.text)
				.setValue("1023196"));
		list.add(new Param().setName("password").setType(ParamType.text)
				.setValue("1023196"));
		list.add(new Param().setName("option").setType(ParamType.text)
				.setValue("login"));


		SiteDataSource sds = new SiteDataSource(
				"http://222.190.117.5:8886/elink11/invoiceAction/query.do");
		sds.setMethod(Method.POST);
		List<Param> dsParam = new ArrayList<Param>();
		 dsParam.add(new
		 Param().setName("dateFrom").setValue("2013-05-17").setType(ParamType.text));
		 dsParam.add(new
				 Param().setName("dateTo").setValue("2013-06-18").setType(ParamType.text));
		 dsParam.add(new
		 Param().setName("showPrice").setValue("true").setType(ParamType.text));
		 dsParam.add(new
		 Param().setName("self").setValue("false").setType(ParamType.text));
		 dsParam.add(new
		 Param().setName("start").setValue("0").setType(ParamType.text));
		 dsParam.add(new
				 Param().setName("stock").setValue("A").setType(ParamType.text));
		 dsParam.add(new
				 Param().setName("limit").setValue("1000").setType(ParamType.text));
		sds.setParams(dsParam);
		ArrayList<SiteDataSource> dss = new ArrayList<SiteDataSource>();
		dss.add(sds);

		Circuit5 ct = new Circuit5(siteName, rootUrl, loginUrl, list,
				null, dss);


		ct.login();
		System.out.println(ct.gatherData());
		sds.setUrlTemplate("http://222.190.117.5:8886/elink11/poinoutAction/query.do");
		System.out.println(ct.gatherData());
		sds.setUrlTemplate("http://222.190.117.5:8886/elink11/poinoutAction/query.do");
		System.out.println(ct.gatherData());
		sds.setUrlTemplate("http://222.190.117.5:8886/elink11/storageAction/query.do");
		System.out.println(ct.gatherData());
	}
}
