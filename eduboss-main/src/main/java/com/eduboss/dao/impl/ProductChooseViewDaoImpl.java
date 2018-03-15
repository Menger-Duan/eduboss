package com.eduboss.dao.impl;

import com.eduboss.dao.ProductChooseViewDao;
import com.eduboss.domain.ProductChooseView;
import org.springframework.stereotype.Repository;

/**
 * Created by xuwen on 2014/12/29.
 */
@Repository("productChooseViewDao")
public class ProductChooseViewDaoImpl extends GenericDaoImpl<ProductChooseView,String> implements ProductChooseViewDao {
}
