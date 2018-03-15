package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.service.TwoTeacherClassService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.ProductType;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.ProductChooseViewService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

/**
 * Created by xuwen on 2014/12/29.
 */
@Service("ProductChooseViewService")
public class ProductChooseViewServiceImpl implements ProductChooseViewService {

    @Autowired
    private ProductChooseViewDao productChooseViewDao;

    @Autowired
    private MiniClassStudentDao miniClassStudentDao;

    @Autowired
    private MiniClassCourseDao miniClassCourseDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PromotionDao promotionDao;
    
    @Autowired
    private LectureClassStudentDao lectureClassStudentDao ;

    @Autowired
    private ProductPackageDao productPackageDao;

    @Autowired
    private TwoTeacherClassStudentDao twoTeacherClassStudentDao;

    @Autowired
    private TwoTeacherClassStudentAttendentDao twoTeacherClassStudentAttendentDao;

    @Autowired
    private TwoTeacherClassService twoTeacherClassService;

    /**
    @Override
    public DataPackage findPage(DataPackage dp, ProductChooseVo productChooseVo) {
        String currentDate = DateTools.getCurrentDate();
        List<Criterion> criterions = new ArrayList<Criterion>();
        if(productChooseVo.getExpressions() != null){
            for(String expression : productChooseVo.getExpressions()){
                criterions.add(productChooseViewDao.buildCriterion(expression));
            }
        }
        // 产品类别
        if(productChooseVo.getProduct() != null && productChooseVo.getProduct().getCategory() != null){
            criterions.add(Restrictions.eq("product.category",productChooseVo.getProduct().getCategory()));
        }
        // 排除已禁用的产品
        criterions.add(Restrictions.ne("product.proStatus","1"));
        // 排除不在有效期内的产品
        criterions.add(Restrictions.ge("product.validEndDate", currentDate));
        criterions.add(Restrictions.le("product.validStartDate", currentDate));
        // 本公司的产品和本校区的小班
        criterions.add(Restrictions.eq("product.organization.id", userService.getBelongBrench().getId()));
        // 产品组织架构向上兼容
//        criterions.add(Restrictions.sqlRestriction("(select orgLevel from organization where id = '"+userService.getCurrentLoginUser().getOrganizationId()+"') like concat((select orgLevel from organization where id = (select organization_id from product where id = product_id)),'%')"));
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
    */


    /**
     * 班级名称搜索
     * @param expressionList
     * @param index
     * @return
     */
    private LogicalExpression classNameSearch(List<String> expressionList, int index){
        if (index == 1){
            return Restrictions.or(productChooseViewDao.buildCriterion(expressionList.get(1)), productChooseViewDao.buildCriterion(expressionList.get(0)));
        }else {
            return Restrictions.or(classNameSearch(expressionList,index-1), productChooseViewDao.buildCriterion(expressionList.get(index)));
        }
    }


