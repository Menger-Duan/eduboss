package com.eduboss.utils;

import com.eduboss.common.ContractType;
import com.eduboss.common.FundsPayType;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.User;
import com.eduboss.jedis.FinanceMessage;
import com.eduboss.jedis.StudentStatusMsg;

public class PushRedisMessageUtil {
    /**
     * 收款或者冲销后传如队列处理现金流
     * @param fun
     * @param con
     * @param flag
     * @author  author :Yao
     * @date  2016年8月17日 下午2:37:33
     * @version 1.0
     */
    public static void putMsgToQueueForFinance(FundsChangeHistory fun,Contract con,String flag){
        if (fun.getChannel() != null
                && (!fun.getChannel().equals(PayWay.ELECTRONIC_ACCOUNT) || (fun.getChannel().equals(PayWay.ELECTRONIC_ACCOUNT) && "2".equals(flag)))) {
            FinanceMessage msg = new FinanceMessage();
            msg.setCampusId(fun.getFundBlCampusId());
            msg.setUserId(con.getSignStaff().getUserId());
            msg.setUserName(con.getSignStaff().getName());
            msg.setCountDate(fun.getTransactionTime().substring(0, 10));

            for(ContractProduct cp:con.getContractProducts()){
                if(cp.getType().equals(ProductType.LIVE)){
                    msg.setOnline(true);
                    break;
                }
            }
            if (con.getContractType() != null
                    && con.getContractType().equals(ContractType.NEW_CONTRACT)) {
                msg.setNewMoney(fun.getTransactionAmount());
            } else if (con.getContractType() != null
                    && con.getContractType().equals(ContractType.RE_CONTRACT)) {
                msg.setPreMoney(fun.getTransactionAmount());
            }
            msg.setTotalMoney(fun.getTransactionAmount());
            msg.setFlag(flag);
            leftPush(CommonUtil.FINANCE_QUEUE,msg);
        }
    }

    public static void putMsgToQueueForFinance(FundsChangeHistory fun, User signStaff, ContractType type,String flag){
            FinanceMessage msg = new FinanceMessage();
            msg.setCampusId(fun.getFundBlCampusId());
            msg.setUserId(signStaff.getUserId());
            msg.setUserName(signStaff.getName());
            msg.setCountDate(fun.getTransactionTime().substring(0, 10));

        for(ContractProduct cp:fun.getContract().getContractProducts()){
            if(cp.getType().equals(ProductType.LIVE)){
                msg.setOnline(true);
                break;
            }
        }
            if (type.equals(ContractType.NEW_CONTRACT)) {
                msg.setNewMoney(fun.getTransactionAmount());
            } else if (type.equals(ContractType.RE_CONTRACT)) {
                msg.setPreMoney(fun.getTransactionAmount());
            }
            msg.setTotalMoney(fun.getTransactionAmount());
            msg.setFlag(flag);
            leftPush(CommonUtil.FINANCE_QUEUE,msg);
    }


    public static void leftPush(String key,Object value){
        try {// 加入队列
            JedisUtil.lpush(
                    ObjectUtil.objectToBytes(key),
                    ObjectUtil.objectToBytes(value));
        } catch (Exception e) {
            // 错了
        }
    }

    //学生状态队列
    public static  void pushStudentToMsg(String studentId){
        StudentStatusMsg msg = new StudentStatusMsg();
        msg.setStudentId(studentId);
        msg.setType(ProductType.OTHERS);
        leftPush("studentStatus",msg);
    }
	
}
