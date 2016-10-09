package com.pingan.factory;

import java.io.File;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFactory {
	
	public static ServletFileUpload getBaseUpLoad(File tempFile,long MaxSize){
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024); // 设置缓冲区大小为1M
		factory.setRepository(tempFile); // 设置临时目录
		
		// 创建一个文件上传处理器
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		upload.setHeaderEncoding("utf-8");
		upload.setSizeMax(20 * 1024 * 1024); // 允许文件的最大上传尺寸10M
		
		return upload;
	}

}
