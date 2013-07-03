package harmo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

public abstract class HttpAbstractMethod implements HttpMethodInterface {

	private String url;

	private int statusCode;
	private String statusMessage;
	private HashMap<String, String> header;
	private HttpMethodProcessor client;
	protected Method method;
	private HashMap<String, String> parameters = new HashMap<String, String>();

	protected HttpMethodProcessor getClient() {
		return client;
	}

	protected void setClient(HttpMethodProcessor client) {
		this.client = client;
	}

	public HttpAbstractMethod(String url) {
		this.url = url;
	}

	public Map<String, String> redirect(HashMap<String, Boolean> Urlist) {
		Map<String, String> postList = new HashMap<String, String>();

		for (String url : Urlist.keySet()) {
			if (Urlist.get(url)) {
				postList.put(url, this.execute());
			} else
				this.touch();
		}
		return postList;
	}

	public String execute() {
		try {
			return this.getClient().execute(this);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void touch() {
		try {
			this.getClient().touch(this);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public OutputStream executeAsStream(OutputStream ops) {
		try {
			ops = this.getClient().executeAsStream(this, ops);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				ops.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ops;
	}

	public String getUrl() {
		return this.url;
	}

	public HttpAbstractMethod setUrl(String url) {
		this.url = url;
		return this;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	protected HttpAbstractMethod setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	protected HttpAbstractMethod setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		return this;
	}

	public HashMap<String, String> getHeader() {
		return this.header;
	}

	protected HttpAbstractMethod setHeader(HashMap<String, String> header) {
		this.header = header;
		return this;
	}

	public Method getMethod() {
		return this.method;
	}

	public HttpAbstractMethod setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
		return this;
	}

	public HttpAbstractMethod addParameter(HashMap<String, String> parameter) {
		this.parameters.putAll(parameter);
		return this;
	}

	public HttpAbstractMethod addParameter(String key, String value) {
		this.parameters.put(key, value);
		return this;
	}

	public HashMap<String, String> getParameters() {
		return this.parameters;
	}
}
