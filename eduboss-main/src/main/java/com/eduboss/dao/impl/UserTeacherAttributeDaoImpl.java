package com.eduboss.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.boss.rpc.eduboss.service.dto.TeacherSearchRpcVo;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserTeacherAttributeDao;
import com.eduboss.domain.UserTeacherAttribute;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2017/5/19.
 */
@Repository("UserTeacherAttributeDao")
public class UserTeacherAttributeDaoImpl extends GenericDaoImpl<UserTeacherAttribute, Integer> implements UserTeacherAttributeDao {

	@Override
	public DataPackage listPageUserTeacherAttribute(TeacherSearchRpcVo teacher, DataPackage dp) {
	    StringBuffer sql = new StringBuffer(" select distinct uta.* from user_teacher_attribute uta left join user u on u.user_id = uta.user_id ");
	    sql.append(" left join organization o on u.organizationID = o.id ");
	    StringBuffer sqlWhere = new StringBuffer(" where 1=1 and teacher_switch = 1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotBlank(teacher.getName())) {
		    sqlWhere.append(" and u.name like :name ");
			params.put("name", "%" + teacher.getName() + "%");
		}
		if (StringUtil.isNotBlank(teacher.getBlBrenchId())) {
		    sqlWhere.append(" and o.orgLevel like concat((select orgLevel from organization where id = :blBrenchId), '%') ");
			params.put("blBrenchId", teacher.getBlBrenchId());
		}
		if (teacher.getGrades() != null && teacher.getGrades().length > 0) {
		    sqlWhere.append(" and uta.grade_id in (:grades) ");
            params.put("grades", teacher.getGrades());
		}
		if (teacher.getSubjects() != null && teacher.getSubjects().length > 0) {
            sqlWhere.append(" and uta.subject_id in (:subjects) ");
            params.put("subjects", teacher.getSubjects());
        }
		sql.append(sqlWhere);
		dp = super.findPageBySql(sql.toString(), dp, true, params);
		return dp;
	}
	
	@Override
	public Integer getRecommendTeacherCount(String blBrenchId) {
		String sql = " select count(distinct uta.id) from user_teacher_attribute uta left join user u on u.user_id = uta.user_id "
				+ " left join organization o on u.organizationID = o.id where 1=1 and teacher_switch = 1 and recommend_status = 'RECOMMEND' ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotBlank(blBrenchId)) {
			sql += " and o.orgLevel like concat((select orgLevel from organization where id = :blBrenchId), '%') ";
			params.put("blBrenchId", blBrenchId);
		}
		return super.findCountSql(sql, params);
	}

    @Override
    public List<UserTeacherAttribute> listRecommendTeachers(String blBrenchId, Integer limit) {
        String sql = " select uta.* from user_teacher_attribute uta left join user u on u.user_id = uta.user_id "
                + " left join organization o on u.organizationID = o.id where 1=1 and teacher_switch = 1 and recommend_status = 'RECOMMEND' ";
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotBlank(blBrenchId)) {
            sql += " and o.orgLevel like concat((select orgLevel from organization where id = :blBrenchId), '%') ";
            params.put("blBrenchId", blBrenchId);
        }
        sql += " limit :limit ";
        params.put("limit", limit);
        return super.findBySql(sql, params);
    }

    @Override
    public List<UserTeacherAttribute> listTeachersByNames(String[] teacherNames) {
        String sql = " select uta.* from user_teacher_attribute uta left join user u on u.user_id = uta.user_id where 1=1 "
                + " and u.name in (:teacherNames) ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("teacherNames", teacherNames);
        return super.findBySql(sql, params);
    }
	
}
