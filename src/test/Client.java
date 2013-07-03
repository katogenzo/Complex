package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Client {

	HttpClient httpclient = new DefaultHttpClient();

	public HttpClient userProxy(HttpHost proxy) {
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
		return httpclient;
	}

	public File gatherCaptcha(String url) {

		HttpResponse rsp = get(url);
		File file = null;
		if (null != rsp) {

			file = new File("regist.jpg");
			OutputStream ops;
			try {
				ops = new FileOutputStream(file);
				rsp.getEntity().writeTo(ops);
				ops.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public String getCaptcha() {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		String captcha = "";
		try {
			captcha = stdin.readLine();
			stdin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return captcha;
	}

	public void login(String url, String captcha) {

		HttpResponse rsp = loginWithCaptcha(url, captcha);
		System.out.println(rsp);
		if (null != rsp) {
			StatusLine sl = rsp.getStatusLine();
			System.out.println(sl.getStatusCode());
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				String str = getResponseContent(rsp);
			}
		}
	}

	private String getResponseContent(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String content = null;
		if (entity != null) {
			// start 读取整个页面内容
			InputStream is;
			try {
				is = entity.getContent();
				BufferedReader in = new BufferedReader(
						new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();
				String line = "";
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
				// end 读取整个页面内容
				content = buffer.toString();
				EntityUtils.consume(entity);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return content;
	}

	private HttpResponse loginWithCaptcha(String url, String captcha) {
		HttpResponse rsp = null;
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "5444"));
		nvps.add(new BasicNameValuePair("password", "18605888311"));
		nvps.add(new BasicNameValuePair("verifyCode", captcha));
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			rsp = httpclient.execute(post);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rsp;
	}

	private HttpResponse get(String url) {
//		TargetUrl tUrl = URLUtil.parseUrl(url);
//		HttpHost host = new HttpHost(tUrl.getDomain(), tUrl.getPort(),
//				tUrl.getProtocal());
		HttpGet get = new HttpGet(url);
		HttpResponse rsp = null;
		try {
			rsp = httpclient.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rsp;
	}

	public String Get(String url) {
		return getResponseContent(get(url));
	}

	public String Post(String url,String captcha) {
		return getResponseContent(loginWithCaptcha(url,captcha));
	}

}
