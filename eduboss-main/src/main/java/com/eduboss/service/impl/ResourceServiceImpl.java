package com.eduboss.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.DataDictCategory;
import com.eduboss.common.RoleResourceType;
import com.eduboss.common.UploadFileStatus;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.ResourceDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Resource;
import com.eduboss.domain.UploadFileRecord;
import com.eduboss.domainVo.ResourceVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.ResourceService;
import com.eduboss.service.UploadFileService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired 
	private UploadFileService uploadFileService;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
    private MobilePushMsgService mobilePushMsgService;
	
	
	@Override
	public DataPackage getGuideLineList(ResourceVo resourceVo, DataPackage dp) {
		List<Criterion> criterions = new ArrayList<Criterion>();

		criterions.add(Expression.isNotNull("stateType"));
		
		if(StringUtils.isNotBlank(resourceVo.getTitle())){
			criterions.add(Expression.like("title", resourceVo.getTitle(), MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(resourceVo.getRurl())){
			criterions.add(Expression.like("rurl", resourceVo.getRurl(), MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotBlank(resourceVo.getStateType())){
			criterions.add(Expression.eq("stateType.id", resourceVo.getStateType()));
		}

		dp = resourceDao.findPageByCriteria(dp, null, criterions);
		
		List<Resource> list = (List<Resource>)dp.getDatas() ;
		List<ResourceVo> voList = new ArrayList<ResourceVo>();
		voList = HibernateUtils.voListMapping(list, ResourceVo.class);
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public void saveOrEditGuideLine(Resource resource) {
		Resource myResource = new Resource();
		
		//通过RURL获取对应的Resource对象
		if(StringUtil.isNotBlank(resource.getRurl()))
		{
			//判断该资源URL是否存在相应的GuideLine记录
			List<Resource> list = null;
			List<Criterion> criterions = new ArrayList<Criterion>();
			
			criterions.add(Expression.eq("rurl", resource.getRurl()));
			criterions.add(Expression.isNotNull("stateType"));
			
			if(StringUtil.isNotBlank(resource.getId())){
				criterions.add(Expression.not(Expression.eq("id", resource.getId())));
				list = resourceDao.findAllByCriteria(criterions);
				if(list.size()>0)
				{
					throw new ApplicationException("系统已存在这一资源URL，请重新输入");
				}
				myResource = resourceDao.findById(resource.getId());
				myResource.setTitle(resource.getTitle());;
				myResource.setStateType(resource.getStateType());

				String content=resource.getRcontent();			
				try {
					myResource.setRcontent(URLDecoder.decode(content,"UTF-8"));				
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				//UEditor原上传文件处理
				delFromResourceFile(resource.getId());
			}else{
				
				list = resourceDao.findAllByCriteria(criterions);
				if(list.size()>0)
				{
					throw new ApplicationException("系统已存在这一资源URL，请重新输入");
				}
				
				//判断输入的URL是否存在
				criterions.clear();
				criterions.add(Expression.eq("rurl", resource.getRurl()));
				list = resourceDao.findAllByCriteria(criterions);
				if(list.size()<= 0)
				{
					throw new ApplicationException("资源URL对应的菜单不存在，请检查");
				}
				myResource = list.get(0);	
				myResource.setTitle(resource.getTitle());;
				myResource.setStateType(resource.getStateType());
				
				String content=resource.getRcontent();			
				try {
					myResource.setRcontent(URLDecoder.decode(content,"UTF-8"));				
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			throw new ApplicationException("资源URL不能为空");
		}
		

		resourceDao.save(myResource);
		//UEditor上传文件处理
		String filePaths = uploadFileService.listUploadFileFromContent(myResource.getRcontent());
		if(StringUtils.isNotBlank(filePaths)) {
			uploadFileService.updateUploadFileStatus(filePaths, UploadFileStatus.INUSE.getValue());  //更新上传的文件状态为在使用
			List<UploadFileRecord> uploadFileRecordList = uploadFileService.getListByFilePath(filePaths);
			Map<String, Object> params = Maps.newHashMap();
			int i = 0;
			params.put("resourceId", myResource.getId());
	        for(UploadFileRecord uploadFileReocrd : uploadFileRecordList){
	        	i = i+1;
	        	params.put("uploadFileReocrdId"+i, uploadFileReocrd.getId());
	        	String sql = "insert into resource_file(resource_id, file_id) VALUES(:resourceId ,:uploadFileReocrdId"+i+" )";
	        	resourceDao.excuteSql(sql,params);
	        }
		}
		//创建推送的消息
        //mobilePushMsgService.createPushMsgBySystemNotice(systemNotice);
		
	}
	
	@Override
	public void updateGuideLineById(String id, String status) {
		StringBuilder hql = new StringBuilder();
		DataDict dataDict = null;
		if("1".equals(status))
			dataDict = dataDictDao.getDataDictByName("有效", DataDictCategory.STATE_TYPE);
		else
			dataDict = dataDictDao.getDataDictByName("无效", DataDictCategory.STATE_TYPE);
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("stateTypeId", dataDict.getId());
		params.put("id", id);
		hql.append(" update Resource set stateType.id= :stateTypeId "); 
		hql.append(" where id = :id ");
		resourceDao.excuteHql(hql.toString(),params);
	}

	@Override
	public void deleteGuideLineById(String resourceId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("resourceId", resourceId);
		sql.append(" update resource set state_type=null,rcontent=null,title=null where id = :resourceId ");
		resourceDao.excuteSql(sql.toString(),params);
		//删除通过UEditor上传的文件
		delFromResourceFile(resourceId);
	}
	
	
	@Override
	public DataPackage getAllGuideLine() {
		Criterion criterion = Expression.eq("rtype", RoleResourceType.MENU);
		List<Resource> list = resourceDao.findByCriteria(criterion);
		
		List<ResourceVo> voList = HibernateUtils.voListMapping(list, ResourceVo.class);
		//去除rcontent的内容
		for(int i=0;i<voList.size();i++)
			voList.get(0).setRcontent(null);
		
		DataPackage dataPackage = new DataPackage();
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	

	@Override
	public ResourceVo findResourceVoByURL(String url) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		DataDict dataDict = dataDictDao.getDataDictByName("有效", DataDictCategory.STATE_TYPE);
		
		criterions.add(Expression.eq("rurl", url));
		criterions.add(Expression.eq("rtype", RoleResourceType.MENU));
		criterions.add(Expression.eq("stateType", dataDict));
		List<Resource> list = resourceDao.findAllByCriteria(criterions);
		
		if(list.size() ==0 )
		{
			return null;
		}
		
		List<ResourceVo> voList = HibernateUtils.voListMapping(list, ResourceVo.class);
		//对图片的宽度进行限制，图片宽度不超过800
		StringBuilder onload = new StringBuilder();
		onload.append("\\<img onload='if(this.width>950){");
		onload.append("this.height=this.height*950/this.width;this.width=950;}'");
		StringBuffer content = new StringBuffer();
		Pattern pattern = Pattern.compile("\\<img");
		Matcher matcher = pattern.matcher(voList.get(0).getRcontent());
		Boolean result = matcher.find();
		while(result)
		{
			matcher.appendReplacement(content, onload.toString());
			result = matcher.find();
		}
		matcher.appendTail(content);
		voList.get(0).setRcontent(content.toString());
		
		return voList.get(0);
	}

	private void delFromResourceFile(String resourceId) {
		String selectFilePathSql = " select ufr.file_path from "
				+ " (select * from resource_file where resource_id = '"
				+ resourceId + "') sf"
				+ " left join upload_file_record ufr on ufr.id = sf.file_id";
		List<String> filePathList = (List<String>) resourceDao.getCurrentSession().createSQLQuery(selectFilePathSql).list();
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
			String delSql = "delete from resource_file where resource_id = :resourceId " ;
			Map<String, Object> params = Maps.newHashMap();
			params.put("resourceId", resourceId);
			resourceDao.excuteSql(delSql,params);
		}
	}

	

}
