package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.ProductType;
import com.eduboss.dao.*;
import com.eduboss.domain.Course;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.EduPlatform.StudentClassInfoVo;
import com.eduboss.domainVo.EduPlatform.TextBookParamVo;
import com.eduboss.domainVo.EduPlatform.UnChargedClassInfoVo;


import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.CourseStatus;
import com.eduboss.common.MiniClassStatus;
import com.eduboss.domain.MiniClass;
import com.eduboss.domainVo.ClassStudentInfoVo;
import com.eduboss.domainVo.MiniClassInfoVo;
import com.eduboss.domainVo.TwoTeacherClassInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.ClassService;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

import tebie.applib.api.y;


@Service("classService")
public class ClassServiceImpl implements ClassService{

	@Autowired
	private MiniClassDao miniClassDao;
	
	@Autowired
	private TwoTeacherClassTwoDao twoTeacherClassTwoDao;
	
	@Autowired
	private TwoTeacherClassCourseDao twoTeacherClassCourseDao;
	
	@Autowired
	private TwoTeacherClassDao twoTeacherClassDao;
	
	@Autowired
	private MiniClassCourseDao miniClassCourseDao;
	
	@Autowired
	private CourseSummaryDao courseSummaryDao;
	
	@Autowired
	private CourseDao courseDao;

	@Autowired
	private OrganizationDao organizationDao;
	
