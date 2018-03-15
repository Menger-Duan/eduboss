package com.eduboss.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.eduboss.dao.*;
import com.eduboss.domain.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.BaseStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.CourseSummaryStatus;
import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.common.MiniClassStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.common.ProductType;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.HasClassCourseVo;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TwoClassCourseStudentRosterVo;
import com.eduboss.domainVo.TwoTeacherClassCourseVo;
import com.eduboss.domainVo.TwoTeacherClassStudentAttendentVo;
import com.eduboss.domainVo.TwoTeacherClassStudentVo;
import com.eduboss.domainVo.TwoTeacherClassTwoExcelVo;
import com.eduboss.domainVo.TwoTeacherClassTwoVo;
import com.eduboss.domainVo.TwoTeacherClassVo;
import com.eduboss.domainVo.TwoTeacherCourseSearchVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ProductService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.TwoTeacherClassService;
import com.eduboss.service.UserService;
import com.eduboss.service.handler.impl.TwoTeacherClassConProdHandler;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.FileUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2017/3/30.
 */
@Service("twoTeacherClassService")
public class TwoTeacherClassServiceImpl implements TwoTeacherClassService {

    @Autowired
    private TwoTeacherClassDao twoTeacherClassDao;

    @Autowired
    private TwoTeacherClassStudentDao twoTeacherClassStudentDao;

    @Autowired
    private TwoTeacherClassCourseDao twoTeacherClassCourseDao;

    @Autowired
    private TwoTeacherClassTwoDao twoTeacherClassTwoDao;

    @Autowired
    private TwoTeacherClassStudentAttendentDao twoTeacherClassStudentAttendentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TwoTeacherBrenchDao twoTeacherBrenchDao;

    @Autowired
    private ContractService contractService;

    @Autowired
    private StudentDao studentDao;


    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private RoleQLConfigService roleQLConfigService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MoneyWashRecordsDao moneyWashRecordsDao;

    @Autowired
    private CourseConflictDao courseConflictDao;

    @Autowired
    private AccountChargeRecordsDao accountChargeRecordsDao;

    @Autowired
    private DataDictDao dataDictDao;


    private final static Logger log = Logger.getLogger(TwoTeacherClassServiceImpl.class);


    @Override
    public DataPackage findTwoTeacherClassList(DataPackage dp, TwoTeacherClassVo vo) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer hql=new StringBuffer();
        hql.append(" select distinct t from TwoTeacherClass t ,TwoTeacherClassBrench b where 1=1 and t.classId=b.classId");
        if(StringUtils.isNotBlank(vo.getStartDate())){
            hql.append(" and t.startDate >=:startDate");
            pmap.put("startDate",vo.getStartDate());
        }

        if(StringUtils.isNotBlank(vo.getEndDate())){
            hql.append(" and t.startDate <=:endDate");
            pmap.put("endDate",vo.getEndDate());
        }

        if(StringUtil.isNotBlank(vo.getName())){
            hql.append(" and t.name like :name");
            pmap.put("name", "%" + vo.getName() + "%");
        }

        if(StringUtil.isNotBlank(vo.getTeacherId())){
            if(vo.getTeacherId().equals("-1")){
                hql.append(" and t.teacher.userId is null ");
            }else {
                hql.append(" and t.teacher.userId=:userId ");
                pmap.put("userId", vo.getTeacherId());
            }
        }

        if(StringUtils.isNotBlank(vo.getGradeId())){
            hql.append(" and t.product.gradeDict.id =:gradeId");
            pmap.put("gradeId",vo.getGradeId());
        }

        if(StringUtils.isNotBlank(vo.getStatusId())){
            hql.append(" and t.status=:status");
            pmap.put("status",MiniClassStatus.valueOf(vo.getStatusId()));
        }

        if (StringUtils.isNotBlank(vo.getProductQuarterId())){
            hql.append(" and t.product.productQuarter.id = :productQuarterId ");
            pmap.put("productQuarterId", vo.getProductQuarterId());
        }

        if (StringUtils.isNotBlank(vo.getPhaseId())){
            hql.append(" and t.phase.id = :phaseId");
            pmap.put("phaseId", vo.getPhaseId());
        }

        if (StringUtils.isNotBlank(vo.getClassTypeId())){
            hql.append(" and t.product.classType.id = :classTypeId ");
            pmap.put("classTypeId", vo.getClassTypeId());
        }

        if (StringUtils.isNotBlank(vo.getProductCourseSeriesId())){
            hql.append(" and t.product.courseSeries.id = :courseSeries ");
            pmap.put("courseSeries", vo.getProductCourseSeriesId());
        }

        if (StringUtils.isNotBlank(vo.getExceptStatusId())){
            if ("CONPELETE".equals(vo.getExceptStatusId())){//selectProductCommonModal中的 findTwoTeacherClassList
                hql.append(" and t.status<>'CONPELETE' ");//主班班级状态为已完成，该主班不能建辅班
            }
        }

        if (StringUtils.isNotBlank(vo.getSubjectId())){
            hql.append(" and t.subject.id = :subjectId");
            pmap.put("subjectId", vo.getSubjectId());
        }

        if (StringUtils.isNotBlank(vo.getBranchId())){
            hql.append(" and b.brenchId.id = :branchId");
            pmap.put("branchId", vo.getBranchId());
        }

        hql.append(roleQLConfigService.getAppendSqlByAllOrg("双师集团","hql","b.brenchId.id"));

//        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        //权限查询所属组织架构下的所有校区信息
//        if(userOrganizations != null && userOrganizations.size() > 0){
//            Organization org = userOrganizations.get(0);
//            hql.append("  and b.brenchId.id in ( select id from Organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
//            for(int i = 1; i < userOrganizations.size(); i++){
//                hql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
//            }
//            hql.append(")");
//        }

        hql.append(" order by t.createTime desc");
        dp=twoTeacherClassDao.findPageByHQL(hql.toString(),dp,false,pmap);

        List<TwoTeacherClassVo> list=HibernateUtils.voListMapping((List) dp.getDatas(), TwoTeacherClassVo.class);
        for(TwoTeacherClassVo v:list){

            StringBuffer hql2=new StringBuffer();
            hql2.append("select count(1) from Two_Teacher_Class_Two where class_Id=:classId");
            Map<String,Object> pmap2= new HashMap();
            pmap2.put("classId",v.getClassId());
            v.setClassNum(twoTeacherClassTwoDao.findCountSql(hql2.toString(),pmap2));
            List<TwoTeacherClassBrench> listBrench= findClassBrenchNameByClassId(v.getClassId());
            String brenchName="";
            String blCampusId="";
            for(TwoTeacherClassBrench map:listBrench){
                brenchName+=map.getBrenchId().getName()+",";
                blCampusId+=map.getBrenchId().getId()+",";
            }
            if(brenchName.length()>0){
                brenchName=brenchName.substring(0,brenchName.length()-1);
                blCampusId=blCampusId.substring(0,blCampusId.length()-1);
            }
            v.setBlCampusName(brenchName);
            v.setBlCampusId(blCampusId);
        }

        dp.setDatas(list);
        dp.setRowCount(twoTeacherClassDao.findCountHql("select count(distinct t) " + hql.substring(hql.indexOf("from") >= 0 ? hql.indexOf("from") : hql.indexOf("FROM")), pmap));
        return dp;
    }

    @Override
    public TwoTeacherClassVo findTwoTeacherById(int id) {
        TwoTeacherClass domain=twoTeacherClassDao.findById(id);
        TwoTeacherClassVo vo= HibernateUtils.voObjectMapping(domain,TwoTeacherClassVo.class);
        String brenchName="";
        String blCampusId="";
        List<TwoTeacherClassBrench> listBrench= findClassBrenchNameByClassId(id);
        for(TwoTeacherClassBrench map:listBrench){
            brenchName+=map.getBrenchId().getName()+",";
            blCampusId+=map.getBrenchId().getId()+",";
        }
        if(brenchName.length()>0){
            brenchName=brenchName.substring(0,brenchName.length()-1);
            blCampusId=blCampusId.substring(0,blCampusId.length()-1);
        }
        double countCourse=findCountCourse(id);
        vo.setAlreadyTotalClassHours(countCourse);
        vo.setBlCampusName(brenchName);
        vo.setBlCampusId(blCampusId);
        return vo;
    }

    /**
     * 更新双师主班开始上课日期
     * 双师主班和辅班的开始上课日期更新规则优化：
     * 1，当前双师主班未排课，双师主班和辅班的开始上课日期，获取双师主班建班或修改时填写的开始上课日期；
     * 2，当前双师主班已排课，双师主班和辅班的开始上课日期，自动更新为双师主班课程中第一讲课程的上课日期。
     *
     * @param twoTeacherClass
     * @return
     */
    @Override
    public TwoTeacherClass updateTwoTeacherClassStartDate(TwoTeacherClass twoTeacherClass) {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * FROM two_teacher_class_course WHERE CLASS_ID=:classId ORDER BY COURSE_DATE ASC ";
        params.put("classId", twoTeacherClass.getClassId());
        List<TwoTeacherClassCourse> twoTeacherClassCourses = twoTeacherClassCourseDao.findBySql(sql, params);
        if (twoTeacherClassCourses.size()>0){
            twoTeacherClass.setStartDate(twoTeacherClassCourses.get(0).getCourseDate());
        }
        return twoTeacherClass;
    }

    @Override
    public DataPackage findCourseByClassTwoId(DataPackage dp, TwoTeacherClassCourseVo vo) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer sql=new StringBuffer();
        sql.append(" select ttcc.* from two_teacher_class_course ttcc left join two_teacher_class_two ttc on ttc.CLASS_ID=ttcc.CLASS_ID where 1=1");

        if(vo.getTwoTeacherClassTwoId()>0){
            sql.append(" and ttc.CLASS_TWO_ID='"+vo.getTwoTeacherClassTwoId()+"'");
        }

        sql.append(" order by ttcc.course_Num");
        dp=twoTeacherClassCourseDao.findPageBySql(sql.toString(),dp,true,pmap);
        List<TwoTeacherClassCourseVo> list=HibernateUtils.voListMapping((List) dp.getDatas(), TwoTeacherClassCourseVo.class);
        dp.setDatas(list);

        return dp;
    }

    @Override
    public Response saveTwoTeacherClass(TwoTeacherClassVo vo) {

        User user = userService.getCurrentLoginUser();

        Response res=new Response();
        TwoTeacherClass domain=HibernateUtils.voObjectMapping(vo, TwoTeacherClass.class);
        if(twoTeacherClassDao.checkNameDuplicate(domain,vo.getBlBrenchId())){
            throw new ApplicationException("当前存在同名双师主班，请修改双师主班名称");
        }

        ProductVo product = productService.findProductById(vo.getProductId());
        domain.setUnitPrice(product.getPrice());
        domain.setClassTimeLength(product.getClassTimeLength());
        domain.setTotalClassHours(product.getMiniClassTotalhours().doubleValue());
        if(domain.getClassId()==0){
            domain.setCreateTime(DateTools.getCurrentDateTime());
            domain.setCreateUserId(user.getUserId());
            domain.setStatus(MiniClassStatus.PENDDING_START);
        }else{
            deleteClassBrench(domain.getClassId());
        }
        domain.setModifyUserId(user.getUserId());
        domain.setModifyTime(DateTools.getCurrentDateTime());
        domain = updateTwoTeacherClassStartDate(domain);
        twoTeacherClassDao.save(domain);
        twoTeacherClassDao.flush();
        saveClassBrench(vo.getBlBrenchId(), domain);
        return res;
    }

    @Override
    public TwoTeacherClassCourseVo findTwoTeacherClassCourseByCourseIdAndClassId(int twoTeacherCourseId, int twoTeacherClassTwoId) {
        String hql = " from TwoTeacherClassStudentAttendent where twoTeacherClassCourse.courseId=:twoTeacherCourseId and twoTeacherClassTwo.id=:twoTeacherClassTwoId ";
        Map<String, Object> params = new HashMap<>();
        params.put("twoTeacherCourseId", twoTeacherCourseId);
        params.put("twoTeacherClassTwoId", twoTeacherClassTwoId);
        TwoTeacherClassStudentAttendent v = twoTeacherClassStudentAttendentDao.findOneByHQL(hql, params);
        TwoTeacherClassCourseVo twoTeacherClassCourseVo = getTwoTeacherClassCourseVoByStudentAttendent(v);
        return twoTeacherClassCourseVo;
    }

    @Override
    public DataPackage getTwoTeacherClassCourseList(DataPackage dp, TwoTeacherClassCourseVo vo) {

//        vo.setTeacherTwoUserId(userService.getCurrentLoginUserId());//双师考勤只能看辅导老师自己的数据
        dp = twoTeacherClassStudentAttendentDao.getTwoTeacherClassCourseList(vo, dp);
        List<TwoTeacherClassCourseVo> result = new ArrayList<>();
        for (TwoTeacherClassStudentAttendent v:(List<TwoTeacherClassStudentAttendent>)dp.getDatas()){
            TwoTeacherClassCourseVo twoTeacherClassCourseVo = getTwoTeacherClassCourseVoByStudentAttendent(v);
            result.add(twoTeacherClassCourseVo);
        }
        dp.setDatas(result);
        return dp;
    }

    private String findMoneyWashRecords(TwoTeacherClassCourseVo twoTeacherClassCourseVo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT mwr.* FROM money_wash_records mwr, account_charge_records acr, two_teacher_class_student_attendent ttcsa ");
        sql.append(" WHERE mwr.TRANSACTION_ID = acr.TRANSACTION_ID ");
        sql.append(" AND acr.CHARGE_PAY_TYPE='WASH' ");
        sql.append(" AND acr.PRODUCT_TYPE='TWO_TEACHER' ");
        sql.append(" AND acr.TWO_TEACHER_STUDENT_ATTENDENT = ttcsa.ID AND acr.TWO_TEACHER_STUDENT_ATTENDENT is not null ");
        sql.append(" AND ttcsa.TWO_CLASS_COURSE_ID=:twoClassCourseID ");
        sql.append(" AND ttcsa.CLASS_TWO_ID=:classTwoID ");
        Map<String, Object> params = new HashMap<>();
        params.put("twoClassCourseID", twoTeacherClassCourseVo.getCourseId());
        params.put("classTwoID", twoTeacherClassCourseVo.getTwoTeacherClassTwoId());
        List<MoneyWashRecords> list = moneyWashRecordsDao.findBySql(sql.toString(), params);
        StringBuffer result = new StringBuffer();
        for (MoneyWashRecords records : list){
            result.append(records.getDetailReason());
            result.append(",");
        }
        return result.toString();
    }


    private TwoTeacherClassCourseVo getTwoTeacherClassCourseVoByStudentAttendent(TwoTeacherClassStudentAttendent v) {
        TwoTeacherClassCourse twoTeacherClassCourse = v.getTwoTeacherClassCourse();
        TwoTeacherClass twoTeacherClass = twoTeacherClassCourse.getTwoTeacherClass();//主班
        Product product = twoTeacherClass.getProduct();
        TwoTeacherClassTwo twoTeacherClassTwo = v.getTwoTeacherClassTwo();//辅班
        String blCampusName = null;
        String classroom = null;
        String classroomId =null;
        TwoTeacherClassCourseVo twoTeacherClassCourseVo = HibernateUtils.voObjectMapping(twoTeacherClassCourse, TwoTeacherClassCourseVo.class);
        if (twoTeacherClassTwo!=null){
            Organization blCampus = twoTeacherClassTwo.getBlCampus();
            blCampusName = blCampus.getName();
            ClassroomManage classroomManage = twoTeacherClassTwo.getClassroom();
            if (classroomManage!=null){
                classroom = classroomManage.getClassroom();
                classroomId = classroomManage.getId();
            }
            twoTeacherClassCourseVo.setTwoTeacherClassTwoId(twoTeacherClassTwo.getId());
            twoTeacherClassCourseVo.setTwoTeacherClassTwoName(twoTeacherClassTwo.getName());
            User teacher = twoTeacherClassTwo.getTeacher();
            twoTeacherClassCourseVo.setTeacherTwoName(teacher.getName());
            twoTeacherClassCourseVo.setTeacherTwoPhone(teacher.getContact());
        }

        twoTeacherClassCourseVo.setBlCampusName(blCampusName);
        twoTeacherClassCourseVo.setClassroom(classroom);
        twoTeacherClassCourseVo.setClassroomId(classroomId);
        twoTeacherClassCourseVo.setClassTypeName(product.getClassType().getName());
        twoTeacherClassCourseVo.setAttendacePicName(v.getAttendacePicName());

        //人数
        twoTeacherClassCourseVo = studentNumsForTwoTeacherClassCourse(twoTeacherClassCourseVo, v);
        twoTeacherClassCourseVo.setCourseStatus(v.getCourseStatus());
        twoTeacherClassCourseVo.setCourseStatusName(v.getCourseStatus().getName());
        twoTeacherClassCourseVo.setAuditStatus(v.getAuditStatus());
        twoTeacherClassCourseVo.setAuditStatusName(v.getAuditStatus().getName());

        if (!twoTeacherClassCourseVo.getCourseStatus().equals(CourseStatus.NEW.getValue())) {
            String moneyWashRecordsReasons = findMoneyWashRecords(twoTeacherClassCourseVo);
            if (StringUtils.isNotBlank(moneyWashRecordsReasons) ) {
                twoTeacherClassCourseVo.setIsWashed("TRUE");
                twoTeacherClassCourseVo.setWashRemark(moneyWashRecordsReasons);
            } else {
                twoTeacherClassCourseVo.setIsWashed("FALSE");
            }
        } else {
            twoTeacherClassCourseVo.setIsWashed("FALSE");
        }

        return twoTeacherClassCourseVo;
    }

    @Override
    public DataPackage getTwoTeacherClassStudentAttendentList(DataPackage dp, TwoTeacherClassCourseVo vo) {
        StringBuffer hql=new StringBuffer();
        Map<String, Object> params = Maps.newHashMap();
        hql.append(" from TwoTeacherClassStudentAttendent where twoTeacherClassCourse.courseId = :courseId and twoTeacherClassTwo.id = :twoTeacherClassTwoId");
        params.put("courseId", vo.getCourseId());
        params.put("twoTeacherClassTwoId", vo.getTwoTeacherClassTwoId());
        dp = twoTeacherClassStudentAttendentDao.findPageByHQL(hql.toString(), dp, true, params);
        List<TwoTeacherClassStudentAttendentVo> searchResultVoList = HibernateUtils.voListMapping((List<TwoTeacherClassStudentAttendent>)dp.getDatas(), TwoTeacherClassStudentAttendentVo.class);
        for (TwoTeacherClassStudentAttendentVo v : searchResultVoList){
            String hqlStudent = "  from TwoTeacherClassStudent where  classTwo.id = :twoTeacherClassTwoId and student.id = :studentId ";
            Map<String, Object> paramsStudent = new HashMap<>();
            paramsStudent.put("twoTeacherClassTwoId", vo.getTwoTeacherClassTwoId());
            paramsStudent.put("studentId", v.getStudentId());
            TwoTeacherClassStudent twoTeacherClassStudent = twoTeacherClassStudentDao.findOneByHQL(hqlStudent, paramsStudent);
            if (twoTeacherClassStudent!=null){
                ContractProduct contractProduct = twoTeacherClassStudent.getContractProduct();
                if (contractProduct!=null){
                    v.setContractProductStatus(contractProduct.getStatus());
                    v.setContractProductRemainingAmount(contractProduct.getRemainingAmount());
                }else {
                    v.setContractProductStatus(ContractProductStatus.CLOSE_PRODUCT);
                    v.setContractProductRemainingAmount(BigDecimal.ZERO);
                }
            }else {
                v.setContractProductStatus(ContractProductStatus.CLOSE_PRODUCT);
                v.setContractProductRemainingAmount(BigDecimal.ZERO);
            }



            /**
             * <field>
             <a>contractProduct.status</a>
             <b>contractProductStatus</b>
             </field>

             <field>
             <a>contractProduct.remainingAmount</a>
             <b>contractProductRemainingAmount</b>
             </field>
             */
            if (v.getHasTeacherAttendance() == null){
                v.setHasTeacherAttendance(BaseStatus.FALSE);
            }
        }
        dp.setDatas(searchResultVoList);
        return dp;
    }

    @Override
    public synchronized Response modifyTwoTeacherClassTwoCourseStudentAttendance(int courseId, int classTwoId, String attendanceData, String oprationCode) throws Exception {
        Response res = new Response();
        // 解析JSON
        ObjectMapper objectMapper = new ObjectMapper();
        //考勤即扣费
        oprationCode = "charge";
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, TwoTeacherClassStudentAttendentVo.class);
        List<TwoTeacherClassStudentAttendentVo> inputList =  (List<TwoTeacherClassStudentAttendentVo>)objectMapper.readValue(attendanceData, javaType);

        if(inputList.size()<=0){
            res.setResultCode(-2);
            res.setResultMessage("请选择要考勤学生的记录！");
            return res;
        }

        inputList = deleteChargeStudent(courseId, classTwoId, inputList);
        ////检测冲突
        checkMiniClassCourseConflict(courseId, classTwoId);

