package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.ClassroomDao;
import com.eduboss.dao.MiniClassCourseDao;
import com.eduboss.domain.Classroom;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.dto.ClassroomUseStatus;
import com.eduboss.utils.HibernateUtils;

@Repository
public class ClassroomDaoImpl extends GenericDaoImpl<Classroom, String> implements
		ClassroomDao {

	@Autowired
	MiniClassCourseDao miniClassCourseDao;
	
	@Override
	public List<Classroom> getClassroomNames(String namePattern) {
		StringBuffer sql = new StringBuffer(" select distinct classroom from mini_class where classroom is not null ");
		if(StringUtils.isNotBlank(namePattern)){
			sql.append(" and classroom like '%").append(namePattern).append("%'");
		}
		@SuppressWarnings("unchecked")
		List<String> names = getCurrentSession().createSQLQuery(sql.toString()).list();
		List<Classroom> classrooms = new ArrayList<Classroom>(names.size());
		for (int i = 0; i < names.size(); i++) {
			Classroom c = new Classroom();
			c.setId(names.get(i));
			c.setName(names.get(i));
			classrooms.add(c);
		}
		return classrooms;
	}

	@Override
	public List<ClassroomUseStatus> getClassroomStatus(ClassroomUseStatus vo) {
//		StringBuffer sql = new StringBuffer("select mc.CLASSROOM,mc.NAME,mcc.COURSE_DATE,mcc.COURSE_TIME,mcc.COURSE_END_TIME from mini_class_course mcc left join mini_class mc on mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID where classroom is not null ");
//		if(StringUtils.isNotBlank(vo.getDate())){
//			sql.append(" and mcc.COURSE_DATE = '").append(vo.getDate()).append("'");
//		}
//		if(StringUtils.isNotBlank(vo.getName())){
//			sql.append(" and mc.CLASSROOM like '%").append(vo.getName()).append("%'");
//		}
//		@SuppressWarnings("unchecked")
//		List<Object[]> list = getCurrentSession().createSQLQuery(sql.toString()).list();
		StringBuilder sbHql = new StringBuilder();
		sbHql.append(" from MiniClassCourse where miniClass.classroom is not null");
		if(StringUtils.isNotBlank(vo.getDate())){
			sbHql.append(" and courseDate = '").append(vo.getDate()).append("'");
		}
		if(StringUtils.isNotBlank(vo.getName())){
			sbHql.append(" and miniClass.classroom.classroom like '%").append(vo.getName()).append("%' ");
		}
		List<MiniClassCourse> list = miniClassCourseDao.getMiniClassCourse4Class(sbHql.toString());
		List<ClassroomUseStatus> classroomUseStatusList = new ArrayList<ClassroomUseStatus>(list.size());
		for (int i = 0; i < list.size(); i++) {
			MiniClassCourseVo item = HibernateUtils.voObjectMapping(list.get(i), MiniClassCourseVo.class);
			ClassroomUseStatus s = new ClassroomUseStatus();
			s.setId(item.getClassroomId());
			s.setName(item.getClassroom());
			s.setUseSource(item.getMiniClassName());
			s.setDate(item.getCourseDate());
			s.setTimeStart(item.getCourseTime());
			s.setTimeEnd(item.getCourseEndTime());
			classroomUseStatusList.add(s);
		}
		return classroomUseStatusList;
	}

}
