package com.eduboss.service;

import com.eduboss.domain.FAQ;
import com.eduboss.domainVo.FAQVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;

import java.util.List;

/**
 * Created by xuwen on 2015/2/8.
 */
public interface FAQService {

    /**
     * 分页查询
     * @param dataPackage
     * @param faqVo
     * @param timeVo
     * @return
     */
    public DataPackage findPageFAQ(DataPackage dataPackage, FAQVo faqVo, TimeVo timeVo);

    /**
     * 自动补全
     * @param term
     * @return
     */
    public List<FAQVo> findForAutoComplete(String term);

    /**
     * 提出问题
     * @param question
     */
    public void saveQuestion(String title,String question);

    /**
     * 解答问题
     */
    public void saveReply(String id,String reply);

    /**
     * 阅读回复
     * @param id
     */
    public void readReply(String id);

    /**
     *
     * @param id
     */
    public void  deleteFAQ(String id);

    public FAQ saveOrUpdateFAQ(FAQVo faqVo);

    /**
     * 查找页面名称
     * @param faqId
     * @return
     */
    String findPageNameByFaqId(String faqId);
}
