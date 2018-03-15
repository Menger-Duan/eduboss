package com.eduboss.service.impl.rpc;

import java.util.ArrayList;
import java.util.List;

import org.boss.rpc.base.dto.PageRpcVo;
import org.boss.rpc.base.dto.TeacherRpcVo;
import org.boss.rpc.eduboss.service.TeacherRpc;
import org.boss.rpc.eduboss.service.dto.TeacherSearchRpcVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.common.Constants;
import com.eduboss.common.RecommendStatus;
import com.eduboss.domain.User;
import com.eduboss.domain.UserTeacherAttribute;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.service.UserTeacherAttributeService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

@Service("teacherRpcImpl")
public class TeacherRpcImpl implements TeacherRpc {

	@Autowired
	private UserTeacherAttributeService userTeacherAttributeService;
	
	@Autowired
	private UserService userService;
	
	@Override
	@SuppressWarnings("unchecked")
	public PageRpcVo<TeacherRpcVo> listTeacherRpcVo(TeacherSearchRpcVo teacher) {
		DataPackage dp = userTeacherAttributeService.listPageUserTeacherAttribute(teacher);
		PageRpcVo<TeacherRpcVo> resultVo = new PageRpcVo<TeacherRpcVo>();
		resultVo.setCurrent(dp.getPageNo());
		resultVo.setSize(dp.getPageSize());
		resultVo.setTotal(dp.getRowCount());
		List<UserTeacherAttribute> list = (List<UserTeacherAttribute>) dp.getDatas();
		List<TeacherRpcVo> voList = new ArrayList<TeacherRpcVo>();
		for (UserTeacherAttribute ut: list) {
			voList.add(this.copyProperties(ut));
		}
		resultVo.setRecords(voList);
		return resultVo;
	}

	@Override
	public void recommendTeacher(String teacherId, String recommendStatus) {
		userTeacherAttributeService.recommendTeacher(teacherId, RecommendStatus.valueOf(recommendStatus));
	}

	@Override
	public Integer getRecommendTeacherCount(String blBrenchId) {
		return userTeacherAttributeService.getRecommendTeacherCount(blBrenchId);
	}

    @Override
    public List<TeacherRpcVo> listRecommendTeachers(String blBrenchId, Integer limit) {
        List<UserTeacherAttribute> list = userTeacherAttributeService.listRecommendTeachers(blBrenchId, limit);
        List<TeacherRpcVo> voList = new ArrayList<TeacherRpcVo>();
        for (UserTeacherAttribute ut: list) {
            voList.add(this.copyProperties(ut));
        }
        return voList;
    }

    @Override
    public TeacherRpcVo fintTeacherDetailById(String teacherId) {
        UserTeacherAttribute teacher = userTeacherAttributeService.findUserTeacherAttributeById(teacherId);
        TeacherRpcVo vo = this.copyProperties(teacher);
        String videoUrl = StringUtil.isNotBlank(teacher.getVideoUrl()) ? PropertiesUtils.getStringValue("oss.access.url.prefix") + teacher.getVideoUrl() : "";
        vo.setVideoUrl(videoUrl);
        return vo;
    }

    @Override
    public List<TeacherRpcVo> listTeachersByNames(String teacherNames) {
        String redisKey = Constants.TEACHER_RPC + teacherNames;
        if (JedisUtil.exists(redisKey.getBytes())) {
            return JSONObject.parseArray(new String(JedisUtil.get(redisKey.getBytes())), TeacherRpcVo.class);
        }
        List<UserTeacherAttribute> list = userTeacherAttributeService.listTeachersByNames(teacherNames.split(","));
        List<TeacherRpcVo> voList = new ArrayList<TeacherRpcVo>();
        for (UserTeacherAttribute ut: list) {
            voList.add(this.copyProperties(ut));
        }
        JSONArray jsonArray = (JSONArray) JSONObject.toJSON(voList);
        JedisUtil.set(redisKey.getBytes(), jsonArray.toJSONString().getBytes(), 2);
        return voList;
    }
	
	private TeacherRpcVo copyProperties(UserTeacherAttribute ut) {
	    TeacherRpcVo vo = HibernateUtils.voObjectMapping(ut, TeacherRpcVo.class);
        User u = userService.findUserById(ut.getUserId());
        String picUrl = StringUtil.isNotBlank(ut.getPicUrl()) ? PropertiesUtils.getStringValue("oss.access.url.prefix") + ut.getPicUrl() : "";
        vo.setPicUrl(picUrl);
        vo.setName(u.getName());
        if (ut.getSubject() != null) {
            vo.setSubject(ut.getSubject().getName());
        }
        if (ut.getGradeDict() != null) {
            vo.setGrade(ut.getGradeDict().getName());
        }
        vo.setBlCampusName(userService.getBelongCampusByOrgId(u.getOrganizationId()).getName());
        vo.setTeacherStatus(ut.getTeacherSwitch());
        vo.setRecommendStatus(ut.getRecommendStatus().getValue());
        vo.setRecommendStatusName(ut.getRecommendStatus().getName());
        return vo;
	}

}
