package com.eduboss.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.FeedBackDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.FeedBack;
import com.eduboss.domain.FeedbackReply;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.domainVo.FeedbackReplyVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class FeedBackDaoImpl extends GenericDaoImpl<FeedBack, String> implements FeedBackDao {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * 查询登录用户可以看到的反馈信息
	 */
	@Override
	public DataPackage getFeedBackList(DataPackage dataPackage,
			FeedBackVo feedBackvo,
			String startDate,
			String endDate) {
		User user=userService.getCurrentLoginUser();
		int i=0;
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		List<Role> roleList=userService.getCurrentLoginUserRoles();
		if(roleList.size()>0){
			for(Role role:roleList){
				if(RoleCode.FEEDBACK_ADMIN.equals(role.getRoleCode())){
					//当前登录人是反馈管理员
					Organization org=userService.getCurrentLoginUserOrganization();
					i++;
					hql.append("select fb from FeedBack fb, UserOrganizationRole uo where fb.createUser=uo.user.userId ");
					hql.append(" and fb.org.id=uo.organization.id and ou.organization.orgLevel like :orgLevel ");
					params.put("orgLevel", org.getOrgLevel()+"%");
				}
			}
		}
		if(i==0){
			hql.append("select fb from FeedBack fb where fb.createUser.userId = :createUser ");
			params.put("createUser", user.getUserId());
		}
		
		if(StringUtils.isNotBlank(feedBackvo.getTitle())){
			hql.append(" and fb.title like :title");
			params.put("title", "%"+feedBackvo.getTitle()+"%");
		}
		if(StringUtils.isNotBlank(feedBackvo.getBackType())){
			hql.append(" and fb.backType.id = :backType ");
			params.put("backType", feedBackvo.getBackType());
		}
		if(StringUtils.isNotBlank(feedBackvo.getIsBack())){
			hql.append(" and fb.isBack = :isBack ");
			params.put("isBack", feedBackvo.getIsBack());
		}
		if(StringUtils.isNotBlank(startDate)){
			hql.append(" and fb.createTime >= :startDate ");
			params.put("startDate", startDate+" 0000");
		}
		if(StringUtils.isNotBlank(endDate)){
			hql.append(" and fb.createTime <= :endDate ");
			params.put("endDate", endDate+" 2359 ");
		}
		hql.append(" order by fb.createTime desc");
		
		dataPackage = super.findPageByHQL(hql.toString(), dataPackage,true,params);
		
		List<FeedBack> list = (List<FeedBack>)dataPackage.getDatas() ;
		List<FeedBackVo> voList = new ArrayList<FeedBackVo>();
		voList = HibernateUtils.voListMapping(list, FeedBackVo.class);
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	/**
	 * 添加 反馈信息
	 */
	@Override
	public void saveOrUpdateFeedBack(FeedBack feedBack) {
		this.save(feedBack);
		
	}

	/**
	 * 查看详情
	 */
	
	@Override
	public FeedBackVo findFeedBackById(String id) {    
		Map<String, Object> params = Maps.newHashMap();
		FeedBack feedback=this.findById(id);
		FeedBackVo feedBackvo=HibernateUtils.voObjectMapping(feedback,FeedBackVo.class);
		StringBuffer sqlj=new StringBuffer();
		StringBuffer sqlo=new StringBuffer();
		StringBuffer sqlcount=new StringBuffer();
		sqlo.append("select o.name from organization o INNER JOIN user_dept_job d on o.id=d.dept_id where d.user_id= :userId and d.isMajorRole='0' ");
		sqlj.append(" select j.job_name from user_job j inner JOIN user_dept_job d on j.id=d.job_id where d.user_id= :userId and d.isMajorRole='0' ");
 		sqlcount.append("select count(1) from feedback where create_user= :userId ");
 		String deptName="";
 		String jobName="";
 		
 		params.put("userId",feedBackvo.getCreateUserId());
 		
 		int feedbackNumbers=this.findCountSql(sqlcount.toString(),params);
 		List<Map<Object,Object>> lo=super.findMapBySql(sqlo.toString(),params);
 		List<Map<Object,Object>> lj=super.findMapBySql(sqlj.toString(),params);
 		if(lo!=null && lo.size()>0){
 			deptName=lo.get(0).get("name").toString();
 		}
 		if(lj!=null && lj.size()>0){
 		   jobName=lj.get(0).get("job_name").toString();
 		}
		
 		User user=feedback.getCreateUser(); 		
 		feedBackvo.setJobName(jobName);
 		feedBackvo.setDeptName(deptName);
 		feedBackvo.setFeedbackNumbers(feedbackNumbers);
 		feedBackvo.setTels(user.getContact());
		
		
		
		return feedBackvo;
	}

	/**
	 * 删除反馈信息
	 * 记住还要删除回复表
	 */
	@Override
	public void deleteFeedBack(FeedBack feedBack) {		
		StringBuffer hql=new StringBuffer();
		hql.append("delete from FeedBack where id = :feedbackId ");
		StringBuffer sql=new StringBuffer();
		sql.append(" delete from feedback_reply where feedback_id = :feedbackId " );
		Map<String, Object> params = Maps.newHashMap();
		params.put("feedbackId", feedBack.getId());
		this.excuteHql(hql.toString(),params);
		this.excuteSql(sql.toString(),params);
	}
	
	

}
