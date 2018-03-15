package com.eduboss.utils;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.IncomeDistribution;
import com.eduboss.domain.User;
import com.eduboss.jedis.Message;
import com.eduboss.service.UserService;

import java.util.List;

/**
 * Created by Yao on 2017/5/31.
 */
public class BonusQueueUtils {

    protected static UserService userService = ApplicationContextUtil.getContext().getBean(UserService.class);

    public static void addBonusToMessage(IncomeDistribution bonus, List<Message> messageList,String flag){//ContractBonus bonus 修改为新的表
        Message message=new Message();
        if (BonusDistributeType.USER_USER.equals(bonus.getSubBonusDistributeType())){
            message.setDateTime(bonus.getFundsChangeHistory().getTransactionTime().substring(0,10));
            message.setUserId(bonus.getBonusStaff().getUserId());
            message.setCampusId(bonus.getBonusStaffCampus().getId());
            message.setMoney(bonus.getAmount());
            message.setFlag(flag);
            if(bonus.getProductType()!=null && bonus.getProductType().equals(ProductType.LIVE)){
                message.setOnline(true);
            }
            messageList.add(message);
        }
        
    }


    /**
     * 计算需要修改的业绩传入队列处理
     * @param newMap
     * @param oldMap
     * @param fundsChangeHistory
     * @author  author :Yao
     * @date  2016年7月14日 下午3:53:57
     * @version 1.0
     */
//    public static void getReturnBonusQueueList(Map<String,BigDecimal> newMap, Map<String,BigDecimal> oldMap, FundsChangeHistory fundsChangeHistory,String flag1,String flag0){
//        //当旧的有，比新的大，小，相等就不用处理
//        List<Message> msgList=new ArrayList<Message>();
//        for (String newKey : newMap.keySet()) {
//            Message message=new Message();
//            User user=userService.findUserById(newKey);
//            if(oldMap.get(newKey)!=null){
//                message.setDateTime(fundsChangeHistory.getTransactionTime().substring(0,10));
//                message.setUserId(newKey);
//                if(oldMap.get(newKey).compareTo(newMap.get(newKey))>0){//旧数据比新的大  ，要减业绩
//                    message.setCampusId(user.getOrganizationId());
//                    message.setMoney(oldMap.get(newKey).subtract(newMap.get(newKey)));
//                    message.setFlag(flag1);
//                    msgList.add(message);
//                }else if(oldMap.get(newKey).compareTo(newMap.get(newKey))<0){//旧数据比新的小，要加业绩
//                    message.setCampusId(user.getOrganizationId());
//                    message.setMoney(newMap.get(newKey).subtract(oldMap.get(newKey)));
//                    message.setFlag(flag0);
//                    msgList.add(message);
//                }
//                oldMap.remove(newKey);
//            }else{//当旧的没有，就是新增
//                message.setDateTime(fundsChangeHistory.getTransactionTime().substring(0,10));
//                message.setUserId(newKey);
//                message.setCampusId(user.getOrganizationId());
//                message.setMoney(newMap.get(newKey));
//                message.setFlag(flag0);
//                msgList.add(message);
//            }
//        }
//        for (String oleKey : oldMap.keySet()) {//当新的没有，旧的要减
//            Message message=new Message();
//            User user=userService.findUserById(oleKey);
//            message.setDateTime(fundsChangeHistory.getTransactionTime().substring(0,10));
//            message.setUserId(oleKey);
//            message.setCampusId(user.getOrganizationId());
//            message.setMoney(oldMap.get(oleKey));
//            message.setFlag(flag1);
//            msgList.add(message);
//        }
//
//        pushToQueue(msgList);
//    }

    /**
     * 加入队列
     * @param message
     * @author  author :Yao
     * @date  2016年7月14日 下午3:45:13
     * @version 1.0
     */
    public static void pushToQueue(List<Message> message){
        for (Message msg : message) {
            try {// 加入队列
                JedisUtil.lpush(
                        ObjectUtil.objectToBytes("bonusQueue"),
                        ObjectUtil.objectToBytes(msg));
            } catch (Exception e) {
                // 错了
            }
        }
    }

//    public static void resetBonus(String flag,String transactionTime,BigDecimal money,String userId,String campusId){
//        List<Message> list=new ArrayList<>();
//        Message msg = new Message();
//        msg.setDateTime(transactionTime.substring(0,10));
//        msg.setMoney(money);
//        msg.setUserId(userId);
//        msg.setCampusId(campusId);
//        msg.setFlag(flag);
//        list.add(msg);
//        pushToQueue(list);
//    }

//    public void clearBonusAmount(FundsChangeHistory fund,BigDecimal totalAmount,String userId,String blcampus){
//        /****************增加队列处理top10数据*****************/
//        Message msg = new Message();
//        msg.setDateTime(fund.getTransactionTime().substring(0,10));
//        msg.setMoney(totalAmount);
//        msg.setUserId(userId);
//        msg.setCampusId(blcampus);
//        msg.setFlag("1");
//        try {// 加入队列
//            JedisUtil.lpush(
//                    ObjectUtil.objectToBytes("queue"),
//                    ObjectUtil.objectToBytes(msg));
//        } catch (Exception e) {
//            // 错了
//        }
//        /****************增加队列处理top10数据*****************/
//    }

}
