package com.eduboss.dao;

import com.eduboss.domain.TwoTeacherClass;

import java.util.Set;

import org.springframework.stereotype.Repository;


@Repository
public interface TwoTeacherClassDao extends GenericDAO<TwoTeacherClass, String> {

    /**
     * 查询是否有重名的双师班级
     * @param teacherClass
     * @return
     */
    boolean checkNameDuplicate(TwoTeacherClass teacherClass,Set<String> blBrenchId);
}
