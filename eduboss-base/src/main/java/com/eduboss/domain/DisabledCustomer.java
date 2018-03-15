package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 无效客户表
 * @author arvin
 *
 */
@Entity
@Table(name = "disabled_customer")
public class DisabledCustomer {

    private Integer id; 
    
    private String contact;
    
    private String remark;
    
    private int udpateEnabled;

    public DisabledCustomer() {
        super();
    }

    public DisabledCustomer(Integer id, String contact, String remark, int udpateEnabled) {
        super();
        this.id = id;
        this.contact = contact;
        this.remark = remark;
        this.udpateEnabled = udpateEnabled;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable=false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 联系方式
     * @return
     */
    @Column(name = "contact", length = 11)
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 备注
     * @return
     */
    @Column(name = "remark", length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 0未更新为有效，1已更新为有效
     * @return
     */
    @Column(name = "udpate_enabled", length = 1)
    public int getUdpateEnabled() {
        return udpateEnabled;
    }

    public void setUdpateEnabled(int udpateEnabled) {
        this.udpateEnabled = udpateEnabled;
    }

    @Override
    public String toString() {
        return "DisabledCustomer [id=" + id + ", contact=" + contact
                + ", remark=" + remark + ", udpateEnabled=" + udpateEnabled
                + "]";
    }
    
}
