package com.eduboss.eo;

import com.eduboss.excel.anno.ExcelIgnore;
import com.eduboss.excel.imported.GenericExcelImportedEo;
import com.eduboss.excel.imported.anno.CellImportedHeaderDefine;
import com.eduboss.excel.imported.anno.ImportedSheetDefine;

/**
 * Created by Administrator on 2016/7/5.
 */
@ImportedSheetDefine(sheetName = "数据收集格式模板")
public class PosPayDataImportedEo extends GenericExcelImportedEo{

    @ExcelIgnore
    private static final long serialVersionUID = 1760964096814089730L;

    @CellImportedHeaderDefine("系统流水号(交易参考号)")
    private String posId;

    @CellImportedHeaderDefine("交易时间")
    private String posTime;
    
    @CellImportedHeaderDefine("交易金额(单位:元)")
    private String amount;

    @CellImportedHeaderDefine("终端编号")
    private String posNumber;

    @CellImportedHeaderDefine("交易帐号")
    private String posAccount;

    @CellImportedHeaderDefine("商户名称")
    private String merchantName;

    @CellImportedHeaderDefine("商户手续费")
    private String poundage;

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getPosTime() {
		return posTime;
	}

	public void setPosTime(String posTime) {
		this.posTime = posTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}

	public String getPosAccount() {
		return posAccount;
	}

	public void setPosAccount(String posAccount) {
		this.posAccount = posAccount;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPoundage() {
		return poundage;
	}

	public void setPoundage(String poundage) {
		this.poundage = poundage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
