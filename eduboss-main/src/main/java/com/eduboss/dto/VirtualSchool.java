package com.eduboss.dto;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.Region;

/**
 * Created by Administrator on 2016/10/8.
 */
public class VirtualSchool {
    private String virtualSchoolName;
    private DataDict region;
    private String contractId;

    private Region city;
    private Region province;

    public String getVirtualSchoolName() {
        return virtualSchoolName;
    }

    public void setVirtualSchoolName(String virtualSchoolName) {
        this.virtualSchoolName = virtualSchoolName;
    }

    public DataDict getRegion() {
        return region;
    }

    public void setRegion(DataDict region) {
        this.region = region;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Region getCity() {
        return city;
    }

    public void setCity(Region city) {
        this.city = city;
    }

    public Region getProvince() {
        return province;
    }

    public void setProvince(Region province) {
        this.province = province;
    }
}
