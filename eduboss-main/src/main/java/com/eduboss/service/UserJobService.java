package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.UserJob;
import com.eduboss.domainVo.UserJobVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

public interface UserJobService {

    /**
     * 分页查询
     * @param dataPackage
     * @param UserJob
     * @return
     */
    public DataPackage findPageUserJob(DataPackage dataPackage, UserJob userJob);


    public Response  deleteUserJob(String id);

    public void saveOrUpdateUserJob(UserJob userJob);
    

    public UserJob findUserJobById(String id);
    
    public DataPackage getJobListForSelection(UserJob userJob, DataPackage dp);
    
    public String getUniqueJobSign(String newJobSign);
    
    public List<UserJob> findAllUserJob();
    

    /** 
     * 获取所有的职位信息用于同步
    * @return
    * @author  author :Yao 
    * @date  2016年7月30日 下午2:46:46 
    * @version 1.0 
    */
    public List<UserJobVo> getAllUserJobForSync();
    
    public List<UserJob> findAllUserJobBySql(String sql, Map<String, Object> params);
}
