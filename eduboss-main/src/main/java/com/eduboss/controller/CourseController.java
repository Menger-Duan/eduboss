package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.Course;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AuditAutoRecogVo;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.CourseConflictVo;
import com.eduboss.domainVo.CourseExcelVo;
import com.eduboss.domainVo.CourseRequirementSearchResultVo;
import com.eduboss.domainVo.CourseSummarySearchResultVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.MiniClassCourseStudentRosterVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.MiniClassProductVo;
import com.eduboss.domainVo.MiniClassStudentVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.domainVo.MultiStudentProductSubjectVo;
import com.eduboss.domainVo.OneOnOneBatchAttendanceEditVo;
import com.eduboss.domainVo.OneOnOneBatchAttendanceExcelVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.domainVo.OtmCourseInfoExcelVo;
import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.domainVo.SmallClassExcelVo;
import com.eduboss.domainVo.SmallCourseInfoExcelVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TextBookBossVo;
import com.eduboss.domainVo.TodayCourseBillVo;
import com.eduboss.domainVo.TwoTeacherCourseInfoExcelVo;
import com.eduboss.domainVo.wechatVo.CourseForWechatVo;
import com.eduboss.dto.CourseEditVo;
import com.eduboss.dto.CourseRequirementEditVo;
import com.eduboss.dto.CourseRequirementSearchInputVo;
import com.eduboss.dto.CourseSearchInputVo;
import com.eduboss.dto.CourseSummarySearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.MiniClassRelationSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.MutilCourseVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.dto.TeacherSubjectRequestVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ContractService;
import com.eduboss.service.CourseConflictService;
import com.eduboss.service.CourseService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.StudentService;
import com.eduboss.service.TeacherSubjectService;
import com.eduboss.service.TeacherVersionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.CSVUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.RotateImage;
import com.eduboss.utils.StringUtil;




@Controller
@RequestMapping(value = "/CourseController")
public class CourseController {

	@Autowired
	private SmallClassService smallClassService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	
	@Autowired
	private CourseConflictService courseConflictService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	
	@Autowired
	private TeacherVersionService teacherVersionService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private DataDictService dataDictService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(CourseController.class);
	
	private final static ObjectMapper objectMapper = new ObjectMapper();


	/**
	 *
	 * 切换销售渠道
	 * @param miniClassIds
	 * @param campusSale
	 * @param onlineSale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changeMiniClassSaleType",method = RequestMethod.POST)
	@ResponseBody
	public Response changeMiniClassSaleType(@RequestParam String miniClassIds,int campusSale,int onlineSale,String remark) throws Exception{
		return smallClassService.changeMiniClassSaleType(miniClassIds,campusSale,onlineSale,remark);
	}

	/**
	 *
	 * 检验分公司是否存在
	 * @param campusId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkOnlineSaleBrench",method = RequestMethod.GET)
	@ResponseBody
	public Response checkOnlineSaleBrench(@RequestParam(value="campusId[]") String[] campusId){
		return smallClassService.checkOnlineSaleBrench(campusId);
	}


	/**
	 * 获取小班jqGrid
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getMiniClassList")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassList(@ModelAttribute MiniClass miniClass, @ModelAttribute GridRequest gridRequest,String miniClassTypeId, String branchLevel, String studentId) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = smallClassService.getMiniClassList(miniClass, dataPackage,miniClassTypeId, branchLevel, studentId);
		DataPackageForJqGrid dpfj =  new DataPackageForJqGrid(dataPackage);
		if(miniClass.getProduct() != null && StringUtils.isNotBlank(miniClass.getProduct().getId())){
			dpfj.setResultMessage(contractService.countContractByProductId(miniClass.getProduct().getId())+"");
		}
		return dpfj;
	}
	
	/**
	 * 根据id获取小班
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/findMiniClassById")
	@ResponseBody
	public MiniClassVo findMiniClassById(@RequestParam("miniClassId") String id) {
		return smallClassService.findMiniClassById(id);
	}

	/**
	 * 根据报班日期获取小班合同产品需要购买的课时
	 * @param startDate
	 * @param miniClassId
	 * @return
	 */
	@RequestMapping(value ="/findMiniClassWillBuyHour")
	@ResponseBody
	public BigDecimal findMiniClassWillBuyHour(@RequestParam("miniClassId") String id,@RequestParam("startDate") String startDate) {
		return smallClassService.findMiniClassWillBuyHour(id, startDate);
	}

	/**
	 * 删除和编辑小班信息
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/editMiniClass")
	@ResponseBody
	public Response editMiniClass(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClass miniClass, 
			String productIdArray) throws Exception{
		smallClassService.operationMiniClassRecord(gridRequest.getOper(), miniClass, productIdArray);
		return new Response();
	}
	
	/**
	 * 操作小班管理产品记录
	 * @param productIdArray
	 * @param miniClassId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editMiniClassProduct")
	@ResponseBody
	public Response editMiniClassProduct(@RequestParam("productIdArray") String productIdArray, @RequestParam("miniClassId") String miniClassId) throws Exception{
		smallClassService.operationMiniClassProduct(productIdArray, miniClassId);
		return new Response();
	}
	
	/**
	 * 增加单条小班管理产品记录
	 * @param productId
	 * @param miniClassId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addOneMiniClassProduct")
	@ResponseBody
	public Response addOneMiniClassProduct(@RequestParam("productId") String productId, @RequestParam("miniClassId") String miniClassId) throws Exception{
		return smallClassService.addOneMiniClassProduct(productId, miniClassId);
	}
	
	/**
	 * 获取小班管理产品记录
	 * @param miniClassId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getMiniClassProducts")
	@ResponseBody
	public List<MiniClassProductVo> getMiniClassProducts(@RequestParam("miniClassId") String miniClassId) throws Exception{
		return smallClassService.getMiniClassProductList(miniClassId);
	}
	
	/**
	 * 获取符合产品，小班的报名学生
	 * @param miniClassId
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getStudentsByMiniClassProduct")
	@ResponseBody
	public List<MiniClassStudentVo> getStudentsByMiniClassProduct(@RequestParam("miniClassId") String miniClassId, 
			@RequestParam("productId") String productId) throws Exception{
		return smallClassService.getStudentsByMiniClassProduct(miniClassId, productId);
	}
	
	/**
	 * 删除小班产品关联记录
	 * @param miniClassId
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteMiniClassProduct")
	@ResponseBody
	public Response deleteMiniClassProduct(@RequestParam("miniClassId") String miniClassId, 
			@RequestParam("productId") String productId) throws Exception{
		Response response=new Response();
		smallClassService.deleteMiniClassProduct(miniClassId, productId);
		return response;
	}
	
	/**
	 * AutoCompelete 查询小班列表
	 * 
	 * @param gridRequest
	 * @param courseRequirementEditVo
	 * @return
	 */
	@RequestMapping(value="/getAutoCompeleteMiniClass")
	@ResponseBody
	public String getAutoCompeleteMiniClass(@RequestParam("term") String idOrName){
		String autoCompeleteData = "";
		
		List<MiniClass> list = smallClassService.getMiniClassByIdOrName(idOrName);
		
		for (MiniClass miniClass : list) {
			autoCompeleteData += "\""+miniClass.getName()+"-"+miniClass.getMiniClassId()+"\","; 
		}
		if (autoCompeleteData.length() > 0) {
			autoCompeleteData = autoCompeleteData.substring(0, autoCompeleteData.length() - 1);
		}
		autoCompeleteData = "["+autoCompeleteData+"]";
		
		return autoCompeleteData;
	}
	
	/**
	 * 小班报名 ：根据id或名称查询学生
	 * 
	 * @param gridRequest
	 * @param courseRequirementEditVo
	 * @return
	 */
	@RequestMapping(value="/getStudentForEnrollMiniClasss")
	@ResponseBody
	public String getStudentForEnrollMiniClasss(@RequestParam("term") String idOrName){
		String autoCompeleteData = "";
		
		List<StudentVo> list = smallClassService.getStudentForEnrollMiniClasss(idOrName);
		
		for (StudentVo vo : list) {
			autoCompeleteData += "\""+vo.getName()+"-"+vo.getId()+"\","; 
		}
		if (autoCompeleteData.length() > 0) {
			autoCompeleteData = autoCompeleteData.substring(0, autoCompeleteData.length() - 1);
		}
		autoCompeleteData = "["+autoCompeleteData+"]";
		
		return autoCompeleteData;
	}
	
	/**
	 * 检测学生id是否存在，是否已报读该小班
	 * @param studentId
	 * @param smallClassId
	 * @return
	 */
	@RequestMapping(value="/checkMiniClassStudent")
	@ResponseBody
	public Response checkMiniClassStudent(@RequestParam String studentId, @RequestParam String smallClassId){
		return smallClassService.checkMiniClassStudent(studentId,smallClassId);
	}
	
