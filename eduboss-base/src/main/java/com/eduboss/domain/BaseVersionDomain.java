package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.eduboss.exception.ApplicationException;

/**
 * 基础带版本domain
 * @author arvin
 *
 */
@MappedSuperclass
public class BaseVersionDomain extends BaseDomain {

    private static final long serialVersionUID = 3565785594079601083L;

    private int version;
    
    @Version
    @Column(name = "version", nullable=false,unique=true)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    public void checkVesion(int version){
        if(this.version!= version){
            throw new ApplicationException("该数据已经被别人修改，请刷新后再编辑");
        }
    }

    @Override
    public String toString() {
        return ", version=" + version + super.toString();
    }
    
}
