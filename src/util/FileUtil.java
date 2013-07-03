package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Properties;

public class FileUtil {

	public static HashMap<String, String> getPropertiesConfig(String fileName) {

		InputStream inputStream = ClassLoader
				.getSystemResourceAsStream(fileName);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		for (Object key : p.keySet()) {
			map.put(key.toString(), p.getProperty((String) key));
		}
		return map;
	}

	public static File createFile(String filePath) throws IOException {
		File f = new File(filePath);
		if (!f.exists()) {
			File pd = f.getParentFile();
			if (null != pd && !pd.exists()) {
				pd.mkdirs();
			}
			f.createNewFile();
		}
		return f;
	}

	public static void createFile(String filePath, String charset,
			String content) throws IOException {
		File file = createFile(filePath);
		FileOutputStream fos=new FileOutputStream(file);
		Writer bw;
		bw = new OutputStreamWriter(fos, charset);
		bw.write(null==content?"":content);
		bw.flush();
		bw.close();
		fos.flush();
		fos.close();
	}

	public static String readFile(String filePath) {
		StringBuffer stb = new StringBuffer();

		InputStream ins = ClassLoader.getSystemResourceAsStream(filePath);
		try {
			if (null == ins) {
				ins = new FileInputStream(new File(filePath));
			}
			BufferedReader bufRead = new BufferedReader(new InputStreamReader(
					ins));
			String str;
			while ((str = bufRead.readLine()) != null) {
				stb.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stb.toString();
	}

	public static FileOutputStream fileOutputStream(File f)
			throws FileNotFoundException {
		return new FileOutputStream(f);
	}

	public static void main(String[] argv) {
		getPropertiesConfig("proxy.properties");
	}

}
