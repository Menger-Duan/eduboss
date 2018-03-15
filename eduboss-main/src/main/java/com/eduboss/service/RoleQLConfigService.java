package com.eduboss.service;

import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.dto.RoleQLConfigVo;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2014/12/11.
 */
public interface RoleQLConfigService {

    public static final Pattern elExpressionPattern = Pattern.compile("\\$\\{(.*?)\\}");

    public void save(RoleQLConfig roleQLConfig);

    public void delete(RoleQLConfig roleQLConfig);

    public DataPackage findPage(DataPackage dp, RoleQLConfigVo roleQLConfigVo);

    public RoleQLConfig findById(String id);

    /**
     * 根据key返回当前权限所有配置
     * @param name
     * @param type
     * @return
     */
    public List<RoleQLConfig> findAllByNameAndType(String name,String type);

    public String findAllByNameAndType(RoleQLConfigSearchVo vo,Map keyMap);

    public String getAppendSqlByOrgAndRoleByConfig(RoleQLConfigSearchVo vo,Map keyMap);


    public String getAppendSqlByAllOrg(String name,String type,String alias);

    /**
     * 获取所有组织架构的查询sql
     * @return
     */
    public String getAllOrgAppendSql();

    /**
     * 根据key返回当前权限配置值并按照and或or拼接
     * and直接
     * @param name
     * @return
     */
    public String getValueResult(String name,String type);

    /**
     * 获取配置表达式的值
     * @param key
     * @return
     */
    public Object getExpressionValue(String key);


    String replaceExpression(String source);
}
