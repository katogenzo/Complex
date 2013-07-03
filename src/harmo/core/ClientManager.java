package harmo.core;

import harmo.Proxy;

import java.io.Serializable;
import java.net.ProxySelector;
import java.util.Hashtable;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class ClientManager implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Hashtable<String, AbstractHttpClient> context = new Hashtable<String, AbstractHttpClient>();
	protected final static String user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36";
//	static {
//		System.setProperty("java.net.UseSystemProxies", "true");
//	}

	public static String createClient() {
		String value = UUID.randomUUID().toString().replaceAll("-", "");
		DefaultHttpClient client = new DefaultHttpClient();
		ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
				client.getConnectionManager().getSchemeRegistry(),
				ProxySelector.getDefault());
		client.setRoutePlanner(routePlanner);

		context.put(value, client);
		return value;
	}

	public static String createProxyClient(Proxy proxy) {
		String value = UUID.randomUUID().toString().replaceAll("-", "");
		HttpParams param = new BasicHttpParams();
		HttpProtocolParams.setUserAgent(param, user_agent);
		DefaultHttpClient client = new DefaultHttpClient(param);
		client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, true);
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);

		if (proxy.getUsername() != null && proxy.getUsername().length() > 0) {
			String password = proxy.getPassword() == null ? "" : proxy
					.getPassword();
			client.getCredentialsProvider().setCredentials(
					new AuthScope(proxy.getHostName(), proxy.getPort()),
					new UsernamePasswordCredentials(proxy.getUsername(),
							password));
		}
		HttpHost host = new HttpHost(proxy.getHostName(), proxy.getPort(),
				proxy.getProtocal());

		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		context.put(value, client);
		return value;
	}

	public static AbstractHttpClient getClient(String sValue) {
		if (context.containsKey(sValue)) {
			return context.get(sValue);
		} else {
			return null;
		}
	}

	public static void close(String sValue) {
		if (context.containsKey(sValue)) {
			context.get(sValue).getConnectionManager().shutdown();
		}
	}
}
