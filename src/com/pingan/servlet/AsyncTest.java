package com.pingan.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AsyncTest extends HttpServlet {

	private static final long serialVersionUID = -1987481036448599107L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		PrintWriter writer = response.getWriter();

		System.out.println("Servlet开始时间：" + new Date() + ".");
		AsyncContext ctx = request.startAsync();
		new Thread(new Executor(ctx)).start();
		System.out.println("Servlet结束时间：" + new Date() + ".");


//		PrintWriter out = response.getWriter();
//		System.out.println("Servlet Do Post Start");
//		out.println("<h1>Servlet Do Post Start</h1>");
//		out.flush();
//
//		AsyncContext context = request.startAsync();
//
//		// 开启线程
//		context.start(new AsyncThread(context));
//
//		// new AsyncThread(context).start();
//		System.out.println("Servlet Do Post End");
//		out.println("<h1>Servlet Do Post End</h1>");
//		out.flush();

	}

}

class AsyncThread extends Thread {
	private AsyncContext context;

	public AsyncThread(AsyncContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {

			PrintWriter out = context.getResponse().getWriter();
			Thread.sleep(2000);
			out.println("<h1>异步延时输出，这里可以进行一些耗时的操作</h1>");
			out.flush();
			context.complete();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Executor implements Runnable {
	private AsyncContext ctx = null;
	public Executor(AsyncContext ctx) {
		this.ctx = ctx;
	}

	public void run() {
		try {
			// 等待十秒钟，以模拟业务方法的执行
			Thread.sleep(5000);
			PrintWriter out = ctx.getResponse().getWriter();
			System.out.println("业务处理完毕的时间：" + new Date() + ".");
			out.write("xxxxx");
			out.flush();
			out.close();
			ctx.complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}