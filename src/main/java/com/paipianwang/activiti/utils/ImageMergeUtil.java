package com.paipianwang.activiti.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.paipianwang.pat.common.config.PublicConfig;

public class ImageMergeUtil {
	public static final String QRCODE_BACKGROUD_PATH=PublicConfig.DOC_TEMP_PATH+File.separator+"qrCode"+File.separator+"background.jpg";

	public static void mergeQRCodeImg(byte[] qrCode,  OutputStream out) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(qrCode);// 将b作为输入流；
		BufferedImage qrImage = ImageIO.read(in);// 将in作为输入流，读取图片存入image中，而这里in可以为

		BufferedImage buf = ImageIO.read(new File(QRCODE_BACKGROUD_PATH));
		
		int w = buf.getWidth(), h = buf.getHeight();
		BufferedImage newimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = newimage.createGraphics();
		// draw start
		g.drawImage(buf, 0, 0, w, h, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));

		// 原图760*1024 新图占300*300
		int size = 300;
		int x = (w - size) / 2;
		int y = 635;

		g.drawImage(qrImage, x, y, size, size, null);
		g.dispose();
		// draw end

		ImageIO.write(newimage, "JPEG", out);
	}

}
