package com.eduboss.domainVo;

import com.eduboss.dto.SelectOptionResponse;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class RegionVo implements SelectOptionResponse.NameValue  {

    private String id;
    private String name;
    private int level;
    private int sort;
    private List<RegionVo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<RegionVo> getChildren() {
        return children;
    }

    public void setChildren(List<RegionVo> children) {
        this.children = children;
    }

    @Override
    public String getValue() {
        return id;
    }
}
