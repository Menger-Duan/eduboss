package com.eduboss.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.eduboss.service.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.boss.rpc.base.dto.MiniClassRpcVo;
import org.boss.rpc.mobile.service.BossCourseRpc;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.common.AuditStatus;
import com.eduboss.common.BaseStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.CourseSummaryStatus;
import com.eduboss.common.EnrollStatus;
import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.common.MiniClassRelationType;
import com.eduboss.common.MiniClassStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.ProductType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.StudentStatus;
import com.eduboss.common.WeekDay;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.CourseConflictDao;
import com.eduboss.dao.CourseModalDao;
import com.eduboss.dao.CourseModalWeekDao;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.MiniClassCourseDao;
import com.eduboss.dao.MiniClassDao;
import com.eduboss.dao.MiniClassProductDao;
import com.eduboss.dao.MiniClassRelationDao;
import com.eduboss.dao.MiniClassStudentAttendentDao;
import com.eduboss.dao.MiniClassStudentDao;
import com.eduboss.dao.ProductChooseViewDao;
import com.eduboss.dao.ProductDao;
import com.eduboss.dao.PromiseClassSubjectDao;
import com.eduboss.dao.SmallClassDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.ClassroomManage;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.CourseConflict;
import com.eduboss.domain.CourseModal;
import com.eduboss.domain.CourseModalWeek;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassProduct;
import com.eduboss.domain.MiniClassRelation;
import com.eduboss.domain.MiniClassStudent;
import com.eduboss.domain.MiniClassStudentAttendent;
import com.eduboss.domain.MiniClassStudentId;
import com.eduboss.domain.MoneyWashRecords;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Product;
import com.eduboss.domain.PromiseClassSubject;
import com.eduboss.domain.Role;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainJdbc.MiniClassCourseJdbc;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.domainVo.AttentanceInfoVo;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.domainVo.ContinueExtendMiniClassVo;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.CourseModalVo;
import com.eduboss.domainVo.HasClassCourseVo;
import com.eduboss.domainVo.MiniClassCourseConsumeVo;
import com.eduboss.domainVo.MiniClassCourseStudentRosterVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.MiniClassMaxMinDateVo;
import com.eduboss.domainVo.MiniClassProductVo;
import com.eduboss.domainVo.MiniClassStudentAttendentVo;
import com.eduboss.domainVo.MiniClassStudentVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.EduPlatform.UpdateAttentanceInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MiniClassEditModalVo;
import com.eduboss.dto.MiniClassModalVo;
import com.eduboss.dto.MiniClassProductSearchVo;
import com.eduboss.dto.MiniClassRelationSearchVo;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.task.SendJoinMiniCourseMsgThread;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.HttpClientUtil;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.MessageQueueUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;


@Service("com.eduboss.service.SmallClassService")
public class SmallClassServiceImpl implements SmallClassService {
	
	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	
	@Autowired
	private ChargeService chargeService;

	@Autowired
	private SmallClassDao smallClassDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private MiniClassStudentDao miniClassStudentDao;
	
	@Autowired
	private MiniClassCourseDao miniClassCourseDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictService dataDictService;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private MiniClassStudentAttendentDao miniClassStudentAttendentDao;

	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private AccountChargeRecordsDao accountChargeRecordsDao;
	
	@Autowired
	private MiniClassDao miniClassDao;

	@Autowired
	private CourseConflictDao courseConflictDao;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ClassroomManageService classroomManageService;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private MiniClassRelationDao miniClassRelationDao;
	
	@Autowired
	private ProductChooseViewDao productChooseViewDao;
	
	@Autowired
	private MiniClassProductDao miniClassProductDao;
	
	@Autowired
	private MiniClassStudentDao miniclassStudentDao;
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MoneyWashRecordsService moneyWashRecordsService;

	@Autowired
	private OdsMonthIncomeCampusService odsMonthIncomeCampusService;
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private PromiseClassSubjectDao promiseClassSubjectDao;

	@Autowired
	private MiniClassInventoryService miniClassInventoryService;

	@Autowired
	private CourseModalDao courseModalDao;

	@Autowired
	private CourseModalService courseModalService;

	@Resource(name = "bossCourseRpc")
    private BossCourseRpc bossCourseRpc;

	@Autowired
	private PromiseClassService promiseClassService;

	@Autowired
	private CourseModalWeekDao courseModalWeekDao;


	private Logger log = Logger.getLogger(SmallClassServiceImpl.class);

	@Override
	public void operationMiniClassRecord(String oper, MiniClass miniClass, String productIdArray) throws Exception {
	    boolean isChangedProduct = false; // 是否修改了资料费与小班关系
		if ("del".equalsIgnoreCase(oper)) {
			deleteMiniClass(miniClass);
		} else {
			String miniClassId = miniClass.getMiniClassId();

			String productId =smallClassDao.findProductIdByMiniClassId(miniClassId);
			if(miniClass.getCourseModaleId()!=null && miniClass.getCourseModaleId()>0 && miniClass.getStudyManeger()==null){
				throw new ApplicationException("请先选择小班班主任");
			}

			Integer textBookId_New = miniClass.getTextBookId();
			Integer textBookId_Old = -1;
			
			if(miniClassId!=null){
				String sql = "select TEXTBOOK_ID, course_start_time, coures_end_time from mini_class where MINI_CLASS_ID = :miniClassId ";
				Map<String, Object> param = Maps.newHashMap();
				param.put("miniClassId", miniClassId);
				List<Map<Object, Object>> list =miniClassDao.findMapBySql(sql, param);
				if(list!=null && list.size()>0){
				    Map<Object, Object> map = list.get(0);
				    textBookId_Old = map.get("TEXTBOOK_ID") != null ? (Integer) map.get("TEXTBOOK_ID") : -1;
				    String courseStartTime = map.get("course_start_time") != null ? (String) map.get("course_start_time") : null;
				    miniClass.setCourseStartTime(courseStartTime);
				    String courseEndTime = map.get("coures_end_time") != null ? (String) map.get("coures_end_time") : null;
                    miniClass.setCourseEndTime(courseEndTime);
				}
			}
			if(miniClass.getTeacher()!=null && miniClass.getTeacher().getUserId().equals("0")){
				miniClass.setTeacher(null);
			}
			
            Boolean flag = false;
            if(StringUtils.isNotEmpty(miniClass.getMiniClassId())){
                flag=getModifyCourseModal(miniClass);
            }

            smallClassDao.save(miniClass);
            //修改今天之后的小班课程
            if(!flag){
                miniClassCourseDao.updateMiniClassCourse(miniClass);
            }

            smallClassDao.flush();


			saveOrUpdateMiniClass(miniClass);

			//同步教材数据给教学平台  xiaojinwang 20170823
            if(miniClassId!=null){
            	//修改
            	if(textBookId_New!=null && textBookId_New!=textBookId_Old){
            		//传数据过去
                	final Integer groupCode = PropertiesUtils.getIntValue("groupCode");
         	 	 	final String url = PropertiesUtils.getStringValue("textBookBindingDataUrl");
         	 	 	//?wareId=12&courseId=a1&type=SMALL_CLASS&groupCode=1
         	 	 	Map<String, String> params = Maps.newConcurrentMap();
         	 	 	params.put("wareId",Integer.toString(textBookId_New));
         	 	 	params.put("classId",miniClassId);
         	 	 	params.put("type",ProductType.SMALL_CLASS.getValue());
         	 	 	params.put("groupCode",Integer.toString(groupCode));
         	 	 	updateTextBookBindingData(url,params);

            	}
            }else{
            	miniClassId = miniClass.getMiniClassId();
            	//传数据过去
				if (textBookId_New != null && textBookId_New != textBookId_Old) {
					final Integer groupCode = PropertiesUtils.getIntValue("groupCode");
					final String url = PropertiesUtils.getStringValue("textBookBindingDataUrl");
					// ?wareId=12&classId=a1&type=SMALL_CLASS&groupCode=1
					Map<String, String> params = Maps.newConcurrentMap();
					params.put("wareId", Integer.toString(textBookId_New));
					params.put("classId", miniClassId);
					params.put("type", ProductType.SMALL_CLASS.getValue());
					params.put("groupCode", Integer.toString(groupCode));
					updateTextBookBindingData(url, params);
				}

            }

			miniClassCourseDao.saveClassroom4itsClass(miniClass);
			// 小班关联多产品逻辑
			String userId = userService.getCurrentLoginUser().getUserId();
			if(null != miniClass.getProduct() && !Objects.equals(miniClass.getProduct().getId(),productId)){
				Product oldProduct = productDao.findById(productId);
				Product newProduct = productDao.findById(miniClass.getProduct().getId());
				/*
				* 修改关联产品总课时不一致，清理所有排课信息。
				* */
				if(oldProduct!=null && newProduct !=null && !Objects.equals(oldProduct.getMiniClassTotalhours(),newProduct.getMiniClassTotalhours())){
				    isChangedProduct = true;
                    clearAllClassCourse(miniClassId);
				}

				miniClassProductDao.saveMiniClassProduct(miniClass.getProduct(), miniClass, userId, "1");
				
			}

            if(flag){
                saveNewCourseModal(miniClass);
            }
            
			if (StringUtil.isNotBlank(productIdArray)) {
				this.operationMiniClassProduct(productIdArray, miniClass.getMiniClassId());
			}
		}
		if (isChangedProduct) { // 修改小班管理产品，推送消息中心miniClassId
		    MessageQueueUtils.postToMq(MessageQueueUtils.TopicCode.MINI_CLASS_OTHER_PRODUCT_SYNC, miniClass.getMiniClassId());
		}
	}
	
