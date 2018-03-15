package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.InventoryOperateType;
import com.eduboss.dao.InventoryManagerDao;
import com.eduboss.dao.InventoryProductDao;
import com.eduboss.dao.InventoryRecordDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.domain.InventoryManager;
import com.eduboss.domain.InventoryProduct;
import com.eduboss.domain.InventoryRecord;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.InventoryManagerVo;
import com.eduboss.domainVo.InventoryProductVo;
import com.eduboss.domainVo.InventoryRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.InventoryService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service
public class InventoryServiceImpl implements InventoryService{
	
	@Autowired
	private InventoryRecordDao inventoryRecordDao;
	
	@Autowired
	private InventoryProductDao inventoryProductDao;
	
	@Autowired
	private InventoryManagerDao inventoryManagerDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private StudentDao studentDao;

	/**********************************库存管理开始*********************************************************/
	
	/**
	 * 库存管理列表
	 * */
	@Override
	public DataPackage getInventoryRecordForGrid(InventoryRecordVo inventoryRecordVo, DataPackage dp) {
		return inventoryRecordDao.getInventoryRecordForGrid(inventoryRecordVo, dp);
	}

	/**
	 * 保存库存产品
	 * */
	@Override
	public void saveInventoryProduct(InventoryProduct product) {
		inventoryProductDao.saveInventoryProduct(product);
	}

	/**
	 * 分页查询库存产品
	 * */
	@Override
	public DataPackage getInventoryProductForGrid(InventoryProductVo inventoryProductVo, DataPackage dp) {
		return inventoryProductDao.getInventoryProductForGrid(inventoryProductVo, dp);
	}

	/**
	 * 新增库存记录
	 * */
	@Override
	public void saveInventoryRecord(InventoryRecord inventoryRecord) {
		/*
		DataPackage dp = new DataPackage();
		InventoryRecord oldRecord = null;
		if(StringUtils.isNotBlank(inventoryRecord.getId())){
			oldRecord = inventoryRecordDao.findById(inventoryRecord.getId());
		}else{
			InventoryRecordVo inventoryRecordVo = new InventoryRecordVo();
			inventoryRecordVo.setOrganizationId(inventoryRecord.getOrganization().getId());
			inventoryRecordVo.setInventoryProductId(inventoryRecord.getInventoryProduct().getId());
			List<InventoryRecord> list = inventoryRecordDao.getInventoryRecordForList(inventoryRecordVo, dp);
			//List<InventoryRecord> list = (List<InventoryRecord>) dp.getDatas();
			if(list!=null && list.size()>0){
				oldRecord = list.get(0);
			}
		}
		
		if(oldRecord != null){
			double oldNumber = oldRecord.getNumber();
			double oldPrice =  oldRecord.getPrice();
			oldRecord.setNewInventoryTime(DateTools.getCurrentDateTime());
			oldRecord.setNumber(oldNumber+inventoryRecord.getNumber());
			//重新计算单价
			oldRecord.setPrice((oldNumber*oldPrice+inventoryRecord.getNumber()*inventoryRecord.getPrice())/(oldNumber+inventoryRecord.getNumber()));
			oldRecord.setModifyTime(DateTools.getCurrentDateTime());
			oldRecord.setModifyUser(userService.getCurrentLoginUser());
			inventoryRecordDao.saveInventoryRecord(oldRecord);
		}else{
			inventoryRecord.setNewInventoryTime(DateTools.getCurrentDateTime());
			inventoryRecord.setCreateTime(DateTools.getCurrentDateTime());
			inventoryRecord.setCreateUser(userService.getCurrentLoginUser());
			inventoryRecordDao.saveInventoryRecord(inventoryRecord);
		}*/
		inventoryRecordDao.saveInventoryRecord(inventoryRecord);
		
		//新增一条入库记录
		InventoryManager manager = new InventoryManager();
		manager.setResourceInventory(inventoryRecord);
		manager.setTargetInventory(inventoryRecord);
		manager.setCreateTime(DateTools.getCurrentDateTime());
		manager.setCreateUser(userService.getCurrentLoginUser());
		manager.setOperationType(InventoryOperateType.WAREHOUSING);
		manager.setNumber(inventoryRecord.getNumber());
		inventoryManagerDao.save(manager);
	}
	
