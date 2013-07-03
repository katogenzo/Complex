package util;

import java.util.HashMap;

public class Constants {
	private static HashMap<String, String> p = FileUtil
			.getPropertiesConfig("directory.properties");
	public static String outPoutDirectory = p.get("outPutDirectory");
	public static String captchaDirectory = p.get("captchaDirectory");
	public static String transferURL = p.get("transferURL");

}