	private void updateTextBookBindingData(String url, Map<String, String> params) {
		if (StringUtils.isNotBlank(url)) {
			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						long timeBegin = System.currentTimeMillis();
						String response = HttpClientUtil.doGet(url, params);
						long timeEnd = System.currentTimeMillis();
						log.info("attentanceInfo-costtime:" + (timeEnd - timeBegin) + "ms");
						JSONObject object = JSON.parseObject(response);
						if (object != null) {
							if (!object.get("code").toString().equals("200")) {
								// 同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
								JedisUtil.lpush("textBookBindingData".getBytes(), JedisUtil.ObjectToByte(params));
							}
						}

					} catch (RuntimeException e) {
						// 异常,同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
						JedisUtil.lpush("textBookBindingData".getBytes(), JedisUtil.ObjectToByte(params));
					}
				}
			});
		}
	}

	/**
	 * 操作小班管理产品记录
	 */
	@Override
	public void operationMiniClassProduct(String productIdArray, String miniClassId)
			throws Exception {
		miniClassProductDao.delMiniClassProduct(miniClassId, "0");
		if (StringUtil.isNotBlank(productIdArray)) {
			String[] idArray = productIdArray.split(",");
			for (String id : idArray) {
				this.addOneMiniClassProduct(id, miniClassId);
			}
		}
	}

	/**
	 * 增加单条小班管理产品记录
	 */
	@Override
	public Response addOneMiniClassProduct(String productId, String miniClassId)
			throws Exception {
		MiniClass miniClass = smallClassDao.findById(miniClassId);
		String userId = userService.getCurrentLoginUserId();
		MiniClassProduct mp = miniClassProductDao.findMiniClassProductByMiniClassAndProduct(miniClassId, productId);
		if (null == mp) {
			miniClassProductDao.saveMiniClassProduct(new Product(productId), miniClass, userId, "0");
		} else {
			return new Response(-1, "已存在该小班产品");
		}
		return new Response();
	}
	
	/**
	 * 获取小班管理产品记录
	 */
	@Override
	public List<MiniClassProductVo> getMiniClassProductList(String miniClassId) throws Exception {
		List<MiniClassProduct> list =  miniClassProductDao.findByCriteria(Expression.eq("miniClass.id", miniClassId));
		List<MiniClassProductVo> voList=new ArrayList<MiniClassProductVo>();
		for(MiniClassProduct miniProduct:list){
			MiniClassProductVo vo=new MiniClassProductVo();
			vo=HibernateUtils.voObjectMapping(miniProduct, MiniClassProductVo.class);
			if(miniProduct.getProduct().getClassType()!=null){
				vo.setClassType(miniProduct.getProduct().getClassType().getValue());
			}
			if(miniProduct.getProduct().getProductVersion()!=null){
				vo.setProductVersion(miniProduct.getProduct().getProductVersion().getValue());
			}
			if(miniProduct.getProduct().getProductQuarter()!=null){
				vo.setProductQuarter(miniProduct.getProduct().getProductQuarter().getValue());
			}
			if(miniProduct.getProduct().getGradeDict()!=null){
				vo.setProductGrade(miniProduct.getProduct().getGradeDict().getValue());
			}

			/**
			 * 是否主产品
			 */
			if (miniProduct.getIsMainProduct().equals("1")){
				/**
				 * 检查是否有已经扣费或者取消的课程
				 */
				if(checkHasChargeOrCancelCourse(miniClassId)){
					vo.setFlagOfChargeCourse("existCharge");
				}else {
					vo.setFlagOfChargeCourse("notExistCharge");
				}
			}

			voList.add(vo);						
		}
		return voList;
	}

	/**
	 *
	 * @param miniClassId
	 * @return
	 */
	private boolean checkHasChargeOrCancelCourse(String miniClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("miniClassId", miniClassId);
		int countHql =miniClassDao.findCountSql(" SELECT count(1) FROM mini_class WHERE MINI_CLASS_ID = :miniClassId AND STATUS in ('"+MiniClassStatus.CONPELETE+"' , '"+MiniClassStatus.STARTED+"' )  ", params);
		if (countHql>0){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 获取符合产品，小班的报名学生
	 */
	@Override
	public List<MiniClassStudentVo> getStudentsByMiniClassProduct(String miniClassId, String productId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from  MiniClassStudent  where 1=1");
		if(StringUtils.isNotBlank(miniClassId)){
			hql.append(" and  miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		if(StringUtils.isNotBlank(productId)){
			hql.append(" and  contractProduct.product.id = :productId ");
			params.put("productId", productId);
		}
		
		List<MiniClassStudentVo> miList = HibernateUtils.voListMapping(miniClassStudentDao.findAllByHQL(hql.toString(), params), MiniClassStudentVo.class);
		Product product = productDao.findById(productId);
		for (MiniClassStudentVo vo : miList) {
			vo.setProductName(product.getName());
		}
		return miList;
	}
	
	/**
	 * 删除小班产品关联记录
	 */
	@Override
	public void deleteMiniClassProduct(String miniClassId, String productId)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" delete MiniClassProduct  where 1=1 ");
		hql.append(" and  miniClass.miniClassId = :miniClassId ");
		params.put("miniClassId", miniClassId);
		hql.append(" and  product.id = :productId ");
		params.put("productId", productId);
		miniClassProductDao.excuteHql(hql.toString(), params);
	}

	@Override
	public void updateMiniClassRecruitStatus(MiniClass miniClass) {
		miniClass= miniClassDao.findById(miniClass.getMiniClassId());
		List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
		StudentCriterionList.add(Restrictions.eq("miniClass.miniClassId", miniClass.getMiniClassId()));
		int count=miniClassStudentDao.findCountByCriteria(StudentCriterionList);

		if(count==miniClass.getPeopleQuantity()+miniClass.getAllowedExcess()){
			miniClass.setRecruitStudentStatus(EnrollStatus.END);
		}else if(count<miniClass.getPeopleQuantity()+miniClass.getAllowedExcess()){
			miniClass.setRecruitStudentStatus(EnrollStatus.ING);
		}

	}

	@Override
	public DataPackage getMiniClassList(MiniClass miniClass,DataPackage dp,String miniClassTypeId, String branchLevel, String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from MiniClass m ";
		String hqlWhere="";
		if(miniClass.getProduct() != null){
			if(StringUtils.isNotEmpty(miniClass.getProduct().getId())) {
				hqlWhere+=" and product.id = :productId ";
				params.put("productId", miniClass.getProduct().getId());
			}
			if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
				hqlWhere+=" and product.productQuarter.id = :productQuarterId ";
				params.put("productQuarterId", miniClass.getProduct().getProductQuarter().getId());
			}
			if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
				hqlWhere+=" and product.productVersion.id = :productVersionId ";
				params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
			}
			if(miniClass.getProduct().getClassType() != null && StringUtils.isNotEmpty(miniClass.getProduct().getClassType().getId())) {
				hqlWhere+=" and product.classType.id = :getClassTypeId ";
				params.put("getClassTypeId", miniClass.getProduct().getClassType().getId());
			}
			
			if(miniClass.getProduct().getCourseSeries() != null && StringUtils.isNotEmpty(miniClass.getProduct().getCourseSeries().getId())) {
				hqlWhere+=" and product.courseSeries.id = :courseSeriesId ";
				params.put("courseSeriesId", miniClass.getProduct().getCourseSeries().getId());
			}

			if(StringUtils.isNotBlank(miniClassTypeId)){
				String[] miniClassTypes=miniClassTypeId.split(",");
				if(miniClassTypes.length>0){
					hqlWhere+=" and (product.classType.id = :miniClassType0 ";
					params.put("miniClassType0", miniClassTypes[0]);
					for (int i = 1; i < miniClassTypes.length; i++) {
						hqlWhere+=" or product.classType.id = :miniClassType" + i;
						params.put("miniClassType" + i, miniClassTypes[i]);
					}
					hqlWhere+=" )";
				}
			}
		}

		if (StringUtils.isNotEmpty(miniClass.getMiniClassId())){
			hqlWhere+=" and miniClassId = :miniClassId ";
			params.put("miniClassId", miniClass.getMiniClassId());
		}

		if(StringUtils.isNotEmpty(miniClass.getStartDate())){
			hqlWhere+=" and startDate >= :startDate ";
			params.put("startDate", miniClass.getStartDate());
		}
		if(StringUtils.isNotEmpty(miniClass.getEndDate())){
			hqlWhere+=" and startDate <= :endDate ";
			params.put("endDate", miniClass.getEndDate());
		}
		
		if(StringUtils.isNotEmpty(miniClass.getName())){
			hqlWhere+=" and name like :miniClassName ";
			params.put("miniClassName", "%" + miniClass.getName() + "%");
		}
		
		if(miniClass.getMiniClassType()!=null && StringUtils.isNotEmpty(miniClass.getMiniClassType().getId())){
			hqlWhere+=" and miniClassType.id = :miniClassTypeId ";
			params.put("miniClassTypeId", miniClass.getMiniClassType().getId());
		}
		
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			hqlWhere+=" and grade.id = :gradeId ";
			params.put("gradeId", miniClass.getGrade().getId());
		}
		
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			hqlWhere+=" and subject.id = :subjectId ";
			params.put("subjectId", miniClass.getSubject().getId());
		}
		
		if(miniClass.getPhase()!=null && StringUtils.isNotEmpty(miniClass.getPhase().getId())){
			hqlWhere+=" and phase.id = :phaseId ";
			params.put("phaseId", miniClass.getPhase().getId());
		}
		
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())
			&& !"0".equals(miniClass.getTeacher().getUserId())){
			hqlWhere+=" and teacher.userId = :teacherId ";
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}else if(miniClass.getTeacher()!=null&&StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())&&"0".equals(miniClass.getTeacher().getUserId())){
			hqlWhere+=" and teacher.userId is null ";
		}
		
		if(miniClass.getStudyManeger()!=null && StringUtils.isNotEmpty(miniClass.getStudyManeger().getUserId())){
			hqlWhere+=" and studyManeger.userId = :studyManegerId ";
			params.put("studyManegerId", miniClass.getStudyManeger().getUserId());
		}
		
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			hqlWhere+=" and m.blCampus.id = :blCampusId ";
			params.put("blCampusId", miniClass.getBlCampus().getId());

			/**
			 * #1826
			 * 如果一个用户分配了“分公司建班人”的角色，
			 * 那么这个用户的数据权限范围无论是在“分公司”还是在“校区”，
			 * 都可以在小班管理界面建立用户所在分公司所有校区的小班，
			 * 并且可以查看其所在分公司的所有校区的小班；
			 */
			List<Role> roles = userService.getCurrentLoginUserRoles();
			for (Role role : roles){
				if ("分公司建班人".equals(role.getName())){
					branchLevel = "group";
				}
			}

		}
		
		if(miniClass.getStatus() != null && StringUtils.isNotEmpty(miniClass.getStatus().getValue())) {
			hqlWhere+=" and m.status = :status ";
			params.put("status", miniClass.getStatus());
		}
		
		//是否绑定教材 20170614 xiaojinwang
		if(miniClass.getBindTextBook()!=null){
			if(miniClass.getBindTextBook()){
				hqlWhere+=" and m.textBookId is not null ";
			}else {
				hqlWhere+=" and m.textBookId is null ";
			}			
		}

		if(StringUtils.isNotBlank(miniClass.getSaleType())){
			if("1".equals(miniClass.getSaleType())){
				hqlWhere+=" and m.onlineSale =0";
			}else if("2".equals(miniClass.getSaleType())){
				hqlWhere+=" and m.campusSale =0";
			}
		}


		if(!"group".equals(branchLevel)){
			//归属组织架构
			Organization campus = userService.getBelongCampus();
			if ("branchLevel".equals(branchLevel)){
				Organization branch = userService.getBelongBranch();
				campus= branch;
			}

			//归属组织架构
			if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
				String orgLevel = campus.getOrgLevel();
				hqlWhere+=" and m.blCampus.id in (select id from Organization where orgLevel like '"+orgLevel+"%')";
			}
		}
		if(!"".equals(hqlWhere)){
			hqlWhere="where "+hqlWhere.substring(4);
		}
		hql+=hqlWhere;
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by m."+dp.getSidx()+" "+dp.getSord();
		} 
		
		dp=smallClassDao.findPageByHQL(hql, dp, true, params);
		
		Date current_date = new Date();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(current_date);
		
		String firstSchoolTime=null;
		if(studentId != null && StringUtils.isNotBlank(studentId)){
			Map<String, Object> stuParams = new HashMap<String, Object>();
			String stuSql="select * from mini_class_student where STUDENT_ID = :studentId and MINI_CLASS_ID = :miniClassId ";
			stuParams.put("studentId", studentId);
			stuParams.put("miniClassId", miniClass.getMiniClassId());
			List<MiniClassStudent> stu=miniclassStudentDao.findBySql(stuSql, stuParams);
			if(stu!=null && stu.size() > 0){
				firstSchoolTime=stu.get(0).getFirstSchoolTime();
			}
		}
		
		List<MiniClass> list=(List<MiniClass>) dp.getDatas();
		List<MiniClassVo> voList= new ArrayList<MiniClassVo>();
		for(MiniClass mini : list){
			MiniClassVo vo=HibernateUtils.voObjectMapping(mini, MiniClassVo.class);
			List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
			StudentCriterionList.add(Expression.eq("miniClass.miniClassId", mini.getMiniClassId()));
			vo.setNextMiniClassTime(miniClassCourseDao.getTop1MiniClassCourseByDate(date,mini.getMiniClassId()));
			vo.setMiniClassPeopleNum(miniClassStudentDao.findCountByCriteria(StudentCriterionList));
			if(firstSchoolTime != null ){
				vo.setStuFirstCourseDate(firstSchoolTime);
			}
			if(vo.getTextBookId()!=null){
				vo.setBindTextBook(true);
			}else{
				vo.setBindTextBook(false);
			}
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public int getMiniClassRealMember(String miniClassId){
		List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
		StudentCriterionList.add(Expression.eq("miniClass.miniClassId", miniClassId));
		return miniClassStudentDao.findCountByCriteria(StudentCriterionList);
	}
	/*@Override
	public DataPackage getMiniClassList(MiniClass miniClass, DataPackage dp, String miniClassTypeId, String branchLevel, String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " SELECT mc.*, mc.PRODUCE_ID PRODUCT_ID, p.NAME PRODUCT_NAME, mc.GRADE GRADE_ID, mc.SUBJECT SUBJECT_ID, mc.PHASE PHASE_ID FROM mini_class mc , product p ";
		String sqlWhere = " WHERE 1=1 AND mc.PRODUCE_ID = p.ID ";
		if(miniClass.getProduct() != null){
//			sql += " , product p";
//			sqlWhere += " AND mc.PRODUCE_ID = p.ID ";
			if(StringUtils.isNotEmpty(miniClass.getProduct().getId())) {
				sqlWhere+=" AND p.ID = '" + miniClass.getProduct().getId() + "'";
			}
			if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
				sqlWhere+=" AND p.PRODUCT_QUARTER_ID = '" + miniClass.getProduct().getProductQuarter().getId() + "'";
			}
			if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
				sqlWhere+=" AND p.PRODUCT_VERSION_ID = '" + miniClass.getProduct().getProductVersion().getId() + "'";
			}
			if(miniClass.getProduct().getClassType() != null && StringUtils.isNotEmpty(miniClass.getProduct().getClassType().getId())) {
				sqlWhere+=" AND p.CLASS_TYPE_ID = '"+miniClass.getProduct().getClassType().getId()+"'";
			}
			if(miniClass.getProduct().getCourseSeries() != null && StringUtils.isNotEmpty(miniClass.getProduct().getCourseSeries().getId())) {
				sqlWhere+=" AND P.COURSE_SERIES_ID = '"+miniClass.getProduct().getCourseSeries().getId()+"'";
			}
			if(StringUtils.isNotBlank(miniClassTypeId)){
				String[] miniClassTypes=miniClassTypeId.split(",");
				if(miniClassTypes.length>0){
					sqlWhere+=" and (p.CLASS_TYPE_ID ='"+miniClassTypes[0]+"' ";
					for (int i = 1; i < miniClassTypes.length; i++) {
						sqlWhere+=" or p.CLASS_TYPE_ID ='"+miniClassTypes[i]+"' ";
					}
					sqlWhere+=" )";
				}
			}
		}

		if (StringUtils.isNotEmpty(miniClass.getMiniClassId())){
			sqlWhere+=" AND mc.MINI_CLASS_ID = '"+miniClass.getMiniClassId()+"' ";
		}
		if(StringUtils.isNotEmpty(miniClass.getStartDate())){
			sqlWhere+=" AND mc.START_DATE >= '"+miniClass.getStartDate()+"'";
		}
		if(StringUtils.isNotEmpty(miniClass.getEndDate())){
			sqlWhere+=" AND mc.START_DATE<= '"+miniClass.getEndDate()+"'";
		}
		if(StringUtils.isNotEmpty(miniClass.getName())){
			sqlWhere+=" AND mc.`NAME`  LIKE '%"+miniClass.getName()+"%'";
		}
		if(miniClass.getMiniClassType()!=null && StringUtils.isNotEmpty(miniClass.getMiniClassType().getId())){
			sqlWhere+=" AND mc.MINI_CLASS_TYPE = '"+miniClass.getMiniClassType().getId()+"'";
		}
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			sqlWhere+=" AND mc.GRADE = '"+miniClass.getGrade().getId()+"'";
		}
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			sqlWhere+=" AND mc.SUBJECT = '"+miniClass.getSubject().getId()+"'";
		}
		
		if(miniClass.getPhase()!=null && StringUtils.isNotEmpty(miniClass.getPhase().getId())){
			sqlWhere+=" AND mc.PHASE = '"+miniClass.getPhase().getId()+"'";
		}
		
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			sqlWhere+=" AND mc.TEACHER_ID = '"+miniClass.getTeacher().getUserId()+"'";
		}
		
		if(miniClass.getStudyManeger()!=null && StringUtils.isNotEmpty(miniClass.getStudyManeger().getUserId())){
			sqlWhere+=" AND mc.STUDY_MANEGER_ID = '"+miniClass.getStudyManeger().getUserId()+"'";
		}
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			sqlWhere+=" AND mc.BL_CAMPUS_ID = '"+miniClass.getBlCampus().getId()+"'";
		}
		if(miniClass.getStatus() != null && StringUtils.isNotEmpty(miniClass.getStatus().getValue())) {
			sqlWhere+=" AND mc.status = '"+miniClass.getStatus().getValue()+"'";
		}
		//归属组织架构
		Organization campus = userService.getBelongCampus();
		if ("branchLevel".equals(branchLevel)){
			Organization branch = userService.getBelongBranch();
		    campus= branch;
		}

		if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
			String orgLevel = campus.getOrgLevel();
			sqlWhere+=" AND mc.BL_CAMPUS_ID in (SELECT ID from organization WHERE orgLevel like '"+orgLevel+"%')";
		}
		sql += sqlWhere;
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql+=" ORDER BY mc."+dp.getSidx()+" "+dp.getSord();
		} 
		dp=jdbcTemplateDao.queryPage(sql, MiniClassVo.class, dp,  true);
		Date current_date = new Date();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(current_date);
		
		String firstSchoolTime=null;
		if(studentId != null && StringUtils.isNotBlank(studentId)){
			String stuSql="select * from mini_class_student where STUDENT_ID='"+studentId+"' and MINI_CLASS_ID = '"+miniClass.getMiniClassId()+"' ";
			List<MiniClassStudent> stu=miniclassStudentDao.findBySql(stuSql);
			if(stu!=null && stu.size() > 0){
				firstSchoolTime=stu.get(0).getFirstSchoolTime();
			}
		}
		List<MiniClassVo> list=(List<MiniClassVo>) dp.getDatas();
		for(MiniClassVo miniVo : list){
			if (StringUtil.isNotBlank(miniVo.getGradeId())) {
				DataDict gradeDict = dataDictDao.findById(miniVo.getGradeId());
				miniVo.setGradeName(gradeDict.getName());
			}
			if (StringUtil.isNotBlank(miniVo.getSubjectId())) {
				DataDict subjectDict = dataDictDao.findById(miniVo.getSubjectId());
				miniVo.setSubjectName(subjectDict.getName());
			}
			if (StringUtil.isNotBlank(miniVo.getPhaseId())) {
				DataDict phaseDict = dataDictDao.findById(miniVo.getPhaseId());
				miniVo.setPhaseName(phaseDict.getName());
			}
			if (StringUtil.isNotBlank(miniVo.getTeacherId())) {
				User user = userDao.findById(miniVo.getTeacherId());
				miniVo.setTeacherName(user.getName());
			}
			if (StringUtil.isNotBlank(miniVo.getStudyManegerId())) {
				User user = userDao.findById(miniVo.getStudyManegerId());
				miniVo.setStudyManegerName(user.getName());
			}
			if(firstSchoolTime != null ){
				miniVo.setStuFirstCourseDate(firstSchoolTime);
			}
			miniVo.setNextMiniClassTime(miniClassCourseDao.getTop1MiniClassCourseByDateJdbc(date,miniVo.getMiniClassId()));
			miniVo.setMiniClassPeopleNum(miniClassStudentDao.countMiniClassPeopleNum(miniVo.getMiniClassId()));
		}
		return dp;
	}*/

	/**
	 * 找小班by id
	 * @param id
	 * @return
	 */
	@Override
    public MiniClassVo findMiniClassById(String id) {
        MiniClass mini= smallClassDao.findById(id);
        log.info("----" + id);
        MiniClassVo miniClassVo = null;
        if (mini != null) {
            miniClassVo = HibernateUtils.voObjectMapping(mini, MiniClassVo.class);
            List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
            StudentCriterionList.add(Expression.eq("miniClass.miniClassId", mini.getMiniClassId()));
            int miniClassPeopleNum = miniClassStudentDao.findCountByCriteria(StudentCriterionList);
            miniClassVo.setMiniClassPeopleNum(miniClassPeopleNum);
            miniClassVo.setAlreadyTotalClassHours(miniClassCourseDao.findCountClassHours(mini.getMiniClassId()));
            int totalNumber = 0;
            if (null != mini.getMiniClassType()) {
                String miniClassTypeName = mini.getMiniClassType().getName();
                String regex = "\\d*";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(miniClassTypeName);
                while (m.find()) {
                    if (!"".equals(m.group())) {
                        totalNumber = Integer.parseInt(m.group());
                    }
                }
            } else {
                int peopleQuantity = mini.getPeopleQuantity();
                if (peopleQuantity > 0) {
                    totalNumber = peopleQuantity;
                }
            }
            int continueNum = this.countMiniClassRelation(miniClassVo.getMiniClassId(), MiniClassRelationType.CONTINUE);
            int extendNum = this.countMiniClassRelation(miniClassVo.getMiniClassId(), MiniClassRelationType.EXTEND);
            String continueRate = "0%";
            String extendRate = "0%";
            if (miniClassPeopleNum > 0) {
                continueRate = getPercent(continueNum, miniClassPeopleNum);
                extendRate = getPercent(extendNum, miniClassPeopleNum);
            }
            String recruitStudentRate = getPercent(miniClassPeopleNum, totalNumber);
            String continueRateStr = "" + continueRate + "(" + continueNum + "/" + miniClassPeopleNum +")";
            String extendRateStr = "" + extendRate + "(" + extendNum + "/" + miniClassPeopleNum +")";
            String recruitStudentRateStr = "" + recruitStudentRate + "(" + miniClassPeopleNum + "/" + totalNumber +")";
            miniClassVo.setContinueClassRate(continueRateStr);
            miniClassVo.setExtendSubjectRate(extendRateStr);
            miniClassVo.setRecruitStudentRate(recruitStudentRateStr);
            
        }
        return miniClassVo;
    }
	
	private String getPercent(int y, int z) {
        String baifenbi = "";// 接受百分比的值
        double baiy = y * 1.0;
        double baiz = z * 1.0;
        double fen = baiy / baiz;
        DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
        baifenbi = df1.format(fen);
        System.out.println(baifenbi);
        return baifenbi;
    }
	

	@Override
	public void deleteMiniClass(MiniClass miniClass) throws Exception {
		
		if (StringUtils.isBlank(miniClass.getMiniClassId())) {
			throw new ApplicationException("要删除的小班ID为空！");
		}
		List<AccountChargeRecords> list = accountChargeRecordsDao.getAccountChargeRecordsByOtmClass(miniClass.getMiniClassId());
		
		if(list.size()>0){
			throw new ApplicationException("已有扣费或者冲销扣费的记录，不能删除该小班！");
		}
		
		// 删除小班的课程
		miniClassCourseDao.deleteMiniClassCourse(miniClass.getMiniClassId());
		this.checkMiniClassOnLineSale(miniClass.getMiniClassId());
//		if(miniClass.getOnlineSale()==0 && miniClass.getOnShelves()==0){ // 删除小班，系统下架
//		    bossCourseRpc.updateCourseStatusByMiniClassIds(miniClass.getMiniClassId(), "SYSTEM_OFF");
//        }
        // 删除小班记录
        smallClassDao.delete(miniClass);
		String miniClassId = miniClass.getMiniClassId();
		MiniClassRelationSearchVo vo = new MiniClassRelationSearchVo();
		vo.setNewMiniClassId(miniClassId);
		this.deleteOldMiniClass(vo);
		MiniClassRelationSearchVo vo2 = new MiniClassRelationSearchVo();
		vo2.setOldMiniClassId(miniClassId);
		this.deleteOldMiniClass(vo2);
	}
	
	private void checkMiniClassOnLineSale(String miniClassId) {
	    String sql = " select count(1) from mini_class where mini_class_id = :miniClassId and ONLINE_SALE = 0 and on_shelves = 0 ";
	    Map<String, Object> params = Maps.newHashMap();
	    params.put("miniClassId", miniClassId);
	    int result = smallClassDao.findCountSql(sql, params);
	    if (result > 0) {
	        throw new ApplicationException("该小班已在报读平台销售，请先下架该小班后再删除小班");
	    }
	}

	@Override
	public void saveOrUpdateMiniClass(MiniClass miniClass) throws Exception {
		
//		boolean miniClassHasPreChargeRecord = miniClassHasPreChargeRecord(miniClass.getMiniClassId());
//		if (miniClassHasPreChargeRecord) {
//			throw new ApplicationException("该小班已经有扣费记录，不允许修改！");
//		}
		/**
		 * 同名班级
		 */
		miniClassSameNameAndSameCampus(miniClass);
		
		miniClass.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		miniClass.setModifyUserId(user.getUserId());

		if(miniClass.getMiniClassId()==null){
			miniClass.setCreateTime(DateTools.getCurrentDateTime());
			miniClass.setCreateUserId(user.getUserId());
			miniClass.setStatus(MiniClassStatus.PENDDING_START);
		}
		
		int maxQuantity = miniClass.getAllowedExcess() != null ? miniClass.getPeopleQuantity() + miniClass.getAllowedExcess() : miniClass.getPeopleQuantity();
		miniClassInventoryService.updateInventory(miniClass.getMiniClassId(), maxQuantity, miniClass.getPeopleQuantity());
		
//		if (null != miniClass.getGrade() && "null".equals(miniClass.getGrade().getId())) {
//			miniClass.setGrade(null);
//		}

		// 同步小班到在线报读
		if(miniClass.getOnlineSale() == 0 && !this.syncMiniClassToMobile(miniClass)){
            log.error("同步小班到在线出错：" + miniClass.getMiniClassId());
		}

		// 对该小班进行排课
		//arrangementMiniClassCourse(miniClass.getMiniClassId());
	}

	public Boolean getModifyCourseModal(MiniClass miniClass){
            String sql = "select modal_id from mini_class mc where mc.MINI_CLASS_ID = :miniClassId ";
            Map<String, Object> params = Maps.newHashMap();
            params.put("miniClassId", miniClass.getMiniClassId());
            List<Map<Object, Object>> list = miniClassDao.findMapBySql(sql, params);
            Integer oldModalId = 0;
            if(list!=null && !list.isEmpty()){
                Object modalId = list.get(0).get("modal_id");
                if(modalId!=null){
                    oldModalId = (Integer)modalId;
                }
            }
            //金旺修改。
            if(miniClass.getCourseModaleId()!=null && miniClass.getCourseModaleId()!=0
                    && !Objects.equals(miniClass.getCourseModaleId(),oldModalId) ){
                miniClass.setIsModal(2);
                return true;
            }
        return false;
    }

	public void saveNewCourseModal(MiniClass miniClass){
        User user = userService.getCurrentLoginUser();
        clearAllClassCourse(miniClass.getMiniClassId());
        String courseEndTime = null;
        if(miniClass.getClassTime()!=null && miniClass.getEveryCourseClassNum()!=null && miniClass.getClassTimeLength()!=null){
            Double dou= miniClass.getEveryCourseClassNum()*miniClass.getClassTimeLength().doubleValue();
            try {
                courseEndTime = DateTools.dateConversString(DateTools.add(DateTools.getDateTime("2012-01-01 " + miniClass.getClassTime()), Calendar.MINUTE, dou.intValue()), "HH:mm");
            }catch (Exception e){
                throw new ApplicationException("计算结束时间错误！");
            }
        }

        CourseModal modal = courseModalDao.findById(miniClass.getCourseModaleId());
        miniClass.setModal(modal);
        miniClass.setIsModal(2);
        CourseModalVo modalVo=courseModalService.findModalByModalId(miniClass.getCourseModaleId());

        int i =1;

        for(String courseDate:modalVo.getCourseDate()){
            MiniClassCourse course = new MiniClassCourse();
            course.setCourseDate(courseDate);
            if(miniClass.getClassroom()!=null) {
                course.setClassroom(miniClass.getClassroom());
            }
            if(StringUtils.isNotBlank(miniClass.getClassTime())) {
                course.setCourseTime(miniClass.getClassTime());
            }
            if(miniClass.getTeacher()!=null) {
                course.setTeacher(miniClass.getTeacher());
            }
            course.setCourseNum(i);
            course.setMiniClass(miniClass);
            course.setSubject(miniClass.getSubject());
            course.setGrade(miniClass.getGrade());
            course.setMiniClassName(miniClass.getName());
            course.setCourseMinutes(BigDecimal.valueOf(miniClass.getClassTimeLength()));
            course.setStudyHead(miniClass.getStudyManeger());
            course.setCourseHours(miniClass.getEveryCourseClassNum());
            course.setCreateTime(DateTools.getCurrentDateTime());
            course.setCreateUserId(user.getUserId());
            course.setModifyTime(DateTools.getCurrentDateTime());
            course.setModifyUserId(user.getUserId());
            course.setCourseStatus(CourseStatus.NEW);
            course.setAuditStatus(AuditStatus.UNAUDIT);
            //
            course.setCourseEndTime(courseEndTime);
            miniClassCourseDao.save(course);
            i++;
        }
        updateCourseNumByClassId(miniClass.getMiniClassId());//更新排序
        miniClassCourseDao.flush();
        updateMiniClassMaxMinCourseDate(miniClass);
        List<MiniClassStudent> list=miniclassStudentDao.getMiniClassStudent(miniClass.getMiniClassId());
        for(MiniClassStudent mcs : list){//考勤记录补充
            this.addMiniClassStudentAttendents(mcs.getStudent().getId(), miniClass.getMiniClassId(), mcs.getFirstSchoolTime());
        }
    }

	/**
	 * 同步小班到在线报读
	 * @param miniClass
	 * @return
	 * @throws Exception
	 */
	private boolean syncMiniClassToMobile(MiniClass miniClass) throws Exception {
	    if (miniClass.getTeacher() == null) {
	        throw new ApplicationException("同步到线上的课程必须有老师");
	    }
	    MiniClassVo mVo = HibernateUtils.voObjectMapping(miniClass, MiniClassVo.class);
	    if (StringUtil.isBlank(mVo.getSubjectName()) && StringUtil.isNotBlank(mVo.getSubjectId())) {
	        mVo.setSubjectName(dataDictService.findById(mVo.getSubjectId()).getName());
	    }
	    if (StringUtil.isBlank(mVo.getGradeName()) && StringUtil.isNotBlank(mVo.getGradeId())) {
            mVo.setGradeName(dataDictService.findById(mVo.getGradeId()).getName());
        }
	    if (StringUtil.isBlank(mVo.getTeacherName()) && StringUtil.isNotBlank(mVo.getTeacherId())) {
            mVo.setTeacherName(userService.findUserById(mVo.getTeacherId()).getName());
        }
        MiniClassRpcVo vo = HibernateUtils.voObjectMapping(mVo, MiniClassRpcVo.class);
        if (StringUtils.isNotBlank(miniClass.getCourseStartTime())) {
            vo.setStartDate(miniClass.getCourseStartTime());
        }
        if (StringUtils.isNotBlank(miniClass.getCourseEndTime())) {
            vo.setEndDate(miniClass.getCourseEndTime());
        }
        vo.setBlBranchId(organizationService.findById(vo.getBlCampusId()).getParentId());
        String classroomName = mVo.getClassroomName();
        if (StringUtils.isBlank(classroomName) && StringUtils.isNotBlank(mVo.getClassroomId())) {
            classroomName = classroomManageService.findClassroomById(mVo.getClassroomId()).getClassroom();
        }
        vo.setClassAddress(classroomName);
        int chapterNum = miniClass.getTotalClassHours() % miniClass.getEveryCourseClassNum() == 0 ?
                (int)(miniClass.getTotalClassHours()/miniClass.getEveryCourseClassNum()) :
                    (int)(miniClass.getTotalClassHours()/miniClass.getEveryCourseClassNum() + 1);
        vo.setChapterNum(chapterNum);
        vo.setTotalClassHours(miniClass.getTotalClassHours());
        if (PropertiesUtils.getStringValue("institution").equals("xinghuo")) {
            vo.setOrgSysType("XINGHUO");
        } else {
            vo.setOrgSysType("ADVANCE");
        }
        return bossCourseRpc.saveCourseByBoss(vo);
    }

	/**
	 * 同一个校区不能录入相同的小班
	 * @param miniClass
	 * @return
	 */
	private void miniClassSameNameAndSameCampus(MiniClass miniClass) {
		if (miniClass!=null){
			Map<String, Object>  params = new HashMap<>();
			String sql = " SELECT count(*) from mini_class where `NAME` = :name AND BL_CAMPUS_ID = :blCampusId  ";
			params.put("name", miniClass.getName());
			if (miniClass.getBlCampus()!=null){
				params.put("blCampusId", miniClass.getBlCampus().getId());
			}

			if (StringUtils.isNotEmpty(miniClass.getMiniClassId())){
				sql+=" AND MINI_CLASS_ID <> :miniClassId ";
				params.put("miniClassId", miniClass.getMiniClassId());
			}

			int nums = smallClassDao.findCountSql(sql, params);
			if (nums>0){
				throw new ApplicationException("该校区已有同名班级，请修改班级名称再保存！");
			}
		}
	}

	@Override
	public List<StudentVo> getStudentForEnrollMiniClasss(String idOrName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from Student where (name like :name or id like :id )";
		params.put("name", "%" + idOrName + "%");
		params.put("id", idOrName + "%");
		//归属组织架构
		Organization campus = userService.getBelongCampus();
		if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
			String orgLevel = campus.getOrgLevel();
			hql+=" and blCampusId in (select id from Organization where orgLevel like '"+orgLevel+"%')";
		}
		List<Student> studentList = studentDao.findAllByHQL(hql, params);
		List<StudentVo> vos= new ArrayList<StudentVo>(); 
		for(Student stu : studentList){
			StudentVo vo=new StudentVo();
			vo.setId(stu.getId());
			vo.setName(stu.getName());
			vos.add(vo);
		}
		return vos;
	}
	
	/**
	 * 根据小班id查看小班已报读学生
	 *@param smallClassId
	 *@return
	 */
	@Override
	public List<StudentVo> getStudentAlreadyEnrollMiniClasss(String smallClassId) {
		List<Criterion> list=new ArrayList<Criterion>(); 
		list.add(Expression.eq("miniClass.miniClassId", smallClassId));
		List<Order> orderList=new ArrayList<Order>();

		orderList.add(Order.desc("contractProduct.type"));
		orderList.add(Order.desc("createTime"));
		List<MiniClassStudent> miniClassList =miniClassStudentDao.findAllByCriteria(list,orderList);
		List<StudentVo> stuList=new ArrayList<StudentVo>();
		for(MiniClassStudent miniStudent : miniClassList){
			if(miniStudent.getStudent()!=null){
				StudentVo vo=HibernateUtils.voObjectMapping(miniStudent.getStudent(), StudentVo.class);
				vo.setMiniClassStudentChargeStatus(miniStudent.getMiniClassStudentChargeStatus().getValue());
				if (null != miniStudent.getContractProduct()) {
					vo.setProductName(miniStudent.getContractProduct().getProduct().getName());
					vo.setContractType(miniStudent.getContractProduct().getType().getValue());
				}
				if(StringUtils.isEmpty(miniStudent.getFirstSchoolTime())){
					vo.setFirstSchoolTime("");
				}else{
					vo.setFirstSchoolTime(miniStudent.getFirstSchoolTime());
				}
				
				stuList.add(vo);
			}
		}
		return stuList;
	}

	/**
	 * 根据小班id查看合同拟报读学生id列表
	 *@param smallClassId
	 *@return
	 */
	@Override
	public List<StudentVo> getStudentWantListBySmallClassId(String smallClassId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.*,c.id contractId,ifnull(cp.ID,'') as ID1,cp.PAID_AMOUNT-cp.CONSUME_AMOUNT paidAmount,((cp.PAID_AMOUNT-cp.CONSUME_AMOUNT)/(PRICE*DEAL_DISCOUNT)) quantity from student s INNER JOIN contract c on s.id = c.STUDENT_ID INNER JOIN contract_product cp on c.id = cp.CONTRACT_ID and cp.type ='SMALL_CLASS' ");
		// 已经报读了这个产品
		sql.append(" where cp.PRODUCT_ID in (");
		sql.append(" select PRODUCE_ID from mini_class where MINI_CLASS_ID = '").append(smallClassId).append("'");
		sql.append(" )");
		// 且没有报读到其他 实际小班的
		sql.append("and not exists ( select 1 from mini_class_student where  mini_class_student.CONTRACT_PRODUCT_ID = cp.ID  and  MINI_CLASS_ID <> '").append(smallClassId).append("')");
		// 还没有报读到该小班的
		sql.append(" and s.id not in(");
		sql.append(" select STUDENT_ID from mini_class_student where MINI_CLASS_ID   = '").append(smallClassId).append("'");
		//sql.append(" select STUDENT_ID from mini_class_student where MINI_CLASS_ID  in (select MINI_CLASS_ID from mini_class where produce_id = (select produce_id from mini_class where MINI_CLASS_ID = '").append(smallClassId).append("'))");
//		sql.append(" ) and s.BL_CAMPUS_ID in (select id from organization where orgLevel like '").append(userService.getBelongCampus().getOrgLevel()).append("%' ) ");
		sql.append(" ) and s.id in (select STUDENT_ID from STUDENT_ORGANIZATION where ORGANIZATION_ID= '").append(userService.getBelongCampus().getId()).append("' ) ");
		
		//sql.append(" group by s.id");
		
		// ID1 是合同产品ID。
		List<Object[]> list= studentDao.getCurrentSession().createSQLQuery(sql.toString()).addEntity("s", Student.class).addScalar("contractId").addScalar("ID1").addScalar("paidAmount").addScalar("quantity").list();
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < list.size(); i++) {
			students.add((Student) list.get(i)[0]);
		}
		List<StudentVo> vos = HibernateUtils.voListMapping(students, StudentVo.class);
		for (int i = 0; i < vos.size(); i++) {
			vos.get(i).setContractId(list.get(i)[1].toString());
			vos.get(i).setContractProductId(list.get(i)[2].toString());
			// 剩余资金 和 剩余课时 的显示
			if(!"".equals(list.get(i)[2].toString())){
				ContractProduct conProd = contractProductDao.findById(list.get(i)[2].toString());
				vos.get(i).setRemainingAmount(conProd.getRemainingAmount()==null? BigDecimal.ZERO :conProd.getRemainingAmount());
				vos.get(i).setQuantity((conProd.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 || conProd.getPrice().compareTo(BigDecimal.ZERO) == 0 ||conProd.getDealDiscount().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : conProd.getRemainingAmount().divide(conProd.getPrice().multiply(conProd.getDealDiscount()),2));
			}
//			if(list.get(i)[3] != null){
//				vos.get(i).setPaidAmount(BigDecimal.valueOf(Double.valueOf(list.get(i)[3].toString())));
//			}
//			if(list.get(i)[4] != null){
//				vos.get(i).setQuantity(BigDecimal.valueOf(Double.valueOf(list.get(i)[4].toString())));
//			}
		}
		return vos;
	}
	
	/**
	 * 新逻辑，小班关联多产品
	 * 根据小班id查看合同拟报读学生列表
	 * @param miniClassId 小班id
	 * @return
	 */
	@Override
	public List<StudentVo> getStudentWantListByMiniClassId(String miniClassId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.*,c.id contractId,ifnull(cp.ID,'') as ID1,cp.PAID_AMOUNT-cp.CONSUME_AMOUNT paidAmount,((cp.PAID_AMOUNT-cp.CONSUME_AMOUNT)/(PRICE*DEAL_DISCOUNT)) quantity from student s INNER JOIN contract c on s.id = c.STUDENT_ID INNER JOIN contract_product cp on c.id = cp.CONTRACT_ID and cp.type ='SMALL_CLASS' ");
		// 已经报读了这个产品
		sql.append(" where cp.PRODUCT_ID in (");
		sql.append(" select PRODUCT_ID from mini_class_product where MINI_CLASS_ID = '").append(miniClassId).append("'");
//							.append(" and IS_MAIN_PRODUCT = '0'");
		sql.append(" )");
		// 且没有报读到其他 实际小班的
		sql.append("and not exists ( select 1 from mini_class_student where  mini_class_student.CONTRACT_PRODUCT_ID = cp.ID  and  MINI_CLASS_ID <> '").append(miniClassId).append("')");
		// 还没有报读到该小班的
		sql.append(" and s.id not in(");
		sql.append(" select STUDENT_ID from mini_class_student where MINI_CLASS_ID   = '").append(miniClassId).append("'");
		sql.append(" ) and s.id in (select STUDENT_ID from STUDENT_ORGANIZATION where ORGANIZATION_ID= '").append(userService.getBelongCampus().getId()).append("' ) ");
		//排除合同产品结课的
		sql.append(" and cp.STATUS <> '").append(ContractProductStatus.CLOSE_PRODUCT).append("' ");
		sql.append(" and cp.STATUS <> '").append(ContractProductStatus.UNVALID).append("' ");
		// ID1 是合同产品ID。
		List<Object[]> list= studentDao.getCurrentSession().createSQLQuery(sql.toString()).addEntity("s", Student.class).addScalar("contractId").addScalar("ID1").addScalar("paidAmount").addScalar("quantity").list();
		List<Student> students = new ArrayList<Student>();
		for (int i = 0; i < list.size(); i++) {
			students.add((Student) list.get(i)[0]);
		}
		List<StudentVo> vos = HibernateUtils.voListMapping(students, StudentVo.class);
		for (int i = 0; i < vos.size(); i++) {
			vos.get(i).setContractId(list.get(i)[1].toString());
			vos.get(i).setContractProductId(list.get(i)[2].toString());
			// 剩余资金 和 剩余课时 的显示
			if(!"".equals(list.get(i)[2].toString())){
				ContractProduct conProd = contractProductDao.findById(list.get(i)[2].toString());
				vos.get(i).setProductId(conProd.getProduct().getId());
				vos.get(i).setProductName(conProd.getProduct().getName());
				vos.get(i).setRemainingAmount(conProd.getRemainingAmount()==null? BigDecimal.ZERO :conProd.getRemainingAmount());
				vos.get(i).setQuantity((conProd.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 || conProd.getPrice().compareTo(BigDecimal.ZERO) == 0 ||conProd.getDealDiscount().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : conProd.getRemainingAmount().divide(conProd.getPrice().multiply(conProd.getDealDiscount()),2));
			}
		}
		return vos;
	}

	/**
     * 批量添加小班学生
     */
    @Override
    public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,String contractProductId,String firstSchoolTime,Boolean isAllowAddStudent){
        AddStudentForMiniClasss(studentIds, smallClassId, contractId, contractProductId, firstSchoolTime, isAllowAddStudent, true);
    }

	/**
	 * 批量添加小班学生
	 */
	@Override
	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,String contractProductId,String firstSchoolTime,Boolean isAllowAddStudent, boolean updateInventory){
		String[] stuIds=studentIds.split(",");
		String[] contractIds=contractId.split(",");
		String[] contractProductIds= null;
		if(StringUtils.isNotEmpty(contractProductId))
			contractProductIds=contractProductId.split(",");
		boolean allowRegistration =true;
		if(isAllowAddStudent!=null && isAllowAddStudent) {
			allowRegistration = allowAddStudent4MiniClass(smallClassId, stuIds.length, updateInventory);// 是否允许添加小班学生
		}
		
		for (int i = 0;i < stuIds.length; i++) {
			if (null != miniClassStudentDao.getOneMiniClassStudent(smallClassId, stuIds[i])) {
				Student student = studentDao.findById(stuIds[i]);
				throw new ApplicationException("学生：" + student.getName() + "已经在本班有报名记录了！");
			}
			ContractProduct cp =contractProductDao.findById(contractProductIds[i]);

			if(!cp.getType().equals(ProductType.ECS_CLASS)  && null !=miniClassStudentDao.getMiniClassStudentByStuIdAndCpId(stuIds[i],contractProductIds[i])){
				Student student = studentDao.findById(stuIds[i]);
				throw new ApplicationException("学生：" + student.getName() + "该产品已经报读具体的班级，请不要重复报班！");
			}
		}
		
		if (allowRegistration) {
			MiniClass miniClass = miniClassDao.findById(smallClassId);
			User user = userService.getCurrentLoginUser();
			for(int i = 0;i < stuIds.length; i++){
				MiniClassStudent miniStu=new MiniClassStudent();
				miniStu.setStudent(new Student(stuIds[i]));
				miniStu.setMiniClass(miniClass);
				miniStu.setMiniClassStudyStatus(MiniClassStatus.PENDDING_START);
				miniStu.setCreateUserId(user.getUserId());
				miniStu.setCreateTime(DateTools.getCurrentDateTime());
				miniStu.setModifyUserId(user.getUserId());
				miniStu.setModifyTime(DateTools.getCurrentDateTime());
//				miniStu.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);
				miniStu.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.CHARGED);
				miniStu.setContract(new Contract(contractIds[i]));
				if(StringUtils.isNotBlank(firstSchoolTime)) {
					miniStu.setFirstSchoolTime(firstSchoolTime);
				}else{
					miniStu.setFirstSchoolTime(miniClass.getStartDate());
				}
				if(contractProductIds!=null && contractProductId.length()>0) {
					miniStu.setContractProduct(new ContractProduct(contractProductIds[i]));
					ContractProduct cp = contractProductDao.findById(contractProductIds[i]);
					cp.setMiniClassId(smallClassId);
					contractProductDao.merge(cp);
				}
				miniClassStudentDao.save(miniStu);
				this.addMiniClassStudentAttendents(stuIds[i], smallClassId, firstSchoolTime);

			}

			updateMiniClassRecruitStatus(miniClass);

		}
	}

    /**
     * 批量添加小班学生
     */
    @Override
    public void AddPromiseStudentForMiniClasss(Student student, MiniClass miniClass,ContractProduct cp,BigDecimal hours){
		miniClass=miniClassDao.findById(miniClass.getMiniClassId());
		User user = userService.getCurrentLoginUser();
        if (null != miniClassStudentDao.getOneMiniClassStudent(miniClass.getMiniClassId(), student.getId())) {
            throw new ApplicationException("学生：" + student.getName() + "已经在本班有报名记录了！");
        }

		int peopleExistingNumber = getMiniClassExistingPeopleNumber(miniClass.getMiniClassId());// 现有人数
		if(peopleExistingNumber+1>miniClass.getPeopleQuantity()+miniClass.getAllowedExcess()){
			Integer peopleNum = miniClass.getPeopleQuantity()+miniClass.getAllowedExcess();
			throw new ApplicationException("小班：“"+miniClass.getName()+"”超出招生人数("+peopleNum+"人)");
		}


		MiniClassStudent miniStu=new MiniClassStudent();
		miniStu.setStudent(student);
		miniStu.setMiniClass(miniClass);
		miniStu.setMiniClassStudyStatus(MiniClassStatus.PENDDING_START);
		miniStu.setCreateUserId(user.getUserId());
		miniStu.setCreateTime(DateTools.getCurrentDateTime());
		miniStu.setModifyUserId(user.getUserId());
		miniStu.setModifyTime(DateTools.getCurrentDateTime());
		miniStu.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.CHARGED);
		miniStu.setContract(cp.getContract());
		miniStu.setFirstSchoolTime(getFirstSchoolTime(miniClass,hours));
		miniStu.setContractProduct(cp);
		cp.setMiniClassId(miniClass.getMiniClassId());
		miniClassStudentDao.save(miniStu);
		this.addMiniClassStudentAttendents(student.getId(), miniClass.getMiniClassId(), miniStu.getFirstSchoolTime());
    }

	/**
	 * 获取开始上课日期。
	 * @param miniClass
	 * @param hours
	 * @return
	 */
	public String getFirstSchoolTime(MiniClass miniClass,BigDecimal hours){
    	if(miniClass.getTotalClassHours().compareTo(hours.doubleValue())==0){
    		return miniClass.getStartDate();
		}
		String firstSchoolTime = miniClass.getStartDate();
    	List<MiniClassCourse> list = findMiniClassCourseByClassId(miniClass.getMiniClassId());

    	for(int i = list.size()-1;i>=0;i--){
			MiniClassCourse mc = list.get(i);
			hours=hours.subtract(BigDecimal.valueOf(mc.getCourseHours()));
			if(hours.compareTo(BigDecimal.ZERO)<=0){
				firstSchoolTime= mc.getCourseDate();
				break;
			}
		}

    	return firstSchoolTime;
	}
	
	/**
	 *  判断能否添加学生到小班中去
	 */
	public Response checkAllowAddStudent4MiniClass(String studentIds, String smallClassId) {
		Response response = new Response();
		String[] stuIds=studentIds.split(",");
		int newStudentNumber = stuIds.length;
		int miniClassMaxPeopleNumber = getMiniClassMaxPeopleNumber(smallClassId);// 最大人数
		int peopleExistingNumber = getMiniClassExistingPeopleNumber(smallClassId);// 现有人数
		int afterPeopleNumber = peopleExistingNumber + newStudentNumber;
		if (afterPeopleNumber <= miniClassMaxPeopleNumber) {
			// 小班现有人数少于最大人数，还可以继续报名
			response.setResultCode(0);
		} else {
			response.setResultCode(-1);
			response.setResultMessage("超出招生人数（"+ miniClassMaxPeopleNumber + "人）");
		}
		return response;
	}
	
	/**
	 * 增加小班续班扩科记录
	 * @param studentId
	 * @param smallClassId
	 * @param continueMiniClassId
	 * @param extendMiniClassId
	 */
	@Override
	public void addMiniClassRelation(String studentId, String smallClassId, String continueMiniClassId, String extendMiniClassId) {
		User user = userService.getCurrentLoginUser();
		Student student = new Student();
		student.setId(studentId);
		MiniClass newMiniClass = new MiniClass();
		newMiniClass.setMiniClassId(smallClassId);
		if (StringUtil.isNotBlank(continueMiniClassId)) {
			MiniClassRelation relation = new MiniClassRelation();
			relation.setRelationType(MiniClassRelationType.CONTINUE);
			relation.setNewMiniClass(newMiniClass);
			MiniClass oldMiniClass = new MiniClass();
			oldMiniClass.setMiniClassId(continueMiniClassId);
			relation.setOldMiniClass(oldMiniClass);
			relation.setStudent(student);
			relation.setCreateUserId(user.getUserId());
			relation.setCreateTime(DateTools.getCurrentDateTime());
			miniClassRelationDao.save(relation);
		}
		if (StringUtil.isNotBlank(extendMiniClassId)) {
			MiniClassRelation relation = new MiniClassRelation();
			relation.setRelationType(MiniClassRelationType.EXTEND);
			relation.setNewMiniClass(newMiniClass);
			MiniClass oldMiniClass = new MiniClass();
			oldMiniClass.setMiniClassId(extendMiniClassId);
			relation.setOldMiniClass(oldMiniClass);
			relation.setStudent(student);
			relation.setCreateUserId(user.getUserId());
			relation.setCreateTime(DateTools.getCurrentDateTime());
			miniClassRelationDao.save(relation);
		}
	}
	
	/**
	 * 根据学生ID和新的小班ID获取续班扩科记录
	 * @param studentId
	 * @param smallClassId
	 */
	public List<MiniClassRelation> getMiniClassRelatonsByStudentNewMiniClass(String studentId, String smallClassId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("student.id", studentId));
		criterionList.add(Expression.eq("newMiniClass.miniClassId", smallClassId));
		return miniClassRelationDao.findAllByCriteria(criterionList);
	}
	
	/**
	 * 批量添加小班学生
	 */
	@Override
	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,String contractProductId,Boolean isAllowAddStudent) throws Exception {
		this.AddStudentForMiniClasss(studentIds, smallClassId, contractId,contractProductId, null, isAllowAddStudent);
	}
	
	/**
	 * 批量添加小班学生
	 */
	@Override
	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,Boolean isAllowAddStudent) throws Exception {
		this.AddStudentForMiniClasss(studentIds, smallClassId, contractId, null, isAllowAddStudent);
	}
	
	/**
	 * 是否允许新增小班学生
	 */
	@Override
	public boolean allowAddStudent4MiniClass(String smallClassId, int newStudentNumber, boolean updateInventory) {
		boolean returnAllow = false;// 是否允许新增小班学生
		boolean constraintPeopleQuantity = true;// 是否约束小班人数 默认限制
		
//		MiniClass miniClass = smallClassDao.findById(smallClassId);
//		if (BaseStatus.TRUE == miniClass.getConstraintPeopleQuantity()) {// 默认和TRUE的情况，约束小班人数
//			constraintPeopleQuantity = true;
//		} else if (BaseStatus.FALSE == miniClass.getConstraintPeopleQuantity()) {// FALSE的情况，不约束小班人数
//			constraintPeopleQuantity = false;
//		} 
		
		int miniClassMaxPeopleNumber = getMiniClassMaxPeopleNumber(smallClassId);// 最大人数
		if (updateInventory) {
		    if (!miniClassInventoryService.reduceInventory(smallClassId, newStudentNumber, true)) {
		        throw new ApplicationException("报名人数超出小班计划招生人数（"+ miniClassMaxPeopleNumber + "人）");
		    } else {
		        returnAllow = true;
		    }
		} else {
			if (constraintPeopleQuantity) {// 如果约束人数
				int peopleExistingNumber = getMiniClassExistingPeopleNumber(smallClassId);// 现有人数
				int afterPeopleNumber = peopleExistingNumber + newStudentNumber;
				if (afterPeopleNumber <= miniClassMaxPeopleNumber) {
					// 小班现有人数少于最大人数，还可以继续报名
					returnAllow = true;
				} else {
					returnAllow = false;
					throw new ApplicationException("超出招生人数（"+ miniClassMaxPeopleNumber + "人）");
				}
			} else {// 如果不约束人数
				returnAllow = true;
			}
		}


		return returnAllow;
	}
	
	/**
	 * 小班最大人数
	 */
	@Override
	public int getMiniClassMaxPeopleNumber(String miniClassId) {
//		MiniClass mniClass = smallClassDao.findById(miniClassId);
//		mniClass.getMiniClassType();
//		String miniClassType = mniClass.getMiniClassType().getName();
//		String regEx="[^0-9]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(miniClassType);
//		int peopleMaxNumber = Integer.valueOf(m.replaceAll("").trim());
//		return peopleMaxNumber;
		MiniClass mniClass = smallClassDao.findById(miniClassId);
		Integer peopleQuantity = mniClass.getPeopleQuantity();
		if (null == peopleQuantity) {
			throw new RuntimeException("未设置该小班报名人数，请设置小班报名人数！");
		}
		if(mniClass.getAllowedExcess()!=null && mniClass.getAllowedExcess()>0){
			peopleQuantity+=mniClass.getAllowedExcess();
		}
		return peopleQuantity;
	}
	
	/**
	 * 小班现有人数
	 */
	@Override
	public int getMiniClassExistingPeopleNumber(String miniClassId) {
		List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
		StudentCriterionList.add(Expression.eq("miniClass.miniClassId", miniClassId));
		int peopleExistingNumber = miniClassStudentDao.findCountByCriteria(StudentCriterionList);
		return peopleExistingNumber;
	}

	@Override
	public void deleteStudentInMiniClasss(String studentIds, String smallClassId){
		MiniClass miniClass=miniClassDao.findById(smallClassId);
		String[] stuIds=studentIds.split(",");
		for (String id : stuIds) {
			MiniClassStudent oneMiniClassStudent = miniClassStudentDao.getOneMiniClassStudent(smallClassId, id);
			if(oneMiniClassStudent != null)
			{
				ContractProduct contractProduct = oneMiniClassStudent.getContractProduct();
				contractProduct.setMiniClassId(null);
				contractProductDao.merge(contractProduct);
				miniClassStudentDao.delete(oneMiniClassStudent);
				miniClassInventoryService.revertInventory(smallClassId);
			}
			miniClassStudentAttendentDao.deleteMiniClassStudentAttendentByStudentAndMiniClass(id, smallClassId);
		}
		updateMiniClassRecruitStatus(miniClass);
	}
	
	/**
	 *  根据合同移除学生报名记录
	 */
	public void deleteStudentByContract(Contract contract) {
	    List<MiniClassStudent> list = miniclassStudentDao.listMiniClassStudentByContract(contract);
	    if (list != null && list.size() > 0) {
	        for (MiniClassStudent student : list) {
	            miniClassInventoryService.revertInventory(student.getMiniClass().getMiniClassId());
	            miniclassStudentDao.delete(student);
	        }
	    }
	}

	/**
	 * 获取有考勤记录的学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	public List<Student> getMiniClassAttendedStudent(String studentIds, String smallClassId) throws Exception {
		List<Student> studentList = new ArrayList<Student>();
		String[] stuIds=studentIds.split(",");
		for (String id : stuIds) {
			int count = miniClassStudentAttendentDao.getCountOfUnchargedAttendenceRecordByMiniClassAndStudent(smallClassId, id, "UNCHARGE");
			if (count > 0) {
				studentList.add(studentDao.findById(id));
			}
		}
		return studentList;
	}

	/**
	 * 检测学生id是否存在，是否已报读该小班
	 * @param studentId
	 * @param smallClassId
	 * @return
	 */
	@Override
	public Response checkMiniClassStudent(String studentId, String smallClassId) {
		Response res=new Response();
		MiniClassStudent miniStu=miniClassStudentDao.findById(new MiniClassStudentId(smallClassId, studentId));
		if(miniStu!=null){
			res.setResultCode(1);
			res.setResultMessage("该学生已经报读当前小班");
		}else if(studentDao.findById(studentId)==null){
			res.setResultCode(1);
			res.setResultMessage("该学生不存在");
		}
		return res;
	}

	@Override
	public List<MiniClass> getMiniClassByIdOrName(String idOrName) {
		List<Criterion> list=new ArrayList<Criterion>();
		list.add(Expression.like("name", idOrName,MatchMode.ANYWHERE));
		//list.add(Expression.or(Expression.like("miniClassId", idOrName,MatchMode.ANYWHERE), Expression.like("name", idOrName,MatchMode.ANYWHERE)));
		return smallClassDao.findAllByCriteria(list);
	}

	@Override
	public void adjustMiniClassStudent(String oldSmallClassId,
			String newSmallClassId, String studentIds) {
		String sql = ""; // 更新合同产品关联的小班
		for(String id: studentIds.split(",")){
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			sql = " update contract_product cp left join contract c on cp.CONTRACT_ID = c.ID SET MINI_CLASS_ID = :newSmallClassId ";
			sql += " where cp.MINI_CLASS_ID = :oldSmallClassId and c.STUDENT_ID = :id ";
			sqlParams.put("newSmallClassId", newSmallClassId);
			sqlParams.put("oldSmallClassId", oldSmallClassId);
			sqlParams.put("id", id);
			miniClassStudentDao.excuteSql(sql, sqlParams);
		}
		Map<String, Object> hqlParams = new HashMap<String, Object>();
		String hql=" update MiniClassStudent set miniClassId='"+newSmallClassId+"' where  " +
				"miniClassId='"+oldSmallClassId+"' and studentId in(:studentIds) ";
		hqlParams.put("newSmallClassId", newSmallClassId);
		hqlParams.put("oldSmallClassId", oldSmallClassId);
		hqlParams.put("studentIds", studentIds.split(","));
		
		miniClassStudentDao.excuteHql(hql, hqlParams);
	}

	/**
	 * 小班是否已经有预扣费记录
	 */
	@Override
	public boolean miniClassHasPreChargeRecord(String miniClassId) {
		if (StringUtils.isBlank(miniClassId)) {
			return false;
		} else {
			DataPackage dp = new DataPackage(0,10);
			List<Criterion> criterionList = new ArrayList<Criterion>();
			criterionList.add(Expression.eq("miniClass.miniClassId", miniClassId));
			dp=accountChargeRecordsDao.findPageByCriteria(dp,null,criterionList);
			if (dp.getDatas().size() > 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 对该小班进行排课
	 * @param miniClassId
	 * @throws Exception
	 */
	@Override
	public void arrangementMiniClassCourse(String miniClassId) throws Exception {
		
		MiniClass miniClass = smallClassDao.findById(miniClassId);// 对该小班进行自动排课
		
		if (miniClass != null) {
			Double consume = miniClass.getConsume();
			if (null == consume || consume<=0) {// 已上课时数为零才允许更改小班课程
				
				// 删除已排课程
				
				miniClassCourseDao.deleteMiniClassCourse(miniClassId);
				
				
				String strStartDate = miniClass.getStartDate();// 开始时间
//				String strEndDate = miniClass.getEndDate();// 结束时间
				String courseWeekDay = miniClass.getCourseWeekday();
				String[] weekStrArray = courseWeekDay.split(",");
				int[] weekArray = new int[weekStrArray.length];// 每周几排课

				// 课程的
				DataDict dictClassTime =null;
//						miniClass.getClassTime();
				dictClassTime = dataDictDao.findById(dictClassTime.getValue());
				String classTime = dictClassTime.getName();
				String[] classTimeArray = classTime.split("-");
				String strEveryDateBeginTime = classTimeArray[0].trim();// 生成每个小班课程的开始时间
				String strEveryDateEndTime = classTimeArray[1].trim();// 生成每个小班课程的结束时间
				
				// 每个课程的课时
				Product product = productDao.findById( miniClass.getProduct().getId());
				// 这里命名会产生歧义其实MiniClassTotalhours（小班总课时数） 应该是 （小班课程总节数）MiniClassTotalClass
				BigDecimal miniClassTotalClass = BigDecimal.valueOf(miniClass.getTotalClassHours());
				if (null == miniClassTotalClass
						|| (-1 == miniClassTotalClass.compareTo(BigDecimal.ZERO))
						|| (0 == miniClassTotalClass.compareTo(BigDecimal.ZERO))) {
					throw new RuntimeException("关联产品的小班课程总节数为空，请设置产品的总课程节数！");
				}
				if (null == product.getClassTimeLength() || product.getClassTimeLength() <=0 ) {
					throw new RuntimeException("关联产品的课时时长为空，请设置产品的课时时长！");
				}
				if (null == miniClass.getEveryCourseClassNum() || miniClass.getEveryCourseClassNum() <= 0) {
					throw new RuntimeException("小班的每次课时数为空，请设置小班的每次课时数！");
				}
				Double everyCourseHours = miniClass.getEveryCourseClassNum();// 这里的课时应该是每节课消耗课时（课程节数）
						
				// 勾选的星期记录
				for (int n = 0; n < weekArray.length; n++) {
					WeekDay weekDay = WeekDay.valueOf(WeekDay.class, weekStrArray[n]);
					if (WeekDay.MON == weekDay) {
						weekArray[n] = 1;
					} else if (WeekDay.TUE == weekDay) {
						weekArray[n] = 2;
					} else if (WeekDay.WED == weekDay) {
						weekArray[n] = 3;
					} else if (WeekDay.THU == weekDay) {
						weekArray[n] = 4;
					} else if (WeekDay.FRI == weekDay) {
						weekArray[n] = 5;
					} else if (WeekDay.SAT == weekDay) {
						weekArray[n] = 6;
					} else if (WeekDay.SUN == weekDay) {
						weekArray[n] = 0;
					}
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = sdf.parse(strStartDate);
//				Date endDate = sdf.parse(strEndDate);

				Calendar calendarPos = new GregorianCalendar();
//				Calendar calendarEnd = new GregorianCalendar();
				calendarPos.setTime(startDate);
//				calendarEnd.setTime(endDate);

//				Product product = productDao.findById(miniClass.getProduct().getId());
				
				Double totalClass = miniClass.getTotalClassHours();// 产品列表里小班总课程节数， 这里的总课时数应该是总课程节数，特别声明，以免产生歧义
				Double arrangedClass = 0.0;// 已排课程节数
				
				String currDateTime = DateTools.getCurrentDateTime();
				User user =userService.getCurrentLoginUser();

				// 已排课时不能超过小班的总课时数 + 排课日期不能超过结束日期
//				while ((arrangedClass < totalClass)
//						&& (calendarPos.before(calendarEnd) || calendarPos
//								.equals(calendarEnd))) {
				while ((arrangedClass < totalClass)) {

					// 剩余课时和排课日期符合排课条件

					int currDayOfWeek = calendarPos.get(Calendar.DAY_OF_WEEK) - 1;
					if (currDayOfWeek < 0) {
						currDayOfWeek = 0;
					}

					for (int week : weekArray) {
						if (currDayOfWeek == week) {// 如果符合匹配设定星期

							// 符合排课条件，排一节课

							// 组装每个小班课程记录
							MiniClassCourse miniClassCourse = new MiniClassCourse();
							miniClassCourse.setTeacher(miniClass.getTeacher());
							miniClassCourse.setStudyHead(miniClass.getStudyManeger());
							miniClassCourse.setCourseDate(DateTools.dateConversString(
									calendarPos.getTime(), "yyyy-MM-dd"));
							miniClassCourse.setCourseEndTime(strEveryDateEndTime);
							miniClassCourse.setCourseHours(everyCourseHours);
							miniClassCourse
									.setCourseStatus(CourseStatus.NEW);
							miniClassCourse.setCourseTime(strEveryDateBeginTime);
							miniClassCourse.setCourseMinutes(BigDecimal.valueOf(miniClass.getClassTimeLength()));
							miniClassCourse.setMiniClass(miniClass);
							miniClassCourse.setMiniClassName(miniClass.getName());
							
							miniClassCourse.setModifyTime(currDateTime);
							miniClassCourse.setModifyUserId(user.getUserId());
							if(null == miniClassCourse.getMiniClassCourseId()){
								miniClassCourse.setCreateTime(currDateTime);
								miniClassCourse.setCreateUserId(user.getUserId());
							}

							if(miniClassCourse.getAuditStatus() == AuditStatus.UNVALIDATE){
								miniClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
							}
							

							miniClassCourseDao.save(miniClassCourse);
							log.info(miniClassCourse.getMiniClassCourseId()+",状态："+miniClassCourse.getCourseStatus());
							this.initMiniClassStudentAttendent(miniClassCourse);

							// 累加总课时数， 不能超过小班的总课时数
							arrangedClass += miniClass.getEveryCourseClassNum();

						}
					}
					miniClassCourseDao.flush();
					this.updateMiniClassMaxMinCourseDate(miniClass);
					// 日期加一
					calendarPos.add(calendarPos.DATE, 1);
				}
			} else {
				throw new RuntimeException("小班已上课时数大于0，不允许修改！");
			}
		} else {
			throw new RuntimeException("找不到指定的小班记录");
		}

	}
	
	/**
	 * 初始化小班考勤记录
	 * @param miniClassCourse
	 */
	public void initMiniClassStudentAttendent(MiniClassCourse miniClassCourse) {
		User user = userService.getCurrentLoginUser();
		MiniClassStudentVo miniClassStudentVo = new MiniClassStudentVo();
		miniClassStudentVo.setMiniClassId(miniClassCourse.getMiniClass().getMiniClassId());
		miniClassStudentVo.setFirstSchoolTime(miniClassCourse.getCourseDate());
		DataPackage dp = new DataPackage(0, 999);
		dp = miniClassStudentDao.getMiniClassStudentList(miniClassStudentVo, dp);
		List<MiniClassStudent> miniClassStudentList = (List<MiniClassStudent>) dp.getDatas();
		for (MiniClassStudent mcs: miniClassStudentList) {
			Student student = mcs.getStudent();
			MiniClassStudentAttendent miniClassStudentAttendent = new MiniClassStudentAttendent();
			miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
			miniClassStudentAttendent.setStudentId(student.getId());
			miniClassStudentAttendent.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW); // 未上课
			miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
			miniClassStudentAttendent.setCreateUser(user);
			miniClassStudentAttendent.setCreateTime(DateTools.getCurrentDateTime());
			miniClassStudentAttendentDao.save(miniClassStudentAttendent);
		}
	}
	
	/**
	 * 获取小班课程列表
	 */
	/*
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getMiniClassCourseList(MiniClassCourseVo miniClassCourseVo, DataPackage dp) {
		
//		//校区下面的人只能查看本校区的小班课程
//		Organization campus =  userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			miniClassCourseVo.setBlCampusName(campus.getName());
//		}
		
		dp = miniClassCourseDao.getMiniClassCourseList(miniClassCourseVo, dp);
		List<MiniClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassCourse>) dp.getDatas(), MiniClassCourseVo.class);
		List<MiniClassStudentAttendent> miniClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		for(MiniClassCourseVo vo : searchResultVoList){			
			//查询参与考勤的人数
			miniClassStudentAttendentList=miniClassStudentAttendentDao.findByCriteria(Expression.eq("miniClassCourse.miniClassCourseId", vo.getMiniClassCourseId()));
			
			//报名的所有学生
			studentCount=miniClassStudentDao.findCountHql("select count(*) from MiniClassStudent where miniClass.miniClassId='"+vo.getMiniClassId()+"' and firstSchoolTime <= '" + vo.getCourseDate() + "' ");
			
			vo.setStudentCount(studentCount);//报名的学生人数
			if(miniClassStudentAttendentList!=null){
				// 考勤人数
				attendanceCount=miniClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, lateNumber=0, leaveNumber=0, absentNumber=0, chargeNumber = 0,makeUp=0 ; // 未上课，已上课，迟到， 请假， 缺勤， 扣费 人数
				for(MiniClassStudentAttendent miniClassStudentAttendent : miniClassStudentAttendentList){
					if(MiniClassStudentChargeStatus.CHARGED.equals(miniClassStudentAttendent.getChargeStatus())){
						chargeNumber++;
					}
						
					if(MiniClassAttendanceStatus.NEW.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						newNumber++;
					}						
					else if(MiniClassAttendanceStatus.CONPELETE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						completeNumber++;
					}
					else if(MiniClassAttendanceStatus.LATE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						lateNumber++;
					}						
					else if(MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						leaveNumber++;
					}						
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						absentNumber++;
					}						
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
							&& MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus()) 
							&& miniClassStudentAttendent.getSupplementDate() != null){
						makeUp++; //补课人数   请假缺勤且补课日期不为空
					}
						
				}
				
				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setCompleteClassPeopleNum(completeNumber+lateNumber); // 已上课
				vo.setLateClassPeopleNum(lateNumber); //迟到人数
				vo.setCompleteAndLate(completeNumber+lateNumber+"（"+lateNumber+"）");
				vo.setLeaveClassPeopleNum(leaveNumber); // 请假
				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				//只要考勤过就已经结算了
				vo.setNoAttendanceCount(attendanceCount - chargeNumber); //未结算
//				vo.setNoAttendanceCount(attendanceCount-completeNumber-leaveNumber-absentNumber); // 未考勤
				vo.setMakeUp(makeUp);//补课
			}else{
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setNoAttendanceCount(studentCount); // 未考勤
				vo.setMakeUp(0);
			}
			
			
			
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}*/
	
	/**
	 * 获取小班课程列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage 	getMiniClassCourseList(MiniClassCourseVo miniClassCourseVo, DataPackage dp) {
		dp = miniClassCourseDao.getMiniClassCourseList(miniClassCourseVo, dp);
		List<MiniClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassCourseJdbc>) dp.getDatas(), MiniClassCourseVo.class);
		List<MiniClassStudentAttendent> miniClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		for(MiniClassCourseVo vo : searchResultVoList){	
			
			vo.setAuditStatusName(vo.getAuditStatus() == null ? null : vo.getAuditStatus().getName());			
			if (StringUtil.isBlank(vo.getBlCampusName()) && StringUtil.isNotBlank(vo.getMiniClassId())) {
				MiniClass mc = miniClassDao.findById(vo.getMiniClassId());
				vo.setBlCampusName(mc.getBlCampus().getName());
//				if (mc.getMiniClassType() != null) {
//					vo.setMiniClassType(mc.getMiniClassType().getName());
//				}
				if (mc.getProduct()!=null){
					vo.setMiniClassType(mc.getProduct().getClassType().getName());
				}
			}
			
			if (StringUtil.isBlank(vo.getGrade()) && StringUtil.isNotBlank(vo.getGradeId())) {
				DataDict gradeDict = dataDictDao.findById(vo.getGradeId());
				vo.setGrade(gradeDict.getName());
			}
			
			if (StringUtil.isBlank(vo.getSubject()) && StringUtil.isNotBlank(vo.getSubjectId())) {
				DataDict subjectDict = dataDictDao.findById(vo.getSubjectId());
				vo.setSubject(subjectDict.getName());
			}
			
			if ((StringUtil.isBlank(vo.getTeacherName()) || StringUtil.isBlank(vo.getTeacherMobile())) && StringUtil.isNotBlank(vo.getTeacherId())) {
				User teacher = userDao.findById(vo.getTeacherId());
				vo.setTeacherName(teacher.getName());
				vo.setTeacherMobile(teacher.getContact());
			}
			
			if ((StringUtil.isBlank(vo.getStudyManegerName()) ||  StringUtil.isBlank(vo.getStudyManegerMobile())) && StringUtil.isNotBlank(vo.getStudyManegerId())) {
				User studyManager = userDao.findById(vo.getStudyManegerId());
				vo.setStudyManegerName(studyManager.getName());
				vo.setStudyManegerMobile(studyManager.getContact());
			}
			
			//报名的所有学生
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("miniClassId", vo.getMiniClassId());
			params.put("courseDate", vo.getCourseDate());
			studentCount=miniClassStudentAttendentDao.findCountSql("select count(*) from mini_class_student where MINI_CLASS_ID = :miniClassId and FIRST_SCHOOL_TIME <= :courseDate ", params);
			//查询参与考勤的人数
			miniClassStudentAttendentList=miniClassStudentAttendentDao.findByCriteria(Expression.eq("miniClassCourse.miniClassCourseId", vo.getMiniClassCourseId()));
			
			vo.setStudentCount(studentCount);//报名的学生人数
			if(miniClassStudentAttendentList!=null){
				// 考勤人数
				attendanceCount=miniClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, lateNumber=0, absentNumber=0, chargeNumber = 0,makeUp=0 ; // 未上课，准时上课，迟到， 请假， 缺勤， 扣费 人数 , leaveNumber=0
				for(MiniClassStudentAttendent miniClassStudentAttendent : miniClassStudentAttendentList){
					if(MiniClassStudentChargeStatus.CHARGED.equals(miniClassStudentAttendent.getChargeStatus())){
						chargeNumber++;
					}

					if(MiniClassAttendanceStatus.NEW.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						newNumber++;
					}
					else if(MiniClassAttendanceStatus.CONPELETE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						completeNumber++;
					}
					else if(MiniClassAttendanceStatus.LATE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						lateNumber++;
					}
//					else if(MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
//						leaveNumber++;
//					}
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						absentNumber++;
					}
					if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
//							&& MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
							&& miniClassStudentAttendent.getSupplementDate() != null){
						makeUp++; //补课人数   请假缺勤且补课日期不为空
					}
				}

				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setCompleteClassPeopleNum(completeNumber); // 已 准时上课
				vo.setLateClassPeopleNum(lateNumber); //迟到人数
//				vo.setCompleteAndLate(completeNumber+lateNumber+"（"+lateNumber+"）");
//				vo.setLeaveClassPeopleNum(leaveNumber); // 请假
				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				//只要考勤过就已经结算了
//				vo.setNoAttendanceCount(attendanceCount - chargeNumber); //未结算
				vo.setNoAttendanceCount(attendanceCount-completeNumber-absentNumber-lateNumber); // 未考勤  -leaveNumber
				vo.setMakeUp(makeUp);//补课
			}else{
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
				vo.setLateClassPeopleNum(0); //迟到
//				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setNoAttendanceCount(studentCount); // 未考勤
				vo.setMakeUp(0);
			}
			
			
			
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}

	//手机端小班课程列表
	/*
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getMiniClassCourseListForMobile(
			MiniClassCourseVo miniClassCourseVo, DataPackage dp) {
		dp = miniClassCourseDao.getMiniClassCourseListForMobile(miniClassCourseVo, dp);
		List<MiniClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassCourse>) dp.getDatas(), MiniClassCourseVo.class);
		List<MiniClassStudentAttendent> miniClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		for(MiniClassCourseVo vo : searchResultVoList){			
			//查询参与考勤的人数
			miniClassStudentAttendentList=miniClassStudentAttendentDao.findByCriteria(Expression.eq("miniClassCourse.miniClassCourseId", vo.getMiniClassCourseId()));
			
			vo.setAuditStatusName(vo.getAuditStatus() == null ? null : vo.getAuditStatus().getName());
			//报名的所有学生
//			studentCount=miniClassStudentDao.findCountHql("select count(*) from MiniClassStudent where miniClass.miniClassId='"+vo.getMiniClassId()+"' and firstSchoolTime <= '" + vo.getCourseDate() + "' ");
			studentCount=miniClassStudentAttendentList.size();
			vo.setStudentCount(studentCount);//报名的学生人数
			if(miniClassStudentAttendentList!=null){
				// 考勤人数
				attendanceCount=miniClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, leaveNumber=0, absentNumber=0, chargeNumber = 0,makeUp=0,lateNumber=0 ; // 未上课，已上课， 请假， 缺勤， 扣费,迟到 人数
				for(MiniClassStudentAttendent miniClassStudentAttendent : miniClassStudentAttendentList){
					if(MiniClassStudentChargeStatus.CHARGED.equals(miniClassStudentAttendent.getChargeStatus())){
						chargeNumber++;
					}

					if(MiniClassAttendanceStatus.NEW.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						newNumber++;
					}
					else if(MiniClassAttendanceStatus.CONPELETE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						completeNumber++;
					}
					else if(MiniClassAttendanceStatus.LATE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						lateNumber++;
					}
					else if(MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						leaveNumber++;
					}
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						absentNumber++;
					}
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
							&& MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
							&& miniClassStudentAttendent.getSupplementDate() != null){
						makeUp++; //补课人数   请假缺勤且补课日期不为空
					}

				}
				
				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setLeaveClassPeopleNum(leaveNumber); // 请假
				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				vo.setNoAttendanceCount(attendanceCount-completeNumber-leaveNumber-absentNumber); // 未考勤
//				vo.setNoAttendanceCount(studentCount-attendanceCount); // 未考勤
				vo.setMakeUp(makeUp);//补课
				vo.setCompleteClassPeopleNum(completeNumber+lateNumber); // 已上课
				vo.setLateClassPeopleNum(lateNumber); //迟到人数
				vo.setCompleteAndLate(completeNumber+lateNumber+"（"+lateNumber+"）");
			}else{
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setNoAttendanceCount(studentCount); // 未考勤
				vo.setMakeUp(0);
				vo.setLateClassPeopleNum(0);//迟到
			}
			
			
			
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}*/
	
	//手机端小班课程列表

	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getMiniClassCourseListForMobile(
			MiniClassCourseVo miniClassCourseVo, DataPackage dp) {
		dp = miniClassCourseDao.getMiniClassCourseList(miniClassCourseVo, dp);
		List<MiniClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassCourseJdbc>) dp.getDatas(), MiniClassCourseVo.class);
		List<MiniClassStudentAttendent> miniClassStudentAttendentList = null;
		int studentCount = 0;//报名的学生人数
		int attendanceCount =0;//考勤人数
		for(MiniClassCourseVo vo : searchResultVoList){
			vo.setAuditStatusName(vo.getAuditStatus() == null ? null : vo.getAuditStatus().getName());

			if (StringUtil.isBlank(vo.getBlCampusName()) && StringUtil.isNotBlank(vo.getMiniClassId())) {
				MiniClass mc = miniClassDao.findById(vo.getMiniClassId());
				vo.setBlCampusName(mc.getBlCampus().getName());
				if (mc.getMiniClassType() != null) {
					vo.setMiniClassType(mc.getMiniClassType().getName());
				}
			}

			if (StringUtil.isBlank(vo.getGrade()) && StringUtil.isNotBlank(vo.getGradeId())) {
				DataDict gradeDict = dataDictDao.findById(vo.getGradeId());
				vo.setGrade(gradeDict.getName());
			}

			if (StringUtil.isBlank(vo.getSubject()) && StringUtil.isNotBlank(vo.getSubjectId())) {
				DataDict subjectDict = dataDictDao.findById(vo.getSubjectId());
				vo.setSubject(subjectDict.getName());
			}

			if ((StringUtil.isBlank(vo.getTeacherName()) || StringUtil.isBlank(vo.getTeacherMobile())) && StringUtil.isNotBlank(vo.getTeacherId())) {
				User teacher = userDao.findById(vo.getTeacherId());
				vo.setTeacherName(teacher.getName());
				vo.setTeacherMobile(teacher.getContact());
			}

			if ((StringUtil.isBlank(vo.getStudyManegerName()) ||  StringUtil.isBlank(vo.getStudyManegerMobile())) && StringUtil.isNotBlank(vo.getStudyManegerId())) {
				User studyManager = userDao.findById(vo.getStudyManegerId());
				vo.setStudyManegerName(studyManager.getName());
				vo.setStudyManegerMobile(studyManager.getContact());
			}

			if (StringUtil.isNotBlank(vo.getClassroomId())) {
				ClassroomManageVo classRoomManageVo = classroomManageService.findClassroomById(vo.getClassroomId());
				vo.setClassroom(classRoomManageVo.getClassroom());
			}

			//查询参与考勤的人数
			miniClassStudentAttendentList = miniClassStudentAttendentDao.findByCriteria(Expression.eq("miniClassCourse.miniClassCourseId", vo.getMiniClassCourseId()));
			studentCount=miniClassStudentAttendentList.size();
			vo.setStudentCount(studentCount);//报名的学生人数
			if(miniClassStudentAttendentList != null && studentCount > 0){
				// 考勤人数
				attendanceCount=miniClassStudentAttendentList.size();
				vo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, absentNumber=0, chargeNumber = 0,makeUp=0,lateNumber=0 ; // 未上课，已准时上课， 请假， 缺勤， 扣费,迟到 人数 , leaveNumber=0
				for(MiniClassStudentAttendent miniClassStudentAttendent : miniClassStudentAttendentList){
					if(MiniClassStudentChargeStatus.CHARGED.equals(miniClassStudentAttendent.getChargeStatus())){
						chargeNumber++;
					}

					if(MiniClassAttendanceStatus.NEW.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						newNumber++;
					}
					else if(MiniClassAttendanceStatus.CONPELETE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						completeNumber++;
					}
					else if(MiniClassAttendanceStatus.LATE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						lateNumber++;
					}
//					else if(MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
//						leaveNumber++;
//					}
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())){
						absentNumber++;
					}
					if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
//							&& MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
							&& miniClassStudentAttendent.getSupplementDate() != null){
						makeUp++; //补课人数   请假缺勤且补课日期不为空
					}
				}

				vo.setDeductionCount(chargeNumber);// 扣费人数
				vo.setNewClassPeopleNum(newNumber); // 设置 未上课
				vo.setCompleteClassPeopleNum(completeNumber+lateNumber); // 已上课
//				vo.setLeaveClassPeopleNum(leaveNumber); // 请假
				vo.setAbsentClassPeopleNum(absentNumber); // 缺勤
				vo.setNoAttendanceCount(attendanceCount-completeNumber-absentNumber); // 未考勤 -leaveNumber
//					vo.setNoAttendanceCount(studentCount-attendanceCount); // 未考勤
				vo.setMakeUp(makeUp);//补课
				vo.setLateClassPeopleNum(lateNumber); //迟到人数
//				vo.setCompleteAndLate(completeNumber+lateNumber+"（"+lateNumber+"）");
			} else {
				vo.setAttendanceCount(0);//考勤人数
				vo.setDeductionCount(0);// 扣费人数
				vo.setNewClassPeopleNum(0); // 设置 未上课
				vo.setCompleteClassPeopleNum(0); // 已上课
//				vo.setLeaveClassPeopleNum(0); // 请假
				vo.setAbsentClassPeopleNum(0); // 缺勤
				vo.setNoAttendanceCount(studentCount); // 未考勤
				vo.setMakeUp(0);
//				vo.setLateClassPeopleNum(0);//迟到
			}
			if (!vo.getCourseStatus().equals(CourseStatus.NEW.getValue())) {
				MoneyWashRecords moneyWashRecords = moneyWashRecordsService.findMoneyWashRecordsByMiniCourseId(vo.getMiniClassCourseId());
				if (moneyWashRecords != null) {
					vo.setIsWashed("TRUE");
					vo.setWashRemark(moneyWashRecords.getDetailReason());
				} else {
					vo.setIsWashed("FALSE");
				}
			} else {
				vo.setIsWashed("FALSE");
			}
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}
	
	/**
	 * 根据ID获取小班课程
	 */
	@Override
	public MiniClassCourseVo findMiniClassCourseById(String miniClassCourseId) {		
		if(StringUtil.isBlank(miniClassCourseId)){
			return new MiniClassCourseVo();
		}		
		MiniClassCourse miniClassCourse= miniClassCourseDao.findById(miniClassCourseId);
		if(miniClassCourse==null){
			return new MiniClassCourseVo();
		}		
		MiniClassCourseVo miniClassCourseVo = HibernateUtils.voObjectMapping(miniClassCourse, MiniClassCourseVo.class);
		//miniClassCourseVo.setCourseTime(miniClassCourseVo.getCourseTime() + " - " + miniClassCourseVo.getCourseEndTime());
		if(miniClassCourseVo!=null){		
			
			//查询参与考勤的人数
			List<MiniClassStudentAttendent> miniClassStudentAttendentList=miniClassStudentAttendentDao.findByCriteria(Expression.eq("miniClassCourse.miniClassCourseId", miniClassCourseVo.getMiniClassCourseId()));
			
			//报名的所有学生
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("miniClassId", miniClassCourseVo.getMiniClassId());
			params.put("courseDate", miniClassCourseVo.getCourseDate());
			int studentCount=miniClassStudentDao.findCountHql("select count(*) from MiniClassStudent where miniClass.miniClassId = :miniClassId and firstSchoolTime <= :courseDate ", params);
				
			miniClassCourseVo.setStudentCount(miniClassStudentAttendentList.size());//报名的学生人数
			if(miniClassStudentAttendentList!=null){
				// 考勤人数
				int attendanceCount=miniClassStudentAttendentList.size();//考勤人数
				miniClassCourseVo.setAttendanceCount(attendanceCount);
				int newNumber=0, completeNumber=0, absentNumber=0, chargeNumber = 0 ,makeUp=0,late=0; // 未上课，已准时上课， 请假， 缺勤， 扣费 人数,补课  , leaveNumber=0
				for(MiniClassStudentAttendent miniClassStudentAttendent : miniClassStudentAttendentList){
					if(MiniClassStudentChargeStatus.CHARGED.equals(miniClassStudentAttendent.getChargeStatus()))
						chargeNumber++;
					if(MiniClassAttendanceStatus.NEW.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus()))
						newNumber++;
					else if(MiniClassAttendanceStatus.CONPELETE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus()))
//							|| MiniClassAttendanceStatus.LATE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus()))// 上课人数 =已上课+迟到
						completeNumber++;
					else if(MiniClassAttendanceStatus.LATE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus()))
						late++;
					else if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus()))
						absentNumber++;
					if(MiniClassAttendanceStatus.ABSENT.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
//							&& MiniClassAttendanceStatus.LEAVE.equals(miniClassStudentAttendent.getMiniClassAttendanceStatus())
							&& miniClassStudentAttendent.getSupplementDate() != null){
						makeUp++; //补课人数   请假缺勤且补课日期不为空
					}
				}
				miniClassCourseVo.setDeductionCount(chargeNumber);// 扣费人数
				miniClassCourseVo.setNewClassPeopleNum(newNumber); // 设置 未上课
