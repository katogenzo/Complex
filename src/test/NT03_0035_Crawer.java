package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;

import be.roam.hue.doj.Doj;
import be.roam.hue.doj.MatchType;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class NT03_0035_Crawer {
	private WebClient client;
	private WebRequest request;

	// static {
	// System.setProperty("java.net.useSystemProxies", "true");
	// }

	public NT03_0035_Crawer() {
		this.client = new WebClient(BrowserVersion.INTERNET_EXPLORER_9,
				"192.168.1.158", 8080);
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
			// System.out.println(content);

			Doj.on(p).get("input#txtName").value("10038");
			// System.out.println(user.value());
			Doj.on(p).get("input#txtPwd").value("200110");
			Doj login = Doj.on(p).getByAttribute("name", "btLogin");
			login.click();
			p = client
					.getPage("http://www.yht.com.cn/BC/Page/SaleRecords.aspx");
			// System.out.println(mp);
			// System.out.println(p.getWebResponse().getContentAsString());

			Doj.on(p).getByAttribute("name", "txtBeginDate")
					.value("2013-06-01");
			Doj.on(p).getByAttribute("name", "txtEndDate").value("2013-06-25");
			Doj query = Doj.on(p).getByAttribute("name", "btFind");

			HtmlSelect goods = (HtmlSelect) Doj.on(p).get("select#ddlGoods")
					.getElement(0);

			List<HtmlOption> list = goods.getOptions();
			for (HtmlOption o : list) {
				System.out.println(o.getIndex() + "\t" + o.getValueAttribute()
						+ "\t" + o.getText());

				if (o.getValueAttribute().equals(""))
					continue;

				goods.setSelectedAttribute(o, true);

				HtmlPage storePage = (HtmlPage) query.click();
				// HtmlPage res=(HtmlPage)
				// Doj.on(storePage).get("table#tabGoodsInfo td").select();
				// Doj t=Doj.on(storePage).getByAttribute("valign", "middle");
				//
				// for(Doj d:t){
				//
				// System.out.println(d.trimmedText()+"=========");
				// }

				DomNodeList<DomNode> nodes = storePage
						.querySelectorAll("table#tabGoodsInfo td[valign=middle]");
				DomNodeList<DomNode> values = storePage
						.querySelectorAll("table#tabGoodsInfo input");

				int size = nodes.size();
				int index = 0;
				for (; index < size; index++) {
					DomNode n = nodes.get(index);
					System.out.println(n.getTextContent() + "\t");
					NamedNodeMap nmm = values.get(index).getAttributes();
				}

				// Document doc = Jsoup.parse(storePage.getWebResponse()
				// .getContentAsString());
				// Elements text =
				// doc.select("table#tabGoodsInfo td[valign=middle]");
				// Elements value = doc.select("table#tabGoodsInfo input");
				//
				// int index=0;
				// int size=text.size();
				// for(;index<size;index++){
				// System.out.println("------"+text.get(index).text()+"------\t---------"+value.get(index).val().trim()+"-------");
				// }

			}

			// HtmlInput username = p.getElementByName("tbUsername");
			// HtmlInput password = p.getElementByName("tbpassword");
			// HtmlInput login = p.getElementByName("btOk");
			// username.setValueAttribute("FW00001SK");
			// password.setValueAttribute("8888");
			// List<HtmlElement> he = p.getDocumentElement()
			// .getElementsByAttribute("input", "id", "*name");
			// for (HtmlElement e : he) {
			// System.out.println(e.getId());
			// }
			//
			// Doj u = Doj.on(p.getDocumentElement()).get("input#tbUsername");
			// System.out.println(u.value());
			// HtmlPage dp = login.click();
			// // System.out.println(dp.getWebResponse().getContentAsString());
			// HtmlSelect cot = dp
			// .getElementByName("ctl00$ContentPlaceHolder1$gv$ctl13$sys__ddlPagePerCount");
			// List<HtmlOption> list = cot.getOptions();
			// for (HtmlOption o : list) {
			// System.out.println(o.getIndex() + "\t" + o.getValueAttribute());
			// }
			// cot.setSelectedAttribute("100", true);
			// // HtmlInput
			// //
			// query=dp.getElementByName("ctl00$ContentPlaceHolder1$btQuery");
			// // dp=query.click();
			// // dp=cot.click();
			// System.out.println("dp.getDocumentElement().getAttribute   "
			// + dp.getDocumentElement().getAttribute("id"));
			// Doj query = Doj.on(dp.getDocumentElement()).getByAttribute("id",
			// MatchType.ENDING_WITH, "btQuery");
			// System.out.println(query.value());
			// // dp=(HtmlPage) query.click();
			// // System.out.println(select);
			// // select.value("100");
			// // Page ap=select.click();
			// // System.out.println(dp.getWebResponse().getContentAsString());

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NT03_0035_Crawer e = new NT03_0035_Crawer();
		e.request("http://www.yht.com.cn/BC/page/login.aspx");
	}

}
