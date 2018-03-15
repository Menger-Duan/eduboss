package com.eduboss.service.impl;

import com.eduboss.common.MiniClassStatus;
import com.eduboss.common.WeekDay;
import com.eduboss.dao.CourseModalDao;
import com.eduboss.dao.CourseModalDateDao;
import com.eduboss.dao.CourseModalWeekDao;
import com.eduboss.domain.*;
import com.eduboss.domainVo.CourseModalVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.CourseModalService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

@Service(value = "CourseModalService")
public class CourseModalServiceImpl implements CourseModalService{
	private final static Logger log = Logger.getLogger(CourseModalServiceImpl.class);
	
	@Resource(name = "CourseModalDao")
	private CourseModalDao courseModalDao;

	@Resource(name = "CourseModalDateDao")
	private CourseModalDateDao courseModalDateDao;

	@Resource(name = "CourseModalWeekDao")
	private CourseModalWeekDao courseModalWeekDao;


	@Autowired
	private UserService userService;

	@Autowired
	private SmallClassService smallClassService;

	@Override
	public Response saveCourseModal(CourseModalVo vo) {
		if(vo.getId()>0){
			checkCanModifyModal(vo.getId());
		}
		Boolean isChangeTechNum=false;
		Response res = new Response();
		User user = userService.getCurrentLoginUser();
		vo.setModalName(vo.getModalName().replace("null",""));
		CourseModal readyDomain=HibernateUtils.voObjectMapping(vo,CourseModal.class);
		checkModalName(vo);
		CourseModal domain=null;
		if(vo.getId()>0){
			domain=courseModalDao.findById(vo.getId());
			if(domain.getTechNum()!=vo.getTechNum()){
				isChangeTechNum=true;
			}
			domain.checkVesion(vo.getVersion());//乐观锁处理
			domain.setBranch(readyDomain.getBranch());
			domain.setCoursePhase(readyDomain.getCoursePhase());
//			domain.setCourseWeek(readyDomain.getCourseWeek());
			domain.setModalName(readyDomain.getModalName());
			domain.setProductSeason(readyDomain.getProductSeason());
			domain.setProductYear(readyDomain.getProductYear());
			domain.setTechNum(readyDomain.getTechNum());
			domain.setSuffixName(readyDomain.getSuffixName());
			domain.setClassTime(readyDomain.getClassTime());
		}else{
			domain= readyDomain;
			if(user!=null){
				domain.setCreateUser(user);
			}
			domain.setCreateTime(DateTools.getCurrentDateTime());
		}
		if(user!=null){
			domain.setModifyUser(user);
		}
		domain.setModifyTime(DateTools.getCurrentDateTime());

		courseModalDao.save(domain);
		saveCourseModalDate(domain.getId(),vo.getCourseDate());
		saveCourseModalWeek(domain,vo.getCourseWeek());
		if(vo.getId()>0) {
			smallClassService.modifyClassModal(domain,isChangeTechNum);
		}
		return res;
	}

	public void checkModalName(CourseModalVo vo){
		CourseModalVo newVo = new CourseModalVo();
		newVo.setModalName(vo.getModalName());
		newVo.setBranchId(vo.getBranchId());
		newVo.setProductYearId(vo.getProductYearId());
		newVo.setProductSeasonId(vo.getProductSeasonId());
		List<CourseModal> mo=courseModalDao.getCourseModalList(newVo);
		if(vo.getId()>0) {
			for (CourseModal m : mo) {
				if(vo.getId()!=m.getId()){
					throw new ApplicationException("有重名的模板，请重新命名");
				}
			}
		}else if(mo.size()>0){
			throw new ApplicationException("有重名的模板，请重新命名");
		}
	}

	public void saveCourseModalDate(int modalId, TreeSet<String> courseDate){

		User user = userService.getCurrentLoginUser();

		List<CourseModalDate> dateList=courseModalDateDao.findModalDateByModalId(modalId);

		for(Iterator i =dateList.iterator();i.hasNext();){
			CourseModalDate date= (CourseModalDate) i.next();
			if(courseDate.contains(date.getCourseDate())){
				courseDate.remove(date.getCourseDate());
				i.remove();
			}
		}

		if(dateList.size()>0){
			courseModalDateDao.deleteAll(dateList);
		}

		for(String date:courseDate){
			CourseModalDate modalDate=new CourseModalDate();
			modalDate.setModalId(modalId);
			modalDate.setCourseDate(date);
			modalDate.setCreateTime(DateTools.getCurrentDateTime());
			if(user!=null) {
				modalDate.setCreateUser(user);
			}
			courseModalDateDao.save(modalDate);
		}
	}

