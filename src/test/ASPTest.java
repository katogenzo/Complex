package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import be.roam.hue.doj.Doj;
import be.roam.hue.doj.MatchType;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.javascript.host.Selection;

public class ASPTest {

	private WebClient client;
	private WebRequest request;

	public ASPTest() {
		this.client = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
		this.client.setJavaScriptEnabled(true);
		this.client.setThrowExceptionOnScriptError(false);
	}

	public void request(String url) {
		URL page = null;
		try {
			page = new URL(url);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println(page.getPath());
		try {
			HtmlPage p = client.getPage(url);
			String content = p.getWebResponse().getContentAsString();
//			System.out.println(content);
			
			HtmlInput username=p.getElementByName("tbUsername");
			HtmlInput password=p.getElementByName("tbpassword");
			HtmlInput login=p.getElementByName("btOk");
			username.setValueAttribute("FW00001SK");
			password.setValueAttribute("8888");
			List<HtmlElement> he=p.getDocumentElement().getElementsByAttribute("input", "id", "*name");
			for(HtmlElement e:he){
				System.out.println(e.getId());
			}
			
			Doj u=Doj.on(p.getDocumentElement()).get("input#tbUsername");
			System.out.println(u.value());
			HtmlPage dp=login.click();
//			System.out.println(dp.getWebResponse().getContentAsString());
			HtmlSelect cot=dp.getElementByName("ctl00$ContentPlaceHolder1$gv$ctl13$sys__ddlPagePerCount");
			List<HtmlOption> list=cot.getOptions();
			for(HtmlOption o:list){
				System.out.println(o.getIndex()+"\t"+o.getValueAttribute());
			}
			cot.setSelectedAttribute("100", true);
//			HtmlInput query=dp.getElementByName("ctl00$ContentPlaceHolder1$btQuery");
//			dp=query.click();
//			dp=cot.click();
			System.out.println("dp.getDocumentElement().getAttribute   "+dp.getDocumentElement().getAttribute("id"));
			Doj query=Doj.on(dp.getDocumentElement()).getByAttribute("id", MatchType.ENDING_WITH, "btQuery");
			System.out.println(query.value());
//			dp=(HtmlPage) query.click();
//			System.out.println(select);
//			select.value("100");
//			Page ap=select.click();
//			System.out.println(dp.getWebResponse().getContentAsString());
			
			
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		ASPTest e = new ASPTest();
		e.request("http://liuxiang.mypharma.com/Wuhan/Index.aspx");
	}
}
