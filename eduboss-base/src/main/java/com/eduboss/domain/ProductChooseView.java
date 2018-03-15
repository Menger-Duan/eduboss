package com.eduboss.domain;

import javax.persistence.*;

/**
 * Created by xuwen on 2014/12/29.
 */
@Entity
@Table(name = "product_choose_view")
public class ProductChooseView {

    private String id;

    private Product product;
    private MiniClass miniClass;
    private LectureClass lectureClass;

    private TwoTeacherClassTwo twoTeacherClassTwo;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mini_class_id", insertable=false, updatable=false)
    public MiniClass getMiniClass() {
        return miniClass;
    }

    public void setMiniClass(MiniClass miniClass) {
        this.miniClass = miniClass;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_id", insertable=false, updatable=false)
	public LectureClass getLectureClass() {
		return lectureClass;
	}

	public void setLectureClass(LectureClass lectureClass) {
		this.lectureClass = lectureClass;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "two_class_id", insertable=false, updatable=false)
    public TwoTeacherClassTwo getTwoTeacherClassTwo() {
        return twoTeacherClassTwo;
    }

    public void setTwoTeacherClassTwo(TwoTeacherClassTwo twoTeacherClassTwo) {
        this.twoTeacherClassTwo = twoTeacherClassTwo;
    }
}
