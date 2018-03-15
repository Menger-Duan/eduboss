package com.eduboss.tools.ueditor.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;

import com.eduboss.listener.SpringInit;
import com.eduboss.service.UploadFileService;
import com.eduboss.tools.ueditor.define.AppInfo;
import com.eduboss.tools.ueditor.define.BaseState;
import com.eduboss.tools.ueditor.define.State;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.PropertiesUtils;

/**@author wmy
 *@date 2015年11月2日下午5:52:13
 *@version 1.0 
 *@description
 */
public class StorageManagerExt extends StorageManager{
	
	private static UploadFileService uploadFileService;
	
	
	public static final int BUFFER_SIZE = 8192;

	public StorageManagerExt() {
		super();
	}

	public static State saveBinaryFile(byte[] data, String path) {
		State state = null;
		String aliName = null;
		InputStream sbs = new ByteArrayInputStream(data);
		String prefix =path.substring(path.lastIndexOf(".")+1);
		aliName = UUID.randomUUID().toString()+"." + prefix;//阿里云上面的文件名
		if(uploadFileService==null){
			ApplicationContext app = SpringInit.getApplicationContext();
			uploadFileService = app.getBean(UploadFileService.class);			
		}
		uploadFileService.saveUploadFile(aliName, sbs, data.length, DateTools.getCurrentDateTime()+"." + prefix );
		state = new BaseState(true);
		state.putInfo( "size", data.length );
		state.putInfo( "title", aliName);
		state.putInfo("url", PropertiesUtils.getStringValue("oss.access.url.prefix") + aliName);
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
			long maxSize, String realName) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile(tmpFile, path, realName);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
			
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path) {
		return null;
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path, String realFileName) {
		State state = null;
		String aliName = null;
		try {
			String prefix =path.substring(path.lastIndexOf(".")+1);
			aliName = UUID.randomUUID().toString()+"." + prefix;//阿里云上面的文件名
			if(uploadFileService==null){
				ApplicationContext app = SpringInit.getApplicationContext();
				uploadFileService = app.getBean(UploadFileService.class);			
			}
			uploadFileService.saveUploadFile(aliName, new FileInputStream((tmpFile)), tmpFile.length(), realFileName);
			state = new BaseState(true);
			state.putInfo( "size", tmpFile.length() );
			state.putInfo( "title", aliName);	
			state.putInfo("url", PropertiesUtils.getStringValue("oss.access.url.prefix") + aliName);
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
		
		return state;
	}

}


