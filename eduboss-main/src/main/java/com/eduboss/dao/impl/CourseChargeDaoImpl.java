package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseChargeDao;
import com.eduboss.domain.Course;

@Repository
public class CourseChargeDaoImpl extends GenericDaoImpl<Object, String> implements CourseChargeDao {	
	
	/**
	 *“老师1对1课消 年级分布” 和 “1对1 课消科目分布”
	 * @param course
	 */
	public void teacherCourseCharge(Course course){
		
		
		String subjectName=course.getSubject().getRemark();		
		String gradeName=course.getGrade().getRemark();
		
	   BigDecimal courseHours=course.getAuditHours();
	   BigDecimal TOTAL_CONSUME_HOUR=courseHours;
		   
	   BigDecimal CHINESE_SUBJECT=new BigDecimal("0"); 
	   BigDecimal MATH_SUBJECT=new BigDecimal("0"); 
	   BigDecimal ENGLISH_SUBJECT=new BigDecimal("0"); 
	   BigDecimal PHYSICS_SUBJECT=new BigDecimal("0"); 
	   BigDecimal CHEMISTRY_SUBJECT=new BigDecimal("0"); 
	   BigDecimal POLITICS_SUBJECT=new BigDecimal("0"); 
	   BigDecimal HISTORY_SUBJECT=new BigDecimal("0");	   
	   BigDecimal GEOGRAPHY_SUBJECT=new BigDecimal("0");
	   BigDecimal BIOLOGY_SUBJECT=new BigDecimal("0");
	   BigDecimal OTHER_SUBJECT=new BigDecimal("0"); 
	  
	   if (StringUtils.isEmpty(subjectName)) {
		   subjectName = "";
	   }
	   
	   if(subjectName.equals("CHINESE_SUBJECT")){
		   CHINESE_SUBJECT=courseHours;
	   }else if(subjectName.equals("MATH_SUBJECT")){
		   MATH_SUBJECT=courseHours;
	   }else if(subjectName.equals("ENGLISH_SUBJECT")){
		   ENGLISH_SUBJECT=courseHours;
	   }else if(subjectName.equals("PHYSICS_SUBJECT")){
		   PHYSICS_SUBJECT=courseHours;
	   }else if(subjectName.equals("CHEMISTRY_SUBJECT")){
		   CHEMISTRY_SUBJECT=courseHours;
	   }else if(subjectName.equals("POLITICS_SUBJECT")){
		   POLITICS_SUBJECT=courseHours;
	   }else if(subjectName.equals("HISTORY_SUBJECT")){
		   HISTORY_SUBJECT=courseHours;
	   }else if(subjectName.equals("GEOGRAPHY_SUBJECT")){
		   GEOGRAPHY_SUBJECT=courseHours;
	   }else if(subjectName.equals("BIOLOGY_SUBJECT")){
		   BIOLOGY_SUBJECT=courseHours;
	   }else{
		   OTHER_SUBJECT=courseHours;
	   }
	   	   
	   BigDecimal FIRST_GRADE=new BigDecimal("0"); 
	   BigDecimal SECOND_GRADE=new BigDecimal("0"); 
	   BigDecimal THIRD_GRADE=new BigDecimal("0"); 
	   BigDecimal FOURTH_GRADE=new BigDecimal("0"); 
	   BigDecimal FIFTH_GRADE=new BigDecimal("0"); 
	   BigDecimal SIXTH_GRADE=new BigDecimal("0"); 
	   BigDecimal MIDDLE_SCHOOL_FIRST_GRADE=new BigDecimal("0");	   
	   BigDecimal MIDDLE_SCHOOL_SECOND_GRADE=new BigDecimal("0");
	   BigDecimal MIDDLE_SCHOOL_THIRD_GRADE=new BigDecimal("0");	   
	   BigDecimal HIGH_SCHOOL_FIRST_GRADE=new BigDecimal("0");	   
	   BigDecimal HIGH_SCHOOL_SECOND_GRADE=new BigDecimal("0");
	   BigDecimal HIGH_SCHOOL_THIRD_GRADE=new BigDecimal("0");	   
	   
	   if(gradeName.equals("FIRST_GRADE")){
		   FIRST_GRADE=courseHours;
	   }else if(gradeName.equals("SECOND_GRADE")){
		   SECOND_GRADE=courseHours;
	   }else if(gradeName.equals("THIRD_GRADE")){
		   THIRD_GRADE=courseHours;
	   }else if(gradeName.equals("FOURTH_GRADE")){
		   FOURTH_GRADE=courseHours;
	   }else if(gradeName.equals("FIFTH_GRADE")){
		   FIFTH_GRADE=courseHours;
	   }else if(gradeName.equals("SIXTH_GRADE")){
		   SIXTH_GRADE=courseHours;
	   }else if(gradeName.equals("MIDDLE_SCHOOL_FIRST_GRADE")){
		   MIDDLE_SCHOOL_FIRST_GRADE=courseHours;
	   }else if(gradeName.equals("MIDDLE_SCHOOL_SECOND_GRADE")){
		   MIDDLE_SCHOOL_SECOND_GRADE=courseHours;
	   }else if(gradeName.equals("MIDDLE_SCHOOL_THIRD_GRADE")){
		   MIDDLE_SCHOOL_THIRD_GRADE=courseHours;
	   }else if(gradeName.equals("HIGH_SCHOOL_FIRST_GRADE")){
		  HIGH_SCHOOL_FIRST_GRADE=courseHours;
	   }else if(gradeName.equals("HIGH_SCHOOL_SECOND_GRADE")){
		  HIGH_SCHOOL_SECOND_GRADE=courseHours;
	   }else if(gradeName.equals("HIGH_SCHOOL_THIRD_GRADE")){
		  HIGH_SCHOOL_THIRD_GRADE=courseHours;
	   }
		Map<String, Object> params = Maps.newHashMap();
		String sql="select count(*) from ODS_DAY_COURSE_CONSUME_TEACHER_SUBJECT where COUNT_DATE = :courseDate AND USER_ID= :userId ";
		params.put("courseDate", course.getCourseDate());
		params.put("userId", course.getTeacher().getUserId());
		int count=this.findCountSql(sql, params);
		if(count==0){//新增			
			addCourseConsumeSubject(course, TOTAL_CONSUME_HOUR, CHINESE_SUBJECT, MATH_SUBJECT, ENGLISH_SUBJECT, PHYSICS_SUBJECT, CHEMISTRY_SUBJECT, POLITICS_SUBJECT, HISTORY_SUBJECT, GEOGRAPHY_SUBJECT, BIOLOGY_SUBJECT, OTHER_SUBJECT, FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, FIFTH_GRADE, SIXTH_GRADE, MIDDLE_SCHOOL_FIRST_GRADE, MIDDLE_SCHOOL_SECOND_GRADE, MIDDLE_SCHOOL_THIRD_GRADE, HIGH_SCHOOL_FIRST_GRADE, HIGH_SCHOOL_SECOND_GRADE, HIGH_SCHOOL_THIRD_GRADE);
			addCourseConsumeSubjectXiaoshi(course, TOTAL_CONSUME_HOUR, CHINESE_SUBJECT, MATH_SUBJECT, ENGLISH_SUBJECT, PHYSICS_SUBJECT, CHEMISTRY_SUBJECT, POLITICS_SUBJECT, HISTORY_SUBJECT, GEOGRAPHY_SUBJECT, BIOLOGY_SUBJECT, OTHER_SUBJECT, FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, FIFTH_GRADE, SIXTH_GRADE, MIDDLE_SCHOOL_FIRST_GRADE, MIDDLE_SCHOOL_SECOND_GRADE, MIDDLE_SCHOOL_THIRD_GRADE, HIGH_SCHOOL_FIRST_GRADE, HIGH_SCHOOL_SECOND_GRADE, HIGH_SCHOOL_THIRD_GRADE);
			
		}else{//更新
			StringBuffer sql_ = new StringBuffer();

			updateCourseConsumeSubject(course, TOTAL_CONSUME_HOUR, CHINESE_SUBJECT, MATH_SUBJECT, ENGLISH_SUBJECT, PHYSICS_SUBJECT, CHEMISTRY_SUBJECT, POLITICS_SUBJECT, HISTORY_SUBJECT, GEOGRAPHY_SUBJECT, BIOLOGY_SUBJECT, OTHER_SUBJECT, FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, FIFTH_GRADE, SIXTH_GRADE, MIDDLE_SCHOOL_FIRST_GRADE, MIDDLE_SCHOOL_SECOND_GRADE, MIDDLE_SCHOOL_THIRD_GRADE, HIGH_SCHOOL_FIRST_GRADE, HIGH_SCHOOL_SECOND_GRADE, HIGH_SCHOOL_THIRD_GRADE, sql_);

			StringBuffer xiaoshiSql = new StringBuffer();
			updateCourseConsumeSubjectXiaoshi(course, TOTAL_CONSUME_HOUR, CHINESE_SUBJECT, MATH_SUBJECT, ENGLISH_SUBJECT, PHYSICS_SUBJECT, CHEMISTRY_SUBJECT, POLITICS_SUBJECT, HISTORY_SUBJECT, GEOGRAPHY_SUBJECT, BIOLOGY_SUBJECT, OTHER_SUBJECT, FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, FIFTH_GRADE, SIXTH_GRADE, MIDDLE_SCHOOL_FIRST_GRADE, MIDDLE_SCHOOL_SECOND_GRADE, MIDDLE_SCHOOL_THIRD_GRADE, HIGH_SCHOOL_FIRST_GRADE, HIGH_SCHOOL_SECOND_GRADE, HIGH_SCHOOL_THIRD_GRADE, xiaoshiSql);
		}
	}

