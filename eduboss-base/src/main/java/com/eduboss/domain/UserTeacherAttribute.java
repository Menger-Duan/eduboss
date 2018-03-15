package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.RecommendStatus;

/**
 * Created by Administrator on 2017/5/18.
 */
@Entity
@Table(name = "user_teacher_attribute")
public class UserTeacherAttribute implements java.io.Serializable {
	
	private static final long serialVersionUID = -3304357433066070076L;
	
	private int id;
    private String userId; //
    private Boolean teacherSwitch;//授课老师开关
    private String picUrl;  //头像
    private String videoUrl; //介绍视频
    private String university; //毕业院校
    private String degree; //学历
    private DataDict gradeDict; //授课年级
    private DataDict subject; //授课科目
    private String remark; //个人简介
    private String teachingStyle;//教学风格
    private RecommendStatus recommendStatus;


    public UserTeacherAttribute() {
    }

    public UserTeacherAttribute(String userId) {
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "user_id", length = 32)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="teacher_switch")
    public Boolean getTeacherSwitch() {
        return teacherSwitch;
    }

    public void setTeacherSwitch(Boolean teacherSwitch) {
        this.teacherSwitch = teacherSwitch;
    }

    @Column(name="pic_url", length=32)
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Column(name="video_url", length=32)
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Column(name="university", length=32)
    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Column(name="degree", length=32)
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    public DataDict getGradeDict() {
        return gradeDict;
    }

    public void setGradeDict(DataDict gradeDict) {
        this.gradeDict = gradeDict;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    public DataDict getSubject() {
        return subject;
    }

    public void setSubject(DataDict subject) {
        this.subject = subject;
    }

    @Column(name = "remark", length = 512)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "teachingStyle", length = 512)
    public String getTeachingStyle() {
        return teachingStyle;
    }

    public void setTeachingStyle(String teachingStyle) {
        this.teachingStyle = teachingStyle;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "recommend_status", length = 20)
	public RecommendStatus getRecommendStatus() {
		return recommendStatus;
	}

	public void setRecommendStatus(RecommendStatus recommendStatus) {
		this.recommendStatus = recommendStatus;
	}
    
}
