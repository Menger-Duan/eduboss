package com.eduboss.controller;

import com.eduboss.dao.FAQDao;
import com.eduboss.domain.FAQ;
import com.eduboss.domain.IntroJS;
import com.eduboss.domainVo.FAQVo;
import com.eduboss.dto.*;
import com.eduboss.service.FAQService;
import com.eduboss.service.IntroJSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by xuwen on 2015/2/8.
 */
@Controller
@RequestMapping(value = "/FAQController")
public class FAQController {

    @Autowired
    private FAQService faqService;

    @Autowired
    private FAQDao faqDao;

    @Autowired
    private IntroJSService introJSService;

    /**
     * 分页查询
     */
    @RequestMapping(value = "/findPageFAQ", method =  RequestMethod.GET)
    @ResponseBody
    public DataPackageForJqGrid findPageFAQ(@ModelAttribute GridRequest gridRequest,@ModelAttribute FAQVo faqVo,@ModelAttribute TimeVo timeVo){
        DataPackage dataPackage = new DataPackage(gridRequest);
        dataPackage= faqService.findPageFAQ(dataPackage, faqVo, timeVo);
        DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
        return dataPackageForJqGrid ;
    }

    /**
     * 自动补全
     */
    @RequestMapping(value = "/findForAutoComplete", method =  RequestMethod.GET)
    @ResponseBody
    public List<FAQVo> findForAutoComplete(@RequestParam String term){
        return faqService.findForAutoComplete(term);
    }

    /**
     * 提出问题
     */
    @RequestMapping(value = "/saveQuestion", method =  RequestMethod.GET)
    @ResponseBody
    public Response saveQuestion(@RequestParam String title,@RequestParam String question){
        faqService.saveQuestion(title, question);
        return new Response();
    }

    /**
     * 解答问题
     */
    @RequestMapping(value = "/saveReply", method =  RequestMethod.GET)
    @ResponseBody
    public Response saveReply(@RequestParam String id,@RequestParam String reply){
        faqService.saveReply(id, reply);
        return new Response();
    }

    /**
     * 阅读回复
     * @param id
     */
    @RequestMapping(value = "/readReply", method =  RequestMethod.GET)
    @ResponseBody
    public Response readReply(@RequestParam String id){
        faqService.readReply(id);
        return new Response();
    }


    @RequestMapping(value = "/findFAQById", method =  RequestMethod.GET)
    @ResponseBody
    public FAQ findFAQById(@RequestParam String id) {
        return faqDao.findById(id);
    }

    @RequestMapping(value="/findIntroJSByFaqId",method=RequestMethod.GET)
    @ResponseBody
    public List<IntroJS> findIntroJSByFaqId(@RequestParam String id){
        return introJSService.findIntroJSById(id);
    }

    /**
     *
     * @param faqVo
     * @param pageUrls
     * @param elementSelector
     * @param dataIntro
     * @return
     */
    @RequestMapping(value = "/saveOrEditFAQ", method = RequestMethod.POST)
    @ResponseBody
    public Response saveOrEditFAQ(@ModelAttribute FAQVo faqVo,String[] pageUrls,String[] elementSelector,String[] dataIntro){
        if(faqVo.getId()==""){
            faqVo.setId(null);
        }
        FAQ faq= faqService.saveOrUpdateFAQ(faqVo);
        faqDao.getHibernateTemplate().flush();
        introJSService.saveOrUpdateIntroJS(faq, pageUrls, elementSelector, dataIntro);
        return new Response();
    }



    @RequestMapping(value = "/deleteFAQById",method = RequestMethod.GET)
    @ResponseBody
    public void deleteFAQById(@RequestParam String id){
        faqService.deleteFAQ(id);
    }

    @RequestMapping(value = "/findPageNameByFaqId", method = RequestMethod.GET)
    @ResponseBody
    public Response findPageNameByFaqId(@RequestParam String faqId) {
        String result =  faqService.findPageNameByFaqId(faqId);
        return new Response(0, result);
    }
}
