package com.pingan.servlet.async.thread;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.AsyncContext;

import net.sf.json.JSONObject;

import com.pingan.constant.Constant;
import com.pingan.domain.PCMRequestBean;
import com.pingan.service.impl.PCMSerivceImpl;
import com.pingan.utils.FeatureUtils;
import com.pingan.utils.PublicUtils;

public class AsyncRegisterProcessor extends BaseAsyncProcessor implements
		Runnable {

	private AsyncContext asyncContext;
	private PCMRequestBean pcb;
	private String userfilepath;
	private boolean isCancel;
	PCMSerivceImpl service;
	private int statues_code;
	private String response_num;

	public AsyncRegisterProcessor() {

	}

	public AsyncRegisterProcessor(AsyncContext asyncCtx, PCMRequestBean pcb,
			String userfilepath, boolean isCancel, PCMSerivceImpl service,
			int statues_code, String response_num) {
		this.asyncContext = asyncCtx;
		this.pcb = pcb;
		this.userfilepath = userfilepath;
		this.isCancel = isCancel;
		this.service = service;
		this.statues_code = statues_code;
		this.response_num = response_num;
	}

	@Override
	public void run() {
		
		long start = System.currentTimeMillis();
		int statue_code = register(pcb, statues_code, userfilepath, isCancel,
				service);
		
		JSONObject json = new JSONObject();
		json.put("response_num", response_num);
		json.put("statues_code", statues_code);

		// PrintWriter out = asyncContext.getResponse().getWriter();
		// responsebyJson(response_num, statue_code, out);
		System.out.println(json.toString());
		
		long end = System.currentTimeMillis();
		System.out.println("register:"+(end-start)/1000+"s");

		// complete the processing
		// asyncContext.complete();

	}

	/**
	 * 
	 * @param pcb
	 *            注册的pcb对象
	 * @param statues_code
	 *            状态吗
	 * @param userfilepath
	 *            用户文件路径
	 * @param isCancel
	 *            用户是否注销
	 * @param service
	 * @return statues_code
	 */
	private int register(PCMRequestBean pcb, int statues_code,
			String userfilepath, boolean isCancel, PCMSerivceImpl service) {
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
		return statues_code;
	}
}
