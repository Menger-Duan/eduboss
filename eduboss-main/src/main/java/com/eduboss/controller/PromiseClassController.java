package com.eduboss.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.dto.*;
import com.eduboss.service.SmallClassService;
import org.directwebremoting.guice.RequestParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.eduboss.common.ProductType;
import com.eduboss.service.PromiseClassService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;


@Controller
@RequestMapping(value = "/PromiseClassController")
public class PromiseClassController {

	@Autowired
	private PromiseClassService promiseService;

	@Autowired
	private SmallClassService smallClassService;

	/**
	 * 查询目标班列表
	 * */
	@RequestMapping(value = "/getPromiseClassList")
	@ResponseBody
	public DataPackageForJqGrid getPromiseClassList(@ModelAttribute PromiseClass promiseClass, @ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = promiseService.getPromiseClassList(promiseClass, dataPackage);
		DataPackageForJqGrid dpfj =  new DataPackageForJqGrid(dataPackage);
		return dpfj;
		
	}
	
	
	/**
	 * 查询目标班学生列表
	 * */
	@RequestMapping(value = "/getPromiseStudentList")
	@ResponseBody
	public DataPackageForJqGrid getPromiseStudentList(@ModelAttribute PromiseStudentSearchDto dto, GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = promiseService.getPromiseStudentList(dto, dataPackage);
		DataPackageForJqGrid dpfj = new DataPackageForJqGrid(dataPackage);
		return dpfj;
	}

	@RequestMapping(value = "/getPromiseSubjectDetailList")
	@ResponseBody
	public List getPromiseSubjectDetailList(@RequestParam String promiseStudentId){
		return promiseService.getPromiseSubjectDetailList(promiseStudentId);
	}

	@RequestMapping(value = "/getEcsContractInfo")
	@ResponseBody
	public Map<Object,Object> getEcsContractInfo(@RequestParam String contractProductId){
		return promiseService.getEcsContractInfo(contractProductId);
	}

	/**
	 * 扣费详情
	 * @param contractProductId
	 * @return
	 */
	@RequestMapping(value = "/getEcsContractChargeInfo")
	@ResponseBody
	public Map<Object,Object> getEcsContractChargeInfo(@RequestParam String contractProductId){
		return promiseService.getEcsContractChargeInfo(contractProductId);
	}


	/**
	 * 中途退费
	 * @param promiseStudent
	 * @return
	 */
	@RequestMapping(value = "/savaPromiseReturnAuditInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response  savaPromiseReturnAuditInfo(@ModelAttribute PromiseStudent promiseStudent){
		return promiseService.savaPromiseReturnAuditInfo(promiseStudent);
	}

	/**
	 * 保存新增目标班信息
	 * */
	@RequestMapping(value = "/savePromiseClass")
	@ResponseBody
	public PromiseClass savePromiseClass(@ModelAttribute PromiseClass promiseClass){
		 return promiseService.savePromiseClass(promiseClass);
		//System.out.println("pSchool = " + promiseClass.getpSchool());
	}
	
	/**
	 * 学生报班
	 * */
	@RequestMapping(value = "/savaPromiveClassStudent")
	@ResponseBody
	public Response  savaPromiveClassStudent(@ModelAttribute PromiseStudent promiseStudent){
		promiseService.savaPromiveClassStudent(promiseStudent);
		//System.out.println(promiseStudent.toString());
		return new Response();
		
	}
	
	/**
	 * 目标班学生退班
	 * */
	@RequestMapping(value = "/cancelPromiseClassApply")
	@ResponseBody
	public Response cancelPromiseClassApply(@RequestParam String studentId,@RequestParam String promiseClassId,@RequestParam String contractProductId){
		promiseService.cancelPromiseClassApply(studentId, promiseClassId,contractProductId);
		return new Response();
		
	}
	
	/**
	 * 返回符合报目标班条件的学生(如果status不为空则为判断是否还有学生未结课，且status的值为“开通中”)
	 * */
	@RequestMapping(value = "/getPromiseClassStudentWantList")
	@ResponseBody
	public List<PromiseClassApplyVo> getStudentWantList(HttpServletRequest request,@RequestParam String grade,@RequestParam String promiseClassId){
		String status = "";
		String studentId="";
		String studentName="";
		if(StringUtil.isNotBlank(request.getParameter("status"))){
			status = request.getParameter("status");
		}
		if(StringUtil.isNotBlank(request.getParameter("studentId"))){
			studentId = request.getParameter("studentId");
		}
		if(StringUtil.isNotBlank(request.getParameter("studentName"))){
			studentName = request.getParameter("studentName");
		}
		return promiseService.getStudentWantList(ProductType.ECS_CLASS, grade,promiseClassId,status,studentId,studentName);
	}
	
