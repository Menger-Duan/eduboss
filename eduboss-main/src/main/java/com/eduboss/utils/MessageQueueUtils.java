package com.eduboss.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.eduboss.dto.MessageQueueDataVo;
import com.google.common.collect.Maps;

public class MessageQueueUtils {

    private static Logger LOGGER = Logger.getLogger(MessageQueueUtils.class);
    private static final String MQ_URLmqUrl = PropertiesUtils.getStringValue("mq.url");
    private static final String INSTITUTION = PropertiesUtils.getStringValue("institution");
    private static final String MQ_URL = PropertiesUtils.getStringValue("mq.url");
    private static final String AUTHORIZATION = PropertiesUtils.getStringValue("mq.authorization");
    
    public static String postToMq(TopicCode topicCode, String data) {
        return postToMq(topicCode.getValue(),0,data);
    }

    public static String postToMq(TopicCode topicCode, long delay_second, String data) {
        return postToMq(topicCode.getValue(), delay_second, data);
    }

    public static String postToMq(String topicCode,long delay_second, String data) {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Authorization", AUTHORIZATION);
        String orgSysType = "XINGHUO";
        if (!INSTITUTION.equals("xinghuo")) {
            orgSysType = "ADVANCE";
        }
        String param = JSON.toJSONString(new MessageQueueDataVo(data, orgSysType));
        try {
            param = URLEncoder.encode(param, HttpClientUtil.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String postUrl = MQ_URL + "?topic_code=" +  topicCode + "&delay_second=" + delay_second + "&data=" + param;
        String result = HttpClientUtil.doPost(postUrl, null, headers, HttpClientUtil.CHARSET);
        LOGGER.info("推送消息到消息中心topicCode：" + topicCode + ",delay_second:" + delay_second + ",data：" + data + ",结果：" + result);
        return result;
    }

    /**
     * @param topicCode  消息代码
     * @param data  参数
     * @param pushTime  推送时间   YYYY-MM-DD HH:mm:ss
     * @return
     */
    public static String postToMq(String topicCode, String data,String pushTime) {
        long times = DateTools.getSecondsBetweenTwoDays(DateTools.getCurrentDateTime(),pushTime);
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Authorization", AUTHORIZATION);
        String param = JSON.toJSONString(new MessageQueueDataVo(data));
        try {
            param = URLEncoder.encode(param, HttpClientUtil.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String postUrl = MQ_URLmqUrl + "?topic_code=" +  topicCode + "&data=" + param+"&delay_second="+times;
        String result = HttpClientUtil.doPost(postUrl, null, headers, HttpClientUtil.CHARSET);
        LOGGER.info("推送消息到消息中心topicCode：" + topicCode + ",data：" + data + ",结果：" + result+ ",delay_second：" + times);
        return result;
    }

    public static String postToMq(String topicCode, String data) {
    	return postToMq(topicCode,0,data);
    }

    public enum TopicCode {

    	//BOSS_TONGLIAN_CALLBACK_HANDLE("BOSS_TONGLIAN_CALLBACK_HANDLE", "通联支付队列回查"),
        MINI_CLASS_COURSE_DATE_SYNC("MINI_CLASS_COURSE_DATE_SYNC", "小班课程日期变更"),
        MINI_CLASS_OTHER_PRODUCT_SYNC("MINI_CLASS_OTHER_PRODUCT_SYNC", "小班资料费关联关系同步"),
        OTHER_PRODUCT_SYNC("OTHER_PRODUCT_SYNC", "资料费同步");
        
        private String value;
        private String name;
        
        private TopicCode(String value, String name) {
            this.value = value;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name();
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
    
}
