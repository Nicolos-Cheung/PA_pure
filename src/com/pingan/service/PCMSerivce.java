package com.pingan.service;

import com.pingan.domain.PCMRequestBean;

public interface PCMSerivce {

	/**
	 * 用户声纹注册
	 */
	boolean register(PCMRequestBean pcb);

	/**
	 * 查询用户注册信息
	 */
	PCMRequestBean Query(String person_id);

	/**
	 * 该用户是否注销
	 */
	boolean isAvailable(String person_id);

	boolean IsUserExist(String person_id);

	/**
	 * 查询特征文件路径
	 */
	String QueryIvectorPath(String person_id);

	/**
	 * 查询特征文件版本
	 */
	String QueryIvectorVersion(String person_id);

	/**
	 * 查找wav地址
	 */
	String QueryRegisterVoicePath(String person_id);

	/**
	 * 更新
	 * 
	 * @param iv
	 */
	boolean update(PCMRequestBean pcb);

	
	String getRegisterRootPath(String person_id);

}
