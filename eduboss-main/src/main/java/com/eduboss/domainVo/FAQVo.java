package com.eduboss.domainVo;

import com.eduboss.common.BaseStatus;
import com.eduboss.domain.IntroJS;

import java.util.List;

/**
 * Created by xuwen on 2015/2/8.
 */
public class FAQVo {

    private String id;
    private String title;
    private String question;
    private String createUserName;
    private String createTime;
    private String reply;
    private String replyUserName;
    private String replyTime;
    private BaseStatus replied; // 已解答
    private BaseStatus readReply; // 已阅读解答



    private List<IntroJS> introJsList;


    public List<IntroJS> getIntroJsList() {
        return introJsList;
    }

    public void setIntroJsList(List<IntroJS> introJsList) {
        this.introJsList = introJsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public BaseStatus getReplied() {
        return replied;
    }

    public void setReplied(BaseStatus replied) {
        this.replied = replied;
    }

    public BaseStatus getReadReply() {
        return readReply;
    }

    public void setReadReply(BaseStatus readReply) {
        this.readReply = readReply;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }
}