	public void saveCourseModalWeek(CourseModal courseModal, TreeSet<WeekDay> courseWeek){

		List<CourseModalWeek> dateList=courseModalWeekDao.findModalWeekByModalId(courseModal.getId());
		for(Iterator i =dateList.iterator();i.hasNext();){
			CourseModalWeek date= (CourseModalWeek) i.next();
			if(courseWeek.contains(date.getCourseWeek())){
				courseWeek.remove(date.getCourseWeek());
				i.remove();
			}
		}
		if(dateList.size()>0){
			courseModalWeekDao.deleteAll(dateList);
		}

		for(WeekDay week:courseWeek){
			CourseModalWeek modalWeek=new CourseModalWeek();
			modalWeek.setCourseModal(courseModal);
			modalWeek.setCourseWeek( week);
			courseModalWeekDao.save(modalWeek);
		}
	}

	@Override
	public List<CourseModalVo> getCourseModalList(CourseModalVo vo) {
		List<CourseModalVo> list= HibernateUtils.voListMapping(courseModalDao.getCourseModalList(vo),CourseModalVo.class);
		for(CourseModalVo modalVo:list){
			List<CourseModalDate> dateList=courseModalDateDao.findModalDateByModalId(modalVo.getId());
			TreeSet<String> set = new TreeSet<>();
			for(CourseModalDate date:dateList){
				set.add(date.getCourseDate());
			}
			modalVo.setCourseDate(set);

			List<CourseModalWeek> weekList=courseModalWeekDao.findModalWeekByModalId(modalVo.getId());
			TreeSet<WeekDay> weekSet = new TreeSet<>();
			for(CourseModalWeek week:weekList){
				weekSet.add(week.getCourseWeek());
			}
			modalVo.setCourseWeek(weekSet);
		}
		return list;
	}

	@Override
	public CourseModalVo findModalByModalId(int modalId) {
		CourseModalVo vo= HibernateUtils.voObjectMapping(courseModalDao.findById(modalId),CourseModalVo.class);

		if(vo!=null) {
			List<CourseModalDate> dateList = courseModalDateDao.findModalDateByModalId(modalId);
			TreeSet<String> set = new TreeSet<>();
			for(CourseModalDate date:dateList){
				set.add(date.getCourseDate());
			}
			vo.setCourseDate(set);

			List<CourseModalWeek> weekList=courseModalWeekDao.findModalWeekByModalId(vo.getId());
			TreeSet<WeekDay> weekSet = new TreeSet<>();
			for(CourseModalWeek week:weekList){
				weekSet.add(week.getCourseWeek());
			}
			vo.setCourseWeek(weekSet);
		}

		return vo;

	}

    @Override
    public Response deleteCourseModal(int modalId) {
		checkCanDeleteModal(modalId);
		CourseModal modal=courseModalDao.findById(modalId);
		List<CourseModalDate> dateList=courseModalDateDao.findModalDateByModalId(modalId);
		List<CourseModalWeek> weekList=courseModalWeekDao.findModalWeekByModalId(modalId);
		courseModalDateDao.deleteAll(dateList);
		courseModalWeekDao.deleteAll(weekList);
		courseModalDao.delete(modal);
        return new Response();
    }

	private void checkCanDeleteModal(int modalId) {
		List list=smallClassService.findMiniClassByModalId(modalId);
		if(list.size()>0){
			throw new ApplicationException("本模板正在使用，暂不能删除");
		}

	}

	private void checkCanModifyModal(int modalId) {
		List<MiniClass> list=smallClassService.findMiniClassByModalId(modalId);
		for(MiniClass miniClass :list){
			if(!miniClass.getStatus().equals(MiniClassStatus.PENDDING_START)){
				throw new ApplicationException("使用本模板的小班已在上课中，不能修改模板");
			}
		}

	}
}
