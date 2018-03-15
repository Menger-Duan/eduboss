package com.eduboss.domain;

import com.eduboss.common.BaseStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by xuwen on 2015/2/8.
 */
@Entity
@Table(name = "faq")
public class FAQ {

    private String id; // ID
    private String title; // 标题
    private String question; // 问题
    private User createUser; // 提问人
    private String createTime; // 提问时间
    private String reply; // 回复
    private User replyUser; // 回复人
    private String replyTime; // 回复时间
    private BaseStatus replied = BaseStatus.FALSE; // 已回复
    private BaseStatus readReply = BaseStatus.FALSE; // 提问人已阅读回复
    private String readReplyTime; // 阅读回复的时间

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

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "reply")
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Column(name = "question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @ManyToOne
    @JoinColumn(name="create_user_id")
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    @Column(name = "create_time")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn(name="reply_user_id")
    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    @Column(name = "reply_time")
    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    @Column(name = "replied")
    @Enumerated(EnumType.STRING)
    public BaseStatus getReplied() {
        return replied;
    }

    public void setReplied(BaseStatus replied) {
        this.replied = replied;
    }

    @Column(name = "read_reply")
    @Enumerated(EnumType.STRING)
    public BaseStatus getReadReply() {
        return readReply;
    }

    public void setReadReply(BaseStatus readReply) {
        this.readReply = readReply;
    }

    @Column(name = "read_reply_time")
    public String getReadReplyTime() {
        return readReplyTime;
    }

    public void setReadReplyTime(String readReplyTime) {
        this.readReplyTime = readReplyTime;
    }
}
