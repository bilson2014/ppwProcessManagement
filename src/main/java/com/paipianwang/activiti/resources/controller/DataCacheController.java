package com.paipianwang.activiti.resources.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.dao.DataCacheDao;
import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.DataCacheEntity;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.BitUtils;
import com.paipianwang.pat.common.util.DataGZIUtils;
import com.paipianwang.pat.common.util.ValidateUtil;

@RestController
@RequestMapping("/cache")
public class DataCacheController extends BaseController {
	
	@Autowired
	private DataCacheDao dataCacheDao;

	@RequestMapping("/save")
	public PmsResult saveCacheData(@RequestBody final DataCacheEntity data, final HttpServletRequest request,
			final HttpServletResponse response) {
		PmsResult result = new PmsResult();
		result.setResult(false);

		HttpSession session = request.getSession();
		final SessionInfo info = (SessionInfo) session.getAttribute(PmsConstant.SESSION_INFO);
		if (info != null) {
			if (data.getType() != null && ValidateUtil.isValid(data.getDataContent())) {
				String key = session.getId() + PmsConstant.CACHE_KEYNAME ;
				String value = data.getDataContent();

				//TODO 如果字节数超过400，进行压缩；否则头部加tiny
				try {
					dataCacheDao.setCacheData(key,data.getType()+"", DataGZIUtils.compress(value));
					result.setResult(true);
					
					Integer cacheTab=info.getCacheTab()!=null ? info.getCacheTab():0;
					info.setCacheTab( BitUtils.setBit(cacheTab, data.getType()));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				result.setMsg("数据格式不正确");
			}
		}else {
			result.setMsg("请登录");
		}

		return result;
	}
	
	@RequestMapping("/get")
	public PmsResult getCacheData(@RequestBody final DataCacheEntity data, final HttpServletRequest request,
			final HttpServletResponse response) {
		PmsResult result = new PmsResult();		

		HttpSession session = request.getSession();
		final SessionInfo info = (SessionInfo) session.getAttribute(PmsConstant.SESSION_INFO);
		if (info != null) {
			if (data.getType() != null) {
				//当前type位为1--有对应缓存数据
				if(info.getCacheTab()!=null && BitUtils.getBit(info.getCacheTab(), data.getType())) {
					String key = session.getId() + PmsConstant.CACHE_KEYNAME;
					try {
						String value=dataCacheDao.getCacheData(key,data.getType()+"");
						//TODO 如果头部有tiny，表示未压缩，直接返回
						result.setMsg(DataGZIUtils.unCompress(value));
					} catch (IOException e) {
						result.setResult(false);
						e.printStackTrace();
					}
				}	
			}else {
				result.setResult(false);
				result.setMsg("数据格式不正确");
			}
		}else {
			result.setResult(false);
			result.setMsg("请登录");
		}

		return result;
	}
}
