package com.eduboss.eo;

import com.eduboss.excel.anno.ExcelIgnore;
import com.eduboss.excel.imported.GenericExcelImportedEo;
import com.eduboss.excel.imported.anno.*;
import com.eduboss.excel.imported.metadata.CellValidationType;
import com.eduboss.excel.imported.metadata.ExcelConvertType;
import javassist.SerialVersionUID;

/**
 * Created by Administrator on 2016/7/5.
 */
@ImportedSheetDefine(sheetName = "数据收集格式模板")
public class StudentSchoolImportedEo extends GenericExcelImportedEo{

    @ExcelIgnore
    private static final long serialVersionUID = -2470964049085089730L;

    @CellImportedHeaderDefine("城市")// FIXME: 2016/12/7 现在学校的城市改成其他字段了
    @CellImportedConverterDefine(type = ExcelConvertType.SQL, sql = "select id from data_dict where CATEGORY ='CITY'  AND `NAME` =  '${value}'  ", throwIfMoreThanOneReturn = true)
    private String cityId;

    @CellImportedHeaderDefine("学校名字（必填）")
    private String schoolName;

    @CellImportedHeaderDefine("学校级别")
    @CellImportedConverterDefine(type = ExcelConvertType.SQL, sql = "select id from data_dict where CATEGORY = 'SCHOOL_LEVEL' AND  `NAME` = '${value}' ", throwIfMoreThanOneReturn = true)
    private String schoolLevelId;

    @CellNotMandatory
    @CellImportedHeaderDefine("学校地址")
    private String address;

    @CellNotMandatory
    @CellImportedHeaderDefine("学校联系方式")
//    @CellValidationDefine(type = CellValidationType.REGULAR, regEx = "^[0-9]*$", message = "电话号码格式错误")
    private String contact;

    @CellNotMandatory
    @CellImportedHeaderDefine("学校联系人")
    private String schoolLeader;

    @CellNotMandatory
//    @CellValidationDefine(type = CellValidationType.REGULAR, regEx = "^[0-9]*$", message = "电话号码格式错误")
    @CellImportedHeaderDefine("学校电话")
    private String schoolLeaderContact;

    @CellNotMandatory
    @CellImportedHeaderDefine("备注")
    private String remark;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolLevelId() {
        return schoolLevelId;
    }

    public void setSchoolLevelId(String schoolLevelId) {
        this.schoolLevelId = schoolLevelId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSchoolLeader() {
        return schoolLeader;
    }

    public void setSchoolLeader(String schoolLeader) {
        this.schoolLeader = schoolLeader;
    }

    public String getSchoolLeaderContact() {
        return schoolLeaderContact;
    }

    public void setSchoolLeaderContact(String schoolLeaderContact) {
        this.schoolLeaderContact = schoolLeaderContact;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
