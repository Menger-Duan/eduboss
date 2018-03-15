package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.common.Constants;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.TeacherType;
import com.eduboss.dao.TeacherVersionDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TeacherFunctionVersion;
import com.eduboss.domain.TeacherSubjectVersion;
import com.eduboss.domain.TeacherVersion;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TeacherFunctionVersionVo;
import com.eduboss.domainVo.TeacherSubjectVersionVo;
import com.eduboss.domainVo.TeacherVersionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.StudentService;
import com.eduboss.service.TeacherFunctionVersionService;
import com.eduboss.service.TeacherSubjectVersionService;
import com.eduboss.service.TeacherVersionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

/**
 * 2016-12-20
 * @author lixuejun
 *
 */
@Service
public class TeacherVersionServiceImpl implements TeacherVersionService {
	
	@Autowired
	private TeacherFunctionVersionService teacherFunctionVersionService;
	
	@Autowired
	private TeacherSubjectVersionService teacherSubjectVersionService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private TeacherVersionDao teacherVersionDao;

	/**
	 * 保存或修改编制老师版本
	 */
	@Override
	public void editTeacherVersion(TeacherVersionVo teacherVersionVo) throws ApplicationException {
		TeacherVersion teacherVersion = HibernateUtils.voObjectMapping(teacherVersionVo, TeacherVersion.class);
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		String currentTime = DateTools.getCurrentDateTime();
		teacherVersion.setModifyTime(currentTime);
		teacherVersion.setModifyUserId(currentUserId);
		Set<TeacherFunctionVersionVo> tfvVoSet = teacherVersionVo.getTeacherFunctions();
		Set<TeacherFunctionVersion> tfvSet = HibernateUtils.voSetMapping(tfvVoSet, TeacherFunctionVersion.class);
		Set<TeacherSubjectVersionVo> tsvVoSet = teacherVersionVo.getTeacherSubjectVersions();
		Set<TeacherSubjectVersion> tsvSet = HibernateUtils.voSetMapping(tsvVoSet, TeacherSubjectVersion.class);
		String teacherFunctionDes = "";
		String functionsLevelDes = "";
		String subjectDes = "";
		String gradeDes = "";
		int isCurrentVersion = 0; // 是否有效版本 1是
		int isMonthVersion = 0; // 是否月份版本 1是
		int versionMonthInt = Integer.parseInt(teacherVersion.getVersionDate().substring(0, 4) 
				+ teacherVersion.getVersionDate().substring(5,7));
		if (teacherVersionVo.getId() > 0) { // 修改
			TeacherVersion teacherVersionInDb = teacherVersionDao.findById(teacherVersionVo.getId());
			if (teacherVersionInDb.getIsCurrentVersion() != 1 
					&& DateTools.daysBetween(DateTools.getCurrentDate(), teacherVersionInDb.getVersionDate()) < 0) {
				throw new ApplicationException("旧版本不容许再修改");
			}
			if (teacherVersionInDb.getIsCurrentVersion() == 1 && !teacherVersion.getVersionDate().equals(teacherVersionInDb.getVersionDate())) {
				throw new ApplicationException("当前正在使用版本不可以修改日期");
			}
			Set<TeacherFunctionVersion> tfvInDbSet = teacherVersionInDb.getTeacherFunctions();
			Set<TeacherSubjectVersion> tsvInDbSet = teacherVersionInDb.getTeacherSubjectVersions();
			boolean exists = false;
			String teacherFunctionsLevels = "";
			// 对比判断教师职能关联的变动
			for (TeacherFunctionVersion tfv : tfvSet) {
				String teacherFunctionsLevel = tfv.getTeacherFunctions().getValue() + "_" + tfv.getFunctionsLevel().getValue();
				if (teacherFunctionsLevels.contains(teacherFunctionsLevel)) {
					throw new ApplicationException("教师职能不能重复");
				} else {
					teacherFunctionsLevels += teacherFunctionsLevel + ",";
				}
				exists = false; // 判断是否新增的
				for (TeacherFunctionVersion tfvInDb : tfvInDbSet) {
					if (tfvInDb.getId() == tfv.getId()) {
						exists = true;
						break;
					}
				}
				if (tfv.getTeacherFunctions() == teacherVersion.getMainTeacherFunctions()) {
					teacherVersion.setMainFunctionsLevel(tfv.getFunctionsLevel());
				}
				tfv.setTeacherVersion(teacherVersionInDb);
				if (!teacherFunctionDes.contains(tfv.getTeacherFunctions().getValue())) {
					teacherFunctionDes += tfv.getTeacherFunctions().getValue() + ",";
				}
				if (!functionsLevelDes.contains(tfv.getFunctionsLevel().getValue())) {
					functionsLevelDes += tfv.getFunctionsLevel().getValue() + ",";
				}
				teacherFunctionVersionService.editTeacherFunctionVersion(tfv);
			}
			
			if (DateTools.daysBetween(teacherVersion.getVersionDate(), DateTools.getCurrentDate()) >= 0 || 
					teacherVersion.getVersionDate().equals(DateTools.getFirstDayOfMonth(teacherVersion.getVersionDate()))) {
				isMonthVersion = 1;
			}
			
			if (tsvInDbSet.size() > 0) {
				for (TeacherSubjectVersion tsvInDb : tsvInDbSet) {
					exists = false;
					for (TeacherSubjectVersion tsv : tsvSet) {
						if (tsv.getId() == tsvInDb.getId()) {
							exists = true;
							break;
						}
					}
					// 判断是否删除了
					if (!exists) {
						teacherSubjectVersionService.deleteTeacherSubjectVersion(tsvInDb);
					}
				}
			}
			
			// 对比判断授课老师科目关联的变动
			for (TeacherSubjectVersion tsv : tsvSet) {
				exists = false; // 判断是否新增的
				for (TeacherSubjectVersion tsvInDb : tsvInDbSet) {
					if (tsvInDb.getId() == tsv.getId()) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					tsv.setTeacherVersion(teacherVersion);
				}
				if (!subjectDes.contains(tsv.getSubject().getId())) {
					subjectDes += tsv.getSubject().getId() + ",";
				}
				if (!gradeDes.contains(tsv.getGrade().getId())) {
					gradeDes += tsv.getGrade().getId() + ",";
				}
				tsv.setBlCampus(teacherVersion.getBlCampus());
				tsv.setVersionMonth(versionMonthInt);
				tsv.setIsMonthVersion(isMonthVersion);
				teacherSubjectVersionService.editTeacherSubjectVersion(tsv);
			}
			
			teacherVersion.setIsCurrentVersion(teacherVersionInDb.getIsCurrentVersion());
			teacherVersion.setTeacherFunctionDes(teacherFunctionDes.substring(0, teacherFunctionDes.length() - 1));
			teacherVersion.setFunctionsLevelDes(functionsLevelDes.substring(0, functionsLevelDes.length() - 1));
			if (StringUtil.isNotBlank(subjectDes)) {
				teacherVersion.setSubjectDes(subjectDes.substring(0, subjectDes.length() - 1));
			}
			if (StringUtil.isNotBlank(gradeDes)) {
				teacherVersion.setGradeDes(gradeDes.substring(0, gradeDes.length() - 1));
			}
			if (StringUtils.isBlank(teacherVersionVo.getMainGradeId())) {
				teacherVersion.setMainGrade(null);
			}
			if (StringUtils.isBlank(teacherVersionVo.getMainSubjectId())) {
				teacherVersion.setMainSubject(null);
			}
			teacherVersion.setTeacherFunctions(null);
			teacherVersion.setTeacherSubjectVersions(null);
			teacherVersion.setCreateTime(teacherVersionInDb.getCreateTime());
			teacherVersion.setCreateUserId(teacherVersionInDb.getCreateUserId());
			teacherVersionDao.merge(teacherVersion);
		} else { // 新增
			userService.updateTeacherSubjectStatus(teacherVersion.getTeacher().getUserId(), 1);
			List<TeacherVersion> tvList = this.getTeacherVersionList(teacherVersion.getTeacher().getUserId());
			if (tvList.size() > 0) {
				for (TeacherVersion tv : tvList) {
					if (tv.getVersionDate().equals(teacherVersion.getVersionDate())) {
						throw new ApplicationException("已有生效日期的版本");
					}
				}
			} else {
				isCurrentVersion = 1;
				isMonthVersion = 1;
			}
			// 更新用户状态为已编制老师
			if (teacherVersion.getVersionDate().equals(DateTools.getCurrentDate())) {
				// 把旧的有效老师版本更新为无效,并且把关联的老师科目的月份版本更新为非月份版本
				isCurrentVersion = 1;
				isMonthVersion = 1;
				this.updateOldCurrentVersion(tvList);
			} else {
				String versionMonth = teacherVersion.getVersionDate().substring(0, 7);
				if (!DateTools.getCurrentDate().substring(0, 7).equals(versionMonth) && 
						teacherVersion.getVersionDate().equals(DateTools.getFirstDayOfMonth(teacherVersion.getVersionDate()))) {
					isMonthVersion = 1;
				}
			}
			
			for (TeacherFunctionVersion tfv : tfvSet) {
				if (tfv.getTeacherFunctions() == teacherVersion.getMainTeacherFunctions()) {
					teacherVersion.setMainFunctionsLevel(tfv.getFunctionsLevel());
				}
				if (!teacherFunctionDes.contains(tfv.getTeacherFunctions().getValue())) {
					teacherFunctionDes += tfv.getTeacherFunctions().getValue() + ",";
				}
				if (!functionsLevelDes.contains(tfv.getFunctionsLevel().getValue())) {
					functionsLevelDes += tfv.getFunctionsLevel().getValue() + ",";
				}
			}
			for (TeacherSubjectVersion tsv : tsvSet) {
				if (!subjectDes.contains(tsv.getSubject().getId())) {
					subjectDes += tsv.getSubject().getId() + ",";
				}
				if (!gradeDes.contains(tsv.getGrade().getId())) {
					gradeDes += tsv.getGrade().getId() + ",";
				}
			}
			if (StringUtils.isBlank(teacherVersionVo.getMainGradeId())) {
				teacherVersion.setMainGrade(null);
			}
			if (StringUtils.isBlank(teacherVersionVo.getMainSubjectId())) {
				teacherVersion.setMainSubject(null);
			}
			teacherVersion.setCreateTime(currentTime);
			teacherVersion.setCreateUserId(currentUserId);
			teacherVersion.setIsCurrentVersion(isCurrentVersion);
			teacherVersion.setTeacherFunctionDes(teacherFunctionDes.substring(0, teacherFunctionDes.length() - 1));
			teacherVersion.setFunctionsLevelDes(functionsLevelDes.substring(0, functionsLevelDes.length() - 1));
			if (StringUtil.isNotBlank(subjectDes)) {
				teacherVersion.setSubjectDes(subjectDes.substring(0, subjectDes.length() - 1));
			}
			if (StringUtil.isNotBlank(gradeDes)) {
				teacherVersion.setGradeDes(gradeDes.substring(0, gradeDes.length() - 1));
			}
			teacherVersion.setTeacherFunctions(null);
			teacherVersion.setTeacherSubjectVersions(null);
			teacherVersionDao.save(teacherVersion);
			teacherVersionDao.flush();
			for (TeacherFunctionVersion tfv : tfvSet) {
				tfv.setTeacherVersion(teacherVersion);
				teacherFunctionVersionService.editTeacherFunctionVersion(tfv);
			}
			
			for (TeacherSubjectVersion tsv : tsvSet) {
				tsv.setBlCampus(teacherVersion.getBlCampus());
				tsv.setTeacherVersion(teacherVersion);
				tsv.setIsMonthVersion(isMonthVersion);
				tsv.setVersionMonth(versionMonthInt);
				teacherSubjectVersionService.editTeacherSubjectVersion(tsv);
			}
		}
	}
	
