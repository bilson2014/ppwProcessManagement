package com.paipianwang.activiti.resources.model;

import java.io.Serializable;

public class ChanpinSelection implements Serializable{

	private static final long serialVersionUID = -6209756345596410312L;
	
	private String id;
	private String text;//显示值
	private String type;//计算维度
	private Double price=0.0;//价格
	
	
	public ChanpinSelection(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	
	public ChanpinSelection(String id, String text, String type, Double price) {
		super();
		this.id = id;
		this.text = text;
		this.type = type;
		this.price = price;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

}
