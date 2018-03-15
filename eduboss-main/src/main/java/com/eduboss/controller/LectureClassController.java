package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.LectureClassAttendanceStatus;
import com.eduboss.domainVo.LectureClassStudentVo;
import com.eduboss.domainVo.LectureClassVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.LectureClassService;



@Controller
@RequestMapping(value = "/LectureClassController")
public class LectureClassController {

	@Autowired
	private LectureClassService lectureClassService;
	
	
	private final static Logger log = Logger.getLogger(LectureClassController.class);
	
	

	@RequestMapping(value ="/getLectureClassList")
	@ResponseBody
	public DataPackageForJqGrid getLectureClassList(@ModelAttribute GridRequest gridRequest,LectureClassVo vo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = lectureClassService.getLectureClassList(dataPackage,vo);
		DataPackageForJqGrid dpfj =  new DataPackageForJqGrid(dataPackage);
		return dpfj;
	}
	
	
	@RequestMapping(value="/saveOrUpdateLectureClass")
	@ResponseBody
	public Response saveOrUpdateLectureClass(@ModelAttribute GridRequest gridRequest, @ModelAttribute LectureClassVo lectureClassVo) throws Exception{
		Response res=new Response();
		res=lectureClassService.saveOrUpdateLectureClass(gridRequest.getOper(), lectureClassVo);
		return res;
	}
	
	
	@RequestMapping(value ="/findLectureClassById")
	@ResponseBody
	public LectureClassVo findLectureClassById(@RequestParam String lectureClassId) {
		return lectureClassService.findLectureClassById(lectureClassId);
	}
	
	@RequestMapping(value ="/delteLectureClass")
	@ResponseBody
	public Response delteLectureClass(@RequestParam String lectureClassId) {
		return lectureClassService.delteLectureClass(lectureClassId);
	}
	
	
	@RequestMapping(value ="/addStudentToLectureClass")
	@ResponseBody
	public Response addStudentToLectureClass(@ModelAttribute LectureClassStudentVo lectureClassStudentVo) {
		return lectureClassService.addStudentToLectureClass(lectureClassStudentVo);
	}
	
	@RequestMapping(value ="/getLectureClassStudentList")
	@ResponseBody
	public DataPackageForJqGrid getLectureClassStudentList(@ModelAttribute GridRequest gridRequest,LectureClassStudentVo vo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = lectureClassService.getLectureClassStudentList(dataPackage,vo);
		DataPackageForJqGrid dpfj =  new DataPackageForJqGrid(dataPackage);
		return dpfj;
	}
	
	@RequestMapping(value ="/deleteStudentFromLectureClass")
	@ResponseBody
	public Response deleteStudentFromLectureClass(@RequestParam String lectureClassStudentId) {
		return lectureClassService.deleteStudentFromLectureClass(lectureClassStudentId);
	}
	
	@RequestMapping(value ="/removeStudentFromLecture")
	@ResponseBody
	public Response removeStudentFromLecture(@RequestParam String lectureId,@RequestParam String studentId) {
		return lectureClassService.removeStudentFromLecture(lectureId,studentId);
	}
	
	@RequestMapping(value="/getStudentWantListByLectureClassId")
	@ResponseBody
	public List<StudentVo> getStudentWantListByMiniClassId(@RequestParam String lectureClassId){
		return lectureClassService.getStudentWantListByLectureClassId(lectureClassId);
	}
	
	
	@RequestMapping(value="/getLectureClasssStudentByClassId")
	@ResponseBody
	public DataPackageForJqGrid getLectureClasssStudentBySmallClassId(@RequestParam String lectureClassId){
		List<StudentVo> list=lectureClassService.getLectureClasssStudentByClassId(lectureClassId);
		DataPackage dataPackage = new DataPackage(0,list.size());
		dataPackage.setDatas(list);
		return  new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value="/getStudentWantListByBranchAndType")
	@ResponseBody
	public List<Map<Object ,Object>> getStudentWantListByBranchAndType(@RequestParam String lectureClassId,@RequestParam String productType){
		return lectureClassService.getStudentWantListByBranchAndType(lectureClassId,productType);
	}
	
	
	@RequestMapping(value="/auditLectureClassStudent")
	@ResponseBody
	public Response auditLectureClassStudent(@RequestParam String ids,@RequestParam LectureClassAttendanceStatus auditStatus){
		return lectureClassService.auditLectureClassStudent(ids,auditStatus);
	}
	
	@RequestMapping(value="/chargeLectureClassStudent")
	@ResponseBody
	public Response chargeLectureClassStudent(@RequestParam String ids){
		return lectureClassService.chargeLectureClassStudent(ids);
	}
	
}