	/**
	 * 把旧的有效老师版本更新为无效,并且把关联的老师科目的月份版本更新为非月份版本
	 * @param tvList
	 * @return
	 */
	private void updateOldCurrentVersion(List<TeacherVersion> tvList) {
		TeacherVersion oldCurrentVersion = null;
		if (tvList.size() > 0) {
			for (TeacherVersion tv : tvList) {
				if (tv.getIsCurrentVersion() == 1) {
					oldCurrentVersion = tv;
					break;
				}
			}
		}
		if (oldCurrentVersion != null) {
			oldCurrentVersion.setIsCurrentVersion(0);
			teacherVersionDao.merge(oldCurrentVersion);
			Set<TeacherSubjectVersion> oldTsvSet = oldCurrentVersion.getTeacherSubjectVersions();
			for (TeacherSubjectVersion oldTsv : oldTsvSet) {
				oldTsv.setIsMonthVersion(0);
				teacherSubjectVersionService.editTeacherSubjectVersion(oldTsv);
			}
		}
	}

	/**
	 * 分页查找已编制教师
	 */
	@Override
	public DataPackage getPageTeacherVersion(TeacherVersionVo teacherVersionVo,
			DataPackage dp) {
		return teacherVersionDao.getPageTeacherVersion(teacherVersionVo, userService.getBelongBranch(), dp);
	}
	
