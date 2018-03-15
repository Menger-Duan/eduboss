package com.eduboss.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.UploadFileStatus;
import com.eduboss.dao.FeedBackDao;
import com.eduboss.domain.FeedBack;
import com.eduboss.domain.UploadFileRecord;
import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.FeedBackService;
import com.eduboss.service.UploadFileService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service("FeedBackService")
public class FeedBackServiceImpl implements FeedBackService {
	
	@Autowired
	private FeedBackDao feedBackDao;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	
	@Override
	public DataPackage getFeedBackList(DataPackage dataPackage,
			FeedBackVo feedBackvo,
			String startDate,
			String endDate) {
		
		return feedBackDao.getFeedBackList(dataPackage, feedBackvo,startDate,endDate);
	}

	@Override
	public void saveOrUpdateFeedBack(FeedBackVo feedBackvo) {
		FeedBack feedBack=HibernateUtils.voObjectMapping(feedBackvo, FeedBack.class);
		if(feedBack!=null){
			feedBack.setCreateUser(userService.getCurrentLoginUser());
			feedBack.setOrg(userService.getCurrentLoginUserOrganization());
			feedBack.setCreateTime(DateTools.getCurrentDateTime());
			feedBack.setIsBack("0");
			//对内容做处理
			String content=feedBack.getContent();			
			try {
				feedBack.setContent(URLDecoder.decode(content,"UTF-8"));				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			feedBackDao.saveOrUpdateFeedBack(feedBack);
			//UEditor上传文件处理
			String filePaths = uploadFileService.listUploadFileFromContent(feedBack.getContent());
			if(StringUtils.isNotBlank(filePaths)) {
				uploadFileService.updateUploadFileStatus(filePaths, UploadFileStatus.INUSE.getValue());  //更新上传的文件状态为在使用
				List<UploadFileRecord> uploadFileRecordList = uploadFileService.getListByFilePath(filePaths);
		        for(UploadFileRecord uploadFileReocrd : uploadFileRecordList){
		        	String sql = "insert into feedback_file(feedback_id, file_id) VALUES('" + feedBack.getId() + "', '" + uploadFileReocrd.getId() + "')";
		        	feedBackDao.excuteSql(sql, new HashMap<String, Object>());
		        }
			}
		}	
		
	}
	
	private void delFromFeedbackFile(String feedbackId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String selectFilePathSql = " select ufr.file_path from "
				+ " (select * from feedback_file where feedback_id = '"
				+ feedbackId + "') fbf"
				+ " left join upload_file_record ufr on ufr.id = fbf.file_id";
		List<String> filePathList = (List<String>) feedBackDao.getCurrentSession().createSQLQuery(selectFilePathSql).list();
		if (filePathList != null && filePathList.size() > 0) {
			StringBuffer filePathSb = new StringBuffer();
			for (String str : filePathList) {
				filePathSb.append("," + str);
			}
			if (StringUtils.isNotBlank(filePathSb)) {
				uploadFileService.updateUploadFileStatus(filePathSb.toString()
						.substring(1), UploadFileStatus.UNUSED.getValue()); // 更新上传的文件状态为未使用（未使用的文件将会通过跑批删除）
			}
			// 删除旧的文件关联记录UEditor
			String delSql = "delete from feedback_file where feedback_id = :feedbackId ";
			params.put("feedbackId", feedbackId);
			feedBackDao.excuteSql(delSql, params);
		}
	}

	@Override
	public FeedBackVo findFeedBackById(String id) {
		if(StringUtils.isNotBlank(id) && !"".equals(id)){
			return feedBackDao.findFeedBackById(id);
		}else{
			return new FeedBackVo();
		}
		
	}

	@Override
	public void deleteFeedBack(FeedBackVo feedBackvo) {
		FeedBack feedBack=HibernateUtils.voObjectMapping(feedBackvo, FeedBack.class);
		if(feedBack!=null){
			delFromFeedbackFile(feedBack.getId());
			feedBackDao.deleteFeedBack(feedBack);
		}
	}
	
	

}
