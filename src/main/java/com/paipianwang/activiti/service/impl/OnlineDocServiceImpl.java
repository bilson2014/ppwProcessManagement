package com.paipianwang.activiti.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.activiti.service.OnlineDocService;
import com.paipianwang.activiti.service.ProjectResourceService;
import com.paipianwang.activiti.utils.HttpUtil;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.FileUtils;
import com.paipianwang.pat.common.util.PathFormatUtils;
import com.paipianwang.pat.common.util.VerifyFileUtils;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsProjectResource;

@Service
public class OnlineDocServiceImpl implements OnlineDocService {
	@Autowired
	private ProjectResourceService projectResourceService;
	
	String pdf2html = Constants.PDF2HTML;
	static String CONVERSION_URL = PublicConfig.FILE_CONVERTION_SERVER + "/FileConversion/convert";

	public String convertFile(PmsProjectResource pmsProjectResource) {
		// modify by laowang 2016.5.17 12:10 begin
		// -->修改操作redis方法
//		indentResourceService.saveResourceState(indentResource, OnlineDocService.TRANSFORMATION);
		// modify by laowang 2016.5.17 12:10 end
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String fileName = pmsProjectResource.getResourcePath();
					String extName = FileUtils.getExtName(fileName, ".");
					boolean isDoc = VerifyFileUtils.verifyDocFile(extName);
					if (isDoc) {
						// 修改为从dfs上获取文件
						InputStream inputStream = FastDFSClient.downloadFile(fileName);
						File temp = new File(PublicConfig.DOC_TEMP_PATH,
								PathFormatUtils.parse("{rand:6}{time}." + extName));
						FileOutputStream fos = new FileOutputStream(temp);
						try {
							HttpUtil.saveTo(inputStream, fos);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 构建模拟表单
						MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
						multipartEntityBuilder.addBinaryBody("file", temp);
						InputStream stream = HttpUtil.httpPostFileForm(CONVERSION_URL, multipartEntityBuilder);
						fos = new FileOutputStream(temp);
						try {
							HttpUtil.saveTo(stream, fos);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String viewname = FastDFSClient.uploadFile(temp, "default.html");
						temp.delete();
						// 添加文件转换失败状态
						// if(!res){
						if (!StringUtils.isNotBlank(viewname)) {
//							indentResourceService.saveResourceState(indentResource, OnlineDocService.FAIL);
							return;
						} else {
							// 增加转换名字
							pmsProjectResource.setPreviewPath(viewname);
							projectResourceService.updatePreview(pmsProjectResource);
						}

					}
//					indentResourceService.saveResourceState(indentResource, OnlineDocService.FINISH);
					// modify by laowang 2016.5.17 12:15 end
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		return "";
	}
}
