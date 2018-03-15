package com.eduboss.domainVo;

import com.eduboss.common.SaleChannel;
import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;
import com.eduboss.dto.VirtualSchool;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


/**
 * 专门用于接收合同 搜索参数 或者传送内容信息给 前台的 VO
 * @author robinzhang
 *
 */

@JsonIgnoreProperties( ignoreUnknown = true)
public class ContractVo {
	private String contractId;
	private String cusId;
	private String cusPhone;
	private String cusName;
	
	private String stuId;
	private String stuGrade;
	private String gradeId;
	private String contractGradeId;//合同年级   
	private String contractGrade;//合同年级   
	private String stuName;
	private String stuType;//学生类型，正式学生
	
	private String signByWho;
	private String signTime;
	
	private BigDecimal oneOnOneTotalAmount = BigDecimal.ZERO;
	private BigDecimal miniClassTotalAmount = BigDecimal.ZERO;
	private BigDecimal otherTotalAmount = BigDecimal.ZERO;
	private BigDecimal oneOnManyTotalAmount=BigDecimal.ZERO;
	private BigDecimal promiseClassTotalAmount=BigDecimal.ZERO;
	private BigDecimal lectureClassTotalAmount=BigDecimal.ZERO;
	private BigDecimal twoTeacherClassTotalAmount=BigDecimal.ZERO;
	private BigDecimal liveAmount = BigDecimal.ZERO;
	
	private BigDecimal freeTotalHour = BigDecimal.ZERO;
	private BigDecimal oneOnOneRemainingHour;
	
	private String remark;
	
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private BigDecimal paidAmount = BigDecimal.ZERO;
	private BigDecimal pendingAmount = BigDecimal.ZERO;
	private BigDecimal availableAmount = BigDecimal.ZERO;
	private BigDecimal promotionAmount = BigDecimal.ZERO;
	
	
	
	private ContractType contractType;

	private boolean ecsContract; //是否目标班合同

	//有2019及以后的目标班产品
	private boolean hasEcsCPAfter2018;

	private String contractTypeName;
	private String contractTypeValue;
	
	private ContractStatus contractStatus;
	private String contractStatusName;
	private String contractStatusValue;
	
	private ContractPaidStatus paidStatus;
	private String paidStatusName;
	private String paidStatusValue;
	
	private String checkbyWho;
	
	private BigDecimal remainingAmount = BigDecimal.ZERO;
//	private BigDecimal oneOnOneClassBalance;
//	private BigDecimal miniClassBalance;
//	private BigDecimal otherConsume;
	private BigDecimal oneOnOneUnitPrice = BigDecimal.ZERO;
	
	private String classBalance;//剩余课时
	private String classArrangable;//可排课时
	private String gradeName;//年级
	private String classNum;//总课时数
	private String productText;//产品详情
	
	private String oneOnOneJson; // 保存一对一的信息， 因为这些信息只是展示，不会使用
    private String blCampusId; // 合同归属校区
    private String blCampusName;
	private String stuBlCampusId;//学生归属校区 查询合同列表用到
	private String stuBlCampusName;
	private String channel;//支付方式
	
	private Set<ContractProductVo> contractProductVos =  new HashSet<ContractProductVo>(); 
	
	private ContractProductVo oneOnOneNormalProductVo = null;
	private ContractProductVo oneOnOneFreeProductVo = null;
	private Set<ContractProductVo> smallProductsVo = null;
	private Set<ContractProductVo> otherProductsVo = null;
	
	private Set<ContractProductVo> oneOnManyProductsVo = null;
	
	private String studyManagerId;
	private String schoolId;
	private String schoolName;

	private String schoolTempId; //待审核的学校id
	private String schoolTempName;//待审核学校的名字

	private VirtualSchool virtualSchool; //选择学校为其他的时候建立的待审核学校

	private Integer printedNumber;//合同打印次数
	//
	private Integer pointialStudentIndex;
	
	private String available; //待分配资金是否为0
	
	private String transactionUuid;
	
	private int isNarrow;
	
