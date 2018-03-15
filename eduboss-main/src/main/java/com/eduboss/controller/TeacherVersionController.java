package com.eduboss.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.DataDict;
import com.eduboss.domainVo.PreTeacherVersionVo;
import com.eduboss.domainVo.TeacherVersionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.service.TeacherVersionService;
import com.eduboss.service.UserService;

/**
 * 2016-12-20
 * @author lixuejun
 *
 */
@RequestMapping(value="/TeacherVersionController")
@Controller
public class TeacherVersionController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeacherVersionService teacherVersionService;

	@RequestMapping(value="/getPagePreTeacherVersion")
	@ResponseBody
	public DataPackageForJqGrid getPagePreTeacherVersion(@ModelAttribute GridRequest gridRequest, @ModelAttribute PreTeacherVersionVo preTeacherVersionVo) {
		DataPackage dp = new DataPackage(gridRequest);
		userService.getPagePreTeacher(preTeacherVersionVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@RequestMapping(value="/editTeacherVersion")
	@ResponseBody
	public Response editTeacherVersion(@RequestBody TeacherVersionVo teacherVersionVo) {
		teacherVersionService.editTeacherVersion(teacherVersionVo);
		return new Response();
	}
	
	@RequestMapping(value="/getPageTeacherVersion")
	@ResponseBody
	public DataPackageForJqGrid getPageTeacherVersion(@ModelAttribute GridRequest gridRequest, @ModelAttribute TeacherVersionVo teacherVersionVo) {
		DataPackage dp = new DataPackage(gridRequest);
		teacherVersionService.getPageTeacherVersion(teacherVersionVo, dp);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	@RequestMapping(value="/findTeacherVersionById")
	@ResponseBody
	public TeacherVersionVo findTeacherVersionById(@RequestParam int teacherVersionId, String versionDate) {
		return teacherVersionService.findTeacherVersionById(teacherVersionId, versionDate);
	}
	
	/**
	 * 根据科目、年级查询可教老师 要根据登录人来查询 本校区或跨校区 老师
	 * @param gradeId 年级
	 * @param subjetId科目
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTeacherByGradeSubject")
	public List<Map<Object, Object>> getTeacherVersionByGradeSubject(String gradeId, String subjetId, Boolean isOtherOrganizationTeacher, String campusId) {
		return teacherVersionService.getTeacherByGradeSubject(gradeId,subjetId, isOtherOrganizationTeacher, campusId);
	}
	

	/**
	 * 根据科目、年级查询可教老师 要根据登录人来查询 本校区和跨校区 老师
	 * @param gradeId
	 * @param subjectId
	 * @param campusId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getThisAndOtherOrganizationTeacher")
	public Map<String, List<Map<Object, Object>>> getThisAndOtherOrganizationTeacher(String gradeId, String subjectId, String campusId){
		Map<String, List<Map<Object, Object>>> map = new HashMap<>();
		map.put("renke", teacherVersionService.getTeacherByGradeSubject(gradeId,subjectId, false, campusId));
		map.put("kuaxiaoqu", teacherVersionService.getTeacherByGradeSubject(gradeId, subjectId, true, campusId));
		return map;
	}
	
	/**
     * 根据学生、科目查询可教老师 要根据登录人来查询 本校区或跨校区 老师
     * @param studentId 学生
     * @param subjectId 科目
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTeacherByStudentSubject")
    public List<NameValue> getTeacherByStudentSubject(String studentId,String subjectId,Boolean isOtherOrganizationTeacher) {
    	List<Map<Object, Object>> list = teacherVersionService.getTeacherByStudentSubject(studentId, subjectId, isOtherOrganizationTeacher, null);
        List<NameValue> nvs = new ArrayList<NameValue>();
		for (Map<Object, Object> map : list) {
			nvs.add(SelectOptionResponse.buildNameValue((String)map.get("name"), (String)map.get("userId")));
		}
		return nvs;
    }
    
    /**
     * 根据老师获取可教科目
     * @param teacherId
     * @return
     */
    @RequestMapping(value = "/getCanTaughtSubjectByTeacher", method = RequestMethod.GET)
    @ResponseBody
    public Set<DataDict> getCanTaughtSubjectByTeacher(String teacherId){
        return teacherVersionService.getCanTaughtSubjectByTeacherAndGrade(teacherId, null);
    }

    /**
     * 根据老师和年级获取可教科目
     * @param teacherId
     * @param gradeId
     * @return
     */
    @RequestMapping(value = "/getCanTaughtSubjectByTeacherAndGrade", method = RequestMethod.GET)
    @ResponseBody
    public Set<DataDict> getCanTaughtSubjectByTeacherAndGrade(String teacherId,String gradeId){
        return teacherVersionService.getCanTaughtSubjectByTeacherAndGrade(teacherId,gradeId);
    }
    
    /**
     * 根据老师和学生获取可教科目
     * @param teacherId
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/getCanTaughtSubjectByTeacherAndStudent", method = RequestMethod.GET)
    @ResponseBody
    public Set<DataDict> getCanTaughtSubjectByTeacherAndStudent(String teacherId,String studentId){
        return teacherVersionService.getCanTaughtSubjectByTeacherAndStudent(teacherId, studentId);
    }
    
    /**
     * 根据校区获取该校区或跨校区的老师
     * @param campusId
     * @param isOtherOrganizationTeacher
     * @return
     */
    @RequestMapping(value = "/getTeachersByCampusId", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<Object, Object>> getTeachersByCampusId(String campusId, Boolean isOtherOrganizationTeacher) {
		return teacherVersionService.getTeachersByCampusId(campusId, isOtherOrganizationTeacher);
    }
    
   /** 获取当前用户的编制老师操作权限
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTeacherVersionAuthTags", method =  RequestMethod.GET)
	@ResponseBody
	public String getTeacherVersionAuthTags() {
		return teacherVersionService.getTeacherVersionAuthTags();
	}
	
}
