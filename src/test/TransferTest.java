package test;

import java.io.File;

import util.Constants;
import util.Transfer;

public class TransferTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File f=new File(Constants.outPoutDirectory+"福建同春药业股份有限公司"+"/");
		try {
			Transfer.process(f,Constants.transferURL, "3405");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("上传文件过程中出现异常！");
		}
		
		
	}

}
