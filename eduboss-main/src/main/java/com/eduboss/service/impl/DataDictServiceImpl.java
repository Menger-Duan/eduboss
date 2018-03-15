package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.common.DataDictCategory;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.TeacherSubjectDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.User;
import com.eduboss.domainVo.DataDictVo;
import com.eduboss.domainVo.GradeInfoVo;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.EduPlatform.BaseRelateDataVo;
import com.eduboss.domainVo.EduPlatform.BaseRelateDate2BossVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.DataDictService;
import com.eduboss.service.ProductService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.HttpClientUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service
public class DataDictServiceImpl implements DataDictService {
	
	private final static Logger log = Logger.getLogger(DataDictServiceImpl.class);
	
	@Autowired
	DataDictDao dataDictDao;
	
	@Autowired
	UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherSubjectDao teacherSubjectDao;

    @Autowired
    private ProductService productService;
    
    
    @Autowired
    private OrganizationDao organizationDao;

	@Override
	public DataPackage getDataDictList(DataDict dataDict, DataPackage dp) {
		//如果有字段有值，用like进行条件查询
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(dataDict);
		DataDict parentDataDict = dataDict.getParentDataDict();
		if (null != parentDataDict && StringUtil.isNotBlank(parentDataDict.getId())) {
			criterionList.add(Expression.eq("parentDataDict.id", dataDict.getParentDataDict().getId()));
		}
		criterionList.add(Expression.ne("category", DataDictCategory.CITY));
		criterionList.add(Expression.ne("category", DataDictCategory.PROVINCE));
		criterionList.add(Expression.ne("state", "1"));
		
		List<Order> orderList=new ArrayList<Order>();
		orderList.add(Order.desc("category"));
		if (null != dp.getSidx()) {
			if (dp.getSidx().equals("parentDataDictCategoryName")) {
				if ("asc".equalsIgnoreCase(dp.getSord())) {
					orderList.add(Order.asc("parentDataDict.category"));
				} else {
					orderList.add(Order.desc("parentDataDict.category"));
				}
			} else if (dp.getSidx().equals("parentDataDictName")) {
				if ("asc".equalsIgnoreCase(dp.getSord())) {
					orderList.add(Order.asc("parentDataDict.id"));
				} else {
					orderList.add(Order.desc("parentDataDict.id"));
				}
			} else {
				orderList.addAll(HibernateUtils.prepareOrder(dp, null, null));
			}
		}
//		orderList.add(Order.desc("parentDataDict.id"));
		dp = dataDictDao.findPageByCriteria(dp, orderList, criterionList);
		List<DataDictVo> list =  HibernateUtils.voListMapping((List<DataDict>) dp.getDatas(), DataDictVo.class);
		dp.setDatas(list);
		return dp;
	}

	@Override
	public void deleteDataDict(DataDict dataDict) {
		dataDictDao.delete(dataDict);
		List<DataDict> list=dataDictDao.findByCriteria(Expression.eq("parentDataDict.id", dataDict.getId()));
		if(list!=null && list.size()>0)
			dataDictDao.deleteAll(list);
	}

