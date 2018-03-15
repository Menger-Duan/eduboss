package com.pad.entity;

import com.eduboss.common.OrganizationType;
import com.pad.common.CmsContentStatus;
import com.pad.common.CmsContentType;

import javax.persistence.*;
import java.util.Date;

    /**
     * <p>
     *
     * </p>
     *
     * @author Spark
     * @since 2017-11-30
     */
    @Entity
    @Table(name= "cms_content")
    public class CmsContent implements java.io.Serializable {

        private static final long serialVersionUID = 1L;
        private Integer id;
        private CmsContentType type;
        private Integer menuId;
        private String title;
        private String facePic;
        private String remark;
        private Integer orderNum;
        private String createUser;
        private CmsContentStatus status;
        private String modifyUser;
        private Date createTime;
        private Date modifyTime;
        private Integer version;
        private Integer visitNum;
        private String appointmentPublishTime;
        private String publishTime;
        /**
         * 集团 和 分公司
         */
        private OrganizationType orgType;

        private String content;

        private Integer isFront;

        private Integer allCanSee;

        private Integer isDel;


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="id")
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Enumerated(value = EnumType.STRING)
        @Column(name = "type")
        public CmsContentType getType() {
            return type;
        }

        public void setType(CmsContentType type) {
            this.type = type;
        }

        @Column(name ="menu_id")
        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        @Column
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Column(name ="face_pic")
        public String getFacePic() {
            return facePic;
        }

        public void setFacePic(String facePic) {
            this.facePic = facePic;
        }

        @Column
        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Column(name ="order_num")
        public Integer getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(Integer orderNum) {
            this.orderNum = orderNum;
        }


        @Enumerated(value = EnumType.STRING)
        @Column(name = "status")
        public CmsContentStatus getStatus() {
            return status;
        }

        public void setStatus(CmsContentStatus status) {
            this.status = status;
        }

        @Column(name ="create_user")
        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        @Column(name ="modify_user")
        public String getModifyUser() {
            return modifyUser;
        }

        public void setModifyUser(String modifyUser) {
            this.modifyUser = modifyUser;
        }

        @Column(name ="create_time",updatable = false,insertable = false)
        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        @Column(name ="modify_time",updatable = false,insertable = false)
        public Date getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(Date modifyTime) {
            this.modifyTime = modifyTime;
        }

        @Column
        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        @Column(name ="visit_num")
        public Integer getVisitNum() {
            return visitNum;
        }

        public void setVisitNum(Integer visitNum) {
            this.visitNum = visitNum;
        }

        @Column(name ="appointment_publish_time")
        public String getAppointmentPublishTime() {
            return appointmentPublishTime;
        }

        public void setAppointmentPublishTime(String appointmentPublishTime) {
            this.appointmentPublishTime = appointmentPublishTime;
        }

        @Column(name ="publish_time")
        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        @Enumerated(value = EnumType.STRING)
        @Column(name ="org_type")
        public OrganizationType getOrgType() {
            return orgType;
        }

        public void setOrgType(OrganizationType orgType) {
            this.orgType = orgType;
        }


        @Column(name = "content")
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Column(name = "is_front")
        public Integer getIsFront() {
            return isFront;
        }

        public void setIsFront(Integer isFront) {
            this.isFront = isFront;
        }


        @Column(name="all_can_see")
        public Integer getAllCanSee() {
            return allCanSee;
        }

        public void setAllCanSee(Integer allCanSee) {
            this.allCanSee = allCanSee;
        }


        @Column(name = "is_del")
        public Integer getIsDel() {
            return isDel;
        }

        public void setIsDel(Integer isDel) {
            this.isDel = isDel;
        }
    }