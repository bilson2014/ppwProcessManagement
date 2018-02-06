package com.paipianwang.activiti.poi.entity;

import java.util.ArrayList;
import java.util.List;

import com.paipianwang.pat.common.util.ValidateUtil;

public class QuotationTableEntity {

	private String title;//标题
	private String[] headItems;//表头
	private List<List<QuotationTableCell>> items=new ArrayList<>();//表格项
	
	private int colSize;//table列数
	private int greenCell;//0:偶数行 1:奇数行 -1:无
	private int type;//1-汇总 0-明细表 2-信息
	
	public QuotationTableEntity(){}
	
	public QuotationTableEntity(String title, String[] headItems, List<List<QuotationTableCell>> items,int greenCell,int type) {
		super();
		this.title = title;
		this.headItems = headItems;
		this.items = items;
		this.greenCell=greenCell;
		if(ValidateUtil.isValid(headItems)){
			colSize=headItems.length;
		}else{
			colSize=items.get(0).size();
		}
		this.type=type;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getHeadItems() {
		return headItems;
	}
	public void setHeadItems(String[] headItems) {
		this.headItems = headItems;
	}
	public List<List<QuotationTableCell>> getItems() {
		return items;
	}
	public void setItems(List<List<QuotationTableCell>> items) {
		this.items = items;
	}

	public int getColSize() {
		return colSize;
	}

	public void setColSize(int colSize) {
		this.colSize = colSize;
	}

	public int getGreenCell() {
		return greenCell;
	}

	public void setGreenCell(int greenCell) {
		this.greenCell = greenCell;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
