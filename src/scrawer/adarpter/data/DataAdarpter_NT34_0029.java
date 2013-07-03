package scrawer.adarpter.data;

import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import scrawer.CircuitTemplate;
import util.FileUtil;
import util.HTMLUtil;

public class DataAdarpter_NT34_0029 extends DataAdarpter {

	private static String siteName = "浙江省上虞市医药有限责任公司";
	private static DataAdarpter_NT34_0029 instance = new DataAdarpter_NT34_0029(
			siteName);
	
	static {
		instance.init();
	}

	public DataAdarpter_NT34_0029(String siteName, CircuitTemplate ct) {
		super(siteName, ct);
	}

	public DataAdarpter_NT34_0029(String siteName) {
		super(siteName);
	}

	@Override
	public String analyzer(String type, String content) {

		if (null == content || content.trim().length() < 1)
			return "";

		Document doc = Jsoup.parse(content);

		Element storage = doc.select("td[valign=top]").get(0);
		storage = storage.select("tbody").first();
		String storageContent = HTMLUtil.TableToCSV(storage.toString());

		// System.out.println(storageContent);

		Element sales = doc.select("td[valign=top]").get(1);
		sales = sales.select("tbody").first();
		String saleContent = HTMLUtil.TableToCSV(sales.toString());

		StringTokenizer stk = new StringTokenizer(saleContent, "\n");

		String row = stk.nextToken();

		StringBuffer outSell = new StringBuffer();
		StringBuffer inCome = new StringBuffer();

		outSell.append(assignOutSell(row.split(",")));
		inCome.append(assigninCome(row.split(",")));

		
		
		while (stk.hasMoreElements()) {
			row = stk.nextToken();
			String[] cells = row.split(",");
			if (cells[7]
					.substring(cells[7].indexOf('"') + 1,
							cells[7].lastIndexOf('"')).trim().equals("0.0000")) {
				outSell.append(assignOutSell(cells));
			} else {
				inCome.append(assigninCome(cells));
			}
		}
//		System.out.println(saleContent);
//		String storagefilePath = Constants.outPoutDirectory + siteName + "_"
//				+ "库存" + "_" + dateString + ".csv";
//		String salefilePath = Constants.outPoutDirectory + siteName + "_"
//				+ "流向" + "_" + dateString + ".csv";
		this.GenerateFile("库存", storageContent);
		this.GenerateFile("销售", outSell.toString());
		this.GenerateFile("进货", inCome.toString());
//		System.out.println(storage);
		return null;
	}

	private StringBuffer assignOutSell(String[] s) {
		return new StringBuffer().append(s[0]).append(',').append(s[1])
				.append(',').append(s[2]).append(',').append(s[3]).append(',')
				.append(s[4]).append(',').append(s[5]).append(',').append(s[6])
				.append(',').append(s[8]).append(',').append(s[9]).append(',')
				.append(",\n");
	}

	private StringBuffer assigninCome(String[] s) {
		return new StringBuffer().append(s[0]).append(',').append(s[1])
				.append(',').append(s[2]).append(',').append(s[3]).append(',')
				.append(s[4]).append(',').append(s[5]).append(',').append(s[6])
				.append(',').append(s[7]).append(',').append(s[9]).append(',')
				.append(",\n");
	}

	public static void main(String[] argv) {
		String content = FileUtil
				.readFile("D:/output/浙江省上虞市医药有限责任公司_导出_output.html");
		new DataAdarpter_NT34_0029("浙江省上虞市医药有限责任公司").analyzer("导出", content);
	}
}
