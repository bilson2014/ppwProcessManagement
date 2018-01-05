package com.paipianwang.activiti.domin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 报价单模板下拉实体
 */
public class QuotationTemplateSelectVO implements Serializable{
	private static final long serialVersionUID = -5771807445148093439L;

	/** 隐藏值 **/
	private String id;

	/** 显示值**/
	private String name;
	
	/** 子类**/
	private List<QuotationTemplateSelectVO> children=new ArrayList<>();
	
	

	public QuotationTemplateSelectVO(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<QuotationTemplateSelectVO> getChildren() {
		return children;
	}

	public void setChildren(List<QuotationTemplateSelectVO> children) {
		this.children = children;
	}
	
	
	
}
