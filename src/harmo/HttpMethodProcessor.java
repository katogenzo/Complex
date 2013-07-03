package harmo;

import harmo.core.ClientManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpMethodProcessor implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sValue;
	private boolean isClosed;
	private Charset charset = Charset.forName("UTF-8");

	public Charset getCharset() {
		return charset;
	}

	public HttpMethodProcessor() {
		this.sValue = ClientManager.createClient();
	}

	public HttpMethodProcessor(Proxy proxy) {
		this.sValue = ClientManager.createProxyClient(proxy);
	}

	public Get get(String url) {
		Get get = new Get(url);
		get.setClient(this);
		return get;
	}

	public Post post(String url) {
		Post post = new Post(url);
		post.setClient(this);
		return post;
	}

	private void isClosed() throws IOException {
		if (isClosed)
			throw new IOException();
	}

	protected void touch(HttpAbstractMethod method)
			throws ClientProtocolException, IOException {
		isClosed();
		EntityUtils.consume(executeForResponse(method).getEntity());
	}

	protected String execute(HttpAbstractMethod method)
			throws ClientProtocolException, IOException {
		isClosed();
		return getResponseContent(executeForResponse(method));
	}

	protected OutputStream executeAsStream(HttpAbstractMethod method,
			OutputStream ops) throws ClientProtocolException, IOException {
		isClosed();
		HttpResponse response = executeForResponse(method);
		response.getEntity().writeTo(ops);
		ops.close();
		return ops;
	}

	private HttpResponse executeForResponse(HttpAbstractMethod method)
			throws ClientProtocolException, IOException {
		HttpResponse response = null;
//		Header[] headers;
		switch (method.getMethod()) {
		case GET:
			HttpGet get = new HttpGet(method.getUrl());
			get.setHeader("Cache-Control", "no-cache");
			get.setHeader("Accept-Encoding", "gzip,deflate");
//			headers = get.getAllHeaders();
//			for (int index = 0; index < headers.length; index++) {
//				System.out.println("Get 请求 Headers: "
//						+ headers[index].getName() + "\t"
//						+ headers[index].getValue());
//			}

			response = ClientManager.getClient(this.sValue).execute(get);
			initializeRequest(method, response);
			return response;
		case POST:
			HttpPost post = new HttpPost(method.getUrl());
			HashMap<String, String> map = ((Post) method).getParameters();
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String key : map.keySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(key,
						map.get(key));
				nvps.add(pair);
			}
			post.setEntity(new UrlEncodedFormEntity(nvps));

//			headers = post.getAllHeaders();
//			for (Header header : headers) {
//				System.out.println("Post 请求 Headers: " + header.getName()
//						+ "\t" + header.getValue());
//			}

			response = ClientManager.getClient(this.sValue).execute(post);
			initializeRequest(method, response);
			return response;
		default:
			return null;
		}

	}

	private void initializeRequest(HttpAbstractMethod method,
			HttpResponse response) {

		HttpEntity entity = response.getEntity();
		Charset cs = ContentType.getOrDefault(entity).getCharset();
		if (null != cs)
			this.charset = cs;

		System.out.println("访问后状态码为："
				+ response.getStatusLine().getStatusCode());
		method.setStatusCode(response.getStatusLine().getStatusCode());
		method.setStatusMessage(response.getStatusLine().getReasonPhrase());
		List<Cookie> cookies = ClientManager.getClient(this.sValue)
				.getCookieStore().getCookies();
		if (!cookies.isEmpty()) {
			for (int i = 0; i < cookies.size(); i++) {
				// System.out.println("Cookies "+cookies.get(i).getName() + "\t"
				// + cookies.get(i).getValue());
				// System.out.println("Cookies对象 "+cookies.get(i));
				ClientManager.getClient(this.sValue).getCookieStore()
						.addCookie(cookies.get(i));
			}
		}
		method.setStatusCode(response.getStatusLine().getStatusCode());
		method.setStatusMessage(response.getStatusLine().getReasonPhrase());
		Header[] headers = response.getAllHeaders();
		HashMap<String, String> header = new HashMap<String, String>();
		for (int index = 0; index < headers.length; index++) {
			header.put(headers[index].getName(), headers[index].getValue());
			// System.out.println(headers[index].getName()+"\t"+headers[index].getValue());
		}
		method.setHeader(header);
	}

	private String getResponseContent(HttpResponse response) {

		int sc = response.getStatusLine().getStatusCode();
		String content = null;
		if (sc == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is;

				String acceptEncoding = "";
				if (response.getHeaders("Content-Encoding") != null) {
					Header[] headers = response.getHeaders("Content-Encoding");
					for (Header header : headers) {
						if (header.getName().equalsIgnoreCase(
								"Content-Encoding")) {
							acceptEncoding = header.getValue();
						}
					}
					GZIPInputStream gzins = null;
					BufferedReader in;
					try {

						is = entity.getContent();

						// System.out.println("Content-Encoding \t"+acceptEncoding);

						if (acceptEncoding.toLowerCase().indexOf("gzip") > -1) {
							gzins = new GZIPInputStream(is);
							in = new BufferedReader(
									new InputStreamReader(gzins));
						} else
							in = new BufferedReader(new InputStreamReader(is,
									this.charset));

						StringBuffer buffer = new StringBuffer();
						String line = "";
						while ((line = in.readLine()) != null) {
							buffer.append(line);
						}
						content = buffer.toString();
						EntityUtils.consume(entity);
						in.close();

						if (gzins != null)
							gzins.close();
						is.close();

					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (sc == HttpStatus.SC_MOVED_PERMANENTLY
				|| sc == HttpStatus.SC_MOVED_TEMPORARILY) {
			if (response.getHeaders("location") != null) {
				Header[] headers = response.getHeaders("location");
				for (Header header : headers) {
					if (header.getName().equalsIgnoreCase("location")) {
						content = header.getValue();
					}
				}

			}
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content;
	}

	public void close() {
		ClientManager.close(this.sValue);
		this.isClosed = true;
	}
}
