package com.pad.dto;

import com.eduboss.common.OrganizationType;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */
public class CmsCopyContentDto {

    private List<String> newBrenchId;//分公司ID

    private List<Integer> contentId;

    private List<Integer> newMenuId;

    private OrganizationType newType;

    public List<String> getNewBrenchId() {
        return newBrenchId;
    }

    public void setNewBrenchId(List<String> newBrenchId) {
        this.newBrenchId = newBrenchId;
    }

    public OrganizationType getNewType() {
        return newType;
    }

    public void setNewType(OrganizationType newType) {
        this.newType = newType;
    }

    public List<Integer> getNewMenuId() {
        return newMenuId;
    }

    public void setNewMenuId(List<Integer> newMenuId) {
        this.newMenuId = newMenuId;
    }

    public List<Integer> getContentId() {
        return contentId;
    }

    public void setContentId(List<Integer> contentId) {
        this.contentId = contentId;
    }
}
