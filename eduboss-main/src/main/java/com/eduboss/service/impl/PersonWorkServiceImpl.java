package com.eduboss.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.domainVo.WorkScheduleFromOAVo;
import com.eduboss.utils.*;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.xmlbeans.impl.util.Base64;
import org.codehaus.jackson.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.PersonWorkScheduleRecordDao;
import com.eduboss.domain.PersonWorkScheduleRecord;
import com.eduboss.domainVo.CountVo;
import com.eduboss.domainVo.PersonWorkScheduleRecordVo;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.PersonWorkService;
import com.eduboss.service.UserService;

import static com.eduboss.service.handler.ContractProductHandler.objectMapper;

/**
 * 个人工作日程
 * @author lixuejun
 * 2015-11-13
 */
@Service
public class PersonWorkServiceImpl implements PersonWorkService {
	
	@Autowired
	private PersonWorkScheduleRecordDao personWorkScheduleRecordDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MobileUserService mobileUserService;

	/**
	 * 获取指定时间内的个人工作日程
	 */
	@Override
	public List<PersonWorkScheduleRecord> getPersonWorkScheduleRecords(
			String startDate, String endDate) throws IOException {

		List<PersonWorkScheduleRecord> a = getPersonWorkScheduleRecordsFromOA(startDate, endDate, userService.getCurrentLoginUser().getUserId());
		return a;
//		return personWorkScheduleRecordDao.getPersonWorkScheduleRecords(startDate, endDate, userService.getCurrentLoginUser().getUserId());
	}



	/**
	 * 保存或修改个人工作日程
	 */
	@Override
	public void saveEditPersonWorkScheduleRecord(PersonWorkScheduleRecord record) throws IOException, IllegalAccessException {
		if(record.getIsDay()!=null && StringUtils.isNotBlank(record.getIsDay())){
			record.setScheduleStartTime("00:00");
			record.setScheduleEndTime("23:59");
		}
		if (StringUtil.isBlank(record.getId())) {
			record.setId(null);
			record.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			record.setCreateTime(DateTools.getCurrentDateTime());
			record.setModifyTime(DateTools.getCurrentDateTime());
			if(StringUtils.isBlank(record.getEndDate()) || record.getEndDate()==null){
				record.setEndDate(record.getStartDate());
			}else if(DateTools.daysBetween(record.getStartDate(), record.getEndDate())<0){
				record.setEndDate(record.getStartDate());
			}
			saveWorkScheduleToOA(record, userService.getCurrentLoginUser().getUserId());
//			personWorkScheduleRecordDao.save(record);
		} else {
			PersonWorkScheduleRecord recordInDb = findPersonWorkScheduleRecordById(record.getId());
//			PersonWorkScheduleRecord recordInDb = personWorkScheduleRecordDao.findById(record.getId());//
			recordInDb.setStartDate(record.getStartDate());
			if(StringUtils.isBlank(record.getEndDate()) || record.getEndDate()==null){
				record.setEndDate(record.getStartDate());
			}else if(DateTools.daysBetween(record.getStartDate(), record.getEndDate())<0){
				//结束日程小于开始日程
				record.setEndDate(record.getStartDate());
			}else{
				recordInDb.setEndDate(record.getEndDate());
			}
			
			recordInDb.setTitle(record.getTitle());
			recordInDb.setContent(record.getContent());
			recordInDb.setIconStr(record.getIconStr());
			recordInDb.setColorStr(record.getColorStr());
			recordInDb.setScheduleStartTime(record.getScheduleStartTime());
			recordInDb.setScheduleEndTime(record.getScheduleEndTime());
			recordInDb.setModifyTime(DateTools.getCurrentDateTime());
			recordInDb.setWorkEventType(record.getWorkEventType());
			recordInDb.setWorkPriority(record.getWorkPriority());
			recordInDb.setWorkRemindTime(record.getWorkRemindTime());
			recordInDb.setIsDay(record.getIsDay());
			saveWorkScheduleToOA(recordInDb, userService.getCurrentLoginUser().getUserId());
//			personWorkScheduleRecordDao.save(recordInDb);
		}
		
		
		/*if (StringUtil.isNotBlank(record.getId())) {
			PersonWorkScheduleRecord deleteRcord = new PersonWorkScheduleRecord();
			deleteRcord.setId(record.getId());
			personWorkScheduleRecordDao.delete(deleteRcord);
		}
		if (StringUtil.isBlank(record.getEndDate())) {
			record.setEndDate(record.getStartDate());
		}
		int days = DateTools.daysOfTwo(record.getStartDate(), record.getEndDate());
		PersonWorkScheduleRecord psRecord = null;
		if (days > 0) {
			for (int i=0; i<=days; i++) {
				String day = DateTools.addDateToString(record.getStartDate(), i);
				psRecord = new PersonWorkScheduleRecord();
				psRecord.setId(null);
				psRecord.setStartDate(day);
				psRecord.setEndDate(day);
				psRecord.setTitle(record.getTitle());
				psRecord.setContent(record.getContent());
				psRecord.setIconStr(record.getIconStr());
				psRecord.setColorStr(record.getColorStr());
				psRecord.setScheduleStartTime(record.getScheduleStartTime());
				psRecord.setScheduleEndTime(record.getScheduleEndTime());
				psRecord.setCreateUserId(userService.getCurrentLoginUser().getUserId());
				psRecord.setCreateTime(DateTools.getCurrentDateTime());
				psRecord.setModifyTime(DateTools.getCurrentDateTime());
				personWorkScheduleRecordDao.save(psRecord);
			}
		} else {
			record.setId(null);
			record.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			record.setCreateTime(DateTools.getCurrentDateTime());
			record.setModifyTime(DateTools.getCurrentDateTime());
			personWorkScheduleRecordDao.save(record);
		}*/
	}

