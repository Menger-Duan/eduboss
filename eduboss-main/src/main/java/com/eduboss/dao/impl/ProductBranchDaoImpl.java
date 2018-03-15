package com.eduboss.dao.impl;

import com.eduboss.dao.ProductBranchDao;
import com.eduboss.domain.ProductBranch;
import org.springframework.stereotype.Repository;

@Repository("ProductBranchDao")
public class ProductBranchDaoImpl extends GenericDaoImpl<ProductBranch, String> implements ProductBranchDao {
}
