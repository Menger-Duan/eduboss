package com.eduboss.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class RotateImage {

	public static BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// calculate the new image size
		Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
				src_width, src_height)), angel);

		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		// transform
		g2.translate((rect_des.width - src_width) / 2,
				(rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

		g2.drawImage(src, null, null);
		return res;
	}

	public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90) {
			if(angel / 90 % 2 == 1){
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width, des_height));
	}
	
	
	/**
	 * 图片旋转
	 * @param aliUrl  阿里云URL 
	 * @param rate  旋转角度   
	 * @throws Exception
	 */
	public static void reventImage(String aliUrl,int rate) throws Exception{
			//new一个URL对象
				URL url = new URL(aliUrl);
				//打开链接
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				//设置请求方式为"GET"
				conn.setRequestMethod("GET");
				//超时响应时间为5秒
				conn.setConnectTimeout(5 * 1000);
				//通过输入流获取图片数据
				InputStream inStream = conn.getInputStream();
				BufferedImage src = ImageIO.read(inStream);//读取图片
				ByteArrayOutputStream bs = new ByteArrayOutputStream();  //输出流
				BufferedImage des = RotateImage.Rotate(src, rate);//旋转图片
				ImageOutputStream imOut; 
				InputStream is = null; 
				imOut = ImageIO.createImageOutputStream(bs); //创建输出流
//				File nowFile=new File("C:/Users/Administrator/Desktop/001.jpg");
//				ImageIO.write(des, "jpg", nowFile);
				ImageIO.write(des, "jpg", imOut);//写入输出流
				is= new ByteArrayInputStream(bs.toByteArray());//写进输入流 
//				AliyunOSSUtils.remove(aliUrl.substring(aliUrl.lastIndexOf("/")+1));
				AliyunOSSUtils.put(aliUrl.substring(aliUrl.lastIndexOf("/")+1), is,bs.toByteArray().length);//把转换的文件存到阿里云，覆盖原来的图片
	}
	public static void main(String[] args) throws IOException {
		String aliUrl="http://xuebangsoft-eduboss.oss-cn-shenzhen.aliyuncs.com/ONE_ON_ONE_ATTEND_PIC_COU0000543472.jpg";
		//new一个URL对象
		URL url = new URL(aliUrl);
		System.out.println(aliUrl.substring(aliUrl.lastIndexOf("/")+1));

		//打开链接
//		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//设置请求方式为"GET"
//		conn.setRequestMethod("GET");
		//超时响应时间为5秒
//		conn.setConnectTimeout(5 * 1000);
		//通过输入流获取图片数据
//		InputStream inStream = conn.getInputStream();
//		BufferedImage src = ImageIO.read(inStream);
		
//		BufferedImage src = ImageIO.read(new File("C:/Users/Administrator/Desktop/222.jpg"));
//		BufferedImage des = RotateImage.Rotate(src, 90);
//		ImageIO.write(des, "jpg", new File("C:/Users/Administrator/Desktop/001.jpg"));
				
	}
	
	
}
