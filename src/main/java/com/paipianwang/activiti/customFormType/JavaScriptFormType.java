package com.paipianwang.activiti.customFormType;

import org.activiti.engine.form.AbstractFormType;

public class JavaScriptFormType extends AbstractFormType {

	private static final long serialVersionUID = 7158260112667527491L;

	@Override
	public String getName() {
		return "javascript";
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return (String) modelValue;
	}

}
