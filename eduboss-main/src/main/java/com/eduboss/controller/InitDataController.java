package com.eduboss.controller;

import com.eduboss.domainVo.DeleteDataLogVo;
import com.eduboss.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.eduboss.service.InitDataDeleteService;

@Controller
@RequestMapping(value="/InitDataController")
public class InitDataController {
	
	@Autowired
	private InitDataDeleteService initDataDeleteService;
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public Response delete(@RequestParam String id,@RequestParam String reason) throws Exception{
		initDataDeleteService.deleteInitData(id,reason);
		return new Response();
	}


    /**
     * 分页查询
     */
    @RequestMapping(value = "/findPageDeleteLog", method = RequestMethod.GET)
    @ResponseBody
    public DataPackageForJqGrid findPageDeleteLog(@ModelAttribute GridRequest gridRequest,@ModelAttribute DeleteDataLogVo delLogVo,@ModelAttribute TimeVo timeVo){
        DataPackage dataPackage = new DataPackage(gridRequest);
        dataPackage= initDataDeleteService.findPageDeleteLog(dataPackage, delLogVo, timeVo);
        DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
        return dataPackageForJqGrid ;
    }

}
