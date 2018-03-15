package com.eduboss.tools.ueditor.upload;

import com.eduboss.tools.ueditor.PathFormat;
import com.eduboss.tools.ueditor.define.AppInfo;
import com.eduboss.tools.ueditor.define.BaseState;
import com.eduboss.tools.ueditor.define.FileType;
import com.eduboss.tools.ueditor.define.State;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public final class Base64Uploader {

	public static State save(String content, Map<String, Object> conf) {
		
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"));
		
		savePath = savePath + suffix;
		String physicalPath = (String) conf.get("rootPath") + savePath;

		//扩展
		State storageState = StorageManagerExt.saveBinaryFile(data, physicalPath);
		
		//State storageState = StorageManager.saveBinaryFile(data, physicalPath);		 //原代码

		if (storageState.isSuccess()) {
			//storageState.putInfo("url", PathFormat.format(savePath));   //原代码
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}