package com.eduboss.dto;

/**
 * @author yao
 */
public class RoleQLConfigSearchVo {
    private String id;
    private String name;
    private String description;
    private String roleId;
    private String value;
    private String joiner = "or"; // and or
    private String type; // sql hql
    private String resource;
    private String alias;//别名
    private String oldType;

    public RoleQLConfigSearchVo() {
    }

    public RoleQLConfigSearchVo(String name, String type, String oldType) {
        this.name = name;
        this.type = type;
        this.oldType=oldType;
    }

    public RoleQLConfigSearchVo(String name, String type, String resource,String oldType) {
        this.name = name;
        this.type = type;
        this.resource = resource;
        this.oldType=oldType;
    }

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getJoiner() {
        return joiner;
    }

    public void setJoiner(String joiner) {
        this.joiner = joiner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
