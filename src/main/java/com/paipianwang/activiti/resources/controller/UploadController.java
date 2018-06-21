package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.paipianwang.activiti.domin.BaseMsg;
import com.paipianwang.activiti.resources.model.PhotoCutParam;
import com.paipianwang.activiti.utils.PhotoUtil;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.FileUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;


/**
 *文件上传
 */
@RestController
public class UploadController extends BaseController{
	
	@RequestMapping("/web/upload")
	public BaseMsg getAllProvince(HttpServletRequest request,MultipartFile file,String oldUrl) {
		// 检测视频是否大于限制
		final BaseMsg msg = checkFile(file,1024l);
		if (0 == msg.getCode()) {
			String fileId = FastDFSClient.uploadFile(file);
			//删除旧地址
			if(StringUtils.isNotBlank(oldUrl)){
				FastDFSClient.deleteFile(oldUrl);
			}
			if(ValidateUtil.isValid(fileId)) {
				msg.setResult(fileId);
			}else {
				msg.setCode(4);
			}
			
		} 
		return msg;
	}
	
	public BaseMsg checkFile(final MultipartFile file,Long img_MaxSize) {
		if (file != null && !file.isEmpty()) {
			if(img_MaxSize==null) {
				img_MaxSize = Long.parseLong(PublicConfig.PRODUCT_IMAGE_MAX_SIZE);
			}
			
			final long video_MaxSize = Long.parseLong(PublicConfig.VIDEO_MAX_SIZE);

			// 检测文件类型
			final String extName = FileUtils.getExtName(file.getOriginalFilename(), ".");
			final short fileType = FileUtils.divideIntoGroup(extName, PublicConfig.ALLOW_IMAGE_TYPE, PublicConfig.ALLOW_VIDEO_TYPE);
			final long fileSize = file.getSize();
			switch (fileType) {
			case 0: // video
				// 检查视频大小
				if (fileSize > video_MaxSize * 1024 * 1024) {
					// 视频文件超过500M上限
					return new BaseMsg(1, "视频文件超过500M上限");
				}
				break;
			case 1: // image
				// 检查图片大小
				if (fileSize > img_MaxSize * 1024) {
					// 图片文件超过250K上限
					return new BaseMsg(2, "图片文件超过上限");
				}
				break;
			case 2: // other
//				throw new RuntimeException("file type error ...");
				return new BaseMsg(4, "文件格式不正确");
			}
			return new BaseMsg(0, "齐活");
		}
		return new BaseMsg(3, "文件不存在");
	}
	
	@RequestMapping(value = "/web/multipUpload", method = RequestMethod.POST)
	public BaseMsg uploadFiles(final HttpServletRequest request, final HttpServletResponse response) {
		BaseMsg result=new BaseMsg();
		result.setResult("");
		
		MultipartHttpServletRequest multipartRquest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRquest.getFileMap();
		
		result.setCode(0);
		for (final Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			final MultipartFile file = entity.getValue();
			if(!file.isEmpty()) {
				result.setResult(result.getResult()+FastDFSClient.uploadFile(file)+";");
			}
		}
		return result;
	}
	@RequestMapping("/web/cutPhoto")
	public BaseMsg uploadDIYUserImg(@RequestBody final PhotoCutParam param, final HttpServletRequest request)
			throws IOException {
		BaseMsg result=new BaseMsg();
		if (param != null && !"".equals(param.getImgUrl())) {

			final String imgPath = param.getImgUrl();
			InputStream inputStream = FastDFSClient.downloadFile(imgPath);
			
			final String extName = FileUtils.getExtName(imgPath, ".");

			SessionInfo sessionInfo = getCurrentInfo(request);
//			Log.error(" cut photo begin", sessionInfo);

			// cut photo
			inputStream = PhotoUtil.cutPhoto(inputStream, param, extName);
//			Log.error(" cut photo - success", sessionInfo);

			String path = FastDFSClient.uploadFile(inputStream, imgPath);
			
			if(ValidateUtil.isValid(path)) {
				result.setCode(BaseMsg.NORMAL);
				result.setResult(path);
			}			
		}
		return result;
	}
	
	@RequestMapping("/web/delImg")
	public BaseMsg delImg(String path) {
		BaseMsg msg=new BaseMsg();
		msg.setCode(BaseMsg.ERROR);
		if(ValidateUtil.isValid(path)) {
			int result= FastDFSClient.deleteFile(path);
			if(result==0) {
				msg.setCode(BaseMsg.NORMAL);
			}
		}
		
		return msg;
	}
}