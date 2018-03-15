package com.eduboss.dto;

import com.eduboss.domainVo.PromiseClassSubjectVo;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 2017/6/12.
 */
public class PromiseClassStudentDto {
    private String promiseStudentId;
    private TreeSet<PromiseClassSubjectVo> subjectVos;

    public String getPromiseStudentId() {
        return promiseStudentId;
    }

    public void setPromiseStudentId(String promiseStudentId) {
        this.promiseStudentId = promiseStudentId;
    }

    public TreeSet<PromiseClassSubjectVo> getSubjectVos() {
        return subjectVos;
    }

    public void setSubjectVos(TreeSet<PromiseClassSubjectVo> subjectVos) {
        this.subjectVos = subjectVos;
    }
}
