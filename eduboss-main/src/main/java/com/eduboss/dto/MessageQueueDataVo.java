package com.eduboss.dto;

import java.io.Serializable;

public class MessageQueueDataVo implements Serializable {

    private static final long serialVersionUID = -471689242131210325L;
    private String data;
    private String orgSysType;
    
    public MessageQueueDataVo() {
        super();
    }
    
    public MessageQueueDataVo(String data) {
        super();
        this.data = data;
    }
    
    public MessageQueueDataVo(String data, String orgSysType) {
        super();
        this.data = data;
        this.orgSysType = orgSysType;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getOrgSysType() {
        return orgSysType;
    }

    public void setOrgSysType(String orgSysType) {
        this.orgSysType = orgSysType;
    }

    @Override
    public String toString() {
        return "MessageQueueDataVo [data=" + data + ", orgSysType="
                + orgSysType + "]";
    }

}
