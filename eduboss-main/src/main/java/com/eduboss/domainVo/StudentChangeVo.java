package com.eduboss.domainVo;

import java.io.Serializable;

public class StudentChangeVo implements Serializable {

    private static final long serialVersionUID = -5866259655387592591L;

    private String studentId;
    
    private String oldContact;
    
    private String newContact;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOldContact() {
        return oldContact;
    }

    public void setOldContact(String oldContact) {
        this.oldContact = oldContact;
    }

    public String getNewContact() {
        return newContact;
    }

    public void setNewContact(String newContact) {
        this.newContact = newContact;
    }

    @Override
    public String toString() {
        return "StudentChangeVo [studentId=" + studentId + ", oldContact="
                + oldContact + ", newContact=" + newContact + "]";
    }
    
}
