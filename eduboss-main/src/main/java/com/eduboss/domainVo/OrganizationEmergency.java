package com.eduboss.domainVo;

import com.eduboss.common.StateOfEmergency;

/**
 * Created by Administrator on 2017/7/18.
 */
public class OrganizationEmergency {
    private String organizationId;
    private String organizationName;
    private StateOfEmergency stateOfEmergency;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public StateOfEmergency getStateOfEmergency() {
        return stateOfEmergency;
    }

    public void setStateOfEmergency(StateOfEmergency stateOfEmergency) {
        this.stateOfEmergency = stateOfEmergency;
    }
}
