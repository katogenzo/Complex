package scrawer;

import java.io.Serializable;

public class Param  implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_DATE_PATTERN="yyyy-MM-dd";
	/**
	 * 参数类型
	 */
	private ParamType type;
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 参数值
	 */
	private String value;
	/**
	 * 参数来源
	 */
	private String source;

	/**
	 * 
	 */
	private String pattern;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * 获取参数类型
	 * @return	参数类型枚举值
	 */
	public ParamType getType() {
		return type;
	}

	/**
	 * 设置参数类型
	 * @param type	参数类型枚举值
	 */
	public Param setType(ParamType type) {
		this.type = type;
		return this;
	}

	/**
	 * 获取参数名称
	 * @return	参数名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置参数名称
	 * @param name	参数名称
	 */
	public Param setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 获取参数值
	 * @return	参数值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置参数值
	 * @param value	参数值
	 */
	public Param setValue(String value) {
		this.value = value;
		return this;
	}

	/**
	 * 获取参数来源
	 * @return 参数来源
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 设置参数来源
	 * @param source 参数来源
	 */
	public Param setSource(String source) {
		this.source = source;
		return this;
	}

}