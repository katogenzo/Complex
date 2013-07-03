package test;

import java.io.File;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;

import util.HTMLUtil;

public class ClientUser {

	public static void main(String[] argv) {

		Client client = new Client();
		HttpHost proxy = new HttpHost("192.168.1.158", 8080, "http");
		HttpClient httpClient = client.userProxy(proxy);

		String res = client.Get("http://www.drugoogle.com/index/index.htm");
		String captchaUrl = "http://www.drugoogle.com/verifyCode/verifyCode.jsp";
		File file = client.gatherCaptcha(captchaUrl);

		String captcha = client.getCaptcha();
		client.login("http://www.drugoogle.com/index/registerloginjson.jspx?"
				+ System.currentTimeMillis(), captcha);
		res = client.Get("http://www.drugoogle.com/index/index.htm");
		client.Get("http://www.drugoogle.com/index/sms.jsp?randCode=000000000&url=http://www.drugoogle.com/index/index.htm&sUrl=http://www.drugoogle.com/index/index.htm");
		String rest = client
				.Get("http://www.drugoogle.com/member/suppliers/medicineGoto/medicinegototab1.jspx?entryId=3&medicineId=0&storegeType=1&updateTime=20130603053820&month=&year=");
		System.out.println(HTMLUtil.delHTMLTag(rest));
	}
}
