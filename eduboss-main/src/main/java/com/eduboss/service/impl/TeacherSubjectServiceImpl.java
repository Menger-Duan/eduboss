package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.common.Constants;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.TeacherSubjectDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TeacherSubjectVo;
import com.eduboss.domainVo.UserVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TeacherSubjectRequestVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.StudentService;
import com.eduboss.service.TeacherSubjectService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service
public class TeacherSubjectServiceImpl implements TeacherSubjectService {
	
	@Autowired
	TeacherSubjectDao teacherSubjectDao;
	
	@Autowired
	OrganizationDao organizationDao;
	
	@Autowired
	DataDictDao dataDictDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;

    @Autowired
    private StudentService studentService;
	
	@Override
	public DataPackage getTeacherSubjectList(TeacherSubject teacherSubject, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from TeacherSubject where 1=1 ";
		User user = userService.getCurrentLoginUser();
		hql += " and teacher.userId = :teacherId ";
		if (teacherSubject.getTeacher() != null) {
			params.put("teacherId", teacherSubject.getTeacher().getUserId());
		} else {
			params.put("teacherId", user.getUserId());
		}
		if (teacherSubject.getGrade() != null) {
			hql += " and grade.id = :gradeId ";
			params.put("gradeId", teacherSubject.getGrade().getId());
		}
		if (teacherSubject.getSubject() != null) {
			hql += " and subject.id = :subjectId ";
			params.put("subjectId", teacherSubject.getSubject().getId());
		}
		if (StringUtils.isNotBlank(teacherSubject.getSubjectStatus())) {
			hql += " and subjectStatus = :subjectStatus ";
			params.put("subjectStatus", teacherSubject.getSubjectStatus());
		}
		hql += " order by createTime desc ";
		dp = teacherSubjectDao.findPageByHQL(hql, dp, true, params);
		dp.setDatas(HibernateUtils.voListMapping((List<TeacherSubject>)dp.getDatas(), TeacherSubjectVo.class));
		return dp;
	}

	@Override
	public void deleteTeacherSubject(TeacherSubject teacherSubject) {
		teacherSubjectDao.delete(teacherSubject);
	}

	@Override
	public void saveOrUpdateTeacherSubject(TeacherSubject teacherSubject) {
		User user = userService.getCurrentLoginUser();
		if(teacherSubject.getId() > 0){
			teacherSubject.setCreateTime(DateTools.getCurrentDateTime());
			teacherSubject.setCreateUserId(user.getUserId());
		}
		if(teacherSubject.getGrade().getId().indexOf(",")==-1){
			if (this.findOneTeacherSubject(teacherSubject) != null) {
				throw new ApplicationException("已存在相同科目，年级的可教课程");
			}
			teacherSubjectDao.save(teacherSubject);
		}else{
			for(String grade : teacherSubject.getGrade().getId().split(",")){
				TeacherSubject newTeacherSubject= new TeacherSubject();
				newTeacherSubject.setCreateTime(DateTools.getCurrentDateTime());
				newTeacherSubject.setCreateUserId(user.getUserId());
				newTeacherSubject.setSubjectStatus(teacherSubject.getSubjectStatus());
				newTeacherSubject.setGrade(new DataDict(grade));
				newTeacherSubject.setSubject(teacherSubject.getSubject());
				newTeacherSubject.setTeacher(teacherSubject.getTeacher());
				if (this.findOneTeacherSubject(newTeacherSubject) != null) {
					throw new ApplicationException("已存在相同科目，年级的可教课程");
				}
				teacherSubjectDao.save(newTeacherSubject);
			}
		}
		
	}

	@Override
	public TeacherSubject findOneTeacherSubject(TeacherSubject teacherSubject) {
		return teacherSubjectDao.findOneTeacherSubject(teacherSubject);
	}

	@Override
	public TeacherSubject disableTeacherSubjectById(String teacherSubjectId) {
		TeacherSubject teacherSubject = teacherSubjectDao.findById(Integer.parseInt(teacherSubjectId));
		if("0".equals(teacherSubject.getSubjectStatus()))
			teacherSubject.setSubjectStatus("1");
		else
			teacherSubject.setSubjectStatus("0");
		teacherSubjectDao.save(teacherSubject);
		return teacherSubject;
	}

	@Override
	public List<UserVo> getTeacherSubjectByGradeSubject(String gradeId, String subjetId) {
		String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(gradeId)){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			hqlWhere+=" and subject.id = :subjectId ";
			params.put("subjectId", subjetId);
		}
		hql+=hqlWhere.substring(4)+")";
		
