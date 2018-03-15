package com.eduboss.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.SystemNotice;
import com.eduboss.domainVo.AppNewsManageVo;
import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.AppNewsManageService;

@Controller
@RequestMapping(value="/AppNewsManageController")
public class AppNewsManageController {
	
	@Autowired
	private AppNewsManageService appNewsManageService;
	
	@RequestMapping(value="/appNewsManageList")
	@ResponseBody
	public DataPackageForJqGrid getFeedBackList(@ModelAttribute GridRequest gridRequest,
			@ModelAttribute AppNewsManageVo vo){	
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage=appNewsManageService.getNews(dataPackage, vo);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
		
	}
	
	/**
	 * 新增或修改新闻
	 * */
	@RequestMapping(value = "/saveOrEditNewsManage")
	@ResponseBody
	public Response saveOrEditNewsManage(AppNewsManageVo vo,@RequestParam(value = "coverImage",required=false) MultipartFile myfile1,
			String delImage,String x,String y,String w,String h,
			String x1,String y1,String w1,String h1){
		appNewsManageService.saveOrEditNewsManage(vo,myfile1,delImage,x,y,w,h,x1,y1,w1,h1);
		return new Response();
	}
	
	/**
	 * 得到一条新闻
	 * */
	@RequestMapping(value = "/getNewsManageById")
	@ResponseBody
	public AppNewsManageVo getNewsManageById(@RequestParam String id){
		return appNewsManageService.findAppNewsManageById(id);
		
	}
	
	/**
	 * 删除新闻
	 * */
	@RequestMapping(value = "/delNewsManageById")
	@ResponseBody
	public Response delNewsManageById(@RequestParam String id){
		appNewsManageService.delNewsManage(id);
		return new Response();
	}

	/**
	 * 获取五大类的最新图片
	 * */
	@RequestMapping(value = "/getImageUrl")
	@ResponseBody
	public List<AppNewsManageVo> getImageUrl(){
		return appNewsManageService.getImageUrl();
	}
	
	/**
	 * 查询封面图片数
	 */
	@RequestMapping(value = "/getNewsbannerNum")
	@ResponseBody
	public String getNewsbannerNum(){
		return appNewsManageService.getNewsbannerNum();
	}
	
	/**
	 * 设置封面图片数
	 */
	@RequestMapping(value = "/setNewsbannerNum")
	@ResponseBody
	public void setNewsbannerNum(@RequestParam String num){
		appNewsManageService.setNewsbannerNum(num);
	}
}
