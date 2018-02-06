package com.paipianwang.activiti.poi.entity;

public class QuotationTableCell {

	private Object value;
	private int type;//1-货币 2-备注
	
	public QuotationTableCell() {}
	
	public QuotationTableCell(Object value) {
		super();
		this.value = value;
	}
	public QuotationTableCell(Object value, Integer type) {
		super();
		this.value = value;
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
