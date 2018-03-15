package com.eduboss.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eduboss.common.MobileUserType;
import com.eduboss.common.ProductType;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.UploadFileStatus;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ReportDao;
import com.eduboss.dao.SystemNoticeDao;
import com.eduboss.dao.SystemNoticeUserDao;
import com.eduboss.dao.UserDao;
import com.eduboss.dao.impl.BasicGenericDaoImpl.QueryTypeEnum;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.SystemNotice;
import com.eduboss.domain.SystemNoticeUser;
import com.eduboss.domain.UploadFileRecord;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SystemNoticeUserVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.domainVo.WelcomeNoticeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.SystemNoticeService;
import com.eduboss.service.UploadFileService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.SpringMvcUtils;
import com.eduboss.utils.StringUtil;

@Service
public class SystemNoticeServiceImpl implements SystemNoticeService{
	@Autowired
	private SystemNoticeDao systemNoticeDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemNoticeUserDao systemNoticeUserDao;
	
	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
    private MobilePushMsgService mobilePushMsgService;
	
	@Autowired
    private ReportDao reportDao;
	
	@Autowired 
	private UploadFileService uploadFileService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MobileUserDao mobileUserDao;

	
	
	/**
	 * 系统公告管理列表
	 * */
	@Override
	public DataPackage getSystemNoticeList(SystemNoticeVo systemNoticeVo, DataPackage dp) {
		return systemNoticeDao.getSystemNoticeList(systemNoticeVo, dp);
	}

     public void createImage(CommonsMultipartFile myimage,HttpServletResponse response) throws IOException {
	        //根据ComminsMutilpartFile得到文件输入流，BufferedImage读取这个输入流得到bufferedImage实例
	         BufferedImage bufferedImage1=new BufferedImage(1000, 500, BufferedImage.TYPE_INT_BGR);
	         BufferedImage bufferedImage= ImageIO.read(myimage.getInputStream());
	        Image image=bufferedImage.getScaledInstance(1000, 500, BufferedImage.SCALE_DEFAULT);//钓鱼getScaledInstance得到一个一个1000*500的Image对象
	        bufferedImage1.getGraphics().drawImage(image, 0, 0, null);
	        ImageIO.write(bufferedImage1, "jpg",response.getOutputStream());//把图片输出
	        //ImageIO.createImageInputStream(input);
	        
     }
	
