package com.eduboss.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.StudentFileVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.StudentService;

@RequestMapping(value="/StudentDocumentController")
@Controller
public class StudentDocumentController {
	
//	@Autowired
//	private StudentDocumentService studentDocumentService;	
//	
	@Autowired
	private StudentService studentService;
	
	 /**
     * 学生档案记录
     * */
//    @RequestMapping(value = "/getStudentDocument")
//    @ResponseBody
//    public DataPackageForJqGrid getStudentDocument(StudentDocumentVo studentDocumentVo, GridRequest gridRequest){
//    	DataPackage dataPackage = new DataPackage(gridRequest);
//    	dataPackage = studentDocumentService.getStudentDocument(studentDocumentVo, dataPackage);
//    	return new DataPackageForJqGrid(dataPackage);
//    }
//    
	 /**
	   * 学生档案记录
	   * */
	  @RequestMapping(value = "/getStudentDocument")
	  @ResponseBody
	  public DataPackageForJqGrid getStudentDocument(StudentFileVo studentFileVo, GridRequest gridRequest,TimeVo timeVo,HttpServletRequest request){
	  	DataPackage dataPackage = new DataPackage(gridRequest);
	  	dataPackage = studentService.getStudentFileByPC(studentFileVo,dataPackage,timeVo);
	  	return new DataPackageForJqGrid(dataPackage); 	
	  }
    
    /**
     * 保存学生档案
     * */
//    @RequestMapping(value = "/saveStudentDocument")
//    @ResponseBody
//    public Response savaStudentDocument(StudentDocument studentDocument,@RequestParam("documentfile") MultipartFile documentfile){
//    	studentDocumentService.saveStudentDocument(studentDocument,documentfile);
//    	return new Response();
//    }
	
	  /**
	   * 保存学生档案
	   * */
	  @RequestMapping(value = "/saveStudentDocument")
	  @ResponseBody
	  public Response savaStudentDocument(StudentFileVo studentFileVo,@RequestParam("documentfile") MultipartFile documentfile,HttpServletRequest request){
		String servicePath = request.getSession().getServletContext().getRealPath("/");
		studentService.saveStudentFile(documentfile, studentFileVo,servicePath);	
	  	return new Response();
	  }
	
}
