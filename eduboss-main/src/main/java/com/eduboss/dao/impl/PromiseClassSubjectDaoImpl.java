package com.eduboss.dao.impl;

import com.eduboss.dao.PromiseClassSubjectDao;
import com.eduboss.domain.PromiseClassSubject;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.PromiseChargeSearchDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("PromiseClassSubjectDao")
public class PromiseClassSubjectDaoImpl extends GenericDaoImpl<PromiseClassSubject, String> implements PromiseClassSubjectDao{

    @Override
    public DataPackage getPromiseSubjectList(DataPackage dp, Map<String, Object> paramMap) {
        String hql=" from PromiseClassSubject where promiseStudent.id = :promiseStudentId";
        return this.findPageByHQL(hql,dp,true,paramMap);
    }

    @Override
    public void updateNewPromiseStudentId(String newId, String oldId) {
        StringBuilder hql = new StringBuilder();
        Map<String,Object> param=new HashMap();
        hql.append("update PromiseClassSubject set promiseStudent.id=:newId  where promiseStudent.id=:oldId");
        param.put("newId",newId);
        param.put("oldId",oldId);
        this.excuteHql(hql.toString(),param);
    }

    @Override
    public List<PromiseClassSubject> getPromiseSubjectList(Map<String, Object> paramMap) {
        String hql=" from PromiseClassSubject where promiseStudent.id = :promiseStudentId";
        return this.findAllByHQL(hql,paramMap);
    }

    @Override
    public List getPromiseSubjectDetailList(String promiseStudentId) {
        StringBuilder sql = new StringBuilder();
         sql.append(" SELECT pcs.quarter_id quarterId,")
            .append("     SUM(IFNULL(CASE WHEN pcs.mini_class_id IS NOT NULL THEN pcs.course_hours  ELSE 0 END,")
            .append("             0)) hasMiniClassCount,")
            .append("     SUM(IFNULL(CASE WHEN pcs.mini_class_id IS NULL THEN pcs.course_hours ELSE 0  END,")
            .append("             0)) hasNotMiniClassCount,")
            .append("     SUM(IFNULL(pcs.course_hours, 0)) allHours,")
            .append("     SUM(IFNULL((SELECT  SUM(b.COURSE_HOURS)")
            .append("                 FROM mini_class_student_attendent a, mini_class_course b")
            .append("                 WHERE a.STUDENT_ID = pstu.STUDENT_ID")
            .append(" 					AND a.MINI_CLASS_COURSE_ID = b.MINI_CLASS_COURSE_ID")
            .append(" 					AND b.MINI_CLASS_ID = pcs.MINI_CLASS_ID")
            .append(" 					AND a.CHARGE_STATUS = 'CHARGED'),0)) consumeHours")
            .append(" FROM")
            .append("     promise_class_student pstu")
            .append(" 	LEFT JOIN promise_class_subject pcs ON pstu.ID = pcs.promise_student_id")
            .append(" WHERE 1=1 ")
            .append(" and pcs.promise_student_id = '"+promiseStudentId+"'");

         sql.append(" GROUP BY pcs.QUARTER_ID");
        return this.findMapBySql(sql.toString(),new HashMap<String, Object>());
    }

    @Override
    public Map getEcsContractInfo(String contractId) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> param= new HashMap<>();
             sql.append(" select ")
                .append(" sum(PLAN_AMOUNT) vipAmount,")
                .append(" sum(PROMOTION_AMOUNT) vipProAmount,")
                .append(" sum(REAL_AMOUNT) vipRealPayAmount,")
                .append("sum(consume_Amount) vipConsumeAmount")
                .append("  from contract_product")
                .append(" where contract_id =:contractId  and type='OTHERS'");

        param.put("contractId",contractId);
        List<Map<Object,Object>> list= this.findMapBySql(sql.toString(),param);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List getEcsContractChargeInfoAfter2018(String contractProductId, BigDecimal price) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> param= new HashMap<>();

        param.put("price", price);
        sql.append("           SELECT                                                                                                ");
        sql.append("           p.PRODUCT_QUARTER_ID seasonId,                                                                          ");
        sql.append("           SUM(CASE WHEN acr.PAY_TYPE='PROMOTION' THEN acr.QUANTITY*:price ELSE 0 END ) promotionAmount,         ");
        sql.append("                   SUM(CASE WHEN acr.PAY_TYPE='REAL' THEN acr.QUANTITY*:price ELSE 0 END ) realAmount,           ");
        sql.append("                   SUM(acr.QUANTITY*:price) totalAmount,                                                         ");
        sql.append("                   SUM(CASE WHEN acr.PAY_TYPE='PROMOTION' THEN acr.QUANTITY ELSE 0 END ) promotionQuantity,      ");
        sql.append("                   SUM(CASE WHEN acr.PAY_TYPE='REAL' THEN acr.QUANTITY ELSE 0 END ) realQuantity,                ");
        sql.append("                   SUM(acr.QUANTITY) totalQuantity                                                               ");
        sql.append("           FROM account_charge_records acr                                                                       ");
        sql.append("           LEFT JOIN mini_class mc ON mc.MINI_CLASS_ID = acr.MINI_CLASS_ID                                       ");
        sql.append("           LEFT JOIN product p ON mc.PRODUCE_ID=p.ID                                                             ");
        sql.append("           WHERE acr.CONTRACT_PRODUCT_ID = :contractProductId                                                    ");
        sql.append("          AND acr.CHARGE_TYPE='NORMAL' AND acr.CHARGE_PAY_TYPE='CHARGE'                                          ");
        sql.append("           AND acr.IS_WASHED='FALSE' AND acr.AMOUNT=0                                                            ");
        sql.append("           GROUP BY p.PRODUCT_QUARTER_ID order by transaction_time                                               ");
        param.put("contractProductId",contractProductId);

