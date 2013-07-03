package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static SimpleDateFormat YYYYMMDD = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 根据给定的日期模板和Date对象，返回一个日期的字符值。
	 * 
	 * @param pattern
	 *            日期模板
	 * @param d
	 *            待格式化的日期对象
	 * @return 格式化后的日期字符串
	 */
	public static String generatePatternDate(String pattern, Date d) {
		return new SimpleDateFormat(pattern).format(d);
	}

	/**
	 * 根据给定的日期模板和日期字符值，返回一个该日期值的Date对象。
	 * 
	 * @param pattern
	 *            日期模板
	 * @param DateString
	 *            日期值的字符串形式
	 * @return 该日期的Date对象
	 * @throws ParseException
	 *             解析异常
	 */
	public static Date getDateWithPatterValue(String pattern, String DateString)
			throws ParseException {
		
		return new SimpleDateFormat(pattern).parse(DateString);
	}

	/**
	 * 按照给定的日期对象，返回默认的YYYY-MM-DD格式日期串
	 * 
	 * @param d
	 * @return
	 */
	public static String getDataString(Date d) {
		return YYYYMMDD.format(d);
	}

	public static Date getDate(String FullYMD) throws ParseException {

		return YYYYMMDD.parse(FullYMD);
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date d = new Date();
		System.out.println(getDataString(getDateBefore(d, 65)));

	}

}
