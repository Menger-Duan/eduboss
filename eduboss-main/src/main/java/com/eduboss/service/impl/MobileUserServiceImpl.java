package com.eduboss.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.MobileUserType;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.domain.MobileUser;
import com.eduboss.domainVo.MobileUserVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MobileUserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.PropertiesUtils;

@Service("com.eduboss.service.MobileUserService")
public class MobileUserServiceImpl implements MobileUserService {

	@Autowired
	private MobileUserDao mobileUserDao;
	
	@Override
	public MobileUser findMobileUserByStuId(String studentId) {
		MobileUser mUser = mobileUserDao.findMobileUserByStuId(studentId);
		if(mUser == null) {
			mUser = new MobileUser();
			mUser.setUserId(studentId);
			mUser.setUserType(MobileUserType.STUDENT_USER);
			mUser.setCreateUser(null);
			mUser.setCreateTime(DateTools.getCurrentDateTime());
			mobileUserDao.save(mUser);
		}
		return mUser;
	}

	@Override
	public MobileUser findMobileUserByStaffId(String userId) {
		MobileUser mUser = mobileUserDao.findMobileUserByUserId(userId);
		if(mUser == null) {
			mUser = new MobileUser();
			mUser.setUserId(userId);
			mUser.setUserType(MobileUserType.STAFF_USER);
			mUser.setCreateUser(null);
			mUser.setCreateTime(DateTools.getCurrentDateTime());
			mobileUserDao.save(mUser);
		}
		return mUser;
	}
	
	// 按类型和userID 和 用户类型 查找
	@Override
	public MobileUser findMobileUserByStaffId(String userId,MobileUserType mobileUserType) {		
		MobileUser mUser = mobileUserDao.findMobileUserByUserId(userId);
		if(mUser == null) {
			mUser = new MobileUser();
			mUser.setUserId(userId);
			mUser.setUserType(mobileUserType);
			mUser.setCreateUser(null);
			mUser.setCreateTime(DateTools.getCurrentDateTime());
			mobileUserDao.save(mUser);
		}
		return mUser;
	}

	@Override
	public void updateChannelInfo(MobileUserVo mobileUserVo) {		
		if(StringUtils.isNotBlank(mobileUserVo.getId()) && StringUtils.isNotBlank(mobileUserVo.getPlatFormChannelId()) && StringUtils.isNotBlank(mobileUserVo.getPlatFormUserId()) && mobileUserVo.getMobileType()!=null && mobileUserVo.getUserType()!=null){
			MobileUser user =  mobileUserDao.findById(mobileUserVo.getId());
			if(user!=null){
				user.setPlatFormChannelId(mobileUserVo.getPlatFormChannelId());
				user.setPlatFormUserId(mobileUserVo.getPlatFormUserId());
				user.setMobileType(mobileUserVo.getMobileType());
				user.setUserType(mobileUserVo.getUserType());
				user.setAppVersion(mobileUserVo.getAppVersion());
				user.setMobileSystem(mobileUserVo.getMobileSystem());
				user.setMobileModel(mobileUserVo.getMobileModel());
				user.setModifyTime(DateTools.getCurrentDateTime());
				mobileUserDao.save(user);
			}
			Map<String, Object> params = new HashMap<String, Object>();
			String sql="select * from mobile_user where id!= :id and ( PLATFORM_USER_ID = :platFormUserId or PLATFORM_CHANNEL_ID = :platFormChannelId)";
			params.put("id", mobileUserVo.getId());
			params.put("platFormUserId", mobileUserVo.getPlatFormUserId());
			params.put("platFormChannelId", mobileUserVo.getPlatFormChannelId());
			List<MobileUser> userList=mobileUserDao.findBySql(sql, params);
			if(userList!=null && userList.size()>0){
				for(MobileUser u:userList){
					String sql2="UPDATE mobile_user SET PLATFORM_CHANNEL_ID=NULL ,PLATFORM_USER_ID=NULL where id='"+u.getId()+"' ";
					mobileUserDao.excuteSql(sql2, new HashMap<String, Object>());
				}
				
			}
		}	
	}

	@Override
	public void saveHeaderImgFile(MultipartFile myfile1, String mobileUserId,String servicePath) {
		if(mobileUserId!=null){
			//MobileUser muser = this.findMobileUserByStaffId(user.getUserId());
//				String location = System.getProperty("user.dir");
				String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
				isNewFolder(folder);
				//String fileName=myfile1.getOriginalFilename().substring(0,myfile1.getOriginalFilename().lastIndexOf("."));//上传的文件名
				//String puff=myfile1.getOriginalFilename().replace(fileName, "");//后缀
				String bigFileName = "MOBILE_HEADER_BIG_"+mobileUserId+".jpg";//阿里云上面的文件名 大   默认是JPG 
				String midFileName = "MOBILE_HEADER_MID_"+mobileUserId+".jpg";//阿里云上面的文件名 中
				String smallFileName = "MOBILE_HEADER_SMALL_"+mobileUserId+".jpg";//阿里云上面的文件名 小
				String puff = UUID.randomUUID().toString();
				String relFileName=folder+"/realFile"+puff+".jpg";
				File realFile=new File(relFileName);
				File bigFile=new File(folder+"/"+bigFileName);
				File midFile=new File(folder+"/"+midFileName);
				File smallFile=new File(folder+"/"+smallFileName);
				try {
					myfile1.transferTo(realFile);
					ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
					ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
					ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
					AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
					AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
					AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云
					
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					realFile.delete();
					bigFile.delete();
					midFile.delete();
					smallFile.delete();
				}
			}else{
				throw new ApplicationException("找不到手机用户。");
			}
	}
	
