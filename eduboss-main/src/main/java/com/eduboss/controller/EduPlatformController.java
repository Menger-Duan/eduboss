package com.eduboss.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.Constants;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.AttentanceInfoVo;
import com.eduboss.domainVo.EduPlatform.BaseRelateDataVo;
import com.eduboss.domainVo.EduPlatform.TextBookParamVo;
import com.eduboss.domainVo.EduPlatform.UpdateAttentanceInfoVo;
import com.eduboss.dto.CourseSearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ClassService;
import com.eduboss.service.CourseService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ProductService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.StudentService;
import com.eduboss.service.TeacherService;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;



/**
 * 
 * @author xiaojinwang
 * @date 2016-10-18
 * @description 开放给教育平台的接口
 * 这些接口映射路径需要在application-security.xml配置 免校验
 */
/**
 * 所有接口要返回格式
 * resultStatus 状态码  200是成功 999是系统错误 其他的也是错误
 * resultMessage    状态码描述或者应用层描述
 * result 结果集 json格式
 */


@Controller
@RequestMapping(value = {"/EduPlatform","/boss/{name}"})
public class EduPlatformController {

	private final static Logger log = Logger.getLogger(EduPlatformController.class);
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private DataDictService dataDictService;
    
	@Autowired
	private ClassService classService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SmallClassService smallClassService;
	
	// 课程信息
	@RequestMapping(value = "/course/courseInfo")
	@ResponseBody
	public Map<String, Object> getCourseInfo(@RequestParam(required = false) String teacherId,
			@RequestParam(required = false) String studentId, @RequestParam(required = false) String courseId,
			@RequestParam(required = false) String beginDate, @RequestParam(required = false) String endDate,String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		
        if(StringUtils.isBlank(teacherId)&&StringUtils.isBlank(studentId)&&StringUtils.isBlank(courseId)){
        	if(log.isDebugEnabled())
        		log.debug("教师编号、学生编号、课程编号必填其中一个");
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教师编号、学生编号、课程编号必填其中一个");
        	map.put("result", null);
        	return map;
        }
        
        if(StringUtils.isBlank(teacherId)&&StringUtils.isNotBlank(studentId)&&StringUtils.isBlank(courseId)){
        	if(log.isDebugEnabled())
        		log.debug("只有学生studentId暂不支持");
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "只有学生studentId暂不支持");
        	map.put("result", null);
        	return map;
        }
        
