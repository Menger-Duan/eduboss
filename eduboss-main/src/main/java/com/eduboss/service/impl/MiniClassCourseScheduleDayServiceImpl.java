package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.dao.CourseConflictDao;
import com.eduboss.dao.MiniClassStudentDao;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassStudentAttendent;
import com.eduboss.domainVo.MiniClassCourseScheduleDayVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.CourseService;
import com.eduboss.service.MiniClassCourseScheduleDayService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service("MiniClassCourseScheduleDayService")
public class MiniClassCourseScheduleDayServiceImpl implements MiniClassCourseScheduleDayService{

	@Autowired
	CourseService courseService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private MiniClassStudentDao miniclassStudentDao;
	
	@Autowired
	private CourseConflictDao courseConflictDao;
	
	@Override
	public DataPackage getMiniClassCourseScheduleDayList(DataPackage dataPackage,Map<String, Object> params) {
		StringBuilder conditionHql = new StringBuilder();
		conditionHql.append(" from MiniClassCourse where 1=1 ");
		
		Map<String, Object> param = Maps.newHashMap();
		
		if(StringUtil.isNotBlank(StringUtil.toString(params.get("organizationId")))){
			conditionHql.append(" and miniClass.blCampus.id = :organizationId ");
		    param.put("organizationId", params.get("organizationId"));
		}else{
			conditionHql.append(" and miniClass.blCampus.id = :blCampusId ");
			param.put("blCampusId", userService.getBelongCampus().getId().trim());
		}
		if(StringUtil.isNotBlank(params.get("date").toString())){
			conditionHql.append(" and courseDate = :courseDate ");
			param.put("courseDate", params.get("date"));
		}
		if(StringUtil.isNotBlank(params.get("classroom").toString())){
			conditionHql.append(" and classroom.classroom like :classroom ");
			param.put("classroom", "%"+params.get("classroom")+"%");
		}
		if(StringUtil.isNotBlank(params.get("miniClass").toString())){
			conditionHql.append(" and miniClass.name like :miniClass ");
			param.put("miniClass", "%"+params.get("miniClass")+"%");
		}

		if(StringUtil.isNotBlank(params.get("miniClassTypeId").toString())){

			String miniClassTypeId = params.get("miniClassTypeId").toString();
			String[] miniClassTypes=miniClassTypeId.split(",");
			if(miniClassTypes.length>0){
//				conditionHql.append(" and (miniClass.product.classType.id ='").append(miniClassTypes[0]).append("' ");
//
//				for (int i = 1; i < miniClassTypes.length; i++) {
//					conditionHql.append(" or miniClass.product.classType.id ='").append(miniClassTypes[i]).append("' ");
//				}
//				conditionHql.append(" )");
				conditionHql.append(" and miniClass.product.classType.id in ( :miniClassTypes )");
				param.put("miniClassTypes", miniClassTypes);
			}
		}

		conditionHql.append(" order by miniClass.classroom.id,miniClassCourseId desc ");
		
		List<MiniClassCourse> courseList = courseService.getCourseByCondition(conditionHql.toString(),param);		
		List<MiniClassCourseScheduleDayVo> voList = new ArrayList<MiniClassCourseScheduleDayVo>();
		int crashInd=0;
		for (MiniClassCourse miniClassCourse : courseList) {
//			if (miniClassCourse.getClassroom() == null)
//				continue;
			MiniClassCourseVo mccVo = HibernateUtils.voObjectMapping(miniClassCourse, MiniClassCourseVo.class);
			List<MiniClassStudentAttendent> mcsaList = courseService.getMiniClassStudentAttendentById(mccVo.getMiniClassCourseId());
			int arrivals = 0;//准时上课
			int nonono = 0;//未上课
//			int leave = 0;//请假
			int absent = 0;//缺勤
			int paid = 0;//结算	
			int late = 0; //迟到
			
			int promisers = 0;//  应到人数
			for(MiniClassStudentAttendent mcsa : mcsaList){
				promisers++;
				if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.CONPELETE)){
					arrivals++;
				}
				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.LATE)){
					late++;
				}
				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.NEW))
					nonono++;	
//				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.LEAVE))
//					leave++;
				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.ABSENT))
					absent++;
				
				if(mcsa.getChargeStatus().equals(MiniClassStudentChargeStatus.CHARGED)){
					paid++;
				}
			}

			
			MiniClassCourseScheduleDayVo mccsdVo = new MiniClassCourseScheduleDayVo();
			mccsdVo.setMiniClassName(mccVo.getMiniClassName());
			mccsdVo.setOrganizationName(mccVo.getBlCampusName());
			mccsdVo.setCourseDate(mccVo.getCourseDate());
			mccsdVo.setCourseStartTime(mccVo.getCourseTime());
			mccsdVo.setCourseEndTime(mccVo.getCourseEndTime());
			mccsdVo.setTeacherName(mccVo.getTeacherName()==null?"无":mccVo.getTeacherName());
			mccsdVo.setClassroom(mccVo.getClassroom());
			mccsdVo.setClassroomId(mccVo.getClassroomId());
			mccsdVo.setAttendance(mccVo.getCourseStatus());
			mccsdVo.setArrivals(arrivals);
			mccsdVo.setLate(late);
			mccsdVo.setPromisers(promisers);
			mccsdVo.setNonono(nonono);
//			mccsdVo.setLeave(leave);
			mccsdVo.setAbsent(absent);
			mccsdVo.setPaid(paid);
			
			String courseTime = miniClassCourse.getCourseTime() + " - " + miniClassCourse.getCourseEndTime();
			// 检测是否有冲突
//			crashInd = courseConflictDao.countMiniClassDistinctConflicts(miniClassCourse.getMiniClassCourseId(),
//					"", miniClassCourse.getTeacher()==null?"":miniClassCourse.getTeacher().getUserId(), miniClassCourse.getCourseDate(), courseTime);

			crashInd = courseConflictDao.findCourseConflictCount(miniClassCourse.getMiniClassCourseId(), "", miniClassCourse.getTeacher()==null?"":miniClassCourse.getTeacher().getUserId());
			mccsdVo.setCrashInd(crashInd);	
			
			voList.add(mccsdVo);			
						
		}
		
		dataPackage.setDatas(voList);
		return dataPackage;
	}

}
