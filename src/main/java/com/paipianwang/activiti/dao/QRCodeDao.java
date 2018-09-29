package com.paipianwang.activiti.dao;

public interface QRCodeDao {

	public void setData(String key, int seconds,String data);
	public String getData(String key);
	
	public void setExpire(String key,int expireSeconds);
	
	public Long deleteData(String key);
}
