package com.eduboss.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/11/21.
 */
@Entity
@Table(name = "region")
public class Region implements java.io.Serializable  {
    private String id; // id
    private String name; // 名称
    private int level; // 等级 1-省 2-市 3-县
    private int sort; // 排序
    private Region parent; // 上级地区id

    public Region(){}

    public Region(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Column(name = "sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
    }
}
