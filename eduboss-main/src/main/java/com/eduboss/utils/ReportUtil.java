package com.eduboss.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.eduboss.domainVo.OperatBonusAndClsHrInxVo;
import com.eduboss.domainVo.OperatStuClsCompInxVo;
import com.eduboss.report.ReportMergedBean;



/**
 * 报表
 * @author qin.jingkai
 *
 */
public class ReportUtil {
	
	
	public static  HSSFWorkbook  generateBonusAndClassHourReport(List<OperatBonusAndClsHrInxVo>  bonusAndClsHrInxVoList,List<OperatStuClsCompInxVo> StuClsCompInxVoList){ 
		HSSFWorkbook workbook=null;
		//第三行合并使用bean（横向合并）
		ReportMergedBean horiMergedBean_onOne=new ReportMergedBean("一对一",8,ReportMergedBean.MERGEDTYPE_HORIZONTAL);
		List <ReportMergedBean> horiMergedList=new ArrayList<ReportMergedBean>() ;
		ReportMergedBean horiMergedBean_smallClass=new ReportMergedBean("小班",5,ReportMergedBean.MERGEDTYPE_HORIZONTAL);
		ReportMergedBean horiMergedBean_promiClass=new ReportMergedBean("目标班",2,ReportMergedBean.MERGEDTYPE_HORIZONTAL);
		horiMergedList.add(horiMergedBean_onOne);
		horiMergedList.add(horiMergedBean_smallClass);
		horiMergedList.add(horiMergedBean_promiClass);
		
		List <ReportMergedBean> vertMergedList=new ArrayList<ReportMergedBean>() ;
		ReportMergedBean vertMB_name=new ReportMergedBean("姓名",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_persPreFund=new ReportMergedBean("个人总预收款",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_preFundTarg=new ReportMergedBean("预收目标",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_onOneOneReFund=new ReportMergedBean("一对一退费",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_smallClsReFund=new ReportMergedBean("小班退费",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_preFunRat=new ReportMergedBean("预收目标达成率",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_otoPreTarg=new ReportMergedBean("一对一预收目标",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_otoPreTargRat=new ReportMergedBean("一对一预收达成率",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_smClsReFuSubTime=new ReportMergedBean("小班退费科次",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_smClsSubTarg=new ReportMergedBean("小班科次目标",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_smClsSubRat=new ReportMergedBean("小班科次达成率",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_smClsPreFunTarg=new ReportMergedBean("小班预收目标",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_smClsPreFunRat=new ReportMergedBean("小班预收达成率",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_persClsPreFunTarg=new ReportMergedBean("目标班预收目标",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_persClsPreFunRat=new ReportMergedBean("目标班预收达成率",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		ReportMergedBean vertMB_transIntro=new ReportMergedBean("转介绍个数",2,ReportMergedBean.MERGEDTYPE_VERTICAL);
		//vertMergedList.add(vertMB_name);  姓名一列不加入
		vertMergedList.add(vertMB_persPreFund);
		vertMergedList.add(vertMB_preFundTarg);
		vertMergedList.add(vertMB_onOneOneReFund);
		vertMergedList.add(vertMB_smallClsReFund);
		vertMergedList.add(vertMB_preFunRat);
		vertMergedList.add(vertMB_otoPreTarg);
		vertMergedList.add(vertMB_otoPreTargRat);
		vertMergedList.add(vertMB_smClsReFuSubTime);
		vertMergedList.add(vertMB_smClsSubTarg);
		vertMergedList.add(vertMB_smClsSubRat);
		vertMergedList.add(vertMB_smClsPreFunTarg);
		vertMergedList.add(vertMB_smClsPreFunRat);
		vertMergedList.add(vertMB_persClsPreFunTarg);
		vertMergedList.add(vertMB_persClsPreFunRat);
		vertMergedList.add(vertMB_transIntro);
		
		//第四行标题
		String title_row4[]=new String[]{"实签预收额","签单人数","签单科次","实签课时","赠送课时","均单","赠送比例","人均科次","实签预收额","签单人数","签单科次","均单",	"人均科次","实签预收额","签单人数"};

		  try {  
		   // 创建新的Excel 工作簿  
			  workbook= new HSSFWorkbook();  
		   // 在Excel工作簿中建一工作表，其名为缺省值  
		   // 如要新建一名为"效益指标"的工作表，其语句为：  
		   // HSSFSheet sheet = workbook.createSheet("efficial");  
		   HSSFSheet sheet = workbook.createSheet();  
		   // 在索引0的位置创建行（最顶端的行）  
		   
		   //第一行
		   HSSFRow row1 = sheet.createRow((short) 0);  
		   // 在索引0的位置创建单元格（左上端）  
		   HSSFCell cell = row1.createCell((short) 0);  
		   // 定义单元格为字符串类型  
		   cell.setCellType(HSSFCell.CELL_TYPE_STRING);  
		   // 在单元格中输入一些内容  
		   HSSFRichTextString richTextString=new HSSFRichTextString("星火教育校区营运部月结表（学管组）");
		   cell.setCellValue(richTextString);
		   
		   //第二行 
		   HSSFRow row2 = sheet.createRow((short) 1);
		   HSSFCell  cell0_row_title_h2=row2.createCell((short) 0);
		   // 定义单元格为字符串类型  
		   cell0_row_title_h2.setCellType(HSSFCell.CELL_TYPE_STRING);  
		   // 在单元格中输入一些内容  
		   HSSFRichTextString richTextString_cell_h2=new HSSFRichTextString("学管组--业绩、课时");
		   cell.setCellValue(richTextString_cell_h2);
		   
		   
		 
		   //第三行 begin 
		   int cellIndex_row3=0;
		   HSSFRow row3 = sheet.createRow((short) 2);
		   HSSFCell  cell0_row3=row3.createCell((short) cellIndex_row3);
		   cell0_row3.setCellType(HSSFCell.CELL_TYPE_STRING);  
		   cell0_row3.setCellValue(new HSSFRichTextString("姓名"));
		   cellIndex_row3++;
			
		   for (ReportMergedBean mergedBean : horiMergedList) {
				for (int i = 0; i < mergedBean.getMergedCellNum(); i++) {
					HSSFCell cell_temp = row3.createCell((short) cellIndex_row3);
					cell_temp.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell_temp.setCellValue(new HSSFRichTextString(mergedBean.getText()));
					cellIndex_row3++;
				}
			}
		   
		   
		   for (ReportMergedBean mergedBean : vertMergedList) {
					HSSFCell cell_temp = row3.createCell((short) cellIndex_row3);
					cell_temp.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell_temp.setCellValue(new HSSFRichTextString(mergedBean.getText()));
					cellIndex_row3++;
			}
		 //第三行 end
		   
		   
		  //第四行 begin
		   int cellIndex_row4=0;
		   HSSFRow row4 = sheet.createRow((short) 3);
		   HSSFCell  cell0_row4=row4.createCell((short) cellIndex_row4);
		   cell0_row4.setCellType(HSSFCell.CELL_TYPE_STRING);  
		   cell0_row4.setCellValue(new HSSFRichTextString("姓名"));
		   cellIndex_row4++;
			
		   for (String title : title_row4) {
					HSSFCell cell_temp = row4.createCell((short) cellIndex_row4);
					cell_temp.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell_temp.setCellValue(new HSSFRichTextString(title));
					cellIndex_row4++;
			}
		  
		  int perRowcellNum_firstSheet= cellIndex_row4;//第一个表格单元格总数
		 //第四行 end
		   
		  
		   //写入第一个表格数据 begin
		  short rowIdex_bonClsHr=4;
		  for(OperatBonusAndClsHrInxVo bonusAndClsHrInxVo:bonusAndClsHrInxVoList){
			  writeRowForBonusAndClassHour(workbook,sheet, rowIdex_bonClsHr, bonusAndClsHrInxVo);
			  rowIdex_bonClsHr++;
		  }
		  
		  //写入第一个表格数据 end
		  //汇总begin
		  int  totalRowIndex_firstSheet=4+bonusAndClsHrInxVoList.size();
		  writeTotalRowForBonusAndClassHour(workbook,sheet, (short) totalRowIndex_firstSheet,(short)4,(short)( totalRowIndex_firstSheet-1));
		  //汇总end
		  
		  
		  //第二个表格表头
		  int rowIndex_secdSheetTh=totalRowIndex_firstSheet+1; //第二个表格头的行索引
		  HSSFRow rowTh_secdSheet = sheet.createRow((short) rowIndex_secdSheetTh);
		  HSSFCell  cell0_secdSheetTb =rowTh_secdSheet.createCell((short)0);
		  cell0_secdSheetTb.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_secdSheetTb.setCellValue(new HSSFRichTextString("学员、班级构成与服务"));
		  
		  
		  //第二个表格标题行
		  
		  String [] secdSheetTitleArr=new String[]{"新签学生",	"续费学生"	,"退费学生","停课学生","结课学生","总在读学生","停课学生比例","续生率	","月总转介绍人数","低于20H学生数","低于10H学生数","续读科次","新签科次","退费科次"	,"目标科次",
					"科次达成率","人均科次","总开班数","班均人数","新生数量","旧生数量","计划课时数","校区实际总课时","课时达成率","家长会次数","月均家长会次数","回访次数","月均回访次数","个辅数量","不合格个辅数量",		
					"学管满意度","集团D类电访个数","语文科学生数","数学科学生数","英语科学生数","物理科学生数","化学科学生数","政治科学生数","历史科学生数","地理科学生数",	"生物科学生数",	"其它科学生数",	
					"小一至五占比",	"小六占比","初一占比","初二占比","初三占比","高一占比","高二占比","高三占比",	"*学校占比","*学校占比","*学校占比"	,"*学校占比"																																																																																																																																																																																																																																											
		  };
		  
		  int rowIndex_secdSheetTitle=rowIndex_secdSheetTh+1;
		  HSSFRow rowTitle_secdSheet = sheet.createRow((short) rowIndex_secdSheetTitle);
		  HSSFCell  cell0_secdSheetTitle =rowTitle_secdSheet.createCell((short)1);
		  cell0_secdSheetTitle.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_secdSheetTitle.setCellValue(new HSSFRichTextString(""));
		  
		  
		  for (int i = 0; i < secdSheetTitleArr.length; i++) {
			String string = secdSheetTitleArr[i];
			 HSSFCell  tempCell =rowTitle_secdSheet.createCell((short)(i+1));
			 tempCell.setCellType(HSSFCell.CELL_TYPE_STRING);  
			 tempCell.setCellValue(new HSSFRichTextString(string));
		  }
		  
		  //
		  int rowIndex_secdSheet_oto=rowIndex_secdSheetTitle+1; //第二个表格头的行索引
		  HSSFRow row_secdSheet_oto = sheet.createRow((short) rowIndex_secdSheet_oto);
		  HSSFCell  cell0_row_secdSheetOto =row_secdSheet_oto.createCell((short)0);
		  cell0_row_secdSheetOto.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_row_secdSheetOto.setCellValue(new HSSFRichTextString("一对一"));
		  
		  
		  //
		  int rowIndex_secdSheet_smCls=rowIndex_secdSheet_oto+1; //第二个表格头的行索引
		  HSSFRow row_secdSheet_smCls = sheet.createRow((short) rowIndex_secdSheet_smCls);
		  HSSFCell  cell0_row_secdSheetSmCls =row_secdSheet_smCls.createCell((short)0);
		  cell0_row_secdSheetSmCls.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_row_secdSheetSmCls.setCellValue(new HSSFRichTextString("小班"));
		  
		  
		  //
		  int rowIndex_secdSheet_pmCls=rowIndex_secdSheet_smCls+1; //第二个表格头的行索引
		  HSSFRow pmCls = sheet.createRow((short) rowIndex_secdSheet_pmCls);
		  HSSFCell  cell0_row_secdSheetPmCls =pmCls.createCell((short)0);
		  cell0_row_secdSheetPmCls.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_row_secdSheetPmCls.setCellValue(new HSSFRichTextString("目标班"));
		  
		  int rowIndex_secdSheet_total=rowIndex_secdSheet_pmCls+1; //第二个表格头的行索引
		  HSSFRow row_secdSheet_total = sheet.createRow((short) rowIndex_secdSheet_total);
		  HSSFCell  cell0_row_secdSheet_total =row_secdSheet_total.createCell((short)0);
		  cell0_row_secdSheet_total.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_row_secdSheet_total.setCellValue(new HSSFRichTextString("校区/个人总"));
		  
		   //合并处理begin
		   //short merIndex_row1=
		   sheet.addMergedRegion(new Region(0,(short)0,0,(short) (cellIndex_row3-1)));     //第一行合并处理
		   sheet.addMergedRegion(new Region(1,(short)0,1,(short) (cellIndex_row3-1)));     //第二行合并处理
		   
		   short mercellIndex_row3_begin=1;
		   short mercellIndex_row3_end=1;
		   
		   //sheet.addMergedRegion(new Region(2,(short)1,2,(short)8));     //第三行合并处理
		   for (ReportMergedBean mergedBean : horiMergedList) {
			   
			   mercellIndex_row3_end=(short) (mercellIndex_row3_begin+mergedBean.getMergedCellNum()-1);
			   //System.out.println("mercellIndex_row3_begin==="+mercellIndex_row3_begin+"===mercellIndex_row3_end:"+mercellIndex_row3_end);
			   sheet.addMergedRegion(new Region(2,mercellIndex_row3_begin,2,mercellIndex_row3_end));     //第三行合并处理
			   mercellIndex_row3_begin=(short) (mercellIndex_row3_end+1);
			}
		   
		   
		   sheet.addMergedRegion(new Region(2,(short)0,3,(short) 0));     //第三行的第一个单元格和第四行 的 第一个单元格合并
		   
		   short mercellIndex_3to4ver=mercellIndex_row3_begin;
		   for (ReportMergedBean mergedBean : vertMergedList) {
			   
			   //System.out.println("mercellIndex_row3_begin==="+mercellIndex_row3_begin+"===mercellIndex_row3_end:"+mercellIndex_row3_end);
			   sheet.addMergedRegion(new Region(2,mercellIndex_3to4ver,3,mercellIndex_3to4ver));
			   mercellIndex_3to4ver++;
			}
		   
		   
		   
		   
		   //合并处理end
//		   Region r1= new Region(0,(short)0,0,(short)1);
//		   sheet.addMergedRegion(r1);     //同一行合并   ok
//		   
//		   Region r2= new Region(1,(short)0,2,(short)0);  //纵向合并  ok
//		   sheet.addMergedRegion(r2);
		   
		  // cell.setCellValue("a");
		   // 新建一输出文件流  
		   //FileOutputStream fOut = new FileOutputStream(filePath);  
		   // 把相应的Excel 工作簿存盘  
		  // workbook.write(fOut);  
		  // fOut.flush();  
		   // 操作结束，关闭文件  
		   //fOut.close();  
		   System.out.println("文件生成...");  
		  } catch (Exception e) {  
			 e.printStackTrace();
		   System.out.println("已运行 xlCreate() : " + e);  
		  }  
		  return workbook;
		
	
		
	
	}
	
	/**
	 * BonusAndClassHour  写入行  营运部月结表（学管组）  业绩、课时
	 * @return
	 */
	public static boolean writeRowForBonusAndClassHour(HSSFWorkbook workbook, HSSFSheet sheet ,short rowIdex,OperatBonusAndClsHrInxVo bonusAndClsHrInxVo){
		   
		  short nextRowIndex=(short) (rowIdex+1); //下一行的行索引
		  
		  
		  //样式和格式定义begin
		  short doubleFormat = HSSFDataFormat.getBuiltinFormat("0.00"); 
		   HSSFCellStyle doubleCellStyle = workbook.createCellStyle(); 
		   doubleCellStyle.setDataFormat(doubleFormat);
		   
		   short intFormat = HSSFDataFormat.getBuiltinFormat("0"); 
		   HSSFCellStyle intCellStyle = workbook.createCellStyle(); 
		   intCellStyle.setDataFormat(intFormat); 
		  //end
		
		   HSSFRow row = sheet.createRow(rowIdex);
		   //学生姓名
		   HSSFCell cell=row.createCell((short) 0);
		   setCellValueAndTypeForString(cell,bonusAndClsHrInxVo.getName());
		   
		   //实签预收额
		   HSSFCell cell_oooRealSignPreFundAmt=row.createCell((short) 1);
		   //setCellValueAndTypeForString(cell_oooRealSignPreFundAmt,bonusAndClsHrInxVo.getOooRealSignPreFundAmt());
		   setCellValueAndTypeForNumeric(cell_oooRealSignPreFundAmt, bonusAndClsHrInxVo.getOooRealSignPreFundAmt().doubleValue(), doubleCellStyle,null);
		   
		   // 签单人数(一对一) oooSignNum
		   HSSFCell cell_oooSignNum=row.createCell((short) 2);
		   //setCellValueAndTypeForString(cell_oooSignNum,bonusAndClsHrInxVo.getOooSignNum());
		   setCellValueAndTypeForNumeric(cell_oooSignNum, bonusAndClsHrInxVo.getOooSignNum().intValue(), intCellStyle,null);
		   //签单科次 oooSignSubNum 
		   HSSFCell cell_oooSignSubNum=row.createCell((short) 3);
		   //setCellValueAndTypeForString(cell_oooSignSubNum,bonusAndClsHrInxVo.getOooSignSubNum());
		   setCellValueAndTypeForNumeric(cell_oooSignSubNum, bonusAndClsHrInxVo.getOooSignSubNum().intValue(), intCellStyle,null);
		   //实签课时  实签课时 
		   HSSFCell cell_oooRealSignHr=row.createCell((short) 4);
		   //setCellValueAndTypeForString(cell_oooRealSignHr,bonusAndClsHrInxVo.getOooRealSignHr());
		   setCellValueAndTypeForNumeric(cell_oooRealSignHr, bonusAndClsHrInxVo.getOooRealSignHr().intValue(), intCellStyle,null);
		   // 赠送课时
		   HSSFCell cell_oooFreeSignHr=row.createCell((short) 5);
		   //setCellValueAndTypeForString(cell_oooFreeSignHr,bonusAndClsHrInxVo.getOooFreeSignHr());
		   setCellValueAndTypeForNumeric(cell_oooFreeSignHr, bonusAndClsHrInxVo.getOooFreeSignHr().doubleValue(), doubleCellStyle,null);
		   //均单
		   HSSFCell cell_oooAvgSign=row.createCell((short) 6);
		   String formula_oooAvgSign="B"+nextRowIndex+"/"+"C"+nextRowIndex;  //均单公式
		   setCellValueAndTypeForNumeric(cell_oooAvgSign,null,doubleCellStyle,formula_oooAvgSign);
		   //赠送比例
		   HSSFCell cell_oooFreeRat=row.createCell((short) 7);
		   String formula_oooFreeRat="F"+nextRowIndex+"/"+"E"+nextRowIndex;  //均单公式
		   setCellValueAndTypeForNumeric(cell_oooFreeRat,null,doubleCellStyle,formula_oooFreeRat);
		   //人均科次( 一对一) =D5/C5
		   HSSFCell cell_oooAvgSubNum=row.createCell((short) 8);
		   String formula_oooAvgSubNum="D"+nextRowIndex+"/"+"C"+nextRowIndex;  //均单公式
		   setCellValueAndTypeForNumeric(cell_oooAvgSubNum,null,doubleCellStyle,formula_oooAvgSubNum);
		   //实签预收额(小班) smclsRealSignPreFundAmt
		   HSSFCell cell_smclsRealSignPreFundAmt=row.createCell((short) 9);
		   //setCellValueAndTypeForString(cell_smclsRealSignPreFundAmt,bonusAndClsHrInxVo.getSmclsRealSignPreFundAmt());
		   setCellValueAndTypeForNumeric(cell_smclsRealSignPreFundAmt, bonusAndClsHrInxVo.getSmclsRealSignPreFundAmt().doubleValue(), doubleCellStyle,null);
		   //签单人数(小班)  smclsSignNum
		   HSSFCell cell_smclsSignNum=row.createCell((short) 10);
		   //setCellValueAndTypeForString(cell_smclsSignNum,bonusAndClsHrInxVo.getSmclsSignNum());
		   setCellValueAndTypeForNumeric(cell_smclsSignNum, bonusAndClsHrInxVo.getSmclsSignNum().intValue(), intCellStyle,null);
		   //签单科次  smclsSignSubNum
		   HSSFCell cell_smclsSignSubNum=row.createCell((short) 11);
		   //setCellValueAndTypeForString(cell_smclsSignSubNum,bonusAndClsHrInxVo.getSmclsSignSubNum());
		   setCellValueAndTypeForNumeric(cell_smclsSignSubNum, bonusAndClsHrInxVo.getSmclsSignSubNum().intValue(), intCellStyle,null);
		   //smclsAvgSign  均单(小班) J5/K5
		   HSSFCell cell_smclsAvgSign=row.createCell((short) 12);
		   String formula_smclsAvgSign="J"+nextRowIndex+"/"+"K"+nextRowIndex;  //均单公式
		   setCellValueAndTypeForNumeric(cell_smclsAvgSign,null,doubleCellStyle,formula_smclsAvgSign);
		   //setCellDefTypeAndFormula(cell_smclsAvgSign,formula_smclsAvgSign);
		   //smclsAvgSubNum 人均科次  L5/K5
		   HSSFCell cell_smclsAvgSubNum=row.createCell((short) 13);
		   String formula_smclsAvgSubNum="L"+nextRowIndex+"/"+"K"+nextRowIndex;  
		   setCellValueAndTypeForNumeric(cell_smclsAvgSubNum,null,doubleCellStyle,formula_smclsAvgSubNum);
		   //permclsRealSignPreFundAmt
		   HSSFCell cell_permclsRealSignPreFundAmt=row.createCell((short) 14);
		   //setCellValueAndTypeForString(cell_permclsRealSignPreFundAmt,bonusAndClsHrInxVo.getPermclsRealSignPreFundAmt());
		   setCellValueAndTypeForNumeric(cell_permclsRealSignPreFundAmt, bonusAndClsHrInxVo.getPermclsRealSignPreFundAmt().doubleValue(), doubleCellStyle,null);
		   //permclsSignNum
		   HSSFCell cell_permclsSignNum=row.createCell((short) 15);
		   //setCellValueAndTypeForString(cell_permclsSignNum,bonusAndClsHrInxVo.getPermclsSignNum());
		   setCellValueAndTypeForNumeric(cell_permclsSignNum, bonusAndClsHrInxVo.getPermclsSignNum().intValue(), intCellStyle,null);
		   //preFundTotal
		   HSSFCell cell_preFundTotal=row.createCell((short) 16);
		   setCellValueAndTypeForString(cell_preFundTotal,bonusAndClsHrInxVo.getPreFundTotal());
		   //preFundTarg
//		   HSSFCell cell_preFundTarg=row.createCell((short) 17);
//		   setCellValueAndTypeForString(cell_preFundTarg,bonusAndClsHrInxVo.getPreFundTarg());
//		   //preFundTarg
//		   HSSFCell cell_oooReFund=row.createCell((short) 18);
//		   setCellValueAndTypeForString(cell_oooReFund,bonusAndClsHrInxVo.getOooReFund());
//		  //smReFund
//		   HSSFCell cell_smReFund=row.createCell((short) 19);
//		   setCellValueAndTypeForString(cell_smReFund,bonusAndClsHrInxVo.getSmReFund());
//		  //preFundTargRat (Q6-S6-T6)/R6
//		   HSSFCell cell_preFundTargRat=row.createCell((short) 20);
//		   String formula_preFundTargRat="(Q"+nextRowIndex+"-S"+nextRowIndex+"-T"+nextRowIndex+")/"+"R"+nextRowIndex; 
//		   setCellDefTypeAndFormula(cell_preFundTargRat,formula_preFundTargRat);
		   //oooPreFundTarg
//		   HSSFCell cell_oooPreFundTarg=row.createCell((short) 21);
//		   setCellValueAndTypeForString(cell_oooPreFundTarg,bonusAndClsHrInxVo.getOooPreFundTarg());
//		   //oooPreFundRat
//		   HSSFCell cell_oooPreFundRat=row.createCell((short) 22);
//		   setCellValueAndTypeForString(cell_oooPreFundRat,bonusAndClsHrInxVo.getOooPreFundRat());
//		   //smclsReFundSubNum
//		   HSSFCell cell_smclsReFundSubNum=row.createCell((short) 23);
//		   setCellValueAndTypeForString(cell_smclsReFundSubNum,bonusAndClsHrInxVo.getSmclsReFundSubNum());
//		   //smClsSubTarg
//		   HSSFCell cell_smClsSubTarg=row.createCell((short) 24);
//		   setCellValueAndTypeForString(cell_smClsSubTarg,bonusAndClsHrInxVo.getSmClsSubTarg());
//		   //smclsSubNumRat
//		   HSSFCell cell_smclsSubNumRat=row.createCell((short) 25);
//		   setCellValueAndTypeForString(cell_smclsSubNumRat,bonusAndClsHrInxVo.getSmclsSubNumRat());
//		   //smclsPreFundTarg
//		   HSSFCell cell_smclsPreFundTarg=row.createCell((short) 26);
//		   setCellValueAndTypeForString(cell_smclsPreFundTarg,bonusAndClsHrInxVo.getSmclsPreFundTarg());
//		   //smclsPreFundRat
//		   HSSFCell cell_smclsPreFundRat=row.createCell((short) 27);
//		   setCellValueAndTypeForString(cell_smclsPreFundRat,bonusAndClsHrInxVo.getSmclsPreFundRat());
//		   //permclsPreFundTarg
//		   HSSFCell cell_permclsPreFundTarg=row.createCell((short) 28);
//		   setCellValueAndTypeForString(cell_permclsPreFundTarg,bonusAndClsHrInxVo.getPermclsPreFundTarg());
//		   //permclsPreFundTargRat
//		   HSSFCell cell_permclsPreFundTargRat=row.createCell((short) 29);
//		   setCellValueAndTypeForString(cell_permclsPreFundTargRat,bonusAndClsHrInxVo.getPermclsPreFundTargRat());
//		 //transIntrod
//		   HSSFCell cell_transIntrod=row.createCell((short) 30);
//		   setCellValueAndTypeForString(cell_transIntrod,bonusAndClsHrInxVo.getTransIntrod());
		   
		return true;
	}
	
	
	/**
	 *  写入汇总行 
	 * @param sheet
	 * @param rowIdex
	 * @param beginDataRowInx  数据行索引（第一行）,传人值位poi api中的下标，即从0开始 比电子表格中的索引少1
	 * @param endDataRowInx    数据行索引(最后一行)
	 * @return
	 */

	private static boolean  writeTotalRowForBonusAndClassHour(HSSFWorkbook workbook,HSSFSheet sheet ,short rowIdex,short beginDataRowInx,short endDataRowInx){
		  short beginDataRowInx_excel=(short) (beginDataRowInx+1);
		  short endDataRowInx_excel=(short) (endDataRowInx+1);
		
		  HSSFRow row = sheet.createRow((short)rowIdex);
		  HSSFCell  cell0_totalRow =row.createCell((short)0);
		  cell0_totalRow.setCellType(HSSFCell.CELL_TYPE_STRING);  
		  cell0_totalRow.setCellValue(new HSSFRichTextString("汇总"));
		  

		  //样式和格式定义begin
		  short doubleFormat = HSSFDataFormat.getBuiltinFormat("0.00"); 
		   HSSFCellStyle doubleCellStyle = workbook.createCellStyle(); 
		   doubleCellStyle.setDataFormat(doubleFormat);
		   
		   short intFormat = HSSFDataFormat.getBuiltinFormat("0"); 
		   HSSFCellStyle intCellStyle = workbook.createCellStyle(); 
		   intCellStyle.setDataFormat(intFormat); 
		  //end
		  
		   
		  short nextRowIndex=(short) (rowIdex+1); //下一行的行索引
		
		   //实签预收额 SUM(B5:B9)
		   HSSFCell cell_oooRealSignPreFundAmt=row.createCell((short) 1);
		   String formula_oooRealSignPreFundAmt="SUM(B"+beginDataRowInx_excel+":"+"B"+endDataRowInx_excel+")";
		   //setCellDefTypeAndFormula(cell_oooRealSignPreFundAmt,formula_oooRealSignPreFundAmt);
		   setCellValueAndTypeForNumeric(cell_oooRealSignPreFundAmt, null, doubleCellStyle, formula_oooRealSignPreFundAmt);
		   
     	   // 签单人数(一对一) oooSignNum
		   HSSFCell cell_oooSignNum=row.createCell((short) 2);
		   String formula_oooSignNum="SUM(C"+beginDataRowInx_excel+":"+"C"+endDataRowInx_excel+")";
		   setCellValueAndTypeForNumeric(cell_oooSignNum, null, intCellStyle, formula_oooSignNum);
		   
		   //签单科次 oooSignSubNum 
		   HSSFCell cell_oooSignSubNum=row.createCell((short) 3);
		   String formula_oooSignSubNum="SUM(D"+beginDataRowInx_excel+":"+"D"+endDataRowInx_excel+")";
		   setCellValueAndTypeForNumeric(cell_oooSignSubNum, null, intCellStyle, formula_oooSignSubNum);
		   
		   //实签课时  实签课时 
		   HSSFCell cell_oooRealSignHr=row.createCell((short) 4);
		   String formula_oooRealSignHr="SUM(E"+beginDataRowInx_excel+":"+"E"+endDataRowInx_excel+")";
		   setCellValueAndTypeForNumeric(cell_oooRealSignHr, null, intCellStyle, formula_oooRealSignHr);
		   
		   // 赠送课时
		   HSSFCell cell_oooFreeSignHr=row.createCell((short) 5);
		   String formula_oooFreeSignHr="SUM(F"+beginDataRowInx_excel+":"+"F"+endDataRowInx_excel+")";
		   setCellValueAndTypeForNumeric(cell_oooFreeSignHr, null, intCellStyle, formula_oooFreeSignHr);
		   
		   //均单
		   HSSFCell cell_oooAvgSign=row.createCell((short) 6);
		   String formula_oooAvgSign="B"+nextRowIndex+"/"+"C"+nextRowIndex;
		   setCellValueAndTypeForNumeric(cell_oooAvgSign, null, doubleCellStyle, formula_oooAvgSign);
		   //赠送比例
		   HSSFCell cell_oooFreeRat=row.createCell((short) 7);
		   String formula_oooFreeRat="F"+nextRowIndex+"/"+"E"+nextRowIndex;  //均单公式
		   setCellValueAndTypeForNumeric(cell_oooFreeRat, null, doubleCellStyle, formula_oooFreeRat);
	       //人均科次( 一对一) =D5/C5
		   HSSFCell cell_oooAvgSubNum=row.createCell((short) 8);
           String formula_oooAvgSubNum="D"+nextRowIndex+"/"+"C"+nextRowIndex;  //均单公式
	       setCellValueAndTypeForNumeric(cell_oooAvgSubNum, null, doubleCellStyle, formula_oooAvgSubNum);
		   
		   
//		   //实签预收额(小班) smclsRealSignPreFundAmt
	  		HSSFCell cell_smclsRealSignPreFundAmt=row.createCell((short) 9);
	        String formula_smclsRealSignPreFundAmt="SUM(J"+beginDataRowInx_excel+":"+"J"+endDataRowInx_excel+")";
	  	    setCellValueAndTypeForNumeric(cell_smclsRealSignPreFundAmt, null, doubleCellStyle, formula_smclsRealSignPreFundAmt);
	       
	       
//		   //签单人数(小班)  smclsSignNum
			HSSFCell cell_smclsSignNum=row.createCell((short) 10);
	        String formula_smclsSignNum="SUM(K"+beginDataRowInx_excel+":"+"K"+endDataRowInx_excel+")";
	  	    setCellValueAndTypeForNumeric(cell_smclsSignNum, null, intCellStyle, formula_smclsSignNum);
	  	    
	  	    
//		   //签单科次  smclsSignSubNum
	  	    HSSFCell cell_smclsSignSubNum=row.createCell((short) 11);
	        String formula_smclsSignSubNum="SUM(L"+beginDataRowInx_excel+":"+"L"+endDataRowInx_excel+")";
	  	    setCellValueAndTypeForNumeric(cell_smclsSignSubNum, null, intCellStyle, formula_smclsSignSubNum);
	  	    
	  	    
//		   //smclsAvgSign  均单(小班) J5/K5
	  	   HSSFCell cell_smclsAvgSign=row.createCell((short) 12);
	  	   String formula_smclsAvgSign="J"+nextRowIndex+"/"+"K"+nextRowIndex;  //均单公式
		   setCellValueAndTypeForNumeric(cell_smclsAvgSign, null, doubleCellStyle, formula_smclsAvgSign); 
	  	    
	  	    
//		   //smclsAvgSubNum 人均科次  L5/K5
		   HSSFCell cell_smclsAvgSubNum=row.createCell((short) 13);
		   String formula_smclsAvgSubNum="L"+nextRowIndex+"/"+"K"+nextRowIndex;  
		   setCellValueAndTypeForNumeric(cell_smclsAvgSubNum, null, doubleCellStyle, formula_smclsAvgSubNum); 
		   
			HSSFCell cell_permclsRealSignPreFundAmt=row.createCell((short) 14);
	        String formula_permclsRealSignPreFundAmt="SUM(O"+beginDataRowInx_excel+":"+"O"+endDataRowInx_excel+")";
	  	    setCellValueAndTypeForNumeric(cell_permclsRealSignPreFundAmt, null, doubleCellStyle, formula_permclsRealSignPreFundAmt);
//		   //permclsSignNum
//		   HSSFCell cell_permclsSignNum=row.createCell((short) 15);
//		   setCellValueAndTypeForString(cell_permclsSignNum,bonusAndClsHrInxVo.getPermclsSignNum());
	  	   HSSFCell cell_permclsSignNum=row.createCell((short) 15);
	       String formula_permclsSignNum="SUM(P"+beginDataRowInx_excel+":"+"P"+endDataRowInx_excel+")";
	  	   setCellValueAndTypeForNumeric(cell_permclsSignNum, null, intCellStyle, formula_permclsSignNum);
	  	    
	  	    
//		   //preFundTotal
//		   HSSFCell cell_preFundTotal=row.createCell((short) 16);
//		   setCellValueAndTypeForString(cell_preFundTotal,bonusAndClsHrInxVo.getPreFundTotal());
//		   //preFundTarg
//		   HSSFCell cell_preFundTarg=row.createCell((short) 17);
//		   setCellValueAndTypeForString(cell_preFundTarg,bonusAndClsHrInxVo.getPreFundTarg());
//		   //preFundTarg
//		   HSSFCell cell_oooReFund=row.createCell((short) 18);
//		   setCellValueAndTypeForString(cell_oooReFund,bonusAndClsHrInxVo.getOooReFund());
//		  //smReFund
//		   HSSFCell cell_smReFund=row.createCell((short) 19);
//		   setCellValueAndTypeForString(cell_smReFund,bonusAndClsHrInxVo.getSmReFund());
//		  //preFundTargRat (Q6-S6-T6)/R6
//		   HSSFCell cell_preFundTargRat=row.createCell((short) 20);
//		   String formula_preFundTargRat="(Q"+nextRowIndex+"-S"+nextRowIndex+"-T"+nextRowIndex+")/"+"R"+nextRowIndex; 
//		   setCellDefTypeAndFormula(cell_preFundTargRat,formula_preFundTargRat);
//		   //oooPreFundTarg
//		   HSSFCell cell_oooPreFundTarg=row.createCell((short) 21);
//		   setCellValueAndTypeForString(cell_oooPreFundTarg,bonusAndClsHrInxVo.getOooPreFundTarg());
//		   //oooPreFundRat
//		   HSSFCell cell_oooPreFundRat=row.createCell((short) 22);
//		   setCellValueAndTypeForString(cell_oooPreFundRat,bonusAndClsHrInxVo.getOooPreFundRat());
//		   //smclsReFundSubNum
//		   HSSFCell cell_smclsReFundSubNum=row.createCell((short) 23);
//		   setCellValueAndTypeForString(cell_smclsReFundSubNum,bonusAndClsHrInxVo.getSmclsReFundSubNum());
//		   //smClsSubTarg
//		   HSSFCell cell_smClsSubTarg=row.createCell((short) 24);
//		   setCellValueAndTypeForString(cell_smClsSubTarg,bonusAndClsHrInxVo.getSmClsSubTarg());
//		   //smclsSubNumRat
//		   HSSFCell cell_smclsSubNumRat=row.createCell((short) 25);
//		   setCellValueAndTypeForString(cell_smclsSubNumRat,bonusAndClsHrInxVo.getSmclsSubNumRat());
//		   //smclsPreFundTarg
//		   HSSFCell cell_smclsPreFundTarg=row.createCell((short) 26);
//		   setCellValueAndTypeForString(cell_smclsPreFundTarg,bonusAndClsHrInxVo.getSmclsPreFundTarg());
//		   //smclsPreFundRat
//		   HSSFCell cell_smclsPreFundRat=row.createCell((short) 27);
//		   setCellValueAndTypeForString(cell_smclsPreFundRat,bonusAndClsHrInxVo.getSmclsPreFundRat());
//		   //permclsPreFundTarg
//		   HSSFCell cell_permclsPreFundTarg=row.createCell((short) 28);
//		   setCellValueAndTypeForString(cell_permclsPreFundTarg,bonusAndClsHrInxVo.getPermclsPreFundTarg());
//		   //permclsPreFundTargRat
//		   HSSFCell cell_permclsPreFundTargRat=row.createCell((short) 29);
//		   setCellValueAndTypeForString(cell_permclsPreFundTargRat,bonusAndClsHrInxVo.getPermclsPreFundTargRat());
//		 //transIntrod
//		   HSSFCell cell_transIntrod=row.createCell((short) 30);
//		   setCellValueAndTypeForString(cell_transIntrod,bonusAndClsHrInxVo.getTransIntrod());
		   
		return true;
	
	}
	
	
	
	/**
	 *  写入行  营运部月结表（学管组）  学员、班级构成与服务
	 * @param sheet
	 * @param firsCellTxt
	 * @param rowIdex
	 * @param stuClsCompInxVo
	 * @return
	 */
	public static boolean writeRowForStuClsComp(HSSFWorkbook workbook,HSSFSheet sheet ,String firstCellTxt,short rowIdex,OperatStuClsCompInxVo stuClsCompInxVo){
		   HSSFRow row = sheet.createRow(rowIdex);
		   //学生姓名
		   HSSFCell cell=row.createCell((short) 0);
		   setCellValueAndTypeForString(cell,firstCellTxt);
		   
		   //newSignStuNum
		   HSSFCell cell_oooRealSignPreFundAmt=row.createCell((short) 1);
		   setCellValueAndTypeForString(cell_oooRealSignPreFundAmt,stuClsCompInxVo.getNewSignStuNum());
		   //renewSignStuNum
		   HSSFCell cell_renewSignStuNum=row.createCell((short) 2);
		   setCellValueAndTypeForString(cell_renewSignStuNum,stuClsCompInxVo.getRenewSignStuNum());
		   
		   //refundStuNum
		   HSSFCell cell_refundStuNum=row.createCell((short) 3);
		   setCellValueAndTypeForString(cell_refundStuNum,stuClsCompInxVo.getRefundStuNum());
		   //suspendStuNum
		   HSSFCell cell_suspendStuNum=row.createCell((short) 4);
		   setCellValueAndTypeForString(cell_suspendStuNum,stuClsCompInxVo.getSuspendStuNum());
		   
		  //endClsStuNum
		   HSSFCell cell_endClsStuNum=row.createCell((short) 5);
		   setCellValueAndTypeForString(cell_endClsStuNum,stuClsCompInxVo.getEndClsStuNum());
		   
		   //inSchoolStuNum
		   HSSFCell cell_inSchoolStuNum=row.createCell((short) 6);
		   setCellValueAndTypeForString(cell_inSchoolStuNum,stuClsCompInxVo.getInSchoolStuNum());
		   
		   //suspendStuRat
		   HSSFCell cell_suspendStuRat=row.createCell((short) 7);
		   setCellValueAndTypeForString(cell_suspendStuRat,stuClsCompInxVo.getSuspendStuRat());
		   
		   //renewStuRat
		   HSSFCell cell_renewStuRat=row.createCell((short) 8);
		   setCellValueAndTypeForString(cell_renewStuRat,stuClsCompInxVo.getRenewStuRat());
		   
		   //transIntrodMonthTotal
		   HSSFCell cell_transIntrodMonthTotal=row.createCell((short) 9);
		   setCellValueAndTypeForString(cell_transIntrodMonthTotal,stuClsCompInxVo.getTransIntrodMonthTotal());
		   
		   //lowerTwetHStuNum
		   HSSFCell cell_lowerTwetHStuNum=row.createCell((short) 10);
		   setCellValueAndTypeForString(cell_lowerTwetHStuNum,stuClsCompInxVo.getLowerTwetHStuNum());
		   
		   //lowerTenHStuNum
		   HSSFCell cell_lowerTenHStuNum=row.createCell((short) 11);
		   setCellValueAndTypeForString(cell_lowerTenHStuNum,stuClsCompInxVo.getLowerTenHStuNum());
		   
		   //renewSubNum
		   HSSFCell cell_renewSubNum=row.createCell((short) 12);
		   setCellValueAndTypeForString(cell_renewSubNum,stuClsCompInxVo.getRenewSubNum());
		   
		   //newSignSubNum
		   HSSFCell cell_newSignSubNum=row.createCell((short) 13);
		   setCellValueAndTypeForString(cell_newSignSubNum,stuClsCompInxVo.getNewSignSubNum());
		   
		   
		   //newSignSubNum
		   HSSFCell cell_refundSubNum=row.createCell((short) 14);
		   setCellValueAndTypeForString(cell_refundSubNum,stuClsCompInxVo.getRefundSubNum());
		   
		   //targSubNum
		   HSSFCell cell_targSubNum=row.createCell((short) 15);
		   setCellValueAndTypeForString(cell_targSubNum,stuClsCompInxVo.getTargSubNum());
		   //subReachRat
		   HSSFCell cell_subReachRat=row.createCell((short) 16);
		   setCellValueAndTypeForString(cell_subReachRat,stuClsCompInxVo.getSubReachRat());
		   
		   //avgSubNum
		   HSSFCell cell_avgSubNum=row.createCell((short) 17);
		   setCellValueAndTypeForString(cell_avgSubNum,stuClsCompInxVo.getAvgSubNum());
		   
		   //openClsTotal
		   HSSFCell cell_openClsTotal=row.createCell((short) 18);
		   setCellValueAndTypeForString(cell_openClsTotal,stuClsCompInxVo.getOpenClsTotal());
		   
		   //clsAvgNum
		   HSSFCell cell_clsAvgNum=row.createCell((short) 19);
		   setCellValueAndTypeForString(cell_clsAvgNum,stuClsCompInxVo.getClsAvgNum());
		   
		   //newStuNum
		   HSSFCell cell_newStuNum=row.createCell((short) 20);
		   setCellValueAndTypeForString(cell_newStuNum,stuClsCompInxVo.getNewStuNum());
		   
		   //newStuNum
		   HSSFCell cell_oldStuNum=row.createCell((short) 21);
		   setCellValueAndTypeForString(cell_oldStuNum,stuClsCompInxVo.getOldStuNum());
		   
		return true;
	}
	
	
	
	
	
	
	/**
	 * 设置单元格的值
	 * @param cell
	 * @param value  要设置的值
	 * @param type   单元格格式
	 */
	public static void setCellValueAndTypeForString(HSSFCell cell,Object value){
		//cell.setCellValue(new HSSFRichTextString(value.toString()));
		//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		setCellValueAndType(cell,value,HSSFCell.CELL_TYPE_STRING);
	}
	
	
	/**
	 * 设置单元格的值
	 * @param cell
	 * @param value  要设置的值
	 * @param type   单元格格式 NUMERIC
	 */
	public static void setCellValueAndTypeForNumeric(HSSFCell cell,Number value,HSSFCellStyle cellStyle,String formula){
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
		
		if(formula!=null){
			 cell.setCellFormula(formula);
		}
		
		if(value==null){
			return;
		}
		
		if(value instanceof Integer){
			cell.setCellValue(value.intValue());
		}else{
			cell.setCellValue(value.doubleValue());
		}
	}
	
	
	/**
	 * 设置单元格的值
	 * @param cell
	 * @param value  要设置的值
	 * @param type   单元格格式
	 */
	public static void setCellValueAndType(HSSFCell cell,Object value,int cellType){
		cell.setCellValue(new HSSFRichTextString(value.toString()));
		cell.setCellType(cellType);
	}
	
	
	/**
	 * 设置单元格为字符串类型，并为其设置公式
	 * @param cell
	 * @param formula
	 */
	public static void setCellDefTypeAndFormula(HSSFCell cell,String formula){
		   cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		   cell.setCellFormula(formula);
	}
	
	
	public static Object setColumnValue(Object input,String clsName){
		if(input==null){
			if(clsName.contains("String")){
				return "";
			}
			
			if(clsName.contains("BigDecimal")){
				return new BigDecimal("0");
			}
			
			if(clsName.contains("Integer")){
				return new Integer("0");
			}
			
			if(clsName.contains("Double")){
				return new Double("0");
			}
			
		}else{
			return input;
		}
		return input;
	}
	
	
	
	public static BigDecimal getFormatDecimal(BigDecimal  inputData){
		inputData=inputData.setScale(2, BigDecimal.ROUND_HALF_UP);
		return inputData;
	}
	
	
	
	public static void main(String[] args) {
		List<OperatBonusAndClsHrInxVo> bonusAndClsHrInxVoList = new ArrayList<OperatBonusAndClsHrInxVo>();
		
		OperatBonusAndClsHrInxVo bonusAndClsHrInxVo=new OperatBonusAndClsHrInxVo();
		bonusAndClsHrInxVo.setName("stu001");
		bonusAndClsHrInxVo.setOooRealSignPreFundAmt(new BigDecimal("150"));
		bonusAndClsHrInxVo.setOooSignNum(5);
		bonusAndClsHrInxVo.setOooSignSubNum(100);
		bonusAndClsHrInxVo.setOooRealSignHr(100);
		bonusAndClsHrInxVo.setOooFreeSignHr(new BigDecimal("20"));
		bonusAndClsHrInxVo.setSmclsRealSignPreFundAmt(new BigDecimal("200"));
		bonusAndClsHrInxVo.setSmclsSignNum(10);
		bonusAndClsHrInxVo.setSmclsSignSubNum(10);
		bonusAndClsHrInxVo.setPermclsRealSignPreFundAmt(new BigDecimal("1000"));
		bonusAndClsHrInxVo.setPermclsSignNum(20);
		bonusAndClsHrInxVo.setPreFundTotal(new BigDecimal(800));
		bonusAndClsHrInxVo.setPreFundTarg(new BigDecimal(10));
		bonusAndClsHrInxVo.setOooReFund(new BigDecimal(50));
		bonusAndClsHrInxVo.setSmReFund(new BigDecimal(150));
		bonusAndClsHrInxVoList.add(bonusAndClsHrInxVo);
		
		
		OperatBonusAndClsHrInxVo bonusAndClsHrInxV2=new OperatBonusAndClsHrInxVo();
		bonusAndClsHrInxV2.setName("stu002");
		bonusAndClsHrInxV2.setOooRealSignPreFundAmt(new BigDecimal("150"));
		bonusAndClsHrInxV2.setOooSignNum(5);
		bonusAndClsHrInxV2.setOooSignSubNum(100);
		bonusAndClsHrInxV2.setOooRealSignHr(100);
		bonusAndClsHrInxV2.setOooFreeSignHr(new BigDecimal("20"));
		bonusAndClsHrInxV2.setSmclsRealSignPreFundAmt(new BigDecimal("200"));
		bonusAndClsHrInxV2.setSmclsSignNum(10);
		bonusAndClsHrInxV2.setSmclsSignSubNum(10);
		bonusAndClsHrInxV2.setPermclsRealSignPreFundAmt(new BigDecimal("1000"));
		bonusAndClsHrInxV2.setPermclsSignNum(20);
		bonusAndClsHrInxV2.setPreFundTotal(new BigDecimal(800));
		bonusAndClsHrInxV2.setPreFundTarg(new BigDecimal(10));
		bonusAndClsHrInxV2.setOooReFund(new BigDecimal(50));
		bonusAndClsHrInxV2.setSmReFund(new BigDecimal(150));
		bonusAndClsHrInxVoList.add(bonusAndClsHrInxV2);
		
		
		
		
		
		
		
		HSSFWorkbook workbook = generateBonusAndClassHourReport(
				bonusAndClsHrInxVoList, null);
		
		

		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream("d:/a1.xls");
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 把相应的Excel 工作簿存盘
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}

		// 操作结束，关闭文件

	}

}
