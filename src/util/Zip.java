package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Zip {

	public static void zip(String source, String zipFileName)
			throws IOException {
		File sourceFile = new File(source);
		List<?> files = getSubFiles(sourceFile);

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				zipFileName));

		ZipEntry ze = null;
		byte[] buf = new byte[2048];
		int readLen = 0;
		for (int i = 0; i < files.size(); i++) {
			File subFile = (File) files.get(i);

			ze = new ZipEntry(getAbsFileName(sourceFile, subFile));
			ze.setSize(subFile.length());
			ze.setTime(subFile.lastModified());

			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(
					subFile));
			while ((readLen = is.read(buf, 0, 2048)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.close();
	}

	public static void zip(Set<String> source, String zipFileName)
			throws IOException {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				zipFileName));

		ZipEntry ze = null;
		byte[] buf = new byte[2048];
		int readLen = 0;
		for (String filePath : source) {
			File subFile = new File(filePath);
			ze = new ZipEntry(subFile.getName());
			ze.setSize(subFile.length());
			ze.setTime(subFile.lastModified());

			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(
					subFile));
			while ((readLen = is.read(buf, 0, 2048)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.close();
	}

	public static void unzip(String zipFileName, String target)
			throws Exception {
		ZipFile zipfile = new ZipFile(zipFileName);
		Enumeration<?> subFiles = zipfile.entries();
		ZipEntry zipEntry = null;
		byte[] buf = new byte[1024];
		while (subFiles.hasMoreElements()) {
			zipEntry = (ZipEntry) subFiles.nextElement();
			if (zipEntry.isDirectory()) {
				continue;
			}
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(target, zipEntry.getName())));
			InputStream is = new BufferedInputStream(
					zipfile.getInputStream(zipEntry));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zipfile.close();
	}

	public static Key getKey(String keyPath) throws Exception {
		FileInputStream fis = new FileInputStream(keyPath);
		byte[] b = new byte[16];
		fis.read(b);
		SecretKeySpec dks = new SecretKeySpec(b, "AES");
		fis.close();
		return dks;
	}

	public static void encrypt(String srcFile, String destFile, Key privateKey)
			throws Exception {
		SecureRandom sr = new SecureRandom();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(privateKey.getEncoded());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey, spec, sr);
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		byte[] b = new byte[2048];
		int byteread = 0;
		while ((byteread = fis.read(b)) != -1) {
			fos.write(cipher.doFinal(b, 0, byteread));
		}
		fos.close();
		fis.close();
	}

	public static void decrypt(String srcFile, String destFile, Key privateKey)
			throws Exception {
		SecureRandom sr = new SecureRandom();
		Cipher ciphers = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(privateKey.getEncoded());
		ciphers.init(Cipher.DECRYPT_MODE, privateKey, spec, sr);
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		byte[] b = new byte[2064];
		int byteread = 0;
		while ((byteread = fis.read(b)) != -1) {
			fos.write(ciphers.doFinal(b, 0, byteread));
		}
		fos.close();
		fis.close();
	}

	public static void encryptZip(String source, String destFile, String keyFile)
			throws Exception {
		Key key = getKey(keyFile);
		File temp = new File(UUID.randomUUID().toString() + ".zip");
		temp.deleteOnExit();
		zip(source, temp.getAbsolutePath());
		encrypt(temp.getAbsolutePath(), destFile, key);
		temp.delete();
	}

	public static void decryptUnzip(String srcFile, String target,
			String keyFile) throws Exception {
		File temp = new File(UUID.randomUUID().toString() + ".zip");
		temp.deleteOnExit();
		decrypt(srcFile, temp.getAbsolutePath(), getKey(keyFile));
		unzip(temp.getAbsolutePath(), target);
		temp.delete();
	}

	public static void createKey(String keyFile) throws Exception {
		SecureRandom sr = new SecureRandom();
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(128, sr);
		SecretKey key = kg.generateKey();
		File f = new File(keyFile);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key.getEncoded());
	}

	private static File getRealFileName(String source, String absFileName) {
		String[] dirs = absFileName.split("/");
		File file = new File(source);
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				file = new File(file, dirs[i]);
			}
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, dirs[dirs.length - 1]);
		return file;
	}

	private static String getAbsFileName(File sourceFile, File subFile) {
		String result = subFile.getName();
		if (sourceFile.isDirectory()) {
			File sub = subFile;
			while (true) {
				sub = sub.getParentFile();
				if (sub == null) {
					break;
				}
				if (sub.equals(sourceFile)) {
					break;
				} else {
					result = sub.getName() + "/" + result;
				}
			}
		}
		return result;
	}

	private static List<File> getSubFiles(File source) {
		List<File> files = new ArrayList<File>();
		if (source.isDirectory()) {
			File[] tmp = source.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isFile()) {
					files.add(tmp[i]);
				}
				if (tmp[i].isDirectory()) {
					files.addAll(getSubFiles(tmp[i]));
				}
			}
		} else {
			files.add(source);
		}
		return files;
	}
}
