package com.eduboss.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * 图片工具类
 * @classname	ImageUtils.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-6-29 04:23:24
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
public class ImageUtils {
	
	/**
	 * ImageUtils
	 */
	public ImageUtils() {

    }

    /**
     *
     * @param pressImg --
     * @param targetImg --
     * @param x
     * @param y
     */
    public final static void pressImage(String pressImg, String targetImg,
            int x, int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            g.dispose();
            ImageIO.write(image,  "jpeg" , new File(targetImg));
            image.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     */
    public static void markByText(String logoText, String srcImgPath, 
            String targerPath) {
        InputStream is = null; 
        OutputStream os = null; 
        try { 
            Image srcImg = ImageIO.read(new File(srcImgPath)); 
 
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), 
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB); 
 
            Graphics2D g = buffImg.createGraphics();
            
            int  wideth  =  srcImg.getWidth( null );
            int  height  =  srcImg.getHeight( null );
            g.drawImage(srcImg, 0, 0, wideth, height, null); 
 
            Integer degree = -24;
            if (null != degree) { 
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2, (double) buffImg 
                                .getHeight() / 2); 
            } 
 
            g.setColor(new Color(230, 220, 230));
 
            g.setFont(new Font("宋体", Font.BOLD, 18));
 
            float alpha = 0.5f; 
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 
                    alpha)); 
 
            g.drawString(logoText, 84, 130);
 
            g.dispose(); 
 
            os = new FileOutputStream(targerPath); 
 
            ImageIO.write(buffImg, "JPG", os);
 
        } catch (Exception e) {
            e.printStackTrace(); 
        } finally { 
            try { 
                if (null != is) 
                    is.close(); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
            try { 
                if (null != os) 
                    os.close(); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
        } 
    }

    public static void main(String[] args) {
    	markByText("111111111111111111111111111111", "e:/1.jpg", "e:/2.jpg");
    }
}
