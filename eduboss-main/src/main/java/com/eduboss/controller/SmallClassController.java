package com.eduboss.controller;

import com.eduboss.dto.MiniClassEditModalVo;
import com.eduboss.dto.MiniClassModalVo;
import com.eduboss.dto.Response;
import com.eduboss.service.SmallClassService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = "/SmallClassController")
public class SmallClassController {

	@Autowired
	private SmallClassService smallClassService;

	private final static Logger log = Logger.getLogger(SmallClassController.class);



	@RequestMapping(value = "/getSmallClassListOnModal", method =  RequestMethod.POST)
	@ResponseBody
	public List<MiniClassModalVo> getSmallClassListOnModal(@RequestBody MiniClassModalVo vo) {
		return smallClassService.getSmallClassListOnModal(vo) ;
	}

	@RequestMapping(value = "/getSmallClassListByTeacher", method =  RequestMethod.POST)
	@ResponseBody
	public List<MiniClassModalVo> getSmallClassListByTeacher(@RequestBody MiniClassModalVo vo) {
		return smallClassService.getSmallClassListByTeacher(vo) ;
	}

	@RequestMapping(value = "/saveEditClassModal", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveEditClassModal(@ModelAttribute MiniClassEditModalVo vo) {
		return smallClassService.saveEditClassModal(vo) ;
	}

	@RequestMapping(value = "/clearAllClassCourse", method =  RequestMethod.POST)
	@ResponseBody
	public Response clearAllClassCourse(@RequestParam String miniClassId) {
		 smallClassService.clearAllClassCourse(miniClassId) ;
		 return new Response();
	}


	@RequestMapping(value = "/getStudentMiniClassAttendent", method =  RequestMethod.GET)
	@ResponseBody
	public List getStudentMiniClassAttendent(@RequestParam String miniClassId,@RequestParam String studentId) {
		return smallClassService.getStudentMiniClassAttendent(miniClassId,studentId) ;
	}

	@RequestMapping(value = "/getMiniClassCourseDateInfo", method =  RequestMethod.GET)
	@ResponseBody
	public List getMiniClassCourseDateInfo(@RequestParam String miniClassId) {
		return smallClassService.getMiniClassCourseDateInfo(miniClassId) ;
	}

	/**
	 * 获取是否可以修改小班产品
	 * @param miniClassId
	 * @return
	 */
	@RequestMapping(value = "/getMiniClassCanChangeProduct", method =  RequestMethod.GET)
	@ResponseBody
	public Response getMiniClassCanChangeProduct(@RequestParam String miniClassId) {
		return smallClassService.getMiniClassCanChangeProduct(miniClassId) ;
	}

}