	private void saveWorkScheduleToOA(PersonWorkScheduleRecord record, String userId) throws IOException, IllegalAccessException {

		WorkScheduleFromOAVo workScheduleFromOAVo = HibernateUtils.voObjectMapping(record, WorkScheduleFromOAVo.class);

		String url = PropertiesUtils.getStringValue("OA_HOST")+"/saveOAWorkSchedule";

		HttpClient client = wrapHttpClient();

		Field[] fields=workScheduleFromOAVo.getClass().getDeclaredFields();

		HttpPost httpPost = new HttpPost(url);
		setRequestHeader(httpPost);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userId", userId));
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.get(workScheduleFromOAVo) != null && StringUtils.isNotBlank(field.get(workScheduleFromOAVo).toString())){
				nvps.add(new BasicNameValuePair(field.getName(), field.get(workScheduleFromOAVo).toString()));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

		HttpResponse getResponse = client.execute(httpPost);

//		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//			System.out.println(getResponse.getStatusLine().getStatusCode());
//		}
	}

	private static HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}

	private static void setRequestHeader(HttpRequest getrequest){
		String auth = PropertiesUtils.getStringValue("API_USER")+":" +  PropertiesUtils.getStringValue("API_PWD");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		getrequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
//		getrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	}


	/**
	 * 删除个人工作日程
	 */
	@Override
	public void deletePersonWorkScheduleRecord(PersonWorkScheduleRecord record) throws IOException {
		if (StringUtil.isNotBlank(record.getId())) {
			String userId = userService.getCurrentLoginUser().getUserId();
//			personWorkScheduleRecordDao.delete(record);
			String url = PropertiesUtils.getStringValue("OA_HOST")+"/deleteOAWorkSchedule?id="+record.getId()+"&userId="+userId;
			HttpClient client = wrapHttpClient();
			HttpPost httpPost = new HttpPost(url);
			setRequestHeader(httpPost);
			HttpResponse getResponse = client.execute(httpPost);
			if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				System.out.println(getResponse.getStatusLine().getStatusCode());
			}
		}
	}

	private List<PersonWorkScheduleRecord> getPersonWorkScheduleRecordsFromOA(String startDate, String endDate, String userId) throws IOException {
		String url = PropertiesUtils.getStringValue("OA_HOST")+"/getOAPersonWorkSechedules?startDate="+startDate+"&endDate="+endDate+"&userId="+userId;

		List<PersonWorkScheduleRecord> list = new ArrayList<>();
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		setRequestHeader(getrequest);
		HttpResponse getResponse = client.execute(getrequest);
		String str = EntityUtils.toString(getResponse.getEntity());
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK && StringUtils.isNotBlank(str)){
			try {
				WorkScheduleFromOAVo[] workScheduleFromOAVos = objectMapper.readValue(str, WorkScheduleFromOAVo[].class);
				for (WorkScheduleFromOAVo vo : workScheduleFromOAVos){
					list.add(HibernateUtils.voObjectMapping(vo, PersonWorkScheduleRecord.class));
				}
			}catch (Exception e) {
				e.printStackTrace();
			}


		}
		return list;
	}
	
	/**
	 * 根据id查询个人工作日程
	 */
	@Override
	public PersonWorkScheduleRecord findPersonWorkScheduleRecordById(String id) throws IOException {
		String userId = userService.getCurrentLoginUser().getUserId();
		String url = PropertiesUtils.getStringValue("OA_HOST")+"/findOAWorkScheduleById?id="+id+"&userId="+userId;
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		setRequestHeader(getrequest);
		HttpResponse getResponse = client.execute(getrequest);
		String str = EntityUtils.toString(getResponse.getEntity());
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK && StringUtils.isNotBlank(str)){

			try {
				WorkScheduleFromOAVo workScheduleFromOAVo = objectMapper.readValue(str, WorkScheduleFromOAVo.class);
				PersonWorkScheduleRecord personWorkScheduleRecord = HibernateUtils.voObjectMapping(workScheduleFromOAVo, PersonWorkScheduleRecord.class);
				return personWorkScheduleRecord;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		return personWorkScheduleRecordDao.findById(id);
		return null;
	}	
	
	/**
	 * 供手机端做日程标记
	 */
	public List<PersonWorkScheduleRecordVo> getPersonWorkScheduleRecordsAndDates(String startDate, String endDate){
		if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
			List<PersonWorkScheduleRecord> personWorks=personWorkScheduleRecordDao.getPersonWorkScheduleRecords(startDate,endDate,userService.getCurrentLoginUser().getUserId());
			List<PersonWorkScheduleRecordVo> volist=HibernateUtils.voListMapping(personWorks, PersonWorkScheduleRecordVo.class);
			Set<String> allDates=new HashSet<String>();
			if(volist!=null && volist.size()>0){
				for(PersonWorkScheduleRecordVo person:volist){
					String workStartDate=person.getStartDate();
					String workEndDate=person.getEndDate();		
					int num=DateTools.daysBetween(workStartDate, workEndDate);
						if(workEndDate !=null && StringUtils.isNotBlank(workEndDate) && num>=0){
							person.getWorkDates().add(workStartDate);
							allDates.add(workStartDate);
							while(!workStartDate.equals(workEndDate)){
								 workStartDate=DateTools.addDateToString(workStartDate, 1);
								person.getWorkDates().add(workStartDate);
								allDates.add(workStartDate);
							}	
						}
//						else if(num<0){
//							throw new ApplicationException("有结束时间早于开始时间的数据");
//						}
						else {
							person.getWorkDates().add(workStartDate);
							allDates.add(workStartDate);
						}																		
								
				}
				volist.get(0).setWorkDates(allDates);
				return volist;
			}else{
				return new ArrayList<PersonWorkScheduleRecordVo>();
			}			
		}else{
			return new ArrayList<PersonWorkScheduleRecordVo>();
		}
		
		
		
	}
	
	/**
	 * APP获取登录人当天的工作日程个数
	 */
	@Override
	public CountVo getTodayWorkNumberByUserId(String userId,String workDate){
		CountVo countVo=new CountVo();
		int count=personWorkScheduleRecordDao.getTodayWorkNumberByUserId(userId, workDate);
		countVo.setCount(count);
		return countVo;
	}
}
