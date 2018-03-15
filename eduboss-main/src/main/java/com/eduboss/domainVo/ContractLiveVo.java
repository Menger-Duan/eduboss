package com.eduboss.domainVo;


import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;
import com.eduboss.dto.VirtualSchool;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties( ignoreUnknown = true)
public class ContractLiveVo {
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

    private ContractProductVo contractProductVo = null;


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

    private String orderNum;//直播同步合同id


    private int isNarrow;

    private String isReceipted; // TRUE：发生过收款，FALSE：没发生过

    private String isCharged; // TRUE：发生过扣费，FALSE：没发生过

    private BigDecimal consumeAmount; // 消耗金额

    private String schoolOrTemp;//显示school or schoolTemp   ： school ： 显示 合同的school字段     schoolTemp ： 显示合同 的schoolTemp 字段

    private Double transactionAmount;

    private String signByWhoAccount;

    private String firstContractId;//首单合同

    private String liveReceiptTime;//直播合同收款时间

    private String payWay;//支付方式



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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getFirstContractId() {
        return firstContractId;
    }

    public void setFirstContractId(String firstContractId) {
        this.firstContractId = firstContractId;
    }

    public String getLiveReceiptTime() {
        return liveReceiptTime;
    }

    public void setLiveReceiptTime(String liveReceiptTime) {
        this.liveReceiptTime = liveReceiptTime;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public ContractProductVo getContractProductVo() {
        return contractProductVo;
    }

    public void setContractProductVo(ContractProductVo contractProductVo) {
        this.contractProductVo = contractProductVo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractLiveVo that = (ContractLiveVo) o;

        if (ecsContract != that.ecsContract) return false;
        if (isNarrow != that.isNarrow) return false;
        if (contractId != null ? !contractId.equals(that.contractId) : that.contractId != null) return false;
        if (cusId != null ? !cusId.equals(that.cusId) : that.cusId != null) return false;
        if (cusPhone != null ? !cusPhone.equals(that.cusPhone) : that.cusPhone != null) return false;
        if (cusName != null ? !cusName.equals(that.cusName) : that.cusName != null) return false;
        if (stuId != null ? !stuId.equals(that.stuId) : that.stuId != null) return false;
        if (stuGrade != null ? !stuGrade.equals(that.stuGrade) : that.stuGrade != null) return false;
        if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) return false;
        if (contractGradeId != null ? !contractGradeId.equals(that.contractGradeId) : that.contractGradeId != null)
            return false;
        if (contractGrade != null ? !contractGrade.equals(that.contractGrade) : that.contractGrade != null)
            return false;
        if (stuName != null ? !stuName.equals(that.stuName) : that.stuName != null) return false;
        if (stuType != null ? !stuType.equals(that.stuType) : that.stuType != null) return false;
        if (signByWho != null ? !signByWho.equals(that.signByWho) : that.signByWho != null) return false;
        if (signTime != null ? !signTime.equals(that.signTime) : that.signTime != null) return false;
        if (oneOnOneTotalAmount != null ? !oneOnOneTotalAmount.equals(that.oneOnOneTotalAmount) : that.oneOnOneTotalAmount != null)
            return false;
        if (miniClassTotalAmount != null ? !miniClassTotalAmount.equals(that.miniClassTotalAmount) : that.miniClassTotalAmount != null)
            return false;
        if (otherTotalAmount != null ? !otherTotalAmount.equals(that.otherTotalAmount) : that.otherTotalAmount != null)
            return false;
        if (oneOnManyTotalAmount != null ? !oneOnManyTotalAmount.equals(that.oneOnManyTotalAmount) : that.oneOnManyTotalAmount != null)
            return false;
        if (promiseClassTotalAmount != null ? !promiseClassTotalAmount.equals(that.promiseClassTotalAmount) : that.promiseClassTotalAmount != null)
            return false;
        if (lectureClassTotalAmount != null ? !lectureClassTotalAmount.equals(that.lectureClassTotalAmount) : that.lectureClassTotalAmount != null)
            return false;
        if (twoTeacherClassTotalAmount != null ? !twoTeacherClassTotalAmount.equals(that.twoTeacherClassTotalAmount) : that.twoTeacherClassTotalAmount != null)
            return false;
        if (liveAmount != null ? !liveAmount.equals(that.liveAmount) : that.liveAmount != null) return false;
        if (freeTotalHour != null ? !freeTotalHour.equals(that.freeTotalHour) : that.freeTotalHour != null)
            return false;
        if (oneOnOneRemainingHour != null ? !oneOnOneRemainingHour.equals(that.oneOnOneRemainingHour) : that.oneOnOneRemainingHour != null)
            return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (totalAmount != null ? !totalAmount.equals(that.totalAmount) : that.totalAmount != null) return false;
        if (paidAmount != null ? !paidAmount.equals(that.paidAmount) : that.paidAmount != null) return false;
        if (pendingAmount != null ? !pendingAmount.equals(that.pendingAmount) : that.pendingAmount != null)
            return false;
        if (availableAmount != null ? !availableAmount.equals(that.availableAmount) : that.availableAmount != null)
            return false;
        if (promotionAmount != null ? !promotionAmount.equals(that.promotionAmount) : that.promotionAmount != null)
            return false;
        if (contractType != that.contractType) return false;
        if (contractTypeName != null ? !contractTypeName.equals(that.contractTypeName) : that.contractTypeName != null)
            return false;
        if (contractTypeValue != null ? !contractTypeValue.equals(that.contractTypeValue) : that.contractTypeValue != null)
            return false;
        if (contractStatus != that.contractStatus) return false;
        if (contractStatusName != null ? !contractStatusName.equals(that.contractStatusName) : that.contractStatusName != null)
            return false;
        if (contractStatusValue != null ? !contractStatusValue.equals(that.contractStatusValue) : that.contractStatusValue != null)
            return false;
        if (paidStatus != that.paidStatus) return false;
        if (paidStatusName != null ? !paidStatusName.equals(that.paidStatusName) : that.paidStatusName != null)
            return false;
        if (paidStatusValue != null ? !paidStatusValue.equals(that.paidStatusValue) : that.paidStatusValue != null)
            return false;
        if (checkbyWho != null ? !checkbyWho.equals(that.checkbyWho) : that.checkbyWho != null) return false;
        if (remainingAmount != null ? !remainingAmount.equals(that.remainingAmount) : that.remainingAmount != null)
            return false;
        if (oneOnOneUnitPrice != null ? !oneOnOneUnitPrice.equals(that.oneOnOneUnitPrice) : that.oneOnOneUnitPrice != null)
            return false;
        if (classBalance != null ? !classBalance.equals(that.classBalance) : that.classBalance != null) return false;
        if (classArrangable != null ? !classArrangable.equals(that.classArrangable) : that.classArrangable != null)
            return false;
        if (gradeName != null ? !gradeName.equals(that.gradeName) : that.gradeName != null) return false;
        if (classNum != null ? !classNum.equals(that.classNum) : that.classNum != null) return false;
        if (productText != null ? !productText.equals(that.productText) : that.productText != null) return false;
        if (oneOnOneJson != null ? !oneOnOneJson.equals(that.oneOnOneJson) : that.oneOnOneJson != null) return false;
        if (blCampusId != null ? !blCampusId.equals(that.blCampusId) : that.blCampusId != null) return false;
        if (blCampusName != null ? !blCampusName.equals(that.blCampusName) : that.blCampusName != null) return false;
        if (stuBlCampusId != null ? !stuBlCampusId.equals(that.stuBlCampusId) : that.stuBlCampusId != null)
            return false;
        if (stuBlCampusName != null ? !stuBlCampusName.equals(that.stuBlCampusName) : that.stuBlCampusName != null)
            return false;
        if (channel != null ? !channel.equals(that.channel) : that.channel != null) return false;
        if (contractProductVos != null ? !contractProductVos.equals(that.contractProductVos) : that.contractProductVos != null)
            return false;
        if (contractProductVo != null ? !contractProductVo.equals(that.contractProductVo) : that.contractProductVo != null)
            return false;
        if (oneOnOneNormalProductVo != null ? !oneOnOneNormalProductVo.equals(that.oneOnOneNormalProductVo) : that.oneOnOneNormalProductVo != null)
            return false;
        if (oneOnOneFreeProductVo != null ? !oneOnOneFreeProductVo.equals(that.oneOnOneFreeProductVo) : that.oneOnOneFreeProductVo != null)
            return false;
        if (smallProductsVo != null ? !smallProductsVo.equals(that.smallProductsVo) : that.smallProductsVo != null)
            return false;
        if (otherProductsVo != null ? !otherProductsVo.equals(that.otherProductsVo) : that.otherProductsVo != null)
            return false;
        if (oneOnManyProductsVo != null ? !oneOnManyProductsVo.equals(that.oneOnManyProductsVo) : that.oneOnManyProductsVo != null)
            return false;
        if (studyManagerId != null ? !studyManagerId.equals(that.studyManagerId) : that.studyManagerId != null)
            return false;
        if (schoolId != null ? !schoolId.equals(that.schoolId) : that.schoolId != null) return false;
        if (schoolName != null ? !schoolName.equals(that.schoolName) : that.schoolName != null) return false;
        if (schoolTempId != null ? !schoolTempId.equals(that.schoolTempId) : that.schoolTempId != null) return false;
        if (schoolTempName != null ? !schoolTempName.equals(that.schoolTempName) : that.schoolTempName != null)
            return false;
        if (virtualSchool != null ? !virtualSchool.equals(that.virtualSchool) : that.virtualSchool != null)
            return false;
        if (printedNumber != null ? !printedNumber.equals(that.printedNumber) : that.printedNumber != null)
            return false;
        if (pointialStudentIndex != null ? !pointialStudentIndex.equals(that.pointialStudentIndex) : that.pointialStudentIndex != null)
            return false;
        if (available != null ? !available.equals(that.available) : that.available != null) return false;
        if (transactionUuid != null ? !transactionUuid.equals(that.transactionUuid) : that.transactionUuid != null)
            return false;
        if (orderNum != null ? !orderNum.equals(that.orderNum) : that.orderNum != null) return false;
        if (isReceipted != null ? !isReceipted.equals(that.isReceipted) : that.isReceipted != null) return false;
        if (isCharged != null ? !isCharged.equals(that.isCharged) : that.isCharged != null) return false;
        if (consumeAmount != null ? !consumeAmount.equals(that.consumeAmount) : that.consumeAmount != null)
            return false;
        if (schoolOrTemp != null ? !schoolOrTemp.equals(that.schoolOrTemp) : that.schoolOrTemp != null) return false;
        if (transactionAmount != null ? !transactionAmount.equals(that.transactionAmount) : that.transactionAmount != null)
            return false;
        if (signByWhoAccount != null ? !signByWhoAccount.equals(that.signByWhoAccount) : that.signByWhoAccount != null)
            return false;
        if (firstContractId != null ? !firstContractId.equals(that.firstContractId) : that.firstContractId != null)
            return false;
        if (liveReceiptTime != null ? !liveReceiptTime.equals(that.liveReceiptTime) : that.liveReceiptTime != null)
            return false;
        return payWay != null ? payWay.equals(that.payWay) : that.payWay == null;
    }