	@Override
	public Response saveOrUpdateDataDict(DataDict dataDict) {
		Response response = new Response();
		User user = userService.getCurrentLoginUser();
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("category", dataDict.getCategory()));
		criterionList.add(Expression.eq("name", dataDict.getName()));
		DataDict parentDataDict =dataDict.getParentDataDict();
		if (null != parentDataDict && StringUtil.isNotBlank(parentDataDict.getId()) && !parentDataDict.getId().equals("")) {
			criterionList.add(Expression.eq("parentDataDict.id", dataDict.getParentDataDict().getId()));
		} else {
			criterionList.add(Expression.isNull("parentDataDict"));
		}
		if(dataDict.getId()!=null && !dataDict.getId().equals("") && StringUtils.isNotBlank(dataDict.getId())){
			criterionList.add(Expression.ne("id", dataDict.getId()));
		}
		List<DataDict> list = dataDictDao.findAllByCriteria(criterionList);
		if (null != list && list.size() > 0) {
			response.setResultCode(-1);
			response.setResultMessage("已存在相同的数字字典！");
		} else {
			if(dataDict.getId()==null){
				dataDict.setCreateTime(DateTools.getCurrentDateTime());
				dataDict.setCreateUserId(user.getUserId());
			}
			dataDictDao.save(dataDict);
		}
		return response;
	}

	public int findDataDictCountByName(String DataDictName,String id,String orderId){
		List<Criterion> listCriterion=new ArrayList();
		listCriterion.add(Expression.eq("name", DataDictName));
		if(StringUtils.isNotBlank(orderId) && !"".equals(orderId)){
			listCriterion.add(Expression.eq("dictOrder", Integer.valueOf(orderId)));			
		}
		if(StringUtils.isNotEmpty(id)){
			listCriterion.add(Expression.ne("id", id));
		}
		List<DataDict> list=dataDictDao.findAllByCriteria(listCriterion);
		return list.size();
	}
	
	@Override
	public DataDict findDataDictByNameAndCateGory(String name, String category) {
		List<Criterion> listCriterion=new ArrayList();
		listCriterion.add(Expression.eq("name", name));
		listCriterion.add(Expression.eq("category", DataDictCategory.valueOf(category)));
		
		List<DataDict> list=dataDictDao.findAllByCriteria(listCriterion);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public DataDictVo findDataDictById(String id) {
		DataDict dd = dataDictDao.findById(id);
		return HibernateUtils.voObjectMapping(dd, DataDictVo.class);
	}
	
	@Override
	public DataDict findById(String id) {
		return dataDictDao.findById(id);
	}

	@Override
	public Map getRegionList() {
		List orderList=new ArrayList<Order>();
		orderList.add(Order.asc("dictOrder"));
		Map map=new LinkedHashMap();
		List<DataDict> provinceList=dataDictDao.findByCriteria(orderList,Expression.eq("category", DataDictCategory.PROVINCE));
		List<DataDict> cityList=null;
		for(DataDict dict : provinceList){
			Map map1=new LinkedHashMap();
			cityList=dataDictDao.findByCriteria(orderList,Expression.eq("parentDataDict.id", dict.getId()),Expression.eq("category", DataDictCategory.CITY));
			for(DataDict cityDict : cityList){
				map1.put(cityDict.getId(), cityDict.getName());
			}
			map.put(dict.getId()+"-"+dict.getName(), map1);
		}
		
//		Map map1=new LinkedHashMap();
//		map1.put("3", "广州市");
//		map1.put("4", "佛山市");
//		map.put("1-广东省", map1);
//		map.put("2-山东省", null);
		return map;
	}

    /**
     * 根据老师获取可教科目
     *
     * @param teacherId
     * @return
     */
    @Override
    public Set<DataDict> getCanTeachSubjectByTeacher(String teacherId) {
        Set<DataDict> subjects = new HashSet<DataDict>();
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("teacher.userId", teacherId));
        List<TeacherSubject> list = teacherSubjectDao.findAllByCriteria(criterions);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).getSubject().getName();
            subjects.add(list.get(i).getSubject());
        }
        return subjects;
    }

    /**
     * 根据老师和年级获取可教科目
     *
     * @param teacherId
     * @param gradeId
     * @return
     */
    @Override
    public Set<DataDict> getCanTeachSubjectByTeacherAndGrade(String teacherId, String gradeId) {
        Set<DataDict> subjects = new HashSet<DataDict>();
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("id.id",teacherId));
        criterions.add(Restrictions.eq("id.grade",gradeId));
        List<TeacherSubject> list = teacherSubjectDao.findAllByCriteria(criterions);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).getSubject().getName();
            subjects.add(list.get(i).getSubject());
        }
        return subjects;
    }

    /**
     * 根据老师和学生获取可教科目
     *
     * @param teacherId
     * @param studentId
     * @return
     */
    @Override
    public Set<DataDict> getCanTeachSubjectByTeacherAndStudent(String teacherId, String studentId) {
        StudentVo studentVo = studentService.findStudentById(studentId);
        if(studentVo == null){
            throw new ApplicationException("找不到对应学生，ID：" + studentId);
        }
        Set<DataDict> subjects = new HashSet<DataDict>();
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("teacher.userId",teacherId));
        criterions.add(Restrictions.eq("grade.id",studentVo.getGradeId()));
        List<TeacherSubject> list = teacherSubjectDao.findAllByCriteria(criterions);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).getSubject().getName();
            subjects.add(list.get(i).getSubject());
        }
        return subjects;
    }

    /**
     * 根据老师和科目获取可教年级
     *
     * @param teacherId
     * @param subjectId
     * @return
     */
    @Override
    public Set<DataDict> getCanTeachGradeByTeacherAndSubject(String teacherId, String subjectId) {
        Set<DataDict> grades = new HashSet<DataDict>();
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("teacher.userId",teacherId));
        criterions.add(Restrictions.eq("subject.id",subjectId));
        List<TeacherSubject> list = teacherSubjectDao.findAllByCriteria(criterions);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).getGrade().getName();
            grades.add(list.get(i).getGrade());
        }
        return grades;
    }

    /**
     * 根据产品获取关联科目
     *
     * @param productId
     * @return
     *//*
    @Override
    public Set<DataDict> getSubjectsByProduct(String productId) {
        Set<DataDict> subjects = new HashSet<DataDict>();
        ProductVo product = productService.findProductById(productId);
        if(StringUtils.isNotBlank(product.getSubjectIds())){
            List<Criterion> criterions = new ArrayList<Criterion>();
            criterions.add(Restrictions.in("id",product.getSubjectIds().split(",")));
            subjects.addAll(dataDictDao.findAllByCriteria(criterions));
        }
        return subjects;
    }*/
    
    /**
     * 根据产品获取关联科目
     *
     * @param productId
     * @return
     */
    @Override
    public Set<DataDict> getSubjectsByProduct(String productId) {
        Set<DataDict> subjects = new HashSet<DataDict>();
        ProductVo product = productService.findProductById(productId);

        Map<String, Object> params = Maps.newHashMap();
        if(StringUtils.isNotBlank(product.getSubjectIds())){
        	String[] subjectIds = product.getSubjectIds().split(",");
//        	String subjectIdsInSql = "";
//        	for (String subjectId : subjectIds) {
//        		subjectIdsInSql += "'" + subjectId + "' ,"; 
//        	}
//        	subjectIdsInSql = subjectIdsInSql.substring(0, subjectIdsInSql.length() -1);
        	params.put("subjectIds", subjectIds);
        	String sql = " select * from data_dict where id in (:subjectIds ) ";
            subjects.addAll(dataDictDao.findBySql(sql, params));
        }
        return subjects;
    }

	/**
	 * @param category
	 * @return
	 */
	@Override
	public SelectOptionResponse getSubSelectOptionBySuper(String category, String superId) {
		if (category == null) {
			throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
		}

		List<NameValue> nvs = new ArrayList<>();
		DataPackage dataPackage = new DataPackage(0, 999);
		dataPackage = getSubSelectOption(new DataDict(null, DataDictCategory.valueOf(category)), dataPackage,superId);
		List<DataDict> sos = (List<DataDict>)dataPackage.getDatas();
		for (DataDict so : sos) {
			nvs.add(so);
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}

	/**
	 * 类型为option
	 * 父类型为superId
	 * @param option
	 * @param dp
	 * @param superId
     * @return
     */
	private DataPackage getSubSelectOption(DataDict option, DataPackage dp,String superId) {
		if (option.getCategory() == null) {
			throw new ApplicationException(ErrorCode.OPTION_CATEGORY_EMPTY);
		}
		List<DataDict> selectOptions = dataDictDao.findByCriteria(HibernateUtils.prepareOrder(dp, "dictOrder", "asc"), Expression.eq("category", option.getCategory()), Expression.ne("state", "1"),Expression.eq("parentDataDict.id",superId));

		dp.setDatas(selectOptions);

		return dp;

	}
	
	@Override
	public Map<String, Object> getGradeInfo(String gradeId) {
		Map<String, Object> map = new HashMap<String,Object>();
		StringBuffer query = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		query.append(" select * from data_dict where CATEGORY ='STUDENT_GRADE' ");
		if(StringUtils.isNotBlank(gradeId)){
			query.append(" and ID = :gradeId ");
			params.put("gradeId", gradeId);
		}
		List<DataDict> dataDicts = dataDictDao.findBySql(query.toString(),params);
		List<GradeInfoVo> gradeInfoVos = new ArrayList<GradeInfoVo>();
		for(DataDict dataDict:dataDicts){
			GradeInfoVo gradeInfoVo = new GradeInfoVo();
			gradeInfoVo.setId(dataDict.getId());
			gradeInfoVo.setName(dataDict.getName());
			gradeInfoVo.setCreateDate(dataDict.getCreateTime());
			gradeInfoVo.setUpdateDate("");
			gradeInfoVos.add(gradeInfoVo);
		}
		map.put("resultStatus", 200);
		map.put("resultMessage", "年级列表");
		map.put("result", gradeInfoVos);	
		return map;
	}
	
	@Override
	public Map<String, Object> findDataDictByCategory(String category) {
		Map<String, Object> map = new HashMap<String,Object>();
		StringBuilder query = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		params.put("category", category);
		query.append(" select dd.`VALUE` as dataId ,dd.`NAME` as dataValue from data_dict dd where dd.CATEGORY = :category ");
		List<Map<Object, Object>> result = dataDictDao.findMapBySql(query.toString(),params);
		if(result==null || result.size()==0){
			map.put("resultStatus", 400);
			map.put("resultMessage", "找不到相应的基础数据");
			map.put("result", null);			
		}else{
			map.put("resultStatus", 200);
			map.put("resultMessage", "基础数据列表");
			map.put("result", result);				
		}

		return map;
	}

	
	@Override
	public Map<String, Object> findDataDictProductInfo(String category) {
		Map<String , Object> map = new HashMap<String,Object>();
		Map<String , Object> result = new HashMap<String,Object>();
		//所属分公司organization organizationId organizationName
		//产品年份productVersion productVersionId productVersionName
		//产品季度productQuarter productQuarterId productQuarterName
		//产品系列courseSeries courseSeriesId courseSeriesName
		//年级 grade gradeId gradeName
		//科目subject subjectId subjectName
		//班级类型classType classTypeId classTypeName
		
		StringBuilder query = new StringBuilder();
	
	    long organizaiton_time1 = System.currentTimeMillis();		
        Map<String, Object> params = Maps.newHashMap();
		query.append(" SELECT trim(id) as organizationId,trim(`NAME`) as organizationName from organization WHERE orgType = 'BRENCH' ");
		List<Map<Object, Object>> organization =organizationDao.findMapBySql(query.toString(),params);
		result.put("organization", organization);
		query.delete(0, query.length());
		long organizaiton_time2 = System.currentTimeMillis();
		if(log.isDebugEnabled()){
			log.debug("organization_time:"+(organizaiton_time2-organizaiton_time1));
		}
	    
		
		long productVersion_time1 = System.currentTimeMillis();	
		query.append(" SELECT id as productVersionId,`NAME` as productVersionName from data_dict WHERE CATEGORY = 'PRODUCT_VERSION' ORDER BY DICT_ORDER desc ");
		List<Map<Object, Object>> productVersion =dataDictDao.findMapBySql(query.toString(),params);
		result.put("productVersion", productVersion);
		query.delete(0, query.length());
		long productVersion_time2 = System.currentTimeMillis();
		if(log.isDebugEnabled()){
			log.debug("productVersion_time:"+(productVersion_time2-productVersion_time1));
		}
		//合并查询
		long query_time1 = System.currentTimeMillis();	
		query.append("select id,`NAME`,CATEGORY from data_dict where CATEGORY in('PRODUCT_QUARTER','STUDENT_GRADE','SUBJECT','CLASS_TYPE') and STATE ='0' ORDER BY DICT_ORDER ");
	    List<Map<Object, Object>> temp = dataDictDao.findMapBySql(query.toString(),params);
	    query.delete(0, query.length());
	    
	    List<Map<String, String>> productQuarter = new ArrayList<>();
	    List<Map<String, String>> grade = new ArrayList<>();
	    List<Map<String, String>> subject = new ArrayList<>();
	    List<Map<String, String>> classType = new ArrayList<>();
	    Map<String, String> tempmap = null;
	    for(Map<Object, Object> tempMap:temp){
	    	Map<String, String> temp_map =(Map)tempMap;
	    	String string =temp_map.get("CATEGORY");
	    	//temp_map.remove("CATEGORY");
	    	if("PRODUCT_QUARTER".equals(string)){
	    		tempmap = new HashMap<>();
	    		tempmap.put("productQuarterId", temp_map.get("id"));
	    		tempmap.put("productQuarterName", temp_map.get("NAME"));
	    		productQuarter.add(tempmap);
	    	}
	    	if("STUDENT_GRADE".equals(string)){
	    		tempmap = new HashMap<>();
	    		tempmap.put("gradeId", temp_map.get("id"));
	    		tempmap.put("gradeName", temp_map.get("NAME"));
	    		grade.add(tempmap);
	    	}
	    	if("SUBJECT".equals(string)){
	    		tempmap = new HashMap<>();
	    		tempmap.put("subjectId", temp_map.get("id"));
	    		tempmap.put("subjectName", temp_map.get("NAME"));
	    		subject.add(tempmap);
	    	}
	    	if("CLASS_TYPE".equals(string)){
	    		tempmap = new HashMap<>();
	    		tempmap.put("classTypeId", temp_map.get("id"));
	    		tempmap.put("classTypeName", temp_map.get("NAME"));
	    		classType.add(tempmap);
	    	} 
	    }
	    result.put("productQuarter", productQuarter);
	    result.put("grade", grade);
	    result.put("subject", subject);
	    result.put("classType", classType);
	    
	    long query_time2 = System.currentTimeMillis();
		if(log.isDebugEnabled()){
			log.debug("productVersion_time:"+(query_time2-query_time1));
		}
				
		//联动产品系列
		query.append(" SELECT DISTINCT p.COURSE_SERIES_ID AS courseSeriesId ,dd.`NAME` as courseSeriesName ");
		params.put("category", category);
		query.append(" from product p LEFT JOIN data_dict dd on p.COURSE_SERIES_ID = dd.ID WHERE p.CATEGORY = :category AND p.COURSE_SERIES_ID IS NOT NULL ");
		List<Map<Object, Object>> courseSeries =dataDictDao.findMapBySql(query.toString(),params);
		result.put("courseSeries", courseSeries);
		query.delete(0, query.length());
		
		map.put("resultStatus", 200);
		map.put("resultMessage", "产品基础数据信息");
		map.put("result", result);
		return map;
	}
	
	/**
	 * 根据category获取数字字典列表
	 */
	@Override
	public List<DataDict> getDataDictListByCategory(String category) {
		return dataDictDao.getDataDictListByCategory(DataDictCategory.valueOf(category));
	}
	
	@Override
	public Map<String, Object> findOrgInfoForTextBook() {
		Map<String , Object> map = new HashMap<String,Object>();
		Map<String , Object> result = new HashMap<String,Object>();
		//所属分公司organization organizationId organizationName	
		StringBuilder query = new StringBuilder();	
        Map<String, Object> params = Maps.newHashMap();
		query.append(" SELECT trim(id) as pressExtId,trim(`NAME`) as pressExtName from organization WHERE orgType ='GROUNP' or orgType ='BRENCH' ORDER BY orgLevel ");
		List<Map<Object, Object>> organization =organizationDao.findMapBySql(query.toString(),params);
		result.put("pressExt", organization);	
		map.put("resultStatus", 200);
		map.put("resultMessage", "产品基础数据信息");
		map.put("result", result);
		return map;
	}

	@Override
	public Map<String, Object> findBaseForTextBook() {
		Map<String, String> params = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		String json = null;
        try {
            final String listUrl = PropertiesUtils.getStringValue("baseData"); 
            log.info("findBaseForTextBook:"+listUrl);
            long timeBegin = System.currentTimeMillis();
            json = HttpClientUtil.doGet(listUrl, params);
            long timeEnd = System.currentTimeMillis();
            log.info("findBaseForTextBook-costtime:"+(timeEnd-timeBegin)+"ms");
            // 解析结果
            JSONObject object = JSON.parseObject(json);
            if(object==null)return object;
            Integer code = (Integer) object.get("code");
            if (code!=200) {
                return null;
            }
            JSONObject data = (JSONObject) object.get("data");
            result.put("result", data);
            return result;
        } catch (Exception e) {
            // 请求失败 则抛出返回 null
            e.printStackTrace();
            throw new ApplicationException("请求教材基础数据失败");
        }
	}

	@Override
	public Map<String, Object> findBaseRelateDataForTextBook(BaseRelateDataVo baseRelateDataVo) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		final String url = PropertiesUtils.getStringValue("baseRelateData");
		// params.put("type", 1);
		if (baseRelateDataVo.getSection() != null) {
			params.put("sectionId", baseRelateDataVo.getSection());
		}
		if (baseRelateDataVo.getSubject() != null) {
			params.put("subjectId", baseRelateDataVo.getSubject());
		}
		if (baseRelateDataVo.getPublishVersion() != null) {
			params.put("publishVersionId", baseRelateDataVo.getPublishVersion());
		}
		if (baseRelateDataVo.getBookVersion() != null) {
			params.put("bookVersionId", baseRelateDataVo.getBookVersion());
		}
		final String postContent = Json.toJson(params);
		try {
			
            log.info("findBaseRelateDataForTextBook:"+url);
            long timeBegin = System.currentTimeMillis();			
			String response = HttpClientUtil.doPostJson(url, postContent);
            long timeEnd = System.currentTimeMillis();
            log.info("findBaseRelateDataForTextBook-costtime:"+(timeEnd-timeBegin)+"ms");
			JSONObject object = JSON.parseObject(response);
			BaseRelateDate2BossVo baseRelateDate2BossVo = new BaseRelateDate2BossVo();
			if (object.get("code").toString().equals("200")) {
				JSONObject result = (JSONObject)object.get("data");			
				baseRelateDate2BossVo.setPublishVersion(JSON.parseArray(result.getJSONArray("simplePublishVersionList").toJSONString(),BaseRelateDate2BossVo.PublishVersion.class));
				baseRelateDate2BossVo.setBookVersion(JSON.parseArray(result.getJSONArray("simpleBookVersionList").toJSONString(),BaseRelateDate2BossVo.BookVersion.class));
				baseRelateDate2BossVo.setSection(JSON.parseArray(result.getJSONArray("simpleSectionList").toJSONString(),BaseRelateDate2BossVo.Section.class));
				baseRelateDate2BossVo.setSubject(JSON.parseArray(result.getJSONArray("simpleSubjectList").toJSONString(),BaseRelateDate2BossVo.Subject.class));
				map.put("result", baseRelateDate2BossVo);
				return map;
			} else {
				return null;
			}
		} catch (Exception e) {
			 e.printStackTrace();
			throw new ApplicationException("请求教材基础数据失败");
		}

	}

    @Override
    public List<DataDict> listDataDictsByIds(String[] ids) {
        return dataDictDao.listDataDictsByIds(ids);
    }
}
