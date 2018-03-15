package com.eduboss.mail.pojo;

import org.apache.commons.lang3.StringUtils;

/**@author wmy
 *@date 2015年9月24日上午11:31:22
 *@version 1.0 
 *@description
 */
public class MailInfo {
    
	private String mailAddr;  //用户邮件地址(支持别名): user_id@domain_name
	private Integer fid;  //要列举的文件夹标识, 缺省为收件箱 (1)
	private Integer skip; //(翻页控制) 跳过信件数, 缺省为 0
	private Integer limit;//(翻页控制) 返回信件数量上限, 缺省为 100
	private String doubleDecode; //(容错控制) 是否再度执行 MIME 解码, 缺省为 false
	private String format = "xml";  //返回结果格式, 必须指定为 "xml", 否则无法通过 APIContext.result 获取结果
	
	public String getMailAddr() {
		return mailAddr;
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public Integer getSkip() {
		return skip;
	}
	public void setSkip(Integer skip) {
		this.skip = skip;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getDoubleDecode() {
		return doubleDecode;
	}
	public void setDoubleDecode(String doubleDecode) {
		this.doubleDecode = doubleDecode;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getAttrs(){
		String ret = null;
    	StringBuffer attrs = new StringBuffer();
    	if (this.fid != null) {
    		attrs.append("&fid=" + this.fid.toString());
    	}
    	if (this.skip != null) {
    		attrs.append("&skip=" + this.skip.toString());
    	}
    	if (this.limit != null) {
    		attrs.append("&limit=" + this.limit.toString());
    	}
    	if (StringUtils.isNotBlank(this.doubleDecode)) {
    		attrs.append("&doubleDecode=" + this.doubleDecode);
    	}
    	if (StringUtils.isNotBlank(this.format)) {
    		attrs.append("&format=" + this.format);
    	}
    	if(StringUtils.isNotBlank(attrs)) ret = attrs.toString().substring(1);
    	return ret;
	}
}


