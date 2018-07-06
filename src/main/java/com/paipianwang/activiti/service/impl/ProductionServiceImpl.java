package com.paipianwang.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.service.ProductionService;
import com.paipianwang.pat.common.entity.BaseProductionEntity;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.workflow.entity.PmsProductionActor;
import com.paipianwang.pat.workflow.entity.PmsProductionCameraman;
import com.paipianwang.pat.workflow.entity.PmsProductionCostume;
import com.paipianwang.pat.workflow.entity.PmsProductionDevice;
import com.paipianwang.pat.workflow.entity.PmsProductionDirector;
import com.paipianwang.pat.workflow.entity.PmsProductionInfo;
import com.paipianwang.pat.workflow.entity.PmsProductionPersonnel;
import com.paipianwang.pat.workflow.entity.PmsProductionStudio;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.enums.ProductionResource;
import com.paipianwang.pat.workflow.facade.PmsProductionActorFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionCameramanFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionCostumeFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionDeviceFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionDirectorFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionInfoFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionPersonnelFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionStudioFacade;
import com.paipianwang.pat.workflow.facade.PmsProjectFlowFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationTypeFacade;

@Service
public class ProductionServiceImpl implements ProductionService {

	@Autowired
	private PmsQuotationTypeFacade pmsQuotationTypeFacade;
	@Autowired
	private PmsProductionInfoFacade pmsProductionInfoFacade;
	@Autowired
	private PmsProjectFlowFacade pmsProjectFlowFacade;
	@Autowired
	private PmsProductionActorFacade pmsProductionActorFacade;
	@Autowired
	private PmsProductionDeviceFacade pmsProductionDeviceFacade;
	@Autowired
	private PmsProductionDirectorFacade pmsProductionDirectorFacade;
	@Autowired
	private PmsProductionStudioFacade pmsProductionStudioFacade;
	@Autowired
	private PmsProductionCameramanFacade pmsProductionCameramanFacade;
	@Autowired
	private PmsProductionPersonnelFacade pmsProductionPersonnelFacade;
	@Autowired
	private PmsProductionCostumeFacade pmsProductionCostumeFacade;
	

	private void editQuotationTypeName(PmsProductionInfo info) {
		if (!ValidateUtil.isValid(info.getResources())) {
			return;
		}
		List<PmsQuotationType> types = pmsQuotationTypeFacade.findAll();
		for (PmsQuotationType type : types) {
			if(type.getParentId()==null) {
				continue;
			}
			for (PmsQuotationType parent : types) {
				if (type.getParentId().equals(parent.getTypeId())) {
					type.setParent(parent);
					break;
				}
			}
		}
		// 相同类型集合
		for (PmsProductionInfo.ProductionResource resource : info.getResources()) {
			for (PmsQuotationType type : types) {
				if (type.getTypeId().equals(resource.getTypeId())) {

					resource.setTypeName(type.getTypeName());
					resource.setCategory(type.getParent().getParent().getTypeName());
					resource.setCategoryId(type.getParent().getParent().getTypeId());
					resource.setSubType(type.getParent().getTypeName());
					resource.setSubTypeId(type.getParentId());

					/*
					 * if(ValidateUtil.isValid(resource.getName())) {
					 * resource.setName(resource.getName()+"/"+type.getTypeName());//TODO
					 * 去掉，不处理了，自己组装吧 }else {
					 
					resource.setName(type.getTypeName());
					// }
*/
					if (ProductionResource.device.getKey().equals(resource.getType())) {
						resource.setMainPhoto(type.getPhoto());
					}

					break;
				}
			}
		}
	}

	@Override
	public PmsProductionInfo getByProjectId(String projectId) {
		PmsProductionInfo info = pmsProductionInfoFacade.getByProjectId(projectId);

		if (info != null && ValidateUtil.isValid(info.getResources())) {
			editQuotationTypeName(info);
		}
		
		for (PmsProductionInfo.ProductionResource resource : info.getResources()) {
			resource.setPicScale(ProductionResource.getEnum(resource.getType()).getPicScale());
		}
		return info;
	}