	private void updateCourseConsumeSubjectXiaoshi(Course course, BigDecimal TOTAL_CONSUME_HOUR, BigDecimal CHINESE_SUBJECT, BigDecimal MATH_SUBJECT, BigDecimal ENGLISH_SUBJECT, BigDecimal PHYSICS_SUBJECT, BigDecimal CHEMISTRY_SUBJECT, BigDecimal POLITICS_SUBJECT, BigDecimal HISTORY_SUBJECT, BigDecimal GEOGRAPHY_SUBJECT, BigDecimal BIOLOGY_SUBJECT, BigDecimal OTHER_SUBJECT, BigDecimal FIRST_GRADE, BigDecimal SECOND_GRADE, BigDecimal THIRD_GRADE, BigDecimal FOURTH_GRADE, BigDecimal FIFTH_GRADE, BigDecimal SIXTH_GRADE, BigDecimal MIDDLE_SCHOOL_FIRST_GRADE, BigDecimal MIDDLE_SCHOOL_SECOND_GRADE, BigDecimal MIDDLE_SCHOOL_THIRD_GRADE, BigDecimal HIGH_SCHOOL_FIRST_GRADE, BigDecimal HIGH_SCHOOL_SECOND_GRADE, BigDecimal HIGH_SCHOOL_THIRD_GRADE, StringBuffer sql_) {
		if (course.getCourseMinutes()==null){
			course.setCourseMinutes(BigDecimal.valueOf(course.getProduct().getClassTimeLength()));
		}
		sql_.append( "update ods_day_course_xiaoshi_consume_teacher_subject ");

		sql_.append(" set TOTAL_CONSUME_HOUR = TOTAL_CONSUME_HOUR+"+TOTAL_CONSUME_HOUR.multiply(course.getCourseMinutes())+"");
		if(CHINESE_SUBJECT.floatValue()>0){
			sql_.append(" ,CHINESE_SUBJECT = CHINESE_SUBJECT+"+CHINESE_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(MATH_SUBJECT.floatValue()>0){
			sql_.append(" ,MATH_SUBJECT = MATH_SUBJECT+"+MATH_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(ENGLISH_SUBJECT.floatValue()>0){
			sql_.append(" ,ENGLISH_SUBJECT = ENGLISH_SUBJECT+"+ENGLISH_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(PHYSICS_SUBJECT.floatValue()>0){
			sql_.append(" ,PHYSICS_SUBJECT = PHYSICS_SUBJECT+"+PHYSICS_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(CHEMISTRY_SUBJECT.floatValue()>0){
			sql_.append(" ,CHEMISTRY_SUBJECT = CHEMISTRY_SUBJECT+"+CHEMISTRY_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(POLITICS_SUBJECT.floatValue()>0){
			sql_.append(" ,POLITICS_SUBJECT = POLITICS_SUBJECT+"+POLITICS_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(HISTORY_SUBJECT.floatValue()>0){
			sql_.append(" ,HISTORY_SUBJECT = HISTORY_SUBJECT+"+HISTORY_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(GEOGRAPHY_SUBJECT.floatValue()>0){
			sql_.append(" ,GEOGRAPHY_SUBJECT = GEOGRAPHY_SUBJECT+"+GEOGRAPHY_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(BIOLOGY_SUBJECT.floatValue()>0){
			sql_.append(" ,BIOLOGY_SUBJECT = BIOLOGY_SUBJECT+"+BIOLOGY_SUBJECT.multiply(course.getCourseMinutes())+"");
		}
		if(OTHER_SUBJECT.floatValue()>0){
			sql_.append(" ,OTHER_SUBJECT = OTHER_SUBJECT+"+OTHER_SUBJECT.multiply(course.getCourseMinutes())+"");
		}

		if(FIRST_GRADE.floatValue()>0){
			sql_.append(" ,FIRST_GRADE = FIRST_GRADE+"+FIRST_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(SECOND_GRADE.floatValue()>0){
			sql_.append(" ,SECOND_GRADE = SECOND_GRADE+"+SECOND_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(THIRD_GRADE.floatValue()>0){
			sql_.append(" ,THIRD_GRADE = THIRD_GRADE+"+THIRD_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(FOURTH_GRADE.floatValue()>0){
			sql_.append(" ,FOURTH_GRADE = FOURTH_GRADE+"+FOURTH_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(FIFTH_GRADE.floatValue()>0){
			sql_.append(" ,FIFTH_GRADE = FIFTH_GRADE+"+FIFTH_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(SIXTH_GRADE.floatValue()>0){
			sql_.append(" ,SIXTH_GRADE = SIXTH_GRADE+"+SIXTH_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(MIDDLE_SCHOOL_FIRST_GRADE.floatValue()>0){
			sql_.append(" ,MIDDLE_SCHOOL_FIRST_GRADE = MIDDLE_SCHOOL_FIRST_GRADE+"+MIDDLE_SCHOOL_FIRST_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(MIDDLE_SCHOOL_SECOND_GRADE.floatValue()>0){
			sql_.append(" ,MIDDLE_SCHOOL_SECOND_GRADE = MIDDLE_SCHOOL_SECOND_GRADE+"+MIDDLE_SCHOOL_SECOND_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(MIDDLE_SCHOOL_THIRD_GRADE.floatValue()>0){
			sql_.append(" ,MIDDLE_SCHOOL_THIRD_GRADE = MIDDLE_SCHOOL_THIRD_GRADE+"+MIDDLE_SCHOOL_THIRD_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(HIGH_SCHOOL_FIRST_GRADE.floatValue()>0){
			sql_.append(" ,HIGH_SCHOOL_FIRST_GRADE = HIGH_SCHOOL_FIRST_GRADE+"+HIGH_SCHOOL_FIRST_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(HIGH_SCHOOL_SECOND_GRADE.floatValue()>0){
			sql_.append(" ,HIGH_SCHOOL_SECOND_GRADE = HIGH_SCHOOL_SECOND_GRADE+"+HIGH_SCHOOL_SECOND_GRADE.multiply(course.getCourseMinutes())+"");
		}
		if(HIGH_SCHOOL_THIRD_GRADE.floatValue()>0){
			sql_.append(" ,HIGH_SCHOOL_THIRD_GRADE = HIGH_SCHOOL_THIRD_GRADE+"+HIGH_SCHOOL_THIRD_GRADE.multiply(course.getCourseMinutes())+"");
		}
		Map<String, Object> params = Maps.newHashMap();
		sql_.append(" where COUNT_DATE = :courseDate AND USER_ID= :userId ");
		params.put("courseDate", course.getCourseDate());
		params.put("userId", course.getTeacher().getUserId());
		this.excuteSql(sql_.toString(), params);
	}


	private void updateCourseConsumeSubject(Course course, BigDecimal TOTAL_CONSUME_HOUR, BigDecimal CHINESE_SUBJECT, BigDecimal MATH_SUBJECT, BigDecimal ENGLISH_SUBJECT, BigDecimal PHYSICS_SUBJECT, BigDecimal CHEMISTRY_SUBJECT, BigDecimal POLITICS_SUBJECT, BigDecimal HISTORY_SUBJECT, BigDecimal GEOGRAPHY_SUBJECT, BigDecimal BIOLOGY_SUBJECT, BigDecimal OTHER_SUBJECT, BigDecimal FIRST_GRADE, BigDecimal SECOND_GRADE, BigDecimal THIRD_GRADE, BigDecimal FOURTH_GRADE, BigDecimal FIFTH_GRADE, BigDecimal SIXTH_GRADE, BigDecimal MIDDLE_SCHOOL_FIRST_GRADE, BigDecimal MIDDLE_SCHOOL_SECOND_GRADE, BigDecimal MIDDLE_SCHOOL_THIRD_GRADE, BigDecimal HIGH_SCHOOL_FIRST_GRADE, BigDecimal HIGH_SCHOOL_SECOND_GRADE, BigDecimal HIGH_SCHOOL_THIRD_GRADE, StringBuffer sql_) {
		Map<String, Object> params = Maps.newHashMap();
		sql_.append( "update ODS_DAY_COURSE_CONSUME_TEACHER_SUBJECT ");

		sql_.append(" set TOTAL_CONSUME_HOUR = TOTAL_CONSUME_HOUR+"+TOTAL_CONSUME_HOUR+"");
		if(CHINESE_SUBJECT.floatValue()>0){
            sql_.append(" ,CHINESE_SUBJECT = CHINESE_SUBJECT+"+CHINESE_SUBJECT+"");
        }
		if(MATH_SUBJECT.floatValue()>0){
            sql_.append(" ,MATH_SUBJECT = MATH_SUBJECT+"+MATH_SUBJECT+"");
        }
		if(ENGLISH_SUBJECT.floatValue()>0){
            sql_.append(" ,ENGLISH_SUBJECT = ENGLISH_SUBJECT+"+ENGLISH_SUBJECT+"");
        }
		if(PHYSICS_SUBJECT.floatValue()>0){
            sql_.append(" ,PHYSICS_SUBJECT = PHYSICS_SUBJECT+"+PHYSICS_SUBJECT+"");
        }
		if(CHEMISTRY_SUBJECT.floatValue()>0){
            sql_.append(" ,CHEMISTRY_SUBJECT = CHEMISTRY_SUBJECT+"+CHEMISTRY_SUBJECT+"");
        }
		if(POLITICS_SUBJECT.floatValue()>0){
            sql_.append(" ,POLITICS_SUBJECT = POLITICS_SUBJECT+"+POLITICS_SUBJECT+"");
        }
		if(HISTORY_SUBJECT.floatValue()>0){
            sql_.append(" ,HISTORY_SUBJECT = HISTORY_SUBJECT+"+HISTORY_SUBJECT+"");
        }
		if(GEOGRAPHY_SUBJECT.floatValue()>0){
            sql_.append(" ,GEOGRAPHY_SUBJECT = GEOGRAPHY_SUBJECT+"+GEOGRAPHY_SUBJECT+"");
        }
		if(BIOLOGY_SUBJECT.floatValue()>0){
            sql_.append(" ,BIOLOGY_SUBJECT = BIOLOGY_SUBJECT+"+BIOLOGY_SUBJECT+"");
        }
		if(OTHER_SUBJECT.floatValue()>0){
            sql_.append(" ,OTHER_SUBJECT = OTHER_SUBJECT+"+OTHER_SUBJECT+"");
        }

		if(FIRST_GRADE.floatValue()>0){
            sql_.append(" ,FIRST_GRADE = FIRST_GRADE+"+FIRST_GRADE+"");
        }
		if(SECOND_GRADE.floatValue()>0){
            sql_.append(" ,SECOND_GRADE = SECOND_GRADE+"+SECOND_GRADE+"");
        }
		if(THIRD_GRADE.floatValue()>0){
            sql_.append(" ,THIRD_GRADE = THIRD_GRADE+"+THIRD_GRADE+"");
        }
		if(FOURTH_GRADE.floatValue()>0){
            sql_.append(" ,FOURTH_GRADE = FOURTH_GRADE+"+FOURTH_GRADE+"");
        }
		if(FIFTH_GRADE.floatValue()>0){
            sql_.append(" ,FIFTH_GRADE = FIFTH_GRADE+"+FIFTH_GRADE+"");
        }
		if(SIXTH_GRADE.floatValue()>0){
            sql_.append(" ,SIXTH_GRADE = SIXTH_GRADE+"+SIXTH_GRADE+"");
        }
		if(MIDDLE_SCHOOL_FIRST_GRADE.floatValue()>0){
            sql_.append(" ,MIDDLE_SCHOOL_FIRST_GRADE = MIDDLE_SCHOOL_FIRST_GRADE+"+MIDDLE_SCHOOL_FIRST_GRADE+"");
        }
		if(MIDDLE_SCHOOL_SECOND_GRADE.floatValue()>0){
            sql_.append(" ,MIDDLE_SCHOOL_SECOND_GRADE = MIDDLE_SCHOOL_SECOND_GRADE+"+MIDDLE_SCHOOL_SECOND_GRADE+"");
        }
		if(MIDDLE_SCHOOL_THIRD_GRADE.floatValue()>0){
            sql_.append(" ,MIDDLE_SCHOOL_THIRD_GRADE = MIDDLE_SCHOOL_THIRD_GRADE+"+MIDDLE_SCHOOL_THIRD_GRADE+"");
        }
		if(HIGH_SCHOOL_FIRST_GRADE.floatValue()>0){
            sql_.append(" ,HIGH_SCHOOL_FIRST_GRADE = HIGH_SCHOOL_FIRST_GRADE+"+HIGH_SCHOOL_FIRST_GRADE+"");
        }
		if(HIGH_SCHOOL_SECOND_GRADE.floatValue()>0){
            sql_.append(" ,HIGH_SCHOOL_SECOND_GRADE = HIGH_SCHOOL_SECOND_GRADE+"+HIGH_SCHOOL_SECOND_GRADE+"");
        }
		if(HIGH_SCHOOL_THIRD_GRADE.floatValue()>0){
            sql_.append(" ,HIGH_SCHOOL_THIRD_GRADE = HIGH_SCHOOL_THIRD_GRADE+"+HIGH_SCHOOL_THIRD_GRADE+"");
        }
		sql_.append(" where COUNT_DATE = :courseDate AND USER_ID= :userId ");
		params.put("courseDate", course.getCourseDate());
		params.put("userId", course.getTeacher().getUserId());
		this.excuteSql(sql_.toString(), params);
	}

	private void addCourseConsumeSubject(Course course, BigDecimal TOTAL_CONSUME_HOUR, BigDecimal CHINESE_SUBJECT, BigDecimal MATH_SUBJECT, BigDecimal ENGLISH_SUBJECT, BigDecimal PHYSICS_SUBJECT, BigDecimal CHEMISTRY_SUBJECT, BigDecimal POLITICS_SUBJECT, BigDecimal HISTORY_SUBJECT, BigDecimal GEOGRAPHY_SUBJECT, BigDecimal BIOLOGY_SUBJECT, BigDecimal OTHER_SUBJECT, BigDecimal FIRST_GRADE, BigDecimal SECOND_GRADE, BigDecimal THIRD_GRADE, BigDecimal FOURTH_GRADE, BigDecimal FIFTH_GRADE, BigDecimal SIXTH_GRADE, BigDecimal MIDDLE_SCHOOL_FIRST_GRADE, BigDecimal MIDDLE_SCHOOL_SECOND_GRADE, BigDecimal MIDDLE_SCHOOL_THIRD_GRADE, BigDecimal HIGH_SCHOOL_FIRST_GRADE, BigDecimal HIGH_SCHOOL_SECOND_GRADE, BigDecimal HIGH_SCHOOL_THIRD_GRADE) {
		String sql;
		Map<String, Object> params = Maps.newHashMap();
		sql="INSERT INTO ODS_DAY_COURSE_CONSUME_TEACHER_SUBJECT (GROUP_ID,BRANCH_ID,CAMPUS_ID,USER_ID,WORK_TYPE,COUNT_DATE,TOTAL_CONSUME_HOUR,CHINESE_SUBJECT,MATH_SUBJECT,ENGLISH_SUBJECT,	PHYSICS_SUBJECT,CHEMISTRY_SUBJECT,POLITICS_SUBJECT,	HISTORY_SUBJECT,GEOGRAPHY_SUBJECT,BIOLOGY_SUBJECT,OTHER_SUBJECT,FIRST_GRADE,SECOND_GRADE,THIRD_GRADE,FOURTH_GRADE,FIFTH_GRADE,SIXTH_GRADE,MIDDLE_SCHOOL_FIRST_GRADE,MIDDLE_SCHOOL_SECOND_GRADE,MIDDLE_SCHOOL_THIRD_GRADE,HIGH_SCHOOL_FIRST_GRADE,HIGH_SCHOOL_SECOND_GRADE,HIGH_SCHOOL_THIRD_GRADE,COURSE_CAMPUS_ID) " +
                "SELECT  p.GROUP_ID, p.BRANCH_ID, p.CAMPUS_ID, u.USER_ID, case when u.WORK_TYPE = 'FULL_TIME'  then '正职' else '兼职' end, :courseDate,"+TOTAL_CONSUME_HOUR+","+CHINESE_SUBJECT+","+MATH_SUBJECT+","+ENGLISH_SUBJECT+","+PHYSICS_SUBJECT+","+CHEMISTRY_SUBJECT+","+POLITICS_SUBJECT+","+HISTORY_SUBJECT+","+GEOGRAPHY_SUBJECT+","+BIOLOGY_SUBJECT+","+OTHER_SUBJECT+","+FIRST_GRADE+","+SECOND_GRADE+","+THIRD_GRADE+","+FOURTH_GRADE+","+FIFTH_GRADE+","+SIXTH_GRADE+","+MIDDLE_SCHOOL_FIRST_GRADE+","+MIDDLE_SCHOOL_SECOND_GRADE+","+MIDDLE_SCHOOL_THIRD_GRADE+","+HIGH_SCHOOL_FIRST_GRADE+","+HIGH_SCHOOL_SECOND_GRADE+","+HIGH_SCHOOL_THIRD_GRADE+",:blCampusId	FROM `REF_USER_ORG` p , `user` u  WHERE u.user_id= :teacherId  and p.USER_ID= :studyManagerId ";
		params.put("courseDate", course.getCourseDate());
		params.put("blCampusId", course.getBlCampusId().getId());
		params.put("studyManagerId", course.getStudyManager().getUserId());
		params.put("teacherId", course.getTeacher().getUserId());
		this.excuteSql(sql, params);
	}

	private void addCourseConsumeSubjectXiaoshi(Course course, BigDecimal TOTAL_CONSUME_HOUR, BigDecimal CHINESE_SUBJECT, BigDecimal MATH_SUBJECT, BigDecimal ENGLISH_SUBJECT, BigDecimal PHYSICS_SUBJECT, BigDecimal CHEMISTRY_SUBJECT, BigDecimal POLITICS_SUBJECT, BigDecimal HISTORY_SUBJECT, BigDecimal GEOGRAPHY_SUBJECT, BigDecimal BIOLOGY_SUBJECT, BigDecimal OTHER_SUBJECT, BigDecimal FIRST_GRADE, BigDecimal SECOND_GRADE, BigDecimal THIRD_GRADE, BigDecimal FOURTH_GRADE, BigDecimal FIFTH_GRADE, BigDecimal SIXTH_GRADE, BigDecimal MIDDLE_SCHOOL_FIRST_GRADE, BigDecimal MIDDLE_SCHOOL_SECOND_GRADE, BigDecimal MIDDLE_SCHOOL_THIRD_GRADE, BigDecimal HIGH_SCHOOL_FIRST_GRADE, BigDecimal HIGH_SCHOOL_SECOND_GRADE, BigDecimal HIGH_SCHOOL_THIRD_GRADE) {
		String sql;
		if (course.getCourseMinutes()==null){
			course.setCourseMinutes(BigDecimal.valueOf(course.getProduct().getClassTimeLength()));
		}
		Map<String, Object> params = Maps.newHashMap();
		sql="INSERT INTO ods_day_course_xiaoshi_consume_teacher_subject (GROUP_ID,BRANCH_ID,CAMPUS_ID,USER_ID,WORK_TYPE,COUNT_DATE,TOTAL_CONSUME_HOUR,CHINESE_SUBJECT,MATH_SUBJECT,ENGLISH_SUBJECT,	PHYSICS_SUBJECT,CHEMISTRY_SUBJECT,POLITICS_SUBJECT,	HISTORY_SUBJECT,GEOGRAPHY_SUBJECT,BIOLOGY_SUBJECT,OTHER_SUBJECT,FIRST_GRADE,SECOND_GRADE,THIRD_GRADE,FOURTH_GRADE,FIFTH_GRADE,SIXTH_GRADE,MIDDLE_SCHOOL_FIRST_GRADE,MIDDLE_SCHOOL_SECOND_GRADE,MIDDLE_SCHOOL_THIRD_GRADE,HIGH_SCHOOL_FIRST_GRADE,HIGH_SCHOOL_SECOND_GRADE,HIGH_SCHOOL_THIRD_GRADE,COURSE_CAMPUS_ID) " +
				"SELECT  p.GROUP_ID, p.BRANCH_ID, p.CAMPUS_ID, u.USER_ID, case when u.WORK_TYPE = 'FULL_TIME'  then '正职' else '兼职' end, :courseDate,"+TOTAL_CONSUME_HOUR.multiply(course.getCourseMinutes())+","+CHINESE_SUBJECT.multiply(course.getCourseMinutes())+","+MATH_SUBJECT.multiply(course.getCourseMinutes())+","+ENGLISH_SUBJECT.multiply(course.getCourseMinutes())+","+PHYSICS_SUBJECT.multiply(course.getCourseMinutes())+","+CHEMISTRY_SUBJECT.multiply(course.getCourseMinutes())+","+POLITICS_SUBJECT.multiply(course.getCourseMinutes())+","+HISTORY_SUBJECT.multiply(course.getCourseMinutes())+","+GEOGRAPHY_SUBJECT.multiply(course.getCourseMinutes())+","+BIOLOGY_SUBJECT.multiply(course.getCourseMinutes())+","+OTHER_SUBJECT.multiply(course.getCourseMinutes())+","+FIRST_GRADE.multiply(course.getCourseMinutes())+","+SECOND_GRADE.multiply(course.getCourseMinutes())+","+THIRD_GRADE.multiply(course.getCourseMinutes())+","+FOURTH_GRADE.multiply(course.getCourseMinutes())+","+FIFTH_GRADE.multiply(course.getCourseMinutes())+","+SIXTH_GRADE.multiply(course.getCourseMinutes())+","+MIDDLE_SCHOOL_FIRST_GRADE.multiply(course.getCourseMinutes())+","+MIDDLE_SCHOOL_SECOND_GRADE.multiply(course.getCourseMinutes())+","+MIDDLE_SCHOOL_THIRD_GRADE.multiply(course.getCourseMinutes())+","+HIGH_SCHOOL_FIRST_GRADE.multiply(course.getCourseMinutes())+","+HIGH_SCHOOL_SECOND_GRADE.multiply(course.getCourseMinutes())+","+HIGH_SCHOOL_THIRD_GRADE.multiply(course.getCourseMinutes())+", :blCampusId  FROM `REF_USER_ORG` p , `user` u  WHERE u.user_id=: teacherId and p.USER_ID= :studyManagerId ";
		params.put("courseDate", course.getCourseDate());
		params.put("blCampusId", course.getBlCampusId().getId());
		params.put("studyManagerId", course.getStudyManager().getUserId());
		params.put("teacherId", course.getTeacher().getUserId());
		this.excuteSql(sql, params);
	}

	/**
	 * 一对一审批无效操作，更新“老师1对1课消 年级分布” 和 “1对1 课消科目分布”
	 * @param course
	 */
	public void unValidateTeacherCourseCharge(Course course) {
		String subjectName=course.getSubject().getRemark();		
		String gradeName=course.getGrade().getRemark();
		
	   BigDecimal courseHours=course.getAuditHours();
	   BigDecimal TOTAL_CONSUME_HOUR=courseHours;
		   
	   BigDecimal CHINESE_SUBJECT=new BigDecimal("0"); 
	   BigDecimal MATH_SUBJECT=new BigDecimal("0"); 
	   BigDecimal ENGLISH_SUBJECT=new BigDecimal("0"); 
	   BigDecimal PHYSICS_SUBJECT=new BigDecimal("0"); 
	   BigDecimal CHEMISTRY_SUBJECT=new BigDecimal("0"); 
	   BigDecimal POLITICS_SUBJECT=new BigDecimal("0"); 
	   BigDecimal HISTORY_SUBJECT=new BigDecimal("0");	   
	   BigDecimal GEOGRAPHY_SUBJECT=new BigDecimal("0");
	   BigDecimal BIOLOGY_SUBJECT=new BigDecimal("0");
	   BigDecimal OTHER_SUBJECT=new BigDecimal("0"); 
	  
	   if (StringUtils.isEmpty(subjectName)) {
		   subjectName = "";
	   }
	   
	   if(subjectName.equals("CHINESE_SUBJECT")){
		   CHINESE_SUBJECT=courseHours;
	   }else if(subjectName.equals("MATH_SUBJECT")){
		   MATH_SUBJECT=courseHours;
	   }else if(subjectName.equals("ENGLISH_SUBJECT")){
		   ENGLISH_SUBJECT=courseHours;
	   }else if(subjectName.equals("PHYSICS_SUBJECT")){
		   PHYSICS_SUBJECT=courseHours;
	   }else if(subjectName.equals("CHEMISTRY_SUBJECT")){
		   CHEMISTRY_SUBJECT=courseHours;
	   }else if(subjectName.equals("POLITICS_SUBJECT")){
		   POLITICS_SUBJECT=courseHours;
	   }else if(subjectName.equals("HISTORY_SUBJECT")){
		   HISTORY_SUBJECT=courseHours;
	   }else if(subjectName.equals("GEOGRAPHY_SUBJECT")){
		   GEOGRAPHY_SUBJECT=courseHours;
	   }else if(subjectName.equals("BIOLOGY_SUBJECT")){
		   BIOLOGY_SUBJECT=courseHours;
	   }else{
		   OTHER_SUBJECT=courseHours;
	   }
	   	   
	   BigDecimal FIRST_GRADE=new BigDecimal("0"); 
	   BigDecimal SECOND_GRADE=new BigDecimal("0"); 
	   BigDecimal THIRD_GRADE=new BigDecimal("0"); 
	   BigDecimal FOURTH_GRADE=new BigDecimal("0"); 
	   BigDecimal FIFTH_GRADE=new BigDecimal("0"); 
	   BigDecimal SIXTH_GRADE=new BigDecimal("0"); 
	   BigDecimal MIDDLE_SCHOOL_FIRST_GRADE=new BigDecimal("0");	   
	   BigDecimal MIDDLE_SCHOOL_SECOND_GRADE=new BigDecimal("0");
	   BigDecimal MIDDLE_SCHOOL_THIRD_GRADE=new BigDecimal("0");	   
	   BigDecimal HIGH_SCHOOL_FIRST_GRADE=new BigDecimal("0");	   
	   BigDecimal HIGH_SCHOOL_SECOND_GRADE=new BigDecimal("0");
	   BigDecimal HIGH_SCHOOL_THIRD_GRADE=new BigDecimal("0");	   
	   
	   if(gradeName.equals("FIRST_GRADE")){
		   FIRST_GRADE=courseHours;
	   }else if(gradeName.equals("SECOND_GRADE")){
		   SECOND_GRADE=courseHours;
	   }else if(gradeName.equals("THIRD_GRADE")){
		   THIRD_GRADE=courseHours;
	   }else if(gradeName.equals("FOURTH_GRADE")){
		   FOURTH_GRADE=courseHours;
	   }else if(gradeName.equals("FIFTH_GRADE")){
		   FIFTH_GRADE=courseHours;
	   }else if(gradeName.equals("SIXTH_GRADE")){
		   SIXTH_GRADE=courseHours;
	   }else if(gradeName.equals("MIDDLE_SCHOOL_FIRST_GRADE")){
		   MIDDLE_SCHOOL_FIRST_GRADE=courseHours;
	   }else if(gradeName.equals("MIDDLE_SCHOOL_SECOND_GRADE")){
		   MIDDLE_SCHOOL_SECOND_GRADE=courseHours;
	   }else if(gradeName.equals("MIDDLE_SCHOOL_THIRD_GRADE")){
		   MIDDLE_SCHOOL_THIRD_GRADE=courseHours;
	   }else if(gradeName.equals("HIGH_SCHOOL_FIRST_GRADE")){
		  HIGH_SCHOOL_FIRST_GRADE=courseHours;
	   }else if(gradeName.equals("HIGH_SCHOOL_SECOND_GRADE")){
		  HIGH_SCHOOL_SECOND_GRADE=courseHours;
	   }else if(gradeName.equals("HIGH_SCHOOL_THIRD_GRADE")){
		  HIGH_SCHOOL_THIRD_GRADE=courseHours;
	   }
		Map<String, Object> params = Maps.newHashMap();
		String sql="select count(*) from ODS_DAY_COURSE_CONSUME_TEACHER_SUBJECT where COUNT_DATE = :courseDate AND USER_ID= :teacherId ";
		params.put("courseDate", course.getCourseDate());
		params.put("teacherId", course.getTeacher().getUserId());
		int count=this.findCountSql(sql, params);
		if(count!=0){//更新			
			StringBuffer sql_ = new StringBuffer();
			Map<String, Object> params1 = Maps.newHashMap();
			sql_.append( "update ODS_DAY_COURSE_CONSUME_TEACHER_SUBJECT ");
			
			sql_.append(" set TOTAL_CONSUME_HOUR = TOTAL_CONSUME_HOUR-"+TOTAL_CONSUME_HOUR+"");
			if(CHINESE_SUBJECT.floatValue()>0){
				sql_.append(" ,CHINESE_SUBJECT = CHINESE_SUBJECT-"+CHINESE_SUBJECT+"");
			}
			if(MATH_SUBJECT.floatValue()>0){
				sql_.append(" ,MATH_SUBJECT = MATH_SUBJECT-"+MATH_SUBJECT+"");
			}		
			if(ENGLISH_SUBJECT.floatValue()>0){
				sql_.append(" ,ENGLISH_SUBJECT = ENGLISH_SUBJECT-"+ENGLISH_SUBJECT+"");
			}
			if(PHYSICS_SUBJECT.floatValue()>0){
				sql_.append(" ,PHYSICS_SUBJECT = PHYSICS_SUBJECT-"+PHYSICS_SUBJECT+"");
			}			
			if(CHEMISTRY_SUBJECT.floatValue()>0){
				sql_.append(" ,CHEMISTRY_SUBJECT = CHEMISTRY_SUBJECT-"+CHEMISTRY_SUBJECT+"");
			}
			if(POLITICS_SUBJECT.floatValue()>0){
				sql_.append(" ,POLITICS_SUBJECT = POLITICS_SUBJECT-"+POLITICS_SUBJECT+"");
			}
			if(HISTORY_SUBJECT.floatValue()>0){
				sql_.append(" ,HISTORY_SUBJECT = HISTORY_SUBJECT-"+HISTORY_SUBJECT+"");
			}
			if(GEOGRAPHY_SUBJECT.floatValue()>0){
				sql_.append(" ,GEOGRAPHY_SUBJECT = GEOGRAPHY_SUBJECT-"+GEOGRAPHY_SUBJECT+"");
			}			
			if(BIOLOGY_SUBJECT.floatValue()>0){
				sql_.append(" ,BIOLOGY_SUBJECT = BIOLOGY_SUBJECT-"+BIOLOGY_SUBJECT+"");
			}
			if(OTHER_SUBJECT.floatValue()>0){
				sql_.append(" ,OTHER_SUBJECT = OTHER_SUBJECT-"+OTHER_SUBJECT+"");
			}		
			
			if(FIRST_GRADE.floatValue()>0){
				sql_.append(" ,FIRST_GRADE = FIRST_GRADE-"+FIRST_GRADE+"");
			}			
			if(SECOND_GRADE.floatValue()>0){
				sql_.append(" ,SECOND_GRADE = SECOND_GRADE-"+SECOND_GRADE+"");
			}
			if(THIRD_GRADE.floatValue()>0){
				sql_.append(" ,THIRD_GRADE = THIRD_GRADE-"+THIRD_GRADE+"");
			}			
			if(FOURTH_GRADE.floatValue()>0){
				sql_.append(" ,FOURTH_GRADE = FOURTH_GRADE-"+FOURTH_GRADE+"");
			}			
			if(FIFTH_GRADE.floatValue()>0){
				sql_.append(" ,FIFTH_GRADE = FIFTH_GRADE-"+FIFTH_GRADE+"");
			}
			if(SIXTH_GRADE.floatValue()>0){
				sql_.append(" ,SIXTH_GRADE = SIXTH_GRADE-"+SIXTH_GRADE+"");
			}			
			if(MIDDLE_SCHOOL_FIRST_GRADE.floatValue()>0){
				sql_.append(" ,MIDDLE_SCHOOL_FIRST_GRADE = MIDDLE_SCHOOL_FIRST_GRADE-"+MIDDLE_SCHOOL_FIRST_GRADE+"");
			}
			if(MIDDLE_SCHOOL_SECOND_GRADE.floatValue()>0){
				sql_.append(" ,MIDDLE_SCHOOL_SECOND_GRADE = MIDDLE_SCHOOL_SECOND_GRADE-"+MIDDLE_SCHOOL_SECOND_GRADE+"");
			}
			if(MIDDLE_SCHOOL_THIRD_GRADE.floatValue()>0){
				sql_.append(" ,MIDDLE_SCHOOL_THIRD_GRADE = MIDDLE_SCHOOL_THIRD_GRADE-"+MIDDLE_SCHOOL_THIRD_GRADE+"");
			}
			if(HIGH_SCHOOL_FIRST_GRADE.floatValue()>0){
				sql_.append(" ,HIGH_SCHOOL_FIRST_GRADE = HIGH_SCHOOL_FIRST_GRADE-"+HIGH_SCHOOL_FIRST_GRADE+"");
			}
			if(HIGH_SCHOOL_SECOND_GRADE.floatValue()>0){
				sql_.append(" ,HIGH_SCHOOL_SECOND_GRADE = HIGH_SCHOOL_SECOND_GRADE-"+HIGH_SCHOOL_SECOND_GRADE+"");
			}
			if(HIGH_SCHOOL_THIRD_GRADE.floatValue()>0){
				sql_.append(" ,HIGH_SCHOOL_THIRD_GRADE = HIGH_SCHOOL_THIRD_GRADE-"+HIGH_SCHOOL_THIRD_GRADE+"");
			}			
			sql_.append(" where COUNT_DATE = :courseDate AND USER_ID=:teacherId");
			params1.put("courseDate", course.getCourseDate());
			params1.put("teacherId", course.getTeacher().getUserId());
			this.excuteSql(sql_.toString(), params1);
		}
	}
	
	
	/**
	 *校区1对1课消 
	 * @param course
	 */
	public void schoolCourseCharge(Course course,BigDecimal courseAmout){
//		BigDecimal price= course.getProduct().getPrice();	
//		BigDecimal courseHours= course.getAuditHours();		
//		float courseAmout= price.floatValue()*courseHours.floatValue();		
		String sql="INSERT INTO ODS_DAY_COURSE_CONSUME (GROUP_ID, BRANCH_ID, CAMPUS_ID, TEACHER_ID, STY_MNG_ID, EDU_ID, STU_ID, COUNT_DATE, CONSUME_HOUR, CONSUME_AMOUNT) " +
				"SELECT  p.GROUP_ID, p.BRANCH_ID, p.CAMPUS_ID, :teacherId, :studyManagerId , :teachingManagerAuditId , :studentId , :courseDate , :auditHours , :courseAmout  FROM `REF_USER_ORG` p INNER JOIN `user` u on p.USER_ID = u.user_id	WHERE u.user_id= :studyManagerId and p.USER_ID= :studyManagerId ";
		Map<String, Object> params = Maps.newHashMap();
		params.put("teacherId", course.getTeacher().getUserId());
		params.put("studyManagerId", course.getStudyManager().getUserId());
		params.put("teachingManagerAuditId", course.getTeachingManagerAuditId());
		params.put("studentId", course.getStudent().getId());
		params.put("courseDate", course.getCourseDate());
		params.put("auditHours", course.getAuditHours());
		params.put("courseAmout", courseAmout);
		this.excuteSql(sql, params);
	}
	
	
	

	
	
	
}
