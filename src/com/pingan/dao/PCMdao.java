package com.pingan.dao;

import com.pingan.domain.PCMRequestBean;

public interface PCMdao {
	
	boolean register(PCMRequestBean pcb);
	
	PCMRequestBean Query(String person_id);
	
	void update(PCMRequestBean pcb);
	
	void remoove(String person_id);
	
}
