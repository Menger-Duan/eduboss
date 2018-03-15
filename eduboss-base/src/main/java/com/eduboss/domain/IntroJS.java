package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;
import org.nutz.dao.entity.annotation.Name;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/4/8.
 */


@Entity
@Table(name="introJS")
public class IntroJS {
    private String id;
    private String pageUrl;
    private String dataStep;
    private String dataIntro;
    private String elementSelector;
    private FAQ faq;


    @Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="URL")
    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
    @Column(name="DATA_STEP")
    public String getDataStep() {
        return dataStep;
    }

    public void setDataStep(String dataStep) {
        this.dataStep = dataStep;
    }
    @Column(name = "DATA_INTRO")
    public String getDataIntro() {
        return dataIntro;
    }

    public void setDataIntro(String dataIntro) {
        this.dataIntro = dataIntro;
    }

    @Column(name = "ELEMENT")
    public String getElementSelector() {
        return elementSelector;
    }

    public void setElementSelector(String elementSelector) {
        this.elementSelector = elementSelector;
    }


    @ManyToOne
    @JoinColumn(name="FAQ_ID")
    public FAQ getFaq() {
        return faq;
    }

    public void setFaq(FAQ faq) {
        this.faq = faq;
    }
}
