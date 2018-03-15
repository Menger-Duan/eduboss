package com.eduboss.domainVo;

import com.eduboss.domain.TwoTeacherClassTwo;

import java.util.List;

/**
 * Created by xuwen on 2014/12/29.
 */
public class ProductChooseVo {

    private ProductVo product;

    private MiniClassVo miniClass;
    
    private LectureClassVo lectureClass;

    private TwoTeacherClassTwoVo twoTeacherClassTwo;
    
    private String samllEnroll; //小班报名查询产品

    private String teacherId;

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    public MiniClassVo getMiniClass() {
        return miniClass;
    }

    public void setMiniClass(MiniClassVo miniClass) {
        this.miniClass = miniClass;
    }

    private List<String> expressions;

    public List<String> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<String> expressions) {
        this.expressions = expressions;
    }

	public LectureClassVo getLectureClass() {
		return lectureClass;
	}

	public void setLectureClass(LectureClassVo lectureClass) {
		this.lectureClass = lectureClass;
	}

	public String getSamllEnroll() {
		return samllEnroll;
	}

	public void setSamllEnroll(String samllEnroll) {
		this.samllEnroll = samllEnroll;
	}

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public TwoTeacherClassTwoVo getTwoTeacherClassTwo() {
        return twoTeacherClassTwo;
    }

    public void setTwoTeacherClassTwo(TwoTeacherClassTwoVo twoTeacherClassTwo) {
        this.twoTeacherClassTwo = twoTeacherClassTwo;
    }
}