    @Override
    public DataPackage findPage(DataPackage dp, ProductChooseVo productChooseVo, String isEcsProductCategory) {
        String currentDate = DateTools.getCurrentDate();
        List<Criterion> criterions = new ArrayList<Criterion>();
        List<String> expressionList = new ArrayList<>();
        if(productChooseVo.getExpressions() != null){
            for(String expression : productChooseVo.getExpressions()){
                System.out.println(expression);
                if (expression.contains("LIKE_miniClass.name")){
                    expressionList.add(expression);
                }else if (expression.contains("LIKE_twoTeacherClassTwo.name")){
                    expressionList.add(expression);
                }else if (expression.contains("LIKE_lectureClass.lectureName")){
                    expressionList.add(expression);
                }else {
                    Criterion criterion = productChooseViewDao.buildCriterion(expression);
                    criterions.add(criterion);
                }
            }
            if (expressionList.size()==1){
                criterions.add(productChooseViewDao.buildCriterion(expressionList.get(0)));
            }else if (expressionList.size()>1){
                criterions.add(classNameSearch(expressionList, expressionList.size()-1));
            }
        }
        //#1736 1738 屏蔽直播产品
        criterions.add(Restrictions.ne("product.category",ProductType.LIVE));
        
        /**
         * 精英合同的选择产品
         */
        if (productChooseVo.getProduct() != null && productChooseVo.getProduct().getCategory() != null){
            if (productChooseVo.getProduct().getCategory()==ProductType.OTHERS){
                if ("isEcs".equals(isEcsProductCategory)){
                    criterions.add(Restrictions.and(Restrictions.eq("product.category", ProductType.OTHERS ), Restrictions.eq("product.other_of_type.id", "ECS_CLASS_FOR_OTHER")));
                }else if ("isNotEcs".equals(isEcsProductCategory)){
                    criterions.add(Restrictions.or(Restrictions.and(Restrictions.eq("product.category", ProductType.OTHERS), Restrictions.isNull("product.other_of_type.id")),Restrictions.ne("product.other_of_type.id", "ECS_CLASS_FOR_OTHER")));
                }
            }else {
                criterions.add(Restrictions.eq("product.category",productChooseVo.getProduct().getCategory()));
            }

        }else {
            //判断是否获取精英产品
            if ("isEcs".equals(isEcsProductCategory)){
                /**
                 * 目标班合同的产品的定义为： 产品类型为目标班，或者是其他中的其他类型为目标班类型
                 */
                criterions.add(Restrictions.or(Restrictions.eq("product.category", ProductType.ECS_CLASS ),
                        Restrictions.and(Restrictions.eq("product.category", ProductType.OTHERS ), Restrictions.eq("product.other_of_type.id", "ECS_CLASS_FOR_OTHER")) //目标班或者目标班的其他
                ));
            }else if ("isNotEcs".equals(isEcsProductCategory)){
                /**
                 * 非目标班合同的产品的定义为：产品类型为非目标班的，但是其他中也要除开其他类型为目标班类型（包括其他类型为空）
                 */
                criterions.add(Restrictions.or(Restrictions.and(Restrictions.ne("product.category", ProductType.ECS_CLASS), Restrictions.ne("product.category", ProductType.OTHERS)),Restrictions.or(//除开目标班和其他
                        Restrictions.and(Restrictions.eq("product.category", ProductType.OTHERS), Restrictions.isNull("product.other_of_type.id")),Restrictions.ne("product.other_of_type.id", "ECS_CLASS_FOR_OTHER"))));//其他中除开其他类型目标班类型
            }
        }



        // 产品类别
        if(productChooseVo.getProduct() != null){
//        	if(productChooseVo.getProduct().getCategory() != null){
//        	    if (productChooseVo.getProduct().getCategory()==ProductType.OTHERS){
//
//                }else {
//                    criterions.add(Restrictions.eq("product.category",productChooseVo.getProduct().getCategory()));
//                }
//            }
//        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductCategoryId())) {
//        		criterions.add(Restrictions.eq("product.productCategoryId.id",productChooseVo.getProduct().getProductCategoryId()));
//        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getCourseSeriesId())) {
        		criterions.add(Restrictions.eq("product.courseSeries.id",productChooseVo.getProduct().getCourseSeriesId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductVersionId())) {
        		criterions.add(Restrictions.eq("product.productVersion.id",productChooseVo.getProduct().getProductVersionId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductQuarterId())) {
        		criterions.add(Restrictions.eq("product.productQuarter.id",productChooseVo.getProduct().getProductQuarterId()));
        	}
//        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getSubjectId())) {
//        		criterions.add(Restrictions.eq("product.subject.id",productChooseVo.getProduct().getSubjectId()));
//        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getClassTypeId())) {
        		criterions.add(Restrictions.eq("product.classType.id",productChooseVo.getProduct().getClassTypeId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getProductGroupId())) {
        		criterions.add(Restrictions.eq("product.productGroup.id",productChooseVo.getProduct().getProductGroupId()));
        	}
        	if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeId())) {
        		criterions.add(Restrictions.eq("product.gradeDict.id",productChooseVo.getProduct().getGradeId()));
        	}

            //增加老师的过滤条件
            if (StringUtils.isNotBlank(productChooseVo.getTeacherId())) {
//                criterions.add(Restrictions.eq("miniClass.teacher.userId",productChooseVo.getTeacherId()));
                criterions.add(Restrictions.or(Restrictions.eq("twoTeacherClassTwo.teacher.userId", productChooseVo.getTeacherId())
                        , Restrictions.eq("miniClass.teacher.userId",productChooseVo.getTeacherId())));
            }

            // 改用小班的科目
            if (StringUtils.isNotBlank(productChooseVo.getProduct().getSubjectId())) {
//                criterions.add(Restrictions.eq("miniClass.subject.id",productChooseVo.getProduct().getSubjectId()));

                // Restrictions.eq("twoTeacherClassTwo_twoTeacherClass_subject.id", productChooseVo.getProduct().getSubjectId())
                criterions.add(
                        Restrictions.or(Restrictions.eq("miniClass.subject.id",productChooseVo.getProduct().getSubjectId()),
                                Restrictions.sqlRestriction("exists (select 1 from two_teacher_class t left join two_teacher_class_two tt on tt.class_id = t.class_id where tt.class_two_id={alias}.id and  t.SUBJECT='"+productChooseVo.getProduct().getSubjectId()+"')")
                        ));
            }

			if (StringUtils.isNotBlank(productChooseVo.getProduct().getGradeSegmentId())) {
				criterions.add(Restrictions.eq("product.gradeSegmentDict.id",productChooseVo.getProduct().getGradeSegmentId()));
			}
        }
        /**
         * 小班报名选择产品时可以看到无效和已过期的产品    
         * modify by Yao 2016-10-27  注释掉，以后要放开跟需求确认
         */
//        if(! ( productChooseVo.getSamllEnroll() !=null && productChooseVo.getSamllEnroll().equals("SMALL_CLASS"))){
        	 // 排除已禁用的产品
            criterions.add(Restrictions.ne("product.proStatus","1"));
            // 排除不在有效期内的产品
            criterions.add(
                    Restrictions.or(
                    Restrictions.and(Restrictions.ge("product.validEndDate", currentDate),Restrictions.le("product.validStartDate", currentDate))
            ,Restrictions.and(Restrictions.ge("product.validEndDate", currentDate),Restrictions.eq("product.category", ProductType.LIVE )))
            );
//        }
       
