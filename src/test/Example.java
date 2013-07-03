package test;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import util.StreamUtil;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class Example {

	private WebClient client;
	private WebRequest request;

//	static {
//		System.setProperty("java.net.useSystemProxies", "true");
//	}
	public Example() {
		this.client = new WebClient(BrowserVersion.CHROME);
		this.client.setThrowExceptionOnScriptError(false);
	}

	public void request(String url) {
		URL page = null;
		try {
			page = new URL(url);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println(page.getPath());

//		if (null == page)
//			return;

//		request = new WebRequest(page, HttpMethod.GET);
//		request.setCharset("UTF8");
		
		
		
		 try {
			HtmlPage p=client.getPage(url);
			
//			System.out.println(p.getWebResponse().getContentAsString());
			
			HtmlImage img=(HtmlImage) p.getElementById("verifyCodeId");
			ImageReader reader=img.getImageReader();
			BufferedImage image=reader.read(0);
			ImageIO.write(image, "jpg", new File("v.jpg"));
			
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));
			String captcha = "";
			try {
				captcha = stdin.readLine();
				stdin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			HtmlInput username=p.getElementByName("username");
			HtmlInput password=p.getElementByName("password");
			HtmlInput verifyCode=p.getElementByName("verifyCode");
			HtmlSubmitInput login=p.getElementByName("Submit");
			
			username.setValueAttribute("5444");
			password.setValueAttribute("18605888311");
			verifyCode.setValueAttribute(captcha);
			login.click();
			p=client.getPage(url);
			System.out.println(p.getWebResponse().getContentAsString());
			
			
			
//			File f=new File("verify.jpg");
//			FileOutputStream ops=new FileOutputStream(f);
//			StreamUtil.copyStream(p.getWebResponse().getContentAsStream(), ops);
			
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] argv){
		Example e=new Example();
		e.request("http://www.drugoogle.com/index/index.htm");
	}
	
}

