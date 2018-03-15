package com.eduboss.domainVo;

import java.io.Serializable;

public class PhoneCustomerVo implements Serializable {

    private static final long serialVersionUID = 4628247668879389306L;

    private String customerId;
    
    private String name;
    
    private String contact;
    
    private String resEntrance;
    
    private String cusType;
    
    private String cusOrg;
    
    private String deliverTarget;
    
    private String intentionCampus;
    
    private String remark;
    
    private int phoneStatus;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getResEntrance() {
        return resEntrance;
    }

    public void setResEntrance(String resEntrance) {
        this.resEntrance = resEntrance;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getCusOrg() {
        return cusOrg;
    }

    public void setCusOrg(String cusOrg) {
        this.cusOrg = cusOrg;
    }

    public String getDeliverTarget() {
        return deliverTarget;
    }

    public void setDeliverTarget(String deliverTarget) {
        this.deliverTarget = deliverTarget;
    }

    public String getIntentionCampus() {
        return intentionCampus;
    }

    public void setIntentionCampus(String intentionCampus) {
        this.intentionCampus = intentionCampus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(int phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    @Override
    public String toString() {
        return "PhoneCustomerVo [customerId=" + customerId + ", name=" + name
                + ", contact=" + contact + ", resEntrance=" + resEntrance
                + ", cusType=" + cusType + ", cusOrg=" + cusOrg
                + ", deliverTarget=" + deliverTarget + ", intentionCampus="
                + intentionCampus + ", remark=" + remark + ", phoneStatus=" + phoneStatus
                + "]";
    }
    
}
