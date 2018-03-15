package com.eduboss.domainVo;

import java.math.BigDecimal;

public class LiveContractProductRefundVo {

    private String liveId;

    private String contractId;

    private BigDecimal amountFromPromotionAcc = BigDecimal.ZERO;

    private BigDecimal returnAmountFromPromotionAcc = BigDecimal.ZERO;

    private BigDecimal returnMoney = BigDecimal.ZERO;

    private BigDecimal returnSpecialAmount= BigDecimal.ZERO;

    private String returnReason;

    private String requestId;

    private String operateUser;//操作人

    private String createTime;//退费时间

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public BigDecimal getAmountFromPromotionAcc() {
        return amountFromPromotionAcc;
    }

    public void setAmountFromPromotionAcc(BigDecimal amountFromPromotionAcc) {
        this.amountFromPromotionAcc = amountFromPromotionAcc;
    }

    public BigDecimal getReturnAmountFromPromotionAcc() {
        return returnAmountFromPromotionAcc;
    }

    public void setReturnAmountFromPromotionAcc(BigDecimal returnAmountFromPromotionAcc) {
        this.returnAmountFromPromotionAcc = returnAmountFromPromotionAcc;
    }

    public BigDecimal getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(BigDecimal returnMoney) {
        this.returnMoney = returnMoney;
    }

    public BigDecimal getReturnSpecialAmount() {
        return returnSpecialAmount;
    }

    public void setReturnSpecialAmount(BigDecimal returnSpecialAmount) {
        this.returnSpecialAmount = returnSpecialAmount;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiveContractProductRefundVo that = (LiveContractProductRefundVo) o;

        if (liveId != null ? !liveId.equals(that.liveId) : that.liveId != null) return false;
        if (contractId != null ? !contractId.equals(that.contractId) : that.contractId != null) return false;
        if (amountFromPromotionAcc != null ? !amountFromPromotionAcc.equals(that.amountFromPromotionAcc) : that.amountFromPromotionAcc != null)
            return false;
        if (returnAmountFromPromotionAcc != null ? !returnAmountFromPromotionAcc.equals(that.returnAmountFromPromotionAcc) : that.returnAmountFromPromotionAcc != null)
            return false;
        if (returnMoney != null ? !returnMoney.equals(that.returnMoney) : that.returnMoney != null) return false;
        if (returnSpecialAmount != null ? !returnSpecialAmount.equals(that.returnSpecialAmount) : that.returnSpecialAmount != null)
            return false;
        if (returnReason != null ? !returnReason.equals(that.returnReason) : that.returnReason != null) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (operateUser != null ? !operateUser.equals(that.operateUser) : that.operateUser != null) return false;
        return createTime != null ? createTime.equals(that.createTime) : that.createTime == null;
    }

    @Override
    public int hashCode() {
        int result = liveId != null ? liveId.hashCode() : 0;
        result = 31 * result + (contractId != null ? contractId.hashCode() : 0);
        result = 31 * result + (amountFromPromotionAcc != null ? amountFromPromotionAcc.hashCode() : 0);
        result = 31 * result + (returnAmountFromPromotionAcc != null ? returnAmountFromPromotionAcc.hashCode() : 0);
        result = 31 * result + (returnMoney != null ? returnMoney.hashCode() : 0);
        result = 31 * result + (returnSpecialAmount != null ? returnSpecialAmount.hashCode() : 0);
        result = 31 * result + (returnReason != null ? returnReason.hashCode() : 0);
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (operateUser != null ? operateUser.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
