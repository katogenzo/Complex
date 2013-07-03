package executor;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import util.CSVUtil;
import util.DateUtil;
import util.FileUtil;
import be.roam.hue.doj.Doj;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class NT03_0035_Mart_Scraber {
	private WebClient client;

	public NT03_0035_Mart_Scraber() {
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

	public void request(String url) {
		try {

			HtmlPage page = this.client.getPage(url);
			// System.out.println(page.getWebResponse().getContentAsString());
			Doj.on(page).get("#txtName").value("10038");
			Doj.on(page).get("#txtPwd").value("200110");
			Doj login = Doj.on(page.getDocumentElement()).getByAttribute(
					"name", "btLogin");
			login.click();
			// System.out.println(Doj.on(page).get("input#txtPwd").value());
			// System.out.println(p);
			// System.out.println(p.getWebResponse().getContentAsString());
			page = this.client
					.getPage("http://www.yht.com.cn/BC/Page/SaleRecords.aspx");
			Doj pDoj = Doj.on(page);
			pDoj.get("#txtBeginDate").value("2013-04-24");
			pDoj.get("#txtEndDate").value("2013-06-28");

			HtmlSelect selects = (HtmlSelect) pDoj.get("#ddlGoods").getElement(
					0);

			StringBuffer headLine = new StringBuffer();
			boolean assignHead = false;
			String colName = "";
			StringBuffer storage = new StringBuffer();
			// 循环下拉菜单进行查询
			for (int index = 0; index < selects.getOptionSize(); index++) {

				HtmlOption o = selects.getOption(index);
				if (o.getValueAttribute().equals(""))
					continue;

				selects.setSelectedAttribute(o, true);
				HtmlPage p = (HtmlPage) pDoj.getByAttribute("name", "btFind")
						.click();

				System.out.println(index + "查询" + o.getText());

				// 基本的商品信息
				Doj storeDoj = Doj.on(p);
				Doj rows = storeDoj.get("#tabGoodsInfo tr");

				// LinkedHashMap<String, String> itemInfo = new
				// LinkedHashMap<String, String>(
				// 15, 1L, false);
				StringBuffer itemInfo = new StringBuffer();
				for (Doj tr : rows) {
					Doj tabs = tr.get("td");

					for (Doj tab : tabs) {
						Doj input = tab.get("input");
						if (input.isEmpty()) {
							if (!assignHead) {
								colName += "\"" + tab.trimmedText() + "\",";
							}
						} else {
							itemInfo.append("\"" + input.value().trim() + "\",");
						}
					}
				}
				// System.out.println(colName);
				// System.out.println(itemInfo);

				// 商品销售记录
				// 销售明细行
				Doj dgGoods = storeDoj.get("#dgGoods tr");
				int i = 1;
				int height = dgGoods.size() - 2;
				Doj st = dgGoods.get(dgGoods.size() - 1).get("td");
				// 填充表头
				if (!assignHead) {
					headLine.append(colName);
					storage.append(colName).append(
							"\"" + st.get(0).trimmedText() + "\",\n");
					Doj h = dgGoods.get(0).get("td");
					for (Doj j : h) {
						headLine.append("\"" + j.trimmedText() + "\",");
					}
					headLine.append("\n");
					assignHead = true;
				}

				String saleItem = "";
				if (i == height) {
					saleItem = CSVUtil.appendCommaRow(dgGoods.get(0).get("td")
							.size());
					headLine.append(itemInfo).append(saleItem).append("\n");
				} else {
					for (; i < height; i++) {
						saleItem = "";
						Doj h = dgGoods.get(i).get("td");
						if (h.isEmpty()) {
							saleItem = CSVUtil.appendCommaRow(h.size());
							headLine.append(itemInfo).append(saleItem).append("\n");
							// System.out.println(saleItem);
						} else {
							for (int t = 0; t < h.size(); t++) {
								saleItem += "\"" + h.get(t).trimmedText()
										+ "\",";
							}
							headLine.append(itemInfo).append(saleItem).append("\n");
						}
					}
				}
				
				storage.append(itemInfo).append(
						"\"" + st.get(4).trimmedText() + "\",\n");
			}

			FileUtil.createFile("D:/output/上海浦东新区医药药材有限公司/批发_销售.csv", "GBK",
					headLine.toString());
			FileUtil.createFile("D:/output/上海浦东新区医药药材有限公司/批发_库存.csv", "GBK",
					storage.toString());

		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date start = null;

		String startTime = "2013-06-29 09:30:00";
		try {
			start = DateUtil.getDateWithPatterValue("yyyy-MM-dd HH:mm:ss",
					startTime);
			Date d = new Date();
			while (!(d.after(start))) {
				d = new Date();
				Thread.sleep(600000);
			}
		} catch (ParseException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		NT03_0035_Mart_Scraber s = new NT03_0035_Mart_Scraber();
		s.request("http://www.yht.com.cn/BC/page/login.aspx");
	}

}
