package com.eduboss.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.PersonWorkScheduleRecord;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.PersonWorkService;

/**
 * 个人工作日程
 * @author lixuejun
 * 2015-11-13
 */
@Controller
@RequestMapping(value ="/PersonWorkController")
public class PersonWorkController {

	@Autowired
	private PersonWorkService personWorkService;
	
    /**
     * 获取指定时间内的个人工作日程
     * @param studentId
     * @return
     */
    @RequestMapping(value ="/getPersonWorkScheduleRecords")
    @ResponseBody
    public List<PersonWorkScheduleRecord> getPersonWorkScheduleRecords(@RequestParam String startDate, @RequestParam String endDate) throws IOException {
    	List<PersonWorkScheduleRecord> list =  personWorkService.getPersonWorkScheduleRecords(startDate, endDate);
    	return list;
    }
    
    /**
     * 保存或修改个人工作日程
     * @param record
     * @return
     */
    @RequestMapping(value ="/editPersonWorkScheduleRecord")
    @ResponseBody
    public Response editPersonWorkScheduleRecord(@ModelAttribute GridRequest gridRequest, @ModelAttribute PersonWorkScheduleRecord record) throws IOException, IllegalAccessException {
    	if ("del".equalsIgnoreCase(gridRequest.getOper())) {
    		personWorkService.deletePersonWorkScheduleRecord(record);
    	} else {
    		personWorkService.saveEditPersonWorkScheduleRecord(record);
    	}
    	return new Response();
    }
    
    /**
	 * 根据id查询个人工作日程
	 * @param id
	 * @return
	 */
   @RequestMapping(value ="/findPersonWorkScheduleRecordById")
   @ResponseBody
   public PersonWorkScheduleRecord findPersonWorkScheduleRecordById(@RequestParam String id) throws IOException {
	   return personWorkService.findPersonWorkScheduleRecordById(id);
   }
    
}
