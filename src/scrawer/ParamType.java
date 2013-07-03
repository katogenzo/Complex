package scrawer;

/**
 * 参数类型枚举类
 * 
 * @author wangbibo
 * 
 */
public enum ParamType {

	/**
	 * 基本字符型参数
	 */
	text("text"),
	/**
	 * 验证码参数类型
	 */
	captcha("captcha"),
	/**
	 * 日期参数类型
	 */
	date("date"), 
	/**
	 * 页面取值
	 */
	inhtmlById("inhtmlById");
	private String _name;

	private ParamType(String type) {
		this._name = type;
	}

	public String getType() {
		return this._name;
	}

}