package scrawer.adarpter.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import util.FileUtil;
import util.HTMLUtil;

public class DataAdarpter_3380 extends DataAdarpter {
	private static String siteName = "宁波医药股份有限公司";
	private static DataAdarpter_3380 instance = new DataAdarpter_3380(siteName);
	static {
		instance.init();
	}

	public DataAdarpter_3380(String siteName) {
		super(siteName);
	}

	@Override
	public String analyzer(String type, String content) {
		if (null == content || content.trim().length() < 1)
			return "";
		Document doc = Jsoup.parse(content);
		Element e = doc.select("tbody").first();
		content = HTMLUtil.TableToCSV(e.toString());
//		System.out.println(type);
//		System.out.println(content);
		this.GenerateFile(type, content);

		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String content = FileUtil
				.readFile("D:/output/宁波医药股份有限公司_进货 _output.html");
		new DataAdarpter_3380("").analyzer("进货", content);
		 content = FileUtil
		 .readFile("D:/output/宁波医药股份有限公司_库存_output.html");
		 new DataAdarpter_3380("").analyzer("库存", content);
		 content = FileUtil
				 .readFile("D:/output/宁波医药股份有限公司_销售_output.html");
		 new DataAdarpter_3380("").analyzer("销售", content);
	}

}
