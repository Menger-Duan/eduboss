package com.eduboss.domain;

import com.eduboss.common.BaseStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by xuwen on 2014/12/17.
 */
@Entity
@Table(name = "role_ql_config")
public class RoleQLConfig {
    private String id;
    private String name;
    private String description;
    private String roleId;
    private String value;
    private String joiner = "or"; // and or
    private String type; // sql hql
    private BaseStatus isOtherRole;
    private String resource;

    @Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
    @GeneratedValue(generator = "generator")
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

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "role_id")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "joiner")
    public String getJoiner() {
        return joiner;
    }

    public void setJoiner(String joiner) {
        this.joiner = joiner;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "is_other_role")
    public BaseStatus getIsOtherRole() {
        return isOtherRole;
    }

    public void setIsOtherRole(BaseStatus isOtherRole) {
        this.isOtherRole = isOtherRole;
    }

    @Column(name = "resource_id")
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
