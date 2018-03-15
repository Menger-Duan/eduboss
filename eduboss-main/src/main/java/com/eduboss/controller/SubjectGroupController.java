package com.eduboss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.SubjectGroupSummaryVo;
import com.eduboss.domainVo.SubjectGroupVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.service.SubjectGroupService;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
@RequestMapping(value="/SubjectGroupController")
@Controller
public class SubjectGroupController {

	@Autowired
	private SubjectGroupService subjectGroupService;
	
	@RequestMapping(value="/editSubjectGroup")
	@ResponseBody
	public Response editSubjectGroup(@RequestBody SubjectGroupVo subjectGroupVo) {
		subjectGroupService.editSubjectGroup(subjectGroupVo);
		return new Response();
	}
	
	@RequestMapping(value="/deleteSubjectGroup")
	@ResponseBody
	public Response deleteSubjectGroup(@RequestBody SubjectGroupVo subjectGroupVo) {
		subjectGroupService.deleteSubjectGroup(subjectGroupVo);
		return new Response();
	}
	
	@RequestMapping(value="/moveSubjectGroup")
	@ResponseBody
	public Response moveSubjectGroup(@RequestParam int id, @RequestParam int type) {
		subjectGroupService.moveSubjectGroup(id, type);
		return new Response();
	}
	
	@RequestMapping(value="/getSubjectGroupSummary")
	@ResponseBody
	public SubjectGroupSummaryVo getSubjectGroupSummary(@RequestParam String brenchId, @RequestParam String campusId, @RequestParam int version) {
		return subjectGroupService.updateAndgetSubjectGroupSummary(brenchId, campusId, version);
	}
	
	@RequestMapping(value="/findSubjectGroup")
	@ResponseBody
	public SubjectGroupVo findSubjectGroup(@RequestParam int groupId) {
		return subjectGroupService.findSubjectGroup(groupId);
	}
	
	@RequestMapping(value="/getTransferGroupSubject")
	@ResponseBody
	public List<SubjectGroupVo> getTransferGroupSubject(@RequestParam int groupId) {
		return subjectGroupService.getTransferGroupSubjects(groupId);
	}
	
	@RequestMapping(value="/findSubjectGroupName")
	@ResponseBody
	public Response findSubjectGroupName(@RequestParam String subjectId, @RequestParam String campusId, String versionDate, String teacherId) {
		String subjectGroupName = subjectGroupService.findSubjectGroupName(subjectId, campusId, versionDate, teacherId);
		return new Response(0, subjectGroupName);
	}
	
	@RequestMapping(value="/getSubjectGroupForSelection")
	@ResponseBody
	public SelectOptionResponse getSubjectGroupForSelection(@RequestParam String campusId) {
		return subjectGroupService.getSubjectGroupForSelection(campusId);
	}
	
}
