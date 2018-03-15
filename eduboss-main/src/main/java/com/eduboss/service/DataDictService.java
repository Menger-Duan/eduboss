package com.eduboss.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.domain.DataDict;
import com.eduboss.domainVo.DataDictVo;
import com.eduboss.domainVo.EduPlatform.BaseRelateDataVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;

public interface DataDictService {

	public DataDict findById(String id);
	
	public DataPackage getDataDictList(DataDict dataDict, DataPackage dataPackage);

	public void deleteDataDict(DataDict dataDict);

	public Response saveOrUpdateDataDict(DataDict dataDict);

	public DataDictVo findDataDictById(String id);
	
	public DataDict findDataDictByNameAndCateGory(String name,String category);

	public Map getRegionList();
	
	public int findDataDictCountByName(String DataDictName,String id,String orderId);


    /**
     * 根据老师获取可教科目
     * @param teacherId
     * @return
     */
    public Set<DataDict> getCanTeachSubjectByTeacher(String teacherId);

    /**
     * 根据老师和年级获取可教科目
     * @param teacherId
     * @param gradeId
     * @return
     */
    public Set<DataDict> getCanTeachSubjectByTeacherAndGrade(String teacherId,String gradeId);

    /**
     * 根据老师和学生获取可教科目
     * @param teacherId
     * @param studentId
     * @return
     */
    public Set<DataDict> getCanTeachSubjectByTeacherAndStudent(String teacherId,String studentId);

    /**
     * 根据老师和科目获取可教年级
     * @param teacherId
     * @param subjectId
     * @return
     */
    public Set<DataDict> getCanTeachGradeByTeacherAndSubject(String teacherId,String subjectId);

    /**
     * 根据产品获取关联科目
     * @param productId
     * @return
     */
    public Set<DataDict> getSubjectsByProduct(String productId);

    /**
     *
     * @param category
     * @param superId
     * @return
     */
    SelectOptionResponse getSubSelectOptionBySuper(String category,String superId);
    
    /**
     * 
     * 开发给教育平台的接口
     */
    public Map<String, Object> getGradeInfo(String gradeId);
    
    /**
     * 开发给教育平台的接口 根据字段类目Category获取字典数据 
     */
    public Map<String, Object> findDataDictByCategory(String category);
    public Map<String, Object> findDataDictProductInfo(String category);
    
    //用于获取星火教材 组织架构的 信息
    public Map<String, Object> findOrgInfoForTextBook();
    //从教学平台获取基础数据 绑定星火教材
    public Map<String, Object> findBaseForTextBook();
    public Map<String, Object> findBaseRelateDataForTextBook(BaseRelateDataVo baseRelateDataVo);
    
    /**
     * 根据category获取数字字典列表
     * @param category
     * @return
     */
   	List<DataDict> getDataDictListByCategory(String category);
   	
   	List<DataDict> listDataDictsByIds(String[] ids);
}
