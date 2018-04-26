package org.clc.utils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FileUtil {
	private static final int[] REVERSE_MAPPING = new int[123];
	private static String CHAR_SET = "UTF-8";
	private static String BASE64_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	private static final char[] SIXTY_FOUR_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			is.close();
			// 文件太大，无法读取
			throw new IOException("File is to large " + file.getName());
		}
		// 创建一个数据来保存文件数据
		byte[] bytes = new byte[(int) length];
		// 读取数据到byte数组中
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
			offset += numRead;
		// 确保所有数据均被读取
		if (offset < bytes.length) {
			is.close();
			throw new IOException("Could not completely read file " + file.getName());
		}
		is.close();
		return bytes;
	}

	public static void writerFile(String filePath, String fileName, byte[] xmlContent) throws IOException {
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		FileOutputStream fos = new FileOutputStream(filePath + File.separator + fileName + ".txt");
		fos.write(xmlContent);
		fos.flush();
		fos.close();
	}

	/**
	 * String转Stream型方法
	 *
	 * @param strFile
	 * @return
	 */
	public static InputStream string2InputStream(String strFile) {
		ByteArrayInputStream stream = new ByteArrayInputStream(strFile.getBytes());
		return stream;
	}

	/**
	 * Stream转String型方法1
	 *
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null)
			buffer.append(line);
		return buffer.toString();
	}

	/**
	 * Stream转String型方法2
	 *
	 * @param is
	 * @return
	 */
	public static String inputStream2String2(InputStream is) {
		String allContent = null;
		try {
			allContent = new String();
			InputStream ins = is;
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			byte[] str_b = new byte[1024];
			int i;
			while ((i = ins.read(str_b)) > 0)
				outputstream.write(str_b, 0, i);
			allContent = outputstream.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allContent;
	}

	/**
	 * stream转file
	 *
	 * @param is
	 * @param file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void inputStream2File(InputStream is, File file) throws IOException, FileNotFoundException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead;
		byte[] buffer = new byte[8192];
		while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
			os.write(buffer, 0, bytesRead);
		os.close();
		is.close();
	}

	public static final InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	public static final byte[] input2byte(InputStream inStream) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc;
		while ((rc = inStream.read(buff, 0, 100)) > 0)
			swapStream.write(buff, 0, rc);
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	public static byte[] readInputStream(InputStream inputStream) {
		// 定义一个输出流向内存输出数据
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 定义一个缓冲区
		byte[] buffer = new byte[1024];
		// 读取数据长度
		int len;
		// 当取得完数据后会返回一个-1
		try {
			while ((len = inputStream.read(buffer)) != -1)
				// 把缓冲区的数据 写到输出流里面
				byteArrayOutputStream.write(buffer, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		// 得到数据后返回
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * 传入原图名称，，获得一个以时间格式的新名称
	 *
	 * @param fileName 　原图名称
	 * @return
	 */
	public static String generateFileName(String fileName) {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String formatDate = format.format(new Date());
		int random = new Random().nextInt(10000);
		int position = fileName.lastIndexOf(".");
		String extension = fileName.substring(position);
		return formatDate + random + extension;
	}

	/**
	 * 文件流base64编码
	 *
	 * @return
	 */
	public static String EncodeBase64(byte[] value) {
		ByteArrayOutputStream o = new ByteArrayOutputStream();

		byte[] d = new byte[4];
		try {
			int count = 0;
			// byte[] x = Value.getBytes(this.Charset);
			// byte[] x = file2byte("");
			byte[] x = value;
			while (count < x.length) {
				byte c = x[count];
				count++;
				d[0] = (byte) ((c & 0xFC) >> 2);
				d[1] = (byte) ((c & 0x3) << 4);
				if (count < x.length) {
					c = x[count];
					count++;
					d[1] = (byte) (d[1] + (byte) ((c & 0xF0) >> 4));
					d[2] = (byte) ((c & 0xF) << 2);
					if (count < x.length) {
						c = x[count];
						count++;
						d[2] = (byte) (d[2] + ((c & 0xC0) >> 6));
						d[3] = (byte) (c & 0x3F);
					} else
						d[3] = 64;
				} else {
					d[2] = 64;
					d[3] = 64;
				}
				for (int n = 0; n <= 3; n++)
					o.write(BASE64_STR.charAt(d[n]));
			}
		} catch (StringIndexOutOfBoundsException e) {
		}
		return o.toString();
	}

	/**
	 * 文件流base64解码
	 *
	 * @param value
	 * @return
	 */
	public String DecodeBase64(byte[] value) {
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		String m = "";
		byte[] d = new byte[4];
		try {
			int count = 0;
			// byte[] x = Value.getBytes();
			byte[] x = value;
			while (count < x.length) {
				for (int n = 0; n <= 3; n++) {
					if (count >= x.length)
						d[n] = 64;
					else {
						int y = BASE64_STR.indexOf(x[count]);
						if (y < 0)
							y = 65;
						d[n] = (byte) y;
					}
					count++;
				}
				o.write((byte) (((d[0] & 0x3F) << 2) + ((d[1] & 0x30) >> 4)));
				if (d[2] != 64) {
					o.write((byte) (((d[1] & 0xF) << 4) + ((d[2] & 0x3C) >> 2)));
					if (d[3] != 64)
						o.write((byte) (((d[2] & 0x3) << 6) + (d[3] & 0x3F)));
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			m = o.toString(CHAR_SET);
		} catch (UnsupportedEncodingException ea) {
		}
		return m;
	}

	/**
	 * 获取文件流
	 *
	 * @param filePath
	 * @return
	 */
	public static byte[] file2byte(String filePath) {
		byte[] btemp;
		File f = new File(filePath);
		if (!f.exists()) {
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size)))
				bos.write(buffer, 0, len);
			btemp = bos.toByteArray();
			return btemp;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static boolean deleteDir(String path) {
		if (!(path.endsWith("/") || path.endsWith("\\")))
			path += "/";
		File dir = new File(path);
		String[] list = dir.list();
		if (list == null)
			return true;
		for (int i = 0; i < list.length; i++) {
			File f = new File(path + list[i]);
			if (f.isFile()) {
				if (f.delete()) {
				} else
					return false;
			} else if (!deleteDir(path + list[i] + "/"))
				return false;
		}
		if (delete(dir))
			return true;
		else
			return false;
	}

	private static boolean delete(File file) {
		for (int i = 0; i < 60; i++) {
			if (file.delete())
				return true;
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean exists(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static void makeDir(String path) {
		(new File(path)).mkdir();
	}

	public static List<File> fileList(String path, boolean recursiveFlag) {
		List<File> fileLists = new ArrayList<File>();
		if (!(path.endsWith("/") || path.endsWith("\\")))
			path += "/";
		File dir = new File(path);
		String[] list = dir.list();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File f = new File(path + list[i]);
				if (f.isFile())
					fileLists.add(f);
				else {
					if (recursiveFlag) {
						List<File> child_files = fileList(f.getPath(), recursiveFlag);
						for (int j = 0; j < child_files.size(); j++)
							fileLists.add(child_files.get(j));
					}
				}
			}
		}
		return fileLists;
	}

	public static List<File> dirList(String path) {
		List<File> fileLists = new ArrayList<File>();
		if (!(path.endsWith("/") || path.endsWith("\\")))
			path += "/";
		File dir = new File(path);
		String[] list = dir.list();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File f = new File(path + list[i]);
				if (!f.isFile())
					fileLists.add(f);
			}
		}
		return fileLists;
	}

	/**
	 * 获取字节流的base64编码的字符串
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String imageByte2String(byte[] data) {
		return data != null ? encode(data) : null;
	}

	public static String encode(byte[] input) {
		StringBuffer result = new StringBuffer();
		int outputCharCount = 0;
		for (int i = 0; i < input.length; i += 3) {
			int remaining = Math.min(3, input.length - i);
			int oneBigNumber = (input[i] & 0xff) << 16 | (remaining <= 1 ? 0 : input[i + 1] & 0xff) << 8 | (remaining <= 2 ? 0 : input[i + 2] & 0xff);
			for (int j = 0; j < 4; j++)
				result.append(remaining + 1 > j ? SIXTY_FOUR_CHARS[0x3f & oneBigNumber >> 6 * (3 - j)] : '=');
			if ((outputCharCount += 4) % 76 == 0) result.append('\n');
		}
		return result.toString();
	}
	public static byte[] decode(String input) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			StringReader in = new StringReader(input);
			for (int i = 0; i < input.length(); i += 4) {
				int a[] = {mapCharToInt(in), mapCharToInt(in), mapCharToInt(in), mapCharToInt(in)};
				int oneBigNumber = (a[0] & 0x3f) << 18 | (a[1] & 0x3f) << 12 | (a[2] & 0x3f) << 6 | (a[3] & 0x3f);
				for (int j = 0; j < 3; j++) if (a[j + 1] >= 0) out.write(0xff & oneBigNumber >> 8 * (2 - j));
			}
			return out.toByteArray();
		} catch (IOException e) {
			throw new Error(e + ": " + e.getMessage());
		}
	}

	private static int mapCharToInt(Reader input) throws IOException {
		int c;
		while ((c = input.read()) != -1) {
			int result = REVERSE_MAPPING[c];
			if (result != 0) return result -1;
			if (c == '=') return -1;
		}
		return -1;
	}


}
