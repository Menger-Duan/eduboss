package com.eduboss.utils;

import com.eduboss.dto.Response;
import org.apache.commons.lang.StringUtils;

public class ParamCheckUtil {
	/**
	 * 判断如果为空就返回错误
	 * @param param
	 * @param res
	 * @return
	 */
	 public static Boolean checkParamIsNull(String param,Response res){
	 	if(StringUtils.isNotBlank(param)){
	 		return true;
		}
		res.setResultCode(-1);
	 	res.setResultMessage("参数有问题");
	 	return false;
	 }
}
