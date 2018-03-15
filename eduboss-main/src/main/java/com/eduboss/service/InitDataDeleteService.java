package com.eduboss.service;

import com.eduboss.domainVo.DeleteDataLogVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import org.springframework.stereotype.Service;


@Service
public interface InitDataDeleteService {
	
	public void deleteInitData (String id ,String reason)throws Exception ;


    public DataPackage findPageDeleteLog (DataPackage dataPackage,DeleteDataLogVo deleteDataLogVo,TimeVo timeVo);
    
    public void deleteSomeConract();
}
