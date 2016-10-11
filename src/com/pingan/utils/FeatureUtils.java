package com.pingan.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pingan.constant.Constant;

public class FeatureUtils {

	/**
	 * 
	 * @param filepath
	 *            wav路径
	 * @param toolpath
	 *            tool的路径
	 * @return
	 */
	public static List<String> KaldiToIvecter(String filepath, String toolpath) {
		Process process = null;
		List<String> processList = new ArrayList<String>();

		
		String commond = toolpath + "/tgb --config=" + toolpath
				+ "/compute.conf " + filepath + " " + toolpath + "/awq "
				+ toolpath + "/rty" + " " + toolpath + "/edc";
		try {
			process = Runtime.getRuntime().exec(commond);
			process.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processList;
	}

	/**
	 * 
	 * @param filepath
	 *            wav路径
	 * @param toolpath
	 *            tool的路径
	 * @return
	 */
	public static List<String> KaldiToPcmIvecter(String filepath,
			String toolpath) {
		Process process = null;
		List<String> processList = new ArrayList<String>();

		String commond = toolpath + "/yuh --config=" + toolpath
				+ "/compute.conf " + filepath + " " + toolpath + "/awq "
				+ toolpath + "/rty" + " " + toolpath + "/edc";
		try {
			process = Runtime.getRuntime().exec(commond);
			process.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processList;
	}

	/**
	 * 
	 * 点积评分方式
	 * @param registerdir
	 *            注册特征值文件路径
	 * @param testdir
	 *            测试生成的特征值文件路径
	 * @param toolpath
	 * @return
	 */
	public static double KaldiDotscore(String registerdir, String testdir,
			String toolpath) {
		Process process = null;
		List<String> processList = new ArrayList<String>();
		String commond = toolpath + "/wav_score_dot_product " + registerdir
				+ " " + testdir;
		try {
			process = Runtime.getRuntime().exec(commond);
			process.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();

			if (processList != null && processList.size() > 0) {
				String str = processList.get(0).trim();
				return Double.parseDouble(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * PLDA评分方式
	 * @param registerdir
	 *            注册特征值文件路径
	 * @param testdir
	 *            测试生成的特征值文件路径
	 * @param toolpath
	 * @return
	 */
	public static double KaldiPLDAscore(String registerdir, String testdir,
			String toolpath) {
		Process process = null;
		List<String> processList = new ArrayList<String>();
		String commond = toolpath + "/wav_score " + registerdir + " " + testdir
				+ " " + toolpath + "/iok";
		try {
			process = Runtime.getRuntime().exec(commond);
			process.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();

			String string = processList.get(0).trim();
			if (processList != null && processList.size() > 0) {
				return Double.parseDouble(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * PLDA3评分方式
	 * @param registerdir
	 *            注册特征值文件路径
	 * @param testdir
	 *            测试生成的特征值文件路径
	 * @param toolpath
	 * @return
	 */
	public static double KaldiPLDAscore2(String registerdir, String testdir,
			String toolpath) {
		Process process = null;
		List<String> processList = new ArrayList<String>();
		String commond = toolpath + "/scoring " + registerdir + " "
				+ testdir + " " + toolpath + "/iok " + toolpath
				+ "/ghb";
		try {
			process = Runtime.getRuntime().exec(commond);
			process.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();

			String string = processList.get(0).trim();
			if (processList != null && processList.size() > 0) {
				return Double.parseDouble(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @param list
	 * @param path
	 *            特征值存放的路径
	 * @param filename
	 *            特征值文件名
	 * @return
	 */
	public static String ListToFile_Return_FilePath(List<String> list,
			String path, String filename) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		StringBuffer bf = new StringBuffer();
		if (list.size() == 0) {
			return "";
		}
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			bf.append(iter.next());
		}
		try {
			fos = new FileOutputStream(new File(path, filename));

			osw = new OutputStreamWriter(fos);

			osw.write(bf.toString());

			return path + "/" + filename + ".feature";

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (osw != null) {
					osw.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (osw != null) {

					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";

	}

	/**
	 * 
	 * @param list
	 * @param path
	 *            特征值存放的路径
	 * @param filename
	 *            特征值文件名
	 * @return
	 */
	public static String ListToFile_Return_FilePath2(List<String> list,
			String path, String filename) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		StringBuffer bf = new StringBuffer();
		if (list.size() == 0) {
			return "";
		}
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			bf.append(iter.next());
		}
		try {
			fos = new FileOutputStream(new File(path, filename));

			osw = new OutputStreamWriter(fos);

			osw.write(bf.toString());

			return path + "/" + filename;

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (osw != null) {
					osw.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (osw != null) {

					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";

	}

	/**
	 * 
	 * @param list
	 * @param path
	 *            特征值存放的路径
	 * @param filename
	 *            特征值文件名
	 * @return
	 */
	public static boolean ListToFile(List<String> list, String path,
			String filename) {

		StringBuffer bf = new StringBuffer();

		if (list.size() == 0) {
			return false;
		}

		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			bf.append(iter.next());
		}
		try {
			FileOutputStream fos = new FileOutputStream(
					new File(path, filename));

			OutputStreamWriter osw = new OutputStreamWriter(fos);

			osw.write(bf.toString());

			osw.close();
			fos.close();

			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 
	 * @param list
	 * @param path
	 *            特征值存放的路径
	 * @param filename
	 *            特征值文件名
	 * @return
	 */
	public static String ListToString(List<String> list) {

		StringBuffer bf = new StringBuffer();
		if (list.size() == 0) {
			return "";
		}

		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			bf.append(iter.next());
		}

		return bf.toString();
	}
	
	/**
	 * 
	 * @param register_ivecter_dir
	 *            注册的特征文件的路径
	 * @param test_ivector_dir
	 *            验证的wav的文件路径
	 * @return 评分
	 */
	public static double KaldiTest(String register_ivecter_dir, String test_ivector_dir) {

		double result = -999;

		switch (Constant.SCORE_MODE) {
		case PLDA:
			result = FeatureUtils.KaldiPLDAscore(register_ivecter_dir,
					test_ivector_dir, Constant.PCMTOOLPATH);
			break;

		case DOT:
			result = FeatureUtils.KaldiDotscore (register_ivecter_dir,
					test_ivector_dir, Constant.PCMTOOLPATH);
			break;
		}
		return result;
	}
}