//				miniClassCourseVo.setCompleteClassPeopleNum(completeNumber); // 已上课
//				miniClassCourseVo.setLeaveClassPeopleNum(leaveNumber); // 请假
				miniClassCourseVo.setAbsentClassPeopleNum(absentNumber); // 缺勤
//				miniClassCourseVo.setNoAttendanceCount(studentCount-attendanceCount); // 未考勤
				miniClassCourseVo.setMakeUp(makeUp);//补课
				miniClassCourseVo.setLateClassPeopleNum(late);//迟到

				miniClassCourseVo.setCompleteClassPeopleNum(completeNumber+late); // 已上课
				miniClassCourseVo.setNoAttendanceCount(attendanceCount-completeNumber-absentNumber); // 未考勤 -leaveNumber
			}else{
				miniClassCourseVo.setAttendanceCount(0);//考勤人数
				miniClassCourseVo.setDeductionCount(0);// 扣费人数
				miniClassCourseVo.setNewClassPeopleNum(0); // 设置 未上课
				miniClassCourseVo.setCompleteClassPeopleNum(0); // 已上课
//				miniClassCourseVo.setLeaveClassPeopleNum(0); // 请假
				miniClassCourseVo.setAbsentClassPeopleNum(0); // 缺勤
				miniClassCourseVo.setNoAttendanceCount(studentCount); // 未考勤
				miniClassCourseVo.setMakeUp(0);
				miniClassCourseVo.setLateClassPeopleNum(0);
			}
			
			//获取小班里面主产品的课时时长
			MiniClass miniClass = miniClassDao.findById(miniClassCourseVo.getMiniClassId());
			Product product = miniClass.getProduct();
			miniClassCourseVo.setMainProductCourseTime(product.getClassTimeLength());
		}else{
			miniClassCourseVo=new MiniClassCourseVo();
		}
		
		if (!miniClassCourseVo.getCourseStatus().equals(CourseStatus.NEW.getValue())) {
			MoneyWashRecords moneyWashRecords = moneyWashRecordsService.findMoneyWashRecordsByMiniCourseId(miniClassCourseVo.getMiniClassCourseId());
			if (moneyWashRecords != null) {
				miniClassCourseVo.setIsWashed("TRUE");
				miniClassCourseVo.setWashRemark(moneyWashRecords.getDetailReason());
			} else {
				miniClassCourseVo.setIsWashed("FALSE");
			}
		} else {
			miniClassCourseVo.setIsWashed("FALSE");
		}
		
		return miniClassCourseVo;
	}

	/**
	 * 更新小班课程
	 * @param miniClassCourseVo
	 * @throws Exception
	 */
	@Override
	public void saveOrUpdateMiniClassCourse(MiniClassCourseVo miniClassCourseVo) throws Exception {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseVo.getMiniClassCourseId());

		if(miniClassCourseVo.getCourseHours()!=null && miniClassCourse.getCourseHours()!=null && miniClassCourseVo.getCourseHours()>miniClassCourse.getCourseHours()){
			Double alHours=miniClassCourseDao.findCountClassHours(miniClassCourse.getMiniClass().getMiniClassId());
			if(alHours-miniClassCourse.getCourseHours()+miniClassCourseVo.getCourseHours()>miniClassCourse.getMiniClass().getTotalClassHours()){
				throw  new ApplicationException("所排课时已经超过了小班的最大课时！");
			}

		}


		String newCourseDate=miniClassCourseVo.getCourseDate();
		String oldCourseDate=miniClassCourse.getCourseDate();
		//营收凭证如果已经审批完成就不能再排以前的课程了。
		try{
			if( ((miniClassCourseVo.getCancelStatus()!=null && !"cancel".equals(miniClassCourseVo.getCancelStatus())) ||  miniClassCourseVo.getCancelStatus() == null)
					&& DateTools.getDateSpace(DateTools.getFistDayofMonth(),miniClassCourseVo.getCourseDate())<0){
				odsMonthIncomeCampusService.isFinishAudit(miniClassCourse.getMiniClass().getBlCampus().getId(),DateTools.getDateToString(DateTools.getLastDayOfMonth(miniClassCourseVo.getCourseDate())));
			}
		}catch(ParseException e){
			e.printStackTrace();
		}

		//取消小班课程
		if(miniClassCourseVo.getCancelStatus() != null && miniClassCourseVo.getCancelStatus().equals("cancel")){
			if(!miniClassCourse.getCourseStatus().equals(CourseStatus.NEW) && !miniClassCourse.getCourseStatus().equals(CourseStatus.TEACHER_ATTENDANCE)){
				throw new ApplicationException("只有“未上课”和“老师已考勤”状态的课程才允许取消!");
			}
			this.updateMiniclassCourseStatus(miniClassCourseVo.getMiniClassCourseId(), CourseStatus.CANCEL);
			updateCourseNumByClassId(miniClassCourse.getMiniClass().getMiniClassId());//更新排序
			return;
		}
		
		if(miniClassCourse.getCourseStatus()!=null && !miniClassCourse.getCourseStatus().equals(CourseStatus.NEW) && !miniClassCourse.getCourseStatus().equals(CourseStatus.TEACHER_ATTENDANCE)){
			throw new ApplicationException("只有“未上课”和“老师已考勤”状态的课程才允许修改!");
		}
		
		if (StringUtils.isNotBlank(miniClassCourseVo.getCourseDate())) {
			miniClassCourse.setCourseDate(miniClassCourseVo.getCourseDate());
			this.deleteMiniClassStudentAttendentByMiniClassCourse(miniClassCourse);
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getCourseTime())) {
			miniClassCourse.setCourseTime(miniClassCourseVo.getCourseTime());
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getCourseEndTime())) {
			miniClassCourse.setCourseEndTime(miniClassCourseVo.getCourseEndTime());
		}
		if (miniClassCourseVo.getCourseHours() != null) {
			Double oldCourseHours=miniClassCourse.getCourseHours();
			Double newCourseHours=miniClassCourseVo.getCourseHours();
			if(!oldCourseHours.equals(newCourseHours)){
				String miniClasId=miniClassCourse.getMiniClass().getMiniClassId();
				if(miniClasId != null){
					MiniClass miniClass=miniClassDao.findById(miniClasId);
					miniClass.setArrangedHours(miniClass.getArrangedHours() - oldCourseHours + newCourseHours );
				}
				
			}
			miniClassCourse.setCourseHours(miniClassCourseVo.getCourseHours());
			
		}
		
		if (StringUtils.isNotBlank(miniClassCourseVo.getSubjectId())) {
			miniClassCourse.setSubject(new DataDict(miniClassCourseVo.getSubjectId()));
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getTeacherId())) {
			User teacher = new User();
			teacher.setUserId(miniClassCourseVo.getTeacherId());
			miniClassCourse.setTeacher(teacher);
		}
		
		if (miniClassCourseVo.getClassroomId() != null && miniClassCourseVo.getClassroomId().equals("")){
			miniClassCourse.setClassroom(null);
		}else{
			ClassroomManage c = new ClassroomManage();
			c.setId(miniClassCourseVo.getClassroomId());
			miniClassCourse.setClassroom(c);
		}

		if(StringUtil.isNotBlank(miniClassCourseVo.getCourseName())){
			miniClassCourse.setCourseName(miniClassCourseVo.getCourseName());
		}
		
		miniClassCourse.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		miniClassCourse.setModifyUserId(user.getUserId());
		if(miniClassCourse.getMiniClassCourseId()==null){
			miniClassCourse.setCreateTime(DateTools.getCurrentDateTime());
			miniClassCourse.setCreateUserId(user.getUserId());
		}
		
		if(miniClassCourse.getAuditStatus() == AuditStatus.UNVALIDATE){
			miniClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
		}
		miniClassCourseDao.save(miniClassCourse);
		log.info(miniClassCourse.getMiniClassCourseId()+",状态："+miniClassCourse.getCourseStatus());
		if(!newCourseDate.equals(oldCourseDate)){
			updateCourseNumByClassId(miniClassCourse.getMiniClass().getMiniClassId());//更新排序
		}
		miniClassCourseDao.flush();
        // 更新小班开课结课日期
		MiniClass miniClass = smallClassDao.findById((miniClassCourse.getMiniClass().getMiniClassId()));
		this.updateMiniClassMaxMinCourseDate(miniClass);
	}
	
	/**
	 * @author tangyuping
	 * 更新小班状态
	 * @param miniClassCourseId
	 * @param courseStatus
	 */
	public void updateMiniclassCourseStatus(String miniClassCourseId,CourseStatus courseStatus){
		MiniClassCourse course = miniClassCourseDao.findById(miniClassCourseId);
		MiniClass miniClass = smallClassDao.findById((course.getMiniClass().getMiniClassId()));
		if(courseStatus == CourseStatus.CANCEL){
			if(miniClass != null){
				miniClass.setCanceledHours(miniClass.getCanceledHours() + course.getCourseHours());
				miniClass.setArrangedHours(miniClass.getArrangedHours() > 0 ? miniClass.getArrangedHours() - course.getCourseHours() : 0);
			}
			courseConflictDao.deleteCourseConflictByCourseId(miniClassCourseId);
			course.setCourseNum(0);
		}
		course.setCourseStatus(courseStatus);
		miniClassCourseDao.save(course);
		log.info(course.getMiniClassCourseId()+",状态："+course.getCourseStatus());
		if (courseStatus == CourseStatus.CANCEL) {
		    miniClassCourseDao.flush();
		    // 更新小班开课结课日期
		    this.updateMiniClassMaxMinCourseDate(miniClass);
		}
	}
	
	/**
	 * 获取小班学生考勤列表
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public DataPackage getMiniClassStudentAttendentList(MiniClassStudentVo miniClassStudentVo, DataPackage dp) throws Exception {
		MiniClassCourse currMiniClassCourse =  miniClassCourseDao.findById(miniClassStudentVo.getMiniClassCourseId());
		miniClassStudentVo.setMiniClassId(currMiniClassCourse.getMiniClass().getMiniClassId());
//		miniClassStudentVo.setFirstSchoolTime(currMiniClassCourse.getCourseDate());
		
		//miniClassStudentVo.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.CHARGED.name());// 只取已扣费的学生
		dp = miniClassStudentDao.getMiniClassStudentList(miniClassStudentVo, dp);
		List<MiniClassStudentVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassStudent>) dp.getDatas(), MiniClassStudentVo.class, "withoutStudent");
		
		// 过滤未报名的学生
		//	searchResultVoList = filterNoSignStudent(currMiniClassCourse, searchResultVoList);
				
		
		List<MiniClassStudentAttendent> attendentList=miniClassStudentAttendentDao.getMiniClassAttendsByCourseId(miniClassStudentVo.getMiniClassCourseId());
		
		List<MiniClassStudentVo> returnList = new ArrayList<MiniClassStudentVo>();
		List<MiniClassStudentVo> zeroList = new ArrayList<MiniClassStudentVo>();		
						
		for(MiniClassStudentAttendent attendent:attendentList){
			int stuNum=0;
			for (MiniClassStudentVo eachVo:searchResultVoList){
				if(attendent.getStudentId().equals(eachVo.getStudentId())){
					stuNum+=1;
//					MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(miniClassStudentVo.getMiniClassCourseId(), eachVo.getStudent().getId());
					MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(miniClassStudentVo.getMiniClassCourseId(), eachVo.getStudentId());
					eachVo.setCourseDate(currMiniClassCourse.getCourseDate());// 设置考勤的课程日期
					
					// 设置考勤状态
					if (miniClassStudentAttendent != null) {
						eachVo.setMiniClassAttendanceStatus(miniClassStudentAttendent.getMiniClassAttendanceStatus().getValue());
						eachVo.setMiniClassStudentChargeStatus(miniClassStudentAttendent.getChargeStatus().getValue());
						eachVo.setAbsentRemark(miniClassStudentAttendent.getAbsentRemark());
						eachVo.setSupplementDate(miniClassStudentAttendent.getSupplementDate());
					} else {// 没有考勤记录的情况，初始化考勤状态为“未考勤”
						eachVo.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW.getValue());
						eachVo.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.UNCHARGE.getValue());
					}
					
					// 设置老师是否考勤状态
					if (miniClassStudentAttendent != null && miniClassStudentAttendent.getHasTeacherAttendance ()!= null) {// 设置老师是否已考勤字段
						eachVo.setHasTeacherAttendance(miniClassStudentAttendent.getHasTeacherAttendance());
					} else {
						eachVo.setHasTeacherAttendance(BaseStatus.FALSE);
					}

					if(eachVo.getIsFrozen() == 0){
						eachVo.setInClassStatus("退班中");
					}else {
						eachVo.setInClassStatus("在读");
					}
					
					if (eachVo.getContractProductRemainingAmount().compareTo(BigDecimal.ZERO) == 0 
							&& eachVo.getMiniClassAttendanceStatus() == MiniClassAttendanceStatus.NEW.getValue()) {
						zeroList.add(eachVo);
					} else {
						returnList.add( eachVo);
					}

				}
				

			}


			/**
			 * 现存在学生报读小班扣费之后退班，在小班考勤结算点开考勤看不到已扣费退班的学生，
			 * 不能以小班学生为主体来查询，
			 * 已考勤未结算的考勤记录在退班后会清空考勤记录表		
			 */			
			if(stuNum==0){
				MiniClassStudentVo stuVo=new MiniClassStudentVo();
				Student stu=studentDao.findById(attendent.getStudentId());
				stuVo.setId(attendent.getStudentId()+"X");//前端用到ID  ，如果为空的话，不能渲染，设置为学生ID
				stuVo.setStudentId(attendent.getStudentId());
				stuVo.setStudentName(stu.getName());
				stuVo.setCourseDate(currMiniClassCourse.getCourseDate());// 设置考勤的课程日期
				// 设置考勤状态
				if (attendent != null) {
					stuVo.setMiniClassAttendanceStatus(attendent.getMiniClassAttendanceStatus().getValue());
					stuVo.setMiniClassStudentChargeStatus(attendent.getChargeStatus().getValue());
					stuVo.setAbsentRemark(attendent.getAbsentRemark());
					stuVo.setSupplementDate(attendent.getSupplementDate());
				} else {// 没有考勤记录的情况，初始化考勤状态为“未考勤”
					stuVo.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW.getValue());
					stuVo.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.UNCHARGE.getValue());
				}
				// 设置老师是否考勤状态
				if (attendent != null && attendent.getHasTeacherAttendance ()!= null) {// 设置老师是否已考勤字段
					stuVo.setHasTeacherAttendance(attendent.getHasTeacherAttendance());
				} else {
					stuVo.setHasTeacherAttendance(BaseStatus.FALSE);
				}
				
				stuVo.setInClassStatus("已退班");
				returnList.add(stuVo);

			}
			
		}


		returnList.addAll(zeroList);
		
		for(MiniClassStudentVo vo :returnList) {

			BigDecimal consumeAmount = BigDecimal.ZERO;
			BigDecimal consumeHours = BigDecimal.ZERO;

			if (vo.getMiniClassId()==null){
				break;
			}

			List<AccountChargeRecords> recordList = accountChargeRecordsDao.getAccountChargeRecordsByMiniClassAndStudent(vo.getMiniClassId(), vo.getStudentId());
			for (AccountChargeRecords record : recordList) {
				consumeAmount = consumeAmount.add(record.getAmount());
				consumeHours=consumeHours.add(record.getQuality());
			}

			//精英班按科目分配的课时来计算
			List<PromiseClassSubject> list = promiseClassSubjectDao.findPromiseSubjectByClassIdAndStudentId(currMiniClassCourse.getMiniClass().getMiniClassId(), vo.getStudentId());
			if (list.size() > 0 && vo.getContractProductRemainingAmount()!=null && vo.getContractProductRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
				PromiseClassSubject pro = list.get(0);

				ContractProduct contractProduct = pro.getPromiseStudent().getContractProduct();

				BigDecimal price = pro.getPromiseStudent().getContractProduct().getPrice();
				BigDecimal dealDiscount = contractProduct.getDealDiscount();// 折扣
				BigDecimal amount = contractProduct.getRemainingAmount();// 剩余资金
				BigDecimal hour = BigDecimal.ZERO;// 剩余课时
				BigDecimal realPrice = price.multiply(dealDiscount);// 真实价格
				if (amount.compareTo(BigDecimal.ZERO) > 0) {
					int result = amount.compareTo(realPrice);
					if (result != -1) {// 剩余资金必须大于课程价格，不然剩余课时为零
						hour = amount.divide(realPrice,2,RoundingMode.HALF_UP);
					}
				}



//				double consumeHours=miniClassStudentAttendentDao.getSumAttendentHours(currMiniClassCourse.getMiniClass().getMiniClassId(),vo.getStudentId(),MiniClassStudentChargeStatus.CHARGED);
				BigDecimal surplusCourseHour = pro.getCourseHours().subtract(pro.getConsumeCourseHours());//分配课时-课消课时

				BigDecimal surplusFinance = BigDecimal.ZERO;
				if (promiseClassService.isYearAfter2018(contractProduct)){
					hour = hour.subtract(consumeHours);
					surplusFinance = pro.getCourseHours().multiply(price);
				}else {
					surplusFinance = surplusCourseHour.multiply(price);
				}
				if (!(hour.compareTo(surplusCourseHour)>0)){
					surplusFinance = amount;
				}

				if(vo.getContractProductRemainingAmount().compareTo(surplusFinance)>0) {
					vo.setContractProductRemainingAmount(surplusFinance);
				}
			}
		}
		dp.setDatas(returnList);
		dp.setRowCount(returnList.size());
		return dp;
	}
	
	public Boolean checkMiniClassCourseAttends(String miniClassCourseId,List inputList,MiniClassCourse miniClassCourse){
		
		List<MiniClassStudentAttendent> list = miniClassStudentAttendentDao.getMiniClassAttendsByCourseId(miniClassCourseId);
		
		List<MiniClassStudent> listStudent = miniClassStudentDao.getMiniClassStudent(miniClassCourse.getMiniClass().getMiniClassId());
		List<MiniClassStudentVo> volist = HibernateUtils.voListMapping(listStudent, MiniClassStudentVo.class,"withoutStudent");
		
		Map<String,BigDecimal> remainingHourMap=new HashMap<String,BigDecimal>();

		Map<String, ContractProductStatus> contractProductStatusMap = new HashMap<>();
		Map<String, Integer> contractProductFrozenMap = new HashMap<>();
		for (MiniClassStudentVo ms : volist) {
			remainingHourMap.put(ms.getStudentId(), ms.getContractProductRemainingAmount());
			contractProductStatusMap.put(ms.getStudentId(), ms.getContractProductStatus());
			contractProductFrozenMap.put(ms.getStudentId(),ms.getIsFrozen());
		}
		
		int mustChargeAll=0;//必须要扣费的人数
		
		for (MiniClassStudentAttendent msa : list) {
			Student stu=studentDao.findById(msa.getStudentId());
			// 同步数据库
			miniClassStudentAttendentDao.getHibernateTemplate().refresh(msa);

			if(contractProductFrozenMap.get(msa.getStudentId())!=null && contractProductFrozenMap.get(msa.getStudentId())==0){
				continue;
			}

			//合同产品有剩余资金 这个学生就可以扣费。并且扣费状态为未扣费。 合同产品为正常 和 开始上课
			if(remainingHourMap.get(msa.getStudentId())!=null && remainingHourMap.get(msa.getStudentId()).compareTo(BigDecimal.ZERO)>=0
					&& msa.getChargeStatus()==MiniClassStudentChargeStatus.UNCHARGE &&
					(contractProductStatusMap.get(msa.getStudentId()).equals(ContractProductStatus.NORMAL)||contractProductStatusMap.get(msa.getStudentId()).equals(ContractProductStatus.STARTED))){
//				并且学生状态不为毕业
				if(stu.getStatus() != StudentStatus.GRADUATION){
					mustChargeAll+=1;
				} else {
					String errMsg = "学生（"+stu.getName()+"）状态为“毕业”，相关课程操作已被禁止，请核实后再继续操作。";
					throw new ApplicationException(errMsg);
				}
			}
		}
		
		if(inputList.size()==mustChargeAll){//如果 提交扣费的人数跟 可以扣费的人数相等，就通过
			return true;
		}
		
		return false;
	}
	
	/**
	 * 更新小班学生考勤信息
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized Response modifyMiniClassCourseStudentAttendance(String miniClassCourseId, String attendanceData, String oprationCode) throws Exception {
		Response res = new Response();
		// 解析JSON
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, MiniClassStudentAttendentVo.class);
		List<MiniClassStudentAttendentVo> inputList =  (List<MiniClassStudentAttendentVo>)objectMapper.readValue(attendanceData, javaType);

		if(inputList.size()<=0){
			res.setResultCode(-2);
			res.setResultMessage("请选择要考勤学生的记录！");
			return res;
		}

		inputList = deleteChargeStudent(miniClassCourseId, inputList);

		
		// 是否有操作权限
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		
		if(miniClassCourse.getAuditStatus() == AuditStatus.UNVALIDATE){
			miniClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
		}
		
		
		//检测冲突
		this.miniClassCourseBeforAttendance(inputList,miniClassCourse.getTeacher().getUserId(), miniClassCourseId);
		
		if("charge".equals(oprationCode) && !checkMiniClassCourseAttends(miniClassCourseId, inputList, miniClassCourse)){
			res.setResultCode(-2);
 			res.setResultMessage("小班扣费需全员扣费，包括考勤状态为迟到缺勤的学生！");
 			return res;
		}
		
		permission4MiniClassCourseAttendanceOperation(miniClassCourse.getTeacher().getUserId(), miniClassCourse.getStudyHead().getUserId(), oprationCode);
		
		if (null == miniClassCourse.getClassroom() || null == miniClassCourse.getClassroom().getId()) {
			res.setResultCode(-1);
 			res.setResultMessage("小班课程需关联具体教室才可以考勤！"); 
 			return res;
		}
		
		
		String stuName="";
 	 		for (int i = 0; i < inputList.size(); i++) {
 				MiniClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);			
 				
 				// 小班扣费
 				if ("charge".equals(oprationCode)) {
 					// 获取 targe Contract product 
 					ContractProduct targetConPrd = null; 
 					MiniClass miniClass = miniClassCourse.getMiniClass();
 					MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClass.getMiniClassId(), eachInputStuAttendentVo.getStudentId());
 					targetConPrd = miniClassStudent.getContractProduct();
 					targetConPrd.getType();
 					// 获取到课时 和 小班Product
 					Double courseHour  = miniClassCourse.getCourseHours();
 					if(targetConPrd!=null && 
 							(targetConPrd.getStatus() == ContractProductStatus.STARTED
 							||	targetConPrd.getStatus() == ContractProductStatus.NORMAL)) {
 						BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
 						
 						// 旧数据 可能会是 price 价格为 0
 						BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
 						// 总价格 = 单价 * 课时 * 折扣
 						BigDecimal detel = price.multiply(targetConPrd.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));
 						Student stu=targetConPrd.getContract().getStudent();
 						if(remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
// 							updateContractAndStuViewForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
// 							doAfterForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
 						} else {
 							if(ProductType.ECS_CLASS==targetConPrd.getType()){
 								//目标班学生不参与判断
 							}else{
 								
 	 							stuName+=stu.getName()+",";
 							}
 						}
 						
 					}
 					
 				}
 			}
 	 		

 	 	    Boolean miniClassCharge = false;
 	 		if(StringUtils.isNotBlank(stuName)){
					throw new ApplicationException(""+stuName+" 剩余课时不足以执行本次扣费，请续费后再重新扣费");
 	 		}else{
 	 			// 循环设每个学生考勤
 	 	 		for (int i = 0; i < inputList.size(); i++) {
 	 				MiniClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);
 	 				
 	 				// 更新考勤记录
 	 				updateMiniClassAttendanceRecord(miniClassCourseId, eachInputStuAttendentVo, oprationCode);
 	 				
 	 				
 	 				// 小班扣费
 	 				if ("charge".equals(oprationCode)) {
 	 					boolean chargeResult = miniClassCourseCharge(miniClassCourseId, eachInputStuAttendentVo);

 	 					if(chargeResult){
                            changeConsumeCourseNum(miniClassCourse.getMiniClass().getMiniClassId(),eachInputStuAttendentVo.getStudentId(),miniClassCourse.getCourseHours());
                        }

 	 					miniClassCharge = chargeResult;
 	 					// 更新小班状态
 	 					if(chargeResult && (i == inputList.size() - 1)) {
 	 						//课程表添加考勤时间,只取第一次考勤时间
 	 						if(miniClassCourse.getFirstAttendTime() == null){
 	 							miniClassCourse.setFirstAttendTime(DateTools.getCurrentDateTime());
 	 						}
 	 						miniClassCourse.setTeacherAttendTime(DateTools.getCurrentDateTime());
 	 						smallClassDao.flush();
 	 						updateMiniClassStatus(miniClassCourseId);
 	 					}
 	 				}
 	 			}
 	 		}
 	 		
 	 		log.info("小班扣费" + miniClassCourseId + ": " + "按照用户角色更新小班的考勤记录" + "当前小班状态" +  miniClassCourse.getCourseStatus().getName());
 	 	
 	 	    //调用对方的接口 开辟一条线程调用对方的接口  同步更新点名考勤        by  xiaojinwang  20170216
 	 		final List<AttentanceInfoVo> list = new ArrayList<>();
 	 	 	AttentanceInfoVo attentanceInfoVo = null;
 	 	 	final Integer groupCode = PropertiesUtils.getIntValue("groupCode");
 	 	 	for(MiniClassStudentAttendentVo temp:inputList){
 	 	 		attentanceInfoVo = new AttentanceInfoVo();
 	 	 		attentanceInfoVo.setCourseId(miniClassCourseId);
 	 	 		attentanceInfoVo.setStudentId(temp.getStudentId());
 	 	 		attentanceInfoVo.setStatus(temp.getAttendanceType());
 	 	 		attentanceInfoVo.setGroupCode(groupCode);
 	 	 		list.add(attentanceInfoVo);
 	 	 	} 	 		
 	 	 	final String url = PropertiesUtils.getStringValue("attentanceInfo.url");
 	 	 	final String postContent =Json.toJson(list);
 	 	 	if(StringUtils.isNotBlank(url)){
 	 	 	 	cachedThreadPool.execute(new Runnable() {		
 	 				@Override
 	 				public void run() {
 	 					try {
 	 					  long timeBegin = System.currentTimeMillis();
 	  					  String response =HttpClientUtil.doPostJson(url, postContent);
 	  					  long timeEnd = System.currentTimeMillis();
 	  					  log.info("attentanceInfo-costtime:"+(timeEnd-timeBegin)+"ms");
 	  					  JSONObject object =JSON.parseObject(response);
 	  					  if(object!=null){
 	  						 if(!object.get("code").toString().equals("200")){
 	  							 //同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
 	  							JedisUtil.lpush("attentanceInfo".getBytes(), JedisUtil.ObjectToByte(list)); 
 	  						 }
 	  					  }
 	  						
 	 					} catch (RuntimeException e) {
 	 						//异常,同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
 	 						JedisUtil.lpush("attentanceInfo".getBytes(), JedisUtil.ObjectToByte(list)); 
 	 					}			
 	 				}
 	 			});
// 	 	 	 	cachedThreadPool.shutdown();
 	 	 	}
 	 	 	
 	 	 	//扣费了
 	 	 	if(miniClassCharge){
 	 	 		//通知教学平台
 	 	 		final String chargeNoticeUrl = PropertiesUtils.getStringValue("chargeNoticeUrl");
 	 	 		if(StringUtil.isNotBlank(chargeNoticeUrl)){
 	 	 	 		Map<String, String> params = new HashMap<>();
 	 	 	 		params.put("groupCode", Integer.toString(groupCode));
 	 	 	 		params.put("courseId", miniClassCourseId);
 	 	 	 	 	cachedThreadPool.execute(new Runnable() {		
 	 	 				@Override
 	 	 				public void run() {
 	 	 					try {
 	 	 					  long timeBegin = System.currentTimeMillis();
 	 	  					  String response =HttpClientUtil.doGet(chargeNoticeUrl, params);
 	 	  					  long timeEnd = System.currentTimeMillis();
 	 	  					  log.info("chargeInfo-costtime:"+(timeEnd-timeBegin)+"ms");
 	 	  					  JSONObject object =JSON.parseObject(response);
 	 	  					  if(object!=null){
 	 	  						 if(!object.get("code").toString().equals("200")){
 	 	  							 log.debug("chargeNoticeUrl-http-response-code:"+object.get("code"));
 	 	  							 //同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
 	 	  							JedisUtil.lpush("chargeInfo".getBytes(), JedisUtil.ObjectToByte(params)); 
 	 	  						 }
 	 	  					  }
 	 	  						
 	 	 					} catch (RuntimeException e) {
 	 	 						//异常,同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
 	 	 						JedisUtil.lpush("chargeInfo".getBytes(), JedisUtil.ObjectToByte(params)); 
 	 	 					}			
 	 	 				}
 	 	 			}); 	 	 			
 	 	 		}
 	 	 	}
  	 		
		return res;
	}


	private void changeConsumeCourseNum(String miniClassId,String studentId,Double courseHours){
        List<PromiseClassSubject>  list = promiseClassSubjectDao.findPromiseSubjectByClassIdAndStudentId(miniClassId,studentId);
        if(list.size()>0){
            PromiseClassSubject pcs = list.get(0);
            pcs.setConsumeCourseHours(pcs.getConsumeCourseHours().add(BigDecimal.valueOf(courseHours)));
        }
    }

	/**
	 * 排除已经扣费的
	 * @param miniClassCourseId
	 * @param inputList
	 * @return
	 */
	private List<MiniClassStudentAttendentVo> deleteChargeStudent(String miniClassCourseId, List<MiniClassStudentAttendentVo> inputList) {
		List<MiniClassStudentAttendentVo> resultList = new ArrayList<>();
		List<MiniClassStudentAttendent> list = miniClassStudentAttendentDao.getMiniClassAttendsByCourseId(miniClassCourseId);
		for (MiniClassStudentAttendent msa : list) {
			if (msa.getChargeStatus().equals(MiniClassStudentChargeStatus.UNCHARGE)){
				MiniClassStudent stu=miniclassStudentDao.getOneMiniClassStudent(msa.getMiniClassCourse().getMiniClass().getMiniClassId(),msa.getStudentId());
				if(stu!=null && stu.getContractProduct()!=null && stu.getContractProduct().getIsFrozen() == 0){//冻结的合同产品不扣费。
					continue;
				}
				for (MiniClassStudentAttendentVo m : inputList){
					if (m.getStudentId().equals(msa.getStudentId())){
						resultList.add(m);
					}
				}
			}
		}
		return resultList;
	}


	/**
	 * 更新小班学生补课时间
	 * @param miniClassCourseId
	 * @param studentId
	 * @param supplementDate
	 */
	public void modifyMiniClassCourseStudentSupplement(String miniClassCourseId, String studentId, String supplementDate) {
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao
				.getOneMiniClassStudentAttendent(miniClassCourseId,
						studentId);// 已有学生考勤记录
		if (null != miniClassStudentAttendent) {
			miniClassStudentAttendent.setSupplementDate(supplementDate);
			miniClassStudentAttendentDao.save(miniClassStudentAttendent);
		}
	}
	
	/**
	 * 更新小班学生缺勤备注
	 * @param miniClassCourseId
	 * @param studentId
	 * @param absentRemark
	 * @return
	 */
	public void modifyMiniClassCourseAbsentRemark(String miniClassCourseId, String studentId, String absentRemark) {
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao
				.getOneMiniClassStudentAttendent(miniClassCourseId,
						studentId);// 已有学生考勤记录
		if (null != miniClassStudentAttendent) {
			miniClassStudentAttendent.setAbsentRemark(absentRemark);
			miniClassStudentAttendentDao.save(miniClassStudentAttendent);
		}
	}
	
	/**
	 * 按照用户角色更新小班的考勤记录
	 */
	public void updateMiniClassAttendanceRecord(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode) {
		User user = userService.getCurrentLoginUser();
		
		// 根据用户角色更新小班考勤记录
		MiniClassStudentAttendent miniClassStudentAttendent = updateMiniClassAttendanceRecordByUserRole(miniClassCourseId, eachInputStuAttendentVo, oprationCode);
		
		// 修改人记录
		String currDateTime = DateTools.getCurrentDateTime();
		miniClassStudentAttendent.setModifyUser(user);
		miniClassStudentAttendent.setModifyTime(currDateTime);
		if (miniClassStudentAttendent.getId() == null) {
			miniClassStudentAttendent.setCreateUser(user);
			miniClassStudentAttendent.setCreateTime(currDateTime);			
		}		
		miniClassStudentAttendent.setAttendentUser(user);// 考勤操作人
		miniClassStudentAttendentDao.save(miniClassStudentAttendent);
		if(miniClassStudentAttendent.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.ABSENT)){// FIXME: 2017/3/13 MiniClassAttendanceStatus.LEAVE 原来是请假的看看怎么改
			//删除冲突表当前冲突记录
			courseConflictDao.deleteCourseConflictByCourseIdAndStudentId(miniClassStudentAttendent.getMiniClassCourse().getMiniClassCourseId(), miniClassStudentAttendent.getStudentId());
		
		}
		
		//课程表添加考勤时间,只取第一次考勤时间
		MiniClassCourse course = miniClassStudentAttendent.getMiniClassCourse();
		// 现在必须是所有学生一起考勤，更新考勤时间提取到外面
