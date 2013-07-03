package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {

	public static OutputStream copyStream(InputStream ins, OutputStream ops) {
		BufferedInputStream bis = new BufferedInputStream(ins);
		BufferedOutputStream bos = new BufferedOutputStream(ops);
		byte[] b = new byte[1024];
		int num = -1;
		try {
			while (true) {
				num = bis.read(b);
				if (num == -1) {// 没读到字节
//					System.out.println("复制完毕！ 文件保存在F:\\beauty.jpg");
					break;
				}
				bos.write(b, 0, num);// 此语句实现输入流和输入流的间接联系
				bos.flush();// 否则图片会变小
			}// end while

			ins.close();
			bis.close();
			ops.close();
			bos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ops;
	}
}
