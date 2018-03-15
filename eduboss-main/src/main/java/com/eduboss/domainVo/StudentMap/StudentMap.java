package com.eduboss.domainVo.StudentMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public class StudentMap {

    private List<StudentAddress> studentAddressList = new ArrayList<>();

    private int enrolledSize = 0;

    private int potentialSize = 0;

    private String gradeName;

    public List<StudentAddress> getStudentAddressList() {
        return studentAddressList;
    }

    public void setStudentAddressList(List<StudentAddress> studentAddressList) {
        this.studentAddressList = studentAddressList;
    }

    public int getEnrolledSize() {
        return enrolledSize;
    }

    public void setEnrolledSize(int enrolledSize) {
        this.enrolledSize = enrolledSize;
    }

    public int getPotentialSize() {
        return potentialSize;
    }

    public void setPotentialSize(int potentialSize) {
        this.potentialSize = potentialSize;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
