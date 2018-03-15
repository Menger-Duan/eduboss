package com.eduboss.controller;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eduboss.service.ImportContractStuAndCusService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.ReadExcel;



/**
 * 文件上传
 * @author ndd
 *
 */
@Controller
@RequestMapping(value="/ImportContractStuAndCusController")
public class ImportContractStuAndCusController {
	
	@Autowired
	private ImportContractStuAndCusService importContractStuAndCusService;
	
	
	@RequestMapping(value="/importContractStuAndCusData")
	public ModelAndView importContractStuAndCusData(@RequestParam(value = "file") MultipartFile file,String fileType,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException{
		String importMes="";
		int sheetAt=0;
		int writeRow=3;
		String fileUrl=null;
		if("miniClassFile".equals(fileType)){
			sheetAt=1;
			writeRow=2;
		}	
		if(".xlsx".equals(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase())
				|| ".xls".equals(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase())){
			String rootPath=getClass().getResource("/").getFile().toString();  
			String fileName=file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."))
					+"_"+DateTools.getCurrentExactDateTime()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String uploadFile=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/"+fileName;
			uploadFile=new File(uploadFile).getPath().replaceAll("%20", " ");
			boolean  isUploadFinish = FileUtil.readInputStreamToFile(file.getInputStream(),uploadFile);
			if(isUploadFinish){
				List<List<Object>> list=ReadExcel.readExcel(new File(uploadFile),sheetAt);
				List<Map<String, String>> resList = null;
				
				if("miniClassFile".equals(fileType))
					resList=importContractStuAndCusService.saveImportMiniClass(list);
				else
					resList=importContractStuAndCusService.saveImportContractStuAndCusData(list);
				
				if(resList!=null && resList.size()>0){
						ReadExcel.writeExcel(resList, uploadFile,sheetAt,writeRow);
				}
				int currentImpSuccuess=0;
				int oldImpSuccess=0;
				int currentError=0;
				for(Map<String,String> map : resList){
					if("成功".equals(map.get("isSave"))){
						currentImpSuccuess++;
					}else if("old成功".equals(map.get("isSave"))){
						oldImpSuccess++;
					}else if("失败".equals(map.get("isSave"))){
						currentError++;
					}
				}
				importMes="本次上传成功"+currentImpSuccuess+"条数据，上传失败"+currentError+"条数据";
				//if(currentError>0){
					fileUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/uploadfile/"+fileName;
				//}
			}else{
				importMes="上传失败，请联系管理员";
			}
		}else{
			importMes="数据格式错误";
		}
		session.setAttribute("importResult", importMes);
		if(fileUrl!=null){
			session.setAttribute("fileUrl", fileUrl);
		}
		
		return new ModelAndView("redirect:/function/dataimport/importResult.jsp");
	}
	
	
}