//        TwoTeacherClassCourse twoTeacherClassCourse = twoTeacherClassCourseDao.findById(courseId);

//        if("charge".equals(oprationCode) && !checkClassTwoCourseAttends(courseId, classTwoId, inputList)){
//            res.setResultCode(-2);
//            res.setResultMessage("双师扣费需全员扣费，包括考勤状态为迟到缺勤的学生！");
//            return res;
//        }

        TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassTwoDao.findById(classTwoId);

        if (null == twoTeacherClassTwo.getClassroom() || null == twoTeacherClassTwo.getClassroom().getId()) {
            res.setResultCode(-1);
            res.setResultMessage("双师课程需关联具体教室才可以考勤！");
            return res;
        }

        String stuName="";
        for (int i = 0; i<inputList.size(); i++){
            TwoTeacherClassStudentAttendentVo eachInputStudentAttendentVo = inputList.get(i);
            if ("charge".equals(oprationCode)){
                ContractProduct targetConPrd = null;
                TwoTeacherClassStudent twoTeacherClassStudent = twoTeacherClassStudentDao.getTwoTeacherClassStudent(eachInputStudentAttendentVo.getStudentId(), classTwoId);
                TwoTeacherClassStudentAttendent studentAttendent = getTwoTeacherClassStudentAttendent(courseId, classTwoId, eachInputStudentAttendentVo.getStudentId());
                targetConPrd = twoTeacherClassStudent.getContractProduct();
                TwoTeacherClassCourse twoTeacherClassCourse = studentAttendent.getTwoTeacherClassCourse();
                Double courseHour = twoTeacherClassCourse.getCourseHours();
                if (targetConPrd!=null && (targetConPrd.getStatus() == ContractProductStatus.STARTED || targetConPrd.getStatus() == ContractProductStatus.NORMAL)){
                    BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
                    // 旧数据 可能会是 price 价格为 0
                    BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice();
                    // 总价格 = 单价 * 课时
                    BigDecimal detel = price.multiply(BigDecimal.valueOf(courseHour));//
                    Student stu=targetConPrd.getContract().getStudent();

                    if(remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {

                    } else {
                        stuName+=stu.getName()+",";
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(stuName)){
            throw new ApplicationException(""+stuName+" 剩余课时不足以执行本次扣费，请续费后再重新扣费");
        }else{
            // 循环设每个学生考勤
            for (int i = 0; i<inputList.size(); i++){
                TwoTeacherClassStudentAttendentVo eachInputStuAttendentVo = inputList.get(i);

                TwoTeacherClassStudentAttendent studentAttendent = getTwoTeacherClassStudentAttendent(courseId, classTwoId, eachInputStuAttendentVo.getStudentId());
                // 更新考勤记录
                updateTwoTeacherClassAttendanceRecord(courseId, classTwoId, eachInputStuAttendentVo, oprationCode, studentAttendent);

                if (MiniClassStudentChargeStatus.UNCHARGE==studentAttendent.getChargeStatus()){
                    boolean chargeResult = twoTeacherCharge(courseId, classTwoId, eachInputStuAttendentVo, studentAttendent);
                }

            }
            /**
             * 更新 辅班班级状态
             */
            updateClassTwoStatus(classTwoId);

            /**
             * 更新 主班课程状态
             */
            updateTwoTeacherClassCourseStatus(courseId);

            /**
             * 更新 主班状态
             */
            updateTwoTeacherClassStatus(courseId);
        }


        return res;
    }

    /**
     * 更新 主班状态
     * 1，未上课：主班所有课程都是未上课
     * 2，上课中：主班有一个或以上课程已结算，且存在课程未上课
     * 3，已完成：主班已上课时数等于主班总课时数
     * @param courseId
     */
    private void updateTwoTeacherClassStatus(int courseId) {
        TwoTeacherClassCourse twoTeacherClassCourse = twoTeacherClassCourseDao.findById(courseId);
        TwoTeacherClass twoTeacherClass = twoTeacherClassCourse.getTwoTeacherClass();
        int classId = 0;
        if (twoTeacherClass!=null){
            classId = twoTeacherClass.getClassId();
        }
        if (classId>0){
            String allCourseSql = " SELECT * FROM two_teacher_class_course WHERE CLASS_ID=:classId ";
            Map<String, Object> map = new HashMap<>();
            map.put("classId", classId);
            List<TwoTeacherClassCourse> allCourse = twoTeacherClassCourseDao.findBySql(allCourseSql, map);
            String newCourseSql =" SELECT * FROM two_teacher_class_course WHERE CLASS_ID=:classId and COURSE_STATUS='NEW' ";
            List<TwoTeacherClassCourse> newCourse = twoTeacherClassCourseDao.findBySql(newCourseSql, map);
            String chargeCourseSql = " SELECT * FROM two_teacher_class_course WHERE CLASS_ID=:classId and COURSE_STATUS='CHARGED' ";
            List<TwoTeacherClassCourse> chargeCourse = twoTeacherClassCourseDao.findBySql(chargeCourseSql, map);
            if (allCourse.size()>0){
                if (allCourse.size() ==newCourse.size() ){
                    twoTeacherClass.setStatus(MiniClassStatus.PENDDING_START);
                }else if (allCourse.size() == chargeCourse.size()){
                    twoTeacherClass.setStatus(MiniClassStatus.CONPELETE);
                }else {
                    twoTeacherClass.setStatus(MiniClassStatus.STARTED);
                }
            }
        }

    }

    /**
     * 更新 主班课程状态
     * 主班课程状态转换规则：
     * 1，未上课：所有辅班当前课程未上课
     * 2，已结算：存在有一个辅班当前课程已结算
     */
    private void updateTwoTeacherClassCourseStatus(int courseId) {
        TwoTeacherClassCourse twoTeacherClassCourse = twoTeacherClassCourseDao.findById(courseId);
        String allCourseSql = " select * from two_teacher_class_student_attendent where TWO_CLASS_COURSE_ID =:courseId group by CLASS_TWO_ID  ";
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        List<TwoTeacherClassStudentAttendent> allCourse = twoTeacherClassStudentAttendentDao.findBySql(allCourseSql, map);
        String newCourseSql =" select * from two_teacher_class_student_attendent where COURSE_STATUS='NEW'  and TWO_CLASS_COURSE_ID =:courseId group by CLASS_TWO_ID ";
        List<TwoTeacherClassStudentAttendent> newCourse = twoTeacherClassStudentAttendentDao.findBySql(newCourseSql, map);
        if (allCourse.size()>0){
            if (allCourse.size()==newCourse.size()){
                twoTeacherClassCourse.setCourseStatus(CourseStatus.NEW);
            }else {
                twoTeacherClassCourse.setCourseStatus(CourseStatus.CHARGED);
            }
        }
        twoTeacherClassCourseDao.save(twoTeacherClassCourse);
    }

    /**
     * 更新 辅班班级状态
     *
     * 1，未上课：辅班所有课程未上课
     * 2，上课中：辅班存在有一个课程已结算，且存在课程未上课
     * 3，已完成：辅班已上课时数等于主班总课时数
     * @param classTwoId
     */
    private void updateClassTwoStatus(int classTwoId) {
        TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassTwoDao.findById(classTwoId);
        String allCourseSql = " select * from two_teacher_class_student_attendent where CLASS_TWO_ID=:classTwoId group by TWO_CLASS_COURSE_ID  ";
        Map<String, Object> map = new HashMap<>();
        map.put("classTwoId", classTwoId);
        List<TwoTeacherClassStudentAttendent> allCourse = twoTeacherClassStudentAttendentDao.findBySql(allCourseSql, map);
        String newCourseSql = " select * from  two_teacher_class_student_attendent where CLASS_TWO_ID=:classTwoId and COURSE_STATUS='NEW' group by TWO_CLASS_COURSE_ID  ";

        List<TwoTeacherClassStudentAttendent> newCourse = twoTeacherClassStudentAttendentDao.findBySql(newCourseSql, map);
        String chargedCourseSql = " select * from  two_teacher_class_student_attendent where CLASS_TWO_ID=:classTwoId and COURSE_STATUS='CHARGED' group by TWO_CLASS_COURSE_ID ";
        List<TwoTeacherClassStudentAttendent> chargeCourse = twoTeacherClassStudentAttendentDao.findBySql(chargedCourseSql, map);

        if (allCourse.size()>0){
            if (allCourse.size() == newCourse.size()){
                twoTeacherClassTwo.setStatus(MiniClassStatus.PENDDING_START);
            }else if (allCourse.size() == chargeCourse.size()){
                twoTeacherClassTwo.setStatus(MiniClassStatus.CONPELETE);
            }else {
                twoTeacherClassTwo.setStatus(MiniClassStatus.STARTED);
            }
        }
        twoTeacherClassTwoDao.save(twoTeacherClassTwo);
    }

    @Override
    public List<HasClassCourseVo> checkHasTwoTeacherClassForPeriodDate(String teacherId, String startDate, String endDate) {
        List<String> dates =   DateTools.getDates(startDate, endDate);
        boolean hasCourse = false;
        boolean hasUnCheckAttendanceCourse = false;

        boolean hasDeducationCourse = false;
        boolean hasUnDeducationCourse = false;
        List<HasClassCourseVo> listHasMiniClass = new ArrayList<HasClassCourseVo>();
        for (String strDate : dates){
            HasClassCourseVo hasMiniClassVo=new HasClassCourseVo();
            hasMiniClassVo.setDateValue(strDate);//日期
            hasCourse = this.checkHasTwoTeacherClassForDate(teacherId, strDate, new ArrayList<CourseStatus>());
            if (hasCourse){
                List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
                listOfStatus.add(CourseStatus.NEW);
                listOfStatus.add(CourseStatus.TEACHER_ATTENDANCE);
//                listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
                hasUnCheckAttendanceCourse = this.checkHasTwoTeacherClassForDate(teacherId, strDate, listOfStatus);
                if (hasUnCheckAttendanceCourse){
                    hasMiniClassVo.setHasValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());
                }else {
                    hasMiniClassVo.setHasValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());
                }
            }else {
                hasMiniClassVo.setHasValue(CourseSummaryStatus.NO_COURSE.getValue());
            }

//            hasDeducationCourse = this.checkHasTwoTeacherClassForDate(teacherId, strDate, new ArrayList<CourseStatus>());
//            if (hasCourse){
//                List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
//                listOfStatus.add(CourseStatus.NEW);
//                listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
//                listOfStatus.add(CourseStatus.TEACHER_ATTENDANCE);
//                hasUnDeducationCourse = this.checkHasTwoTeacherClassForDate(teacherId, strDate, listOfStatus);
//                if (hasUnDeducationCourse){
//                    hasMiniClassVo.setHasDeductionValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());
//                }else {
//                    hasMiniClassVo.setHasDeductionValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());
//                }
//            }else {
//                hasMiniClassVo.setHasDeductionValue(CourseSummaryStatus.NO_COURSE.getValue());
//            }

            listHasMiniClass.add(hasMiniClassVo);
        }
        return listHasMiniClass;
    }

    private boolean checkHasTwoTeacherClassForDate(String teacherId, String date, List<CourseStatus> listOfStatus) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer countHql = new StringBuffer();
        countHql.append(" select count(*) from TwoTeacherClassStudentAttendent where 1=1 ");
        countHql.append(" and twoTeacherClassTwo.teacher.userId= :teacherId ");
        countHql.append(" and twoTeacherClassCourse.courseDate = :date ");
        params.put("teacherId", teacherId);
        params.put("date", date);
        if (listOfStatus.size()>0){
            countHql.append(" and (courseStatus in (:statuses) or auditStatus='UNVALIDATE' ) ");
            params.put("statuses", listOfStatus);
        }
        int count = twoTeacherClassStudentAttendentDao.findCountHql(countHql.toString(), params);
        return count >=1 ;
    }

    @Override
    public void modifyTwoTeacherClassTwoCourseStudentAttendanceSupplement(int id, String supplementDate, String absentRemark) {
        TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent = twoTeacherClassStudentAttendentDao.findById(id);
        if (twoTeacherClassStudentAttendent!=null){
            if (StringUtils.isNotBlank(supplementDate)){
                twoTeacherClassStudentAttendent.setSupplementDate(supplementDate);
            }
            if (StringUtils.isNotBlank(absentRemark)){
                twoTeacherClassStudentAttendent.setAbsentRemark(absentRemark);
            }
            twoTeacherClassStudentAttendentDao.save(twoTeacherClassStudentAttendent);
        }
    }

    @Override
    public void submitTwoTeacherPic(int twoTeacherCourseId, int twoTeacherClassTwoId, MultipartFile attendancePicFile, String servicePath) {
        TwoTeacherClassCourse course = twoTeacherClassCourseDao.findById(twoTeacherCourseId);
        if(course !=null) {
            // 统一使用 文件名 来 标明签名文件
            //String fileName = UUID.randomUUID().toString();
            String fileName =  String.format("TWO_CLASS_ATTEND_PIC_%s.jpg", twoTeacherCourseId+"_"+twoTeacherClassTwoId);
            course.setAttendacePicName(fileName);
            twoTeacherClassStudentAttendentDao.updatePicByCourseAndClassId(twoTeacherCourseId,twoTeacherClassTwoId,fileName);
            String folder=servicePath+ PropertiesUtils.getStringValue("save_file_path");//系统路径
            FileUtil.isNewFolder(folder);

            String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG
            String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
            String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小

            String relFileName=folder+"/realFile_"+twoTeacherCourseId+"_"+twoTeacherClassTwoId+UUID.randomUUID().toString()+".jpg";
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
        }
    }




    private boolean twoTeacherCharge(int courseId, int classTwoId, TwoTeacherClassStudentAttendentVo eachInputStuAttendentVo, TwoTeacherClassStudentAttendent studentAttendent) {
        TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent = studentAttendent;
        twoTeacherClassStudentAttendent.setCourseStatus(CourseStatus.CHARGED);
        twoTeacherClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.CHARGED);
        TwoTeacherClassCourse twoTeacherClassCourse = twoTeacherClassCourseDao.findById(courseId);
        TwoTeacherClassStudent twoTeacherClassStudent = twoTeacherClassStudentDao.getTwoTeacherClassStudent(eachInputStuAttendentVo.getStudentId(), classTwoId);
        TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassTwoDao.findById(classTwoId);
        Double courseHour = twoTeacherClassCourse.getCourseHours();
        ContractProduct targetConPrd = null;
        boolean hasCharge = false;
        targetConPrd = twoTeacherClassStudent.getContractProduct();

        if (targetConPrd!=null && (targetConPrd.getStatus() == ContractProductStatus.STARTED || targetConPrd.getStatus() == ContractProductStatus.NORMAL)){
            TwoTeacherClassConProdHandler prodHandler = (TwoTeacherClassConProdHandler)contractService.selectHandlerByType(ProductType.TWO_TEACHER);
            prodHandler.chargeTwoTeacherClass(targetConPrd, twoTeacherClassTwo, twoTeacherClassStudentAttendent, courseHour);
            hasCharge = true;
            return hasCharge;
        }

        return hasCharge;
    }

    private void updateTwoTeacherClassAttendanceRecord(int courseId, int classTwoId, TwoTeacherClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode, TwoTeacherClassStudentAttendent studentAttendent) {
        User user = userService.getCurrentLoginUser();




//        TwoTeacherClassStudentAttendent studentAttendent = twoTeacherClassStudentAttendentDao.getTwoTeacherClassStudentAttendent(courseId, classTwoId, studentId);

        MiniClassAttendanceStatus newStatus = MiniClassAttendanceStatus.valueOf(eachInputStuAttendentVo.getAttendanceType());

        studentAttendent.setMiniClassAttendanceStatus(newStatus);

        if ((newStatus == MiniClassAttendanceStatus.ABSENT) && eachInputStuAttendentVo.getAbsentRemark()!=null){
            studentAttendent.setAbsentRemark(eachInputStuAttendentVo.getAbsentRemark());
            studentAttendent.setSupplementDate(eachInputStuAttendentVo.getSupplementDate());
        }else {
            studentAttendent.setAbsentRemark("");
            studentAttendent.setSupplementDate(null);
        }
        studentAttendent.setHasTeacherAttendance(BaseStatus.TRUE);
        studentAttendent.setCourseStatus(CourseStatus.CHARGED);

        // 修改人记录
        String currDateTime = DateTools.getCurrentDateTime();
        studentAttendent.setModifyUser(user);
        studentAttendent.setModifyTime(currDateTime);
        studentAttendent.setAttendentUser(user);
        twoTeacherClassStudentAttendentDao.save(studentAttendent);

    }

    private TwoTeacherClassStudentAttendent getTwoTeacherClassStudentAttendent(int courseId, int classTwoId, String studentId) {
        String hql = "from TwoTeacherClassStudentAttendent where twoTeacherClassCourse.courseId =:courseId and twoTeacherClassTwo.id = :classTwoId and student.id=:studentId ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseId", courseId);
        params.put("classTwoId", classTwoId);
        params.put("studentId", studentId);
        return twoTeacherClassStudentAttendentDao.findOneByHQL(hql, params);
    }

    private boolean checkClassTwoCourseAttends(int courseId, int classTwoId, List<TwoTeacherClassStudentAttendentVo> inputList) {
        String hql = " from TwoTeacherClassStudentAttendent where twoTeacherClassCourse.courseId =:courseId and twoTeacherClassTwo.id = :classTwoId ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseId", courseId);
        params.put("classTwoId", classTwoId);
        List<TwoTeacherClassStudentAttendent> twoTeacherClassStudentAttendents = twoTeacherClassStudentAttendentDao.findAllByHQL(hql, params);
        hql+=" and chargeStatus = :chargeStatus ";
        params.put("chargeStatus", MiniClassStudentChargeStatus.CHARGED);
        List<TwoTeacherClassStudentAttendent> chargeList = twoTeacherClassStudentAttendentDao.findAllByHQL(hql, params);
        if (twoTeacherClassStudentAttendents.size()-chargeList.size() == inputList.size()){
            return true;
        }else {
            return false;
        }

    }

    private void checkMiniClassCourseConflict(int courseId, int classTwoId) {

    }

    private List<TwoTeacherClassStudentAttendentVo> deleteChargeStudent(int courseId, int classTwoId, List<TwoTeacherClassStudentAttendentVo> inputList) {
        return inputList;
    }

    /**
     * 辅班考勤的各种人数
     * @param twoTeacherClassCourseVo
     * @return
     */
    private TwoTeacherClassCourseVo studentNumsForTwoTeacherClassCourse(TwoTeacherClassCourseVo twoTeacherClassCourseVo, TwoTeacherClassStudentAttendent v) {
        String sql = "SELECT * FROM two_teacher_class_student_attendent WHERE two_class_course_id =:twoClassCourseId AND class_two_id=:classTwoId ";
        Map<String, Object> params = new HashMap<>();
        params.put("twoClassCourseId", v.getTwoTeacherClassCourse().getCourseId());
        params.put("classTwoId", v.getTwoTeacherClassTwo().getId());
        List<TwoTeacherClassStudentAttendent> twoTeacherClassStudentAttendents = twoTeacherClassStudentAttendentDao.findBySql(sql, params);

        int studentNums=twoTeacherClassStudentAttendents.size();//应到学生数
        twoTeacherClassCourseVo.setStudentNums(studentNums);

        int completeClassPeopleNum=0;//准时上课人数

        int lateClassPeopleNum=0;//迟到人数

        int absentClassPeopleNum=0;//缺勤人数

        int noAttendanceCount=0; //未考勤人数

        int deductionCount=0;// 扣费人数

        int makeUp = 0; //补课人数
        for (TwoTeacherClassStudentAttendent a : twoTeacherClassStudentAttendents){
            if (MiniClassAttendanceStatus.CONPELETE.equals(a.getMiniClassAttendanceStatus())){
                completeClassPeopleNum++;
            }
            if (MiniClassAttendanceStatus.LATE.equals(a.getMiniClassAttendanceStatus())){
                lateClassPeopleNum++;
            }
            if (MiniClassAttendanceStatus.ABSENT.equals(a.getMiniClassAttendanceStatus())){
                absentClassPeopleNum++;
            }
            if (MiniClassAttendanceStatus.ABSENT.equals(a.getMiniClassAttendanceStatus()) && a.getSupplementDate()!=null){
                makeUp++;
            }
            if (MiniClassAttendanceStatus.NEW.equals(a.getMiniClassAttendanceStatus())){
                noAttendanceCount++;
            }
            if (MiniClassStudentChargeStatus.CHARGED.equals(a.getChargeStatus())){
                deductionCount++;
            }
        }
//        deductionCount = completeClassPeopleNum+lateClassPeopleNum+absentClassPeopleNum; //扣费人数
//        noAttendanceCount = studentNums-deductionCount;//未考勤人数

        twoTeacherClassCourseVo.setCompleteClassPeopleNum(completeClassPeopleNum);//准时上课人数
        twoTeacherClassCourseVo.setLateClassPeopleNum(lateClassPeopleNum);//迟到人数
        twoTeacherClassCourseVo.setAbsentClassPeopleNum(absentClassPeopleNum);//缺勤人数
        twoTeacherClassCourseVo.setNoAttendanceCount(noAttendanceCount);//未考勤
        twoTeacherClassCourseVo.setMakeUp(makeUp);//补课人数
        twoTeacherClassCourseVo.setDeductionCount(deductionCount);

        return twoTeacherClassCourseVo;
    }

    /**
     * 双师下次上课时间
     *
     * @param currentDate
     * @param twoTeacherClassId
     * @return
     */
    @Override
    public String getNextTwoTeacherClassCourseByDate(String currentDate, String twoTeacherClassId) {
        String sql =" select * from two_teacher_class_course where class_id=:classId and course_date>=:currentDate order by course_date asc  ";
        Map<String,String> map = new HashMap<>();
        map.put("classId", twoTeacherClassId);
        map.put("currentDate", currentDate);
        List<TwoTeacherClassCourse> list = twoTeacherClassCourseDao.findBySql(sql, map);
        if (list.size()>0){
            return list.get(0).getCourseDate();
        }
        return null;
    }

    @Override
    public DataPackage findTwoTeacherClassCourseList(DataPackage dp, TwoTeacherClassCourseVo vo) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer hql=new StringBuffer();
        hql.append(" from TwoTeacherClassCourse where 1=1");

        if(StringUtils.isNotBlank(vo.getTwoTeacherClassId())){
            hql.append(" and twoTeacherClass.classId='"+vo.getTwoTeacherClassId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getStartDate())){
            hql.append(" and courseDate >='"+vo.getStartDate()+"'");
        }

        if(StringUtils.isNotBlank(vo.getEndDate())){
            hql.append(" and courseDate <='"+vo.getEndDate()+"'");
        }


        if (StringUtils.isNotBlank(dp.getSord())
                && StringUtils.isNotBlank(dp.getSidx())) {
            hql.append(" order by "+dp.getSidx()+" "+dp.getSord());
        }else{
            hql.append(" order by courseNum");
        }
        dp=twoTeacherClassCourseDao.findPageByHQL(hql.toString(),dp,true,pmap);
        List<TwoTeacherClassCourseVo> list=HibernateUtils.voListMapping((List) dp.getDatas(), TwoTeacherClassCourseVo.class);
        dp.setDatas(list);

        return dp;
    }

    @Override
    public DataPackage findTwoTeacherClassStudentList(DataPackage dp, TwoTeacherClassStudentVo vo) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer hql=new StringBuffer();
        hql.append(" from TwoTeacherClassStudent where 1=1");

        if(StringUtils.isNotBlank(vo.getClassTwoId())){
            hql.append(" and classTwo.id='"+vo.getClassTwoId()+"'");
        }

        if(StringUtil.isNotBlank(vo.getClassId())){
            hql.append(" and classTwo.twoTeacherClass.classId='"+vo.getClassId()+"'");
        }

        if(StringUtil.isNotBlank(vo.getStudentName())){
            hql.append(" and student.name like :studentName");
            pmap.put("studentName","%"+vo.getStudentName()+"%");
        }
        hql.append(" order by createTime desc");
        dp=twoTeacherClassStudentDao.findPageByHQL(hql.toString(),dp,true,pmap);
        List<TwoTeacherClassStudentVo> list=new ArrayList<>();
        for(TwoTeacherClassStudent ttcs:(List<TwoTeacherClassStudent>)dp.getDatas()){
            TwoTeacherClassStudentVo tvo = HibernateUtils.voObjectMapping(ttcs,TwoTeacherClassStudentVo.class);
            ContractProduct cp = ttcs.getContractProduct();
            tvo.setConsumeAmount(cp.getConsumeAmount());
            tvo.setRemaningtAmount(cp.getRemainingAmount());
            tvo.setRemaningHours(cp.getRemainingAmount().divide(cp.getPrice(),BigDecimal.ROUND_HALF_DOWN));
            list.add(tvo);
        }

        dp.setDatas(list);

        return dp;
    }

    @Override
    public DataPackage findTwoTeacherClassTwoList(DataPackage dp, TwoTeacherClassTwoVo vo) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer hql=new StringBuffer();
        hql.append(" from TwoTeacherClassTwo where 1=1");

        if(StringUtils.isNotBlank(vo.getTwoTeacherClassId())){
            hql.append(" and twoTeacherClass.classId='"+vo.getTwoTeacherClassId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getStartDate())){
            hql.append(" and twoTeacherClass.startDate >='" + vo.getStartDate() + "'");
        }

        if(StringUtils.isNotBlank(vo.getEndDate())){
            hql.append(" and twoTeacherClass.startDate <='"+vo.getEndDate()+"'");
        }

        if(StringUtil.isNotBlank(vo.getName())){
            hql.append(" and name like '%"+vo.getName()+"%'");
        }

        if(StringUtils.isNotBlank(vo.getBlCampusId())){
            hql.append(" and blCampus.id='"+vo.getBlCampusId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getBrenchId())){
            hql.append(" and blCampus.parentId='"+vo.getBrenchId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getClassTypeId())){
            hql.append(" and twoTeacherClass.product.classType.id='"+vo.getClassTypeId()+"'");
        }
        if(StringUtils.isNotBlank(vo.getGradeId())){
            hql.append(" and twoTeacherClass.product.gradeDict.id='"+vo.getGradeId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getSubject())){
            hql.append(" and twoTeacherClass.subject.id='"+vo.getSubject()+"'");
        }

        if(StringUtils.isNotBlank(vo.getProductCourseSeriesId())){
            hql.append(" and twoTeacherClass.product.courseSeries.id='"+vo.getProductCourseSeriesId()+"'");
        }


        if(StringUtils.isNotBlank(vo.getTeacherId())){
            hql.append(" and teacher.id='"+vo.getTeacherId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getMainTeacherId())){
            if("-1".equals(vo.getMainTeacherId())){
                hql.append(" and twoTeacherClass.teacher.id is null ");
            }else {
                hql.append(" and twoTeacherClass.teacher.id='" + vo.getMainTeacherId() + "'");
            }
        }

        if(vo.getStatus()!=null){
            hql.append(" and status='"+vo.getStatus().getValue()+"'");
        }

        if(StringUtil.isNotBlank(vo.getProductVersion())){
            hql.append(" and twoTeacherClass.product.productVersion.id ='"+vo.getProductVersion()+"'");
        }
        if(StringUtil.isNotBlank(vo.getProductQuarter())){
            hql.append(" and twoTeacherClass.product.productQuarter.id ='"+vo.getProductQuarter()+"'");
        }


        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        //权限查询所属组织架构下的所有校区信息
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            hql.append("  and blCampus.id in ( select id from Organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
            for(int i = 1; i < userOrganizations.size(); i++){
                hql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            hql.append(")");
        }
        hql.append(" order by createTime desc");
        dp=twoTeacherClassStudentDao.findPageByHQL(hql.toString(),dp,true,pmap);

        List<TwoTeacherClassTwo> domainList= (List<TwoTeacherClassTwo>) dp.getDatas();
        List<TwoTeacherClassTwoVo> voList =new ArrayList();
        for(TwoTeacherClassTwo domain:domainList){
            TwoTeacherClassTwoVo v=HibernateUtils.voObjectMapping(domain,TwoTeacherClassTwoVo.class);
            if(domain.getBlCampus()!=null) {
                Organization brench= organizationService.findById(domain.getBlCampus().getParentId());
                v.setBlBrenchName(brench.getName());
            }

            v.setRealPeople(findCountStudent(domain.getId()));
            voList.add(v);
        }
        dp.setDatas(voList);
        return dp;
    }

    @Override
    public void saveTwoTeacherClassCourse(TwoTeacherClassCourse[] courseList, String arrangedHours) {
        User user = userService.getCurrentLoginUser();
        int classId=0;
        for (TwoTeacherClassCourse classCourse : courseList) {
            classCourse.setCreateTime(DateTools.getCurrentDateTime());
            classCourse.setCreateUserId(user.getUserId());
            classCourse.setModifyTime(DateTools.getCurrentDateTime());
            classCourse.setModifyUserId(user.getUserId());
            classCourse.setCourseStatus(CourseStatus.NEW);
            if(classId == 0 ){
                classId=classCourse.getTwoTeacherClass().getClassId();
            }
            classCourse.setAuditStatus(AuditStatus.UNAUDIT);

            twoTeacherClassCourseDao.save(classCourse);
            //解决考勤记录
            initTwoTeacherClassStudentAttendent(classCourse);
        }

        if(classId>0) {
            updateCourseNumByClassId(classId);
            TwoTeacherClass twoTeacherClass = twoTeacherClassDao.findById(classId);
            twoTeacherClass = updateTwoTeacherClassStartDate(twoTeacherClass);
            twoTeacherClassDao.save(twoTeacherClass);
        }
//        TwoTeacherClass twoClass=twoTeacherClassDao.findById(classId);
//        twoClass.setArrangedHours(Double.valueOf(arrangedHours));
//        miniClassDao.save(miniClass);
    }

    public void initTwoTeacherClassStudentAttendent(TwoTeacherClassCourse classCourse){
        User user = userService.getCurrentLoginUser();
        List<TwoTeacherClassStudent> studentList = findTwoTeacherClassStudentByClassId(classCourse.getTwoTeacherClass().getClassId());
        for (TwoTeacherClassStudent mcs: studentList) {
            if(DateTools.daysBetween(mcs.getFirstSchoolTime(),classCourse.getCourseDate())<0){
                continue;
            }
            Student student = mcs.getStudent();
            TwoTeacherClassStudentAttendent attendent = new TwoTeacherClassStudentAttendent();
            attendent.setTwoTeacherClassCourse(classCourse);
            attendent.setTwoTeacherClassTwo(mcs.getClassTwo());//辅班
            attendent.setStudent(student);
            attendent.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW); // 未上课
            attendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
            CourseStatus courseStatus = getCourseStatusByClassTwoIdAndCourseId(mcs.getClassTwo().getId(), classCourse.getCourseId()); //不改变原来的课程状态
            attendent.setCourseStatus(courseStatus);//未上课
            attendent.setAuditStatus(AuditStatus.UNAUDIT);//未审批
            attendent.setCreateUser(user);
            attendent.setCreateTime(DateTools.getCurrentDateTime());
            attendent.setModifyTime(DateTools.getCurrentDateTime());
            attendent.setModifyUser(user);
            attendent.setCourseDateTime(classCourse.getCourseDate()+" "+classCourse.getCourseTime());
            twoTeacherClassStudentAttendentDao.save(attendent);
        }
    }

    /**
     * 根据课程id和辅班id查找考勤记录的辅班状态
     * @param classTwoId
     * @param courseId
     * @return
     */
    private CourseStatus getCourseStatusByClassTwoIdAndCourseId(int classTwoId, int courseId) {
        String sql = " select * from two_teacher_class_student_attendent where CLASS_TWO_ID=:classTwoId and TWO_CLASS_COURSE_ID=:courseId group by CLASS_TWO_ID, TWO_CLASS_COURSE_ID  ";
        Map<String, Object> map = new HashMap<>();
        map.put("classTwoId", classTwoId);
        map.put("courseId", courseId);
        List<TwoTeacherClassStudentAttendent> list = twoTeacherClassStudentAttendentDao.findBySql(sql, map);
        if (list.size()>0){
            return list.get(0).getCourseStatus();
        }
        return CourseStatus.NEW;
    }

    public List<TwoTeacherClassStudent> findTwoTeacherClassStudentByClassId(int classId){
        Map<String,Object> pmap= new HashMap();
        String hql="from TwoTeacherClassStudent where classTwo.twoTeacherClass.classId='"+classId+"'";
        return twoTeacherClassStudentDao.findAllByHQL(hql,pmap);
    }

    @Override
    public Response saveTwoTeacherClassTwo(TwoTeacherClassTwoVo vo) {
        User user = userService.getCurrentLoginUser();
        Response res=new Response();
        TwoTeacherClassTwo domain=HibernateUtils.voObjectMapping(vo, TwoTeacherClassTwo.class);
        String countSql = "select count(1)  from TWO_TEACHER_CLASS_TWO where CLASS_ID = :classId and TEACHER_ID = :teacherId ";
        Map<String, Object> countParams = new HashMap<>();
        countParams.put("classId", vo.getTwoTeacherClassId());
        countParams.put("teacherId", vo.getTeacherId());
        if(domain.getId()==0){
            domain.setStatus(MiniClassStatus.PENDDING_START);
            domain.setCreateTime(DateTools.getCurrentDateTime());
            domain.setCreateUserId(user.getUserId());
        }else {
            countSql += " and CLASS_TWO_ID != :id ";
            countParams.put("id", vo.getId());
            String sql = "select CREATE_TIME, CREATE_USER_ID  from TWO_TEACHER_CLASS_TWO where CLASS_TWO_ID =:id ";
            Map<String, Object> params = new HashMap<>();
            params.put("id", vo.getId());
            List<Map<Object, Object>> result = twoTeacherClassTwoDao.findMapBySql(sql, params);
            Map<Object, Object> objectObjectMap = result.get(0);
            if (objectObjectMap.get("CREATE_TIME")!=null){
                domain.setCreateTime(objectObjectMap.get("CREATE_TIME").toString());
            }
            if (objectObjectMap.get("CREATE_USER_ID")!=null){
                domain.setCreateUserId(objectObjectMap.get("CREATE_USER_ID").toString());
            }
        }
        domain.setModifyUserId(user.getUserId());
        domain.setModifyTime(DateTools.getCurrentDateTime());
        int count = twoTeacherClassTwoDao.findCountSql(countSql, countParams);
        if (count > 0) {
            throw new ApplicationException("当前辅导老师已进入同一主班下的另一个辅班，请选择其他辅导老师。");
        }
        try {
            twoTeacherClassTwoDao.save(domain);
            twoTeacherClassTwoDao.flush();
        }catch (DataIntegrityViolationException e){
            throw new ApplicationException("相同校区存在同名辅班名字");
        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
        }


        return res;
    }

    @Override
    public Response batchSaveTwoTeacherClassTwo(TwoTeacherClassTwoVo[] twoTeacherClassTwoVos) {

        checkParams(twoTeacherClassTwoVos);
        for (TwoTeacherClassTwoVo vo : twoTeacherClassTwoVos){
            saveTwoTeacherClassTwo(vo);
        }
        Response res=new Response();
        return res;
    }

    /**
     * 不能添加重复的 辅班
     * @param twoTeacherClassTwoVos
     */
    private void checkParams(TwoTeacherClassTwoVo[] twoTeacherClassTwoVos) {
        Map<String, TwoTeacherClassTwoVo> map1 = new HashMap<>();
        Map<String, TwoTeacherClassTwoVo> map2 = new HashMap<>();
        Map<String, TwoTeacherClassTwoVo> map3 = new HashMap<>();
        for (TwoTeacherClassTwoVo vo : twoTeacherClassTwoVos){
            /**
             * 同一校区班级名称不能完全相同
             */
            if (map1.get(vo.getName()+vo.getBlCampusId()) == null){
                map1.put(vo.getName()+vo.getBlCampusId(), vo);
            }else {
                throw new ApplicationException("班级名称不能完全相同，请修改");
            }

            /**
             * 同一校区同一教室只能创建一个辅班
             */
            if (map2.get(vo.getBlCampusId()+vo.getClassroomId()) == null){
                map2.put(vo.getBlCampusId()+vo.getClassroomId(), vo);
            }else {
                throw new ApplicationException("同一校区同一教室只能创建一个辅班，请修改");
            }

            /**
             * 同一辅导老师只能服务一个辅班
             */
            if (map3.get(vo.getTeacherId()) == null){
                map3.put(vo.getTeacherId(), vo);
            }else {
                throw new ApplicationException("同一辅导老师只能服务一个辅班，请修改");
            }
        }

    }

    /**
     * 获取双师主班老师
     * @return
     */
    @Override
    public List<Map<String, String>> getTwoTeacherClassTeacher() {


        StringBuffer hql = new StringBuffer();

        hql.append(" select distinct t.teacher from TwoTeacherClass t ,TwoTeacherClassBrench b where 1=1 and t.classId=b.classId");
        hql.append(roleQLConfigService.getAppendSqlByAllOrg("双师集团","hql","b.brenchId.id"));
        Map<String, Object> params = Maps.newHashMap();
        List<User> list = userDao.findAllByHQL(hql.toString(), params);


        List<Map<String, String>> result = new ArrayList<>();
        for (User user : list){
            Map<String, String> map = new LinkedHashMap<>();
            map.put("teacherId", user.getUserId());
            map.put("teacherName", user.getName());
            map.put("account", user.getAccount());
            result.add(map);
        }
        return result;
    }




    @Override
    public void AddStudentToClasss(String studentId, int twoClassId, String contractId, String contractProductId, String firstCourseDate) {
        List list=getTwoClassStudent(twoClassId, studentId);
        if(list.size()>0) {
            throw new ApplicationException("该学生已经报读了该班级，请不要重复报班！");
        }

        TwoTeacherClassTwo domain  =twoTeacherClassTwoDao.findById(twoClassId);
        if(findCountStudent(twoClassId)>=domain.getPeopleQuantity()){
            throw new ApplicationException("该班级已经满员，请选择另外的班级报读！");
        }

        User user = userService.getCurrentLoginUser();
        TwoTeacherClassStudent ttcs=new TwoTeacherClassStudent();
        ttcs.setClassTwo(domain);
        ttcs.setStudent(new Student(studentId));
        ttcs.setContractProduct(new ContractProduct(contractProductId));
        ttcs.setFirstSchoolTime(firstCourseDate);
        ttcs.setCreateUserId(user.getUserId());
        ttcs.setCreateTime(DateTools.getCurrentDateTime());
        ttcs.setModifyUserId(user.getUserId());
        ttcs.setModifyTime(DateTools.getCurrentDateTime());
        twoTeacherClassStudentDao.save(ttcs);
        addStudentAttendent(twoClassId,studentId,firstCourseDate);
    }

    public void addStudentAttendent(int twoClassId,String studentId, String firstCourseDate){
        User user = userService.getCurrentLoginUser();
        List<TwoTeacherClassCourse> courseList = findCourseByClassId(twoClassId);
        TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassTwoDao.findById(twoClassId);
        for (TwoTeacherClassCourse mcs: courseList) {
            if(DateTools.daysBetween(mcs.getCourseDate(),firstCourseDate)>0){
                continue;
            }
            //已经结算的考勤记录不需要再添加了。
            List<TwoTeacherClassStudentAttendent> attendentList=twoTeacherClassStudentAttendentDao.getAttendentByCourseAndStudentId(studentId,mcs.getCourseId());
            boolean flag=false;
            for(TwoTeacherClassStudentAttendent attendent:attendentList){
                if(attendent.getChargeStatus()!=null && attendent.getChargeStatus().equals(MiniClassStudentChargeStatus.CHARGED)){
                    flag=true;
                }
            }

            if(flag){
                continue;
            }


            TwoTeacherClassStudentAttendent attendent = new TwoTeacherClassStudentAttendent();
            attendent.setTwoTeacherClassCourse(mcs);
            attendent.setTwoTeacherClassTwo(twoTeacherClassTwo);
            attendent.setStudent(new Student(studentId));
            attendent.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW); // 未上课
            attendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);// 未扣费
            CourseStatus courseStatus = getCourseStatusByClassTwoIdAndCourseId(twoTeacherClassTwo.getId(), mcs.getCourseId());//不改变原来的课程状态
            attendent.setCourseStatus(courseStatus);//未上课
            attendent.setAuditStatus(AuditStatus.UNAUDIT);//未审批
            attendent.setCreateUser(user);
            attendent.setCreateTime(DateTools.getCurrentDateTime());
            attendent.setModifyTime(DateTools.getCurrentDateTime());
            attendent.setModifyUser(user);
            attendent.setCourseDateTime(mcs.getCourseDate()+" "+mcs.getCourseTime());
            twoTeacherClassStudentAttendentDao.save(attendent);
        }
    }

    public List<TwoTeacherClassCourse> findCourseByClassId(int classId){
        Map<String,Object> pmap= new HashMap();
        TwoTeacherClassTwoVo two=findTwoTeacherClassTwoById(classId);
        String hql="from TwoTeacherClassCourse where twoTeacherClass.classId='"+two.getTwoTeacherClassId()+"'";
        return twoTeacherClassCourseDao.findAllByHQL(hql,pmap);
    }

    @Override
    public Response deleteTwoTeacherClass(int classId) {
        Map<String,Object> pmap= new HashMap();
        TwoTeacherClass domain= twoTeacherClassDao.findById(classId);
        if(domain!=null) {
            String hql ="from TwoTeacherClassTwo where twoTeacherClass.classId='"+classId+"'";
            List<TwoTeacherClassTwo> tt=twoTeacherClassTwoDao.findAllByHQL(hql,pmap);

            for (TwoTeacherClassTwo t:tt){
                List list=getTwoClassStudent(t.getId(),null);
                if(list.size()>0){
                    throw new ApplicationException("本班已经有学生报读或已经开课，不能删除！");
                }
                twoTeacherClassTwoDao.delete(t);
            }
            twoTeacherClassDao.delete(domain);
        }
        return new Response();
    }

    @Override
    public Response deleteTwoTeacherClassTwo(int id) {
        TwoTeacherClassTwo domain= twoTeacherClassTwoDao.findById(id);
        if(domain!=null) {
            List list=getTwoClassStudent(id,null);
            if(list.size()>0){
                throw new ApplicationException("本班已经有学生报读或已经开课，不能删除！");
            }
            twoTeacherClassTwoDao.delete(domain);
        }
        return new Response();
    }

    @Override
    public TwoTeacherClassTwoVo findTwoTeacherClassTwoById(int id) {
        return HibernateUtils.voObjectMapping(twoTeacherClassTwoDao.findById(id), TwoTeacherClassTwoVo.class);
    }

    @Override
    public void deleteClassCourse(int[] courseId) {
        int classId=0;
        for(int id : courseId){
            TwoTeacherClassCourse course=twoTeacherClassCourseDao.findById(id);
            deleteClassStudentAttendent(id);
            twoTeacherClassCourseDao.delete(course);
            if(classId==0){
                classId=course.getTwoTeacherClass().getClassId();
            }
        }
        if(classId>0) {
            updateCourseNumByClassId(classId);
        }
    }

    @Override
    public TwoTeacherClassCourseVo findTwoTeacherClassCourseById(int courseId) {
        return HibernateUtils.voObjectMapping(twoTeacherClassCourseDao.findById(courseId), TwoTeacherClassCourseVo.class);
    }

    @Override
    public Response saveTwoTeacherClassCourse(TwoTeacherClassCourseVo vo) {
        User user = userService.getCurrentLoginUser();
        TwoTeacherClassCourse domain=HibernateUtils.voObjectMapping(vo, TwoTeacherClassCourse.class);
        if(domain.getCourseId()==0){
            domain.setCreateTime(DateTools.getCurrentDateTime());
            domain.setCreateUserId(user.getUserId());
        }else{
            TwoTeacherClassCourse course=twoTeacherClassCourseDao.findById(domain.getCourseId());
            if(domain.getCourseHours().compareTo(course.getCourseHours())>0) {
                double readyHours = findCountCourse(course.getTwoTeacherClass().getClassId());
                if(domain.getCourseHours()-course.getCourseHours()+readyHours>course.getTwoTeacherClass().getTotalClassHours()){
                    throw new ApplicationException("修改课时超出总课时数！");
                }
            }

            domain.setAuditStatus(course.getAuditStatus());
            domain.setCourseStatus(course.getCourseStatus());
            domain.setTwoTeacherClass(course.getTwoTeacherClass());
        }
        domain.setModifyUserId(user.getUserId());
        domain.setModifyTime(DateTools.getCurrentDateTime());
        twoTeacherClassCourseDao.merge(domain);
        twoTeacherClassDao.flush();

        updateCourseNumByClassId(domain.getTwoTeacherClass().getClassId());//课程序号

        /**
         * 更新主班开始日期
         */
        TwoTeacherClass twoTeacherClass = twoTeacherClassDao.findById(domain.getTwoTeacherClass().getClassId());
        twoTeacherClass = updateTwoTeacherClassStartDate(twoTeacherClass);
        twoTeacherClassDao.save(twoTeacherClass);
        return new Response();
    }

    @Override
    public DataPackage findTwoTeacherClassAttendentList(DataPackage dp, TwoTeacherClassStudentAttendentVo vo) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer hql=new StringBuffer();
        hql.append(" from TwoTeacherClassStudentAttendent where 1=1");


        if(StringUtils.isNotBlank(vo.getTwoTeacherClassCourseId())){
            hql.append(" and twoTeacherClassCourse.courseId = '"+vo.getTwoTeacherClassCourseId()+"'");
        }

        hql.append(" order by createTime desc");
        dp=twoTeacherClassCourseDao.findPageByHQL(hql.toString(),dp,true,pmap);
        List<TwoTeacherClassStudentAttendentVo> list=HibernateUtils.voListMapping((List) dp.getDatas(), TwoTeacherClassStudentAttendentVo.class);
        dp.setDatas(list);
        return dp;
    }

    @Override
    public List<StudentVo> getStudentWantListByClassId(String classId) {
        return null;
    }

    /**
     * 查询已报课时数
     * @param classId
     * @return
     */
    @Override
    public double findCountCourse(int classId) {
        Map<String,Object> pmap= new HashMap();
        String sql ="select sum(courseHours) from TwoTeacherClassCourse  where twoTeacherClass.classId ='"+classId+"'";
//        String hql="from TwoTeacherClassCourse where twoTeacherClass.classId='"+classId+"'";
        return twoTeacherClassCourseDao.findSumHql(sql,pmap);
    }

    @Override
    public ContractProductVo getTwoClassStuRemain(int classId, String studentId) {
        List<TwoTeacherClassStudent> student = getTwoClassStudent(classId, studentId);
        TwoTeacherClassStudent stuClass= new TwoTeacherClassStudent();
        if(student.size()>0){
            stuClass= student.get(0);
        }
        ContractProduct contractProduct = stuClass.getContractProduct();
        contractService.calSingleContractProduct(contractProduct);

        ContractProductVo contractProductVo = HibernateUtils.voObjectMapping(contractProduct, ContractProductVo.class);
        contractProductVo.setStudentName(stuClass.getStudent().getName());
        contractProductVo.setRemainFinance(contractProduct.getRemainingAmount());// 设置剩余资金

        BigDecimal dealDiscount = contractProduct.getDealDiscount();// 折扣
        BigDecimal price = contractProduct.getPrice();// 每节课价格
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

        contractProductVo.setRemainCourseHour(remainCourseHour);// 设置剩余课时

        return contractProductVo;
    }

    @Override
    public Response checkAllowAddStudent4Class(String studentIds, int classId) {
        Response response = new Response();
        String[] stuIds=studentIds.split(",");
        int newStudentNumber = stuIds.length;
        int miniClassMaxPeopleNumber = getMiniClassMaxPeopleNumber(classId);// 最大人数
        int peopleExistingNumber = findCountStudent(classId);// 现有人数
        int afterPeopleNumber = peopleExistingNumber + newStudentNumber;
        if (afterPeopleNumber <= miniClassMaxPeopleNumber) {
            // 小班现有人数少于最大人数，还可以继续报名
            response.setResultCode(0);
        } else {
            response.setResultCode(-1);
            response.setResultMessage("报名人数超出双师计划招生人数（"+ miniClassMaxPeopleNumber + "人）");
        }
        return response;
    }

    @Override
    public void changeStudentTwoClass(int oldClassId, int newClassId, String studentIds, String firstClassDate) {
        String ids="";
        List<TwoTeacherClassStudent> miniClassStudent = null;
        for(String id: studentIds.split(",")){
            ids+=",'"+id+"'";
            miniClassStudent = getTwoClassStudent(newClassId,id);
            if (miniClassStudent.size()>0) {
                Student student = studentDao.findById(id);
                throw new ApplicationException("学生" + student.getName() + "在想要转入的班中已有记录！");
            }
            List<TwoTeacherClassStudent> oldStudent= getTwoClassStudent(oldClassId,id);
            if(oldStudent.size()>0){
                TwoTeacherClassStudent stu=oldStudent.get(0);
                AddStudentToClasss(id, newClassId, stu.getContractProduct().getContract().getId(), stu.getContractProduct().getId(), firstClassDate);
            }

            for (TwoTeacherClassStudent s:oldStudent){
                twoTeacherClassStudentDao.delete(s);
            }
            //删除双师考勤记录的时候需要检查扣费记录里面不会再有这些双师考勤的id redmine id #1414
            contractService.updateNullTwoTeacherClassStudentAttendentByStudentAndTwoClassId(id, oldClassId);
            twoTeacherClassStudentAttendentDao.deleteTwoTeacherClassStudentAttendentByStudentAndTwoClassId(id, oldClassId);
        }
    }

    @Override
    public List<TwoTeacherClassTwoVo> findTwoClassForChangeClassSelect(int twoClassId) {
        Map<String,Object> pmap= new HashMap();
        TwoTeacherClassTwo t= twoTeacherClassTwoDao.findById(twoClassId);
        Organization o =  userService.getBelongBranch();
        String hql = " from TwoTeacherClassTwo where status<>'CONPELETE' and twoTeacherClass.product.id='"+t.getTwoTeacherClass().getProduct().getId()+"' and blCampus.parentId='"+o.getId()+"' and id<>'"+twoClassId+"'";
        return HibernateUtils.voListMapping(twoTeacherClassTwoDao.findAllByHQL(hql,pmap),TwoTeacherClassTwoVo.class);
    }

    @Override
    public Map getClassCourseTimeInfo(String classId) {
        Map<String,Object> pmap= new HashMap();
        StringBuffer sql = new StringBuffer();
        sql.append("  SELECT ");
        sql.append("     IFNULL(mc.TOTAL_CLASS_HOURS, 0) AS TOTAL_CLASS_HOURS,");
        sql.append("     IFNULL(SUM(CASE WHEN mcc.COURSE_STATUS <> 'CANCEL' THEN mcc.COURSE_HOURS ELSE 0 END), 0) AS ARRANGED_COURSE_HOURS,");
        sql.append("     (mc.TOTAL_CLASS_HOURS - IFNULL(SUM(CASE WHEN mcc.COURSE_STATUS <> 'CANCEL' THEN mcc.COURSE_HOURS ELSE 0 END), 0)) AS NOT_ARRANG_COURSE_HOURS,");
        sql.append("     SUM(CASE WHEN mcc.COURSE_STATUS = 'CHARGED' THEN mcc.COURSE_HOURS ELSE 0 END) AS CHARGED_COURSE_HOURS,");
        sql.append("     SUM(CASE WHEN mcc.COURSE_STATUS = 'NEW' THEN mcc.COURSE_HOURS ELSE 0 END) AS NEW_COURSE_HOURS,");
        sql.append("      IFNULL(SUM(CASE  WHEN mcc.COURSE_STATUS = 'CANCEL' THEN mcc.COURSE_HOURS ELSE 0 END), 0) CANCELED_HOURS,");
        sql.append("     SUM(CASE WHEN mcc.COURSE_STATUS = 'TEACHER_ATTENDANCE' OR mcc.COURSE_STATUS = 'CHARGED' THEN mcc.COURSE_HOURS ELSE 0 END) AS ATTENDANCE_COURSE_HOURS");
        sql.append(" FROM");

        sql.append("     two_teacher_class mc");
        sql.append("         LEFT JOIN");
        sql.append("     two_teacher_class_course mcc ON mc.class_id = mcc.class_id");
        sql.append("         LEFT JOIN");
        sql.append("     product p ON p.id = mc.PRODUCE_ID");

        sql.append(" where mc.class_id='"+classId+"'");

        List<Map<Object, Object>> list=twoTeacherClassTwoDao.findMapBySql(sql.toString(), pmap);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new ApplicationException("没找到双师ID为："+classId+"的课时信息");
        }
    }

    @Override
    public TwoTeacherClassTwoVo findTwoTeacherTwoByContractProductId(String conProId) {
        Map param=new HashMap();
        String hql="select t  from TwoTeacherClassTwo t ,TwoTeacherClassStudent s where t.id=s.classTwo.id";
        hql+=" and s.contractProduct.id =:contractProductId";
        param.put("contractProductId",conProId);
        TwoTeacherClassTwo t=twoTeacherClassTwoDao.findOneByHQL(hql,param);
        if(t!=null){
            return  HibernateUtils.voObjectMapping(t,TwoTeacherClassTwoVo.class);
        }
        return null;
    }

    @Override
    public BigDecimal findClassWillBuyHour(int classId, String startDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql=new StringBuilder();
        sql.append("select course_Hours from two_teacher_class_course where course_date >= :startDate and class_id = :classId and COURSE_STATUS<>'CANCEL' ");
        params.put("startDate", startDate);
        params.put("classId", classId);
        List<Map<Object, Object>> map=twoTeacherClassCourseDao.findMapBySql(sql.toString(), params);
        BigDecimal hours=BigDecimal.ZERO;
        for(Map<Object,Object> m:map){
            hours=hours.add((BigDecimal)m.get("course_Hours"));
        }

        return hours;
    }

    @Override
    public void deleteTwoClassStudentByCpId(String cpId) {
        Map param=new HashMap();
        String hql=" from TwoTeacherClassStudent  where 1=1";
        hql+=" and contractProduct.id =:contractProductId";
        param.put("contractProductId",cpId);
        List<TwoTeacherClassStudent> t=twoTeacherClassStudentDao.findAllByHQL(hql,param);
        for(TwoTeacherClassStudent tt:t){
            deleteClassStudentAttendentByStudentId(tt.getStudent().getId(), tt.getClassTwo().getId());
            twoTeacherClassStudentDao.delete(tt);
        }

    }

    public void deleteClassStudentAttendentByStudentId(String studentId, int classTwoId){
        Map param=new HashMap();
        String hql=" from TwoTeacherClassStudentAttendent  where chargeStatus<>'CHARGED' ";
        hql+=" and student.id =:studentId";
        hql+=" and twoTeacherClassTwo.id=:classTwoId ";
        param.put("studentId",studentId);
        param.put("classTwoId", classTwoId);
        List<TwoTeacherClassStudentAttendent> at=twoTeacherClassStudentAttendentDao.findAllByHQL(hql,param);
        for (TwoTeacherClassStudentAttendent att:at) {
            twoTeacherClassStudentAttendentDao.delete(att);
        }
    }

    public int getMiniClassMaxPeopleNumber(int classId) {
        TwoTeacherClassTwo mniClass = twoTeacherClassTwoDao.findById(classId);
        Integer peopleQuantity = mniClass.getPeopleQuantity();
        if (null == peopleQuantity) {
            throw new RuntimeException("未设置该小班报名人数，请设置小班报名人数！");
        }
        return peopleQuantity;
    }

    public int findCountStudent(int twoClassTwoId){
        Map<String,Object> pmap= new HashMap();
        String sql ="select count(1) from two_teacher_class_student where CLASS_TWO_ID='"+twoClassTwoId+"'";
        return twoTeacherClassStudentDao.findCountSql(sql,pmap);
    }

    public void deleteClassStudentAttendent(int courseId){
        List<TwoTeacherClassStudentAttendent> list =findClassStudentAttendentByCourseid(courseId);
        for(TwoTeacherClassStudentAttendent attendent:list){
            twoTeacherClassStudentAttendentDao.delete(attendent);
        }
    }

    public List<TwoTeacherClassStudentAttendent> findClassStudentAttendentByCourseid(int courseId){
        Map<String,Object> pmap= new HashMap();
        String hql="from TwoTeacherClassStudentAttendent where twoTeacherClassCourse.courseId='"+courseId+"'";
        return twoTeacherClassStudentAttendentDao.findAllByHQL(hql,pmap);
    }

    private List getTwoClassStudent(int twoClassId, String studentId) {
        Map<String,Object> pmap= new HashMap();
        String hql="from TwoTeacherClassStudent where classTwo.id='"+twoClassId+"' ";

        if(StringUtils.isNotBlank(studentId)){
            hql+=" and student.id ='"+studentId+"'";
        }

        return twoTeacherClassStudentDao.findAllByHQL(hql,pmap);
    }

    public void saveClassBrench(Set<String> brenchId,TwoTeacherClass classId){
        for(String brench:brenchId) {
            TwoTeacherClassBrench tcb=new TwoTeacherClassBrench();
            tcb.setBrenchId(new Organization(brench));
            tcb.setClassId(classId);
            twoTeacherBrenchDao.save(tcb);
        }
    }

    public void deleteClassBrench(int classId){
        List<TwoTeacherClassBrench> brench= findClassBrenchByClassId(classId);
        twoTeacherBrenchDao.deleteAll(brench);
        twoTeacherBrenchDao.flush();
    }

    public List<TwoTeacherClassBrench>  findClassBrenchByClassId(int classId){
        Map<String,Object> pmap= new HashMap();
        String hql = "from TwoTeacherClassBrench where classId = '"+classId+"'";
        return twoTeacherBrenchDao.findAllByHQL(hql,pmap);
    }

    /**
     * 获取所有有主班的分公司
     *
     * @return
     */
    @Override
    public List<Organization> getAllClassBranch() {
        String hql = "select distinct brenchId from TwoTeacherClassBrench ";
        List list = twoTeacherBrenchDao.findAllByHQL(hql, new HashMap());

        return list;
    }

    public List findClassBrenchNameByClassId(int classId){
        Map<String,Object> pmap= new HashMap();
        String sql =" select name from two_teacher_brench ttb , organization  o where ttb.brench_id = o.ID and ttb.class_id='"+classId+"'";

        String hql=" from TwoTeacherClassBrench where classId.classId='"+classId+"'";
        return twoTeacherBrenchDao.findAllByHQL(hql,pmap);
//        return twoTeacherBrenchDao.findBySql(sql);
    }



    @Override
    public void updateCourseNumByClassId(int miniClassId){
        List<TwoTeacherClassCourse> list=findMiniClassCourseByClassId(miniClassId);
        int i=1;
        for(TwoTeacherClassCourse m:list){
            m.setCourseNum(i);
            twoTeacherClassCourseDao.save(m);
            i++;
        }
    }


    public List<TwoTeacherClassCourse> findMiniClassCourseByClassId(int classId){
        StringBuffer hql = new StringBuffer();
        hql.append(" from TwoTeacherClassCourse  where courseStatus<>'CANCEL' ");
        Map<String, Object> params = Maps.newHashMap();
        hql.append(" and twoTeacherClass.classId = :classId ");
        params.put("classId", classId);

        hql.append(" order by courseDate ");
        return twoTeacherClassCourseDao.findAllByHQL(hql.toString(),params);
    }


    @Override
    public List getLoginUserMainTeacherList() {
        StringBuilder sql = new StringBuilder("select distinct u.user_id userId,u.name from user u,two_teacher_class ttc,two_teacher_class_two ttct where u.user_id =ttc.teacher_id\n" +
                "and ttc.class_id =ttct.CLASS_ID ");
        sql.append(getOrganizationSql("ttct.BL_CAMPUS_ID"));
        return twoTeacherClassDao.findMapBySql(sql.toString(),new HashMap<String, Object>());
    }

    @Override
    public List getLoginUserTwoTeacherList() {
        StringBuilder sql = new StringBuilder("select distinct u.user_id userId,u.name from user u,two_teacher_class_two ttct where u.user_id =ttct.teacher_id  ");
        sql.append(getOrganizationSql("ttct.BL_CAMPUS_ID"));
        return twoTeacherClassDao.findMapBySql(sql.toString(),new HashMap<String, Object>());
    }


    public String getOrganizationSql(String orgColumn){
        StringBuilder sql = new StringBuilder();
        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        //权限查询所属组织架构下的所有校区信息
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append("  and "+orgColumn+" in ( select id from Organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(")");
        }
        return sql.toString();
    }

    /**
     * 双师退班
     *
     * @param studentIds
     * @param twoClassId
     */
    @Override
    public void deleteStudentInTwoTeacherTwo(String studentIds, int twoClassId) {
        String[] stuIds=studentIds.split(",");

        for (String id : stuIds){
            StringBuffer hql = new StringBuffer();
            hql.append(" from TwoTeacherClassStudent ttcs where ttcs.classTwo.id=:twoClassId and student.id=:studentId ");
            Map<String, Object> map= new HashMap<>();
            map.put("twoClassId", twoClassId);
            map.put("studentId", id);
            TwoTeacherClassStudent twoTeacherClassStudent = twoTeacherClassStudentDao.findOneByHQL(hql.toString(), map);

//            StringBuffer sb = new StringBuffer();
//            sb.append(" from TwoTeacherClassStudentAttendent where twoTeacherClassTwo.id=:twoClassId and student.id=:studentId and chargeStatus='CHARGED' ");
//            List<TwoTeacherClassStudentAttendent> check = twoTeacherClassStudentAttendentDao.findAllByHQL(sb.toString(), map);
//            if (check.size()>0){
//                Student student = check.get(0).getStudent();
//                throw new ApplicationException("学生："+student.getName()+"已扣费，不允许删除！");
//            }

            if (twoTeacherClassStudent!=null){
                twoTeacherClassStudentDao.delete(twoTeacherClassStudent);
            }
            //删除双师考勤记录的时候需要检查扣费记录里面不会再有这些双师考勤的id redmine id #1414
            contractService.updateNullTwoTeacherClassStudentAttendentByStudentAndTwoClassId(id, twoClassId);
            twoTeacherClassStudentAttendentDao.deleteTwoTeacherClassStudentAttendentByStudentAndTwoClassId(id, twoClassId);

        }
    }

    @Override
    public TwoClassCourseStudentRosterVo getTwoCourseStudentRoster(Integer twoClassId) {
        //传进来的双师辅班的id
        //跟进id获取辅班
        TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassTwoDao.findById(twoClassId);
        TwoTeacherClass twoTeacherClass =twoTeacherClassTwo.getTwoTeacherClass();

        TwoTeacherClassTwoVo twoTeacherClassTwoVo = HibernateUtils.voObjectMapping(twoTeacherClassTwo, TwoTeacherClassTwoVo.class);
        TwoClassCourseStudentRosterVo vo = new TwoClassCourseStudentRosterVo();
        vo.setTwoClassId(twoClassId.toString());
        vo.setTwoClassName(twoTeacherClassTwoVo.getName());
        vo.setTeacherName(twoTeacherClassTwoVo.getMainTeacherName());
        vo.setTeacherTwoName(twoTeacherClassTwoVo.getTeacherName());
        vo.setClassTimeLength(twoTeacherClassTwoVo.getClassTimeLength());
        vo.setClassTime(twoTeacherClassTwoVo.getCourseTime());
        vo.setEveryCourseClassNum(twoTeacherClassTwoVo.getEveryCourseClassNum());

        ClassroomManage classroom = twoTeacherClassTwo.getClassroom();
        if (classroom!=null){
            vo.setClassroomName(classroom.getClassroom());
        }else {
            vo.setClassroomName("");
        }


        //获取学生列表
        List<TwoTeacherClassStudent> studentList = this.getTwoTeacherClassStudentsByTwoClassId(twoClassId.toString());
        List<TwoTeacherClassCourse> courseList = this.getTwoTeacherClassCourseByClassId(twoTeacherClass.getClassId());
        int studentSize = studentList.size() <= 30 ? studentList.size() : 30;
        String studentNames[] = new String[studentSize];

        String studentIds[] = new String[studentSize];
        int i = 0;
        for (TwoTeacherClassStudent twoStudent : studentList) {
            if (i < studentSize) {
                Student student = twoStudent.getStudent();
                studentNames[i] = student.getName();
                studentIds[i] = student.getId();
            }
            i++;
        }
        if(ArrayUtils.isNotEmpty(studentIds)){
            String studentContacts[]  = this.getCustomerContactByStuIds(studentIds, twoClassId.toString());
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
            vo.setStartDate(twoTeacherClass.getStartDate());
        }
        for (TwoTeacherClassCourse course : courseList) {
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

    private  String[] getCustomerContactByStuIds(String[]studentIds,String twoClassId){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = Maps.newHashMap();
        params.put("studentIds", studentIds);
        params.put("twoClassId", twoClassId);
        sql.append("select cus.CONTACT as contact,ttcs.STUDENT_ID as stuId from two_teacher_class_student ttcs ,contract_product cp,contract c,customer cus where ttcs.CONTRACT_PRODUCT_ID = cp.ID and  cp.CONTRACT_ID = c.ID and c.CUSTOMER_ID = cus.ID ");
        sql.append("and ttcs.CLASS_TWO_ID = :twoClassId and ttcs.STUDENT_ID in ( :studentIds ) ");
        List<Map<Object, Object>> list = twoTeacherClassStudentDao.findMapBySql(sql.toString(), params);
        if(list!=null && list.size()>0){

            Map<String, String> map = new HashMap<>();

            String studentContacts[] = new String[list.size()];
            for(int i=0;i<list.size();i++){
                map.put(list.get(i).get("stuId").toString(), list.get(i).get("contact")!=null?list.get(i).get("contact").toString():"");
            }
            for(int j=0;j<list.size();j++){
                studentContacts[j] = map.get(studentIds[j]);
            }
            return studentContacts;
        }

        return null;
    }

    private List<TwoTeacherClassCourse> getTwoTeacherClassCourseByClassId(Integer classId){
        String sql = "select * from two_teacher_class_course where CLASS_ID = :classId ";
        Map<String, Object> param = Maps.newHashMap();
        param.put("classId", classId);
        return twoTeacherClassCourseDao.findBySql(sql, param);
    }

    @Override
    public List<TwoTeacherClassStudent> getTwoTeacherClassStudentsByTwoClassId(String twoClassId) {
        String sql = "select * from two_teacher_class_student where CLASS_TWO_ID = :twoClassId";
        Map<String, Object> param = Maps.newHashMap();
        param.put("twoClassId", twoClassId);
        return twoTeacherClassStudentDao.findBySql(sql, param);
    }

    @Override
    public List<Map<Object,Object>> getTwoTeacherCourseScheduleList(TwoTeacherCourseSearchVo courseSearch) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder(" select distinct ttcc.COURSE_ID courseId, ttcc.COURSE_STATUS courseStatus, ttcc.COURSE_DATE courseDate, ");
        sql.append(" ttcc.COURSE_TIME courseTime, ttcc.COURSE_END_TIME courseEndTime, ttct.`NAME` className, ");
        sql.append(" (select name from data_dict where id = ttc.`SUBJECT`) subject, (select name from data_dict where id = p.GRADE_ID) grade, ");
        if (StringUtil.isNotBlank(courseSearch.getStudentId())) {
            sql.append(" s.id studentId, s.name studentName, ");
        }
        sql.append("  ttct.TEACHER_ID teacherId, (select name from user where user_id = ttct.TEACHER_ID) teacherName ");
        sql.append(" from two_teacher_class_student ttcs  ");
        sql.append(" left join two_teacher_class_two ttct on ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID ");
        sql.append(" left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID ");
        sql.append(" left join two_teacher_class_course ttcc on ttc.CLASS_ID = ttcc.CLASS_ID ");
        sql.append(" left join product p on ttc.PRODUCE_ID = p.id ");
        if (StringUtil.isNotBlank(courseSearch.getStudentId())) {
            sql.append(" left join student s on ttcs.STUDENT_ID = s.ID ");
        }
        sql.append(" where 1=1 ");
        if (StringUtil.isNotBlank(courseSearch.getStudentId())) {
            sql.append(" and ttcs.STUDENT_ID = :studentId ");
            params.put("studentId", courseSearch.getStudentId());
        }
        if (StringUtil.isNotBlank(courseSearch.getTeacherId())) {
            sql.append(" and  ttct.TEACHER_ID = :teacherId ");
            params.put("teacherId", courseSearch.getTeacherId());
        }
        if (StringUtil.isNotBlank(courseSearch.getStartDate())) {
            sql.append("  and ttcc.COURSE_DATE >= :startDate ");
            params.put("startDate", courseSearch.getStartDate());
        }

        if (StringUtil.isNotBlank(courseSearch.getEndDate())) {
            sql.append("  and ttcc.COURSE_DATE <= :endDate ");
            params.put("endDate", courseSearch.getEndDate());
        }
        List<Map<Object,Object>> list = twoTeacherClassCourseDao.findMapBySql(sql.toString(), params);
        int crashInd = 0;
        for (Map<Object,Object> map : list) {
            String studentId = map.get("studentId") != null ? map.get("studentId").toString() : null;
            crashInd = courseConflictDao.findCourseConflictCount(map.get("courseId").toString(), studentId,
                    map.get("teacherId").toString());
            int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(map.get("courseId").toString(), ProductType.TWO_TEACHER);
            if (count > 0) {
                map.put("isWashed", "TRUE");
            } else {
                map.put("isWashed", "FALSE");
            }
            map.put("crashInd", Integer.toString(crashInd));
        }
        return list;
    }

    @Override
    public List<TwoTeacherClassTwoExcelVo> getTwoClassTwoToExcel(TwoTeacherClassTwoVo vo, DataPackage dataPackage) {
        StringBuffer sql = new StringBuffer();

        sql.append(" select ttct.CLASS_ID classId,ttct.CLASS_TWO_ID classTwoId,ttct.`NAME` classTwoName,d.`NAME` productVersionName,");
        sql.append(" dd.`NAME` productQuarterName,phaseD.`NAME` phaseName,classTypeD.`NAME` classTypeName,");
        sql.append(" ttct.PEOPLE_QUANTITY peopleQuantity,o.`name` campusName,d1.CLASS_ROOM classRoomName,");
        sql.append(" d2.`NAME` gradeName,d3.`NAME` subjectName,u1.`NAME` mainTeacherName,u2.`NAME` teacherName,ttc.START_DATE classDate,");
        sql.append(" ttc.CLASS_TIME classTime,ttc.EVERY_COURSE_CLASS_NUM courseClassNum,ttc.CLASS_TIME_LENGTH classTimeLength,");
        sql.append(" ttct.`STATUS` classStatus,ttc.UNIT_PRICE unitPrice,ttc.TOTAL_CLASS_HOURS totalClassHours,ttct.CREATE_TIME createTime");
        sql.append(" from two_teacher_class_two ttct");
        sql.append(" left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID");
        sql.append(" left join product p  on ttc.PRODUCE_ID = p.ID");
        sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID");
        sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID");
        sql.append(" left join data_dict phaseD on ttc.PHASE = phaseD.ID");
        sql.append(" left join data_dict classTypeD on p.CLASS_TYPE_ID =classTypeD.ID");
        sql.append(" left join organization o on ttct.BL_CAMPUS_ID = o.id");
        sql.append(" left join classroom_manage d1 on ttct.CLASS_ROOM_ID = d1.ID");
        sql.append(" left join data_dict d2 on p.GRADE_ID = d2.ID");
        sql.append(" left join data_dict d3 on ttc.`SUBJECT` = d3.ID");
        sql.append(" left join `user` u1 on ttc.TEACHER_ID = u1.USER_ID");
        sql.append(" left join `user` u2 on ttct.TEACHER_ID = u2.USER_ID  " );
        sql.append(" left join organization oo on o.parentID = oo.id ");
        sql.append(" where 1=1 ");


        if(StringUtils.isNotBlank(vo.getStartDate())){
            sql.append(" and ttc.START_DATE >='" + vo.getStartDate() + "'");
        }

        if(StringUtils.isNotBlank(vo.getEndDate())){
            sql.append(" and ttc.START_DATE <='"+vo.getEndDate()+"'");
        }

        if(StringUtil.isNotBlank(vo.getName())){
            sql.append(" and ttct.`NAME` like '%"+vo.getName()+"%'");
        }

        if(StringUtils.isNotBlank(vo.getBlCampusId())){
            sql.append(" and ttct.BL_CAMPUS_ID ='"+vo.getBlCampusId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getBrenchId())){
            sql.append(" and oo.id ='"+vo.getBrenchId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getClassTypeId())){
            sql.append(" and p.CLASS_TYPE_ID ='"+vo.getClassTypeId()+"'");
        }
        if(StringUtils.isNotBlank(vo.getGradeId())){
            sql.append(" and p.GRADE_ID ='"+vo.getGradeId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getSubject())){
            sql.append(" and ttc.`SUBJECT` ='"+vo.getSubject()+"'");
        }


        if(StringUtils.isNotBlank(vo.getTeacherId())){
            sql.append(" and ttct.TEACHER_ID ='"+vo.getTeacherId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getMainTeacherId())){
            if("-1".equals(vo.getMainTeacherId())){
                sql.append(" and ttc.TEACHER_ID is null ");
            }else {
                sql.append(" and ttc.TEACHER_ID='" + vo.getMainTeacherId() + "'");
            }
        }

        if(vo.getStatus()!=null){
            sql.append(" and ttct.`STATUS`='"+vo.getStatus().getValue()+"'");
        }

        if(StringUtil.isNotBlank(vo.getProductVersion())){
            sql.append(" and p.PRODUCT_VERSION_ID ='"+vo.getProductVersion()+"'");
        }
        if(StringUtil.isNotBlank(vo.getProductQuarter())){
            sql.append(" and p.PRODUCT_QUARTER_ID ='"+vo.getProductQuarter()+"'");
        }


        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        //权限查询所属组织架构下的所有校区信息
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append(" and ( o.orgLevel like '"+org.getOrgLevel()+"%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(")");
        }


        //退费人数
        //已报名人数

        //开课日期 如有排课开课日期，取双师辅班对应主班第一节课的日期；如无排课，取双师辅班对应主班开始上课日期
        //结束日期 双师辅班对应主班排课最后一节课的日期

        //上课时间：双师辅班对应主班上课时间（开始时间-结束时间）

        //金额（不含资料费）：双师辅班对应主班产品总额
        //总课时：双师辅班对应主班总课时数
        //已排课时：双师辅班对应主班已排课时数
        //已上课时：双师辅班已上课时数

        List<Map<Object,Object>> result =twoTeacherClassTwoDao.findMapOfPageBySql(sql.toString(), dataPackage, new HashMap<>());

        //退费人数
        Map<String, BigInteger> closeProductNum = null;
        //报名人数
        Map<String, BigInteger> peopleNum = null;
        //开课日期 和 结束日期
        Map<String, String> courseDate = null;
        Map<String, String> courseEndDate = null;

        //上课时间 开始时间 结束时间
        Map<String, String> startTime = null;
        Map<String, String> endTime = null;

        //已排课时 已上课时
        Map<String, BigInteger> scheduledHours = null;
        Map<String, BigDecimal> inHours = null;



        if(result!=null && !result.isEmpty()){
            StringBuffer classTwoId = new StringBuffer();//辅班的id
            for(Map<Object, Object> map: result){
                classTwoId.append("'"+map.get("classTwoId").toString()+"',");
            }
            String classTwoIds = classTwoId.substring(0, classTwoId.length()-1);

            StringBuffer queryCloseProductNum = new StringBuffer("select count(DISTINCT ttcs.STUDENT_ID) closeProduct,ttcs.CLASS_TWO_ID classTwoId from ");
            queryCloseProductNum.append(" two_teacher_class_student ttcs, contract_product cp ");
            queryCloseProductNum.append(" where ttcs.CONTRACT_PRODUCT_ID = cp.ID and cp.`STATUS` ='CLOSE_PRODUCT' and ttcs.CLASS_TWO_ID in (");
            queryCloseProductNum.append(classTwoIds);
            queryCloseProductNum.append(" )group by ttcs.CLASS_TWO_ID ");

            StringBuffer queryPeopleNum = new StringBuffer("select count(DISTINCT ttcs.STUDENT_ID) peopleNum,ttcs.CLASS_TWO_ID classTwoId from two_teacher_class_student ttcs ");
            queryPeopleNum.append("where ttcs.CLASS_TWO_ID in ( ");
            queryPeopleNum.append(classTwoIds);
            queryPeopleNum.append(" ) group by ttcs.CLASS_TWO_ID");

            //开课日期 结束日期
            StringBuffer queryCourseDate = new StringBuffer();
            queryCourseDate.append("select ttc.CLASS_TWO_ID classTwoId,min(ttcc.COURSE_DATE) courseDate,max(ttcc.COURSE_DATE) courseEndDate ");
            queryCourseDate.append(" from two_teacher_class_course ttcc ,two_teacher_class_two ttc where ttcc.CLASS_ID = ttc.CLASS_ID ");
            queryCourseDate.append(" and ttc.CLASS_TWO_ID in (");
            queryCourseDate.append(classTwoIds);
            queryCourseDate.append(" ) group by ttc.CLASS_TWO_ID");

            //上课时间 开始时间 结束时间
            StringBuffer queryClassTime = new StringBuffer();
            queryClassTime.append("select ttct.CLASS_TWO_ID classTwoId,ttc.CLASS_TIME classTime, ");
            queryClassTime.append("ttc.EVERY_COURSE_CLASS_NUM classNum,ttc.CLASS_TIME_LENGTH classLength ");
            queryClassTime.append("from two_teacher_class_two ttct,two_teacher_class ttc ");
            queryClassTime.append("where ttct.CLASS_ID = ttc.CLASS_ID and ttct.CLASS_TWO_ID in ( ");
            queryClassTime.append(classTwoIds);
            queryClassTime.append(") group by ttct.CLASS_TWO_ID ");

            //已排课时
            StringBuffer queryScheduled = new StringBuffer();
            queryScheduled.append(" SELECT count(a.courseNum) courseNum,a.classTwoId FROM ");
            queryScheduled.append("	( ");
            queryScheduled.append(" SELECT ");
            queryScheduled.append(" count(*) AS courseNum, ");
            queryScheduled.append(" CLASS_TWO_ID AS classTwoId ");
            queryScheduled.append(" FROM ");
            queryScheduled.append(" two_teacher_class_student_attendent ");
            queryScheduled.append(" WHERE ");
            queryScheduled.append(" CLASS_TWO_ID IN ( ");
            queryScheduled.append(classTwoIds);
            queryScheduled.append(" ) GROUP BY TWO_CLASS_COURSE_ID,CLASS_TWO_ID ");
            queryScheduled.append("	) a GROUP BY a.classTwoId ");

            //已上课时
            StringBuffer queryInHours = new StringBuffer();
            queryInHours.append(" select sum(case when a.courseNum=0 then 0 else 1 end) courseNum,a.classTwoId  ");
            queryInHours.append(" from (select CLASS_TWO_ID classTwoId, ");
            queryInHours.append(" sum(CASE WHEN (COURSE_STATUS='CHARGED' OR COURSE_STATUS='TEACHER_ATTENDANCE') THEN 1 ELSE 0 END) as courseNum ");
            queryInHours.append(" from two_teacher_class_student_attendent where CLASS_TWO_ID in ( ");
            queryInHours.append(classTwoIds);
            queryInHours.append(" ) GROUP BY TWO_CLASS_COURSE_ID ,CLASS_TWO_ID)a GROUP BY a.classTwoId ");


            List<Map<Object,Object>>  closeProductResult = twoTeacherClassStudentDao.findMapBySql(queryCloseProductNum.toString(), Maps.newHashMap());
            List<Map<Object,Object>>  peopleResult = twoTeacherClassStudentDao.findMapBySql(queryPeopleNum.toString(), Maps.newHashMap());

            List<Map<Object,Object>> courseDateResult = twoTeacherClassCourseDao.findMapBySql(queryCourseDate.toString(), Maps.newHashMap());
            List<Map<Object,Object>> classTimeResult = twoTeacherClassTwoDao.findMapBySql(queryClassTime.toString(), Maps.newHashMap());

            //已排课时 已上课时
            List<Map<Object, Object>> scheduledResult = twoTeacherClassStudentAttendentDao.findMapBySql(queryScheduled.toString(), Maps.newHashMap());
            List<Map<Object, Object>> inHoursResult = twoTeacherClassStudentAttendentDao.findMapBySql(queryInHours.toString(), Maps.newHashMap());
            if(scheduledResult!=null && !scheduledResult.isEmpty()){
                scheduledHours = new HashMap<>();
                for(Map<Object, Object> map:scheduledResult){
                    if(map.get("classTwoId")!=null){
                        scheduledHours.put(map.get("classTwoId").toString(), (BigInteger)map.get("courseNum"));
                    }

                }
            }
            if(inHoursResult!=null && !inHoursResult.isEmpty()){
                inHours = new HashMap<>();
                for(Map<Object, Object> map:inHoursResult){
                    if(map.get("classTwoId")!=null){
                        inHours.put(map.get("classTwoId").toString(), (BigDecimal)map.get("courseNum"));
                    }

                }
            }


            if(closeProductResult!=null && !closeProductResult.isEmpty()){
                closeProductNum = new HashMap<>();
                for(Map<Object, Object> map:closeProductResult){
                    if(map.get("classTwoId")!=null){
                        closeProductNum.put(map.get("classTwoId").toString(), (BigInteger)map.get("closeProduct"));
                    }
                }
            }
            if(peopleResult!=null && !peopleResult.isEmpty()){
                peopleNum = new HashMap<>();
                for(Map<Object, Object> map:peopleResult){
                    if(map.get("classTwoId")!=null){
                        peopleNum.put(map.get("classTwoId").toString(), (BigInteger)map.get("peopleNum"));
                    }
                }
            }
            if(courseDateResult!=null && !courseDateResult.isEmpty()){
                courseDate = new HashMap<>();
                courseEndDate = new HashMap<>();
                for(Map<Object, Object> map:courseDateResult){
                    if(map.get("classTwoId")!=null){
                        courseDate.put(map.get("classTwoId").toString(), ""+map.get("courseDate"));
                        courseEndDate.put(map.get("classTwoId").toString(), ""+map.get("courseEndDate"));
                    }
                }
            }
            if(classTimeResult!=null && !classTimeResult.isEmpty()){
                startTime = new HashMap<>();
                endTime = new HashMap<>();

                String start = null;
                String end = null;



                for(Map<Object, Object> map:classTimeResult){
                    start = map.get("classTime")+"";
                    String twoId = map.get("classTwoId").toString();
                    startTime.put(twoId, start);

                    if(map.get("classNum")!=null&&map.get("classLength")!=null&& map.get("classTime")!=null){
                        BigDecimal classNum=(BigDecimal)map.get("classNum");
                        Integer classTimeLength = (Integer)map.get("classLength");
                        Integer delta = classNum.multiply(new BigDecimal(classTimeLength)).intValue();
                        try {
                            end = DateTools.timeAddMinutes(map.get("classTime")+":00",delta);
                        } catch (ParseException e) {
                            end =map.get("classTime").toString();
                        }

                    }
                    endTime.put(twoId, end);
                }
            }

        }



        //开始组装结果
        List<TwoTeacherClassTwoExcelVo> list = new ArrayList<>();
        TwoTeacherClassTwoExcelVo excelVo = null;
        if(result!=null & !result.isEmpty()){
            String id = null;
            for(Map<Object, Object> map: result){
                id = map.get("classTwoId").toString();
                excelVo = new TwoTeacherClassTwoExcelVo();

                excelVo.setClassTwoName(map.get("classTwoName")!=null?map.get("classTwoName").toString():"");
                excelVo.setProductVersionName(map.get("productVersionName")!=null?map.get("productVersionName").toString():"");
                excelVo.setProductQuarterName(map.get("productQuarterName")!=null?map.get("productQuarterName").toString():"");
                excelVo.setPhaseName(map.get("phaseName")!=null?map.get("phaseName").toString():"");
                excelVo.setClassTypeName(map.get("classTypeName")!=null?map.get("classTypeName").toString():"");
                excelVo.setPeopleQuantity(map.get("peopleQuantity")!=null?(Integer)map.get("peopleQuantity"):0);

                if(peopleNum!=null){
                    if(peopleNum.get(id)!=null){
                        excelVo.setApplyNum(peopleNum.get(id));
                    }else{
                        excelVo.setApplyNum(new BigInteger("0"));
                    }
                }else{
                    excelVo.setApplyNum(new BigInteger("0"));
                }
                if(closeProductNum!=null){
                    if(closeProductNum.get(id)!=null){
                        excelVo.setCloseProductNum(closeProductNum.get(id));
                    }else{
                        excelVo.setCloseProductNum(new BigInteger("0"));
                    }
                }else{
                    excelVo.setCloseProductNum(new BigInteger("0"));
                }
                excelVo.setCampusName(map.get("campusName")!=null?map.get("campusName").toString():"");
                excelVo.setClassRoomName(map.get("classRoomName")!=null?map.get("classRoomName").toString():"");
                excelVo.setGradeName(map.get("gradeName")!=null?map.get("gradeName").toString():"");
                excelVo.setSubjectName(map.get("subjectName")!=null?map.get("subjectName").toString():"");
                excelVo.setMainTeacherName(map.get("mainTeacherName")!=null?map.get("mainTeacherName").toString():"其他");
                excelVo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"其他");


                if(courseDate!=null){
                    if(courseDate.get(id)!=null){
                        excelVo.setClassDate(courseDate.get(id));
                    }else{
                        excelVo.setClassDate(map.get("classDate")!=null?map.get("classDate").toString():"");
                    }
                }else{
                    excelVo.setClassDate(map.get("classDate")!=null?map.get("classDate").toString():"");
                }

                if(courseEndDate!=null){
                    if(courseEndDate.get(id)!=null){
                        excelVo.setClassEndDate(courseEndDate.get(id));
                    }else{
                        excelVo.setClassEndDate("");
                    }
                }else{
                    excelVo.setClassEndDate("");
                }

                if(startTime!=null && endTime!=null){
                    if(startTime.get(id)!=null && endTime.get(id)!=null){
                        excelVo.setClassTime(startTime.get(id)+"-"+endTime.get(id));
                    }else{
                        excelVo.setClassTime(map.get("classTime")!=null?map.get("classTime").toString():"");
                    }
                }else{
                    excelVo.setClassTime(map.get("classTime")!=null?map.get("classTime").toString():"");
                }

                excelVo.setCourseClassNum(map.get("courseClassNum")!=null?(BigDecimal)map.get("courseClassNum"):new BigDecimal("0"));

                excelVo.setClassTimeLength(map.get("classTimeLength")!=null?(Integer)map.get("classTimeLength"):0);

                excelVo.setUnitPrice(map.get("unitPrice")!=null?(BigDecimal)map.get("unitPrice"):new BigDecimal("0"));


                //总课时
                excelVo.setTotalClassHours(map.get("totalClassHours")!=null?(BigDecimal)map.get("totalClassHours"):new BigDecimal("0"));
                //金额（不含资料费）
                excelVo.setClassAmount(excelVo.getUnitPrice().multiply(excelVo.getTotalClassHours()));

                //双师课程状态
                excelVo.setClassStatus(map.get("classStatus")!=null?MiniClassStatus.valueOf(map.get("classStatus").toString()).getName():"");


                if(scheduledHours!=null){
                    if(scheduledHours.get(id)!=null){
                        BigInteger bigInteger = scheduledHours.get(id);
                        BigDecimal temp = new BigDecimal(bigInteger);
                        excelVo.setAlreadyClassHours(temp.multiply(excelVo.getCourseClassNum()));
                    }else{
                        excelVo.setAlreadyClassHours(new BigDecimal("0"));
                    }
                }else{
                    excelVo.setAlreadyClassHours(new BigDecimal("0"));
                }

                //已上课时
                if(inHours!=null){
                    if(inHours.get(id)!=null){
                        excelVo.setConsumeClassHours(inHours.get(id).multiply(excelVo.getCourseClassNum()));
                    }else{
                        excelVo.setConsumeClassHours(new BigDecimal("0"));
                    }
                }else{
                    excelVo.setConsumeClassHours(new BigDecimal("0"));
                }

                excelVo.setCreateTime(map.get("createTime")!=null?map.get("createTime").toString():"");

                list.add(excelVo);
            }
        }


        return list;
    }

    @Override
    public List<Map<Object, Object>> getTwoClassTwoToExcelSize(TwoTeacherClassTwoVo vo, DataPackage dataPackage) {
        StringBuffer sql = new StringBuffer();

        sql.append(" select * ");
        sql.append(" from two_teacher_class_two ttct");
        sql.append(" left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID");
        sql.append(" left join product p  on ttc.PRODUCE_ID = p.ID");
        sql.append(" left join organization o on ttct.BL_CAMPUS_ID = o.id");
        sql.append(" left join organization oo on o.parentID = oo.id ");
        sql.append(" where 1=1 ");

        if(StringUtils.isNotBlank(vo.getStartDate())){
            sql.append(" and ttc.START_DATE >='" + vo.getStartDate() + "'");
        }

        if(StringUtils.isNotBlank(vo.getEndDate())){
            sql.append(" and ttc.START_DATE <='"+vo.getEndDate()+"'");
        }

        if(StringUtil.isNotBlank(vo.getName())){
            sql.append(" and ttct.`NAME` like '%"+vo.getName()+"%'");
        }

        if(StringUtils.isNotBlank(vo.getBlCampusId())){
            sql.append(" and ttct.BL_CAMPUS_ID ='"+vo.getBlCampusId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getBrenchId())){
            sql.append(" and oo.id ='"+vo.getBrenchId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getClassTypeId())){
            sql.append(" and p.CLASS_TYPE_ID ='"+vo.getClassTypeId()+"'");
        }
        if(StringUtils.isNotBlank(vo.getGradeId())){
            sql.append(" and p.GRADE_ID ='"+vo.getGradeId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getSubject())){
            sql.append(" and ttc.`SUBJECT` ='"+vo.getSubject()+"'");
        }


        if(StringUtils.isNotBlank(vo.getTeacherId())){
            sql.append(" and ttct.TEACHER_ID ='"+vo.getTeacherId()+"'");
        }

        if(StringUtils.isNotBlank(vo.getMainTeacherId())){
            if("-1".equals(vo.getMainTeacherId())){
                sql.append(" and ttc.TEACHER_ID is null ");
            }else {
                sql.append(" and ttc.TEACHER_ID='" + vo.getMainTeacherId() + "'");
            }
        }

        if(vo.getStatus()!=null){
            sql.append(" and ttct.`STATUS`='"+vo.getStatus().getValue()+"'");
        }

        if(StringUtil.isNotBlank(vo.getProductVersion())){
            sql.append(" and p.PRODUCT_VERSION_ID ='"+vo.getProductVersion()+"'");
        }
        if(StringUtil.isNotBlank(vo.getProductQuarter())){
            sql.append(" and p.PRODUCT_QUARTER_ID ='"+vo.getProductQuarter()+"'");
        }

//        User user = userService.getCurrentLoginUser();
//        //当前登陆者的数据权限
//        String orgId = user.getOrganizationId();
//        if(StringUtil.isNotBlank(orgId)){
//        	Organization organization = organizationDao.findById(orgId);
//        	if(organization!=null){
//        		 sql.append(" and o.orgLevel like '"+organization.getOrgLevel()+"%'");
//        	}
//        }

        List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        //权限查询所属组织架构下的所有校区信息
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append(" and ( o.orgLevel like '"+org.getOrgLevel()+"%'");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(")");
        }


        List<Map<Object,Object>> result =twoTeacherClassTwoDao.findMapOfPageBySql(sql.toString(), dataPackage, new HashMap<>());

        return result;
    }

    @Override
    public TwoTeacherClassCourse getTwoTeacherCourseById(int twoTeacherCourseId) {
        TwoTeacherClassCourse course = twoTeacherClassCourseDao.findById(twoTeacherCourseId);
        return course;
    }

    @Override
    public void updatePicByCourseAndClassId(int twoTeacherCourseId, int twoTeacherClassTwoId, String fileName) {
        twoTeacherClassStudentAttendentDao.updatePicByCourseAndClassId(twoTeacherCourseId,twoTeacherClassTwoId,fileName);
    }

    /**
     *
     * @param teacherId
     * @param productVersion
     * @param productQuarter
     * @param gradeId
     * @param subjectId
     * @return
     */
    @Override
    public List<TwoTeacherClassTwoVo> getTwoTeacherClassTwoByTeacherAndYearAndQuarter(String teacherId, String productVersion, String productQuarter, String gradeId, String subjectId) {
        StringBuffer sql = new StringBuffer();
        sql.append("           SELECT                                                ");
        sql.append("           ttct.*                                                ");
        sql.append("                   FROM                                          ");
        sql.append("           two_teacher_class_two ttct,                           ");
        sql.append("           two_teacher_class ttc,                                ");
        sql.append("           product p,                                            ");
        sql.append("           data_dict productVersion,                             ");
        sql.append("           data_dict productQuarter                              ");
        sql.append("           WHERE                                                 ");
        sql.append("           ttct.CLASS_ID = ttc.CLASS_ID                          ");
        sql.append("           AND ttc.PRODUCE_ID = p.id                             ");
        sql.append("           AND p.PRODUCT_VERSION_ID = productVersion.ID          ");
        sql.append("           AND p.PRODUCT_QUARTER_ID = productQuarter.ID          ");
        sql.append("           AND productQuarter.`NAME` = :productQuarter                      ");
        sql.append("           AND productVersion.`NAME` = :productVersion                   ");
        sql.append("           AND ttct.TEACHER_ID = :teacherId              ");
        Map<String, Object> params = new HashMap<>(16);
        params.put("teacherId", teacherId);
        params.put("productVersion", productVersion);
        params.put("productQuarter", productQuarter);
        if (StringUtils.isNotBlank(gradeId)){
            sql.append("      AND p.GRADE_ID=:gradeId       ");
            params.put("gradeId", gradeId);
        }

        if (StringUtils.isNotBlank(subjectId)){
            sql.append("      AND ttc.`SUBJECT`=:subjectId  ");
            params.put("subjectId", subjectId);
        }

        List<TwoTeacherClassTwo> twoTeacher = twoTeacherClassTwoDao.findBySql(sql.toString(), params);
        List<TwoTeacherClassTwoVo> twoTeacherClassTwoVos = HibernateUtils.voListMapping(twoTeacher, TwoTeacherClassTwoVo.class);
        return twoTeacherClassTwoVos;
    }


    /**
     * 双师续班率
     * 按年级和科目分类双师辅班
     *
     * @param list
     * @return
     */
    @Override
    public Map<String, List<TwoTeacherClassTwoVo>> classifyTwoTeacherClassTwoForGradeAndSubject(List<TwoTeacherClassTwoVo> list) {
        Map<String, List<TwoTeacherClassTwoVo>> result = new HashMap<>(16);

        for (TwoTeacherClassTwoVo twoTeacherClassTwoVo : list){
            if (result.containsKey(twoTeacherClassTwoVo.getGradeId()+"_"+twoTeacherClassTwoVo.getSubject())){
                List<TwoTeacherClassTwoVo> sameGradeAndSubjectOldList = result.get(twoTeacherClassTwoVo.getGradeId() + "_" + twoTeacherClassTwoVo.getSubject());
                sameGradeAndSubjectOldList.add(twoTeacherClassTwoVo);
            }else {
                List<TwoTeacherClassTwoVo> sameGradeAndSubjectList = new ArrayList<>();
                sameGradeAndSubjectList.add(twoTeacherClassTwoVo);
                result.put(twoTeacherClassTwoVo.getGradeId()+"_"+twoTeacherClassTwoVo.getSubject(), sameGradeAndSubjectList);
            }
        }
        return result;
    }

    /**
     * @param teacherId
     * @param productVersion
     * @param productQuarter
     * @param map
     */
    @Override
    public Map<String, Integer> getStudentXuDu(String teacherId, String productVersion, String productQuarter, Map<String, List<TwoTeacherClassTwoVo>> map) {


        Map<String, List<Student>> studentInMap =new HashMap<>(16);

        Set<String> gradeAndSubjectStrings = map.keySet();

        Map<String, List<Student>> studentGradeAndSubjectAndQuarter = new HashMap<>(16);

        for (String gradeAndSubject: gradeAndSubjectStrings){
            String[] gradeAndSubjectArray = gradeAndSubject.split("_");
            if ("春季".equals(productQuarter)){
                String grade = gradeAndSubjectArray[0];
                String nextGrade = getNextGrade(grade);
                String subject = gradeAndSubjectArray[1];
                //春季到暑假属于续班的
                List<TwoTeacherClassTwoVo> springToSummerList = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, productVersion, "暑假", nextGrade, subject);

                //春季到秋季属于续班的
                List<TwoTeacherClassTwoVo> springToAutumnList = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, productVersion, "秋季", nextGrade, subject);


                List<Student> studentFromTwoTeacherClassTwoSummer = this.getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(springToSummerList, productQuarter, productVersion);
                studentGradeAndSubjectAndQuarter.put(grade+"_"+subject+"_暑假", studentFromTwoTeacherClassTwoSummer);

                List<Student> studentFromTwoTeacherClassTwoAutumn = this.getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(springToAutumnList, productQuarter, productVersion);
                studentGradeAndSubjectAndQuarter.put(grade+"_"+subject+"_秋季", studentFromTwoTeacherClassTwoAutumn);

                List<Student> list = this.getStudentFromTwoTeacherClassTwo(map.get(gradeAndSubject));
                studentInMap.put(gradeAndSubject+"_春季", list);

            }

            if ("暑假".equals(productQuarter)){
                String grade = gradeAndSubjectArray[0];
                String subject = gradeAndSubjectArray[1];
                //暑假到秋季属于续班的
                List<TwoTeacherClassTwoVo> summerToAutumnList = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, productVersion, "秋季", grade, subject);

                List<Student> studentFromTwoTeacherClassTwoAutumn = this.getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(summerToAutumnList, productQuarter, productVersion);
                studentGradeAndSubjectAndQuarter.put(grade+"_"+subject+"_秋季", studentFromTwoTeacherClassTwoAutumn);
                List<Student> list = this.getStudentFromTwoTeacherClassTwo(map.get(gradeAndSubject));
                studentInMap.put(gradeAndSubject+"_暑假", list);
            }

            if ("秋季".equals(productQuarter)){
                String grade = gradeAndSubjectArray[0];
                String subject = gradeAndSubjectArray[1];
                String nextProductVersion = getNextProductVersion(productVersion);
                //秋季到寒假属于续班的
                List<TwoTeacherClassTwoVo> autumnToWinterList = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, nextProductVersion, "寒假", grade, subject);

                List<Student> studentFromTwoTeacherClassTwoWinter = this.getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(autumnToWinterList, productQuarter, productVersion);
                studentGradeAndSubjectAndQuarter.put(grade+"_"+subject+"_寒假", studentFromTwoTeacherClassTwoWinter);

                //秋季到春季属于续班的
                List<TwoTeacherClassTwoVo> autumnToSpringList = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, nextProductVersion, "春季", grade, subject);
                List<Student> studentFromTwoTeacherClassTwoSpring = this.getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(autumnToSpringList, productQuarter, productVersion);
                studentGradeAndSubjectAndQuarter.put(grade+"_"+subject+"_春季", studentFromTwoTeacherClassTwoSpring);

                List<Student> list = this.getStudentFromTwoTeacherClassTwo(map.get(gradeAndSubject));
                studentInMap.put(gradeAndSubject+"_秋季", list);

            }

            if ("寒假".equals(productQuarter)){
                String grade = gradeAndSubjectArray[0];
                String subject = gradeAndSubjectArray[1];
                //寒假到春季属于续班的
                List<TwoTeacherClassTwoVo> winterToSpringList = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, productVersion, "春季", grade, subject);
                List<Student> studentFromTwoTeacherClassTwoSpring = this.getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(winterToSpringList, productQuarter, productVersion);
                studentGradeAndSubjectAndQuarter.put(grade+"_"+subject+"_春季", studentFromTwoTeacherClassTwoSpring);

                List<Student> list = this.getStudentFromTwoTeacherClassTwo(map.get(gradeAndSubject));
                studentInMap.put(gradeAndSubject+"_寒假", list);
            }
        }

        return this.xuduStudent(studentInMap, studentGradeAndSubjectAndQuarter);
    }

    /**
     * 下一年份
     * @param productVersion
     * @return
     */
    private String getNextProductVersion(String productVersion) {
        Integer year = Integer.valueOf(productVersion);
        Integer nextYear = year+1;
        return nextYear.toString();
    }

    /**
     * @param preMap
     * @param nextMap
     */
    @Override
    public Map<String, Integer> xuduStudent(Map<String, List<Student>> preMap, Map<String, List<Student>> nextMap) {
        Integer xuduStudentFirstSize = 0;
        Integer xuduStudentSecondSize = 0;
        for (Map.Entry<String, List<Student>> entry : preMap.entrySet()){
            String gradeAndSubjectAndQuarter = entry.getKey();
            String[] arrays = gradeAndSubjectAndQuarter.split("_");
            if ("春季".equals(arrays[2])){
                //春续暑
                List<Student> springToSummerStudentList = nextMap.get(arrays[0] + "_" + arrays[1]+"_暑假");
                if (springToSummerStudentList!=null){
                    List<Student> springStudent = entry.getValue();
                    List<String> springStudentIdList = new ArrayList<>();
                    for (Student s : springStudent){
                        springStudentIdList.add(s.getId());
                    }
                    List<String> springToSummerStudentIdList = new ArrayList<>();
                    for (Student s : springToSummerStudentList){
                        springToSummerStudentIdList.add(s.getId());
                    }
                    System.out.println("春季学生数量"+springStudentIdList.size());
                    //春续暑的学生
                    springStudentIdList.retainAll(springToSummerStudentIdList);
                    System.out.println("同一年级学科春续暑的学生size"+springStudentIdList.size());
                    xuduStudentFirstSize+=springStudentIdList.size();
                }
                System.out.println("春续暑的学生数量"+xuduStudentFirstSize);

                //春续秋
                List<Student> springToAutumnStudentList = nextMap.get(arrays[0] + "_" + arrays[1] + "_秋季");
                if (springToAutumnStudentList!=null){
                    List<Student> springStudent = entry.getValue();
                    List<String> springStudentIdList = new ArrayList<>();
                    for (Student s : springStudent){
                        springStudentIdList.add(s.getId());
                    }
                    List<String> springToAutumnStudentIdList = new ArrayList<>();
                    for (Student s : springToAutumnStudentList){
                        springToAutumnStudentIdList.add(s.getId());
                    }
                    springStudentIdList.retainAll(springToAutumnStudentIdList);
                    xuduStudentSecondSize+=springStudentIdList.size();
                }

                System.out.println("春续秋的学生数量"+xuduStudentSecondSize);
            }


            if ("暑假".equals(arrays[2])){
                //暑续秋
                List<Student> summerToAutumnStudentList = nextMap.get(arrays[0] + "_" + arrays[1]+"_秋季");
                if (summerToAutumnStudentList!=null){
                    List<Student> summerStudent = entry.getValue();
                    List<String> summerStudentIdList = new ArrayList<>();
                    for (Student s : summerStudent){
                        summerStudentIdList.add(s.getId());
                    }

                    List<String> summerToAutumnStudentIdList = new ArrayList<>();
                    for (Student s : summerToAutumnStudentList){
                        summerToAutumnStudentIdList.add(s.getId());
                    }
                    System.out.println("暑假学生数量"+summerStudentIdList.size());
                    //暑续秋的学生
                    summerStudentIdList.retainAll(summerToAutumnStudentIdList);
                    System.out.println("同一年级学科暑续秋的学生size"+summerStudentIdList.size());
                    xuduStudentFirstSize+=summerStudentIdList.size();
                }
                System.out.println("暑续秋的学生数量"+xuduStudentFirstSize);
            }

            if ("秋季".equals(arrays[2])){
                //秋续寒
                List<Student> autumnToWinterStudentList = nextMap.get(arrays[0]+"_"+arrays[1]+"_寒假");
                if (autumnToWinterStudentList!=null){
                    List<Student> autumnStudent = entry.getValue();
                    List<String> autumnStudentIdList = new ArrayList<>();
                    for (Student s : autumnStudent){
                        autumnStudentIdList.add(s.getId());
                    }

                    List<String> autumnToWinterStudentIdList = new ArrayList<>();
                    for (Student s:autumnToWinterStudentList){
                        autumnToWinterStudentIdList.add(s.getId());
                    }
                    System.out.println("秋季学生数量"+autumnStudentIdList.size());
                    //秋续寒的学生
                    autumnStudentIdList.retainAll(autumnToWinterStudentIdList);
                    System.out.println("同一年级学科秋续寒的学生size"+autumnStudentIdList.size());
                    xuduStudentFirstSize+=autumnStudentIdList.size();
                }
                System.out.println("秋续寒的学生数量"+xuduStudentFirstSize);


                //秋续春
                List<Student> autumnToSpringStudentList = nextMap.get(arrays[0] + "_" + arrays[1] + "_春季");
                if (autumnToSpringStudentList!=null){
                    List<Student> autumnStudent = entry.getValue();
                    List<String> autumnStudentIdList = new ArrayList<>();
                    for (Student s : autumnStudent){
                        autumnStudentIdList.add(s.getId());
                    }
                    List<String> autumnToSpringStudentIdList = new ArrayList<>();
                    for (Student s : autumnToSpringStudentList){
                        autumnToSpringStudentIdList.add(s.getId());
                    }
                    autumnStudentIdList.retainAll(autumnToSpringStudentIdList);
                    xuduStudentSecondSize+=autumnStudentIdList.size();
                }
                System.out.println("秋续春的学生数量"+xuduStudentSecondSize);

            }


            if ("寒假".equals(arrays[2])){
                //寒续春
                List<Student> winterToSpringStudentList = nextMap.get(arrays[0] + "_" + arrays[1]+"_春季");
                if (winterToSpringStudentList!=null){
                    List<Student> winterStudent = entry.getValue();
                    List<String> winterStudentIdList = new ArrayList<>();
                    for (Student s : winterStudent){
                        winterStudentIdList.add(s.getId());
                    }

                    List<String> winterToSpringStudentIdList = new ArrayList<>();
                    for (Student s : winterToSpringStudentList){
                        winterToSpringStudentIdList.add(s.getId());
                    }
                    System.out.println("寒假学生数量"+winterStudentIdList.size());
                    //暑续秋的学生
                    winterStudentIdList.retainAll(winterToSpringStudentIdList);
                    System.out.println("同一年级学科暑续秋的学生size"+winterStudentIdList.size());
                    xuduStudentFirstSize+=winterStudentIdList.size();
                }
                System.out.println("寒续春的学生数量"+xuduStudentFirstSize);
            }
        }

        Map<String, Integer> result = new HashMap<>(16);
        result.put("first", xuduStudentFirstSize);
        result.put("second", xuduStudentSecondSize);
        return result;
    }



    private String getNextGrade(String gradeId) {
        DataDict grade = dataDictDao.findById(gradeId);
        Integer gradeOrder = grade.getDictOrder()+1;
        List<DataDict> gradeList = dataDictDao.findBySql("SELECT * FROM data_dict WHERE CATEGORY='STUDENT_GRADE' AND DICT_ORDER=" + gradeOrder, new HashMap<>());
        String nextGradeId = gradeList.get(0).getId();
        return nextGradeId;
    }



    /**
     * @param list
     * @return
     */
    @Override
    public List<Student> getStudentFromTwoTeacherClassTwo(List<TwoTeacherClassTwoVo> list) {
        List<Student> studentList = new ArrayList<>();
//        if (list.size()==0){
//
//        }
        for (TwoTeacherClassTwoVo teacherClassTwoVo : list){
            List<TwoTeacherClassStudent> twoTeacherClassStudentsByTwoClassId = this.getTwoTeacherClassStudentsByTwoClassId(""+teacherClassTwoVo.getId()+"");
            for (TwoTeacherClassStudent teacherClassStudent :twoTeacherClassStudentsByTwoClassId){
                studentList.add(teacherClassStudent.getStudent());
            }
        }


        return studentList;
    }

    /**
     * 统一规定时间: 以下一季的第二讲上课时间为取数节点
     *
     * @param list
     * @param productQuarter
     * @param productVersion
     * @return
     */
    @Override
    public List<Student> getStudentFromTwoTeacherClassTwoAndProductVersionAndProductQuarter(List<TwoTeacherClassTwoVo> list, String productQuarter, String productVersion) {
        List<Student> studentList = new ArrayList<>();
        if ("春季".equals(productQuarter)){
            String utilTime = productVersion+"-07-18 00:00:00";
            studentList = this.getAbleXuduStudentByTimeAndClassId(utilTime, list);
        }
        if ("暑假".equals(productQuarter)){
            String utilTime = productVersion+"-09-24 00:00:00";
            studentList = this.getAbleXuduStudentByTimeAndClassId(utilTime, list);
        }
        if ("秋季".equals(productQuarter)){
            Integer nextYear = Integer.valueOf(productVersion)+1;
            String utilTime = nextYear+"-02-08 00:00:00";
            studentList = this.getAbleXuduStudentByTimeAndClassId(utilTime, list);
        }
        if ("寒假".equals(productQuarter)){
            String utilTime = productVersion+"-03-12 00:00:00";
            studentList = this.getAbleXuduStudentByTimeAndClassId(utilTime, list);
        }

        return studentList;
    }

    /**
     * @param utilTime
     * @param list
     * @return
     */
    @Override
    public List<Student> getAbleXuduStudentByTimeAndClassId(String utilTime, List<TwoTeacherClassTwoVo> list) {
        List<Student> result = new ArrayList<>();
        if (list == null||list.size()==0){
            return result;
        }

        StringBuffer sql = new StringBuffer();

        sql.append("                 SELECT                                        ");
        sql.append("                 ttcs.*                                        ");
        sql.append("                         FROM                                 ");
        sql.append("                 two_teacher_class_student ttcs,               ");
        sql.append("                 contract_product cp                           ");
        sql.append("                 WHERE                                         ");
        sql.append("                 ttcs.CONTRACT_PRODUCT_ID = cp.ID              ");
        sql.append("                 AND cp.CREATE_TIME <= :utilTime                      ");
        sql.append("                 AND ttcs.CLASS_TWO_ID in (:idList)                  ");
        List<Integer> idList = new ArrayList<>();
        for (TwoTeacherClassTwoVo vo : list){
            idList.add(vo.getId());
        }
        Map<String, Object> params = new HashMap<>(16);
        params.put("utilTime", utilTime);
        params.put("idList", idList);
        List<TwoTeacherClassStudent> twoTeacherClassStudentList = twoTeacherClassStudentDao.findBySql(sql.toString(), params);


        for (TwoTeacherClassStudent teacherClassStudent : twoTeacherClassStudentList){
            result.add(teacherClassStudent.getStudent());
        }

        return result;
    }

    /**
     * 双师已经考勤最多的人数
     *
     * @param list
     * @return
     */
    @Override
    public int getMaxStudentNumFromTwoTeacherClassTwo(List<TwoTeacherClassTwoVo> list) {
        int result = 0;
        for (TwoTeacherClassTwoVo teacherClassTwoVo : list){
            List<TwoTeacherClassStudent> twoTeacherClassStudentsByTwoClassId = this.getTwoTeacherClassStudentsByTwoClassId(""+teacherClassTwoVo.getId()+"");
            int chargeNum = getChargeStudentNumByTwoClassId(teacherClassTwoVo.getId());
            result = Math.max(twoTeacherClassStudentsByTwoClassId.size(), chargeNum)+result;
        }
        return result;
    }

    private int getChargeStudentNumByTwoClassId(int id) {
        String sql = " SELECT count(1) FROM  (SELECT STUDENT_ID FROM two_teacher_class_student_attendent WHERE CLASS_TWO_ID="+id+" AND CHARGE_STATUS='CHARGED'  GROUP BY STUDENT_ID ) a ";
        int num = twoTeacherClassStudentAttendentDao.findCountSql(sql, new HashMap<>());
        return num;
    }

    /**
     * @param teacherId
     * @param productVersion
     * @param productQuarter
     * @param twoTeacherClassTwoExcelVo
     */
    @Override
    public Map<String, String> getXuduStudent(String teacherId, String productVersion, String productQuarter, TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo) {
        //根据季度和年份和老师找到所有双师辅班
        List<TwoTeacherClassTwoVo> twoTeacherClassTwoByTeacherAndYearAndQuarter = this.getTwoTeacherClassTwoByTeacherAndYearAndQuarter(teacherId, productVersion, productQuarter, twoTeacherClassTwoExcelVo.getGradeId(), twoTeacherClassTwoExcelVo.getSubjectId());
        //根据年级和科目分类双师辅班
        Map<String, List<TwoTeacherClassTwoVo>> classifyStudent = this.classifyTwoTeacherClassTwoForGradeAndSubject(twoTeacherClassTwoByTeacherAndYearAndQuarter);

        Map<String, Integer> map = this.getStudentXuDu(teacherId, productVersion, productQuarter, classifyStudent);

        //总共报读人数 辅班的人数中取在读人数和已结算人数最多者
        int size = this.getMaxStudentNumFromTwoTeacherClassTwo(twoTeacherClassTwoByTeacherAndYearAndQuarter);

        Map<String, String> resultMap = new HashMap<>();


        if ("春季".equals(productQuarter)){
            BigDecimal addResult = (BigDecimal.valueOf(map.get("first")).add(BigDecimal.valueOf(map.get("second")))).multiply(BigDecimal.valueOf(100));

            if (size!=0){
                BigDecimal springToSummer = BigDecimal.valueOf(map.get("first")).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("first", springToSummer+"%");
                BigDecimal springToAutumn = BigDecimal.valueOf(map.get("second")).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("second", springToAutumn+"%");
                size = size*2;
                BigDecimal spring = addResult.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("third", spring+"%");
                System.out.println("春季续报率"+spring);

                return resultMap;
            }else {
                System.out.println("春季续报率 无");
                return resultMap;
            }
        }

        if ("秋季".equals(productQuarter)){
            BigDecimal addResult = (BigDecimal.valueOf(map.get("first")).add(BigDecimal.valueOf(map.get("second")))).multiply(BigDecimal.valueOf(100));

            if (size!=0){

                BigDecimal autumnToWinter = BigDecimal.valueOf(map.get("first")).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("first", autumnToWinter+"%");

                BigDecimal autumnToSpring = BigDecimal.valueOf(map.get("second")).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("second", autumnToSpring+"%");

                size = size*2;
                BigDecimal autumn = addResult.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("third", autumn+"%");
                System.out.println("秋季续报率"+autumn);
                return resultMap;
            }else {
                System.out.println("秋季续报率 无");
                return resultMap;
            }
        }

        if ("暑假".equals(productQuarter)){
            if (size!=0){
                BigDecimal summer = BigDecimal.valueOf(map.get("first")).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                System.out.println("暑假续报率 "+summer);
                resultMap.put("third", summer+"%");
                return resultMap;
            }else {
                System.out.println("暑假续报率 无");
                return resultMap;
            }

        }

        if ("寒假".equals(productQuarter)){
            if (size!=0){
                BigDecimal winter = BigDecimal.valueOf(map.get("first")).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_UP);
                System.out.println("寒假续报率"+winter);
                resultMap.put("third", winter+"%");
                return resultMap;
            }else {
                System.out.println("寒假续报率 无");
                return resultMap;
            }

        }

        return resultMap;

    }

    /**
     * @param dataPackage
     * @param twoTeacherClassTwoExcelVo
     * @return
     */
    @Override
    public DataPackage getTwoTeacherClassTwoXuDuRate(DataPackage dataPackage, TwoTeacherClassTwoExcelVo twoTeacherClassTwoExcelVo) {
        Map<String, Object> params = new HashMap<>();
        String quarterName = twoTeacherClassTwoExcelVo.getProductQuarterName();
        String productVersionName = twoTeacherClassTwoExcelVo.getProductVersionName();
        params.put("quarterName", quarterName);
        params.put("productVersionName", productVersionName);


        StringBuilder sql = new StringBuilder();

        sql.append("  SELECT                                                  ");
        sql.append("  '"+productVersionName+"' productVersionName, '"+quarterName+"' quarterName,                    ");
        sql.append("  ttct.teacher_id teacherId,                                         ");
        sql.append("  teacher. NAME teacherName,                                         ");
        sql.append("  branch. NAME branchName,                                         ");
        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getGradeId())){
            sql.append(" (SELECT NAME FROM data_dict WHERE id='"+twoTeacherClassTwoExcelVo.getGradeId()+"')   grade, ");
        }
        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getSubjectId())){
            sql.append(" (SELECT NAME FROM data_dict WHERE id='"+twoTeacherClassTwoExcelVo.getSubjectId()+"')   subject, ");
        }
        sql.append("  blCampus. NAME blCampusName                                         ");
        sql.append("  FROM                                         ");
        sql.append("  two_teacher_class_two ttct,                                         ");
        sql.append("  organization blCampus,                                         ");
        sql.append("  organization branch,                                         ");
        sql.append("  `user` teacher,                                         ");
        sql.append("          two_teacher_class ttc,                                         ");
        sql.append("          product p,                                         ");
        sql.append("          data_dict productVersion,                                         ");
        sql.append("          data_dict productQuarter                                         ");
        sql.append("          WHERE                                         ");
        sql.append("  ttct.bl_campus_id = blCampus.id                                         ");
        sql.append("  AND blCampus.parentID = branch.id                                         ");
        sql.append("  AND ttct.teacher_id = teacher.user_id                                         ");
        sql.append("  AND ttct.teacher_id IS NOT NULL                                         ");
        sql.append("  AND ttc.CLASS_ID = ttct.CLASS_ID                                         ");
        sql.append("  AND ttc.PRODUCE_ID = p.ID                                         ");
        sql.append("  AND p.PRODUCT_VERSION_ID = productVersion.ID                                         ");
        sql.append("  AND p.PRODUCT_QUARTER_ID = productQuarter.ID                                         ");
        sql.append("  AND productQuarter.`NAME` = :quarterName                                         ");
        sql.append("  AND productVersion.`NAME` = :productVersionName                                         ");
        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getBlBrenchId())){
            params.put("blBrenchId", twoTeacherClassTwoExcelVo.getBlBrenchId());
            sql.append(" AND branch.id=:blBrenchId               ");
        }

        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getBlCampusId())){
            params.put("blCampusId", twoTeacherClassTwoExcelVo.getBlCampusId());
            sql.append(" AND ttct.bl_campus_id = :blCampusId            ");
        }

        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getGradeId())){
            params.put("gradeId", twoTeacherClassTwoExcelVo.getGradeId());
            sql.append(" AND p.GRADE_ID = :gradeId            ");
        }
        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getSubjectId())){
            params.put("subjectId", twoTeacherClassTwoExcelVo.getSubjectId());
            sql.append(" AND ttc.SUBJECT = :subjectId            ");
        }
        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getClassTypeId())){
            params.put("classTypeId", twoTeacherClassTwoExcelVo.getClassTypeId());
            sql.append(" AND p.CLASS_TYPE_ID = :classTypeId      ");
        }
        if (StringUtil.isNotBlank(twoTeacherClassTwoExcelVo.getTwoTeacherId())){
            params.put("twoTeacherId", twoTeacherClassTwoExcelVo.getTwoTeacherId());
            sql.append(" AND ttct.teacher_id = :twoTeacherId                   ");
        }
        sql.append(getOrganizationSql("ttct.BL_CAMPUS_ID"));


        sql.append("  GROUP BY                                         ");
        sql.append("  ttct.teacher_id                                          ");
        if (StringUtils.isNotBlank(dataPackage.getSord())
                && StringUtils.isNotBlank(dataPackage.getSidx())) {
            sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());

        }
        List<Map<Object, Object>> list = twoTeacherClassTwoDao.findMapOfPageBySql(sql.toString(), dataPackage, params);

        for (Map<Object, Object> map : list){
            Map<String, String> result = this.getXuduStudent(map.get("teacherId").toString(), productVersionName, quarterName, twoTeacherClassTwoExcelVo);
            if ("春季".equals(quarterName)){
                map.put("first", result.get("first"));
                map.put("second", result.get("second"));
                map.put("third", result.get("third"));
            }
            if ("暑假".equals(quarterName)){
                map.put("third", result.get("third"));
            }
            if ("秋季".equals(quarterName)){
                map.put("first", result.get("first"));
                map.put("second", result.get("second"));
                map.put("third", result.get("third"));
            }
            if ("寒假".equals(quarterName)){
                map.put("third", result.get("third"));
            }

        }

        dataPackage.setRowCount(twoTeacherClassTwoDao.findCountSql("select count(*) from ( "+ sql.toString()+" ) countall ", params, 300));
        dataPackage.setDatas(list);
        return dataPackage;
    }


}
