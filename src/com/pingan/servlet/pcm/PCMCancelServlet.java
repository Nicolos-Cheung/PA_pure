package com.pingan.servlet.pcm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.pingan.domain.PCMRequestBean;
import com.pingan.service.impl.PCMSerivceImpl;

public class PCMCancelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private PCMSerivceImpl service;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		service = new PCMSerivceImpl();
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------Cancel...----------------");

		int statues_code = 3;
		PCMRequestBean pcb = null;

		String person_id = request.getParameter("person_id");
		String response_num = request.getParameter("response_num");
		
		System.out.println("cancel_person_id="+person_id);


		if (person_id != null && !("".equals(person_id))) {
			pcb = service.Query(person_id.trim());
		}

		if (null != pcb && pcb.isAbleToCancel()) {
			statues_code -= 2;
			pcb.setAvailable("0");
			boolean iscancel = service.update(pcb);

			if (iscancel)
				statues_code -= 1;
		}

		JSONObject jo = new JSONObject();
		jo.put("response_num", response_num);
		jo.put("statues_code", statues_code);

		response.getWriter().write(jo.toString());
		System.out.println("Json==>" + jo.toString());

		System.out.println("----------------Cancel Complete!----------------");
	}
}
