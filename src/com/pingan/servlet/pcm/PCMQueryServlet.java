package com.pingan.servlet.pcm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.pingan.service.PCMSerivce;
import com.pingan.service.impl.PCMSerivceImpl;

public class PCMQueryServlet extends BaseUploadServlet {

	private static final long serialVersionUID = -6751736837368642046L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------Query...----------------");

		int statues_code = 0;

		String person_id = request.getParameter("person_id");
		System.out.println("Person_id=" + person_id);

		String response_num = request.getParameter("response_num");

		if (!service.isAvailable(person_id)) {
			statues_code = 1;
		}

		responsebyJson(response_num, statues_code, response.getWriter());
		
		System.out.println("----------------Query Complete!----------------");

	}

}
