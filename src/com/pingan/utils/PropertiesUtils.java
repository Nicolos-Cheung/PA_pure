package com.pingan.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	public static Properties prop;

	static {

		InputStream in = PropertiesUtils.class.getClassLoader()
				.getResourceAsStream("prop.properties");
		prop = new Properties();

		try {
			prop.load(in);
		} catch (Exception e) {

			throw new RuntimeException("获取配置文件信息失败！");
		}
	}

	public static String getProp(String key) {

		return prop.getProperty(key, "");
	}
}
