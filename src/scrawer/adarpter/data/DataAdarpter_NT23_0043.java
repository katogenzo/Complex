package scrawer.adarpter.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import util.FileUtil;
import util.HTMLUtil;

public class DataAdarpter_NT23_0043 extends DataAdarpter {

	private static String siteName = "江苏华晓医药物流有限公司";
	private static DataAdarpter_NT23_0043 instance = new DataAdarpter_NT23_0043(siteName);
	static {
		instance.init();
	}
	public DataAdarpter_NT23_0043(String siteName) {
		super(siteName);
	}

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

	public static void main(String[] argv) {
		String content = FileUtil
				.readFile("D:/output/江苏华晓医药物流有限公司_网上流向查询_output.html");
		new DataAdarpter_NT23_0043("").analyzer("网上流向查询", content);
		 content = FileUtil
		 .readFile("D:/output/江苏华晓医药物流有限公司_网上库存查询_output.html");
		 new DataAdarpter_NT23_0043("").analyzer("网上库存查询", content);
	}

}
