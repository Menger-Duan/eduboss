package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eduboss.utils.DateTools;
import com.eduboss.utils.ExportExcel;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.domainVo.OtmClassVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.OtmClassService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/OtmCourseController")
public class OtmClassController {
	
	@Autowired
	private OtmClassService otmClassService;
	
	private final static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 获取小班jqGrid
	 * @param getMiniClassList
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getOtmClassListForJqGrid")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassListForJqGrid(@ModelAttribute OtmClassVo otmClassVo, @ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = otmClassService.getOtmClassList(otmClassVo, dataPackage);
		DataPackageForJqGrid dpfj =  new DataPackageForJqGrid(dataPackage);
		return dpfj;
	}
	
	/**
	 * 删除和编辑一对多班级信息
	 * @param gridRequest
	 * @param otmClassVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/editOtmClass")
	@ResponseBody
	public Response editOtmClass(@ModelAttribute GridRequest gridRequest, @ModelAttribute OtmClassVo otmClassVo) throws Exception {
		otmClassService.operationOtmClassRecord(gridRequest.getOper(), otmClassVo);
		return new Response();
	}
	
	/**
	 * 根据id查找一对多班级
	 * @param otmClassId
	 * @return
	 */
	@RequestMapping(value ="/findOtmClassById")
	@ResponseBody
	public OtmClassVo findOtmClassById(@RequestParam("otmClassId") String id) throws Exception {
		return otmClassService.findOtmClassById(id);
	}
	
	/**
	 * 根据一对多id查看合同拟报读学生id列表
	 * 
	 * @param otmClassId 一对多id
	 * @return
	 */
	@RequestMapping(value="/getStudentWantListByOtmClassId")
	@ResponseBody
	public List<StudentVo> getStudentWantListByOtmClassId(@RequestParam String otmClassId){
		return otmClassService.getStudentWantListByOtmClassId(otmClassId);
	}
	
	/**
	 * 根据一对多id查看一对多已报读学生
	 * 
	 * @param otmClassId 小班id
	 * @return
	 */
	@RequestMapping(value="/getStudentAlreadyEnrollOtmClasss")
	@ResponseBody
	public List<StudentVo> getStudentAlreadyEnrollOtmClasss(@RequestParam String otmClassId){
		return otmClassService.getStudentAlreadyEnrollOtmClasss(otmClassId);
	}
	
	/**
	 * 批量添加一对多学生
	 * @param studentIds  学生id （多条用，隔开）
	 * @param otmClassId 一对多id
	 * @param firstSchoolTime 第一次上课时间
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value="/AddStudentForOtmClasss")
	@ResponseBody
	public Response AddStudentForOtmClasss(@RequestParam String studentIds, @RequestParam String otmClassId, String firstSchoolTime) throws Exception{
		Response response=new Response();
		otmClassService.AddStudentForOtmClasss(studentIds, otmClassId, firstSchoolTime, true);
		return response;
	}
	
	/**
	 * 修改一对多学生首次上课时间
	 * @param studentId
	 * @param otmClassId
	 * @param firstSchoolTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateOtmClassStudentfirstSchoolTime")
	@ResponseBody
	public Response updateOtmClassStudentfirstSchoolTime(@RequestParam String studentId,@RequestParam String otmClassId,@RequestParam String firstSchoolTime) throws Exception{
		Response response=new Response();
		otmClassService.updateOtmClassStudentfirstSchoolTime(studentId,otmClassId,firstSchoolTime);
		return response;
	}
	
	/**
	 * 获取有考勤记录的一对多学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param otmClassId 一对多班级id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getOtmClassAttendedStudent")
	@ResponseBody
	public List<NameValue> getOtmClassAttendedStudent(@RequestParam String studentIds, @RequestParam String otmClassId) throws Exception{
		List<Student> students =  otmClassService.getOtmClassAttendedStudent(studentIds, otmClassId);
        List<NameValue> nvs = new ArrayList<NameValue>();
		for (Student student : students) {
			nvs.add(SelectOptionResponse.buildNameValue(student.getName(), student.getId()));
		}
		return nvs;
	}
	
	/**
	 * 批量删除一对多学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param otmClassId 一对多id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteStudentInOtmClasss")
	@ResponseBody
	public Response deleteStudentInOtmClasss(@RequestParam String studentIds, @RequestParam String otmClassId) throws Exception{
		Response response=new Response();
		otmClassService.deleteStudentInOtmClasss(studentIds, otmClassId);
		return response;
	}
	
	/**
	 * 一对多课程详情
	 * @param otmClassId
	 * @param gridRequest
	 * @param modelVo
	 * @return
	 */
	@RequestMapping("/getOtmClassCourseDetail")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassCourseDetail( @RequestParam("otmClassId") String otmClassId, @ModelAttribute GridRequest gridRequest,ModelVo modelVo){
		DataPackage dp = new DataPackage(gridRequest);
		dp = otmClassService.getOtmClassCourseDetail(otmClassId, dp, modelVo);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 一对多详情-在读学生信息列表
	 * @param otmClassId
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping("/getOtmClassDetailStudentList")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassDetailStudentList( @RequestParam("otmClassId") String otmClassId, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp = otmClassService.getOtmClassDetailStudentList(otmClassId, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 一对多详情-一对多学生剩余资金&剩余课时
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value ="/findOtmClassStuRemainFinAndHour")
	@ResponseBody
	public OtmClassStudentVo findOtmClassStuRemainFinAndHour(@RequestParam String otmClassId, @RequestParam String studentId) {
		return otmClassService.findOtmClassStuRemainFinAndHour(otmClassId, studentId);
	}
	
	/**
	 * 所有otmType一对多班级列表，用作Selection 
	 * @param otmType
	 * @return
	 */
	@RequestMapping(value = "/getOtmClassList4Selection", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getOtmClassList4Selection(Integer otmType) {//OrganizationType orgType, String orgId
		List<OtmClass> list = otmClassService.getOtmClassList4Selection(otmType);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(OtmClass otmClass : list){
				nvs.add(SelectOptionResponse.buildNameValue(otmClass.getName(), otmClass.getOtmClassId()));
		}
		//nvs.add(SelectOptionResponse.buildNameValue("请选择",""));
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
	/**
	 * 一对多学生转班 + 设置第一次上课日期
	 * @param oldOtmClassId
	 * @param newOtmClassId
	 * @param studentIds
	 * @param firstClassDate
	 * @return
	 */
	@RequestMapping(value="/stuChgOtmClassAndSetFistClassDate")
	@ResponseBody
	public Response stuChgOtmClassAndSetFistClassDate(@RequestParam String oldOtmClassId, @RequestParam String newOtmClassId,@RequestParam String studentIds, @RequestParam String firstClassDate){
		Response response=new Response();
		otmClassService.stuChgOtmClassAndSetFistClassDate(oldOtmClassId,newOtmClassId,studentIds,firstClassDate);
		return response;
	}
	
	/**
	 * 一对多详情-学生详情
	 * @param otmClassId
	 * @param studentId
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping("/getOtmClassDetailStudentDetail")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassDetailStudentDetail( @RequestParam("otmClassId") String otmClassId, @RequestParam("studentId") String studentId, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp = otmClassService.getOtmClassDetailStudentDetail(otmClassId, studentId, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 一对多课时信息 
	 * @param otmClassId
	 * @return
	 */
	@RequestMapping("/getOtmClassCourseTimeAnalyze")
	@ResponseBody
	public Map getOtmClassCourseTimeAnalyze( @RequestParam("otmClassId") String otmClassId){
		return otmClassService.getOtmClassCourseTimeAnalyze(otmClassId);
	}
	
	/**
	 * 保存一对多课程列表
	 * jsonCourseList 格式="[{\"courseId\":\"001\",\"courseDate\":\"2012-04-02\"},{\"courseId\":\"002\",\"courseDate\":\"2014-08-02\"}]";
	 * @param jsonCourseList
	 * @return
	 */
	@RequestMapping(value="/saveOtmCourseList")
	@ResponseBody
	public Response saveOtmCourseList(@RequestParam String courseListJsonStr) {
		OtmClassCourse[] courseList=null;
		try {
			courseList= objectMapper.readValue(courseListJsonStr, OtmClassCourse[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		
		otmClassService.saveOtmCourseList(courseList);
		
		return new Response();
	}
	
	
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value ="/getOtmClassCourseList")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassCourseList(@ModelAttribute GridRequest gridRequest, @ModelAttribute OtmClassCourseVo otmClassCourseVo) {
		if (gridRequest.getRows() == 0) {
			gridRequest.setRows(999);
		}
		DataPackage dp = new DataPackage(gridRequest);
		dp = otmClassService.getOtmClassCourseList(otmClassCourseVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 一对多考勤前判断
	 */
	@RequestMapping("/otmClassCourseBeforAttendance")
	@ResponseBody
	public Response  otmClassCourseBeforAttendance(@RequestParam String otmClassCourseId) throws Exception{
		otmClassService.otmClassCourseBeforAttendance(otmClassCourseId);
		return new Response();
	}
	
	/**
	 * 获取一对多学生考勤列表
	 * @param gridRequest
	 * @param otmClassStudentAttendentVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getOtmClassStudentAttendentList")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassStudentAttendentList(@ModelAttribute GridRequest gridRequest, @ModelAttribute OtmClassStudentAttendentVo otmClassStudentAttendentVo, @RequestParam String oprationCode) throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = otmClassService.getOtmClassStudentAttendentList(otmClassStudentAttendentVo, oprationCode , dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 根据id获取一对多课程
	 * @param id
	 * @return
	 */
	@RequestMapping(value ="/findOtmClassCourseById")
	@ResponseBody
	public OtmClassCourseVo findOtmClassCourseById(@RequestParam String id) {
		OtmClassCourseVo vo = new OtmClassCourseVo();
		vo.setOtmClassCourseId(id);
		return otmClassService.findOtmClassCourseById(vo);
	}
	
	/**
	 * 更新一对多学生考勤信息
	 * @param otmClassCourseId
	 * @param attendanceData
	 * @param oprationCode
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/modifyOtmClassCourseStudentAttendance")
	@ResponseBody
	public Response modifyOtmClassCourseStudentAttendance(@RequestParam String otmClassCourseId, @RequestParam String attendanceData, @RequestParam String oprationCode) throws Exception{
		return otmClassService.modifyOtmClassCourseStudentAttendance(otmClassCourseId, attendanceData, oprationCode);
	}
	
	/**
	 * 一对多详情-主界面信息
	 * @param otmClassCourseId
	 * @return
	 */
	@RequestMapping(value ="/getOtmClassDetailAttChaPageInfo")
	@ResponseBody
	public OtmClassCourseVo getOtmClassDetailAttChaPageInfo(@RequestParam String otmClassCourseId) {
		return otmClassService.getOtmClassDetailAttChaPageInfo(otmClassCourseId);
	}
	
	/**
	 * 一对多详情-一对多考勤扣费详情
	 * @param otmClassStudentVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/getOtmClassAttChargedDetail")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassAttChargedDetail(@ModelAttribute GridRequest gridRequest, @ModelAttribute OtmClassStudentAttendentVo otmClassStudentAttendentVo) throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = otmClassService.getOtmClassAttChargedDetail(otmClassStudentAttendentVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 修改一对多课程信息
	 * @param otmClassCourseVo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/modifyOtmClassCourse")
	@ResponseBody
	public Response modifyOtmClassCourse(@ModelAttribute OtmClassCourseVo otmClassCourseVo) throws Exception{
		otmClassService.saveOrUpdateOtmClassCourse(otmClassCourseVo);
		return new Response();
	}
	
	/**
	 * 删除一对多课程
	 * @param otmClassCourseId
	 * @return
	 */
	@RequestMapping(value="/deleteOtmClassCourse")
	@ResponseBody
	public Response deleteOtmClassCourse(@RequestParam String otmClassCourseId){
		otmClassService.deleteOtmClassCourse(otmClassCourseId);
		return new Response();
	}
	
	/**
	 * 一对多课程审批汇总
	 */
	@RequestMapping(value="/otmClassCourseAuditAnalyzeList")
	@ResponseBody
	public DataPackageForJqGrid getOtmClassCourseAuditAnalyze(@ModelAttribute GridRequest gridRequest,
			BasicOperationQueryVo vo,
			String AuditStatuss,
			String otmClassTypes,@RequestParam(required = false, defaultValue = "keshi") String anshazhesuan){
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage=otmClassService.getOtmClassCourseAuditAnalyze(dataPackage, vo, AuditStatuss,otmClassTypes,anshazhesuan);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
		
	}
	
	/**
	 *一对多课程审批汇总工资 
	 */
	 @RequestMapping("/otmClaCourseAuditAnalyzeSalary")
	 @ResponseBody
	 public DataPackageForJqGrid otmClaCourseAuditAnalyzeSalary(@ModelAttribute GridRequest gridRequest,
			 BasicOperationQueryVo vo,
				String AuditStatuss,
				String otmClassTypes,
				String anshazhesuan){
		 DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = otmClassService.otmClaCourseAuditAnalyzeSalary(dataPackage,vo, AuditStatuss, otmClassTypes,anshazhesuan);
		return new DataPackageForJqGrid(dataPackage);
	 }

	@RequestMapping("/getExcelOtmClaCourseAuditAnalyzeSalary")
	@ResponseBody
	 public void getExcelOtmClaCourseAuditAnalyzeSalary(@ModelAttribute GridRequest gridRequest, HttpServletResponse response, BasicOperationQueryVo vo
			, String AuditStatuss, String otmClassTypes, String anshazhesuan
			, String totalType, String teacherName, String teacherTypeName
	        , String AuditStatussName, String branchName, String blCampusName) throws IOException {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);
		dataPackage = otmClassService.otmClaCourseAuditAnalyzeSalary(dataPackage,vo, AuditStatuss, otmClassTypes,anshazhesuan);
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

			if ("0.00".equals(map.get("unAuditHours").toString())){
				map.put("unAuditHours", "-");
			}

			if ("0.00".equals(map.get("unValidateHours").toString())){
				map.put("unValidateHours", "-");
			}

			BigDecimal salarys1 = map.get("salarys1")!=null? new BigDecimal(map.get("salarys1").toString()):BigDecimal.ZERO;
			BigDecimal salarys2 = map.get("salarys2")!=null? new BigDecimal(map.get("salarys2").toString()):BigDecimal.ZERO;
			BigDecimal salarys3 = map.get("salarys3")!=null? new BigDecimal(map.get("salarys3").toString()):BigDecimal.ZERO;
			BigDecimal salarys4 = map.get("salarys4")!=null? new BigDecimal(map.get("salarys4").toString()):BigDecimal.ZERO;
			BigDecimal salarys5 = map.get("salarys5")!=null? new BigDecimal(map.get("salarys5").toString()):BigDecimal.ZERO;
			BigDecimal salarys6 = map.get("salarys6")!=null? new BigDecimal(map.get("salarys6").toString()):BigDecimal.ZERO;
			BigDecimal salarys7 = map.get("salarys7")!=null? new BigDecimal(map.get("salarys7").toString()):BigDecimal.ZERO;
			BigDecimal salarys8 = map.get("salarys8")!=null? new BigDecimal(map.get("salarys8").toString()):BigDecimal.ZERO;
			BigDecimal salarys9 = map.get("salarys9")!=null? new BigDecimal(map.get("salarys9").toString()):BigDecimal.ZERO;
			BigDecimal salarys10 = map.get("salarys10")!=null? new BigDecimal(map.get("salarys10").toString()):BigDecimal.ZERO;
			BigDecimal salarys11 = map.get("salarys11")!=null? new BigDecimal(map.get("salarys11").toString()):BigDecimal.ZERO;
			BigDecimal salarys12 = map.get("salarys12")!=null? new BigDecimal(map.get("salarys12").toString()):BigDecimal.ZERO;
			BigDecimal otherSalarys = map.get("otherSalarys")!=null? new BigDecimal(map.get("otherSalarys").toString()):BigDecimal.ZERO;

			BigDecimal allSalarys = salarys1.add(salarys2).add(salarys3).add(salarys4).add(salarys5).add(salarys6).add(salarys7).add(salarys8).add(salarys9).add(salarys10).add(salarys11).add(salarys12);

			if (map.get("validateHours") == null ||"0.00".equals(map.get("validateHours").toString()) && allSalarys.compareTo(BigDecimal.ZERO) == 0){
				map.put("validateHours", "-");
			}

			if (map.get("gradeOneHours") == null ||"0.00".equals(map.get("gradeOneHours").toString()) && salarys1.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeOneHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeOneHours", salarys1);
				}
			}

			if (map.get("gradeTwoHours") == null ||"0.00".equals(map.get("gradeTwoHours").toString()) && salarys2.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeTwoHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeTwoHours", salarys2);
				}
			}

			if (map.get("gradeThreeHours") == null ||"0.00".equals(map.get("gradeThreeHours").toString()) && salarys3.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeThreeHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeThreeHours", salarys3);
				}
			}

			if (map.get("gradeFourHours") == null ||"0.00".equals(map.get("gradeFourHours").toString()) && salarys4.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeFourHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeFourHours", salarys4);
				}
			}

			if (map.get("gradeFiveHours") == null ||"0.00".equals(map.get("gradeFiveHours").toString()) && salarys5.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeFiveHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeFiveHours", salarys5);
				}
			}

			if (map.get("gradeSixHours") == null ||"0.00".equals(map.get("gradeSixHours").toString()) && salarys6.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeSixHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeSixHours", salarys6);
				}
			}

			if (map.get("gradeSevenHours") == null ||"0.00".equals(map.get("gradeSevenHours").toString()) && salarys7.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeSevenHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeSevenHours", salarys7);
				}
			}

			if (map.get("gradeEightHours") == null ||"0.00".equals(map.get("gradeEightHours").toString()) && salarys8.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeEightHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeEightHours", salarys8);
				}
			}

			if (map.get("gradeNineHours") == null ||"0.00".equals(map.get("gradeNineHours").toString()) && salarys9.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeNineHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeNineHours", salarys9);
				}
			}

			if (map.get("gradeTenHours") == null ||"0.00".equals(map.get("gradeTenHours").toString()) && salarys10.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeTenHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeTenHours", salarys10);
				}
			}

			if (map.get("gradeElevenHours") == null ||"0.00".equals(map.get("gradeElevenHours").toString()) && salarys11.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeElevenHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeElevenHours", salarys11);
				}
			}

			if (map.get("gradeTwelveHours") == null ||"0.00".equals(map.get("gradeTwelveHours").toString()) && salarys12.compareTo(BigDecimal.ZERO) == 0){
				map.put("gradeTwelveHours", "-");
			}else {
				if ("MINI_CLASS".equals(totalType)){
					map.put("gradeTwelveHours", salarys12);
				}
			}

			if (map.get("otherHours") == null ||"0.00".equals(map.get("otherHours").toString()) && otherSalarys.compareTo(BigDecimal.ZERO) == 0){
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
		if (StringUtils.isNotEmpty(AuditStatussName)){
			fileBuffer.append("_"+AuditStatussName);
		}

		if ("hour".equals(anshazhesuan)){
			fileBuffer.append("_一对多课程审批汇总工资_按小时");
		}else {
			fileBuffer.append("_一对多课程审批汇总工资_按课时");
		}

		fileBuffer.append("_导出时间"+ DateTools.getCurrentDate()+".xls");

		String fileName =fileBuffer.toString() ;
		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		ExportExcel<Map<Object, Object>> exporter = new ExportExcel<>();
		String[] hearders = null;//表头数组
		String[] heardersId = null;//表头数组


		heardersId = new String[]{"groupName","brenchName","campusName", "teacher", "employeeNo", "teacherTypeName", "teacherLevelName", "workType", "teacherBlCampus", "otmClassName", "monthLastStudents", "unAuditHours", "unValidateHours", "validateHours", "gradeOneHours", "gradeTwoHours", "gradeThreeHours", "gradeFourHours", "gradeFiveHours", "gradeSixHours", "gradeSevenHours", "gradeEightHours", "gradeNineHours", "gradeTenHours", "gradeElevenHours", "gradeTwelveHours", "otherHours"};
		if ("hour".equals(anshazhesuan)){
			hearders = new String[]{"集团", "分公司", "课程校区", "老师", "工号", "教师类型","教师等级", "全/兼", "老师所属校区", "一对多名称", "月末学生数", "未审小时", "已审无效小时", "已审有效小时", "一年级", "二年级","三年级", "四年级","五年级", "六年级", "初一", "初二", "初三", "高一", "高二", "高三", "其他"};
		}else {
			hearders = new String[]{"集团", "分公司", "课程校区", "老师", "工号", "教师类型","教师等级", "全/兼", "老师所属校区", "一对多名称", "月末学生数", "未审课时", "已审无效课时", "已审有效课时", "一年级", "二年级","三年级", "四年级","五年级", "六年级", "初一", "初二", "初三", "高一", "高二", "高三", "其他"};
		}

		try(OutputStream out = response.getOutputStream()){
			exporter.exportExcelFromMap(hearders, list, out,heardersId);
		}
	 }
	 
	/**
	 * 一对多审批列表
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @return
	 */
	@RequestMapping(value ="/otmClassCourseAuditList")
	@ResponseBody
	public DataPackageForJqGrid otmClassCourseAuditList(@ModelAttribute GridRequest gridRequest,
			String startDate,
			String endDate,
			String campusId,
			String teacherId,
			String auditStatus,
            String subject
			) {			
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(999);
		dataPackage = otmClassService.otmClassCourseAuditList(dataPackage, startDate, endDate, campusId, teacherId, auditStatus,subject);
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value = "/getOtmClassCourseAuditSalaryNums")
	@ResponseBody
	public List getOtmClassCourseAuditSalaryNums( BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes){
		List  list = otmClassService.getOtmClassCourseAuditSalaryNums( vo, AuditStatus, otmClassTypes);
		return list;
	}
	
	/**
	 * 一对多课程审批
	 */
	@RequestMapping("/otmClassCourseAuditSubmit")
	@ResponseBody
	public Response otmClassCourseAuditSubmit(@RequestParam String courseId, @RequestParam String auditStatus) throws Exception{
		otmClassService.otmClassCourseAudit(courseId, auditStatus);
		return new Response();
	}
	
}
