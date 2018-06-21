package com.paipianwang.activiti.resources.model;

import com.paipianwang.pat.common.entity.BaseEntity;

public class PhotoCutParam extends BaseEntity {

	private static final long serialVersionUID = 1155350380579334858L;

	private long userId = 0l; // 用户ID
	
	private String imgUrl = null; // 用户头像地址
	
	private String imgFileName = null; // 用户头像文件名
	
	private int x = 0;
	
	private int y = 0;
	
	private int x2 = 0;
	
	private int y2 = 0;
	
	private int width = 0;
	
	private int height = 0;
	
	private int originalWidth = 0;
	
	private int originalHeight = 0;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getOriginalWidth() {
		return originalWidth;
	}

	public void setOriginalWidth(int originalWidth) {
		this.originalWidth = originalWidth;
	}

	public int getOriginalHeight() {
		return originalHeight;
	}

	public void setOriginalHeight(int originalHeight) {
		this.originalHeight = originalHeight;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	
}
