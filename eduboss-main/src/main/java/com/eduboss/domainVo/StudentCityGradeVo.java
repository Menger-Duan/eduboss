package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 学生城市年级vo
 * @author arvin
 *
 */
public class StudentCityGradeVo implements Serializable {

    private static final long serialVersionUID = -8106752610155305110L;

    private String studentId;
    
    private String cityId;
    
    private String cityName;
    
    private String gradeId;
    
    private String gradeName;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    @Override
    public String toString() {
        return "StudentCityGradeVo [studentId=" + studentId + ", cityId="
                + cityId + ", cityName=" + cityName + ", gradeId=" + gradeId
                + ", gradeName=" + gradeName + "]";
    }
    
}
