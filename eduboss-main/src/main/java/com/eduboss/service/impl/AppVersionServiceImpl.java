package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MobileType;
import com.eduboss.common.PushMsgType;
import com.eduboss.dao.AppVersionDao;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.domain.AppVersion;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AppVersionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.AppVersionService;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.SpringMvcUtils;
import com.google.common.collect.Maps;

@Service("AppVersionService")
public class AppVersionServiceImpl implements AppVersionService {

	@Autowired
	private AppVersionDao appVersionDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MobileUserDao mobileuserDao;
	
	@Autowired
	private MobilePushMsgService mobilePushMsgService;
	
	@Override
	public DataPackage getAppVersionList(DataPackage datapackage,AppVersionVo appVersionVo,String startDate,String endDate) {
		
		Map<String, Object> params = Maps.newHashMap();
				
		StringBuffer hql=new StringBuffer();
		hql.append(" from AppVersion where 1=1");
		if(StringUtils.isNotBlank(startDate)){
			hql.append(" and createTime >= :startDate ");
			params.put("startDate", startDate+" 0000");
		}
		if(StringUtils.isNotBlank(endDate)){
			hql.append(" and createTime <= '"+endDate+" 2359' ");
			params.put("endDate", endDate+" 2359");
		}
		if(StringUtils.isNotBlank(appVersionVo.getAppName())){
			hql.append(" and appName like :appName ");
			params.put("appName", "%"+appVersionVo.getAppName()+"%");
		}
		if(StringUtils.isNotBlank(appVersionVo.getVersion())){
			hql.append(" and version like :version ");
			params.put("version", "%"+appVersionVo.getVersion()+"%");
		}
		if(appVersionVo.getMobileType()!=null){
			hql.append(" and mobileType = :mobileType ");
			params.put("mobileType", appVersionVo.getMobileType());
		}
		if (StringUtils.isNotBlank(datapackage.getSord())
				&& StringUtils.isNotBlank(datapackage.getSidx())) {
			hql.append(" order by "+datapackage.getSidx()+" "+datapackage.getSord());
		} 
				
		datapackage=appVersionDao.findPageByHQL(hql.toString(), datapackage,true,params);
		List<AppVersion> list=(List<AppVersion>)datapackage.getDatas();
		List<AppVersionVo> voList=new ArrayList<AppVersionVo>();
		voList=HibernateUtils.voListMapping(list,AppVersionVo.class);
		datapackage.setDatas(voList);
		return datapackage;
	}

	@Override
	public String saveOrUpdateAppVersion(AppVersionVo appVersionVo) {
		String mark="";
		if(appVersionVo!=null){
			int count=appVersionDao.getVersionCount(appVersionVo);
			User user=userService.getCurrentLoginUser();
			AppVersion version=HibernateUtils.voObjectMapping(appVersionVo, AppVersion.class);						
			if(count>0){
				mark="fail";
			}else{		
				if(StringUtils.isBlank(version.getId()) || version.getId()==null){
					String createTime=DateTools.getCurrentDateTime();
					version.setId(null);
					version.setCreateTime(createTime);
					version.setCreateUser(user);
				}				
				appVersionDao.save(version);
				mark=version.getId();
			}
			if(version.getId()!=null && StringUtils.isNotBlank(version.getId())){
	        	HttpSession session=SpringMvcUtils.getSession();
	        	session.setAttribute("param", version.getId());       	
	        }
		}
		return mark;
	}

	@Override
	public void deleteAppVersion(String appVersionId) {
		if(StringUtils.isNotBlank(appVersionId)){
			AppVersion version=appVersionDao.findById(appVersionId);
			appVersionDao.delete(version);
		}
		
	}
	
	@Override
	public AppVersionVo findAppVersionVoById(String id){
		if(StringUtils.isNotBlank(id)){
			AppVersion verdion=appVersionDao.findById(id);
			AppVersionVo appVerSionVo=HibernateUtils.voObjectMapping(verdion, AppVersionVo.class);
			return appVerSionVo;
		}else{
			return new AppVersionVo();
		}
		
		
		
	}

	/**
	 * App版本升级推送消息到手机端
	 */
	@Override
	public void pushAppVersionToMobile(String versionStr,String mobileType) {
		HttpSession session=SpringMvcUtils.getSession();
		String id="";
		AppVersion app=new AppVersion();
		String time="";
		Map<String, Object> params = Maps.newHashMap();
		if(session.getAttribute("param")!=null){
			id=session.getAttribute("param").toString();
			app=appVersionDao.findById(id);
			time=app.getCreateTime();
		}
		StringBuffer hql=new StringBuffer();
		hql.append(" from MobileUser where userType='MANAGE' ");
		if(mobileType != null && StringUtils.isNotBlank(mobileType)){
			hql.append(" and mobileType = :mobileType ");
			params.put("mobileType", MobileType.valueOf(mobileType));
		}
		List<MobileUser> userList=this.mobileuserDao.findAllByHQL(hql.toString(),params);
		if(userList!=null && userList.size()>0){
			for(MobileUser user:userList){
				mobilePushMsgService.pushMsg(user, versionStr,id,PushMsgType.APP_VERSION.toString(),time, null, "", "星火管理端");
			}
		}
		
	}
	
	/**
	 * @param type     IOS   or android
	 * @param appId    eduBossManager  星火管理端
	 * @param isUpdate    1  是，2 否  是否强制更新
	 * @return
	 */
	@Override
	public AppVersionVo getInfoByMbUserType(String type,String appId,String isUpdate) {
		StringBuilder hql=new StringBuilder();
		
		 hql.append(" from AppVersion where 1=1 ");
		 Map<String, Object> params = Maps.newHashMap();
		 if(StringUtils.isNotBlank(type)){
				hql.append(" and mobileType = :mobileType ");
				params.put("mobileType", MobileType.valueOf(type.toUpperCase()));
		 }
		 
		 if(StringUtils.isNotBlank(appId)){
				hql.append(" and appId = :appId ");
				params.put("appId", appId);
		 }
		 
		 if(StringUtils.isNotBlank(isUpdate)){
				hql.append(" and isUpdate = :isUpdate ");
				params.put("isUpdate", isUpdate);
		 }
		 
		 List<AppVersion> list = appVersionDao.findLimitHql(hql.toString(), 1,params);
		 AppVersionVo vo=new AppVersionVo();
		 if(list.size()>0){
			 vo=HibernateUtils.voObjectMapping(list.get(0), AppVersionVo.class);
			 return vo;
		 }
		 
		return null;
	}
	
	
	
	
	
	

	
}
