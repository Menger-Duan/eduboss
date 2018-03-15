package com.eduboss.domainVo;



/**
 * Created by Administrator on 2017/5/23.
 */
public class UserTeacherAttributeVo {

    private int id;
    private String userId; //
    private Boolean teacherSwitch;//授课老师开关
    private String picUrl;  //头像
    private String videoUrl; //介绍视频
    private String university; //毕业院校
    private String degree; //学历
    private String gradeId; //授课年级
    private String subjectId; //授课科目
    private String remark; //个人简介
    private String teachingStyle;//教学风格

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getTeacherSwitch() {
        return teacherSwitch;
    }

    public void setTeacherSwitch(Boolean teacherSwitch) {
        this.teacherSwitch = teacherSwitch;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTeachingStyle() {
        return teachingStyle;
    }

    public void setTeachingStyle(String teachingStyle) {
        this.teachingStyle = teachingStyle;
    }
}