	/**
	 * 新增或修改公告管理
	 * @throws IOException 
	 * */
	@Override
	public void saveOrEditSystemNotice(SystemNotice sNotice,MultipartFile dataFile, String isAllOrganization) {
		Map<String, Object> params = new HashMap<String, Object>();
		SystemNotice systemNotice = new SystemNotice();
		User user = new User();
		if (isAllOrganization != null && isAllOrganization.equals("TRUE")) {
			sNotice.setOrganization(organizationDao.findAll());
		}
		if(StringUtil.isNotBlank(sNotice.getId())){
//			systemNoticeVo.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			systemNotice = systemNoticeDao.findById(sNotice.getId());
			systemNotice.setTitle(sNotice.getTitle());
			systemNotice.setIsReading(sNotice.getIsReading());
			String content=sNotice.getContent();			
			try {
				systemNotice.setContent(URLDecoder.decode(content,"UTF-8"));				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			user.setUserId(userService.getCurrentLoginUser().getUserId());
			systemNotice.setModifyUser(user);
			systemNotice.setModyfyTime(DateTools.getCurrentDateTime());
			systemNotice.setOrganization(sNotice.getOrganization());
			systemNotice.setRole(sNotice.getRole());
			systemNotice.setNoticeType(sNotice.getNoticeType());
			String hql = "delete from SystemNoticeUser s where 1=1";
			String hqlWhere="";
			if (StringUtils.isNotBlank(systemNotice.getId())) {
				hqlWhere+=" and systemNotice.id = :systemNoticeId ";
				params.put("systemNoticeId", systemNotice.getId());
			}
			hql += hqlWhere;
			systemNoticeUserDao.excuteHql(hql, params);
			
			//UEditor原上传文件处理
			delFromSystemNoticeFile(systemNotice.getId());
		}else{			
//			systemNoticeVo.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			systemNotice = sNotice;
			user.setUserId(userService.getCurrentLoginUser().getUserId());
			systemNotice.setCreateUser(user);
			systemNotice.setCreateTime(DateTools.getCurrentDateTime());
			systemNotice.setId(null);
			
			String content=sNotice.getContent();			
			try {
				systemNotice.setContent(URLDecoder.decode(content,"UTF-8"));				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(dataFile!=null && dataFile.getSize()>0){
			String fileName=dataFile.getOriginalFilename().substring  
					(0,dataFile.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=dataFile.getOriginalFilename().replace(fileName, "");
			String aliName = UUID.randomUUID().toString()+puff;//阿里云上面的文件名
			systemNotice.setFilePath(aliName);
			systemNotice.setRealFileName(fileName);
			if(systemNotice.getNoticeType().getId().equals("DAT0000000344") && dataFile.getContentType().substring(0, 5).equals("image")){
				this.saveFileToRemoteServer(dataFile, aliName);
				//下面的注释的代码为限制上传图片附件的长宽大小
//				BufferedImage bufferedImage;
//				try {
//					bufferedImage = ImageIO.read(dataFile.getInputStream());
//					System.out.println("image; height= "+bufferedImage.getHeight()+" width= "+bufferedImage.getWidth());
//					if(bufferedImage.getHeight()<=500 && bufferedImage.getWidth()<=1000){
//						this.saveFileToRemoteServer(dataFile, fileName);
//					}else{
//						//图片大小不符合要求，公告插入不成功 。。。
//						
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}else{
				this.saveFileToRemoteServer(dataFile, aliName);
			}
		}
		systemNoticeDao.save(systemNotice);
		//UEditor上传文件处理
		String filePaths = uploadFileService.listUploadFileFromContent(systemNotice.getContent());
		if(StringUtils.isNotBlank(filePaths)) {
			uploadFileService.updateUploadFileStatus(filePaths, UploadFileStatus.INUSE.getValue());  //更新上传的文件状态为在使用
			List<UploadFileRecord> uploadFileRecordList = uploadFileService.getListByFilePath(filePaths);
	        for(UploadFileRecord uploadFileReocrd : uploadFileRecordList){
	        	Map<String, Object> insertParams = new HashMap<String, Object>();
	        	String sql = "insert into system_notice_file(notice_id, file_id) VALUES(:systemNoticeId, :uploadFileReocrdId)";
	        	insertParams.put("systemNoticeId", systemNotice.getId());
	        	insertParams.put("uploadFileReocrdId", uploadFileReocrd.getId());
	        	systemNoticeUserDao.excuteSql(sql, insertParams);
	        }
		}
		//创建推送的消息
        mobilePushMsgService.createPushMsgBySystemNotice(systemNotice);
        
        if(systemNotice.getId()!=null && StringUtils.isNotBlank(systemNotice.getId())){
        	HttpSession session=SpringMvcUtils.getSession();
        	session.setAttribute("param", systemNotice.getId());       	
        }
       
		
	}
	
	private void delFromSystemNoticeFile(String noticeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String selectFilePathSql = " select ufr.file_path from "
				+ " (select * from system_notice_file where notice_id = :noticeId) snf"
				+ " left join upload_file_record ufr on ufr.id = snf.file_id";
		params.put("noticeId", noticeId);
		Query q = systemNoticeUserDao.getCurrentSession().createSQLQuery(selectFilePathSql);
		systemNoticeUserDao.setParamsIfNotNull(QueryTypeEnum.SQL, params, q);
		List<String> filePathList = (List<String>) q.list();
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
			String delSql = "delete from system_notice_file where notice_id = :noticeId ";
			systemNoticeUserDao.excuteSql(delSql, params);
		}
	}
	/**
	 * 把上传的档案存到阿里云
	 * @param myfile1
	 * @param fileName
	 */
	public void saveFileToRemoteServer(MultipartFile myfile1, String fileName)  {
		try {
			AliyunOSSUtils.put(fileName, myfile1.getInputStream(), myfile1.getSize());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
	}

	public void saveFileToRemoteServer1(InputStream  is, String fileName,long size)  {
		try {
			AliyunOSSUtils.put(fileName,is, size);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
	}
	
	/**
	 * 删除公告
	 * */
	@Override
	public void deleteSystemNoticeById(SystemNotice systemNotice) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" delete from SystemNotice where id = :systemNoticeId ");
		params.put("systemNoticeId", systemNotice.getId());
		systemNoticeDao.excuteHql(hql.toString(), params);
		StringBuffer sql=new StringBuffer();
		sql.append(" delete from system_notice_role where notice_id= :systemNoticeId ");
		systemNoticeUserDao.excuteSql(sql.toString(), params);
		//删除通过UEditor上传的文件
		delFromSystemNoticeFile(systemNotice.getId());
	}


	/**
	 * 查询当前登录用户未读的必读公告
	 * */
	@Override
	public List<WelcomeNoticeVo> getSystemNoticeUserList(
			SystemNoticeUserVo systemNoticeUserVo) {
		User curUser = userService.getCurrentLoginUser();
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct sn.* from SYSTEM_NOTICE sn ");
		sql.append("inner join SYSTEM_NOTICE_ORG o  on sn.id=o.notice_id ");
		sql.append("inner join SYSTEM_NOTICE_ROLE r on sn.id=r.notice_id  ");
		sql.append(" where  sn.IS_READING='1' ");
		sql.append("and concat(o.org_id,r.role_id) in (select concat(organization_Id,role_id) from user_organization_role where user_ID='"+curUser.getUserId()+"')  ");
		//sql.append("and r.role_id in (select roleID from user_role where userID='"+curUser.getUserId()+"')  ");
		sql.append("and sn.id in (select id from  system_notice where id not in(select system_notice_id from system_notice_user where user_ID='"+curUser.getUserId()+"'))  ");
		sql.append(" order by sn.CREATE_TIME desc ");
		
		List<SystemNotice> list = systemNoticeDao.findBySql(sql.toString(), new HashMap<String, Object>());
		return HibernateUtils.voListMapping(list, WelcomeNoticeVo.class);
	}


	/**
	 * 保存已读公告
	 * */
	@Override
	public void saveHasReadedNotice(SystemNoticeUser systemNoticeUser) {
		//查看记录是否已经存在
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT COUNT(1) FROM SYSTEM_NOTICE_USER WHERE USER_ID = '"+userService.getCurrentLoginUser().getUserId()+"' AND SYSTEM_NOTICE_ID = :systemNoticeId ");
		params.put("systemNoticeId", systemNoticeUser.getSystemNotice().getId());
		int count = systemNoticeUserDao.findCountSql(hql.toString(), params);
		if(count<=0){
			systemNoticeUser.setUser(userService.getCurrentLoginUser());
			systemNoticeUser.setReadingTime(DateTools.getCurrentDateTime());
			systemNoticeUserDao.save(systemNoticeUser);
		}
		
	}


	/**
	 * 查询已读公告用户
	 * */
	@Override
	public List<SystemNoticeUserVo> getHasReadedSystemNoticeUsers(SystemNoticeUserVo systemNoticeUserVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from SystemNoticeUser where 1=1 ");
		if(StringUtils.isNotBlank(systemNoticeUserVo.getSystemNoticeId())){
			hql.append(" and systemNotice.id = :systemNoticeId ");
			params.put("systemNoticeId", systemNoticeUserVo.getSystemNoticeId());
		}
		List<SystemNoticeUser> list = systemNoticeUserDao.findAllByHQL(hql.toString(), params);
		List<SystemNoticeUserVo> voList = new ArrayList<SystemNoticeUserVo>();
		for(SystemNoticeUser syst : list){
			SystemNoticeUserVo vo = HibernateUtils.voObjectMapping(syst, SystemNoticeUserVo.class);
			Organization organization = organizationDao.findById(vo.getUserOrgId());
			vo.setUserOrg(organization.getName());
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 获取noticeType公告类型的前num条记录
	 */
	@Override
	public List<SystemNoticeVo> getSystemNoticeByTypeTopNum(String recordId, String noticeType,String num){
		return systemNoticeDao.getSystemNoticeByTypeTopNum(recordId, noticeType, num);
	}

	@Override
	public List<SystemNoticeVo> getSystemNoticeOneAndMore(String recordId,String noticeType,String num){
		return systemNoticeDao.getSystemNoticeOneAndMore(recordId,noticeType, num);
	}
	
	@Override
	public void saveSystemNotice(SystemNotice systemNotice) {
		systemNoticeDao.save(systemNotice);
	}


	@Override
	public DataPackage getSystemNoticeListByRoles(
			SystemNoticeVo systemNoticeVo, DataPackage dp) {
//		return systemNoticeDao.getSystemNoticeListByRoles(systemNoticeVo, dp);
		return systemNoticeDao.getSystemNoticeListByRolesFuckCriteria(systemNoticeVo, dp);
	}
	
	@Override
	public DataPackage getSystemNoticeListByRolesWelCome(
			SystemNoticeVo systemNoticeVo, DataPackage dp) {
//		return systemNoticeDao.getSystemNoticeListByRoles(systemNoticeVo, dp);
		return systemNoticeDao.getSystemNoticeListByRolesFuckCriteriaWelCome(systemNoticeVo, dp);
	}
	
	@Override
	public SystemNoticeVo findSystemNoticeVoById(String id) {
		SystemNotice systemNotice = systemNoticeDao.findById(id);
		SystemNoticeVo systemNoticeVo = HibernateUtils.voObjectMapping(systemNotice, SystemNoticeVo.class);
		Organization organization = organizationDao.findById(systemNoticeVo.getCreateUserOrgId());
		systemNoticeVo.setCreateUserOrg(organization.getName());
		if(StringUtil.isNotEmpty(systemNoticeVo.getModifyUserOrgId())){//新增的 公告没有modifyId 会报空指针
			organization = organizationDao.findById(systemNoticeVo.getModifyUserOrgId());
			systemNoticeVo.setModifyUserOrg(organization.getName());
		}
		//对图片的宽度进行限制
		StringBuilder onload = new StringBuilder();
		onload.append("\\<img onload='if(this.width>697){");
		onload.append("this.height=this.height*697/this.width;this.width=697;}'");
		StringBuffer content = new StringBuffer();
		Pattern pattern = Pattern.compile("\\<img");
		Matcher matcher = pattern.matcher(systemNoticeVo.getContent());
		Boolean result = matcher.find();
		while(result)
		{
			matcher.appendReplacement(content, onload.toString());
			result = matcher.find();
		}
		matcher.appendTail(content);
		systemNoticeVo.setContent(content.toString());
		return systemNoticeVo;
	}

	@Override
	public Map getTodayAndMonthTotal() {
		return reportDao.getTodayAndMonthTotal();
	}
	
	@Override
	public List getTodayAndMonthTotalComsume() {
		return reportDao.getTodayAndMonthTotalComsume();
	}
	
	@Override
	public List getTodayAndMonthTotalIncome() {
		return reportDao.getTodayAndMonthTotalIncome();
	}

	/**
	 * 向手机端推送 系统公告信息
	 */
	@Override
	public void pushSystemNoticeToMobile(SystemNotice systemNotice, String isAllOrganization) {
		HttpSession session=SpringMvcUtils.getSession();
		String id="";
		SystemNotice notice=new SystemNotice();
		String time="";
		if(session.getAttribute("param")!=null){
			id=session.getAttribute("param").toString();
			notice=systemNoticeDao.findById(id);
			time=notice.getCreateTime();
		}
		
		StringBuffer orgArr=new StringBuffer();
		StringBuffer roleArr=new StringBuffer();
		if (systemNotice!=null) {
			String msgContent=systemNotice.getContent();					
			if(msgContent.length()>20){
				msgContent=msgContent.substring(0, 20);//传部分内容到手机端
			}				
			
			List<Organization> orgList = systemNotice.getOrganization();
			if (isAllOrganization.equals("TRUE")) {
				orgList = organizationDao.findAll();
			}
				
			List<Role> roleList=systemNotice.getRole();
			for(int j=0;j<orgList.size();j++){
				orgArr.append(orgList.get(j).getId()).append(",");
			}
			if(orgArr.length() > 0){
				orgArr.setLength(orgArr.length() - 1);
			}
			for(int j=0;j<roleList.size();j++){
				roleArr.append("").append(roleList.get(j).getRoleId()).append(",");
			}
			if(roleArr.length() > 0){
				roleArr.setLength(roleArr.length() - 1);
			}
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuffer sql=new StringBuffer();
			sql.append("select DISTINCT mu.* from mobile_user mu, user_organization_role uor where  mu.USER_ID=uor.user_ID and mu.USER_TYPE='"+MobileUserType.MANAGE+"' ");
			if (roleArr.length() > 0) {
				sql.append("  and uor.role_ID in (:roleArr)  ");
				params.put("roleArr", roleArr.toString().split(","));
			}
			if (orgArr.length() > 0) {
				sql.append("  and uor.organization_ID in (:orgArr) ");
				params.put("orgArr", orgArr.toString().split(","));
			}
			List<MobileUser> muserList=mobileUserDao.findBySql(sql.toString(), params);
			if(muserList!=null && muserList.size()>0){
				for(MobileUser user:muserList){
					if(user!=null ){
						mobilePushMsgService.pushMsg(user, msgContent,id,PushMsgType.SYSTEM_NOTICE.toString(),time, null, "", "星火管理端");
					}
					
					
				}
				
			}
		
		}	
	}

	@Override
	public List getTodayMonthConsumeByType(String productType) {
		if(StringUtil.isBlank(productType)){
			return null;
		}
		if(productType.equals(ProductType.ONE_ON_ONE_COURSE.getValue())){
			return reportDao.getTodayAndMonthTotalComsume();
		}else if (productType.equals(ProductType.SMALL_CLASS.getValue())) {
			return reportDao.getTodayMonthSmallConsume();
		}else if (productType.equals(ProductType.OTHERS.getValue())) {
			return reportDao.getTodayMonthOtherConsume();
		}else if (productType.equals("TOTAL")) {
			return reportDao.getTodayMonthTotalConsume();
		}
		return null;
	}

}
