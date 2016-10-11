package com.pingan.servlet.pcm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * 处理register请求的servlet
 * 
 * @author ning
 */
public class PCMRegisterServlet extends BaseUploadServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("----------------Register...----------------");

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
								|| "".equals(pcb.getPerson_id())) {
							statues_code += 8;  //person_id为空

						} else if (service.IsUserExist(pcb.getPerson_id())) {
							//用户存在
							statues_code += 4;
						} else {
							PCMRequestBean oldpcb = service.Query(pcb
									.getPerson_id());
							if (oldpcb != null && oldpcb.getAvailable() != null) {
								userfilepath = oldpcb.getUser_root_path();

								isCancel = true;
							} else {
								// 根据userid 和根目录 得到用户的文件目录
								userfilepath = PublicUtils.getUserFilePath(
										pcb.getPerson_id(), Constant.PCMROOT);
							}

							System.out.println(userfilepath);

							pcb.setUser_root_path(userfilepath);

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

		// 进行注册

		if (statues_code <= 0) {
			List<String> ivectorList = FeatureUtils.KaldiToPcmIvecter(
					pcb.getRegister_voice_path(), Constant.PCMTOOLPATH);

			if (null == ivectorList || ivectorList.size() == 0) {
				statues_code += 2; // ivector计算出错
			} else if (statues_code == 0) {
				String ivectorPath = FeatureUtils.ListToFile_Return_FilePath2(
						ivectorList, userfilepath, PublicUtils.getFileName(
								"register", pcb.getPerson_id(), "feature"));

				pcb.setAvailable("1"); // 标志位
				pcb.setIvector_path(ivectorPath);
				pcb.setIvector_version(Constant.IVECTOR_VERSION);

				pcb.toString();
				if (pcb.isAbleToRegister()) {

					if (isCancel) {
						boolean update = service.update(pcb);
						if (!update) {
							statues_code += 16;
						}

					} else {
						boolean register = service.register(pcb);
						if (!register) {
							statues_code += 16;
						}
					}

					System.out.println("RegisterPCB:==>" + pcb.toString());
				} else {
					statues_code += 8;
				}

			}
		}

		responsebyJson(response_num, statues_code, outNet);
		System.out
				.println("----------------Register Complete!----------------");

	}

}
