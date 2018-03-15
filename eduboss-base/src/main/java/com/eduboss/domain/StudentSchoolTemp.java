package com.eduboss.domain;

import com.eduboss.common.SchoolTempAuditStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/8.
 */

@Entity
@Table(name = "SCHOOL_TEMP")
public class StudentSchoolTemp implements java.io.Serializable  {
    private String id;
    private String name;

    private String contractId;  //合同
    private String studentId; //学生id

    private SchoolTempAuditStatus schoolTempAuditStatus; //审核状态

    private User auditUser; //审核人

    private String value;
    private Integer orderVal;
    private DataDict region;
    private DataDict schoolType;
    private String schoolStatus;
    private String createUserId;
    private String createTime;
    private String modifyUserId;
    private String modifyTime;
    private String address; // 学校地址
    private String contact; // 联系方式
    private String remark;

    private String schoolLeader;//学校负责人
    private String schoolLeaderContact;//学校负责人联系方式

    private Region province;	//省份
    private Region city;	//城市
    private Region area;	//地区

    public StudentSchoolTemp() {
    }

    public StudentSchoolTemp(String id) {
        super();
        this.id = id;
    }


    // Property accessors
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME", length = 128)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "contract_id", length = 32)
    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }


    @Column(name = "student_id", length = 32)
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Column(name = "VALUE", length = 128)
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "ORDER_VAL")
    public Integer getOrderVal() {
        return this.orderVal;
    }

    public void setOrderVal(Integer orderVal) {
        this.orderVal = orderVal;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public DataDict getRegion() {
        return region;
    }

    public void setRegion(DataDict region) {
        this.region = region;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY")
    public DataDict getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(DataDict schoolType) {
        this.schoolType = schoolType;
    }

    @Column(name = "SCHOOL_STATUS", length = 32)
    public String getSchoolStatus() {
        return this.schoolStatus;
    }

    public void setSchoolStatus(String schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    @Column(name = "CREATE_USER_ID", length = 32)
    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "CREATE_TIME", length = 20)
    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name = "MODIFY_USER_ID", length = 32)
    public String getModifyUserId() {
        return this.modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    @Column(name = "MODIFY_TIME", length = 20)
    public String getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "ADDRESS", length = 255)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "CONTACT", length = 128)
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Column(name = "REMARK", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "SCHOOL_LEADER", length = 20)
    public String getSchoolLeader() {
        return schoolLeader;
    }

    public void setSchoolLeader(String schoolLeader) {
        this.schoolLeader = schoolLeader;
    }

    @Column(name = "SCHOOL_LEADER_CONTACT", length = 32)
    public String getSchoolLeaderContact() {
        return schoolLeaderContact;
    }

    public void setSchoolLeaderContact(String schoolLeaderContact) {
        this.schoolLeaderContact = schoolLeaderContact;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "AUDIT_STATUS", length = 32)
    public SchoolTempAuditStatus getSchoolTempAuditStatus() {
        return schoolTempAuditStatus;
    }

    public void setSchoolTempAuditStatus(SchoolTempAuditStatus schoolTempAuditStatus) {
        this.schoolTempAuditStatus = schoolTempAuditStatus;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_user_id")
    public User getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(User auditUser) {
        this.auditUser = auditUser;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    public Region getProvince() {
        return province;
    }

    public void setProvince(Region province) {
        this.province = province;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    public Region getCity() {
        return city;
    }

    public void setCity(Region city) {
        this.city = city;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    public Region getArea() {
        return area;
    }

    public void setArea(Region area) {
        this.area = area;
    }
}
