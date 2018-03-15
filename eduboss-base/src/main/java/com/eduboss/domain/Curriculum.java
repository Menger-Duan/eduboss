package com.eduboss.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "curriculum")
public class Curriculum {
    private String id; //直播扣费id
    private String courseId; //直播课时id
    private String curriculumName;//直播班课名称
    private String liveId;//直播课程id
    private String grade;//年级
    private String subject;//科目
    private String teacher;//主讲老师
    private String type;//班课类型
    private String startTimeStamp;//开始时间
    private String endTimeStamp;//结束时间
    private String startDateTime;//开始时间
    private String endDateTime;//结束时间
//    private BigDecimal courseHours;//课时
    private BigDecimal courseAmount;//扣费金额
    private String operateUser;//操作人
    private String operateTime;// 扣费或冲销的发生时间

    private String contractId;//合同id
    private int courseHours;//课时

    private String requestId;//

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "course_id", length = 32)
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Column(name = "curriculum_name", length = 64)
    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    @Column(name = "live_id", length = 32)
    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    @Column(name = "grade", length = 32)
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Column(name = "subject", length = 32)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "teacher", length = 32)
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Column(name = "type", length = 32)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    @Transient
    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    @Transient
    public String getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(String endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    @Column(name = "start_date_time", length = 32)
    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    @Column(name = "end_date_time", length = 32)
    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

//    @Column(name = "course_hours", precision = 10)
//    public BigDecimal getCourseHours() {
//        return courseHours;
//    }
//
//    public void setCourseHours(BigDecimal courseHours) {
//        this.courseHours = courseHours;
//    }

    @Column(name = "course_amount", precision = 10)
    public BigDecimal getCourseAmount() {
        return courseAmount;
    }

    public void setCourseAmount(BigDecimal courseAmount) {
        this.courseAmount = courseAmount;
    }

    @Column(name = "operate_user", length = 32)
    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    @Column(name = "contract_id", length = 32)
    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    @Transient
    public int getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(int courseHours) {
        this.courseHours = courseHours;
    }

    @Column(name = "request_id", length = 32)
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Column(name = "operate_time", length = 32)
    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Curriculum that = (Curriculum) o;

        if (courseHours != that.courseHours) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (curriculumName != null ? !curriculumName.equals(that.curriculumName) : that.curriculumName != null)
            return false;
        if (liveId != null ? !liveId.equals(that.liveId) : that.liveId != null) return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (teacher != null ? !teacher.equals(that.teacher) : that.teacher != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (startTimeStamp != null ? !startTimeStamp.equals(that.startTimeStamp) : that.startTimeStamp != null)
            return false;
        if (endTimeStamp != null ? !endTimeStamp.equals(that.endTimeStamp) : that.endTimeStamp != null) return false;
        if (startDateTime != null ? !startDateTime.equals(that.startDateTime) : that.startDateTime != null)
            return false;
        if (endDateTime != null ? !endDateTime.equals(that.endDateTime) : that.endDateTime != null) return false;
        if (courseAmount != null ? !courseAmount.equals(that.courseAmount) : that.courseAmount != null) return false;
        if (operateUser != null ? !operateUser.equals(that.operateUser) : that.operateUser != null) return false;
        if (operateTime != null ? !operateTime.equals(that.operateTime) : that.operateTime != null) return false;
        if (contractId != null ? !contractId.equals(that.contractId) : that.contractId != null) return false;
        return requestId != null ? requestId.equals(that.requestId) : that.requestId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        result = 31 * result + (curriculumName != null ? curriculumName.hashCode() : 0);
        result = 31 * result + (liveId != null ? liveId.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (startTimeStamp != null ? startTimeStamp.hashCode() : 0);
        result = 31 * result + (endTimeStamp != null ? endTimeStamp.hashCode() : 0);
        result = 31 * result + (startDateTime != null ? startDateTime.hashCode() : 0);
        result = 31 * result + (endDateTime != null ? endDateTime.hashCode() : 0);
        result = 31 * result + (courseAmount != null ? courseAmount.hashCode() : 0);
        result = 31 * result + (operateUser != null ? operateUser.hashCode() : 0);
        result = 31 * result + (operateTime != null ? operateTime.hashCode() : 0);
        result = 31 * result + (contractId != null ? contractId.hashCode() : 0);
        result = 31 * result + courseHours;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        return result;
    }
}
