package com.eduboss.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eduboss.common.Constants;
import com.eduboss.domainVo.ItemFile;
import com.eduboss.domainVo.OSSCallbackFileData;
import com.eduboss.service.ItemInstanceFileService;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.ProtostuffUtils;
import com.eduboss.utils.StringUtil;

@Service("itemInstanceFileService")
public class ItemInstanceFileServiceImpl implements ItemInstanceFileService {
	private static final Logger logger = Logger.getLogger(ItemInstanceFileServiceImpl.class);
	private static final String prefix = PropertiesUtils.getStringValue("oss.access.url.prefix");
	

	@Override
	public void deleteFile(String filePath, String uploadType, String uploadId) {
	    logger.info("filePath:" + filePath + ",uploadType:" + uploadType + ",uploadId:" + uploadId);
		if(StringUtil.isBlank(filePath) || (StringUtil.isBlank(uploadId) && filePath.length() <= prefix.length()))
			return;
		if(!filePath.contains(prefix)) {
		    filePath = prefix + filePath;
		}
		
		delFile(filePath, uploadType, uploadId);
	}
	
	private void delFile(String filePath, String uploadType, String uploadId) {
		// 若uploadType 不为空， 则需要OSS上传回调缓存处理
		if(StringUtil.isNotBlank(uploadType) && StringUtil.isNotBlank(uploadId))
		{
			String key = Constants.REDIS_PRE_OSS_UPLOAD_ID + uploadId;
			byte[] result = JedisUtil.get(key.getBytes());
			if(result != null && result.length > 0)
			{
				OSSCallbackFileData fileData = ProtostuffUtils.deserializer(result, OSSCallbackFileData.class);
				fileData.setCount(fileData.getCount()-1);
				List<ItemFile> list = fileData.getFileInfo();
				
				// 去掉删除的文件
				if(list != null && list.size() > 0)
				{
					for(ItemFile file:list)
					{
						if(filePath.equals(file.getFilePath()))
						{
							list.remove(file);
							break;
						}
					}
				}
				
				fileData.setFileInfo(list);
				// 判断上传终端
				if(("PC").equals(uploadType)) {
					fileData.setCheckedPC(true);
					fileData.setCheckedH5(false);
				}
				if(("H5").equals(uploadType)) {
					fileData.setCheckedPC(false);
					fileData.setCheckedH5(true);
				}
				
				//序列化UserPermissionTree对象
				byte[] bytes = ProtostuffUtils.serializer(fileData);
				JedisUtil.set(key.getBytes(), bytes, 60 * 10);
			}
			else {
				OSSCallbackFileData fileData = new OSSCallbackFileData();
				if(("PC").equals(uploadType)) {
					fileData.setCheckedPC(true);
					fileData.setCheckedH5(false);
				}
				if(("H5").equals(uploadType)) {
					fileData.setCheckedPC(false);
					fileData.setCheckedH5(true);
				}
				
				//序列化UserPermissionTree对象
				byte[] bytes = ProtostuffUtils.serializer(fileData);
				JedisUtil.set(key.getBytes(), bytes, 60 * 10);
			}
		}
	}

}