	public void isNewFolder(String folder){
		File f1=new File(folder);
		if(!f1.exists())
		{
			f1.mkdirs();
		}
	}
	
	@Override
	public MobileUser findMobileUserById(String id) {
		MobileUser mUser = mobileUserDao.findMobileUserById(id);		
		return mUser;
	}
	
	@Override
	public void saveSessionUser(String noticeId,String sessionId){
		Map<String, Object> params = new HashMap<String, Object>();
	    StringBuffer  sql=new StringBuffer("INSERT INTO MOBILE_PUSH_MSG_SESSION_USER(SESSION_ID,MOBILE_USER_ID) SELECT :sessionId,m_user.ID FROM MOBILE_USER m_user WHERE m_user.USER_ID IN ( ");
	    params.put("sessionId", sessionId);
	    sql.append("SELECT  u.USER_ID ");
	    sql.append("FROM	user u ");
	    sql.append("WHERE EXISTS ( ");   // 角色判断
	    sql.append("SELECT 1 ");
	    sql.append("FROM (");
	    sql.append("SELECT ");
	    sql.append("nrole.role_id ");
	    sql.append("FROM ");
	    sql.append("SYSTEM_NOTICE notice ");
	    sql.append("INNER JOIN SYSTEM_NOTICE_ROLE nrole ON notice.ID = nrole.notice_id ");
	    sql.append("WHERE ");
	    sql.append("notice.id = :noticeId ");
	    params.put("noticeId", noticeId);
	    sql.append(") nroles ");
	    sql.append("INNER JOIN user_role ur ON nroles.role_id = ur.roleId where ur.userId = u.user_Id ");
	    sql.append(")  ");
	  
	    sql.append(" and EXISTS ( ");  	// 组织架构判断
	    sql.append("SELECT 1 ");
	    sql.append("FROM ( ");
	    sql.append("select id from organization  ");
	    sql.append("where exists (  ");
	    sql.append("select 1 from ( ");
	    sql.append("SELECT org.orgLevel ");
	    sql.append("FROM ");
	    sql.append("SYSTEM_NOTICE notice ");
	    sql.append("INNER JOIN SYSTEM_NOTICE_ORG norg ON notice.ID = norg.notice_id ");
	    sql.append("INNER join organization org on norg.org_id = org.id ");
	    sql.append("WHERE ");
	    sql.append("notice.id = :noticeId2 ");
	    params.put("noticeId2", noticeId);
	    sql.append(") noticeOrg where organization.orgLevel like concat( noticeOrg.orgLevel,'%') ");
	    sql.append(") ");
	    sql.append(") userOrg ");
	    sql.append("where u.organizationID = userOrg.id ");
	    sql.append(") ");
	
	    sql.append("and  u.ENABLE_FLAG=0 ");
	    sql.append(" ); ");
	    mobileUserDao.excuteSql(sql.toString(), params);      
	}

	@Override
	public void saveSessionUserNew(String noticeId,String sessionId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer  sql=new StringBuffer("INSERT INTO MOBILE_PUSH_MSG_SESSION_USER(SESSION_ID,MOBILE_USER_ID) SELECT :sessionId,m_user.ID FROM MOBILE_USER m_user WHERE m_user.USER_ID IN ( ");
		params.put("sessionId", sessionId);
		sql.append("SELECT  u.USER_ID ");
		sql.append("FROM	user u ");
		sql.append("WHERE EXISTS ( ");   // 角色判断
		sql.append("SELECT 1 ");
		sql.append("FROM (");
		sql.append("SELECT ");
		sql.append("nrole.role_id ");
		sql.append("FROM ");
		sql.append("SYSTEM_NOTICE notice ");
		sql.append("INNER JOIN SYSTEM_NOTICE_ROLE nrole ON notice.ID = nrole.notice_id ");
		sql.append("WHERE ");
		sql.append("notice.id = :noticeId ");
		params.put("noticeId", noticeId);
		sql.append(") nroles ");
		sql.append("INNER JOIN user_organization_role ur ON nroles.role_id = ur.role_Id where ur.user_Id = u.user_Id ");
		sql.append(")  ");

		sql.append(" and EXISTS ( ");  	// 组织架构判断
		sql.append("SELECT 1 ");
		sql.append("FROM ( ");
		sql.append("select id from organization  ");
		sql.append("where exists (  ");
		sql.append("select 1 from ( ");
		sql.append("SELECT org.orgLevel ");
		sql.append("FROM ");
		sql.append("SYSTEM_NOTICE notice ");
		sql.append("INNER JOIN SYSTEM_NOTICE_ORG norg ON notice.ID = norg.notice_id ");
		sql.append("INNER join organization org on norg.org_id = org.id ");
		sql.append("WHERE ");
		sql.append("notice.id = :noticeId2 ");
		params.put("noticeId2", noticeId);
		sql.append(") noticeOrg where organization.orgLevel like concat( noticeOrg.orgLevel,'%') ");
		sql.append(") ");
		sql.append(") userOrg ");
		sql.append("where u.organizationID = userOrg.id ");
		sql.append(") ");

		sql.append("and  u.ENABLE_FLAG=0 ");
		sql.append(" ); ");
		mobileUserDao.excuteSql(sql.toString(), params);
	}
}