        return this.findMapBySql(sql.toString(),param);
    }

    @Override
    public List getEcsContractChargeInfo(String contractProductId) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> param= new HashMap<>();
        sql.append(" select ")
                .append(" p.PRODUCT_QUARTER_ID seasonId,")
                .append(" SUM(CASE WHEN acr.PAY_TYPE='PROMOTION' THEN acr.AMOUNT ELSE 0 END ) promotionAmount,")
                .append(" SUM(CASE WHEN acr.PAY_TYPE='REAL' THEN acr.AMOUNT ELSE 0 END ) realAmount,")
                .append(" SUM(acr.AMOUNT) totalAmount,")
                .append(" SUM(CASE WHEN acr.PAY_TYPE='PROMOTION' THEN acr.QUANTITY ELSE 0 END ) promotionQuantity,")
                .append(" SUM(CASE WHEN acr.PAY_TYPE='REAL' THEN acr.QUANTITY ELSE 0 END ) realQuantity,")
                .append(" SUM(acr.QUANTITY) totalQuantity")
                .append(" from account_charge_records acr ")
                .append(" left join mini_class mc on mc.MINI_CLASS_ID = acr.MINI_CLASS_ID")
                .append(" left join product p on mc.PRODUCE_ID= p.ID")
                .append(" where acr.contract_product_id = :contractProductId ")
                .append(" and acr.CHARGE_TYPE='NORMAL' and acr.CHARGE_PAY_TYPE='CHARGE' AND acr.IS_WASHED ='FALSE'")
                .append(" group by p.PRODUCT_QUARTER_ID order by transaction_time");

        param.put("contractProductId",contractProductId);
        return this.findMapBySql(sql.toString(),param);
    }

    @Override
    public List<PromiseClassSubject> findPromiseSubjectByClassIdAndStudentId(String miniClassId, String studentId) {
        Map<String,Object> param=new HashMap();
        String hql = "from PromiseClassSubject where miniClass.miniClassId=:miniClassId and promiseStudent.student.id=:studentId";
        param.put("miniClassId",miniClassId);
        param.put("studentId",studentId);
        return this.findAllByHQL(hql,param);
    }

    @Override
    public DataPackage getEcsContractChargeList(String contractProductId,PromiseChargeSearchDto dto,DataPackage dataPackage ) {
        Map<String,Object> param=new HashMap();
        StringBuilder sql = new StringBuilder();
             sql.append(" select ")
                .append(" mc.`NAME` as promiseName,")
                .append(" substr(acr.TRANSACTION_TIME,1,10) as courseDate,")
                .append(" substr(acr.TRANSACTION_TIME,11,6) as courseTime,")
                .append(" sum(acr.QUANTITY) courseHours,")
                .append(" u.name as teacherName,")
                .append(" u2.name as studyManagerName,")
                .append(" mcsa.ATTENDENT_STATUS as attendentStatus,")
                .append(" acr.PAY_TIME as payTime,")
                .append(" sum(case when acr.PAY_TYPE='REAL' then acr.AMOUNT else 0 end) realAmount,")
                .append(" sum(case when acr.PAY_TYPE='REAL' then acr.QUANTITY else 0 end) realHours,")
                .append(" sum(case when acr.PAY_TYPE='PROMOTION' then acr.AMOUNT else 0 end) proAmount,")
                .append(" sum(case when acr.PAY_TYPE='PROMOTION' then acr.QUANTITY else 0 end) proHours,")
                .append(" cp.price ")
                .append(" from account_charge_records acr ")
                .append(" left join mini_class mc on mc.MINI_CLASS_ID = acr.MINI_CLASS_ID")
                .append(" left join user u on u.user_id = mc.teacher_id")
                .append(" left join user u2 on u2.user_id = mc.STUDY_MANEGER_ID")
                 .append(" left join contract_product cp on cp.id=acr.contract_product_id")
                .append(" left join mini_class_student_attendent mcsa on mcsa.student_id=acr.student_id  and mcsa.mini_class_course_id =  acr.mini_class_course_id")
                 .append(" left join product p on p.id = mc.PRODUCE_ID ")
                 .append(" where acr.CONTRACT_PRODUCT_ID=:contractProductId and acr.CHARGE_PAY_TYPE='CHARGE' and acr.IS_WASHED='FALSE' and acr.charge_type ='NORMAL'")
               ;
        param.put("contractProductId",contractProductId);


        if(StringUtils.isNotBlank(dto.getAttendanceStatus())){
            sql.append(" and mcsa.ATTENDENT_STATUS=:AttendanceStatus");
            param.put("AttendanceStatus",dto.getAttendanceStatus());
        }

        if(StringUtils.isNotBlank(dto.getStartDate())){
            sql.append(" and acr.TRANSACTION_TIME>=:startDate");
            param.put("startDate",dto.getStartDate());
        }

        if(StringUtils.isNotBlank(dto.getEndDate())){
            sql.append(" and acr.TRANSACTION_TIME<=:endDate");
            param.put("endDate",dto.getEndDate());
        }

        if(StringUtils.isNotBlank(dto.getMiniClassName())){
            sql.append(" and mc.name like :miniClassName");
            param.put("miniClassName","%"+dto.getMiniClassName()+"%");
        }

        if(StringUtils.isNotBlank(dto.getSeason())){
            sql.append(" and p.PRODUCT_QUARTER_ID=:season");
            param.put("season",dto.getSeason());
        }
        sql.append(" group by acr.TRANSACTION_ID");
        return this.findMapPageBySQL(sql.toString(),dataPackage,true,param);
    }


}
