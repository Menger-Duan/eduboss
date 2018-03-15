package com.eduboss.controller;

import com.eduboss.common.ProductType;
import com.eduboss.dao.MiniClassDao;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.StudentReturnFee;
import com.eduboss.domainVo.*;
import com.eduboss.dto.*;
import com.eduboss.service.ContractService;
import com.eduboss.service.ProductService;
import com.eduboss.service.SmallClassService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.service.StudentReturnService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping(value="/StudentReturnController")
@Controller
public class StudentReturnController {
	
	@Autowired
	private StudentReturnService studentReturnService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private SmallClassService smallClassService;

	@Autowired
	private ProductService productService;

	/**
	 * 返回学生退费记录
	 * @param gridRequest
	 * @param studentReturnvo
	 * @param modelVo
	 * @return
	 */
	@RequestMapping(value="/getStudentReturnList")
	@ResponseBody
	public DataPackageForJqGrid getStudentReturnList(GridRequest gridRequest, StudentReturnFeeVo studentReturnvo,ModelVo modelVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = studentReturnService.getStudentReturnList(studentReturnvo, dataPackage,modelVo);
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value = "/getReturnFeeToExcel")
	@ResponseBody
	public void getReturnFeeToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute StudentReturnFeeVo studentReturnFeeVo, @ModelAttribute ModelVo modelVo , HttpServletResponse response) throws IOException {

		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		dataPackage= studentReturnService.getStudentReturnList( studentReturnFeeVo, dataPackage, modelVo);
		ExportExcel<StudentReturnFeeExcelVo> ex = new ExportExcel<>();
		String[] hearders = new String[] {"退费时间","操作人", "退费校区", "退费学生","学生年级", "退费合同","退费产品","小班名称","小班科目","班级类型","班级老师","小班学管","实收退费","超额退费","退费总金额","退费原因"};//表头数组
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		OutputStream out = response.getOutputStream();
		Set<StudentReturnFeeExcelVo> datas = new LinkedHashSet<>();
//		Set<StudentReturnFeeExcelVo> datas = HibernateUtils.voCollectionMapping(dataPackage.getDatas(), StudentReturnFeeExcelVo.class);
		for (StudentReturnFeeVo s : (List<StudentReturnFeeVo>)dataPackage.getDatas()){
			StudentReturnFeeExcelVo studentReturnFeeExcelVo = HibernateUtils.voObjectMapping(s, StudentReturnFeeExcelVo.class);
			String contractProduct = s.getProductType();
			if (StringUtils.isNotEmpty(contractProduct)){
				if (ProductType.SMALL_CLASS.equals(ProductType.valueOf(contractProduct))){
					ContractProductVo contractProductVo = contractService.getContractProductById(s.getContractProductId());
					if (contractProductVo!=null){
						if (StringUtils.isNotEmpty(contractProductVo.getMiniClassId())){
							MiniClassVo miniClassVo = smallClassService.findMiniClassById(contractProductVo.getMiniClassId());
							if (miniClassVo!=null){
								if (StringUtils.isNotEmpty(contractProductVo.getProductId())){
									ProductVo productVo = productService.findProductById(contractProductVo.getProductId());
									studentReturnFeeExcelVo.setMiniClassName(miniClassVo.getName());
									studentReturnFeeExcelVo.setMiniClassSubject(miniClassVo.getSubjectName());
									if (productVo!=null){
										studentReturnFeeExcelVo.setMiniClassType(productVo.getClassTypeName());
									}
									studentReturnFeeExcelVo.setTeacher(miniClassVo.getTeacherName());
									studentReturnFeeExcelVo.setStudyManager(miniClassVo.getStudyManegerName());
								}
							}
						}
					}
				}
			}
			datas.add(studentReturnFeeExcelVo);
		}
		ex.exportExcel(hearders, datas, out);

		out.close();
	}

	
}