        // 本公司的产品和本校区的小班
        criterions.add(
                                Restrictions.or(
                                    Restrictions.or(Restrictions.eq("product.organization.id", userService.getBelongBranch().getId()),
                                            Restrictions.isNotNull("twoTeacherClassTwo.blCampus.id"))
                                    ,Restrictions.sqlRestriction("exists (select 1 from product_branch where product_id={alias}.product_id and branch_id='"+userService.getBelongBranch().getId()+"')"))

        );
        criterions.add(Restrictions.or(
                Restrictions.and(Restrictions.isNotNull("miniClass.miniClassId"),Restrictions.eq("miniClass.campusSale",0)),
                Restrictions.isNull("miniClass.miniClassId")
                ));

        // 产品组织架构向上兼容
//        criterions.add(Restrictions.sqlRestriction("(select orgLevel from organization where id = '"+userService.getCurrentLoginUser().getOrganizationId()+"') like concat((select orgLevel from organization where id = (select organization_id from product where id = product_id)),'%')"));
        if (productChooseVo.getMiniClass() != null && StringUtils.isNotBlank(productChooseVo.getMiniClass().getBlCampusId())) {
        	criterions.add(Restrictions.or(Restrictions.eq("miniClass.blCampus.id", productChooseVo.getMiniClass().getBlCampusId()),Restrictions.isNull("miniClass.blCampus.id")));
            criterions.add(Restrictions.or(Restrictions.eq("twoTeacherClassTwo.blCampus.id", productChooseVo.getMiniClass().getBlCampusId()),Restrictions.isNull("twoTeacherClassTwo.blCampus.id")));
		} else {
			criterions.add(Restrictions.or(Restrictions.eq("miniClass.blCampus.id", userService.getBelongCampus().getId()),Restrictions.isNull("miniClass.blCampus.id")));
            //双师归属分公司
            criterions.add(Restrictions.or(Restrictions.eq("twoTeacherClassTwo.blCampus.id", userService.getBelongCampus().getId()),Restrictions.isNull("twoTeacherClassTwo.blCampus.id")));
        }

        //有讲座，并且讲座没有完结
        criterions.add(Restrictions.or(Restrictions.eq("lectureClass.blBranch.id", userService.getBelongBranch().getId()),Restrictions.isNull("lectureClass.blBranch.id")));

