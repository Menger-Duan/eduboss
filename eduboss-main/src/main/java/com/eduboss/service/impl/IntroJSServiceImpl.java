package com.eduboss.service.impl;

import com.eduboss.dao.IntroJSDao;
import com.eduboss.domain.FAQ;
import com.eduboss.domain.IntroJS;

import com.eduboss.exception.ApplicationException;
import com.eduboss.service.IntroJSService;

import org.apache.commons.lang.StringUtils;

import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2015/4/8.
 */

@Service
public class IntroJSServiceImpl implements IntroJSService {

    @Autowired
    private IntroJSDao introJSDao;


    @Override
    public void saveOrUpdateIntroJS(FAQ faq,String[] pageUrls,String[] elementSelectors,String[] dataIntros) {
        List<IntroJS> ls= findIntroJSById(faq.getId());
        introJSDao.deleteAll(ls);
        introJSDao.commit();

        for(int i=0;i<elementSelectors.length;i++){
            IntroJS introJS= new IntroJS();
            introJS.setFaq(faq);
            introJS.setElementSelector(elementSelectors[i]);
            if(pageUrls.length >0 && StringUtils.isNotEmpty(pageUrls[i]))
                introJS.setPageUrl(pageUrls[i]);
            if(dataIntros.length >0 && StringUtils.isNotEmpty(dataIntros[i]))
                introJS.setDataIntro(dataIntros[i]);
            introJSDao.save(introJS);
        }

    }

    @Override
    public void deleteIntroJS(String id) {
        IntroJS introJS= introJSDao.findById(id);
        if(introJS==null){
            throw new ApplicationException("删除对象为空！");
        }
        introJSDao.delete(introJS);
    }

    @Override
    public List<IntroJS> findIntroJSById(String id) {
        return introJSDao.findByCriteria(Expression.eq("faq.id", id));
    }
}
