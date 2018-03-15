package com.eduboss.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.dao.FinanceUserDao;
import com.eduboss.dao.IncomeCountBrenchDao;
import com.eduboss.dao.IncomeCountCampusDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.IncomeCountBrench;
import com.eduboss.domain.IncomeCountCampus;
import com.eduboss.domain.Organization;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.IncomeMessage;
import com.eduboss.service.IncomeWorkingService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

@Service("IncomeWorkingService")
public class IncomeWorkingServiceImpl implements IncomeWorkingService {

	@Autowired
	private IncomeCountBrenchDao incomeCountBrenchDao;
	
	@Autowired
	private IncomeCountCampusDao incomeCountCampusDao;
	
	@Autowired
	private FinanceUserDao financeUserDao;
	

	@Autowired
	private OrganizationDao organizationDao;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public synchronized void saveIncomeByQueue(IncomeMessage message) {
		
		if(StringUtils.isBlank(message.getCountDate())){
			throw new ApplicationException("参数必须");
		}
		
		if(StringUtils.isNotBlank(message.getCampusId())){//校区名字
			Organization or = organizationDao.findById(message.getCampusId());
			Organization brench=userService.getBelongBranchByCampusId(message.getCampusId());
			if(or!=null){
				message.setCampusName(or.getName());
			}
			if(brench!=null){
				message.setBrenchId(brench.getId());
				message.setBrenchName(brench.getName());
			}
		}
		
		saveIncomeCountBrench(message);
		saveIncomeCountCampus(message);
		
	}
	
	public synchronized void saveIncomeCountBrench(IncomeMessage messge){
		IncomeCountBrench brench=incomeCountBrenchDao.findInfoByBrenchAndDate(messge.getBrenchId(),messge.getCountDate());
		brench=getNewIncomeCountBrench(brench, messge);
		incomeCountBrenchDao.save(brench);
		incomeCountBrenchDao.flush();
	}
	
