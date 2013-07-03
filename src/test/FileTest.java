package test;

import java.io.IOException;

import util.FileUtil;

public class FileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = FileUtil.readFile("c:/export.txt");
		try {
			FileUtil.createFile("d:/export.xls", "UTF-8", s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
