package com.eduboss.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.domainVo.StudentMap.ParamVo;
import com.eduboss.domainVo.StudentMap.StudentMap;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@RequestMapping(value="/StudentController")
@Controller
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/getStudentList")
	@ResponseBody
	public DataPackageForJqGrid getStudentList(GridRequest gridRequest, 
			Student student,
			ModelVo modelVo,
			String brenchId,
			String stuType,
			String rcourseHour,
			String rcourseHourEnd,
			String stuNameGradeSchool){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentService.getStudentList(student,true, dataPackage, modelVo,rcourseHour,rcourseHourEnd,brenchId,stuType,stuNameGradeSchool);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	/**
	 * 小班学生列表
	 * @return
	 */
	
	@RequestMapping(value="/getMiniClassStudents")
	@ResponseBody
	 public DataPackageForJqGrid getMiniClassStudents(@ModelAttribute GridRequest gridRequest,@ModelAttribute StudentVo studentvo,
			 @ModelAttribute MiniClassVo miniclassvo,
			 	String brenchId, String miniClassGradeId,
			 	ModelVo modelvo,String stuNameGradeSchool){
		 DataPackage dataPackage=new DataPackage(gridRequest);
		 dataPackage=studentService.getMiniClassStudents(studentvo, miniclassvo, dataPackage,brenchId, miniClassGradeId, modelvo,stuNameGradeSchool);
		 return new DataPackageForJqGrid(dataPackage);
	 }
	 
	 /***
	  * 目标班学生列表
	  * @return
	  */
	@RequestMapping(value="/getPromiseStudents")
	@ResponseBody
	 public DataPackageForJqGrid getPromiseStudents(@ModelAttribute GridRequest gridRequest,@ModelAttribute StudentVo studentvo,
			 @ModelAttribute PromiseClassVo promiseClassvo,
			 	String brenchId,
			 	ModelVo modelvo,
			 	String productQuarterId,String stuNameGradeSchool){
		 
		 DataPackage dataPackage=new DataPackage(gridRequest);
		 dataPackage=studentService.getPromiseStudents(studentvo, promiseClassvo, dataPackage, brenchId, modelvo,productQuarterId,stuNameGradeSchool);
		 return new DataPackageForJqGrid(dataPackage);
	 }
	
	@RequestMapping(value="/getStudentAccountList")
	@ResponseBody
	public DataPackageForJqGrid getStudentAccountList(GridRequest gridRequest, Student student, ModelVo modelVo,String rcourseHour ,String rcourseHourEnd,String brenchId,String stuType,String stuNameGrade){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentService.getStudentList(student, dataPackage, modelVo, true, rcourseHour, rcourseHourEnd,brenchId,stuType,stuNameGrade);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value="/getCustomerStudentList")
	@ResponseBody
	public DataPackageForJqGrid getCustomerStudentList(@RequestParam String studentId){
		DataPackage dataPackage = new DataPackage(0,999);
		dataPackage = studentService.getCustomerStudentList(studentId, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}

	/**
	 * 获取已删除的与学生关联的客户
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value="/getDeletedCustomerStudentList")
	@ResponseBody
	public DataPackageForJqGrid getDeletedCustomerStudentList(@RequestParam String studentId){
		DataPackage dataPackage = new DataPackage(0,3);
		dataPackage = studentService.getDeletedCustomerStudentList(studentId, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
    @RequestMapping(value="/transferElectronicAmount")
    @ResponseBody
    public Response transferElectronicAmount(String origStuId, String destStuId, BigDecimal transferAmount) throws ApplicationException{
        studentService.transferElectronicAmount(origStuId,destStuId,transferAmount);
        return new Response();
    }

	@RequestMapping(value="/rechargeMoney")
    public void rechargeMoney(String studentId, BigDecimal money, String remark){
		studentService.rechargeMoney(studentId, money, remark);
	}
	
	/**
	 * 添加学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	@RequestMapping(value="/addCustomerStudent")
	@ResponseBody
	public Response addCustomerStudent(CustomerStudent customerStudent){
		return studentService.addCustomerStudent(customerStudent);
	}
	
	/**
	 * 修改学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	@RequestMapping(value="/updateCustomerStudent")
	@ResponseBody
	public Response updateCustomerStudent(CustomerStudent customerStudent){
		return studentService.updateCustomerStudent(customerStudent);
	}
	
	/**
	 * 还原删除的学生关联客户
	 * @param customerStudent
	 * @return
	 */
	@RequestMapping(value="/restoreCustomerStudent")
	@ResponseBody
	public Response restoreCustomerStudent(CustomerStudent customerStudent){
		return studentService.restoreCustomerStudent(customerStudent);
	}
	
	@RequestMapping(value="/deleteCustomerStudent")
	@ResponseBody
	public Response deleteCustomerStudent(CustomerStudent customerStudent){
		return studentService.deleteCustomerStudent(customerStudent);
	}
	
	
	@RequestMapping(value = "/editStudent")
	@ResponseBody
	public Response editStudent(@ModelAttribute GridRequest gridRequest, @ModelAttribute Student student,
			TransactionCampusRecord transactionRecord,boolean isTransactionCampus) throws Exception {
		//如果转校则生成一条转校区记录
		if(isTransactionCampus){
			transactionRecord.setStudent(student);
			studentService.saveStudentTransactionCampusRecord(transactionRecord);
		}
		
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			studentService.deleteStudent(student);
		} else if("updateStudentStatus".equalsIgnoreCase(gridRequest.getOper())){
			studentService.updateStudentStatus(student);
		} else{
			studentService.saveOrUpdateStudent(student);
		}
		return new Response();
	}
	
	/**
	 * 检查手机号码是否被使用
	 * @param contact
	 * @return
	 */
	@RequestMapping(value = "/checkContactUsed")
    @ResponseBody
	public Response checkContactUsed(@RequestParam(required=true) String contact) {
	    if (studentService.isContactUsed(contact)) {
            throw new ApplicationException("该号码已被客户或学生使用");
        }
	    return new Response();
	}

	/**
	 * 检查能否修改学管
	 * @param student
     * @return
     */
	@ResponseBody
	@RequestMapping(value = "/checkUpdateStudentManage")
	public Map<String,String> checkUpdateStudentManage( @ModelAttribute Student student,String StudentManage){
		Response response = new Response();
		response = studentService.checkCanUpdateStudentManage(student,StudentManage,response);
		Map<String,String> resultMap =  new HashMap<>();
		if (response.getResultCode()==1){
			resultMap.put("canUpdate","false"); // 不能更新学管
			resultMap.put("content",response.getResultMessage());
		}else {
			resultMap.put("canUpdate","true"); // 能更新学管
			resultMap.put("content",response.getResultMessage());
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/turnCampusWithUnpayMiniCourseByStudentId")
	public Map<String,String> turnCampusWithUnpayMiniCourseByStudentId(String studentId){
		return studentService.turnCampusWithUnpayMiniCourseByStudentId(studentId);
	}

	@ResponseBody
	@RequestMapping(value = "/findStudentById")
	public StudentVo findStudentById(@RequestParam String id) {
		return studentService.findStudentById(id);
	}

	@RequestMapping(value = "/findStudentOneOnOnePaiKeInfo")
	@ResponseBody
	public Map<String, Object> findStudentOneOnOnePaiKeInfo(String studentId){
		Map<String, Object> map =new HashMap<>();
		map = studentService.findStudentOneOnOnePaiKeInfo(studentId);
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value ="/editAttanceNo")
	public Response editAttanceNo(@RequestParam String studentId,@RequestParam String attanceNo,@RequestParam String icCardNo){
		return studentService.editAttanceNo(studentId, attanceNo, icCardNo);
	}
	
	/**
	 * 修改学生学管
	 * @param studentId
	 * @param studyManegerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStudentStudyManager")
	public Response updateStudentStudyManager(@RequestParam String studentId,@RequestParam String studyManegerId) throws Exception{
		return studentService.updateStudentStudyManager(studentId ,studyManegerId);
	}
	/**
	 * 学管师替换
	 * @return
	 */
	@RequestMapping(value="/studyManegerReplace")
	@ResponseBody
	public Response studyManegerReplace(String studentIds,String oldStudyManagerId,String newStudyManagerId){
		studentService.updateStudentManeger(studentIds, oldStudyManagerId, newStudyManagerId);
		
		return new Response();
	}
	
	
	/**
	 * 根据学生id查询剩余课时>0的合同
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value="/getContractByStudentId")
	@ResponseBody
	public List<ContractVo> getContractByStudentId(@RequestParam String studentId){
		return contractService.getContractByStudentId(studentId);
	}
	
	/**
	 * 查询
	 * @param term
	 * @return
	 */
	@RequestMapping(value="/findStudentForAutoCompelete")
	@ResponseBody
	public String findStudentForAutoCompelete(@RequestParam String term) {
		List<StudentVo> vos = studentService.findStudentsByNameOrId(term);
		String autoCompeleteData = "";
		for (StudentVo student : vos) {
			autoCompeleteData += "\"" + student.getName() + "-" + student.getId() + "\",";
		}
		if (autoCompeleteData.length() > 0) {
			autoCompeleteData = autoCompeleteData.substring(0, autoCompeleteData.length() - 1);
		}
		return "[" + autoCompeleteData + "]";
	}
	
	
	/**
	 * 查询排课需求
	 * 根据所属校区和学生姓名或id 查询排课需求
	 * @param term
	 * @return
	 */
	@RequestMapping(value="/findCourseRequirement")
	@ResponseBody
	public List<StudentVo> findCourseRequirement( String term) {
		Organization org=userService.getBelongCampus();
		if(org==null){
			return null;
		}
		List<StudentVo> vos = studentService.findStudentByCourseRequirement(term,org.getId());
		return vos ;
	}
	

	/**
	 * 根据学生id查询未排课排课需求
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value="/getCourseRequirementByStudentIdAndWaitArrenge")
	@ResponseBody
	public List<Map<String,String>> getCourseRequirementByStudentIdAndWaitArrenge(@RequestParam String studentId){
		return studentService.getCourseRequirementByStudentIdAndWaitArrenge(studentId);
	}
	
	/**
	 * 根据学生id查询排课概要
	 * @param studentId
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value="/getCourseSummaryByStudentId")
	@ResponseBody
	public List<CourseSummarySearchResultVo> getCourseSummaryByStudentId(@RequestParam String studentId,@RequestParam(required=false) String endDate){
		return studentService.getCourseSummaryByStudentId(studentId, endDate);
	}
	
	/**
	 * 根据学生id查询学生帐户情况
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value="/getStudentAccoutInfo")
	@ResponseBody
	public StudnetAccMvVo getStudentAccoutInfo(String studentId) {
		return studentService.getStudentAccoutInfo(studentId);
	}
	
	/**
	 * 学生学校
	 * @param gridRequest
	 * @param studentSchool
	 * @return
	 */
	@RequestMapping(value="/getStudentSchoolList")
	@ResponseBody
	public DataPackageForJqGrid getStudentSchoolList(GridRequest gridRequest, StudentSchool studentSchool){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentService.getStudentSchoolList(studentSchool, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}

	/**
	 * 待审核学生学校
	 * @param gridRequest
	 * @param studentSchoolTemp
	 * @return
	 */
	@RequestMapping(value="/getSchoolTempList")
	@ResponseBody
	public DataPackageForJqGrid getSchoolTempList(GridRequest gridRequest, StudentSchoolTemp studentSchoolTemp){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentService.getSchoolTempList(studentSchoolTemp, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	/**
	 * 获取学生成绩信息 
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/getStudentScoreList")
	@ResponseBody
	public DataPackageForJqGrid getStudentScoreList(GridRequest gridRequest,StudentScore studentScore,BigDecimal minScore,BigDecimal maxScore ) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentService.getStudentScoreList(studentScore,dataPackage,minScore,maxScore);
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value = "/importStudentSchoolFromExcel")
	@ResponseBody
	public Response importStudentSchoolFromExcel(@RequestParam("path") MultipartFile path,HttpServletRequest request) throws Exception {

		String rootPath=getClass().getResource("/").getFile().toString();
		String fileName=path.getOriginalFilename().substring(0,path.getOriginalFilename().lastIndexOf("."))
				+"_"+ DateTools.getCurrentExactDateTime()+path.getOriginalFilename().substring(path.getOriginalFilename().lastIndexOf("."));
//		String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("target"))+"/target/eduboss/uploadfile/"+fileName;
		String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/"+fileName;
		File file = new File(uploadFile);
		if ((int)(path.getSize() /1024/1024)+1>2){//限制2Mb
			return new Response(0, "文件过大！不能超过2Mb");
		}
		uploadFile=file.getPath().replaceAll("%20", " ");

		boolean  isUploadFinish = FileUtil.readInputStreamToFile(path.getInputStream(),uploadFile);
		Map<Boolean, String> resultMap = new HashMap();
		if (isUploadFinish){
			resultMap = studentService.importStudentSchoolFromExcel(new File(uploadFile));
			String fileUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/uploadfile/"+fileName;
			if (resultMap.containsKey(Boolean.TRUE)){
				return new Response(0, fileUrl);
			}else {
				return new Response(0, resultMap.get(Boolean.FALSE).toString());
			}
		}else {
			return new Response(0, "上传失败！");
		}
	}
	
	@RequestMapping(value = "/editStudentSchool")
	@ResponseBody
	public Response editStudentSchool(@ModelAttribute GridRequest gridRequest, StudentSchool studentSchool) {
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			studentService.deleteStudentSchool(studentSchool);
		} else {
			studentService.saveOrUpdateStudentSchool(studentSchool);
		}
		return new Response();
	}

	@RequestMapping(value = "/saveNewSchool")
	@ResponseBody
	public Response saveNewSchool(@ModelAttribute GridRequest gridRequest, StudentSchool studentSchool, @RequestParam String schoolTempId){
		studentService.saveNewSchool(studentSchool, schoolTempId);
		return new Response();
	}

	@RequestMapping(value = "/unvalidSchool")
	@ResponseBody
	public Response  unvalidSchool(@RequestParam String schoolTempId){
		studentService.unvalidSchool(schoolTempId);
		return new Response();
	}
	
	@RequestMapping(value="/editStudentScore")
	@ResponseBody
	public Response editStudentScore(@ModelAttribute GridRequest gridRequest,StudentScore studentscoremanage,String[] subjects,BigDecimal[] scores,String[] classRanges,String[] gradeRanges){
		if("del".equalsIgnoreCase(gridRequest.getOper())){
			studentService.deleteStudentScore(studentscoremanage);
		}else{
			studentService.saveOrUpdateStudentScore(studentscoremanage,subjects, scores, classRanges,gradeRanges);
		}
		return new Response();
	}

	@ResponseBody
	@RequestMapping(value = "/findStudentSchoolById")
	public StudentSchoolVo findStudentSchoolById(@RequestParam String id) {
		return studentService.findStudentSchoolById(id);
	}

	@ResponseBody
	@RequestMapping(value = "/findSchoolTempById")
	public SchoolTempVo findSchoolTempById(@RequestParam String id){
		return studentService.findSchoolTempById(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findStudentScoreById")
	public StudentScoreVo findStudentScoreById(@RequestParam String id) {
		return studentService.findStudentScoreById(id);
	}
	
	/**
	 * 获取学生学校autoComplete
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "/getAutoCompleteByStudentSchool", method =  RequestMethod.GET)
	@ResponseBody
	public List<AutoCompleteOptionVo> getAutoCompleteByStudentSchool(@RequestParam String term) {
		List<StudentSchoolVo> resList = studentService.getStudentSchoolForAutoCompelate(term);
		List<AutoCompleteOptionVo> optionVos =  new ArrayList<AutoCompleteOptionVo>();
		for(StudentSchoolVo cus: resList){
			AutoCompleteOptionVo auto=new AutoCompleteOptionVo();
			auto.setLabel(cus.getName()+"（"+cus.getRegionName()+"-"+cus.getSchoolTypeName()+"）");
			auto.setValue(cus.getId());
			optionVos.add(auto);
		}
		return optionVos;
	}

	/**
	 * 获取学生校区信息
	 * @param blCampusId
	 * @return
     */
	@RequestMapping(value = "/getCampusInfoByStudentId", method =  RequestMethod.GET)
	@ResponseBody
	public StuInfoForFundsVo getCampusInfoByStudentId(@RequestParam String blCampusId){
		return studentService.getCampusInfoByCampusId(blCampusId);
	}

    /**
     * 查找所有学生with课程数量
     * @return
     */
    @RequestMapping(value = "/findStudentWithCourseCount", method =  RequestMethod.GET)
    @ResponseBody
    public List<CurtailStudentVo> findStudentWithCourseCount(CourseVo courseVo){
    	List<StudentVo> studentVoList = studentService.findStudentWithCourseCount(courseVo);
    	List<CurtailStudentVo> curtialStudentVoList = new ArrayList<CurtailStudentVo>();
    	for (StudentVo studentVo: studentVoList) {
    		CurtailStudentVo curtailStudentVo = HibernateUtils.voObjectMapping(studentVo, CurtailStudentVo.class);
    		curtialStudentVoList.add(curtailStudentVo);
    	}
        return curtialStudentVoList;
    }

    /**
	 * 查询可以地图显示的学生
	 * @param studentvo
	 * @param modelVo
	 * @return
     */
    @RequestMapping(value = "/findStudentForMapView", method =  RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> findStudentForMapView(StudentVo studentvo, ModelVo modelVo) {
    	return studentService.findStudentForMapView(studentvo, modelVo);
    }
    
    /**
     * 维护学生地理位置信息
     * @param studentId
     * @param address
     * @param log
     * @param lat
     * @return
     */
    @ResponseBody
	@RequestMapping(value ="/editStudentMapInfo")
	public Response editStudentMapInfo(@RequestParam String studentId, @RequestParam String address, @RequestParam String log, @RequestParam String lat){
		return studentService.editStudentMapInfo(studentId, address, log, lat);
	}
    
    @ResponseBody
   	@RequestMapping(value ="/getStudentListByName")
    public List<StudentVo> getStudentListByName(String studentName,String brenchType,String conCampusId){
    	return studentService.getStudentListByName(studentName,brenchType,conCampusId);
    }


	@ResponseBody
	@RequestMapping(value ="/getStudentGridByName")
	public DataPackageForJqGrid getStudentGridByName(GridRequest gridRequest, String studentName,String brenchType,String conCampusId){
		DataPackage dataPackage = new DataPackage(gridRequest);
		List<StudentVo> list = studentService.getStudentListByName(studentName, brenchType, conCampusId);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(list==null?0:list.size());
		return new DataPackageForJqGrid(dataPackage);
	}


	/**
	 * 插多两个数目，优惠&不优惠
	 * @param studentId
	 * @param returnNormalAmount
	 * @param returnPromotionAmount
	 * @param withDrawAmount
	 * @param returnReason
	 * @param returnType
	 * @param cbsIdA
	 * @param userIdA
	 * @param bonusA
	 * @param bonusAA
	 * @param cbsIdB
	 * @param userIdB
	 * @param bonusB
	 * @param bonusBB
	 * @param cbsIdC
	 * @param userIdC
	 * @param bonusC
	 * @param bonusCC
	 * @param schoolA
	 * @param schoolB
	 * @param schoolC
	 * @return
	 */
	@ResponseBody
   	@RequestMapping(value ="/withDrawElectronicAmount")
    public Response withDrawElectronicAmount(@RequestParam String studentId, @RequestParam(defaultValue = "0") BigDecimal returnNormalAmount, @RequestParam(defaultValue = "0") BigDecimal returnPromotionAmount,
											 @RequestParam BigDecimal withDrawAmount,String returnReason,String returnType,
    		String cbsIdA, String userIdA, String bonusA, String bonusAA, String cbsIdB, String userIdB, String bonusB, String bonusBB,
			String cbsIdC, String userIdC, String bonusC, String bonusCC, String schoolA, String schoolB, String schoolC, 
			@RequestParam(required=false)MultipartFile certificateImageFile,String accountName,String account) {
		Response response = new Response();
		returnNormalAmount = returnNormalAmount.equals(BigDecimal.ZERO)?withDrawAmount:returnNormalAmount;
		BigDecimal retAmount = studentService.withDrawElectronicAmount(studentId, returnNormalAmount, BigDecimal.ZERO, returnPromotionAmount, withDrawAmount, returnReason, returnType,
                null, certificateImageFile,accountName,account, null, null,  null);
		response.setResultMessage(retAmount.toString());
		return response;
	}
    
    @ResponseBody
   	@RequestMapping(value ="/putMoneyIncome")
    public Response putMoneyIncome(@RequestParam String studentId,@RequestParam BigDecimal withDrawAmount, String reason) {
    	Response response=new Response();
    	BigDecimal retAmount= studentService.putMoneyIncome(studentId, withDrawAmount, reason);
    	response.setResultMessage(retAmount.toString());
    	return response;
    }
    
    /**
     * 获取学生资金变更记录列表
     * */
    @RequestMapping(value = "/getStudentMoneyChanges")
    @ResponseBody
    public DataPackageForJqGrid getStudentMoneyChanges(HttpServletRequest request,StudentMoneyChanges studentMoneyChanges, GridRequest gridRequest){
    	Map<String,Object> map = new HashMap<String,Object>();
    	String startTime="",endTime="",studentId="";
    	if(StringUtil.isNotEmpty(request.getParameter("startTime"))){startTime=request.getParameter("startTime");}
    	if(StringUtil.isNotEmpty(request.getParameter("endTime"))){endTime=request.getParameter("endTime");}
    	if(StringUtil.isNotEmpty(request.getParameter("studentId"))){studentId=request.getParameter("studentId");}
    	map.put("startTime", startTime);
    	map.put("endTime", endTime);
    	map.put("studentId", studentId);
    	
    	DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentService.getStudentMoneyChanges(studentMoneyChanges,map, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
    }
    
    /**
     * 学生回访跟进记录
     * */
    @RequestMapping(value = "/getStudentFollowUp")
    @ResponseBody
    public DataPackageForJqGrid getStudentFollowUp(StudentFollowUpVo studentFollowUpVo, GridRequest gridRequest){
    	DataPackage dataPackage = new DataPackage(gridRequest);
    	dataPackage = studentService.getStudentFollowUp(studentFollowUpVo, dataPackage);
    	return new DataPackageForJqGrid(dataPackage);
    }

	@RequestMapping(value = "/getStudentFollowUpById")
	@ResponseBody
	public StudentFollowUpVo getStudentFollowUpById(String id){
		return studentService.getStudentFollowUpById(id);
	}

	@RequestMapping(value = "/delStudentFollowUpById",method = RequestMethod.POST)
	@ResponseBody
	public Response delStudentFollowUpById(String id){
		return studentService.delStudentFollowUpById(id);
	}

	/**
	 * 保存学生回访跟进记录
	 * */
	@RequestMapping(value = "/saveStudentFollowUp")
	@ResponseBody
	public Response savaStudentFollowUp(StudentFollowUp studentFollowUp){
		studentService.saveStudentFollowUp(studentFollowUp);
		return new Response();
	}
    
   /**
    * 获取学生电子账户记录
    * */
    @RequestMapping(value = "/getElectronicAccountChangeLogByStudentId")
    @ResponseBody
    public List<ElectronicAccountChangeLogVo> getElectronicAccountChangeLogByStudentId(ElectronicAccountChangeLogVo electronicAccountChangeLogVo){
	   return studentService.getElectronicAccountChangeLogByStudentId(electronicAccountChangeLogVo);
    }
    
    //学生转校区
    @RequestMapping(value = "/studentTurnCampus")
	@ResponseBody
	public Response studentTurnCampus(@ModelAttribute GridRequest gridRequest, 
			TransactionCampusRecord transactionRecord) throws Exception {
		studentService.updateStudentTrunCampus(transactionRecord);
		return new Response();
	}

    @RequestMapping(value = "/getStudentOrganization")
	@ResponseBody
    public List<StudentOrganizationVo> getStudentOrganization(@RequestParam String studentId){
    	return studentService.getStudentOrganization(studentId);
    }
    
    @RequestMapping(value = "/saveStudentOrganization", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveStudentOrganization(@ModelAttribute GridRequest gridRequest,
			@RequestBody StudentOrganizationForm studentOrganizationForm) throws Exception {
		studentService.saveStudentOrganization(studentOrganizationForm);
		return new Response();
	}
    
    @RequestMapping(value = "/editStudentOrganization", method =  RequestMethod.POST)
   	@ResponseBody
   	public Response editStudentOrganization(@RequestBody StudentOrganizationVo studentOrganizationVo) throws Exception {
    	if ("del".equalsIgnoreCase(studentOrganizationVo.getOper())) {
    		// 由于增加了一对对 删除是否要限制
    		studentService.deleteStudentOrganization(studentOrganizationVo);
		} else {
			studentService.saveOrUpdateStudentOrganization(studentOrganizationVo);
		}
   		return new Response();
   	}

    @RequestMapping(value = "/getStudentByTeacherAndSubject")
    @ResponseBody
    public List<StudentVo> getStudentByTeacherAndSubject(@RequestParam String teacherId,@RequestParam String subjectId){
        return studentService.getStudentByTeacherAndSubject(teacherId,subjectId);
    }

    /**
     * 根据老师、科目和产品获取可消费的学生
     * @param teacherId
     * @param subjectId
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getCanConsumeStudents")
    @ResponseBody
    public List<NameValue> getCanConsumeStudents(@RequestParam String teacherId,@RequestParam String subjectId,@RequestParam String productId){
		List<StudentVo> studens = studentService.getCanConsumeStudents(teacherId, subjectId, productId);
		List<NameValue> nvs = new ArrayList<NameValue>();
		for (StudentVo so : studens) {
			nvs.add(SelectOptionResponse.buildNameValue(so.getName(), so.getValue()));
		}
		
		return nvs;
    }
    
    /**
     * 查找当前校区所有学生
     * @return
     */
    @RequestMapping(value = "/findStudentByCampus", method =  RequestMethod.GET)
    @ResponseBody
    public List<NameValue> findStudentByCampus(){
    	List<StudentVo> studens =  studentService.findStudentByCampus();
        List<NameValue> nvs = new ArrayList<NameValue>();
		for (StudentVo so : studens) {
			nvs.add(SelectOptionResponse.buildNameValue(so.getName(), so.getValue()));
		}
		
		return nvs;
    }
    
    /**
     * 查找所有学生
     * @param term
     * @return
     */
    @RequestMapping(value = "/getStudentAutoComplateAll", method =  RequestMethod.GET)
    @ResponseBody
    public List<AutoCompleteOptionVo> getStudentAutoComplateAll(@RequestParam String term){
    	return studentService.getStudentAutoComplateAll(term);
    }

    /**
	 * 查找某校区的学生
	 * @param campusId
	 * @return
     */
    @RequestMapping(value = "/findStudentsByCampusId", method =  RequestMethod.GET)
    @ResponseBody
    public List<NameValue> findStudentsByCampusId(@RequestParam String campusId){
    	List<StudentVo> studens =  studentService.findStudentsByCampusId(campusId);
        List<NameValue> nvs = new ArrayList<NameValue>();
		for (StudentVo so : studens) {
			nvs.add(SelectOptionResponse.buildNameValue(so.getName(), so.getValue()));
		}
		
		return nvs;
    }


    /**
	 * 根据学生Id找到客户信息以及学生姓名
	 * @param studentId
	 * @return
     */
    @RequestMapping(value = "/getCusAndStuByStudentId", method =  RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> getCusAndStuByStudentId(@RequestParam String studentId){
    	return studentService.getCusAndStuByStudentId(studentId);
    }
    
    /**
     * 一对多学生列表
     */
    @RequestMapping(value="/getOtmClassStudents")
	@ResponseBody
	 public DataPackageForJqGrid getOtmClassStudents(@ModelAttribute GridRequest gridRequest,@ModelAttribute StudentVo studentvo,
			 @ModelAttribute OtmClassVo otmClassVo,
			 	String brenchId, ModelVo modelvo,
			 	String stuNameGradeSchool){
		 DataPackage dataPackage=new DataPackage(gridRequest);
		 dataPackage=studentService.getOtmClassStudents(studentvo, otmClassVo, dataPackage, brenchId, null, modelvo, stuNameGradeSchool, null, null, null);
		 return new DataPackageForJqGrid(dataPackage);
	 }


	/**
	 *
	 * @return
	 */
	@RequestMapping(value="/matchingStudentSchool")
	@ResponseBody
	public Response  matchingStudentSchool(@RequestParam String id, @RequestParam String matchingSchool){
		studentService.matchingStudentSchool(id, matchingSchool);
		return new Response();
	}

	/**
	 *
	 * @param vo
	 * @return
	 */
	@RequestMapping(value="/getStudentAddressAndLatLog")
	@ResponseBody
	public Map<String, StudentMap>getStudentAddressAndLatLog(ParamVo vo){
		return studentService.getStudentAddressAndLatLog(vo);
	}
   
}
