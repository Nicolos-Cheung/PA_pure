package com.pingan.servlet.async.thread;

import java.io.PrintWriter;

import javax.servlet.AsyncContext;

import net.sf.json.JSONObject;

public class BaseAsyncProcessor {


	public void responsebyJson(String response_num, int statues_code,
			PrintWriter outNet) {

		JSONObject json = new JSONObject();
		json.put("response_num", response_num);
		json.put("statues_code", statues_code); // 1上传失败 2ivector计算出错 4用户已注册

		outNet.write(json.toString());
		// outNet.print(json.toString());
		if (outNet != null) {
			outNet.close();
		}
		System.out.println("Json==>" + json.toString());

	}

	public void responsebyJson(String response_num, int statues_code,
			int result, PrintWriter outNet) {

		JSONObject json = new JSONObject();
		json.put("response_num", response_num);
		json.put("statues_code", statues_code);

		outNet.print(json.toString());
		if (outNet != null) {
			outNet.close();
		}

		System.out.println("Json==>" + json.toString());

	}
}
