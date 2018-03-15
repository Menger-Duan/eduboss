package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.eduboss.domain.Organization;
import com.eduboss.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.SmallClassExcelVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TwoClassCourseStudentRosterVo;
import com.eduboss.domainVo.TwoTeacherClassCourseVo;
import com.eduboss.domainVo.TwoTeacherClassStudentAttendentVo;
import com.eduboss.domainVo.TwoTeacherClassStudentVo;
import com.eduboss.domainVo.TwoTeacherClassTwoExcelVo;
import com.eduboss.domainVo.TwoTeacherClassTwoVo;
import com.eduboss.domainVo.TwoTeacherClassVo;
import com.eduboss.domainVo.TwoTeacherCourseSearchVo;
import com.eduboss.service.DataDictService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.TwoTeacherClassService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/TwoTeacherClassController")
public class TwoTeacherClassController {
	
	private final static Logger log = Logger.getLogger(TwoTeacherClassController.class);
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private TwoTeacherClassService twoTeacherClassService;
	
	@Autowired
	private DataDictService dataDictService;
	
	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value="/findTwoTeacherClassList")
	@ResponseBody
	public DataPackageForJqGrid findTwoTeacherClassList(@ModelAttribute GridRequest gridRequest, TwoTeacherClassVo vo){
		DataPackage dp = new DataPackage(gridRequest);
		dp=twoTeacherClassService.findTwoTeacherClassList(dp,vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	@RequestMapping(value="/findTwoTeacheClassrById")
	@ResponseBody
	public TwoTeacherClassVo findTwoTeacheClassrById(int id){
		return twoTeacherClassService.findTwoTeacherById(id);
	}

	@RequestMapping(value="/saveTwoTeacherClass",method = RequestMethod.POST)
	@ResponseBody
	public Response saveTwoTeacherClass(@RequestBody TwoTeacherClassVo vo){
		if(StringUtils.isNotBlank(vo.getOper()) && "del".equals(vo.getOper())){
			return twoTeacherClassService.deleteTwoTeacherClass(vo.getClassId());
		}
		else {
			return twoTeacherClassService.saveTwoTeacherClass(vo);
		}
	}


	@RequestMapping(value="/findTwoTeacherClassCourseList")
	@ResponseBody
	public DataPackageForJqGrid findTwoTeacherClassCourseList(@ModelAttribute GridRequest gridRequest,TwoTeacherClassCourseVo vo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = twoTeacherClassService.findTwoTeacherClassCourseList(dp, vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	@RequestMapping(value="/findCourseByClassTwoId")
	@ResponseBody
	public DataPackageForJqGrid findCourseByClassTwoId(@ModelAttribute GridRequest gridRequest,TwoTeacherClassCourseVo vo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = twoTeacherClassService.findCourseByClassTwoId(dp, vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	@RequestMapping(value="/getTwoTeacherClassCourseList")
	@ResponseBody
	public DataPackageForJqGrid getTwoTeacherClassCourseList(@ModelAttribute GridRequest gridRequest,TwoTeacherClassCourseVo vo){
		DataPackage dp = new DataPackage(gridRequest);
		dp = twoTeacherClassService.getTwoTeacherClassCourseList(dp, vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	/**
	 *
	 * @param gridRequest
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/getTwoTeacherClassStudentAttendentList")
	@ResponseBody
	public DataPackageForJqGrid getTwoTeacherClassStudentAttendentList(@ModelAttribute GridRequest gridRequest,TwoTeacherClassCourseVo vo){
		DataPackage dp = new DataPackage(gridRequest);
		dp = twoTeacherClassService.getTwoTeacherClassStudentAttendentList(dp, vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}


	@RequestMapping(value="/findTwoTeacherClassStudentList")
	@ResponseBody
	public DataPackageForJqGrid findTwoTeacherClassStudentList(@ModelAttribute GridRequest gridRequest,TwoTeacherClassStudentVo vo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = twoTeacherClassService.findTwoTeacherClassStudentList(dp, vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	@RequestMapping(value="/findTwoTeacherClassTwoList")
	@ResponseBody
	public DataPackageForJqGrid findTwoTeacherClassTwoList(@ModelAttribute GridRequest gridRequest,TwoTeacherClassTwoVo vo) {
		DataPackage dp = new DataPackage(gridRequest);
		dp = twoTeacherClassService.findTwoTeacherClassTwoList(dp, vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	@RequestMapping(value="/saveMultTwoTeacherClassCourse")
	@ResponseBody
	public Response saveMultTwoTeacherClassCourse(@RequestParam String courseListJsonStr,@RequestParam String arrangedHours) {
		TwoTeacherClassCourse[] courseList=null;
		try {
			courseList= objectMapper.readValue(courseListJsonStr, TwoTeacherClassCourse[].class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		twoTeacherClassService.saveTwoTeacherClassCourse(courseList,arrangedHours);

		return new Response();
	}

	@RequestMapping(value="/saveTwoTeacherClassTwo",method = RequestMethod.POST)
	@ResponseBody
	public Response saveTwoTeacherClassTwo(@RequestBody TwoTeacherClassTwoVo vo){
		if(StringUtils.isNotBlank(vo.getOper()) && "del".equals(vo.getOper())){
			return twoTeacherClassService.deleteTwoTeacherClassTwo(vo.getId());
		}
		else {
			return twoTeacherClassService.saveTwoTeacherClassTwo(vo);
		}
	}

	@RequestMapping(value="/batchSaveTwoTeacherClassTwo",method = RequestMethod.POST)
	@ResponseBody
	public Response batchSaveTwoTeacherClassTwo( @RequestBody TwoTeacherClassTwoVo[] twoTeacherClassTwoVos){
		return twoTeacherClassService.batchSaveTwoTeacherClassTwo(twoTeacherClassTwoVos);
	}

	/**
	 * 获取双师主班老师
	 * @return
	 */
	@RequestMapping(value="/getTwoTeacherClassTeacher",method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> getTwoTeacherClassTeacher(){
		return twoTeacherClassService.getTwoTeacherClassTeacher();
	}


	@RequestMapping(value="/findTwoTeacherClassTwoById")
	@ResponseBody
	public TwoTeacherClassTwoVo findTwoTeacherClassTwoById(int id){
		return twoTeacherClassService.findTwoTeacherClassTwoById(id);
	}


	/**
	 * 删除小班课程
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value="/deleteClassCourse",method = RequestMethod.POST)
	@ResponseBody
	public Response deleteClassCourse(@RequestParam(value = "courseId[]") int[] courseId){
		twoTeacherClassService.deleteClassCourse(courseId);
		return new Response();
	}


	@RequestMapping(value="/findTwoTeacherClassCourseById")
	@ResponseBody
	public TwoTeacherClassCourseVo findTwoTeacherClassCourseById(int courseId){
		return twoTeacherClassService.findTwoTeacherClassCourseById(courseId);
	}

	@RequestMapping(value="/saveTwoTeacherClassCourse",method = RequestMethod.POST)
	@ResponseBody
	public Response saveTwoTeacherClassCourse(@ModelAttribute TwoTeacherClassCourseVo vo) throws Exception{

		return twoTeacherClassService.saveTwoTeacherClassCourse(vo);
	}


	@RequestMapping(value="/findTwoTeacherClassAttendentList")
	@ResponseBody
	public DataPackageForJqGrid findTwoTeacherClassAttendentList(@ModelAttribute GridRequest gridRequest ,TwoTeacherClassStudentAttendentVo vo){
		DataPackage dp = new DataPackage(gridRequest);
		dp=twoTeacherClassService.findTwoTeacherClassAttendentList(dp,vo);
		DataPackageForJqGrid returnDp = new DataPackageForJqGrid(dp);
		return returnDp;
	}

	@RequestMapping(value="/getStudentWantListByClassId")
	@ResponseBody
	public List<StudentVo> getStudentWantListByClassId(@RequestParam String classId){
		return twoTeacherClassService.getStudentWantListByClassId(classId);
	}

	@RequestMapping(value ="/getTwoClassStuRemain")
	@ResponseBody
	public ContractProductVo getTwoClassStuRemain(@RequestParam int classId, @RequestParam String studentId) {
		return twoTeacherClassService.getTwoClassStuRemain(classId, studentId);
	}

	@RequestMapping(value="/checkAllowAddStudent4Class")
	@ResponseBody
	public Response checkAllowAddStudent4Class(@RequestParam String studentIds, @RequestParam int classId){
		return twoTeacherClassService.checkAllowAddStudent4Class(studentIds, classId);
	}

	@RequestMapping(value="/changeStudentTwoClass")
	@ResponseBody
	public Response changeStudentTwoClass(@RequestParam int oldClassId,@RequestParam int newClassId,@RequestParam String studentIds, @RequestParam String firstClassDate){
		Response response=new Response();
		twoTeacherClassService.changeStudentTwoClass(oldClassId,newClassId,studentIds,firstClassDate);
		return response;
	}

	@RequestMapping(value="/findTwoClassForChangeClassSelect")
	@ResponseBody
	public List<TwoTeacherClassTwoVo> findTwoClassForChangeClassSelect(@RequestParam int twoClassId){
		List<TwoTeacherClassTwoVo> list=twoTeacherClassService.findTwoClassForChangeClassSelect(twoClassId);
		return list;
	}

	@RequestMapping("/getClassCourseTimeInfo")
	@ResponseBody
	public Map getClassCourseTimeInfo(@RequestParam String classId){
		return twoTeacherClassService.getClassCourseTimeInfo(classId);
	}

	@RequestMapping(value="/findTwoTeacherTwoByContractProductId")
	@ResponseBody
	public TwoTeacherClassTwoVo findTwoTeacherTwoByContractProductId(String conProId){
		return twoTeacherClassService.findTwoTeacherTwoByContractProductId(conProId);
	}

	@RequestMapping(value ="/findClassWillBuyHour")
	@ResponseBody
	public BigDecimal findClassWillBuyHour(@RequestParam int classId, @RequestParam String startDate) {
		return twoTeacherClassService.findClassWillBuyHour(classId, startDate);
	}



	/**
	 * 更新双师学生考勤信息
	 * @param courseId 主班课程id
	 * @param classTwoId 辅班id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/modifyTwoTeacherClassTwoCourseStudentAttendance",method = RequestMethod.POST)
	@ResponseBody
	public Response modifyTwoTeacherClassTwoCourseStudentAttendance(@RequestParam int courseId, @RequestParam int classTwoId, @RequestParam String attendanceData, @RequestParam String oprationCode) throws Exception{
		return twoTeacherClassService.modifyTwoTeacherClassTwoCourseStudentAttendance(courseId, classTwoId, attendanceData, oprationCode);
	}

	@RequestMapping(value="/modifyTwoTeacherClassTwoCourseStudentAttendanceSupplement",method = RequestMethod.POST)
	@ResponseBody
	public Response  modifyTwoTeacherClassTwoCourseStudentAttendanceSupplement(@RequestParam int id, String supplementDate , String absentRemark){
		twoTeacherClassService.modifyTwoTeacherClassTwoCourseStudentAttendanceSupplement(id, supplementDate, absentRemark);
		return new Response();
	}

	@RequestMapping(value ="/getLoginUserMainTeacherList")
	@ResponseBody
	public List<Map<String,String>> getLoginUserMainTeacherList() {
		return twoTeacherClassService.getLoginUserMainTeacherList();
	}

	@RequestMapping(value ="/getLoginUserTwoTeacherList")
	@ResponseBody
	public List<Map<String,String>> getLoginUserTwoTeacherList() {
		return twoTeacherClassService.getLoginUserTwoTeacherList();
	}

	/**
	 *双师退班
	 * @param studentIds
	 * @param twoClassId
	 * @return
	 */
	@RequestMapping(value="/deleteStudentInTwoTeacherTwo")
	@ResponseBody
	public Response deleteStudentInTwoTeacherTwo(@RequestParam String studentIds, @RequestParam int twoClassId){
		Response response=new Response();
		twoTeacherClassService.deleteStudentInTwoTeacherTwo(studentIds, twoClassId);
		return response;
	}

	 /**
	  * 双师学生花名册
	 */
	@RequestMapping("/getTwoCourseStudentRoster")
	@ResponseBody
	public TwoClassCourseStudentRosterVo getTwoCourseStudentRoster(@RequestParam Integer twoClassId){
		//TODO 
		return twoTeacherClassService.getTwoCourseStudentRoster(twoClassId);
	}
	
	/**
	 * 学生课程表或老师课程表
	 * @param studentId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value ="/getTwoTeacherCourseScheduleList")
    @ResponseBody
    public List<Map<Object,Object>> getTwoTeacherCourseScheduleList(@Valid TwoTeacherCourseSearchVo courseSearch) throws Exception {
        return twoTeacherClassService.getTwoTeacherCourseScheduleList(courseSearch);
    }

    
	/**
	 * 导出班课 到Excel
	 */
	
	 @ResponseBody
	 @RequestMapping(value="/getTwoClassTwoToExcel")
	 public void getTwoClassTwoToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute TwoTeacherClassTwoVo vo , String branchLevel,HttpServletRequest request, HttpServletResponse response) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		long time1 = System.currentTimeMillis();
		List<TwoTeacherClassTwoExcelVo> list = twoTeacherClassService.getTwoClassTwoToExcel(vo, dataPackage);
		long time2 = System.currentTimeMillis();
		log.info("导出excel耗时:"+(time2-time1)+"ms");
		//导出的excel
		//文件名为 年份+季度/时间段+校区（如有）+年级（如有）+科目（如有）+班课数据
		StringBuffer fileBuffer = new StringBuffer();
		if(StringUtil.isNotBlank(vo.getStartDate()) && StringUtil.isNotBlank(vo.getEndDate()) ){
            fileBuffer.append(vo.getStartDate().toString().replaceAll("-", "")+"--");
            fileBuffer.append(vo.getEndDate().toString().replaceAll("-", ""));
		}else{
			//产品年份
			if(StringUtil.isNotBlank(vo.getProductVersion())) {
				String productVersionId = vo.getProductVersion();
				fileBuffer.append(dataDictService.findById(productVersionId).getName());
			}		
			//产品季度
			if(StringUtil.isNotBlank(vo.getProductQuarter())) {
				String productQuarterId = vo.getProductQuarter(); 
	            fileBuffer.append(dataDictService.findById(productQuarterId).getName());
			}			
		}	
		//校区
		if(StringUtil.isNotBlank(vo.getBlCampusId())){
			String blCampusId = vo.getBlCampusId();
			fileBuffer.append(organizationService.findById(blCampusId).getName());
		}
		//年级
		if(StringUtil.isNotBlank(vo.getGradeId())){
			String gradeId = vo.getGradeId();
			fileBuffer.append(dataDictService.findById(gradeId).getName());
		}
		//科目
		if(StringUtil.isNotBlank(vo.getSubject())){
			String subjectId = vo.getSubject();
			fileBuffer.append(dataDictService.findById(subjectId).getName());
		}
		fileBuffer.append("双师辅班明细.xls");
		String fileName =fileBuffer.toString() ;
		
		response.setContentType("application/ms-excel;charset=UTF-8");
		//设置Excel文件名字
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
		
		ExportExcel<TwoTeacherClassTwoExcelVo> exporter = new ExportExcel<TwoTeacherClassTwoExcelVo>();
		String[] hearders = new String[] {"班级名称","年份","季度","期","班级类型","计划招生人数","已报名人数", "退费人数","所属校区","教室", "年级","科目","主讲教师","辅导老师","开课日期","结束日期","上课时间", "课时长度", "课时时长", "单价",
				"金额（不含资料费）","双师课程状态","总课时","已排课时","已上课时","建班时间"};//表头数组
		try(OutputStream out = response.getOutputStream();){		
			exporter.exportExcel(hearders, list, out);
		}
		

		
		
	 }
	 
	 @ResponseBody
	 @RequestMapping(value="/getTwoClassTwoToExcelSize")
	 public Response getTwoClassTwoToExcelSize(@ModelAttribute GridRequest gridRequest, @ModelAttribute TwoTeacherClassTwoVo vo) throws IOException{

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		
		List<Map<Object, Object>> list = twoTeacherClassService.getTwoClassTwoToExcelSize(vo, dataPackage);
		
		Response resp = new Response();
	    if(list==null || list.size()==0){
	    	resp.setResultCode(-1);    	
	    }
	    return resp;
	 }

	@RequestMapping(value="/getTwoTeacherClassTwoXuDuRate")
	@ResponseBody
	public DataPackageForJqGrid getTwoTeacherClassTwoXuDuRate(@ModelAttribute GridRequest gridRequest, @ModelAttribute TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo){
		DataPackage dataPackage=new DataPackage(gridRequest);

		dataPackage = twoTeacherClassService.getTwoTeacherClassTwoXuDuRate(dataPackage, twoTeacherClassTwoExcelVo);

		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}

	@ResponseBody
	@RequestMapping(value="/getTwoTeacherClassTwoXuDuRateSize")
	public Response getTwoTeacherClassTwoXuDuRateSize(@ModelAttribute GridRequest gridRequest, @ModelAttribute TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(20000);

		List<Map<Object, Object>> list = null;

		dataPackage = twoTeacherClassService.getTwoTeacherClassTwoXuDuRate(dataPackage, twoTeacherClassTwoExcelVo);
		list = (List) dataPackage.getDatas();

		Response resp = new Response();
		if(list==null || list.size()==0){
			resp.setResultCode(-1);
		}
		return resp;
	}




	@RequestMapping(value="/getTwoTeacherClassTwoXuDuListToExcel")
	@ResponseBody
	 public void getTwoTeacherClassTwoXuDuListToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo, HttpServletRequest request, HttpServletResponse response) throws IOException{
		 DataPackage dataPackage = new DataPackage(gridRequest);
		 dataPackage.setPageSize(20000);

		 List list = (List)twoTeacherClassService.getTwoTeacherClassTwoXuDuRate(dataPackage, twoTeacherClassTwoExcelVo).getDatas();

		 StringBuffer fileBuffer = new StringBuffer();
		 //年份
		 if(StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getProductVersionName())) {
			 fileBuffer.append(twoTeacherClassTwoExcelVo.getProductVersionName());
		 }
		 //产品季度
		 if(StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getProductQuarterName())) {
			 fileBuffer.append(twoTeacherClassTwoExcelVo.getProductQuarterName());
		 }
		 //分公司
		if (StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getBlBrenchName())){
			fileBuffer.append(twoTeacherClassTwoExcelVo.getBlBrenchName());
		}

		//校区
		if (StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getBlCampusName())){
			fileBuffer.append(twoTeacherClassTwoExcelVo.getBlCampusName());
		}
		//年级
		if (StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getGradeName())){
			fileBuffer.append(twoTeacherClassTwoExcelVo.getGradeName());
		}

		//科目
		if (StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getSubjectName())){
			fileBuffer.append(twoTeacherClassTwoExcelVo.getSubjectName());
		}
		// 班级类型
		if (StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getClassTypeName())){
			fileBuffer.append(twoTeacherClassTwoExcelVo.getClassTypeName());
		}

		//老师
		if (StringUtils.isNotEmpty(twoTeacherClassTwoExcelVo.getTeacherName())){
			fileBuffer.append(twoTeacherClassTwoExcelVo.getTeacherName());
		}



		 fileBuffer.append("续读明细.xls");
		 String fileName =fileBuffer.toString() ;

		 response.setContentType("application/ms-excel;charset=UTF-8");
		 //设置Excel文件名字
		 response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

		 ExportExcel<Map<Object, Object>> exporter = new ExportExcel<>();

		 String[] hearders = null;//表头数组
		 String[] heardersId = null;//表头数组

		 if ("春季".equals(twoTeacherClassTwoExcelVo.getProductQuarterName())){
			 hearders = new String[]{"年份", "季度", "分公司", "所属校区", "年级", "科目", "辅班老师", "春续暑续报率", "春续秋续报率", "春季续报率"};
			 heardersId = new String[]{"productVersionName", "quarterName", "branchName", "blCampusName", "grade", "subject", "teacherName", "first", "second", "third"};
		 }

		 if ("暑假".equals(twoTeacherClassTwoExcelVo.getProductQuarterName())){
			 hearders = new String[]{"年份", "季度", "分公司", "所属校区", "年级", "科目", "辅班老师", "暑假续报率"};
			 heardersId = new String[]{"productVersionName", "quarterName", "branchName", "blCampusName", "grade", "subject", "teacherName", "third"};
		 }

		 if ("秋季".equals(twoTeacherClassTwoExcelVo.getProductQuarterName())){
			 hearders = new String[]{"年份", "季度", "分公司", "所属校区", "年级", "科目", "辅班老师", "秋续寒续报率", "秋续春续报率", "秋季续报率"};
			 heardersId = new String[]{"productVersionName", "quarterName", "branchName", "blCampusName", "grade", "subject", "teacherName", "first", "second", "third"};
		 }

		if ("寒假".equals(twoTeacherClassTwoExcelVo.getProductQuarterName())){
			hearders = new String[]{"年份", "季度", "分公司", "所属校区", "年级", "科目", "辅班老师", "寒假续报率"};
			heardersId = new String[]{"productVersionName", "quarterName", "branchName", "blCampusName", "grade", "subject", "teacherName", "third"};
		}

		 try(OutputStream out = response.getOutputStream()){
			 exporter.exportExcelFromMap(hearders, list, out,heardersId);
		 }

	 }

    @RequestMapping(value="/getAllBranchInTwoTeacher")
    @ResponseBody
    public SelectOptionResponse getAllBranchInTwoTeacher(){
        List<Organization> list = twoTeacherClassService.getAllClassBranch();
        List<SelectOptionResponse.NameValue> nvs = new ArrayList<SelectOptionResponse.NameValue>();
        for(Organization org : list){
            nvs.add(SelectOptionResponse.buildNameValue(org.getName(), org.getId()));
        }
        SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
        selectOptionResponse.getValue().remove(null);
        return selectOptionResponse;
    }

}
