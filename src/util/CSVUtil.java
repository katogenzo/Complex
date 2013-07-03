package util;

import java.util.StringTokenizer;

public class CSVUtil {

	public static String[][] formatAsArray(String content, String delimiter) {
		if (null != content && content.trim().length() > 1 && null != delimiter
				&& delimiter.trim().length() > 0) {
			StringTokenizer stk = new StringTokenizer(content, "\n");
			String[][] arrayContent = new String[stk.countTokens()][];
			int index = 0;
			while (stk.hasMoreElements()) {
				String row = stk.nextToken();
				arrayContent[index] = row.split(delimiter);
				index++;
			}
			return arrayContent;
		}
		return null;
	}

	public static String filterColumn(String[] source, int[] colindex,String delimiter) {
		StringBuffer tString=new StringBuffer();
		int index = 0;
		for (; index < colindex.length; index++) {
			tString.append(source[colindex[index]]).append(delimiter);
		}
		return tString.toString();
	}

	
	public static String appendCommaRow(int commas){
		String s="";
		for(int i=0;i<commas;i++){
			s+="\"\",";
		}
		return s;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