//		if(course.getFirstAttendTime() == null){
//			course.setFirstAttendTime(DateTools.getCurrentDateTime());
//		}
//		course.setTeacherAttendTime(DateTools.getCurrentDateTime());
		log.info("小班扣费" + miniClassCourseId + ": " + "按照用户角色更新小班的考勤记录" + "当前小班状态" +  course.getCourseStatus().getName());
//		miniClassStudentAttendentDao.flush();
//		miniClassStudentAttendentDao.getHibernateTemplate().refresh(miniClassStudentAttendent);
	}
	
	/**
	 * 按照用户角色更新小班的考勤记录
	 */
	public void updateMiniClassAttendanceRecordByUser(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode,String userId) {
		User user = userService.getCurrentLoginUser();
		if(user==null){
			user = userService.loadUserById(userId);
			if(user==null){
				throw new ApplicationException("更新考勤失败，找不到考勤操作人");
			}
		}
		// 根据用户角色更新小班考勤记录
		MiniClassStudentAttendent miniClassStudentAttendent = updateMiniClassAttendanceRecordByUserRole(miniClassCourseId, eachInputStuAttendentVo, oprationCode);
		
		// 修改人记录
		String currDateTime = DateTools.getCurrentDateTime();
		miniClassStudentAttendent.setModifyUser(user);
		miniClassStudentAttendent.setModifyTime(currDateTime);
		if (miniClassStudentAttendent.getId() == null) {
			miniClassStudentAttendent.setCreateUser(user);
			miniClassStudentAttendent.setCreateTime(currDateTime);			
		}		
		miniClassStudentAttendent.setAttendentUser(user);// 考勤操作人
		miniClassStudentAttendentDao.save(miniClassStudentAttendent);
		if(miniClassStudentAttendent.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.ABSENT)){// FIXME: 2017/3/13 MiniClassAttendanceStatus.LEAVE 原来是请假的看看怎么改
			//删除冲突表当前冲突记录
			courseConflictDao.deleteCourseConflictByCourseIdAndStudentId(miniClassStudentAttendent.getMiniClassCourse().getMiniClassCourseId(), miniClassStudentAttendent.getStudentId());
		
		}
		
		//课程表添加考勤时间,只取第一次考勤时间
		MiniClassCourse course = miniClassStudentAttendent.getMiniClassCourse();
		// 现在必须是所有学生一起考勤，更新考勤时间提取到外面
//		if(course.getFirstAttendTime() == null){
//			course.setFirstAttendTime(DateTools.getCurrentDateTime());
//		}
//		course.setTeacherAttendTime(DateTools.getCurrentDateTime());
		log.info("小班扣费" + miniClassCourseId + ": " + "按照用户角色更新小班的考勤记录" + "当前小班状态" +  course.getCourseStatus().getName());
//		miniClassStudentAttendentDao.flush();
//		miniClassStudentAttendentDao.getHibernateTemplate().refresh(miniClassStudentAttendent);
	}
	

	/**
	 * 根据用户角色更新小班考勤记录
	 * @param miniClassCourseId
	 * @param eachInputStuAttendentVo
	 * @param oprationCode
     * @return
     */
	public MiniClassStudentAttendent updateMiniClassAttendanceRecordByUserRole(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode) {
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao
				.getOneMiniClassStudentAttendent(miniClassCourseId,
						eachInputStuAttendentVo.getStudentId());// 已有学生考勤记录
		MiniClassAttendanceStatus newStatus = MiniClassAttendanceStatus.valueOf(eachInputStuAttendentVo.getAttendanceType());// 新的考勤状态

		if (newStatus == MiniClassAttendanceStatus.LEAVE){ // FIXME: 2017/3/15 等app改好统一改回来
			newStatus = MiniClassAttendanceStatus.LATE;
		}
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		String absentRemark = eachInputStuAttendentVo.getAbsentRemark();



		if ("attendance".equals(oprationCode)) {// 如果是考勤
			if (miniClassStudentAttendent != null) {// 更新考勤状态
				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
				if (( newStatus == MiniClassAttendanceStatus.ABSENT) && null != absentRemark) {//newStatus == MiniClassAttendanceStatus.LEAVE ||
					miniClassStudentAttendent.setAbsentRemark(absentRemark);
				} else {
					miniClassStudentAttendent.setAbsentRemark("");
					miniClassStudentAttendent.setSupplementDate(null);
				}
			} else {// 新建学生考勤
				miniClassStudentAttendent = new MiniClassStudentAttendent();
				miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
				miniClassStudentAttendent.setStudentId(eachInputStuAttendentVo.getStudentId());
				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
			}
			miniClassStudentAttendent.setHasTeacherAttendance(BaseStatus.TRUE);// 老师考勤
			//当前小班第一次考勤更新小班课程状态
			if(miniClassCourse.getCourseStatus() != null && miniClassCourse.getCourseStatus().equals(CourseStatus.NEW)){
				miniClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
			}
			miniClassCourse.setTeacherAttendTime(DateTools.getCurrentDateTime());


			//通知老师学管学生已来扣费    关闭dwr   2016-12-17
//			messageService.sendMessage(MessageType.SYSTEM_MSG, "老师小班课程考勤提醒", miniClassStudentAttendent.getMiniClassCourse().getTeacher().getName() + " 已对小班 "+miniClassStudentAttendent.getMiniClassCourse().getMiniClassName()+" 考勤，请及时核对扣费", MessageDeliverType.SINGLE, miniClassStudentAttendent.getMiniClassCourse().getMiniClass().getStudyManeger().getUserId());

		} else if ("charge".equals(oprationCode)) {// 如果是扣费
			if (miniClassStudentAttendent != null) {// 更新考勤状态
				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
				if (null != absentRemark) {
					miniClassStudentAttendent.setAbsentRemark(absentRemark);
				}
			} else {
				// 新建学生考勤
				miniClassStudentAttendent = new MiniClassStudentAttendent();
				miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
				miniClassStudentAttendent.setStudentId(eachInputStuAttendentVo.getStudentId());
				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.CHARGED);// 未扣费
			}
			miniClassStudentAttendent.setHasTeacherAttendance(BaseStatus.TRUE);// 老师考勤
		} else if ("change".equals(oprationCode)) { // 如果是修改
			if (miniClassStudentAttendent != null) {// 更新考勤状态
				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
				if (( newStatus == MiniClassAttendanceStatus.ABSENT) && null != absentRemark) {//newStatus == MiniClassAttendanceStatus.LEAVE ||
					miniClassStudentAttendent.setAbsentRemark(absentRemark);
				} else {
					miniClassStudentAttendent.setAbsentRemark("");
					miniClassStudentAttendent.setSupplementDate(null);
				}
			}
			} else {
			throw new ApplicationException("没有相关操作，请检查！");
		}




