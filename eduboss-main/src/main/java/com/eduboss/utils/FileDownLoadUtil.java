package com.eduboss.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class FileDownLoadUtil {
	
	static final int LENGTH = 1024*8;
	/**
	 * 下载
	 */
	public static void down(HttpServletResponse response,String downloadFileName){
		System.out.println("******downloadFileName************"+downloadFileName);
		if (downloadFileName == null) {
			return;
		}
		String name;
		String locationFile;
		if (downloadFileName.indexOf("\\") == -1
				&& downloadFileName.indexOf("/") == -1) {
			System.out.println("***if*****");
			String location = ServletActionContext.getServletContext()
					.getRealPath("/download");
			String downfile = location + "/" + downloadFileName;
			name = downloadFileName;
			locationFile = downfile;
		} else {
			System.out.println("***else*****");
			name = getFileNameFromPath(downloadFileName);
			locationFile = downloadFileName.replaceAll("\\\\", "/");
		}
		BufferedInputStream input = null;
		OutputStream os = null;
		
		try {
			response.reset();
			String contenttype;
			try {
				contenttype = "application/octet-stream";
			} catch(UnsupportedOperationException e) {
				contenttype = "application/txt";
			}
			response.setContentType(contenttype);
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(name.getBytes("utf-8"), "iso-8859-1"));
			System.out.println("*********************locationFile="+locationFile);
			locationFile=URLDecoder.decode(locationFile,"UTF-8");
			input = new BufferedInputStream(new FileInputStream(new File(locationFile)));
			os = response.getOutputStream();
//			os = new FileOutputStream("f://aaa.csv");
			copy(input, os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if(os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void copy(InputStream input, OutputStream output) throws IOException {
		int bufread = 0;
		byte[] memdata = new byte[LENGTH];
		while( (bufread = input.read(memdata, 0, LENGTH)) != -1) {
			output.write(memdata, 0, bufread);
		}
	}
	
	public static String getFileNameFromPath(String fullPath) {
		int index = fullPath.lastIndexOf(File.separator);
		return fullPath.substring(index+1);
	}
}
