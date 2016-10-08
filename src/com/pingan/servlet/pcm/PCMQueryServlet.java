package com.pingan.servlet.pcm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.pingan.service.PCMSerivce;
import com.pingan.service.impl.PCMSerivceImpl;

public class PCMQueryServlet extends HttpServlet {

	private static final long serialVersionUID = -6751736837368642046L;
	private PCMSerivceImpl service;

	@Override
	public void init() throws ServletException {

		super.init();
		service = new PCMSerivceImpl();

	}

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

		JSONObject json = new JSONObject();
		json.put("response_num", response_num);
		json.put("statues_code", statues_code);

		response.getWriter().write(json.toString());

		System.out.println("json==>" + json.toString());
		System.out.println("----------------Query Complete!----------------");

	}

}