	/**
	 * 移库
	 * */
	@Override
	public void moveInventoryRecord(InventoryManager inventoryManager){
		//源仓库更新数量
		if(inventoryManager.getResourceInventory() != null && StringUtils.isNotBlank(inventoryManager.getResourceInventory().getId())){
			InventoryRecord record = inventoryRecordDao.findById(inventoryManager.getResourceInventory().getId());
			record.setNumber(record.getNumber()-inventoryManager.getNumber());
			inventoryRecordDao.save(record);
			inventoryRecordDao.flush();
		} else {
			throw new ApplicationException("源库 ID 不能为 空！");
		}
		//目的仓库更新数量 
		if(inventoryManager.getTargetInventory() != null && StringUtils.isNotBlank(inventoryManager.getTargetInventory().getId())){
			InventoryRecord record = inventoryRecordDao.findById(inventoryManager.getTargetInventory().getId());
			record.setNewInventoryTime(DateTools.getCurrentDateTime());
			record.setNumber(record.getNumber()+inventoryManager.getNumber());
		}else{
			InventoryRecord newInvRecord = inventoryManager.getTargetInventory();
			newInvRecord.setNumber(inventoryManager.getNumber());
			newInvRecord = inventoryRecordDao.saveInventoryRecord(newInvRecord);
			
			//String id = saveInventoryRecord(record);
//			InventoryRecord rc = new InventoryRecord();
//			rc.setId(id);
			inventoryManager.setTargetInventory(newInvRecord);
		}
		
		/*
		if(inventoryManager.getTargetInventory() != null && StringUtils.isNotBlank(inventoryManager.getTargetInventory().getId())){
			InventoryRecord record = inventoryRecordDao.findById(inventoryManager.getTargetInventory().getId());
			record.setNumber(record.getNumber()+inventoryManager.getNumber());
			inventoryRecordDao.save(record);
		}else{//如果目的仓库不存在，则新建一个
			InventoryRecord record = inventoryManager.getTargetInventory();
			saveInventoryRecord(record);
		}*/
		//保存移库记录
		//inventoryManager.setTargetInventory(targetInventory)
		inventoryManager.setOperationType(InventoryOperateType.MOVEINVENTORY);
		inventoryManager.setCreateTime(DateTools.getCurrentDateTime());
		inventoryManager.setCreateUser(userService.getCurrentLoginUser());
		inventoryManagerDao.save(inventoryManager);
	}
	
	/**
	 * 报损
	 * */
	@Override
	public void destroyInventoryRecord(InventoryManager inventoryManager){
		//更新源仓库数量
		double number = inventoryManager.getNumber();
		InventoryRecord record = new InventoryRecord();
		if(inventoryManager.getResourceInventory() != null && StringUtils.isNotBlank(inventoryManager.getResourceInventory().getId())){
			record = inventoryRecordDao.findById(inventoryManager.getResourceInventory().getId());
			record.setNumber(record.getNumber()-number);
			inventoryRecordDao.save(record);
		}
		
		//新增一条报损记录
		inventoryManager.setTargetInventory(record);
		inventoryManager.setCreateTime(DateTools.getCurrentDateTime());
		inventoryManager.setCreateUser(userService.getCurrentLoginUser());
		inventoryManager.setOperationType(InventoryOperateType.DAMAGE);
		inventoryManagerDao.save(inventoryManager);
	}
	
	/**
	 * 销售
	 * */
	@Override
	public void saleInventoryRecord(InventoryManager inventoryManager){
		//更新源仓库数量
		InventoryRecord record = new InventoryRecord();
		if(inventoryManager.getResourceInventory() != null && StringUtils.isNotBlank(inventoryManager.getResourceInventory().getId())){
			record = inventoryRecordDao.findById(inventoryManager.getResourceInventory().getId());
			record.setNumber(record.getNumber()-inventoryManager.getNumber());
			inventoryRecordDao.save(record);
		}
		//新增一条销售记录
		inventoryManager.setTargetInventory(record);
		inventoryManager.setCreateTime(DateTools.getCurrentDateTime());
		inventoryManager.setCreateUser(userService.getCurrentLoginUser());
		inventoryManager.setOperationType(InventoryOperateType.SALE);
		inventoryManagerDao.save(inventoryManager);
		
		
		
	}
	
