package scrawer.adarpter.data;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scrawer.Param;
import util.CSVUtil;
import util.HTMLUtil;

public class DataAdarpter_NT34_0030 extends DataAdarpter {
	private static String siteName = "绍兴县华通医药有限公司";
	private static DataAdarpter_NT34_0030 instance = new DataAdarpter_NT34_0030(
			siteName);
	private static final int[] saleColIndex = { 0, 1, 2, 3, 4, 5, 6, 9, 10, 13,
			14, 15, 16 };
	private static final int[] storeColIndex = { 0, 1, 2, 3, 4, 5, 6, 9, 10,
			11, 12, 13 };
	private static final int[] receiptColIndex = { 0, 1, 2, 3, 4, 5, 6, 9, 10,
			11 };
	private String siteUrl = "http://60.190.198.154:8080/aweb/";
	// private String productUrl = siteUrl + "find_csdb.jsp?PlanSearch=1&PNo=";
	static {
		instance.init();
	}

	public DataAdarpter_NT34_0030(String siteName) {
		super(siteName);
	}

	@Override
	public String analyzer(String type, String content) {
		if (null == content || content.trim().length() < 1)
			return "";
		if (type.equals("ProductList")) {
			analyzProductList(content);
		} else if (type.equals("receiptList")) {
			analyzReceiptList(content);
		}

		return null;
	}

	private String analyzReceiptList(String content) {
		content = parsePerPage(content);
		if (content.equals(""))
			return content;
		Document doc = Jsoup.parse(content);

		List<Element> list = doc.select("tr");
		StringBuffer allReceiptList = new StringBuffer().append(list.get(0));
		boolean appendTitle = false;
		int index = 1;
		for (; index < list.size(); index++) {
			Element e = list.get(index);
			String detail = e.select("a[onclick^=javascript]").first()
					.attr("onclick");
			detail = siteUrl
					+ detail.substring(detail.indexOf('\'') + 1,
							detail.indexOf("','"));
			HashMap<String, String> detailParams = generalQueryData(detail);

			List<Param> params = this.getCt().getDatasources().get(1)
					.getParams();

			for (Param p : params) {
				detailParams.put(p.getName(), p.getValue());
			}

			String detailUrl = detail.substring(0, detail.indexOf('?'));
			String detailList = this.getCt().post(detailUrl, detailParams);
			detailList = parsePerPage(detailList);

			Document receiptDetails = Jsoup.parse(detailList);
			List<Element> receiptList = receiptDetails.select("tr");

			if (!appendTitle) {
				allReceiptList.append(receiptList.get(0)).append("\n");
				appendTitle = true;
			}

			if (receiptList.size() < 2) {
				allReceiptList
						.append(e)
						.append(appendTabRow(receiptList.get(0).select("th")
								.size())).append('\n');
			} else {
				for (int i = 1; i < receiptList.size(); i++) {
					allReceiptList.append(e).append(receiptList.get(i))
							.append("\n");
				}
			}
		}

		allReceiptList = new StringBuffer(HTMLUtil.TableToCSV(allReceiptList
				.toString().replaceAll("</tr><tr>", "")));

		String[][] allRows = CSVUtil.formatAsArray(allReceiptList.toString(),
				",");
		allReceiptList = new StringBuffer();
		index = 0;
		
		for (; index < allRows.length; index++) {
			allReceiptList.append(
					CSVUtil.filterColumn(allRows[index], receiptColIndex, ","))
					.append("\n");

		}
		
		this.GenerateFile("进货", allReceiptList.toString());

		return null;
	}

