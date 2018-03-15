package com.eduboss.domain;

import com.eduboss.common.BaseStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 工作流记录父类
 */
@MappedSuperclass
public class WorkFlowrRecordBase implements java.io.Serializable {

    // Fields
    protected String id; // 逻辑ID
    protected WorkFlowConfig workFlowConfig; // 对应节点配置
    protected BaseStatus approve; // 是否同意
    protected User user; // 审批人
    protected String time; // 审批时间
    protected String remark; // 备注
    protected WorkFlowConfig nextConfig; // 下一步

    // Property accessors
    @Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONFIG_ID")
    public WorkFlowConfig getWorkFlowConfig() {
        return workFlowConfig;
    }

    public void setWorkFlowConfig(WorkFlowConfig workFlowConfig) {
        this.workFlowConfig = workFlowConfig;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVE")
    public BaseStatus getApprove() {
        return approve;
    }

    public void setApprove(BaseStatus approve) {
        this.approve = approve;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "TIME")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_CONFIG_ID")
    public WorkFlowConfig getNextConfig() {
        return nextConfig;
    }

    public void setNextConfig(WorkFlowConfig nextConfig) {
        this.nextConfig = nextConfig;
    }
}