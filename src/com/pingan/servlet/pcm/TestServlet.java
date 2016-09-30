package com.pingan.servlet.pcm;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pingan.domain.PCMRequestBean;
import com.pingan.service.impl.PCMSerivceImpl;

public class TestServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PCMSerivceImpl service = new PCMSerivceImpl();
		PCMRequestBean query = service.Query("123456");
		System.out.println(query.toString());
	}

}