	@Override
	public Map<String, Object> getMiniClassInfo(DataPackage dataPackage, String miniClassId) {
		Map<String, Object> map = new HashMap<String,Object>();
		Integer start = (dataPackage.getPageNo()-1)*dataPackage.getPageSize();	
		List<MiniClassInfoVo> miniClassInfoVos = new ArrayList<MiniClassInfoVo>();
		StringBuffer query = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		query.append(" select * from mini_class mc ");
		if(StringUtils.isNotBlank(miniClassId)){
			query.append(" where mc.MINI_CLASS_ID = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		String count ="select count(*) from ( " + query.toString() + " ) countall ";
		query.append(" limit :start , :pageSize");
		params.put("start", start);
		params.put("pageSize", dataPackage.getPageSize());
		List<MiniClass> miniClasses = miniClassDao.findBySql(query.toString(),params);
		for(MiniClass miniClass:miniClasses){
			MiniClassInfoVo miniClassInfoVo = new MiniClassInfoVo();
			miniClassInfoVo.setId(miniClass.getMiniClassId());
			miniClassInfoVo.setName(miniClass.getName());
			miniClassInfoVo.setGradeId(miniClass.getGrade().getValue());
			miniClassInfoVo.setGradeName(miniClass.getGrade().getName());
			miniClassInfoVo.setSubjectId(miniClass.getSubject().getValue());
			miniClassInfoVo.setSubjectName(miniClass.getSubject().getName());
			miniClassInfoVo.setCreateDate(miniClass.getCreateTime());
			miniClassInfoVo.setUpdateDate(miniClass.getModifyTime());
			miniClassInfoVos.add(miniClassInfoVo);
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Integer totalCount = miniClassDao.findCountSql(count,params);
		Integer pageSize = dataPackage.getPageSize();
	    Integer totalPage = totalCount / pageSize;  
        if (totalCount % pageSize > 0) {  
        	totalPage++;  
        } 
		resultMap.put("totalPage",totalPage );
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", miniClassInfoVos);
		map.put("resultStatus", 200);
		map.put("resultMessage", " 班级列表（小班）");
		map.put("result", resultMap);				
		
		return map;
	}

	@Override
	public Map<String, Object> getTwoTeacherClassInfo(String classId,String classIdTwo) {
		//双师传主班或者副班的id
		Map<String, Object> map = new HashMap<String,Object>();
		
		TwoTeacherClassInfoVo teacherClassInfoVo = new TwoTeacherClassInfoVo();
		StringBuffer query = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		query.append(" select ttct.CLASS_TWO_ID as classIdTwo,ttct.`NAME` as classNameTwo,ttc.TEACHER_ID as teacherId, ");
		query.append(" ttc.CLASS_ID as classId,ttc.NAME as className,");
		query.append(" u.`NAME` as teacherName,ttct.TEACHER_ID as teacherIdTwo,uu.`NAME` as teacherNameTwo, ");
		query.append(" ttc.`SUBJECT` as subjectId,d.`NAME` as subjectName,p.GRADE_ID as gradeId,dd.`NAME` as gradeName, ");
		query.append(" o.id as organizationId,o.`name` as organizationName,r.id as regionId,r.`name` as regionName, ");
		query.append(" o.ADDRESS as address,o.CONTACT as phone,ttct.REMARK as remark ");
		query.append(" from two_teacher_class_two ttct ");
		query.append(" left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID ");
		query.append(" left join `user` u on ttc.TEACHER_ID = u.USER_ID ");
		query.append(" left join `user` uu on ttct.TEACHER_ID = uu.USER_ID ");
		query.append(" left join data_dict d on d.ID =  ttc.`SUBJECT` ");
		query.append(" left join product p on p.ID = ttc.PRODUCE_ID ");
		query.append(" left join data_dict dd on dd.ID = p.GRADE_ID ");
		query.append(" left join organization o on o.id = ttct.BL_CAMPUS_ID ");
		query.append(" left join region r on r.id = o.city_id ");
		query.append(" where 1=1 ");
		if(StringUtil.isNotBlank(classIdTwo)){
			query.append(" and ttct.CLASS_TWO_ID = :classIdTwo ");
			params.put("classIdTwo", classIdTwo);
		}
		boolean flag = false;
		if(StringUtil.isNotBlank(classId)){
			query.append(" and ttc.CLASS_ID = :classId ");
			params.put("classId", classId);
			flag = true;
		}
		
		
		List<Map<Object, Object>> list = twoTeacherClassTwoDao.findMapBySql(query.toString(), params);
		if(list!=null && list.size()>0){
			Map<String, String>  result  = (Map)list.get(0);
			Object cclassId = result.get("classId");
			teacherClassInfoVo.setClassId(cclassId.toString());
			teacherClassInfoVo.setClassName(result.get("className"));
			Object cclassIdTwo = result.get("classIdTwo");
			teacherClassInfoVo.setClassIdTwo(!flag?cclassIdTwo.toString():"");
			teacherClassInfoVo.setClassNameTwo(!flag?result.get("classNameTwo"):"");
			teacherClassInfoVo.setTeacherId(result.get("teacherId"));
			teacherClassInfoVo.setTeacherName(result.get("teacherName"));
			teacherClassInfoVo.setTeacherIdTwo(!flag?result.get("teacherIdTwo"):"");
			teacherClassInfoVo.setTeacherNameTwo(!flag?result.get("teacherNameTwo"):"");
			teacherClassInfoVo.setSubjectId(result.get("subjectId"));
			teacherClassInfoVo.setSubjectName(result.get("subjectName"));
			teacherClassInfoVo.setGradeId(result.get("gradeId"));
			teacherClassInfoVo.setGradeName(result.get("gradeName"));
			teacherClassInfoVo.setOrganizationId(!flag?result.get("organizationId"):"");
			teacherClassInfoVo.setOrganizationName(!flag?result.get("organizationName"):"");
			Object regionId = result.get("regionId");
			if(regionId!=null){
				teacherClassInfoVo.setRegionId(!flag?regionId.toString():"");
			}else{
				teacherClassInfoVo.setRegionId("");
			}
			
			teacherClassInfoVo.setRegionName(!flag?result.get("regionName"):"");
			teacherClassInfoVo.setAddress(!flag?result.get("address"):"");
			teacherClassInfoVo.setPhone(!flag?result.get("phone"):"");
			teacherClassInfoVo.setRemark(!flag?result.get("remark"):"");
			
			//剩余课时 和已上课时
			//主班的id
			Object mClassId = result.get("classId");
			Map<String, Object> param = Maps.newHashMap();
			param.put("mClassId", mClassId);
			query.delete(0, query.length());
			query.append("select sum(case when COURSE_STATUS ='"+CourseStatus.NEW+"' then COURSE_HOURS else 0 end ) as remain, ");
            query.append("sum(case when COURSE_STATUS ='"+CourseStatus.CHARGED+"' then COURSE_HOURS else 0 end ) as consume ");
            query.append("from two_teacher_class_course where CLASS_ID = :mClassId");
            List<Map<Object, Object>> time = twoTeacherClassCourseDao.findMapBySql(query.toString(), param);
            if(time!=null && time.size()>0){
            	Map<String, String>  mapp  = (Map)time.get(0);
            	Object remain = mapp.get("remain");
            	Object consume = mapp.get("consume");
            	teacherClassInfoVo.setRemainingTime(remain!=null?remain.toString():"0");
            	teacherClassInfoVo.setDoneTime(consume!=null?consume.toString():"0");
            }else{
            	teacherClassInfoVo.setRemainingTime("0");
            	teacherClassInfoVo.setDoneTime("0");
            }
			
		}
		
		
		map.put("resultStatus", 200);
		map.put("resultMessage", "（双师）班级详情");
		map.put("result", teacherClassInfoVo);	
		return map;
	}

	/**
	 * 学生查询自己的班级列表(按照结课时间排序，最晚结课的在最上)
	 *
	 * @param studentId
	 * @param type
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> queryStudentClassInfo(String studentId, String type, Map<String, Object> map) {

		ProductType productType = ProductType.valueOf(type);
		Boolean flag = false;
		if(productType == ProductType.ONE_ON_ONE_COURSE){
			//一对一
			List<StudentClassInfoVo> result = getOneOnOneByStudentId(studentId);
			result = getOneOnOneInfoForStudentClassInfoVo(result);
			map.put("resultStatus", 200);
			map.put("resultMessage", "学生查询自己的班级列表");
			map.put("result", result);
		}else if(productType == ProductType.ONE_ON_MANY){
			//一对多
		}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
			//小班、目标班
			List<StudentClassInfoVo> quitResult = getMiniClassQuitByStudentId(studentId,type);
			List<StudentClassInfoVo> result = getMiniClassInfoByStudentId(studentId,type);
			result.addAll(quitResult);
			result = getMiniClassInfoForStudentClassInfoVo(result);
			map.put("resultStatus", 200);
			map.put("resultMessage", "学生查询自己的班级列表");
			map.put("result", result);
		}else if(productType == ProductType.TWO_TEACHER){
			//双师
			flag = true;
			//List<StudentClassInfoVo> quitResult = getTwoTeacherQuitByStudentId(studentId);
			List<StudentClassInfoVo> result = getTwoTeacherClassInfoByStudentId(studentId);
			//result.addAll(quitResult);
			result = getInfoForStudentClassInfoVo(result);
			map.put("resultStatus", 200);
			map.put("resultMessage", "学生查询自己的班级列表");
			map.put("result", result);
		}
		if(!flag){
			map.put("resultStatus", 200);
			map.put("resultMessage", "目前暂时只能查询双师的情况");
			map.put("result", null);
		}
		return map;
	}
	private List<StudentClassInfoVo> getOneOnOneInfoForStudentClassInfoVo(List<StudentClassInfoVo> result) {
		// TODO Auto-generated method stub
		Map<String, StudentClassInfoVo> map = new HashMap<>();
		for (StudentClassInfoVo vo : result){
			if (map.get(vo.getCourseSummaryId())==null){
				StudentClassInfoVo courseInfo = new StudentClassInfoVo();
				courseInfo = findOneOnOneCourseDetail(vo.getCourseSummaryId(), courseInfo);
				map.put(vo.getCourseSummaryId(), courseInfo);
			}
		}

		for (StudentClassInfoVo vo : result){
			StudentClassInfoVo studentClassInfoVo = map.get(vo.getCourseSummaryId());
			vo.setCourseDate(studentClassInfoVo.getCourseDate());
			vo.setCourseTime(studentClassInfoVo.getCourseTime());
			vo.setCourseEndTime(studentClassInfoVo.getCourseEndTime());
			vo.setDayOfWeek(studentClassInfoVo.getDayOfWeek());
			vo.setAllCourseCount(studentClassInfoVo.getAllCourseCount());
			vo.setFinishCourseCount(studentClassInfoVo.getFinishCourseCount());
			vo.setClassEndTime(studentClassInfoVo.getClassEndTime());
			vo.setFinishFlag(vo.isFinishFlag()?vo.isFinishFlag():studentClassInfoVo.isFinishFlag());
			vo.setCourseTeacherId(studentClassInfoVo.getCourseTeacherId());
			vo.setCourseTeacherName(studentClassInfoVo.getCourseTeacherName());
			vo.setCourseId(studentClassInfoVo.getCourseId());
			vo.setRemainCourseHoursCount(studentClassInfoVo.getRemainCourseHoursCount());
			vo.setFinishCourseHoursCount(studentClassInfoVo.getFinishCourseHoursCount());
		}
		return result;
	}

	private StudentClassInfoVo findOneOnOneCourseDetail(String courseSummaryId, StudentClassInfoVo courseInfo) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT CONVERT(c.COURSE_DATE ,CHAR) courseDate,c.COURSE_TIME courseTime, c.COURSE_TIME courseEndTime ");
		sql.append(" ,u.Name courseTeacherName,u.USER_ID courseTeacherId,c.COURSE_ID courseId");
		sql.append(" FROM course c LEFT JOIN user u on c.TEACHER_ID = u.USER_ID ");
		sql.append(" WHERE c.COURSE_STATUS='NEW' AND CONCAT(c.COURSE_DATE,' ',SUBSTRING(c.COURSE_TIME,9),':00') > now()  ");
		sql.append(" AND c.COURSE_SUMMARY_ID = :courseSummaryId ORDER BY c.course_date ASC LIMIT 1 ");
		params.put("courseSummaryId", courseSummaryId);
		List<Map<Object, Object>> list = courseDao.findMapBySql(sql.toString(), params);
		if (list.size()>0){
			courseInfo = HibernateUtils.voObjectMapping(list.get(0), StudentClassInfoVo.class);
			courseInfo.setDayOfWeek(DateTools.getWeekOfDate(DateTools.getDate(courseInfo.getCourseDate())));
			courseInfo.setCourseEndTime(courseInfo.getCourseTime().substring(8, 13));
			courseInfo.setCourseTime(courseInfo.getCourseTime().substring(0, 5));
		}
		String allCourseCountSql = " SELECT count(*) FROM course WHERE COURSE_SUMMARY_ID = :courseSummaryId ";
		int allCourseCount = courseDao.findCountSql(allCourseCountSql, params);
		courseInfo.setAllCourseCount(allCourseCount);
		String finishCourseCountSql =" SELECT count(*) FROM course WHERE COURSE_SUMMARY_ID = :courseSummaryId AND COURSE_STATUS = 'CHARGED'";
		int finishCourseCount = courseDao.findCountSql(finishCourseCountSql, params);
		courseInfo.setFinishCourseCount(finishCourseCount);
		courseInfo.setFinishFlag(allCourseCount==finishCourseCount);
		
		sql.delete(0, sql.length());
		sql.append("SELECT IFNULL(SUM(CASE WHEN COURSE_STATUS='CHARGED' THEN PLAN_HOURS ELSE 0 END),0) as finishCourseHoursCount,");
		sql.append(" IFNULL(SUM(CASE WHEN COURSE_STATUS='CHARGED' THEN 0 ELSE PLAN_HOURS END),0) as remainCourseHoursCount ");
		sql.append(" from course WHERE COURSE_SUMMARY_ID = :courseSummaryId ");
		List<Map<Object, Object>> result = courseDao.findMapBySql(sql.toString(), params);
		if(result!=null && result.size()>0){
			courseInfo.setRemainCourseHoursCount(result.get(0).get("remainCourseHoursCount").toString());
			courseInfo.setFinishCourseHoursCount(result.get(0).get("finishCourseHoursCount").toString());
		}	
		
		sql.delete(0, sql.length());
		sql.append(" SELECT CONCAT(COURSE_DATE,' ',SUBSTRING(COURSE_TIME,9),':00') as classEndTime ");
		sql.append(" from course WHERE COURSE_SUMMARY_ID = :courseSummaryId ORDER BY COURSE_DATE desc LIMIT 1 ");
		List<Map<Object, Object>> classEndTime = courseDao.findMapBySql(sql.toString(), params);
		if(classEndTime.size()>0){
			courseInfo.setClassEndTime(classEndTime.get(0).get("classEndTime")!=null?classEndTime.get(0).get("classEndTime").toString():"");
		}
		return courseInfo;
	}

	private List<StudentClassInfoVo> getOneOnOneByStudentId(String studentId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT cs.COURSE_SUMMARY_ID courseSummaryId,u.Name teacherName,cs.TEACHER_ID teacherId,p.NAME productName,dd.ID gradeId ,dd.NAME gradeName , d.ID subjectId ,d.NAME subjectName,'ONE_ON_ONE_COURSE' type ");
		sql.append(" ,(CASE WHEN cs.DEL_FLAG=1 THEN TRUE ELSE FALSE END ) AS finishFlag,(CASE WHEN cs.DEL_FLAG=1 THEN TRUE ELSE FALSE END ) quitFlag ");
		sql.append(" FROM course_summary cs ");
		sql.append(" LEFT JOIN data_dict d ON  cs.SUBJECT = d.ID ");
		sql.append(" LEFT JOIN product p ON cs.PRODUCT_ID = p.ID");
		sql.append(" LEFT JOIN data_dict dd ON cs.GRADE = dd.ID ");
		sql.append(" LEFT JOIN USER u ON cs.TEACHER_ID = u.USER_ID ");
		sql.append(" where cs.STUDENT_ID = :studentId ");
		sql.append(" AND EXISTS (SELECT 1 FROM course WHERE COURSE_SUMMARY_ID = cs.COURSE_SUMMARY_ID)  ");
		params.put("studentId", studentId);
		List<Map<Object, Object>> list = courseSummaryDao.findMapBySql(sql.toString(), params);
		List<StudentClassInfoVo> studentClassInfoVos = HibernateUtils.voListMapping(list, StudentClassInfoVo.class);
		return studentClassInfoVos;
	}

	/**
	 * 查找小班课程详情
	 * @author: duanmenrun
	 * @Title: getMiniClassInfoForStudentClassInfoVo 
	 * @Description: TODO 
	 * @param result
	 * @return
	 */
	private List<StudentClassInfoVo> getMiniClassInfoForStudentClassInfoVo(List<StudentClassInfoVo> result) {
		// TODO Auto-generated method stub
		Map<String, StudentClassInfoVo> map = new HashMap<>();
		for (StudentClassInfoVo vo : result){
			if (map.get(vo.getClassId())==null){
				StudentClassInfoVo courseInfo = new StudentClassInfoVo();
				courseInfo = findMiniCourseDetail(vo.getClassId(), courseInfo);
				map.put(vo.getClassId(), courseInfo);
			}
		}

		for (StudentClassInfoVo vo : result){
			StudentClassInfoVo studentClassInfoVo = map.get(vo.getClassId());
			vo.setCourseDate(studentClassInfoVo.getCourseDate());
			vo.setCourseTime(studentClassInfoVo.getCourseTime());
			vo.setCourseEndTime(studentClassInfoVo.getCourseEndTime());
			vo.setDayOfWeek(studentClassInfoVo.getDayOfWeek());
			vo.setAllCourseCount(studentClassInfoVo.getAllCourseCount());
			vo.setFinishCourseCount(studentClassInfoVo.getFinishCourseCount());
			vo.setClassEndTime(studentClassInfoVo.getClassEndTime());
		}
		return result;
	}

	private StudentClassInfoVo findMiniCourseDetail(String classId, StudentClassInfoVo courseInfo) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT course_date courseDate,COURSE_TIME courseTime, COURSE_END_TIME courseEndTime ");
		sql.append(" FROM mini_class_course WHERE COURSE_STATUS='NEW' AND CONCAT(COURSE_DATE,' ',COURSE_END_TIME,':00') > now() ");
		sql.append(" AND MINI_CLASS_ID = :classId ORDER BY course_date ASC LIMIT 1 ");
		params.put("classId", classId);
		List<Map<Object, Object>> list = miniClassCourseDao.findMapBySql(sql.toString(), params);
		if (list.size()>0){
			courseInfo = HibernateUtils.voObjectMapping(list.get(0), StudentClassInfoVo.class);
			courseInfo.setDayOfWeek(DateTools.getWeekOfDate(DateTools.getDate(courseInfo.getCourseDate())));
		}
		String allCourseCountSql = " SELECT count(*) FROM mini_class_course WHERE MINI_CLASS_ID =:classId ";
		int allCourseCount = miniClassCourseDao.findCountSql(allCourseCountSql, params);
		courseInfo.setAllCourseCount(allCourseCount);
		String finishCourseCountSql =" SELECT count(*) FROM mini_class_course WHERE MINI_CLASS_ID =:classId AND COURSE_STATUS = 'CHARGED'";
		int finishCourseCount = miniClassCourseDao.findCountSql(finishCourseCountSql, params);
		courseInfo.setFinishCourseCount(finishCourseCount);
		
		sql.delete(0, sql.length());
		sql.append(" SELECT CONCAT(COURSE_DATE,' ',COURSE_END_TIME,':00') as classEndTime ");
		sql.append(" from mini_class_course WHERE MINI_CLASS_ID = :classId ORDER BY COURSE_DATE desc LIMIT 1 ");
		List<Map<Object, Object>> classEndTime = miniClassCourseDao.findMapBySql(sql.toString(), params);
		if(classEndTime.size()>0){
			courseInfo.setClassEndTime(classEndTime.get(0).get("classEndTime")!=null?classEndTime.get(0).get("classEndTime").toString():"");
		}
		return courseInfo;
	}

	/**
	 * 根据学生id和类型查询小班
	 * @author: duanmenrun
	 * @Title: getMiniClassInfoByStudentId 
	 * @Description: TODO 
	 * @param studentId
	 * @param type
	 * @return
	 */
	private List<StudentClassInfoVo> getMiniClassInfoByStudentId(String studentId, String type) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT mc.MINI_CLASS_ID classId,u.Name teacherName,mc.TEACHER_ID teacherId,mc.NAME className,dd.ID gradeId ,dd.NAME gradeName , d.ID subjectId ,d.NAME subjectName,cp.TYPE type ");
		sql.append(" ,(CASE WHEN mc.status='CONPELETE' THEN TRUE ELSE FALSE END ) AS finishFlag,mc.CONSUME AS finishCourseHoursCount,(mc.TOTAL_CLASS_HOURS-mc.CONSUME) AS remainCourseHoursCount,FALSE quitFlag ");
		sql.append(" FROM mini_class_student mcs  ");
		sql.append(" JOIN mini_class mc ON mcs.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" LEFT JOIN data_dict d ON  mc.SUBJECT = d.ID ");
		sql.append(" LEFT JOIN product p ON mc.PRODUCE_ID = p.ID");
		sql.append(" LEFT JOIN data_dict dd ON p.GRADE_ID = dd.ID ");
		sql.append(" LEFT JOIN USER u ON mc.TEACHER_ID = u.USER_ID ");
		sql.append(" LEFT JOIN contract_product cp ON mcs.CONTRACT_PRODUCT_ID = cp.ID ");
		sql.append(" where mcs.STUDENT_ID = :studentId ");
		sql.append(" AND cp.TYPE =:type ");
		params.put("studentId", studentId);
		params.put("type", type);
		List<Map<Object, Object>> list = miniClassDao.findMapBySql(sql.toString(), params);
		List<StudentClassInfoVo> studentClassInfoVos = HibernateUtils.voListMapping(list, StudentClassInfoVo.class);
		return studentClassInfoVos;
	}
	/**
	 * 根据学生id和类型查询已退小班
	 * @author: duanmenrun
	 * @Title: getMiniClassQuitByStudentId 
	 * @Description: TODO 
	 * @param studentId
	 * @param type
	 * @return
	 */
	private List<StudentClassInfoVo> getMiniClassQuitByStudentId(String studentId, String type) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT mc.MINI_CLASS_ID classId,u.Name teacherName,mc.TEACHER_ID teacherId,mc.NAME className,dd.ID gradeId ,dd.NAME gradeName , d.ID subjectId ,d.NAME subjectName,'SMALL_CLASS' type ");
		sql.append(" ,(CASE WHEN mc.status='CONPELETE' THEN TRUE ELSE FALSE END ) AS finishFlag,mc.CONSUME AS finishCourseHoursCount,(mc.TOTAL_CLASS_HOURS-mc.CONSUME) AS remainCourseHoursCount,TRUE quitFlag ");
		sql.append(" FROM mini_class_student_attendent mcsa  ");
		sql.append(" JOIN mini_class_course mcc  ON mcsa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID ");
		sql.append(" JOIN  mini_class mc ON mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID   ");
		sql.append(" LEFT JOIN data_dict d ON  mc.SUBJECT = d.ID ");
		sql.append(" LEFT JOIN product p ON mc.PRODUCE_ID = p.ID");
		sql.append(" LEFT JOIN data_dict dd ON p.GRADE_ID = dd.ID ");
		sql.append(" LEFT JOIN USER u ON mc.TEACHER_ID = u.USER_ID ");
		sql.append(" where mcsa.STUDENT_ID = :studentId AND mcsa.CHARGE_STATUS = 'CHARGED' ");
		sql.append(" AND NOT EXISTS ( SELECT 1 FROM mini_class_student WHERE STUDENT_ID = mcsa.STUDENT_ID AND MINI_CLASS_ID = mc.MINI_CLASS_ID) ");
		sql.append(" GROUP BY mc.MINI_CLASS_ID ");
		params.put("studentId", studentId);
		List<Map<Object, Object>> list = miniClassDao.findMapBySql(sql.toString(), params);
		List<StudentClassInfoVo> studentClassInfoVos = HibernateUtils.voListMapping(list, StudentClassInfoVo.class);
		return studentClassInfoVos;
	}

	/**
	 * 根据学生查找已退的双师
	 * @author: duanmenrun
	 * @Title: getTwoTeacherQuitByStudentId 
	 * @Description: TODO 
	 * @param studentId
	 * @return
	 */
	private List<StudentClassInfoVo> getTwoTeacherQuitByStudentId(String studentId) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT ttc.CLASS_ID classId,u.Name teacherName,ttc.TEACHER_ID teacherId,ttc.NAME className,dd.ID gradeId ,dd.NAME gradeName , d.ID subjectId ,d.NAME subjectName,'TWO_TEACHER' type ");
		sql.append(" ,(CASE WHEN ttc.status='CONPELETE' THEN TRUE ELSE FALSE END ) AS finishFlag,TRUE as quitFlag ");
		sql.append(" FROM two_teacher_class_student_attendent ttcsa   ");
		sql.append(" JOIN two_teacher_class_two  ttct ON ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID ");
		sql.append(" JOIN  two_teacher_class ttc ON ttct.CLASS_ID = ttc.CLASS_ID  ");
		sql.append(" LEFT JOIN data_dict d ON  ttc.SUBJECT = d.ID ");
		sql.append(" LEFT JOIN product p ON ttc.PRODUCE_ID = p.ID ");
		sql.append(" LEFT JOIN data_dict dd ON p.GRADE_ID = dd.ID ");
		sql.append(" LEFT JOIN USER u ON ttc.TEACHER_ID = u.USER_ID ");
		sql.append(" where ttcsa.STUDENT_ID = :studentId AND ttcsa.CHARGE_STATUS = 'CHARGED' ");
		sql.append(" AND NOT EXISTS ( SELECT 1 FROM two_teacher_class_student WHERE STUDENT_ID = ttcsa.STUDENT_ID AND CLASS_TWO_ID = ttct.CLASS_TWO_ID) ");
		sql.append(" GROUP BY ttct.CLASS_ID ");
		params.put("studentId", studentId);
		List<Map<Object, Object>> list = twoTeacherClassTwoDao.findMapBySql(sql.toString(), params);
		List<StudentClassInfoVo> studentClassInfoVos = HibernateUtils.voListMapping(list, StudentClassInfoVo.class);
		return studentClassInfoVos;
	}

