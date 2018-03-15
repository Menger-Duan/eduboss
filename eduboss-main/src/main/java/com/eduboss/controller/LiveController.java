package com.eduboss.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.LiveCourseDetail;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.LiveCourseDetailService;
import com.eduboss.service.LivePaymentRecordService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.ExportExcelUtil;
import com.eduboss.utils.PropertiesUtils;



@Controller
@RequestMapping(value = "/LiveController")
public class LiveController { 

	@Autowired
	private LivePaymentRecordService livePaymentRecordService;
	
	@Autowired
	private LiveCourseDetailService liveCourseDetailService;
	
	private final static Logger log = Logger.getLogger(LiveController.class);
	
	/**
	 * 查询直播流水记录
	 *
	 * @return
	 */
	@RequestMapping(value = "/getLivePaymentRecordList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getLivePaymentRecordList(@ModelAttribute GridRequest gridRequest, @ModelAttribute LivePaymentRecordVo livePaymentRecordVo, @ModelAttribute TimeVo timeVo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage= livePaymentRecordService.getLivePaymentRecordList( dataPackage, livePaymentRecordVo, timeVo  );

		return new DataPackageForJqGrid(dataPackage) ;
	}
	
	/**
	 * 导出直播流水记录
	 *
	 * @return
	 */
	@RequestMapping(value = "/getLivePaymentRecordToExcel", method =  RequestMethod.GET)
	@ResponseBody
	public void getLivePaymentRecordToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute LivePaymentRecordVo livePaymentRecordVo, @ModelAttribute TimeVo timeVo, HttpServletRequest request, HttpServletResponse response) {
		long timeStart = System.currentTimeMillis();
		/*DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage= livePaymentRecordService.getLivePaymentRecordList( dataPackage, livePaymentRecordVo, timeVo  );
		List<LivePaymentRecordVo> vos = (List<LivePaymentRecordVo>) dataPackage.getDatas();
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String filename = "直播流水"+timeFormat.format(new Date())+".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			
			String[] headers = new String[] {"序号","日期","合同类型", "学生姓名",  "学生ID", "学生手机号","签单人姓名","签单人工号","签单人手机号",
					"合同校区", "类型", "总额", "分成总额", "校区业绩","个人业绩","订单号","课程名称","课程金额",};//表头数组
			String[] fields = new String[]  {"","paymentDate","contactTypeName", "studentName",  "studentId", "studentContact","userName","userEmployeeNo","userContact",
					"orderCampusName", "financeTypeName", "totalAmount", "paidAmount", "campusAchievement","userAchievement","orderNum","courseDetails.courseName","courseDetails.paidAmount"};
			ExportExcelUtil.exportExcelIncludeMerged("sheet1", headers, fields, vos, filename, LivePaymentRecordVo.class, new Class[] {LiveCourseDetail.class}, response.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("getLivePaymentRecordToExcel导出失败！");
			e.printStackTrace();
		}*/
		List lists =  livePaymentRecordService.getLivePaymentRecordToExcel( livePaymentRecordVo, timeVo  );
		
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String filename = "直播流水"+timeFormat.format(new Date())+".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			
			String[] headers = null;
			String[] fields = null;
			if(PropertiesUtils.getStringValue("institution")!=null && "advance".equals(PropertiesUtils.getStringValue("institution"))){
				headers = new String[] {"序号","日期","合同类型", "学生姓名",  "学生ID","签单人姓名","签单人工号","签单人手机号",
						"合同校区", "类型", "总额", "分成总额", "校区业绩","个人业绩","订单号","课程名称","课程金额",};//表头数组
				fields = new String[]  {"","paymentDate","contactTypeName", "studentName",  "studentId","userName","userEmployeeNo","userContact",
						"orderCampusName", "financeTypeName", "totalAmount", "paidAmount", "campusAchievement","userAchievement","orderNum","courseName","coursePaidAmount"};
			}else {
				headers = new String[] {"序号","日期","合同类型", "学生姓名",  "学生ID","签单人姓名","签单人工号","签单人手机号",
						"合同校区", "类型", "总额", "分成总额", "校区业绩","个人业绩","订单号","支付方式","支付参考号","课程名称","课程金额",};//表头数组
				fields = new String[]  {"","paymentDate","contactTypeName", "studentName",  "studentId","userName","userEmployeeNo","userContact",
						"orderCampusName", "financeTypeName", "totalAmount", "paidAmount", "campusAchievement","userAchievement","orderNum","payType","reqsn","courseName","coursePaidAmount"};
			}
			
			ExportExcel<Map> exporter = new ExportExcel<Map>();
			List<String> repeatFields = new ArrayList<>();
			repeatFields.add("courseName");
			repeatFields.add("coursePaidAmount");
			exporter.exportExcelFromMapMerge(headers, lists, response.getOutputStream(),fields,"id",repeatFields);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("getLivePaymentRecordToExcel导出失败！");
			e.printStackTrace();
		}
		long timeEnd = System.currentTimeMillis();
		System.out.println("getLivePaymentRecordToExcel-costtime:"+(timeEnd-timeStart)+"ms");
	}
	
	/**
	 * 查询直播流水记录
	 *
	 * @return
	 */
	@RequestMapping(value = "/getCourseDetailList", method =  RequestMethod.GET)
	@ResponseBody
	public List<LiveCourseDetail> getCourseDetailList(String id) {
		if(StringUtils.isBlank(id)) {
			throw new ApplicationException(ErrorCode.LIVE_PARAM_EMPTY);
		}
		List<LiveCourseDetail> vos = liveCourseDetailService.findListByPayRecordId(id);

		return vos ;
	}
}
