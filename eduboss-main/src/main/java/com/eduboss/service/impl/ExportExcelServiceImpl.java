package com.eduboss.service.impl;

import com.eduboss.domainVo.ContractExcelVo;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.ContractService;
import com.eduboss.service.ExportExcelService;
import com.eduboss.utils.ExportExcelUtil;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("com.eduboss.service.ExportExcelService")
public class ExportExcelServiceImpl implements ExportExcelService {

	private final static Logger log=Logger.getLogger(ExportExcelServiceImpl.class);
	
	@Autowired
	private ContractService contractService;
	

	@Override
	public String exportContractListExcel(DataPackage dataPackage, ContractVo contractVo , TimeVo timeVo) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String ossKey= timeFormat.format(new Date())+ MD5.getMD5(UUID.randomUUID().toString())+".xls";
		List list= contractService.findExcelContract( dataPackage, contractVo, timeVo  );
		log.info("导出合同！");
		Set<ContractExcelVo> datas = HibernateUtils.voCollectionMapping(list, ContractExcelVo.class);
		Map<String,String > header= new HashMap<>();
		header.put("contractId","编号");
		header.put("blCampusName","合同校区");
		header.put("stuName","学生");
		header.put("stuBlCampusName","学生校区");
		header.put("contractGrade","年级");
		header.put("signTime","签约日期");
		header.put("oneOnOneTotalAmount","一对一收费");
		header.put("miniClassTotalAmount","小班收费");
		header.put("oneOnManyTotalAmount","一对多收费");
		header.put("promiseClassTotalAmount","精英班收费");
		header.put("lectureTotalAmount","讲座收费");
		header.put("twoTeacherTotalAmount","双师收费");
		header.put("otherTotalAmount","其他收费");
		header.put("totalAmount","总额");
		header.put("promotionAmount","优惠");
		header.put("paidAmount","已收款");
		header.put("pendingAmount","待缴费");
		header.put("availableAmount","待分配资金");
		header.put("contractTypeName","类型");
		header.put("contractStatusName","合同状态");
		header.put("paidStatusName","收款状态");
		header.put("signByWho","签单人");
		ExportExcelUtil.exportExcelByListVo("sheet1",header,datas,ossKey,ContractExcelVo.class);
	return ossKey;
	}
}

