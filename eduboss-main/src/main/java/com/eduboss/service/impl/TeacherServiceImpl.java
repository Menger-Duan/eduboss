package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.DataDictCategory;
import com.eduboss.dao.DataDictDao;
import com.eduboss.domain.DataDict;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.TeacherSubjectDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.TeacherInfoSubjectVo;
import com.eduboss.domainVo.TeacherInfoVo;
import com.eduboss.domainVo.TeacherSubjectVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.TeacherService;
import com.eduboss.service.TeacherSubjectService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeacherSubjectDao teacherSubjectDao;

	@Autowired
	private DataDictDao dataDictDao;

	@Override
	public Map<String, Object> getTeacherInfo(DataPackage dataPackage, String teacherId, String phone,String name) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<TeacherInfoVo> result = new ArrayList<TeacherInfoVo>();
		StringBuilder query = new StringBuilder();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Integer start = (dataPackage.getPageNo() - 1) * dataPackage.getPageSize();
				
		query.append(" select u.USER_ID as id,u.`NAME` as `name`,u.organizationID as orgId,o.`name` as orgName,o.city_id as regionId,rr.`name` as regionName,u.CREATE_TIME as createDate,u.MODIFY_TIME as updateDate ");
		query.append(" ,u.MAIL_ADDR as email,uj.ID as jobId,uj.JOB_NAME as jobName ");
		query.append(" from `user` u,organization o ,user_role ur,role r,region rr,user_dept_job udj,user_job uj ");
		query.append(" where  o.id = u.organizationID and udj.isMajorRole = 0 and u.ENABLE_FLAG = '0' and rr.id = o.city_id and u.USER_ID = udj.USER_ID and udj.JOB_ID = uj.ID ");
		query.append(" and r.roleCode='"+RoleCode.TEATCHER+"' and ur.userID = u.USER_ID and ur.roleID = r.id ");
