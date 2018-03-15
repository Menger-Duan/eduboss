package com.eduboss.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.PosMachineStatus;
import com.eduboss.dao.PosMachineDao;
import com.eduboss.domain.PosMachine;
import com.eduboss.domain.PosMachineManage;
import com.eduboss.domain.PosPayData;
import com.eduboss.domain.User;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.eo.PosPayDataImportedEo;
import com.eduboss.excel.imported.ExcelImporter;
import com.eduboss.excel.imported.ExecuteFunction;
import com.eduboss.excel.imported.PoiExcelImporter;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.PosMachineManageService;
import com.eduboss.service.PosMachineService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.base.Function;

@Service
public class PosMachineServiceImpl implements PosMachineService {

	@Autowired
	private PosMachineDao posMachineDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PosMachineManageService posMachineManageService;
	
	@Autowired
	private RedisDataSource redisDataSource;
	
	@Autowired
	private OperationCountService operationCountService;
	
	private Logger logger = Logger.getLogger(PosMachineServiceImpl.class);
	
	/**
	 * 新增终端
	 * @param posMachineVo
	 */
	@Override
	public Response savePosMachineManage(PosMachineVo posMachineVo) {
		
		String currentTime = DateTools.getCurrentDateTime();
		User curerntUser = userService.getCurrentLoginUser();
		PosMachine posMachine = HibernateUtils.voObjectMapping(posMachineVo, PosMachine.class);
		posMachine.setStartTime(posMachineVo.getStartDate() + " " + posMachineVo.getStartDateTime() + ":00");
		posMachine.setEndTime(posMachineVo.getEndDate() + " " + posMachineVo.getEndDateTime() + ":00");
		posMachine.setStatus(PosMachineStatus.ACTIVATE);
		posMachine.setCreateTime(currentTime);
		posMachine.setCreateUser(curerntUser);
		posMachine.setModifyTime(currentTime);
		posMachine.setModifyUser(curerntUser);
		PosMachineManage posMachineManage = HibernateUtils.voObjectMapping(posMachineVo, PosMachineManage.class);
		posMachineManage.setStartTime(posMachineVo.getStartDate() + " " + posMachineVo.getStartDateTime() + ":00");
		posMachineManage.setEndTime(posMachineVo.getEndDate() + " " + posMachineVo.getEndDateTime() + ":00");
		posMachineManage.setStatus(PosMachineStatus.ACTIVATE);
		posMachineManage.setCreateTime(currentTime);
		posMachineManage.setCreateUser(curerntUser);
		posMachineManage.setModifyTime(currentTime);
		posMachineManage.setModifyUser(curerntUser);
		posMachineManageService.savePosMachineManage(posMachineManage);
		posMachineDao.save(posMachine);
		return new Response();
	}
	
	/**
	 * 删除pos终端
	 */
	@Override
	public void deletePosMachine(PosMachineVo posMachineVo) {
		PosMachine posMachine = HibernateUtils.voObjectMapping(posMachineVo, PosMachine.class);
		posMachineDao.delete(posMachine);
	}
	
	/**
	 * 新增或修改pos终端使用日期
	 */
	@Override
	public Response saveOrUpdatePosMachine(PosMachineVo posMachineVo) {
		PosMachine posMachine = HibernateUtils.voObjectMapping(posMachineVo, PosMachine.class);
		posMachine.setStartTime(posMachineVo.getStartDate() + " " + posMachineVo.getStartDateTime() + ":00");
		posMachine.setEndTime(posMachineVo.getEndDate() + " " + posMachineVo.getEndDateTime() + ":59");
		String currentTime = DateTools.getCurrentDateTime();
		User curerntUser = userService.getCurrentLoginUser();
		if (posMachineDao.findPosMachineConflictCount(posMachine) > 0) {
			throw new ApplicationException("pos终端使用日期不可以有重复日期");
		}
		if (posMachine.getId() > 0) {
			PosMachine posMachineInDb = posMachineDao.findById(posMachine.getId());
			posMachine.setStatus(posMachineInDb.getStatus());
			posMachine.setCreateTime(posMachineInDb.getCreateTime());
			posMachine.setCreateUser(posMachineInDb.getCreateUser());
		} else {
			posMachine.setStatus(PosMachineStatus.ACTIVATE);
			posMachine.setCreateTime(currentTime);
			posMachine.setCreateUser(curerntUser);
		}
		posMachine.setModifyTime(currentTime);
		posMachine.setModifyUser(curerntUser);
		if (posMachine.getId() > 0) {
			posMachineDao.merge(posMachine);
		} else {
			posMachineDao.save(posMachine);
		}
		if (posMachine.getPosType() != null || posMachine.getType() != null) {
		    posMachineManageService.updatePosTypeByPosNumber(posMachine.getPosNumber(), posMachine.getPosType(), posMachine.getType());
		}
		return new Response();
	}
	