	/**
	 * 129 查看小班已报读学生
	 * 
	 * @param smallClassId 小班id
	 * @param List<StudentVo>
	 * @return
	 */
	@RequestMapping(value="/getMiniClasssStudentBySmallClassId")
	@ResponseBody
	public DataPackageForJqGrid getMiniClasssStudentBySmallClassId(@RequestParam String smallClassId){
		List<StudentVo> list=smallClassService.getStudentAlreadyEnrollMiniClasss(smallClassId);
		DataPackage dataPackage = new DataPackage(0,list.size());
		dataPackage.setDatas(list);
		return  new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 129 查看小班已报读学生
	 * 
	 * @param smallClassId 小班id
	 * @param List<StudentVo>
	 * @return
	 */
	@RequestMapping(value="/getStudentAlreadyEnrollMiniClasss")
	@ResponseBody
	public List<StudentVo> getStudentAlreadyEnrollMiniClasss(@RequestParam String smallClassId){
		return smallClassService.getStudentAlreadyEnrollMiniClasss(smallClassId);
	}
	
	
	@RequestMapping(value="/checkAllowAddStudent4MiniClass")
	@ResponseBody
	public Response checkAllowAddStudent4MiniClass(@RequestParam String studentIds, @RequestParam String smallClassId){
		return smallClassService.checkAllowAddStudent4MiniClass(studentIds, smallClassId);
	}
	
	
	/**
	 * 130 批量添加小班学生
	 * @param studentIds  学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value="/AddStudentForMiniClasss")
	@ResponseBody
	public Response AddStudentForMiniClasss(@RequestParam String studentIds, @RequestParam String smallClassId,@RequestParam String contractId,@RequestParam String contractProductId,String firstSchoolTime) throws Exception{
		Response response=new Response();
		smallClassService.AddStudentForMiniClasss(studentIds, smallClassId, contractId, contractProductId, firstSchoolTime, true);
		return response;
	}
	
	
	/**
	 * 131批量删除小班学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteStudentInMiniClasss")
	@ResponseBody
	public Response deleteStudentInMiniClasss(@RequestParam String studentIds, @RequestParam String smallClassId) throws Exception{
		Response response=new Response();
		smallClassService.deleteStudentInMiniClasss(studentIds, smallClassId);
		return response;
	}
	
	/**
	 * 获取有考勤记录的学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getMiniClassAttendedStudent")
	@ResponseBody
	public List<NameValue> getMiniClassAttendedStudent(@RequestParam String studentIds, @RequestParam String smallClassId) throws Exception{
		List<Student> students =  smallClassService.getMiniClassAttendedStudent(studentIds, smallClassId);
        List<NameValue> nvs = new ArrayList<NameValue>();
		for (Student student : students) {
			nvs.add(SelectOptionResponse.buildNameValue(student.getName(), student.getId()));
		}
		return nvs;
	}
	
	/**
	 * 调整小班学生
	 * @param oldSmallClass
	 * @param newSmallClass
	 * @param studentIds
	 * @return
	 */
	@RequestMapping(value="/adjustMiniClassStudent")
	@ResponseBody
	public Response adjustMiniClassStudent(@RequestParam String oldSmallClassId,@RequestParam String newSmallClassId,@RequestParam String studentIds){
		Response response=new Response();
		smallClassService.adjustMiniClassStudent(oldSmallClassId,newSmallClassId,studentIds);
		return response;
	}
	
	/**
	 * 132根据小班id查看合同拟报读学生id列表
	 * 
	 * @param smallClassId 小班id
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/getStudentWantListBySmallClassId")
	@ResponseBody
	public List<StudentVo> getStudentWantListBySmallClassId(@RequestParam String smallClassId){
		return smallClassService.getStudentWantListBySmallClassId(smallClassId);
	}
	
	/**
	 * 新逻辑，小班关联多产品
	 * 根据小班id查看合同拟报读学生列表
	 * 
	 * @param miniClassId 小班id
	 * @return
	 */
	@RequestMapping(value="/getStudentWantListByMiniClassId")
	@ResponseBody
	public List<StudentVo> getStudentWantListByMiniClassId(@RequestParam String miniClassId){
		return smallClassService.getStudentWantListByMiniClassId(miniClassId);
	}
	
	/**
	 * 小班学生预扣费
	 * @param miniClassName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/miniClassStudentPay")
	@ResponseBody
	public Response miniClassStudentPay(@ModelAttribute GridRequest gridRequest, @RequestParam String miniClassId, @RequestParam String stuIds) throws Exception {
		smallClassService.miniClassStudentPreCharge(miniClassId, stuIds);
		return new Response();
	}

	/**
     * 复制课程
     * @param origDate 源日期
     * @param destDate 目标日期
     * @return
     */
    @RequestMapping(value ="/copyCourse")
    @ResponseBody
    public Response copyCourse(@RequestParam String origDate,@RequestParam String destDate, String transactionUuid){
        courseService.copyCourse(origDate, destDate, transactionUuid);
        return new Response();
    }

    @RequestMapping(value = "/checkCopyCourse")
	@ResponseBody
    public Map<String, String> checkCopyCourse(String origDate, String destDate, String courseIds){
		Map<String, String> map =null;
		if (StringUtil.isNotBlank(courseIds)){
			map = courseService.checkCopyMultiCourse(courseIds, destDate);
		}else {
			map = courseService.checkCopyCourse(origDate, destDate);
		}
		return map;
	}
    
    /**
	 * 批量复制课程
	 */
	@RequestMapping(value="/copyMutilCourse")
	@ResponseBody
	public Response copyMutilCourse(@RequestParam String courseIds, @RequestParam String destDate, String transactionUuid) throws Exception{
		courseService.copyMultiCourse(courseIds, destDate, transactionUuid);
		return new Response();
	}
	
	/**
	 * 小班课程列表
	 * @param miniClassName
	 * @return
	 */
	@RequestMapping(value ="/getMiniClassCourseList")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassCourseList(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClassCourseVo miniClassCourseVo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = smallClassService.getMiniClassCourseList(miniClassCourseVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 根据id获取小班课程
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/findMiniClassCourseById")
	@ResponseBody
	public MiniClassCourseVo findMiniClassCourseById(@RequestParam String id) {
		return smallClassService.findMiniClassCourseById(id);
	}
	
	/**
	 * 修改小班课程信息
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/modifyMiniClassCourse")
	@ResponseBody
	public Response modifyMiniClassCourse(@ModelAttribute MiniClassCourseVo miniClassCourseVo) throws Exception{
		smallClassService.saveOrUpdateMiniClassCourse(miniClassCourseVo);
		return new Response();
	}

	/**
	 * 修改小班学生考勤信息（不扣费）
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changeMccStudentAttendance")
	@ResponseBody
	public Response changeMccStudentAttendance(@RequestParam String miniClassCourseId, @RequestParam String attendanceData) throws Exception{
		return smallClassService.changeMccStudentAttendance(miniClassCourseId, attendanceData);
	}


	/**
	 * 更新小班学生考勤信息
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/modifyMiniClassCourseStudentAttendance",method = RequestMethod.POST)
	@ResponseBody
	public Response modifyMiniClassCourseStudentAttendance(@RequestParam String miniClassCourseId, @RequestParam String attendanceData, @RequestParam String oprationCode) throws Exception{
		return smallClassService.modifyMiniClassCourseStudentAttendance(miniClassCourseId, attendanceData, oprationCode);
	}
	
	/**
	 * 更新小班学生补课时间
	 * @param miniClassCourseId
	 * @param studentId
	 * @param supplementDate
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/modifyMiniClassCourseStudentSupplement")
	@ResponseBody
	public Response modifyMiniClassCourseStudentSupplement(@RequestParam String miniClassCourseId, @RequestParam String studentId, @RequestParam String supplementDate) throws Exception{
		smallClassService.modifyMiniClassCourseStudentSupplement(miniClassCourseId, studentId, supplementDate);
		return new Response();
	}
	
	/**
	 * 更新小班学生缺勤备注
	 * @param miniClassCourseId
	 * @param studentId
	 * @param absentRemark
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/modifyMiniClassCourseAbsentRemark")
	@ResponseBody
	public Response modifyMiniClassCourseAbsentRemark(@RequestParam String miniClassCourseId, @RequestParam String studentId, @RequestParam String absentRemark) throws Exception{
		smallClassService.modifyMiniClassCourseAbsentRemark(miniClassCourseId, studentId, absentRemark);
		return new Response();
	}
	
	/**
	 * 获取小班学生考勤列表
	 * @param miniClassName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getMiniClassStudentAttendentList")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassStudentAttendentList(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClassStudentVo miniClassStudentVo) throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = smallClassService.getMiniClassStudentAttendentList(miniClassStudentVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 根据小班id查询小班情况
	 * @param miniClassId
	 * @return
	 */
	@RequestMapping(value="/getMiniClassInfo")
	@ResponseBody
	public MiniClassVo getMiniClassInfo(String miniClassId) {
		return smallClassService.findMiniClassById(miniClassId);
	}

    /**
     * 本校区课程表
     * @param courseSearchInputVo
     * @param gridRequest
     * @return
     */
    @RequestMapping(value ="/getCourseListByCriterion")
    @ResponseBody
    public DataPackageForJqGrid getCourseListByCriterion(CourseSearchInputVo courseSearchInputVo,GridRequest gridRequest){
        DataPackage dp = new DataPackage(gridRequest);
        return new DataPackageForJqGrid(courseService.getCourseList(courseSearchInputVo,dp));
    }

	/**
	 * 本校区课程表
	 * @param courseSearchVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getCourseList")
	@ResponseBody
	public DataPackageForJqGrid getCourseList(@ModelAttribute CourseSearchInputVo courseSearchVo, @ModelAttribute GridRequest gridRequest) {
		
		DataPackage dp = new DataPackage(gridRequest);
		//屏蔽以前方法
		//dp = courseService.getSchoolZoneCourseList2(courseSearchVo, dp);
		dp = courseService.findPageCourseForOneWeek(courseSearchVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 大课表
	 * @param courseSummarySearchInputVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getCourseSummaryList")
	@ResponseBody
	public DataPackageForJqGrid getCourseSummaryList(@ModelAttribute CourseSummarySearchInputVo courseSummarySearchInputVo, @ModelAttribute GridRequest gridRequest) {
		
		DataPackage dp = new DataPackage(gridRequest);
		dp = courseService.getCourseSummaryList(courseSummarySearchInputVo, dp);
		return new DataPackageForJqGrid(dp);
	}

    /**
     * 删除课程预览
     * @param id
     * @return
     */
    @RequestMapping(value ="/deleteCourseSummary")
    @ResponseBody
    public Response deleteCourseSummary(@RequestParam String id){
        courseService.deleteCourseSummary(id);
        return new Response();
    }
	
	/**
	 * 学管排课需求
	 * @param courseRequirementSearchInputVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getCourseRequirementList")
	@ResponseBody
	public DataPackageForJqGrid getCourseRequirementList(
			@ModelAttribute CourseRequirementSearchInputVo courseRequirementSearchInputVo,
			@ModelAttribute GridRequest gridRequest) {
		
		DataPackage dp = new DataPackage(gridRequest);
		dp = courseService.getCourseRequirementList(courseRequirementSearchInputVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 查找学生列表（自动搜索）
	 * @param term
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStudentAutoComplate", method =  RequestMethod.GET)
	@ResponseBody
	public List<Map<Object,Object>> getStudentAutoComplate(@RequestParam String term) throws Exception {
		if(term.indexOf("'") < 0 ) {
			return studentService.getStudentAutoComplate(term);
		} else {
			return new ArrayList<Map<Object,Object>>(0);
		}
	}
	
	/**
	 * 查找某一排课需求
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/findCourseRequirementById")
	@ResponseBody
	public CourseRequirementEditVo findCourseRequirementById(@RequestParam("id") String id) {
		CourseRequirementEditVo courseRequirementEditVo = courseService.findCourseRequirementById(id);
		return courseRequirementEditVo;
	}
	
	@RequestMapping(value ="/findCourseRequirement")
	@ResponseBody
	public List<CourseRequirementSearchResultVo> findCourseRequirement(CourseRequirementSearchInputVo searchVo) {
		return (List<CourseRequirementSearchResultVo>)courseService.getCourseRequirementList(searchVo, new DataPackage(0, 999)).getDatas();
	}
	
	
	/**
	 * 编辑排课需求
	 * 
	 * @param gridRequest
	 * @param courseRequirementEditVo
	 * @return
	 */
	@RequestMapping(value="/editCourseRequirement")
	@ResponseBody
	public Response editCourseRequirement(@ModelAttribute GridRequest gridRequest, @ModelAttribute CourseRequirementEditVo courseRequirementEditVo){

		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			courseService.deleteCourseRequirement(courseRequirementEditVo);
		} else {
			courseService.saveOrUpdateCourseRequirement(courseRequirementEditVo); 
		}
		return new Response();
	}
	
	
	
	/**
	 * 获取要批量更改课程的列表
	 * @param courseRequirementSearchInputVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value = "/getCourseChangesList")
	@ResponseBody
	public DataPackageForJqGrid getCourseChangesList(
			@ModelAttribute CourseSearchInputVo courseSearchInputVo,
			@ModelAttribute GridRequest gridRequest) {
		
		DataPackage dp = new DataPackage(gridRequest);
		dp = courseService.getCourseChangesList(courseSearchInputVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 批量修改课程属性
	 * @param changesAttr
	 * @param changesData
	 * @param ids
	 * @param courseChangesSearchInputVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/courseAttrChanges")
	@ResponseBody
	public Response courseAttrChanges(@RequestParam String changesAttr,@RequestParam String changesData,@RequestParam String ids, @ModelAttribute CourseSearchInputVo courseSearchInputVo ) throws Exception {
		
		courseService.courseAttrChanges(changesAttr, changesData, ids, courseSearchInputVo);
		return new Response();
	}
	
	/**
	 * 批量修改课程的老师
	 * @param teacherUserId
	 * @param ids
	 * @param courseSearchInputVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchChangeTeacher")
	@ResponseBody
	public Response batchChangeTeacher(@RequestParam String teacherUserId,@RequestParam String ids, @ModelAttribute CourseSearchInputVo courseSearchInputVo ) {
		courseService.batchChangeTeacher(teacherUserId, ids);
		return new Response();
	}
	
	/**
	 * @author tangyuping
	 * 批量修改课程的课程年级	
	 */
	@RequestMapping(value="/batchChangeCourseGrade")
	@ResponseBody
	public Response batchChangeCourseGrade(@RequestParam String gradeId,@RequestParam String ids ) {
		courseService.batchChangeCourseGrade(gradeId, ids);
		return new Response();
	}
	
	/**
	 * 获取当前校区下指定科目和年级的老师
	 * @param request
	 * @param gradeId
	 * @param subjectId
	 * @return
	 */
	@RequestMapping(value = "/getTeacherSubjectByGradeSubjectSelection", method =  RequestMethod.POST)
	@ResponseBody
	public SelectOptionResponse getTeacherSubjectByGradeSubjectSelection(HttpServletRequest request, @RequestBody  TeacherSubjectRequestVo tsvo) {
		
		SelectOptionResponse selectOptionResponse = null;
        List<User> users = null;
		try {
			users = teacherSubjectService.getOtherOrganizationTeacherSubjectByGradesSubjects(tsvo.getTeachersrv());
			users.addAll(teacherSubjectService.getTeacherSubjectByGradesSubjects(tsvo.getTeachersrv()));
			List<NameValue> nvs = new ArrayList<NameValue>();
			if(users!=null){
				nvs.addAll(users);
			}
			selectOptionResponse = new SelectOptionResponse(nvs);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return selectOptionResponse;
	}
	
	/**
	 * 查找某一课程
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/findCourseById")
	@ResponseBody
	public CourseVo findCourseById(@RequestParam("id") String id) {
		CourseVo courseVo = courseService.findCourseById(id);
		return courseVo;
	}
	
	/**
	 * 编辑某一课程
	 * 
	 * @param gridRequest
	 * @param courseEditVo
	 * @return
	 */
	@RequestMapping(value="/editCourse")
	@ResponseBody
	public Response editCourse(@ModelAttribute GridRequest gridRequest, @ModelAttribute CourseEditVo courseEditVo){
        if(!userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)){
            throw new ApplicationException(ErrorCode.USER_AUTHROTY_FAIL);
        }
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			courseService.deleteCourse(courseEditVo);
		} else {
			courseService.saveOrUpdateCourse(courseEditVo); 
		}
		return new Response();
	}
	
	@RequestMapping(value="/modifyCourse")
	@ResponseBody
	public Response modifyCourse(@ModelAttribute GridRequest gridRequest, @ModelAttribute CourseEditVo courseEditVo) {
        if(!userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)){
            throw new ApplicationException(ErrorCode.USER_AUTHROTY_FAIL);
        }
		courseService.modifyCourse(courseEditVo);
		return new Response();
	}
	
