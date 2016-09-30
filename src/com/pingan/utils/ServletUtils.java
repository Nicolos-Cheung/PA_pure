package com.pingan.utils;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;

import com.pingan.domain.PCMRequestBean;

public class ServletUtils {

	public static <T> T fillBean(HttpServletRequest request, Class<T> clazz) {
		try {
			T bean = clazz.newInstance();
			BeanUtils.populate(bean, request.getParameterMap());
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param item
	 * @param token
	 *            //用于文件名的标识
	 * @param uploadpath
	 *            //文件存放路径
	 * @param filetype
	 *            //文件类型
	 * @return 0文件上传失败 1文件类型不匹配 正确则换返回文件路径
	 */
	public String processUploadedFile(FileItem item, String token,
			String usage, String uploadpath, String filetype) {

		String filename = item.getName();

		try {
			long fileSize = item.getSize();

			if (filename.equals("") && fileSize == 0) {
				String errinfo = "File upload failed!";
				System.out.println("File upload failed!");
				return "1_" + errinfo;
			}

			String[] split = filename.split("\\.");
			String filetype1 = split[split.length - 1];
			if (!filetype1.equals(filetype)) {
				String errinfo = "Not a " + filetype + " file!";
				System.out.println("Not a" + filetype + "file!");
				return "2_" + errinfo;
			}

			PublicUtils.mkDir(uploadpath);
			File uploadedFile = new File(uploadpath, PublicUtils.getFileName(
					usage, token, filetype));
			item.write(uploadedFile);
			System.out.println("file_path" + uploadedFile.getAbsolutePath());
			return uploadedFile.getAbsolutePath();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static PCMRequestBean paramToBean( List<FileItem> items,String ... args){
		
		 PCMRequestBean pcb = new PCMRequestBean();
		 for(FileItem item : items){
			
			for(int i=0;i<args.length;i++){
				
				if(item.getFieldName().equals(args[i])){
					
				}
				
				
				
			}
			
		}
		return pcb;
	}
}