	public synchronized IncomeCountBrench getNewIncomeCountBrench(IncomeCountBrench brench,IncomeMessage messge){
		if(brench!=null){
			brench.setModifyTime(DateTools.getCurrentDateTime());
		}else{
			brench=new IncomeCountBrench();
			brench.setBrenchId(messge.getBrenchId());
			brench.setBrenchName(messge.getBrenchName());
			brench.setCountDate(messge.getCountDate());
			brench.setCreateTime(DateTools.getCurrentDateTime());
			brench.setModifyTime(DateTools.getCurrentDateTime());
		}
		
		if(messge.getChargeType().equals(ChargeType.NORMAL) && messge.getPayType().equals(PayType.REAL)){
			if(messge.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
				brench.setOneOnoneRealAmount(getRealNumber(brench.getOneOnoneRealAmount(), messge.getAmount(), messge.getChargePayType()));
				brench.setOneOnOneQuantity(getRealNumber(brench.getOneOnOneQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.SMALL_CLASS)){
				brench.setSmallClassrealAmount(getRealNumber(brench.getSmallClassrealAmount(), messge.getAmount(), messge.getChargePayType()));
				brench.setSmallClassQuantity(getRealNumber(brench.getSmallClassQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ECS_CLASS)){
				brench.setEscClassRealAmount(getRealNumber(brench.getEscClassRealAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ONE_ON_MANY)){
				brench.setOtmRealAmount(getRealNumber(brench.getOtmRealAmount(), messge.getAmount(), messge.getChargePayType()));
				brench.setOtmQuantity(getRealNumber(brench.getOtmQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.LECTURE)){
				brench.setLectureRealAmount(getRealNumber(brench.getLectureRealAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.OTHERS)){
				brench.setOthersRealAmount(getRealNumber(brench.getOthersRealAmount(), messge.getAmount(), messge.getChargePayType()));
			}

			else if(messge.getType().equals(ProductType.LIVE)){
				brench.setLiveRealAmount(getRealNumber(brench.getLiveRealAmount(), messge.getAmount(), messge.getChargePayType()));
				brench.setLiveQuantity(getRealNumber(brench.getLiveQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}

			else if(messge.getType().equals(ProductType.TWO_TEACHER)){
				brench.setTwoTeacherRealAmount(getRealNumber(brench.getTwoTeacherRealAmount(), messge.getAmount(), messge.getChargePayType()));
				brench.setTwoTeacherQuantity(getRealNumber(brench.getTwoTeacherQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}
			brench.setRealTotalAmount(getRealNumber(brench.getRealTotalAmount(), messge.getAmount(), messge.getChargePayType()));
		}else if(messge.getChargeType().equals(ChargeType.NORMAL) && messge.getPayType().equals(PayType.PROMOTION)){
			if(messge.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
				brench.setOneOnonePromotionAmount(getRealNumber(brench.getOneOnonePromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.SMALL_CLASS)){
				brench.setSmallClassPromotionAmount(getRealNumber(brench.getSmallClassPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ECS_CLASS)){
				brench.setEscClassPromotionAmount(getRealNumber(brench.getEscClassPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ONE_ON_MANY)){
				brench.setOtmPromotionAmount(getRealNumber(brench.getOtmPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.LECTURE)){
				brench.setLecturePromotionAmount(getRealNumber(brench.getLecturePromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.OTHERS)){
				brench.setOthersPromotionAmount(getRealNumber(brench.getOthersPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}
			else if(messge.getType().equals(ProductType.LIVE)){
				brench.setLivePromotionAmount(getRealNumber(brench.getLivePromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}
			else if(messge.getType().equals(ProductType.TWO_TEACHER)){
				brench.setTwoTeacherPromotionAmount(getRealNumber(brench.getTwoTeacherPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}

		}else if(messge.getChargeType().equals(ChargeType.IS_NORMAL_INCOME) && messge.getPayType().equals(PayType.REAL)){
			brench.setIsNormalRealAmount(getRealNumber(brench.getIsNormalRealAmount(), messge.getAmount(), messge.getChargePayType()));
			brench.setRealTotalAmount(getRealNumber(brench.getRealTotalAmount(), messge.getAmount(), messge.getChargePayType()));
		}else if(messge.getChargeType().equals(ChargeType.IS_NORMAL_INCOME) && messge.getPayType().equals(PayType.PROMOTION)){
			brench.setIsNormalPromotionAmount(getRealNumber(brench.getIsNormalPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
		}
		
		return brench;
	}
	
	public synchronized void saveIncomeCountCampus(IncomeMessage messge){
		IncomeCountCampus campus=incomeCountCampusDao.findInfoByCampusAndDate(messge.getCampusId(),messge.getCountDate());
		campus=getNewIncomeCountCampus(campus, messge);
		incomeCountCampusDao.save(campus);
		incomeCountCampusDao.flush();
	}
	
	public synchronized IncomeCountCampus getNewIncomeCountCampus(IncomeCountCampus campus,IncomeMessage messge){
		if(campus!=null){
			campus.setModifyTime(DateTools.getCurrentDateTime());
		}else{
			campus=new IncomeCountCampus();
			campus.setCampusId(messge.getCampusId());
			campus.setCampusName(messge.getCampusName());
			campus.setCountDate(messge.getCountDate());
			campus.setCreateTime(DateTools.getCurrentDateTime());
			campus.setModifyTime(DateTools.getCurrentDateTime());
		}
		
		if(messge.getChargeType().equals(ChargeType.NORMAL) && messge.getPayType().equals(PayType.REAL)){
			if(messge.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
				campus.setOneOnoneRealAmount(getRealNumber(campus.getOneOnoneRealAmount(), messge.getAmount(), messge.getChargePayType()));
				campus.setOneOnOneQuantity(getRealNumber(campus.getOneOnOneQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.SMALL_CLASS)){
				campus.setSmallClassrealAmount(getRealNumber(campus.getSmallClassrealAmount(), messge.getAmount(), messge.getChargePayType()));
				campus.setSmallClassQuantity(getRealNumber(campus.getSmallClassQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ECS_CLASS)){
				campus.setEscClassRealAmount(getRealNumber(campus.getEscClassRealAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ONE_ON_MANY)){
				campus.setOtmRealAmount(getRealNumber(campus.getOtmRealAmount(), messge.getAmount(), messge.getChargePayType()));
				campus.setOtmQuantity(getRealNumber(campus.getOtmQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.LECTURE)){
				campus.setLectureRealAmount(getRealNumber(campus.getLectureRealAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.OTHERS)){
				campus.setOthersRealAmount(getRealNumber(campus.getOthersRealAmount(), messge.getAmount(), messge.getChargePayType()));
			}

			else if(messge.getType().equals(ProductType.LIVE)){
				campus.setLiveRealAmount(getRealNumber(campus.getLiveRealAmount(), messge.getAmount(), messge.getChargePayType()));
				campus.setLiveQuantity(getRealNumber(campus.getLiveQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}

			else if(messge.getType().equals(ProductType.TWO_TEACHER)){
				campus.setTwoTeacherRealAmount(getRealNumber(campus.getTwoTeacherRealAmount(), messge.getAmount(), messge.getChargePayType()));
				campus.setTwoTeacherQuantity(getRealNumber(campus.getTwoTeacherQuantity(), messge.getQuantity(), messge.getChargePayType()));
			}

			campus.setRealTotalAmount(getRealNumber(campus.getRealTotalAmount(), messge.getAmount(), messge.getChargePayType()));
		}else if(messge.getChargeType().equals(ChargeType.NORMAL) && messge.getPayType().equals(PayType.PROMOTION)){
			if(messge.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
				campus.setOneOnonePromotionAmount(getRealNumber(campus.getOneOnonePromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.SMALL_CLASS)){
				campus.setSmallClassPromotionAmount(getRealNumber(campus.getSmallClassPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ECS_CLASS)){
				campus.setEscClassPromotionAmount(getRealNumber(campus.getEscClassPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.ONE_ON_MANY)){
				campus.setOtmPromotionAmount(getRealNumber(campus.getOtmPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.LECTURE)){
				campus.setLecturePromotionAmount(getRealNumber(campus.getLecturePromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}else if(messge.getType().equals(ProductType.OTHERS)){
				campus.setOthersPromotionAmount(getRealNumber(campus.getOthersPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}

			else if(messge.getType().equals(ProductType.LIVE)){
				campus.setLivePromotionAmount(getRealNumber(campus.getLivePromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}
			else if(messge.getType().equals(ProductType.TWO_TEACHER)){
				campus.setTwoTeacherPromotionAmount(getRealNumber(campus.getTwoTeacherPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
			}
		}else if(messge.getChargeType().equals(ChargeType.IS_NORMAL_INCOME) && messge.getPayType().equals(PayType.REAL)){
			campus.setIsNormalRealAmount(getRealNumber(campus.getIsNormalRealAmount(), messge.getAmount(), messge.getChargePayType()));
			campus.setRealTotalAmount(getRealNumber(campus.getRealTotalAmount(), messge.getAmount(), messge.getChargePayType()));
		}else if(messge.getChargeType().equals(ChargeType.IS_NORMAL_INCOME) && messge.getPayType().equals(PayType.PROMOTION)){
			campus.setIsNormalPromotionAmount(getRealNumber(campus.getIsNormalPromotionAmount(), messge.getAmount(), messge.getChargePayType()));
		}
		return campus;
	}
	
	public synchronized BigDecimal getRealNumber(BigDecimal preNumber,BigDecimal nextNumber,ChargePayType type){
		if(type.equals(ChargePayType.CHARGE)){
			return preNumber.add(nextNumber);
		}else{
			return preNumber.subtract(nextNumber);
		}
		
		
	}
	
}
