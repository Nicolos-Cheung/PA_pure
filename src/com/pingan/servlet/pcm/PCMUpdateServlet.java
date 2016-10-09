package com.pingan.servlet.pcm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 * 处理register请求的servlet
 * 
 * @author ning
 */
public class PCMUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private PCMSerivceImpl service;
	private File tempFile;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		initDir();
		service = new PCMSerivceImpl();
		tempFile = new File(Constant.TEMPPATH);
	}

	private void initDir() {
		PublicUtils.mkDir(Constant.PCMROOT);
		PublicUtils.mkDir(Constant.TEMPPATH);
		PublicUtils.mkDir(Constant.PCMTESTROOT);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("----------------Update...----------------");

		PCMRequestBean updatepcb = new PCMRequestBean();
		String userfilepath = "";
		File pcmfile = null;
		int statues_code = 0;
		boolean hasFile = false;
		response.setContentType("text/plain");
		// 向客户端发送响应正文
		PrintWriter outNet = response.getWriter();
		String response_num = "";

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(8 * 1024); // 设置缓冲区大小为8K
			factory.setRepository(tempFile); // 设置临时目录

			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");
			upload.setSizeMax(20 * 1024 * 1024); // 允许文件的最大上传尺寸20M

			try {

				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {

						// 表单非文件数据
						String name = item.getFieldName();
						if (name.equals("user_id")) {
							updatepcb.setUser_id(item.getString());
						}
						if (name.equals("person_id")) {
							updatepcb.setPerson_id(item.getString().trim());
						}
						if (name.equals("telnum")) {
							updatepcb.setTelnum(item.getString());
						}
						if (name.equals("source")) {
							updatepcb.setSource(item.getString());
						}
						if (name.equals("policy_number")) {
							updatepcb.setPolicy_number(item.getString());
						}
						if (name.equals("response_num")) {
							response_num = item.getString();
						}

						updatepcb.setRegister_date(PublicUtils.getDetailData());

					} else {

						System.out.println("update_person_id"
								+ updatepcb.getPerson_id());

						if (null == updatepcb.getPerson_id()
								|| "".equals(updatepcb.getPerson_id())
								|| !service.isAvailable(updatepcb
										.getPerson_id())) {

							statues_code += 4;

						} else {
							// 根据userid 和根目录 得到用户的文件目录
							userfilepath = service
									.getRegisterRootPath(updatepcb
											.getPerson_id());
							updatepcb.setUser_root_path(userfilepath);
							pcmfile = processUploadedFile(item,
									updatepcb.getPerson_id(), userfilepath,
									"pcm"); // 处理上传文件

							if (pcmfile == null) {
								hasFile = false;
								statues_code += 1;
							} else {
								hasFile = true;
								updatepcb.setRegister_voice_path(pcmfile
										.getAbsolutePath());
							}

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (hasFile) {
			// 重新进行注册
			if (statues_code <= 0) {
				List<String> featureList = FeatureUtils.KaldiToPcmIvecter(
						pcmfile.getAbsolutePath(), Constant.PCMTOOLPATH);

				if (null == featureList || featureList.size() == 0) {
					statues_code += 2;
				} else if (statues_code == 0) {

					String ivectorPath = FeatureUtils
							.ListToFile_Return_FilePath2(featureList,
									userfilepath,
									PublicUtils
											.getFileName("register",
													updatepcb.getPerson_id(),
													"feature"));

					updatepcb.setAvailable("1");
					updatepcb.setIvector_path(ivectorPath);
					updatepcb.setIvector_version(Constant.IVECTOR_VERSION);
					updatepcb.setRegister_date(PublicUtils.getDetailData());

					if (updatepcb.isAbleToRegister()) {
						boolean update = service.update(updatepcb);
						System.out.println("UpdatePCB:==>"
								+ updatepcb.toString());
						if(!update){
							statues_code += 16;
						}
						
					} else {
						statues_code += 8;
					}

				}
			}

		} else {
			if (statues_code == 0) {
				statues_code += 1;
			}

		}

		JSONObject json = new JSONObject();
		json.put("response_num", response_num);
		json.put("statues_code", statues_code); // 1上传失败 2ivector计算出错 4用户已注册

		System.out.println("Json:==>" + json.toString());

		outNet.print(json.toString());
		if (outNet != null) {
			outNet.close();
		}
		System.out.println("----------------Update complete!----------------");

	}

	/**
	 * 处理文件上传
	 * 
	 * @param item
	 * @param customer_id
	 * @param uploadpath
	 *            文件上传的目录
	 * @return 0成功 1上传非wav文件
	 */
	private File processUploadedFile(FileItem item, String user_id,
			String uploadpath, String filetype) {

		String filename = item.getName();

		try {
			long fileSize = item.getSize();

			if (filename.equals("") || fileSize == 0) {
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
			File uploadedFile = new File(uploadpath, PublicUtils.getFileName(
					"update", user_id, filetype));
			item.write(uploadedFile);

			return uploadedFile;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
