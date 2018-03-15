package com.eduboss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.wechatVo.CourseForWechatVo;
import com.eduboss.service.CourseService;

@Controller
@RequestMapping(value = "/c")
public class CourseWechatController {

	@Autowired 
	private CourseService courseService;
	 /**
	  * 一对一和一对多的课时详情 传入课程id  用于扫一扫 免登录
	  */
	 @ResponseBody
	 @RequestMapping(value="/w/b")
	 public CourseForWechatVo getCourseInfoForWechatById(String cId){
		return courseService.getCourseInfoForWechatById(cId);		 
	 }
}
