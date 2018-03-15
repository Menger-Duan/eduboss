package com.eduboss.dao.impl;

import com.eduboss.dao.TwoTeacherBrenchDao;
import com.eduboss.domain.TwoTeacherClassBrench;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/3/30.
 */
@Repository("TwoTeacherBrenchDao")
public class TwoTeacherBrenchDaoImpl extends GenericDaoImpl<TwoTeacherClassBrench, String> implements TwoTeacherBrenchDao {
}
