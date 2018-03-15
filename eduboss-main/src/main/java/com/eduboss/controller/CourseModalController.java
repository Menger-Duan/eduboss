package com.eduboss.controller;

import com.eduboss.domainVo.CourseModalVo;
import com.eduboss.domainVo.PromiseClassSubjectVo;
import com.eduboss.domainVo.PromiseStudentCustomerVo;
import com.eduboss.dto.*;
import com.eduboss.service.CourseModalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/CourseModalController")
public class CourseModalController {

	@Autowired
	private CourseModalService courseModalService;


	/**
	 * 保存模板
	 * */
	@RequestMapping(value = "/saveCourseModal",method = RequestMethod.POST)
	@ResponseBody
	public Response saveCourseModal(@RequestBody CourseModalVo vo){
		return courseModalService.saveCourseModal(vo);
	}


	/**
	 * 查询模板列表
	 * */
	@RequestMapping(value = "/getCourseModalList")
	@ResponseBody
	public List<CourseModalVo> getCourseModalList(CourseModalVo vo){
		return courseModalService.getCourseModalList(vo);
	}

	/**
	 * 根据ID查询模板
	 * */
	@RequestMapping(value = "/findModalByModalId")
	@ResponseBody
	public CourseModalVo findModalByModalId(@RequestParam int modalId){
		return courseModalService.findModalByModalId(modalId);
	}


	/**
	 * 删除模版
	 * */
	@RequestMapping(value = "/deleteCourseModal",method = RequestMethod.POST)
	@ResponseBody
	public Response deleteCourseModal(@RequestParam int modalId){
		return courseModalService.deleteCourseModal(modalId);
	}


}
