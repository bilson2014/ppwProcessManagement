package com.paipianwang.activiti.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paipianwang.activiti.resources.model.PhotoCutParam;

/**
 * 图片处理 工具类
 */
public class PhotoUtil {

	public static Logger logger = LoggerFactory.getLogger("error");
	/**
	 * 图片裁剪
	 */
	public static InputStream cutPhoto(final InputStream is,final PhotoCutParam param,final String extName){
		
		if(is != null && param != null){
			try {
				final BufferedImage image = ImageIO.read(is);
				
				final int x = param.getX();
				
				final int y = param.getY();
				
				final int x2 = param.getX2();
				
				final int y2 = param.getY2();
				
				final int width = param.getWidth();
				
				final int height = param.getHeight();
				
				final int originalWidth = param.getOriginalWidth();
				
				final int originalHeight = param.getOriginalHeight();
				
				if(width <=0 || height <= 0) return null;
				
				if(originalWidth <= 0 || originalHeight <= 0) return null;
				
				if(x <= 0 || x > originalWidth) return null;
				
				if(x2 <= 0 || x2 > originalWidth) return null;
				
				if(y <= 0 || y > originalHeight) return null;
				
				if(y2 <= 0 || y2 > originalHeight) return null;
				
				final double scale_level_x = ((double)image.getWidth()) / ((double)originalWidth);
				
				final double scale_level_y = ((double)image.getHeight()) / ((double)originalHeight);
				
				final int adjusted_x = (int) (x * scale_level_x);
				
				final int adjusted_y = (int) (y * scale_level_y);
				
				final int adjusted_width = (int) (width * scale_level_x);
				
				final int adjusted_height = (int) (height * scale_level_y);
				
				final BufferedImage croppedImage = image.getSubimage(adjusted_x, adjusted_y, adjusted_width, adjusted_height);
				
				final ByteArrayOutputStream cropped_os = new ByteArrayOutputStream();
				
				ImageIO.write(croppedImage, extName, cropped_os);
				
				final InputStream cropped_is = new ByteArrayInputStream(cropped_os.toByteArray());
				
				cropped_is.close();
				
				cropped_os.close();
				
				is.close();
				
				return cropped_is;
				
			} catch (IOException e) {
//				Log.error("image cutting error ...",null);
				e.printStackTrace();
			}
			
		}else{
//			Log.error("photo inputStream is null ...",null);
			return null;
		}
		return null;
	}
}
