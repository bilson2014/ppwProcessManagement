package com.paipianwang.activiti.resources.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paipianwang.activiti.utils.ImageMergeUtil;
import com.paipianwang.activiti.utils.QRCodeHandler;

@RestController
@RequestMapping("mini")
public class MiniProgramController {

	@Autowired
	private QRCodeHandler qrCodeHandler;
	/**
	 * 获取项目小程序入口二维码
	 * @param request
	 * @param resp
	 * 			<img alt="" src="/mini/qrcode?id=20170920173626332">
	 */
	@RequestMapping("/qrcode")
	public void getImg(HttpServletRequest request,HttpServletResponse resp) {
		
		String id=request.getParameter("id");
		
		byte[] imgs=qrCodeHandler.getminiqrQr(id, qrCodeHandler.getProToken(),  new int[] {0,0,0}, true);
		
		OutputStream out=null;
		try {
			out=resp.getOutputStream();
//			out.write(imgs);
			ImageMergeUtil.mergeQRCodeImg(imgs, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
