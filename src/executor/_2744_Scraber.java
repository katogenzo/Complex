package executor;

import java.io.IOException;

import util.MD5;
import be.roam.hue.doj.Doj;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class _2744_Scraber {
	private WebClient client;
	private HtmlPage page;
	private Page p;

	public _2744_Scraber() {
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
			Doj.on(page).get("#txtUser").value("sk");
			Doj.on(page).get("#txtPwd").value("123");
			page=(HtmlPage) Doj.on(page).get("#ibtnOk").click();
			page=(HtmlPage) Doj.on(page).get("#tvMenut1").click();
			System.out.println(page);
			System.out.println(page.getWebResponse());
			System.out.println(page.getWebResponse().getContentAsString());
			
//			Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "txtStartDate").get(0).value("2013-04-24");
//			Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "txtEndDate").get(0).value("2013-06-28");
//			Page p=Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "btnExportToExcel").get(0).click();
//			System.out.println(p.getWebResponse().getContentCharset());
//			System.out.println(p.getWebResponse().getContentType());
//			
//			StreamUtil.copyStream(p.getWebResponse().getContentAsStream(), new FileOutputStream("d:/output/xinlong.xls"));
			
			
//			FileUtil.createFile("d:/output/xinlong.xls", "GBK", p.getWebResponse().getContentAsString());
//			page=this.client.getPage("http://liuxiang.mypharma.com/Wuhan/InventoryQuery.aspx");
//			p=Doj.on(page).getByAttribute("id", MatchType.ENDING_WITH, "btnExportToExcel").get(0).click();
//			StreamUtil.copyStream(p.getWebResponse().getContentAsStream(), new FileOutputStream("d:/output/xinlongInventory.xls"));
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
		String url = "http://221.133.237.227:8002/login.aspx";
		_2744_Scraber s = new _2744_Scraber();
		s.scraber(url);

	}
}
