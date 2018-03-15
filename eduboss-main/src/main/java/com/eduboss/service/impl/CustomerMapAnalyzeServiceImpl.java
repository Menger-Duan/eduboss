package com.eduboss.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.eduboss.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.CustomerMapAnalyzeDao;
import com.eduboss.domain.CustomerMapAnalyze;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.CustomerMapAnalyzeService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.hibernate.Query;

@Service("CustomerMapAnalyzeService")
public class CustomerMapAnalyzeServiceImpl implements CustomerMapAnalyzeService{
	
	@Autowired
	private CustomerMapAnalyzeDao customerMapAnalyzeDao;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Transactional
	@Override
	public List<Map<String,Object>> TransactionAddressToLatitude() {
		/**
		//List<CustomerMapAnalyze> list = customerMapAnalyzeDao.TransactionAddressToLatitude();
		StringBuilder hql = new StringBuilder();
		hql.append(" from CustomerMapAnalyze where id>=").append(begin).append(" and id<").append(end);
		List<CustomerMapAnalyze> list = customerMapAnalyzeDao.findAllByHQL(hql.toString());
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
//		List<CustomerMapAnalyzeVo> voList = new ArrayList<CustomerMapAnalyzeVo>();
//		for(CustomerMapAnalyze cus : list){
//			CustomerMapAnalyzeVo vo = HibernateUtils.voObjectMapping(cus, CustomerMapAnalyzeVo.class);
//			voList.add(vo);
//		}
		for(CustomerMapAnalyze cus : list){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", cus.getId());
			if(StringUtil.isNotBlank(cus.getFatherAddress())){
				map.put("address", cus.getFatherAddress());
			}else{
				map.put("address", cus.getMotherAddress());
			}
			maps.add(map);
		}
		return maps;**/
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select count(id) from CustomerMapAnalyze");
		int totalCount = customerMapAnalyzeDao.findCountHql(hql.toString(),params);
		int batchCount = totalCount/1000;
		if(totalCount%1000>0){
			batchCount += 1;
		}
		for(int i=0; i<batchCount; i++){
			DataPackage dataPackage = new DataPackage();
			dataPackage.setPageNo(i);
			dataPackage.setPageSize(1000);
			dataPackage = customerMapAnalyzeDao.findPageByCriteria(dataPackage, 
					HibernateUtils.prepareOrder(dataPackage, "id", "asc"),
					new ArrayList<Criterion>());
			List<CustomerMapAnalyze> list = (List<CustomerMapAnalyze>) dataPackage.getDatas();
			int j = 0;
			for(CustomerMapAnalyze cus : list){
				j = j+1;
				String address = cus.getFatherAddress()==null?cus.getMotherAddress():cus.getFatherAddress();
				try {
					String[] result = doGet(address);
					params.put("result0"+i+j, result[0]);
					params.put("result1"+i+j, result[1]);
					params.put("cusId"+i+j, cus.getId());
					customerMapAnalyzeDao.excuteSql("UPDATE student_parent_info_01 SET LONGITUDE = :result0"+i+j+" , LATITUDE = :result1"+i+j+"  WHERE ID= :cusId"+i+j+" ",params );
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
//			customerMapAnalyzeDao.flush();
			customerMapAnalyzeDao.commit();
		}
		
		

		return null;
		
	}

	
	/**
	 * 保存客户经纬度
	 * */
	@Override
	public void saveCustomerLatitude(String latitudes) {
		//String sql = "";
		String[] arr = latitudes.split(",");
		Session session=customerMapAnalyzeDao.getHibernateTemplate().getSessionFactory().openSession();
        Transaction tr=session.beginTransaction();
        //tr.begin();
        try{
            String sql="UPDATE student_parent_info_01 SET LONGITUDE = ?, LATITUDE = ?  WHERE ID= ? ";   
           for(int k=0; k< arr.length; k++){
        	   String[] str = arr[k].split(":");
        	   session.createSQLQuery(sql)
	            .setParameter(0,str[1])
	            .setParameter(1, str[2])
	            .setParameter(2, str[0])
	            .executeUpdate();  
        	   if(k%100==0){
                   tr.commit();
                   session.flush();
                   session.clear();
                   tr=session.beginTransaction();
        	   }
           }
           tr.commit();
           session.flush();
           session.clear();
        }catch(Exception e){
           e.printStackTrace();
        }finally{
            if(session!=null){
                session.close();
            }
        
		}

		
		
	}


	@Override
	public List<String> getSchoolSelectionByCity(String city) {
		String tableName="student_parent_dongguan";
		if("yangzhou".equals(city)){
			tableName = "student_parent_yangzhou";
		}else if("changzhou".equals(city)){
			tableName = "student_parent_changzhou";
		}else if("changsha".equals(city)){
			tableName= "student_parent_changsha";
		}else if("suzhou".equals(city)){
			tableName="student_parent_suzhou";
		}

		String sql ="SELECT DISTINCT SCHOOL_NAME FROM "+tableName+" WHERE  SCHOOL_NAME<>'' ";
		Query query= hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List shools=query.list();
		return shools;
	}

	/**
	 * 查询客户经纬度
	 * */
	@Override
	public DataPackage getCustomerLatitude(DataPackage dataPackage,String city, int pageNo, int pageSize,String grade, String blCampusName) {
		dataPackage.setPageNo(pageNo);
		dataPackage.setPageSize(pageSize);
//		StringBuilder hql = new StringBuilder();
		//hql.append(" from CustomerMapAnalyze where id>=").append(begin).append(" and id<").append(end);
//		
//		List<Criterion> criterion = new ArrayList<Criterion>();
//		if(StringUtil.isNotBlank(grade)){
////			hql.append(" and grade = ").append(grade);
//			criterion.add(Restrictions.eq("grade", grade));
//		}
//		dataPackage = customerMapAnalyzeDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "id", "asc"), criterion);
//		List<CustomerMapAnalyze> list =  (List) dataPackage.getDatas();
//		List<CustomerMapAnalyzeVo> voList = new ArrayList<CustomerMapAnalyzeVo>();
//		for(CustomerMapAnalyze cus : list){
//			CustomerMapAnalyzeVo vo = HibernateUtils.voObjectMapping(cus, CustomerMapAnalyzeVo.class);
//			vo.setAddress(cus.getFatherAddress()!=null?cus.getFatherAddress():cus.getMotherAddress());
//			voList.add(vo);
//		}
//		dataPackage.setDatas(voList);
		String tableName="student_parent_dongguan";
//		System.out.println("*********************************************************************"+city);
		if("yangzhou".equals(city)){
			tableName = "student_parent_yangzhou";
		}else if("changzhou".equals(city)){
			tableName = "student_parent_changzhou";
		}else if("changsha".equals(city)){
			tableName= "student_parent_changsha";
		}else if("suzhou".equals(city)){
			tableName="student_parent_suzhou";
		}else if("jiangmen".equals(city)){
			tableName="student_parent_jiangmen";
		}else if ("fuzhoujinshan".equals(city)){
			tableName= "student_parent_fuzhoujinshan";
		}else if ("fuzhoutaijiang".equals(city)){
			tableName="student_parent_fuzhoutaijiang";
		}else if ("fuzhouyangqiao".equals(city)){
			tableName="student_parent_fuzhouyangqiao";
		}else if ("fuzhouall".equals(city)){
			tableName = "student_parent_fuzhouall";
		}
		Map<String, Object> params = Maps.newHashMap();
		
		String sql = "SELECT STUDENT_NAME as name,FATHER_ADDRESS as address,LONGITUDE as lng,LATITUDE as lat FROM "+tableName+" WHERE 1=1  ";
		if(StringUtil.isNotBlank(grade)){
			//grade="初中2014";
			sql+=" AND GRADE LIKE :grade ";
			params.put("grade", "%"+grade+"%");
		}
		if(StringUtils.isNotBlank(blCampusName)){
			sql += " AND SCHOOL_NAME LIKE :blCampusName ";
			params.put("blCampusName", "%"+blCampusName+"%");
		}
		
		dataPackage = customerMapAnalyzeDao.findMapPageBySQL(sql, dataPackage, false,params);
//		System.out.println(dataPackage.getDatas().size());
		return dataPackage;
		//customerMapAnalyzeDao.findPageByCriteria(dataPackage, orderList, criterion)
//		return list;
		
	}


	@Override
	public void updateStudentGrade() {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select count(id) from CustomerMapAnalyze");
		int totalCount = customerMapAnalyzeDao.findCountHql(hql.toString(),params);
		int batchCount = totalCount/1000;
		if(totalCount%1000>0){
			batchCount += 1;
		}
		DataPackage dataPackage = new DataPackage();
		List<CustomerMapAnalyze> list;
		Calendar a=Calendar.getInstance();
		List<Criterion> criterion = new ArrayList<Criterion>();
		int year = a.get(Calendar.YEAR);//得到年
		int stuGrade=0;String grade;
		
		for(int i=0; i<batchCount; i++){
			dataPackage.setPageNo(i);
			dataPackage.setPageSize(1000);
			dataPackage = customerMapAnalyzeDao.findPageByCriteria(dataPackage, 
					HibernateUtils.prepareOrder(dataPackage, "id", "asc"),
					criterion);
			list = (List<CustomerMapAnalyze>) dataPackage.getDatas();
			int j = 0;
			for(CustomerMapAnalyze cus : list){
				
				j=j+1;
				grade = cus.getClassName();
				if(StringUtil.isNotBlank(grade) && !"0".equals(grade)){
					if("小学".equals(grade.subSequence(0, 2))){
						stuGrade =year-Integer.parseInt(grade.substring(2,6));
					}else if("初中".equals(grade.substring(0,2))){
						stuGrade=year-Integer.parseInt(grade.substring(2,6));
						stuGrade+=6;
					}else if("高中".equals(grade.substring(0,2))){
						stuGrade=year-Integer.parseInt(grade.substring(2,6));
						stuGrade+=8;
					}
					params.put("stugrade"+i+j, stuGrade);
					params.put("cusId"+i+j,cus.getId());
					customerMapAnalyzeDao.excuteSql("UPDATE student_parent_info_01 SET GRADE = :stuGrade"+i+j+"  WHERE ID= :cusId"+i+j+" ",params );
				}
				
			}
//			customerMapAnalyzeDao.flush();
			customerMapAnalyzeDao.commit();
		}
	}
		
	
	 /**
     * Get Request
     * @return
     * @throws Exception
     */
    public static String[] doGet(String address) throws Exception {
        URL localURL = new URL("https://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=bHIcux5jImQGio3a0Nhwau1s&callback=showLocation");
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new ApplicationException("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }
        resultBuffer.toString();
        String lng = resultBuffer.substring(resultBuffer.indexOf("lng")+5,resultBuffer.indexOf("lat")-2);
    	String lat = resultBuffer.substring(resultBuffer.indexOf("lat")+5,resultBuffer.indexOf("precise")-3);
    	String[] result = {lng,lat};
        return result;
    }
    
    public static void main(String[] arg) throws Exception{
    	//CustomerMapAnalyzeServiceImpl cus = new CustomerMapAnalyzeServiceImpl();
    	String[] result = doGet("福建省福州市晋安区井店一路2号君临香格里C区1座1111单元");
    	
    	System.out.println(result[0]);
    	System.out.println(result[1]);
    }
	

}