	/**
	 *
	 * @param result
	 * @return
	 */
	private List<StudentClassInfoVo> getInfoForStudentClassInfoVo(List<StudentClassInfoVo> result) {
		Map<String, StudentClassInfoVo> map = new HashMap<>();
		for (StudentClassInfoVo vo : result){
			if (map.get(vo.getClassId())==null){
				StudentClassInfoVo courseInfo = new StudentClassInfoVo();
				courseInfo = findInfoCourseDateByClassId(vo.getClassId(), courseInfo);
				map.put(vo.getClassId(), courseInfo);
			}
		}

		for (StudentClassInfoVo vo : result){
			StudentClassInfoVo studentClassInfoVo = map.get(vo.getClassId());
			vo.setCourseDate(studentClassInfoVo.getCourseDate());
			vo.setCourseTime(studentClassInfoVo.getCourseTime());
			vo.setCourseEndTime(studentClassInfoVo.getCourseEndTime());
			vo.setDayOfWeek(studentClassInfoVo.getDayOfWeek());
			vo.setAllCourseCount(studentClassInfoVo.getAllCourseCount());
			vo.setFinishCourseCount(studentClassInfoVo.getFinishCourseCount());
			vo.setClassEndTime(studentClassInfoVo.getClassEndTime());
			vo.setRemainCourseHoursCount(studentClassInfoVo.getRemainCourseHoursCount());
			vo.setFinishCourseHoursCount(studentClassInfoVo.getFinishCourseHoursCount());
		}
		return result;
	}