    @Override
    public int hashCode() {
        int result = contractId != null ? contractId.hashCode() : 0;
        result = 31 * result + (cusId != null ? cusId.hashCode() : 0);
        result = 31 * result + (cusPhone != null ? cusPhone.hashCode() : 0);
        result = 31 * result + (cusName != null ? cusName.hashCode() : 0);
        result = 31 * result + (stuId != null ? stuId.hashCode() : 0);
        result = 31 * result + (stuGrade != null ? stuGrade.hashCode() : 0);
        result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
        result = 31 * result + (contractGradeId != null ? contractGradeId.hashCode() : 0);
        result = 31 * result + (contractGrade != null ? contractGrade.hashCode() : 0);
        result = 31 * result + (stuName != null ? stuName.hashCode() : 0);
        result = 31 * result + (stuType != null ? stuType.hashCode() : 0);
        result = 31 * result + (signByWho != null ? signByWho.hashCode() : 0);
        result = 31 * result + (signTime != null ? signTime.hashCode() : 0);
        result = 31 * result + (oneOnOneTotalAmount != null ? oneOnOneTotalAmount.hashCode() : 0);
        result = 31 * result + (miniClassTotalAmount != null ? miniClassTotalAmount.hashCode() : 0);
        result = 31 * result + (otherTotalAmount != null ? otherTotalAmount.hashCode() : 0);
        result = 31 * result + (oneOnManyTotalAmount != null ? oneOnManyTotalAmount.hashCode() : 0);
        result = 31 * result + (promiseClassTotalAmount != null ? promiseClassTotalAmount.hashCode() : 0);
        result = 31 * result + (lectureClassTotalAmount != null ? lectureClassTotalAmount.hashCode() : 0);
        result = 31 * result + (twoTeacherClassTotalAmount != null ? twoTeacherClassTotalAmount.hashCode() : 0);
        result = 31 * result + (liveAmount != null ? liveAmount.hashCode() : 0);
        result = 31 * result + (freeTotalHour != null ? freeTotalHour.hashCode() : 0);
        result = 31 * result + (oneOnOneRemainingHour != null ? oneOnOneRemainingHour.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        result = 31 * result + (paidAmount != null ? paidAmount.hashCode() : 0);
        result = 31 * result + (pendingAmount != null ? pendingAmount.hashCode() : 0);
        result = 31 * result + (availableAmount != null ? availableAmount.hashCode() : 0);
        result = 31 * result + (promotionAmount != null ? promotionAmount.hashCode() : 0);
        result = 31 * result + (contractType != null ? contractType.hashCode() : 0);
        result = 31 * result + (ecsContract ? 1 : 0);
        result = 31 * result + (contractTypeName != null ? contractTypeName.hashCode() : 0);
        result = 31 * result + (contractTypeValue != null ? contractTypeValue.hashCode() : 0);
        result = 31 * result + (contractStatus != null ? contractStatus.hashCode() : 0);
        result = 31 * result + (contractStatusName != null ? contractStatusName.hashCode() : 0);
        result = 31 * result + (contractStatusValue != null ? contractStatusValue.hashCode() : 0);
        result = 31 * result + (paidStatus != null ? paidStatus.hashCode() : 0);
        result = 31 * result + (paidStatusName != null ? paidStatusName.hashCode() : 0);
        result = 31 * result + (paidStatusValue != null ? paidStatusValue.hashCode() : 0);
        result = 31 * result + (checkbyWho != null ? checkbyWho.hashCode() : 0);
        result = 31 * result + (remainingAmount != null ? remainingAmount.hashCode() : 0);
        result = 31 * result + (oneOnOneUnitPrice != null ? oneOnOneUnitPrice.hashCode() : 0);
        result = 31 * result + (classBalance != null ? classBalance.hashCode() : 0);
        result = 31 * result + (classArrangable != null ? classArrangable.hashCode() : 0);
        result = 31 * result + (gradeName != null ? gradeName.hashCode() : 0);
        result = 31 * result + (classNum != null ? classNum.hashCode() : 0);
        result = 31 * result + (productText != null ? productText.hashCode() : 0);
        result = 31 * result + (oneOnOneJson != null ? oneOnOneJson.hashCode() : 0);
        result = 31 * result + (blCampusId != null ? blCampusId.hashCode() : 0);
        result = 31 * result + (blCampusName != null ? blCampusName.hashCode() : 0);
        result = 31 * result + (stuBlCampusId != null ? stuBlCampusId.hashCode() : 0);
        result = 31 * result + (stuBlCampusName != null ? stuBlCampusName.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (contractProductVos != null ? contractProductVos.hashCode() : 0);
        result = 31 * result + (contractProductVo != null ? contractProductVo.hashCode() : 0);
        result = 31 * result + (oneOnOneNormalProductVo != null ? oneOnOneNormalProductVo.hashCode() : 0);
        result = 31 * result + (oneOnOneFreeProductVo != null ? oneOnOneFreeProductVo.hashCode() : 0);
        result = 31 * result + (smallProductsVo != null ? smallProductsVo.hashCode() : 0);
        result = 31 * result + (otherProductsVo != null ? otherProductsVo.hashCode() : 0);
        result = 31 * result + (oneOnManyProductsVo != null ? oneOnManyProductsVo.hashCode() : 0);
        result = 31 * result + (studyManagerId != null ? studyManagerId.hashCode() : 0);
        result = 31 * result + (schoolId != null ? schoolId.hashCode() : 0);
        result = 31 * result + (schoolName != null ? schoolName.hashCode() : 0);
        result = 31 * result + (schoolTempId != null ? schoolTempId.hashCode() : 0);
        result = 31 * result + (schoolTempName != null ? schoolTempName.hashCode() : 0);
        result = 31 * result + (virtualSchool != null ? virtualSchool.hashCode() : 0);
        result = 31 * result + (printedNumber != null ? printedNumber.hashCode() : 0);
        result = 31 * result + (pointialStudentIndex != null ? pointialStudentIndex.hashCode() : 0);
        result = 31 * result + (available != null ? available.hashCode() : 0);
        result = 31 * result + (transactionUuid != null ? transactionUuid.hashCode() : 0);
        result = 31 * result + (orderNum != null ? orderNum.hashCode() : 0);
        result = 31 * result + isNarrow;
        result = 31 * result + (isReceipted != null ? isReceipted.hashCode() : 0);
        result = 31 * result + (isCharged != null ? isCharged.hashCode() : 0);
        result = 31 * result + (consumeAmount != null ? consumeAmount.hashCode() : 0);
        result = 31 * result + (schoolOrTemp != null ? schoolOrTemp.hashCode() : 0);
        result = 31 * result + (transactionAmount != null ? transactionAmount.hashCode() : 0);
        result = 31 * result + (signByWhoAccount != null ? signByWhoAccount.hashCode() : 0);
        result = 31 * result + (firstContractId != null ? firstContractId.hashCode() : 0);
        result = 31 * result + (liveReceiptTime != null ? liveReceiptTime.hashCode() : 0);
        result = 31 * result + (payWay != null ? payWay.hashCode() : 0);
        return result;
    }
}
