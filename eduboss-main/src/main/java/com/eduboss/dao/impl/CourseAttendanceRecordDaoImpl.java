package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.CourseAttendanceBusinessDao;
import com.eduboss.dao.CourseAttendanceRecordDao;
import com.eduboss.domain.CourseAttendanceBusiness;
import com.eduboss.domain.CourseAttendanceRecord;
import com.eduboss.utils.StringUtil;

@Repository("CourseAttendanceRecordDao")
public class CourseAttendanceRecordDaoImpl extends GenericDaoImpl<CourseAttendanceRecord, String> implements CourseAttendanceRecordDao {

	@Autowired
	private CourseAttendanceBusinessDao courseAttendanceBusinessdDao;
	
	public void save(CourseAttendanceRecord courseAttendanceRecord) {
		if (StringUtil.isBlank(courseAttendanceRecord.getId())) {
			CourseAttendanceBusiness business = new CourseAttendanceBusiness();
			courseAttendanceBusinessdDao.save(business);
			String businessId = "";
			if (business.getId() > 0) {
				businessId += "COU" + StringUtil.numberPadding0(business.getId(), 10);
			}
			courseAttendanceRecord.setId(businessId);
		}
		super.save(courseAttendanceRecord);
	}

	/**
	 * 查找课程ID相关的所有记录
	 */
	public List<CourseAttendanceRecord> getCourseAttendanceRecordListByCourseId(String courseId) {
		List<Criterion> critList=new ArrayList<Criterion>();
		critList.add(Expression.eq("course.courseId", courseId));
		List<Order> recordList=new ArrayList<Order>();
		recordList.add(Order.desc("oprateTime"));
		List<CourseAttendanceRecord> list=this.findAllByCriteria(critList, recordList);
		return list;
	}
	
	
	/**
	 * 查找课程ID、角色相关的某一记录
	 */
	public CourseAttendanceRecord getCourseAttendanceRecordByCourseIdAndRole(String courseId, RoleCode roleCode) {
		List<Criterion> critList=new ArrayList<Criterion>();
		critList.add(Expression.eq("course.id", courseId));
		critList.add(Expression.eq("checkUserRole.roleCode", roleCode));
		List<Order> recordList=new ArrayList<Order>();
		recordList.add(Order.desc("oprateTime"));
		List<CourseAttendanceRecord> list=this.findAllByCriteria(critList, recordList);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
}
