package com.eduboss.mq.producer;

import org.apache.log4j.Logger;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;

/**
 * MQ 使用Spring发送普通消息
 */
public class MQProducer4Spring {
	
	private static final Logger LOGGER = Logger.getLogger(MQProducer4Spring.class);
	
	private static final String COURSE_SYN_TOPIC = PropertiesUtils.getStringValue("mq.access.course_syn_topic");
	private static final String COURSE_SYN_TAG = PropertiesUtils.getStringValue("mq.access.course_syn_tag");
	
	public static SendResult sendMessage(String topic, String tag, byte[] body) {
		return sendMessage(topic, tag, "", body);
	}
	
	public static SendResult sendMessage(String topic, String tag, String key, byte[] body) {
		ProducerBean producer = (ProducerBean) ApplicationContextUtil.getContext().getBean("producer");
		LOGGER.info("开始发送消息,topic:" + topic + ",tag:" + tag + ",key:" + key + ",body:" + body);
		SendResult sendResult = null;
		for (int i = 0; i < 1; i++) {
			Message message = null;
			if (StringUtil.isNotBlank(key)) {
				message = new Message(topic, tag, key, body);
			} else {
				message = new Message(topic, tag, body);
			}
            sendResult = producer.send(message);
            if (sendResult != null) {
            	LOGGER.info("发送消息成功,topic:" + topic + ",tag:" + tag + ",key:" + key + ",body:" 
            			+ body + ",messageId:" + sendResult.getMessageId());
            } else {
            	LOGGER.info("发送失败,topic:" + topic + ",tag:" + tag + ",key:" + key + ",body:" + body);
            }
        }
		return sendResult;
	}

	public static String getCourseSynTopic() {
		return COURSE_SYN_TOPIC;
	}

	public static String getCourseSynTag() {
		return COURSE_SYN_TAG;
	}

}