	/**
	 *
	 * @param classId
	 * @return
	 */
	private StudentClassInfoVo findInfoCourseDateByClassId(String classId, StudentClassInfoVo courseInfo) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT course_date courseDate,COURSE_TIME courseTime, COURSE_END_TIME courseEndTime ");
		sql.append(" FROM two_teacher_class_course WHERE COURSE_STATUS='NEW' AND CONCAT(COURSE_DATE,' ',COURSE_END_TIME,':00') > NOW()  ");
		sql.append(" AND CLASS_ID = :classId ORDER BY course_date ASC LIMIT 1 ");
		params.put("classId", classId);
		List<Map<Object, Object>> list = twoTeacherClassCourseDao.findMapBySql(sql.toString(), params);
		if (list.size()>0){
			courseInfo = HibernateUtils.voObjectMapping(list.get(0), StudentClassInfoVo.class);
			courseInfo.setDayOfWeek(DateTools.getWeekOfDate(DateTools.getDate(courseInfo.getCourseDate())));
		}
		String allCourseCountSql = " SELECT count(*) FROM two_teacher_class_course WHERE CLASS_ID =:classId ";
		int allCourseCount = twoTeacherClassCourseDao.findCountSql(allCourseCountSql, params);
		courseInfo.setAllCourseCount(allCourseCount);
		String finishCourseCountSql =" SELECT count(*) FROM two_teacher_class_course WHERE CLASS_ID =:classId AND COURSE_STATUS = 'CHARGED'";
		int finishCourseCount = twoTeacherClassCourseDao.findCountSql(finishCourseCountSql, params);
		courseInfo.setFinishCourseCount(finishCourseCount);
		
		sql.delete(0, sql.length());
		sql.append("SELECT IFNULL(SUM(CASE WHEN COURSE_STATUS='CHARGED' THEN COURSE_HOURS ELSE 0 END),0) as finishCourseHoursCount,");
		sql.append(" IFNULL(SUM(CASE WHEN COURSE_STATUS='CHARGED' THEN 0 ELSE COURSE_HOURS END),0) as remainCourseHoursCount ");
		sql.append(" from two_teacher_class_course WHERE CLASS_ID =:classId ");
		List<Map<Object, Object>> result = twoTeacherClassCourseDao.findMapBySql(sql.toString(), params);
		if(result!=null && result.size()>0){
			courseInfo.setRemainCourseHoursCount(result.get(0).get("remainCourseHoursCount").toString());
			courseInfo.setFinishCourseHoursCount(result.get(0).get("finishCourseHoursCount").toString());
		}	
		
