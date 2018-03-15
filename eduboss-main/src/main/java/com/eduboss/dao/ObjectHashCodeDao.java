package com.eduboss.dao;


import com.eduboss.domain.ObjectHashCode;

public interface ObjectHashCodeDao extends GenericDAO<ObjectHashCode, String> {
    ObjectHashCode findByHashCode(int hashCode);
}
