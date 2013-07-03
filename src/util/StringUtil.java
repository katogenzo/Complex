package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String validateNullToSpace(String str) {
		str = null == str ? "" : str;
		return str;
	}

	public static int countChars(String source,String chars) {
		int i = 0;
		if (null != source) {
			Pattern p = Pattern.compile(chars);
			Matcher m = p.matcher(source);
			while (m.find()) {
				i++;
			}
		}
		return i;
	}

	public static int charAtPosOnCount(String source, String chars, int count) {
		if (null != source && source.trim().length() > 1 && count > 0) {
			Matcher slashMatcher = Pattern.compile(chars).matcher(source);
			int mIdx = 0;
			while (slashMatcher.find()) {
				mIdx++;

				if (mIdx == count) {
					break;
				}
			}
			return slashMatcher.start();
		} else
			return -1;

	}
}
