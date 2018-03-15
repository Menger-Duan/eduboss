package com.eduboss.utils;

import java.util.List;

import com.eduboss.domainVo.MobilePushMsgRecordVo;
import com.eduboss.domainVo.MobilePushMsgSessionVo;
import com.eduboss.service.MobilePushMsgService;

public class MessagePushingUtil extends Thread {
	// ios 手机端
	public static final String APIKEY_IOS_TEACHERAPP = PropertiesUtils.getStringValue("push.baidu.apikey.edubossTeacher");
	public static final String SECRETKEY_IOS_TEACHERAPP = PropertiesUtils.getStringValue("push.baidu.secretkey.edubossTeacher");
	public static final String APIKEY_IOS_STUAPP = PropertiesUtils.getStringValue("push.baidu.apikey.edubossStudent");
	public static final String SECRETKEY_IOS_STUAPP = PropertiesUtils.getStringValue("push.baidu.secretkey.edubossStudent");
	public static final String APIKEY_IOS_MANAGEAPP = PropertiesUtils.getStringValue("push.baidu.apikey.edubossManager");
	public static final String SECRETKEY_IOS_MANAGEAPP = PropertiesUtils.getStringValue("push.baidu.secretkey.edubossManager");
	// android 手机端
	public static final String APIKEY_ANDROID_TEACHERAPP = PropertiesUtils.getStringValue("push.baidu.apikey.android.edubossTeacher");
	public static final String SECRETKEY_ANDROID_TEACHERAPP = PropertiesUtils.getStringValue("push.baidu.secretkey.android.edubossTeacher");
	public static final String APIKEY_ANDROID_STUAPP = PropertiesUtils.getStringValue("push.baidu.apikey.android.edubossStudent");
	public static final String SECRETKEY_ANDROID_STUAPP = PropertiesUtils.getStringValue("push.baidu.secretkey.android.edubossStudent");
	public static final String APIKEY_ANDROID_MANAGEAPP = PropertiesUtils.getStringValue("push.baidu.apikey.android.edubossManager");
	public static final String SECRETKEY_ANDROID_MANAGEAPP = PropertiesUtils.getStringValue("push.baidu.secretkey.android.edubossManager");
	
	public static final String ENV = PropertiesUtils.getStringValue("push.baidu.evn");
	
	
	
	protected static MobilePushMsgService mobilePushMsgService =  ApplicationContextUtil.getContext().getBean(MobilePushMsgService.class);
	
	private static volatile MessagePushingUtil instance = null;

	private static boolean isStop = false;
	
	public void run() {
		while (!isStop) {
			//System.out.println(this.currentThread().toString() + " MESSAGE PUSH RUNNING");

                try {
				    if(mobilePushMsgService.hasNeedSendQueueMsg()) {
                        //System.out.println("############################################  there is a mesg to insert into the sending queue ######################");
                        List<MobilePushMsgSessionVo> listOfSessionVo = mobilePushMsgService.getNeedSendQueueSession();
                        for (MobilePushMsgSessionVo sessionVo : listOfSessionVo) {
                            mobilePushMsgService.pushSendQueue(sessionVo);
                        }
                    }
                    if(mobilePushMsgService.hasNewMsg()) {
                        //System.out.println("############################################  there is a mesg to push  ######################");
                        List<MobilePushMsgRecordVo> listOfRecordVo = mobilePushMsgService.getNeedSendRecords();
                        for (MobilePushMsgRecordVo needSendRecord : listOfRecordVo) {
                            mobilePushMsgService.pushMessage(needSendRecord);
                        }
                    }
                  
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {                	
                	  try {
						this.sleep(20000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }

        }
		this.instance = null;
	}
	
	private MessagePushingUtil() {
	}

	public static MessagePushingUtil startRun() {
		if (instance == null) {
			synchronized (MessagePushingUtil.class) {
				if (instance == null) {
					instance = new MessagePushingUtil();
					instance.isStop = false; 
					instance.start();
				}
			}
		}
		return instance;
	}
	
	public synchronized static void stopRun() {
		if (instance != null) {
			instance.isStop =  true;	
		}
	}
	

}
