package scrawer;

import java.io.Serializable;

public class Redirect implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 需要跳转的URL路径
	 */
	private String url;
	/**
	 * 访问跳转过程中是否需要获取返回信息
	 */
	private boolean isCallback;

	/**
	 * 获取跳转的URL
	 * 
	 * @return 跳转的URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置跳转的URL
	 * 
	 * @param url
	 *            跳转的URL
	 */
	public Redirect setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 是否需要返回信息
	 * 
	 * @return 是否需要返回信息
	 */
	public boolean isCallback() {
		return isCallback;
	}

	/**
	 * 设置是否需要返回信息
	 * 
	 * @param isCallback
	 *            是否需要返回信息
	 */
	public Redirect setCallback(boolean isCallback) {
		this.isCallback = isCallback;
		return this;
	}

}
