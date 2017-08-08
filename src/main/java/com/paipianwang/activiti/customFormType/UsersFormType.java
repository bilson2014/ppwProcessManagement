package com.paipianwang.activiti.customFormType;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.form.AbstractFormType;
import org.apache.commons.lang.StringUtils;

import com.paipianwang.activiti.utils.DataUtils;

public class UsersFormType extends AbstractFormType{

	private static final long serialVersionUID = -1300543004070759614L;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "users";
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		String[] split = StringUtils.split(propertyValue, ",");
		// 把字符串的值转换为List集合对象
		return Arrays.asList(split); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		// TODO Auto-generated method stub
		if(modelValue != null) {
			List<String> list = (List<String>) modelValue;
			return DataUtils.listToString(list, ',');
		}
		return null;
	}

}