	/**
	 * 根据老师ID查找所有版本的teacherVersion
	 */
	@Override
	public List<TeacherVersion> getTeacherVersionList(String teacherId) {
		return teacherVersionDao.getTeacherVersionList(teacherId);
	}

	/**
	 * 根据id查找老师版本
	 */
	@Override
	public TeacherVersionVo findTeacherVersionById(int teacherVersionId,
			String versionDate) {
		TeacherVersion teacherVersion = teacherVersionDao.findById(teacherVersionId);
		TeacherVersionVo returnVo = HibernateUtils.voObjectMapping(teacherVersion, TeacherVersionVo.class);
		if (StringUtils.isNotBlank(returnVo.getBirthDay())) {
			returnVo.setAge(DateTools.getAge(returnVo.getBirthDay()));
    	}
    	if (StringUtils.isNotBlank(returnVo.getEntryDate())) {
    		returnVo.setWorkingYears(DateTools.getAge(returnVo.getEntryDate()));
    	}
		if (StringUtils.isBlank(versionDate)) {
			List<TeacherVersion> tvList = this.getTeacherVersionList(teacherVersion.getTeacher().getUserId());
			List<Map<String, Object>> versions = new ArrayList<Map<String,Object>>();
			for (TeacherVersion tv : tvList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("versionDate", tv.getVersionDate());
				map.put("id", tv.getId());
				versions.add(map);
			}
			returnVo.setVersions(versions);
		}
		return returnVo;
	}

