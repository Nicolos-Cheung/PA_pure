package com.pingan.dao;

import com.pingan.domain.PCMRequestBean;

public interface PCMdao {
	
	boolean register(PCMRequestBean pcb);
	
	PCMRequestBean Query(String person_id);
	
	void update(PCMRequestBean pcb);
	
	void remove(String person_id);
	
}
