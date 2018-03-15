package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.DataDictCategory;
import com.eduboss.common.EnumHelper;
import com.eduboss.common.ProductType;
import com.eduboss.common.StudentStatus;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.CustomerDao;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ProductDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.StudentSchoolDao;
import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.ClassroomManage;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Customer;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Product;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentSchool;
import com.eduboss.domain.StudnetAccMv;
import com.eduboss.domain.User;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ClassroomManageService;
import com.eduboss.service.ContractService;
import com.eduboss.service.ImportContractStuAndCusService;
import com.eduboss.service.ProductService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Service
public class ImportContractStuAndCusServiceImpl implements ImportContractStuAndCusService {
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private CustomerDao customerDao; 
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private StudentSchoolDao studentSchoolDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SmallClassService smallClassService;
	
	@Autowired
	private StudnetAccMvDao studnetAccMvDao;
	
	@Autowired
	private ClassroomManageService classroomManageService;
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	public List<Map<String,String>> saveImportContractStuAndCusData(List<List<Object>> list) {
		List<Map<String,String>> mesList = new ArrayList<Map<String,String>>();
		Map<String, Integer> title = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=new HashMap<String,String>();
			List<Object> listObj = list.get(i);
			if (i < 3)
				continue;
			if (i == 3) {
				for (int j = 0; j < listObj.size(); j++) {
					title.put("" + listObj.get(j), new Integer(j));
				}
			} else {
				if(title.get("上传结果")!=null && "成功".equals(StringTrim(listObj,title,"上传结果"))){
					map.put("isSave", "old成功");
				}else{
					try{
						String brench = StringTrim(listObj,title,"分公司");//
						String campus = StringTrim(listObj,title,"校区");//
						String fatherName = StringTrim(listObj,title,"父亲姓名");
						String fatherPhone = StringTrim(listObj,title,"父亲电话");
						String motherName = StringTrim(listObj,title,"母亲姓名");
						String notherPhone = StringTrim(listObj,title,"母亲电话");
						String name = StringTrim(listObj,title,"学生姓名");
						String sex = StringTrim(listObj,title,"性别");
						String studentAddress = StringTrim(listObj,title,"家庭地址");
						String studentPhone = StringTrim(listObj,title,"学生电话");
						String schoolName = StringTrim(listObj,title,"学校");
						String grade = StringTrim(listObj,title,"年级");//
						String bothday = StringTrim(listObj,title,"生日");
						String productName = StringTrim(listObj,title,"科目");//
						String studyManegerName = StringTrim(listObj,title,"学管师");//
						String signStaffName = StringTrim(listObj,title,"签约人");//
						String enrolDate = StringTrim(listObj,title,"入学时间");
						String oneOnOneUnitPrice = StringTrim(listObj,title,"单价（现在）");
						String oneOnOneRemainingClass = StringTrim(listObj,title,"初始化一对一课时");
						String freeRemainingHour= StringTrim(listObj,title,"初始化一对一赠送课时");
						String oneOnOnePaidAmount =StringTrim(listObj,title,"一对一剩余资金");
						
						String miniProductName1 = StringTrim(listObj,title,"小班产品（1）");
						String miniClassName1 = StringTrim(listObj,title,"小班名称（1）");
						String miniHour1String=StringTrim(listObj,title,"开课后剩余（1）");
						BigDecimal miniHour1 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour1String)){
							miniHour1 = BigDecimal.valueOf(Double.valueOf(miniHour1String));
						}
						
						String miniProductName2 = StringTrim(listObj,title,"小班产品（2）");
						String miniClassName2 = StringTrim(listObj,title,"小班名称（2）");
						String miniHour2String=StringTrim(listObj,title,"开课后剩余（2）");
						BigDecimal miniHour2 = BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour2String)){
							miniHour2 = BigDecimal.valueOf(Double.valueOf(miniHour2String));
						}
						
