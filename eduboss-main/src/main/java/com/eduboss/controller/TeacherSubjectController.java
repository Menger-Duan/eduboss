package com.eduboss.controller;

import com.eduboss.common.Constants;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.UserVo;
import com.eduboss.dto.*;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.service.DataDictService;
import com.eduboss.service.TeacherSubjectService;
import com.eduboss.service.UserService;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value="/TeacherSubjectController")
public class TeacherSubjectController {

	
	@Autowired
	TeacherSubjectService teacherSubjectService;
	
	@Autowired
	private UserService userService;

    @Autowired
    private DataDictService dataDictService;
	
	@RequestMapping(value="/getTeacherSubjectList")
	@ResponseBody
	public DataPackageForJqGrid getTeacherSubjectList(GridRequest gridRequest, TeacherSubject teacherSubject){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = teacherSubjectService.getTeacherSubjectList(teacherSubject, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/editTeacherSubject")
	@ResponseBody
	public Response editTeacherSubject(@ModelAttribute GridRequest gridRequest, @ModelAttribute TeacherSubject teacherSubject) {
		
		teacherSubjectService.saveOrUpdateTeacherSubject(teacherSubject);
		
		return new Response();
	}
	
	@RequestMapping(value = "/deleteTeacherSubject")
	@ResponseBody
	public Response deleteTeacherSubject(@ModelAttribute GridRequest gridRequest, @ModelAttribute TeacherSubject teacherSubject) {
		teacherSubjectService.deleteTeacherSubject(teacherSubject);
		return new Response();
	}

	@ResponseBody
	@RequestMapping(value = "/disableTeacherSubjectById")
	public Response disableTeacherSubjectById(String teacherSubjectId) {
		 teacherSubjectService.disableTeacherSubjectById(teacherSubjectId);
		 return new Response();
	}
	
	/**
	 * 根据科目、年级查询可教老师 要根据登录人来查询 本校区 老师
	 * @param gradeId 年级
	 * @param subjetId科目
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTeacherSubjectByGradeSubject")
	public List<UserVo> getTeacherSubjectByGradeSubject(String gradeId,String subjetId,Boolean isOtherOrganizationTeacher) {
//		if(StringUtils.isEmpty(gradeId) && StringUtils.isEmpty(subjetId)){
//			return null;
//		}
		if(isOtherOrganizationTeacher!=null && isOtherOrganizationTeacher){
			return teacherSubjectService.getOtherOrganizationTeacherSubjectByGradeSubject(gradeId, subjetId);
		}
		return teacherSubjectService.getTeacherSubjectByGradeSubject(gradeId,subjetId);
	}
	
	/**
	 * 根据科目、年级 , 校区查询老师
	 * @param gradeId 年级
	 * @param subjetId科目
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTeacherSubjectByGradeSubjectCampusId")
	public List<UserVo> getTeacherSubjectByGradeSubjectCampusId(String gradeId,String subjetId,Boolean isOtherOrganizationTeacher,String campusId) {
		if(isOtherOrganizationTeacher!=null && isOtherOrganizationTeacher){
			return teacherSubjectService.getOtherOrganizationTeacherSubjectByGradeSubjectCampusId(gradeId, subjetId,campusId);
		}
		return teacherSubjectService.getTeacherSubjectByGradeSubjectCampusId(gradeId,subjetId,campusId);
	}

	/**
	 *
	 * @param gradeId
	 * @param subjectId
	 * @param campusId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRenkeHekuaxiaoquTeacher")
	public Map<String, List<UserVo>> getRenkeHekuaxiaoquTeacher(String gradeId, String subjectId, String campusId){
		Map<String, List<UserVo>> map = new HashMap<>();
		map.put("renke", teacherSubjectService.getTeacherSubjectByGradeSubjectCampusId(gradeId,subjectId,campusId));
		map.put("kuaxiaoqu", teacherSubjectService.getOtherOrganizationTeacherSubjectByGradeSubjectCampusId(gradeId, subjectId,campusId));
		return map;
	    
		
		//返回当前登陆者所属的分公司 下面各个校区的老师
	     
	
	}


	@ResponseBody
	@RequestMapping(value = "/getTeacherListForSelect",method = RequestMethod.POST)
	public List getTeacherListForSelect(String gradeId, String subjectId, String campusId, String brenchId){
		String[] campuss=null;
		if(StringUtils.isNotBlank(campusId)) {
			campuss = campusId.split(",");
		}
		return teacherSubjectService.getTeachersForSelectNew(gradeId,subjectId,campuss,brenchId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getTeachersForSelect",method = RequestMethod.GET)
	public List getTeachersForSelect(String gradeId, String subjectId){
		//TODO 是否需要这个限制
//		if(StringUtil.isBlank(gradeId)||StringUtil.isBlank(subjectId)){
//			return new ArrayList<>();
//		}
		String[] campuss=null;
		Organization brench = userService.getBelongBranch();
		String brenchId = null;
		if(brench!=null){
			brenchId = brench.getId();
		}
		return teacherSubjectService.getTeachersForSelectNew(gradeId, subjectId, campuss, brenchId);
	}


    /**
     * 根据学生、科目查询可教老师 要根据登录人来查询 本校区 老师
     * @param studentId 学生
     * @param subjectId 科目
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTeacherSubjectByStudentSubject")
    public List<NameValue> getTeacherSubjectByStudentSubject(String studentId,String subjectId,Boolean isOtherOrganizationTeacher) {
    	List<UserVo> voList = null;
        if(isOtherOrganizationTeacher!=null && isOtherOrganizationTeacher){
        	voList = teacherSubjectService.getOtherOrganizationTeacherSubjectByStudentSubject(studentId, subjectId);
        } else {
        	voList = teacherSubjectService.getTeacherSubjectByStudentSubject(studentId,subjectId);
        }
        List<NameValue> nvs = new ArrayList<NameValue>();
		for (UserVo uv : voList) {
			nvs.add(SelectOptionResponse.buildNameValue(uv.getName(), uv.getValue()));
		}
		return nvs;
    }
	
	/**
	 * 获取当前校区下指定角色的用户
	 * 
	 * @param roleCodes
	 *            多个用，号隔开
	 * @return
	 */
	@RequestMapping(value = "/getTeacherListSelection", method = RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getTeacherListSelection(String teacherName) {
		SelectOptionResponse selectOptionResponse = null;
		try {
			
			List<User> users =teacherSubjectService.getTeacherList(teacherName);
			List<NameValue> nvs = new ArrayList<NameValue>();
			if (users != null)
				for (User so : users) {
					nvs.add(so);
				}
			selectOptionResponse = new SelectOptionResponse(nvs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectOptionResponse;
	}

    /**
     * 根据老师获取可教科目
     * @param teacherId
     * @return
     */
    @RequestMapping(value = "/getCanTeachSubjectByTeacher", method = RequestMethod.GET)
    @ResponseBody
    public Set<DataDict> getCanTeachSubjectByTeacher(String teacherId){
        return dataDictService.getCanTeachSubjectByTeacher(teacherId);
    }

    /**
     * 根据老师和年级获取可教科目
     * @param teacherId
     * @param gradeId
     * @return
     */
    @RequestMapping(value = "/getCanTeachSubjectByTeacherAndGrade", method = RequestMethod.GET)
    @ResponseBody
    public Set<DataDict> getCanTeachSubjectByTeacherAndGrade(String teacherId,String gradeId){
        return dataDictService.getCanTeachSubjectByTeacherAndGrade(teacherId,gradeId);
    }

    /**
     * 根据老师和学生获取可教科目
     * @param teacherId
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/getCanTeachSubjectByTeacherAndStudent", method = RequestMethod.GET)
    @ResponseBody
    public Set<DataDict> getCanTeachSubjectByTeacherAndStudent(String teacherId,String studentId){
        return dataDictService.getCanTeachSubjectByTeacherAndStudent(teacherId, studentId);
    }
    
    /**
     * 根据校区获取该校区和跨校区的老师
     * @param campusId
     * @param isOtherOrganizationTeacher
     * @return
     */
    @RequestMapping(value = "/getTeacherListByCampusId", method = RequestMethod.GET)
    @ResponseBody
    public List<UserVo> getTeacherListByCampusId(String campusId, Boolean isOtherOrganizationTeacher) {
    	if(isOtherOrganizationTeacher!=null && isOtherOrganizationTeacher){
			if(org.apache.commons.lang3.StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
    			return teacherSubjectService.getOtherOrganizationTeacherByCampusIdNew(campusId);
			}else{
				return teacherSubjectService.getOtherOrganizationTeacherByCampusId(campusId);
			}
		}
		return teacherSubjectService.getTeacherListByCampusId(campusId);
    }

	//根据科目来筛选当前登录者所属分公司的老师，当科目为空时获取当前分公司的所有科目
	@RequestMapping(value = "/getCurrentBranchTeacher", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTeacherListByBranchId(){

		if(org.apache.commons.lang3.StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return teacherSubjectService.getTeacherListByBranchIdNew();
		}else{
			return teacherSubjectService.getTeacherListByBranchId();
		}

	}


}
