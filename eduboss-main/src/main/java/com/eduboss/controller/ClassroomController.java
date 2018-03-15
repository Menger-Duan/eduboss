package com.eduboss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.Classroom;
import com.eduboss.dto.ClassroomUseStatus;
import com.eduboss.service.ClassroomService;

@Controller
@RequestMapping(value = "/ClassroomController")
public class ClassroomController {

	@Autowired
	private ClassroomService classroomService;
	
	/**
	 * 根据教室名称获取教室
	 * @param namePattern
	 * @return
	 */
	@RequestMapping(value = "/getClassroomNames", method =  RequestMethod.GET)
	@ResponseBody
	public List<Classroom> getClassroomNames(String namePattern){
		return classroomService.getClassroomNames(namePattern);
	}
	
	/**
	 * 根据条件获取教室使用状态
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/getClassroomStatus", method =  RequestMethod.GET)
	@ResponseBody
	public List<ClassroomUseStatus> getClassroomStatus(@ModelAttribute ClassroomUseStatus vo){
		return classroomService.getClassroomStatus(vo);
	}
	
}