        //这里暂时不校验日期的格式 YYYY-MM-DD
        if(StringUtils.isBlank(beginDate)||StringUtils.isBlank(endDate)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "开始日期或者结束日期不能为空");
        	map.put("result", null);
        	return map;
        }
        //startDate必须小于endDate
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
			Date begin = format.parse(beginDate);
			Date end   = format.parse(endDate);
			if(begin.getTime()>end.getTime()){
		       map.put("resultStatus", 400);
		       map.put("resultMessage", "开始日期必须小于结束日期");
		       map.put("result", null); 
		       return map;
			}
		} catch (ParseException e) {
		   if(log.isDebugEnabled()){
			   log.debug("日期不符合yyyy-MM-dd格式");
		   }
           map.put("resultStatus", 400);
           map.put("resultMessage", "日期不符合yyyy-MM-dd格式");
           map.put("result", null); 
           return map;
		}
        
        //根据条件返回课程信息
        //满足时间区间(startDate,endDate)内的课程信息
        //课程编号为courseId的课程信息  
        //teacherId的教师上课的课程
        //学生studentId上课的课程
        CourseSearchInputVo courseSearchInputVo = new CourseSearchInputVo();
        courseSearchInputVo.setTeacherId(teacherId);
        courseSearchInputVo.setCourseId(courseId);
        courseSearchInputVo.setStudentId(studentId);
        courseSearchInputVo.setStartDate(beginDate);
        courseSearchInputVo.setEndDate(endDate);
		try {
			courseSearchInputVo.setType(ProductType.valueOf(type));
		} catch (IllegalArgumentException e) {
			if (log.isDebugEnabled()) {
				log.debug("课程类型不对");
			}
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
			return map;
		}
        
		return courseService.getCourseInfo(courseSearchInputVo);

	}
	
	
	//教师列表
	@RequestMapping(value = "/user/teacherInfo")
	@ResponseBody
	public Map<String, Object> getTeacherInfo(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String id,String phone,String name){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		

		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return teacherService.getTeacherInfoNew(dataPackage, id, phone, name);
		}else{
			return teacherService.getTeacherInfo(dataPackage, id,phone,name);
		}
	}
	
	
	//学生列表
	@RequestMapping(value = "/user/studentInfo")
	@ResponseBody
	public Map<String, Object> getStudentInfo(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String id){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);
		return studentService.getStudentInfo(dataPackage, id);
	}
	
	
	//年级列表
	@RequestMapping(value = "/grade/gradeInfo")
	@ResponseBody
	public Map<String, Object> getGradeInfo(@RequestParam(required = false) String id){		
          return dataDictService.getGradeInfo(id);
	}
	
	//班级列表(小班)
	@RequestMapping(value = "/class/classInfo")
	@ResponseBody
	public Map<String, Object> getMiniClassInfo(@RequestParam (required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String id){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);
		
		return classService.getMiniClassInfo(dataPackage, id);
	}
	
	//班级内学生信息
	@RequestMapping(value = "/class/studentInfo")
	@ResponseBody
	public Map<String, Object> getClassStudentInfo(@RequestParam(required = false) String classId,String type){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(classId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "班级编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		return studentService.getClassStudentInfo(classId,type);
	}
	
	
	//课程内学生信息
	@RequestMapping(value = "/course/studentInfo")
	@ResponseBody
	public Map<String, Object> getCourseStudentInfo(@RequestParam(required = false) String courseId,String type){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(courseId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程编号不能为空");
        	map.put("result", null);
        	return map;
		}
		return courseService.getCourseStudentInfo(courseId,type);
	}	
	//组织架构列表
	@RequestMapping(value = "/org/organizationInfo")
	@ResponseBody
	public Map<String, Object> getOrganizationInfo(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String id){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		
		return organizationService.getOrganizationInfo(dataPackage, id);
	}
		
	//教师所属组织架构列表
	//没有在使用了，我在文档备注一下，改成用/eduboss/EduPlatform/user/userInfo.do
	@RequestMapping(value = "/org/orgTeacherInfo")
	@ResponseBody
	public Map<String, Object> getOrgTeacherInfo(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String teacherId){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		
		return organizationService.getOrgTeacherInfo(dataPackage, teacherId);
	}	
	
	//根据课程id查询产品信息
	@RequestMapping(value = "/product/courseProductInfo")
	@ResponseBody
	public Map<String, Object> getCourseProductInfo(@RequestParam(required = false)String courseId){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(courseId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程编号不能为空");
        	map.put("result", null);
        	return map;
		}
		return productService.getCourseProductInfo(courseId);
	}
	
	
	
	
	
	//根据数据字典的类目去返回数据字典的值
	@RequestMapping(value = "/datadict/dataDictInfo")
	@ResponseBody
	public Map<String, Object> findDataDictByCategory(String category){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(category)){
			map.put("resultStatus", 400);
        	map.put("resultMessage", "字典类目不能为空");
        	map.put("result", null);
        	return map;
		}
		return dataDictService.findDataDictByCategory(category);
	}
	
	//培优绑定教材页面 查询参数 基础数据
	@RequestMapping(value = "/datadict/productInfo")
	@ResponseBody
	public Map<String, Object> findDataDictProductInfo(String category){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(category)){
			map.put("resultStatus", 400);
        	map.put("resultMessage", "产品类型不能为空");
        	map.put("result", null);
        	return map;
		}	    
		return dataDictService.findDataDictProductInfo(category);
	}
	
	
	//组织架构及其所属人员列表  当前人员只查老师
	@RequestMapping(value = "/organizaiton/orgsAndTeachers")
	@ResponseBody
	public Map<String, Object> getOrgsAndTeachers(String orgId){
		//如果不传orgId则默认获取星火集团
		if(StringUtils.isBlank(orgId)){
			Organization organization = organizationService.fingOrgByOrgLevelAndOrgType("0001", OrganizationType.GROUNP.getValue());
			orgId = organization.getId();
		}
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return organizationService.getOrgsAndTeachersNew(orgId) ;
		} else{
			return organizationService.getOrgsAndTeachers(orgId) ;
		}
	}
	
	//用户列表
	@RequestMapping(value = "/user/userInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String id,String phone,String name,String employeeNo){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		
		return userService.getUserInfo(dataPackage,id,phone,name,1,employeeNo);
	}
	
	//用户列表 精简过字段的用户列表
	@RequestMapping(value = "/user/userList")
	@ResponseBody
	public Map<String, Object> getUserList(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,@RequestParam(required = false) String id,String phone,String name){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		
		return userService.getUserInfo(dataPackage,id,phone,name,2);
	}
	
	//双师班级详情
	@RequestMapping(value = "/twoTeacher/classInfo")
	@ResponseBody
	public Map<String, Object> getTwoTeacherClassInfo(@RequestParam(required = false) String classId,@RequestParam(required = false) String classIdTwo){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(classId) && StringUtils.isBlank(classIdTwo)){
	        map.put("resultStatus", 400);
	        map.put("resultMessage", "班级编号不能为空");
	        map.put("result", null);
	        return map;
		}
		return classService.getTwoTeacherClassInfo(classId,classIdTwo);
	}

	//学生查询自己的班级列表
	@RequestMapping(value = "/courseInfo/queryStudentClassInfo")
	@ResponseBody
	public Map<String, Object> queryStudentClassInfo(@RequestParam(required = false) String studentId, @RequestParam(required = false) String type){
		Map<String, Object> map = new HashMap<>();// LinkedHashMap
		if (StringUtils.isBlank(studentId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "学生编号不能为空");
			map.put("result", null);
			return map;
		}
		if (StringUtils.isBlank(type)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不能为空");
			map.put("result", null);
			return map;
		}else {
			int i = "ONE_ON_ONE_COURSE,SMALL_CLASS,ECS_CLASS,ONE_ON_MANY,TWO_TEACHER".indexOf(type);
			if (i == -1){
				map.put("resultStatus", 400);
				map.put("resultMessage", "课程类型找不到");
				map.put("result", null);
				return map;
			}
		}
		long tr1 = System.currentTimeMillis();
		map = classService.queryStudentClassInfo(studentId, type, map);
		long tr2 = System.currentTimeMillis();
		System.out.println("耗时ms:"+(tr2-tr1));
		return map;
	}

	/**
	 * 晓服务APP 1.8.0
	 * 根据学生id获取一对一年级和科目相关的课程
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/courseInfo/getOneOnOneCourseByStudentId")
	@ResponseBody
	public Map<String, Object> getOneOnOneCourseByStudentId(@RequestParam(required = false) String studentId){
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(studentId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "学生编号不能为空");
			map.put("result", null);
			return map;
		}
		long tr1 = System.currentTimeMillis();
		map = classService.getOneOnOneCourseByStudentId(studentId, map);
		long tr2 = System.currentTimeMillis();
		System.out.println("耗时ms:"+(tr2-tr1));
		return map;
	}

	/**
	 *晓服务APP 1.8.0
	 * 根据学生id,年级id，科目id获取一对一上课详情
	 * @param studentId
	 * @param gradeId
	 * @param subjectId
	 * @return
	 */
	@RequestMapping(value = "/courseInfo/getOneOnOneCourse")
	@ResponseBody
	public Map<String, Object> getOneOnOneCourse(@RequestParam(required = false) String studentId, @RequestParam(required = false) String gradeId, @RequestParam(required = false) String subjectId){
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(studentId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "学生编号不能为空");
			map.put("result", null);
			return map;
		}
		if (StringUtils.isBlank(gradeId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "年级不能为空");
			map.put("result", null);
			return map;
		}
		if (StringUtils.isBlank(subjectId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "科目不能为空");
			map.put("result", null);
			return map;
		}
		long tr1 = System.currentTimeMillis();
		map = classService.getOneOnOneCourse(studentId, gradeId, subjectId, map);
		long tr2 = System.currentTimeMillis();
		System.out.println("耗时ms:"+(tr2-tr1));
		return map;
	}

	/**
	 * 晓服务APP 1.8.0
	 * 根据学生id和科目名字获取相关老师
	 * @param studentId
	 * @param subject
	 * @return
	 */
	@RequestMapping(value = "/user/getTeacherByStudentIdAndSubject")
	@ResponseBody
	public Map<String, Object> getTeacherByStudentIdAndSubject(@RequestParam(required = false) String studentId, @RequestParam(required = false) String subject){
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(studentId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "学生编号不能为空");
			map.put("result", null);
			return map;
		}
		if (StringUtils.isBlank(subject)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "科目不能为空");
			map.put("result", null);
			return map;
		}
		long tr1 = System.currentTimeMillis();
		map = teacherService.getTeacherByStudentIdAndSubject(studentId, subject, map);
		long tr2 = System.currentTimeMillis();
		System.out.println("耗时ms:"+(tr2-tr1));
		return map;
	}
	
	
	//老师查询自己未结课的班级列表
	@RequestMapping(value = "/class/getUnChargedClassInfo")
	@ResponseBody
	public Map<String, Object> getUnChargedClassInfo(@RequestParam (required = false) String teacherId,@RequestParam (required = false) String type){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(teacherId) || StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教师编号或者课程类型不能为空");
        	map.put("result", null);
        	return map;
		}		
		return classService.getUnChargedClassInfo(teacherId, type);
	}
	
	//查询班级内的讲次（课程）列表
	@RequestMapping(value = "/course/getClassCoursesInfo")
	@ResponseBody
	public Map<String, Object> getClassCoursesInfo(@RequestParam (required = false) String classId,@RequestParam (required = false) String classIdTwo,@RequestParam (required = false) String type){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(classId) && StringUtils.isBlank(classIdTwo)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "班级编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		return courseService.getClassCoursesInfo(classId,classIdTwo,type);
	}
	
	//主班的id查所有的副班
	@RequestMapping(value = "/class/getAllClassTwoByClassId",method= RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllClassTwoByClassId(@RequestBody String[] classId){
		Map<String, Object> map = new HashMap<String,Object>();
		if(ArrayUtils.isEmpty(classId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "主班编号不能为空");
        	map.put("result", null);
        	return map;
		}
		return classService.getAllClassTwoByClassId(classId);
	}
	
	//老师查询自己未结课的班级内学生列表
	@RequestMapping(value = "/class/getUnChargedClassStudents")
	@ResponseBody
	public Map<String, Object> getUnChargedClassStudents(String type,String teacherId,String student){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(teacherId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "班级编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		return classService.getUnChargedClassStudents(teacherId,type,student);
	}
    
	/**
	 * 接口：同步更新考勤信息
	 * @throws Exception 
	 */
	@RequestMapping(value = "/student/chargeAttentanceInfo",method= RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> chargeAttentanceInfo(@RequestBody UpdateAttentanceInfoVo info) throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
	
		List<AttentanceInfoVo> infos = info.getInfos();
		
		if(infos==null ||infos.size() ==0){
	       	map.put("resultStatus", 400);
        	map.put("resultMessage", "参数不能为空");
        	map.put("result", null);
        	return map;
		}
//		if(StringUtil.isBlank(info.getType())){
//	       	map.put("resultStatus", 400);
//        	map.put("resultMessage", "参数不能为空");
//        	map.put("result", null);
//        	return map;
//		}
		//对应考勤信息
		//更新考勤表信息
		try {
			Map<String, Object> result = smallClassService.chargeAttentanceInfo(infos,info.getType(),info.getChargeUserId());
            return result;
		} catch (ApplicationException e) {			
		    map.put("resultStatus", 400);
	        map.put("resultMessage", e.getErrorMsg());
        	map.put("result", "fail");
        	return map;
		}
//		Map<String, Object> result = smallClassService.chargeAttentanceInfo(infos,info.getType(),info.getChargeUserId());
//        return result;
		
	}
	
	
	/**
	 * 接口：同步更新考勤信息
	 */
	@RequestMapping(value = "/student/updateAttentanceInfo",method= RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateAttentanceInfo(@RequestBody List<AttentanceInfoVo> params){
		Map<String, Object> map = new HashMap<String,Object>();
	
		if(params==null ||params.size() ==0){
	       	map.put("resultStatus", 400);
        	map.put("resultMessage", "参数不能为空");
        	map.put("result", null);
        	return map;
		}
		//对应考勤信息
		//更新考勤表信息
		try {
			smallClassService.updateAttentanceInfo(params);
	       	map.put("resultStatus", 200);
        	map.put("resultMessage", "更新成功");
        	map.put("result", "success");
        	return map;
		} catch (Exception e) {
			if(e.getMessage().equals("401")){
		       	map.put("resultStatus", 401);
	        	map.put("resultMessage", "数据有问题导致无法更新");
			}else{
		       	map.put("resultStatus", 400);
	        	map.put("resultMessage", "更新失败");
			}

        	map.put("result", "fail");
        	return map;
		}
		
	}

	@RequestMapping(value = "/student/getStudentInfoByClassIdAndPhone")
	@ResponseBody
	public Map<String, Object> getStudentInfoByClassIdAndPhone(String classId, String phone){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(classId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "主班编号不能为空");
			map.put("result", null);
			return map;
		}
		if(StringUtils.isBlank(phone)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "客户电话不能为空");
			map.put("result", null);
			return map;
		}
		return classService.getStudentInfoByClassIdAndPhone(classId, phone);
	}


     
	//传入多个课程编号 用英文逗号隔开  星火专用
	@RequestMapping(value = "/course/getTextBookInfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTextBookIdByCourseId(@RequestParam(required = false) String courseIds){
		if(StringUtils.isBlank(courseIds)){
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程编号不能为空");
			map.put("result", null);
			return map;
		}
		return courseService.getTextBookIdByCourseId(courseIds);
	}

	//传入多个课程编号 用英文逗号隔开  培优专用
	@RequestMapping(value = "/course/textBookInfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findTextBookIdByCourseId(@RequestParam(required = false) String courseIds){
		if(StringUtils.isBlank(courseIds)){
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程编号不能为空");
			map.put("result", null);
			return map;
		}
		return courseService.findTextBookIdByCourseId(courseIds);
	}
	
	//星火绑定教材页面 查询参数 基础数据 组织架构
	@RequestMapping(value = "/datadict/findOrgInfoForTextBook")
	@ResponseBody
	public Map<String, Object> findOrgInfoForTextBook(){ 
		return dataDictService.findOrgInfoForTextBook();
	}
	//星火绑定教材页面 查询参数 基础数据 组织架构
	@RequestMapping(value = "/datadict/findBaseForTextBook")
	@ResponseBody
	public Map<String, Object> findBaseForTextBook(){ 
		return dataDictService.findBaseForTextBook();
	}
	//星火绑定教材页面 查询参数 基础数据 组织架构
	@RequestMapping(value = "/datadict/findBaseRelateDataForTextBook")
	@ResponseBody
	public Map<String, Object> findBaseRelateDataForTextBook(BaseRelateDataVo baseRelateDataVo){ 
		return dataDictService.findBaseRelateDataForTextBook(baseRelateDataVo);
	}
	
	
	//根据教材id查询已绑定的班级数量(不分班级type，无论小班一对一等都需要查，支持批量)  但是目前只能支持小班教材的查询
	@RequestMapping(value = "/textBook/getClassCountByTextBookId",method= RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getClassCountByTextBookId(@RequestBody TextBookParamVo textBookParamVo ){
		Map<String, Object> map = new HashMap<String,Object>();
		if(ArrayUtils.isEmpty(textBookParamVo.getWareId())){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教材编号不能为空");
        	map.put("result", null);
        	return map;
		}
		return classService.getClassCountByTextBookId(textBookParamVo);
	}
	//根据教材id查询已绑定的未结课班级列表(按分公司id分组,不分班级type,分页)(目前只支持星火小班)
	@RequestMapping(value = "/textBook/getUnCompleteClassById",method= RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUnCompleteClassById(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,String wareId){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtil.isBlank(wareId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教材编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		
		return classService.getUnCompleteClassById(dataPackage,wareId);
	}
	
	//根据教材id和公司查询已绑定的未结课班级列表(不分班级type,分页)(目前只支持星火小班)
	@RequestMapping(value = "/textBook/getBranchUnCompleteClassById",method= RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getBranchUnCompleteClassById(@RequestParam(required = false,defaultValue="1") Integer pageNum,@RequestParam(required = false,defaultValue="20") Integer pageSize,String wareId,String branchId){
		Map<String, Object> map = new HashMap<String,Object>();
		if(pageNum==null || pageSize==null){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "pageNum或者pageSize不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtil.isBlank(wareId) || StringUtil.isBlank(branchId) ){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教材编号或者分公司Id不能为空");
        	map.put("result", null);
        	return map;
		}
		if(pageNum<=0)pageNum=1;
		DataPackage dataPackage = new DataPackage(pageNum,pageSize);		
		return classService.getBranchUnCompleteClassById(dataPackage,wareId,branchId);
	}
	
	
	//老师查询自己班级内学生列表  结课或者未结课的  支持班级类型 支持老师id
	//finish true 已结课  false 未结课
	@RequestMapping(value = "/class/getClassStudents")
	@ResponseBody
	public Map<String, Object> getClassStudents(String type,String teacherId,Boolean finish){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(teacherId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "班级编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		return classService.getClassStudents(type, teacherId, finish);
	}
	
	//查新生在一对一产品的第几节课，入参课程id，返回第几节课
	//跟进传入的课程id来定位学生 和科目 然后 只有定位到的所有课程都是为上课才是新生
	//一对一课程信息
	@RequestMapping(value = "/course/oneOnOneCourseInfo")
	@ResponseBody
	public Map<String, Object> getCourseInfo(@RequestParam(required = false) String courseId) {
		Map<String, Object> map = new HashMap<String, Object>();		
        if(StringUtils.isBlank(courseId)){
        	if(log.isDebugEnabled())
        		log.debug("课程编号不能为空");
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程编号不能为空");
        	map.put("result", null);
        	return map;
        }
        CourseSearchInputVo courseSearchInputVo = new CourseSearchInputVo();
        log.info("教学平台/course/courseInfo查询一对一课程id:"+courseId);
        courseSearchInputVo.setCourseId(courseId);      
		return courseService.getOneOnOneCourseInfo(courseSearchInputVo);

	}
	/**
	 * 
	 * @author: duanmenrun
	 * @Title: getClassStudentsListByStatus 
	 * @Description: TODO 老师查询班级内学生列表  duanmenrun 20171103 #1641-1
	 * @throws 
	 * @param type 课程类型，
	 * @param teacherId 教师编号
	 * @param student 学生名字(模糊查询)
	 * @param status 是否完成 finished  unfinished
	 * @return
	 */
	@RequestMapping(value = "/class/getClassStudentsListByStatus")
	@ResponseBody
	public Map<String, Object> getClassStudentsListByStatus(String type,String teacherId,String student,String status){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(teacherId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教师编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		return classService.getClassStudentsListByStatus(teacherId,type,student,status);
	}


	/**
	 * 老师查一对一的学生列表
	 * @param teacherId 教师编号
	 * @param student 学生名称（模糊查询）
	 * @param status 1对1学生状态
	 * @return
	 */
	@RequestMapping(value = "/student/getOneOnOneStudentsListByTeacherId")
	@ResponseBody
	public Map<String, Object> getOneOnOneStudentsListByTeacherId(String teacherId, String student, String status){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(teacherId)){
			map.put("resultStatus", 400);
			map.put("resultMessage", "教师编号不能为空");
			map.put("result", null);
			return map;
		}
		return classService.getOneOnOneStudentsListByTeacherId(teacherId, student, status);
	}
	
	/**
	 * 根据副班教师获取课程信息
	 * @author: duanmenrun
	 * @Title: getCourseDetailByTeacherTwo 
	 * @Description: TODO 
	 * @param type 课程类型，
	 * @param teacherTwoId 辅班教师编号
	 * @param status 1-未开课，2-开课中，3-已结课
	 * @return
	 */
	@RequestMapping(value = "/class/getCourseDetailByTeacherTwo")
	@ResponseBody
	public Map<String, Object> getCourseDetailByTeacherTwo(String type,String teacherTwoId,String status){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(teacherTwoId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "教师编号不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(type)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "课程类型不能为空");
        	map.put("result", null);
        	return map;
		}
		return classService.getCourseDetailByTeacherTwo(teacherTwoId,type,status);
	}
	/**
	 * 查询学生日期类所有课程
	 * @author: duanmenrun
	 * @Title: getStudentAllCourseByTime 
	 * @Description: TODO 
	 * @param start
	 * @param end
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/class/getStudentAllCourseByTime")
	@ResponseBody
	public Map<String, Object> getStudentAllCourseByTime(String start,String end,String studentId){
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtils.isBlank(start)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "开始时间不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(end)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "结束时间不能为空");
        	map.put("result", null);
        	return map;
		}
		if(StringUtils.isBlank(studentId)){
        	map.put("resultStatus", 400);
        	map.put("resultMessage", "学生id不能为空");
        	map.put("result", null);
        	return map;
		}
		return null;
		//return classService.getCourseDetailByTeacherTwo(teacherTwoId,type,status);
	}
}
