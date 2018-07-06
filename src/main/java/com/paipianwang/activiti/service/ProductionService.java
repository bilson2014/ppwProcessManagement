package com.paipianwang.activiti.service;

import java.util.Map;

import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.workflow.entity.PmsProductionInfo;

public interface ProductionService {

	PmsProductionInfo getByProjectId(String projectId);

	PmsResult saveOrUpdate(PmsProductionInfo pmsProductionInfo);

	PmsProductionInfo listResourceByParam(Map<String, Object> paramMap, String type);

}
