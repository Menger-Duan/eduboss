package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseConflictDao;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.domain.CourseConflict;
import com.eduboss.domainVo.CourseConflictVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Repository("CourseConflictDao")
public class CourseConflictDaoImpl extends GenericDaoImpl<CourseConflict,Long> implements CourseConflictDao {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JdbcTemplateDao jdbcTemplateDao;

	@Override
	public DataPackage findCourseConflictInfos(
			CourseConflictVo courseConflictVo,DataPackage dp) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		buildCriterionCause(criterionList,courseConflictVo);
		return super.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "startTime", "asc"),
				criterionList);
	}

	@Override
	public int countCourseConflictInfos(CourseConflictVo courseConflictVo) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		buildCriterionCause(criterionList,courseConflictVo);
		return findCountByCriteria(criterionList);
	}

	/**
	 * 构建查询条件
	 * @param criterionList
	 * @param courseConflictVo
	 */
	private void buildCriterionCause(List<Criterion> criterionList,
			CourseConflictVo courseConflictVo) {
		if(StringUtils.isNotBlank(courseConflictVo.getCurrentCourseId())){
			criterionList.add(Restrictions.ne("courseId", courseConflictVo.getCurrentCourseId()));
		}
		if(StringUtils.isNotBlank(courseConflictVo.getCourseId())){
			criterionList.add(Restrictions.eq("courseId", courseConflictVo.getCourseId()));
		}
		if(StringUtils.isNotBlank(courseConflictVo.getStudentId())){
			criterionList.add(Restrictions.eq("student.id", courseConflictVo.getStudentId()));
		}
		if(StringUtils.isNotBlank(courseConflictVo.getTeacherId())){
			criterionList.add(Restrictions.eq("teacher.id", courseConflictVo.getTeacherId()));
		}
		if(courseConflictVo.getStartTime() != null){
			criterionList.add(Restrictions.eq("startTime", courseConflictVo.getStartTime()));
		}
		if(courseConflictVo.getEndTime() != null){
			criterionList.add(Restrictions.eq("endTime", courseConflictVo.getEndTime()));
		}
		if(StringUtils.isNotBlank(courseConflictVo.getCourseDate())){
			criterionList.add(Restrictions.sqlRestriction("substring(start_time,1,8) = " + courseConflictVo.getCourseDate().replaceAll("-", "")));
		}
		if(StringUtils.isNotBlank(courseConflictVo.getStartDate()) && StringUtils.isNotBlank(courseConflictVo.getEndDate())){
			criterionList.add(Restrictions.sqlRestriction("substring(start_time,1,8) >= " + courseConflictVo.getStartDate().replaceAll("-", "")));
			criterionList.add(Restrictions.sqlRestriction("substring(start_time,1,8) <= " + courseConflictVo.getEndDate().replaceAll("-", "")));
		}
		if(StringUtils.isNotBlank(courseConflictVo.getCourseTime())){
			criterionList.add(Restrictions.sqlRestriction("substring(start_time,9) = " + courseConflictVo.getCourseTime().replaceAll(" - ", "").replaceAll(":", "")));
		}
	}

	@Override
	public int countDistinctConflicts(String courseId,String studentId,String teacherId,String courseDate,String courseTime) {
		String courseDateStart = courseDate.replace("-", "") + "0000";
		String courseDateEnd = courseDate.replace("-", "") + "2359";
		String courseStartTime = courseDate.replace("-", "") + courseTime.substring(0, 2) + courseTime.substring(3, 6);
		String courseEndTime = courseDate.replace("-", "") + courseTime.substring(8, 10) + courseTime.substring(11, 13);
		
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("           select count(distinct course_id) from course_conflict where 1=1 ");
//        sql.append(" and bl_campus_id='").append(userService.getBelongCampus().getId()).append("'");
        sql.append(" and start_time > :courseDateStart  and start_time < :courseDateEnd ");
		sql.append("                 and (                                                            ");
		sql.append("           			  ((                        								  ");
		sql.append("                               start_time <= :courseStartTime and end_time > :courseStartTime ");
		sql.append("          		         or start_time < :courseEndTime and end_time >= :courseEndTime ");
		sql.append("          		      )                   																    ");
		sql.append(" or ");
		sql.append(" ( start_time >= :courseStartTime  and start_time < :courseEndTime ");
		sql.append(" and end_time > :courseStartTime and end_time <= :courseEndTime ");
		sql.append("          		 ))                																			");
		sql.append("  and (                     student_id = :studentId ))");
		params.put("courseDateStart", courseDateStart);
		params.put("courseDateEnd", courseDateEnd);
		params.put("courseStartTime", courseStartTime);
		params.put("courseEndTime", courseEndTime);
		params.put("studentId", studentId);
		if(StringUtils.isNotBlank(teacherId)){
			sql.replace(sql.length()-2, sql.length(), " ");
			sql.append("  or teacher_id = :teacherId ))");
			params.put("teacherId", teacherId);
		}

		int count=findCountSql(sql.toString(), params);
//		int count=jdbcTemplateDao.findCountSql(sql.toString());
		if(count>0)
			count=count-1;
		return count;
	}

	@Override
	public int countMiniClassDistinctConflicts(String courseId,String studentId,String teacherId,String courseDate,String courseTime) {
		String courseDateStart = courseDate.replace("-", "") + "0000";
		String courseDateEnd = courseDate.replace("-", "") + "2359";
		String courseStartTime = courseDate.replace("-", "") + courseTime.substring(0, 2) + courseTime.substring(3, 6);
		String courseEndTime = courseDate.replace("-", "") + courseTime.substring(8, 10) + courseTime.substring(11, 13);
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("           select count(distinct course_id) from course_conflict where 1=1 ");
//        sql.append(" and bl_campus_id='").append(userService.getBelongCampus().getId()).append("'");
		sql.append(" and start_time > :courseDateStart and start_time < :courseDateEnd ");
		sql.append("                 and ((                                                            ");
//		sql.append("                       student_id = '").append(studentId).append("' and  ");
		sql.append("           			  (                        																");
		sql.append("                              :courseStartTime between start_time and end_time  and  :courseStartTime <> end_time");
		sql.append("          		         or :courseEndTime between start_time and end_time  and :courseEndTime <> start_time ");
		sql.append("          		      )                   																    ");
		sql.append(" or ");
		sql.append(" ( start_time BETWEEN :courseStartTime and :courseEndTime and start_time <> :courseEndTime  ");
		sql.append(" and end_time BETWEEN :courseStartTime and :courseEndTime and end_time <> :courseStartTime )  ");
		sql.append("          		 )                																			");
		params.put("courseDateStart", courseDateStart);
		params.put("courseDateEnd", courseDateEnd);
		params.put("courseStartTime", courseStartTime);
		params.put("courseEndTime", courseEndTime);
		
		if(StringUtils.isNotBlank(teacherId)){
			sql.append("                 and                             															");
			sql.append("                 (                                                            ");
			sql.append("                       teacher_id = :teacherId and  ");
			sql.append("           			  ((                        																");
			sql.append("                              :courseStartTime between start_time and end_time  and :courseStartTime <> end_time");
			sql.append("          		         or :courseEndTime between start_time and end_time  and :courseEndTime <> start_time ");
			sql.append("          		      )                   																    ");
			sql.append(" or ");
			sql.append(" ( start_time BETWEEN :courseStartTime and :courseEndTime and start_time <> :courseEndTime  ");
			sql.append(" and end_time BETWEEN :courseStartTime and :courseEndTime and end_time <> :courseStartTime ) ) ");
			sql.append("          		 ))                																			");
			params.put("teacherId", teacherId);
		}


		String sqlString = sql.toString();
		int count=findCountSql(sqlString, params);
		if(count>0)
			count=count-1;
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findConflictDateByCause(String startDate,
			String endDate, String courseTime, String teacherId,
			String studentId,String courseSummaryId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer("select distinct concat(substr(start_time,1,4),'-',substr(start_time,5,2),'-',substr(start_time,7,2)) as courseDate from course_conflict where 1 = 1 ");
        sql.append(" and bl_campus_id= :belongCampusId ");
		params.put("belongCampusId", userService.getBelongCampus().getId());
        if(StringUtils.isNotBlank(startDate)){
			sql.append(" and substr(start_time,1,8) >=  :startTime ");
			params.put("startTime", startDate.replaceAll("-", ""));
		}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and substr(end_time,1,8) <=  :endTime ");
			params.put("endTime", endDate.replaceAll("-", ""));
		}
		if(StringUtils.isNotBlank(courseTime)){
			String startTime = courseTime.split("-")[0].replaceAll(":", "").trim();
			sql.append(" and :courseTime between substr(start_time,9) and substr(end_time,9) ");
			sql.append(" and :courseTime <> substr(end_time,9) ");
			params.put("courseTime", startTime);
		}
		if(StringUtils.isNotBlank(teacherId)){
			sql.append(" and teacher_id = :teacherId ");
			params.put("teacherId", teacherId);
		}
		if(StringUtils.isNotBlank(studentId)){
			sql.append(" and student_id = :studentId ");
			params.put("studentId", studentId);
		}
        if(StringUtils.isNotBlank(courseSummaryId)){
            sql.append(" and course_id not in(select course_id from course where course_summary_id = :courseSummaryId )");
            params.put("courseSummaryId", courseSummaryId);
        }
		SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        super.setParamsIfNotNull(QueryTypeEnum.SQL, params, query);
		return query.addScalar("courseDate").list();
	}
	
	/**
	 * 删除冲突
	 */
	public int deleteCourseConflictByCourseIdAndStudentId(String courseId,String studentId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("courseId", courseId);
		params.put("studentId", studentId);
		return this.excuteHql(" delete from CourseConflict where courseId= :courseId and student.id= :studentId ", params);
	}
	
	/**
	 * 删除小班，一对多冲突
	 */
	public int deleteCourseConflictByCourseId(String courseId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("courseId", courseId);
		return this.excuteHql(" delete from CourseConflict where courseId= :courseId ", params);
	}
	
	

    /**
     * <!--删除昨天以前的数据-->
     * 删除60天(两个月)以前的数据
     */
    @Override
    public void deleteDatasBeforeYesterday() {
		Map<String, Object> params = Maps.newHashMap();
		String dateTime = DateTools.addDateToString(DateTools.getCurrentDateTime(), -60).replace("-", "") + "0000";
		Long dateTimeLong = Long.valueOf(dateTime);
		params.put("dateTime", dateTimeLong);
		this.excuteHql(" delete from CourseConflict where startTime < :dateTime ", params);
    }
       

	@Override
	public List<CourseConflict> getCourseConflictList(String courseId,
			String studentId, String teacherId, String courseDate,
			String courseTime) {
		String courseDateStart = courseDate.replace("-", "") + "0000";
		String courseDateEnd = courseDate.replace("-", "") + "2359";
		String courseStartTime = courseDate.replace("-", "") + courseTime.substring(0, 2) + courseTime.substring(3, 6);
		String courseEndTime = courseDate.replace("-", "") + courseTime.substring(8, 10) + courseTime.substring(11, 13);
		Map<String, Object> params = Maps.newHashMap();
		params.put("courseDateStart", courseDateStart);
		params.put("courseDateEnd", courseDateEnd);
		params.put("courseStartTime", courseStartTime);
		params.put("courseEndTime", courseEndTime);
		params.put("courseEndTime2", "2"+courseEndTime);
		params.put("studentId", studentId);
		StringBuffer sql = new StringBuffer();
		sql.append("           select * from course_conflict where 1=1 ");
        sql.append(" and start_time > :courseDateStart and start_time < :courseDateEnd ");
		sql.append("                 and ((                                                            ");
		sql.append("                       student_id = :studentId and  ");
		sql.append("           			  ((                        																");
		sql.append("                              :courseStartTime between start_time and end_time  and :courseStartTime <> end_time");
		sql.append("          		         or :courseEndTime between start_time and end_time  and :courseEndTime <> start_time  )");
		sql.append(" or  ( ");
		sql.append(" start_time BETWEEN :courseStartTime  and :courseEndTime ");
		sql.append(" and start_time <> :courseEndTime2   ");
//		sql.append(" and start_time <> 2"+courseEndTime+"   ");
		sql.append("  and end_time BETWEEN :courseStartTime  and :courseEndTime  ");
		sql.append(" and end_time <> :courseStartTime ");
		sql.append(" )  ");
		sql.append("          		      )                   																    ");
		sql.append("          		 )                																			");		
		if(StringUtils.isNotBlank(teacherId)){
			params.put("teacherId", teacherId);
			sql.append("                 or                             															");
			sql.append("                 (                                                            ");
			sql.append("                       teacher_id = :teacherId and  ");
			sql.append("           			  ( (                       																");
			sql.append("                              :courseStartTime between start_time and end_time  and :courseStartTime <> end_time");
			sql.append("          		         or :courseEndTime between start_time and end_time  and :courseEndTime <> start_time ");
			sql.append("          		      )                   																    ");
			sql.append(" or  ( ");
			sql.append(" start_time BETWEEN :courseStartTime  and :courseEndTime ");
			sql.append(" and start_time <> :courseEndTime2   ");
			sql.append("  and end_time BETWEEN :courseStartTime  and :courseEndTime  ");
			sql.append(" and end_time <> :courseStartTime ");
			sql.append(" )  ");
			sql.append(" ) ");
			sql.append("          		 ))                																			");
		}
		List<CourseConflict> courseConflictList = super.findBySql(sql.toString(), params);
		if(courseConflictList != null && courseConflictList.size() > 0 ){  
			courseConflictList = getReturnCourseConflictList(courseConflictList);
		}else {
			return courseConflictList = null;
		}
		return courseConflictList;
	}


    private List<CourseConflict> getReturnCourseConflictList(List<CourseConflict> courseConflictList) {
    	List<CourseConflict> returnList = new ArrayList<CourseConflict>();
    	//去掉重复的
		for(CourseConflict courseConflict : courseConflictList) {
			if(returnList == null || returnList.size() == 0) {
				returnList.add(courseConflict);
			}else{
				boolean flag = true;
				for(CourseConflict returnCourseConflict : returnList) {
					if(returnCourseConflict.getCourseId().equals(courseConflict.getCourseId())) {
						flag = false;
						break;
					}
				}
				if(flag == true) {
					returnList.add(courseConflict);
				}
			}
		}
		if(returnList != null && returnList.size() > 1) {   //大于1表示有冲突
			return returnList;
		}else{
			return null;
		}
    }
	
	@Override
	public List<CourseConflict> getMiniClassCourseConfictList(String courseId,
			String studentId, String teacherId, String courseDate,
			String courseTime) {
		String courseDateStart = courseDate.replace("-", "") + "0000";
		String courseDateEnd = courseDate.replace("-", "") + "2359";
		String courseStartTime = courseDate.replace("-", "") + courseTime.substring(0, 2) + courseTime.substring(3, 6);
		String courseEndTime = courseDate.replace("-", "") + courseTime.substring(8, 10) + courseTime.substring(11, 13);
		Map<String, Object> params = Maps.newHashMap();
		params.put("courseDateStart", courseDateStart);
		params.put("courseDateEnd", courseDateEnd);
		params.put("courseStartTime", courseStartTime);
		params.put("courseEndTime", courseEndTime);
		params.put("courseEndTime2", "2"+courseEndTime);
		String[] studentIds = StringUtil.replaceSpace(studentId).split(",");
		params.put("studentId", studentIds);
		StringBuffer sql = new StringBuffer();
		sql.append("           select * from course_conflict where 1=1 ");
		sql.append(" and start_time > :courseDateStart and start_time < :courseDateEnd");
		sql.append("                 and (");
		sql.append("                       student_id in  (:studentId) and (  ");//在当前小班课程里面的学生


		sql.append("           			  (                        																");
		sql.append("                              :courseStartTime between start_time and end_time  and :courseStartTime <> end_time");
		sql.append("          		         or :courseEndTime between start_time and end_time  and :courseEndTime <> start_time )");
		sql.append(" or  ( ");
		sql.append(" start_time BETWEEN :courseStartTime  and :courseEndTime ");
		sql.append(" and start_time <> :courseEndTime2   ");
		sql.append("  and end_time BETWEEN :courseStartTime  and :courseEndTime  ");
		sql.append(" and end_time <> :courseStartTime ");
		sql.append("          		      )                   																    ");
		sql.append("          		 )                																			");
		if(StringUtils.isNotBlank(teacherId)){
			params.put("teacherId", teacherId);
			sql.append("                 or                             															");
			sql.append("                 (                                                            ");
			sql.append("                       teacher_id = :teacherId and  ");//当前小班课程里面的老师
			sql.append("           			  (                        																");
			sql.append("                              :courseStartTime between start_time and end_time  and :courseStartTime <> end_time");
			sql.append("          		         or :courseEndTime between start_time and end_time  and :courseEndTime <> start_time ");
			sql.append(" or  ( ");
			sql.append(" start_time BETWEEN :courseStartTime  and :courseEndTime ");
			sql.append(" and start_time <> :courseEndTime2   ");
			sql.append("  and end_time BETWEEN :courseStartTime  and :courseEndTime  ");
			sql.append(" and end_time <> :courseStartTime ");
			sql.append(" )  ");
			sql.append("          		      )                   																    ");
			sql.append("          		 ))                																			");
		}
		List<CourseConflict> courseConflictList = super.findBySql(sql.toString(), params);
		if(courseConflictList != null && courseConflictList.size() > 0 ){  
			courseConflictList = getReturnCourseConflictList(courseConflictList);
		}else {
			return courseConflictList = null;
		}
		return courseConflictList;
	}
    
	
	@Override
	public int findCourseConflictCount(String courseId,String studentId,String teacherId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from CourseConflict where courseId='"+courseId+"'");
		if (StringUtils.isBlank(studentId) && StringUtils.isBlank(teacherId)){
			hql.append(" and 1=1 ");
		}else {
			hql.append(" and ( ");
			if(StringUtils.isNotBlank(studentId)){
				hql.append(" student.id= :studentId ");
				params.put("studentId", studentId);
			}
			if(StringUtils.isNotBlank(teacherId)){
				params.put("teacherId", teacherId);
				if (StringUtils.isNotBlank(studentId)) {
					hql.append(" or teacher.userId= :teacherId ");
				} else {
					hql.append(" teacher.userId= :teacherId ");
				}
			}
			hql.append(" ) ");
		}
		List<CourseConflict> ccList =this.findAllByHQL(hql.toString(), params);
		int retCount = 0;
		if(ccList != null && ccList.size() > 0){
			for (CourseConflict cc : ccList) {
				StringBuffer sql =new StringBuffer();
				Map<String, Object> params1 = Maps.newHashMap();
				String courseDateStart = cc.getStartTime().toString().substring(0, 8) + "0000";
				String courseDateEnd = cc.getStartTime().toString().substring(0, 8) + "2359";
				params1.put("courseDateStart", courseDateStart);
				params1.put("courseDateEnd", courseDateEnd);
				params1.put("ccStartTime", cc.getStartTime());
				params1.put("ccEndTime", cc.getEndTime());
				params1.put("courseId", courseId);
				sql.append(" select count(distinct course_id) from course_conflict cc where 1=1 ");
				sql.append(" and start_time > :courseDateStart and start_time < :courseDateEnd");
				sql.append(" and (( :ccStartTime between start_time and end_time  and  :ccStartTime <> end_time ");
				sql.append("        or :ccEndTime between start_time and end_time  and :ccEndTime <> start_time )");
				sql.append(" or  ( start_time BETWEEN :ccStartTime  and :ccEndTime ");
				sql.append(" and start_time <> :ccEndTime   ");
				sql.append("  and end_time BETWEEN :ccStartTime  and :ccEndTime  ");
				sql.append(" and end_time <> :ccStartTime )) ");
				sql.append("  and course_id<> :courseId  and ");
				if (cc.getTeacher() != null) {
					params1.put("ccTeacherId", cc.getTeacher().getUserId());
					sql.append(" ( teacher_id= :ccTeacherId  ");
					if (cc.getStudent() != null) {
						params1.put("ccStudentId", cc.getStudent().getId());
						sql.append(" or student_id= :ccStudentId ");
					}
					sql.append(" ) ");
				}else {
					if (cc.getStudent() != null){
						params1.put("ccStudentId", cc.getStudent().getId());
						sql.append("  student_id= :ccStudentId ");
					}else {
						sql.append("  1=2  ");
					}

				}


				retCount += this.findCountSql(sql.toString(), params1);
			}
		}
		
		return retCount;
	}
	
	@Override
	public List<CourseConflict> findCourseConflictList(String courseId,String studentId,String teacherId) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from CourseConflict where courseId= :courseId ");
		params.put("courseId", courseId);
		if(StringUtils.isNotBlank(studentId)){
			params.put("studentId", studentId);
			hql.append(" and student.id= :studentId ");
		}
		if(StringUtils.isNotBlank(teacherId)){
			hql.append(" and teacher.userId= :teacherId ");
			params.put("teacherId", teacherId);
		}
		
		List<CourseConflict> ccList =this.findAllByHQL(hql.toString(), params);
		List<CourseConflict> retList = new ArrayList<CourseConflict>();
		if(ccList != null && ccList.size() > 0){
			for (CourseConflict cc : ccList) {
				StringBuffer sql =new StringBuffer();
				Map<String, Object> params1 = Maps.newHashMap();
				String courseDateStart = cc.getStartTime().toString().substring(0, 8) + "0000";
				String courseDateEnd = cc.getStartTime().toString().substring(0, 8) + "2359";
				params1.put("courseDateStart", courseDateStart);
				params1.put("courseDateEnd", courseDateEnd);
				params1.put("ccStartTime", cc.getStartTime());
				params1.put("ccEndTime", cc.getEndTime());
				sql.append(" select * from course_conflict cc  where 1=1 ");
				sql.append(" and start_time > :courseDateStart and start_time < :courseDateEnd ");
				sql.append(" and (( :ccStartTime between start_time and end_time  and  :ccStartTime <> end_time ");
				sql.append("        or :ccStartTime between start_time and end_time  and :ccStartTime <> start_time )");
				sql.append(" or  ( start_time BETWEEN :ccStartTime  and :ccEndTime ");
				sql.append(" and start_time <> :ccEndTime   ");
				sql.append("  and end_time BETWEEN :ccStartTime  and :ccEndTime  ");
				sql.append(" and end_time <> :ccStartTime )) ");
				sql.append(" and ");
//				sql.append("  and course_id<>'"+courseId+"' and ");
				if (cc.getTeacher() != null) {
					sql.append(" ( teacher_id= :ccTeacherId ");
					params1.put("ccTeacherId", cc.getTeacher().getUserId());
				}
				if (cc.getStudent() != null) {
					sql.append(" or student_id= :ccStudentId ");
					params1.put("ccStudentId", cc.getStudent().getId());
				}
				sql.append(" ) ");
				sql.append(" group by course_id");
				retList.addAll(this.findBySql(sql.toString(), params1));
			}
		}
		return retList;
	}
}
