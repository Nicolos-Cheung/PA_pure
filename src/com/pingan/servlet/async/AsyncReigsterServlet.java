package com.pingan.servlet.async;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pingan.constant.Constant;
import com.pingan.domain.PCMRequestBean;
import com.pingan.factory.UploadFactory;
import com.pingan.servlet.async.thread.AsyncRegisterProcessor;
import com.pingan.servlet.listener.AppAsyncListener;
import com.pingan.servlet.pcm.BaseUploadServlet;
import com.pingan.utils.FeatureUtils;
import com.pingan.utils.PublicUtils;

public class AsyncReigsterServlet extends BaseUploadServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------Async Register...----------------");
		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		response.setContentType("text/plain");
		// 向客户端发送响应正文
		PrintWriter outNet = response.getWriter();

		PCMRequestBean pcb = new PCMRequestBean();
		int statues_code = 0; // 0成功注册，1上传文件失败
		String userfilepath = "";
		String response_num = null;
		boolean isCancel = false; // 用户曾经已注册并注销

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			ServletFileUpload upload = UploadFactory.getBaseUpLoad(tempFile,
					20 * 1024 * 1024);

			try {

				List<FileItem> items = upload.parseRequest(request);

				for (FileItem item : items) {
					if (item.isFormField()) {

						// 表单非文件数据
						String name = item.getFieldName();
						if (name.equals("user_id")) {
							pcb.setUser_id(item.getString());
						}
						if (name.equals("person_id")) {
							pcb.setPerson_id(item.getString());
						}
						if (name.equals("telnum")) {
							pcb.setTelnum(item.getString());
						}
						if (name.equals("source")) {
							pcb.setSource(item.getString());
						}
						if (name.equals("policy_number")) {
							pcb.setPolicy_number(item.getString());
						}
						if (name.equals("response_num")) {
							response_num = item.getString();
						}

						pcb.setRegister_date(PublicUtils.getDetailData());

					} else {

						if (pcb.getPerson_id() == null
								|| "".equals(pcb.getPerson_id())
								|| service.IsUserExist(pcb.getPerson_id())) {
							statues_code += 4;
						} else {

							PCMRequestBean oldpcb = service.Query(pcb
									.getPerson_id());

							if (oldpcb != null && oldpcb.getAvailable() != null) {
								userfilepath = oldpcb.getUser_root_path();

								isCancel = true;
							} else {
								// 根据 userid 和根目录 得到用户的文件目录
								userfilepath = PublicUtils.getUserFilePath(
										pcb.getPerson_id(), Constant.PCMROOT);
							}

							System.out.println(userfilepath);

							pcb.setUser_root_path(userfilepath);

							PublicUtils.mkDir(userfilepath);

							String filename = PublicUtils.getFileName(
									"register", pcb.getPerson_id(), "pcm");

							File pcmfile = PublicUtils.processUploadedFile(
									item, filename, userfilepath, "pcm"); // 处理上传文件

							if (pcmfile == null) {
								statues_code += 1;
							} else {
								pcb.setRegister_voice_path(pcmfile
										.getAbsolutePath());
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		AsyncContext asyncCtx = null;
		try {
			// 进行异步注册
			asyncCtx = request.startAsync();
			asyncCtx.addListener(new AppAsyncListener());
			ThreadPoolExecutor executor = (ThreadPoolExecutor) request
					.getServletContext().getAttribute("executor");
			AsyncRegisterProcessor asyncpro = new AsyncRegisterProcessor(
					asyncCtx, pcb, userfilepath, isCancel, service,
					statues_code, response_num);
			executor.execute(asyncpro);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (asyncCtx != null) {
				asyncCtx.complete();
			}
		}
		System.out
				.println("----------------Register Complete!----------------");

	}

}
