package com.eduboss.service.impl;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.ProductType;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.ContractBonusVo;
import com.eduboss.domainVo.IncomeDistributionVo;
import com.eduboss.domainVo.RefundIncomeDistributeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.jedis.Message;
import com.eduboss.service.ContractBonusService;
import com.eduboss.service.ContractService;
import com.eduboss.service.StudentReturnService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.utils.BonusQueueUtils;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Service("com.eduboss.service.ContractBonusService")
public class ContractBonusServiceImpl implements ContractBonusService {
	
	@Autowired 
	private ContractBonusDao contractBonusDao;
	
	@Autowired 
	private OrganizationDao organizationDao;

	@Autowired 
	private StudentReturnService studentReturnService;
	
	@Autowired
	private FundsChangeHistoryDao fundschangeHistoryDao;
	
	@Autowired
	private StudentService studentService;

	@Autowired
	private ContractProductDao contractProductDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ContractDao contractDao;

	@Autowired
	private ContractService contractService;

	@Autowired
	private IncomeDistributionDao incomeDistributionDao;


	@Transactional
	@Override
	public DataPackage getContractBonusVoList(DataPackage dp,
			ContractBonusVo contractBonusVo,Map params) {
		dp= contractBonusDao.getContractBonusList(dp, contractBonusVo,params);
		List<IncomeDistribution> list = (List<IncomeDistribution>) dp.getDatas();
		List<ContractBonusVo> voList = new ArrayList<ContractBonusVo>();
		for(IncomeDistribution con : list){
			ContractBonusVo vo = new ContractBonusVo();
			vo.setFundsChangeHistoryId(con.getFundsChangeHistory().getId());
			vo.setContractCampusName(con.getContractCampus()!=null?con.getContractCampus().getName():"");
			vo.setBonusType(con.getBonusType().getName());
			vo.setTypeName(con.getProductType()!=null?con.getProductType().getName():"");
			if (con.getBaseBonusDistributeType()==BonusDistributeType.CAMPUS){
				vo.setOrganizationName(con.getBonusOrg()!=null?con.getBonusOrg().getName():"");
				vo.setCampusAmount(con.getAmount());
			}else if (con.getBaseBonusDistributeType() == BonusDistributeType.USER){
				vo.setBonusStaffName(con.getBonusStaff()!=null?con.getBonusStaff().getName():con.getBonusOrg().getName());
				vo.setBonusStaffCampusName(con.getBonusStaffCampus()!=null?con.getBonusStaffCampus().getName():"");
				vo.setBonusAmount(con.getAmount());
			}
			vo.setCreateTime(con.getCreateTime().toString());
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public RefundIncomeDistributeVo getContractBonusByStudentReturnId(
			String studentReturnId) {
//			List<ContractBonus> bonusList = contractBonusDao.findByStudentReturnId(studentReturnId);
		List<IncomeDistribution> bonusList = incomeDistributionDao.findByStudentReturnId(studentReturnId);

		List<IncomeDistribution> campusList = new ArrayList<>();

		List<IncomeDistribution> userList = new ArrayList<>();

		for (IncomeDistribution i: bonusList){
			if (i.getBaseBonusDistributeType()==BonusDistributeType.USER){
				userList.add(i);
			}else if (i.getBaseBonusDistributeType() == BonusDistributeType.CAMPUS){
				campusList.add(i);
			}
		}

		RefundIncomeDistributeVo refundWorkflowVo = new RefundIncomeDistributeVo();

		for (int i = 0;i<campusList.size(); i++){
			switch (i){
				case 0:
					IncomeDistribution i1 = campusList.get(i);
					Organization o1 = i1.getBonusOrg();
					refundWorkflowVo.setFirstSubBonusDistributeTypeCampus(i1.getSubBonusDistributeType());
					refundWorkflowVo.setFirstRefundDutyCampusId(o1.getId());
					refundWorkflowVo.setFirstRefundDutyCampusName(o1.getName());
					refundWorkflowVo.setFirstRefundDutyAmountCampus(i1.getAmount());
					break;
				case 1:
					IncomeDistribution i2 = campusList.get(i);
					Organization o2 = i2.getBonusOrg();
					refundWorkflowVo.setSecondSubBonusDistributeTypeCampus(i2.getSubBonusDistributeType());
					refundWorkflowVo.setSecondRefundDutyCampusId(o2.getId());
					refundWorkflowVo.setSecondRefundDutyCampusName(o2.getName());
					refundWorkflowVo.setSecondRefundDutyAmountCampus(i2.getAmount());
					break;
				case 2:
					IncomeDistribution i3 = campusList.get(i);
					Organization o3 = i3.getBonusOrg();
					refundWorkflowVo.setThirdSubBonusDistributeTypeCampus(i3.getSubBonusDistributeType());
					refundWorkflowVo.setThirdRefundDutyCampusId(o3.getId());
					refundWorkflowVo.setThirdRefundDutyCampusName(o3.getName());
					refundWorkflowVo.setThirdRefundDutyAmountCampus(i3.getAmount());
					break;
				case 3:
					IncomeDistribution i4 = campusList.get(i);
					Organization o4 = i4.getBonusOrg();
					refundWorkflowVo.setFourthSubBonusDistributeTypeCampus(i4.getSubBonusDistributeType());
					refundWorkflowVo.setFourthRefundDutyCampusId(o4.getId());
					refundWorkflowVo.setFourthRefundDutyCampusName(o4.getName());
					refundWorkflowVo.setFourthRefundDutyAmountCampus(i4.getAmount());
					break;
				default:
					IncomeDistribution i5 = campusList.get(i);
					Organization o5 = i5.getBonusOrg();
					refundWorkflowVo.setFifthSubBonusDistributeTypeCampus(i5.getSubBonusDistributeType());
					refundWorkflowVo.setFifthRefundDutyCampusId(o5.getId());
					refundWorkflowVo.setFifthRefundDutyCampusName(o5.getName());
					refundWorkflowVo.setFifthRefundDutyAmountCampus(i5.getAmount());
			}
		}

		for (int i = 0; i<userList.size(); i++){
			switch (i){
				case 0:
					IncomeDistribution i1 = userList.get(i);
					refundWorkflowVo.setFirstSubBonusDistributeTypePerson(i1.getSubBonusDistributeType());
					refundWorkflowVo.setFirstRefundDutyPersonId(getIDbyType(i1));
					refundWorkflowVo.setFirstRefundDutyPersonName(getNamebyType(i1));
					refundWorkflowVo.setFirstRefundDutyAmountPerson(i1.getAmount());
					break;
				case 1:
					IncomeDistribution i2 = userList.get(i);
					refundWorkflowVo.setSecondSubBonusDistributeTypePerson(i2.getSubBonusDistributeType());
					refundWorkflowVo.setSecondRefundDutyPersonId(getIDbyType(i2));
					refundWorkflowVo.setSecondRefundDutyPersonName(getNamebyType(i2));
					refundWorkflowVo.setSecondRefundDutyAmountPerson(i2.getAmount());
					break;
				case 2:
					IncomeDistribution i3 = userList.get(i);
					refundWorkflowVo.setThirdSubBonusDistributeTypePerson(i3.getSubBonusDistributeType());
					refundWorkflowVo.setThirdRefundDutyPersonId(getIDbyType(i3));
					refundWorkflowVo.setThirdRefundDutyPersonName(getNamebyType(i3));
					refundWorkflowVo.setThirdRefundDutyAmountPerson(i3.getAmount());
					break;
				case 3:
					IncomeDistribution i4 = userList.get(i);
					refundWorkflowVo.setFourthSubBonusDistributeTypePerson(i4.getSubBonusDistributeType());
					refundWorkflowVo.setFourthRefundDutyPersonId(getIDbyType(i4));
					refundWorkflowVo.setFourthRefundDutyPersonName(getNamebyType(i4));
					refundWorkflowVo.setFourthRefundDutyAmountPerson(i4.getAmount());
					break;
				default:
					IncomeDistribution i5 = userList.get(i);
					refundWorkflowVo.setFifthSubBonusDistributeTypePerson(i5.getSubBonusDistributeType());
					refundWorkflowVo.setFifthRefundDutyPersonId(getIDbyType(i5));
					refundWorkflowVo.setFifthRefundDutyPersonName(getNamebyType(i5));
					refundWorkflowVo.setFifthRefundDutyAmountPerson(i5.getAmount());
			}
		}

		return refundWorkflowVo;
	}

	private String getIDbyType(IncomeDistribution incomeDistribution) {
		if (incomeDistribution.getSubBonusDistributeType() == BonusDistributeType.USER_USER){
			String id = incomeDistribution.getBonusStaff().getUserId();
			return id;
		}else {
			String id = incomeDistribution.getBonusOrg().getId();
			return id;
		}
	}


	private String getNamebyType(IncomeDistribution incomeDistribution) {
		if (incomeDistribution.getSubBonusDistributeType() == BonusDistributeType.USER_USER){
			String name = incomeDistribution.getBonusStaff().getName();
			return name;
		}else {
			String name = incomeDistribution.getBonusOrg().getName();
			return name;
		}
	}


	@Override
	public void saveContractBonus(String fundChangeHistoryId,
								  String bonusType, String studentReturnId, String returnType, String returnReason, MultipartFile certificateImageFile, String accountName, String account, String contractProductId, RefundIncomeDistributeVo refundVo) {
		ContractProduct contractProduct=null;
		if (StringUtils.isNotBlank(contractProductId)){
			contractProduct=contractProductDao.findById(contractProductId);
		}
//		contractBonusDao.deleteByStudentReturnId(studentReturnId);
		studentReturnService.saveStudentReturn(studentReturnId, returnType, returnReason,accountName,account);

		if(StringUtils.isNotBlank(fundChangeHistoryId)){	
			FundsChangeHistory	fundsChangeHistory=fundschangeHistoryDao.findById(fundChangeHistoryId);
			if(certificateImageFile != null && certificateImageFile.getSize()>0){
				String fileName=certificateImageFile.getOriginalFilename().substring(0,certificateImageFile.getOriginalFilename().lastIndexOf("."));//上传的文件名
				String puff=certificateImageFile.getOriginalFilename().replace(fileName, "");
				String aliName = "CERTIFICATE_" + fundsChangeHistory.getId()+puff;//阿里云上面的文件名			
				fundsChangeHistory.setCertificateImage(aliName);
				studentService.saveFileToRemoteServer(certificateImageFile, aliName);
				fundsChangeHistory.setCertificateImage(aliName);
				fundschangeHistoryDao.save(fundsChangeHistory);
			}
			fundschangeHistoryDao.flush();
		}


		contractService.saveContractBonus(fundChangeHistoryId, bonusType, studentReturnId, contractProduct, refundVo);
		

		
	}
	
	/**
	 * 退费详情删除图片
	 * @param fundsChangeId
	 */
	public void  deleteImage(String fundsChangeId){
		Map<String, Object> params  = Maps.newHashMap();
		params.put("fundsChangeId", fundsChangeId);
		String sql="UPDATE funds_change_history SET CERTIFICATE_IMAGE=NULL where id= :fundsChangeId ";
		contractBonusDao.excuteSql(sql,params);
	}

	/**
	 * 修改合同后如果每种产品的实付金额少于已分配的业绩,对应的业绩要删除
	 * @param contractId
	 */
	@Override
	public List<Message> afterEditContractDeleteContractBonus(String contractId) {
		Contract contract = contractDao.findById(contractId);
		deleteUserOrCampusBonus(contract, BonusDistributeType.CAMPUS);
		return deleteUserOrCampusBonus(contract, BonusDistributeType.USER);
	}

	private List<Message> deleteUserOrCampusBonus(Contract contract, BonusDistributeType bonusDistributeType) {
		List<Message> returnMsg=new ArrayList<>();
		Map<String, Object> params  = Maps.newHashMap();
		params.put("bonusDistributeType", bonusDistributeType);
		params.put("contractId", contract.getId());
		StringBuffer hql = new StringBuffer();
		hql.append(" from IncomeDistribution i where 1=1 and i.bonusType='NORMAL' ");
		hql.append(" and i.baseBonusDistributeType = :bonusDistributeType ");
		hql.append(" and i.fundsChangeHistory.contract.id = :contractId ");
		List<IncomeDistribution> list = incomeDistributionDao.findAllByHQL(hql.toString(),params);
		for (ProductType type: ProductType.values()) {
			BigDecimal incomeAmount=BigDecimal.ZERO;
			for(Iterator i = list.iterator();i.hasNext();){
				IncomeDistribution incomeDistribution=(IncomeDistribution)i.next();
				if(type.equals(incomeDistribution.getProductType())){
					incomeAmount=incomeAmount.add(incomeDistribution.getAmount());
				}
			}

			if (incomeAmount.compareTo(contract.getRealAmountByProductType(type))>0){
				//已分配业绩大于修改后合同的realAmount ,就删除业绩
				for(Iterator i = list.iterator();i.hasNext();){
					IncomeDistribution incomeDistribution=(IncomeDistribution)i.next();
					if(BonusDistributeType.USER.equals(bonusDistributeType)){
						BonusQueueUtils.addBonusToMessage(incomeDistribution,returnMsg,"0");//删除业绩队列
					}
					incomeDistributionDao.delete(incomeDistribution);
					i.remove();
				}
			}

		}
		return returnMsg;
	}

	private void campusBonus(String contractId, BigDecimal oneOnOneAmount, BigDecimal otmAmount, BigDecimal smallClassAmount, BigDecimal promiseClassAmount, BigDecimal lectureAmount, BigDecimal otherAmount) {
		List<ContractBonus> oneOnOneCampus = new ArrayList<>();
		List<ContractBonus> otmCampus = new ArrayList<>();
		List<ContractBonus> smallClassCampus = new ArrayList<>();
		List<ContractBonus> promiseClassCampus = new ArrayList<>();
		List<ContractBonus> lectureCampus = new ArrayList<>();
		List<ContractBonus> otherCampus = new ArrayList<>();

		Map<String, Object> params  = Maps.newHashMap();
		params.put("contractId", contractId);
		StringBuffer hql = new StringBuffer();
		hql.append(" from ContractBonus cb where 1=1 and cb.bonusType='NORMAL' ");
		hql.append(" and cb.organization is not null ");
		hql.append(" and cb.fundsChangeHistory.contract.id = :contractId ");
		List<ContractBonus> list = contractBonusDao.findAllByHQL(hql.toString(),params);
		for (ContractBonus contractBonus : list){
			switch (contractBonus.getType()){
				case ONE_ON_ONE_COURSE:
					oneOnOneCampus.add(contractBonus);
					break;
				case ONE_ON_MANY:
					otmCampus.add(contractBonus);
					break;
				case SMALL_CLASS:
					smallClassCampus.add(contractBonus);
					break;
				case ECS_CLASS:
					promiseClassCampus.add(contractBonus);
					break;
				case LECTURE:
					lectureCampus.add(contractBonus);
					break;
				case OTHERS:
					otherCampus.add(contractBonus);
			}
		}
		campusListDelete(oneOnOneCampus, oneOnOneAmount);
		campusListDelete(otmCampus, otmAmount);
		campusListDelete(smallClassCampus, smallClassAmount);
		campusListDelete(promiseClassCampus, promiseClassAmount);
		campusListDelete(lectureCampus, lectureAmount);
		campusListDelete(otherCampus, otherAmount);
	}

	/**
	 * 校区业绩删除
	 * @param list
	 * @param realAmount
     */
	private void campusListDelete(List<ContractBonus> list, BigDecimal realAmount){
		BigDecimal total = BigDecimal.ZERO;// 已分配的业绩
		for (ContractBonus contractBonus: list){
			total = total.add(contractBonus.getCampusAmount());
		}
		if (total.compareTo(realAmount)>0){
			//已分配业绩大于修改后合同的realAmount ,就删除业绩
			for (ContractBonus contractBonus: list){
				contractBonusDao.delete(contractBonus);
			}
		}
	}

	private void userBonus(String contractId, BigDecimal oneOnOneAmount, BigDecimal otmAmount, BigDecimal smallClassAmount, BigDecimal promiseClassAmount, BigDecimal lectureAmount, BigDecimal otherAmount) {
		List<ContractBonus> oneOnOneUser = new ArrayList<>();
		List<ContractBonus> otmUser = new ArrayList<>();
		List<ContractBonus> smallClassUser = new ArrayList<>();
		List<ContractBonus> promiseClassUser = new ArrayList<>();
		List<ContractBonus> lectureUser = new ArrayList<>();
		List<ContractBonus> otherUser = new ArrayList<>();
		Map<String, Object> params  = Maps.newHashMap();
		params.put("contractId", contractId);
		StringBuffer hql = new StringBuffer();
		hql.append(" from ContractBonus cb where 1=1 and cb.bonusType='NORMAL' ");
		hql.append(" and cb.bonusStaff is not null ");
		hql.append(" and cb.fundsChangeHistory.contract.id = :contractId ");
		List<ContractBonus> list = contractBonusDao.findAllByHQL(hql.toString(),params);
		for (ContractBonus contractBonus : list){
			switch (contractBonus.getType()){
				case ONE_ON_ONE_COURSE:
					oneOnOneUser.add(contractBonus);
					break;
				case ONE_ON_MANY:
					otmUser.add(contractBonus);
					break;
				case SMALL_CLASS:
					smallClassUser.add(contractBonus);
					break;
				case ECS_CLASS:
					promiseClassUser.add(contractBonus);
					break;
				case LECTURE:
					lectureUser.add(contractBonus);
					break;
				case OTHERS:
					otherUser.add(contractBonus);
			}
		}

		userListDelete(oneOnOneUser, oneOnOneAmount);
		userListDelete(otmUser, otmAmount);
		userListDelete(smallClassUser, smallClassAmount);
		userListDelete(promiseClassUser, promiseClassAmount);
		userListDelete(lectureUser, lectureAmount);
		userListDelete(otherUser, otherAmount);
	}

	/**
	 * 个人业绩删除
	 * @param list
	 * @param realAmount
	 */
	private void userListDelete(List<ContractBonus> list, BigDecimal realAmount) {
		BigDecimal total = BigDecimal.ZERO;// 已分配的业绩
		for (ContractBonus contractBonus: list){
			total = total.add(contractBonus.getBonusAmount());
		}
		if (total.compareTo(realAmount)>0){
			//已分配业绩大于修改后合同的realAmount ,就删除业绩
			for (ContractBonus contractBonus: list){
				contractBonusDao.delete(contractBonus);
			}
		}
	}
}
