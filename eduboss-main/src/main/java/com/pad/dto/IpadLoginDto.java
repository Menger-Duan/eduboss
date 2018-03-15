package com.pad.dto;

import com.eduboss.dto.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */
public class IpadLoginDto extends Response{
    private Map<String,String> userInfo;
    private List<CmsMenuVo> workMenuVoList;
    private List<CmsMenuVo> visitorMenuVoList;
    private String token;
    private Boolean setPwd=false;//是否设置PWD

    public Boolean getSetPwd() {
        return setPwd;
    }

    public void setSetPwd(Boolean setPwd) {
        this.setPwd = setPwd;
    }

    public Map<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CmsMenuVo> getWorkMenuVoList() {
        return workMenuVoList;
    }

    public void setWorkMenuVoList(List<CmsMenuVo> workMenuVoList) {
        this.workMenuVoList = workMenuVoList;
    }

    public List<CmsMenuVo> getVisitorMenuVoList() {
        return visitorMenuVoList;
    }

    public void setVisitorMenuVoList(List<CmsMenuVo> visitorMenuVoList) {
        this.visitorMenuVoList = visitorMenuVoList;
    }
}
