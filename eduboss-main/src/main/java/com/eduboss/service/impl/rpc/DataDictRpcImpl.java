package com.eduboss.service.impl.rpc;

import java.util.ArrayList;
import java.util.List;

import org.boss.rpc.base.dto.DataDictRpcVo;
import org.boss.rpc.base.dto.Label;
import org.boss.rpc.eduboss.service.DataDictRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.Region;
import com.eduboss.service.DataDictService;
import com.eduboss.service.RegionService;
import com.eduboss.service.StudentSchoolService;
import com.eduboss.utils.HibernateUtils;

@Service("dataDictRpcImpl")
public class DataDictRpcImpl implements DataDictRpc {

	@Autowired
	private RegionService regionService;
	
	@Autowired
	private StudentSchoolService studentSchoolService;
	
	@Autowired
	private DataDictService dataDictService;
	
	/**
	 * 获取全部省份列表
	 */
	@Override
	public List<Label> listAllProvince() {
		return HibernateUtils.voListMapping(regionService.getAllProvinces(), Label.class);
	}

	/**
	 * 根据省份ID获取城市列表
	 */
	@Override
	public List<Label> listCityByProvinceID(String provinceID) {
		return HibernateUtils.voListMapping(regionService.getCitys(provinceID), Label.class);
	}

	/**
	 * 根据位置信息获取学校列表
	 */
	@Override
	public List<Label> listSchoolByRegion(String provinceID, String cityID) {
		return HibernateUtils.voListMapping(studentSchoolService.listSchoolByRegion(provinceID, cityID), Label.class);
	}

	/**
	 * 根据category获取数字字典列表
	 */
	@Override
	public List<DataDictRpcVo> getDataDictListByCategory(String category) {
		return HibernateUtils.voListMapping(dataDictService.getDataDictListByCategory(category), DataDictRpcVo.class);
	}

	@Override
	public String getRegionNameById(String regionId) {
		Region region = regionService.getRegionById(regionId);
		return region != null ? region.getName() : null;
	}

	@Override
	public List<Label> listDataDictsByIds(String[] dataDictId) {
		List<DataDict> list = dataDictService.listDataDictsByIds(dataDictId);
		List<Label> labelList = new ArrayList<Label>();
		for (DataDict dataDict : list) {
		    Label label = new Label();
		    label.setId(dataDict.getId());
		    label.setText(dataDict.getName());
		    labelList.add(label);
		}
		return labelList;
	}

}
