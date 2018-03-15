package com.eduboss.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.EnumHelper;
import com.eduboss.domain.DataDict;
import com.eduboss.domainVo.DataDictVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.service.DataDictService;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value="/DataDictController")
public class DataDictController {
	
	@Autowired
	DataDictService dataDictService;
	
	@RequestMapping(value="/getDataDictTypeList")
	@ResponseBody
	public DataPackageForJqGrid getDataDictTypeList(String enumNameLike, String enumValueLike){
		DataPackage dataPackage = new DataPackage(0,999);
		List<NameValue> list=EnumHelper.getEnumOptions("DataDictCategory");
		List<NameValue> returnList = new ArrayList<NameValue>();
		if (StringUtil.isNotBlank(enumNameLike) && StringUtil.isNotBlank(enumValueLike)) {
			for (NameValue nv : list) {
				if (-1 != nv.getName().indexOf(enumNameLike.toUpperCase()) && -1 != nv.getValue().indexOf(enumValueLike.toUpperCase())) {
					returnList.add(nv);
				}
			}
		} else if (StringUtil.isNotBlank(enumNameLike)){
			for (NameValue nv : list) {
				if (-1 != nv.getName().indexOf(enumNameLike.toUpperCase())) {
					returnList.add(nv);
				}
			}
		} else if (StringUtil.isNotBlank(enumValueLike)) {
			for (NameValue nv : list) {
				if (-1 != nv.getValue().indexOf(enumValueLike.toUpperCase())) {
					returnList.add(nv);
				}
			}
		} else {
			returnList = list;
		}
		dataPackage.setDatas(returnList);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value="/getDataDictList")
	@ResponseBody
	public DataPackageForJqGrid getDataDictList(GridRequest gridRequest, DataDict dataDict){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = dataDictService.getDataDictList(dataDict, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/editDataDict")
	@ResponseBody
	public Response editDataDict(@ModelAttribute GridRequest gridRequest, @ModelAttribute DataDict dataDict) {
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			dataDictService.deleteDataDict(dataDict);
		} else {
			return dataDictService.saveOrUpdateDataDict(dataDict);
		}
		return new Response();
	}

	@ResponseBody
	@RequestMapping(value = "/findDataDictById")
	public DataDictVo findDataDictById(@RequestParam String id) {
		return dataDictService.findDataDictById(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findDataDictByNameAndCateGory")
	public DataDict findDataDictByNameAndCateGory(@RequestParam String name,@RequestParam String category) {
		return dataDictService.findDataDictByNameAndCateGory(name,category);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRegionList")
	public Map getRegionList(){
		
		return dataDictService.getRegionList();
	}
	
	@ResponseBody
	@RequestMapping(value = "/findDataDictCountByName")
	public int findDataDictCountByName(String DataDictName,String id,String orderId){
		return dataDictService.findDataDictCountByName(DataDictName, id,orderId);
	}

    /**
     * 根据产品获取关联科目
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSubjectsByProduct")
    public List<NameValue> getSubjectsByProduct(@RequestParam String productId,String teacherId,String studentId){
    	List<NameValue> nvs = new ArrayList<NameValue>();
        Set<DataDict> productSubjects = dataDictService.getSubjectsByProduct(productId);
        if(StringUtils.isNotBlank(teacherId) && StringUtils.isNotBlank(studentId)){
            Set<DataDict> canTeachSubjects = dataDictService.getCanTeachSubjectByTeacherAndStudent(teacherId,studentId);
            if(productSubjects != null && productSubjects.size() > 0 && canTeachSubjects != null && canTeachSubjects.size() > 0){
//                if(productSubjects.size() > canTeachSubjects.size()){
//                    // 以 productSubjects 为主取交集
//                    canTeachSubjects.retainAll(productSubjects);
//                    for (DataDict dd: canTeachSubjects) {
//                    	nvs.add(SelectOptionResponse.buildNameValue(dd.getName(), dd.getValue()));
//                    }
//                }else{
                	//  好像retainAll 没有效果，返回的集合是null  
                	for (DataDict dataDict : productSubjects) {
						for (DataDict dataDict2 : canTeachSubjects) {
							if(dataDict.getId().equals(dataDict2.getId())){
								nvs.add(SelectOptionResponse.buildNameValue(dataDict.getName(), dataDict.getValue()));
							}
						}
					}
                	
//                    // 以 canTeachSubjects 为主取交集
//                    productSubjects.retainAll(canTeachSubjects);
//                    for (DataDict dd: productSubjects) {
//                    	nvs.add(SelectOptionResponse.buildNameValue(dd.getName(), dd.getValue()));
//                    }
//                    
                    
//                }
            }
        }else{
        	for (DataDict dd: productSubjects) {
            	nvs.add(SelectOptionResponse.buildNameValue(dd.getName(), dd.getValue()));
            }
        }
        return nvs;
    }

	@RequestMapping(value = "/getSubSelectOptionBySuper", method =  RequestMethod.GET)
	@ResponseBody
	public SelectOptionResponse getSubSelectOptionBySuper(String category){
		return dataDictService.getSubSelectOptionBySuper(category,"SMALL_CLASS");
	}
}
