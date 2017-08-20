package com.paipianwang.activiti.resources.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.domin.BaseMsg;
import com.paipianwang.activiti.resources.model.ChanpinSelection;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.enums.UserLevel;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.product.entity.PmsChanPin;
import com.paipianwang.pat.facade.product.entity.PmsChanPinConfiguration;
import com.paipianwang.pat.facade.product.entity.PmsChanPinConfiguration_ProductModule;
import com.paipianwang.pat.facade.product.entity.PmsDimension;
import com.paipianwang.pat.facade.product.entity.PmsProductModule;
import com.paipianwang.pat.facade.product.service.PmsChanPinConfigurationFacade;
import com.paipianwang.pat.facade.product.service.PmsChanPinFacade;

@RestController
public class ChanPinController {
	
	@Autowired
	private PmsChanPinFacade pmsChanPinFacade;
	@Autowired
	private PmsChanPinConfigurationFacade pmsChanPinConfigurationFacade;
	
	/**
	 * 产品线价格计算
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/product/compute")
	public BaseMsg compute(String json) throws Exception {
		BaseMsg baseMsg = new BaseMsg();
		if (ValidateUtil.isValid(json)) {
			List<String> vv = JsonUtil.fromJsonArray(json, String.class);
			LinkedList<String> value = new LinkedList<>(vv);
			if (ValidateUtil.isValid(value) && value.size() % 2 != 0) {
				BigDecimal A = null;
				BigDecimal B = null;
				while (!value.isEmpty()) {
					String key = value.poll();
					switch (key) {
					case "+":
						B = new BigDecimal(value.poll());
						A = A.add(B);
						break;
					case "-":
						B = new BigDecimal(value.poll());
						A = A.subtract(B);
						break;
					case "*":
						B = new BigDecimal(value.poll());
						A = A.multiply(B);
						break;
					default:
						A = new BigDecimal(key);
						break;
					}
				}
				baseMsg.setCode(BaseMsg.NORMAL);
				baseMsg.setResult(A.doubleValue());
			}
		}
		return baseMsg;
	}
	/**
	 * 获取产品线下拉列表值-产品、配置、可选包、时长维度---初始加载
	 * @return
	 */
	@RequestMapping("/product/productSelection")
	public BaseMsg getChanPinSelection() {
		
		Map<String,Object> result=new HashMap<>();
		//产品
		DataGrid<PmsChanPin> allScene = pmsChanPinFacade.getAllChanPin();
		List<ChanpinSelection> chanpin=new ArrayList<>();
		for(PmsChanPin pmsChanpin:allScene.getRows()){
			chanpin.add(new ChanpinSelection(pmsChanpin.getChanpinId()+"", pmsChanpin.getChanpinName()));
		}
		chanpin.add(new ChanpinSelection("-1","自主"));
		result.put("chanpin", chanpin);
		//产品配置-第一条产品下
//		setConfigByChanpinAndConfig(result,allScene.getRows().get(0).getChanpinId(),null);
		
		//暂时
		//项目来源
		List<Map<String,Object>> emlist=new ArrayList<Map<String,Object>>();
		for(IndentSource source:IndentSource.values()){
			Map<String,Object> map=new HashMap<>();
			map.put("id", source.getValue());
			map.put("text",source.getName());
			emlist.add(map);
		}
		result.put("resource", emlist);
		//客户评级
		List<Map<String,Object>> clientLevel=new ArrayList<Map<String,Object>>();
		for(UserLevel level:UserLevel.values()){
			Map<String,Object> map=new HashMap<>();
			map.put("id", level.getId());
			map.put("text",level.getText());
			clientLevel.add(map);
		}
		result.put("clientLevel", clientLevel);		
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(result);
		return baseMsg;
	}
	
	/**
	 * 根据产品获取其下配置详情-配置、可选包、时长维度
	 * @param chanpinId
	 * @return
	 */
	@RequestMapping("/product/ConfigSelection/{chanpinId}")
	public BaseMsg getConfigSelection(@PathVariable("chanpinId") Long chanpinId) {
		Map<String,Object> result=new HashMap<>();
		//产品配置
		setConfigByChanpinAndConfig(result,chanpinId,null);
		
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(result);
		return baseMsg;
	}
	/**
	 * 根据产品配置取其下配置详情-可选包、时长维度
	 * @param configId
	 * @return
	 */
	@RequestMapping("/product/detailSelection/{configId}")
	public BaseMsg getConfigItemSelection(@PathVariable("configId") Long configId) {
		Map<String,Object> result=new HashMap<>();
		//产品配置
		setConfigByChanpinAndConfig(result,null,configId);
		
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(result);
		return baseMsg;
	}
	
	
	private void setConfigByChanpinAndConfig(Map<String,Object> result,Long chanpinId,Long configId){
		if(chanpinId!=null){
			List<ChanpinSelection> chanpinConfig=new ArrayList<>();
			result.put("config",chanpinConfig);
			List<PmsChanPinConfiguration> allConfig = pmsChanPinConfigurationFacade.getChanPinConfigurationByChanPinId(chanpinId);		
			for(PmsChanPinConfiguration config:allConfig){
				chanpinConfig.add(new ChanpinSelection(config.getChanpinconfigurationId()+"", config.getChanpinconfigurationName(),"",config.getBasePrice()));	
			}
//			configId=allConfig.get(0).getChanpinconfigurationId();//第一条产品配置
			return ;
			
		}
			
		//产品配置项
		PmsChanPinConfiguration defConfig=pmsChanPinConfigurationFacade.getChanPinConfigurationInfo(configId);
		result.put("basePrice", defConfig.computePrice());
		//配置附加包
		List<ChanpinSelection> modules=new ArrayList<>();
		result.put("modules",modules);
		List<PmsProductModule> productModules = defConfig.getPmsProductModule();
		if (ValidateUtil.isValid(productModules)) {
			for (PmsProductModule pmsProductModule : productModules) {
				PmsChanPinConfiguration_ProductModule p = pmsProductModule.getPinConfiguration_ProductModule();
				if (p != null && p.getCpmModuleType().equals(1)) {
					modules.add(new ChanpinSelection(pmsProductModule.getProductModuleId()+"", pmsProductModule.getModuleName(),"+",p.getCpmModulePrice()));
				}
			}
		}
		//配置时长
		List<ChanpinSelection> dimension=new ArrayList<>();
		result.put("dimension",dimension);
		List<PmsDimension> pmsDimensions=defConfig.getPmsDimensions();
		if(ValidateUtil.isValid(pmsDimensions)){
			for(PmsDimension pmsDimension:pmsDimensions){
				String type="+";
				if(pmsDimension.getComputeType()==0){
					type="*";
				}else if(pmsDimension.getComputeType()==1){
					type="+";
				}else if(pmsDimension.getComputeType()==2){
					type="-";
				}
				dimension.add(new ChanpinSelection(pmsDimension.getDimensionId()+"", pmsDimension.getRowName(),type,pmsDimension.getRowValue()));
			}
		}
	}
}
