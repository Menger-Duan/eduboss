package com.eduboss.domainVo;

import java.io.Serializable;

public class MiniClassMaxMinDateVo implements Serializable {

    private static final long serialVersionUID = -6217910978853974090L;
    private String miniClassId;
    private String maxCourseDate;
    private String minCourseDate;
    
    public String getMiniClassId() {
        return miniClassId;
    }
    public void setMiniClassId(String miniClassId) {
        this.miniClassId = miniClassId;
    }
    public String getMaxCourseDate() {
        return maxCourseDate;
    }
    public void setMaxCourseDate(String maxCourseDate) {
        this.maxCourseDate = maxCourseDate;
    }
    public String getMinCourseDate() {
        return minCourseDate;
    }
    public void setMinCourseDate(String minCourseDate) {
        this.minCourseDate = minCourseDate;
    }
    
}