//		query.append(" left join region r on r.id = o.city_id ");
//		query.append(" left join user_dept_job udj on u.USER_ID = udj.USER_ID ");
//		query.append(" left join user_job uj on udj.JOB_ID = uj.ID ");
//		query.append(" where udj.isMajorRole = 0 and u.ENABLE_FLAG = '0' ");
//		query.append(" and u.USER_ID in (SELECT ur.userID from user_role ur WHERE ur.roleID in (SELECT r.id from role r WHERE r.roleCode='"
//							+ RoleCode.TEATCHER + "' )) ");
		Map<String, Object> params = Maps.newHashMap();
		
		if (StringUtils.isNotBlank(teacherId)) {
			query.append(" and u.USER_ID = :teacherId ");
			params.put("teacherId", teacherId);
		}
		if (StringUtils.isNotBlank(phone)) {
			query.append(" and u.CONTACT = :phone ");
			params.put("phone", phone);
		}
		if(StringUtils.isNotBlank(name)){
			query.append(" and u.`NAME` like :name ");
			params.put("name", "%"+name+"%");
		}
		String count = "select count(*) from ( " + query.toString() + " ) countall ";
		query.append(" limit " + start + "," + dataPackage.getPageSize());
		List<Map<Object, Object>> list = userDao.findMapBySql(query.toString(),params);
		query.delete(0, query.length());
		TeacherInfoVo teacherInfoVo = null;
		int i = 0;
		for(Map<Object, Object> oMap:list){
			i = i+1;
			Map<String, Object> object = (Map)oMap;
			List<TeacherInfoSubjectVo> teacherSubjectVos = new ArrayList<TeacherInfoSubjectVo>();
			teacherInfoVo = new TeacherInfoVo();
			teacherInfoVo.setId(object.get("id")!=null?object.get("id").toString():"");
			teacherInfoVo.setName(object.get("name")!=null?object.get("name").toString():"");
			teacherInfoVo.setLevel("");// 教师等级 暂时为空没这个字段
			teacherInfoVo.setOrgId(object.get("orgId")!=null?object.get("orgId").toString():"");
			teacherInfoVo.setOrgName(object.get("orgName")!=null?object.get("orgName").toString():"");
			teacherInfoVo.setRegionId(object.get("regionId")!=null?object.get("regionId").toString():"");
			teacherInfoVo.setRegionName(object.get("regionName")!=null?object.get("regionName").toString():"");
			teacherInfoVo.setCreateDate(object.get("createDate")!=null?object.get("createDate").toString():"");
			teacherInfoVo.setUpdateDate(object.get("updateDate")!=null?object.get("updateDate").toString():"");
			teacherInfoVo.setEmail(object.get("email")!=null?object.get("email").toString():"");
			teacherInfoVo.setJobId(object.get("jobId")!=null?object.get("jobId").toString():"");
			teacherInfoVo.setJobName(object.get("jobName")!=null?object.get("jobName").toString():"");
			if(object.get("orgId")!=null){
				Organization org_branch = userService.getBelongBranchByOrgId(object.get("orgId").toString());
				Organization org_campus = userService.getBelongCampusByOrgId(object.get("orgId").toString());
				if(org_branch!=null){
					teacherInfoVo.setBranchId(org_branch.getId());
					teacherInfoVo.setBranchName(org_branch.getName());
				}
				if(org_campus!=null){
					teacherInfoVo.setCampusId(org_campus.getId());
					teacherInfoVo.setCampusName(org_campus.getName());
				}
			}
			params.put("tsId"+i, object.get("id"));
			query.append(" select d.ID as subjectId ,d.`NAME` as subjectName ,dd.ID as gradeId,dd.`NAME` as gradeName,ts.CREATE_TIME as createDate,ts.UPDATE_TIME as updateDate,ts.SUBJECT_STATUS as subject_Status from teacher_subject ts ");
			query.append(" left join data_dict d on ts.SUBJET = d.ID ");
			query.append(" left join data_dict dd on ts.GRADE = dd.ID ");
			query.append(" where ts.ID = :tsId"+i+" ");
			List<Map<Object, Object>> teacherSubjects = teacherSubjectDao.findMapBySql(query.toString(),params);
			query.delete(0, query.length());
			for(Map<Object, Object> ob:teacherSubjects){
				Map<String, String> obj =(Map)ob;
				TeacherInfoSubjectVo teacherInfoSubjectVo = new TeacherInfoSubjectVo();
				teacherInfoSubjectVo.setSubjectId(obj.get("subjectId"));
				teacherInfoSubjectVo.setSubjectName(obj.get("subjectName"));
				teacherInfoSubjectVo.setGradeId(obj.get("gradeId"));
				teacherInfoSubjectVo.setGradeName(obj.get("gradeName"));
				teacherInfoSubjectVo.setSubjectStatus(obj.get("subject_Status"));
				teacherInfoSubjectVo.setCreateDate(obj.get("createDate"));
				teacherInfoSubjectVo.setUpdateDate(obj.get("updateDate"));
				teacherSubjectVos.add(teacherInfoSubjectVo);
			}
			teacherInfoVo.setSubjectGrade(teacherSubjectVos);
			result.add(teacherInfoVo);
			
		}		
		Integer totalCount = userDao.findCountSql(count,params);
		Integer pageSize = dataPackage.getPageSize();
		Integer totalPage = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			totalPage++;
		}
		resultMap.put("totalPage", totalPage);
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", result);
		map.put("resultStatus", 200);
		map.put("resultMessage", "教师列表");
		map.put("result", resultMap);
		return map;
	}


	@Override
	public Map<String, Object> getTeacherInfoNew(DataPackage dataPackage, String teacherId, String phone,String name) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<TeacherInfoVo> result = new ArrayList<TeacherInfoVo>();
		StringBuilder query = new StringBuilder();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Integer start = (dataPackage.getPageNo() - 1) * dataPackage.getPageSize();

		query.append(" select u.USER_ID as id,u.`NAME` as `name`,u.organizationID as orgId,o.`name` as orgName,o.city_id as regionId,rr.`name` as regionName,u.CREATE_TIME as createDate,u.MODIFY_TIME as updateDate ");
		query.append(" ,u.MAIL_ADDR as email,uj.ID as jobId,uj.JOB_NAME as jobName ");
		query.append(" from `user` u,organization o ,user_organization_role ur,role r,region rr,user_dept_job udj,user_job uj ");
		query.append(" where  o.id = u.organizationID and udj.isMajorRole = 0 and u.ENABLE_FLAG = '0' and rr.id = o.city_id and u.USER_ID = udj.USER_ID and udj.JOB_ID = uj.ID ");
		query.append(" and r.roleCode='"+RoleCode.TEATCHER+"' and ur.user_ID = u.USER_ID and ur.role_ID = r.id ");
		Map<String, Object> params = Maps.newHashMap();

		if (StringUtils.isNotBlank(teacherId)) {
			query.append(" and u.USER_ID = :teacherId ");
			params.put("teacherId", teacherId);
		}
		if (StringUtils.isNotBlank(phone)) {
			query.append(" and u.CONTACT = :phone ");
			params.put("phone", phone);
		}
		if(StringUtils.isNotBlank(name)){
			query.append(" and u.`NAME` like :name ");
			params.put("name", "%"+name+"%");
		}
		String count = "select count(*) from ( " + query.toString() + " ) countall ";
		query.append(" limit " + start + "," + dataPackage.getPageSize());
		List<Map<Object, Object>> list = userDao.findMapBySql(query.toString(),params);
		query.delete(0, query.length());
		TeacherInfoVo teacherInfoVo = null;
		int i = 0;
		for(Map<Object, Object> oMap:list){
			i = i+1;
			Map<String, Object> object = (Map)oMap;
			List<TeacherInfoSubjectVo> teacherSubjectVos = new ArrayList<TeacherInfoSubjectVo>();
			teacherInfoVo = new TeacherInfoVo();
			teacherInfoVo.setId(object.get("id")!=null?object.get("id").toString():"");
			teacherInfoVo.setName(object.get("name")!=null?object.get("name").toString():"");
			teacherInfoVo.setLevel("");// 教师等级 暂时为空没这个字段
			teacherInfoVo.setOrgId(object.get("orgId")!=null?object.get("orgId").toString():"");
			teacherInfoVo.setOrgName(object.get("orgName")!=null?object.get("orgName").toString():"");
			teacherInfoVo.setRegionId(object.get("regionId")!=null?object.get("regionId").toString():"");
			teacherInfoVo.setRegionName(object.get("regionName")!=null?object.get("regionName").toString():"");
			teacherInfoVo.setCreateDate(object.get("createDate")!=null?object.get("createDate").toString():"");
			teacherInfoVo.setUpdateDate(object.get("updateDate")!=null?object.get("updateDate").toString():"");
			teacherInfoVo.setEmail(object.get("email")!=null?object.get("email").toString():"");
			teacherInfoVo.setJobId(object.get("jobId")!=null?object.get("jobId").toString():"");
			teacherInfoVo.setJobName(object.get("jobName")!=null?object.get("jobName").toString():"");
			if(object.get("orgId")!=null){
				Organization org_branch = userService.getBelongBranchByOrgId(object.get("orgId").toString());
				Organization org_campus = userService.getBelongCampusByOrgId(object.get("orgId").toString());
				if(org_branch!=null){
					teacherInfoVo.setBranchId(org_branch.getId());
					teacherInfoVo.setBranchName(org_branch.getName());
				}
				if(org_campus!=null){
					teacherInfoVo.setCampusId(org_campus.getId());
					teacherInfoVo.setCampusName(org_campus.getName());
				}
			}
			Map<String,Object> params2= new HashMap<>();
			params2.put("tsId"+i, object.get("id"));
			query.append(" select d.ID as subjectId ,d.`NAME` as subjectName ,dd.ID as gradeId,dd.`NAME` as gradeName,ts.CREATE_TIME as createDate,ts.UPDATE_TIME as updateDate,ts.SUBJECT_STATUS as subject_Status from teacher_subject ts ");
			query.append(" left join data_dict d on ts.SUBJET = d.ID ");
			query.append(" left join data_dict dd on ts.GRADE = dd.ID ");
			query.append(" where ts.ID = :tsId"+i+" ");
			List<Map<Object, Object>> teacherSubjects = teacherSubjectDao.findMapBySql(query.toString(),params2);
			query.delete(0, query.length());
			for(Map<Object, Object> ob:teacherSubjects){
				Map<String, String> obj =(Map)ob;
				TeacherInfoSubjectVo teacherInfoSubjectVo = new TeacherInfoSubjectVo();
				teacherInfoSubjectVo.setSubjectId(obj.get("subjectId"));
				teacherInfoSubjectVo.setSubjectName(obj.get("subjectName"));
				teacherInfoSubjectVo.setGradeId(obj.get("gradeId"));
				teacherInfoSubjectVo.setGradeName(obj.get("gradeName"));
				teacherInfoSubjectVo.setSubjectStatus(obj.get("subject_Status"));
				teacherInfoSubjectVo.setCreateDate(obj.get("createDate"));
				teacherInfoSubjectVo.setUpdateDate(obj.get("updateDate"));
				teacherSubjectVos.add(teacherInfoSubjectVo);
			}
			teacherInfoVo.setSubjectGrade(teacherSubjectVos);
			result.add(teacherInfoVo);

		}
		Integer totalCount = userDao.findCountSql(count,params);
		Integer pageSize = dataPackage.getPageSize();
		Integer totalPage = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			totalPage++;
		}
		resultMap.put("totalPage", totalPage);
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", result);
		map.put("resultStatus", 200);
		map.put("resultMessage", "教师列表");
		map.put("result", resultMap);
		return map;
	}


	/**
	 *
	 * @param studentId
	 * @param subject
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getTeacherByStudentIdAndSubject(String studentId, String subject,Map<String, Object> map) {
		map.put("resultStatus", 200);
		map.put("resultMessage", "学生查询自己的老师和学管师");
		Map<String, Object> result = new HashMap<>();
		result.put("teachers", new ArrayList<>());
		result.put("studyManager", new ArrayList<>());
		map.put("result", result);

		String subjectId = dataDictDao.getDataDictIdByName(subject, DataDictCategory.SUBJECT);

		StringBuffer courseTeacherSql = new StringBuffer();

		Map<String, Object> params = new HashMap<>();
		params.put("studentId", studentId);
		params.put("subjectId", subjectId);

		courseTeacherSql.append("  SELECT                                ");
		courseTeacherSql.append("  teacher.*                             ");
		courseTeacherSql.append("  		FROM                             ");
		courseTeacherSql.append("  course c,                             ");
	    courseTeacherSql.append("  `user` teacher                        ");
		courseTeacherSql.append("  		WHERE                            ");
		courseTeacherSql.append("  c.TEACHER_ID = teacher.USER_ID        ");
		courseTeacherSql.append("  AND c.`SUBJECT` = :subjectId       ");
		courseTeacherSql.append("  AND c.STUDENT_ID = :studentId      ");
		courseTeacherSql.append("  GROUP BY                              ");
		courseTeacherSql.append("  c.TEACHER_ID;                         ");

		List<User> courseTeacher = userDao.findBySql(courseTeacherSql.toString(), params);
		map = getTeacherFromUserList(courseTeacher, map);

		StringBuffer courseStudyManagerSql = new StringBuffer();
		courseStudyManagerSql.append("  SELECT                                ");
		courseStudyManagerSql.append("  teacher.*                             ");
		courseStudyManagerSql.append("  		FROM                             ");
		courseStudyManagerSql.append("  course c,                             ");
		courseStudyManagerSql.append("  `user` teacher                        ");
		courseStudyManagerSql.append("  		WHERE                            ");
		courseStudyManagerSql.append("  c.STUDY_MANAGER_ID = teacher.USER_ID        ");
		courseStudyManagerSql.append("  AND c.`SUBJECT` = :subjectId       ");
		courseStudyManagerSql.append("  AND c.STUDENT_ID = :studentId      ");
		courseStudyManagerSql.append("  GROUP BY                              ");
		courseStudyManagerSql.append("  c.STUDY_MANAGER_ID;                         ");
		List<User> courseStudyManager = userDao.findBySql(courseStudyManagerSql.toString(), params);
		map = getStudyManagerFromUserList(courseStudyManager, map);

		StringBuffer smallClassTeacherSql = new StringBuffer();

		smallClassTeacherSql.append("  SELECT                                      ");
		smallClassTeacherSql.append("  teacher.*                                   ");
		smallClassTeacherSql.append("  		FROM                                   ");
		smallClassTeacherSql.append("  mini_class mc,                              ");
		smallClassTeacherSql.append("  mini_class_student mcs,                     ");
	    smallClassTeacherSql.append("  `user` teacher,                             ");
		smallClassTeacherSql.append("  		contract_product cp                    ");
		smallClassTeacherSql.append("  		WHERE                                  ");
		smallClassTeacherSql.append("  mc.MINI_CLASS_ID = mcs.MINI_CLASS_ID        ");
		smallClassTeacherSql.append(" AND cp.TYPE = 'SMALL_CLASS'                  ");
		smallClassTeacherSql.append(" AND cp.ID = mcs.CONTRACT_PRODUCT_ID                  ");
		smallClassTeacherSql.append("  AND mc.TEACHER_ID = teacher.USER_ID         ");
		smallClassTeacherSql.append(" AND mc.`SUBJECT` = :subjectId           ");
		smallClassTeacherSql.append(" AND mcs.STUDENT_ID = :studentId         ");
		smallClassTeacherSql.append("  GROUP BY                                    ");
		smallClassTeacherSql.append("  mc.TEACHER_ID ;                              ");

		List<User> smallClassTeacher = userDao.findBySql(smallClassTeacherSql.toString(), params);
		map = getTeacherFromUserList(smallClassTeacher, map);

		StringBuffer smallClassStudyManagerSql = new StringBuffer();
		smallClassStudyManagerSql.append("  SELECT                                      ");
		smallClassStudyManagerSql.append("  teacher.*                                   ");
		smallClassStudyManagerSql.append("  		FROM                                   ");
		smallClassStudyManagerSql.append("  mini_class mc,                              ");
		smallClassStudyManagerSql.append("  mini_class_student mcs,                     ");
		smallClassStudyManagerSql.append("  `user` teacher,                             ");
		smallClassStudyManagerSql.append("  		contract_product cp                    ");
		smallClassStudyManagerSql.append("  		WHERE                                  ");
		smallClassStudyManagerSql.append("  mc.MINI_CLASS_ID = mcs.MINI_CLASS_ID        ");
		smallClassStudyManagerSql.append(" AND cp.TYPE = 'SMALL_CLASS'                  ");
		smallClassStudyManagerSql.append(" AND cp.ID = mcs.CONTRACT_PRODUCT_ID                  ");
		smallClassStudyManagerSql.append("  AND mc.STUDY_MANEGER_ID = teacher.USER_ID         ");
		smallClassStudyManagerSql.append(" AND mc.`SUBJECT` = :subjectId           ");
		smallClassStudyManagerSql.append(" AND mcs.STUDENT_ID = :studentId         ");
		smallClassStudyManagerSql.append("  GROUP BY                                    ");
		smallClassStudyManagerSql.append("  mc.STUDY_MANEGER_ID ;                              ");
		List<User> smallClassStudyManager = userDao.findBySql(smallClassStudyManagerSql.toString(), params);
		map = getStudyManagerFromUserList(smallClassStudyManager, map);


		StringBuffer ECSTeacherSql = new StringBuffer();

		ECSTeacherSql.append("   SELECT                                     ");
		ECSTeacherSql.append("   teacher.*                                  ");
		ECSTeacherSql.append("   		FROM                                ");
		ECSTeacherSql.append("   mini_class mc,                             ");
		ECSTeacherSql.append("   mini_class_student mcs,                    ");
	    ECSTeacherSql.append("   `user` teacher,                            ");
		ECSTeacherSql.append("   		contract_product cp                 ");
		ECSTeacherSql.append("   		WHERE                               ");
		ECSTeacherSql.append("   mc.MINI_CLASS_ID = mcs.MINI_CLASS_ID       ");
		ECSTeacherSql.append("  AND cp.TYPE = 'ECS_CLASS'                   ");
		ECSTeacherSql.append("  AND cp.ID = mcs.CONTRACT_PRODUCT_ID                   ");
		ECSTeacherSql.append("   AND mc.TEACHER_ID = teacher.USER_ID        ");
		ECSTeacherSql.append("  AND mc.`SUBJECT` = :subjectId               ");
		ECSTeacherSql.append("  AND mcs.STUDENT_ID = :studentId             ");
		ECSTeacherSql.append("   GROUP BY                                   ");
		ECSTeacherSql.append("   mc.TEACHER_ID;                             ");
		List<User> ecsTeacher = userDao.findBySql(ECSTeacherSql.toString(), params);
		map = getTeacherFromUserList(ecsTeacher, map);

		StringBuffer lectureSql = new StringBuffer();

		lectureSql.append("   SELECT                                ");
		lectureSql.append("   teacher.*                             ");
		lectureSql.append(" 		FROM                          ");
		lectureSql.append("   LECTURE_CLASS lc,                     ");
		lectureSql.append("   lecture_class_student lcs,            ");
	    lectureSql.append("   `user` teacher                        ");
		lectureSql.append(" 		 WHERE                         ");
		lectureSql.append("   lc.lecture_id = lcs.lecture_id        ");
		lectureSql.append("   AND lc. SUBJECT = :subjectId      ");
		lectureSql.append("   AND teacher.USER_ID = lcs.teacher_id  ");
		lectureSql.append("   AND lcs.student_id = :studentId   ");
		lectureSql.append("   GROUP BY                              ");
		lectureSql.append("   lcs.teacher_id;                       ");
		List<User> lectureTeacher = userDao.findBySql(lectureSql.toString(), params);
		map = getTeacherFromUserList(lectureTeacher, map);

		StringBuffer twoteacherTwoSql = new StringBuffer();

		twoteacherTwoSql.append("  SELECT                                      ");
		twoteacherTwoSql.append("  teacher.*                                   ");
		twoteacherTwoSql.append("  		FROM                                   ");
		twoteacherTwoSql.append("  two_teacher_class_student ttcs,             ");
		twoteacherTwoSql.append("  two_teacher_class_two ttct,                 ");
		twoteacherTwoSql.append("  two_teacher_class ttc,                      ");
	    twoteacherTwoSql.append("  `user` teacher                              ");
		twoteacherTwoSql.append("  		WHERE                                  ");
		twoteacherTwoSql.append(" ttcs.student_id = :studentId            ");
		twoteacherTwoSql.append("  AND ttcs.class_two_id = ttct.CLASS_TWO_ID   ");
		twoteacherTwoSql.append("  AND ttct.CLASS_ID = ttc.CLASS_ID            ");
		twoteacherTwoSql.append(" AND ttc.`SUBJECT` = :subjectId          ");
		twoteacherTwoSql.append("  AND teacher.USER_ID = ttct.TEACHER_ID       ");
		twoteacherTwoSql.append("  GROUP BY                                    ");
		twoteacherTwoSql.append("  ttct.TEACHER_ID;                            ");
		List<User> twoteacherTwoTeacher = userDao.findBySql(twoteacherTwoSql.toString(), params);
		map = getTeacherFromUserList(twoteacherTwoTeacher, map);

		StringBuffer twoteacherSql =  new StringBuffer();
		twoteacherSql.append("  SELECT                                       ");
		twoteacherSql.append("  teacher.*                                    ");
		twoteacherSql.append("  		FROM                                 ");
		twoteacherSql.append("  two_teacher_class_student ttcs,              ");
		twoteacherSql.append("  two_teacher_class_two ttct,                  ");
		twoteacherSql.append("  two_teacher_class ttc,                       ");
	    twoteacherSql.append("  `user` teacher                               ");
		twoteacherSql.append("  		WHERE                                ");
		twoteacherSql.append(" ttcs.student_id = :studentId             ");
		twoteacherSql.append("  AND ttcs.class_two_id = ttct.CLASS_TWO_ID    ");
		twoteacherSql.append("  AND ttct.CLASS_ID = ttc.CLASS_ID             ");
		twoteacherSql.append(" AND ttc.`SUBJECT` = :subjectId           ");
		twoteacherSql.append("  AND teacher.USER_ID = ttc.TEACHER_ID         ");
		twoteacherSql.append("  GROUP BY                                     ");
		twoteacherSql.append("  ttc.TEACHER_ID;                              ");
		List<User> twoteacherTeacher = userDao.findBySql(twoteacherSql.toString(), params);
		map = getTeacherFromUserList(twoteacherTeacher, map);


		StringBuffer liveSql = new StringBuffer();

		liveSql.append("  SELECT                                   ");
		liveSql.append("  teacher.*                                ");
		liveSql.append("  		FROM                                ");
		liveSql.append("  product p,                               ");
		liveSql.append("  contract_product cp,                     ");
		liveSql.append("  contract c,                              ");
	    liveSql.append("  `user` teacher                           ");
		liveSql.append("  		WHERE                               ");
		liveSql.append(" p.CATEGORY = 'LIVE'                       ");
		liveSql.append("  AND p.teacher_id = teacher.USER_ID       ");
		liveSql.append("  AND p.ID = cp.PRODUCT_ID                 ");
		liveSql.append("  AND cp.CONTRACT_ID = c.ID                ");
		liveSql.append(" AND c.STUDENT_ID = :studentId        ");
		liveSql.append(" AND p.SUBJECT_ID = :subjectId        ");
		liveSql.append("  GROUP BY                                 ");
		liveSql.append("  p.teacher_id;                            ");
		List<User> liveTeacher = userDao.findBySql(liveSql.toString(), params);
		map = getTeacherFromUserList(liveTeacher, map);



		return map;
	}

	private Map<String, Object> getStudyManagerFromUserList(List<User> studyManagerList, Map<String, Object> map) {
		Map<String, List> result = (Map<String, List>)map.get("result");
		List<Map<Object, Object>> studyManager = result.get("studyManager");
		for (User user : studyManagerList){
			Map<Object, Object> row = new HashMap<>();
			row.put("id", user.getUserId());
			row.put("name", user.getName());
			row.put("account", user.getAccount());
			row.put("employeeNo", user.getEmployeeNo());
			studyManager.add(row);
		}
		return map;
	}

	private Map<String, Object> getTeacherFromUserList(List<User> courseTeacher, Map<String, Object> map) {
		Map<String, List> result = (Map<String, List>)map.get("result");
		List<Map<Object, Object>> teachers = result.get("teachers");
		for (User user : courseTeacher){
			Map<Object, Object> row = new HashMap<>();
			row.put("id", user.getUserId());
			row.put("name", user.getName());
			row.put("account", user.getAccount());
			row.put("employeeNo", user.getEmployeeNo());
			teachers.add(row);
		}
		return map;
	}
}
