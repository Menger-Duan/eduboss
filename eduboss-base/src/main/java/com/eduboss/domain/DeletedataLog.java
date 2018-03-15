package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by guosh on 2015/2/13.
 */
@Entity
@Table(name = "DELETEDATA_LOG")
public class DeletedataLog {
    private String id;
    private String delete_id;
    private String student_name;
    private String reason;
    private String operationTime;
    private User operationUser;  //删除人
    private String recordCreateUser;
    private String createUser_name;
    private String recordCreateTime;


    @Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "delete_id")
    public String getDelete_id() {
        return delete_id;
    }

    public void setDelete_id(String delete_id) {
        this.delete_id = delete_id;
    }



    @Column(name = "student_name")
    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }


    @Column(name = "createUser_name")
    public String getCreateUser_name() {
        return createUser_name;
    }

    public void setCreateUser_name(String createUser_name) {
        this.createUser_name = createUser_name;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_user_id")
    public User getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(User operationUser) {
        this.operationUser = operationUser;
    }

    @Basic
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Basic
    @Column(name = "operationTime")
    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    @Basic
    @Column(name = "record_createUser")
    public String getRecordCreateUser() {
        return recordCreateUser;
    }

    public void setRecordCreateUser(String recordCreateUser) {
        this.recordCreateUser = recordCreateUser;
    }

    @Basic
    @Column(name = "record_createTime")
    public String getRecordCreateTime() {
        return recordCreateTime;
    }

    public void setRecordCreateTime(String recordCreateTime) {
        this.recordCreateTime = recordCreateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeletedataLog that = (DeletedataLog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (operationTime != null ? !operationTime.equals(that.operationTime) : that.operationTime != null)
            return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        if (recordCreateTime != null ? !recordCreateTime.equals(that.recordCreateTime) : that.recordCreateTime != null)
            return false;
        if (recordCreateUser != null ? !recordCreateUser.equals(that.recordCreateUser) : that.recordCreateUser != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (operationTime != null ? operationTime.hashCode() : 0);
        result = 31 * result + (recordCreateUser != null ? recordCreateUser.hashCode() : 0);
        result = 31 * result + (recordCreateTime != null ? recordCreateTime.hashCode() : 0);
        return result;
    }
}