	/**
	 * 按照班级ID查询班级所有学生
	 * */
	@RequestMapping(value = "/getStudentListByPromiseClassId")
	@ResponseBody
	public DataPackageForJqGrid getStudentListByPromiseClassId(HttpServletRequest request,@RequestParam String promiseClassId,GridRequest gridRequest){
		String apply = "";
		if(StringUtil.isNotBlank(request.getParameter("apply"))){
			apply = request.getParameter("apply");
		}
//		DataPackage dp = new DataPackage(gridRequest);
		DataPackage dp = new DataPackage(0, 999);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("promiseClassId", promiseClassId);
		params.put("apply", apply);
		dp = promiseService.getStudentListByPromiseClassId(params,dp);
		DataPackageForJqGrid dpfj = new DataPackageForJqGrid(dp);
		return dpfj;
	}
	
	/**
	 * 目标班月结记录
	 * */
	@RequestMapping(value = "/savePromiseClassRecord")
	@ResponseBody
	public PromiseClassRecordVo savePromiseClassRecord(PromiseClassRecord record,PromiseClassDetailRecord detail,@RequestParam String contractProductId,String dRecordId,String recordId){
//		System.out.println(record);
//		PromiseClassDetailRecord pcdr = record.getClassDetail();
//			System.out.println("classType = "+pcdr.getClassType());
//			System.out.println("subject = "+pcdr.getSubject());
//			System.out.println("courseHours = "+pcdr.getCourseHours());
//			System.out.println("teacher = "+pcdr.getTeacher());
		if(!"".equals(recordId) && recordId !=null){
			record.setId(recordId);
		}
		if(!"".equals(dRecordId) && dRecordId!=null){
			detail.setId(dRecordId);
		}
		PromiseClassRecordVo recordVo = new PromiseClassRecordVo();
		PromiseClassRecord cRecord =  promiseService.savaPromiseClassRecord(record,detail,contractProductId);
		recordVo = HibernateUtils.voObjectMapping(cRecord, PromiseClassRecordVo.class);
		//return new Response();
		return recordVo;
	}
	
	/**
	 * 目标班结课
	 * */
	@RequestMapping(value = "/endPromiseClass")
	@ResponseBody
	public Response endPromiseClass(@RequestParam String promiseClassId){
		System.out.println(promiseClassId);
		promiseService.endPromiseClass(promiseClassId);
		return new Response();
	}
	
	/**
	 * 合同完结时判断是否还有未扣款的月份的学生
	 * */
	@RequestMapping(value = "/getStudentRecordIsInProgress")
	@ResponseBody
	public String getStudentRecordIsInProgress(@RequestParam String studentId,String contractProductId){
		return promiseService.getStudentRecordIsInProgress(studentId,contractProductId);
	}
	

	/**
	 * 根据目标班ID和学生ID查询月结记录信息
	 * */
	@RequestMapping(value = "/findPromiseClassRecordByProClaIdAndStuId")
	@ResponseBody
	public List<PromiseClassRecordVo> findPrmoseClassRecordByProClaIdAndStuId(@RequestParam String promiseClassId,@RequestParam String studentId){
		return promiseService.findPrmoseClassRecordByProClaIdAndStuId(promiseClassId, studentId);
	}
	
	
	/**
	 * 根据合同产品和学生ID查询月结记录信息
	 * */
	@RequestMapping(value = "/findPromiseClassRecordByConProIdAndStuId")
	@ResponseBody
	public List<PromiseClassRecordVo> findPromiseClassRecordByConProIdAndStuId(@RequestParam String contractProductId,@RequestParam String studentId){
		return promiseService.findPromiseClassRecordByConProIdAndStuId(contractProductId, studentId);
	}
	
	
	/**
	 * 班主任学生管理界面点详情按钮，查出该学生的合同信息
	 * */
	@RequestMapping(value = "/findStudentContractInfo")
	@ResponseBody
	public List<PromiseClassApplyVo> findStudentContractInfo(@RequestParam String studentId,@RequestParam String contractProductId){
		//System.out.println("************************************************************************************************");
		//System.out.println(contractProductId);
		return promiseService.findStudentContractInfo(studentId,  contractProductId);
	}

	/**
	 * 查询扣费详情
	 * */
	@RequestMapping(value = "/getEcsContractChargeList")
	@ResponseBody
	public DataPackageForJqGrid getEcsContractChargeList(@RequestParam String contractProductId,@ModelAttribute PromiseChargeSearchDto dto, GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage= promiseService.getEcsContractChargeList(contractProductId,dto,dataPackage);
		DataPackageForJqGrid dpfj = new DataPackageForJqGrid(dataPackage);
		return dpfj;
	}

	/**
	 * 合同完结
	 * */
	@RequestMapping(value = "/endPromiseClassContract")
	@ResponseBody
	public Response endPromiseClassContract(PromiseClassRecord record,@RequestParam String resultStatus){
		promiseService.endPromiseClassContract(record,resultStatus);
		return new Response();
	}
	
	/**
	 * 查询学生月结详情信息
	 * */
	@RequestMapping(value = "/findStudentMonthlyDetailInfo")
	@ResponseBody
	public List<PromiseClassDetailRecordVo> findStudentMonthlyDetailInfo(PromiseClassRecord promiseClassRecord){
		return promiseService.findStudentMonthlyDetailInfo(promiseClassRecord);
		
	}
	
