package com.pingan.service.impl;

import com.pingan.dao.impl.PCMdaoImpl;
import com.pingan.domain.PCMRequestBean;
import com.pingan.service.PCMSerivce;

public class PCMSerivceImpl implements PCMSerivce {
	private PCMdaoImpl dao = new PCMdaoImpl();

	/**
	 * 进行用户注册
	 */
	@Override
	public boolean register(PCMRequestBean pcb) {

		return dao.register(pcb);
	}

	/**
	 * 查询用户Ivector路径
	 */
	@Override
	public String QueryIvectorPath(String person_id) {
		PCMRequestBean pcb = dao.Query(person_id);
		return pcb.getIvector_path();
	}

	/**
	 * 查询用户Ivector版本
	 */
	@Override
	public String QueryIvectorVersion(String person_id) {
		PCMRequestBean pcb = dao.Query(person_id);
		return pcb.getIvector_version();
	}

	/**
	 * 查询用户的注册音频文件路径
	 */
	@Override
	public String QueryRegisterVoicePath(String person_id) {
		PCMRequestBean pcb = dao.Query(person_id);
		return pcb.getRegister_voice_path();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.pingan.service.PCMSerivce#update(com.pingan.domain.PCMRequestBean)
	 *      返回boolean false: 说明用户不可更新，可能原因有：对象信息不完整，或者原对象已被注销。 true: 用户封更新成功
	 */
	@Override
	public boolean update(PCMRequestBean updatepcb) {

		PCMRequestBean pcb = dao.Query(updatepcb.getPerson_id());

		if (null == pcb.getAvailable() || "0".equals(pcb.getAvailable())) {
			return false;
		} else {
			dao.update(updatepcb);
			return true;
		}

	}

	/**
	 * 返回注册对象实例
	 */
	@Override
	public PCMRequestBean Query(String person_id) {
		return dao.Query(person_id);
	}

	/**
	 * 查询person_id是否为空
	 */
	@Override
	public boolean IsUserExist(String person_id) {

		PCMRequestBean pcb = dao.Query(person_id);
		if (pcb.isExist()) {
			return true;
		}
		return false;
	}

	/**
	 * 查询对象是否可用，即person_id是否为空,该用户是否已注销
	 */
	@Override
	public boolean isAvailable(String person_id) {

		PCMRequestBean query = dao.Query(person_id);

		if (query != null && query.getAvailable() != null
				&& query.getAvailable().equals("1")) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 返回用户文件夹根目录
	 */
	@Override
	public String getRegisterRootPath(String person_id) {

		String user_root_path = dao.Query(person_id).getUser_root_path();
		return user_root_path;
	}

}
