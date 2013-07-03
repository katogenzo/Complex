package harmo;

import java.io.Serializable;

public class Proxy implements Serializable {
	private static final long serialVersionUID = 1L;
	private String hostName;
	private String protocal;
	private int port;
	private String username;
	private String password;

	/**
	 * @param doamin
	 *            域名或IP地址
	 * @param protocal
	 *            访问协议 http/Https/Socket
	 * @param port
	 *            访问端口
	 * @param username
	 *            登录的用户名
	 * @param password
	 *            登录的密码
	 */
	public Proxy(String protocal, String doamin, int port, String username,
			String password) {
		super();
		this.hostName = doamin;
		this.protocal = protocal;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getHostName() {
		return hostName;
	}

	public String getProtocal() {
		return protocal;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
