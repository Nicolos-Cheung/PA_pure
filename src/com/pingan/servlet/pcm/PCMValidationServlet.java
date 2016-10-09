package com.pingan.servlet.pcm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pingan.constant.Constant;
import com.pingan.domain.PCMRequestBean;
import com.pingan.factory.UploadFactory;
import com.pingan.service.PCMSerivce;
import com.pingan.service.impl.PCMSerivceImpl;
import com.pingan.utils.FeatureUtils;
import com.pingan.utils.PublicUtils;

public class PCMValidationServlet extends BaseUploadServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------Validation...----------------");

		int statues_code = 0;
		PCMRequestBean pcb = null;
		File testvoice = null;
		String usertestpath = "";

		response.setContentType("text/plain");
		// 向客户端发送响应正文
		PrintWriter outNet = response.getWriter();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		String person_id = null;
		String response_num = null;
		String nas_dir = null;

		if (isMultipart) {

			ServletFileUpload upload = UploadFactory.getBaseUpLoad(tempFile,
					20 * 1024 * 1024);

			try {
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {
						// 非文件参数
						// processFormField(item, outNet); // 处理普通的表单域
						String name = item.getFieldName();
						if (name.equals("person_id")) {
							person_id = item.getString();
						}
						if (name.equals("response_num")) {
							response_num = item.getString();
						}
						if (name.equals("nas_dir")) {
							nas_dir = item.getString();
						}

					} else {
						System.out.println("Validation_person_id=" + person_id);

						if (person_id == null || "".equals(person_id)) {

						} else {
							pcb = service.Query(person_id);
							// 验证语音的nas_dir
							pcb.setNas_dir(nas_dir);
						}

						if (pcb != null && pcb.isAbleToVali()) {
							// 文件
							if (item.getFieldName().equals("file")) {

								usertestpath = PublicUtils
										.getConstantUserFilePath(
												pcb.getPerson_id(),
												Constant.PCMTESTROOT);

								// 处理上传文件
								// testvoice = processUploadedFile(item,
								// , usertestpath, "pcm");

								// 处理文件上传
								String filename = PublicUtils
										.getFileNameWithUUID("test",
												pcb.getUser_id(), "pcm");
								testvoice = PublicUtils.processUploadedFile(
										item, filename, usertestpath, "pcm");

								if (testvoice == null) {
									statues_code += 1;
								}

							}
						} else {
							statues_code += 4; // 该注册用户不符合验证条件
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		int result = -999;
		if (statues_code <= 0) {

			List<String> featureList = FeatureUtils.KaldiToPcmIvecter(
					testvoice.getAbsolutePath(), Constant.PCMTOOLPATH);

			if (featureList != null && featureList.size() > 0) {

				// 特征文件写入本地
				String testivectorPath = FeatureUtils
						.ListToFile_Return_FilePath2(
								featureList,
								usertestpath,
								PublicUtils.getFileName("test",
										pcb.getPerson_id(), "feature"));

				// 评分
				double score = FeatureUtils.KaldiDotscore(
						pcb.getIvector_path(), testivectorPath,
						Constant.PCMTOOLPATH);
				if (score > Constant.DOT_THRESHOLD) {
					result = 0;
				} else {
					result = 1;
				}
				service.update(pcb);// 更新数据库 nas_dir

				System.out.println("score==>" + score);

			} else {
				statues_code += 2;
			}
		}

		responsebyJson(response_num, statues_code, result, outNet);
		System.out
				.println("----------------Validation Complete!----------------");
	}

}
