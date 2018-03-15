package com.eduboss.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.Constants;
import com.eduboss.common.UploadFileStatus;
import com.eduboss.dao.UploadFileRecordDao;
import com.eduboss.domain.UploadFileRecord;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ItemFile;
import com.eduboss.domainVo.OSSCallbackFileData;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.UploadFileService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.ProtostuffUtils;

/**@author wmy
 *@date 2015年11月4日上午10:18:32
 *@version 1.0 
 *@description
 */

@Service
public class UploadFileServiceImpl implements UploadFileService{
    
    private static final Logger logger = Logger.getLogger(UploadFileServiceImpl.class);

	@Autowired
	private UploadFileRecordDao UploadFileRecordDao;
	
	@Autowired
	private UserService userService;

	@Override
	public void updateUploadFileStatus(String filePaths, String status) {
		UploadFileRecordDao.updateFileStatus(filePaths, status);
	}

	@Override
	public void saveUploadFile(MultipartFile upfile, String fileName, String realFileName) {
		try {
			saveUploadFile(fileName, upfile.getInputStream(), upfile.getSize(), realFileName);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
		
	}
	
	@Override
	public void saveUploadFile(String fileName, InputStream is, long size, String realFileName) {
		DecimalFormat df2  = new DecimalFormat(".00");
		String fileSize = null;
		AliyunOSSUtils.put(fileName, is, size);
		User usr = userService.getCurrentLoginUser();
		UploadFileRecord file = new UploadFileRecord();
		file.setUploadUser(usr);
		file.setFilePath(fileName);
		file.setCreateTime(DateTools.getCurrentDateTime());
		file.setFileStatus(UploadFileStatus.UNUSED);
		file.setRealFileName(realFileName);
		if(size < 1024) fileSize = size + "B";
		else if(size >= 1024 && size < 1024*1024) fileSize = df2.format((double)size/1024)+ "KB";
		else fileSize = df2.format((double)size/(1024*1024)) + "MB";
		file.setFileSize(fileSize);
		UploadFileRecordDao.save(file);		
	}

	@Override
	public String listUploadFileFromContent(String Content) {
		String res = null;
		StringBuffer filePathSb = new StringBuffer();
		if(StringUtils.isNotBlank(Content)) {
			String urlPrefix = PropertiesUtils.getStringValue("oss.access.url.prefix");
			//图片文件
			Pattern p = Pattern.compile("src=\"(.*?)\"");
			Matcher m = p.matcher(Content);
			while (m.find()) {
				String imgUrl = m.group(1);
				if(StringUtils.isNotBlank(imgUrl) && imgUrl.indexOf(urlPrefix) >= 0){
					String filePath = imgUrl.replace(urlPrefix, "");
					if(StringUtils.isBlank(filePathSb) || (StringUtils.isNotBlank(filePathSb) && (filePathSb.toString().indexOf(filePath) < 0))){
						filePathSb.append("," + filePath);
					}
				}
			}
			//附件
			Pattern p2 = Pattern.compile("href=\"(.*?)\"");
			Matcher m2 = p2.matcher(Content);
			while (m2.find()) {
				String fileUrl = m2.group(1);
				if(StringUtils.isNotBlank(fileUrl) && fileUrl.indexOf(urlPrefix) >= 0){
					String filePath2 = fileUrl.replace(urlPrefix, "");
					if(StringUtils.isBlank(filePathSb) || (StringUtils.isNotBlank(filePathSb)  && (filePathSb.toString().indexOf(filePath2) < 0))){
						filePathSb.append("," + filePath2);
					}
			   }           
			}
		}
		if(StringUtils.isNotBlank(filePathSb)) res = filePathSb.toString().substring(1);
		return res;
	}

	@Override
	public List<UploadFileRecord> getListByFilePath(String filePaths) {
		return UploadFileRecordDao.getListByFilePath(filePaths);
	}

	@Override
	public void deleteUnUseFile() {
		List<UploadFileRecord> unUsedList = UploadFileRecordDao.getListByStatus(UploadFileStatus.UNUSED.getValue());
		for(UploadFileRecord ufr : unUsedList) {
			String filePath = ufr.getFilePath();
			//删除阿里云上的文件
			AliyunOSSUtils.remove(filePath);
			ufr.setFileStatus(UploadFileStatus.DELETED);
			ufr.setModifyTime(DateTools.getCurrentDateTime());
		}		
	}

	@Override
    public void saveOSSCallbackFile(String ossCallbackBody) {
	    logger.info("ossCallbackBody:" + ossCallbackBody);
        JSONObject jsonObject = JSONObject.fromObject(ossCallbackBody);
        UploadFileRecord uploadFileRecord = new UploadFileRecord();
        if(jsonObject.get("object") != null)
        {
            String uploadType = null;
            uploadFileRecord.setFilePath(jsonObject.getString("object"));
            if(jsonObject.get("x:realname") != null && jsonObject.get("x:uploadtype") != null)
            {
                uploadFileRecord.setRealFileName(jsonObject.getString("x:realname"));
                uploadType = jsonObject.getString("x:uploadtype");
            }
            if(jsonObject.get("size") != null)
            {
                uploadFileRecord.setFileSize(jsonObject.getString("size")+"B");
            }
            uploadFileRecord.setCreateTime(DateTools.getCurrentDateTime());
            uploadFileRecord.setUploadUser(userService.getCurrentLoginUser());
            uploadFileRecord.setFileStatus(UploadFileStatus.INUSE);
            
            if(jsonObject.get("x:uploadid") != null)
            {
                String uploadId = jsonObject.getString("x:uploadid");
                processOssCallbackWithRedis(uploadId, uploadFileRecord, uploadType);
            }
        }
        UploadFileRecordDao.save(uploadFileRecord);
    }
	
	// 处理缓存文件信息
    private void processOssCallbackWithRedis(String uploadId, UploadFileRecord uploadFileRecord, String uploadType)
    {
        String key = Constants.REDIS_PRE_OSS_UPLOAD_ID + uploadId;
        logger.info("上传终端："+uploadType+" 上传id:"+uploadId+" 上传文件名："+uploadFileRecord.getRealFileName());
        byte[] result = JedisUtil.get(key.getBytes());
        if(result != null && result.length > 0)
        {
            OSSCallbackFileData fileData = ProtostuffUtils.deserializer(result, OSSCallbackFileData.class);
            if(fileData != null)
            {
                List<ItemFile> list = fileData.getFileInfo();
                if(list == null) {
                    list = new ArrayList<ItemFile>();
                }
                ItemFile itemFile = new ItemFile();
                String urlPrefix = PropertiesUtils.getStringValue("oss.access.url.prefix");
                itemFile.setFilePath(urlPrefix +uploadFileRecord.getFilePath());
                itemFile.setFileSize(uploadFileRecord.getFileSize());
                itemFile.setRealFileName(uploadFileRecord.getRealFileName());
                list.add(itemFile);
                
                // 判断上传终端
                if(("PC").equals(uploadType)) {
                    fileData.setCheckedPC(true);
                    fileData.setCheckedH5(false);
                }
                if(("H5").equals(uploadType)) {
                    fileData.setCheckedPC(false);
                    fileData.setCheckedH5(true);
                }
                fileData.setCount(fileData.getCount()+1);
                fileData.setFileInfo(list);
                
                //序列化UserPermissionTree对象
                byte[] bytes = ProtostuffUtils.serializer(fileData);
                JedisUtil.set(key.getBytes(), bytes, 60 * 10);
            }
            else
                return;
        }
        else
        {
            List<ItemFile> list = new ArrayList<>();
            ItemFile itemFile = new ItemFile();
            String urlPrefix = PropertiesUtils.getStringValue("oss.access.url.prefix");
            itemFile.setFilePath(urlPrefix +uploadFileRecord.getFilePath());
            itemFile.setFileSize(uploadFileRecord.getFileSize());
            itemFile.setRealFileName(uploadFileRecord.getRealFileName());
            list.add(itemFile);
            
            OSSCallbackFileData fileData = new OSSCallbackFileData();
            
            // 判断上传终端
            if(("PC").equals(uploadType)) {
                fileData.setCheckedPC(true);
                fileData.setCheckedH5(false);
            }
            if(("H5").equals(uploadType)) {
                fileData.setCheckedPC(false);
                fileData.setCheckedH5(true);
            }
            fileData.setUploadId(uploadId);
            fileData.setCount(1);
            fileData.setFileInfo(list);
            
            //序列化UserPermissionTree对象
            byte[] bytes = ProtostuffUtils.serializer(fileData);
            JedisUtil.set(key.getBytes(), bytes, 60 * 10);
        }
    }

}


