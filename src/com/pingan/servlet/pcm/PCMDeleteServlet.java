package com.pingan.servlet.pcm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.pingan.domain.PCMRequestBean;
import com.pingan.service.impl.PCMSerivceImpl;

public class PCMDeleteServlet extends BaseUploadServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------Delete...----------------");

		int statues_code = 0;
		String person_id = request.getParameter("person_id");
		String response_num = request.getParameter("response_num");

		System.out.println("delete_person_id" + person_id);

		if (person_id != null && !("".equals(person_id))) {
			service.remove(person_id);
		} else {
			statues_code += 1; // 用户不存在
		}

		responsebyJson(response_num, statues_code, response.getWriter());
		
		System.out.println("----------------Delete Complete!----------------");
	}
}
