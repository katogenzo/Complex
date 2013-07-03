package executor;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import util.CSVUtil;
import util.Constants;
import util.DateUtil;
import util.FileUtil;
import util.MD5;
import util.Transfer;
import be.roam.hue.doj.Doj;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class NT_03_0035_Sale_Scraber {

	private WebClient client;
	private HtmlPage page;
	private Page p;

	public NT_03_0035_Sale_Scraber() {
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

	private boolean login(String url) {
		String pKey = "";
		try {
			this.page = this.client.getPage(url);
			pKey = MD5.toMD5String(page.getWebResponse().getContentAsString());

		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		// System.out.println(page.getWebResponse().getContentAsString());
		Doj.on(page).get("#txtName").value("002-02-001");
		Doj.on(page).get("#txtPwd").value("12344321");
		Doj login = Doj.on(page.getDocumentElement()).getByAttribute("name",
				"btLogin");
		try {
			p = login.click();
			return !(pKey.equals(MD5.toMD5String(p.getWebResponse()
					.getContentAsString())));
		} catch (ClassCastException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void paser() {
		try {
			page = this.client
					.getPage("http://www.yht.com.cn/BCC/Page/SaleRecords.aspx");
		} catch (FailingHttpStatusCodeException | IOException e1) {
			e1.printStackTrace();
		}

		Doj pDoj = Doj.on(page);
		StringBuffer headLine = new StringBuffer();
		boolean assignHead = false;
		String colName = "";

		HtmlSelect selects = (HtmlSelect) pDoj.get("#ddlGoods").getElement(0);

		String stDate = "2013-04-24";
		String enDate = "2013-06-28";

		try {
			Date endate = DateUtil.getDateWithPatterValue("yyyy-MM-dd", enDate);
			// 循环下拉菜单进行查询
			for (int index = 0; index < selects.getOptionSize(); index++) {

				HtmlOption o = selects.getOption(index);
				if (o.getValueAttribute().equals(""))
					continue;
				selects.setSelectedAttribute(o, true);

				Date stdate = DateUtil.getDateWithPatterValue("yyyy-MM-dd",
						stDate);

				while (!(stdate.after(endate))) {
					String exDateStr = DateUtil.generatePatternDate(
							"yyyy-MM-dd", stdate);

					System.out.println(index + "\t" + exDateStr);

					pDoj.get("#txtBeginDate").value(exDateStr);
					pDoj.get("#txtEndDate").value(exDateStr);
					stdate = DateUtil.getDateAfter(stdate, 1);

					HtmlPage po = null;
					po = (HtmlPage) pDoj.getByAttribute("name", "btFind")
							.click();

					// 基本的商品信息
					Doj storeDoj = Doj.on(po);
					Doj rows = storeDoj.get("#tabGoodsInfo tr");

					StringBuffer itemInfo = new StringBuffer();
					for (Doj tr : rows) {
						Doj tabs = tr.get("td");

						for (int t = 0; t < tabs.size(); t++) {
							Doj tab = tabs.get(t);
							Doj input = tab.get("input");
							if (input.isEmpty()) {
								if (!assignHead) {
									colName += "\"" + tab.trimmedText() + "\",";
								}
							} else {
								itemInfo.append("\"" + input.value().trim()
										+ "\",");
							}
						}
					}
					if (!assignHead) {
						colName += "\"销售日期\",";
					}
					itemInfo.append("\"" + exDateStr + "\",");
					// System.out.println(itemInfo);

					// 商品销售记录
					// 销售明细行
					Doj dgGoods = storeDoj.get("#dgGoodsDepartment tr");
					int i = 1;
					int height = dgGoods.size() - 1;

					// 填充表头
					if (!assignHead) {
						headLine.append(colName);

						Doj h = dgGoods.get(0).get("td");
						for (int t = 0; t < h.size() - 3; t++) {
							Doj j = h.get(t);
							headLine.append("\"" + j.trimmedText() + "\",");
						}
						headLine.append("\"销售数量\",");
						headLine.append("\n");
						assignHead = true;
					}

					// 销售数据
					String saleItem = "";
					// 如果销售数据表格没有内容
					if (i == height) {
						saleItem = ","
								+ CSVUtil.appendCommaRow(dgGoods.get(0)
										.get("td").size());
						headLine.append(itemInfo).append(saleItem).append("\n");
					} else {
						for (; i < height; i++) {
							saleItem = "";
							Doj h = dgGoods.get(i).get("td");
							if (h.isEmpty()) {
								saleItem = CSVUtil.appendCommaRow(h.size());
							} else {
								if (h.get(h.size() - 3).trimmedText()
										.equals("0.00"))
									continue;
								for (int t = 0; t < h.size() - 2; t++) {
									saleItem += "\"" + h.get(t).trimmedText()
											+ "\",";
								}
								headLine.append(itemInfo).append(saleItem)
										.append("\n");
							}
						}
					}

				}
			}
		} catch (ParseException | ClassCastException | IOException e1) {
			e1.printStackTrace();
		}
		try {
			FileUtil.createFile("D:/output/上海浦东新区医药药材有限公司/连锁_销售.csv", "GBK",
					headLine.toString());
			
			System.out.println("开始压缩上传文件!");
			File f = new File("D:/output/上海浦东新区医药药材有限公司"+ "/");
			try {
				Transfer.process(f, Constants.transferURL, "NT03-0035");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("上传文件过程中出现异常！");
			}
			System.out.println("上传文件成功！");
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date start = null;

		String startTime = "2013-06-29 10:30:00";
		try {
			start = DateUtil.getDateWithPatterValue("yyyy-MM-dd HH:mm:ss",
					startTime);
			Date d = new Date();
			while (!(d.after(start))) {
				d=new Date();
				Thread.sleep(600000);
			}
			String url = "http://www.yht.com.cn/BCC/page/Login.aspx";
			NT_03_0035_Sale_Scraber s = new NT_03_0035_Sale_Scraber();
			System.out.println(s.login(url));
			s.paser();
		} catch (ParseException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
