package com.eduboss.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.pad.common.CmsContentStatus;
import com.pad.common.CmsContentType;
import com.pad.common.CmsUseType;

public class EnumHelper {
	
	private static Map<String, Object> enumMap = new HashMap<String, Object>();
	
	public static synchronized Object getEnum(String enumName) {
		if (EnumHelper.enumMap.isEmpty()) {
			//随便拿对应enum里任意一个值，本方法目的是取得其中一个enum实例用来调用values()来获得所有选项
			enumMap.put("ContractPaidStatus", ContractPaidStatus.PAID);
			enumMap.put("ContractStatus", ContractStatus.FINISHED);
			enumMap.put("ContractType", ContractType.INIT_CONTRACT);
			enumMap.put("CourseRequirementStatus", CourseRequirementStatus.ARRENGED);
			enumMap.put("CustomerDealStatus", CustomerDealStatus.FOLLOWING);
			enumMap.put("CustomerDeliverType", CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
			enumMap.put("DataDictCategory", DataDictCategory.CUS_ORG);
			enumMap.put("ProcedureList", ProcedureList.INITFINACEDATABYDATE);
			enumMap.put("MessageDeliverType", MessageDeliverType.ALL);
			enumMap.put("MessageType", MessageType.CONVERSATION);
			enumMap.put("OrganizationType", OrganizationType.BRENCH);
			enumMap.put("OrganizationTypeHrms", OrganizationTypeHrms.GROUP);
			enumMap.put("PayWay", PayWay.CASH);
			enumMap.put("ProductType", ProductType.ONE_ON_ONE_COURSE);
			enumMap.put("RoleCode", RoleCode.CONSULTOR);
			enumMap.put("RoleResourceType", RoleResourceType.ANON_RES);
			enumMap.put("StudentStatus", StudentStatus.CLASSING);
			enumMap.put("UserWorkType", UserWorkType.FULL_TIME);
			enumMap.put("UserType",UserType.EMPLOYEE_USER);
			enumMap.put("CustomerEventType", CustomerEventType.APPOINTMENT);
			enumMap.put("CourseStatus", CourseStatus.NEW);
			enumMap.put("CourseRequirementCetegory", CourseRequirementCetegory.NEW_CONTRACT);
			enumMap.put("WeekDay", WeekDay.MON);
//			enumMap.put("CustomerType", CustomerType.GROUND_CALLING);
			enumMap.put("MonthType", MonthType.FEB);//月份
			enumMap.put("QuarterType", QuarterType.SPRINGTIME);//季度
			enumMap.put("DateVeidooType", DateVeidooType.YEAR);//时间维度类型
			enumMap.put("MiniClassStatus", MiniClassStatus.PENDDING_START);
			enumMap.put("MiniClassCourseStatus", MiniClassCourseStatus.NEW);
			enumMap.put("BaseStatus", BaseStatus.TRUE);
			enumMap.put("ContractProductStatus", ContractProductStatus.NORMAL);
			enumMap.put("PhoneEvent", PhoneEvent.INCOMING_TEL_RING);
			enumMap.put("PhoneType", PhoneType.INCOMING_TEL);
			enumMap.put("MonitorSubject", MonitorSubject.RECEPTIONIST_INPUT_RES);
			enumMap.put("SummaryCycleType", SummaryCycleType.WEEK);
			enumMap.put("SummaryTableType", SummaryTableType.CAMPUS);
			enumMap.put("SummaryClassType", SummaryClassType.ONE_ON_ONE);
			enumMap.put("MonitorUiSubject", MonitorUiSubject.COURSE_MONITOR_SUMMARY);
			enumMap.put("MiniClassAttendanceStatus", MiniClassAttendanceStatus.NEW);
			enumMap.put("PromotionType", PromotionType.DISCOUNT);
			enumMap.put("MiniClassCourseType", MiniClassCourseType.CONSTANT);
			enumMap.put("BonusType", BonusType.NORMAL);
			enumMap.put("AuditStatus", AuditStatus.VALIDATE);
			enumMap.put("StudentFileType", StudentFileType.COACHING);
			enumMap.put("Dimensionality", Dimensionality.BRENCH_DIM);
			enumMap.put("ChargeType", ChargeType.NORMAL);
			enumMap.put("StudentOneOnOneStatus", StudentOneOnOneStatus.NEW);
			enumMap.put("StudentSmallClassStatus", StudentSmallClassStatus.NEW);
			enumMap.put("SchedulerExecuteStatus", SchedulerExecuteStatus.SUCCESS);
			enumMap.put("PayType", PayType.REAL);
			enumMap.put("VisitType", VisitType.NOT_COME);
			enumMap.put("MobileType", MobileType.ANDROID);
			enumMap.put("ValidStatus", ValidStatus.VALID);
			enumMap.put("ResourcePoolJobType", ResourcePoolJobType.DISTRIBUTABLE);
			enumMap.put("WorkRemindTime", WorkRemindTime.FIVE_MINUTE);
			enumMap.put("AppointmentType", AppointmentType.APPOINTMENT);
			enumMap.put("StudentOneOnManyStatus", StudentOneOnManyStatus.NEW);
			enumMap.put("OtmClassStatus", OtmClassStatus.PENDDING_START);
			enumMap.put("PushMsgType", PushMsgType.SYSTEM_NOTICE);
			enumMap.put("FundsChangeAuditStatus", FundsChangeAuditStatus.UNAUDIT);
			enumMap.put("MsgNo", MsgNo.M1);
			enumMap.put("NewsType", NewsType.NEWSBANNER);
			enumMap.put("LectureClassStatus", LectureClassStatus.CONPELETE);
			enumMap.put("LectureClassStudentChargeStatus", LectureClassStudentChargeStatus.CHARGED);
			enumMap.put("LectureClassAttendanceStatus", LectureClassAttendanceStatus.CONPELETE);
			enumMap.put("RefundFormType", RefundFormType.ACCOUNT_TRANSFER);
			enumMap.put("RefundAuditStatus", RefundAuditStatus.TO_SUBMIT);
			enumMap.put("StudentType", StudentType.ENROLLED);
			enumMap.put("CustomerAuditStatus", CustomerAuditStatus.TOBE_AUDIT);
			enumMap.put("EvidenceAuditStatus", EvidenceAuditStatus.NOT_AUDIT);
			enumMap.put("PosMachineStatus", PosMachineStatus.ACTIVATE);
			enumMap.put("FundsChangeAuditType", FundsChangeAuditType.SYSTEM);
			enumMap.put("MatchingStatus", MatchingStatus.NOT_YET_MATCH);
			enumMap.put("PreparationType", PreparationType.ONE_ON_ONE);
			enumMap.put("TeacherType", TeacherType.FUNCTOINS_TEACHER);
			enumMap.put("TeacherLevel", TeacherLevel.NORMAL_STAR_FIFTH);
			enumMap.put("TeacherFunctions", TeacherFunctions.FUNCTOINS_DISCIPLINE_LEADER);
			enumMap.put("FunctionsLevel", FunctionsLevel.COMMISSIONER);
			enumMap.put("SaleChannel", SaleChannel.OFF_LINE);
			enumMap.put("FundsChangeType", FundsChangeType.HUMAN);
			enumMap.put("PromiseAuditStatus", PromiseAuditStatus.UNAUDIT);
			enumMap.put("PromiseReturnType", PromiseReturnType.RETURN_TO_ACCOUNT);
			enumMap.put("StudentPromiseStatus", StudentPromiseStatus.ING);
			enumMap.put("ProductSingleSubject",ProductSingleSubject.ALL_SUBJECT);
			enumMap.put("EvaluationType",EvaluationType.A);
			enumMap.put("Semester",Semester.LAST_SEMESTER);
			enumMap.put("AchievementType",AchievementType.SCORE);
			enumMap.put("AchievementAbstractType",AchievementAbstractType.SCORE_RANKING);
			enumMap.put("StandardCategory",StandardCategory.PROGRESS);
			enumMap.put("LiveFinanceType",LiveFinanceType.INCOME);
			enumMap.put("LiveContactType",LiveContactType.NEW);
			enumMap.put("CmsContentStatus", CmsContentStatus.PUBLISH);
			enumMap.put("CmsContentType", CmsContentType.DOCUMENT);
			enumMap.put("CmsUseType", CmsUseType.LOGIN_USER);
			enumMap.put("RoleType", RoleType.NORMAL);
		}
		return  EnumHelper.enumMap.get(enumName);
	}
	
	public static List<NameValue> getEnumOptions(String enumName) {
		List<NameValue> nvs = new ArrayList<NameValue> ();
		
		Object enumObject = getEnum(enumName);
		
		try {
			Method m = enumObject.getClass().getDeclaredMethod("values", null);
			Object[] values = (Object[]) m.invoke(enumObject, null);
			for (Object object : values) {
				Method getValue = object.getClass().getDeclaredMethod("getValue", null);
				String valueString = (String)getValue.invoke(object, null);
				Method getName = object.getClass().getDeclaredMethod("getName", null);
				String nameString = (String)getName.invoke(object, null);
				if(!"CITY".equals(valueString) && !"PROVINCE".equals(valueString))
					nvs.add(SelectOptionResponse.buildNameValue(nameString, valueString));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nvs;
	}
	
	public static String getEnumIdByName(String name,String enumName){
		List<NameValue> list= EnumHelper.getEnumOptions(enumName);
		for(NameValue val : list){
			if(val.getName().equals(name))
				return val.getValue();
		}
		return null;
	}
	
	 public static void main(String[] args) {   
    	List<NameValue> nvs = getEnumOptions("ProductType");
    	for (NameValue nv : nvs) {
    		System.out.println(nv.getValue());
    	}
    }
}
