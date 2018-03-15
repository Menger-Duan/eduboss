package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.UploadFileStatus;
import com.eduboss.dao.UploadFileRecordDao;
import com.eduboss.domain.UploadFileRecord;
import com.eduboss.utils.DateTools;

/**@author wmy
 *@date 2015年11月4日上午10:05:13
 *@version 1.0 
 *@description
 */
@Repository("UploadFileRecordDao")
public class UploadFileRecordImpl extends GenericDaoImpl<UploadFileRecord, String> implements  UploadFileRecordDao {

	@Override
	public void updateFileStatus(String filePaths, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(filePaths) && StringUtils.isNotBlank(status)){
			String filePathArr[] = filePaths.split(",");
			if(filePathArr.length > 0) {
				String hql = "";
				if (filePathArr.length > 1) {
					hql = " update UploadFileRecord t set t.fileStatus = :fileStatus, t.modifyTime = :modifyTime where t.filePath in (:filePaths)";
					params.put("filePaths", filePathArr);
				} else {
					hql = " update UploadFileRecord t set t.fileStatus = :fileStatus, t.modifyTime = :modifyTime where t.filePath = :filePath";
					params.put("filePath", filePaths);
				}
				params.put("fileStatus", UploadFileStatus.valueOf(status));
				params.put("modifyTime", DateTools.getCurrentDateTime());
				super.excuteHql(hql, params);
			}
		}
	}

	@Override
	public List<UploadFileRecord> getListByFilePath(String filePaths) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<UploadFileRecord> returnList = null;
		if(StringUtils.isNotBlank(filePaths)){
			String filePathArr[] = filePaths.split(",");
			if(filePathArr.length > 0) {
				String hql = "";
				if (filePathArr.length > 1) {
					hql = " from UploadFileRecord t where t.filePath in (:filePaths) ";
					params.put("filePaths", filePathArr);
				} else {
					hql = " from UploadFileRecord t where t.filePath = :filePath";
					params.put("filePath", filePaths);
				}
				returnList = (List<UploadFileRecord>)super.findAllByHQL(hql, params);
			}
		}
		return returnList;
	}

	@Override
	public List<UploadFileRecord> getListByStatus(String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from UploadFileRecord t where t.fileStatus = :fileStatus";
		params.put("fileStatus", UploadFileStatus.valueOf(status));
		return (List<UploadFileRecord>)super.findAllByHQL(hql, params);
	}

}


