package com.eduboss.common;

/**
 * 手机端 需要 课程状态的 验证
 * @author Administrator
 *
 */
public enum CourseSummaryStatus {
	
	HAS_UNCHECH_ATTENDANCE("HAS_UNCHECH_ATTENDANCE", "有未考勤的课程"), 
	HAS_NO_UNCHECH_ATTENDANCE("HAS_NO_UNCHECH_ATTENDANCE","没有未考勤的课程"), 
	NO_COURSE("NO_COURSE","没有课程");

    private String value;
    private String name;

    private CourseSummaryStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