		sql.delete(0, sql.length());
		sql.append(" SELECT CONCAT(COURSE_DATE,' ',COURSE_END_TIME,':00') as classEndTime ");
		sql.append(" from two_teacher_class_course WHERE CLASS_ID = :classId ORDER BY COURSE_DATE desc LIMIT 1 ");
		List<Map<Object, Object>> classEndTime = twoTeacherClassCourseDao.findMapBySql(sql.toString(), params);
		if(classEndTime.size()>0){
			courseInfo.setClassEndTime(classEndTime.get(0).get("classEndTime")!=null?classEndTime.get(0).get("classEndTime").toString():"");
		}
		return courseInfo;
	}

	/**
	 *
	 * @param studentId
	 * @return
	 */
	private List<StudentClassInfoVo> getTwoTeacherClassInfoByStudentId(String studentId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" SELECT u.`NAME` as teacherName,uu.`NAME` as teacherTwoName,u.ACCOUNT as teacherAccount,uu.ACCOUNT as teacherTwoAccount,ttc.CLASS_ID classId, ttct.CLASS_TWO_ID classIdTwo, ttc.TEACHER_ID teacherId, ttct.TEACHER_ID teacherIdTwo, ttc.`NAME` className, ");
		sql.append(" p.GRADE_ID gradeId , gradeName.`NAME` gradeName , ttc.`SUBJECT` subjectId ,subjectName.`NAME` subjectName ,(CASE WHEN ttc.status='CONPELETE' THEN TRUE ELSE FALSE END ) AS finishFlag , ");
		sql.append(" 'TWO_TEACHER' type, ");
		sql.append(" ttcs.CREATE_TIME as enrollInClassTime,FALSE as quitFlag ");
		sql.append(" FROM two_teacher_class_student ttcs,two_teacher_class_two ttct ,two_teacher_class ttc LEFT JOIN data_dict subjectName ON ttc.`SUBJECT` = subjectName.ID ,product p LEFT JOIN data_dict gradeName on p.GRADE_ID = gradeName.ID ");
		sql.append(" ,`user` u,`user` uu ");
		sql.append(" where ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID  ");
		sql.append(" AND ttct.CLASS_ID = ttc.CLASS_ID ");
		sql.append(" AND ttc.PRODUCE_ID=p.ID ");
		sql.append(" AND ttc.TEACHER_ID = u.USER_ID ");
		sql.append(" AND ttct.TEACHER_ID =uu.USER_ID ");
		sql.append(" AND ttcs.STUDENT_ID = :studentId ");
		params.put("studentId", studentId);
		List<Map<Object, Object>> list = twoTeacherClassTwoDao.findMapBySql(sql.toString(), params);
		List<StudentClassInfoVo> studentClassInfoVos = HibernateUtils.voListMapping(list, StudentClassInfoVo.class);
		return studentClassInfoVos;
	}

	@Override
	public Map<String, Object> getUnChargedClassInfo(String teacherId, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			ProductType productType = ProductType.valueOf(type);			
			Boolean flag = false;
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//一对一
			}else if(productType == ProductType.ONE_ON_MANY){
				//一对多
			}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
				//小班
			}else if(productType == ProductType.TWO_TEACHER){
				//双师
				flag = true;
				//双师的实现是teacherId作为主班老师查一遍主班（不返回classIdTwo），再作为辅导老师查一遍副班及对应的主班
				StringBuffer sql = new StringBuffer(128);
				Map<String, Object> params = Maps.newHashMap();
				params.put("teacherId", teacherId);
				List<UnChargedClassInfoVo> result = new ArrayList<UnChargedClassInfoVo>();
				
				sql.append(" select ttc.CLASS_ID as classId,ttc.`NAME` as className ,p.GRADE_ID as gradeId,d.`NAME` as gradeName,ttc.`SUBJECT` as subjectId,dd.`NAME` as subjectName ");
				sql.append(" from two_teacher_class ttc,product p,data_dict d,data_dict dd ");
				sql.append(" where ttc.PRODUCE_ID = p.ID and p.GRADE_ID = d.ID and ttc.`SUBJECT` = dd.ID and ttc.`STATUS` !='"+MiniClassStatus.CONPELETE+"' and ttc.TEACHER_ID = :teacherId ");
				
				List<Map<Object, Object>> mainClass = twoTeacherClassDao.findMapBySql(sql.toString(), params);
				UnChargedClassInfoVo unChargedClassInfoVo = null;
				if(mainClass!=null && mainClass.size()>0){
					for(Map<Object, Object> tMap:mainClass){
						unChargedClassInfoVo =new UnChargedClassInfoVo();
						unChargedClassInfoVo.setClassId(tMap.get("classId")!=null?tMap.get("classId").toString():"");
						unChargedClassInfoVo.setClassName(tMap.get("className")!=null?tMap.get("className").toString():"");
						unChargedClassInfoVo.setGradeId(tMap.get("gradeId")!=null?tMap.get("gradeId").toString():"");
						unChargedClassInfoVo.setGradeName(tMap.get("gradeName")!=null?tMap.get("gradeName").toString():"");
						unChargedClassInfoVo.setSubjectId(tMap.get("subjectId")!=null?tMap.get("subjectId").toString():"");
						unChargedClassInfoVo.setSubjectName(tMap.get("subjectName")!=null?tMap.get("subjectName").toString():"");
						unChargedClassInfoVo.setType(type);
						unChargedClassInfoVo.setClassIdTwo("");
						unChargedClassInfoVo.setClassNameTwo("");
						result.add(unChargedClassInfoVo);
					}
				}
				sql.delete(0, sql.length());
				sql.append(" select ttct.`NAME` as classNameTwo,ttct.CLASS_TWO_ID as classIdTwo,ttc.CLASS_ID as classId,ttc.`NAME` as className ,p.GRADE_ID as gradeId,d.`NAME` as gradeName,ttc.`SUBJECT` as subjectId,dd.`NAME` as subjectName ");
				sql.append(" from two_teacher_class_two ttct ");
				sql.append(" left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID ");
				sql.append(" left join product p on ttc.PRODUCE_ID = p.ID ");
				sql.append(" left join data_dict d on p.GRADE_ID = d.ID ");
				sql.append(" left join data_dict dd on ttc.`SUBJECT` = dd.ID ");
				sql.append(" where ttct.`STATUS` !='"+MiniClassStatus.CONPELETE+"' and ttct.TEACHER_ID = :teacherId ");
				List<Map<Object, Object>> classTwo = twoTeacherClassTwoDao.findMapBySql(sql.toString(), params);
				if(classTwo!=null && classTwo.size()>0){
					for(Map<Object, Object> tMap:classTwo){
						unChargedClassInfoVo =new UnChargedClassInfoVo();
						unChargedClassInfoVo.setClassId(tMap.get("classId")!=null?tMap.get("classId").toString():"");
						unChargedClassInfoVo.setClassName(tMap.get("className")!=null?tMap.get("className").toString():"");
						unChargedClassInfoVo.setGradeId(tMap.get("gradeId")!=null?tMap.get("gradeId").toString():"");
						unChargedClassInfoVo.setGradeName(tMap.get("gradeName")!=null?tMap.get("gradeName").toString():"");
						unChargedClassInfoVo.setSubjectId(tMap.get("subjectId")!=null?tMap.get("subjectId").toString():"");
						unChargedClassInfoVo.setSubjectName(tMap.get("subjectName")!=null?tMap.get("subjectName").toString():"");
						unChargedClassInfoVo.setType(type);
						unChargedClassInfoVo.setClassIdTwo(tMap.get("classIdTwo")!=null?tMap.get("classIdTwo").toString():"");
						unChargedClassInfoVo.setClassNameTwo(tMap.get("classNameTwo")!=null?tMap.get("classNameTwo").toString():"");
						result.add(unChargedClassInfoVo);
					}
				}
				
				map.put("resultStatus", 200);
				map.put("resultMessage", "老师查询自己未结课的班级列表");
				map.put("result", result);
				return map;
			}
			
			if(!flag){
				map.put("resultStatus", 200);
				map.put("resultMessage", "目前暂时只能查询双师的情况");
				map.put("result", null);		
			}			
			return map;
		} catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
			return map;
		}
	}

	@Override
	public Map<String, Object> getAllClassTwoByClassId(String[] classId) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer();
		params.put("classId", classId);
		sql.append(" select ttc.CLASS_ID as classId,ttct.CLASS_TWO_ID as classIdTwo,ttc.TEACHER_ID as teacherId,ttct.TEACHER_ID as teacherIdTwo,");
		sql.append(" ttc.`NAME` as className,ttct.`NAME` as classNameTwo, blCampus.id organizationId, blCampus.name organizationName, branch.id branchId, branch.name branchName, cm.id classroomId, cm.CLASS_ROOM classroomName  ");
		sql.append(" from two_teacher_class_two ttct left join classroom_manage cm on ttct.CLASS_ROOM_ID=cm.id  ,two_teacher_class ttc, organization blCampus , organization branch where blCampus.parentID=branch.id and ttct.BL_CAMPUS_ID = blCampus.id and ttct.CLASS_ID = ttc.CLASS_ID and ttct.CLASS_ID in ( :classId ) ");
		List<Map<Object, Object>> result = twoTeacherClassTwoDao.findMapBySql(sql.toString(), params);		
		map.put("resultStatus", 200);
		map.put("resultMessage", "副班信息");
		map.put("result", result);
		return map;
	}

	@Override
	public Map<String, Object> getUnChargedClassStudents(String teacherId, String type,String student) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<ClassStudentInfoVo> studentVos = new ArrayList<ClassStudentInfoVo>();
		StringBuffer query = new StringBuffer(128);
		Map<String, Object> params = Maps.newHashMap();
		
		try {
			ProductType productType = ProductType.valueOf(type);			
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//一对一
			}else if(productType == ProductType.ONE_ON_MANY){
				//一对多
			}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
				//小班
			}else if(productType == ProductType.TWO_TEACHER){				
				//双师的实现是teacherId作为主班老师查一遍主班（不返回classIdTwo），再作为辅导老师查一遍副班及对应的主班
				params.put("teacherId", teacherId);
				query.append(" select ttcs.STUDENT_ID as id,s.`NAME` as name from two_teacher_class ttc,two_teacher_class_two ttct,two_teacher_class_student ttcs,student s ");
				query.append(" where ttc.CLASS_ID = ttct.CLASS_ID and ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID and s.ID =ttcs.STUDENT_ID ");
				query.append(" and ttc.`STATUS` !='"+MiniClassStatus.CONPELETE+"' and ttc.TEACHER_ID = :teacherId ");
				if(StringUtils.isNotBlank(student)){
					query.append(" and s.`NAME` like :student ");
					params.put("student", "%"+student+"%");
				}
				List<Map<Object, Object>> mainClass = twoTeacherClassDao.findMapBySql(query.toString(), params);
				ClassStudentInfoVo classStudentInfoVo = null;
				if(mainClass!=null && mainClass.size()>0){
					for(Map<Object, Object> tMap:mainClass){
						classStudentInfoVo =new ClassStudentInfoVo();
						classStudentInfoVo.setId(tMap.get("id")!=null?tMap.get("id").toString():"");
						classStudentInfoVo.setName(tMap.get("name")!=null?tMap.get("name").toString():"");				
						studentVos.add(classStudentInfoVo);
					}
				}
				query.delete(0, query.length());
				query.append(" select ttcs.STUDENT_ID as id,s.`NAME` as name from two_teacher_class_two ttct,two_teacher_class_student ttcs,student s ");
				query.append(" where ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID and s.ID =ttcs.STUDENT_ID ");
				query.append(" and ttct.`STATUS` !='"+MiniClassStatus.CONPELETE+"' and ttct.TEACHER_ID = :teacherId ");
				if(StringUtils.isNotBlank(student)){
					query.append(" and s.`NAME` like :student ");
				}
				List<Map<Object, Object>> classTwo = twoTeacherClassTwoDao.findMapBySql(query.toString(), params);
				if(classTwo!=null && classTwo.size()>0){
					for(Map<Object, Object> tMap:classTwo){
						classStudentInfoVo =new ClassStudentInfoVo();
						classStudentInfoVo.setId(tMap.get("id")!=null?tMap.get("id").toString():"");
						classStudentInfoVo.setName(tMap.get("name")!=null?tMap.get("name").toString():"");				
						studentVos.add(classStudentInfoVo);
					}
				}
				//studentVos去重
				studentVos = removeDuplicate(studentVos);
				map.put("resultStatus", 200);
				map.put("resultMessage", "老师查询自己未结课的班级内学生列表");
				map.put("result", studentVos);
			}
		 } catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
		 }	
		return map;
	}
	private List<ClassStudentInfoVo> removeDuplicate(List<ClassStudentInfoVo> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getId().equals(list.get(i).getId())
						&&(StringUtils.isBlank(list.get(j).getCourseStatus())||list.get(j).getCourseStatus().equals(list.get(i).getCourseStatus()))) {
					list.remove(j);
				}
			}
		}		
		return list;
	}

	@Override
	public Map<String, Object> getClassCountByTextBookId(TextBookParamVo textBookParamVo) {
		//是否查询未结课的班级数量(1是0否)  1是未结课 0 全部
		Integer flag = textBookParamVo.getUnfinished();
		Map<String, Object> param = Maps.newHashMap();
		Map<String, Object> map = Maps.newHashMap();
		param.put("textBookIds", textBookParamVo.getWareId());
		StringBuffer sql = new StringBuffer();
		sql.append(" select TEXTBOOK_ID as wareId ,count(MINI_CLASS_ID) as count ");
		sql.append(" from mini_class mc where mc.TEXTBOOK_ID in ( :textBookIds )  ");
		if(flag==1){
			sql.append(" and mc.`STATUS` <> '"+MiniClassStatus.CONPELETE+"' ");
		}
		sql.append(" group by mc.TEXTBOOK_ID");
		map.put("resultStatus", 200);
		map.put("resultMessage", "查询信息");
		map.put("result", miniClassDao.findMapBySql(sql.toString(), param));
		return map;
	}

	@Override
	public Map<String, Object> getUnCompleteClassById(DataPackage dataPackage, String wareId) {
		//根据教材id查询已绑定的未结课班级列表(按分公司id分组,不分班级type,分页)(目前只支持星火小班)
		Integer start = (dataPackage.getPageNo() - 1) * dataPackage.getPageSize();
		Map<String, Object> param = Maps.newHashMap();
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> map = Maps.newHashMap();
		param.put("wareId", wareId);
		StringBuffer sql = new StringBuffer(" select p.ORGANIZATION_ID as branchId,o.`name` as branchName,mc.TEACHER_ID as teacherId,u.`NAME` as teacherName,mc.MINI_CLASS_ID as classId, mc.`NAME` as className ");
		StringBuffer sqlCount = new StringBuffer("select count(*) as total from (select o.id ");
		StringBuffer temp = new StringBuffer();
		temp.append(" from mini_class mc left join product p on mc.PRODUCE_ID = p.ID ");
		temp.append(" left join organization o on p.ORGANIZATION_ID = o.id ");
		temp.append(" left join `user` u on mc.TEACHER_ID = u.USER_ID ");  
		temp.append(" where mc.TEXTBOOK_ID = :wareId ");
		temp.append(" and mc.`STATUS` <> 'CONPELETE' ");
		temp.append(" group by o.id ");
        String count = sqlCount.append(temp).append(" ) as a ").toString();
        temp.append(" limit " + start + "," + dataPackage.getPageSize());
        
        List<Map<Object, Object>> list = miniClassDao.findMapBySql(sql.append(temp).toString(), param);
        
        StringBuffer countSql = new StringBuffer();
        countSql.append(" select count(o.id) as num,p.ORGANIZATION_ID as id ");
        countSql.append(" from mini_class mc,product p,organization o ");  
        countSql.append(" where mc.PRODUCE_ID = p.ID ");
        countSql.append(" and p.ORGANIZATION_ID = o.id ");
        countSql.append(" and mc.TEXTBOOK_ID = :wareId ");
        countSql.append(" and mc.`STATUS` <> 'CONPELETE' ");
        countSql.append(" group by o.id ");
        List<Map<Object, Object>> countList = miniClassDao.findMapBySql(countSql.toString(), param);
		if (list != null && list.size() > 0) {
			if(countList == null || countList.size()==0){
				return null;
			}
			Map<Object, Object> tempMap = Maps.newHashMap();
			
			for(Map<Object, Object> cMap:countList){
				tempMap.put(cMap.get("id"), cMap.get("num"));
            }
            for(Map<Object, Object> teMap:list){
            	Object id = teMap.get("branchId");
            	teMap.put("num",tempMap.get(id));
            }
		}
		
		Integer totalCount = miniClassDao.findCountSql(count,param);
		Integer pageSize = dataPackage.getPageSize();
		Integer totalPage = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			totalPage++;
		}
		resultMap.put("totalPage", totalPage);
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", list);
		
		map.put("resultStatus", 200);
		map.put("resultMessage", "查询信息");
		map.put("result", resultMap);
		return map;
	}

	@Override
	public Map<String, Object> getBranchUnCompleteClassById(DataPackage dataPackage, String wareId, String branchId) {
		//根据教材id查询已绑定的未结课班级列表(按分公司id分组,不分班级type,分页)(目前只支持星火小班)
		Integer start = (dataPackage.getPageNo() - 1) * dataPackage.getPageSize();
		Map<String, Object> param = Maps.newHashMap();
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> map = Maps.newHashMap();
		param.put("wareId", wareId);
		param.put("branchId", branchId);
		
		StringBuffer sql = new StringBuffer(" select p.ORGANIZATION_ID as branchId,o.`name` as branchName,mc.TEACHER_ID as teacherId,u.`NAME` as teacherName,mc.MINI_CLASS_ID as classId, mc.`NAME` as className ");
		StringBuffer sqlCount = new StringBuffer(" select count(*) as total ");
		StringBuffer temp = new StringBuffer();

		temp.append(" from mini_class mc left join product p on mc.PRODUCE_ID = p.ID ");  
		temp.append(" left join organization o on p.ORGANIZATION_ID = o.id ");
		temp.append(" left join `user` u on mc.TEACHER_ID = u.USER_ID ");  
		temp.append(" where mc.TEXTBOOK_ID = :wareId ");
		temp.append(" and mc.`STATUS` <> 'CONPELETE' ");
		temp.append(" and o.id = :branchId ");
		String count = sqlCount.append(temp).toString();
		temp.append(" limit " + start + "," + dataPackage.getPageSize());
		        
		List<Map<Object, Object>> list = miniClassDao.findMapBySql(sql.append(temp).toString(), param);
		        
		Integer totalCount = miniClassDao.findCountSql(count,param);
		Integer pageSize = dataPackage.getPageSize();
	    Integer totalPage = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			totalPage++;
		}
		resultMap.put("totalPage", totalPage);
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", list);
				
		map.put("resultStatus", 200);
		map.put("resultMessage", "查询信息");
		map.put("result", resultMap);
		return map;
	}

	@Override
	public Map<String, Object> getClassStudents(String type, String teacherId, Boolean finish) {
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		//TODO 
		return map;
		
//		List<ClassStudentInfoVo> studentVos = new ArrayList<ClassStudentInfoVo>();
//		StringBuffer query = new StringBuffer(128);
//		Map<String, Object> params = Maps.newHashMap();
//		
//		
//		//TODO 
//		//finish true 已结课 false 未结课
//		String student ="";
//		
//		try {
//			ProductType productType = ProductType.valueOf(type);			
//			if(productType == ProductType.ONE_ON_ONE_COURSE){
//				//一对一
//			}else if(productType == ProductType.ONE_ON_MANY){
//				//一对多
//			}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
//				//小班
//			}else if(productType == ProductType.TWO_TEACHER){				
//				//双师的实现是teacherId作为主班老师查一遍主班（不返回classIdTwo），再作为辅导老师查一遍副班及对应的主班
//				params.put("teacherId", teacherId);
//				query.append(" select ttcs.STUDENT_ID as id,s.`NAME` as name from two_teacher_class ttc,two_teacher_class_two ttct,two_teacher_class_student ttcs,student s ");
//				query.append(" where ttc.CLASS_ID = ttct.CLASS_ID and ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID and s.ID =ttcs.STUDENT_ID ");
//				query.append(" and ttc.`STATUS` !='"+MiniClassStatus.CONPELETE+"' and ttc.TEACHER_ID = :teacherId ");
//				if(StringUtils.isNotBlank(student)){
//					query.append(" and s.`NAME` like :student ");
//					params.put("student", "%"+student+"%");
//				}
//				List<Map<Object, Object>> mainClass = twoTeacherClassDao.findMapBySql(query.toString(), params);
//				ClassStudentInfoVo classStudentInfoVo = null;
//				if(mainClass!=null && mainClass.size()>0){
//					for(Map<Object, Object> tMap:mainClass){
//						classStudentInfoVo =new ClassStudentInfoVo();
//						classStudentInfoVo.setId(tMap.get("id")!=null?tMap.get("id").toString():"");
//						classStudentInfoVo.setName(tMap.get("name")!=null?tMap.get("name").toString():"");				
//						studentVos.add(classStudentInfoVo);
//					}
//				}
//				query.delete(0, query.length());
//				query.append(" select ttcs.STUDENT_ID as id,s.`NAME` as name from two_teacher_class_two ttct,two_teacher_class_student ttcs,student s ");
//				query.append(" where ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID and s.ID =ttcs.STUDENT_ID ");
//				query.append(" and ttct.`STATUS` !='"+MiniClassStatus.CONPELETE+"' and ttct.TEACHER_ID = :teacherId ");
//				if(StringUtils.isNotBlank(student)){
//					query.append(" and s.`NAME` like :student ");
//				}
//				List<Map<Object, Object>> classTwo = twoTeacherClassTwoDao.findMapBySql(query.toString(), params);
//				if(classTwo!=null && classTwo.size()>0){
//					for(Map<Object, Object> tMap:classTwo){
//						classStudentInfoVo =new ClassStudentInfoVo();
//						classStudentInfoVo.setId(tMap.get("id")!=null?tMap.get("id").toString():"");
//						classStudentInfoVo.setName(tMap.get("name")!=null?tMap.get("name").toString():"");				
//						studentVos.add(classStudentInfoVo);
//					}
//				}
//				//studentVos去重
//				studentVos = removeDuplicate(studentVos);
//				map.put("resultStatus", 200);
//				map.put("resultMessage", "老师查询自己未结课的班级内学生列表");
//				map.put("result", studentVos);
//			}
//		 } catch (IllegalArgumentException e) {
//			map.put("resultStatus", 400);
//			map.put("resultMessage", "课程类型不对");
//			map.put("result", null);
//		 }	
//		return map;
	}

	@Override
	public Map<String, Object> getClassStudentsListByStatus(String teacherId, String type, String student,
			String status) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<ClassStudentInfoVo> studentVos = new ArrayList<ClassStudentInfoVo>();
		StringBuffer query = new StringBuffer(128);
		Map<String, Object> params = Maps.newHashMap();
		
		try {
			ProductType productType = ProductType.valueOf(type);			
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//一对一
				params.put("teacherId", teacherId);
				query.append(" select DISTINCT c.STUDENT_ID as id,s.name as name,if(COURSE_STATUS='CHARGED','finished','unfinished') as courseStatus,d.name as subject,dd.name as grade ");
				query.append(" from course c ");
				query.append(" left join student s on s.ID = c.STUDENT_ID ");
				query.append(" left join data_dict d on d.ID = c.SUBJECT ");
				query.append(" left join data_dict dd on dd.ID = c.GRADE ");
				query.append(" where c.TEACHER_ID = :teacherId ");
				if(StringUtils.isNotBlank(student)){
					query.append(" and s.name like :student ");
					params.put("student", "%"+student+"%");
				}
				if(StringUtils.isNotBlank(status)) {
					if("finished".equals(status)) {
						query.append(" and c.COURSE_STATUS !='"+CourseStatus.NEW+"' ");
					}else if("unfinished".equals(status)){
						query.append(" and c.COURSE_STATUS ='"+CourseStatus.NEW+"' ");
					}
				}
				query.append(" group by c.STUDENT_ID,c.COURSE_STATUS,c.SUBJECT,c.GRADE ");
				List<Map<Object, Object>> mainClass = twoTeacherClassDao.findMapBySql(query.toString(), params);
				
				map.put("resultStatus", 200);
				map.put("resultMessage", "老师查询一对一学生列表");
				map.put("result", mainClass);
			}else if(productType == ProductType.ONE_ON_MANY){
				//一对多
			}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
				//小班
			}else if(productType == ProductType.TWO_TEACHER){				
				//双师的实现是teacherId作为主班老师查一遍主班（不返回classIdTwo），再作为辅导老师查一遍副班及对应的主班
				String courseStatus_sql = "";
				if(StringUtils.isNotBlank(status)) {
					if("finished".equals(status)) {
						courseStatus_sql = " and ttc.STATUS ='"+MiniClassStatus.CONPELETE+"' ";
					}else if("unfinished".equals(status)){
						courseStatus_sql = " and ttc.STATUS !='"+MiniClassStatus.CONPELETE+"' ";
					}
				}
				params.put("teacherId", teacherId);
				query.append(" select ttcs.STUDENT_ID as id,s.`NAME` as name,if(ttc.STATUS='CONPELETE','finished','unfinished') as courseStatus from two_teacher_class ttc,two_teacher_class_two ttct,two_teacher_class_student ttcs,student s ");
				query.append(" where ttc.CLASS_ID = ttct.CLASS_ID and ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID and s.ID =ttcs.STUDENT_ID ");
				query.append(courseStatus_sql + " and ttc.TEACHER_ID = :teacherId ");
				if(StringUtils.isNotBlank(student)){
					query.append(" and s.`NAME` like :student ");
					params.put("student", "%"+student+"%");
				}
				List<Map<Object, Object>> mainClass = twoTeacherClassDao.findMapBySql(query.toString(), params);
				ClassStudentInfoVo classStudentInfoVo = null;
				if(mainClass!=null && mainClass.size()>0){
					for(Map<Object, Object> tMap:mainClass){
						classStudentInfoVo =new ClassStudentInfoVo();
						classStudentInfoVo.setId(tMap.get("id")!=null?tMap.get("id").toString():"");
						classStudentInfoVo.setName(tMap.get("name")!=null?tMap.get("name").toString():"");
						classStudentInfoVo.setCourseStatus(tMap.get("courseStatus")!=null?tMap.get("courseStatus").toString():"");
						studentVos.add(classStudentInfoVo);
					}
				}
				query.delete(0, query.length());
				query.append(" select ttcs.STUDENT_ID as id,s.`NAME` as name,if(ttc.STATUS='CONPELETE','finished','unfinished') as courseStatus from two_teacher_class_two ttc,two_teacher_class_student ttcs,student s ");
				query.append(" where ttc.CLASS_TWO_ID = ttcs.CLASS_TWO_ID and s.ID =ttcs.STUDENT_ID ");
				query.append(courseStatus_sql + " and ttc.TEACHER_ID = :teacherId ");
				if(StringUtils.isNotBlank(student)){
					query.append(" and s.`NAME` like :student ");
				}
				List<Map<Object, Object>> classTwo = twoTeacherClassTwoDao.findMapBySql(query.toString(), params);
				if(classTwo!=null && classTwo.size()>0){
					for(Map<Object, Object> tMap:classTwo){
						classStudentInfoVo =new ClassStudentInfoVo();
						classStudentInfoVo.setId(tMap.get("id")!=null?tMap.get("id").toString():"");
						classStudentInfoVo.setName(tMap.get("name")!=null?tMap.get("name").toString():"");
						classStudentInfoVo.setCourseStatus(tMap.get("courseStatus")!=null?tMap.get("courseStatus").toString():"");
						studentVos.add(classStudentInfoVo);
					}
				}
				//studentVos去重
				studentVos = removeDuplicate(studentVos);
				map.put("resultStatus", 200);
				map.put("resultMessage", "老师查询自己班级内学生列表");
				map.put("result", studentVos);
			}
		 } catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
		 }	
		return map;
	}

	/**
	 * 学生一对一课程 按照年级和科目聚合成一门课程
	 *
	 * @param studentId
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getOneOnOneCourseByStudentId(String studentId, Map<String, Object> map) {
		List<Map<Object, Object>> list = getOneOnOneCourseMaps(studentId, null, null);
		for (Map<Object, Object> gradeAndSubjectAndStudent : list){
			Course course = getNextCourseByGradeAndSubjectAndStudentId((String)gradeAndSubjectAndStudent.get("gradeId"), (String)gradeAndSubjectAndStudent.get("subjectId"), studentId);
			if (course!=null){
			    gradeAndSubjectAndStudent.put("nextTime", course.getCourseDate()+" "+course.getCourseTime());
				gradeAndSubjectAndStudent.put("studyManagerName", course.getStudyManager().getName());
				gradeAndSubjectAndStudent.put("studyManagerAccount", course.getStudyManager().getAccount());
				gradeAndSubjectAndStudent.put("teacherName", course.getTeacher().getName());
				gradeAndSubjectAndStudent.put("teacherAccount", course.getTeacher().getAccount());
			}else{
				gradeAndSubjectAndStudent.put("nextTime", null);
				gradeAndSubjectAndStudent.put("studyManagerName", null);
				gradeAndSubjectAndStudent.put("studyManagerAccount", null);
				gradeAndSubjectAndStudent.put("teacherName", null);
				gradeAndSubjectAndStudent.put("teacherAccount", null);
			}
		}
		map.put("resultStatus", 200);
		map.put("resultMessage", "学生查询自己所属的一对一课程");
		map.put("result", list);
		return map;
	}

	private List<Map<Object, Object>> getOneOnOneCourseMaps(String studentId, String gradeId, String subjectId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append("	 SELECT                                                   ");
		sql.append("	 		c.STUDENT_ID as studentId ,                                     ");
		sql.append("	 c. SUBJECT subjectId,                                    ");
		sql.append("	 SUBJECT .`NAME` subjectName,                             ");
		sql.append("	 		c.GRADE gradeId,                                  ");
		sql.append("	 		GRADE.`NAME` gradeName,                           ");
		sql.append("	 		sum(                                              ");
		sql.append("	 				CASE                                      ");
		sql.append("	 				WHEN CONCAT(c.COURSE_DATE,\' \',SUBSTR(c.COURSE_TIME,1,5)) < NOW() THEN      ");
		sql.append("	 				1                                         ");
		sql.append("	 				ELSE                                      ");
		sql.append("	 				0                                         ");
		sql.append("	 				END                                       ");
		sql.append("	 		) overClassNum,                                   ");
		sql.append("	 		sum(1) allClassNum                              ");
		sql.append("	 		FROM                                              ");
		sql.append("	 course c,                                                ");
		sql.append("	 data_dict SUBJECT,                                       ");
		sql.append("	 data_dict grade                                          ");
		sql.append("	 WHERE     1=1                                             ");
		if (StringUtils.isNotBlank(studentId)){
			sql.append("	 AND		STUDENT_ID = :studentId                         ");
			params.put("studentId", studentId);
		}
		sql.append("	 AND SUBJECT .ID = c.`SUBJECT`                            ");
		if (StringUtils.isNotBlank(subjectId)){
			sql.append("  AND  c.`SUBJECT`= :subjectId        ");
			params.put("subjectId", subjectId);
		}
		sql.append("	 AND GRADE.ID = c.GRADE                                   ");
		if (StringUtils.isNotBlank(gradeId)){
			sql.append(" AND c.GRADE = :gradeId   ");
			params.put("gradeId", gradeId);
		}
		if (params.size()==0){
			sql.append(" AND 1=2     ");
		}
		sql.append("	 GROUP BY                                                 ");
		sql.append("	 SUBJECT,                                                 ");
		sql.append("	 		GRADE                                             ");

		return courseDao.findMapBySql(sql.toString(), params);
	}

	private Course getNextCourseByGradeAndSubjectAndStudentId(String gradeId, String subjectId, String studentId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append("    SELECT                                              ");
		sql.append("    		*                                           ");
		sql.append("    		FROM                                        ");
		sql.append("    course                                              ");
		sql.append("    		WHERE                                       ");
		sql.append("   SUBJECT = :subjectId                                ");
		sql.append("   AND GRADE = :gradeId                                 ");
		sql.append("   AND STUDENT_ID = :studentId                          ");
		sql.append("    AND COURSE_DATE>=CURDATE()                          ");
		sql.append("    ORDER BY                                            ");
		sql.append("    COURSE_DATE                                         ");
		params.put("subjectId", subjectId);
		params.put("gradeId", gradeId);
		params.put("studentId", studentId);
		List<Course> courseList = courseDao.findBySql(sql.toString(), params);
		if (courseList.size()>0){
			return courseList.get(0);
		}
		return null;
	}


	/**
	 *
	 * NEW("NEW", "新签"),//新签
	 *CLASSING("CLASSING", "上课中"),//上课中
	 *STOP_CLASS("STOP_CLASS", "停课"),//停课
	 *FINISH_CLASS("FINISH_CLASS", "结课"),//结课
	 *SPECIAL_STOP("SPECIAL_STOP", "特殊停课"),//特殊停课
	 *GRADUATION("GRADUATION","毕业");//毕业
	 * 老师查一对一的学生列表
	 *
	 * @param teacherId
	 * @param student
	 * @param status
	 * @return
	 */
	@Override
	public Map<String, Object> getOneOnOneStudentsListByTeacherId(String teacherId, String student, String status) {

		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		params.put("teacherId", teacherId);
	    sql.append("	 SELECT                                                           ");
	    sql.append("	 s.ID id,                                                         ");
	    sql.append("	 s.`NAME` name,                                                   ");
	    sql.append("	 		s.GRADE_ID gradeId,                                          ");
	    sql.append("	 		grade.`NAME` gradeName,                                   ");
	    sql.append("	 		s.ONEONONE_STATUS status                                  ");
	    sql.append("	 		FROM                                                      ");
	    sql.append("	 student s LEFT JOIN data_dict grade ON s.GRADE_ID = grade.ID ,      ");
	    sql.append("	 course c                                                         ");
	    sql.append("	 WHERE                                                            ");
	    sql.append("	 s.ID = c.STUDENT_ID                                              ");
	    sql.append("	 AND c.TEACHER_ID = :teacherId                               ");
	    sql.append("	 AND s.ONEONONE_STATUS IS NOT NULL                                ");
	    if (StringUtils.isNotBlank(student)){
	    	sql.append("	 AND s.`NAME` LIKE :student                                         ");
			params.put("student", "%"+student+"%");
		}
		if (StringUtils.isNotBlank(status)){
	    	sql.append(" AND s.ONEONONE_STATUS=:status    ");
	    	params.put("status", status);
		}
		sql.append(" GROUP BY s.ID ");
		sql.append(" ORDER BY s.ONEONONE_STATUS ");


		List<Map<Object, Object>> list = courseDao.findMapBySql(sql.toString(), params);



		Map<String, Object> result = new HashMap<>();
		result.put("resultStatus", 200);
		result.put("resultMessage", "老师查询自己的一对一学生");
		result.put("result", list);
		return result;
	}

	/**
	 * 根据主班id和客户电话获取学生信息
	 *
	 * @param classId
	 * @param phone
	 * @return
	 */
	@Override
	public Map<String, Object> getStudentInfoByClassIdAndPhone(String classId, String phone) {
		StringBuffer sql = new StringBuffer();

		Map<String, Object> params = new HashMap<>();

		params.put("classId", classId);
		params.put("phone", phone);

		sql.append(" SELECT                                           ");
		sql.append(" s.ID studentId,                                  ");
		sql.append(" s.`NAME` studentName,                            ");
		sql.append(" 		p.GRADE_ID gradeId,                       ");
		sql.append(" 		grade.`NAME` gradeName,                   ");
		sql.append(" 		ss.`NAME` schoolName                      ");
		sql.append(" 		FROM                                      ");
		sql.append(" student s,                                       ");
		sql.append(" customer_student_relation csr,                   ");
		sql.append(" customer c,                                      ");
		sql.append(" product p,                                       ");
		sql.append(" data_dict grade,                                 ");
		sql.append(" two_teacher_class ttc,                           ");
		sql.append(" two_teacher_class_two ttct,                      ");
		sql.append(" two_teacher_class_student ttcs,                  ");
		sql.append(" student_school ss                                ");
		sql.append(" WHERE                                            ");
		sql.append(" s.ID = csr.STUDENT_ID                            ");
		sql.append(" AND csr.CUSTOMER_ID = c.ID                       ");
		sql.append(" AND ttcs.STUDENT_ID = s.ID                       ");
		sql.append(" AND ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID        ");
		sql.append(" AND ttct.CLASS_ID = ttc.CLASS_ID                 ");
		sql.append(" AND ttc.PRODUCE_ID = p.ID                        ");
		sql.append(" AND s.SCHOOL = ss.ID                             ");
		sql.append(" AND p.GRADE_ID = grade.ID                        ");
		sql.append(" AND ttc.CLASS_ID = :classId                            ");
		sql.append(" AND c.CONTACT = :phone                           ");

		List<Map<Object, Object>> list = twoTeacherClassTwoDao.findMapBySql(sql.toString(), params);
		Map<String, Object> result = new HashMap<>();
		result.put("resultStatus", 200);
		result.put("resultMessage", "学生信息");
		result.put("result", list);
		return result;
	}

	/**
	 * @param studentId
	 * @param gradeId
	 * @param subjectId
	 * @param map
	 * @return
	 */
	@Override
	public Map<String, Object> getOneOnOneCourse(String studentId, String gradeId, String subjectId, Map<String, Object> map) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();

        sql.append("	  	SELECT                                                      ");
        sql.append("	  	c.course_id as courseId,                                                ");
        sql.append("	  			grade.`NAME` gradeName,                             ");
        sql.append("	  			SUBJECT .`NAME` subjectName,                        ");
        sql.append("	  			CONCAT(c.COURSE_DATE,\'\') as courseDate,                                      ");
        sql.append("	  			c.COURSE_TIME as courseTime,                                      ");
        sql.append("	  			courseTeacher.`NAME` courseTeacherName,             ");
        sql.append("	  			c.PLAN_HOURS courseHours ,                                       ");
        sql.append("	  			studyManager.`NAME` studyManagerName,               ");
        sql.append("	  			studyManager.`ACCOUNT` studyManagerAccount,               ");
        sql.append("	  			blCampus.`name` blCampusName,                        ");
        sql.append("	  			CASE WHEN CONCAT(c.COURSE_DATE,\' \',SUBSTR(c.COURSE_TIME,1,5)) > NOW() THEN \'NEW\' ELSE \'OLD\' END   courseState     ");
        sql.append("	  			FROM                                                ");
        sql.append("	  	course c,                                                   ");
        sql.append("	  	data_dict grade,                                            ");
        sql.append("	  	data_dict SUBJECT,                                          ");
        sql.append("	  `user` courseTeacher,                                         ");
        sql.append("	  `user` studyManager,                                          ");
        sql.append("	  			organization blCampus                               ");
        sql.append("	  			WHERE                                               ");
        sql.append("  	c.STUDENT_ID = :studentId                                ");
        sql.append("  	AND c.`SUBJECT` = :subjectId                               ");
        sql.append("  	AND c.GRADE = :gradeId                      ");
        sql.append("	  	AND c.GRADE = grade.id                                      ");
        sql.append("	  	AND c.`SUBJECT` = SUBJECT .ID                               ");
        sql.append("	  	AND c.TEACHER_ID = courseTeacher.USER_ID                    ");
        sql.append("	  	AND c.STUDY_MANAGER_ID = studyManager.USER_ID               ");
        sql.append("	  	AND c.BL_CAMPUS_ID = blCampus.id                            ");
        sql.append("	  	ORDER BY                                                    ");
        sql.append("	  	c.COURSE_DATE;                                              ");

        params.put("subjectId", subjectId);
        params.put("gradeId", gradeId);
        params.put("studentId", studentId);

        List<Map<Object, Object>> courseList = courseDao.findMapBySql(sql.toString(), params);
        Course nextCourse = getNextCourseByGradeAndSubjectAndStudentId(gradeId, subjectId, studentId);
        List<Map<Object, Object>> info = getOneOnOneCourseMaps(studentId, gradeId, subjectId);
        Map<Object, Object> objectObjectMap = info.get(0);
		BigDecimal overClassNum = (BigDecimal) objectObjectMap.get("overClassNum");
		BigDecimal allClassNum =  (BigDecimal) objectObjectMap.get("allClassNum");

		BigDecimal notOverClassNum = allClassNum.subtract(overClassNum);

        Map<String, Object> result =  new HashMap<>();
        if (courseList.size()>0){
			Map<Object, Object> firstRow = courseList.get(0);
			String gradeName = (String) firstRow.get("gradeName");
			result.put("gradeName", gradeName);
			String subjectName = (String) firstRow.get("subjectName");
			result.put("subjectName", subjectName);
			String studyManagerName = (String) firstRow.get("studyManagerName");
			result.put("studyManagerName", studyManagerName);
			String studyManagerAccount = (String) firstRow.get("studyManagerAccount");
			result.put("studyManagerAccount", studyManagerAccount);
			int i = 0;
			for (Map<Object, Object> eachRow : courseList){
				eachRow.put("orderNumber", i);
				i++;
			}
		}
		if (nextCourse!=null){
			result.put("nextDateTime", nextCourse.getCourseDate()+" "+nextCourse.getCourseTime());
			Organization blCampus = nextCourse.getBlCampusId();
			if (blCampus!=null){
				result.put("campus", blCampus.getName());
			}
			result.put("subjectTeacher", nextCourse.getTeacher().getName());
		}

		result.put("overClassNum", overClassNum);
        result.put("notOverClassNum", notOverClassNum);
		result.put("courseList", courseList);

		return result;
	}

	@Override
	public Map<String, Object> getCourseDetailByTeacherTwo(String teacherTwoId, String type, String status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String,Object>();
		StringBuffer query = new StringBuffer(128);
		Map<String, Object> params = Maps.newHashMap();
		
		try {
			ProductType productType = ProductType.valueOf(type);			
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//一对一
				
			}else if(productType == ProductType.ONE_ON_MANY){
				//一对多
			}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
				//小班
			}else if(productType == ProductType.TWO_TEACHER){				
				//双师
				query.append(" select ttc.class_id as classId,ttc.name as className,d.id as gradeId,d.name as gradeName,dd.id as subjectId,dd.name as subjectName ");
				query.append(" ,'TWO_TEACHER' as type,ttct.class_two_id as classIdTwo,ttct.name as classNameTwo,ttc.teacher_id as teacherId,u.name as teacherName ");
				query.append(" ,ttc.start_date as beginDate,s.endDate as endDate");
				query.append(" from two_teacher_class ttc ");
				query.append(" left join product p on ttc.produce_id = p.id");
				query.append(" left join data_dict d on p.grade_id = d.id ");//年级
				query.append(" left join data_dict dd on ttc.subject = dd.id ");//学科
				query.append(" left join user u on ttc.teacher_id = u.user_id ");//主讲师
				query.append(" left join two_teacher_class_two ttct on ttc.CLASS_ID = ttct.CLASS_ID ");
				query.append(" left join ( select class_id,MAX(COURSE_DATE) as endDate from two_teacher_class_course group by class_id ) s on ttc.class_id = s.class_id ");
				query.append(" where ttct.TEACHER_ID =:teacherTwoId ");
				params.put("teacherTwoId", teacherTwoId);
				if(StringUtils.isNotBlank(status)) {
					if("1".equals(status)) {
						query.append(" and ttc.start_date > CURDATE()");
					}else if("2".equals(status)){
						query.append(" and ttc.start_date <= CURDATE() and s.endDate >= CURDATE()");
					}else if("3".equals(status)){
						query.append("  and s.endDate < CURDATE()");
					}
				}
				List<Map<Object, Object>> details = twoTeacherClassTwoDao.findMapBySql(query.toString(), params);
				map.put("resultStatus", 200);
				map.put("resultMessage", "双师辅班查询");
				map.put("result", details);
			}
		 } catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
		 }	
		return map;
	}
	
}