//		if (currLoginUserHasStudyManagerRole4MiniClassAndTeacherRole4MiniClassCourse(miniClassCourseId)) {// 即是班主任，又是老师
//			if (miniClassStudentAttendent != null) {// 更新考勤状态
//				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
//			} else {// 新建学生考勤
//				miniClassStudentAttendent = new MiniClassStudentAttendent();
//				miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
//				miniClassStudentAttendent.setStudentId(eachInputStuAttendentVo.getStudentId());
//				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
//				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
//			}
//			miniClassStudentAttendent.setHasTeacherAttendance(BaseStatus.TRUE);// 老师考勤
//		} else if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
//			if (miniClassStudentAttendent != null) {// 更新考勤状态
//				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
//			} else {// 新建学生考勤
//				miniClassStudentAttendent = new MiniClassStudentAttendent();
//				miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
//				miniClassStudentAttendent.setStudentId(eachInputStuAttendentVo.getStudentId());
//				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
//				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
//			}
//			miniClassStudentAttendent.setHasTeacherAttendance(BaseStatus.TRUE);// 老师考勤
//			
//			//通知老师学管学生已来扣费
//			messageService.sendMessage(MessageType.SYSTEM_MSG, "老师小班课程考勤提醒", miniClassStudentAttendent.getMiniClassCourse().getTeacher().getName() + " 已对小班 "+miniClassStudentAttendent.getMiniClassCourse().getMiniClassName()+" 考勤，请及时核对扣费", MessageDeliverType.SINGLE, miniClassStudentAttendent.getMiniClassCourse().getMiniClass().getStudyManeger().getUserId());
//		} else if ((userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER)
//				||  userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER_HEAD))) {
//			if (miniClassStudentAttendent != null) {// 更新考勤状态
//				miniClassStudentAttendent.setMiniClassAttendanceStatus(newStatus);
//			} else {// 学管不允许新建学生考勤，必须是老师新建学生考勤
//				throw new RuntimeException("学管(班主任)不允许新建学生考勤，必须是老师先考勤！");
//			}
//		} else {
//			throw new RuntimeException("您不是老师或者（班主任）学管，没有操作的权限！");
//		}
		return miniClassStudentAttendent;
	}

	
	/**
	 * 是否具有小班考勤或小班扣费的操作权限
	 * @throws Exception
	 */
	public void permission4MiniClassCourseAttendanceOperation(String miniClassCourseTeacherId, String miniClassCourseStudyManegerId, String oprationCode)  {
		
		if ("attendance".equals(oprationCode)) {
			// 如果是老师考勤，是不是课程指定的老师或学管
			if (!(userService.getCurrentLoginUser().getUserId().equals(miniClassCourseTeacherId)
					|| userService.getCurrentLoginUser().getUserId().equals(miniClassCourseStudyManegerId))){
				throw new ApplicationException("您不是当前小班课程指定的老师，不允许考勤！");
			}
		} else if ("charge".equals(oprationCode)) {
			//#5510 小班考勤即扣费
//			if (!userService.getCurrentLoginUser().getUserId().equals(miniClassCourseStudyManegerId)) {
//				throw new ApplicationException("您不是当前小班指定的(班主任)学管，不允许扣费！");
//			}
		} else {
			throw new ApplicationException("没有相关操作，请检查！");
		}
		
		
		
//		if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
//			
//		} else if ((userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER)
//				||  userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER_HEAD))) {
//		} else {
//			throw new RuntimeException("您不是老师或者不是学管，没有小班考勤或者小班扣费的操作权限！");
//		}
	}
	
	/**
	 * 更新小班状态
	 */
	@Override
	public void updateMiniClassStatus(String miniClassCourseId) {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		MiniClass miniClass = smallClassDao.findById((miniClassCourse.getMiniClass().getMiniClassId()));
		
		if (null == miniClass.getConsume()) {
			miniClass.setConsume(0.0);
		}
		
		//设置扣费时间
		miniClassCourse.setStudyManageChargeTime(DateTools.getCurrentDateTime());
		
		// 第一次上课则更新小班状态
		if(miniClass.getConsume() == 0){
			miniClass.setStatus(MiniClassStatus.STARTED);
		}
		if(miniClassCourse.getCourseStatus()!=null) {
			miniClassCourse.setCourseStatus(CourseStatus.CHARGED);
			miniClassCourseDao.save(miniClassCourse);
			miniClassCourseDao.flush();
			log.info(miniClassCourse.getMiniClassCourseId()+",状态："+miniClassCourse.getCourseStatus());
		}
		log.info("小班扣费" + miniClassCourseId + ": " + "更新小班状态" + "当前小班状态" +  miniClassCourse.getCourseStatus().getName());
		
		// 更新已上课时
		miniClass.setConsume(miniClassStudentAttendentDao.findConsumeCount(miniClassCourse));
		// 最后一次上课  小班是固定课时且消耗课时等于总课时数 结班
		//#1733 固定课时和延时课时上完后都要修改下班状态
		/*if(MiniClassCourseType.CONSTANT.equals(miniClass.getMiniClassCourseType()) && miniClass.getConsume().equals(miniClass.getTotalClassHours())){
			miniClass.setStatus(MiniClassStatus.CONPELETE);
		}*/
		if(miniClass.getConsume().equals(miniClass.getTotalClassHours())){
			miniClass.setStatus(MiniClassStatus.CONPELETE);
		}
		smallClassDao.save(miniClass);
		smallClassDao.flush();
	}
	
	/**
	 * 更新小班消耗课时
	 * @param miniClassCourseId
	 */
	public void updateMiniClassConsume(String miniClassCourseId) {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		MiniClass miniClass = smallClassDao.findById((miniClassCourse.getMiniClass().getMiniClassId()));
		//#1733 先更新已上课时再判断是否完成
		miniClass.setConsume(miniClassStudentAttendentDao.findConsumeCount(miniClassCourse));
		// 更新已上课时
		//#1733 固定课时和延时课时上完后都要修改下班状态
		/*if(MiniClassCourseType.CONSTANT.equals(miniClass.getMiniClassCourseType()) && miniClass.getConsume().equals(miniClass.getTotalClassHours())){
			miniClass.setStatus(MiniClassStatus.CONPELETE);
		}*/
		//#1733 冲销后修改下班状态
		if(miniClass.getConsume().equals(miniClass.getTotalClassHours())){
			miniClass.setStatus(MiniClassStatus.CONPELETE);
		}else if(miniClass.getConsume() < miniClass.getTotalClassHours() && miniClass.getConsume()>0  ) {
			miniClass.setStatus(MiniClassStatus.STARTED);
		}else {
			miniClass.setStatus(MiniClassStatus.PENDDING_START);
		}
		smallClassDao.save(miniClass);
	}
	
	
	
	/**
	 * 小班扣费
	 * @param miniClassCourseId
	 * @throws Exception
	 */
	@Override
	public boolean miniClassCourseCharge(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo) throws Exception {
		
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao
				.getOneMiniClassStudentAttendent(miniClassCourseId,
						eachInputStuAttendentVo.getStudentId());// 已有学生考勤记录
		
		//考勤扣费一起做
//		if (null == miniClassStudentAttendent) {
//			throw new RuntimeException("该学生考勤记录为空，请检查！");
//		}
//		if (miniClassStudentAttendent.getHasTeacherAttendance() != BaseStatus.TRUE ) {
//			throw new ApplicationException("老师未考勤，不允许扣费！");
//		}
//		if (!miniClassCourseHasStuAttendentRecord(miniClassStudentAttendent.getId())) {
//			throw new RuntimeException("未有该学生的考勤记录，不允许扣费！");// 数据库里是否已经有考勤记录, 已有考勤记录才允许扣费
//		}
		
		// 查询小班学管
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
					

//		if ((userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER)
//				||  userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER_HEAD))
//				&& userService.getCurrentLoginUser().getUserId().equals(miniClassCourse.getStudyHead().getUserId()) ) {
			
				// 小班扣费
        //#5510 小班考勤即扣费
			if (MiniClassStudentChargeStatus.UNCHARGE.equals(miniClassStudentAttendent.getChargeStatus()) &&
					chargeService.chargeMiniClass(miniClassStudentAttendent.getId(), userService.getCurrentLoginUser())
					 ) {
				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.CHARGED);
				return true;
			} else {
				//throw new RuntimeException("小班扣费失败！");
				throw new ApplicationException(ErrorCode.MIN_CLASS_MONEY_NOT_ENOUGH);
			}
//		}
		
//		return false;
	}

	/**
	 * 小班扣费
	 * @param miniClassCourseId
	 * @throws Exception
	 */
	@Override
	public boolean miniClassCourseCharge_Edu(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo,User user) throws Exception {
		
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao
				.getOneMiniClassStudentAttendent(miniClassCourseId,
						eachInputStuAttendentVo.getStudentId());// 已有学生考勤记录			
				// 小班扣费
        //#5510 小班考勤即扣费
			if (MiniClassStudentChargeStatus.UNCHARGE.equals(miniClassStudentAttendent.getChargeStatus()) &&
					chargeService.chargeMiniClass_Edu(miniClassStudentAttendent.getId(), user)
					 ) {
				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.CHARGED);
				return true;
			} else {
				//throw new RuntimeException("小班扣费失败！");
				throw new ApplicationException(ErrorCode.MIN_CLASS_MONEY_NOT_ENOUGH);
			}

	}
	
	
	/**
	 * 用户是否具有双重角色，又是小班班主任，又是小班课程老师
	 * @param miniClassCourseId
	 * @return
	 */
	public boolean currLoginUserHasStudyManagerRole4MiniClassAndTeacherRole4MiniClassCourse(String miniClassCourseId) {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		MiniClass miniClass = smallClassDao.findById(miniClassCourse.getMiniClass().getMiniClassId());
		String currMiniClassCourseTeacherId = miniClassCourse.getTeacher().getUserId();
		String currMiniClassStudyMangerId = miniClass.getStudyManeger().getUserId();
		
		if (userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER) && userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {// 当前登录用户即是学管，又是老师
			if (userService.getCurrentLoginUser().getUserId().equals(currMiniClassStudyMangerId) && userService.getCurrentLoginUser().getUserId().equals(currMiniClassCourseTeacherId)) {// 当前用户即是小班指定的班主任，又是小班课程指定的老师
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 是否已经有该学生的考勤记录
	 * @param miniClassStudentAttendentId
	 * @return
	 */
	@Override
	public boolean miniClassCourseHasStuAttendentRecord(String miniClassStudentAttendentId) {
		boolean hasAttendentRecord = false;// 是否已有考勤记录
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.findById(miniClassStudentAttendentId);
		if ( miniClassStudentAttendent.getMiniClassAttendanceStatus()  != null) {
			hasAttendentRecord = true;// 已有考勤记录
		} else {
			hasAttendentRecord = false;// 未有考勤记录
		}
		return hasAttendentRecord;
	}
	
	/**
	 * 小班学生预扣费
	 * @param miniClassId
	 * @throws Exception
	 */
	@Override
	public void miniClassStudentPreCharge(String miniClassId, String stuIds) throws Exception {
		MiniClass currMiniClass = smallClassDao.findById(miniClassId);
		if (null == currMiniClass) {
			throw new ApplicationException("查询不到该小班，也许是小班已删除！");
		}
		
		String []studentArray = stuIds.split(",");
		for (String studentId:studentArray) {
			MiniClassStudent oneMiniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClassId, studentId);
			if (MiniClassStudentChargeStatus.UNCHARGE == oneMiniClassStudent.getMiniClassStudentChargeStatus()) {
				User user = userService.getCurrentLoginUser();
				boolean chargeStatus = chargeService.chargeToMiniClassAccount(miniClassId, studentId, user);// 小班预扣费
				if (chargeStatus) {
					oneMiniClassStudent.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.CHARGED);
					if(MiniClassStatus.PENDDING_START == oneMiniClassStudent.getMiniClass().getStatus()){// 未开课
						oneMiniClassStudent.getMiniClass().setStatus(MiniClassStatus.STARTED);// 已上课
						smallClassDao.save(oneMiniClassStudent.getMiniClass());
					}
				} else {
					throw new ApplicationException("扣费失败，也许是余额不足, 或者是合同产品分配金额不对等！");
				} 
			} else if (MiniClassStudentChargeStatus.CHARGED == oneMiniClassStudent.getMiniClassStudentChargeStatus()) {
				throw new ApplicationException("该学生已扣费！");
			} else {
				throw new ApplicationException("未知的状态描述，请检查！");
			}
		}
	}
	
	/**
	 * 过滤未报名的学生
	 */
	public List<MiniClassStudentVo> filterNoSignStudent(
			MiniClassCourse currMiniClassCourse,
			List<MiniClassStudentVo> searchResultVoList) throws Exception {

		// 初始化返回列表
		List<MiniClassStudentVo> returnMiniClassStudentVoList = new ArrayList<MiniClassStudentVo>();
		// 建立数组， 按日期排序
//		List<MiniClassCourse> miniClassCourses = miniClassCourseDao
//				.getMiniClassCourseListByMiniClassId(currMiniClassCourse
//						.getMiniClass().getMiniClassId());// 这里一定要按课程时间倒序
		// 每节课 是多少课时
		Double courseHour = currMiniClassCourse.getCourseHours();
		
		// 这节课， 按照时间， 下标
//		int targetMiniClassIndex = 0;
//		for(MiniClassCourse miniClassCourse: miniClassCourses) {
//			if(miniClassCourse.getMiniClassCourseId().equals(currMiniClassCourse.getMiniClassCourseId())) {
//				break;
//			} else {
//				targetMiniClassIndex ++;
//			}
//		}
		// 循环读取学生
		
		//		
//
//		// 现在是倒数第几节课
//		Integer currCourseReciprocalIndex = null;
//		for (int n = 0; n < miniClassCourses.size(); n++) {
//			MiniClassCourse miniClassCourse = miniClassCourses.get(n);
//			if (miniClassCourse.getMiniClassCourseId().equals(
//					currMiniClassCourse.getMiniClassCourseId())) {
//				currCourseReciprocalIndex = n + 1;
//			}
//		}

		// 循环所有学生，开始过滤
		// 		每个学生读取开始上课时间, 上课节数
		//		从上课时间， 要判断出从第几节课开始上课
		for (MiniClassStudentVo student : searchResultVoList) {// 循环所有学生
			
//			List<Contract> contracts = contractDao.getOrderContracts(student.getStudent().getId(), Order.desc("createTime"));// 取学生合同
//			String firstSchoolTime =  student.getFirstSchoolTime();
//			// 取学生的开始的上课时间 是在哪一节课
//			int firstSchoolClassIndex = 0;
//			for(MiniClassCourse miniClassCourse: miniClassCourses) {
//				if(DateTools.getDate(miniClassCourse.getCourseDate()).compareTo(DateTools.getDate(firstSchoolTime )) >=0  ) {
//					break;
//				} else {
//					firstSchoolClassIndex++;
//				}
//			}
			// 获取剩余的课时
			if (StringUtils.isNotBlank(student.getContractProductId())) {
				ContractProduct miniConPrd = contractProductDao.findById(student.getContractProductId());
				// Double remainingCourse = (miniConPrd.getPaidAmount().doubleValue() - miniConPrd.getConsumeAmount().doubleValue()) / (miniConPrd.getPrice().doubleValue() * miniConPrd.getDealDiscount().doubleValue() * courseHour);
				// 本节课 要大于 开始上课日期， 并且 剩余课时大于0
				BigDecimal remainingAmount = miniConPrd.getRemainingAmount();
				if( remainingAmount.compareTo(BigDecimal.ZERO)>0) {
					returnMiniClassStudentVoList.add(student);
				}else{
					MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(currMiniClassCourse.getMiniClassCourseId(), student.getId());
					if(miniClassStudentAttendent!=null)
						returnMiniClassStudentVoList.add(student);
				}
			}
			
			// 找到有报读这个产品的contract
//			B:for(Contract contract : contracts) {// 循环该学生所有合同
//				for(ContractProduct contractProduct : contract.getContractProducts()) {// 循环所有合同产品
//					if (null == contractProduct) {
//						throw new ApplicationException("关联的合同产品数据为空");
//					}
//					
//					// 小班关联产品和合同关联产品是否一致
//					if(contractProduct.getProduct().getId().equals(currMiniClassCourse.getMiniClass().getProduct().getId())) {
//			
//						if (null == contract.getMiniClassTotalAmount()
//								|| contract.getMiniClassTotalAmount().doubleValue() <= 0) {
//							throw new ApplicationException("请检查合同（" + contract.getId()
//									+ "）中的小班消费总额数据，该数据为空或小于等于零");
//						}
//						if (null == contractProduct.getPrice()
//								|| contractProduct.getPrice().doubleValue() <= 0) {
//							throw new ApplicationException("请检查合同产品（"
//									+ contractProduct.getId()
//									+ "）的单价数据，该数据为空或小于等于零");
//						}
//						
//						// 获取该学生的剩余
//						
//						// 该学生是否已经报了当前课程
////						Double quantity = contractProduct.getQuantity().doubleValue();// 课时数量
////						Integer studentClassNum = (int) (quantity / currMiniClassCourse
////								.getMiniClass().getEveryCourseClassNum());// 当前学生报名了几节课
////						if (studentClassNum >= currCourseReciprocalIndex) {// 报名的节数小于当前节数
////							if (ContractProductStatus.FROZEN == contractProduct.getStatus()) {
////								throw new RuntimeException(student.getStudentName()+"的合同已冻结，请取消对于该学生的操作！");
////							}
//						if(contractProduct.getPaidAmount().compareTo(BigDecimal.ZERO)==1){
//							student.setContractProductStatus(contractProduct.getStatus());// 设置合同状态
//							returnMiniClassStudentVoList.add(student);
//						}
////						}
//						break B;
//					}
//				}
			
		}

		return returnMiniClassStudentVoList;
	}

	public MiniClass getMiniClassByName(String name,String blCampus){
		List<MiniClass> list=smallClassDao.findByCriteria(Expression.eq("name", name),Expression.eq("blCampus.id", blCampus));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateMiniClassStudentfirstSchoolTime(String studentId,String smallClassId,String firstSchoolTime) {
		MiniClassStudent miniClassStudent=miniClassStudentDao.getOneMiniClassStudent(smallClassId, studentId);
		if(miniClassStudent!=null){
			miniClassStudent.setFirstSchoolTime(firstSchoolTime);
			miniClassStudentDao.save(miniClassStudent);
			miniClassStudentAttendentDao.deleteMiniClassStudentAttendentByStudentAndMiniClass(studentId, smallClassId);
			this.addMiniClassStudentAttendents(studentId, smallClassId, firstSchoolTime);
			this.deleteCourseConflict(studentId, smallClassId, firstSchoolTime);
			this.addCourseConflict(studentId, smallClassId, firstSchoolTime);
		}
	}
	
	/**
	 * @author tangyuping
	 * 删除对应冲突校验记录，在firstSchoolTime之前的记录
	 */
	public void deleteCourseConflict(String studentId,String smallClassId,String firstSchoolTime){
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String time = sdf.format(DateTools.stringConversDate(firstSchoolTime, "yyyy-MM-dd"));		
		String sql="DELETE FROM course_conflict where mini_class_id = :smallClassId and student_id = :studentId and SUBSTR(start_time,1,8) < :time "
				+ "and course_id in (select MINI_CLASS_COURSE_ID from mini_class_course where COURSE_STATUS = 'NEW') ";
		params.put("smallClassId", smallClassId);
		params.put("studentId", studentId);
		params.put("time", time);
		smallClassDao.excuteSql(sql, params);
	}
	
	/**
	 * @author tangyuping
	 * 添加对应冲突校验记录，在firstSchoolTime之前的记录
	 */
	public void addCourseConflict(String studentId,String smallClassId,String firstSchoolTime){
		MiniClass miniClass = smallClassDao.findById(smallClassId);
		Student student = studentDao.findById(studentId);
		DataPackage dp = new DataPackage(0, 999);
		dp = miniClassCourseDao.getMiniClassCourseListByMiniClassId(smallClassId, firstSchoolTime, dp);
		List<MiniClassCourse> miniClassCourseList = (List<MiniClassCourse>) dp.getDatas();
		for (MiniClassCourse miniClassCourse: miniClassCourseList) {
			CourseConflict courseConflict = this.getOneCourseConflict(studentId, miniClassCourse.getMiniClassCourseId());
			if (courseConflict == null) {
				CourseConflict conflict = new CourseConflict();
				
				String courseDate = miniClassCourse.getCourseDate();			
				String startTime = courseDate.replace("-", "") + miniClassCourse.getCourseTime().substring(0, 2) + miniClassCourse.getCourseTime().substring(3, 5);
				String endTime = courseDate.replace("-", "") + miniClassCourse.getCourseEndTime().substring(0, 2) + miniClassCourse.getCourseEndTime().substring(3, 5);

				conflict.setCourseId(miniClassCourse.getMiniClassCourseId());
				conflict.setMiniClass(miniClass);
				conflict.setBlCampusId(student.getBlCampusId());
				conflict.setStudent(student);
				conflict.setStartTime(Long.valueOf(startTime));
				conflict.setEndTime(Long.valueOf(endTime));
				conflict.setTeacher(miniClassCourse.getTeacher());
				courseConflictDao.save(conflict);
			}
			}
		
		
	}
	
	/**
	 * @author tangyuping
	 * 根据课程和学生查询对应的冲突信息
	 * 
	 */
	public CourseConflict getOneCourseConflict(String stuId, String courseId){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select * from course_conflict where course_id = :courseId and student_id = :stuId ";
		params.put("courseId", courseId);
		params.put("stuId", stuId);
		List<CourseConflict> list=courseConflictDao.findBySql(sql, params);
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	} 
	
	/**
	 * 增加对应小班的对应学生在课程上课时间再firstSchoolTime之后的考勤记录
	 * @param studentId
	 * @param smallClassId
	 * @param firstSchoolTime
	 */
	private void addMiniClassStudentAttendents(String studentId,String smallClassId,String firstSchoolTime) {
		User user = userService.getCurrentLoginUser();
		DataPackage dp = new DataPackage(0, 999);
		dp = miniClassCourseDao.getMiniClassCourseListByMiniClassId(smallClassId, firstSchoolTime, dp);
		List<MiniClassCourse> miniClassCourseList = (List<MiniClassCourse>) dp.getDatas();
		for (MiniClassCourse miniClassCourse: miniClassCourseList) {
			MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(miniClassCourse.getMiniClassCourseId(),studentId);
			if (miniClassStudentAttendent == null) {
				miniClassStudentAttendent = new MiniClassStudentAttendent();
				miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
				miniClassStudentAttendent.setStudentId(studentId);
				miniClassStudentAttendent.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW); // 未上课
				miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
				miniClassStudentAttendent.setCreateUser(user);
				miniClassStudentAttendent.setCreateTime(DateTools.getCurrentDateTime());
				miniClassStudentAttendentDao.save(miniClassStudentAttendent);
				if (miniClassCourse.getCourseStatus() == CourseStatus.CHARGED) {
					// 异步处理发消息
					this.sendJoinMiniCourseMsg(miniClassCourse.getMiniClassCourseId(), studentId);
				}
			}
		}
	}
	
	/**
	 * 小班课程学生插班
	 * @param miniCourseId
	 * @param studentId
     */
	private void sendJoinMiniCourseMsg(String miniCourseId, String studentId){
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendJoinMiniCourseMsgThread thread = new SendJoinMiniCourseMsgThread(miniCourseId, studentId, currentUserId);
		taskExecutor.execute(thread);
	}
	
	@Override
	public MiniClassVo getMiniClassByContractProductId(String contractProductId) {
		List<MiniClassStudent> list = miniClassStudentDao.findByCriteria(Expression.eq("contractProduct.id", contractProductId));
		if(list!=null && list.size()>0){
			MiniClass miniClass=list.get(0).getMiniClass();
			MiniClassVo miniClassVo=HibernateUtils.voObjectMapping(miniClass, MiniClassVo.class);
			List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
			StudentCriterionList.add(Expression.eq("miniClass.miniClassId", miniClass.getMiniClassId()));
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			miniClassVo.setNextMiniClassTime(miniClassCourseDao.getTop1MiniClassCourseByDate(date,miniClass.getMiniClassId()));
			miniClassVo.setMiniClassPeopleNum(miniClassStudentDao.findCountByCriteria(StudentCriterionList));
			return miniClassVo;
		}
		return null;
	}

	/**
	 * 获取小班课程时间段内占用教室列表
	 */
	@Override
	public String getMiniClassCourseUseClassroomListString(String blCampusId,String startDate, String endDate, String classroomName, String miniClassName) {
		StringBuffer classroom = new StringBuffer();
		List<Map> list = miniClassCourseDao.getMiniClassCourseUseClassroomList(blCampusId,startDate, endDate, classroomName, miniClassName);
		boolean firstObject = true;
		for (Map miniClass:list) {
			String classroomId = (String) miniClass.get("classroom");
			String value = classroomManageService.findClassroomById(classroomId).getClassroom(); 
			if (StringUtils.isNotBlank(value)) {
				if (!firstObject) {
					classroom.append(",");
				} else {
					firstObject = false;
				}
				classroom.append(value);
			}
		}
		return classroom.toString();
	}
	
	/**
	 * 获取小班课程
	 */
	@Override
	public List getClassroomMiniClassCourse(String startDate, String endDate, String classroomName, String miniClassName,String campusId,String miniClassTypeId) {
		List voMapList = new ArrayList<Map>();
		List<Map> list = miniClassCourseDao.getClassroomMiniClassCourse(startDate, endDate, classroomName, miniClassName,campusId,miniClassTypeId);
		for (Map map:list) {
			Map voMap = new HashMap();
			voMap.putAll(map);
//			List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
//			StudentCriterionList.add(Expression.eq("miniClass.miniClassId", map.get("MINI_CLASS_ID")));
//			voMap.put("PEOPLE_QUANTITY", miniClassStudentDao.findCountByCriteria(StudentCriterionList));
			List<MiniClassStudentAttendent> mcsaList = courseService.getMiniClassStudentAttendentById(map.get("MINI_CLASS_COURSE_ID").toString());
			int arrivals = 0; //准时上课
			int promisers = 0;
			int nonono = 0;//未上课
			int late = 0;//迟到
//			int leave = 0;//请假
			int absent = 0;//缺勤
			int paid = 0;//结算
//			StringBuffer hql = new StringBuffer();
//			hql.append(" from MiniClassStudent  where 1=1 ");
//			hql.append(" and id.miniClassId = '" + map.get("MINI_CLASS_ID") + "'");
//			hql.append(" and firstSchoolTime <= '" + map.get("COURSE_DATE") + "'");
//			
//			List<MiniClassStudent> classStudent = miniClassStudentDao.findAllByHQL(hql.toString());
			
//			promisers=classStudent.size();//应到人数
			
			int crashInd=0;
			for(MiniClassStudentAttendent mcsa : mcsaList){
				promisers++;
				if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.CONPELETE)){
					arrivals++;
				}
				else if(mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.LATE)){
					late++;
				}
				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.NEW))
					nonono++;	
//				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.LEAVE))
//					leave++;
				else if (mcsa.getMiniClassAttendanceStatus().equals(MiniClassAttendanceStatus.ABSENT))
					absent++;
				
				if(mcsa.getChargeStatus().equals(MiniClassStudentChargeStatus.CHARGED)){
					paid++;
				}
			}
			MiniClassCourse miniClassCourse=miniClassCourseDao.findById(voMap.get("MINI_CLASS_COURSE_ID").toString());
			String courseTime = miniClassCourse.getCourseTime() + " - " + miniClassCourse.getCourseEndTime();
//			 检测是否有冲突
//			crashInd = courseConflictDao.countDistinctConflicts(miniClassCourse.getMiniClassCourseId(),
//					"", miniClassCourse.getTeacher().getUserId(), miniClassCourse.getCourseDate(), courseTime);
			crashInd = courseConflictDao.findCourseConflictCount(miniClassCourse.getMiniClassCourseId(), "", miniClassCourse.getTeacher()==null?"":miniClassCourse.getTeacher().getUserId());
			voMap.put("crashInd", crashInd);
			voMap.put("ARRIVALS", arrivals);
//			voMap.put("late", late);
			voMap.put("PROMISERS", promisers);
			voMap.put("nonono", nonono);
