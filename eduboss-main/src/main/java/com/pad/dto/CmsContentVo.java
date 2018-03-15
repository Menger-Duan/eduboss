package com.pad.dto;

import com.eduboss.common.OrganizationType;
import com.pad.common.CmsContentStatus;
import com.pad.common.CmsContentType;
import com.pad.entity.CmsContentAuth;
import com.pad.entity.CmsContentSign;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsContentVo {

    private Integer id;
    private CmsContentType type;
    private String typeName;
    private Integer menuId;
    private String title;
    private String facePic;
    private String remark;
    private Integer orderNum;
    private CmsContentStatus status;
    private String statusName;
    private String createUser;
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

    private String orgTypeName;

    private List<Integer> signIds;

    private List<CmsContentFileVo> files;

    private String content;

    private Integer isFront;

    private Integer allCanSee;

    private List<CmsContentAuth> auths;

    private Integer isDel;

    private Object fmenuId;//第一菜单
    private Object fmenuName;//第一菜单名字

    private String frontUrl;//前端详情URL

    public CmsContentVo() {
    }

    public CmsContentVo(Integer menuId) {
        this.menuId = menuId;
    }

    public CmsContentVo(Integer id, CmsContentStatus status) {
        this.id = id;
        this.status = status;
    }

    public Object getFmenuId() {
        return fmenuId;
    }

    public void setFmenuId(Object fmenuId) {
        this.fmenuId = fmenuId;
    }

    public Object getFmenuName() {
        return fmenuName;
    }

    public void setFmenuName(Object fmenuName) {
        this.fmenuName = fmenuName;
    }

    private List<CmsContentSign> signList;

    public List<CmsContentSign> getSignList() {
        return signList;
    }

    public void setSignList(List<CmsContentSign> signList) {
        this.signList = signList;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public List<CmsContentAuth> getAuths() {
        return auths;
    }

    public void setAuths(List<CmsContentAuth> auths) {
        this.auths = auths;
    }

    public Integer getAllCanSee() {
        return allCanSee;
    }

    public void setAllCanSee(Integer allCanSee) {
        this.allCanSee = allCanSee;
    }

    public List<Integer> getSignIds() {
        return signIds;
    }

    public void setSignIds(List<Integer> signIds) {
        this.signIds = signIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFacePic() {
        return facePic;
    }

    public void setFacePic(String facePic) {
        this.facePic = facePic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }


    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public String getTypeName() {
        return type.getName();
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOrgTypeName() {
        return orgType.getName();
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public List<CmsContentFileVo> getFiles() {
        return files;
    }

    public void setFiles(List<CmsContentFileVo> files) {
        this.files = files;
    }

    public String getStatusName() {
        return status.getName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsFront() {
        return isFront;
    }

    public void setIsFront(Integer isFront) {
        this.isFront = isFront;
    }

    public CmsContentType getType() {
        return type;
    }

    public void setType(CmsContentType type) {
        this.type = type;
    }

    public CmsContentStatus getStatus() {
        return status;
    }

    public void setStatus(CmsContentStatus status) {
        this.status = status;
    }

    public OrganizationType getOrgType() {
        return orgType;
    }

    public void setOrgType(OrganizationType orgType) {
        this.orgType = orgType;
    }

    public String getAppointmentPublishTime() {
        return appointmentPublishTime;
    }

    public void setAppointmentPublishTime(String appointmentPublishTime) {
        this.appointmentPublishTime = appointmentPublishTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }
}