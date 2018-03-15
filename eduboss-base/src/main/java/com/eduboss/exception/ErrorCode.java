/**
 * 
 */
package com.eduboss.exception;

import java.util.HashMap;
import java.util.Map;


/**
 * 错误提示
 * @classname	ErrorCode.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2011-4-22  11:17:22
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
public enum ErrorCode {
	
	USER_ID_EMPTY("001-用户id不用为空"),
	
	USER_NOT_FOUND("002-用户不存在"),
	
	ORG_ID_EMPTY("003-组织架构不能为空"),
	
	ORG_NOT_FOUND("003-组织架构不存在"),
	
	HAS_USER_FOR_DELETE("004-该角色配置下还有用户关联，请清除所有用户与该角色关联再删除角色配置。"),
	
	HAS_ORG_FOR_DELETE("004-所选记录下属还有关联的组织架构，请先移除本记录下属组织架构再删除。"),
	
	ROLE_NOT_FOUND("005-角色不存在"),
	
	RES_NOT_FOUND("006-资源不存在"),
	
	EMPTY_CONTENT("008-内容不能为空"),
	
	TOKEN_ERROR("013-登录令牌校验失败"),

	OPTION_CATEGORY_EMPTY("014-选择项类别不能为空"),
	
	PARAMETER_EMPTY("015-参数不能为空"),
	
	STUDENT_INFO_EMPTY("016-学生信息不能为空"),
	
	NO_LOGIN_USER_INFO("017-没发登录用户信息，请重新登录"),
	
	USRR_ORG_NOT_FOUND("18-没能找到当前用户组织架构"),
	
	PARAMETER_FORMAT_ERROR("19-参数格式错误"),
	
	COURSE_NOT_FOUND("20-找不到课程"),
	
	COURSE_NOT_AUDITED("21-课时未核对"),
	
	STUDENT_NOT_FOUND_BY_ATTENDANCE_NO("22-根据考勤编号找不到学生"),
	
	USER_AUTHROTY_FAIL("23-当前用户没有本操作权限"),
	
	CUSTOMER_NOT_FOUND("24-找不到客户"),
	
	CUSTOMER_CONTACT_FOUND("25-该号码客户已存在。"),
	
	COURSE_STUDY_MANAGEMENT_NOT_AUDIT("26-学管未核对课时。"),
	
	CUSTOMER_HAD_BEED_CONFIRM("27-客户已经确认。"),
	
	STUDENT_NOT_FOUND_BY_IC_CARD_NO("28-根据IC卡编号找不到学生"),
	
	STUDENT_ATTENDANCE_NUM_EXIST("29-学生考勤编号已存在，请重新录入。"),
	
	TEACHER_NOT_FOUND("30-老师不存在"),
	
	COURSE_CAN_NOT_BE_DELETE("31-只有未上课的课程才能被删除。"),
	
	STUDENT_ID_EMPTY("32-学生编号不能为空。"),
	
	STUDENT_PASSWORD_EMPTY("33-学生密码不能为空。"),
	
	STUDENT_LOGIN_FAIL("33-用户名或密码错误，登录失败。"),
	
	CANCEL_CONTRACT_TYPE_OF_DEPOSIT("34-现在已取消\"定金合同\"功能"),
	
	SYNC_REFLUSH_REPORT("35-报表正在执行中，请稍后查询"),

	IMPORT_EXCEL("数据正在导入中，请稍后"),
	
	USERJOB_CONTACT_FOUND("36-该职位名称已存在，请修改后再保存。"),
	
    BAD_REQUEST("400-Bad Request"),
    
    VERIFICATION_INVALID("4030020-验证码失效"),
    
    VERIFICATION_ERROR("4030040-验证码错误"),
	
	SQL_ERROR("990-数据库错误"),
	
	PERMISSION_DENY("991-没有该操作的权限"),
	
	USER_NAME_PASSWORD_ERROR("998-用户名或密码错误"),
	
	SYSTEM_ERROR("999-系统错误，请联系管理员"), 
	
	MONEY_NOT_ENOUGH("300-合同资金不足够"), 
	
	MONEY_ERROR("301-合同资金出现异常"), 
	
	ONE_ON_ONE_MONEY_NOT_ENOUGH("302-课程扣费失败：资金不足，请确认学生所签合同是否已收费或所签合同的一对一课程是否已分配资金（合同未收完款时需要学管把合同已收款的资金分配给不同的课程账号后才能进行扣费）。"), 
	
	MIN_CLASS_MONEY_NOT_ENOUGH("303-课程扣费失败：资金不足，请确认学生所签合同是否已收费或所签合同的小班课程是否已分配资金（合同未收完款时需要学管把合同已收款的资金分配给不同的课程账号后进行扣费）。"),
	
	EMPTY_START_DATE("70-开始日期不能为空"),
	
	CONTRACT_TOTAL_LESS_PAID("401-合同修改后的金额小于已付款，请重新修改"), 
	
	ONE_ON_ONE_TOTAL_LESS_CONSUME("402-一对一课时总数不能小于已消费课时数"), 
	
	FREE_TOTAL_LESS_CONSUME("403-赠送课时数不能小于已消费的赠送课时数"), 
	
	DATA_TYPE_CONVERSION("数据类型转换错误"),
	
	CANT_DEL_WHEN_CHARGED("404-已扣费的小班产品不允许删除"), 
	
	JSON_PARSE_ERROR("71-JSON编译出错 "), 
	
	CAMPUS_ACCOUNT_ACHARGE_RECORDS("304-当前用户的归属组织架构只有校区下的才能扣费"),
	
	NOT_FIND_STUDY_MANAGER("305-本校区该学生还没有对应的学管，请先分配学管再排课"),
	
	NOT_SUPPORT_METHOD("405-调用出错,或者暂时不支持本方法!"),
	
	MAIL_SYS_DISABLED("501-邮箱功能已屏蔽!"),
	
	MAIL_SYS_CONNECT_ERR("502-邮件系统连接异常，请联系管理员!"),
	
	MAIL_SYS_UNIT_NOT_EXIST_ERR("503-邮件系统部门不存在，请先同步组织架构!"),
	
	MAIL_OVER_LIMIT("504-用户数已超出 License限制"),
	
	OTM_CLASS_MONEY_NOT_ENOUGH("601-课程扣费失败：资金不足，请确认学生所签合同是否已收费或所签合同的一对多课程是否已分配资金（合同未收完款时需要学管把合同已收款的资金分配给不同的课程账号后进行扣费）。"),
	
	SOCKET_TIMEOUT_ERROR("770-系统繁忙，请稍后再试！"),
	
	USB_GO_DIE("911-网络异常了，请稍后再试！"),
	
	LIVE_SYNC_BIT_ERROR("500-同步直播数据，出现超过两位小数"),
	
	HIBERNATE_VERSION_ERROR("678-数据已经重新更新，请再次执行"),
	
	LIVE_USER_PARAM_EMPTY("801-用户工号不为空或者姓名和电话不为空！"),
	
	LIVE_USER_EMPTY("802-用户不存在！"),
	
	LIVE_ORGANIZATION_EMPTY("803-用户组织架构不存在！"),
	
	LIVE_PARAM_EMPTY("804-参数不存在！"),
	
	LIVE_RESULT_EMPTY("805-没有可利用资源，请添加资源！"),
	
	LIVE_BRANCH_NO_PAY("806-该分公司暂未开通网络支付功能，请选择其他方式，或者联系管理员开通再使用！"),
	
	LIVE_TONGLIAN_PAY("807-调用通联接口错误！"),
	
	LIVE_PAYTYPE_PAY("808-支付类型只能是W01、A01、Q01,分别表示微信、支付宝、手机QQ！"),
	
	LIVE_TONGLIAN_PAYINFO("809-通联获取二维码失败！"),
	
	LIVE_DATEFORMAT_ERROR("810-日期转换异常！"),
	
	PHONE_LENGTH_ERROR("811-电话号码请至少输入六位数！"),
	
	PARAMETER_ERROR("4000000-参数错误")
	;
	
	/** The Constant value. */
	private final String value;
	
	/** The Constant STRING_TO_ENUM. */
    private static final Map<String, ErrorCode> STRING_TO_ENUM = new HashMap<String, ErrorCode>();
    
    static {
        for (ErrorCode e : values())
            STRING_TO_ENUM.put(e.getValue(), e);
    }
    
    ErrorCode(String value) {
        this.value = value;
    }
    
    /**
     * @return
     */
    public String getValue() {
        return value;
    }
    /**
     * @return
     */
    public Integer getErrorCode() {
    	String[] error = value.split("-");
    	return Integer.valueOf(error[0]);
    }
    /**
     * @return
     */
    public String getErrorString() {
    	String[] error = value.split("-");
    	return error.length > 1 ? error[1] : "";
    }
    /**
     * @param errorCode
     * @return
     */
    public String getErrorString(String errorCode) {
    	return STRING_TO_ENUM.get(errorCode).getErrorString();
    }
    
}