	private String isReceipted; // TRUE：发生过收款，FALSE：没发生过
	
	private String isCharged; // TRUE：发生过扣费，FALSE：没发生过
	
	private BigDecimal consumeAmount; // 消耗金额

    private String schoolOrTemp;//显示school or schoolTemp   ： school ： 显示 合同的school字段     schoolTemp ： 显示合同 的schoolTemp 字段
	
	private SaleChannel saleChannel;

	

	private Double transactionAmount;

	private String signByWhoAccount;




	private int pubPayContract;//是否公帐 0：否 1：是

	private boolean liveProductContract = false; //是否直播合同 合同产品全是直播产品
	private boolean takeawayMoney;//业绩分配时间截止后，关联的提取功能也同时禁止 redmine id #1976 产品：黄萌
	
	private String posMachineType; // pos机类型


	public int getPubPayContract() {
		return pubPayContract;
	}

	public void setPubPayContract(int pubPayContract) {
		this.pubPayContract = pubPayContract;
	}

	public ContractType getContractType() {
		return contractType;
	}
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	
	public String getCheckbyWho() {
		return checkbyWho;
	}
	public void setCheckbyWho(String checkbyWho) {
		this.checkbyWho = checkbyWho;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}	
	public String getStuType() {
		return stuType;
	}
	public void setStuType(String stuType) {
		this.stuType = stuType;
	}
	public String getCusPhone() {
		return cusPhone;
	}
	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
	public String getSignByWho() {
		return signByWho;
	}
	public void setSignByWho(String signByWho) {
		this.signByWho = signByWho;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getStuGrade() {
		return stuGrade;
	}
	public void setStuGrade(String stuGrade) {
		this.stuGrade = stuGrade;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	public String getClassBalance() {
		return classBalance;
	}
	public void setClassBalance(String classBalance) {
		this.classBalance = classBalance;
	}
	public String getClassArrangable() {
		return classArrangable;
	}
	public void setClassArrangable(String classArrangable) {
		this.classArrangable = classArrangable;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	public String getProductText() {
		return productText;
	}
	public void setProductText(String productText) {
		this.productText = productText;
	}
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public Set<ContractProductVo> getContractProductVos() {
		return contractProductVos;
	}
	public void setContractProductVos(Set<ContractProductVo> contractProductVos) {
		this.contractProductVos = contractProductVos;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}
	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}
	public ContractStatus getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}
	public ContractPaidStatus getPaidStatus() {
		return paidStatus;
	}
	public void setPaidStatus(ContractPaidStatus paidStatus) {
		this.paidStatus = paidStatus;
	}

	public BigDecimal getOneOnOneUnitPrice() {
		return oneOnOneUnitPrice;
	}
	public void setOneOnOneUnitPrice(BigDecimal oneOnOneUnitPrice) {
		this.oneOnOneUnitPrice = oneOnOneUnitPrice;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getContractTypeName() {
		return contractTypeName;
	}
	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}
	public String getContractTypeValue() {
		return contractTypeValue;
	}
	public void setContractTypeValue(String contractTypeValue) {
		this.contractTypeValue = contractTypeValue;
	}
	public String getContractStatusName() {
		return contractStatusName;
	}
	public void setContractStatusName(String contractStatusName) {
		this.contractStatusName = contractStatusName;
	}
	public String getPaidStatusName() {
		return paidStatusName;
	}
	public void setPaidStatusName(String paidStatusName) {
		this.paidStatusName = paidStatusName;
	}
	public String getPaidStatusValue() {
		return paidStatusValue;
	}
	public void setPaidStatusValue(String paidStatusValue) {
		this.paidStatusValue = paidStatusValue;
	}
	public String getContractStatusValue() {
		return contractStatusValue;
	}
	public void setContractStatusValue(String contractStatusValue) {
		this.contractStatusValue = contractStatusValue;
	}
	public BigDecimal getOneOnOneTotalAmount() {
		return oneOnOneTotalAmount;
	}
	public void setOneOnOneTotalAmount(BigDecimal oneOnOneTotalAmount) {
		this.oneOnOneTotalAmount = oneOnOneTotalAmount;
	}
	public BigDecimal getMiniClassTotalAmount() {
		return miniClassTotalAmount;
	}
	public void setMiniClassTotalAmount(BigDecimal miniClassTotalAmount) {
		this.miniClassTotalAmount = miniClassTotalAmount;
	}
	public BigDecimal getOtherTotalAmount() {
		return otherTotalAmount;
	}
	public void setOtherTotalAmount(BigDecimal otherTotalAmount) {
		this.otherTotalAmount = otherTotalAmount;
	}
	public BigDecimal getFreeTotalHour() {
		return freeTotalHour;
	}
	public void setFreeTotalHour(BigDecimal freeTotalHour) {
		this.freeTotalHour = freeTotalHour;
	}
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public String getOneOnOneJson() {
		return oneOnOneJson;
	}
	public void setOneOnOneJson(String oneOnOneJson) {
		this.oneOnOneJson = oneOnOneJson;
	}
	public ContractProductVo getOneOnOneNormalProductVo() {
		return oneOnOneNormalProductVo;
	}
	public void setOneOnOneNormalProductVo(ContractProductVo oneOnOneNormalProductVo) {
		this.oneOnOneNormalProductVo = oneOnOneNormalProductVo;
	}
	public ContractProductVo getOneOnOneFreeProductVo() {
		return oneOnOneFreeProductVo;
	}
	public void setOneOnOneFreeProductVo(ContractProductVo oneOnOneFreeProductVo) {
		this.oneOnOneFreeProductVo = oneOnOneFreeProductVo;
	}
	public Set<ContractProductVo> getSmallProductsVo() {
		return smallProductsVo;
	}
	public void setSmallProductsVo(Set<ContractProductVo> smallProductsVo) {
		this.smallProductsVo = smallProductsVo;
	}
	public Set<ContractProductVo> getOtherProductsVo() {
		return otherProductsVo;
	}
	public void setOtherProductsVo(Set<ContractProductVo> otherProductsVo) {
		this.otherProductsVo = otherProductsVo;
	}
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getStuBlCampusId() {
		return stuBlCampusId;
	}
	public void setStuBlCampusId(String stuBlCampusId) {
		this.stuBlCampusId = stuBlCampusId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getStudyManagerId() {
		return studyManagerId;
	}
	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}
	public BigDecimal getOneOnOneRemainingHour() {
		return oneOnOneRemainingHour;
	}
	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}
	public Integer getPrintedNumber() {
		return printedNumber;
	}
	public void setPrintedNumber(Integer printedNumber) {
		this.printedNumber = printedNumber;
	}

