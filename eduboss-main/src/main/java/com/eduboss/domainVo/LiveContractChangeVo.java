package com.eduboss.domainVo;

import java.util.List;

public class LiveContractChangeVo {
//    private List<LiveContractProductRefundVo> refundVoList;
    private LiveContractProductRefundVo liveContractProductRefundVo;
    private ContractLiveVo contractLiveVo;

//    public List<LiveContractProductRefundVo> getRefundVoList() {
//        return refundVoList;
//    }
//
//    public void setRefundVoList(List<LiveContractProductRefundVo> refundVoList) {
//        this.refundVoList = refundVoList;
//    }


    public LiveContractProductRefundVo getLiveContractProductRefundVo() {
        return liveContractProductRefundVo;
    }

    public void setLiveContractProductRefundVo(LiveContractProductRefundVo liveContractProductRefundVo) {
        this.liveContractProductRefundVo = liveContractProductRefundVo;
    }

    public ContractLiveVo getContractLiveVo() {
        return contractLiveVo;
    }

    public void setContractLiveVo(ContractLiveVo contractLiveVo) {
        this.contractLiveVo = contractLiveVo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiveContractChangeVo that = (LiveContractChangeVo) o;

        if (liveContractProductRefundVo != null ? !liveContractProductRefundVo.equals(that.liveContractProductRefundVo) : that.liveContractProductRefundVo != null)
            return false;
        return contractLiveVo != null ? contractLiveVo.equals(that.contractLiveVo) : that.contractLiveVo == null;
    }

    @Override
    public int hashCode() {
        int result = liveContractProductRefundVo != null ? liveContractProductRefundVo.hashCode() : 0;
        result = 31 * result + (contractLiveVo != null ? contractLiveVo.hashCode() : 0);
        return result;
    }
}
