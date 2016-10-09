package com.pingan.servlet.pcm;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONObject;

import com.pingan.constant.Constant;
import com.pingan.service.impl.PCMSerivceImpl;
import com.pingan.utils.PublicUtils;

public class BaseUploadServlet extends HttpServlet {

	public PCMSerivceImpl service;
	public File tempFile;

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		service = new PCMSerivceImpl();
		tempFile = new File(Constant.TEMPPATH);

		PublicUtils.mkDir(Constant.PCMTESTROOT);
		PublicUtils.mkDir(Constant.PCMROOT);
		PublicUtils.mkDir(Constant.TEMPPATH);

	}

	public void responsebyJson(String response_num, int statues_code,
			PrintWriter outNet) {

		JSONObject json = new JSONObject();
		json.put("response_num", response_num);
		json.put("statues_code", statues_code); // 1上传失败 2ivector计算出错 4用户已注册

		outNet.write(json.toString());
//		outNet.print(json.toString());
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
