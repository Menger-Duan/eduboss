package com.eduboss.service.impl.rpc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.boss.rpc.base.dto.CourseStatusNumVo;
import org.boss.rpc.base.dto.MiniClassCourseRpcVo;
import org.boss.rpc.base.dto.MiniClassInventoryShelvesVo;
import org.boss.rpc.base.dto.MiniClassRpcVo;
import org.boss.rpc.eduboss.service.CourseRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.common.Constants;
import com.eduboss.dao.MiniClassStudentDao;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassInventory;
import com.eduboss.domain.MiniClassStudent;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.service.MiniClassInventoryService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.StudentService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.StringUtil;

@Service("courseRpcImpl")
public class CourseRpcImpl implements CourseRpc {
    
    @Autowired
    private SmallClassService smallClassService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private MiniClassInventoryService miniClassInventoryService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private MiniClassStudentDao miniClassStudentDao;

    @Override
    public List<MiniClassRpcVo> getMiniClassListByIds(String miniClassIds) {
        List<MiniClassRpcVo> resultList = null;
        List<MiniClassVo> list = smallClassService.getMiniClassListByIds(miniClassIds);
        if (list != null && !list.isEmpty()) {
            resultList =  HibernateUtils.voListMapping(list, MiniClassRpcVo.class);
            for (MiniClassRpcVo vo : resultList) {
                Organization o = organizationService.findById(vo.getBlCampusId());
                vo.setBlBranchId(o.getParentId());
            }
        }
        return resultList;
    }

    @Override
    public List<MiniClassInventoryShelvesVo> findMiniClassQuantityByIds(String miniClassIds) {
        String redisKey = Constants.MINI_CLASS_RPC + miniClassIds;
        if (JedisUtil.exists(redisKey.getBytes())) {
            return JSONObject.parseArray(new String(JedisUtil.get(redisKey.getBytes())), MiniClassInventoryShelvesVo.class);
        }
        List<MiniClassInventory> inventorys = miniClassInventoryService.findMiniClassInventoryByMiniClassIds(miniClassIds);
        List<MiniClassInventoryShelvesVo> resultVoList = new ArrayList<MiniClassInventoryShelvesVo>();
        String notMiniClassIds = miniClassIds;
        Map<String, MiniClassInventoryShelvesVo> map = new HashMap<String, MiniClassInventoryShelvesVo>();
        if (inventorys != null && !inventorys.isEmpty()) {
            for (MiniClassInventory inventory : inventorys) {
                MiniClassInventoryShelvesVo qVo = new MiniClassInventoryShelvesVo();
                qVo.setMiniClassId(inventory.getMiniClassId());
                qVo.setMaxQuantity(inventory.getNormalQuantity());
                qVo.setLeftQuantity(inventory.getNormalQuantity() - inventory.getUsedQuantity());
                resultVoList.add(qVo);
                if (miniClassIds.contains(inventory.getMiniClassId() + ",")) {
                    notMiniClassIds = notMiniClassIds.replace(inventory.getMiniClassId() + ",", "");
                } else {
                    notMiniClassIds = notMiniClassIds.replace(inventory.getMiniClassId(), "");
                }
                map.put(inventory.getMiniClassId(), qVo);
            }
        }
        if (StringUtil.isNotBlank(miniClassIds)) {
            for (String miniClassId : miniClassIds.split(",")) {
                MiniClassVo vo = smallClassService.findMiniClassById(miniClassId);
                if (vo != null && notMiniClassIds.contains(miniClassId)) {
                    MiniClassInventory inventory = new MiniClassInventory();
                    inventory.setMiniClassId(miniClassId);
                    Integer maxQuanity = vo.getPeopleQuantity();
                    maxQuanity =  vo.getAllowedExcess() != null ? maxQuanity + vo.getAllowedExcess() : maxQuanity;
                    inventory.setMaxQuantity(maxQuanity);
                    inventory.setNormalQuantity(vo.getPeopleQuantity());
                    inventory.setUsedQuantity(vo.getMiniClassPeopleNum());
                    miniClassInventoryService.saveMiniClassInventory(inventory);
                    MiniClassInventoryShelvesVo qVo = new MiniClassInventoryShelvesVo();
                    qVo.setMiniClassId(miniClassId);
                    qVo.setMaxQuantity(inventory.getNormalQuantity());
                    qVo.setLeftQuantity(inventory.getNormalQuantity() - inventory.getUsedQuantity());
                    resultVoList.add(qVo);
                }
            }
        }
        JSONArray jsonArray = (JSONArray) JSONObject.toJSON(resultVoList);
        JedisUtil.set(redisKey.getBytes(), jsonArray.toJSONString().getBytes(), 2);
        return resultVoList;
    }
    
    @Override
    public List<MiniClassCourseRpcVo> findMiniClassCourseByClassId(
            String miniClassId) {
        List<MiniClassCourseRpcVo> voList = null;
        List<MiniClassCourse> list= smallClassService.findMiniClassCourseByClassId(miniClassId);
        if (list != null && !list.isEmpty()) {
            voList = new ArrayList<MiniClassCourseRpcVo>();
            for (MiniClassCourse course : list) {
                MiniClassCourseRpcVo vo = HibernateUtils.voObjectMapping(course, MiniClassCourseRpcVo.class);
                vo.setTeacher(course.getTeacher().getName());
                vo.setCourseStatus(course.getCourseStatus().getName());
                try {
                    vo.setDayOfWeek(DateTools.getDayOfWeekByDate(vo.getCourseDate()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    @Override
    public void updateMiniClassSaleType(String miniClassIds,
            String courseStatus) {
        int onShelves = 0;
        if (courseStatus.equals("NOT_ON")) {
            onShelves = 0;
        } else if (courseStatus.equals("ARTIFICIAL_OFF")) {
            onShelves = 1;
        } else {
            onShelves = 2;
        }
        smallClassService.updateMiniClassSaleType(miniClassIds, onShelves);
    }

    @Override
    public CourseStatusNumVo findCourseStatusNumVoByOrgId(String orgId) {
        CourseStatusNumVo vo = new CourseStatusNumVo();
        Map<Object, Object> map = smallClassService.findCourseStatusNumVoByOrgId(orgId);
        if (map.containsKey("totalNum") && map.get("totalNum") != null) {
            vo.setTotalNum(((BigInteger)map.get("totalNum")).intValue());
        }
        if (map.containsKey("notOnNum") && map.get("notOnNum") != null) {
            vo.setNotOnNum(((BigDecimal)map.get("notOnNum")).intValue());
        }
        if (map.containsKey("onNum") && map.get("onNum") != null) {
            vo.setOnNum(((BigDecimal)map.get("onNum")).intValue());
        }
        if (map.containsKey("offNum") && map.get("offNum") != null) {
            vo.setOffNum(((BigDecimal)map.get("offNum")).intValue());
        }
        return vo;
    }

    @Override
    public boolean checkAllowAddStudentToMiniClass(String miniClassId,
            String studentId) {
        boolean result = true;
        Student student = studentService.findStudentByRelateNo(studentId);
        if (student != null) {
            MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClassId, student.getId());
            if (miniClassStudent != null) {
                result =  false;
            }
        }
        return result;
    }

}
