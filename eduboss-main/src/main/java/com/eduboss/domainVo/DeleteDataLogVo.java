package com.eduboss.domainVo;

/**
 * Created by guosh on 2015/3/3.
 */
public class DeleteDataLogVo {
    private String id;
    private String delete_id;
    private String student_name;
    private String reason;
    private String operationUserName;
    private String operationTime;
    private String recordCreateUser;
    private String createUser_name;
    private String recordCreateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelete_id() {
        return delete_id;
    }

    public void setDelete_id(String delete_id) {
        this.delete_id = delete_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getRecordCreateUser() {
        return recordCreateUser;
    }

    public void setRecordCreateUser(String recordCreateUser) {
        this.recordCreateUser = recordCreateUser;
    }

    public String getCreateUser_name() {
        return createUser_name;
    }

    public void setCreateUser_name(String createUser_name) {
        this.createUser_name = createUser_name;
    }

    public String getRecordCreateTime() {
        return recordCreateTime;
    }

    public void setRecordCreateTime(String recordCreateTime) {
        this.recordCreateTime = recordCreateTime;
    }

    public String getOperationUserName() {
        return operationUserName;
    }

    public void setOperationUserName(String operationUserName) {
        this.operationUserName = operationUserName;
    }
}
