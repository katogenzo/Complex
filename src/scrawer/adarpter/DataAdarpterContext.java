package scrawer.adarpter;

import java.util.Hashtable;

import scrawer.adarpter.data.DataAdarpter;
import util.ClassUtil;

public class DataAdarpterContext {
	private static Hashtable<String, DataAdarpter> dataAdarpters = new Hashtable<String, DataAdarpter>();

	static {
		ClassUtil.getClasses("scrawer.adarpter.data");
	}

	public static void inputAdarpter(DataAdarpter adp) {
		dataAdarpters.put(adp.getSiteName(), adp);
	}

	public static DataAdarpter getDataAdarpter(String siteName) {
		if (dataAdarpters.containsKey(siteName))
			return dataAdarpters.get(siteName);
		else
			return null;
	}

	public static void main(String[] argv) {
		System.out.println(dataAdarpters.size());
		for (String key : dataAdarpters.keySet()) {
			System.out.println(key + "\t"
					+ dataAdarpters.get(key).getSiteName());
		}
	}

}