	@Override
	public PmsResult saveOrUpdate(PmsProductionInfo pmsProductionInfo) {
		PmsResult result = new PmsResult();
		// 数据保存
		if (pmsProductionInfo.getId() != null) {
			result = pmsProductionInfoFacade.update(pmsProductionInfo);
		} else {
			PmsProductionInfo old = pmsProductionInfoFacade.getByProjectId(pmsProductionInfo.getProjectId());
			if (old != null) {
				// 覆盖
				pmsProductionInfo.setId(old.getId());
				result = pmsProductionInfoFacade.update(pmsProductionInfo);
			} else {
				List<String> metaData = new ArrayList<>();
				metaData.add("projectName");
				Map<String, Object> projectName = pmsProjectFlowFacade.getProjectFlowColumnByProjectId(metaData,
						pmsProductionInfo.getProjectId());
				if (ValidateUtil.isValid(projectName)) {
					pmsProductionInfo.setProjectName(projectName.get("projectName") + "");
				}

				result = pmsProductionInfoFacade.insert(pmsProductionInfo);
			}
		}
		return result;
	}

	@Override
	public PmsProductionInfo listResourceByParam(Map<String, Object> paramMap, String type) {
		PmsProductionInfo info=new PmsProductionInfo();
		info.setResources(new ArrayList<>());
		Integer picScale=null;
		
		//根据类型查询数据
		List<BaseProductionEntity> entities=new ArrayList<>();
		if(ProductionResource.actor.getKey().equals(type)) {
			List<PmsProductionActor> actors=pmsProductionActorFacade.listBy(paramMap);
			if(actors!=null) {
				entities.addAll(actors);
			}
			picScale=ProductionResource.actor.getPicScale();
			
		}else if(ProductionResource.director.getKey().equals(type)) {
			List<PmsProductionDirector> directors=pmsProductionDirectorFacade.listBy(paramMap);
			if(directors!=null) {
				entities.addAll(directors);
			}
			picScale=ProductionResource.director.getPicScale();
			
		}else if(ProductionResource.device.getKey().equals(type)) {
			List<PmsProductionDevice> devices=pmsProductionDeviceFacade.listBy(paramMap);
			if(devices!=null) {
				entities.addAll(devices);
			}
			picScale=ProductionResource.device.getPicScale();
		}else if(ProductionResource.studio.getKey().equals(type)) {
			List<PmsProductionStudio> studios=pmsProductionStudioFacade.listBy(paramMap);
			if(studios!=null) {
				entities.addAll(studios);
			}		
			picScale=ProductionResource.studio.getPicScale();
		}else if(ProductionResource.cameraman.getKey().equals(type)) {
			List<PmsProductionCameraman> cameramans=pmsProductionCameramanFacade.listBy(paramMap);
			if(cameramans!=null) {
				entities.addAll(cameramans);
			}		
			picScale=ProductionResource.cameraman.getPicScale();
		}else if(ProductionResource.lighter.getKey().equals(type) || ProductionResource.editor.getKey().equals(type)
				||ProductionResource.packer.getKey().equals(type) ||ProductionResource.colorist.getKey().equals(type)
				||ProductionResource.propMaster.getKey().equals(type) ||ProductionResource.artist.getKey().equals(type)
				||ProductionResource.costumer.getKey().equals(type) ||ProductionResource.dresser.getKey().equals(type)
				||ProductionResource.mixer.getKey().equals(type)) {
			paramMap.put("profession",type);
			List<PmsProductionPersonnel> personnnels=pmsProductionPersonnelFacade.listBy(paramMap);
			if(personnnels!=null) {
				entities.addAll(personnnels);
			}		
			picScale=ProductionResource.lighter.getPicScale();
		}else if(ProductionResource.clothing.getKey().equals(type) || ProductionResource.props.getKey().equals(type)) {
			paramMap.put("profession",type);
			List<PmsProductionCostume> costumes=pmsProductionCostumeFacade.listBy(paramMap);
			if(costumes!=null) {
				entities.addAll(costumes);
			}		
			picScale=ProductionResource.clothing.getPicScale();
		}
		
		//组装数据
		for(BaseProductionEntity entity:entities) {

			PmsProductionInfo.ProductionResource resource=new PmsProductionInfo.ProductionResource();
			resource.setId(entity.getId());
			resource.setType(type);
			resource.setPrice(entity.getPrice());
			resource.setName(entity.getName());
			resource.setPicScale(picScale);
			
			// 主图处理
			if(ValidateUtil.isValid(entity.getPhoto())) {
				String[] photos = entity.getPhoto().split(";");
				if (ValidateUtil.isValid(photos)) {
					resource.setMainPhoto(photos[0]);
				}
			}
			
			resource.setTypeId(entity.getTypeId());
			info.getResources().add(resource);
		
		}
		//处理类型名称
		editQuotationTypeName(info);
		
		return info;
	}
}
