package com.eduboss.jedis;

import com.eduboss.common.ProductType;

import java.io.Serializable;

/** 
 * @author  author :Yao 
 * @date  2017年2月25日 下午12:00:53
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class StudentStatusMsg implements Serializable{
    private static final long serialVersionUID = 7792729L;
    private String studentId;
    private ProductType type;


    public StudentStatusMsg() {
	}

    public StudentStatusMsg(String i, ProductType type) {
		this.studentId=i;
		this.type=type;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
}
