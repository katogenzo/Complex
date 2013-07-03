package executor;

import java.io.FileOutputStream;
import java.io.IOException;

import util.FileUtil;
import util.MD5;
import util.StreamUtil;
import be.roam.hue.doj.Doj;
import be.roam.hue.doj.MatchType;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class _9355_Scraber {
	private WebClient client;
	private HtmlPage page;
	private Page p;

	public _9355_Scraber() {
		this.client = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
		this.client.getOptions().setJavaScriptEnabled(true);
		this.client.getOptions().setThrowExceptionOnScriptError(false);
		this.useProxy();
	}

	private void useProxy() {
		ProxyConfig pc = new ProxyConfig("192.168.1.158", 8080);
		this.client.getOptions().setProxyConfig(pc);
		DefaultCredentialsProvider dep = new DefaultCredentialsProvider();
		dep.addCredentials("", "");
		this.client.setCredentialsProvider(dep);
	}

	private void scraber(String url) {
		String pKey = "";
		try {
			this.page = this.client.getPage(url);
			pKey = MD5.toMD5String(page.getWebResponse().getContentAsString());
//			System.out.println(page.getWebResponse().getContentAsString());
			Doj.on(page).get("#tbUsername").value("FW00001SK");
			Doj.on(page).get("#tbpassword").value("8888");
			page=(HtmlPage) Doj.on(page).get("#btOk").click();
			Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "txtStartDate").get(0).value("2013-04-24");
			Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "txtEndDate").get(0).value("2013-06-28");
			Page p=Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "btnExportToExcel").get(0).click();
			System.out.println(p.getWebResponse().getContentCharset());
			System.out.println(p.getWebResponse().getContentType());
			
			StreamUtil.copyStream(p.getWebResponse().getContentAsStream(), new FileOutputStream("d:/output/xinlong.xls"));
			
			
//			FileUtil.createFile("d:/output/xinlong.xls", "GBK", p.getWebResponse().getContentAsString());
			page=this.client.getPage("http://liuxiang.mypharma.com/Wuhan/InventoryQuery.aspx");
			p=Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "btnExportToExcel").get(0).click();
			StreamUtil.copyStream(p.getWebResponse().getContentAsStream(), new FileOutputStream("d:/output/xinlongInventory.xls"));
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		this.client.closeAllWindows();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://219.139.241.142/xinlong/Default.aspx";
		_9355_Scraber s = new _9355_Scraber();
		s.scraber(url);

	}

}
