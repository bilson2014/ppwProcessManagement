package com.paipianwang.activiti.resources.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.resources.controller.BaseController;
import com.paipianwang.pat.facade.team.entity.PmsCity;
import com.paipianwang.pat.facade.team.service.PmsCityFacade;

@RestController
public class CityPickerController extends BaseController {

	@Autowired
	private PmsCityFacade pmsCityFacade;
	
	
	@RequestMapping("/all/citys")
	public List<PmsCity> getCitys() {
		return pmsCityFacade.getAll();
	}
}