	/**
	 * 根据科目、年级和校区级别查询可教老师
	 */
	@Override
	public List<Map<Object, Object>> getTeacherByGradeSubject(String gradeId,
			String subjetId, Boolean isOtherOrganizationTeacher, String campusId) {
		Organization campus = null;
		if (StringUtils.isNotBlank(campusId)) {
			campus = organizationService.findById(campusId);
		} else {
			campus = userService.getBelongCampus();
		}
		if(isOtherOrganizationTeacher !=null && isOtherOrganizationTeacher && StringUtils.isBlank(campusId)){
			campusId = campus.getId();
		} 
		return teacherVersionDao.getTeacherByGradeSubject(gradeId, subjetId, campusId, campus.getOrgLevel());
	}

	/**
	 * 根据学生、科目和校区级别查询可教老师
	 */
	@Override
	public List<Map<Object, Object>> getTeacherByStudentSubject(
			String studentId, String subjetId,
			Boolean isOtherOrganizationTeacher, String campusId) {
		StudentVo stu = studentService.findStudentById(studentId);
        if(stu == null){
            throw new ApplicationException("找不到学生，ID：" + studentId);
        }
        return this.getTeacherByGradeSubject(stu.getGradeId(), subjetId, isOtherOrganizationTeacher, campusId);
	}

	/**
	 * 根据老师和年级获取可教科目
	 */
	@Override
	public Set<DataDict> getCanTaughtSubjectByTeacherAndGrade(String teacherId,
			String gradeId) {
		List<TeacherSubjectVersion> list = teacherSubjectVersionService.getCanTaughtSubjectByTeacherAndGrade(teacherId, gradeId);
		String subjectIds = "";
		Set<DataDict> subjects = new HashSet<DataDict>();
		for (TeacherSubjectVersion tsv : list) {
			tsv.getSubject().getName();
			if (!subjectIds.contains(tsv.getSubject().getId())) {
				subjectIds +=  tsv.getSubject().getId() + ",";
				subjects.add(tsv.getSubject());
			}
		}
		return subjects;
	}

	/**
	 * 根据老师和学生获取可教科目
	 */
	@Override
	public Set<DataDict> getCanTaughtSubjectByTeacherAndStudent(
			String teacherId, String studentId) {
		StudentVo stu = studentService.findStudentById(studentId);
        if(stu == null){
            throw new ApplicationException("找不到学生，ID：" + studentId);
        }
		return this.getCanTaughtSubjectByTeacherAndGrade(teacherId, stu.getGradeId());
	}

	/**
	 * 根据校区获取该校区或跨校区的老师
	 */
	@Override
	public List<Map<Object, Object>> getTeachersByCampusId(String campusId,
			Boolean isOtherOrganizationTeacher) {
		String orgId = null;
		Organization campus = organizationService.findById(campusId);
		if(isOtherOrganizationTeacher !=null && isOtherOrganizationTeacher && StringUtils.isBlank(campusId)){
			orgId = campus.getId();
		} 
		return teacherVersionDao.getTeacherByGradeSubject(null, null, orgId, campus.getOrgLevel());
	}

	/**
	 * 获取当前用户的编制老师操作权限
	 */
	@Override
	public String getTeacherVersionAuthTags() {
		User currentUser = userService.getCurrentLoginUser();
		List<Map<Object, Object>> list ;
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			list = teacherVersionDao.getTeacherVersionAuthTagsNew(currentUser.getUserId());
		}else{
			list = teacherVersionDao.getTeacherVersionAuthTags(currentUser.getUserId());
		}
		String tags = "";
		if (list != null && list.size() > 0) {
			for (Map<Object, Object> map : list) {
				tags += map.get("RTAG") + ",";
			}
		}
		return tags;
	}

	/**
	 * 根据用户id，courseDate查找老师类型
	 */
	@Override
	public String getTeacherTypeByUserIdAndCourseDate(String teacherId,
			String courseDate) {
		List<Map<Object, Object>> list = teacherVersionDao.getclosedTeacherByUserIdAndCourseDate(teacherId, courseDate);
		if (list.size() > 0 && list.get(0).get("TEACHER_TYPE") != null) {
			return list.get(0).get("TEACHER_TYPE").toString();
		}
		return TeacherType.NORMAL_TEACHER.getValue();
	}

}