	private String analyzProductList(String content) {

		content = parsePerPage(content);
		if (content.equals(""))
			return content;

		Document doc = Jsoup.parse(content);

		List<Element> list = doc.select("tr");

		StringBuffer allSalesOrder = new StringBuffer().append(list.get(0));
		StringBuffer allStoreOrder = new StringBuffer().append(list.get(0));
		boolean appendTitle = false;

		int index = 1;
		for (; index < list.size(); index++) {
			Element e = list.get(index);
			String kcurl = e.select("a[onclick^=javascript]").first()
					.attr("onclick");
			String xsurl = e.select("a[onclick^=javascript]").last()
					.attr("onclick");
			// 获取库存和销售的URL
			kcurl = siteUrl
					+ kcurl.substring(kcurl.indexOf('\'') + 1,
							kcurl.indexOf("','"));
			xsurl = siteUrl
					+ xsurl.substring(xsurl.indexOf('\'') + 1,
							xsurl.indexOf("','"));
			// System.out.println(kcurl);
			// System.out.println(xsurl);

			HashMap<String, String> xsparams = generalQueryData(xsurl);

			List<Param> params = this.getCt().getDatasources().get(0)
					.getParams();
			for (Param p : params) {
				xsparams.put(p.getName(), p.getValue());
			}

			String xsDurl = xsurl.substring(0, xsurl.indexOf('?'));

			// 访问销售URL
			String itemOrders = this.getCt().post(xsDurl, xsparams);
			itemOrders = parsePerPage(itemOrders);
			// System.out.println(HTMLUtil.TableToCSV(itemOrders));

			// 访问库存URL
			String storeOrders = this.getCt().get(kcurl);
			storeOrders = parsePerPage(storeOrders);
			List<Element> storeListItem = Jsoup.parse(storeOrders).select("tr");

			Document salelists = Jsoup.parse(itemOrders);
			List<Element> saleListItem = salelists.select("tr");

			if (!appendTitle) {
				allSalesOrder.append(saleListItem.get(0)).append("\n");
				allStoreOrder.append(storeListItem.get(0)).append("\n");
				appendTitle = true;
			}

			if (storeListItem.size() < 2) {
				allStoreOrder
						.append(e)
						.append(appendTabRow(storeListItem.get(0).select("th")
								.size())).append('\n');
			} else {
				for (int i = 1; i < storeListItem.size(); i++) {
					allStoreOrder.append(e).append(storeListItem.get(i))
							.append("\n");
				}
			}

			if (saleListItem.size() < 2) {
				allSalesOrder
						.append(e)
						.append(appendTabRow(saleListItem.get(0).select("th")
								.size())).append('\n');
			} else {
				for (int i = 1; i < saleListItem.size(); i++) {
					allSalesOrder.append(e).append(saleListItem.get(i))
							.append("\n");
				}
			}

		}
		allSalesOrder = new StringBuffer(HTMLUtil.TableToCSV(allSalesOrder
				.toString().replaceAll("</tr><tr>", "")));

		String[][] allRows = CSVUtil.formatAsArray(allSalesOrder.toString(),
				",");
		allSalesOrder = new StringBuffer();
		index = 0;
		for (; index < allRows.length; index++) {
			allSalesOrder.append(
					CSVUtil.filterColumn(allRows[index], saleColIndex, ","))
					.append("\n");

		}

		allStoreOrder = new StringBuffer(HTMLUtil.TableToCSV(allStoreOrder
				.toString().replaceAll("</tr><tr>", "")));

		allRows = CSVUtil.formatAsArray(allStoreOrder.toString(), ",");
		allStoreOrder = new StringBuffer();
		index = 0;
		for (; index < allRows.length; index++) {
			allStoreOrder.append(
					CSVUtil.filterColumn(allRows[index], storeColIndex, ","))
					.append("\n");

		}

		this.GenerateFile("库存", allStoreOrder.toString());
		this.GenerateFile("销售", allSalesOrder.toString());
		
		return null;
	}

	private String appendTabRow(int tabs) {
		String row = "<tr>";
		for (int index = 0; index < tabs; index++) {
			row += "<td></td>";
		}
		row += "</tr>";
		return row;
	}

