package scrawer.adarpter.data;

import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import util.FileUtil;
import util.HTMLUtil;
import util.StringUtil;

public class DataAdarpter_3405 extends DataAdarpter {
	private static String siteName = "福建同春药业股份有限公司";
	private static DataAdarpter_3405 instance = new DataAdarpter_3405(siteName);
	private static String siteurl = "http://tc.tcyy.com.cn:8888/tcyy/";
	static {
		instance.init();
	}

	public DataAdarpter_3405(String siteName) {
		super(siteName);
	}

	@Override
	public String analyzer(String type, String content) {

		if (null == content || content.trim().length() < 1)
			return "";

		if (type.equals("商品流向列表")) {
			content = analyzerFlow(content);
		} else if (type.equals("库存明细")) {
			content = analyzerStorage(content);
			this.GenerateFile(type, content);
		}

		return null;
	}

	private String analyzerStoragePerPage(String content) {
		Document doc = Jsoup.parse(content);
		Element e = doc.select("form table table").last();
		content = HTMLUtil.TableToCSV(e.toString());
		return content;
	}

	private String analyzerStorage(String content) {

		StringBuffer contentBuffer = new StringBuffer(
				analyzerStoragePerPage(content));

		Document doc = Jsoup.parse(content);

		String nexPageLink = doc.select("form table").last().select("a")
				.first().attr("href");
		String finalPageLink = doc.select("form table").last().select("a")
				.last().attr("href");
		String pageUrl = finalPageLink.substring(0,
				finalPageLink.indexOf("=") + 1);
		int curPage = Integer.parseInt(nexPageLink.substring(
				nexPageLink.indexOf("=") + 1, nexPageLink.length()));
		int maxPage = Integer.parseInt(finalPageLink.substring(
				finalPageLink.indexOf("=") + 1, finalPageLink.length()));
		for (; curPage <= maxPage;) {
			String toUrl = siteurl + pageUrl + curPage;
			String toPageContent = this.getCt().get(toUrl);
			if (null != toPageContent) {
				toPageContent = analyzerStoragePerPage(toPageContent);
				toPageContent = toPageContent
						.substring(toPageContent.indexOf('\n') + 1,
								toPageContent.length());
			}
			contentBuffer.append(toPageContent);
			curPage++;
		}
		return contentBuffer.toString();
	}

	private String analyzerFlow(String content) {
		Document doc = Jsoup.parse(content);
		Element e = doc.select("tbody table").last();
		content = HTMLUtil.TableToCSV(e.toString());
		content = content.substring(
				0,
				StringUtil.charAtPosOnCount(content, "\n",
						StringUtil.countChars(content, "\n") - 1));

		StringTokenizer stk = new StringTokenizer(content, "\n");

		String row = stk.nextToken();

		StringBuffer outSell = new StringBuffer();
		StringBuffer inCome = new StringBuffer();

		outSell.append(assignOutSell(row.split(",")));
		inCome.append(assigninCome(row.split(",")));

		// delim into rows
		while (stk.hasMoreElements()) {
			row = stk.nextToken();
			String[] cells = row.split(",");
			if (cells[6]
					.substring(cells[6].indexOf('"') + 1,
							cells[6].lastIndexOf('"')).trim().equals("")) {
				outSell.append(assignOutSell(cells));
			} else {
				inCome.append(assigninCome(cells));
			}
		}

		this.GenerateFile("销售明细", outSell.toString());
		this.GenerateFile("进货明细", inCome.toString());
		// System.out.println(outSell);
		// System.out.println(inCome);
		return content;
	}

	private StringBuffer assignOutSell(String[] s) {
		return new StringBuffer().append(s[0]).append(',').append(s[1])
				.append(',').append(s[2]).append(',').append(s[3]).append(',')
				.append(s[4]).append(',').append(s[5]).append(',').append(s[7])
				.append(',').append(s[8]).append(',').append(s[9]).append(',')
				.append(s[10]).append(',').append(s[11]).append(",\n");
	}

	private StringBuffer assigninCome(String[] s) {
		return new StringBuffer().append(s[0]).append(',').append(s[1])
				.append(',').append(s[2]).append(',').append(s[3]).append(',')
				.append(s[4]).append(',').append(s[5]).append(',').append(s[6])
				.append(',').append(s[8]).append(',').append(s[9]).append(',')
				.append(s[10]).append(',').append(s[11]).append(",\n");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String content = FileUtil
				.readFile("D:/output/福建同春药业股份有限公司_商品流向列表_output.html");
		new DataAdarpter_3405(siteName).analyzer("商品流向列表", content);
		// content =
		// FileUtil.readFile("D:/output/福建同春药业股份有限公司_库存明细_output.html");
		// new DataAdarpter_3405("").analyzer("库存明细", content);

	}

}
