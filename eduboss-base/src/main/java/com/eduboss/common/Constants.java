package com.eduboss.common;

public class Constants {
	
	public final static boolean REMIND_USER_MODIFY_DEFAULT_PASSWORD = true;  // 是否提醒用户修改初始密码
	public final static String DEFAULT_PASSWORD = "765432";
	
	public final static int ORG_LEVEL_BRENCH = 1;
	public final static int ORG_LEVEL_CAPUMS = 2;
	
	public final static  boolean DEBUG= true;//是否为debug模式，生产环境改成false


	public final static String STATUS_EMERGENCY_ = "STATUS_EMERGENCY_"; //紧急状态用户方redis的前缀
	
	public final static String FTP_SHOUFU_ = "FTP_SHOUFU_"; // FTP首付redis前缀
	
	public final static String FTP_SHOUFU_CURRENT_DATE_ = "FTP_SHOUFU_CURRENT_DATE_"; // FTP首付当天redis前缀
	
	public final static String FTP_KUAIQIAN_ = "FTP_KUAIQIAN_"; // FTP快钱redis前缀
	
	public final static String FTP_KUAIQIAN_CURRENT_DATE_ = "FTP_KUAIQIAN_CURRENT_DATE_"; // FTP快钱当天redis前缀
	
	/** 验证码时间限制key前缀  */
	public final static String REDIS_PRE_VERIFICATION_TIME_LIMIT_KEY = "verification:time:limit:";
    
    /** 验证码失效key前缀 */
	public final static String REDIS_PRE_VERIFICATION_INVALID_KEY = "verification:invalid:";



	public final static String MINI_CLASS_RPC = "MINI_CLASS_RPC_"; //小班rpc同步redis的前缀

	public final static String TEACHER_RPC = "TEACHER_RPC_"; //老师rpc同步redis的前缀

	public final static String STORAGE_ORDER = "STORAGE_ORDER_"; //在线报读订单入库redis的前缀

	public final static String SURRENDER_ORDER = "SURRENDER_ORDER_"; //订单退款调用boss退费redis的前缀


	public final static String REDIS_PRE_OSS_UPLOAD_ID = "oss:uploadid:";

	public final static String NEW_ORG_ROLE="NEW_ORG_ROLE";


	public final static String INFO="INFO";
	public final static String APPEND="APPEND";
	public final static String RESOURCE_POOL="RESOURCE_POOL";
	public final static String ALL="ALL";

	public final static String ZERO="0";
	public final static Integer NUM_ZERO=0;

	/**
	 * AUTH 鉴权帐号密码KEY
	 */
	public final static String API_USER="API_USER";
	public final static String API_PWD="API_PWD";


	public final static String CONTENT_TYPE_JSON="application/json";

	/**
	 *人事岗位ID
	 */
	public final static String HRMS_TEACHER_IDS="HRMS_TEACHER_IDS";
	public final static String HRMS_CONSULTOR_IDS="HRMS_CONSULTOR_IDS";
	public final static String HRMS_MANERGER_IDS="HRMS_MANERGER_IDS";
	public final static String BOSS_TEACHER_NAME="BOSS_TEACHER_NAME";
	public final static String BOSS_CONSULTOR_NAME="BOSS_CONSULTOR_NAME";
	public final static String BOSS_MANERGER_NAME="BOSS_MANERGER_NAME";
	public final static String DEFAULT_ROLE_ID="DEFAULT_ROLE_ID";
}