    public String getBlCampusId() {
        return blCampusId;
    }

    public void setBlCampusId(String blCampusId) {
        this.blCampusId = blCampusId;
    }
	public Integer getPointialStudentIndex() {
		return pointialStudentIndex;
	}
	public void setPointialStudentIndex(Integer pointialStudentIndex) {
		this.pointialStudentIndex = pointialStudentIndex;
	}
	public String getContractGradeId() {
		return contractGradeId;
	}
	public void setContractGradeId(String contractGradeId) {
		this.contractGradeId = contractGradeId;
	}
	public String getContractGrade() {
		return contractGrade;
	}
	public void setContractGrade(String contractGrade) {
		this.contractGrade = contractGrade;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public BigDecimal getOneOnManyTotalAmount() {
		return oneOnManyTotalAmount;
	}
	public void setOneOnManyTotalAmount(BigDecimal oneOnManyTotalAmount) {
		this.oneOnManyTotalAmount = oneOnManyTotalAmount;
	}
	public Set<ContractProductVo> getOneOnManyProductsVo() {
		return oneOnManyProductsVo;
	}
	public void setOneOnManyProductsVo(Set<ContractProductVo> oneOnManyProductsVo) {
		this.oneOnManyProductsVo = oneOnManyProductsVo;
	}
	public BigDecimal getPromiseClassTotalAmount() {
		return promiseClassTotalAmount;
	}
	public void setPromiseClassTotalAmount(BigDecimal promiseClassTotalAmount) {
		this.promiseClassTotalAmount = promiseClassTotalAmount;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getTransactionUuid() {
		return transactionUuid;
	}
	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}
	public BigDecimal getLectureClassTotalAmount() {
		return lectureClassTotalAmount;
	}
	public void setLectureClassTotalAmount(BigDecimal lectureClassTotalAmount) {
		this.lectureClassTotalAmount = lectureClassTotalAmount;
	}

	public boolean isEcsContract() {
		return ecsContract;
	}

	public void setEcsContract(boolean ecsContract) {
		this.ecsContract = ecsContract;
	}
	
	public int getIsNarrow() {
		return isNarrow;
	}
	public void setIsNarrow(int isNarrow) {
		this.isNarrow = isNarrow;
	}


	public VirtualSchool getVirtualSchool() {
		return virtualSchool;
	}

	public void setVirtualSchool(VirtualSchool virtualSchool) {
		this.virtualSchool = virtualSchool;
	}
	
	public String getIsReceipted() {
		return isReceipted;
	}
	
	public void setIsReceipted(String isReceipted) {
		this.isReceipted = isReceipted;
	}


	public String getSchoolTempId() {
		return schoolTempId;
	}

	public void setSchoolTempId(String schoolTempId) {
		this.schoolTempId = schoolTempId;
	}

	public String getSchoolTempName() {
		return schoolTempName;
	}

	public void setSchoolTempName(String schoolTempName) {
		this.schoolTempName = schoolTempName;
	}

    public String getSchoolOrTemp() {
        return schoolOrTemp;
    }

    public void setSchoolOrTemp(String schoolOrTemp) {
        this.schoolOrTemp = schoolOrTemp;
    }
	public String getIsCharged() {
		return isCharged;
	}
	public void setIsCharged(String isCharged) {
		this.isCharged = isCharged;
	}
	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}
	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public String getStuBlCampusName() {
		return stuBlCampusName;
	}