	/**
	 * 老师课程表
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getTeacherCourseScheduleList")
	@ResponseBody
	public DataPackage getTeacherCourseScheduleList(String teacherId, Date start, Date end, @RequestParam(required = false) Boolean isAllCourseStatus) throws Exception {
		return courseService.getTeacherCourseScheduleList(teacherId, start, end, isAllCourseStatus);
	}
	
	/**
	 * 获取某课程时间段的老师课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	@RequestMapping(value="getTeacherCourseScheduleListByCourseTime")
	@ResponseBody
	public DataPackage getTeacherCourseScheduleListByCourseTime(String teacherId, Date start, Date end, String courseStartTime, String courseEndTime) throws Exception {
		return courseService.getTeacherCourseScheduleListByCourseTime(teacherId, start, end, courseStartTime, courseEndTime);
	}
	
	/**
	 * 老师小班课程表
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getTeacherMiniClassCourseScheduleList")
	@ResponseBody
	public DataPackage getTeacherMiniClassCourseScheduleList(String teacherId, Date start, Date end) throws Exception {
		return courseService.getTeacherMiniClassCourseScheduleList(teacherId, start, end);
	}
	
	/**
	 * 获取某课程时间段的老师小班课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	@RequestMapping(value="getTeacherMiniClassCourseScheduleListByCourseTime")
	@ResponseBody
	public DataPackage getTeacherMiniClassCourseScheduleListByCourseTime(String teacherId, Date start, Date end, String courseStartTime, String courseEndTime) throws Exception {
		return courseService.getTeacherMiniClassCourseScheduleListByCourseTime(teacherId, start, end, courseStartTime, courseEndTime);
	}
	
	/**
	 * 学生课程表
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getStudentCourseScheduleList")
	@ResponseBody
	public DataPackage getStudentCourseScheduleList(@RequestParam String studentId,  Date start,  Date end) throws Exception {
		return courseService.getStudentCourseScheduleList(studentId, start, end);
	}
	
	
	/**
	 * 查询老师某一天的学生课程表
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getStudentCourseScheduleListByTeacher")
	@ResponseBody
	public DataPackage getStudentCourseScheduleListByTeacher(@RequestParam String teacherId, @RequestParam String courseDate) throws Exception {
		return courseService.getStudentCourseScheduleListByTeacher(teacherId, courseDate);
	}
	
	/**
	 * 学生小班课程表
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getStudentMiniClassCourseScheduleList")
	@ResponseBody
	public DataPackage getStudentMiniClassCourseScheduleList(@RequestParam String studentId, @RequestParam Date start, @RequestParam Date end) throws Exception {
		return courseService.getStudentMiniClassCourseScheduleList(studentId, start, end);
	}
	
	/**
	 * 查询某一排课需求的所有已排课程
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/getCourseRequirementArrengedCourseList")
	@ResponseBody
	public DataPackage getCourseRequirementArrengedCourseList(String courseRequirementId, String startDate, String endDate) throws Exception {
		
		return courseService.getCourseRequirementArrengedCourseList(courseRequirementId, startDate, endDate, new DataPackage(0, 999));
	}
	
	/**
	 * 获取一对一批量考勤列表
	 * @param courseSearchVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getOneOnOneBatchAttendanceList")
	@ResponseBody
	public DataPackageForJqGrid getOneOnOneBatchAttendanceList(@ModelAttribute CourseVo courseVo, @ModelAttribute GridRequest gridRequest) {
		
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = courseService.getOneOnOneBatchAttendanceList(courseVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}

	/**
	 *
	 * @param courseVo
	 * @return
	 */
	@RequestMapping(value ="/getOneOnOneAutoRecogCount")
	@ResponseBody
	public Response getOneOnOneAutoRecogCount(@ModelAttribute CourseVo courseVo){
		Response response = new Response();
		int count = courseService.getOneOnOneAutoRecogCount(courseVo);
		response.setData(count);
		response.setResultMessage("自动识别课时单总数");
		return response;
	}

    @RequestMapping(value ="/getOneOnOneAuditSummary")
    @ResponseBody
	public Response getOneOnOneAuditSummary(@ModelAttribute CourseVo courseVo){
        Response response = new Response();
        Map map = courseService.getOneOnOneAuditSummary(courseVo);
        response.setData(map);
        response.setResultMessage("一对一课程审批概况");
        return response;
    }
	
	/**
	 * 获取一对一批量考勤列表到excel
	 * @param courseSearchVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getOneOnOneAttendanceListToExcel")
	@ResponseBody
	public void getOneOnOneAttendanceListToExcel(@ModelAttribute CourseVo courseVo, @ModelAttribute GridRequest gridRequest, HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		dataPackage = courseService.getOneOnOneBatchAttendanceList(courseVo, dataPackage);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<OneOnOneBatchAttendanceExcelVo> exporter = new ExportExcel<OneOnOneBatchAttendanceExcelVo>();
		String[] hearders = new String[] {"课程编号", "报读产品", "上课日期", "课程校区", "学管师" ,"老师", "学生", "年级", "科目", "课时时长", "课程时长", "计划课时", "实际课时", "学生剩余课时", "学管确认", "课程状态"
};//表头数组
		try(OutputStream out = response.getOutputStream()){
			Set<OneOnOneBatchAttendanceExcelVo> datas = HibernateUtils.voCollectionMapping(dataPackage.getDatas(), OneOnOneBatchAttendanceExcelVo.class, "courseToExcelmapper");
			//把课程状态 - courseStatus 一列enum转化为中文string
//			for(OneOnOneBatchAttendanceExcelVo oneOnOneBatchAttendanceExcelVo: datas){
//				oneOnOneBatchAttendanceExcelVo
//			}
			exporter.exportExcel(hearders, datas, out);
		}
	}
	
	/**
	 * 一对一批量考勤操作
	 * 
	 * @param gridRequest
	 * @param courseEditVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/oneOnOneBatchAttendanceEdit")
	@ResponseBody
	public Response oneOnOneBatchAttendanceEdit(@ModelAttribute OneOnOneBatchAttendanceEditVo oneOnOneBatchAttendanceEditVo) throws Exception{
		Response res=new Response();
		try{
			courseService.oneOnOneBatchAttendanceEdit(oneOnOneBatchAttendanceEditVo);
		}catch(StaleObjectStateException e){
			res.setResultCode(1);
			res.setResultMessage("有课程已经被修改，请刷新后再考勤！");
			return res;
		}
		return new Response();
	}
	
	/**
	 * 批量考勤提交
	 * @return
	 */
	@RequestMapping("/mutilAttendaceSubmit")
	@ResponseBody
	public Response mutilAttendaceSubmit(@ModelAttribute OneOnOneBatchAttendanceEditVo oneOnOneBatchAttendanceEditVo) throws Exception{
		Response res=new Response();
		try{
			courseService.mutilAttendaceSubmit(oneOnOneBatchAttendanceEditVo.getVos());
		}catch(StaleObjectStateException e){
			res.setResultCode(1);
			res.setResultMessage("有课程已经被修改，请刷新后再考勤！");
			return res;
		}
		return res;
	}
	
	/**
	 * 一对一审核提交
	 */
	@RequestMapping("/oneOnOneAuditSubmit")
	@ResponseBody
	public Response oneOnOneAuditSubmit(@RequestParam String courseId, @RequestParam String auditStatus) throws Exception{
		courseService.oneOnOneAuditSubmit(courseId, auditStatus);
		return new Response();
	}
	
	
	/**
	 * 批量删除课程
	 */
	@RequestMapping(value="/deleteMultiCourse")
	@ResponseBody
	public Response deleteMultiCourse(@RequestParam String courseIds) throws Exception{
		Response response=new Response();
		courseService.deleteMultiCourse(courseIds);
		return response;
	}

    /**
     * 批量重置考勤状态
     */
    @RequestMapping(value="/resetCourseAttendances")
    @ResponseBody
    public Response resetCourseAttendances(@RequestParam String courseIds) throws Exception{
        Response response=new Response();
        courseService.deleteCourseAttendances(courseIds);
        return response;
    }

