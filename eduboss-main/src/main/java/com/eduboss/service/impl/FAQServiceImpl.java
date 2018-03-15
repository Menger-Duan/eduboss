package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.BaseStatus;
import com.eduboss.dao.FAQDao;
import com.eduboss.dao.IntroJSDao;
import com.eduboss.dao.ResourceDao;
import com.eduboss.domain.FAQ;
import com.eduboss.domain.IntroJS;
import com.eduboss.domain.Resource;
import com.eduboss.domainVo.FAQVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.FAQService;
import com.eduboss.service.IntroJSService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

/**
 * Created by xuwen on 2015/2/8.
 */
@Service
public class FAQServiceImpl implements FAQService {

    @Autowired
    private FAQDao faqDao;

    @Autowired
    private UserService userService;

    @Autowired
    private IntroJSService introJSService;

    @Autowired
    private IntroJSDao introJSDao;

    @Autowired
    private ResourceDao resourceDao;

    /**
     * 分页查询
     *
     * @param dataPackage
     * @param faqVo
     * @param timeVo
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public DataPackage findPageFAQ(DataPackage dataPackage, FAQVo faqVo, TimeVo timeVo) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        List<Order> orders = new ArrayList<Order>();
        if(StringUtils.isNotBlank(faqVo.getTitle())){
            criterions.add(Restrictions.like("title",faqVo.getTitle(), MatchMode.ANYWHERE));
        }
        if(StringUtils.isNotBlank(faqVo.getQuestion())){
            criterions.add(Restrictions.like("question", faqVo.getQuestion(), MatchMode.ANYWHERE));
        }
        if(StringUtils.isNotBlank(faqVo.getReply())){
            criterions.add(Restrictions.like("reply", faqVo.getReply(), MatchMode.ANYWHERE));
        }
        if(StringUtils.isNotBlank(faqVo.getCreateUserName())){
            criterions.add(Restrictions.like("createUser.name", faqVo.getCreateUserName(), MatchMode.ANYWHERE));
        }
        if(StringUtils.isNotBlank(faqVo.getReplyUserName())){
            criterions.add(Restrictions.like("replyUser.name", faqVo.getReplyUserName(), MatchMode.ANYWHERE));
        }
        if(StringUtils.isNotBlank(timeVo.getStartDate())){
            criterions.add(Restrictions.ge("createTime", timeVo.getStartDate()));
        }
        if(StringUtils.isNotBlank(timeVo.getEndDate())) {
            criterions.add(Restrictions.le("createTime", timeVo.getEndDate()));
        }
        DataPackage dp = faqDao.findPageByCriteria(dataPackage,orders,criterions);

        List<FAQVo> FAQVos = HibernateUtils.voListMapping((List) dp.getDatas(), FAQVo.class);

        for (FAQVo vo : FAQVos) {
            vo.setIntroJsList(introJSService.findIntroJSById(vo.getId()));
        }

        dp.setDatas(FAQVos);
        return dp;
    }

    /**
     * 自动补全
     *
     * @param term
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<FAQVo> findForAutoComplete(String term) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        List<Order> orders = new ArrayList<Order>();
        if(StringUtils.isNotBlank(term)){
            Disjunction orLike = Restrictions.disjunction();
            orLike.add(Restrictions.like("title",term, MatchMode.ANYWHERE));
            orLike.add(Restrictions.like("question",term, MatchMode.ANYWHERE));
            criterions.add(orLike);
        }
        List<FAQ> datas = (List<FAQ>) faqDao.findPageByCriteria(new DataPackage(), orders, criterions).getDatas();
        return HibernateUtils.voListMapping(datas,FAQVo.class);
    }

    /**
     * 提出问题
     *
     * @param question
     */
    @Override
    public void saveQuestion(String title,String question) {
        if(StringUtils.isBlank(title) || StringUtils.isBlank(question)){
            throw new ApplicationException("标题和问题都不允许为空");
        }
        FAQ faq = new FAQ();
        faq.setTitle(title);
        faq.setQuestion(question);
        faq.setCreateUser(userService.getCurrentLoginUser());
        faq.setCreateTime(DateTools.getCurrentDateTime());
        faqDao.save(faq);
    }

    /**
     * 解答问题
     *
     * @param id
     * @param reply
     */
    @Override
    public void saveReply(String id, String reply) {
        FAQ faq = faqDao.findById(id);
        faq.setReply(reply);
        faq.setReplied(BaseStatus.TRUE);
        faq.setReplyUser(userService.getCurrentLoginUser());
        faq.setReplyTime(DateTools.getCurrentDateTime());
    }

    /**
     * 阅读回复
     *
     * @param id
     */
    @Override
    public void readReply(String id) {
        FAQ faq = faqDao.findById(id);
        if(userService.getCurrentLoginUser().getUserId().equals(faq.getCreateUser().getUserId())){
            faq.setReadReply(BaseStatus.TRUE);
            faq.setReadReplyTime(DateTools.getCurrentDateTime());
        }else{
            throw new ApplicationException("不可批阅他人提出的问题");
        }
    }

    /**
     *
     * @param id
     */
    public void deleteFAQ(String id) {
        FAQ faq = faqDao.findById(id);

        //先删除所有相关联的introJs
        List<IntroJS> ls= introJSService.findIntroJSById(id);
        introJSDao.deleteAll(ls);

        if(faq==null){
            throw new ApplicationException("删除对象为空！");
        }
        faqDao.delete(faq);
    }

    @Override
    public FAQ saveOrUpdateFAQ(FAQVo faqVo) {
        FAQ faq= HibernateUtils.voObjectMapping(faqVo,FAQ.class);
        faqDao.save(faq);
        return faq;
    }

    /**
     * 查找页面名称
     * @param faqId
     * @return
     */
    @Override
    public String findPageNameByFaqId(String faqId) {
    	Map<String, Object> params = new HashMap<String, Object>();
        FAQ faq = faqDao.findById(faqId);
        StringBuilder hql = new StringBuilder();
        hql.append(" from IntroJS i where i.faq.id= :faqId order by i.id desc ");
        params.put("faqId", faq.getId());
        List<IntroJS> list = introJSDao.findAllByHQL(hql.toString(), params);
        if (list!=null && list.size()>0){
            IntroJS introJS = list.get(0);
            String pageUrl = introJS.getPageUrl();
            String res = " from Resource r where rurl ='"+pageUrl+"' ";
            List<Resource> resourceList = resourceDao.findAllByHQL(res, new HashMap<String, Object>());
            if (resourceList!=null&& resourceList.size()>0){
                Resource resource = resourceList.get(0);
                return resource.getRname();
            }
        }
        return "";
    }
}
