package com.eduboss.domainVo;

import com.eduboss.domain.Curriculum;

import java.util.List;

public class ChargeOrWashCurriculumVo {

    private List<Curriculum> curriculumList;

    public List<Curriculum> getCurriculumList() {
        return curriculumList;
    }

    public void setCurriculumList(List<Curriculum> curriculumList) {
        this.curriculumList = curriculumList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeOrWashCurriculumVo that = (ChargeOrWashCurriculumVo) o;

        return curriculumList != null ? curriculumList.equals(that.curriculumList) : that.curriculumList == null;
    }

    @Override
    public int hashCode() {
        return curriculumList != null ? curriculumList.hashCode() : 0;
    }

}
