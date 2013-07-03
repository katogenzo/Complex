package util;

public class TargetUrl {
	private String protocal;
	private String domain;
	private int port;
	private String path;

	public TargetUrl(String protocal, String domain, int port, String path) {
		super();
		this.protocal = protocal;
		this.domain = domain;
		this.port = port;
		this.path = path;
	}

	public TargetUrl(String url) {
		if (null != url & url.trim().length() > 0) {
			String protocal = "";
			int st;
			int port;
			if (url.toLowerCase().startsWith("https")) {
				st = url.indexOf("//") + 2;
				protocal = "https";

				port = 443;
			} else {
				protocal = "http";
				st = 0;
				if (url.toLowerCase().startsWith("http")) {
					st = url.indexOf("//") + 2;
				}
				port = 80;
			}
			int end = url.indexOf('/', st);
			int portSt = url.indexOf(':', st);
			if (portSt > 0) {

				try {
					port = Integer.parseInt(url.substring(portSt + 1, end));
				} catch (NumberFormatException e) {
				}

			} else if (portSt == -1) {
				portSt = end;
			}
			String domain = url.substring(st, portSt);
			String path = url.substring(end, url.length());
			this.protocal = protocal;
			this.domain = domain;
			this.port = port;
			this.path = path;
		}
	}

	protected void setPort(int port) {
		this.port = port;
	}

	public String getProtocal() {
		return protocal;
	}

	public String getDomain() {
		return domain;
	}

	public String getPath() {
		return path;
	}

	public int getPort() {
		return port;
	}

	public String toString() {
		if (this.port == 80 || this.port == 443)
			return this.protocal + "://" + this.domain + this.path;
		else
			return this.protocal + "://" + this.domain + ":" + this.port
					+ this.path;
	}
}