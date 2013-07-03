package util;

import java.util.Date;

public class URLUtil {

	public static TargetUrl parseUrl(String url) {
		return new TargetUrl(url);
	}

	public static void main(String[] argv) {
		System.out.println(System.currentTimeMillis());
	}

}

