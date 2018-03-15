package com.eduboss.service.impl;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.UserAmountRankInfo;
import com.eduboss.jedis.Message;
import com.eduboss.service.StaffBonusDayService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("StaffBonusDayService")
public class StaffBonusDayServiceImpl implements StaffBonusDayService {

	@Autowired
	private StaffBonusDayDao staffBonusDayDao;
	
	@Autowired
	private StaffBonusWeekDao staffBonusWeekDao;
	
	@Autowired
	private StaffBonusMonthDao staffBonusMonthDao;
	

	@Autowired
	private OrganizationDao organizationDao;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public synchronized int saveOrUpdateStaffBonus(Message message) {
		int year = 0;
		int month = 0;
		int week = 0;
		int weekYear=0;
		int lastWeekDay=0;//上周最后一天是今年的的几周
		int lastWeekYear=0;//上周最后一天是哪一年
		year=DateTools.getYearOfDate(message.getDateTime());
		month=DateTools.getMonthOfDate(message.getDateTime());
		try {//得到周的年份，因为如果是2015-12-31，这个周其实是2016的第一周
			weekYear=DateTools.getWeekLastDay(message.getDateTime());
			lastWeekDay=DateTools.getLastWeekEndDay(message.getDateTime());
			lastWeekYear=DateTools.getLastWeekEndYear(message.getDateTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		week=DateTools.getWeeksOfDateMonth(message.getDateTime());
		
		BigDecimal realMoney = BigDecimal.ZERO;
		if (message.getFlag() != null && message.getFlag().equals("1")) {// 业绩增加
			realMoney = realMoney.add(message.getMoney());
		} else {
			realMoney = realMoney.subtract(message.getMoney());
		}
		if(StringUtils.isNotBlank(message.getUserId())){//用户名字
			User user = userService.findUserById(message.getUserId());
			if(user!=null){
				message.setUserName(user.getName());
			}
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
			}else if(or!=null){
				message.setBrenchId(or.getId());
				message.setBrenchName(or.getName());
			}
		}
		
		saveDayInfo(message, realMoney);
		saveWeekInfo(message, realMoney, week, weekYear,lastWeekDay,lastWeekYear);
		saveMonthInfo(message, realMoney, month, year);
		return 0;
	}
	
	/** 
	 * 保存日统计信息
	* @param message
	* @param realMoney
	* @author  author :Yao 
	* @date  2016年7月11日 下午4:25:02 
	* @version 1.0 
	*/
	public synchronized void saveDayInfo(Message message,BigDecimal realMoney){
		StaffBonusDay inf = staffBonusDayDao.findInfoByDateAndStaff(
				message.getDateTime(), message.getUserId());
		if (inf != null) {
			inf.setAmount(inf.getAmount().add(realMoney));
			inf.setModifyTime(DateTools.getCurrentDateTime());
		} else {// 新增
			inf = new StaffBonusDay();
			inf.setUserId(message.getUserId());
			inf.setOrgId(message.getCampusId());
			inf.setCountDate(message.getDateTime());
			inf.setUserName(message.getUserName());
			inf.setOrgName(message.getCampusName());
			inf.setAmount(realMoney);
			inf.setBrenchId(message.getBrenchId());
			inf.setBrenchName(message.getBrenchName());
			inf.setCreateTime(DateTools.getCurrentDateTime());
			inf.setOnlineAmount(BigDecimal.ZERO);
			inf.setLineAmount(BigDecimal.ZERO);
		}
		if(message.getOnline()){
			inf.setOnlineAmount(inf.getOnlineAmount().add(realMoney));
		}else{
			inf.setLineAmount(inf.getLineAmount().add(realMoney));
		}
		staffBonusDayDao.save(inf);
	}
	
	/** 
	 * 保存周统计信息
	* @param message
	* @param realMoney
	* @author  author :Yao 
	* @date  2016年7月11日 下午4:25:02 
	* @version 1.0 
	*/
	public synchronized void saveWeekInfo(Message message,BigDecimal realMoney,int week,int year,int lastWeek,int lastYear){
		StaffBonusWeek inf = staffBonusWeekDao.findInfoByDateAndStaff(message.getUserId(),week,year);
		if (inf != null) {
			inf.setAmount(inf.getAmount().add(realMoney));
			inf.setModifyTime(DateTools.getCurrentDateTime());
		} else {// 新增
			inf = new StaffBonusWeek();
			inf.setUserId(message.getUserId());
			inf.setOrgId(message.getCampusId());
			inf.setYear(year);
			inf.setWeek(week);
			inf.setUserName(message.getUserName());
			inf.setOrgName(message.getCampusName());
			inf.setAmount(realMoney);
			inf.setCreateTime(DateTools.getCurrentDateTime());
			inf.setBrenchId(message.getBrenchId());
			inf.setBrenchName(message.getBrenchName());
			inf.setOnlineAmount(BigDecimal.ZERO);
			inf.setLineAmount(BigDecimal.ZERO);
		}
		if(message.getOnline()){
			inf.setOnlineAmount(inf.getOnlineAmount().add(realMoney));
		}else{
			inf.setLineAmount(inf.getLineAmount().add(realMoney));
		}
		List<StaffBonusWeek> lastWeekList   = staffBonusWeekDao.findUserWeekRankList(year,week,message.getUserId(),"");//上一周的排名
		StaffBonusWeek lastWeekInfo=new StaffBonusWeek();
		if(lastWeekList.size()>0){
			lastWeekInfo=lastWeekList.get(0);
		}
		
		staffBonusWeekDao.save(inf);
		staffBonusWeekDao.flush();
		//从数据库中找出一个比当前金额大的一条记录，然后再找出一条比当前金额小的记录，  如果大记录没有，排名就是1，小记录没有排名就是最小
		//如果排名是1 ，与上一名比是0，下一名如果存在，就要把下一名的改了。
		List<StaffBonusWeek> weekList   = staffBonusWeekDao.findUserWeekRankList(year,week,"","");//集团排名列表
		List<StaffBonusWeek> brenchWeekList   = staffBonusWeekDao.findUserWeekRankList(year,week,"",message.getBrenchId());//分公司排名列表
		this.updateWeekRankByUserId(weekList,lastWeekInfo);//更新集团排名
		this.updateBrenchWeekRankByUserId(brenchWeekList, lastWeekInfo);//更新分公司排名
	}
	
	/** 
	 * 更新top10排名
	* @param weekList
	* @author  author :Yao
	* @date  2016年7月12日 上午10:16:30 
	* @version 1.0 
	*/
	public synchronized void updateWeekRankByUserId(List<StaffBonusWeek> weekList,StaffBonusWeek lastWeekInfo){
		int i=1;
		BigDecimal beforeAmount=BigDecimal.ZERO;
		for (StaffBonusWeek weeks : weekList) {
			if(i==1){
				weeks.setBalance(BigDecimal.ZERO);
			}else{
				weeks.setBalance(beforeAmount.subtract(weeks.getAmount()));
			}
			weeks.setRank(i);
			if(lastWeekInfo!=null && StringUtils.isNotBlank(lastWeekInfo.getUserId()) &&
				lastWeekInfo.getRank()>0 && lastWeekInfo.getUserId().equals(weeks.getUserId())){//与上周排名比较
				weeks.setUpDown(lastWeekInfo.getRank()-weeks.getRank());//负数是下降，正数上升   
			}
			beforeAmount=weeks.getAmount();
			staffBonusWeekDao.save(weeks);
			i++;
		}
	}
	
	/** 
	 * 更新周分公司top10排名
	* @param weekList
	* @author  author :Yao
	* @date  2016年7月12日 上午10:16:30 
	* @version 1.0 
	*/
	public synchronized void updateBrenchWeekRankByUserId(List<StaffBonusWeek> weekList,StaffBonusWeek lastWeekInfo){
		int i=1;
		BigDecimal beforeAmount=BigDecimal.ZERO;
		for (StaffBonusWeek weeks : weekList) {
			if(i==1){
				weeks.setBrenchBalance(BigDecimal.ZERO);
			}else{
				weeks.setBrenchBalance(beforeAmount.subtract(weeks.getAmount()));
			}
			weeks.setBrenchRank(i);
			if(lastWeekInfo!=null && StringUtils.isNotBlank(lastWeekInfo.getUserId()) &&
				lastWeekInfo.getBrenchRank()>0 && lastWeekInfo.getUserId().equals(weeks.getUserId())){//与上周排名比较
				weeks.setBrenchUpDown(lastWeekInfo.getBrenchRank()-weeks.getBrenchRank());//负数是下降，正数上升   
			}
			beforeAmount=weeks.getAmount();
			staffBonusWeekDao.save(weeks);
			i++;
		}
	}
	
	/** 
	 * 保存月统计信息
	* @param message
	* @param realMoney
	* @author  author :Yao 
	* @date  2016年7月11日 下午4:25:02 
	* @version 1.0 
	*/
	public synchronized void saveMonthInfo(Message message,BigDecimal realMoney,int month,int year){
		int lastMonth=1;
		int lastYear=0;
		if(month==1){
			lastMonth=12;
			lastYear=year-1;
		}else{
			lastMonth=month-1;
			lastYear=year;
		}
		
		
		StaffBonusMonth inf = staffBonusMonthDao.findInfoByDateAndStaff(message.getUserId(),month,year);
		if (inf != null) {
			inf.setAmount(inf.getAmount().add(realMoney));
			inf.setModifyTime(DateTools.getCurrentDateTime());
		} else {// 新增
			inf = new StaffBonusMonth();
			inf.setUserId(message.getUserId());
			inf.setOrgId(message.getCampusId());
			inf.setYear(year);
			inf.setMonth(month);
			inf.setUserName(message.getUserName());
			inf.setOrgName(message.getCampusName());
			inf.setAmount(realMoney);
			inf.setBrenchId(message.getBrenchId());
			inf.setBrenchName(message.getBrenchName());
			inf.setCreateTime(DateTools.getCurrentDateTime());
			inf.setOnlineAmount(BigDecimal.ZERO);
			inf.setLineAmount(BigDecimal.ZERO);
		}
		if(message.getOnline()){
			inf.setOnlineAmount(inf.getOnlineAmount().add(realMoney));
		}else{
			inf.setLineAmount(inf.getLineAmount().add(realMoney));
		}
		List<StaffBonusMonth> lastMonthList   = staffBonusMonthDao.findUserMonthRankList(lastYear,lastMonth,message.getUserId(),"");//上一月的排名
		StaffBonusMonth lastMonthInfo=new StaffBonusMonth();
		if(lastMonthList.size()>0){
			lastMonthInfo=lastMonthList.get(0);
		}
		
		
		staffBonusMonthDao.save(inf);
		staffBonusMonthDao.flush();
		
		List<StaffBonusMonth> monthList = staffBonusMonthDao.findUserMonthRankList(year,month,"","");//集团排名
		List<StaffBonusMonth> brenchMonthList = staffBonusMonthDao.findUserMonthRankList(year,month,"",message.getBrenchId());//分公司排名
		this.updateMonthRankByUserId(monthList,lastMonthInfo);
		this.updateBrenchMonthRankByUserId(brenchMonthList, lastMonthInfo);
	}
	
	
	/** 
	 * 更新月top10排名
	* @param monthList
	* @author  author :Yao
	* @date  2016年7月12日 上午10:16:30 
	* @version 1.0 
	*/
	public synchronized void updateMonthRankByUserId(List<StaffBonusMonth> monthList,StaffBonusMonth lastMonthInfo){
		int i=1;
		BigDecimal beforeAmount=BigDecimal.ZERO;
		for (StaffBonusMonth month : monthList) {
			if(i==1){
				month.setBalance(BigDecimal.ZERO);
			}else{
				month.setBalance(beforeAmount.subtract(month.getAmount()));
			}
			month.setRank(i);
			if(lastMonthInfo!=null && StringUtils.isNotBlank(lastMonthInfo.getUserId()) &&
					lastMonthInfo.getRank()>0 && lastMonthInfo.getUserId().equals(month.getUserId())){//与上周排名比较
					month.setUpDown(lastMonthInfo.getRank()-month.getRank());//负数是下降，正数上升   
				}
			beforeAmount=month.getAmount();
			staffBonusMonthDao.save(month);
			i++;
		}
	}
	
	/** 
	 * 更新月分公司top10排名
	* @param monthList
	* @author  author :Yao
	* @date  2016年7月12日 上午10:16:30 
	* @version 1.0 
	*/
	public synchronized void updateBrenchMonthRankByUserId(List<StaffBonusMonth> monthList,StaffBonusMonth lastMonthInfo){
		int i=1;
		BigDecimal beforeAmount=BigDecimal.ZERO;
		for (StaffBonusMonth month : monthList) {
			if(i==1){
				month.setBrenchBalance(BigDecimal.ZERO);
			}else{
				month.setBrenchBalance(beforeAmount.subtract(month.getAmount()));
			}
			month.setBrenchRank(i);
			if(lastMonthInfo!=null && StringUtils.isNotBlank(lastMonthInfo.getUserId()) &&
					lastMonthInfo.getBrenchRank()>0 && lastMonthInfo.getUserId().equals(month.getUserId())){//与上周排名比较
					month.setBrenchUpDown(lastMonthInfo.getBrenchRank()-month.getBrenchRank());//负数是下降，正数上升   
				}
			beforeAmount=month.getAmount();
			staffBonusMonthDao.save(month);
			i++;
		}
	}

	@Override
	public List<UserAmountRankInfo> getRankWithOrgAndTimeSpanOrgs(String startDate,String endDate, String branch, String role) {
		User curUser=userService.getCurrentLoginUser();
		
		int year =DateTools.getYearOfDate(endDate);
		int month =DateTools.getMonthOfDate(endDate);
		int week =DateTools.getWeeksOfDateMonth(endDate);
		int days=DateTools.daysBetween(startDate, endDate);//获取两个日期间的天数
		
		List<UserAmountRankInfo> returnList= new ArrayList<UserAmountRankInfo>();
		
		RoleCode roleCode=null;
		if(role.equals("1")) roleCode=null;
		else if(role.equals("2")) roleCode=RoleCode.CONSULTOR;
		else if(role.equals("3")) roleCode=RoleCode.STUDY_MANAGER;
		else return null;
		
		if(StringUtils.isNotBlank(branch) && branch.indexOf(",")>-1){
			branch=branch.split(",")[0];
		}
		if(days==6){//周排名
			getWeekTop10Data(endDate, branch, roleCode, curUser, year, week, returnList);
		}else if(days>7){//月排名
			getMonthTop10Data(branch, roleCode, curUser, year, month, returnList);
		}else if(days==0){//天排名
			getDayTop10Data(startDate, branch, roleCode, curUser, returnList);
		}else{
			return returnList;
		}
		
		return returnList;
	}
	
	
	/** 
	 * 获取周top10
	* @param endDate
	* @param branch
	* @param roleCode
	* @param curUser
	* @param year
	* @param week
	* @param returnList
	* @author  author :Yao 
	* @date  2016年8月3日 下午6:13:12 
	* @version 1.0 
	*/
	public List<UserAmountRankInfo> getWeekTop10Data(String endDate,String branch,RoleCode roleCode,User curUser,int year,int week, List<UserAmountRankInfo> returnList){
		List<StaffBonusWeek> weekList=new ArrayList<StaffBonusWeek>();
		List<StaffBonusWeek> oldWeekList=new ArrayList<StaffBonusWeek>();
		Map<String,Integer> oldRankMap=new HashMap<String,Integer>();
		BigDecimal beforAmount=BigDecimal.ZERO;
		Boolean flag=false;
		int lastRank=0;
		int lastWeekDay=0;//上周最后一天是今年的的几周
		int lastWeekYear=0;//上周最后一天是哪一年
		int weekYear=year;//本日期的年份
		try {//得到周的年份，因为如果是2015-12-31，这个周其实是2016的第一周
			lastWeekDay=DateTools.getLastWeekEndDay(endDate);
			lastWeekYear=DateTools.getLastWeekEndYear(endDate);
			weekYear=DateTools.getWeekLastDay(endDate);
			oldWeekList=staffBonusWeekDao.findAllUserWeekRankList(lastWeekYear,lastWeekDay,branch,roleCode,curUser.getUserId());
			weekList=staffBonusWeekDao.findAllUserWeekRankList(weekYear,week,branch,roleCode,curUser.getUserId());
			Integer j=1;
			for (StaffBonusWeek oldWeek : oldWeekList) {
				oldRankMap.put(oldWeek.getUserId(), j);
				j++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		int i=1;
		for (StaffBonusWeek staffBonusWeek : weekList) {
			UserAmountRankInfo info=HibernateUtils.voObjectMapping(staffBonusWeek, UserAmountRankInfo.class);
				info.setOrgName(staffBonusWeek.getBrenchName()+" - "+staffBonusWeek.getOrgName());
				info.setRandId(i);
				if(i==1){
					info.setBalance(BigDecimal.ZERO);
				}else{
					info.setBalance(beforAmount.subtract(info.getAmount()));
				}
				if(oldRankMap.get(info.getUserId())!=null){
					info.setUpDown(oldRankMap.get(info.getUserId())-i);
				}else{
					info.setUpDown(0);
				}
				beforAmount=info.getAmount();
				
				if(staffBonusWeek.getUserId().equals(curUser.getUserId())){
					info.setIsMe("1");
					flag=true;
				}
				
				//如果超过10名还没有找到自己的排名，那就不再往返回结果里面加，直到找到自己的排名，加入结果集后，再break;  所以结果集可能会是11条
				if(i>10 && !flag){
					if(lastRank==0 && info.getAmount().compareTo(BigDecimal.ZERO)<0){//记录大于0的最后一个名次
						lastRank=i;
					}
					i++;
					continue;
				}else if(i>=10 && flag){
					returnList.add(info);
					break;
				}
				
				i++;
			
			returnList.add(info);
		}
		if(!flag){//如果是空 加一个自己的数据
			UserAmountRankInfo info=new UserAmountRankInfo();
			if(lastRank>0){//如果有小于0的业绩人，那么排名就是在第一个小于0的业绩人之前
				info.setRandId(lastRank);
				info.setBalance(BigDecimal.ZERO);
			}else{//没有小于0 的业绩人，那么就是最后一名，金额用最后的金额来算
				info.setRandId(weekList.size()+1);
				info.setBalance(beforAmount);
			}
			info.setUserId(curUser.getUserId());
			info.setUserName(curUser.getName());
			info.setOrgId(curUser.getOrganizationId());
			info.setOrgName(userService.getCurrentLoginUserOrganization().getName()+" - ");
			info.setAmount(BigDecimal.ZERO);
			if(oldRankMap.get(curUser.getUserId())!=null){//与上一次排名比较     
				info.setUpDown(oldRankMap.get(curUser.getUserId())-weekList.size());
			}else{
				info.setUpDown(0);
			}
			flag=true;
			info.setIsMe("1");
			returnList.add(info);
		}
		return returnList;
	}
	
	
	/** 
	 * 日排名
	* @param startDate
	* @param branch
	* @param roleCode
	* @param curUser
	* @param returnList
	* @return
	* @author  author :Yao 
	* @date  2016年8月3日 下午6:28:46 
	* @version 1.0 
	*/
	public List<UserAmountRankInfo> getDayTop10Data(String startDate,String branch,RoleCode roleCode,User curUser, List<UserAmountRankInfo> returnList){
		String preDate=DateTools.addDateToString(startDate, -1);
		List<StaffBonusDay> dayList = staffBonusDayDao.findUserDayRankList(startDate,branch,roleCode,curUser.getUserId());//当前要查的数据
		List<StaffBonusDay> oldList = staffBonusDayDao.findUserDayRankList(preDate,branch,roleCode,curUser.getUserId());// 前一日的数据
		
		Integer old=1;
		Map<String,Integer> rankMap=new HashMap<String,Integer>();//上一期的排名
		for (StaffBonusDay staffBonusDay : oldList) {
			rankMap.put(staffBonusDay.getUserId(), old);
			old++;
		}
		
		Integer i=1;
		BigDecimal beforAmount=BigDecimal.ZERO;
		Boolean flag=false;//用来记录是否前10存在自己的记录
		BigDecimal lastAmount=BigDecimal.ZERO;//最后一名的金额
		Integer lastRank=0;
		for (StaffBonusDay staffBonusDay : dayList) {
			UserAmountRankInfo info=HibernateUtils.voObjectMapping(staffBonusDay, UserAmountRankInfo.class);
			info.setOrgName(staffBonusDay.getBrenchName()+" - "+staffBonusDay.getOrgName());
			info.setRandId(i);
			if(i==1){
				info.setBalance(beforAmount);
			}else{
				info.setBalance(beforAmount.subtract(info.getAmount()));
			}
			beforAmount=staffBonusDay.getAmount();
			if(rankMap.get(staffBonusDay.getUserId())!=null){//与上一次排名比较     
				info.setUpDown(rankMap.get(staffBonusDay.getUserId())-i);
			}else{
				info.setUpDown(0);
			}
			
			if(staffBonusDay.getUserId().equals(curUser.getUserId())){
				flag=true;
				info.setIsMe("1");
			}
			
			//如果超过10名还没有找到自己的排名，那就不再往返回结果里面加，直到找到自己的排名，加入结果集后，再break;  所以结果集可能会是11条
			if(i>10 && !flag){
				if(lastRank==0 && info.getAmount().compareTo(BigDecimal.ZERO)<0){//记录大于0的最后一个名次
					lastRank=i;
				}
				i++;
				continue;
			}else if(i>=10 && flag){
				returnList.add(info);
				break;
			}
			
			if(i==dayList.size() && !flag){//如果是最后一名了。并且没有当前登录人的数据
				lastAmount=staffBonusDay.getAmount();
			}
			
			returnList.add(info);
			i++;
		}
		
		if(!flag){//如果是空 加一个自己的数据
			UserAmountRankInfo info=new UserAmountRankInfo();
			if(lastRank>0){//如果有小于0的业绩人，那么排名就是在第一个小于0的业绩人之前
				info.setRandId(lastRank);
				info.setBalance(BigDecimal.ZERO);
			}else{//没有小于0 的业绩人，那么就是最后一名，金额用最后的金额来算
				info.setRandId(dayList.size()+1);
				info.setBalance(lastAmount);
			}
			info.setUserId(curUser.getUserId());
			info.setUserName(curUser.getName());
			info.setOrgId(curUser.getOrganizationId());
			info.setOrgName(userService.getCurrentLoginUserOrganization().getName()+" - ");
			info.setAmount(BigDecimal.ZERO);
			if(rankMap.get(curUser.getUserId())!=null){//与上一次排名比较     
				info.setUpDown(rankMap.get(curUser.getUserId())-dayList.size());
			}else{
				info.setUpDown(0);
			}
			flag=true;
			info.setIsMe("1");
			returnList.add(info);
		}
		return returnList;
	}
	
	/** 
	 * 月排名
	* @param branch
	* @param roleCode
	* @param curUser
	* @param year
	* @param month
	* @param returnList
	* @return
	* @author  author :Yao 
	* @date  2016年8月3日 下午6:31:12 
	* @version 1.0 
	*/
	public List<UserAmountRankInfo> getMonthTop10Data(String branch,RoleCode roleCode,User curUser,int year,int month, List<UserAmountRankInfo> returnList){
		List<StaffBonusMonth> monthList = new ArrayList<StaffBonusMonth>();
		List<StaffBonusMonth> oldMonthList = new ArrayList<StaffBonusMonth>();
		Map<String,Integer> oldRankMap=new HashMap<String,Integer>();
		BigDecimal beforAmount=BigDecimal.ZERO;
		Boolean flag=false;
		int lastRank=0;
			int lastMonth=1;
			int lastYear=0;
			if(month==1){
				lastMonth=12;
				lastYear=year-1;
			}else{
				lastMonth=month-1;
				lastYear=year;
			}
			monthList=staffBonusMonthDao.findAllUserMonthRankList(year,month,branch,roleCode,curUser.getUserId());
			oldMonthList=staffBonusMonthDao.findAllUserMonthRankList(lastYear,lastMonth,branch,roleCode,curUser.getUserId());
			Integer j=1;
			for (StaffBonusMonth oldMonth : oldMonthList) {
				oldRankMap.put(oldMonth.getUserId(), j);
				j++;
			}
		int i=1;
		for (StaffBonusMonth staffBonusMonth : monthList) {
			UserAmountRankInfo info=HibernateUtils.voObjectMapping(staffBonusMonth, UserAmountRankInfo.class);
				info.setOrgName(staffBonusMonth.getBrenchName()+" - "+staffBonusMonth.getOrgName());
				info.setRandId(i);
				if(i==1){
					info.setBalance(BigDecimal.ZERO);
				}else{
					info.setBalance(beforAmount.subtract(info.getAmount()));
				}
				if(oldRankMap.get(info.getUserId())!=null){
					info.setUpDown(oldRankMap.get(info.getUserId())-i);
				}else{
					info.setUpDown(0);
				}
				beforAmount=info.getAmount();
				
				
				if(info.getUserId().equals(curUser.getUserId())){
					info.setIsMe("1");
					flag=true;
				}
				
				//如果超过10名还没有找到自己的排名，那就不再往返回结果里面加，直到找到自己的排名，加入结果集后，再break;  所以结果集可能会是11条
				if(i>10 && !flag){
					if(lastRank==0 && info.getAmount().compareTo(BigDecimal.ZERO)<0){//记录大于0的最后一个名次
						lastRank=i;
					}
					i++;
					continue;
				}else if(i>=10 && flag){
					returnList.add(info);
					break;
				}
				i++;
			returnList.add(info);
		}
		if(!flag){//如果是空 加一个自己的数据
			UserAmountRankInfo info=new UserAmountRankInfo();
			if(lastRank>0){//如果有小于0的业绩人，那么排名就是在第一个小于0的业绩人之前
				info.setRandId(lastRank);
				info.setBalance(BigDecimal.ZERO);
			}else{//没有小于0 的业绩人，那么就是最后一名，金额用最后的金额来算
				info.setRandId(monthList.size()+1);
				info.setBalance(beforAmount);
			}
			info.setUserId(curUser.getUserId());
			info.setUserName(curUser.getName());
			info.setOrgId(curUser.getOrganizationId());
			info.setOrgName(userService.getCurrentLoginUserOrganization().getName()+" - ");
			info.setAmount(BigDecimal.ZERO);
			if(oldRankMap.get(curUser.getUserId())!=null){//与上一次排名比较     
				info.setUpDown(oldRankMap.get(curUser.getUserId())-monthList.size());
			}else{
				info.setUpDown(0);
			}
			flag=true;
			info.setIsMe("1");
			returnList.add(info);
		}
		return returnList;
	}
	
	public void getWeekRankByRoleCode(UserAmountRankInfo realInfo,String endDate,String branch,String userId,RoleCode roleCode){
		int lastWeekDay=0;//上周最后一天是今年的的几周
		int lastWeekYear=0;//上周最后一天是哪一年
		try {//得到周的年份，因为如果是2015-12-31，这个周其实是2016的第一周
			lastWeekDay=DateTools.getLastWeekEndDay(endDate);
			lastWeekYear=DateTools.getLastWeekEndYear(endDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<StaffBonusWeek> weekList=staffBonusWeekDao.findUserWeekRankList(lastWeekYear,lastWeekDay,branch,roleCode,userId);
		for (StaffBonusWeek staffBonusWeek : weekList) {
			UserAmountRankInfo info=HibernateUtils.voObjectMapping(staffBonusWeek, UserAmountRankInfo.class);
			
			if(roleCode==null){
				if(StringUtils.isNotBlank(branch)){
					info.setRandId(staffBonusWeek.getBrenchRank());
					info.setBalance(staffBonusWeek.getBrenchBalance());
					info.setUpDown(staffBonusWeek.getBrenchUpDown());
				}else{
					info.setRandId(staffBonusWeek.getRank());
				}
				if(staffBonusWeek.getUserId().equals(userId)){
					info.setIsMe("1");
				}
			}else{
				
			}
		}
		
	}
	
	/* 
	 * 跑数据用的
	 * (non-Javadoc)
	 * @see com.eduboss.service.StaffBonusDayService#updateTop10Rank()
	 */
	@Override
	public void updateTop10Rank(String type) throws SQLException {
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		if(StringUtils.isNotBlank(type) && "week".equals(type)){
			StringBuffer sql=new StringBuffer();
			sql.append(" select year,week from staff_bonus_week group by year,week ");
			List<Map<Object,Object >> weekMap=staffBonusWeekDao.findMapBySql(sql.toString(), new HashMap<String, Object>());
			for (Map<Object, Object> map : weekMap) {
				String prosql = "{call updateRank(?,?)}";
				List listParam = new ArrayList<Object>();
				listParam.add(map.get("year"));
				listParam.add(map.get("week"));
				procedureDao.executeProc(prosql, listParam);
			}
			StringBuffer sql2=new StringBuffer();
			sql2.append(" select year,week,brench_id from staff_bonus_week group by year,week,brench_id ");
			List<Map<Object,Object >> weekMap2=staffBonusWeekDao.findMapBySql(sql2.toString(), new HashMap<String, Object>());
			
			for (Map<Object, Object> map : weekMap2) {
				String prosql = "{call updateWeekBrenchRank(?,?,?)}";
				List listParam = new ArrayList<Object>();
				listParam.add(map.get("year"));
				listParam.add(map.get("week"));
				listParam.add(map.get("brench_id"));
				procedureDao.executeProc(prosql, listParam);
			}
		}
		
		if(StringUtils.isNotBlank(type) && "month".equals(type)){
			StringBuffer sql3=new StringBuffer();
			sql3.append(" select year,month from staff_bonus_month group by year,month ");
			
			List<Map<Object,Object >> montMap=staffBonusWeekDao.findMapBySql(sql3.toString(), new HashMap<String, Object>());
			for (Map<Object, Object> map : montMap) {
				String prosql = "{call updateMonthRank(?,?)}";
				List listParam = new ArrayList<Object>();
				listParam.add(map.get("year"));
				listParam.add(map.get("month"));
				procedureDao.executeProc(prosql, listParam);
			}
			
			StringBuffer sql4=new StringBuffer();
			sql4.append(" select year,month,brench_id from staff_bonus_month group by year,month,brench_id ");
			List<Map<Object,Object >> montMap2=staffBonusWeekDao.findMapBySql(sql4.toString(), new HashMap<String, Object>());
			for (Map<Object, Object> map : montMap2) {
				String prosql = "{call updateMonthBrenchRank(?,?,?)}";
				List listParam = new ArrayList<Object>();
				listParam.add(map.get("year"));
				listParam.add(map.get("month"));
				listParam.add(map.get("brench_id"));
				procedureDao.executeProc(prosql, listParam);
			}
		}
	}
	
	
	/** 
	 * Pc端top10
	* @param startDate
	* @param endDate
	* @param branch
	* @param roleCode
	* @return
	* @author  author :Yao 
	* @date  2016年7月21日 下午4:25:31 
	* @version 1.0 
	*/
	@Override
	public List<Map<Object,Object>> getTop10ForPC(String startDate,String endDate, Boolean isBranch, RoleCode roleCode){
		User curUser=userService.getCurrentLoginUser();
		String blBranch = null;
		if (isBranch != null && !isBranch) {
			blBranch = userService.getBelongBranch().getId();
		}
		
		List<Map<Object,Object>> returnList=new ArrayList<Map<Object,Object>>();
		List<Map<Object,Object>> dayList = staffBonusDayDao.findUserDayRankList(startDate,endDate,blBranch,roleCode,curUser.getUserId());//当前要查的数据
		Integer i=1;
		Boolean flag=false;//用来记录是否前10存在自己的记录
		Integer lastRank=0;
		for (Map<Object,Object> map : dayList) {
			if(map.get("isMe")!=null && map.get("isMe").toString().equals("1")){
				flag=true;
			}
			map.put("rank", i);
			//如果超过10名还没有找到自己的排名，那就不再往返回结果里面加，直到找到自己的排名，加入结果集后，再break;  所以结果集可能会是11条
			if(i>10 && !flag){
				i++;
				continue;
			}else if(i>=10 && flag){
				returnList.add(map);
				break;
			}
			map.put("userIdHeadImg", PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_"+map.get("userId")+".jpg");
			returnList.add(map);
			i++;
		}
		
		if(!flag){//如果是空 加一个自己的数据
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("userId", curUser.getUserId());
			map.put("username", curUser.getName());
			map.put("orgid",curUser.getOrganizationId());
			map.put("orgName",userService.getCurrentLoginUserOrganization().getName());
			map.put("rank", dayList.size()+1);
			map.put("amount", 0);
			map.put("onlineAmount", 0);
			map.put("lineAmount", 0);
			map.put("isMe", "1");
			map.put("userIdHeadImg", PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_"+map.get("userId")+".jpg");
			returnList.add(map);
		}
		
		return returnList;
	}
	
}
