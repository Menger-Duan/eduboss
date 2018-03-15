package com.eduboss.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.service.PosMachineManageService;
import com.eduboss.service.PosMachineService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.PropertiesUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Controller
@RequestMapping(value="/PosMachineController")
public class PosMachineController {

	@Autowired 
	private PosMachineService posMachineService;
	
	@Autowired
	private PosMachineManageService posMachineManageService;
	
	@ResponseBody
	@RequestMapping(value="/savePosMachineManage")
	public Response savePosMachineManage(GridRequest gridRequest,PosMachineVo posMachineVo) {
		return posMachineService.savePosMachineManage(posMachineVo);
	}
	
	@ResponseBody
	@RequestMapping(value="/editPosMachine")
	public Response editPosMachine(GridRequest gridRequest,PosMachineVo posMachineVo) {
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			posMachineService.deletePosMachine(posMachineVo);
		} else {
			return posMachineService.saveOrUpdatePosMachine(posMachineVo);
		}
		return new Response();
	}
	
	@ResponseBody
	@RequestMapping(value="/getPosMachineManageList")
	public DataPackageForJqGrid getPosMachineManageList(@ModelAttribute GridRequest gridRequest, @ModelAttribute PosMachineVo posMachineVo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = posMachineManageService.findPagePosMachineManage(dataPackage, posMachineVo);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@ResponseBody
	@RequestMapping(value="/getPosMachineList")
	public DataPackageForJqGrid getPosMachineList(@ModelAttribute GridRequest gridRequest, @RequestParam String posNumber) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = posMachineService.findPagePosMachine(dataPackage, posNumber);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@ResponseBody
	@RequestMapping(value="/disablePosMachineManage")
	public Response disablePosMachineManage(@RequestParam String posNumber) {
		posMachineManageService.disablePosMachineManage(posNumber);
		return new Response();
	}
	
	@ResponseBody
	@RequestMapping(value="/enablePosMachineManage")
	public Response enablePosMachineManage(@RequestParam String posNumber) {
		posMachineManageService.enablePosMachineManage(posNumber);
		return new Response();
	}
	
	@ResponseBody
	@RequestMapping(value="/findPosMachineManageByPosNumber")
	public PosMachineVo findPosMachineManageByPosNumber(@RequestParam String posNumber) {
		return posMachineManageService.findPosMachineManageByPosNumber(posNumber);
	}
	
	@ResponseBody
	@RequestMapping(value="/findPosMachineById")
	public PosMachineVo findPosMachineById(@RequestParam Integer id) {
		return posMachineService.findPosMachineById(id);
	}
	
	@RequestMapping(value = "/importPosPayDataFromExcel")
	@ResponseBody
	public Response importPosPayDataFromExcel(@RequestParam("path") MultipartFile path,HttpServletRequest request) throws Exception {
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
			resultMap = posMachineService.importPosPayDataFromExcel(new File(uploadFile));
//			String fileUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/uploadfile/"+fileName;
			AliyunOSSUtils.put("uploadfile_" + fileName, uploadFile);
			String aliyunFileUrl = PropertiesUtils.getStringValue("oss.access.url.prefix") + "uploadfile_" + fileName;
			if (resultMap.containsKey(Boolean.TRUE)){
				return new Response(0, aliyunFileUrl + "|" + resultMap.get(Boolean.TRUE));
			}else {
				return new Response(0, resultMap.get(Boolean.FALSE).toString());
			}
		}else {
			return new Response(0, "上传失败！");
		}
	}
	
	@RequestMapping(value = "/getPosNumSelectOptionByCampusId", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getPosNumSelectOptionByCampusId(@RequestParam String campusId, String typeId) {
		return posMachineService.getPosNumSelectOptionByCampusId(campusId, typeId);
	}
	
}
