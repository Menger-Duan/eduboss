package com.eduboss.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.eduboss.exception.ApplicationException;

/**
 * 基础domain
 * @author arvin
 *
 */
@MappedSuperclass
public class BaseDomain implements Serializable {

    private static final long serialVersionUID = 3565785594079601083L;

    private Date createTime;
    
    private User createUser;
    
    private Date modifyTime;
    
    private User modifyUser;

    @Column(name = "create_time", updatable = false, insertable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "create_user_id", updatable=false)
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    @Column(name = "modify_time", updatable = false, insertable = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "modify_user_id")
    public User getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(User modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Override
    public String toString() {
        return ", createTime=" + createTime + ", createUser=" + createUser 
                + ", modifyTime=" + modifyTime + ", modifyUser=" + modifyUser;
    }
    
}