	/**
	 * 查找pos终端使用日期列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage findPagePosMachine(DataPackage dp, String posNumber) {
		dp = posMachineDao.findPagePosMachine(dp, posNumber);
		List<PosMachineVo> voList =  HibernateUtils.voListMapping((List<PosMachine>)dp.getDatas(), PosMachineVo.class);
		dp.setDatas(voList);
		return dp;
	}
	
	/**
	 * 根据id查找pos终端使用日期
	 */
	@Override
	public PosMachineVo findPosMachineById(Integer id) {
		PosMachine posMachine = posMachineDao.findById(id);
		return HibernateUtils.voObjectMapping(posMachine, PosMachineVo.class);
	}
	
	/**
	 * 禁用，激活终端的使用日期
	 * @param posNumber
	 * @param status
	 */
	@Override
	public void chagePosMachineByPosNumberStatus(String posNumber, PosMachineStatus status) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "update pos_machine set STATUS = :status WHERE POS_NUMBER = :posNumber ";
		params.put("status", status);
		params.put("posNumber", posNumber);
		posMachineDao.excuteSql(sql, params);
	}
	
	/**
	 * 导入银联反馈数据
     */
	@SuppressWarnings("unchecked")
	@Override
	public Map<Boolean, String> importPosPayDataFromExcel(File file) throws Exception {
		final Map<Boolean, String> resultMap = new HashMap<>();

		String excel = "posPayDataImport";
		String table = "pos_pay_data";
		if (JedisUtil.exists(excel)){
			logger.error("存在导入");
			resultMap.put(false, "系统正在执行一次银联反馈数据批量excel导入，请稍后再试。");
			return resultMap;
		}else {
			try {
				JedisUtil.set(excel, excel);
				if (StringUtils.isBlank(JedisUtil.get(excel))) {
					logger.error("redis set失败");
					resultMap.put(false, "系统出错，请联系管理员。");
					return resultMap;
				}
				final List<PosPayData> canLoadIntoDB = new ArrayList<>();
				final Map<String, String> distinctPosPayDataMap = new HashMap<>();
				final Map<String, String> checkPusNumberMap = new HashMap<String, String>();
				List<Map<Object, Object>> posNumberInDB = posMachineDao.findMapBySql("SELECT POS_NUMBER FROM pos_machine WHERE STATUS = 'ACTIVATE' AND START_TIME <= CURRENT_TIMESTAMP() AND END_TIME >= CURRENT_TIMESTAMP(); ", new HashMap<String, Object>());
				List<Map<Object,Object>> posPayDataInDB = posMachineDao.findMapBySql("select POS_ID, POS_NUMBER from "+table+" ; ", new HashMap<String, Object>());
				for (Map<Object,Object> map:posNumberInDB) {
					checkPusNumberMap.put(map.get("POS_NUMBER").toString(), "1");
				}
				for (Map<Object,Object> map:posPayDataInDB){
					distinctPosPayDataMap.put(map.get("POS_ID") + "_" + map.get("POS_NUMBER"),"1");
				}
				Function<PosPayDataImportedEo, PosPayData> transferFunction = new Function<PosPayDataImportedEo, PosPayData>(){
					@Override
					public PosPayData apply(PosPayDataImportedEo eo) {
						if (!eo.getValidated()){
							return null;
						}

						PosPayData dto = new PosPayData();
						String posId = eo.getPosId().trim();
						posId = posId.replaceFirst("^0*", "");
						dto.setPosId(posId);
						
						String regebDate8 = "^\\d{8}$";
						String regebDate14 = "^\\d{14}$";
						String regebDate = "(19|20)[0-9][0-9]-[0-9][0-9]-[0-9][0-9]";
						String posTime = eo.getPosTime().trim();
						if (posTime.matches(regebDate8) || posTime.matches(regebDate14)) {
							posTime = posTime.substring(0, 8);
						} else if (posTime.matches(regebDate)) {
							posTime = posTime.replaceAll("-", "");
						} else {
							eo.setValidated(false);
							eo.appendValidateMsg("交易时间格式不正确");
							if (resultMap.get(true) == null) {
								resultMap.put(true, "存在导入失败记录，请点击“导入结果下载”查看");
							}
							return null;
						}
						dto.setPosTime(posTime);
						String amount = eo.getAmount().trim().replaceAll(",", "");
						try {
							dto.setAmount(new BigDecimal(amount));
						} catch(Exception e) {
							eo.setValidated(false);
							eo.appendValidateMsg("金额不符合格式");
							if (resultMap.get(true) == null) {
								resultMap.put(true, "存在导入失败记录，请点击“导入结果下载”查看");
							}
							return null;
						}
						dto.setPosNumber(eo.getPosNumber().trim());
						if (distinctPosPayDataMap.get(dto.getPosId() + "_" + dto.getPosNumber())!=null){
							eo.setValidated(false);
							eo.appendValidateMsg("系统流水号与终端号重复了");
							if (resultMap.get(true) == null) {
								resultMap.put(true, "存在导入失败记录，请点击“导入结果下载”查看");
							}
							return null;
						} else {
							distinctPosPayDataMap.put(dto.getPosId() + "_" + dto.getPosNumber(),"1");
						}
						if (checkPusNumberMap.get(dto.getPosNumber()) == null) {
							eo.setValidated(false);
							eo.appendValidateMsg("终端编号无效或没录入系统");
							if (resultMap.get(true) == null) {
								resultMap.put(true, "存在导入失败记录，请点击“导入结果下载”查看");
							}
							return null;
						}
						dto.setPosAccount(eo.getPosAccount().trim());
						dto.setMerchantName(eo.getMerchantName().trim());
						String poundage = eo.getPoundage().trim().replaceAll(",", "");
						try {
							dto.setPoundage(new BigDecimal(poundage));
						} catch(Exception e) {
							eo.setValidated(false);
							eo.appendValidateMsg("手续费不符合格式");
							if (resultMap.get(true) == null) {
								resultMap.put(true, "存在导入失败记录，请点击“导入结果下载”查看");
							}
							return null;
						}
						return  dto;
					}
				};
				ExecuteFunction<PosPayData> dbExecuteFunction = new ExecuteFunction<PosPayData>() {
					@Override
					public void execute(PosPayData t) throws Exception {
						canLoadIntoDB.add(t);
					}
				};
				ExcelImporter<PosPayDataImportedEo, PosPayData> importer = new PoiExcelImporter<>(true);
				importer.setMetaData(PosPayDataImportedEo.class).loadExcel(new FileInputStream(file)).moveToTargetRow(2).parse().addTransferFunction(transferFunction)
						.executeInFeedback(dbExecuteFunction).feebback2Excel().export2Stream(new FileOutputStream(file)).clearExcelWorkBook().closeExcelStream();

				loadDataInFile(canLoadIntoDB, table);
				if (resultMap.get(true) == null) {
					resultMap.put(true, "导入成功");
				}
				operationCountService.updatePosPayDataBlCampusId();
				return resultMap;
			}catch (Exception e){
				e.printStackTrace();
				logger.error("出错了");
				if (resultMap.get(true) != null) {
					resultMap.remove(true);
				}
				resultMap.put(false, "系统出错了");
				return resultMap;
			}finally {
				JedisUtil.del(excel);
				
			}
		}

	}
	
	/**
	 * 校验并导入银联数据
	 */
	@Override
	public void importPosPayDataFromList(Map<Integer, PosPayData> loadIntoDBMap, String fileName) {
		if (loadIntoDBMap.size() > 0) {
			String table = "pos_pay_data";
			final List<PosPayData> canLoadIntoDB = new ArrayList<>();
			final Map<String, String> distinctPosPayDataMap = new HashMap<>();
			final Map<String, String> checkPusNumberMap = new HashMap<String, String>();
			List<Map<Object, Object>> posNumberInDB = posMachineDao.findMapBySql("SELECT POS_NUMBER FROM pos_machine WHERE STATUS = 'ACTIVATE' AND START_TIME <= CURRENT_TIMESTAMP() AND END_TIME >= CURRENT_TIMESTAMP(); ", new HashMap<String, Object>());
			List<Map<Object,Object>> posPayDataInDB = posMachineDao.findMapBySql("select POS_ID, POS_NUMBER, POS_TIME from "+table+" ; ", new HashMap<String, Object>());
			for (Map<Object,Object> map:posNumberInDB) {
				checkPusNumberMap.put(map.get("POS_NUMBER").toString(), "1");
			}
			for (Map<Object,Object> map:posPayDataInDB){
				distinctPosPayDataMap.put(map.get("POS_ID") + "_" + map.get("POS_NUMBER") + "_" + map.get("POS_TIME"),"1");
			}
			boolean canAdd = true; 
			for (Integer key : loadIntoDBMap.keySet()) {
				canAdd = true; 
				PosPayData dto = loadIntoDBMap.get(key);
				if (distinctPosPayDataMap.get(dto.getPosId() + "_" + dto.getPosNumber() + "_" + dto.getPosTime())!=null){
					logger.info("导入" + fileName + "第" + key + "行，系统流水号,终端号和支付时间重复了");
					canAdd = false;
				} else {
					distinctPosPayDataMap.put(dto.getPosId() + "_" + dto.getPosNumber(),"1");
				}
				if (checkPusNumberMap.get(dto.getPosNumber()) == null) {
					logger.info("导入" + fileName + "第" + key + "行，终端编号无效或没录入系统");
					canAdd = false;
				}
				if (canAdd) {
					canLoadIntoDB.add(dto);
				}
			}
			if (canLoadIntoDB.size() > 0) {
				try {
					loadDataInFile(canLoadIntoDB, table);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			operationCountService.updatePosPayDataBlCampusId();
		}
	}
	
	/**
	 * 硬编码
	 * @param canLoadIntoDB
	 * @throws SQLException
     */
	private void loadDataInFile(List<PosPayData> canLoadIntoDB, String table) throws SQLException {
		String fieldsterminated = "\t";
		String linesterminated = "\n";
		String loadDataSql = "LOAD DATA LOCAL INFILE 'sql.csv' INTO TABLE "+table+" FIELDS TERMINATED BY '"
				+ fieldsterminated + "'  LINES TERMINATED BY '" + linesterminated
				+ "' (POS_ID, POS_TIME, AMOUNT, POS_NUMBER, POS_ACCOUNT, MERCHANT_NAME, POUNDAGE, CREATE_USER_ID, CREATE_TIME) ";
		Connection con = null;
		try{
			con = DriverManager.getConnection(PropertiesUtils.getStringValue("jdbc.writeUrl"), PropertiesUtils.getStringValue("jdbc.username"), PropertiesUtils.getStringValue("jdbc.password"));
			con.setAutoCommit(false);
			PreparedStatement pt = con.prepareStatement(loadDataSql);
			com.mysql.jdbc.PreparedStatement mysqlStatement = null;
			if (pt.isWrapperFor(com.mysql.jdbc.Statement.class)) {
				mysqlStatement = pt.unwrap(com.mysql.jdbc.PreparedStatement.class);
			}

			String createUserId = userService.getCurrentLoginUser() != null ? userService.getCurrentLoginUser().getUserId() : "112233";

			int i = 0;
			int batchSize = 1000;
			StringBuilder builder = new StringBuilder(10000);
			if (canLoadIntoDB != null && canLoadIntoDB.size() > 0) {
				for (PosPayData posPayData : canLoadIntoDB){
					builder.append(posPayData.getPosId());
					builder.append("\t");
					builder.append(posPayData.getPosTime());
					builder.append("\t");
					builder.append(posPayData.getAmount());
					builder.append("\t");
					builder.append(posPayData.getPosNumber());
					builder.append("\t");
					builder.append(posPayData.getPosAccount());
					builder.append("\t");
					builder.append(posPayData.getMerchantName());
					builder.append("\t");
					builder.append(posPayData.getPoundage());
					builder.append("\t");
					builder.append(createUserId); // 创建用户ID
					builder.append("\t");
					builder.append(DateTools.getCurrentDateTime()); //创建时间
					builder.append("\t");
					builder.append("NOT_YET_MATCH");
					builder.append("\t");
					builder.append("");
					builder.append("\t");
					builder.append("\n");
					if (i % batchSize == 1){
						byte[] bytes = builder.toString().getBytes();
						InputStream in = new ByteArrayInputStream(bytes);
						mysqlStatement.setLocalInfileInputStream(in);
						mysqlStatement.executeUpdate();
						con.commit();
						builder = new StringBuilder(10000);
					}
					i++;
				}
				byte[] bytes = builder.toString().getBytes();
				InputStream in = new ByteArrayInputStream(bytes);
				mysqlStatement.setLocalInfileInputStream(in);
				mysqlStatement.executeUpdate();
				con.commit();
			}
		}finally {
			con.close();
		}

	}
	
	/**
	 * 根据校区查找pusNumber的下拉框
	 */
	@Override
	public SelectOptionResponse getPosNumSelectOptionByCampusId(String campusId, String typeId) {
		List<NameValue> nvs = new ArrayList<NameValue>();
		if (StringUtil.isNotBlank(typeId)) {
		    List<PosMachine> list = posMachineDao.findPosMachineListByCampusId(campusId, typeId);
		    for (PosMachine posMachine : list) {
		        nvs.add(SelectOptionResponse.buildNameValue(posMachine.getPosNumber(), posMachine.getPosNumber()));
		    }
		    
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
}