	private String parsePerPage(String content) {
		if (null == content || content.trim().length() < 1)
			return "";

		Document doc = Jsoup.parse(content);

		int pageCount = 1;

		StringBuffer htmlContent = new StringBuffer(
				"<html><head></head><body><table><tbody>")
				.append(getProList(content));
//		System.out.println("页面中的总页数标识："
//				+ doc.select("input[name=iPageCount]").size());

		// 如果页数大于1
		if (doc.select("input[name=iPageCount]").size() > 0) {
			pageCount = Integer.parseInt(doc.select("input[name=iPageCount]")
					.first().attr("value"));
//			System.out.println("总页数 " + pageCount);

			if (pageCount > 1) {

				String pageUrl = doc.select("form table").last().select("a")
						.first().attr("href");

				int pnIndex = pageUrl.indexOf("PNo=");
				String pageUrlSubfix = pageUrl.substring(pnIndex + 4,
						pageUrl.length());
				pageUrl = this.siteUrl + pageUrl.substring(0, pnIndex + 4);
				System.out.println(pageUrlSubfix);
				if (pageUrlSubfix.indexOf('&') > -1) {
					pageUrlSubfix = pageUrlSubfix.substring(
							pageUrlSubfix.indexOf('&'), pageUrlSubfix.length());
					if (pageUrlSubfix.indexOf("&AccID=106") == -1) {
						pageUrlSubfix += "&AccID=106";
					}
				} else
					pageUrlSubfix = "";
//				System.out.println(pageUrl);

				int page = 2;
				for (; page <= pageCount; page++) {
					String callUrl = (pageUrl + page + pageUrlSubfix).trim();
//					System.out.println("访问子页的路径 \n" + callUrl);
					String proHtml = this.getCt().get(callUrl);
					proHtml = getProList(proHtml);
					// 去除表头
					proHtml = proHtml.substring(proHtml.indexOf("</tr>") + 5,
							proHtml.length());
					// System.out.println("子页的数据:\n"+proHtml);
					htmlContent.append(proHtml);
				}
			}
			htmlContent.append("</tbody></table></body></html>");
		}
		return htmlContent.toString();
	}

	private String getProList(String content) {
		if (null == content || content.trim().length() < 1)
			return "";

		Document doc = Jsoup.parse(content);
		Elements tbody;
		// System.out.println("子页的源码：\n"+content);
		// System.out.println("子页中的table数量 " + doc.select("form table").size());
		tbody = doc.select("table[id^=tblcs] tbody tr");

		// if (doc.select("form table").size() > 2)
		// tbody = doc.select("form table").get(1).select("tbody tr");
		// else
		// tbody = doc.select("form table tbody tr");
		String proHtml = tbody.toString();
		// System.out.println("分页中的数据:\n"+proHtml);

		// 如果表格中有数据，则除去最后一行汇总数据。
		if (tbody.size() > 2) {
			String cellvalue = tbody.get(tbody.size() - 1).select("td")
					.toString();

			if (cellvalue.indexOf("数量合计") > -1) {
				proHtml = proHtml.substring(0, proHtml.lastIndexOf("<tr>"));
			}
		}
		return proHtml;
	}

	private HashMap<String, String> generalQueryData(String url) {
		if (null == url || url.indexOf('?') == -1)
			return null;

		url = url.substring(url.indexOf('?') + 1, url.length());
		if (url.indexOf("=") == -1)
			return null;

		HashMap<String, String> map = new HashMap<String, String>();
		if (url.indexOf('&') > -1) {
			StringTokenizer stk = new StringTokenizer(url, "&");
			while (stk.hasMoreTokens()) {
				String val = stk.nextToken();
				map.putAll(generateKVFromEQ(val));
			}
		} else
			map.putAll(generateKVFromEQ(url));

		return map;
	}

	private HashMap<String, String> generateKVFromEQ(String eqString) {
		String key = eqString.substring(0, eqString.indexOf('='));
		String val = eqString.substring(eqString.indexOf('=') + 1,
				eqString.length());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, val);
		return map;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataAdarpter_NT34_0030 dap = new DataAdarpter_NT34_0030("test");
		System.out.println(dap.appendTabRow(8));
	}
}
