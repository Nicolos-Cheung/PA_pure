package com.pingan.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PublicUtils {

	public static void deletefile(String filepath) {
		File registerwav = new File(filepath);
		if (registerwav.exists()) {
			registerwav.delete();
		}
	}

	public static void deletefile(File file) {

		if (file.exists()) {
			file.delete();
		}
	}

	public static String getDate() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date);
	}

	/**
	 * 如果路径不存在则为其创建
	 * 
	 * @param path
	 */
	public static void mkDir(String path) {

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 判断文件类型
	 */
	public static boolean checkFileType(String type, String filename) {

		String[] split = filename.split("\\.");
		String filetype = split[split.length - 1];
		if (!filetype.equals(type)) {
			return false;
		}
		return true;
	}

	private static byte[] get16BitPcm(short[] data) {
		byte[] resultData = new byte[2 * data.length];
		int iter = 0;
		for (double sample : data) {
			short maxSample = (short) ((sample * Short.MAX_VALUE));
			resultData[iter++] = (byte) (maxSample & 0x00ff);
			resultData[iter++] = (byte) ((maxSample & 0xff00) >>> 8);
		}
		return resultData;
	}

	public static void rawToWave(final File rawFile, final File waveFile)
			throws IOException {

		int RECORDER_SAMPLERATE = 16; // ***

		byte[] rawData = new byte[(int) rawFile.length()];
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(rawFile));
			input.read(rawData);
		} finally {
			if (input != null) {
				input.close();
			}
		}

		DataOutputStream output = null;
		try {
			output = new DataOutputStream(new FileOutputStream(waveFile));
			// WAVE header
			// see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
			writeString(output, "RIFF"); // chunk id
			writeInt(output, 36 + rawData.length); // chunk size
			writeString(output, "WAVE"); // format
			writeString(output, "fmt "); // subchunk 1 id
			writeInt(output, 16); // subchunk 1 size
			writeShort(output, (short) 1); // audio format (1 = PCM)
			writeShort(output, (short) 1); // number of channels
			writeInt(output, 8000); // sample rate //****
			writeInt(output, RECORDER_SAMPLERATE * 2); // byte rate
			writeShort(output, (short) 2); // block align
			writeShort(output, (short) 16); // bits per sample
			writeString(output, "data"); // subchunk 2 id
			writeInt(output, rawData.length); // subchunk 2 size
			// Audio data (conversion big endian -> little endian)
			short[] shorts = new short[rawData.length / 2];
			ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN)
					.asShortBuffer().get(shorts);
			ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
			for (short s : shorts) {
				bytes.putShort(s);
			}

			output.write(fullyReadFileToBytes(rawFile));
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	static byte[] fullyReadFileToBytes(File f) throws IOException {
		int size = (int) f.length();
		byte bytes[] = new byte[size];
		byte tmpBuff[] = new byte[size];
		FileInputStream fis = new FileInputStream(f);
		try {

			int read = fis.read(bytes, 0, size);
			if (read < size) {
				int remain = size - read;
				while (remain > 0) {
					read = fis.read(tmpBuff, 0, remain);
					System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
					remain -= read;
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			fis.close();
		}

		return bytes;
	}

	private static void writeInt(final DataOutputStream output, final int value)
			throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
		output.write(value >> 16);
		output.write(value >> 24);
	}

	private static void writeShort(final DataOutputStream output,
			final short value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
	}

	private static void writeString(final DataOutputStream output,
			final String value) throws IOException {
		for (int i = 0; i < value.length(); i++) {
			output.write(value.charAt(i));
		}
	}

	/**
	 * 根据用户名，和文件根目录，返回该用户名在根目录下的hashpath
	 * 
	 * @param user_id
	 * @param root
	 * @return /Users/ning/2/3/20160301_userid
	 */
	public static String getUserFilePath(String user_id, String root) {

		StringBuffer sb = new StringBuffer();
		sb.append(root);
		sb.append("/");
		sb.append(user_id.hashCode() & 0xf);
		sb.append("/");
		sb.append((user_id.hashCode() >> 4) & 0xf);
		sb.append("/");
		sb.append(PublicUtils.getDate());
		sb.append("_");
		sb.append(user_id);
		return sb.toString();
	}

	/**
	 * 根据用户名，和文件根目录，返回该用户名在根目录下的hashpath
	 * 
	 * @param user_id
	 * @param root
	 * @return /Users/ning/2/3/20160301_userid
	 */
	public static String getConstantUserFilePath(String user_id, String root) {

		StringBuffer sb = new StringBuffer();
		sb.append(root);
		sb.append("/");
		sb.append(user_id.hashCode() & 0xf);
		sb.append("/");
		sb.append((user_id.hashCode() >> 4) & 0xf);
		sb.append("/");
		sb.append(user_id);
		sb.append("_");
		sb.append("test");
		return sb.toString();
	}

	/**
	 * 获取随机文件名
	 * 
	 * @param user_id
	 *            用户ID
	 * @param filetype
	 *            文件类型
	 * @return register_20160913_userid.pcm
	 */
	public static String getFileName(String usage, String user_id,
			String filetype) {

		StringBuffer sb = new StringBuffer();
		sb.append(usage);
		sb.append("_");
		sb.append(PublicUtils.getDate());

		sb.append("_");
		sb.append(user_id);
		sb.append(".");
		sb.append(filetype);
		return sb.toString();
	}

	/**
	 * 获取随机文件名
	 * 
	 * @param user_id
	 *            用户ID
	 * @param filetype
	 *            文件类型
	 * @return register_uuid_userid.pcm
	 */
	public static String getFileNameWithUUID(String usage, String user_id,
			String filetype) {

		StringBuffer sb = new StringBuffer();
		sb.append(usage);
		sb.append("_");
		sb.append(UUID.randomUUID().toString());

		sb.append("_");
		sb.append(user_id);
		sb.append(".");
		sb.append(filetype);
		return sb.toString();
	}

	/**
	 * 获得一个文件的完整路径
	 * 
	 * @param filename
	 * @param user_id
	 * @param root
	 * @return /Users/ning/2/3/filename.pcm
	 */
	public static String getFilePath(String filename, String user_id,
			String root) {

		StringBuffer sb = new StringBuffer();
		sb.append(getUserFilePath(user_id, root));
		sb.append("/");
		sb.append(filename);
		return sb.toString();
	}

	public static String getDetailData() {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static boolean strIsNotNull(String str) {
		if (null == str || "".equals(str)) {
			return false;
		} else {
			return true;
		}
	}
}
