package test;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import util.HTMLUtil;

public class JSOUP {
	static {
		System.setProperty("java.net.useSystemProxies", "true");
	}

	protected static int timeout = 1000 * 60;
	protected final static String user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36";
	protected static String LOGIN_FLAG = "JSESSIONID";

	public static void main(String[] argv) {
		try {
			// Response response = Jsoup.connect(
			// "http://www.drugoogle.com/index/index.htm").data("username",
			// "5444","password","18605888311").method(Method.POST).execute();
			// Document doc=response.parse();
			Document doc = Jsoup
					.connect(
							"http://www.drugoogle.com/member/suppliers/medicineGoto/medicinegototab1.jspx?entryId=3&medicineId=0&storegeType=1&updateTime=20130603053820&month=&year=")
					.userAgent(user_agent)
					.cookie("JSESSIONID", "6A1D296CFC151FD3837ED9FDFB9598D9")
					.get();
			Elements element1 = doc.getElementsByTag("form");// 找出所有form表单
			Elements element = element1.select("[method=post]");// 筛选出提交方法为post的表单
			// Elements elements = element.select("input[name]");//
			// 把表单中带有name属性的input标签取出
			HashMap<String, String> parmas = new HashMap<String, String>();
			// for (Element temp : element) {
			// System.out.println(temp.attributes().toString());
			// // parmas.put(temp.attr("name"), temp.attr("value"));//
			// 把所有取出的input，取出其name，放入Map中
			// }
			for (int index = 0; index < element1.size(); index++) {
				System.out.println(element1.get(index));
			}

			System.out.println(doc.toString());
			System.out.println(HTMLUtil.delHTMLTag(doc.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
