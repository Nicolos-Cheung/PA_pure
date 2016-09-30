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
import com.pingan.service.PCMSerivce;
import com.pingan.service.impl.PCMSerivceImpl;
import com.pingan.utils.FeatureUtils;
import com.pingan.utils.PublicUtils;

public class PCMValidationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private File tempFile;
	private PCMSerivce service;

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		service = new PCMSerivceImpl();
		tempFile = new File(Constant.TEMPPATH);
		PublicUtils.mkDir(Constant.PCMTESTROOT);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int statues_code = 0;

		PCMRequestBean pcb = null;
		File testvoice = null;
		String usertestpath = "";

		System.out.println("----------PCMValidationServlet run!--------");

		response.setContentType("text/plain");
		// 向客户端发送响应正文
		PrintWriter outNet = response.getWriter();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		String person_id = null;
		String response_num = null;

		if (isMultipart) {

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024); // 设置缓冲区大小为1M
			factory.setRepository(tempFile); // 设置临时目录

			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");
			upload.setSizeMax(8 * 1024 * 1024); // 允许文件的最大上传尺寸8M
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
					} else {
						System.out.println(person_id);

						if (person_id == null || "".equals(person_id)) {

							
						} else {
							pcb = service.Query(person_id);
							System.out.println("register_pcb" + pcb.toString());
						}

						if (pcb != null && pcb.isAbleToVali()) {
							// 文件
							if (item.getFieldName().equals("file")) {

								System.out.println(pcb.getPerson_id());

								usertestpath = PublicUtils
										.getConstantUserFilePath(
												pcb.getPerson_id(),
												Constant.PCMTESTROOT);

								// 处理上传文件
								testvoice = processUploadedFile(item,
										pcb.getUser_id(), usertestpath, "pcm");

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

			System.out.println("------------------");
			if (featureList != null && featureList.size() > 0) {

				// 特征文件写入本地
				String testivectorPath = FeatureUtils
						.ListToFile_Return_FilePath2(
								featureList,
								usertestpath,
								PublicUtils.getFileName("test",
										pcb.getPerson_id(), "feature"));

				// 评分
				double score = FeatureUtils.KaldiPLDAscore2(
						pcb.getIvector_path(), testivectorPath,
						Constant.PCMTOOLPATH);
				if (score > Constant.PLDA_THRESHOLD) {
					result = 0;
				} else {
					result = 1;
				}
				System.out.println(pcb.getPerson_id() + ":" + score);

			} else {
				statues_code += 2;
			}
		}

		JSONObject jo = new JSONObject();
		jo.put("response_num", response_num);
		jo.put("statues_code", statues_code);
		jo.put("result", result);
		outNet.println(jo.toString());
		outNet.close();
	}

	/**
	 * 
	 * @param item
	 * @param user_id
	 * @param uploadpath
	 * @param filetype
	 * @return
	 */
	private File processUploadedFile(FileItem item, String user_id,
			String uploadpath, String filetype) {

		String filename = item.getName();

		try {
			long fileSize = item.getSize();

			if (filename.equals("") && fileSize == 0) {
				System.out.println("File upload failed!");
				return null;
			}

			String[] split = filename.split("\\.");
			String filetype1 = split[split.length - 1];
			if (!filetype1.equals("pcm")) {
				System.out.println("Not a PCM file!");
				return null;
			}

			PublicUtils.mkDir(uploadpath);
			File uploadedFile = new File(uploadpath,
					PublicUtils.getFileNameWithUUID("test", user_id, filetype));
			item.write(uploadedFile);
			System.out.println("PCM_path=" + uploadedFile.getAbsolutePath());

			return uploadedFile;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * @param register_ivecter_dir
	 *            注册的特征文件的路径
	 * @param test_ivector_dir
	 *            验证的wav的文件路径
	 * @return 评分
	 */
	public double KaldiTest(String register_ivecter_dir, String test_ivector_dir) {

		double result = -999;

		switch (Constant.SCORE_MODE) {
		case PLDA:
			result = FeatureUtils.KaldiPLDAscore(register_ivecter_dir,
					test_ivector_dir, Constant.TOOLPATH);
			break;

		case DOT:
			result = FeatureUtils.Kaldiscore(register_ivecter_dir,
					test_ivector_dir, Constant.TOOLPATH);
			break;
		}
		System.out.println("result=" + result);
		return result;
	}
}
