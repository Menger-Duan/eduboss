package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.UploadFileRecord;


/**@author wmy
 *@date 2015年11月4日上午10:04:00
 *@version 1.0 
 *@description
 */
public interface UploadFileRecordDao extends GenericDAO<UploadFileRecord, String> {

	void updateFileStatus(String filePaths, String status);

	List<UploadFileRecord> getListByFilePath(String filePaths);

	List<UploadFileRecord> getListByStatus(String status);

}


