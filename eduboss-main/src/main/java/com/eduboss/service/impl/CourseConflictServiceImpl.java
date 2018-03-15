package com.eduboss.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.CourseConflictDao;
import com.eduboss.dao.CourseDao;
import com.eduboss.dao.TwoTeacherClassCourseDao;
import com.eduboss.domain.Course;
import com.eduboss.domain.CourseConflict;
import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.domainVo.CourseConflictVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.CourseConflictService;
import com.eduboss.utils.HibernateUtils;

@Service
public class CourseConflictServiceImpl implements CourseConflictService {

	@Autowired
	private CourseConflictDao courseConflictDao;
	
	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private TwoTeacherClassCourseDao twoTeacherClassCourseDao;
	
	@Override
	public DataPackage findCourseConflictInfos(
			CourseConflictVo courseConflictVo,DataPackage dp)  {
		dp.setPageSize(999);
		DataPackage dpReturn = courseConflictDao.findCourseConflictInfos(courseConflictVo,dp);
		dpReturn.setDatas(HibernateUtils.voListMapping((List<CourseConflict>)dp.getDatas(), CourseConflictVo.class));
		Iterator<CourseConflictVo> it = dpReturn.getDatas().iterator();
		HashSet<String> courseIdSet = new HashSet<String>();
		while(it.hasNext()){
			CourseConflictVo vo = it.next();
			if(courseIdSet.contains(vo.getCourseId()))it.remove();
			if (vo.getClassTwoId() != null) {
			    TwoTeacherClassCourse twoCourse = twoTeacherClassCourseDao.findById(Integer.parseInt(vo.getCourseId()));
			    vo.setMiniClassName(twoCourse.getTwoTeacherClass().getName());
			} else {
			    if (vo.getMiniClassName() == null){
			        Course cou=courseDao.findById(vo.getCourseId());
			        if(cou!=null && cou.getSubject()!=null){
			            vo.setSubject(cou.getSubject().getName());
			            vo.setCourseStatus(cou.getCourseStatus().toString());
			        }
			        
			    }
			}
			courseIdSet.add(vo.getCourseId());
		}
		return dpReturn;
	}

	@Override
	public int countCourseConflictInfos(CourseConflictVo courseConflictVo) {
		return courseConflictDao.countCourseConflictInfos(courseConflictVo);
	}

	@Override
	public int countDistinctConflicts(String courseId, String studentId,
			String teacherId, String courseDate, String courseTime) {
		return courseConflictDao.countDistinctConflicts(courseId, studentId, teacherId, courseDate, courseTime);
	}

	@Override
	public List<String> findConflictDateByCause(String startDate,
			String endDate, String courseTime, String teacherId,
			String studentId,String courseSummaryId) {
		return courseConflictDao.findConflictDateByCause(startDate, endDate, courseTime, teacherId, studentId, courseSummaryId);
	}

    /**
     * 删除昨天以前的数据
     */
    @Override
    public void deleteDatasBeforeYesterday() {
        courseConflictDao.deleteDatasBeforeYesterday();
    }

}