//			voMap.put("leave", leave);
			voMap.put("late", late);
			voMap.put("absent", absent);
			voMap.put("paid", paid);
			System.out.println(voMap.get("COURSE_STATUS"));
			voMapList.add(voMap);
		}
		return voMapList;
	}
	
	/**
	 * 查询哪个小班课程未安排教室
	 */
	@Override
	public List getNotArrangementClassroomMiniClassCourse(String startDate, String endDate, String classroomName, String miniClassName) {
		List<Map> list = miniClassCourseDao.getNotArrangementClassroomMiniClassCourse(startDate, endDate, classroomName, miniClassName);
		return list;
	}
	
	/**
	 * 小班课程详情
	 * @param miniClassId
	 */
	@Override
	public DataPackage getMiniClassCourseDetail(String miniClassId, DataPackage dp,ModelVo modelVo) {
		dp = miniClassCourseDao.getMiniClassCourseDetail(miniClassId, dp, modelVo);
		List<MiniClassCourseVo> voList = HibernateUtils.voListMapping((List<MiniClassCourse>) dp.getDatas(), MiniClassCourseVo.class);
		List<MiniClassCourseVo> newList = new ArrayList<MiniClassCourseVo>();
		for(MiniClassCourseVo miniClassCourseVo: voList) {
			// 上课人数
			List<Criterion> criterionList = new ArrayList<Criterion>();
			criterionList.add(Expression.eq("miniClassCourse.miniClassCourseId", miniClassCourseVo.getMiniClassCourseId()));
//			criterionList.add(Expression.eq("miniClassAttendanceStatus", MiniClassAttendanceStatus.CONPELETE));
			List<MiniClassAttendanceStatus> list = new ArrayList<>();
			list.add(MiniClassAttendanceStatus.CONPELETE);
			list.add(MiniClassAttendanceStatus.LATE);
			criterionList.add(Expression.in("miniClassAttendanceStatus", list));
			miniClassCourseVo.setCompleteClassPeopleNum(miniClassStudentAttendentDao.findCountByCriteria(criterionList));
			
			// 扣费人数
//			miniClassCourseVo.setChargedPeopleQuantity(miniClassStudentDao.getChargedPeopleQuantity(miniClassCourseVo.getMiniClassId()));
			miniClassCourseVo.setChargedPeopleQuantity(accountChargeRecordsDao.getMiniClassCourseChargeStudentNum(miniClassCourseVo.getMiniClassCourseId()));
			
			// 扣费金额
//			miniClassCourseVo.setChargedMoneyQuantity(miniClassStudentDao.getChargedMoneyQuantity(miniClassCourseVo.getMiniClassId()).doubleValue());
			miniClassCourseVo.setChargedMoneyQuantity(accountChargeRecordsDao.getMiniClassConsumeFinance(miniClassCourseVo.getMiniClassCourseId()).doubleValue());
			
			int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(miniClassCourseVo.getMiniClassCourseId(), ProductType.SMALL_CLASS);
			if (count > 0) {
				miniClassCourseVo.setIsWashed("TRUE");
			} else {
				miniClassCourseVo.setIsWashed("FALSE");
			}
			
			newList.add(miniClassCourseVo);
		}
		dp.setDatas(newList);
		return dp;
	}
	
	/**
	 * 小班课时信息 
	 * @param miniClassId
	 * @return
	 */
	public Map getMiniClassCourseTimeAnalyze(String miniClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ifnull(mc.TOTAL_CLASS_HOURS,0) as TOTAL_CLASS_HOURS,   ");
		sql.append(" ifnull(sum(CASE WHEN mcc.COURSE_STATUS <> 'CANCEL' THEN mcc.COURSE_HOURS ELSE 0 END),0) as ARRANGED_COURSE_HOURS, ");
		sql.append(" (mc.TOTAL_CLASS_HOURS - ifnull(sum(CASE WHEN mcc.COURSE_STATUS <> 'CANCEL' THEN mcc.COURSE_HOURS ELSE 0 END),0)) AS NOT_ARRANG_COURSE_HOURS, ");
		sql.append(" SUM(CASE WHEN mcc.COURSE_STATUS = 'CHARGED' THEN mcc.COURSE_HOURS ELSE 0 END) AS CHARGED_COURSE_HOURS, ");
		sql.append(" SUM(CASE WHEN mcc.COURSE_STATUS = 'NEW' THEN mcc.COURSE_HOURS ELSE 0 END) AS NEW_COURSE_HOURS, ");
		sql.append(" CANCELED_HOURS ,");
		sql.append(" SUM(CASE WHEN mcc.COURSE_STATUS = 'TEACHER_ATTENDANCE' or mcc.COURSE_STATUS = 'CHARGED' THEN mcc.COURSE_HOURS ELSE 0 END) AS ATTENDANCE_COURSE_HOURS, p.MINI_CLASS_COURSE_TYPE as courseType");
		sql.append(" FROM mini_class mc LEFT JOIN mini_class_course mcc on mc.MINI_CLASS_ID = mcc.MINI_CLASS_ID ");
		sql.append(" LEFT JOIN product p on p.id=mc.PRODUCE_ID  ");
		sql.append(" WHERE mc.MINI_CLASS_ID = :miniClassId ");
		params.put("miniClassId", miniClassId);
		List<Map<Object, Object>> list = smallClassDao.findMapBySql(sql.toString(), params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			throw new ApplicationException("没找到小班ID为："+miniClassId+"的课时信息");
		}
	}
	
	/**
	 * 小班详情-在读学生信息列表
	 * @param miniClassId
	 */
	@Override
	public DataPackage getMiniClassDetailStudentList(String miniClassId, DataPackage dp) { dp = miniClassStudentDao.getMiniClassDetailStudentList(miniClassId, dp);
	List<MiniClassStudentVo> voList = HibernateUtils.voListMapping((List<MiniClassStudentVo>) dp.getDatas(), MiniClassStudentVo.class, "withoutStudent");
	List<MiniClassStudentVo> newList = new ArrayList<MiniClassStudentVo>();
		MiniClass miniClass = miniClassDao.findById(miniClassId);

		for(MiniClassStudentVo miniClassStudentVo: voList) {
		if (StringUtils.isBlank(miniClassStudentVo.getContractProductId())) {
			throw new ApplicationException("小班学生表ID为："+miniClassStudentVo.getId()+"的数据没有合同产品ID");
		}
			/**
			 * 设置学生续报情况
			 */
		setXuBaoQingKuang(miniClassStudentVo, miniClass);
			
		ContractProduct contractProduct = contractProductDao.findById(miniClassStudentVo.getContractProductId());
		BigDecimal paidAmount = contractProduct.getPaidAmount();// 已付款资金
		
		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal consumeHours = BigDecimal.ZERO;
		List<AccountChargeRecords> recordList = accountChargeRecordsDao.getAccountChargeRecordsByMiniClassAndStudent(miniClassId, miniClassStudentVo.getStudentId());
		for (AccountChargeRecords record : recordList) {
			consumeAmount = consumeAmount.add(record.getAmount());
			consumeHours=consumeHours.add(record.getQuality());
		}
		
//		BigDecimal consumeAmount = contractProduct.getConsumeAmount();// 消耗资金
		BigDecimal dealDiscount = contractProduct.getDealDiscount();// 折扣
		BigDecimal price = contractProduct.getPrice();// 每节课价格
		
		if (null == paidAmount || null == consumeAmount || null == dealDiscount || null == price) {
			throw new ApplicationException("已付款资金、消耗资金、折扣、每节课价格，这四个必填项中出现空值，请检查！涉及的合同产品ID为："+ contractProduct.getId());
		}




		BigDecimal surplusFinance = contractProduct.getRemainingAmount();// 剩余资金
		BigDecimal surplusCourseHour = BigDecimal.ZERO;// 剩余课时
		BigDecimal realPrice = price.multiply(dealDiscount);// 真实价格
		if (surplusFinance.compareTo(BigDecimal.ZERO) > 0) {
			int result = surplusFinance.compareTo(realPrice);
			if (result != -1) {// 剩余资金必须大于课程价格，不然剩余课时为零
				surplusCourseHour = surplusFinance.divide(realPrice,2,RoundingMode.HALF_UP);
			}
		}

		//精英班按科目分配的课时来计算
		List<PromiseClassSubject> list =promiseClassSubjectDao.findPromiseSubjectByClassIdAndStudentId(miniClassId,miniClassStudentVo.getStudentId());
		if(list.size()>0 && surplusFinance.compareTo(BigDecimal.ZERO) > 0){
			PromiseClassSubject pro=list.get(0);
			BigDecimal surplusCourseHour2=pro.getCourseHours().subtract(pro.getConsumeCourseHours());//分配课时-课消课时
			BigDecimal surplusFinance2 = BigDecimal.ZERO;
			if (promiseClassService.isYearAfter2018(contractProduct)){
				surplusCourseHour = surplusCourseHour.subtract(consumeHours);
				surplusFinance2 = pro.getCourseHours().multiply(realPrice);//总课时*单价
			}else {
				surplusFinance2=surplusCourseHour2.multiply(realPrice);//剩余课时*单价
			}

			if(surplusCourseHour.compareTo(surplusCourseHour2)>0){
				surplusCourseHour=surplusCourseHour2;
				surplusFinance=surplusFinance2;
			}
		}

		miniClassStudentVo.setMiniClassSurplusFinance(surplusFinance);// 小班剩余资金
		miniClassStudentVo.setMiniClassSurplusCourseHour(surplusCourseHour);// 小班剩余课时
		miniClassStudentVo.setMiniClassTotalCharged(consumeAmount);// 累计扣费
		
		List<MiniClassRelation> miniClassRelations = this.findMiniClassRelation(miniClassStudentVo.getStudentId(), miniClassStudentVo.getMiniClassId());
		for (MiniClassRelation miniClassRelation: miniClassRelations) {
			if (MiniClassRelationType.CONTINUE == miniClassRelation.getRelationType()) {
				miniClassStudentVo.setContinueMiniClassId(miniClassRelation.getNewMiniClass().getMiniClassId());
			} else if (MiniClassRelationType.EXTEND == miniClassRelation.getRelationType()) {
				miniClassStudentVo.setExtendMiniClassId(miniClassRelation.getNewMiniClass().getMiniClassId());
			}
		}
			
		newList.add(miniClassStudentVo);
	}
	dp.setDatas(newList);
	return dp;
	}

	/**
	 * 在读学生信息增加一列“续报情况” :学生续报了下一季度或下下季度同科目的小班。
	 * 例子：
	 *  报读情况	           下一季度	        下下季度
	 *2017语文寒假班	     2017语文春季班
	 *2017语文春季班	     2017语文暑假班	  2017语文秋季班
	 *2017语文暑假班	     2017语文秋季班
	 *2017语文秋季班	     2018语文寒假班	  2018语文春季班
	 *
	 * @param miniClassStudentVo
	 * @param miniClass
	 */
	private void setXuBaoQingKuang(MiniClassStudentVo miniClassStudentVo, MiniClass miniClass) {
		/**
		 * 报读科目
		 */
		DataDict subject = miniClass.getSubject();
		/**
		 * 报读产品
		 */
		Product product = miniClass.getProduct();
		/**
		 * 报读年份
		 */
		DataDict year = product.getProductVersion();
		/**
		 * 报读季度
		 */
		DataDict productQuarter = product.getProductQuarter();
		/**
		 * 学生id
		 */
		String studentId = miniClassStudentVo.getStudentId();

		String result = "";

		StringBuffer sql = new StringBuffer();
		sql.append("    SELECT                                        ");
		sql.append("    count(*)                                      ");
		sql.append("    FROM                                          ");
		sql.append("    mini_class_student mcs,                       ");
		sql.append("    mini_class mc,                                ");
		sql.append("    product p,                                    ");
		sql.append("    data_dict QUARTER,                            ");
		sql.append("    data_dict YEAR                                ");
		sql.append("    WHERE                                         ");
		sql.append("    mcs.MINI_CLASS_ID = mc.MINI_CLASS_ID          ");
		sql.append("    AND mc.PRODUCE_ID = p.ID                      ");
		sql.append("    AND QUARTER .ID = p.PRODUCT_QUARTER_ID        ");
		sql.append("    AND p.PRODUCT_VERSION_ID = YEAR .ID           ");
		sql.append("    AND mcs.STUDENT_ID = '"+studentId+"'        ");
		sql.append("    AND mc.`SUBJECT` = '"+subject.getId()+"'            ");


		if ("寒假".equals(productQuarter.getName())){
			StringBuffer springSql = new StringBuffer();
			springSql.append(sql);
			springSql.append("    AND YEAR .`NAME` =  '"+year.getName()+"'                    ");
			springSql.append("    AND QUARTER .`NAME` = '春季'                    ");
			int springCount = miniclassStudentDao.findCountSql(springSql.toString(), new HashMap<>());
			if (springCount>0){
				result+="续春";
			}
			miniClassStudentVo.setXuBaoQingKuang(result);
		}

		if ("春季".equals(productQuarter.getName())){
			StringBuffer summerSql = new StringBuffer();
			summerSql.append(sql);
			summerSql.append("    AND YEAR .`NAME` =  '"+year.getName()+"'                    ");
			summerSql.append("    AND QUARTER .`NAME` = '暑假'                    ");
			int summerCount = miniclassStudentDao.findCountSql(summerSql.toString(), new HashMap<>());
			if (summerCount>0){
				result+="续暑  ";
			}
			StringBuffer autumnSql = new StringBuffer();
			autumnSql.append(sql);
			autumnSql.append("    AND YEAR .`NAME` =  '"+year.getName()+"'                    ");
			autumnSql.append("    AND QUARTER .`NAME` = '秋季'                    ");
			int autumnCount = miniclassStudentDao.findCountSql(autumnSql.toString(), new HashMap<>());
			if (autumnCount>0){
				result+="续秋";
			}
			miniClassStudentVo.setXuBaoQingKuang(result);
		}
		if ("暑假".equals(productQuarter.getName())){
			StringBuffer autumnSql = new StringBuffer();
			autumnSql.append(sql);
			autumnSql.append("    AND YEAR .`NAME` =  '"+year.getName()+"'                    ");
			autumnSql.append("    AND QUARTER .`NAME` = '秋季'                    ");
			int autumnCount = miniclassStudentDao.findCountSql(autumnSql.toString(), new HashMap<>());
			if (autumnCount>0){
				result+="续秋";
			}
			miniClassStudentVo.setXuBaoQingKuang(result);
		}


		if ("秋季".equals(productQuarter.getName())){
			StringBuffer winterSql = new StringBuffer();
			winterSql.append(sql);
			winterSql.append("    AND YEAR .`NAME` =  nextYear('"+year.getName()+"')                    ");
			winterSql.append("    AND QUARTER .`NAME` = '寒假'                    ");
			int winterCount = miniclassStudentDao.findCountSql(winterSql.toString(), new HashMap<>());
			if (winterCount>0){
				result+="续寒  ";
			}
			StringBuffer springSql = new StringBuffer();
			springSql.append(sql);
			springSql.append("    AND YEAR .`NAME` =  nextYear('"+year.getName()+"')                    ");
			springSql.append("    AND QUARTER .`NAME` = '春季'                    ");
			int springCount = miniclassStudentDao.findCountSql(springSql.toString(), new HashMap<>());
			if (springCount>0){
				result+="续春";
			}
			miniClassStudentVo.setXuBaoQingKuang(result);
		}


	}

	/**
	 * 小班详情-学生详情
	 * @param miniClassId
	 */
	@Override
	public DataPackage getMiniClassDetailStudentDetail(String miniClassId, String studentId, DataPackage dp) {
		
		MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClassId, studentId);
		if(miniClassStudent == null)
		{
			throw new ApplicationException("对应的小班学生不存在！");
		}
		// 所有课程
		dp = miniClassCourseDao.getMiniClassCourseListByMiniClassId(miniClassId, miniClassStudent.getFirstSchoolTime(), dp);
		List<MiniClassCourseVo> voList = HibernateUtils.voListMapping((List<MiniClassCourseVo>) dp.getDatas(), MiniClassCourseVo.class);
		
		// 加入学生每节课程的考勤和扣费信息
		for (MiniClassCourseVo miniClassCourseVo:voList) {
			// 取考勤表数据
			MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(miniClassCourseVo.getMiniClassCourseId(), studentId);
			if (miniClassStudentAttendent != null) {
				if (miniClassStudentAttendent.getMiniClassAttendanceStatus() != null) {
					miniClassCourseVo.setMiniClassAttendanceStatus(miniClassStudentAttendent.getMiniClassAttendanceStatus().getName());// 设置考勤状态
				}
				if (miniClassStudentAttendent.getChargeStatus() != null) {
					miniClassCourseVo.setChargeStatus(miniClassStudentAttendent.getChargeStatus().getName());// 设置扣费状态
				}
			}
			
			// 取扣费记录表数据
//			AccountChargeRecords accountChargeRecords = accountChargeRecordsDao.getMiniClassCourseRecordByStudentId(miniClassCourseVo.getMiniClassCourseId(), studentId);
			List<AccountChargeRecords> list = accountChargeRecordsDao.getMiniClassCourseRecordByStudentId(miniClassCourseVo.getMiniClassCourseId(), studentId);
			if (list !=null ){
				BigDecimal chargeFinance = BigDecimal.ZERO;
				for (AccountChargeRecords chargeRecords : list){
					chargeFinance = chargeFinance.add(chargeRecords.getAmount());
				}
				miniClassCourseVo.setChargeFinance(chargeFinance); //设置扣费金额
			}
//			if (accountChargeRecords != null) {
//				if (accountChargeRecords.getAmount() != null) {
//					miniClassCourseVo.setChargeFinance(accountChargeRecords.getAmount());// 设置扣费金额
//				}
//			}
		}
		
		dp.setDatas(voList);
		return dp;
	}
	
	/**
	 * 小班详情-小班考勤扣费详情
	 */
	@Override
	public DataPackage getMiniClassAttendanceChargedDetail(MiniClassStudentVo miniClassStudentVo, DataPackage dp) throws Exception {
		MiniClassCourse currMiniClassCourse =  miniClassCourseDao.findById(miniClassStudentVo.getMiniClassCourseId());
		miniClassStudentVo.setMiniClassId(currMiniClassCourse.getMiniClass().getMiniClassId());
		miniClassStudentVo.setFirstSchoolTime(currMiniClassCourse.getCourseDate());
		//miniClassStudentVo.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.CHARGED.name());// 只取已扣费的学生
//		dp = miniClassStudentDao.getMiniClassStudentList(miniClassStudentVo, dp);
		dp = miniClassStudentDao.getMiniClassStudentListUncharge(miniClassStudentVo, dp);//查找未扣费学生
		if(dp == null)
		{
			throw new ApplicationException("获取不到未扣费学生信息");
		}
		List<MiniClassStudentVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<MiniClassStudent>) dp.getDatas(), MiniClassStudentVo.class, "withoutStudent");
		List<AccountChargeRecordsVo> acrVoList = HibernateUtils.voListMapping(
				accountChargeRecordsDao
						.getMiniClassCourseChargeStudentList(miniClassStudentVo
								.getMiniClassCourseId()),
				AccountChargeRecordsVo.class);
		
		String studentIDsString="";
		for (MiniClassStudentVo mcsv : searchResultVoList) {
			studentIDsString+=mcsv.getStudentId()+",";
		}
		
		int count = dp.getRowCount();
		for (AccountChargeRecordsVo accountChargeRecordsVo : acrVoList) {
			if(studentIDsString.contains(accountChargeRecordsVo.getStudentId())){
				continue;
			}
			studentIDsString += accountChargeRecordsVo.getStudentId() + ",";
			MiniClassStudentVo mcsVo = new MiniClassStudentVo();
			mcsVo.setStudentId(accountChargeRecordsVo.getStudentId());
			mcsVo.setStudentName(accountChargeRecordsVo.getStudentName());
			mcsVo.setContractProductId(accountChargeRecordsVo.getContractProductId());
			searchResultVoList.add(mcsVo);
			count++;
		}
		
		
		// 过滤未报名的学生
		//searchResultVoList = filterNoSignStudent(currMiniClassCourse, searchResultVoList);
		
		for (MiniClassStudentVo eachVo:searchResultVoList){
			MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(miniClassStudentVo.getMiniClassCourseId(), eachVo.getStudentId());
			eachVo.setCourseDate(currMiniClassCourse.getCourseDate());// 设置考勤的课程日期
			
			// 设置考勤状态
			if (miniClassStudentAttendent != null) {
				eachVo.setMiniClassAttendanceStatus(miniClassStudentAttendent.getMiniClassAttendanceStatus().getValue());
				eachVo.setMiniClassStudentChargeStatus(miniClassStudentAttendent.getChargeStatus().getValue());
			} else {// 没有考勤记录的情况，初始化考勤状态为“未考勤”
				eachVo.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW.getValue());
				eachVo.setMiniClassStudentChargeStatus(MiniClassStudentChargeStatus.UNCHARGE.getValue());
			}
			
			// 设置老师是否考勤状态
			if (miniClassStudentAttendent != null && miniClassStudentAttendent.getHasTeacherAttendance ()!= null) {// 设置老师是否已考勤字段
				eachVo.setHasTeacherAttendance(miniClassStudentAttendent.getHasTeacherAttendance());
			} else {
				eachVo.setHasTeacherAttendance(BaseStatus.FALSE);
			}
			
			// 设置合同状态
			ContractProduct contractProduct = contractProductDao.findById(eachVo.getContractProductId());
			if (contractProduct != null) {
				eachVo.setContractProductStatus(contractProduct.getStatus());
			}
		}
		
		dp.setDatas(searchResultVoList);
		dp.setRowCount(count);
		return dp;
	}
	
	/**
	 * 所有小班列表，用作Selection 
	 */
	@Override
	public List getMiniClassList4Selection(String productId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClass m where 1=1 ");
		if(StringUtils.isNotBlank(productId)) {
			hql.append(" and product.id = :productId ");
			params.put("productId", productId);
		}
		hql.append(" and campusSale = 0 ");
		Organization organization=userService.getBelongCampus();
		hql.append("and blCampus.id in (select id from Organization where orgLevel like '"+organization.getOrgLevel()+"%')");

		hql.append(" order by blCampus.id,createTime desc ");
		List<MiniClass> list=miniClassDao.findAllByHQL(hql.toString(), params);
		return list;
		
	
	}
	
	/**
	 * 小班详情-主界面信息
	 */
	@Override
	public MiniClassVo getMiniClassDetailMainPageInfo(String miniClassId) {
		MiniClassVo miniClassVo = new MiniClassVo();
		//miniClassVo.setTotalRemainCourseHour(miniClassStudentDao.getMiniClassTotalRemainCourseHour(miniClassId));// 小班剩余课时
		miniClassVo.setTotalChargedMoneyQuantity(miniClassStudentDao.getChargedMoneyQuantity(miniClassId));// 小班累计总扣款
		return miniClassVo;
	}
	
	/**
	 * 小班详情-小班考勤扣费详情页面信息
	 */
	@Override
	public MiniClassCourseVo getMiniClassDetailAttChaPageInfo(String miniClassCourseId) {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		if(miniClassCourse == null)
		{
			throw new ApplicationException("获取不到对应的小班课程数据！");
		}
		MiniClassCourseVo miniClassCourseVo = HibernateUtils.voObjectMapping(miniClassCourse, MiniClassCourseVo.class);
		
		// 小班课程状态
		if (StringUtils.isNotBlank(miniClassCourseVo.getCourseStatus())) {
			CourseStatus courseStatus = CourseStatus.valueOf(miniClassCourseVo.getCourseStatus());
			miniClassCourseVo.setCourseStatus(courseStatus.getName());
		}
		
		// 小班课程上课人数
		int miniClassCourseStudentNum =getMiniClassCourseStudentNum(miniClassCourseVo.getMiniClassCourseId());
		miniClassCourseVo.setMiniClassCourseStudentNum(miniClassCourseStudentNum);
		return miniClassCourseVo;
	}
	
	
	/**
	 * 小班课程上课人数
	 * @return
	 */
	@Override
	public int getMiniClassCourseStudentNum(String miniClassCourseId) {
		// 上课人数
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("miniClassCourse.miniClassCourseId", miniClassCourseId));
//		criterionList.add(Expression.eq("miniClassAttendanceStatus", MiniClassAttendanceStatus.CONPELETE));
		List<MiniClassAttendanceStatus> list = new ArrayList<>();
		list.add(MiniClassAttendanceStatus.CONPELETE);
		list.add(MiniClassAttendanceStatus.LATE);
		criterionList.add(Expression.in("miniClassAttendanceStatus", list));
		int miniClassCourseStudentNum = miniClassStudentAttendentDao.findCountByCriteria(criterionList);
		return miniClassCourseStudentNum;
	}
	
	/**
	 * 小班学生对应的合同产品
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	@Override
	public ContractProductVo getMiniClassStudentContractProduct(String miniClassId, String studentId) {
		MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClassId, studentId);
		if(miniClassStudent==null){
			throw new ApplicationException("找不到该学生，请刷新后重试");
		}
		ContractProduct contractProduct = miniClassStudent.getContractProduct();
		contractService.calSingleContractProduct(contractProduct);
		
		ContractProductVo contractProductVo = HibernateUtils.voObjectMapping(contractProduct, ContractProductVo.class);
		contractProductVo.setStudentName(miniClassStudent.getStudent().getName());
		contractProductVo.setRemainFinance(contractProduct.getRemainingAmount());// 设置剩余资金
		
//		BigDecimal paidAmount = contractProduct.getPaidAmount();// 已付款资金
//		BigDecimal consumeAmount = contractProduct.getConsumeAmount();// 消耗资金
		BigDecimal dealDiscount = contractProduct.getDealDiscount();// 折扣
		BigDecimal price = contractProduct.getPrice();// 每节课价格
//		
//		if (null == paidAmount || null == consumeAmount || null == dealDiscount || null == price) {
//			throw new ApplicationException("已付款资金、消耗资金、折扣、每节课价格，这四个必填项中出现空值，请检查！涉及的合同产品ID为："+ contractProduct.getId());
//		}
//		
		BigDecimal remainFinance = contractProductVo.getRemainFinance();// 剩余资金
		BigDecimal realPrice = price;
		if (dealDiscount.compareTo(BigDecimal.ZERO) > 0) {
			realPrice = price.multiply(dealDiscount);// 真实价格
		}
		BigDecimal remainCourseHour = BigDecimal.ZERO;// 剩余课时
		
		if (remainFinance.compareTo(BigDecimal.ZERO) > 0) {
			int result = remainFinance.compareTo(realPrice);
			if (result != -1) {// 剩余资金必须大于课程价格，不然剩余课时为零
				remainCourseHour = remainFinance.divide(realPrice,2);
			}
		}
		
//		contractProductVo.setRemainFinance(remainFinance);// 设置剩余资金
		contractProductVo.setRemainCourseHour(remainCourseHour);// 设置剩余课时
		
		return contractProductVo;
	}
	
	/**
	 * 小班学生转班 + 设置第一次上课日期
	 * @param oldSmallClassId
	 * @param newSmallClassId
	 * @param studentIds
	 */
	@Override
	public void stuChangeMiniClassAndSetFistClassDate(String oldSmallClassId,
			String newSmallClassId, String studentIds, String fistClassDate) {
		MiniClass miniClass= miniClassDao.findById(newSmallClassId);
		MiniClass oldMiniClass= miniClassDao.findById(oldSmallClassId);
		MiniClassStudent miniClassStudent = null;
		String sql = "";
		for(String id: studentIds.split(",")){
			miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(newSmallClassId, id);
			if (null != miniClassStudent) {
				Student student = studentDao.findById(id);
				throw new ApplicationException("学生" + student.getName() + "在目标小班中已有记录！");
			}
			MiniClassStudent oldMiniClassStudent= miniClassStudentDao.getOneMiniClassStudent(oldSmallClassId, id);

			if(oldMiniClassStudent==null){
				throw new ApplicationException("数据已被其他人修改，请刷新页面查看最新数据");
			}

			oldMiniClassStudent.setMiniClass(miniClass);
			oldMiniClassStudent.setFirstSchoolTime(fistClassDate);
			ContractProduct cp =oldMiniClassStudent.getContractProduct();
			if(cp!=null && cp.getMiniClassId()!=null){
				cp.setMiniClassId(newSmallClassId);
			}
			try {
				miniClassStudentDao.save(oldMiniClassStudent);
				miniClassStudentDao.commit();
			}catch(Exception e){
				throw new ApplicationException("数据已被其他人修改，请刷新页面查看最新数据");
			}

			miniClassStudentAttendentDao.deleteMiniClassStudentAttendentByStudentAndMiniClass(id, oldSmallClassId);
			this.addMiniClassStudentAttendents(id, newSmallClassId, fistClassDate);
		}
		if (StringUtil.isNotBlank(studentIds)) {
		    int studentNum = studentIds.split(",").length;
		    // 旧小班回滚库存
	        miniClassInventoryService.revertInventory(oldSmallClassId, studentNum);
	        // 新小班减少库存
		    miniClassInventoryService.reduceInventory(newSmallClassId, studentNum, true);
		}
		updateMiniClassRecruitStatus(miniClass);
		updateMiniClassRecruitStatus(oldMiniClass);//旧
	}

	@Override
	public void deleteSingleStudentInMiniClasss(String studentId,
			String smallClassId) {
		MiniClassStudent oneMiniClassStudent = miniClassStudentDao.getOneMiniClassStudent(smallClassId, studentId);
		miniClassStudentDao.delete(oneMiniClassStudent);
		miniClassInventoryService.revertInventory(smallClassId);
		miniClassStudentDao.flush();
	}

	@Override
	public MiniClass getMiniClassByContractProduct(ContractProduct conProduct){
		MiniClass miniClass  = miniClassStudentDao.findMiniClassByContractProductId(conProduct.getId());
		return miniClass;
		
	}
	
	@Override
	public List<MiniClass> getAllMiniClassByContractProduct(ContractProduct conProduct){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select mc.* from mini_class mc left join mini_class_student mcs on mcs.mini_class_id=mc.mini_class_id ");
		sql.append(" where mcs.contract_product_id = :contractProductId ");
		params.put("contractProductId", conProduct.getId());
		return miniClassDao.findBySql(sql.toString(), params);
	}

	@Override
	public boolean hasAttendenceRecordByMiniClassAndStudent(String miniClassId,
			String studentId) {
		int countOfRecord = miniClassStudentAttendentDao.getCountOfAttendenceRecordByMiniClassAndStudent( miniClassId, studentId);
		if(countOfRecord > 0) {
			return true; 
		} else {
			return false;
		}
		
	}
	
	@Override
	public void deleteAttendenceRecordByMiniClassAndStudent(String miniClassId,String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" delete mcsa.* from MINI_CLASS_STUDENT_ATTENDENT mcsa inner join mini_class_course  mcc on mcc.mini_class_course_id=mcsa.mini_class_course_id");
		sql.append(" where 1=1 and charge_Status <>'CHARGED' ");
		sql.append(" and mcsa.student_id= :studentId ");
		sql.append(" and mcc.mini_class_id = :miniClassId ");
		params.put("studentId", id);
		params.put("miniClassId", miniClassId);
		miniClassStudentAttendentDao.excuteSql(sql.toString(), params);
		
		
		
	}

	@Override
	public BigDecimal findMiniClassWillBuyHour(String id, String startDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql=new StringBuilder();
		sql.append("select course_Hours from mini_class_course where course_date >= :startDate and mini_class_id = :miniClassId and COURSE_STATUS<>'CANCEL' ");
		params.put("startDate", startDate);
		params.put("miniClassId", id);
		List<Map<Object, Object>> map=miniClassCourseDao.findMapBySql(sql.toString(), params);
		BigDecimal hours=BigDecimal.ZERO;
		for(Map<Object,Object> m:map){
			hours=hours.add((BigDecimal)m.get("course_Hours"));
		}

		return hours;
	}

	@Override
	public void deleteMiniClassCourse(String miniClassCourseId) {
		Double delCourseHours=0.0;
		String miniclassId=null;
		for(String id : miniClassCourseId.split(",")){
			MiniClassCourse course=miniClassCourseDao.findById(id);

			if(course==null){
				continue;
			}

			if(course!=null&&course.getCourseHours()!=null){
				delCourseHours += course.getCourseHours();
			}		
			if(miniclassId == null){
				MiniClassCourse mcc=miniClassCourseDao.findById(id);
				if(mcc!=null && mcc.getMiniClass()!=null)
					miniclassId = mcc.getMiniClass().getMiniClassId();
			}
			int count = accountChargeRecordsDao.countAccountChargeRecordsByMiniClassCourseId(id);
			if (count > 0) {
				throw new ApplicationException("此课程发生过扣费或者扣费冲销，不允许删除，请取消课程或者修改课程信息重新考勤扣费。");
			}
			miniClassStudentAttendentDao.deleteMiniClassStudentAttendent(id);
			miniClassCourseDao.delete(course);
		}
		if(miniclassId != null){
			MiniClass miniClass=miniClassDao.findById(miniclassId);
			if(miniClass.getArrangedHours()!=null && miniClass.getArrangedHours()>0) {
				miniClass.setArrangedHours(miniClass.getArrangedHours() - delCourseHours);
			}
			updateCourseNumByClassId(miniclassId);//更新排序
			miniClassDao.save(miniClass);
			miniClassCourseDao.flush();
			// 更新小班的开课结课日期
			this.updateMiniClassMaxMinCourseDate(miniClass);
		}
		
		
	}
	
	@Override
	public int countByProductId(String productId) {
		return miniClassDao.countByProductId(productId);
	}
	
	@Override
	public DataPackage getMiniClassCourseConsumeList(
			MiniClassCourseVo miniClassCourseVo, DataPackage dp) {
		dp = this.getMiniClassCourseList(miniClassCourseVo, dp);
		List<MiniClassCourseConsumeVo> consumeVos =  HibernateUtils.voListMapping( (List<MiniClassCourseVo>) dp.getDatas(), MiniClassCourseConsumeVo.class);
		dp.setDatas(consumeVos);
		return dp;
	}

	/**
	 * 根据学生获取已经结束的小班
	 * @param dp
	 * @param miniClassRelationSearchVo
	 * @return
	 */
	public DataPackage findOldMiniClassByStudent(DataPackage dp,
			MiniClassRelationSearchVo miniClassRelationSearchVo) {
	    List<ContinueExtendMiniClassVo> continueExtendMiiniClassVoList = null;
	    if (StringUtils.isNotBlank(miniClassRelationSearchVo.getStudentId()) && null != miniClassRelationSearchVo.getRelationType()) {
	    	Map<String, Object> params = new HashMap<String, Object>();
			String hql=" from MiniClassStudent where studentId='"+miniClassRelationSearchVo.getStudentId() + "' "
	    				+ " and miniClassId not in (select omc.miniClassId from MiniClassRelation mcr join mcr.oldMiniClass omc where mcr.student.id = :studentId "
	    				+ " and mcr.relationType = :relationType )";
			params.put("studentId", miniClassRelationSearchVo.getStudentId());
			params.put("relationType", miniClassRelationSearchVo.getRelationType());
	    	dp = miniClassStudentDao.findPageByHQL(hql, dp, true, params);
	    	List<MiniClassStudent> miniClassStudentList = (List<MiniClassStudent>) dp.getDatas();
	    	continueExtendMiiniClassVoList =  HibernateUtils.voListMapping(miniClassStudentList, ContinueExtendMiniClassVo.class);
	    	for (ContinueExtendMiniClassVo continueExtendMiniClassVo: continueExtendMiiniClassVoList) {
	    		continueExtendMiniClassVo.setRelationType(miniClassRelationSearchVo.getRelationType());
	    	}
	    }
	    dp.setDatas(continueExtendMiiniClassVoList);
	    return dp;
	}
	
	/**
	 * 根据学生获取还没结束的小班
	 * @param dp
	 * @param miniClassRelationSearchVo
	 * @return
	 */
	public DataPackage findUnconpeletedMiniClass(DataPackage dp,
			MiniClassRelationSearchVo miniClassRelationSearchVo) {
	    List<ContinueExtendMiniClassVo> continueExtendMiiniClassVoList = null;
	    if (StringUtils.isNotBlank(miniClassRelationSearchVo.getStudentId()) && null != miniClassRelationSearchVo.getRelationType()) {
	    	Map<String, Object> params = new HashMap<String, Object>();
	    	String hql=" from MiniClassStudent where studentId='"+miniClassRelationSearchVo.getStudentId() + "'"
	    				+ " and miniClassId not in (select omc.miniClassId from MiniClassRelation mcr join mcr.oldMiniClass omc where mcr.student.id = :studentId "
	    				+ " and mcr.relationType = :relationType)";
	    	params.put("studentId", miniClassRelationSearchVo.getStudentId());
			params.put("relationType", miniClassRelationSearchVo.getRelationType());
	    	dp = miniClassStudentDao.findPageByHQL(hql, dp, true, params);
	    	List<MiniClassStudent> miniClassStudentList = (List<MiniClassStudent>) dp.getDatas();
	    	continueExtendMiiniClassVoList =  HibernateUtils.voListMapping(miniClassStudentList, ContinueExtendMiniClassVo.class);
	    	for (ContinueExtendMiniClassVo continueExtendMiniClassVo: continueExtendMiiniClassVoList) {
	    		continueExtendMiniClassVo.setRelationType(miniClassRelationSearchVo.getRelationType());
	    	}
	    }
	    dp.setDatas(continueExtendMiiniClassVoList);
	    return dp;
	}
	
	/**
	 * 获取学生管理已结束小班的续班扩课记录
	 * @param studentId
	 * @param oldMiniClassId
	 * @return
	 */
	public List<MiniClassRelation> findMiniClassRelation(String studentId, String oldMiniClassId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("student.id", studentId));
		criterionList.add(Expression.eq("oldMiniClass.miniClassId", oldMiniClassId));
		return miniClassRelationDao.findAllByCriteria(criterionList);
	}
	
	/**
	 * 删除小班续班扩科关联记录
	 * @param miniClassRelationSearchVo
	 */
	public void deleteOldMiniClass(MiniClassRelationSearchVo miniClassRelationSearchVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "delete from MiniClassRelation ";
		String whereHql = " where 1=1 ";
		if (StringUtils.isNotBlank(miniClassRelationSearchVo.getStudentId())) {
			whereHql += " and student.id = :studentId ";
			params.put("studentId", miniClassRelationSearchVo.getStudentId());
		}
		if (null != miniClassRelationSearchVo.getRelationType()) {
			whereHql += " and relationType = :relationType ";
			params.put("relationType", miniClassRelationSearchVo.getRelationType());
		}
		if (StringUtils.isNotBlank(miniClassRelationSearchVo.getNewMiniClassId())) {
			whereHql += " and newMiniClass.miniClassId = :newMiniClassId ";
			params.put("newMiniClassId", miniClassRelationSearchVo.getNewMiniClassId());
		}
		if (StringUtils.isNotBlank(miniClassRelationSearchVo.getOldMiniClassId())) {
			whereHql += " and oldMiniClass.miniClassId = :oldMiniClassId ";
			params.put("oldMiniClassId", miniClassRelationSearchVo.getOldMiniClassId());
		}
		hql += whereHql;
		miniClassRelationDao.excuteHql(hql, params);
	}
	
	/**
	 * 保存小班续班扩科关联记录
	 * @param miniClassRelationSearchVo
	 */
	public void saveMiniClassRelation(MiniClassRelationSearchVo miniClassRelationSearchVo) {
		User user = userService.getCurrentLoginUser();
		Student student = new Student();
		student.setId(miniClassRelationSearchVo.getStudentId());
		MiniClass newMiniClass = new MiniClass();
		newMiniClass.setMiniClassId(miniClassRelationSearchVo.getNewMiniClassId());
		MiniClassRelation relation = new MiniClassRelation();
		MiniClass oldMiniClass = new MiniClass();
		oldMiniClass.setMiniClassId(miniClassRelationSearchVo.getOldMiniClassId());
		relation.setNewMiniClass(newMiniClass);
		relation.setRelationType(miniClassRelationSearchVo.getRelationType());
		relation.setOldMiniClass(oldMiniClass);
		relation.setStudent(student);
		relation.setCreateUserId(user.getUserId());
		relation.setCreateTime(DateTools.getCurrentDateTime());
		miniClassRelationDao.save(relation);
	}
	
	private int countMiniClassRelation(String miniClassId, MiniClassRelationType relationType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select count(distinct student.id) from MiniClassRelation ";
		String whereHql = " where 1=1 ";
		whereHql += " and oldMiniClass.miniClassId = :miniClassId ";
		whereHql += " and relationType = :relationType ";
		params.put("miniClassId", miniClassId);
		params.put("relationType", relationType);
		hql += whereHql;
		return miniClassRelationDao.findCountHql(hql, params);
	} 
	

	@Override
	public boolean checkHasMiniClassForDate(String teacherId, String date, List<CourseStatus> listOfStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer countHql = new StringBuffer();
		countHql.append("select count(*) from MiniClassCourse where 1 =1 ") 
				.append(" and teacher.userId = :teacherId ")
				.append(" and courseDate = :date ");
		params.put("teacherId", teacherId);
		params.put("date", date);
		if(listOfStatus.size()>0) {
			countHql.append(" and (courseStatus in (:statuses) or auditStatus='UNVALIDATE')");
			params.put("statuses", listOfStatus);
		}
		int count = this.miniClassCourseDao.findCountHql(countHql.toString(), params);
		return count >=1 ;
	}
	
	@Override
	public boolean checkHasDeductionMiniClassForDate(String teacherId, String date, List<CourseStatus> listOfStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer countHql = new StringBuffer();
		countHql.append("select count(*) from MiniClassCourse where 1 =1 ") 
				.append(" and studyHead.userId = :teacherId ")
				.append(" and courseDate = :date ");
		params.put("teacherId", teacherId);
		params.put("date", date);
		if(listOfStatus.size()>0) {
			countHql.append(" and courseStatus in (:statuses)");
			params.put("statuses", listOfStatus);
		}
		int count = this.miniClassCourseDao.findCountHql(countHql.toString(), params);
		return count >=1 ;
	}

	@Override
	public List<HasClassCourseVo> checkHasMiniClassForPeriodDate(String teacherId,String startDate, String endDate) {		
		List<String> dates =   DateTools.getDates(startDate, endDate);		
		boolean hasCourse = false; 
		boolean hasUnCheckAttendanceCourse = false;
		
		boolean hasDeducationCourse = false; 
		boolean hasUnDeducationCourse = false;
		List<HasClassCourseVo> listHasMiniClass = new ArrayList<HasClassCourseVo>();
		for(String strDate : dates) {
			HasClassCourseVo hasMiniClassVo=new HasClassCourseVo();
			hasMiniClassVo.setDateValue(strDate);//日期
			hasCourse = this.checkHasMiniClassForDate(teacherId, strDate, new ArrayList<CourseStatus>());
			if(hasCourse) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤
				List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
				listOfStatus.add(CourseStatus.NEW);
				listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
				hasUnCheckAttendanceCourse = this.checkHasMiniClassForDate(teacherId, strDate, listOfStatus );
				if(hasUnCheckAttendanceCourse ) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤					
					hasMiniClassVo.setHasValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());
				} else {	// 有课 且 都是 没有未考勤的， 就是全部是 不用考勤的  ，  // TEACHER_ATTENDANCE STUDY_MANAGER_AUDITED CHARGED  老师已考勤，  学管已确认， 教务已经扣费 
					hasMiniClassVo.setHasValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());					
				}
			} else {
				hasMiniClassVo.setHasValue(CourseSummaryStatus.NO_COURSE.getValue());
			}
			
			hasDeducationCourse = this.checkHasDeductionMiniClassForDate(teacherId, strDate, new ArrayList<CourseStatus>());
			if(hasDeducationCourse) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤
				List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
				listOfStatus.add(CourseStatus.NEW);
				listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
				listOfStatus.add(CourseStatus.TEACHER_ATTENDANCE);
				hasUnDeducationCourse = this.checkHasDeductionMiniClassForDate(teacherId, strDate, listOfStatus );
				if(hasUnDeducationCourse ) { // TEACHER_ATTENDANCE 老师已考勤					
					hasMiniClassVo.setHasDeductionValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());
				} else {	
					hasMiniClassVo.setHasDeductionValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());					
				}
			} else {
				hasMiniClassVo.setHasDeductionValue(CourseSummaryStatus.NO_COURSE.getValue());
			}
			
			listHasMiniClass.add(hasMiniClassVo);
		}
		return listHasMiniClass;
		
	}
	
	@Override
	public void saveMiniClassAttendancePic(String miniClassCourseId,MultipartFile attendancePicFile,String servicePath) {		 
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		String fileName ="";
		if(miniClassCourse !=null) {
			// 统一使用 文件名 来 标明签名文件
			//String fileName = UUID.randomUUID().toString();
			fileName =  String.format("MINI_CLASS_ATTEND_PIC_%s.jpg", miniClassCourseId);
			String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
			FileUtil.isNewFolder(folder);			 
			
			String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG 
			String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
			String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小					 
			
			String relFileName=folder+"/realFile_"+miniClassCourseId+UUID.randomUUID().toString()+".jpg";
			File realFile=new File(relFileName);
			File bigFile=new File(folder+"/"+bigFileName);
			File midFile=new File(folder+"/"+midFileName);
			File smallFile=new File(folder+"/"+smallFileName);			
			
			try {				
				attendancePicFile.transferTo(realFile);
				ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
				ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
				ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
				AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
				AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
				AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云				
				AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{				
				bigFile.delete();
				midFile.delete();
				smallFile.delete();
				realFile.delete();
			}

			if(StringUtils.isNotBlank(fileName)){
				miniClassCourse =miniClassCourseDao.findById(miniClassCourseId);
				miniClassCourse.setAttendacePicName(fileName);
			}


		}
	}
	
	/**
	 * 删除报名时间在courseDate之前的未扣费的未上课的考勤记录
	 */
	public void deleteMiniClassStudentAttendentByMiniClassCourse(MiniClassCourse miniClassCourse) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" delete MiniClassStudentAttendent where miniClassCourse.miniClassCourseId = :miniClassCourseId and miniClassAttendanceStatus='NEW'";
		params.put("miniClassCourseId", miniClassCourse.getMiniClassCourseId());
		miniClassStudentDao.excuteHql(hql, params);
		User user = userService.getCurrentLoginUser();
		MiniClassStudentVo miniClassStudentVo = new MiniClassStudentVo();
		miniClassStudentVo.setMiniClassId(miniClassCourse.getMiniClass().getMiniClassId());
		miniClassStudentVo.setFirstSchoolTime(miniClassCourse.getCourseDate());
		DataPackage dp = new DataPackage(0, 999);
		dp = miniClassStudentDao.getMiniClassStudentList(miniClassStudentVo, dp);
		List<MiniClassStudent> miniClassStudentList = (List<MiniClassStudent>) dp.getDatas();
		for (MiniClassStudent mcs: miniClassStudentList) {
			Student student = mcs.getStudent();
			//防止重复
			if(miniClassStudentAttendentDao.getOneMiniClassStudentAttendent(miniClassCourse.getMiniClassCourseId(),student.getId())!=null){
				continue;
			}
			MiniClassStudentAttendent miniClassStudentAttendent = new MiniClassStudentAttendent();
			miniClassStudentAttendent.setMiniClassCourse(miniClassCourse);
			miniClassStudentAttendent.setStudentId(student.getId());
			miniClassStudentAttendent.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW); // 未上课
			miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
			miniClassStudentAttendent.setCreateUser(user);
			miniClassStudentAttendent.setCreateTime(DateTools.getCurrentDateTime());
			miniClassStudentAttendentDao.save(miniClassStudentAttendent);
		}
		
	}
	
	/**
	   * 获取未报班合同产品和人次
	   * @return
	   */
	  public List<Map<Object, Object>> getMiniClassProductWithCount(MiniClassProductSearchVo miniClassProductSearchVo) {
		  Map<String, Object> params = new HashMap<String, Object>();
		  StringBuffer sql = new StringBuffer();
		  sql.append(" select cp.product_id as productId, p.PRO_STATUS as proStatus, p.VALID_END_DATE as validEndDate, p.name as productName, count(1) as peopleCount ");
		  sql.append(" from contract_product cp ");
		  sql.append(" inner join product p on cp.PRODUCT_ID = p.ID ");
		  sql.append(" inner join contract c on cp.CONTRACT_ID = c.ID ");
		  sql.append(" inner join student s on c.student_id = s.ID ");
		  sql.append("  where cp.ID not in(select contract_product_id from mini_class_student) and cp.type='SMALL_CLASS' ");
		  if (StringUtil.isNotBlank(miniClassProductSearchVo.getCampusId())) {
			  sql.append("  and s.BL_CAMPUS_ID = :campusId ");
			  params.put("campusId", miniClassProductSearchVo.getCampusId());
		  }else{
			  List<List<Organization>> campus=commonService.getCampusByLoginUser();
			  String[] campusId;
			  String id="";
			  for(List<Organization> orgs:campus){
				  for(int i=0;i<orgs.size();i++){
					  Organization org=orgs.get(i);
					  if(org.getOrgType()==OrganizationType.CAMPUS){
						  id+=org.getId()+",";
						  
					  }				  
				  }
			  }
			  campusId=id.split(",");
			  if(campusId.length>0){
				  sql.append("  and ( s.BL_CAMPUS_ID = '"+campusId[0]+"' ");
				  for(int j=1;j<campusId.length;j++){
					  sql.append(" or s.BL_CAMPUS_ID = '"+campusId[j]+"' ");
				  }
				  sql.append("  ) ");
			  }
			  
			  
		  }
		  if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductId())) {
			  sql.append("  and cp.PRODUCT_ID = :productId ");
			  params.put("productId", miniClassProductSearchVo.getProductId());
		  }
		  if (StringUtil.isNotBlank(miniClassProductSearchVo.getStudentId())) {
			  sql.append("  and c.STUDENT_ID = :studentId ");
			  params.put("studentId", miniClassProductSearchVo.getStudentId());
		  }
		  if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductVersionId())) {
			  sql.append("  and p.PRODUCT_VERSION_ID = :productVersionId ");
			  params.put("productVersionId", miniClassProductSearchVo.getProductVersionId());
		  }
		  if (StringUtil.isNotBlank(miniClassProductSearchVo.getProductQuarterId())) {
			  sql.append("  and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			  params.put("productQuarterId", miniClassProductSearchVo.getProductQuarterId());
		  }
		  sql.append(" and cp.STATUS <> 'CLOSE_PRODUCT' and cp.STATUS <> 'ENDED' AND cp.STATUS <> 'UNVALID' ");
		  sql.append("  group by product_id ");
		  return smallClassDao.findMapBySql(sql.toString(), params);
	  }
	  
	  /**
	   * 获取未报读（或报读有小班ID）当前合同产品关联小班的学生列表
	   * @param campusId
	   * @param productId
	   * @param miniClassId
	   * @return
	   */
	   public List<Map<Object, Object>> getStudentWithRemainingHours(String campusId, String productId, String miniClassId) {
		   Map<String, Object> params = new HashMap<String, Object>();
		   StringBuffer sql = new StringBuffer();
		   sql.append(" select s.ID as studentId, s.`NAME` as studentName, s.status as stuStatus, (case when cp.STATUS = 'CLOSE_PRODUCT' or cp.STATUS='ENDED' then 0 else (round(if((cp.PAID_AMOUNT-cp.CONSUME_AMOUNT)>0, (cp.PAID_AMOUNT-cp.CONSUME_AMOUNT), 0)/cp.PRICE , 2)) end) as remainingRealHours, ");
		   sql.append(" c.ID as contractId, concat('', cp.ID) as contractProductId, cp.PAID_STATUS as paidStatus, ");
		   sql.append(" (case when cp.STATUS = 'CLOSE_PRODUCT' or cp.STATUS='ENDED' then 0 else (case when cp.PAID_STATUS = 'PAID' then round(if(cp.CONSUME_AMOUNT > cp.PAID_AMOUNT, (cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT), cp.PROMOTION_AMOUNT)/cp.PRICE, 2) else 0 end) end) as remianingPromotionHours ");
		   sql.append(" from contract_product cp ");
		   if (StringUtil.isNotBlank(miniClassId)) {
			   sql.append(" inner join mini_class_student mcs on cp.ID = mcs.CONTRACT_PRODUCT_ID ");
		   }
		   sql.append(" inner join contract c on cp.CONTRACT_ID = c.ID ");
		   sql.append(" inner join student s on c.STUDENT_ID = s.ID ");
		   sql.append("  where 1=1 and cp.type='SMALL_CLASS' ");
		   
		   if (StringUtil.isNotBlank(miniClassId)) {
			   sql.append(" and mcs.MINI_CLASS_ID = :miniClassId ");
			   params.put("miniClassId", miniClassId);
			   sql.append(" and cp.ID in(select contract_product_id from mini_class_student) ");
		   } else {
			   sql.append(" and cp.ID not in(select contract_product_id from mini_class_student) ");
		   }
		   
		   if (StringUtil.isNotBlank(campusId)) {
			   sql.append("  and s.BL_CAMPUS_ID = :campusId ");
			   params.put("campusId", campusId);
		   } 
		   if (StringUtil.isNotBlank(productId)) {
			   sql.append("  and cp.PRODUCT_ID = :productId ");
			   params.put("productId", productId);
		   }
		   sql.append(" and cp.STATUS <> 'CLOSE_PRODUCT' and cp.STATUS <> 'ENDED' and cp.STATUS <> 'UNVALID' ");
		   sql.append(" order by s.ID ");
		   return smallClassDao.findMapBySql(sql.toString(), params);
	   }
	   
	   
	   /**
	    * 获取所选小班产品该校区关联的所有具体小班信息
	    * @param campusId
	    * @param productId
	    * @return
	    */
	   public List<MiniClassVo> getApplyMiniClassList(String campusId, String productId) {
		   Map<String, Object> params = new HashMap<String, Object>();
		   StringBuffer hql = new StringBuffer();
		   hql.append(" from MiniClass ");
		   hql.append(" where 1=1 ");
		   if (StringUtil.isNotBlank(campusId)) {
			   hql.append("  and blCampus.id = :campusId ");
			   params.put("campusId", campusId);
		   } 
		   if (StringUtil.isNotBlank(productId)) {
			   hql.append("  and miniClassId in (select miniClass.miniClassId from MiniClassProduct where product.id = :productId) ");
			   params.put("productId", productId);
		   }
		   hql.append("  and status != '" + MiniClassStatus.CONPELETE.getValue() + "' ");
		   hql.append(" and campusSale = 0 ");
		   List<MiniClass> list = smallClassDao.findAllByHQL(hql.toString(), params);
		   List<MiniClassVo> voList =  HibernateUtils.voListMapping(list, MiniClassVo.class);
		   for (MiniClassVo vo : voList) {
			   List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
			   StudentCriterionList.add(Restrictions.eq("miniClass.miniClassId", vo.getMiniClassId()));
			   vo.setMiniClassPeopleNum(miniClassStudentDao.findCountByCriteria(StudentCriterionList));
		   }
		   return voList;
	   }
	   
	   /**
		  * 获取未报读小班产品和剩余课时信息
		  * @param studentId
		  * @param dp
		  * @return
		  */
	    public DataPackage getProductWithRemainingHours(String studentId, DataPackage dp) {
	    	Map<String, Object> params = new HashMap<String, Object>();
	    	StringBuffer sql = new StringBuffer();
	    	sql.append(" select concat('', cp.ID) as contractProductId, c.ID as contractId, p.id as productId, p.name as productName, dd_cs.`NAME` as courseSeries, dd_pv.`NAME` as productVersion, dd_pq.`NAME` as productQuarter, dd_g.`NAME` as grade, ");
	    	sql.append(" (case when cp.STATUS = 'CLOSE_PRODUCT' or cp.STATUS='ENDED' then 0 else (round(if((cp.PAID_AMOUNT-cp.CONSUME_AMOUNT)>0, (cp.PAID_AMOUNT-cp.CONSUME_AMOUNT), 0)/cp.PRICE , 2)) end) as remainingRealHours, cp.PAID_STATUS as paidStatus, ");
	    	sql.append(" (case when cp.STATUS = 'CLOSE_PRODUCT' or cp.STATUS='ENDED' then 0 else (case when cp.PAID_STATUS = 'PAID' then round(if(cp.CONSUME_AMOUNT > cp.PAID_AMOUNT, (cp.PAID_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT), cp.PROMOTION_AMOUNT)/cp.PRICE, 2) else 0 end) end) as remianingPromotionHours ");
	    	sql.append(" from contract_product cp ");
	    	sql.append(" inner join contract c on cp.CONTRACT_ID = c.ID ");
	    	sql.append(" inner join product p on cp.PRODUCT_ID = p.ID ");
	    	sql.append(" left join data_dict dd_cs on p.COURSE_SERIES_ID = dd_cs.ID ");
	    	sql.append(" left join data_dict dd_pv on p.PRODUCT_VERSION_ID = dd_pv.ID ");
	    	sql.append(" left join data_dict dd_pq on p.PRODUCT_QUARTER_ID = dd_pq.ID ");
	    	sql.append(" left join data_dict dd_g on p.GRADE_ID = dd_g.ID ");
	    	sql.append("  where 1=1 and cp.type='SMALL_CLASS' ");
	   
	    	if (StringUtil.isNotBlank(studentId)) {
	    		sql.append(" and cp.ID not in(select contract_product_id from mini_class_student where STUDENT_ID = :studentId) ");
	    		sql.append("  and c.STUDENT_ID = :studentId2 ");
	    		params.put("studentId", studentId);
	    		params.put("studentId2", studentId);
	    	}
	   
	    	sql.append(" and cp.STATUS <> 'CLOSE_PRODUCT' and cp.STATUS <> 'ENDED' ");
	    	List<Map<Object, Object>> list = smallClassDao.findMapOfPageBySql(sql.toString(), dp, params);
	    	dp.setDatas(list);
	    	dp.setRowCount(list.size());
	    	return dp;
	    }
	    
	    /**
		  * 获取已报读小班产品、剩余课时和小班信息
		  * @param studentId
		  * @param dp
		  * @return
		  */
	   public DataPackage getProductWithMiniClass(String studentId, DataPackage dp) {
		   Map<String, Object> params = new HashMap<String, Object>();
		   StringBuffer sql = new StringBuffer();
	    	sql.append(" select concat('', cp.ID) as contractProductId, p.ID as productId, p.name as productName, dd_cs.`NAME` as courseSeries, dd_pv.`NAME` as productVersion, dd_pq.`NAME` as productQuarter, dd_g.`NAME` as grade, ");
	    	sql.append(" round(if((cp.REAL_AMOUNT-cp.CONSUME_AMOUNT)>0, (cp.REAL_AMOUNT-cp.CONSUME_AMOUNT), 0)/cp.PRICE , 2) as remainingRealHours, ");
	    	sql.append(" case when cp.PAID_STATUS = 'PAID' then round(if(cp.CONSUME_AMOUNT > cp.REAL_AMOUNT, (cp.REAL_AMOUNT + cp.PROMOTION_AMOUNT - cp.CONSUME_AMOUNT), cp.PROMOTION_AMOUNT)/cp.PRICE, 2) else 0 end as remianingPromotionHours, ");
	    	sql.append(" mcs.CREATE_TIME as applyTime, mc.MINI_CLASS_ID as miniClassId, mc.`NAME` as miniClassName, org.`name` as campusName, mc.`STATUS` as miniClassStatus ");
	    	sql.append(" from contract_product cp ");
	    	sql.append(" inner join contract c on cp.CONTRACT_ID = c.ID ");
	    	sql.append(" inner join product p on cp.PRODUCT_ID = p.ID ");
	    	sql.append(" left join data_dict dd_cs on p.COURSE_SERIES_ID = dd_cs.ID ");
	    	sql.append(" left join data_dict dd_pv on p.PRODUCT_VERSION_ID = dd_pv.ID ");
	    	sql.append(" left join data_dict dd_pq on p.PRODUCT_QUARTER_ID = dd_pq.ID ");
	    	sql.append(" left join data_dict dd_g on p.GRADE_ID = dd_g.ID ");
	    	sql.append(" inner join mini_class_student mcs on cp.ID = mcs.CONTRACT_PRODUCT_ID ");
	    	sql.append(" inner join mini_class mc on mcs.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
	    	sql.append(" inner join organization org on mc.BL_CAMPUS_ID = org.id ");
	    	sql.append("  where 1=1 and cp.type='SMALL_CLASS' ");
	   
	    	if (StringUtil.isNotBlank(studentId)) {
	    		sql.append(" and cp.ID in(select contract_product_id from mini_class_student where STUDENT_ID = :studentId) ");
	    		sql.append("  and c.STUDENT_ID = :studentId2 ");
	    		params.put("studentId", studentId);
	    		params.put("studentId2", studentId);
	    	}
	   
	    	List<Map<Object, Object>> list = smallClassDao.findMapOfPageBySql(sql.toString(), dp, params);
	    	dp.setDatas(list);
	    	dp.setRowCount(list.size());
	    	return dp;
	   }
	   
	   /**
		  * 刷选小班
		  * @param productChooseVo
		  * @return
		  */
	   public DataPackage findMiniClassForChoose(DataPackage dp, ProductChooseVo productChooseVo) {
		   String currentDate = DateTools.getCurrentDate();
		   List<Criterion> criterions = new ArrayList<Criterion>();
		   if(productChooseVo.getExpressions() != null){
			   for(String expression : productChooseVo.getExpressions()){
				   criterions.add(productChooseViewDao.buildCriterion(expression));
			   }
		   }
		   // 产品类别
		   if(productChooseVo.getProduct() != null){
			   if(productChooseVo.getProduct().getCategory() != null){
				   criterions.add(Restrictions.eq("product.category",productChooseVo.getProduct().getCategory()));
			   }
//	           if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductCategoryId())) {
//	        		criterions.add(Restrictions.eq("product.productCategoryId.id",productChooseVo.getProduct().getProductCategoryId()));
//	        	}
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getCourseSeriesId())) {
				   criterions.add(Restrictions.eq("product.courseSeries.id",productChooseVo.getProduct().getCourseSeriesId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductVersionId())) {
				   criterions.add(Restrictions.eq("product.productVersion.id",productChooseVo.getProduct().getProductVersionId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductQuarterId())) {
				   criterions.add(Restrictions.eq("product.productQuarter.id",productChooseVo.getProduct().getProductQuarterId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getSubjectId())) {
				   criterions.add(Restrictions.eq("product.subject.id",productChooseVo.getProduct().getSubjectId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getClassTypeId())) {
				   criterions.add(Restrictions.eq("product.classType.id",productChooseVo.getProduct().getClassTypeId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductGroupId())) {
				   criterions.add(Restrictions.eq("product.productGroup.id",productChooseVo.getProduct().getProductGroupId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeId())) {
				   criterions.add(Restrictions.eq("product.gradeDict.id",productChooseVo.getProduct().getGradeId()));
			   }
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeSegmentId())) {
				   criterions.add(Restrictions.eq("product.gradeSegmentDict.id",productChooseVo.getProduct().getGradeSegmentId()));
			   }
			   criterions.add(Restrictions.isNotNull("miniClass.miniClassId"));
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getCourseSeriesId())) {
				   criterions.add(Restrictions.eq("miniClass.recruitStudentStatus.id", productChooseVo.getMiniClass().getRecruitStudentStatusId()));
			   }
			   criterions.add(Restrictions.eq("miniClass.campusSale", 0));
			   if (StringUtils.isNotBlank(productChooseVo.getProduct().getId())) {
				   Map<String, Object> params = new HashMap<String, Object>();
				   String hql = " from MiniClassProduct where product.id = :productId ";
				   params.put("productId", productChooseVo.getProduct().getId());
				   List<MiniClassProduct> miniClassProducts = miniClassProductDao.findAllByHQL(hql, params);
				   if (miniClassProducts.size() > 0) {
					   String miniClassIds[]  =  new String[miniClassProducts.size()];
					   int i = 0;
					   for (MiniClassProduct mp : miniClassProducts) {
						   miniClassIds[i] = mp.getMiniClass().getMiniClassId();
						   i++;
					   }
					   criterions.add(Restrictions.in("miniClass.miniClassId", miniClassIds));
				   }
			   }
		   }
		   // 排除已禁用的产品
		   criterions.add(Restrictions.ne("product.proStatus","1"));
		   // 排除不在有效期内的产品
		   criterions.add(Restrictions.ge("product.validEndDate", currentDate));
		   criterions.add(Restrictions.le("product.validStartDate", currentDate));
		   // 本公司的产品和本校区的小班
		   criterions.add(Restrictions.eq("product.organization.id", userService.getBelongBranch().getId()));
		   // 产品组织架构向上兼容
//	        criterions.add(Restrictions.sqlRestriction("(select orgLevel from organization where id = '"+userService.getCurrentLoginUser().getOrganizationId()+"') like concat((select orgLevel from organization where id = (select organization_id from product where id = product_id)),'%')"));
		   criterions.add(Restrictions.or(Restrictions.eq("miniClass.blCampus.id", userService.getBelongCampus().getId()),Restrictions.isNull("miniClass.blCampus.id")));

		   List<Order> orders = new ArrayList<Order>();
		   dp = productChooseViewDao.findPageByCriteria(dp,orders,criterions);
		   dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),ProductChooseVo.class));
		   for(Object iteratorObj : dp.getDatas()){
			   ProductChooseVo iteratorVo = (ProductChooseVo) iteratorObj;
			   MiniClassVo miniClass = iteratorVo.getMiniClass();
			   if(miniClass != null){
				   miniClass.setNextMiniClassTime(miniClassCourseDao.getTop1MiniClassCourseByDate(currentDate,miniClass.getMiniClassId()));
				   List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
				   StudentCriterionList.add(Restrictions.eq("miniClass.miniClassId", miniClass.getMiniClassId()));
				   miniClass.setMiniClassPeopleNum(miniClassStudentDao.findCountByCriteria(StudentCriterionList));
			   }
		   }
		   return dp;
	   }
	   
	   /**
		 * 小班课程审批
		 */
		public void miniClassCourseAudit(String courseId, String auditStatus) {
			MiniClassCourse miniCourse = miniClassCourseDao.findById(courseId);
			miniCourse.setAuditStatus(AuditStatus.valueOf(auditStatus));
			miniClassCourseDao.save(miniCourse);
			log.info(miniCourse.getMiniClassCourseId()+",状态："+miniCourse.getCourseStatus());
		}
	
		/**
		 * 小班考勤前判断
		 */
						
		
		public void miniClassCourseBeforAttendance(String miniClassCourseId) throws Exception {
			this.checkMiniClassCourseConflict(miniClassCourseId);
		}
		
		/**
		 *小班考勤前判断
		 */
		public void miniClassCourseBeforAttendance(List<MiniClassStudentAttendentVo> miniStudent,String teacherId,String courseId) {
			this.checkMiniClassCourseConflict(courseId);
		}
		
		private void checkMiniClassCourseConflict(String miniClassCourseId) {
			if(StringUtil.isNotBlank(miniClassCourseId)){
				MiniClassCourse miniCourse = miniClassCourseDao.findById(miniClassCourseId);					
				List<MiniClassStudentAttendent> mccsList= miniClassStudentAttendentDao.getMiniClassAttendsByCourseId(miniClassCourseId);
				String teacherName = "";
				String studentName = "";
				boolean isConflict = false;
				for (MiniClassStudentAttendent mcsa : mccsList) {
					int count = courseConflictDao.findCourseConflictCount(miniClassCourseId, mcsa.getStudentId(), miniCourse.getTeacher().getUserId());
					if (count > 0) {
						isConflict = true;
						teacherName = miniCourse.getTeacher().getName();
						studentName += studentDao.findById(mcsa.getStudentId()).getName();
					}
				}
				if (isConflict) {
					throw new ApplicationException("课程存在以下学生老师冲突，请移步学生老师课程表确认并修改后再考勤。老师：" + teacherName + ", 学生：" + studentName);
				}
			}
		}
		
		/**
		 * 小班学生花名册
		 */
		@Override
		public MiniClassCourseStudentRosterVo getMiniClaCourseStudentRoster(String miniClassId) {
			List<MiniClassStudent> studentList = miniclassStudentDao.getMiniClassStudent(miniClassId);
			List<MiniClassCourse> courseList =  miniClassCourseDao.getMiniClassCourseListByMiniClassId(miniClassId);
			MiniClassCourseStudentRosterVo vo = new MiniClassCourseStudentRosterVo();
			vo.setMiniClassId(miniClassId);
			MiniClass mc = miniClassDao.findById(miniClassId);
			if(mc==null ){
				throw new ApplicationException("小班不存在，请重新刷新页面！");
			}
			vo.setTeacherName(mc.getTeacher() != null ? mc.getTeacher().getName() : "");
			vo.setMiniClassName(mc.getName());
			vo.setClassTime(mc.getClassTime());
			vo.setEveryCourseClassNum(mc.getEveryCourseClassNum());
			vo.setClassTimeLength(mc.getClassTimeLength());
			ClassroomManage classroom = mc.getClassroom();
			if (classroom!=null){
				vo.setClassroomName(classroom.getClassroom());
			}else {
				vo.setClassroomName("");
			}
			int studentSize = studentList.size() <= 30 ? studentList.size() : 30;
			String studentNames[] = new String[studentSize];

			String studentIds[] = new String[studentSize];
			int i = 0;
			for (MiniClassStudent mcStudent : studentList) {
				if (i < studentSize) {
					Student student = mcStudent.getStudent();
					studentNames[i] = student.getName();
					studentIds[i] = student.getId();
				}
				i++;
			}
            if(ArrayUtils.isNotEmpty(studentIds)){
    			String studentContacts[]  = miniclassStudentDao.getCustomerContactByStuIds(studentIds,miniClassId);
    			if(studentContacts!=null && ArrayUtils.isNotEmpty(studentIds)){
    				vo.setStudentContacts(studentContacts);
    			}
            }

			vo.setStudentNames(studentNames);

			int courseSize = courseList.size() <= 16 ? courseList.size() : 16;
			i = 0;
			String courseDates[] = new String[courseSize];
			if (courseList.size() > 0) {
				vo.setStartDate(courseList.get(0).getCourseDate());
				vo.setEndDate(courseList.get(courseList.size() - 1).getCourseDate());
			} else {
				vo.setStartDate(mc.getStartDate());
			}
			for (MiniClassCourse course : courseList) {
				if (i < courseSize) {
					String coruseDate = course.getCourseDate();
					String monthTmp = coruseDate.substring(5, 7);
				    if (monthTmp.substring(0, 1).equals("0")) {
				    	monthTmp = monthTmp.substring(1);
				    }
				    String dateTmp = coruseDate.substring(8, 10);
				    if (dateTmp.substring(0, 1).equals("0")) {
				    	dateTmp = dateTmp.substring(1);
				    }
				    courseDates[i] = monthTmp + "." + dateTmp;
				}
				i++;
			}
			vo.setCourseDates(courseDates);
			return vo;
		}

	/**
	 * 修改小班学生考勤信息
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Response changeMccStudentAttendance(String miniClassCourseId, String attendanceData) throws Exception {
		Response res = new Response();
		// 解析JSON
		ObjectMapper objectMapper = new ObjectMapper();
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, MiniClassStudentAttendentVo.class);
		List<MiniClassStudentAttendentVo> inputList =  (List<MiniClassStudentAttendentVo>)objectMapper.readValue(attendanceData, javaType);
		// 循环设每个学生考勤
		for (int i = 0; i < inputList.size(); i++) {
			MiniClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);
			// 更新考勤记录
			updateMiniClassAttendanceRecord(miniClassCourseId, eachInputStuAttendentVo, "change");
		}
		return res;
	}

	@Override
   public void updateCourseNumByClassId(String miniClassId){
	   List<MiniClassCourse> list=findMiniClassCourseByClassId(miniClassId);
	   int i=1;
	   for(MiniClassCourse m:list){
	   	 m.setCourseNum(i);
	   	 miniClassCourseDao.save(m);
		   log.info(m.getMiniClassCourseId()+",状态："+m.getCourseStatus());
	   	 i++;
	   }
   }

    @Override
    public DataPackage findMiniClassByPromiseSubjectId(DataPackage dp, Map<String, Object> paramMap) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from MiniClass where blCampus.id=:organization")
                .append(" and subject.id=:subject")
                .append(" and grade.id=:grade")
                .append(" and product.productQuarter.id =:quarter")

                .append(" and totalClassHours>="+paramMap.get("hours"));
        sql.append(" and campusSale=0 ");
        paramMap.remove("hours");
        if(paramMap.get("teacherId")!=null){
            sql.append(" and teacher.userId=:teacherId");
        }
        if(paramMap.get("version")!=null){
        	sql.append(" and product.productVersion.id =:version");
		}
        if(paramMap.get("name")!=null){
            sql.append(" and name like :name");
        }
        if(paramMap.get("miniClassId")!=null){
            sql.append(" and miniClassId <> :miniClassId");
        }

        sql.append(" order by createTime desc ");
        dp= smallClassDao.findPageByHQL(sql.toString(),dp,true,paramMap);
        List<MiniClassVo> list=HibernateUtils.voListMapping((List) dp.getDatas(),MiniClassVo.class);
        dp.setDatas(list);
        return dp;
    }

	@Override
	public List<Map<Object,Object>> findMiniClassTeacherForSelect(String name) {
   		Map<String,Object> paramMap = new HashMap<>();
   		StringBuilder hql = new StringBuilder();
   		hql.append("select user.user_id userId,user.account,user.name from mini_class mc  ")
				.append(" left join user user on user.user_id = mc.teacher_id")
				.append(" left join organization o on o.id = mc.BL_CAMPUS_ID")
				.append(" where 1=1 and mc.status<>'CONPELETE'");

		List<Organization> userOrganizations= userService.getCurrentLoginUser().getOrganization();
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			hql.append("   and (");
			hql.append(" o.orgLevel like '").append(org.getOrgLevel()).append("%'");
			for(int i = 1; i < userOrganizations.size(); i++){
				hql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			hql.append(" )");
		}

		if(StringUtil.isNotBlank(name)){
			hql.append(" and user.name like '%"+ name +"%'");
		}

		hql.append(" group by user.user_id ");

		return miniClassDao.findMapBySql(hql.toString(),paramMap);
	}


	public List<MiniClassCourse> findMiniClassCourseByClassId(String classId){
	   StringBuffer hql = new StringBuffer();
	   hql.append(" from MiniClassCourse  where courseStatus<>'CANCEL' ");
	   Map<String, Object> params = Maps.newHashMap();
	   if (StringUtils.isNotBlank(classId)) {
		   hql.append(" and miniClass.miniClassId = :miniClassId ");
		   params.put("miniClassId", classId);
	   }

	   hql.append(" order by courseDate ");
	   return miniClassCourseDao.findAllByHQL(hql.toString(),params);
   }
   
	@Override
	public Map<String, Object> chargeAttentanceInfo(List<AttentanceInfoVo> attentanceInfoVos, String type,String userId)
			throws Exception {
		Map<String, Object> map = new HashMap<>();

		if (StringUtil.isNotBlank(type) && UpdateAttentanceInfoVo.Type.CHARGE.equals(type)) {

			if (attentanceInfoVos == null || attentanceInfoVos.size() == 0) {
				map.put("resultStatus", 401);
				map.put("resultMessage", "数据有问题导致无法更新");
				map.put("result", "fail");
				return map;
			}

			String miniClassCourseId = attentanceInfoVos.get(0).getCourseId();
			MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
			if (miniClassCourse == null) {
				map.put("resultStatus", 401);
				map.put("resultMessage", "数据有问题导致无法更新");
				map.put("result", "fail");
				return map;
			}
			if (miniClassCourse.getCourseStatus() == CourseStatus.CHARGED) {
				map.put("resultStatus", 401);
				map.put("resultMessage", "课程已经结算");
				map.put("result", "fail");
				return map;
			}
			// 先检查学生的扣费状态
			List<MiniClassStudentAttendentVo> inputList = new ArrayList<>();
			MiniClassStudentAttendentVo mAttendentVo = null;
			for (AttentanceInfoVo attentanceInfoVo : attentanceInfoVos) {
				log.info("教学传入参数：courseId:" + attentanceInfoVo.getCourseId() + "--studentId:"
						+ attentanceInfoVo.getStudentId() + "attanceStatus:" + attentanceInfoVo.getStatus());
				mAttendentVo = new MiniClassStudentAttendentVo();
				mAttendentVo.setStudentId(attentanceInfoVo.getStudentId());
				mAttendentVo.setAttendanceType(attentanceInfoVo.getStatus());
				mAttendentVo.setMiniClassCourseId(attentanceInfoVo.getCourseId());
				inputList.add(mAttendentVo);
			}
			// 过滤已经考勤的学生
			inputList = deleteChargeStudent(miniClassCourseId, inputList);

			// 是否有操作权限
			if (miniClassCourse.getAuditStatus() == AuditStatus.UNVALIDATE) {
				miniClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
			}

			// 检测冲突
			this.miniClassCourseBeforAttendance(inputList, miniClassCourse.getTeacher().getUserId(), miniClassCourseId);

			if (!checkMiniClassCourseAttends(miniClassCourseId, inputList, miniClassCourse)) {
				map.put("resultStatus", 400);
				map.put("resultMessage", "小班扣费需全员扣费，包括考勤状态为迟到缺勤的学生");
				map.put("result", "fail");
				return map;
			}
			String oprationCode = "charge";
			permission4MiniClassCourseAttendanceOperation(miniClassCourse.getTeacher().getUserId(),
					miniClassCourse.getStudyHead().getUserId(), oprationCode);

			if (null == miniClassCourse.getClassroom() || null == miniClassCourse.getClassroom().getId()) {
				map.put("resultStatus", 400);
				map.put("resultMessage", "小班课程需关联具体教室才可以考勤");
				map.put("result", "fail");
				return map;
			}

			List<String> studentIds = new LinkedList<>();
			List<String> studentNames = new LinkedList<>();

			String stuName = "";
			List<MiniClassStudentAttendentVo> chargeList = new ArrayList<>();
			for (int i = 0; i < inputList.size(); i++) {
				MiniClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);

				// 小班扣费
				// 获取 targe Contract product
				ContractProduct targetConPrd = null;
				MiniClass miniClass = miniClassCourse.getMiniClass();
				MiniClassStudent miniClassStudent = miniClassStudentDao
						.getOneMiniClassStudent(miniClass.getMiniClassId(), eachInputStuAttendentVo.getStudentId());
				targetConPrd = miniClassStudent.getContractProduct();
				targetConPrd.getType();
				// 获取到课时 和 小班Product
				Double courseHour = miniClassCourse.getCourseHours();
				if (targetConPrd != null && (targetConPrd.getStatus() == ContractProductStatus.STARTED
						|| targetConPrd.getStatus() == ContractProductStatus.NORMAL)) {
					BigDecimal remainingAmount = targetConPrd.getRemainingAmount();

					// 旧数据 可能会是 price 价格为 0
					BigDecimal price = targetConPrd.getPrice() == null ? BigDecimal.ZERO : targetConPrd.getPrice();
					// 总价格 = 单价 * 课时 * 折扣
					BigDecimal detel = price.multiply(targetConPrd.getDealDiscount())
							.multiply(BigDecimal.valueOf(courseHour));
					Student stu = targetConPrd.getContract().getStudent();
					if (remainingAmount.compareTo(detel) >= 0
							&& (targetConPrd.getStatus() == ContractProductStatus.NORMAL
									|| targetConPrd.getStatus() == ContractProductStatus.STARTED)) {
						chargeList.add(eachInputStuAttendentVo);
					} else {
						if (ProductType.ECS_CLASS == targetConPrd.getType()) {
							// 目标班学生不参与判断
							chargeList.add(eachInputStuAttendentVo);
						} else {
							stuName += stu.getName() + ",";
							studentIds.add(stu.getId());
							studentNames.add(stu.getName());
						}
					}

				}
			}

			if (StringUtils.isNotBlank(stuName)) {
				map.put("resultStatus", 405);
				map.put("resultMessage", "检查到以下同学课时费不足："+stuName.substring(0, stuName.length()-1)+"，请让班级管理员处理资金异常问题后，重新在晓助手点名考勤");
//				map.put("studentId", studentIds);
//				map.put("studentName", studentNames);
				// 部分考勤失败
				return map;
			} else {
				map.put("resultStatus", 200);
				map.put("resultMessage", "更新成功");
				map.put("result", "success");
			}
			// 循环设每个学生考勤
			for (int i = 0; i < chargeList.size(); i++) {
				MiniClassStudentAttendentVo eachInputStuAttendentVo = chargeList.get(i);

				// 更新考勤记录

				//updateMiniClassAttendanceRecord(miniClassCourseId, eachInputStuAttendentVo, oprationCode);
				updateMiniClassAttendanceRecordByUser(miniClassCourseId, eachInputStuAttendentVo, oprationCode,userId);
				
				User user = userService.loadUserById(userId);
				if(user==null){
					throw new ApplicationException("更新考勤失败，找不到考勤操作人");
				}
				
				// 小班扣费
				//boolean chargeResult = miniClassCourseCharge(miniClassCourseId, eachInputStuAttendentVo);
				
				boolean chargeResult = miniClassCourseCharge_Edu(miniClassCourseId, eachInputStuAttendentVo,user);
				if(chargeResult){
					changeConsumeCourseNum(miniClassCourse.getMiniClass().getMiniClassId(),eachInputStuAttendentVo.getStudentId(),miniClassCourse.getCourseHours());
				}
				// 更新小班状态
				if (chargeResult && (i == chargeList.size() - 1)) {
					// 课程表添加考勤时间,只取第一次考勤时间
					if (miniClassCourse.getFirstAttendTime() == null) {
						miniClassCourse.setFirstAttendTime(DateTools.getCurrentDateTime());
					}
					miniClassCourse.setTeacherAttendTime(DateTools.getCurrentDateTime());
					smallClassDao.flush();
					if (StringUtils.isNotBlank(stuName)) {
						updateMiniClassStatusByPartStudent(miniClassCourseId);
					} else {
						// 如果是
						updateMiniClassStatus(miniClassCourseId);
					}

				}
			}

			log.info("小班扣费" + miniClassCourseId + ": " + "按照用户角色更新小班的考勤记录" + "当前小班状态"
					+ miniClassCourse.getCourseStatus().getName());

			// 对学生进行扣费
			// 更新小班学生的考勤信息
			Map<String, Object> params = Maps.newHashMap();
			for (AttentanceInfoVo attentanceInfoVo : attentanceInfoVos) {
				miniClassStudentAttendentDao.excuteSql(
						"update mini_class_student_attendent set ATTENDENT_STATUS = '" + attentanceInfoVo.getStatus()
								+ "' where MINI_CLASS_COURSE_ID ='" + attentanceInfoVo.getCourseId()
								+ "' and STUDENT_ID ='" + attentanceInfoVo.getStudentId() + "'",
						params);
			}
			return map;
		} else {
			Map<String, Object> params = Maps.newHashMap();
			for (AttentanceInfoVo attentanceInfoVo : attentanceInfoVos) {
				miniClassStudentAttendentDao.excuteSql(
						"update mini_class_student_attendent set ATTENDENT_STATUS = '" + attentanceInfoVo.getStatus()
								+ "' where MINI_CLASS_COURSE_ID ='" + attentanceInfoVo.getCourseId()
								+ "' and STUDENT_ID ='" + attentanceInfoVo.getStudentId() + "'",
						params);
			}
			map.put("resultStatus", 200);
			map.put("resultMessage", "更新成功");
			map.put("result", "success");
			return map;
		}
	}
	
	
	@Override
	public int updateAttentanceInfo(List<AttentanceInfoVo> attentanceInfoVos) throws Exception{
		
		if(attentanceInfoVos==null ||attentanceInfoVos.size()==0){
			throw new ApplicationException("401");
		}
		
		String courseId = attentanceInfoVos.get(0).getCourseId();
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(courseId);
		if(miniClassCourse==null){
			throw new ApplicationException("401");
		}
		if(miniClassCourse.getCourseStatus()==CourseStatus.CHARGED){
			return 0;
		}
		
		Map<String, Object> params = Maps.newHashMap();
		
		
		for(AttentanceInfoVo attentanceInfoVo:attentanceInfoVos){
			miniClassStudentAttendentDao.excuteSql("update mini_class_student_attendent set ATTENDENT_STATUS = '"+attentanceInfoVo.getStatus()+"' where MINI_CLASS_COURSE_ID ='"+attentanceInfoVo.getCourseId()+"' and STUDENT_ID ='"+attentanceInfoVo.getStudentId()+"'",params);
		}
		
		return 1;
		
		//sql test异常
//		AttentanceInfoVo attentanceInfoVo = null;
//		for(int i=0;i<attentanceInfoVos.size();i++){
//			attentanceInfoVo = attentanceInfoVos.get(i);
//			miniClassStudentAttendentDao.excuteSql("update mini_class_student_attendent set ATTENDENT_STATUS = '"+attentanceInfoVo.getStatus()+"' where MINI_CLASS_COURSE_ID ='"+attentanceInfoVo.getCourseId()+"' and STUDENT_ID ='"+attentanceInfoVo.getStudentId()+"'");
//		    if(i==2){
//		    	throw new ApplicationException("test exception roll back");
//		    }
//		}
		
//		//hql方式
//		String hql = null;
//		MiniClassStudentAttendent miniClassStudentAttendent = null;
//		for(AttentanceInfoVo attentanceInfoVo:attentanceInfoVos){
//			hql = "from MiniClassStudentAttendent m where m.miniClassCourse.miniClassCourseId ='"+attentanceInfoVo.getCourseId()+"' and m.studentId='"+attentanceInfoVo.getStudentId()+"'";
//			miniClassStudentAttendent=miniClassStudentAttendentDao.findOneByHQL(hql);
//			miniClassStudentAttendent.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.valueOf(attentanceInfoVo.getStatus()));
//			miniClassStudentAttendentDao.save(miniClassStudentAttendent);
//		}
	}
	
	private void updateMiniClassStatusByPartStudent(String miniClassCourseId) {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		MiniClass miniClass = smallClassDao.findById((miniClassCourse.getMiniClass().getMiniClassId()));
		
		if (null == miniClass.getConsume()) {
			miniClass.setConsume(0.0);
		}
		
		//设置扣费时间
		miniClassCourse.setStudyManageChargeTime(DateTools.getCurrentDateTime());
		
		// 第一次上课则更新小班状态
		if(miniClass.getConsume() == 0){
			miniClass.setStatus(MiniClassStatus.STARTED);
		}
		if(miniClassCourse.getCourseStatus()!=null) {
			miniClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
			miniClassCourseDao.save(miniClassCourse);
			miniClassCourseDao.flush();
		}
		log.info("小班扣费" + miniClassCourseId + ": " + "更新小班状态" + "当前小班状态" +  miniClassCourse.getCourseStatus().getName());
		
		// 更新已上课时
		miniClass.setConsume(miniClassStudentAttendentDao.findConsumeCount(miniClassCourse));
		// 最后一次上课  小班是固定课时且消耗课时等于总课时数 结班
		//#1733 固定课时和延时课时上完后都要修改下班状态
		/*if(MiniClassCourseType.CONSTANT.equals(miniClass.getMiniClassCourseType()) && miniClass.getConsume().equals(miniClass.getTotalClassHours())){
			miniClass.setStatus(MiniClassStatus.CONPELETE);
		}*/
		if(miniClass.getConsume().equals(miniClass.getTotalClassHours())){
			miniClass.setStatus(MiniClassStatus.CONPELETE);
		}
		smallClassDao.save(miniClass);
		smallClassDao.flush();
	}


	/**
	 * 根据小班ids获取小班列表
	 */
	@Override
    public List<MiniClassVo> getMiniClassListByIds(String miniClassIds) {
        List<MiniClassVo> voList = null;
        List<MiniClass> list = miniClassDao.getMiniClassListByIds(miniClassIds);
        if (list != null && !list.isEmpty()) {
            voList = new ArrayList<MiniClassVo>();
            for (MiniClass mc : list) {
                MiniClassVo vo = HibernateUtils.voObjectMapping(mc, MiniClassVo.class);
                if (StringUtil.isNotBlank(mc.getCourseStartTime())) {
                    vo.setStartDate(mc.getCourseStartTime());
                }
                if (StringUtil.isNotBlank(mc.getCourseEndTime())) {
                    vo.setEndDate(mc.getCourseEndTime());
                }
                voList.add(vo);
            }
        }
        return voList;
    }

	@Override
	public Response changeMiniClassSaleType(String miniClassIds, int campusSale, int onlineSale,String remark) throws Exception {
		List<MiniClass> teacherList = miniClassDao.getNoTeacherMiniClassListByIds(miniClassIds,true);
		User user= userService.getCurrentLoginUser();
		String userId = user!=null?user.getUserId():"";
		String newText=getTypeText(campusSale,onlineSale);
//		String onlineClassIds = "";
		String offMiniClassIds= "";
		for(MiniClass m:teacherList){
			String oldText=getTypeText(m.getCampusSale(),m.getOnlineSale());
			if(oldText.equals(newText)){
				continue;
			}else if(onlineSale==1 && m.getOnlineSale()==0 && m.getOnShelves()!=3){//下架
			    m.setOnlineSale(onlineSale);
				m.setOnShelves(3);
				offMiniClassIds += m.getMiniClassId() + ",";
			}else if(onlineSale==0 && m.getOnlineSale()==1 && m.getTeacher()!=null){//小班有老师才同步过去
				m.setOnShelves(0);//待上架
//			    onlineClassIds += m.getMiniClassId() + ",";
			    // 同步小班到在线报读
			    if (!this.syncMiniClassToMobile(m)){
			    	continue;
				}
			}
			Map<String,Object> map = new HashMap<>();
			map.put("oldText",oldText);
			map.put("newText",newText);
			map.put("remark",remark);
			map.put("userId",userId);
			map.put("createTime",DateTools.getCurrentDateTime());
			map.put("miniClassId",m.getMiniClassId());
			String sql="insert into mini_class_change_sale_type_log(mini_class_id,old_Type,new_type,remark,CREATE_USER_ID,CREATE_TIME) values(:miniClassId, :oldText,:newText,:remark,:userId,:createTime)";
			miniClassDao.excuteSql(sql,map);
			m.setCampusSale(campusSale);
			if(onlineSale==0 && m.getOnlineSale()==1 && m.getTeacher()!=null) {
		        m.setOnlineSale(onlineSale);
			}
			if (onlineSale == 0 && m.getOnlineSale() == 0 && m.getTeacher() == null) { // 没有老师的要下架
				m.setOnlineSale(1);
			}
			
			m.setModifyTime(DateTools.getCurrentDateTime());
			m.setModifyUserId(userId);
			miniClassDao.save(m);
		}
		//同步到线上
		/*if (StringUtils.isNotBlank(onlineClassIds)) {
		    onlineClassIds = onlineClassIds.substring(0, onlineClassIds.length() -1);
            return this.syncMiniClassToMobile(miniClassIds);
		}*/
		// 线上下架操作
		if (StringUtils.isNotBlank(offMiniClassIds)) {
		    offMiniClassIds = offMiniClassIds.substring(0, offMiniClassIds.length() -1);
		    bossCourseRpc.updateCourseStatusByMiniClassIds(offMiniClassIds, "SYSTEM_OFF");
		}
		return new Response();
	}

	@Override
    public void updateMiniClassSaleType(String miniClassIds, int onlineSale) {
        miniClassDao.updateMiniClassSaleType(miniClassIds, onlineSale);
    }

	@Override
    public Map<Object, Object> findCourseStatusNumVoByOrgId(String orgId) {
        return miniClassDao.findCourseStatusNumVoByOrgId(orgId);
    }

    @Override
    public Response checkOnlineSaleBrench(String[] campusId) {
		Response res = new Response();
		String resultMsg="";
		Map<String,String > map = new HashMap<>();
		for(String campus:campusId){
			if(map.get(campus)==null) {
				map.put(campus,campus);
				Organization brench=organizationService.findBrenchByCampusId(campus);
				if(brench!=null && !bossCourseRpc.checkOrgInfoExit(PropertiesUtils.getStringValue("institution"), brench.getId()) && !resultMsg.contains(brench.getName())){
					resultMsg+=brench.getName()+"、";
				}
			}
		}

		if(StringUtils.isNotBlank(resultMsg)){
			res.setResultCode(-1);
			res.setResultMessage(resultMsg.substring(0,resultMsg.length()-1)+"未在在线报读平台添加，该分公司所有校区小班将不能同步，请先添加该分公司；");
		}

        return res;
    }

    public String getTypeText(int campusSale, int onlineSale){
		if(campusSale==0 && onlineSale==0 ){
			return "同时销售";
		}
		if(campusSale==1 && onlineSale==0 ){
			return  "线上销售";
		}
		if(campusSale==0 && onlineSale==1 ){
			return  "校区销售";
		}
		if(campusSale==1 && onlineSale==1 ){
			return  "都不销售" ;
		}
		return "";
	}

    @Override
    public MiniClassMaxMinDateVo getMaxMinCourseDateByMiniClass(String miniClassId) {
        MiniClassMaxMinDateVo vo = null;
        Map<Object, Object> map = miniClassCourseDao.getMaxMinCourseDateByMiniClass(miniClassId);
        if (map != null) {
            vo = new MiniClassMaxMinDateVo();
            vo.setMiniClassId(miniClassId);
            if (map.containsKey("maxCourseDate")) {
                vo.setMaxCourseDate((String)map.get("maxCourseDate"));
            }
            if (map.containsKey("minCourseDate")) {
                vo.setMinCourseDate((String)map.get("minCourseDate"));
            }
        }
        return vo;
    }

    /**
     * 更新小班的开课结课日期
     * @param miniClass
     */
    public void updateMiniClassMaxMinCourseDate(MiniClass miniClass) {
        log.info("updateMiniClassMaxMinCourseDate miniClassId:" + miniClass.getMiniClassId()
                + ",onlineSale:" + miniClass.getOnlineSale() + ",onShelves:" + miniClass.getOnShelves());
        MiniClassMaxMinDateVo maxMinDate = this.getMaxMinCourseDateByMiniClass(miniClass.getMiniClassId());
        if (maxMinDate != null && StringUtil.isNotBlank(maxMinDate.getMinCourseDate())) {
            miniClass.setStartDate(maxMinDate.getMinCourseDate().substring(0, 10));
            miniClass.setCourseStartTime(maxMinDate.getMinCourseDate());
        }
        if (maxMinDate != null && StringUtil.isNotBlank(maxMinDate.getMaxCourseDate())) {
            miniClass.setEndDate(maxMinDate.getMaxCourseDate().substring(0, 10));
            miniClass.setCourseEndTime(maxMinDate.getMaxCourseDate());
        }
        miniClassDao.merge(miniClass);
        // 发送小班开课结课变更到消息中心
//        String data = "{\"data\":\"" + maxMinDate.getMiniClassId() + "\"}";
        if (miniClass.getOnlineSale() == 0 && miniClass.getOnShelves() != 3) {
            MessageQueueUtils.postToMq(MessageQueueUtils.TopicCode.MINI_CLASS_COURSE_DATE_SYNC, maxMinDate.getMiniClassId());
        }
    }

    @Override
	public void deleteMiniStudentAttendent(String contractProductId){
		Map<String,Object> map = new HashMap<>();
		map.put("contractProductId",contractProductId);
		StringBuilder hql = new StringBuilder();
		hql.append(" from MiniClassStudent where contractProduct.id =:contractProductId");
		List<MiniClassStudent> list= miniClassStudentDao.findAllByHQL(hql.toString(),map);
		for (MiniClassStudent stu:list){
			if(stu.getMiniClass()!=null && StringUtils.isNotBlank(stu.getMiniClass().getMiniClassId())){
				miniClassStudentAttendentDao.deleteMiniClassStudentAttendentByStudentAndMiniClass(stu.getStudent().getId(), stu.getMiniClass().getMiniClassId());
			}
		}
	}

    @Override
    public List<MiniClass> findMiniClassByModalId(int modalId) {
		Map<String,Object> map = new HashMap<>();
		map.put("modalId",modalId);
		StringBuilder hql = new StringBuilder();
		hql.append(" from MiniClass where modal.id =:modalId");
		return miniClassDao.findAllByHQL(hql.toString(),map);
    }

    @Override
    public List<MiniClassModalVo> getSmallClassListByTeacher(MiniClassModalVo vo) {
		User user=userService.getCurrentLoginUser();
		List<MiniClass> list =smallClassDao.getSmallClassListByTeacher(vo,user);
		List<MiniClassModalVo> returnList= new ArrayList<>();
		for(MiniClass v :list){
			MiniClassModalVo vv=HibernateUtils.voObjectMapping(v,MiniClassModalVo.class);
			if(StringUtils.isNotBlank(vv.getBlBrenchId())){
				Organization brench = organizationService.findById(vv.getBlBrenchId());
				if(brench!=null)
					vv.setBlBrenchName(brench.getName());
			}
			TreeSet<WeekDay> weekSet = new TreeSet<>();
			if(v.getModal()!=null) {
				List<CourseModalWeek> weekList = courseModalWeekDao.findModalWeekByModalId(v.getModal().getId());
				for (CourseModalWeek week : weekList) {
					weekSet.add(week.getCourseWeek());
				}
			}
			vv.setCourseWeek(weekSet);
			returnList.add(vv);
		}

		return returnList;
    }

    @Override
    public List<MiniClassModalVo> getSmallClassListOnModal(MiniClassModalVo vo) {
		User user=userService.getCurrentLoginUser();
		List<MiniClass> list =smallClassDao.getSmallClassListOnModal(vo,user);
		List<MiniClassModalVo> returnList= new ArrayList<>();
		for(MiniClass v :list){
			MiniClassModalVo vv=HibernateUtils.voObjectMapping(v,MiniClassModalVo.class);
			if(StringUtils.isNotBlank(vv.getBlBrenchId())){
				Organization brench = organizationService.findById(vv.getBlBrenchId());
				if(brench!=null)
					vv.setBlBrenchName(brench.getName());
			}
			TreeSet<WeekDay> weekSet = new TreeSet<>();
			if(v.getModal()!=null) {
				List<CourseModalWeek> weekList = courseModalWeekDao.findModalWeekByModalId(v.getModal().getId());
				for (CourseModalWeek week : weekList) {
					weekSet.add(week.getCourseWeek());
				}
			}
			vv.setCourseWeek(weekSet);

			List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
			StudentCriterionList.add(Expression.eq("miniClass.miniClassId", vv.getMiniClassId()));
			int miniClassPeopleNum = miniClassStudentDao.findCountByCriteria(StudentCriterionList);
			vv.setMiniClassPeopleNum(miniClassPeopleNum);

			returnList.add(vv);
		}

		return returnList;
    }

    @Override
    public Response saveEditClassModal(MiniClassEditModalVo vo) {
    	Response res = new Response();
		MiniClass miniClass =smallClassDao.findById(vo.getMiniClassId());
		if(miniClass==null){
			res.setResultCode(-1);
			res.setResultMessage("找不到小班信息");
			return res;
		}

		if(miniClass.getStudyManeger()==null){
			res.setResultCode(-1);
			res.setResultMessage("请先选择小班班主任");
			return res;
		}

		CourseModal modal = courseModalDao.findById(vo.getModalId());
		CourseModalVo modalVo=courseModalService.findModalByModalId(vo.getModalId());
		User user = userService.getCurrentLoginUser();

		int i =1;
		if(miniClass.getIsModal()>0){
			clearAllClassCourse(miniClass.getMiniClassId());
		}

		for(String courseDate:modalVo.getCourseDate()){
			MiniClassCourse course = new MiniClassCourse();
			course.setCourseDate(courseDate);
			if(StringUtils.isNotBlank(vo.getClassRoomId())) {
				course.setClassroom(new ClassroomManage(vo.getClassRoomId()));
			}
			if(StringUtils.isNotBlank(vo.getClassTime())) {
				course.setCourseTime(vo.getClassTime());
			}
			if(StringUtils.isNotBlank(vo.getTeacherId())) {
				course.setTeacher(new User(vo.getTeacherId()));
			}
			course.setCourseNum(i);
			course.setMiniClass(miniClass);
			course.setSubject(miniClass.getSubject());
			course.setGrade(miniClass.getGrade());
			course.setMiniClassName(miniClass.getName());
			course.setCourseMinutes(BigDecimal.valueOf(miniClass.getClassTimeLength()));
			course.setStudyHead(miniClass.getStudyManeger());
			course.setCourseHours(miniClass.getEveryCourseClassNum());
			course.setCreateTime(DateTools.getCurrentDateTime());
			course.setCreateUserId(user.getUserId());
			course.setModifyTime(DateTools.getCurrentDateTime());
			course.setModifyUserId(user.getUserId());
			course.setCourseStatus(CourseStatus.NEW);
			course.setAuditStatus(AuditStatus.UNAUDIT);
			//
			 course.setCourseEndTime(vo.getClassEndTime());
			miniClassCourseDao.save(course);
			i++;
		}
		miniClass.setClassTime(vo.getClassTime());
		if(StringUtils.isNotBlank(vo.getTeacherId())) {
			miniClass.setTeacher(new User(vo.getTeacherId()));
		}else{
			miniClass.setTeacher(null);
		}
		miniClass.setClassroom(new ClassroomManage(vo.getClassRoomId()));
		miniClass.setArrangedHours(i*miniClass.getEveryCourseClassNum());
		updateCourseNumByClassId(miniClass.getMiniClassId());//更新排序
		miniClass.setModal(modal);
		miniClass.setIsModal(2);
		miniClassCourseDao.flush();
		updateMiniClassMaxMinCourseDate(miniClass);
		List<MiniClassStudent> list=miniclassStudentDao.getMiniClassStudent(miniClass.getMiniClassId());
		for(MiniClassStudent mcs : list){//考勤记录补充
			this.addMiniClassStudentAttendents(mcs.getStudent().getId(), miniClass.getMiniClassId(), mcs.getFirstSchoolTime());
		}
		return res;
    }

	@Override
	public Response modifyClassModal(CourseModal modal,Boolean isChangeTechNum) {
		User user = userService.getCurrentLoginUser();
		Response res = new Response();
		CourseModalVo modalVo = courseModalService.findModalByModalId(modal.getId());
		List<MiniClass> list=findMiniClassByModalId(modal.getId());
		for(MiniClass miniClass :list) {
			int i = 1;
			if(isChangeTechNum){
				clearAllClassCourse(miniClass.getMiniClassId());
				miniClass.setArrangedHours(0.00);
				miniClass.setModal(null);
				miniClass.setIsModal(0);
			}else {
				MiniClassCourse mcc = miniClassCourseDao.findOneByHQL(" from MiniClassCourse where miniClass.miniClassId='" + miniClass.getMiniClassId() + "'", new HashMap<>());
				clearAllClassCourse(miniClass.getMiniClassId());
				miniClass.setModal(modal);
				miniClass.setIsModal(2);
				String courseEndTime = null;
				if(StringUtils.isNotBlank(modal.getClassTime())) {
					miniClass.setClassTime(modal.getClassTime());
					if(miniClass.getClassTime()!=null && miniClass.getEveryCourseClassNum()!=null && miniClass.getClassTimeLength()!=null){
						Double dou= miniClass.getEveryCourseClassNum()*miniClass.getClassTimeLength().doubleValue();
						try {
							courseEndTime=DateTools.dateConversString(DateTools.add(DateTools.getDateTime("2012-01-01 "+miniClass.getClassTime()), Calendar.MINUTE,dou.intValue()), "HH:mm");
						} catch (Exception e) {
							log.error("计算结束课时报错****************************************修改课程模板！");
						}
					}
				}
				for (String courseDate : modalVo.getCourseDate()) {
					MiniClassCourse course = new MiniClassCourse();
					course.setCourseDate(courseDate);
					course.setClassroom(mcc.getClassroom());
					course.setCourseTime(miniClass.getClassTime());
					course.setTeacher(mcc.getTeacher());
					course.setCourseNum(i);
					course.setMiniClass(miniClass);
					course.setSubject(miniClass.getSubject());
					course.setGrade(miniClass.getGrade());
					course.setMiniClassName(miniClass.getName());
					course.setCourseMinutes(BigDecimal.valueOf(miniClass.getClassTimeLength()));
					course.setStudyHead(miniClass.getStudyManeger());
					course.setCourseHours(miniClass.getEveryCourseClassNum());
					course.setCreateTime(DateTools.getCurrentDateTime());
					course.setCreateUserId(user.getUserId());
					course.setModifyTime(DateTools.getCurrentDateTime());
					course.setModifyUserId(user.getUserId());
					course.setCourseStatus(CourseStatus.NEW);
					course.setAuditStatus(AuditStatus.UNAUDIT);
					if(courseEndTime!=null){
						course.setCourseEndTime(courseEndTime);
					}else {
						course.setCourseEndTime(mcc.getCourseEndTime());
					}
					miniClassCourseDao.save(course);
					i++;
				}
				miniClass.setArrangedHours(i * miniClass.getEveryCourseClassNum());
				updateCourseNumByClassId(miniClass.getMiniClassId());//更新排序
				miniClassCourseDao.flush();
				updateMiniClassMaxMinCourseDate(miniClass);
				List<MiniClassStudent> lists = miniclassStudentDao.getMiniClassStudent(miniClass.getMiniClassId());
				for (MiniClassStudent mcs : lists) {//考勤记录补充
					this.addMiniClassStudentAttendents(mcs.getStudent().getId(), miniClass.getMiniClassId(), mcs.getFirstSchoolTime());
				}
			}
		}
		return res;
	}


	@Override
    public void clearAllClassCourse(String miniClassId){
    	MiniClass miniClass =miniClassDao.findById(miniClassId);
		miniClass.setIsModal(0);
		miniClass.setModal(null);
		List<MiniClassCourse> list= findMiniClassCourseByClassId(miniClassId);
    	for(MiniClassCourse course:list) {
    		if(course.getCourseStatus().equals(CourseStatus.CHARGED)){
    			throw new ApplicationException("该小班已经上过课，不能再修改模版信息了");
			}
			miniClassStudentAttendentDao.deleteMiniClassStudentAttendent(course.getMiniClassCourseId());
			miniClassCourseDao.delete(course);
		}
	}
    
    private Response checkRepeatMiniClassName(MiniClassVo[] miniClassVo){
    	
    	Response response = new Response();
		Map<String,Integer> map = new HashMap<>();		
		StringBuilder repeatIndex =new StringBuilder();
		if(miniClassVo!=null && miniClassVo.length>0){
			int i = 0;
			for(MiniClassVo vo:miniClassVo){
				i = i+1;
				String key = vo.getName()+vo.getBlCampusId();
				if(map.get(key)!=null){
					map.put(key, map.get(key)+1);
					repeatIndex.append(""+i+",");
				}else{
					map.put(key, 1);
				}
			}
			if(repeatIndex.length()>0){
				response.setResultCode(-100);
				response.setResultMessage(repeatIndex.substring(0, repeatIndex.length()-1));
			}
		}
		
		
    	return response;
    }
	
    
