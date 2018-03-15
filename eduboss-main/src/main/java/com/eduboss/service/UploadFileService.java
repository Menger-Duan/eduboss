package com.eduboss.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.UploadFileRecord;

/**@author wmy
 *@date 2015年11月4日上午10:11:32
 *@version 1.0 
 *@description
 */
@Service
public interface UploadFileService {
	
	public void saveUploadFile(MultipartFile upfile, String fileName, String realFileName);
	
	public void saveUploadFile(String fileName, InputStream is, long length, String realFileName);
                
	public void updateUploadFileStatus(String filePaths, String status);

	public String listUploadFileFromContent(String Content);
	
	//filePaths = "..,..,.."
	public List<UploadFileRecord> getListByFilePath(String filePaths);
	
	public void deleteUnUseFile();
	
	public void saveOSSCallbackFile(String ossCallbackBody);
	
}


