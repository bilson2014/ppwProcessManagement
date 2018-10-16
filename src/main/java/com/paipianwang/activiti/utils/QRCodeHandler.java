package com.paipianwang.activiti.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.paipianwang.activiti.dao.QRCodeDao;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;

/**
 * 微信获取二维码工具
 * 
 * @author rui
 *
 */
@Component
public class QRCodeHandler {
	
	@Autowired
	private QRCodeDao qrCodeDao;
	
	private final String QRCODEKEY="qr_code_key";
	private final String appId="wxfe2196e169a32c3f";
	private final String secret="4599f008df9adfa30b3351678389a580";
	

	public String getProToken() {
		// redis获取
		String token=qrCodeDao.getData(QRCODEKEY);		
		// redis校验
		if(!ValidateUtil.isValid(token)) {
			Map tokenInfo = getToken(appId, secret);
			token=(String) tokenInfo.get("access_token");
			int seconds=((Double)tokenInfo.get("expires_in")).intValue();
			//保存 TODO 确保一下seconds token获取及保存的时间去除出去
			qrCodeDao.setData(QRCODEKEY, seconds-10, token);
		}

		return token;
	}

	/**
	 * 获取token
	 * @param appid
	 * @param secret
	 * @return token值：至少512个字符；有效期：目前为24小时
	 * 			每次调用都会获取一个新的为期24小时的token
	 */
	public Map getToken(String appid, String secret) {

		RestTemplate rest = new RestTemplate();
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid
					+ "&secret=" + secret;
			
			ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, null, byte[].class, new Object[0]);	            
//	        ResponseEntity<byte[]> entity =  rest.postForEntity(url, null, byte[].class);
			
			byte[] result = entity.getBody();

			String s = new String(result);
			return JsonUtil.toBean(s, Map.class);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;

	}

	/**
	 * 生成小程序码B
	 * @param sceneStr 参数字符串
	 * @param accessToken token
	 * @param rgb  小程序码线条RGB颜色
	 * @param is_hyaline 是否透明背景
	 * @param desPath 生成小程序目标文件
	 */
	public void getminiqrQr(String sceneStr, String accessToken, int[] rgb, boolean is_hyaline, String desPath) {
		byte[] result = getminiqrQr(sceneStr, accessToken, rgb, is_hyaline);

		InputStream inputStream = null;
		OutputStream outputStream = null;
		inputStream = new ByteArrayInputStream(result);

		try {
			File file = new File(desPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
		} catch (Exception e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 生成小程序码B
	 * @param sceneStr 参数字符串
	 * @param accessToken token
	 * @param rgb  小程序码线条RGB颜色
	 * @param is_hyaline 是否透明背景
	 * @return
	 */
	public byte[] getminiqrQr(String sceneStr, String accessToken, int[] rgb, boolean is_hyaline) {
		if(!ValidateUtil.isValid(sceneStr)) {
			throw new RuntimeException("param is requisite.");
		}
		
		if(!ValidateUtil.isValid(accessToken)) {
			accessToken=getProToken();
		}

		RestTemplate rest = new RestTemplate();
		try {
			String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
			Map<String, Object> param = new HashMap<>();
			param.put("scene", sceneStr);
			param.put("page", "pages/main/main");// 已结发布且存在的页面
			param.put("width", 430);
			param.put("auto_color", false);
			Map<String, Object> line_color = new HashMap<>();
			line_color.put("r", rgb[0]);
			line_color.put("g", rgb[1]);
			line_color.put("b", rgb[2]);
			param.put("line_color", line_color);
			param.put("is_hyaline", is_hyaline);//透明底色
			
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			HttpEntity requestEntity = new HttpEntity(param, headers);
			ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class,
					new Object[0]);
			byte[] result = entity.getBody();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	enum page {
//		main, quotation;
//	}
	
}
