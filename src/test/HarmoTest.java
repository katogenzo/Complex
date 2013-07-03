package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import harmo.Get;
import harmo.HttpMethodProcessor;
import harmo.Post;
import harmo.Proxy;

public class HarmoTest {
	public static void main(String[] argv) {
		Proxy proxy=new Proxy("http", "192.168.1.158", 8080, null, null);
		HttpMethodProcessor processor=new HttpMethodProcessor(proxy);
		Get get=processor.get("http://www.drugoogle.com/index/index.htm");
		get.touch();
		get.setUrl("http://www.drugoogle.com/verifyCode/verifyCode.jsp");
		try {
			OutputStream ops=new FileOutputStream(new File("captacha.jpg"));
			get.executeAsStream(ops);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("username", "5444");
		map.put("password", "18605888311");
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		String captcha = "";
		try {
			captcha = stdin.readLine();
			stdin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(captcha);
		
		map.put("verifyCode", captcha);

		Post post=processor.post("http://www.drugoogle.com/index/registerloginjson.jspx?"
				+ System.currentTimeMillis());
		post.setParameters(map);
		post.execute();
		get.setUrl("http://www.drugoogle.com/index/index.htm").touch();
		get.setUrl("http://www.drugoogle.com/index/sms.jsp?randCode=000000000&url=http://www.drugoogle.com/index/index.htm&sUrl=http://www.drugoogle.com/index/index.htm").touch();
		post.setUrl("http://www.drugoogle.com/member/suppliers/medicineGoto/medicinegototab1.jspx?entryId=3&medicineId=0&storegeType=1&updateTime=20130603053820&month=&year=");
		System.out.println(post.execute());
		
	}
}