		// 加入校区限制
		Organization campus = userService.getBelongCampus();
		hql+= " and organizationId in (select id from Organization where orgLevel like :orgLevel )";
		params.put("orgLevel", campus.getOrgLevel()+"%");
		hql+=" order by account";
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}
	
	@Override
	public List<User> getTeacherSubjectByGradesSubjects(Set<TeacherSubjectRequestVo> teachersrv) {
		String sql = "select * from user u";
		String hqlWhere = " where 1=1";
		Map<String, Object> params = Maps.newHashMap();
		if (teachersrv.size() > 0) {
			Iterator<TeacherSubjectRequestVo> it = teachersrv.iterator(); 
			int i = 0;
			while (it.hasNext()) {  
				TeacherSubjectRequestVo vo = it.next(); 
				params.put("subject"+i, vo.getSubjectId());
				params.put("grade"+i, vo.getGradeId());
				sql += ",(select TEACHER_ID ID from teacher_subject where SUBJET= :subject"+i+"  and SUBJECT_STATUS='0'" + " and GRADE= :grade"+i+" ) t" + i;
				if (i != 0) {
					hqlWhere += " and t0.ID=t" + i + ".ID"; 
				}
				i++;
			}
//			i = 0;
//			while (it.hasNext()) {  
//				if (i != 0) {
//					hqlWhere += " and t0.ID=t" + i + ".ID"; 
//				}
//				i++;
//			} 
			hqlWhere += " and u.USER_ID= t0.ID";
		}
		
		// 加入校区限制
		Organization campus = userService.getBelongCampus();
		params.put("orgLevel", campus.getOrgLevel()+"%");
		hqlWhere += " and (u.organizationID in (select org.id from organization org  where org.orgLevel like :orgLevel )) ";
//		hqlWhere += " and organizationId in (select id from organization where orgLevel like '"  + campus.getOrgLevel() + "%')";
		hqlWhere += " order by u.account";
		sql += hqlWhere;
		List<User> list= userDao.findBySql(sql,params);
		return list;
	}

    @Override
    public List<UserVo> getTeacherSubjectByStudentSubject(String studentId, String subjetId) {
        StudentVo stu = studentService.findStudentById(studentId);
        if(stu == null){
            throw new ApplicationException("找不到学生，ID：" + studentId);
        }
        return this.getTeacherSubjectByGradeSubject(stu.getGradeId(),subjetId);
    }

    /**
	 * 查询跨校区老师
	 * @param gradeId
	 * @param subjetId
	 * @return
	 */
	@Override
	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubject(String gradeId, String subjetId) {
		if(org.apache.commons.lang3.StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return getOtherOrganizationTeacherSubjectByGradeSubjectNew(gradeId,subjetId);
		}else{
			return getOtherOrganizationTeacherSubjectByGradeSubjectOld(gradeId,subjetId);
		}
	}

	@Deprecated
	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubjectOld(String gradeId, String subjetId) {
		String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(gradeId)){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			hqlWhere+=" and subject.id = :subjectId ";
			params.put("subjectId", subjetId);
		}
		hql+=hqlWhere.substring(4)+")";
		// 加入校区限制
		Organization campus = userService.getBelongCampus();
		hql+= " and organizationId not in (select id from Organization where orgLevel like '"  + campus.getOrgLevel() + "%')";