						String miniProductName3 = StringTrim(listObj,title,"小班产品（3）");
						String miniClassName3 = StringTrim(listObj,title,"小班名称（3）");
						String miniHour3String=StringTrim(listObj,title,"开课后剩余（3）");
						BigDecimal miniHour3 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour3String)){
							miniHour3 = BigDecimal.valueOf(Double.valueOf(miniHour3String));
						}
						
						String miniProductName4 = StringTrim(listObj,title,"小班产品（4）");
						String miniClassName4 = StringTrim(listObj,title,"小班名称（4）");
						String miniHour4String=StringTrim(listObj,title,"开课后剩余（4）");
						BigDecimal miniHour4 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour4String)){
							miniHour4 = BigDecimal.valueOf(Double.valueOf(miniHour4String));
						}
						
						
						String miniProductName5 = StringTrim(listObj,title,"小班产品（5）");
						String miniClassName5 = StringTrim(listObj,title,"小班名称（5）");
						String miniHour5String=StringTrim(listObj,title,"开课后剩余（5）");
						BigDecimal miniHour5 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour5String)){
							miniHour5 = BigDecimal.valueOf(Double.valueOf(miniHour5String));
						}
						
						String miniProductName6 = StringTrim(listObj,title,"小班产品（6）");
						String miniClassName6 = StringTrim(listObj,title,"小班名称（6）");
						String miniHour6String=StringTrim(listObj,title,"开课后剩余（6）");
						BigDecimal miniHour6 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour6String)){
							miniHour6 = BigDecimal.valueOf(Double.valueOf(miniHour6String));
						}
						
						String miniProductName7 = StringTrim(listObj,title,"小班产品（7）");
						String miniClassName7 = StringTrim(listObj,title,"小班名称（7）");
						String miniHour7String=StringTrim(listObj,title,"开课后剩余（7）");
						BigDecimal miniHour7 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour7String)){
							miniHour7 = BigDecimal.valueOf(Double.valueOf(miniHour7String));
						}
						
						String miniProductName8 = StringTrim(listObj,title,"小班产品（8）");
						String miniClassName8 = StringTrim(listObj,title,"小班名称（8）");
						String miniHour8String=StringTrim(listObj,title,"开课后剩余（8）");
						BigDecimal miniHour8 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour8String)){
							miniHour8 = BigDecimal.valueOf(Double.valueOf(miniHour8String));
						}
						
						String miniProductName9 = StringTrim(listObj,title,"小班产品（9）");
						String miniClassName9 = StringTrim(listObj,title,"小班名称（9）");
						String miniHour9String=StringTrim(listObj,title,"开课后剩余（9）");
						BigDecimal miniHour9 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour9String)){
							miniHour9 = BigDecimal.valueOf(Double.valueOf(miniHour9String));
						}
						
						String miniProductName10 = StringTrim(listObj,title,"小班产品（10）");
						String miniClassName10 = StringTrim(listObj,title,"小班名称（10）");
						String miniHour10String=StringTrim(listObj,title,"开课后剩余（10）");
						BigDecimal miniHour10 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour10String)){
							miniHour10 = BigDecimal.valueOf(Double.valueOf(miniHour10String));
						}
						
						String miniProductName11 = StringTrim(listObj,title,"小班产品（11）");
						String miniClassName11 = StringTrim(listObj,title,"小班名称（11）");
						String miniHour11String=StringTrim(listObj,title,"开课后剩余（11）");
						BigDecimal miniHour11 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour11String)){
							miniHour11 = BigDecimal.valueOf(Double.valueOf(miniHour11String));
						}
						
						String miniProductName12 = StringTrim(listObj,title,"小班产品（12）");
						String miniClassName12 = StringTrim(listObj,title,"小班名称（12）");
						String miniHour12String=StringTrim(listObj,title,"开课后剩余（12）");
						BigDecimal miniHour12 =  BigDecimal.ZERO;
						if(StringUtil.isNotEmpty(miniHour12String)){
							miniHour12 = BigDecimal.valueOf(Double.valueOf(miniHour12String));
						}
						
						if(bothday!=null && bothday.length()>10){
							bothday=bothday.substring(0,10);
						}
						if(StringUtil.isNotEmpty(fatherPhone) && fatherPhone.indexOf(".") > -1){
							fatherPhone=fatherPhone.substring(0,fatherPhone.indexOf("."));
						}
						if(StringUtil.isNotEmpty(notherPhone) && notherPhone.indexOf(".") > -1){
							notherPhone=notherPhone.substring(0,notherPhone.indexOf("."));
						}
						if(StringUtil.isNotEmpty(studentPhone) && studentPhone.indexOf(".") > -1){
							studentPhone=studentPhone.substring(0,studentPhone.indexOf("."));
						}
						boolean isError =false;
						String errorMes="";
						if(StringUtil.isEmpty(name)){
							isError=true;
							errorMes+="学生姓名不能为空，";
						}else{
							if(studentDao.isExistsStudent(name,studentPhone,fatherPhone,notherPhone)){
								isError=true;
								errorMes+="学生已存在，";
							}
						}
						
						if(StringUtil.isEmpty(oneOnOneUnitPrice)){
							oneOnOneUnitPrice="0";
						}else if( BigDecimal.valueOf(Double.valueOf(oneOnOneUnitPrice)).compareTo(new BigDecimal(0))<0){
							isError=true;
							errorMes+="单价（现在）小于0，";
						}
						
						if(StringUtil.isEmpty(oneOnOneRemainingClass)){
							oneOnOneRemainingClass="0";
						} else if( BigDecimal.valueOf(Double.valueOf(oneOnOneRemainingClass)).compareTo(new BigDecimal(0))<0){
							isError=true;
							errorMes+="初始化一对一课时不能小于0，";
						}
						
						if (StringUtil.isEmpty(oneOnOnePaidAmount)){
							oneOnOnePaidAmount="0";
						}else if(BigDecimal.valueOf(Double.valueOf(oneOnOnePaidAmount)).compareTo(new BigDecimal(0))<0){
							isError=true;
							errorMes+="一对一剩余资金不能小于0，";
						}
						
						if(StringUtil.isEmpty(freeRemainingHour)){
							freeRemainingHour="0";
							//isError=true;
							//errorMes+="初始化一对一赠送课时不能为空或小于0，";
						} else if (BigDecimal.valueOf(Double.valueOf(freeRemainingHour)).compareTo(new BigDecimal(0))<0) {
							freeRemainingHour="0";
							//赠送课时小于0没有意义，把剩余资金减去赠送课时*单价，然后将赠送课时变为0，以达到剩余课时一致的效果 贵伴1001
							//oneOnOnePaidAmount = new BigDecimal(oneOnOnePaidAmount).add(new BigDecimal(freeRemainingHour).multiply(new BigDecimal(oneOnOneUnitPrice))).setScale(2).toString();
							//freeRemainingHour = "0";
						}
						
						
						String brenchId=null;
						if(StringUtil.isNotEmpty(brench)){
							brenchId=organizationDao.getOrganizationIdByName(brench);
						}
						if(StringUtil.isEmpty(brenchId)){
							isError=true;
							errorMes+="系统不能查询到分公司名称，";
						}
						String campusId=null;
						if(StringUtil.isNotEmpty(campus)){
							campusId=organizationDao.getOrganizationIdByName(campus);
						}
						if(StringUtil.isEmpty(campusId)){
							isError=true;
							errorMes+="系统不能查询到校区名称，";
						}
						String gradeId=null;
						if(StringUtil.isNotEmpty(grade)){
							gradeId=dataDictDao.getDataDictIdByName(grade,DataDictCategory.STUDENT_GRADE);
						}
						if(StringUtil.isEmpty(gradeId)){
							isError=true;
							errorMes+="系统不能查询到年级名称，";
						}
						
						boolean isCheck=false;
						if(BigDecimal.valueOf(Double.valueOf(oneOnOneRemainingClass)).compareTo(new BigDecimal(0))>0){
							isCheck=true;
						}
						
						String productId=null;
						if(StringUtil.isNotEmpty(gradeId) && StringUtil.isNotEmpty(productName)){
							productId=productDao.getProductIdByNameAndGrade(productName,gradeId,brenchId);
						}
						if(isCheck && StringUtil.isEmpty(productId)){
							isError=true;
							errorMes+="系统（产品）不能查询到科目名称，";
						}
						String studyManegerId=null;
						if(StringUtil.isNotEmpty(studyManegerName)){
							studyManegerId=userDao.getUserIdByName(studyManegerName);
						}
						if(isCheck && StringUtil.isEmpty(studyManegerId)){
							isError=true;
							errorMes+="系统不能查询到学管师，";
						}
						
						Product oneOnOneProduct =null;
						Product freeProduct = null;
						if(StringUtil.isNotEmpty(gradeId)){
							oneOnOneProduct= productDao.getOneOnOneNormalProduct(gradeId,new Organization(brenchId));
							freeProduct = productDao.getFreeHourProduct(gradeId);
						}
						
						//一对一统一单价
						if(StringUtil.isNotEmpty(oneOnOneRemainingClass) 
								&& BigDecimal.valueOf(Double.valueOf(oneOnOneRemainingClass)).compareTo(new BigDecimal(0))>0){
							if(oneOnOneProduct == null){
								isError=true;
								errorMes+="系统没有查询到一对一统一单价产品，";
							}
						}
						//一对一赠送课时
						if(StringUtil.isNotEmpty(freeRemainingHour) &&  BigDecimal.valueOf(Double.valueOf(freeRemainingHour)).compareTo(new BigDecimal(0))>0){
							if(freeProduct == null){
								isError=true;
								errorMes+="系统没有查询到一对一赠送单价产品，";
							}
						}
						
						Product miniProduct1 = null;
						Product miniProduct2 = null;
						Product miniProduct3 = null;
						Product miniProduct4 = null;
						Product miniProduct5 = null;
						
						Product miniProduct6 = null;
						Product miniProduct7 = null;
						Product miniProduct8 = null;
						Product miniProduct9 = null;
						Product miniProduct10 = null;
						Product miniProduct11 = null;
						Product miniProduct12 = null;
						
						MiniClass miniClass1 = null;
						MiniClass miniClass2 = null;
						MiniClass miniClass3 = null;
						MiniClass miniClass4 = null;
						MiniClass miniClass5 = null;
						
						MiniClass miniClass6 = null;
						MiniClass miniClass7 = null;
						MiniClass miniClass8 = null;
						MiniClass miniClass9 = null;
						MiniClass miniClass10 = null;
						MiniClass miniClass11 = null;
						MiniClass miniClass12 = null;
						
						if(campusId!=null &&  miniHour1.compareTo(new BigDecimal(0))>0){
							miniClass1 = smallClassService.getMiniClassByName(miniClassName1, campusId);
							if(miniClass1 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（1）:"+miniClassName1+"，";
							}else{
								miniProduct1=miniClass1.getProduct();
							}
						}else if(StringUtil.isNotEmpty(miniProductName1)){
							miniProduct1 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName1,new Organization(brenchId));
							if(miniProduct1 == null){
								isError=true;
								errorMes+="系统没有查询到小班-1产品，";
							}
						}
						
						if(miniHour2.compareTo(new BigDecimal(0))>0){
							miniClass2 = smallClassService.getMiniClassByName(miniClassName2, campusId);
							if(miniClass2 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（2）:"+miniClassName2+"，";
							}else{
								miniProduct2=miniClass2.getProduct();
							}
						}else if(StringUtil.isNotEmpty(miniProductName2)){
							miniProduct2 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName2,new Organization(brenchId));
							if(miniProduct2 == null){
								isError=true;
								errorMes+="系统没有查询到小班-2产品，";
							}
						}
						
						if( miniHour3.compareTo(new BigDecimal(0))>0){
							miniClass3 = smallClassService.getMiniClassByName(miniClassName3, campusId);
							if(miniClass3 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（3）:"+miniClassName3+"，";
							}else{
								miniProduct3=miniClass3.getProduct();
							}
						}else if(StringUtil.isNotEmpty(miniProductName3)){
							miniProduct3 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName3,new Organization(brenchId));
							if(miniProduct3 == null){
								isError=true;
								errorMes+="系统没有查询到小班-3产品，";
							}
						}
						
						if( miniHour4.compareTo(new BigDecimal(0))>0){
							miniClass4 = smallClassService.getMiniClassByName(miniClassName4, campusId);
							if(miniClass4 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（4）:"+miniClassName4+"，";
							}else{
								miniProduct4=miniClass4.getProduct();
							}
						}else if(StringUtil.isNotEmpty(miniProductName4)){
							miniProduct4 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName4,new Organization(brenchId));
							if(miniProduct4 == null){
								isError=true;
								errorMes+="系统没有查询到小班-4产品，";
							}
						}
						
						if( miniHour5.compareTo(new BigDecimal(0))>0){
							miniClass5 = smallClassService.getMiniClassByName(miniClassName5, campusId);
							if(miniClass5 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（5）:"+miniClassName5+"，";
							}else{
								miniProduct5=miniClass5.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName5)){
							miniProduct5 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName5,new Organization(brenchId));
							if(miniProduct5 == null){
								isError=true;
								errorMes+="系统没有查询到小班-5产品，";
							}
						}
						
						if( miniHour6.compareTo(new BigDecimal(0))>0){
							miniClass6 = smallClassService.getMiniClassByName(miniClassName6, campusId);
							if(miniClass6 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（6）:"+miniClassName6+"，";
							}else{
								miniProduct6=miniClass6.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName6)){
							miniProduct6 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName6,new Organization(brenchId));
							if(miniProduct6 == null){
								isError=true;
								errorMes+="系统没有查询到小班-6产品，";
							}
						}
						
						if( miniHour7.compareTo(new BigDecimal(0))>0){
							miniClass7 = smallClassService.getMiniClassByName(miniClassName7, campusId);
							if(miniClass7 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（7）:"+miniClassName7+"，";
							}else{
								miniProduct7=miniClass7.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName7)){
							miniProduct7 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName7,new Organization(brenchId));
							if(miniProduct7 == null){
								isError=true;
								errorMes+="系统没有查询到小班-7产品，";
							}
						}
						
						if( miniHour8.compareTo(new BigDecimal(0))>0){
							miniClass8 = smallClassService.getMiniClassByName(miniClassName8, campusId);
							if(miniClass8 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（8）:"+miniClassName8+"，";
							}else{
								miniProduct8=miniClass8.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName8)){
							miniProduct8 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName8,new Organization(brenchId));
							if(miniProduct8 == null){
								isError=true;
								errorMes+="系统没有查询到小班-8产品，";
							}
						}
						
						if( miniHour9.compareTo(new BigDecimal(0))>0){
							miniClass9 = smallClassService.getMiniClassByName(miniClassName9, campusId);
							if(miniClass9 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（9）:"+miniClassName9+"，";
							}else{
								miniProduct9=miniClass9.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName9)){
							miniProduct9 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName9,new Organization(brenchId));
							if(miniProduct9 == null){
								isError=true;
								errorMes+="系统没有查询到小班-9产品，";
							}
						}
						
						if( miniHour10.compareTo(new BigDecimal(0))>0){
							miniClass10 = smallClassService.getMiniClassByName(miniClassName10, campusId);
							if(miniClass10 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（10）:"+miniClassName10+"，";
							}else{
								miniProduct10=miniClass10.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName10)){
							miniProduct10 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName10,new Organization(brenchId));
							if(miniProduct10 == null){
								isError=true;
								errorMes+="系统没有查询到小班-10产品，";
							}
						}
						
						if( miniHour11.compareTo(new BigDecimal(0))>0){
							miniClass11 = smallClassService.getMiniClassByName(miniClassName11, campusId);
							if(miniClass11 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（11）:"+miniClassName11+"，";
							}else{
								miniProduct11=miniClass11.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName11)){
							miniProduct11 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName11,new Organization(brenchId));
							if(miniProduct11 == null){
								isError=true;
								errorMes+="系统没有查询到小班-11产品，";
							}
						}
						
						if( miniHour12.compareTo(new BigDecimal(0))>0){
							miniClass12 = smallClassService.getMiniClassByName(miniClassName12, campusId);
							if(miniClass12 == null){
								isError=true;
								errorMes+="系统没有查询到小班名称（12）:"+miniClassName12+"，";
							}else{
								miniProduct12=miniClass12.getProduct();
							}
	
						}else if(StringUtil.isNotEmpty(miniProductName12)){
							miniProduct12 = productDao.getMiniProductByNameAndGradeId(gradeId, miniProductName12,new Organization(brenchId));
							if(miniProduct12 == null){
								isError=true;
								errorMes+="系统没有查询到小班-12产品，";
							}
						}
						
						String signStaffId=null;
						if(StringUtil.isNotEmpty(signStaffName)){
							signStaffId=userDao.getUserIdByName(signStaffName);
						}
						
						String schoolId =null;
						if(StringUtil.isNotEmpty(schoolName)){
							schoolId=studentSchoolDao.getStudentSchoolIdByName(schoolName);
						}
						
						Map<String,MiniClass> miniMap =new Hashtable<String, MiniClass>();
						if(miniClass1!=null){
							if(miniMap.containsKey(miniClass1.getMiniClassId())){
								isError=true;
								errorMes+="小班名称1重复，";
							}else{
								miniMap.put(miniClass1.getMiniClassId(), miniClass1);
							}
						}
						if(miniClass2!=null){
							if(miniMap.containsKey(miniClass2.getMiniClassId())){
								isError=true;
								errorMes+="小班名称2重复，";
							}else{
								miniMap.put(miniClass2.getMiniClassId(), miniClass2);
							}
						}
						if(miniClass3!=null){
							if(miniMap.containsKey(miniClass3.getMiniClassId())){
								isError=true;
								errorMes+="小班名称3重复，";
							}else{
								miniMap.put(miniClass3.getMiniClassId(), miniClass3);
							}
						}
						if(miniClass4!=null){
							if(miniMap.containsKey(miniClass4.getMiniClassId())){
								isError=true;
								errorMes+="小班名称4重复，";
							}else{
								miniMap.put(miniClass4.getMiniClassId(), miniClass4);
							}
						}
						if(miniClass5!=null){
							if(miniMap.containsKey(miniClass5.getMiniClassId())){
								isError=true;
								errorMes+="小班名称5重复，";
							}else{
								miniMap.put(miniClass5.getMiniClassId(), miniClass5);
							}
						}
						if(miniClass6!=null){
							if(miniMap.containsKey(miniClass6.getMiniClassId())){
								isError=true;
								errorMes+="小班名称6重复，";
							}else{
								miniMap.put(miniClass6.getMiniClassId(), miniClass6);
							}
						}
						if(miniClass7!=null){
							if(miniMap.containsKey(miniClass7.getMiniClassId())){
								isError=true;
								errorMes+="小班名称7重复，";
							}else{
								miniMap.put(miniClass7.getMiniClassId(), miniClass7);
							}
						}
						if(miniClass8!=null){
							if(miniMap.containsKey(miniClass8.getMiniClassId())){
								isError=true;
								errorMes+="小班名称8重复，";
							}else{
								miniMap.put(miniClass8.getMiniClassId(), miniClass8);
							}
						}
						if(miniClass9!=null){
							if(miniMap.containsKey(miniClass9.getMiniClassId())){
								isError=true;
								errorMes+="小班名称9重复，";
							}else{
								miniMap.put(miniClass9.getMiniClassId(), miniClass9);
							}
						}
						if(miniClass10!=null){
							if(miniMap.containsKey(miniClass10.getMiniClassId())){
								isError=true;
								errorMes+="小班名称10重复，";
							}else{
								miniMap.put(miniClass10.getMiniClassId(), miniClass10);
							}
						}
						if(miniClass11!=null){
							if(miniMap.containsKey(miniClass11.getMiniClassId())){
								isError=true;
								errorMes+="小班名称11重复，";
							}else{
								miniMap.put(miniClass11.getMiniClassId(), miniClass11);
							}
						}
						if(miniClass12!=null){
							if(miniMap.containsKey(miniClass12.getMiniClassId())){
								isError=true;
								errorMes+="小班名称12重复，";
							}else{
								miniMap.put(miniClass12.getMiniClassId(), miniClass12);
							}
						}
						
						if(isError){
							map.put("isSave", "失败");
							map.put("errorMes", errorMes);
						}else{
							User user = userService.getCurrentLoginUser();
							
							Student stu=new Student();
							stu.setBlCampusId(campusId);
							stu.setFatherName(fatherName);
							stu.setFatherPhone(fatherPhone);
							stu.setMotherName(motherName);
							stu.setNotherPhone(notherPhone);
							stu.setName(name);
							stu.setSex(""+(sex.equals("男")?1:0));
							if(StringUtil.isNotEmpty(schoolId)){
								stu.setSchool(new StudentSchool(schoolId));
							}
							stu.setAddress(studentAddress);
							if (StringUtil.isNotBlank(studentPhone)) {
							    stu.setContact(studentPhone);
							} else {
							    if(StringUtil.isNotEmpty(fatherPhone)){
							        stu.setContact(fatherPhone);
	                            }else{
	                                stu.setContact(notherPhone);
	                            }
							}
							stu.setGradeDict(new DataDict(gradeId));
							stu.setBothday(bothday);
							stu.setStudyManegerId(studyManegerId);
							stu.setEnrolDate(enrolDate);
							stu.setStatus(StudentStatus.NEW);
							stu.setCreateUserId(user.getUserId());
							stu.setCreateTime(DateTools.getCurrentDateTime());
							stu.setModifyUserId(user.getUserId());
							stu.setModifyTime(DateTools.getCurrentDateTime());
							studentDao.save(stu);
							Customer customer=new Customer();
							//customer.setBlBranch(brenchId);
							customer.setBlSchool(campusId);
							customer.setBlCampusId(StringUtils.isNotBlank(campusId)?organizationDao.findById(campusId):null);
							if(StringUtil.isNotEmpty(fatherName)){
								customer.setName(fatherName);
							}else{
								customer.setName(motherName);
							}
							if(StringUtil.isNotEmpty(fatherPhone)){
								customer.setContact(fatherPhone);
							}else{
								customer.setContact(notherPhone);
							}
							
							customer.setCreateUser(user);
							customer.setCreateTime(DateTools.getCurrentDateTime());
							customer.setModifyUserId(user.getUserId());
							customer.setModifyTime(DateTools.getCurrentDateTime());
							customer.setDealStatus(CustomerDealStatus.SIGNEUP);
                            customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
                            customer.setDeliverTarget(user.getUserId());
                            customer.setDeliverTargetName(user.getName());
							customer.setRecordDate(DateTools.getCurrentDateTime());
							customerDao.save(customer);
							
							Contract contract=new Contract();
							setInitContract(contract,user);
							contract.setStudent(new Student(stu.getId()));
							contract.setCustomer(new Customer(customer.getId()));
							contract.setContractStatus(ContractStatus.NORMAL);
							contract.setPaidStatus(ContractPaidStatus.PAID);
							contract.setContractType(ContractType.INIT_CONTRACT);
							
							if(StringUtil.isNotEmpty(signStaffId)){
								contract.setSignStaff(new User(signStaffId));
							}
							
							contract.setOneOnOneUnitPrice(BigDecimal.valueOf(Double.valueOf(oneOnOneUnitPrice)));//单价（现在）
							contract.setFreeTotalHour(BigDecimal.valueOf(Double.valueOf(freeRemainingHour)));//赠送总课时
							
							setOneOnOneOneJson(productName, contract, oneOnOnePaidAmount, oneOnOneRemainingClass);
							Set<ContractProduct> contractProducts = new HashSet<ContractProduct>(0);
							
							//一对一
							if(BigDecimal.valueOf(Double.valueOf(oneOnOneRemainingClass)).compareTo(new BigDecimal(0))>0){
								ContractProduct cp=new ContractProduct();
								cp.setContract(contract);
								cp.setDealDiscount(new BigDecimal(1));
								
								// 统一单价
								cp.setProduct(oneOnOneProduct);
								cp.setPrice(BigDecimal.valueOf(Double.valueOf(oneOnOneUnitPrice)));
								cp.setQuantity(BigDecimal.valueOf(Double.valueOf(oneOnOneRemainingClass)));
								cp.setConsumeQuanity(BigDecimal.ZERO);
								cp.setPlanAmount(BigDecimal.valueOf(Double.valueOf(oneOnOnePaidAmount)));
								cp.setPaidAmount(BigDecimal.valueOf(Double.valueOf(oneOnOnePaidAmount)));
								cp.setConsumeAmount(BigDecimal.ZERO);
								cp.setType(ProductType.ONE_ON_ONE_COURSE_NORMAL);
								cp.setStatus(ContractProductStatus.NORMAL);
								cp.setPaidStatus(ContractProductPaidStatus.PAID);
								contractProducts.add(cp);
							}
							
							//一对一赠送课时
							if(BigDecimal.valueOf(Double.valueOf(freeRemainingHour)).compareTo(new BigDecimal(0))>0){
								ContractProduct cp=new ContractProduct();
								cp.setContract(contract);
								cp.setDealDiscount(new BigDecimal(1));
								//一对一赠送产品
								cp.setProduct(freeProduct);
								cp.setQuantity(BigDecimal.valueOf(Double.valueOf(freeRemainingHour)));
								cp.setPrice(BigDecimal.ZERO);
								cp.setConsumeQuanity(BigDecimal.ZERO);
								cp.setPlanAmount(BigDecimal.ZERO);
								cp.setPaidAmount(BigDecimal.ZERO);
								cp.setConsumeAmount(BigDecimal.ZERO);
								cp.setType(ProductType.ONE_ON_ONE_COURSE_FREE);
								cp.setStatus(ContractProductStatus.NORMAL);
								cp.setPaidStatus(ContractProductPaidStatus.PAID);
								contractProducts.add(cp);
							}
							ContractProduct cp1 = null;
							ContractProduct cp2 = null;
							ContractProduct cp3 = null;
							ContractProduct cp4 = null;
							ContractProduct cp5 = null;
							ContractProduct cp6 = null;
							ContractProduct cp7 = null;
							ContractProduct cp8 = null;
							ContractProduct cp9 = null;
							ContractProduct cp10 = null;
							ContractProduct cp11 = null;
							ContractProduct cp12 = null;
							//小班
							if(miniHour1.compareTo(new BigDecimal(0))>0){
								cp1=getInitContractProduct(miniProduct1.getPrice(),miniHour1);
								cp1.setContract(contract);
								cp1.setProduct(miniProduct1);
								contractProducts.add(cp1);
							}
							if( miniHour2.compareTo(new BigDecimal(0))>0){
								cp2=getInitContractProduct(miniProduct2.getPrice(),miniHour2);
								cp2.setContract(contract);
								cp2.setProduct(miniProduct2);
								contractProducts.add(cp2);
							}
							if(miniHour3.compareTo(new BigDecimal(0))>0){
								cp3=getInitContractProduct(miniProduct3.getPrice(),miniHour3);
								cp3.setContract(contract);
								cp3.setProduct(miniProduct3);
								contractProducts.add(cp3);
							}
							if( miniHour4.compareTo(new BigDecimal(0))>0){
								cp4=getInitContractProduct(miniProduct4.getPrice(),miniHour4);
								cp4.setContract(contract);
								cp4.setProduct(miniProduct4);
								contractProducts.add(cp4);
							}
							if(miniHour5.compareTo(new BigDecimal(0))>0){
								cp5=getInitContractProduct(miniProduct5.getPrice(),miniHour5);
								cp5.setContract(contract);
								cp5.setProduct(miniProduct5);
								contractProducts.add(cp5);
							}
							if(miniHour6.compareTo(new BigDecimal(0))>0){
								cp6=getInitContractProduct(miniProduct6.getPrice(),miniHour6);
								cp6.setContract(contract);
								cp6.setProduct(miniProduct6);
								contractProducts.add(cp6);
							}
							if(miniHour7.compareTo(new BigDecimal(0))>0){
								cp7=getInitContractProduct(miniProduct7.getPrice(),miniHour7);
								cp7.setContract(contract);
								cp7.setProduct(miniProduct7);
								contractProducts.add(cp7);
							}
							if(miniHour8.compareTo(new BigDecimal(0))>0){
								cp8=getInitContractProduct(miniProduct8.getPrice(),miniHour8);
								cp8.setContract(contract);
								cp8.setProduct(miniProduct8);
								contractProducts.add(cp8);
							}
							if(miniHour9.compareTo(new BigDecimal(0))>0){
								cp9=getInitContractProduct(miniProduct9.getPrice(),miniHour9);
								cp9.setContract(contract);
								cp9.setProduct(miniProduct9);
								contractProducts.add(cp9);
							}
							if(miniHour10.compareTo(new BigDecimal(0))>0){
								cp10=getInitContractProduct(miniProduct10.getPrice(),miniHour10);
								cp10.setContract(contract);
								cp10.setProduct(miniProduct10);
								contractProducts.add(cp10);
							}
							if(miniHour11.compareTo(new BigDecimal(0))>0){
								cp11=getInitContractProduct(miniProduct11.getPrice(),miniHour11);
								cp11.setContract(contract);
								cp11.setProduct(miniProduct11);
								contractProducts.add(cp11);
							}
							if(miniHour12.compareTo(new BigDecimal(0))>0){
								cp12=getInitContractProduct(miniProduct12.getPrice(),miniHour12);
								cp12.setContract(contract);
								cp12.setProduct(miniProduct12);
								contractProducts.add(cp12);
							}
							
							contract.setContractProducts(contractProducts);
							contractService.calContractPlanAmount(contract);
							
							// 为了兼容calContractPlanAmount 方法， 特意增加下面3个值， 另外设置
							contract.setPendingAmount(BigDecimal.ZERO);
							contract.setPaidAmount(contract.getTotalAmount());
							contract.setPaidStatus(ContractPaidStatus.PAID);
							contract.setRemainingAmount(contract.getTotalAmount());
							
							// 下面是处理学生账户资金
							StudnetAccMv studnetAccMv = new StudnetAccMv(stu);
							studnetAccMv.setTotalAmount(contract.getTotalAmount());
							studnetAccMv.setPaidAmount(contract.getPaidAmount());
							studnetAccMv.setRemainingAmount(contract.getRemainingAmount());
							studnetAccMv.setOneOnOnePaidAmount(contract.getOneOnOneTotalAmount());
							if (contract.getOneOnOneUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
								// MathContext mc = new MathContext(5, RoundingMode.HALF_DOWN);
								DecimalFormat nf = new DecimalFormat("0.00");
	//							nf.format(contract.getOneOnOneTotalAmount().doubleValue()/contract.getOneOnOneUnitPrice().doubleValue());
//								studnetAccMv.setOneOnOneRemainingHour(Double.valueOf(nf.format(contract.getOneOnOneTotalAmount().doubleValue()/contract.getOneOnOneUnitPrice().doubleValue())));
								studnetAccMv.setOneOnOneRemainingHour(contract.getOneOnOneTotalAmount().divide(contract.getOneOnOneUnitPrice(), 2));
							} else {
								studnetAccMv.setOneOnOneRemainingHour(BigDecimal.ZERO);
							}
							studnetAccMv.setMiniPaidAmount(contract.getMiniClassTotalAmount());
							studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
							contractDao.save(contract);
							
							
							
							//小班插班
							if(miniHour1.compareTo(new BigDecimal(0))>0 && miniClass1 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass1.getMiniClassId(), contract.getId(), cp1.getId(),"2014-12-01",false);
							}
							if( miniHour2.compareTo(new BigDecimal(0))>0 && miniClass2 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass2.getMiniClassId(), contract.getId(), cp2.getId(),"2014-12-01",false);
							}
							if(miniHour3.compareTo(new BigDecimal(0))>0 && miniClass3 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass3.getMiniClassId(), contract.getId(), cp3.getId(),"2014-12-01",false);
							}
							if( miniHour4.compareTo(new BigDecimal(0))>0 && miniClass4 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass4.getMiniClassId(), contract.getId(), cp4.getId(),"2014-12-01",false);
							}
							if(miniHour5.compareTo(new BigDecimal(0))>0 && miniClass5 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass5.getMiniClassId(), contract.getId(), cp5.getId(),"2014-12-01",false);
							}
							if(miniHour6.compareTo(new BigDecimal(0))>0 && miniClass6 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass6.getMiniClassId(), contract.getId(), cp6.getId(),"2014-12-01",false);
							}
							if(miniHour7.compareTo(new BigDecimal(0))>0 && miniClass7 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass7.getMiniClassId(), contract.getId(), cp7.getId(),"2014-12-01",false);
							}
							if(miniHour8.compareTo(new BigDecimal(0))>0 && miniClass8 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass8.getMiniClassId(), contract.getId(), cp8.getId(),"2014-12-01",false);
							}
							if(miniHour9.compareTo(new BigDecimal(0))>0 && miniClass9 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass9.getMiniClassId(), contract.getId(), cp9.getId(),"2014-12-01",false);
							}
							if(miniHour10.compareTo(new BigDecimal(0))>0 && miniClass10 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass10.getMiniClassId(), contract.getId(), cp10.getId(),"2014-12-01",false);
							}
							if(miniHour11.compareTo(new BigDecimal(0))>0 && miniClass11 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass11.getMiniClassId(), contract.getId(), cp11.getId(),"2014-12-01",false);
							}
							if(miniHour12.compareTo(new BigDecimal(0))>0 && miniClass12 != null){
								smallClassService.AddStudentForMiniClasss(stu.getId(), miniClass12.getMiniClassId(), contract.getId(), cp12.getId(),"2014-12-01",false);
							}
							
							map.put("isSave", "成功");
						}
					}catch (Exception e) {
						map.put("isSave", "失败");
						map.put("errorMes", e.getMessage());
						e.printStackTrace();
						throw new ApplicationException("输入数据有错误");
					}
				}
				mesList.add(map);
			}
		}

		return mesList;
	}
	
	private ContractProduct getInitContractProduct(BigDecimal miniPrice, BigDecimal miniHour){
		ContractProduct cp=new ContractProduct();
		cp.setPrice(miniPrice);
		cp.setQuantity(miniHour);
		BigDecimal amount=miniPrice.multiply(miniHour);
		cp.setPlanAmount(amount);
		cp.setPaidAmount(amount);
		cp.setDealDiscount(new BigDecimal(1));
		cp.setConsumeQuanity(BigDecimal.ZERO);
		cp.setConsumeAmount(BigDecimal.ZERO);
		cp.setType(ProductType.SMALL_CLASS);
		cp.setStatus(ContractProductStatus.STARTED);
		cp.setPaidStatus(ContractProductPaidStatus.PAID);
		return cp;
	}

	private void setOneOnOneOneJson(String productName,Contract contract,String oneOnOnePaidAmount,String oneOnOneRemainingClass) throws Exception{
		List <Map> list=new ArrayList();
		if(oneOnOneRemainingClass!=null && Double.valueOf(oneOnOneRemainingClass)>0){
			Map<String, String> map =new HashMap<String, String>();
			map.put("courseName", productName);
			map.put("quantity", oneOnOneRemainingClass);
			map.put("price", contract.getOneOnOneUnitPrice().toString());
			map.put("subtotal", oneOnOnePaidAmount);
			list.add(map);
		}
		contract.setOneOnOneJson(URLEncoder.encode(objectMapper.writeValueAsString(list),"utf-8"));
	}
	
	private void setInitContract(Contract contract,User user){
		
		contract.setCreateByStaff(user);
		contract.setCreateTime(DateTools.getCurrentDateTime());
		contract.setModifyByStaff(user);
		contract.setModifyTime(DateTools.getCurrentDateTime());
		contract.setTotalAmount(BigDecimal. ZERO);
		contract.setPaidAmount(BigDecimal. ZERO);
		contract.setPendingAmount(BigDecimal. ZERO);
		contract.setRemainingAmount(BigDecimal. ZERO);
		contract.setConsumeAmount(BigDecimal. ZERO);
		contract.setAvailableAmount(BigDecimal.ZERO);
		
		contract.setOneOnOneRemainingClass(BigDecimal. ZERO);
		contract.setOneOnOneTotalAmount(BigDecimal. ZERO);
		contract.setOneOnOneRemainingAmount(BigDecimal. ZERO);
		contract.setOneOnOnePaidAmount(BigDecimal. ZERO);
		contract.setOneOnOneConsumeAmount(BigDecimal. ZERO);
		contract.setOneOnOneUnitPrice(BigDecimal. ZERO);
		
		contract.setMiniClassTotalAmount(BigDecimal. ZERO);
		contract.setMiniClassPaidAmount(BigDecimal. ZERO);
		contract.setMiniClassConsumeAmount(BigDecimal. ZERO);
		contract.setMiniClassRemainingAmount(BigDecimal. ZERO);
		
		contract.setOtherTotalAmount(BigDecimal. ZERO);
		contract.setOtherPaidAmount(BigDecimal. ZERO);
		contract.setOtherConsumeAmount(BigDecimal. ZERO);
		contract.setOtherRemainingAmount(BigDecimal. ZERO);
		
		contract.setFreeConsumeHour(BigDecimal.ZERO);
		contract.setFreeRemainingHour(BigDecimal.ZERO );
		
		
	}
	
	
	private String StringTrim(Object obj) {
		String str="";
		if (obj == null)
			return "";
		else{
			str=obj.toString().trim();
			if(str.length()>3 && str.substring(str.length()-3).equals(".00")){
				str=str.substring(0,str.length()-3);
			}else if(str.length()>2 && str.substring(str.length()-2).equals(".0")){
				str=str.substring(0,str.length()-2);
			}
		}
		return str;	
	}
	
	private String StringTrim(List<Object> listObj,Map<String, Integer> title ,String name) {
		if(title.get(name)!=null && listObj.size()>title.get(name)){
			return StringTrim(listObj.get(title.get(name)));
		}
		return null;	
	}
	
	@Override
	public List<Map<String, String>> saveImportMiniClass(List<List<Object>> list) {

		List<Map<String,String>> mesList = new ArrayList<Map<String,String>>();
		Map<String, Integer> title = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map=new HashMap<String,String>();
			List<Object> listObj = list.get(i);
			if (i < 2)
				continue;
			if (i == 2) {
				for (int j = 0; j < listObj.size(); j++) {
					title.put("" + listObj.get(j), new Integer(j));
				}
			} else {
				if(title.get("上传结果")!=null && "成功".equals(StringTrim(listObj.get(title.get("上传结果"))))){
					map.put("isSave", "old成功");
				}else{
					String brench = StringTrim(listObj,title,"分公司");//
					String campus = StringTrim(listObj,title,"校区");//
					String miniProductName = StringTrim(listObj,title,"小班产品");
					String grade = StringTrim(listObj,title,"年级");//
					String price = StringTrim(listObj,title,"单价");//
					
					String classTimeLength = StringTrim(listObj,title,"总课时数");
					String miniClassName = StringTrim(listObj,title,"小班名称");
					String miniTypeName = StringTrim(listObj,title,"小班类型");
					String subjectName = StringTrim(listObj,title,"科目");
					String courseWeekday = StringTrim(listObj,title,"上课星期");
					
					
					String studyManegerName = StringTrim(listObj,title,"班主任");
					String teacherName = StringTrim(listObj,title,"老师");
					String startDate = StringTrim(listObj,title,"开班日期");
					String classTimeName = StringTrim(listObj,title,"上课时间");
					String classroomName = StringTrim(listObj,title,"教室");
					
					String everyCourseClassNum = StringTrim(listObj,title,"每次课时数");
					String residueClassTime= StringTrim(listObj,title,"小班剩余课时");
					String status =StringTrim(listObj,title,"开班");
					
					String miniProductName1 = StringTrim(listObj,title,"开课后剩余");
					String remark = StringTrim(listObj,title,"备注");
					
					boolean isError =false;
					String errorMes="";
					
					String brenchId=null;
					if(StringUtil.isNotEmpty(brench)){
						brenchId=organizationDao.getOrganizationIdByName(brench);
					}
					if(StringUtil.isEmpty(brenchId)){
						isError=true;
						errorMes+="系统不能查询到分公司名称，";
					}
					String campusId=null;
					if(StringUtil.isNotEmpty(campus)){
						campusId=organizationDao.getOrganizationIdByName(campus);
					}
					if(StringUtil.isEmpty(campusId)){
						isError=true;
						errorMes+="系统不能查询到校区名称，";
					}
					
					Product miniProduct= null;
					if(StringUtil.isNotEmpty(miniProductName) && brenchId != null){
						 miniProduct= productDao.getProductIdByNameAndType(miniProductName, ProductType.SMALL_CLASS, brenchId);
					}
					if(miniProduct == null){
						isError=true;
						errorMes+="系统不能查询到分公司下的小班产品，";
					}
					if(campusId!=null){
						MiniClass mini = smallClassService.getMiniClassByName(miniClassName, campusId);
						if(mini!=null){
							isError=true;
							errorMes+="系统小班已经存在，";
						}
					}
					
					
					String gradeId=null;
					if(StringUtil.isNotEmpty(grade)){
						gradeId=dataDictDao.getDataDictIdByName(grade,DataDictCategory.STUDENT_GRADE);
					}
					if(StringUtil.isEmpty(gradeId)){
						isError=true;
						errorMes+="系统不能查询到年级名称，";
					}
					String subjectId=null;
					if( StringUtil.isNotEmpty(subjectName)){
						subjectId=dataDictDao.getDataDictIdByName(subjectName,DataDictCategory.SUBJECT);
					}
					if(StringUtil.isEmpty(subjectId)){
						isError=true;
						errorMes+="系统不能查询到科目名称，";
					}
					
					String courseWeekdays = null;
					if(StringUtil.isNotEmpty(courseWeekday)){
						String[] weekdays= courseWeekday.split(",");
						String weekdayValue= null;
						for(int j=0;weekdays!=null && j<weekdays.length;j++){
							String weekday=weekdays[j];
							String value=EnumHelper.getEnumIdByName(weekday, "WeekDay");
							if(value == null){
								break;
							}
							if(weekdayValue == null)
								weekdayValue=value;
							else
								weekdayValue+=","+value;
							if(j==(weekdays.length-1))
								courseWeekdays=weekdayValue;
						}
					}
					
					String miniTypeId = null;
					if(StringUtil.isNotEmpty(miniTypeName)){
						miniTypeId=dataDictDao.getDataDictIdByName(miniTypeName,DataDictCategory.MINI_CLASS_TYPE);
					}
					if(StringUtil.isEmpty(miniTypeId)){
						isError=true;
						errorMes+="系统不能小班类型，";
					}
					
					String classTimeId = null;
					if(StringUtil.isNotEmpty(classTimeName)){
						if(classTimeName.length()==11)
							classTimeName=classTimeName.replace("-", " - ");
						classTimeId=dataDictDao.getDataDictIdByName(classTimeName,DataDictCategory.COURSE_TIME);
					}
					if(StringUtil.isEmpty(classTimeId)){
						isError=true;
						errorMes+="系统不能上课时间段，";
					}
					
					String studyManegerId=null;
					if(StringUtil.isNotEmpty(studyManegerName)){
						studyManegerId=userDao.getUserIdByName(studyManegerName);
					}
					if(StringUtil.isEmpty(studyManegerId)){
						isError=true;
						errorMes+="系统不能查询到班主任，";
					}
					
					String teacherId=null;
					if(StringUtil.isNotEmpty(teacherName)){
						teacherId=userDao.getUserIdByName(teacherName);
					}
					if(StringUtil.isNotEmpty(teacherName) && StringUtil.isEmpty(teacherId)){
						isError=true;
						errorMes+="系统不能查询到老师，";
					}
					
					String statusId = null;
					if(StringUtil.isNotEmpty(status)){
						statusId= EnumHelper.getEnumIdByName(status, "MiniClassStatus");
					}
					if(StringUtil.isEmpty(statusId)){
						isError=true;
						errorMes+="系统不能查询到开课，";
					}
					
					if(StringUtil.isEmpty(everyCourseClassNum) || Double.valueOf(everyCourseClassNum)<=0){
						isError=true;
						errorMes+="每次课时数不能为空或者不能小于等于0，";
					}
					
					if(StringUtil.isEmpty(startDate)){
						isError=true;
						errorMes+="开班时间不能为空，";
					}
					
					String classroomId=null;
					if(StringUtil.isNotEmpty(classroomName)){
						classroomId=classroomManageService.getClassroomIdByName(classroomName);
					}
					if(StringUtil.isNotEmpty(classroomName) && StringUtil.isEmpty(classroomId)){
						isError=true;
						errorMes+="系统不能查询到教室，";
					}
					
					if(isError){
						map.put("isSave", "失败");
						map.put("errorMes", errorMes);
					}else{
						try{
						User user = userService.getCurrentLoginUser();
						MiniClass miniClass=new MiniClass();
						miniClass.setBlCampus(new Organization(campusId));
						if(StringUtil.isNotEmpty(classroomId)){
							miniClass.setClassroom(HibernateUtils.voObjectMapping(classroomManageService.findClassroomById(classroomId), ClassroomManage.class));
						}
//						miniClass.setClassTime(new DataDict(classTimeId));
						miniClass.setCourseWeekday(courseWeekdays);
						
						miniClass.setGrade(new DataDict(gradeId));
						miniClass.setMiniClassType(new DataDict(miniTypeId));
						miniClass.setName(miniClassName);
						
						miniClass.setRemark(remark);
						if(StringUtil.isNotEmpty(startDate)){
							startDate =DateTools.addDateToString(startDate.replaceAll("\\.", "-").replaceAll("/", "-"), 0);
							miniClass.setStartDate(startDate);
						}
						
						miniClass.setStudyManeger(new User(studyManegerId));
						miniClass.setSubject(new DataDict(subjectId));
						if(StringUtil.isNotEmpty(teacherId))
							miniClass.setTeacher(new User(teacherId));
						miniClass.setProduct(miniProduct);
						miniClass.setTotalClassHours(miniProduct.getMiniClassTotalhours().doubleValue());
						miniClass.setUnitPrice(miniProduct.getPrice().doubleValue());
						miniClass.setEveryCourseClassNum(Double.valueOf(everyCourseClassNum));
						
						miniClass.setCreateUserId(user.getUserId());
						miniClass.setCreateTime(DateTools.getCurrentDateTime());
						miniClass.setModifyUserId(user.getUserId());
						miniClass.setModifyTime(DateTools.getCurrentDateTime());
						smallClassService.saveOrUpdateMiniClass(miniClass);
						map.put("isSave", "成功");
						}catch (Exception e) {
							map.put("isSave", "失败");
							map.put("errorMes", e.getMessage());
							e.printStackTrace();
							throw new ApplicationException("输入数据有错误");
						}
					}
				}
				mesList.add(map);
			}
		}

		return mesList;
	
	}
	
	
}
