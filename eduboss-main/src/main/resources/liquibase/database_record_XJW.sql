#1366
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTwoTeacherClassCourseInfo', 'ANON_RES', '/CourseController/getTwoTeacherClassCourseInfo.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('excelTwoTeacherClassCourseInfo', 'ANON_RES', '/CourseController/excelTwoTeacherClassCourseInfo.do');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('twoTeacherCourse', 'ANON_RES', '/RealTimeReportController/twoTeacherCourse.do');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('twoCourseDetailByStatus', 'ANON_RES', 'function/modals/twoTeacherCourseDetailByStatus.html');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('twoStudentDetailByStatus', 'ANON_RES', 'function/modals/twoStudentDetailByStatus.html');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('twoCourseStudentByStatus', 'ANON_RES', '/RealTimeReportController/twoCourseStudentByStatus.do');


#1408


ALTER TABLE `mini_class`
ADD COLUMN `allowed_excess`  int(11) NULL COMMENT '允许超额人数' AFTER `is_modal`,
##ADD COLUMN `course_modal_id`  int(11) NULL COMMENT '课程模板id' AFTER `allowed_excess`;

INSERT INTO resource(ID, RTYPE, RURL) VALUE('batchSaveMiniClass', 'ANON_RES', '/CourseController/batchSaveMiniClass.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getBatchMiniClassNum', 'ANON_RES', '/CourseController/getBatchMiniClassNum.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getCampusContact', 'ANON_RES', '/CommonAction/getCampusContact.do');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('addMiniClass', 'ANON_RES', 'function/course/addMiniClass.html');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getOrgByIdAndTypeOption', 'ANON_RES', '/CommonAction/getOrgByIdAndTypeOption.do');
#1454


INSERT INTO resource(ID, RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUE('TWOCLASS_ROSTER_PHONE_BTN', 'BUTTON','打印点名册（含客户电话）',0,'twoteacherclassbrench',6, 'TWOCLASS_ROSTER_PHONE_BTN');
INSERT INTO resource(ID, RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUE('TWOCLASS_ROSTER_NOPHONE_BTN', 'BUTTON','打印点名册（无客户电话）',0,'twoteacherclassbrench',7, 'TWOCLASS_ROSTER_NOPHONE_BTN');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTwoCourseStudentRoster', 'ANON_RES', '/TwoTeacherClassController/getTwoCourseStudentRoster.do');

#1455

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTodayCourseBillList', 'ANON_RES', '/CourseController/getTodayCourseBillList.do');
INSERT INTO resource (ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL) VALUE ('getTodayCourseBill', 'MENU', '今日课时单', 0, '1', 5, 'function/course/getTodayCourseBill.html');

ALTER TABLE `course`
ADD COLUMN `COURSE_ATTENCE_TYPE`  varchar(32) NULL COMMENT '一对一考勤类型' AFTER `FIRST_ATTENDENT_TIME`;

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getCourseBillInfoById', 'ANON_RES', '/CourseController/getCourseBillInfoById.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getCourseBillsPrint', 'ANON_RES', '/CourseController/getCourseBillsPrint.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getCourseBillsPrintByDate', 'ANON_RES', '/CourseController/getCourseBillsPrintByDate.do');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getBillsPrintInfoByDate', 'ANON_RES', '/CourseController/getBillsPrintInfoByDate.do');

ALTER TABLE `course`
ADD COLUMN `course_version`  int(11) NULL DEFAULT 0 COMMENT '课程版本' AFTER `COURSE_ATTENCE_TYPE`;

#1524 
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTeachersForSelect', 'ANON_RES', '/TeacherSubjectController/getTeachersForSelect.do');

#1427
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTwoClassTwoToExcelSize', 'ANON_RES', '/TwoTeacherClassController/getTwoClassTwoToExcelSize.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTwoClassTwoToExcel', 'ANON_RES', '/TwoTeacherClassController/getTwoClassTwoToExcel.do');

INSERT INTO resource(ID, RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUE('TWO_CLASS_TOEXCEL_BTN', 'BUTTON','双师辅班导出明细',0,'twoteacherclassbrench',8, 'TWO_CLASS_TOEXCEL_BTN');

#1611
INSERT INTO resource(ID, RTYPE, RURL) VALUE('auditAutoRecog', 'ANON_RES', '/CourseController/auditAutoRecog.do');


#1698 20171104
ALTER TABLE `course`
ADD COLUMN `ATTENDANCE_DETAIL`  varchar(32) NULL COMMENT '考勤信息详情类型' AFTER `course_version`;

#1770 2017-11-16 
#目前在boss的uat添加了 便于测试 实际是在培优上 执行
INSERT INTO resource (ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL) VALUE ('miniClassFullClassRate', 'MENU', '小班满班率', 0, 'RES0000000094',7, 'function/reportforms/miniClassFullClassRate.html');
INSERT INTO resource (ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL) VALUE ('miniClassQuitClassRate', 'MENU', '小班退班率', 0, 'RES0000000094',8, 'function/reportforms/miniClassQuitClassRate.html');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getMiniClassFullClassRate', 'ANON_RES', '/ReportAction/getMiniClassFullClassRate.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getMiniClassQuitClassRate', 'ANON_RES', '/ReportAction/getMiniClassQuitClassRate.do');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getMiniClassFullClassRateSize', 'ANON_RES', '/ReportAction/getMiniClassFullClassRateSize.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getMiniClassQuitClassRateSize', 'ANON_RES', '/ReportAction/getMiniClassQuitClassRateSize.do');

INSERT INTO resource(ID, RTYPE, RURL) VALUE('getMCFullClassRateToExcel', 'ANON_RES', '/ReportAction/getMCFullClassRateToExcel.do');
INSERT INTO resource(ID, RTYPE, RURL) VALUE('getMCQuitClassRateToExcel', 'ANON_RES', '/ReportAction/getMCQuitClassRateToExcel.do');