	/**
	 * 领用
	 * */
	@Override
	public void consumeInventoryRecord(InventoryManager inventoryManager){
		//更新源仓库数量
		InventoryRecord record = new InventoryRecord();
		if(inventoryManager.getResourceInventory() != null && StringUtils.isNotBlank(inventoryManager.getResourceInventory().getId())){
			record = inventoryRecordDao.findById(inventoryManager.getResourceInventory().getId());
			record.setNumber(record.getNumber()-inventoryManager.getNumber());
			inventoryRecordDao.save(record);
		}
		//新增一条领用记录
		inventoryManager.setTargetInventory(record);
		inventoryManager.setCreateTime(DateTools.getCurrentDateTime());
		inventoryManager.setCreateUser(userService.getCurrentLoginUser());
		inventoryManager.setOperationType(InventoryOperateType.CONSUME);
		inventoryManagerDao.save(inventoryManager);
	}
	
	/**
	 * 退回
	 * */
	@Override
	public void backInventoryRecord(InventoryManager inventoryManager){
		//更新源仓库数量
		if(inventoryManager.getResourceInventory() != null && StringUtils.isNotBlank(inventoryManager.getResourceInventory().getId())){
			InventoryRecord record = inventoryRecordDao.findById(inventoryManager.getResourceInventory().getId());
			record.setNumber(record.getNumber()-inventoryManager.getNumber());
			inventoryRecordDao.save(record);
		}
		//更新目的仓库数量
		if(inventoryManager.getTargetInventory() != null && StringUtils.isNotBlank(inventoryManager.getTargetInventory().getId())){
			InventoryRecord record = inventoryRecordDao.findById(inventoryManager.getTargetInventory().getId());
			record.setNewInventoryTime(DateTools.getCurrentDateTime());
			record.setNumber(record.getNumber()+inventoryManager.getNumber());
		}else{
			InventoryRecord record1 = inventoryManager.getTargetInventory();
			record1.setNumber(inventoryManager.getNumber());
			record1 = inventoryRecordDao.saveInventoryRecord(record1);
			
			//String id = saveInventoryRecord(record);
//			InventoryRecord rc = new InventoryRecord();
//			rc.setId(id);
			inventoryManager.setTargetInventory(record1);
		}
		
		
		//新增一条退回记录
		inventoryManager.setCreateTime(DateTools.getCurrentDateTime());
		inventoryManager.setCreateUser(userService.getCurrentLoginUser());
		inventoryManager.setOperationType(InventoryOperateType.RETURN);
		inventoryManagerDao.save(inventoryManager);
	}

	@Override
	public DataPackage getInventoryManagerForGrid(InventoryManagerVo inventoryManagerVo, DataPackage dp) {
		dp = inventoryManagerDao.getInventoryManagerForGrid(inventoryManagerVo, dp);
		List<InventoryManager> list = (List<InventoryManager>) dp.getDatas();
		List<InventoryManagerVo> voList = new ArrayList<InventoryManagerVo>();
		for(InventoryManager manager : list){
			InventoryManagerVo vo = HibernateUtils.voObjectMapping(manager, InventoryManagerVo.class);
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public List<AutoCompleteOptionVo> getStudentForPerformance(String input) {
//			List<User> resList =userDao.getLimitUserAutoComplate(term, roleCode, parentOrgId);
		List<Student> resList = studentDao.getStudentForPerform(input);
		List<AutoCompleteOptionVo> voList = new ArrayList<AutoCompleteOptionVo>();
			
			for(Student stu : resList){
				String label = "";
				AutoCompleteOptionVo stuPer  = new AutoCompleteOptionVo();
				stuPer.setValue(stu.getId());
				label += stu.getName();
				label += "("+stu.getBlCampus().getName()+")";
				//label += stu.getGradeDict().getName()+")";
//				if(StringUtils.isNotBlank(user.getRoleId())){
//					Role role=roleDao.findById(user.getRoleId());
//					if(role !=null )
//						label += role.getName()+")";
//				}
				stuPer.setLabel(label);
				voList.add(stuPer);
			}
			return voList;
	}

	@Override
	public List<Organization> getOrganizationForInventory(Organization org) {
//		Organization campus = new Organization();
//		campus.setOrgLevel(userService.getCurrentLoginUser().getOrgLevelId());
		return organizationDao.getOrganizationBoy(org, org);
	}

	
	
	/**********************************库存管理结束*********************************************************/
}