//    public static void main(String[] args) {
//      MiniClassVo[] array = new MiniClassVo[5];
//      MiniClassVo vo = null;
//      for(int i =0;i<3;i++){
//    	  vo = new MiniClassVo();
//    	  vo.setName("wang");
//    	  vo.setBlCampusId(String.valueOf(i));
//    	  array[i] = vo;
//      }
//      MiniClassVo miniClassVo = new MiniClassVo();
//      miniClassVo.setName("wang");
//      miniClassVo.setBlCampusId("2");
//      array[3]= miniClassVo;
//      array[4]= miniClassVo;
//      Response response=  checkRepeatMiniClassName(array);
//      System.out.println(response.getResultCode()+"---"+response.getResultMessage());
//	}
    
    
	@Override
	public Response batchSaveMiniClass(MiniClassVo[] miniClassVo) throws Exception {
		Response response = new Response();
		if(miniClassVo==null ||miniClassVo.length==0){
			response.setResultCode(-1);
			response.setResultMessage("没有要保存的小班");
			return response;
		}
		List<MiniClass> miniClasses = new ArrayList<MiniClass>();
		HashSet<String> miniClassName = new HashSet<>();
		response = checkRepeatMiniClassName(miniClassVo);
		if(response.getResultCode()==-100){
			return response;
		}
		int i =0;
		StringBuilder repeatIndex =new StringBuilder();
        for(MiniClassVo miniVo:miniClassVo){        	
        	i = i+1;        	
        	miniClassName.add(miniVo.getName()+miniVo.getBlCampusId());
        	MiniClass mini = HibernateUtils.voObjectMapping(miniVo, MiniClass.class);
        	miniClasses.add(mini);
        	try {
        		miniClassSameNameAndSameCampus(mini);
			}catch (ApplicationException e) {
				repeatIndex.append(""+i+",");

			}   		
        }
    	if(repeatIndex.length()>0){
			response.setResultCode(-100);
			response.setResultMessage(repeatIndex.substring(0, repeatIndex.length()-1));
			return response;        		
    	}
        //检查重复的名字
        if(miniClassName.size()!=miniClassVo.length){
			response.setResultCode(-1);
			response.setResultMessage("小班存在重名，请修改班级名称再保存");
			return response;
        }
       
		//检查小班的参数  （小班名字+校区）	
		//确定保存小班需要的参数		
		//批量插入数据
		final Integer groupCode = PropertiesUtils.getIntValue("groupCode");
		final String url = PropertiesUtils.getStringValue("textBookBindingDataUrl");
		String miniClassId = null;
		Integer textBookId_New = -1;
		Integer textBookId_Old = -1;
		String userId = userService.getCurrentLoginUser().getUserId();
		String syncMiniClassIds = "";
		for(MiniClass mini:miniClasses){
			mini.setConsume(0D);
			
			if(mini.getTeacher()!=null && mini.getTeacher().getUserId().equals("0")){
				mini.setTeacher(null);
			}
			
			saveMiniClass(mini);			
			//同步教材数据给教学平台 
			textBookId_New = mini.getTextBookId();
	        miniClassId = mini.getMiniClassId();
	        	//传数据过去
			if (textBookId_New != null && textBookId_New != textBookId_Old) {
				// ?wareId=12&classId=a1&type=SMALL_CLASS&groupCode=1
				Map<String, String> params = Maps.newConcurrentMap();
				params.put("wareId", Integer.toString(textBookId_New));
				params.put("classId", miniClassId);
				params.put("type", ProductType.SMALL_CLASS.getValue());
				params.put("groupCode", Integer.toString(groupCode));
				updateTextBookBindingData(url, params);
			}

			miniClassCourseDao.saveClassroom4itsClass(mini);
			// 小班关联多产品逻辑			
			miniClassProductDao.saveMiniClassProduct(mini.getProduct(), mini, userId, "1");

			//保存辅产品
			if (StringUtil.isNotBlank(mini.getProductIdArray())){
				operationMiniClassProduct(mini.getProductIdArray(), miniClassId);
			}
			
			syncMiniClassIds += miniClassId;

			
		}	
		if (StringUtils.isNotBlank(syncMiniClassIds)) { // 新增小班，关联了产品则推送到消息中心
            MessageQueueUtils.postToMq(MessageQueueUtils.TopicCode.MINI_CLASS_OTHER_PRODUCT_SYNC, syncMiniClassIds);
        }
		return response;
	}

	@Override
	public Integer getBatchMiniClassNum(String productId, String coursePhase, String campusId,String subjectId) throws ApplicationException{		
		Product product = productDao.findById(productId);
		if(product==null){
			throw new ApplicationException("该关联产品不存在");
		}
		if(StringUtil.isNotBlank(coursePhase)){
			DataDict dataDict = dataDictDao.findById(coursePhase);
			if(dataDict==null){
				throw new ApplicationException("选择期数有误");
			}
		}
		DataDict subject = dataDictDao.findById(subjectId);
		if(subject==null){
			throw new ApplicationException("科目不存在");
		}
		Map<String, Object> map = Maps.newHashMap();
		String sql ="select count(*) from mini_class mc where mc.PRODUCE_ID = :productId and mc.BL_CAMPUS_ID = :campusId and mc.`SUBJECT` = :subject ";
		map.put("productId", productId);
		map.put("campusId", campusId);
		map.put("subject", subjectId);
		if(StringUtil.isNotBlank(coursePhase)){
			sql = sql +" and mc.PHASE= :coursePhase ";
			map.put("coursePhase", coursePhase);
		}
		int result = miniClassDao.findCountSql(sql, map);
		return result;
	}

	@Override
	public List getStudentMiniClassAttendent(String miniClassId, String studentId) {
		return miniClassStudentAttendentDao.getStudentMiniClassAttendent(miniClassId,studentId);
	}

    @Override
    public List getMiniClassCourseDateInfo(String miniClassId) {
        return miniClassCourseDao.getMiniClassCourseDateInfo(miniClassId);
    }

	@Override
	public Response getMiniClassCanChangeProduct(String miniClassId) {
    	Response res = new Response();
		List<AccountChargeRecords> list = accountChargeRecordsDao.getAccountChargeRecordsByOtmClass(miniClassId);
		if(list.size()>0){
			res.setResultCode(-1);
			res.setResultMessage("有扣费或者冲销记录。");
		}

		return res;
	}

	@Override
	public void saveMiniClass(MiniClass miniClass) throws Exception {	
		miniClass.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		miniClass.setModifyUserId(user.getUserId());		
		if(miniClass.getMiniClassId()==null){			
			miniClass.setCreateTime(DateTools.getCurrentDateTime());
			miniClass.setCreateUserId(user.getUserId());
			miniClass.setStatus(MiniClassStatus.PENDDING_START);
		}
		if(miniClass.getModal()!=null && miniClass.getModal().getId()!=0){
			if(miniClass.getStudyManeger()==null){
				throw new ApplicationException("请先选择小班班主任");
			}
			miniClass.setIsModal(2);
		}
		smallClassDao.save(miniClass);
		smallClassDao.flush();
		//修改今天之后的小班课程
//		miniClassCourseDao.updateMiniClassCourse(miniClass);
		String courseEndTime = null;
		if(miniClass.getClassTime()!=null && miniClass.getEveryCourseClassNum()!=null && miniClass.getClassTimeLength()!=null){
			//courseTime=miniClass.getClassTime();
			Double dou= miniClass.getEveryCourseClassNum()*miniClass.getClassTimeLength().doubleValue();
			courseEndTime=DateTools.dateConversString(DateTools.add(DateTools.getDateTime("2012-01-01 "+miniClass.getClassTime()), Calendar.MINUTE,dou.intValue()), "HH:mm");
		}
		
		//插入小班排课逻辑
		//如果模板id不为空
		if(miniClass.getModal()!=null && miniClass.getModal().getId()!=0){
			//CourseModal modal = courseModalDao.findById(miniClass.getModal().getId());
			CourseModalVo modalVo=courseModalService.findModalByModalId(miniClass.getModal().getId());

			int i =1;
            String endDate = null;
            int k = modalVo.getCourseDate().size()+1;
			for(String courseDate:modalVo.getCourseDate()){
				MiniClassCourse course = new MiniClassCourse();
				course.setCourseDate(courseDate);
				if(miniClass.getClassroom()!=null) {
					course.setClassroom(miniClass.getClassroom());
				}
				if(StringUtils.isNotBlank(miniClass.getClassTime())) {
					course.setCourseTime(miniClass.getClassTime());
				}
				if(miniClass.getTeacher()!=null) {
					course.setTeacher(miniClass.getTeacher());
				}
				course.setCourseNum(i);
				course.setMiniClass(miniClass);
				course.setSubject(miniClass.getSubject());
				course.setGrade(miniClass.getGrade());
				course.setMiniClassName(miniClass.getName());
				course.setCourseMinutes(BigDecimal.valueOf(miniClass.getClassTimeLength()));
				course.setStudyHead(miniClass.getStudyManeger());
				course.setCourseHours(miniClass.getEveryCourseClassNum());
				course.setCreateTime(DateTools.getCurrentDateTime());
				course.setCreateUserId(user.getUserId());
				course.setModifyTime(DateTools.getCurrentDateTime());
				course.setModifyUserId(user.getUserId());
				course.setCourseStatus(CourseStatus.NEW);
				course.setAuditStatus(AuditStatus.UNAUDIT);
				//
				course.setCourseEndTime(courseEndTime);
				miniClassCourseDao.save(course);
				i++;
				if(i==k){
					endDate = courseDate;
				}
			}
			
			miniClass.setEndDate(endDate);
		}
		miniClassCourseDao.flush();
		// 同步小班到在线报读
		if(miniClass.getOnlineSale() == 0 && !this.syncMiniClassToMobile(miniClass)){
            log.error("同步小班到在线出错：" + miniClass.getMiniClassId());
		}
		
	}

	@Override
	public MiniClassCourse getMiniClassCourseById(String miniClassCourseId) {
		MiniClassCourse miniClassCourse = miniClassCourseDao.findById(miniClassCourseId);
		return miniClassCourse;
	}

	@Override
	public void updateMiniClassCourse(MiniClassCourse miniClassCourse) {
		miniClassCourseDao.save(miniClassCourse);		
	}

	@Override
	public List<MiniClassVo> changeMiniClassSelectList(String productId) {
		return HibernateUtils.voListMapping(getMiniClassList4Selection(productId),MiniClassVo.class);
	}

	@Override
	public List<MiniClassCourseVo> findAllEnableMiniClassCourse(String miniClassId) {
		return HibernateUtils.voListMapping(miniClassCourseDao.findAllEnableMiniClassCourse(miniClassId),MiniClassCourseVo.class);
	}

	//private List<MiniClass> getMiniClassBy

}