	/**
	 * 保存课程列表
	 * jsonCourseList 格式="[{\"courseId\":\"001\",\"courseDate\":\"2012-04-02\"},{\"courseId\":\"002\",\"courseDate\":\"2014-08-02\"}]";
	 * @param jsonCourseList
	 * @return
	 */
	@RequestMapping(value="/saveCourseList")
	@ResponseBody
	public Response saveCourseList(@RequestParam String courseListJsonStr, @ModelAttribute CourseSummarySearchResultVo courseSummaryVo) {
		Course[] courseList=null;
		try {
			courseList= objectMapper.readValue(courseListJsonStr, Course[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		
		courseService.saveCourseList(courseList, courseSummaryVo);
		
		return new Response();
	}
	
	@RequestMapping(value="/saveMutilCourseList")
	@ResponseBody
	public Response saveMutilCourseList(@RequestParam String weekDayCourseListJsonStr, @RequestParam String summaryListJsonStr, String transactionUuid) {
		MutilCourseVo[] mutilCourseVoList = null;
		CourseSummarySearchResultVo[] summaryList = null;
		try {
			mutilCourseVoList= objectMapper.readValue(weekDayCourseListJsonStr, MutilCourseVo[].class);
			summaryList = objectMapper.readValue(summaryListJsonStr, CourseSummarySearchResultVo[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		courseService.saveMutilCourseList(mutilCourseVoList, summaryList, transactionUuid);
		return new Response();
	}

	@RequestMapping(value = "/checkAheadOfRemainingHour")
	@ResponseBody
	public boolean checkAheadOfRemainingHour(String studentId, BigDecimal courseHours, String courseSummaryId, String courseId){
		boolean result = courseService.checkAheadOfOneOnOneRemainingHour(studentId, courseHours,courseSummaryId, courseId);
		return result;
	}
	
	
	/**
	 * 保存小班课程列表
	 * jsonCourseList 格式="[{\"courseId\":\"001\",\"courseDate\":\"2012-04-02\"},{\"courseId\":\"002\",\"courseDate\":\"2014-08-02\"}]";
	 * @param jsonCourseList
	 * @return
	 */
	@RequestMapping(value="/saveMiniCourseList")
	@ResponseBody
	public Response saveMiniCourseList(@RequestParam String courseListJsonStr,@RequestParam String arrangedHours) {
		MiniClassCourse[] courseList=null;
		try {
			courseList= objectMapper.readValue(courseListJsonStr, MiniClassCourse[].class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		
		courseService.saveMiniCourseList(courseList,arrangedHours);
		
		return new Response();
	}
	
	/**
	 * 提交审核课时结果
	 * @param courseId
	 * @param auditCourseHours
	 * @return
	 */
	@RequestMapping(value = "/submitCourseAudit")
	@ResponseBody
	public Response submitCourseAudit(String courseId, BigDecimal auditCourseHours) {
		courseService.submitCourseAudit(courseId, auditCourseHours, userService.getCurrentLoginUser());
		return new Response();
	}
	
	/**
	 * 保存列表中单个单元格修改的值
	 * @param courseEditVo
	 * @return
	 */
	@RequestMapping(value = "/courseCellEdit")
	@ResponseBody
	public Response courseCellEdit(CourseEditVo courseEditVo) {
		courseService.courseCellEdit(courseEditVo);
		return new Response();
	}
	
	/**
	 * 查找课程冲突表信息
	 * @param courseConflictVo
	 * @return
	 */
	@RequestMapping("/findCourseConflictInfos")
	@ResponseBody
	public DataPackage findCourseConflictInfos( @ModelAttribute CourseConflictVo courseConflictVo, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp = courseConflictService.findCourseConflictInfos(courseConflictVo,dp);
		return dp;
	}
	
	/**
	 * 根据条件查询冲突日期
	 * @param startDate
	 * @param endDate
	 * @param courseTime
	 * @param teacherId
	 * @param studentId
	 * @return
	 */
	@RequestMapping("/findConflictDateByCause")
	@ResponseBody
	public List<String> findConflictDateByCause(String startDate,
			String endDate, String courseTime, String teacherId,
			String studentId,String courseSummaryId){
		return courseConflictService.findConflictDateByCause(startDate, endDate, courseTime, teacherId, studentId, courseSummaryId);
	}
	
	/**
	 * 修改小班学生首次上课时间
	 * @param id
	 * @param firstSchoolTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateMiniClassStudentfirstSchoolTime")
	@ResponseBody
	public Response updateMiniClassStudentfirstSchoolTime(@RequestParam String studentId,@RequestParam String smallClassId,@RequestParam String firstSchoolTime) throws Exception{
		Response response=new Response();
		smallClassService.updateMiniClassStudentfirstSchoolTime(studentId,smallClassId,firstSchoolTime);
		return response;
	}
	
	@RequestMapping(value="/getMiniClassByContractProductId")
	@ResponseBody
	public MiniClassVo getMiniClassByContractProductId(@RequestParam String contractProductId){
		return smallClassService.getMiniClassByContractProductId(contractProductId);
	}
	
	
	/**
	 * 获取小班课程时间段内占用教室列表
	 * @param gridRequest
	 * @param courseRequirementEditVo
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getMiniClassCourseUseClassrooms")
	@ResponseBody
	public Map getMiniClassCourseUseClassrooms(
			@RequestParam("blCampusId") String blCampusId,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("classroomName") String classroomName,
			@RequestParam("miniClassName") String miniClassName) {
		String classrooms = smallClassService.getMiniClassCourseUseClassroomListString(blCampusId,startDate, endDate, classroomName, miniClassName);
		Map classroomMap = new HashMap();
		classroomMap.put("classrooms", classrooms);
		return classroomMap;
	}
	
	/**
	 * 获取小班教室的小班课程
	 * @param gridRequest
	 * @param courseRequirementEditVo
	 * @return
	 */
	@RequestMapping(value="/getClassroomMiniClassCourse")
	@ResponseBody
	public List getClassroomMiniClassCourse(
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("classroomName") String classroomName,
			@RequestParam("miniClassName") String miniClassName,String campusId,String miniClassTypeId){
		List list = smallClassService.getClassroomMiniClassCourse(startDate, endDate, classroomName, miniClassName,campusId,miniClassTypeId);
		return list;
	}
	
	/**
	 * 获取小班教室的小班课程
	 * 
	 * @param gridRequest
	 * @param courseRequirementEditVo
	 * @return
	 */
	@RequestMapping(value = "/getNotArrMiniClassRoomCourse")
	@ResponseBody
	public List getNotArrangementClassroomMiniClassCourse(
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			@RequestParam("classroomName") String classroomName,
			@RequestParam("miniClassName") String miniClassName) {
		List list = smallClassService.getNotArrangementClassroomMiniClassCourse(startDate,
				endDate, classroomName, miniClassName);
		return list;
	}
	
	/**
	 * 小班课程详情
	 * @param courseConflictVo
	 * @return
	 */
	@RequestMapping("/getMiniClassCourseDetail")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassCourseDetail( @RequestParam("miniClassId") String miniClassId, @ModelAttribute GridRequest gridRequest,ModelVo modelVo){
		DataPackage dp = new DataPackage(gridRequest);
		dp = smallClassService.getMiniClassCourseDetail(miniClassId, dp, modelVo);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 小班课时信息
	 * @param courseConflictVo
	 * @return
	 */
	@RequestMapping("/getMiniClassCourseTimeAnalyze")
	@ResponseBody
	public Map getMiniClassCourseTimeAnalyze( @RequestParam("miniClassId") String miniClassId){
		return smallClassService.getMiniClassCourseTimeAnalyze(miniClassId);
	}
	
	/**
	 * 根据学生获取还没结束的小班
	 * @param dp
	 * @param miniClassRelationSearchVo
	 * @return
	 */
    @RequestMapping(value ="/findUnconpeletedMiniClass")
    @ResponseBody
    public DataPackageForJqGrid findUnconpeletedMiniClass(@ModelAttribute MiniClassRelationSearchVo miniClassRelationSearchVo, @ModelAttribute GridRequest gridRequest) {
        DataPackage dp = new DataPackage(gridRequest);
        return new DataPackageForJqGrid(smallClassService.findUnconpeletedMiniClass(dp,miniClassRelationSearchVo));
    }
	
    /**
	 * 保存小班续班扩科关联记录
	 * @param miniClassRelationSearchVo
	 */
    @RequestMapping(value ="/saveMiniClassRelation")
    @ResponseBody
    public Response saveMiniClassRelation(@ModelAttribute MiniClassRelationSearchVo miniClassRelationSearchVo) {
        smallClassService.saveMiniClassRelation(miniClassRelationSearchVo);
        return new Response();
    }
    
	/**
	 * 小班详情-在读学生信息列表
	 * @param courseConflictVo
	 * @return
	 */
	@RequestMapping("/getMiniClassDetailStudentList")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassDetailStudentList( @RequestParam("miniClassId") String miniClassId, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp = smallClassService.getMiniClassDetailStudentList(miniClassId, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 小班详情-学生详情
	 * @param courseConflictVo
	 * @return
	 */
	@RequestMapping("/getMiniClassDetailStudentDetail")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassDetailStudentDetail( @RequestParam("miniClassId") String miniClassId, @RequestParam("studentId") String studentId, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp = smallClassService.getMiniClassDetailStudentDetail(miniClassId, studentId, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 小班详情-小班考勤扣费详情
	 * @param miniClassName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getMiniClassAttChargedDetail")
	@ResponseBody
	public DataPackageForJqGrid getMiniClassAttChargedDetail(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClassStudentVo miniClassStudentVo) throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = smallClassService.getMiniClassAttendanceChargedDetail(miniClassStudentVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 所有小班列表，用作Selection 
	 * @return
	 */
	@RequestMapping(value = "/getMiniClassList4Selection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getMiniClassList4Selection(String productId) {//OrganizationType orgType, String orgId
		List<MiniClass> list = smallClassService.getMiniClassList4Selection(productId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(MiniClass miniClass : list){
				String name =miniClass.getName();
				nvs.add(SelectOptionResponse.buildNameValue(name, miniClass.getMiniClassId()));
		}
		//nvs.add(SelectOptionResponse.buildNameValue("请选择",""));
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}


	/**
	 * 转班列表
	 * @return
	 */
	@RequestMapping(value = "/changeMiniClassSelectList", method =  RequestMethod.GET)
	@ResponseBody
	public List<MiniClassVo> changeMiniClassSelectList(String productId) {//OrganizationType orgType, String orgId
		return smallClassService.changeMiniClassSelectList(productId);
	}

	/**
	 * 转班列表
	 * @return
	 */
	@RequestMapping(value = "/findAllEnableMiniClassCourse", method =  RequestMethod.GET)
	@ResponseBody
	public List<MiniClassCourseVo> findAllEnableMiniClassCourse(String miniClassId) {
		return smallClassService.findAllEnableMiniClassCourse(miniClassId);
	}



	
	/**
	 * 小班详情-主界面信息
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getMiniClassDetailMainPageInfo")
	@ResponseBody
	public MiniClassVo getMiniClassDetailMainPageInfo(@RequestParam String miniClassId) {
		return smallClassService.getMiniClassDetailMainPageInfo(miniClassId);
	}
	
	/**
	 * 小班详情-主界面信息
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getMiniClassDetailAttChaPageInfo")
	@ResponseBody
	public MiniClassCourseVo getMiniClassDetailAttChaPageInfo(@RequestParam String miniClassId) {
		return smallClassService.getMiniClassDetailAttChaPageInfo(miniClassId);
	}
	
	/**
	 * 小班详情-小班学生剩余资金&剩余课时
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getMiniClassStuRemainFinAndHour")
	@ResponseBody
	public ContractProductVo getMiniClassStuRemainFinAndHour(@RequestParam String miniClassId, @RequestParam String studentId) {
		return smallClassService.getMiniClassStudentContractProduct(miniClassId, studentId);
	}
	
	/**
	 * 小班学生转班 + 设置第一次上课日期
	 * @param oldSmallClass
	 * @param newSmallClass
	 * @param studentIds
	 * @return
	 */
	@RequestMapping(value="/stuChgMiClassAndSetFistClassDate")
	@ResponseBody
	public Response stuChgMiClassAndSetFistClassDate(@RequestParam String oldSmallClassId,@RequestParam String newSmallClassId,@RequestParam String studentIds, @RequestParam String firstClassDate){
		Response response=new Response();
		smallClassService.stuChangeMiniClassAndSetFistClassDate(oldSmallClassId,newSmallClassId,studentIds,firstClassDate);
		return response;
	}

    /**
     * 根据学生id 查询课程概要
     *@param teacherId
     *@return
     */
    @RequestMapping(value="/getCourseSummaryByTeacherId")
    @ResponseBody
    public List<CourseSummarySearchResultVo> getCourseSummaryByTeacherId(@RequestParam String teacherId) {
        return courseService.getCourseSummaryByTeacherId(teacherId);
    }
	/**
	 * 删除小班课程
	 * @param miniClassCourseId
	 * @return
	 */
	@RequestMapping(value="/deleteMiniClassCourse")
	@ResponseBody
	public Response deleteMiniClassCourse(@RequestParam String miniClassCourseId){
		smallClassService.deleteMiniClassCourse(miniClassCourseId);
		return new Response();
	}
	
	/**
	 * 老师请假，取消请假
	 * @param courseId
	 * @param courseStatus
	 * @return
	 */
	@RequestMapping(value="/teacherAskForLeave")
	@ResponseBody
	public Response teacherAskForLeave(@RequestParam String courseId, @RequestParam String courseStatus) {
		courseService.teacherAskForLeave(courseId, courseStatus);
		return new Response();
	}
	
	@RequestMapping(value="/sendMessageByCourse")
	@ResponseBody
	public Response sendMessageByCourse(@RequestParam String courseId, @RequestParam String type) {
		courseService.sendMessageByCourse(courseId, type);
		return new Response();
	}
	
	
	 /**
	  * 导出一对一本周课表Excel
	  * @param gridRequest
	  * @param request
	  * @return
	 * @throws IOException 
	  */
	 @ResponseBody
	 @RequestMapping(value="/getWeekCourseToExcel")
	 public void getWeekCourseToExcel(@ModelAttribute CourseSearchInputVo courseSearchVo, @ModelAttribute GridRequest gridRequest,HttpServletRequest request,HttpServletResponse response) throws IOException{
		 
		String product = "";
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		Map<String, String> condtion = new HashMap<String, String>(); 
		if(StringUtil.isNotBlank(request.getParameter("productFinder"))){
			product = request.getParameter("productFinder");
		}
		condtion.put("product", product);
		dataPackage = courseService.findPageCourseForOneWeek(courseSearchVo, dataPackage);
//		
		ExportExcel<CourseExcelVo> ex = new ExportExcel<CourseExcelVo>();
		
		String[] hearders = new String[] {"编号","报读产品", "上课日期","校区","上课时间","学管师", "学生", "年级",  "科目", "上课星期", "计划课时", "实际课时", "老师","课程状态","冲突"};//表头数组
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		OutputStream out = response.getOutputStream();
		Set<CourseExcelVo> datas = HibernateUtils.voCollectionMapping(dataPackage.getDatas(), CourseExcelVo.class);
		ex.exportExcel(hearders, datas, out);
        
        out.close();
	 }
	 
	 
	 /**
	  * 当天课表
	  * @param gridRequest
	  * @param request
	  * @return
	 * @throws IOException 
	  */
	 @ResponseBody
	 @RequestMapping(value="/getTodayCourse")
	 public DataPackage getTodayCourse(@ModelAttribute CourseSearchInputVo courseSearchVo, @ModelAttribute GridRequest gridRequest) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = courseService.findPageCourseForToday(courseSearchVo, dataPackage);
        return  dataPackage;
	 }
	 
	 /**
	  * 旋转图片
	  * @param aliUrl 图片路径
	  * @param rate   角度
	  * @return
	 * @throws IOException 
	  */
	 @ResponseBody
	 @RequestMapping(value="/reventImage")
	 public Response reventImage(@RequestParam String aliUrl, @RequestParam int rate) throws IOException{
		try {
			RotateImage.reventImage(aliUrl, rate);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return new Response();
	 }
	 
	 /**
	 * 一对一课程审批与查看汇总
	 * @param courseSearchVo
	 * @param gridRequest
	 * @return
	 */
	 @RequestMapping(value ="/getOneOnOneAuditAnalyzeList")
	 @ResponseBody
	 public DataPackageForJqGrid getOneOnOneAuditAnalyzeList(@ModelAttribute CourseVo courseVo, @ModelAttribute GridRequest gridRequest,@RequestParam(required = false, defaultValue = "keshi") String anshazhesuan) {
		 DataPackage dataPackage = new DataPackage(gridRequest);
		 courseVo.setAnshazhesuan(anshazhesuan);
		 dataPackage = courseService.getOneOnOneAuditAnalyzeList(courseVo, dataPackage);
		 return new DataPackageForJqGrid(dataPackage);
	 }
	 
	 /**
	  * 获取未报班合同产品和人次
	  * @param campusId
	  * @param productId
	  * @param studentId
	  * @return
	  */
	 @RequestMapping(value ="/getMiniClassProductWithCount")
	 @ResponseBody
	 public List<Map<Object, Object>> getMiniClassProductWithCount(@ModelAttribute MiniClassProductSearchVo miniClassProductSearchVo) {
		 return smallClassService.getMiniClassProductWithCount(miniClassProductSearchVo);
	 }
	 
	 /**
	  * 获取未报读（或报读有小班ID）当前合同产品关联小班的学生列表
	  * @param campusId
	  * @param productId
	  * @param miniClassId
	  * @return
	  */
	 @RequestMapping(value ="/getStudentWithRemainingHours")
	 @ResponseBody
	 public List<Map<Object, Object>> getStudentWithRemainingHours(@RequestParam String campusId, @RequestParam String productId, String miniClassId) {
		 return smallClassService.getStudentWithRemainingHours(campusId, productId, miniClassId);
	 }
	 
	 /**
	  * 获取所选小班产品该校区关联的所有具体小班信息
	  * @param campusId
	  * @param productId
	  * @return
	  */
	 @RequestMapping(value ="/getApplyMiniClassList")
	 @ResponseBody
	 public List<MiniClassVo> getApplyMiniClassList(@RequestParam String campusId, @RequestParam String productId) {
		 return smallClassService.getApplyMiniClassList(campusId, productId);
	 }
	 
	 /**
	  * 获取未报读小班产品和剩余课时信息
	  * @param studentId
	  * @param gridRequest
	  * @return
	  */
	 @RequestMapping(value ="/getProductWithRemainingHours")
	 @ResponseBody
	 public DataPackageForJqGrid getProductWithRemainingHours(@RequestParam String studentId, @ModelAttribute GridRequest gridRequest) {
		 DataPackage dp = new DataPackage(gridRequest);
		 dp = smallClassService.getProductWithRemainingHours(studentId, dp);
		 return new DataPackageForJqGrid(dp);
	 }
	 
	 /**
	  * 获取已报读小班产品、剩余课时和小班信息
	  * @param studentId
	  * @param gridRequest
	  * @return
	  */
	 @RequestMapping(value ="/getProductWithMiniClass")
	 @ResponseBody
	 public DataPackageForJqGrid getProductWithMiniClass(@RequestParam String studentId, @ModelAttribute GridRequest gridRequest) {
		 DataPackage dp = new DataPackage(gridRequest);
		 dp = smallClassService.getProductWithMiniClass(studentId, dp);
		 return new DataPackageForJqGrid(dp);
	 }
	 
	 /**
	  * 刷选小班
	  * @param productChooseVo
	  * @param gridRequest
	  * @return
	  */
	 @RequestMapping(value ="/findMiniClassForChoose")
     @ResponseBody
     public DataPackageForJqGrid findMiniClassForChoose(@ModelAttribute ProductChooseVo productChooseVo, @ModelAttribute GridRequest gridRequest) {
         DataPackage dp = new DataPackage(gridRequest);
         RequestAttributes ra = RequestContextHolder.getRequestAttributes();
         HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
         productChooseVo.setExpressions(new ArrayList<String>());
         for(Object paramName : request.getParameterMap().keySet()){
             if(paramName.toString().startsWith("filter_") && StringUtils.isNotBlank(request.getParameter(paramName.toString()))){
                 productChooseVo.getExpressions().add(paramName.toString().substring("filter_".length()) + "=" + request.getParameter(paramName.toString()));
             }
         }
         return new DataPackageForJqGrid(smallClassService.findMiniClassForChoose(dp,productChooseVo));
     }
	 
	 	/**
		 * 小班课时汇总
		 */
		
		@RequestMapping(value="/miniClassCourseCollectList")
		@ResponseBody
		public DataPackageForJqGrid miniClassCourseCollectList(@ModelAttribute GridRequest gridRequest,
				String startDate,
				String endDate,
				String organizationIdFinder,String miniClassTypeId){
			DataPackage dataPackage=new DataPackage(gridRequest);
			
			dataPackage=courseService.MiniClassCourseCollectList(dataPackage, startDate, endDate, organizationIdFinder,miniClassTypeId);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		
		/**
		 * 小班点击已排班次查看课程详情
		 * @param gridRequest
		 * @param startDate
		 * @param endDate
		 * @param campusId
		 * @param miniClassCourseVo
		 * @return
		 */
		@RequestMapping(value="/getSmallClassCourseInfo")
		@ResponseBody
		public DataPackageForJqGrid getSmallClassCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate			
				,String campusId, MiniClassCourseVo miniClassCourseVo){			
			DataPackage dp=new DataPackage(gridRequest);			
			dp=courseService.getSmallClassCourseInfo(dp, startDate,  endDate, campusId, miniClassCourseVo);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
			return dataPackageForJqGrid;
		}
		
		/**
		 * 小班点击已排班次查看课程详情导出excel
		 * @param gridRequest
		 * @param startDate
		 * @param endDate
		 * @param campusId
		 * @param miniClassCourseVo
		 * @return
		 */
		@RequestMapping(value="/excelSmallClassCourseInfo")
		@ResponseBody
		public void excelSmallClassCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate			
				,String campusId, MiniClassCourseVo miniClassCourseVo, HttpServletRequest request, HttpServletResponse response) throws IOException{
			
			DataPackage dp=new DataPackage(gridRequest);			
			dp.setPageSize(gridRequest.getNumOfRecordsLimitation());
			dp=courseService.getSmallClassCourseInfo(dp, startDate,  endDate, campusId, miniClassCourseVo);	    	
					
			String filename = "小班查勤表.xls";
			String orgName="";
			if(StringUtils.isNotBlank(campusId)){
				Organization campus=organizationDao.findById(campusId);
				orgName=campus.getName();
				filename = campus.getName()+"小班查勤表.xls";
			}	
			if(StringUtil.isNotBlank(miniClassCourseVo.getCourseDate())){
	    		filename =orgName+ miniClassCourseVo.getCourseDate()+"小班查勤表.xls";
	    	}else if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
	    		filename =orgName+ startDate+"至"+endDate+"小班查勤表.xls";
	    	}
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			
			ExportExcel<SmallCourseInfoExcelVo> exporter = new ExportExcel<SmallCourseInfoExcelVo>();
			String[] hearders = new String[] {"上课日期","上课星期","上课时间","课时","小班名称","所属校区","教室","年级","科目","老师","班主任","应到人数","实际查勤","查勤人"};//表头数组
			try(OutputStream out = response.getOutputStream();){
				List<SmallCourseInfoExcelVo> datas=HibernateUtils.voListMapping((List)dp.getDatas(), SmallCourseInfoExcelVo.class);
				exporter.exportExcel(hearders, datas, out, 1);
			}
		}
		
		
		
		/**
		 * 双师点击已排班次查看课程详情
		 * @param gridRequest
		 * @param startDate
		 * @param endDate
		 * @param campusId
		 * @param miniClassCourseVo
		 * @return
		 */
		@RequestMapping(value="/getTwoTeacherClassCourseInfo")
		@ResponseBody
		public DataPackageForJqGrid getTwoTeacherClassCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate			
				,String campusId, MiniClassCourseVo miniClassCourseVo){			
			DataPackage dp=new DataPackage(gridRequest);			
			dp=courseService.getTwoTeacherClassCourseInfo(dp, startDate,  endDate, campusId, miniClassCourseVo);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
			return dataPackageForJqGrid;
		}
		
		/**
		 * 双师点击已排班次查看课程详情导出excel
		 * @param gridRequest
		 * @param startDate
		 * @param endDate
		 * @param campusId
		 * @param miniClassCourseVo
		 * @return
		 */
		@RequestMapping(value="/excelTwoTeacherClassCourseInfo")
		@ResponseBody
		public void excelTwoTeacherClassCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate			
				,String campusId, MiniClassCourseVo miniClassCourseVo, HttpServletRequest request, HttpServletResponse response) throws IOException{
			
			DataPackage dp=new DataPackage(gridRequest);			
			dp.setPageSize(gridRequest.getNumOfRecordsLimitation());
			dp=courseService.getTwoTeacherClassCourseInfo(dp, startDate,  endDate, campusId, miniClassCourseVo);    
			
			
			
					
			String filename = "双师辅班查勤表.xls";
			String orgName="";
			if(StringUtils.isNotBlank(campusId)){
				Organization campus=organizationDao.findById(campusId);
				orgName=campus.getName();
				filename = campus.getName()+"双师辅班查勤表.xls";
			}	
			if(StringUtil.isNotBlank(miniClassCourseVo.getCourseDate())){
	    		filename =orgName+ miniClassCourseVo.getCourseDate()+"双师辅班查勤表.xls";
	    	}else if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
	    		filename =orgName+ startDate+"至"+endDate+"双师辅班查勤表.xls";
	    	}
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			
			ExportExcel<TwoTeacherCourseInfoExcelVo> exporter = new ExportExcel<TwoTeacherCourseInfoExcelVo>();//,"实际查勤","查勤人"
			String[] hearders = new String[] {"上课日期","上课星期","上课时间","课时","双师辅班名称","所属校区","教室","年级","科目","主讲老师","辅导老师","应到人数"};//表头数组
			
			
			try(OutputStream out = response.getOutputStream();){
				List<TwoTeacherCourseInfoExcelVo> datas=HibernateUtils.voListMapping((List)dp.getDatas(), TwoTeacherCourseInfoExcelVo.class);
				exporter.exportExcel(hearders, datas, out, 0);
			}
		}
		
		
				
		/**
		 * 一对多课时汇总
		 */
		
		@RequestMapping(value="/getOtmClassCourseCollectList")
		@ResponseBody
		public DataPackageForJqGrid getOtmClassCourseCollectList(@ModelAttribute GridRequest gridRequest,
				String startDate,
				String endDate,
				String organizationIdFinder,String otmClassTypeId){
			DataPackage dataPackage=new DataPackage(gridRequest);
			
			dataPackage=courseService.getOtmClassCourseCollectList(dataPackage, startDate, endDate, organizationIdFinder,otmClassTypeId);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
		}
		
		/**
		 * 一对多点击已排班次查看课程详情
		 * @param gridRequest
		 * @param startDate
		 * @param endDate
		 * @param campusId
		 * @param miniClassCourseVo
		 * @return
		 */
		@RequestMapping(value="/getOtmCourseInfo")
		@ResponseBody
		public DataPackageForJqGrid getOtmCourseInfo(@ModelAttribute GridRequest gridRequest
				,String campusId, OtmClassCourseVo otmClassCourseVo){		
			DataPackage dp=new DataPackage(gridRequest);			
			dp=courseService.getOtmCourseInfo(dp, campusId, otmClassCourseVo);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
			return dataPackageForJqGrid;
		}
		
		/**
		 * 小班点击已排班次查看课程详情导出excel
		 * @param gridRequest
		 * @param startDate
		 * @param endDate
		 * @param campusId
		 * @param miniClassCourseVo
		 * @return
		 */
		@RequestMapping(value="/excelOtmCourseInfo")
		@ResponseBody
		public void excelOtmCourseInfo(@ModelAttribute GridRequest gridRequest, String startDate, String endDate			
				,String campusId, OtmClassCourseVo otmClassCourseVo, HttpServletRequest request, HttpServletResponse response) throws IOException{
			
			DataPackage dp=new DataPackage(gridRequest);			
			dp.setPageSize(gridRequest.getNumOfRecordsLimitation());
			dp=courseService.excelOtmCourseInfo(dp, campusId, otmClassCourseVo);

			String filename = "一对多查勤表.xls";
			String orgName="";
			if(StringUtils.isNotBlank(campusId)){
				Organization campus=organizationDao.findById(campusId);
				orgName=campus.getName();
				filename = campus.getName()+"一对多查勤表.xls";
			}			
			if(StringUtil.isNotBlank(otmClassCourseVo.getCourseDate())){
	    		filename = orgName+otmClassCourseVo.getCourseDate()+"一对多查勤表.xls";
	    	}else if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
	    		filename = orgName+startDate+"至"+endDate+"一对多查勤表.xls";
	    	}
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			
			ExportExcel<OtmCourseInfoExcelVo> exporter = new ExportExcel<OtmCourseInfoExcelVo>();			
			String[] hearders = new String[] {"上课日期","上课星期","上课时间","课时","一对多名称","所属校区","年级","科目","老师","学管师","应到人数","实际查勤","查勤人"};//表头数组
			try(OutputStream out = response.getOutputStream();){
				List<OtmCourseInfoExcelVo> datas = HibernateUtils.voListMapping((List)dp.getDatas(), OtmCourseInfoExcelVo.class);
				exporter.exportExcel(hearders, datas, out, 1);
			}
		}
		
		
		/**
		 * 小班课程审批汇总
		 */
		@RequestMapping(value="/miniClassCourseAuditAnalyzeList")
		@ResponseBody
		public DataPackageForJqGrid getMiniClassCourseAuditAnalyze(@ModelAttribute GridRequest gridRequest,
				BasicOperationQueryVo vo,
				String AuditStatuss,@RequestParam(required = false, defaultValue = "keshi") String anshazhesuan,
				String productQuarterSearch){
			DataPackage dataPackage=new DataPackage(gridRequest);
			dataPackage=courseService.getMiniClassCourseAuditAnalyze(dataPackage, vo,AuditStatuss,anshazhesuan,productQuarterSearch);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
			
		}
		
		/**
		 * 小班批量考勤列表
		 */
		
		@RequestMapping(value ="/miniClassCourseAuditList")
		@ResponseBody
		public DataPackageForJqGrid miniClassCourseAuditList(@ModelAttribute GridRequest gridRequest,
				String startDate,
				String endDate,
				String campusId,
				String teacherId,
				String auditStatus,String subject,@RequestParam(required = false, defaultValue = "keshi")String anshazhesuan,
				String productQuarterSearch
				) {			
			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage.setPageSize(999);
			dataPackage = courseService.miniClassCourseAuditList(dataPackage, startDate, endDate, campusId, teacherId, auditStatus,anshazhesuan,subject,productQuarterSearch);
			return new DataPackageForJqGrid(dataPackage);
		}


	    @RequestMapping(value = "/getMiniClassCourseAuditSalaryNums")
	    @ResponseBody
		public List getMiniClassCourseAuditSalaryNums(BasicOperationQueryVo vo, String AuditStatus){
			List list = courseService.getMiniClassCourseAuditSalaryNums(vo, AuditStatus);
			return list;
		}
		
		
		/**
		 * 小班课程审批
		 */
		@RequestMapping("/miniClassCourseAuditSubmit")
		@ResponseBody
		public Response miniClassCourseAuditSubmit(@RequestParam String courseId, @RequestParam String auditStatus) throws Exception{
			smallClassService.miniClassCourseAudit(courseId, auditStatus);
			return new Response();
		}
		
		/**
		 * 小班考勤前判断
		 */
		@RequestMapping("/miniClassCourseBeforAttendance")
		@ResponseBody
		public Response  miniClassCourseBeforAttendance(@RequestParam String miniClassCourseId) throws Exception{
			smallClassService.miniClassCourseBeforAttendance(miniClassCourseId);
			return new Response();
		}
		
		/**
		 *小班课程审批汇总工资 
		 */
		 @RequestMapping("/miniClaCourseAuditAnalyzeSalary")
		 @ResponseBody
		 public DataPackageForJqGrid miniClaCourseAuditAnalyzeSalary(@ModelAttribute GridRequest gridRequest,
																	 @Valid BasicOperationQueryVo vo,
					String AuditStatuss,
					String anshazhesuan,
					String productQuarterSearch){
			 DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage = courseService.miniClaCourseAuditAnalyzeSalary(dataPackage, vo, AuditStatuss, anshazhesuan,productQuarterSearch);
			return new DataPackageForJqGrid(dataPackage);
		 }

	@RequestMapping("/getExcelMiniClaCourseAuditAnalyzeSalary")
	@ResponseBody
	public void getExcelMiniClaCourseAuditAnalyzeSalary(@ModelAttribute GridRequest gridRequest, HttpServletResponse response, @Valid BasicOperationQueryVo vo
			, String AuditStatuss, String anshazhesuan, String productQuarterSearch, String productQuarterSearchName
			, String totalType, String branchName, String blCampusName, String teacherName, String teacherTypeName) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);
		dataPackage = courseService.miniClaCourseAuditAnalyzeSalary(dataPackage, vo, AuditStatuss, anshazhesuan,productQuarterSearch);

		List<Map<Object, Object>> datas = (List)dataPackage.getDatas();

		List list = new ArrayList<>();

		for (Map<Object, Object> map:datas){
			if (map.get("workType")!=null && StringUtils.isNotEmpty(map.get("workType").toString())){
				String workType = map.get("workType").toString();
				if ("FULL_TIME".equals(workType)){
					map.put("workType", "全职");
				}else if ("PART_TIME".equals(workType)){
					map.put("workType", "兼职");
				}else if ("DUMMY".equals(workType)){
					map.put("workType", "虚拟");
				}
			}


			BigDecimal salarys1 = new BigDecimal(map.get("salarys1").toString());
			BigDecimal salarys2 = new BigDecimal(map.get("salarys2").toString());
			BigDecimal salarys3 = new BigDecimal(map.get("salarys3").toString());
			BigDecimal salarys4 = new BigDecimal(map.get("salarys4").toString());
			BigDecimal salarys5 = new BigDecimal(map.get("salarys5").toString());
			BigDecimal salarys6 = new BigDecimal(map.get("salarys6").toString());
			BigDecimal salarys7 = new BigDecimal(map.get("salarys7").toString());
			BigDecimal salarys8 = new BigDecimal(map.get("salarys8").toString());
			BigDecimal salarys9 = new BigDecimal(map.get("salarys9").toString());
			BigDecimal salarys10 = new BigDecimal(map.get("salarys10").toString());
			BigDecimal salarys11 = new BigDecimal(map.get("salarys11").toString());
			BigDecimal salarys12 = new BigDecimal(map.get("salarys12").toString());
			BigDecimal otherSalarys = new BigDecimal(map.get("otherSalarys").toString());

			BigDecimal allSalarys = salarys1.add(salarys2).add(salarys3).add(salarys4).add(salarys5).add(salarys6).add(salarys7).add(salarys8).add(salarys9).add(salarys10).add(salarys11).add(salarys12);
			if ((map.get("validateHours") == null||"0.0".equals(map.get("validateHours"))||"0.00".equals(map.get("validateHours").toString())) && allSalarys.compareTo(BigDecimal.ZERO) == 0){
				map.put("validateHours", "-");
			}
			if ((map.get("gradeOneHours") == null||"0.0".equals(map.get("gradeOneHours"))||"0.00".equals(map.get("gradeOneHours").toString())) && salarys1.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeOneHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeOneHours", salarys1);
				}
			}

			if ((map.get("gradeTwoHours") == null||"0.0".equals(map.get("gradeTwoHours"))||"0.00".equals(map.get("gradeTwoHours").toString())) && salarys2.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeTwoHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeTwoHours", salarys2);
				}
			}

			if ((map.get("gradeThreeHours") == null||"0.0".equals(map.get("gradeThreeHours"))||"0.00".equals(map.get("gradeThreeHours").toString())) && salarys3.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeThreeHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeThreeHours", salarys3);
				}
			}

			if ((map.get("gradeFourHours") == null||"0.0".equals(map.get("gradeFourHours"))||"0.00".equals(map.get("gradeFourHours").toString())) && salarys4.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeFourHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeFourHours", salarys4);
				}
			}

			if ((map.get("gradeFiveHours") == null||"0.0".equals(map.get("gradeFiveHours"))||"0.00".equals(map.get("gradeFiveHours").toString())) && salarys5.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeFiveHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeFiveHours", salarys5);
				}
			}

			if ((map.get("gradeSixHours") == null||"0.0".equals(map.get("gradeSixHours"))||"0.00".equals(map.get("gradeSixHours").toString())) && salarys6.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeSixHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeSixHours", salarys6);
				}
			}

			if ((map.get("gradeSevenHours") == null||"0.0".equals(map.get("gradeSevenHours"))||"0.00".equals(map.get("gradeSevenHours").toString())) && salarys7.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeSevenHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeSevenHours", salarys7);
				}
			}

			if ((map.get("gradeEightHours") == null||"0.0".equals(map.get("gradeEightHours"))||"0.00".equals(map.get("gradeEightHours").toString())) && salarys8.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeEightHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeEightHours", salarys8);
				}
			}

			if ((map.get("gradeNineHours") == null||"0.0".equals(map.get("gradeNineHours"))||"0.00".equals(map.get("gradeNineHours").toString())) && salarys9.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeNineHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeNineHours", salarys9);
				}
			}

