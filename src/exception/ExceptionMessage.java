package exception;

public class ExceptionMessage {
	public static final String IllegalArgument = "给定的参数值 {p} 不能使用。";

	public static String getExceptionMessage(String code, String message) {
		if (null != message) {
			if (null != code && code.trim().length() < 1) {
				code = "空值";
			}
			message = message.replace("{p}", code);
		}
		return message;
	}
}
