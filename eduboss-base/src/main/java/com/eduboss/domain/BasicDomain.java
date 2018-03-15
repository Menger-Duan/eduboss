package com.eduboss.domain;

import com.eduboss.exception.ApplicationException;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/7/6.
 */
@MappedSuperclass
public class BasicDomain   implements java.io.Serializable{

    private int version;
    private User createUser;
    private String createTime;
    private String modifyTime;
    private User modifyUser;

    @Version
    @Column(name = "VERSION", nullable=false,unique=true)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version=version;
    }

    public void checkVesion(int version){
        if(this.version!= version){
            throw new ApplicationException("该数据已经被别人修改，请刷新后再编辑");
        }
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "CREATE_USER", updatable=false)
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "MODIFY_USER")
    public User getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(User modifyUser) {
        this.modifyUser = modifyUser;
    }

    @Column(name = "CREATE_TIME", length = 20, updatable=false)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name = "MODIFY_TIME", length = 20)
    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }


}
