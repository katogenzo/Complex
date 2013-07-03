package scrawer;

import harmo.Method;

import java.util.List;

public class SiteDataSource {

	/**
	 * 数据源url模板
	 */
	private String urlTemplate;
	private Method method;
	private String name;

	public String getName() {
		return name;
	}

	public SiteDataSource setName(String name) {
		this.name = name;
		return this;
	}

	public String getUrlTemplate() {
		return urlTemplate;
	}

	public SiteDataSource setUrlTemplate(String urlTemplate) {
		this.urlTemplate = urlTemplate;
		return this;
	}

	public Method getMethod() {
		return method;
	}

	public SiteDataSource setMethod(Method method) {
		this.method = method;
		return this;
	}

	/**
	 * 数据源的参数列表
	 */
	private List<Param> params;

	public SiteDataSource(String urlTemplate) {
		this.urlTemplate = urlTemplate;
	}

	public SiteDataSource(String urlTemplate, List<Param> params) {
		this.urlTemplate = urlTemplate;
		this.params = params;
	}

	public List<Param> getParams() {
		return params;
	}

	public SiteDataSource setParams(List<Param> params) {
		this.params = params;
		return this;
	}

	public String generateUrl() {
		if (null != params && params.size() > 0) {
			for (Param p : params) {
				urlTemplate.replace(p.getName(),
						p.getValue() == null ? "" : p.getValue());
			}
		}
		return urlTemplate;
	}

}
