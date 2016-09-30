package com.pingan.dao.impl;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.pingan.constant.Constant;
import com.pingan.dao.PCMdao;
import com.pingan.domain.PCMRequestBean;

public class PCMdaoImpl implements PCMdao {

	public String mongodb_ip = Constant.MONGODB_IP;
	public int mongodb_port = Constant.MONGODB_PORT;
	public String DB_name = Constant.MONGODB_DB_NAME;
	public String Collection_name = "pcm_users";

	@Override
	public boolean register(PCMRequestBean pcb) {
		Mongo mongo = null;
		try {
			mongo = new Mongo(mongodb_ip, mongodb_port);
			DB db = mongo.getDB(DB_name);
			DBCollection collection = db.getCollection(Collection_name);
			DBObject dbObject = new BasicDBObject();
			dbObject.put("user_id", pcb.getUser_id());
			dbObject.put("person_id", pcb.getPerson_id());
			dbObject.put("telnum", pcb.getTelnum());
			dbObject.put("source", pcb.getSource());
			dbObject.put("policy_number", pcb.getPolicy_number());
			dbObject.put("nas_dir", pcb.getNas_dir());
			dbObject.put("ivector_version", pcb.getIvector_version());
			dbObject.put("register_voice_path", pcb.getRegister_voice_path());
			dbObject.put("register_date", pcb.getRegister_date());
			dbObject.put("ivector_path", pcb.getIvector_path());
			dbObject.put("available", pcb.getAvailable());
			dbObject.put("user_root_path", pcb.getUser_root_path());
			collection.insert(dbObject);
			return true;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
		return false;
	}

	@Override
	public PCMRequestBean Query(String person_id) {

		Mongo mongo = null;
		PCMRequestBean pcb = null;
		try {
			mongo = new Mongo(mongodb_ip, mongodb_port);
			DB db = mongo.getDB(DB_name);
			DBCollection collection = db.getCollection(Collection_name);

			DBObject searchQuery = new BasicDBObject();
			searchQuery.put("person_id", person_id);
			DBCursor dbCursor = collection.find(searchQuery); // dbCursor
																// 结果集(ResultSet)
			pcb = new PCMRequestBean();
			while (dbCursor.hasNext()) {
				DBObject ob = dbCursor.next();
				if (ob.get("user_id") != null) {
					pcb.setUser_id(ob.get("user_id").toString().trim());
				}
				if (ob.get("person_id") != null) {
					pcb.setPerson_id(ob.get("person_id").toString().trim());
				}
				if (ob.get("telnum") != null) {
					pcb.setTelnum(ob.get("telnum").toString().trim());
				}
				if (ob.get("source") != null) {
					pcb.setSource(ob.get("source").toString().trim());
				}
				if (ob.get("policy_number") != null) {
					pcb.setPolicy_number(ob.get("policy_number").toString()
							.trim());
				}
				if (ob.get("nas_dir") != null) {
					pcb.setNas_dir(ob.get("nas_dir").toString().trim());
				}

				if (ob.get("ivector_version") != null) {
					pcb.setIvector_version(ob.get("ivector_version").toString()
							.trim());
				}
				if (ob.get("register_voice_path") != null) {
					pcb.setRegister_voice_path(ob.get("register_voice_path")
							.toString().trim());
				}

				if (ob.get("register_date") != null) {
					pcb.setRegister_date(ob.get("register_date").toString()
							.trim());
				}
				if (ob.get("ivector_path") != null) {
					pcb.setIvector_path(ob.get("ivector_path").toString()
							.trim());
				}
				if (ob.get("available") != null) {
					pcb.setAvailable(ob.get("available").toString().trim());
				}
				if (ob.get("user_root_path") != null) {

					pcb.setUser_root_path(ob.get("user_root_path").toString()
							.trim());
				}

			}
			return pcb;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
		return null;
	}

	@Override
	public void update(PCMRequestBean pcb) {
		Mongo mongo = null;
		try {
			mongo = new Mongo(mongodb_ip, mongodb_port);
			DB db = mongo.getDB(DB_name);
			DBObject queryObject = new BasicDBObject();
			queryObject.put("person_id", pcb.getUser_id());
			DBCollection collection = db.getCollection(Collection_name);
			DBObject dbObject = new BasicDBObject();
			if (isNotNull(pcb.getUser_id())) {
				dbObject.put("user_id", pcb.getUser_id());
			}
			if (isNotNull(pcb.getPerson_id())) {
				dbObject.put("person_id", pcb.getPerson_id());
			}
			if (isNotNull(pcb.getTelnum())) {
				dbObject.put("telnum", pcb.getTelnum());
			}
			if (isNotNull(pcb.getSource())) {
				dbObject.put("source", pcb.getSource());
			}
			if (isNotNull(pcb.getPolicy_number())) {
				dbObject.put("policy_number", pcb.getPolicy_number());
			}
			if (isNotNull(pcb.getNas_dir())) {
				dbObject.put("nas_dir", pcb.getNas_dir());
			}
			if (isNotNull(pcb.getIvector_version())) {
				dbObject.put("ivector_version", pcb.getIvector_version());
			}
			if (isNotNull(pcb.getRegister_voice_path())) {
				dbObject.put("register_voice_path",
						pcb.getRegister_voice_path());
			}
			if (isNotNull(pcb.getRegister_date())) {
				dbObject.put("register_date", pcb.getRegister_date());
			}
			if (isNotNull(pcb.getIvector_path())) {
				dbObject.put("ivector_path", pcb.getIvector_path());
			}
			if (isNotNull(pcb.getAvailable())) {
				dbObject.put("available", pcb.getAvailable());
			}

			collection.update(queryObject, dbObject);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	@Override
	public void remoove(String person_id) {
		Mongo mongo = null;
		try {
			mongo = new Mongo(mongodb_ip, mongodb_port);
			DB db = mongo.getDB(DB_name);
			DBCollection collection = db.getCollection(Collection_name);
			DBObject dbObject = new BasicDBObject();
			dbObject.put("person_id", person_id);
			collection.remove(dbObject);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	private boolean isNotNull(String str) {
		if (str == null || "".equals(str)) {
			return false;
		} else {
			return true;
		}
	}

}