			if ((map.get("gradeTenHours") == null||"0.0".equals(map.get("gradeTenHours"))||"0.00".equals(map.get("gradeTenHours").toString())) && salarys10.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeTenHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeTenHours", salarys10);
				}
			}

			if ((map.get("gradeElevenHours") == null||"0.0".equals(map.get("gradeElevenHours"))||"0.00".equals(map.get("gradeElevenHours").toString())) && salarys11.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeElevenHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeElevenHours", salarys11);
				}
			}

			if ((map.get("gradeTwelveHours") == null||"0.0".equals(map.get("gradeTwelveHours"))||"0.00".equals(map.get("gradeTwelveHours").toString())) && salarys12.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeTwelveHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeTwelveHours", salarys12);
				}
			}

			if ((map.get("otherHours") == null||"0.0".equals(map.get("otherHours"))||"0.00".equals(map.get("otherHours").toString())) && otherSalarys.compareTo(BigDecimal.ZERO) == 0){
				map.put("otherHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("otherHours", otherSalarys);
				}
			}



			list.add(map);
		}



		StringBuffer fileBuffer = new StringBuffer();

		//时间段
		if (StringUtils.isNotEmpty(vo.getStartDate())&&StringUtils.isNotEmpty(vo.getEndDate())){
			fileBuffer.append(vo.getStartDate()+"-"+vo.getEndDate());
		}

		if (StringUtils.isNotEmpty(productQuarterSearchName)){
			fileBuffer.append("_"+productQuarterSearchName);
		}

		if (StringUtils.isNotEmpty(branchName)){
			fileBuffer.append("_"+branchName);
		}

		if (StringUtils.isNotEmpty(blCampusName)){
			fileBuffer.append("_"+blCampusName);
		}



		if (StringUtils.isNotEmpty(teacherName)){
			fileBuffer.append("_"+teacherName);
		}

		if (StringUtils.isNotEmpty(teacherTypeName)){
			fileBuffer.append("_"+teacherTypeName);
		}
		if ("MINI_CLASS".equals(totalType)){
			fileBuffer.append("_小班");
		}else {
			fileBuffer.append("_10人课堂");
		}
		if ("hour".equals(anshazhesuan)){
			fileBuffer.append("_小班课程审批汇总工资_按小时");
		}else {
			fileBuffer.append("_小班课程审批汇总工资_按课时");
		}


		fileBuffer.append("_导出时间"+DateTools.getCurrentDate()+".xls");

		String fileName =fileBuffer.toString() ;
		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		ExportExcel<Map<Object, Object>> exporter = new ExportExcel<>();
		String[] hearders = null;//表头数组
		String[] heardersId = null;//表头数组

		if ("hour".equals(anshazhesuan)){
			hearders = new String[]{"集团", "分公司", "课程校区", "老师", "工号", "教师类型","教师等级", "全/兼", "老师所属校区", "小班名称", "月末学生数", "有效小时", "一年级", "二年级","三年级", "四年级","五年级", "六年级", "初一", "初二", "初三", "高一", "高二", "高三", "其他"};
		}else {
			hearders = new String[]{"集团", "分公司", "课程校区", "老师", "工号", "教师类型","教师等级", "全/兼", "老师所属校区", "小班名称", "月末学生数", "有效课时", "一年级", "二年级","三年级", "四年级","五年级", "六年级", "初一", "初二", "初三", "高一", "高二", "高三", "其他"};
		}

		heardersId = new String[]{"groupName","brenchName","campusName", "teacher", "employeeNo", "teacherTypeName", "teacherLevelName", "workType", "teacherBlCampus", "miniClassName", "monthLastStudents", "validateHours", "gradeOneHours", "gradeTwoHours", "gradeThreeHours", "gradeFourHours", "gradeFiveHours", "gradeSixHours", "gradeSevenHours", "gradeEightHours", "gradeNineHours", "gradeTenHours", "gradeElevenHours", "gradeTwelveHours", "otherHours"};

		try(OutputStream out = response.getOutputStream()){
			exporter.exportExcelFromMap(hearders, list, out,heardersId);
		}



	}

		 
	@RequestMapping(value = "/checkContractProductSubjectHours")
	@ResponseBody
	public boolean checkContractProductSubjectHours(String studentId, String productId, String subjectId, 
			BigDecimal courseHours, String courseSummaryId, String courseId){
		return courseService.checkContractProductSubjectHours(studentId, productId,subjectId, courseHours,courseSummaryId, courseId);
	}
	
	@RequestMapping(value = "/multiCheckContractProductSubjectHours")
	@ResponseBody
	public Response multiCheckContractProductSubjectHours(@RequestBody MultiStudentProductSubjectVo multiStudentProductSubjectVo){
		return courseService.multiCheckContractProductSubjectHours(multiStudentProductSubjectVo);
	}
	
	@RequestMapping(value = "/getTeacherTypeByUserIdAndCourseDate")
	@ResponseBody
	public String getTeacherTypeByUserIdAndCourseDate(@RequestParam String teacherId, @RequestParam String courseDate){
		return teacherVersionService.getTeacherTypeByUserIdAndCourseDate(teacherId, courseDate);
	}

			/**
			 * 查询当前登陆的老师或者学管师的考勤数量  今日 本周 本月 的考勤数 
			 */
			 @RequestMapping("/attendanceCount")
			 @ResponseBody
			 public Map<String, Integer> attendanceCount(){
				 try {
					 return courseService.attendanceCount();
					// return null;
				} catch (Exception e) {
					return new HashMap<>();
				}
				 
			 }

	/**
	 * 小班学生花名册
	 */
	 @RequestMapping("/getMiniClaCourseStudentRoster")
	 @ResponseBody
	 public MiniClassCourseStudentRosterVo getMiniClaCourseStudentRoster(@RequestParam String miniClassId){
		 return smallClassService.getMiniClaCourseStudentRoster(miniClassId);
	 }
	 	 
	 
	/**
	 * 获取教材信息 boss专用
	 */
	@RequestMapping(value = "/getTextBookBossList")
	@ResponseBody
	public DataPackageForJqGrid getTextBookBossList(@ModelAttribute TextBookBossVo textBookBossVo,
			@ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		// pageSize 和pageNum必传
		dataPackage = courseService.getTextBookBossList(textBookBossVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 导出班课 到Excel
	 */
	
	 @ResponseBody
	 @RequestMapping(value="/getSmallClassListToExcel")
	 public void getSmallClassListToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClass miniClass , String branchLevel,HttpServletRequest request, HttpServletResponse response) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		long time1 = System.currentTimeMillis();
		List<SmallClassExcelVo> list = courseService.getSmallClassToExcel(miniClass, dataPackage);
		long time2 = System.currentTimeMillis();
		log.info("导出excel耗时:"+(time2-time1)+"ms");
		//导出的excel
		//文件名为 年份+季度/时间段+校区（如有）+年级（如有）+科目（如有）+班课数据
		StringBuffer fileBuffer = new StringBuffer();
		if(StringUtil.isNotBlank(miniClass.getStartDate()) && StringUtil.isNotBlank(miniClass.getEndDate()) ){
            fileBuffer.append(miniClass.getStartDate().toString().replaceAll("-", "")+"--");
            fileBuffer.append(miniClass.getEndDate().toString().replaceAll("-", ""));
		}else{
			//产品年份
			if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
				String productVersionId = miniClass.getProduct().getProductVersion().getId();
				fileBuffer.append(dataDictService.findById(productVersionId).getName());
			}		
			//产品季度
			if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
				String productQuarterId = miniClass.getProduct().getProductQuarter().getId(); 
	            fileBuffer.append(dataDictService.findById(productQuarterId).getName());
			}			
		}	
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			String blCampusId = miniClass.getBlCampus().getId();
			fileBuffer.append(organizationService.findById(blCampusId).getName());
		}
		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			String gradeId = miniClass.getGrade().getId();
			fileBuffer.append(dataDictService.findById(gradeId).getName());
		}
		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			String subjectId = miniClass.getSubject().getId();
			fileBuffer.append(dataDictService.findById(subjectId).getName());
		}
		fileBuffer.append("班课数据.xls");
		String fileName =fileBuffer.toString() ;
		
		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		
		ExportExcel<SmallClassExcelVo> exporter = new ExportExcel<SmallClassExcelVo>();
		String[] hearders = new String[] {"班名","年份","季度","期","班型","限额", "已报名人数","未缴费人数","部分缴费人数","已缴费人数","退费人数","分公司","教学点","教室", "年级","科目","任课教师","辅导老师","开课日期","结束日期","上课时间", "课时长度", "课时时长", "每讲长度（小时）","单价",
				"每讲单价","课次","金额（不含资料费）","小班状态","总课时","已排课时","已上课时","建班时间","绑定教材"};//表头数组
		try(OutputStream out = response.getOutputStream();){		
			exporter.exportExcel(hearders, list, out);
		}
		

		
		
	 }
	 
	 @ResponseBody
	 @RequestMapping(value="/getSmallClassListToExcelSize")
	 public Response getSmallClassListToExcelSize(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClass miniClass) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		
		List<Map<Object, Object>> list = courseService.getSmallClassToExcelSize(miniClass, dataPackage);
		
		Response resp = new Response();
	    if(list==null || list.size()==0){
	    	resp.setResultCode(-1);    	
	    }
	    return resp;
	 }
	 
	 
	 @ResponseBody
	 @RequestMapping(value="/getSmallClassListToCSV")
	 public void getSmallClassListToCSV(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClass miniClass , String branchLevel,HttpServletRequest request, HttpServletResponse response) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		long time1 = System.currentTimeMillis();
		List<List<Object>> list = courseService.getSmallClassToCSV(miniClass, dataPackage);
		long time2 = System.currentTimeMillis();
		log.info("导出excel耗时:"+(time2-time1)+"ms");
		//导出的excel
		//文件名为 年份+季度/时间段+校区（如有）+年级（如有）+科目（如有）+班课数据
		StringBuffer fileBuffer = new StringBuffer();
		if(StringUtil.isNotBlank(miniClass.getStartDate()) && StringUtil.isNotBlank(miniClass.getEndDate()) ){
            fileBuffer.append(miniClass.getStartDate().toString().replaceAll("-", "")+"--");
            fileBuffer.append(miniClass.getEndDate().toString().replaceAll("-", ""));
		}else{
			//产品年份
			if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
				String productVersionId = miniClass.getProduct().getProductVersion().getId();
				fileBuffer.append(dataDictService.findById(productVersionId).getName());
			}		
			//产品季度
			if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
				String productQuarterId = miniClass.getProduct().getProductQuarter().getId(); 
	            fileBuffer.append(dataDictService.findById(productQuarterId).getName());
			}			
		}	
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			String blCampusId = miniClass.getBlCampus().getId();
			fileBuffer.append(organizationService.findById(blCampusId).getName());
		}
		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			String gradeId = miniClass.getGrade().getId();
			fileBuffer.append(dataDictService.findById(gradeId).getName());
		}
		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			String subjectId = miniClass.getSubject().getId();
			fileBuffer.append(dataDictService.findById(subjectId).getName());
		}
		fileBuffer.append("班课数据.csv");
		String fileName =fileBuffer.toString() ;
		
		//response.setContentType("application/ms-excel;charset=UTF-8");
		response.setContentType("text/csv;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		
		Object[] hearders = new Object[] {"班名","年份","季度","期","班型","限额", "已报名人数","未缴费人数","部分缴费人数","已缴费人数","退费人数","教学点","教室", "年级","科目","任课教师","辅导老师","开课日期","结束日期","上课时间", "课时长度", "课时时长", "每讲长度（小时）","单价",
				"每讲单价","课次","金额（不含资料费）","小班状态","总课时","已排课时","已上课时","建班时间","绑定教材"};//表头数组
		List<Object> headList = Arrays.asList(hearders);
		try(OutputStream out = response.getOutputStream();){		
			CSVUtils.exportCSVFile(headList, list, out);
		}
		
		
	 }
	 
	//批量保存小班
	@RequestMapping(value="/batchSaveMiniClass",method = RequestMethod.POST)
	@ResponseBody
	public Response batchSaveMiniClass(@RequestBody MiniClassVo[] miniClassVo)throws Exception{
		return smallClassService.batchSaveMiniClass(miniClassVo);
    }

	//批量新增小班的时候返回小班的 小班的序号 几班几班
	@RequestMapping(value="/getBatchMiniClassNum",method = RequestMethod.GET)
	@ResponseBody
	public Integer getBatchMiniClassNum(@RequestParam String productId,String coursePhase,@RequestParam String campusId,@RequestParam String subjectId)throws ApplicationException{
		if(StringUtil.isBlank(productId)||StringUtil.isBlank(campusId)||StringUtil.isBlank(subjectId)){
			throw new ApplicationException("接口参数有误");
		}
		return smallClassService.getBatchMiniClassNum(productId, coursePhase, campusId,subjectId);
    }
	
	 /**
	  * 今日课时单 
	  */
	@RequestMapping(value ="/getTodayCourseBillList")
	@ResponseBody
	public DataPackageForJqGrid getTodayCourseBillList(@ModelAttribute GridRequest gridRequest, @ModelAttribute TodayCourseBillVo todayCourseBillVo) {
		if(gridRequest.getRows() == 0) {
			gridRequest.setRows(999);
		}
		DataPackage dp = new DataPackage(gridRequest);
		dp = courseService.getTodayCourseBillList(dp, todayCourseBillVo);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 根据课程id获取课时单　
	 */
	 @ResponseBody
	 @RequestMapping(value="/getCourseBillInfoById")
	 public TodayCourseBillVo getCourseBillInfoById(String courseId){
		 return courseService.getCourseBillInfoById(courseId);
		  
	 }
	 
	 
	 /**
	  * 根据courseId数组获取课时单详情　这个详情需要按老师分组装回给前端
	  */
	 @ResponseBody
	 @RequestMapping(value="/getCourseBillsPrint")
	 public Map<String, List<TodayCourseBillVo>> getCourseBillsPrint(String courseId){
		 
		if(StringUtil.isBlank(courseId)){
			return null;
		}
		String[] courseIds = courseId.split(",");
		return courseService.getCourseBillsPrint(courseIds);
	 }
	 
	 /**
	  * 根据传进来的日期获取所有的课程然后　按老师分组装回给前端
	  */
	 @ResponseBody
	 @RequestMapping(value="/getCourseBillsPrintByDate")
	 public Map<String, List<TodayCourseBillVo>> getCourseBillsPrintByDate(String date){
		 if(StringUtil.isBlank(date)){
			 date = DateTools.getCurrentDate();
		 }
		 return courseService.getCourseBillsPrintByDate(date); 
	 }
	 
	 @ResponseBody
	 @RequestMapping(value="/getBillsPrintInfoByDate")
	 public Map<String, Object> getBillsPrintInfoByDate(String date){
		 if(StringUtil.isBlank(date)){
			 date = DateTools.getCurrentDate();
		 }
		 return courseService.getBillsPrintInfoByDate(date);
	 }

	@ResponseBody
	@RequestMapping(value="/getSmallClassXuDuListToExcelSize")
	public Response getSmallClassXuDuListToExcelSize(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClass miniClass) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);

		List list = courseService.getSmallClassXuDuListToExcel(miniClass, dataPackage);

		Response resp = new Response();
		if(list==null || list.size()==0){
			resp.setResultCode(-1);
		}
		return resp;
	}

	@ResponseBody
	@RequestMapping(value="/getSmallClassXuDuListToExcel")
	public void getSmallClassXuDuListToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute MiniClass miniClass , String branchLevel,HttpServletRequest request, HttpServletResponse response) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);
		long time1 = System.currentTimeMillis();
		List list = courseService.getSmallClassXuDuListToExcel(miniClass, dataPackage);
		long time2 = System.currentTimeMillis();
		log.info("导出excel耗时:"+(time2-time1)+"ms");
		//导出的excel
		//文件名为 导出明细excel文件命名规则：年份+季度+校区（如有）+年级（如有）+老师（如有）+科目（如有）+续读明细
		StringBuffer fileBuffer = new StringBuffer();
		//年份
		if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
			String productVersionId = miniClass.getProduct().getProductVersion().getId();
			fileBuffer.append(dataDictService.findById(productVersionId).getName());
		}
		//产品季度
		if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
			String productQuarterId = miniClass.getProduct().getProductQuarter().getId();
			fileBuffer.append(dataDictService.findById(productQuarterId).getName());
		}

		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			String blCampusId = miniClass.getBlCampus().getId();
			fileBuffer.append(organizationService.findById(blCampusId).getName());
		}
		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			String gradeId = miniClass.getGrade().getId();
			fileBuffer.append(dataDictService.findById(gradeId).getName());
		}

		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			String teacherId = miniClass.getTeacher().getUserId();
			String userName = userService.findUserNameById(teacherId);
			fileBuffer.append(userName);
		}
		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			String subjectId = miniClass.getSubject().getId();
			fileBuffer.append(dataDictService.findById(subjectId).getName());
		}
		fileBuffer.append("续读明细.xls");
		String fileName =fileBuffer.toString() ;

		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		ExportExcel<Map<Object, Object>> exporter = new ExportExcel<>();

		//产品季度
		if (miniClass.getProduct().getProductQuarter() != null && org.apache.commons.lang.StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())){
			String quarterId = miniClass.getProduct().getProductQuarter().getId();
			String[] hearders = null;//表头数组
			String[] heardersId = null;//表头数组

			if ("DAT0000000231".equals(quarterId)){
				//春季
				hearders = new String[] {"分公司","所属校区","报读班级", "春季合同时间",  "老师","年份", "季度", "期", "上课时间", "年级", "科目", "班型", "学生ID", "学生姓名", "续报暑假原老师", "续报暑假", "暑假班级", "暑假合同时间", "暑假班级班型", "老师", "上课时间", "续报秋季原老师", "续报秋季", "秋季班级", "秋季合同时间", "秋季班级班型", "老师", "上课时间"};
				heardersId = new String[] {"branchName", "blCampusName","springMCName", "springContractTime",  "springTeacherName","springYearName", "springQuarterName", "phaseName", "springClassTimeSpace", "springGradeName", "springSubjectName", "classTypeName",
						"studentId", "studentName", "originSpring1Teacher", "XuBaoSummer", "summerMCName", "summerContractTime", "summerClassType", "summerTeacherName", "summerTimeSpace", "originSpring2Teacher", "XuBaoAutumn", "autumnMCName", "autumnContractTime", "autumnClassType", "autumnTeacherName", "autumnTimeSpace"};

			}else if ("DAT0000000232".equals(quarterId)){
				//暑假
				hearders = new String[] {"分公司","所属校区","报读班级", "暑假合同时间",  "老师","年份", "季度", "期", "年级", "科目", "班型", "学生ID", "学生姓名", "续报秋季原老师", "续报秋季", "秋季班级", "秋季合同时间", "秋季班级班型", "老师", "上课时间"};
				heardersId = new String[] {"branchName", "blCampusName","summerMCName", "summerContractTime",  "summerTeacherName","summerYearName", "summerQuarterName", "phaseName", "summerGradeName", "summerSubjectName", "classTypeName",
						"studentId", "studentName", "originSummerTeacher", "XuBaoAutumn", "autumnMCName", "autumnContractTime", "autumnClassType", "autumnTeacherName", "autumnTimeSpace"};

			}else if ("DAT0000000233".equals(quarterId)){
				//秋季
				hearders = new String[] {"分公司","所属校区","报读班级", "秋季合同时间",  "老师","年份", "季度", "期", "年级", "科目", "班型", "学生ID", "学生姓名", "续报寒假原老师", "续报寒假", "寒假班级", "寒假合同时间", "寒假班级班型", "老师", "上课时间", "续报春季原老师", "续报春季", "春季班级", "春季合同时间", "春季班级班型", "老师", "上课时间"};
				heardersId = new String[] {"branchName", "blCampusName","autumnMCName", "autumnContractTime",  "autumnTeacherName","autumnYearName", "autumnQuarterName", "phaseName", "autumnGradeName", "autumnSubjectName", "classTypeName",
						"studentId", "studentName", "originAutumn1Teacher", "XuBaoWinter", "winterMCName", "winterContractTime", "winterClassType", "winterTeacherName", "winterTimeSpace", "originAutumn2Teacher", "XuBaoSpring", "springMCName", "springContractTime", "springClassType", "springTeacherName", "springTimeSpace"};
			}else if ("DAT0000000230".equals(quarterId)){
				//寒假
				hearders = new String[] {"分公司","所属校区","报读班级", "寒假合同时间",  "老师","年份", "季度", "期", "年级", "科目", "班型", "学生ID", "学生姓名", "续报春季原老师", "续报春季", "春季班级", "春季合同时间", "春季班级班型", "老师", "上课时间"};
				heardersId = new String[] {"branchName", "blCampusName","winterMCName", "winterContractTime",  "winterTeacherName","winterYearName", "winterQuarterName", "phaseName", "winterGradeName", "winterSubjectName", "classTypeName",
						"studentId", "studentName", "originSpringTeacher", "XuBaoSpring", "springMCName", "springContractTime", "springClassType", "springTeacherName", "springTimeSpace"};

			}else {
				throw new ApplicationException("找不到的季度");
			}

			try(OutputStream out = response.getOutputStream()){
				exporter.exportExcelFromMap(hearders, list, out,heardersId);
			}

		}else {
			throw new ApplicationException("导出季度必选");
		}



	}
	
	/**
	 * 自动识别审批全部通过 一对一
	 * @param courseSearchVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/auditAutoRecog",method = RequestMethod.POST)//,
	@ResponseBody
	public Response auditAutoRecog(@RequestBody AuditAutoRecogVo courseVo) {
        //DataPackage dataPackage = new DataPackage();
        return courseService.auditAutoRecog(courseVo);
		//courseService.getOneOnOneBatchAttendanceList(courseVo, dataPackage);
		//return new Response();
	}


	
	  
}
