package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class Transfer {
	
	//http://118.144.74.39:9198/

	public static void process(File dir, String serverUrl, String clientCode)
			throws Exception {
		String filePath = File.createTempFile("web_", ".zip").getAbsolutePath();

		zipFolder(dir, filePath);
		
		new File(filePath).deleteOnExit();

		// String serverUrl = "http://127.0.0.1:8080/base/";
		// String clientCode = "200646";
		// String serverUrl = "http://118.144.79.164:9090/";
		// String clientCode = "sxblj";
		//
		URLConnection conn = new URL(serverUrl
				+ "/diplugin/dmsFileClientService.do?method=upload&zip=1&clientCode="
								+ clientCode).openConnection();
		conn.setRequestProperty("Content-type", "application/octet-stream");
		// conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		// conn.setUseCaches(false);

		FileInputStream input = new FileInputStream(filePath);
		try {
			OutputStream output = conn.getOutputStream();
			try {
				byte[] buffer = new byte[1024 * 4];
				int n = 0;
				while (-1 != (n = input.read(buffer))) {
					output.write(buffer, 0, n);
				}
			} finally {
				try {
					if (output != null) {
						output.flush();
						output.close();
					}
				} catch (IOException ioe) {
					// ignore
				}
			}
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ioe) {
				// ignore
			}
		}

		// OutputStream os = os.write(uploadFile.get);
		// os.flush();
		// os.close();
		// // int cah = conn.getResponseCode();
		//
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		String mess = reader.readLine();
		reader.close();
	}

	private static void zipFolder(File folder, String targetFileName)
			throws Exception {
		Set<String> fileSet = new HashSet<String>();

		for (File file : folder.listFiles()) {
			fileSet.add(file.getAbsolutePath());
		}
		Zip.zip(fileSet, targetFileName);
	}

}