	/**
	 * 学生基本信息（结课页面显示）
	 * */
	@RequestMapping(value = "/getPromiseStudentInfo")
	@ResponseBody
	public List<PromiseClassApplyVo> getStudentInfo(PromiseStudent promiseStudent){
		return promiseService.getPromiseStudentInfo(promiseStudent);
	}
	
	/**
	 * 获取可以转目标班的校区
	 * */
	@RequestMapping(value = "/getCanTurnToClassOrganization")
	@ResponseBody
	public List<OrganizationVo> getCanTurnToClassOrganization(String promiseStudentId){
		return promiseService.getCanTurnToClassOrganization(promiseStudentId);
	}
	
	/**
	 * 获取可以转目标班的校区
	 * */
	@RequestMapping(value = "/getPromiseClassByCampus")
	@ResponseBody
	public List<PromiseClassVo> getPromiseClassByCampus(String campusId){
		return promiseService.getPromiseClassByCampus(campusId);
	}
	
	
	
	/**
	 * 转换目标班
	 * */
	@RequestMapping(value = "/turnPromiseClass",method = RequestMethod.POST)
	@ResponseBody
	public Response turnPromiseClass(String promiseStudentId,String turnPromiseClassId,String newCourseDate){
		return promiseService.turnPromiseClass(promiseStudentId,turnPromiseClassId,newCourseDate);
	}


	/**
	 * 保存科目分配
	 * */
	@RequestMapping(value = "/savePromiseSubject",method = RequestMethod.POST)
	@ResponseBody
	public Response savePromiseSubject(@RequestBody PromiseClassStudentDto dto){
		return promiseService.savePromiseSubject(dto.getSubjectVos(),dto.getPromiseStudentId());
	}

	/**
	 * 科目报班
	 * */
	@RequestMapping(value = "/savePromiseToMiniClass",method = RequestMethod.POST)
	@ResponseBody
	public Response savePromiseToMiniClass(PromiseClassSubjectVo vo){
		return promiseService.savePromiseToMiniClass(vo);
	}


	/**
	 * 查询目标班科目分配列表
	 * */
	@RequestMapping(value = "/getPromiseSubjectList")
	@ResponseBody
	public DataPackageForJqGrid getPromiseSubjectList(@RequestParam String promiseStudentId,GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp.setPageSize(999);
		dp = promiseService.getPromiseSubjectList(dp,promiseStudentId);
		DataPackageForJqGrid dpfj = new DataPackageForJqGrid(dp);
		return dpfj;
	}


	/**
	 * 查询可报读小班列表
	 * */
	@RequestMapping(value = "/findMiniClassByPromiseSubjectId")
	@ResponseBody
	public DataPackageForJqGrid findMiniClassByPromiseSubjectId(@RequestParam Integer promiseSubjectId,GridRequest gridRequest,String teacherId,String organizationId,String name ){
		DataPackage dp = new DataPackage(gridRequest);
		dp = promiseService.findMiniClassByPromiseSubjectId(dp,promiseSubjectId,teacherId,organizationId,name);
		DataPackageForJqGrid dpfj = new DataPackageForJqGrid(dp);
		return dpfj;
	}

	/**
	 * 查询小班正常的老师列表
	 * */
	@RequestMapping(value = "/findMiniClassTeacherForSelect")
	@ResponseBody
	public List<Map<Object,Object>> findMiniClassTeacherForSelect(String name){
		return smallClassService.findMiniClassTeacherForSelect(name);
	}

	/**
	 * 科目报班
	 * */
	@RequestMapping(value = "/deleteStudentFromClass",method = RequestMethod.POST)
	@ResponseBody
	public Response deleteStudentFromClass(Integer id){
		return promiseService.deleteStudentFromClass(id);
	}


	/**
	 * 查询可报读小班列表
	 * */
	@RequestMapping(value = "/findStudentInfoByPromiseStudentId")
	@ResponseBody
	public PromiseStudentCustomerVo findStudentInfoByPromiseStudentId(@RequestParam String promiseStudentId){
		return promiseService.findStudentInfoByPromiseStudentId(promiseStudentId);
	}


	/**
	 * 发起完结合同审批
	 * @param promiseStudentId
	 * @return
	 */
	@RequestMapping(value = "/startAuditPromiseStudent")
	@ResponseBody
	public Response startAuditPromiseStudent(@RequestParam String promiseStudentId,@RequestParam String resultStatus){
		return promiseService.startAuditPromiseStudent(promiseStudentId,resultStatus);
	}


	/**
	 * 审批通过
	 * @param auditId
	 * @param resultStatus
	 * @return
	 */
	@RequestMapping(value = "/confirmAuditPromiseStudent")
	@ResponseBody
	public Response confirmAuditPromiseStudent(@RequestParam int auditId, @RequestParam String resultStatus,String remark){
		return promiseService.confirmAuditPromiseStudent(auditId,resultStatus,remark);
	}

}
