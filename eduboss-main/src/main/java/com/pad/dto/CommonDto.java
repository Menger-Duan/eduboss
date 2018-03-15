package com.pad.dto;

/**
 * Created by Administrator on 2017/12/9.
 */
public class CommonDto {

    private String brenchId;//分公司ID

    private String orderBy;

    private String orderType;

    private String  signIds;

    private Integer page;

    private Integer size;

    private Integer frontId;


    public Integer getFrontId() {
        return frontId;
    }

    public void setFrontId(Integer frontId) {
        this.frontId = frontId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBrenchId() {
        return brenchId;
    }

    public void setBrenchId(String brenchId) {
        this.brenchId = brenchId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSignIds() {
        return signIds;
    }

    public void setSignIds(String signIds) {
        this.signIds = signIds;
    }
}