//		// 加入本分公司限制
		params.put("orgLevel", campus.getOrgLevel()+"%");
		// 跨校区老师属于部门的也查出来
		hql+= " and userId in (select uo.user.userId from UserOrganization uo ";
		hql+= " where uo.organization.orgLevel like :orgLevel and uo.organization.id != uo.user.organizationId )";
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}

	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubjectNew(String gradeId, String subjetId) {
		String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(gradeId)){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			hqlWhere+=" and subject.id = :subjectId ";
			params.put("subjectId", subjetId);
		}
		hql+=hqlWhere.substring(4)+")";
		// 跨校区老师属于部门的也查出来
		hql+= " and userId in (select uo.user.userId from UserOrganizationRole uo ";
		hql+= " where uo.organization.orgLevel like :orgLevel)";
		Organization campus = userService.getBelongCampus();
		params.put("orgLevel", campus.getOrgLevel()+"%");

		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}
	
	@Override
	public List<User> getOtherOrganizationTeacherSubjectByGradesSubjects(Set<TeacherSubjectRequestVo> teachersrv) {
		
		String sql = "select * from user u";
		String hqlWhere = " where 1=1";
		Map<String, Object> params = Maps.newHashMap();
		if (teachersrv.size() > 0) {
			Iterator<TeacherSubjectRequestVo> it = teachersrv.iterator(); 
			int i = 0;
			while (it.hasNext()) {  
				TeacherSubjectRequestVo vo = it.next(); 
				params.put("subject"+i, vo.getSubjectId());
				params.put("grade"+i, vo.getGradeId());
				sql += ",(select TEACHER_ID ID from teacher_subject where SUBJET= :subject"+i+"  and SUBJECT_STATUS='0'" + " and GRADE= :grade"+i+" ) t" + i;
				if (i != 0) {
					hqlWhere += " and t0.ID=t" + i + ".ID";
				}
				i++;
			}
			hqlWhere += " and u.USER_ID= t0.ID";
		}
		Organization campus = userService.getBelongCampus();
		hqlWhere+= " and u.organizationID != :organizationId ";
		params.put("organizationId", campus.getId());
		params.put("orgLevel", campus.getOrgLevel()+"%");
//		 hqlWhere+= " and (u.USER_ID in (select ug.userID from user_organization ug  where ug.organizationID='" + campus.getId() + "'))";		
		hqlWhere+=" and (u.USER_ID in (select ug.USER_ID from user_organization_role ug ,Organization org ,User us";
		hqlWhere+=" where ug.organization_Id = org.id and ug.USER_ID=us.user_Id ";
		hqlWhere+=" and org.orgLevel like :orgLevel and ug.organization_Id != us.organizationId ) ) ";
		hqlWhere+=" order by u.account";
		sql += hqlWhere;
		List<User> list= userDao.findBySql(sql,params);
		return list;
	}

    /**
     * 查询跨校区老师
     *
     * @param studentId
     * @param subjetId
     * @return
     */
    @Override
    public List<UserVo> getOtherOrganizationTeacherSubjectByStudentSubject(String studentId, String subjetId) {
        StudentVo stu = studentService.findStudentById(studentId);
        if(stu == null){
            throw new ApplicationException("找不到学生，ID：" + studentId);
        }
        return this.getOtherOrganizationTeacherSubjectByGradeSubject(stu.getGradeId(),subjetId);
    }

    @Override
	public List<User> getTeacherList(String teacherName) {
		/**
		 * 权限控制 
		 * 如果角色为老师 则可以维护自己本人的信息
		 * 如果为教务专员 则可以维护本教区的老师信息
		 */
		List<User> users =new ArrayList<User>();
		if(userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)){
			String roleCode="TEATCHER";
			users = userService.getUserByRoldCodes(roleCode,teacherName);
		}else if(userService.isCurrentUserRoleCode(RoleCode.TEATCHER)){
			//String id=userService.getCurrentLoginUser().getUserId();
			//User u=userDao.findById(userService.getCurrentLoginUser().getUserId()) ;
			users.add(userService.getCurrentLoginUser());
		}
		return users;
	}
    
    public List<UserVo> getTeacherListByCampusId(String campusId) {
    	String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		hql+=hqlWhere.substring(4)+")";
		
		// 加入校区限制  校区下面的部门里面的老师也要一起显示出来
		hql+= " and (organizationId = :campusId or organizationId in( select id from Organization where parentId= :campusId ))";
		hql+=" order by account";
		Map<String, Object> params = Maps.newHashMap();
		params.put("campusId", campusId);
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
    }
    
    @Override
	public List<UserVo> getOtherOrganizationTeacherByCampusId(String campusId) {
    	String hql="select u from User u, Organization org where u.enableFlg = '0' and u.userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		hql+=hqlWhere.substring(4)+")";
		
		hql+= " and (case when org.orgType = 'CAMPUS' then u.organizationId else org.parentId end) != :campusId ";	
		hql+= " and u.organizationId = org.id ";
		
		hql+= " and u.userId in(select uo.user.userId from UserOrganization uo where (case when uo.organization.orgType = 'CAMPUS' then uo.organization.id else uo.organization.parentId end) = :campusId ) ";
		hql+=" order by u.account";
		Map<String, Object> params = Maps.newHashMap();
		params.put("campusId", campusId);
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
    }

	@Override
	public List<UserVo> getOtherOrganizationTeacherByCampusIdNew(String campusId) {
		String hql="select u from User u, Organization org where u.enableFlg = '0' and u.userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		hql+=hqlWhere.substring(4)+")";

		hql+= " and (case when org.orgType = 'CAMPUS' then u.organizationId else org.parentId end) != :campusId ";
		hql+= " and u.organizationId = org.id ";

		hql+= " and u.userId in(select uo.user.userId from UserOrganizationRole uo where (case when uo.organization.orgType = 'CAMPUS' then uo.organization.id else uo.organization.parentId end) = :campusId ) ";
		hql+=" order by u.account";
		Map<String, Object> params = Maps.newHashMap();
		params.put("campusId", campusId);
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}
    
    /**
     * 根据年级，科目，校区查询老师
     * 
     */
	public List<UserVo> getTeacherSubjectByGradeSubjectCampusId(String gradeId, String subjetId,String campusId) {
		String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(gradeId)){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			hqlWhere+=" and subject.id = :subjetId ";
			params.put("subjetId", subjetId);
		}
		hql+=hqlWhere.substring(4)+")";
		
		// 加入校区限制
		if(StringUtils.isNotBlank(campusId) && !campusId.equals("")){
			Organization campus = organizationDao.findById(campusId);
			hql+= " and organizationId in (select id from Organization where orgLevel like :orgLevel )";	
			params.put("orgLevel", campus.getOrgLevel()+"%");
		}		
		hql+=" order by account";
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}
	/**
	 * 根据年级，科目，校区查询跨校区老师
	 */
	@Override
	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubjectCampusId(String gradeId, String subjetId, String campusId) {
		if(org.apache.commons.lang3.StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return getOtherOrganizationTeacherSubjectByGradeSubjectCampusIdNew(gradeId,subjetId,campusId);
		}else{
			return getOtherOrganizationTeacherSubjectByGradeSubjectCampusIdOld(gradeId,subjetId,campusId);
		}
	}

	@Deprecated
	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubjectCampusIdOld(String gradeId, String subjetId,String campusId) {
		String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(gradeId)){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			hqlWhere+=" and subject.id= :subjectId ";
			params.put("subjectId", subjetId);
		}
		hql+=hqlWhere.substring(4)+")";

		// 加入校区限制
		Organization campus=new Organization();
		if(StringUtils.isNotBlank(campusId) && !campusId.equals("")){
			campus = organizationDao.findById(campusId);
		} else {
			campus = organizationDao.findById("000001");
		}
		hql+= " and organizationId != :organizationId ";
		params.put("organizationId", campus.getId());
		// 跨校区老师属于部门的也查出来
		hql+= " and userId in (select uo.user.userId from UserOrganization uo ";
		hql+= " where uo.organization.orgLevel like :orgLevel and uo.organization.id != uo.user.organizationId )";
		params.put("orgLevel", campus.getOrgLevel()+"%");
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}

	public List<UserVo> getOtherOrganizationTeacherSubjectByGradeSubjectCampusIdNew(String gradeId, String subjetId,String campusId) {
		String hql=" from User where enableFlg = '0' and userId in ( select teacher.userId from TeacherSubject where ";
		String hqlWhere=" and subjectStatus='0' ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotEmpty(gradeId)){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			hqlWhere+=" and subject.id= :subjectId ";
			params.put("subjectId", subjetId);
		}
		hql+=hqlWhere.substring(4)+")";

		// 加入校区限制
		Organization campus=new Organization();
		if(StringUtils.isNotBlank(campusId) && !campusId.equals("")){
			campus = organizationDao.findById(campusId);
		} else {
			campus = organizationDao.findById("000001");
		}
		hql+= " and organizationId != :organizationId ";
		params.put("organizationId", campus.getId());
		// 跨校区老师属于部门的也查出来
		hql+= " and userId in (select uo.user.userId from UserOrganizationRole uo ";
		hql+= " where uo.organization.orgLevel like :orgLevel and uo.organization.id != uo.user.organizationId )";
		params.put("orgLevel", campus.getOrgLevel()+"%");
		List<User> list= userDao.findAllByHQL(hql,params);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}


	@Override
	public Map<String, Object> getTeacherListByBranchId() {
		Map<String, Object> map = new HashMap<String, Object>();
		Organization organization = userService.getBelongBranch();
		StringBuffer query = new StringBuffer();
		query.append(" select u.USER_ID as teacherId,u.`NAME` as teacherName from `user` u,organization o ,user_role ur, role r where u.organizationID =o.id and u.ENABLE_FLAG = '0' ");
		query.append(" and u.USER_ID = ur.userID and ur.roleID = r.id and r.roleCode='"+ RoleCode.TEATCHER + "'  ");
		query.append(" and o.orgLevel like '"+organization.getOrgLevel()+"%'");
		List<Map<Object, Object>> result =organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
		map.put("teachers", result);
		return map;
	}

	@Override
	public Map<String, Object> getTeacherListByBranchIdNew() {
		Map<String, Object> map = new HashMap<String, Object>();
		Organization organization = userService.getBelongBranch();
		StringBuffer query = new StringBuffer();
		query.append(" select u.USER_ID as teacherId,u.`NAME` as teacherName from `user` u,organization o ,user_organization_role ur, role r where u.organizationID =o.id and u.ENABLE_FLAG = '0' ");
		query.append(" and u.USER_ID = ur.user_ID and ur.role_ID = r.id and r.roleCode='"+ RoleCode.TEATCHER + "'  ");
		query.append(" and o.orgLevel like '"+organization.getOrgLevel()+"%'");
		List<Map<Object, Object>> result =organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
		map.put("teachers", result);
		return map;
	}

	@Override
	@Deprecated
	public List getTeacherListForSelect(String gradeId, String subjetId, String[] campusId, String brenchId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		StringBuilder sqlwhere = new StringBuilder();

		sql2.append(" select DISTINCT u.name,u.USER_ID,o.id campusId ,o.name campusName" +
				" from user u ")
				.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
				.append(" left join organization o on u.organizationID = o.id")
				.append(" left join user_role ur on ur.userID=u.USER_ID")
				.append(" left join role r on ur.roleID=r.id")
				.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");

		 sql.append(" select DISTINCT u.name,u.USER_ID,o2.id campusId ,o2.name campusName" +
				 " from user u ")
			.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
			.append(" left join user_organization uo on uo.userID= u.USER_ID")
			.append(" left join organization o on uo.organizationID = o.id")
			.append(" left join organization o2 on u.organizationID = o2.id")
			.append(" left join user_role ur on ur.userID=u.USER_ID")
			.append(" left join role r on ur.roleID=r.id")
			.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");

		if(StringUtils.isNotEmpty(gradeId)){
			sqlwhere.append(" and ts.GRADE =:gradeId");
			params.put("gradeId",gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			sqlwhere.append(" and ts.SUBJET =:subjetId");
			params.put("subjetId",subjetId);
		}

		if(campusId!=null){
			sqlwhere.append(" and (1=2 ");
			int i =0;
			for(String campus:campusId) {
				Organization org = organizationDao.findById(campus);
				sqlwhere.append(" or o.orgLevel like:campusId"+i);
				params.put("campusId"+i, org.getOrgLevel()+"%");
				i++;
			}
			sqlwhere.append(") ");
		}

		if(StringUtils.isNotBlank(brenchId)){
			Organization brench = organizationDao.findById(brenchId);
			sqlwhere.append(" and o.orgLevel like :brenchId");
			params.put("brenchId",brench.getOrgLevel()+"%");
		}

		sqlwhere.append(" and (1=2  ");
		for(Organization o:userService.getCurrentLoginUser().getOrganization()){
			sqlwhere.append( " or o.orgLevel like '"+o.getOrgLevel()+"%'");
		}
		sqlwhere.append(" )");


		sqlwhere.append(" order by u.USER_ID");

		sql.append(sqlwhere);
		sql2.append(sqlwhere);
		List<Map<Object,Object>> list=teacherSubjectDao.findMapBySql(sql.toString(),params);
		List<Map<Object,Object>> list2=teacherSubjectDao.findMapBySql(sql2.toString(),params);
		List<Map<Object,Object>> returnList= new ArrayList<>();

		for (Map map :list){
			if(list2.contains(map)){
				continue;
			}
			map.put("isOtherCampusTeahcer","1");
			returnList.add(map);
		}

		for (Map map :list2){
			map.put("isOtherCampusTeahcer","0");
			returnList.add(map);
		}

		return returnList;
	}
	
	@Override
	@Deprecated
	public List getTeachersForSelect(String gradeId, String subjetId, String[] campusId, String brenchId) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		StringBuilder sql = new StringBuilder();
////		StringBuilder sql2 = new StringBuilder();
//		StringBuilder sqlwhere = new StringBuilder();
//
////		sql2.append(" select DISTINCT u.name,u.USER_ID,o.id campusId ,o.name campusName" +
////				" from user u ")
////				.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
////				.append(" left join organization o on u.organizationID = o.id")
////				.append(" left join user_role ur on ur.userID=u.USER_ID")
////				.append(" left join role r on ur.roleID=r.id")
////				.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");
//
//		 sql.append(" select DISTINCT u.name,u.USER_ID userId,CASE o2.orgType ")
//		    .append(" WHEN 'CAMPUS' THEN o2.id ")
//		    .append(" WHEN 'DEPARTMENT' THEN o3.id  ")
//		    .append(" ELSE o2.id ")
//		    .append(" END as campusId, ")
//		    .append(" CASE o2.orgType ")
//		    .append(" WHEN 'CAMPUS' THEN o2.name ")
//		    .append(" WHEN 'DEPARTMENT' THEN o3.name ")
//		    .append(" ELSE o2.name ")
//		    .append(" END as campusName " )
//		    .append(" from user u ")
//			.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
//			.append(" left join user_organization uo on uo.userID= u.USER_ID")
//			.append(" left join organization o on uo.organizationID = o.id")
//			.append(" left join organization o2 on u.organizationID = o2.id")
//			.append(" left join organization o3 on o2.parentID = o3.id")
//			.append(" left join user_role ur on ur.userID=u.USER_ID")
//			.append(" left join role r on ur.roleID=r.id")
//			.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");
//
//		if(StringUtils.isNotEmpty(gradeId)){
//			sqlwhere.append(" and ts.GRADE =:gradeId");
//			params.put("gradeId",gradeId);
//		}
//		if(StringUtils.isNotEmpty(subjetId)){
//			sqlwhere.append(" and ts.SUBJET =:subjetId");
//			params.put("subjetId",subjetId);
//		}
//
//		if(campusId!=null){
//			sqlwhere.append(" and (1=2 ");
//			int i =0;
//			for(String campus:campusId) {
//				Organization org = organizationDao.findById(campus);
//				sqlwhere.append(" or o.orgLevel like:campusId"+i);
//				params.put("campusId"+i, org.getOrgLevel()+"%");
//				i++;
//			}
//			sqlwhere.append(") ");
//		}
//
//		if(StringUtils.isNotBlank(brenchId)){
//			Organization brench = organizationDao.findById(brenchId);
//			sqlwhere.append(" and o.orgLevel like :brenchId");
//			params.put("brenchId",brench.getOrgLevel()+"%");
//		}
//
////		sqlwhere.append(" and (1=2  ");
////		for(Organization o:userService.getCurrentLoginUser().getOrganization()){
////			sqlwhere.append( " or o.orgLevel like '"+o.getOrgLevel()+"%'");
////		}
////		sqlwhere.append(" )");
//
//
//		sqlwhere.append(" order by u.USER_ID");
//
//		sql.append(sqlwhere);
////		sql2.append(sqlwhere);
//		List<Map<Object,Object>> list=teacherSubjectDao.findMapBySql(sql.toString(),params);
////		List<Map<Object,Object>> list2=teacherSubjectDao.findMapBySql(sql2.toString(),params);
////		List<Map<Object,Object>> returnList= new ArrayList<>();
//
//		return list;
////		for (Map map :list){
////			if(list2.contains(map)){
////				continue;
////			}
////			map.put("isOtherCampusTeahcer","1");
////			returnList.add(map);
////		}
////
////		for (Map map :list2){
////			map.put("isOtherCampusTeahcer","0");
////			returnList.add(map);
////		}
//
////		return returnList;
		
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		StringBuilder sqlwhere = new StringBuilder();

		sql2.append(" select DISTINCT u.name,u.USER_ID userId,o.id campusId ,o.name campusName" +
				" from user u ")
				.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
				.append(" left join organization o on u.organizationID = o.id")
				.append(" left join user_role ur on ur.userID=u.USER_ID")
				.append(" left join role r on ur.roleID=r.id")
				.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");

		 sql.append(" select DISTINCT u.name,u.USER_ID userId,o2.id campusId ,o2.name campusName" +
				 " from user u ")
			.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
			.append(" left join user_organization uo on uo.userID= u.USER_ID")
			.append(" left join organization o on uo.organizationID = o.id")
			.append(" left join organization o2 on u.organizationID = o2.id")
			.append(" left join user_role ur on ur.userID=u.USER_ID")
			.append(" left join role r on ur.roleID=r.id")
			.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");

		if(StringUtils.isNotEmpty(gradeId)){
			sqlwhere.append(" and ts.GRADE =:gradeId");
			params.put("gradeId",gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			sqlwhere.append(" and ts.SUBJET =:subjetId");
			params.put("subjetId",subjetId);
		}

		if(campusId!=null){
			sqlwhere.append(" and (1=2 ");
			int i =0;
			for(String campus:campusId) {
				Organization org = organizationDao.findById(campus);
				sqlwhere.append(" or o.orgLevel like:campusId"+i);
				params.put("campusId"+i, org.getOrgLevel()+"%");
				i++;
			}
			sqlwhere.append(") ");
		}

		if(StringUtils.isNotBlank(brenchId)){
			Organization brench = organizationDao.findById(brenchId);
			sqlwhere.append(" and o.orgLevel like :brenchId");
			params.put("brenchId",brench.getOrgLevel()+"%");
		}


        

		//sqlwhere.append(" order by u.USER_ID");

		sql.append(sqlwhere).append(" order by o2.orgLevel,u.USER_ID ");
		sql2.append(sqlwhere).append(" order by o.orgLevel,u.USER_ID ");;
		
		
		List<Map<Object,Object>> list=teacherSubjectDao.findMapBySql(sql.toString(),params);
		List<Map<Object,Object>> list2=teacherSubjectDao.findMapBySql(sql2.toString(),params);
		List<Map<Object,Object>> returnList= new ArrayList<>();

		for (Map map :list){
			if(list2.contains(map)){
				continue;
			}
			map.put("isOtherCampusTeahcer","1");
			returnList.add(map);
		}

		for (Map map :list2){
			map.put("isOtherCampusTeahcer","0");
			returnList.add(map);
		}

		return returnList;
	}


	/**
	 *
	 * 修改成新的组织架构角色表   user_organization_role
	 *
	 * @param gradeId
	 * @param subjetId
	 * @param campusId
	 * @param brenchId
	 * @return
	 */
	@Override
	public List getTeachersForSelectNew(String gradeId, String subjetId, String[] campusId, String brenchId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT u.name,u.USER_ID userId,o.id campusId ,o.name campusName,uor.is_main as isMain from user u ")
				.append(" left join teacher_subject ts on u.user_id = ts.TEACHER_ID")
				.append(" left join user_organization_role uor on uor.user_ID= u.USER_ID")
				.append(" left join organization o on uor.organization_ID = o.id")
				.append(" left join role r on uor.role_ID=r.id")
				.append(" where u.ENABLE_FLAG='0' and ts.SUBJECT_STATUS='0' and r.name ='老师'");

		if(StringUtils.isNotEmpty(gradeId)){
			sql.append(" and ts.GRADE =:gradeId");
			params.put("gradeId",gradeId);
		}
		if(StringUtils.isNotEmpty(subjetId)){
			sql.append(" and ts.SUBJET =:subjetId");
			params.put("subjetId",subjetId);
		}

		if(campusId!=null){
			sql.append(" and (1=2 ");
			int i =0;
			for(String campus:campusId) {
				Organization org = organizationDao.findById(campus);
				sql.append(" or o.orgLevel like:campusId"+i);
				params.put("campusId"+i, org.getOrgLevel()+"%");
				i++;
			}
			sql.append(") ");
		}

		if(StringUtils.isNotBlank(brenchId)){
			Organization brench = organizationDao.findById(brenchId);
			sql.append(" and o.orgLevel like :brenchId");
			params.put("brenchId",brench.getOrgLevel()+"%");
		}

		sql.append(" order by o.orgLevel,u.USER_ID ");


		List<Map<Object,Object>> list=teacherSubjectDao.findMapBySql(sql.toString(),params);
		List<Map<Object,Object>> returnList= new ArrayList<>();

		for (Map map :list){//主校区
			if(map.get("isMain")!=null && "0".equals(map.get("isMain").toString())){
				map.put("isOtherCampusTeahcer","0");
				returnList.add(map);
			}
		}

		for (Map map :list){//其他校区
			if(!returnList.contains(map)) {
				map.put("isOtherCampusTeahcer", "1");
				returnList.add(map);
			}
		}

		return returnList;
	}

}