	public void setStuBlCampusName(String stuBlCampusName) {
		this.stuBlCampusName = stuBlCampusName;
	}

	public BigDecimal getTwoTeacherClassTotalAmount() {
		return twoTeacherClassTotalAmount;
	}

	public void setTwoTeacherClassTotalAmount(BigDecimal twoTeacherClassTotalAmount) {
		this.twoTeacherClassTotalAmount = twoTeacherClassTotalAmount;
	}

	public BigDecimal getLiveAmount() {
		return liveAmount;
	}

	public void setLiveAmount(BigDecimal liveAmount) {
		this.liveAmount = liveAmount;
	}

    public SaleChannel getSaleChannel() {
        return saleChannel;
    }
    public void setSaleChannel(SaleChannel saleChannel) {
        this.saleChannel = saleChannel;
    }
    public String getSaleChannelName() {
        return saleChannel != null ? saleChannel.getName() : "";
    }


	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getSignByWhoAccount() {
		return signByWhoAccount;
	}

	public void setSignByWhoAccount(String signByWhoAccount) {
		this.signByWhoAccount = signByWhoAccount;
	}

	public boolean isLiveProductContract() {
		return liveProductContract;
	}

	public void setLiveProductContract(boolean liveProductContract) {
		this.liveProductContract = liveProductContract;
	}

	public void setTakeawayMoney(boolean takeawayMoney) {
		this.takeawayMoney = takeawayMoney;
	}

	public boolean isTakeawayMoney() {
		return takeawayMoney;
	}


	public boolean isHasEcsCPAfter2018() {
		return hasEcsCPAfter2018;
	}

	public void setHasEcsCPAfter2018(boolean hasEcsCPAfter2018) {
		this.hasEcsCPAfter2018 = hasEcsCPAfter2018;
	}

	/**
	 * pos机类型
	 * @return
	 */
    public String getPosMachineType() {
        return posMachineType;
    }

    public void setPosMachineType(String posMachineType) {
        this.posMachineType = posMachineType;
    }
	
}
