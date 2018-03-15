package com.eduboss.service;

import com.eduboss.domain.FAQ;
import com.eduboss.domain.IntroJS;
import java.util.List;


/**
 * Created by Administrator on 2015/4/8.
 */
public interface IntroJSService {

    public void saveOrUpdateIntroJS(FAQ faq,String[] pageUrls,String[] elementSelectors,String[] dataIntros);

    public void deleteIntroJS(String id);


    /**
     * 根据faq的id找到所有的分步演示的introJS
     * @param id
     * @return
     */
    public List<IntroJS> findIntroJSById(String id);
}
