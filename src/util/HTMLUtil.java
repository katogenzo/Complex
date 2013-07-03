package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtil {

	private static Pattern regEx_cmt = Pattern.compile("<!--[^>]*?>");
	private static Pattern regEx_th = Pattern.compile("<th[^>]*?>");
	private static Pattern regEx_tr = Pattern.compile("<tr[^>]*?>");
	private static Pattern regEx_td = Pattern.compile("<td[^>]*?>");
	private static Pattern regEx_the = Pattern.compile("</th>");
	private static Pattern regEx_tre = Pattern.compile("</tr>");
	private static Pattern regEx_tde = Pattern.compile("</td>");
	private static Pattern regEx_spc = Pattern.compile("\\s*|\t|\r|\n");
	private static Pattern regEx_lg = Pattern.compile("\\<.*?>");

	/**
	 * 去掉字符串里面的html代码。<br>
	 * 要求数据要规范，比如大于小于号要配套,否则会被集体误杀。
	 * 
	 * @param content
	 *            内容
	 * @return 去掉后的内容
	 */
	public static String stripHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r\n");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		content = content.replaceAll("\\<.*?>", "");
		// 还原HTML
		// content = HTMLDecoder.decode(content);
		return content;
	}

	public static String replaceDLEC(String url) {
		if (url.trim().length() < 4)
			return url;
		url = url.replaceAll("&amp;", "&").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">").replaceAll("&apos;", "\'")
				.replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ")
				.replaceAll("&copy;", "©").replaceAll("&reg;", "?")
				.replaceAll("&times;", "×").replaceAll("&nbsp;", " ")
				.replaceAll("&sect;", "§");
		return url;
	}

	public static String TableToCSV(String htmlStr) {
		if (null == htmlStr || htmlStr.trim().length() < 1)
			return "";

		Matcher m_spc = regEx_spc.matcher(htmlStr);
		htmlStr = m_spc.replaceAll("");

		Matcher m_cmt = regEx_cmt.matcher(htmlStr);
		htmlStr = m_cmt.replaceAll("");

		Matcher m_th = regEx_th.matcher(htmlStr);
		htmlStr = m_th.replaceAll("\"");
		Matcher m_the = regEx_the.matcher(htmlStr);
		htmlStr = m_the.replaceAll("\",");

		Matcher m_tr = regEx_tr.matcher(htmlStr);
		htmlStr = m_tr.replaceAll("");

		Matcher m_tre = regEx_tre.matcher(htmlStr);
		htmlStr = m_tre.replaceAll("\n");

		Matcher m_td = regEx_td.matcher(htmlStr);
		htmlStr = m_td.replaceAll("\"");

		Matcher m_tde = regEx_tde.matcher(htmlStr);
		htmlStr = m_tde.replaceAll("\",");

		Matcher m_lgt = regEx_lg.matcher(htmlStr);
		htmlStr = m_lgt.replaceAll("");

		htmlStr = replaceDLEC(htmlStr);
		return htmlStr;
	}

	public static String delHTMLTag(String htmlStr) {

		if (null == htmlStr)
			return "null";

		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_blank = "\n";

		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_blank = Pattern.compile(regEx_blank);
		Matcher m_blank = p_blank.matcher(htmlStr);
		htmlStr = m_blank.replaceAll("");
		return htmlStr.trim(); // 返回文本字符串
	}
}
