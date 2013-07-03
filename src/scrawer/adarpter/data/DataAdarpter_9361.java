package scrawer.adarpter.data;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import util.FileUtil;
import util.JsonUtil;

public class DataAdarpter_9361 extends DataAdarpter {
	private static String siteName = "南京医药药事服务有限公司";
	private static DataAdarpter_9361 instance = new DataAdarpter_9361(siteName);
	static {
		instance.init();
	}

	private DataAdarpter_9361(String siteName) {
		super(siteName);
	}

	@Override
	public String getSiteName() {
		return siteName;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public String analyzer(String type, String content) {

		if (null == content || content.trim().length() < 1)
			return "";
		content = content.substring(content.indexOf('['),
				content.indexOf(']') + 1);
		List<LinkedHashMap<String, Object>> list = JsonUtil.JsonToList(content);
		StringBuffer stb = new StringBuffer();
		Set<String> colHead = new LinkedHashSet<String>();
		for (LinkedHashMap<String, Object> map : list) {
			colHead.addAll(map.keySet());

			// for(String colName:map.keySet()){
			// colHead.put(colName, "");
			// }
		}
		for (String key : colHead) {
			System.out.println(key);
		}
		String[] keys = new String[colHead.size()];
		int index = 0;
		for (String k : colHead) {
			keys[index] = k;
			index++;
			stb.append("\"" + k + "\",");
		}
		stb.append("\n");
		int length = keys.length;
		for (LinkedHashMap<String, Object> map : list) {
			for (index = 0; index < length; index++) {
				String value = "";
				if (map.containsKey(keys[index]))
					value = map.get(keys[index]).toString();
				
				stb.append("\"" + value + "\",");
			}
			stb.append("\n");
		}
		this.GenerateFile(type, stb.toString());

		return null;
	}

	public static void main(String[] argv) {
		String content = FileUtil.readFile("json.txt");
		// System.out.println(content);
		DataAdarpter_9361 dap = new DataAdarpter_9361("");
		dap.analyzer("进货", content);
	}

}