        List<Order> orders = new ArrayList<Order>();
        dp = productChooseViewDao.findPageByCriteria(dp,orders,criterions);
        dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),ProductChooseVo.class));
        for(Object iteratorObj : dp.getDatas()){
            ProductChooseVo iteratorVo = (ProductChooseVo) iteratorObj;
            ProductVo productVo = iteratorVo.getProduct();

            /**
             * 添加打包产品
             */
            addSonProduct(productVo);

            MiniClassVo miniClass = iteratorVo.getMiniClass();
            LectureClassVo lectureClass=iteratorVo.getLectureClass();
            if(miniClass != null){
                miniClass.setNextMiniClassTime(miniClassCourseDao.getTop1MiniClassCourseByDate(currentDate,miniClass.getMiniClassId()));
                List<Criterion> StudentCriterionList = new ArrayList<Criterion>();
                StudentCriterionList.add(Restrictions.eq("miniClass.miniClassId", miniClass.getMiniClassId()));
                miniClass.setMiniClassPeopleNum(miniClassStudentDao.findCountByCriteria(StudentCriterionList));
            }
            
            if(lectureClass!=null){
            	//现在报名人数
            	lectureClass.setNowMembers(lectureClassStudentDao.findCountSql("select count(1) from lecture_class_student where lecture_id='"+lectureClass.getLectureId()+"'", new HashMap<String, Object>()));
            }

            TwoTeacherClassTwoVo twoTeacherClassTwoVo = iteratorVo.getTwoTeacherClassTwo();
            if (twoTeacherClassTwoVo!=null){
                List<Criterion> l = new ArrayList<>();
                l.add(Restrictions.eq("classTwo.id", twoTeacherClassTwoVo.getId()));
                twoTeacherClassTwoVo.setTwoTeacherClassPeopleNum(twoTeacherClassStudentDao.findCountByCriteria(l));//报读人数

                Double consume = twoTeacherClassStudentAttendentDao.getConsumeHourByClassTwoId(twoTeacherClassTwoVo.getId());
                twoTeacherClassTwoVo.setConsume(consume);
                twoTeacherClassTwoVo.setNextTwoTeacherClassTime(twoTeacherClassService.getNextTwoTeacherClassCourseByDate(currentDate, twoTeacherClassTwoVo.getTwoTeacherClassId()));
            }

            String promotionId = iteratorVo.getProduct().getPromotionId();
            Map map = getPromotionForContract(promotionId);
            iteratorVo.getProduct().setPromotionId((String) map.get("promotionId"));
            iteratorVo.getProduct().setPromotionName((String) map.get("promotionName"));
        }
        return dp;
    }



    private void addSonProduct(ProductVo productVo) {
        String productId = productVo.getId();
        String hql = "from ProductPackage where parentProduct.id = :id ";
        Map<String,String> map = new HashMap<>();
        map.put("id", productId);
        List<ProductPackage> productPackages = productPackageDao.findAllByHQL(hql, map);
        List<ProductVoCount> result =  new ArrayList<>();
        Map<Product, Integer> temp = new HashMap<>();
        for (ProductPackage p : productPackages){
            Integer integer = temp.get(p.getSonProduct());
            if (integer == null){
                temp.put(p.getSonProduct(), 1);
            }else {
                integer++;
                temp.put(p.getSonProduct(), integer);
            }
        }
        for (Map.Entry<Product, Integer> entry : temp.entrySet()){
            ProductVoCount vo = new ProductVoCount();
            vo.setProductVo(HibernateUtils.voObjectMapping(entry.getKey(), ProductVo.class));
            vo.setCount(entry.getValue());
            result.add(vo);
        }



        productVo.setSubProducts(result);
    }

    /**
     * 过滤掉已经过期的优惠 并且是无效的
     * @param promotionId
     * @return
     */
    private Map<String,String> getPromotionForContract(String promotionId) {
        Map<String,String> map = new HashMap<>();
        if (promotionId!=null){
            if (!"-1".equals(promotionId)){
                String[] promotions = promotionId.split(",");
                StringBuffer promotionOutId = new StringBuffer();
                StringBuffer promotionName =new StringBuffer();
                for (String Id: promotions){
                    Promotion promotion = promotionDao.findById(Id);//
                    if (promotion!=null){
                        if (DateTools.daysBetween(promotion.getEndTime(),DateTools.getCurrentDate())<=0&&"Y".equals(promotion.getIsActive())){
                            promotionOutId.append(Id).append(",");
                            promotionName.append(promotion.getName()).append(",");
                        }
                    }
                }

                if (StringUtils.isNotBlank(promotionOutId)&&StringUtils.isNotBlank(promotionName)){
                    promotionOutId.delete(promotionOutId.lastIndexOf(","),promotionOutId.length());
                    promotionName.delete(promotionName.lastIndexOf(","),promotionName.length());
                    map.put("promotionId",promotionOutId.toString());
                    map.put("promotionName",promotionName.toString());
                }else {
                    map.put("promotionId",null);
                    map.put("promotionName",null);
                }
                return map;
            }else {
                map.put("promotionId",promotionId);
                map.put("promotionName","所有优惠");
                return map;
            }
        }else {
            map.put("promotionId",null);
            map.put("promotionName",null);
            return map;
        }
    }

}
