package com.pingan.servlet.async.thread;

import java.io.File;
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

public class AsyncValidationProcessor extends BaseAsyncProcessor implements
		Runnable {
	private static PCMSerivceImpl service;
	private AsyncContext asc;
	private int statues_code;
	private PCMRequestBean pcb;
	private File testvoice;
	private String usertestpath;
	private String response_num;

	static {
		service = new PCMSerivceImpl();
	}
	public AsyncValidationProcessor(AsyncContext asc, int statues_code,
			PCMRequestBean pcb, File testvoice, String usertestpath,
			String response_num) {
		this.asc = asc;
		this.pcb = pcb;
		this.statues_code = statues_code;
		this.testvoice = testvoice;
		this.usertestpath = usertestpath;
		this.response_num = response_num;

	}

	@Override
	public void run() {

		long start = System.currentTimeMillis();

		// PrintWriter out = asyncContext.getResponse().getWriter();
		// responsebyJson(response_num, statue_code, out);
		int validation = validation(statues_code, pcb, testvoice, usertestpath);
		
		
		long end = System.currentTimeMillis();
		
		System.out.println("register:" + (end - start) / 1000 + "s");

		// complete the processing
		// asyncContext.complete();

	}

	private int  validation(int statues_code, PCMRequestBean pcb,
			File testvoice, String usertestpath
			) {
		double result = -999;
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
					// result = 0;
					result = (((score - (double) Constant.DOT_THRESHOLD) / (400 - (double) Constant.DOT_THRESHOLD)) * 10);

				} else {
					// result = 1;
					result = (((score - (double) Constant.DOT_THRESHOLD) / (double) Constant.DOT_THRESHOLD) * 10);
				}
				
				service.update(pcb);// 更新数据库 nas_dir
				
				
				
				

				System.out.println("score==>" + score);

			} else {
				statues_code += 2;
			}
		}
		return statues_code;

	}

}
