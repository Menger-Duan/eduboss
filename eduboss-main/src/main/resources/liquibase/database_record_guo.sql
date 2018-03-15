--liquibase formatted sql

--changeset 郭诗博:#918
--comment 小班与双师的产品与建班优化
ALTER TABLE `two_teacher_class`
ADD COLUMN `PHASE`  varchar(32) NULL AFTER `CLASS_TIME_LENGTH`;


--changeset 郭诗博:#964
--comment
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getOtherProductForEcs', 'ANON_RES', '/ProductController/getOtherProductForEcs.do');

DROP TABLE if EXISTS `product_package`;
 CREATE TABLE `product_package`(
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `parent_product_id` varchar(32) DEFAULT NULL COMMENT '父级产品ID',
 `son_product_id` varchar(32) DEFAULT NULL COMMENT '子产品ID',
 `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
 `create_time` varchar(20)  DEFAULT NULL COMMENT '创建时间',
 PRIMARY KEY (`id`)
 )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--changeset 郭诗博:#batchsave 2017060601
--comment
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('batchSaveTwoTeacherClassTwo', 'ANON_RES', '/TwoTeacherClassController/batchSaveTwoTeacherClassTwo.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherClassTeacher', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherClassTeacher.do');

--changeset 郭诗博:#delSummaryLog 2017060701
--comment
DELETE FROM resource WHERE ID ='summaryLog';


--changeset 郭诗博:#1040
--comment 双师考勤页面
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`, `PARENT_ID`, `RNAME`) VALUES ('twoTeacherClassTwoCourse', 'MENU', 'function/twoteacher/twoTeacherClassTwoCourse.html', 'twoteacherclass', '双师考勤');

UPDATE resource SET RNAME='双师考勤（分公司）' WHERE ID='twoTeacherClassTwoCourse';

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherClassCourseList', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherClassCourseList.do');

ALTER TABLE `two_teacher_class_student_attendent`
ADD COLUMN `CLASS_TWO_ID`  int(11) NULL COMMENT '辅班id' AFTER `TWO_CLASS_COURSE_ID`,
ADD COLUMN `ATTENDANCE_PIC_NAME`  varchar(50) NULL COMMENT '考勤图片地址' AFTER `version`;



UPDATE two_teacher_class_student_attendent a, two_teacher_class_student b  SET a.CLASS_TWO_ID= b.CLASS_TWO_ID WHERE a.STUDENT_ID=b.STUDENT_ID;


ALTER TABLE `two_teacher_class_student_attendent`
ADD COLUMN `COURSE_STATUS`  varchar(32) NULL COMMENT '课程状态' AFTER `MODIFY_USER_ID`;

ALTER TABLE `two_teacher_class_student_attendent`
ADD COLUMN `AUDIT_STATUS`  varchar(32) NULL COMMENT '审批状态' AFTER `COURSE_STATUS`;


UPDATE two_teacher_class_student_attendent SET COURSE_STATUS='NEW';

UPDATE two_teacher_class_student_attendent SET AUDIT_STATUS='UNAUDIT';

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherClassStudentAttendentList', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherClassStudentAttendentList.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('modifyTwoTeacherClassTwoCourseStudentAttendance', 'ANON_RES', '/TwoTeacherClassController/modifyTwoTeacherClassTwoCourseStudentAttendance.do');

ALTER TABLE `account_charge_records`
ADD COLUMN `TWO_TEACHER_STUDENT_ATTENDENT`  int(32) NULL COMMENT '双师考勤记录id' AFTER `MINI_CLASS_COURSE_ID`;


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('updateTwoTeacherSupplement', 'ANON_RES', '/TwoTeacherClassController/modifyTwoTeacherClassTwoCourseStudentAttendanceSupplement.do');

DROP TABLE IF EXISTS `student_parent_fuzhouAll`;
CREATE TABLE `student_parent_fuzhouAll` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SCHOOL_NAME` varchar(32) DEFAULT NULL COMMENT '学校名称',
  `STUDENT_NAME` varchar(32) DEFAULT NULL COMMENT '学生名字',
  `CLASS_NAME` varchar(32) DEFAULT NULL COMMENT '班级名称',
  `GRADE` varchar(20) DEFAULT NULL COMMENT '年级',
  `CONTACT` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `FATHER` varchar(32) DEFAULT NULL COMMENT '父亲名称',
  `FATHER_CONTACT` varchar(32) DEFAULT NULL COMMENT '父亲联系电话',
  `FATHER_ADDRESS` varchar(100) DEFAULT NULL COMMENT '父亲地址',
  `MOTHER` varchar(32) DEFAULT NULL COMMENT '母亲名称',
  `MOTHER_CONTACT` varchar(32) DEFAULT NULL COMMENT '母亲联系电话',
  `MOTHER_ADDRESS` varchar(100) DEFAULT NULL COMMENT '母亲地址',
  `LONGITUDE` varchar(32) DEFAULT NULL COMMENT '经度',
  `LATITUDE` varchar(32) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '福州全部';

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCounties', 'ANON_RES', '/SystemAction/getCounties.do');

INSERT INTO `system_message_manage`
(`ID`, `MSG_NO`, `STATUS`, `MSG_NAME`, `MSG_TYPE`, `LOGIC_SQL`, `LABEL_GATHER`, `MSG_CONTENT`, `SEND_TYPE`, `DETAILED_INFORMATION`)
VALUES
('2c9080cd5cf87bbc015cf887133f0027', 'M14', 'VALID', '双师扣费冲销', 'DAT0000000377', '1', '[]', '1', 'SMS,SYS_MSG', '1');


UPDATE two_teacher_class_student_attendent c
left join two_teacher_class_course d on d.course_id = c.TWO_CLASS_COURSE_ID
left join two_teacher_class a on a.CLASS_ID=d.class_id
left join two_teacher_class_two e on e.CLASS_ID= a.CLASS_ID
left join two_teacher_class_student b on b.CLASS_TWO_ID= e.CLASS_TWO_ID
SET c.CLASS_TWO_ID=b.CLASS_TWO_ID
where  c.STUDENT_ID=b.STUDENT_ID
AND c.CLASS_TWO_ID IS NULL;





CREATE
     OR REPLACE ALGORITHM = UNDEFINED
    SQL SECURITY INVOKER
VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`,
	NULL AS `two_class_id`
FROM
	`product` `p`
WHERE
	(
		(
			`p`.`CATEGORY` <> 'SMALL_CLASS'
		)
		AND (
			`p`.`CATEGORY` <> 'TWO_TEACHER'
		)
	)
UNION ALL
	SELECT
		`p`.`ID` AS `id`,
		`p`.`ID` AS `product_id`,
		NULL AS `mini_class_id`,
		NULL AS `LECTURE_ID`,
		NULL AS `two_class_id`
	FROM
		`product` `p`
	WHERE
		(
			(
				`p`.`CATEGORY` = 'SMALL_CLASS'
			)
			AND (
				`p`.`CLASS_TYPE_ID` = 'DAT0000000240'
			)
		)
	UNION ALL
		SELECT
			ifnull(
				`mc`.`MINI_CLASS_ID`,
				`p`.`ID`
			) AS `id`,
			`p`.`ID` AS `product_id`,
			`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
			NULL AS `LECTURE_ID`,
			NULL AS `two_class_id`
		FROM
			(
				`mini_class` `mc`
				LEFT JOIN `product` `p` ON (
					(`mc`.`PRODUCE_ID` = `p`.`ID`)
				)
			)
		WHERE
			(`mc`.`STATUS` <> 'CONPELETE')
		UNION ALL
			SELECT
				ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
				`p`.`ID` AS `product_id`,
				NULL AS `MINI_CLASS_ID`,
				`lc`.`LECTURE_ID` AS `LECTURE_ID`,
				NULL AS `two_class_id`
			FROM
				(
					`lecture_class` `lc`
					LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
				)
			UNION ALL
				SELECT
					`tctt`.`CLASS_TWO_ID` AS `CLASS_TWO_ID`,
					`tctc`.`PRODUCE_ID` AS `product_id`,
					NULL AS `MINI_CLASS_ID`,
					NULL AS `LECTURE_ID`,
					`tctt`.`CLASS_TWO_ID` AS `two_class_id`
				FROM
					(
						`two_teacher_class_two` `tctt`
						LEFT JOIN `two_teacher_class` `tctc` ON (
							(
								`tctt`.`CLASS_ID` = `tctc`.`CLASS_ID`
							)
						)
					)
        WHERE
        (`tctc`.STATUS<>'CONPELETE')

--changeset 郭诗博: 20170710
--comment 在线报读老师用户属性
INSERT INTO `resource`
(`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
VALUES ('findUserTeacherAttribute', 'ANON_RES', NULL, NULL, NULL, NULL, '/SystemAction/findUserTeacherAttribute.do', NULL, NULL, NULL, NULL, NULL, NULL);

DROP TABLE if EXISTS `user_teacher_attribute`;
CREATE TABLE `user_teacher_attribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `teacher_switch` tinyint(1) DEFAULT '0' COMMENT '授课老师开关',
  `pic_url` varchar(512) DEFAULT NULL COMMENT '头像地址',
  `video_url` varchar(512) DEFAULT NULL COMMENT '介绍视频地址',
  `university` varchar(32) DEFAULT NULL COMMENT '毕业院校',
  `degree` varchar(32) DEFAULT NULL COMMENT '学历',
  `grade_id` varchar(32) DEFAULT NULL COMMENT '授课年级',
  `subject_id` varchar(32) DEFAULT NULL COMMENT '授课科目',
  `remark` varchar(1024) DEFAULT NULL COMMENT '个人简介',
  `teachingStyle` varchar(1024) DEFAULT NULL COMMENT '教学风格',
  `recommend_status` varchar(20) NOT NULL DEFAULT 'NOT_RECOMMEND' COMMENT '推荐状态：不推荐NOT_RECOMMEND,推荐RECOMMEND',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8



INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getStudentAddressAndLatLog', 'ANON_RES', '/StudentController/getStudentAddressAndLatLog.do');

ALTER TABLE `student_school`
ADD COLUMN `LOG`  varchar(255) NULL COMMENT '学校经度' AFTER `ADDRESS`,
ADD COLUMN `LAT`  varchar(255) NULL COMMENT '学校维度' AFTER `LOG`;


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getStudentSchoolByRegionId', 'ANON_RES', '/CommonAction/getStudentSchoolByRegionId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCampusByRegionId', 'ANON_RES', '/CommonAction/getCampusByRegionId.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('editUserTeacherAttribute', 'ANON_RES', '/SystemAction/editUserTeacherAttribute.do');


UPDATE resource SET RNAME='学生地域分析' WHERE ID='customerMapAnalyze';


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('deleteStudentInTwoTeacherTwo', 'ANON_RES', '/TwoTeacherClassController/deleteStudentInTwoTeacherTwo.do');


INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('deleteTwoTeacherClass', 'BUTTON', '退班', 'twoteacherclassbrench', 'deleteTwoTeacherClass');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findUserId', 'ANON_RES', '/SystemAction/existUserId.do');


ALTER TABLE `organization`
ADD COLUMN `state_of_emergency`  varchar(32) NULL DEFAULT 'NORMAL' COMMENT '组织架构紧急状态' AFTER `area_id`;


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findOrganization', 'ANON_RES', '/SystemAction/findOrganization.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getAllBranch', 'ANON_RES', '/SystemAction/getAllBranch.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getDeptAndCampusByBranchId', 'ANON_RES', '/SystemAction/getDeptAndCampusByBranchId.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('updateOrgStatusById', 'ANON_RES', '/SystemAction/updateOrgStatusById.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('updateBatchOrgStatusById', 'ANON_RES', '/SystemAction/updateBatchOrgStatusById.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
VALUES ('stateOfEmergency', 'MENU', '紧急状态切换管理', '0', '7', '1', NULL, NULL, NULL, NULL, '紧急状态切换管理', NULL, NULL);


UPDATE role_ql_config a,(SELECT id, VALUE FROM role_ql_config WHERE ROLE_ID in ('specialRole', 'specialRole1', 'specialRole2')) b SET a.`VALUE`=CONCAT(b.`VALUE`, " and ${isForbidden} ")
WHERE a.ID = b.id;



INSERT INTO `resource`
(`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`, `new_url`)
VALUES ('getSmallClassXuDuListToExcelSize', 'ANON_RES', NULL, NULL, NULL, NULL, '/CourseController/getSmallClassXuDuListToExcelSize.do', NULL, NULL, NULL, NULL, NULL, NULL, NULL);



INSERT INTO `resource`
(`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`, `new_url`)
VALUES ('getSmallClassXuDuListToExcel', 'ANON_RES', NULL, NULL, NULL, NULL, '/CourseController/getSmallClassXuDuListToExcelSize.do', NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`, `new_url`)
VALUES ('smallClassXuDuToExcel', 'BUTTON', '续读明细', '0', 'RES0000000089', '2', NULL, 'SMALL_CLASS_XUDU_TOEXCEL_BTN', NULL, NULL, NULL, NULL, NULL, NULL);



ALTER TABLE `account_charge_records`
ADD COLUMN `curriculum`  int(11) NULL AFTER `TWO_TEACHER_STUDENT_ATTENDENT`;


DROP TABLE if EXISTS `curriculum`;
CREATE TABLE `curriculum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` varchar(32) DEFAULT NULL COMMENT '直播课时id',
  `curriculum_name` varchar(32) DEFAULT NULL COMMENT '直播班课名称',
  `grade` varchar(32) DEFAULT NULL COMMENT '年级',
  `subject` varchar(512) DEFAULT NULL COMMENT '科目',
  `teacher` varchar(32) DEFAULT NULL COMMENT '主讲老师',
  `type` varchar(32) DEFAULT NULL COMMENT '班课类型',
  `date` varchar(32) DEFAULT NULL COMMENT '上课日期',
  `time` varchar(32) DEFAULT NULL COMMENT '上课时间',
  `course_hours` decimal(10,2) DEFAULT NULL COMMENT '课时',
  `course_amount` decimal(10,2) DEFAULT NULL COMMENT '扣费金额',
  `operate_user_id` varchar(32) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


ALTER TABLE `contract`
ADD COLUMN `curriculum_id`  varchar(32) NULL COMMENT '直播同步合同的订单号' AFTER `pub_pay_contract`;



-- #1509小班续读明细导出，续读班级信息增加授课老师、上课时间
-- 计算时间
DROP FUNCTION IF EXISTS `timeADD`;
DELIMITER ;;
CREATE  FUNCTION `timeADD`(classTime varchar(32), CLASS_TIME_LENGTH int(11), EVERY_COURSE_CLASS_NUM decimal(9,2)) RETURNS varchar(32) CHARSET utf8
begin
DECLARE  value varchar(32);
SET @addSecond = CLASS_TIME_LENGTH*EVERY_COURSE_CLASS_NUM*60;
set @dt = CONCAT("2017-09-30 ",classTime,":00");
SET value = date_add(@dt , interval @addSecond second);
SET value = SUBSTR(value,12,5);
SET VALUE = CONCAT(classTime,"-",value);
return value;
end
;;
DELIMITER ;



DROP TABLE if EXISTS `objectHashCode`;
CREATE TABLE `objectHashCode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hashCode` int(32) NOT NULL COMMENT 'hashcode',
  `contractId` varchar(32) DEFAULT NULL COMMENT '合同id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hashCode` (`hashCode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


ALTER TABLE `curriculum`
ADD COLUMN `operate_time`  varchar(32) CHARACTER SET utf8mb4 NULL COMMENT '课程扣费或冲销的发生时间' AFTER `request_id`;


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherClassTwoXuDuListToExcel', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherClassTwoXuDuListToExcel.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherClassTwoXuDuRate', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherClassTwoXuDuRate.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherClassTwoXuDuRateSize', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherClassTwoXuDuRateSize.do');

INSERT INTO resource (ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL) VALUE ('twoTeacherBaoBiao', 'MENU', '双师报表', 2, 'RES0000000065',12, '#');

INSERT INTO resource (ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL) VALUE ('twoTeacherClassTwoXuBaolv', 'MENU', '双师续报率', 0, 'twoTeacherBaoBiao',1, 'function/reportforms/twoTeacherClassTwoXuBaolv.html');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getAllBranchInTwoTeacher', 'ANON_RES', '/TwoTeacherClassController/getAllBranchInTwoTeacher.do');

INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('TWO_TEACHER_TWO_XUBAO_EXPORT','BUTTON','双师续报excel导出按钮',0,'twoTeacherBaoBiao',0,'TWO_TEACHER_TWO_XUBAO_EXPORT');


UPDATE funds_change_history fch, contract c SET fch.AUDIT_TYPE="SYSTEM"  WHERE fch.CONTRACT_ID =c.ID AND c.CONTRACT_TYPE="LIVE_CONTRACT";


-- #1826 分公司建班人小班管理权限优化 优化：如果一个用户分配了“分公司建班人”的角色，那么这个用户的数据权限范围无论是在“分公司”还是在“校区”，都可以在小班管理界面建立用户所在分公司所有校区的小班，并且可以查看其所在分公司的所有校区的小班；
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCampusForSmallClassCourse', 'ANON_RES', '/CommonAction/getCampusForSmallClassCourse.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getMiniClassAttendsAnalyzeToExcel', 'ANON_RES', '/ReportAction/getMiniClassAttendsAnalyzeToExcel.do');

-- #1769 【增加“导出Excel”按钮】
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('miniClassAttendsExcelExport','BUTTON','小班课时消耗excel导出按钮',0,'miniClassAttendsAnalyze2',0,'MINI_CLASS_ATTENDS_EXCEL_EXPORT');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('ssepush', 'ANON_RES', '/sse/push.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('videoPlay', 'ANON_RES', 'function/modals/videoPlay.html');


INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('MCFullClassRateToExcelBTN','BUTTON','小班满班率excel导出按钮',0,'miniClassFullClassRate',0,'MCFullClassRateToExcelBTN');

INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('QuitMCRateToExcelBTN','BUTTON','小班退班率excel导出按钮',0,'miniClassQuitClassRate',0,'QuitMCRateToExcelBTN');



INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getExcelOooMiniOtmCourseConsumeTeacherView', 'ANON_RES', '/ReportAction/getExcelOooMiniOtmCourseConsumeTeacherView.do');

INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('TEACHER_GRADE_DISTRIBUTION_EXCEL','BUTTON','excel导出按钮',0,'oooMiniCourseTeacherWithGrade',0,'TEACHER_GRADE_DISTRIBUTION_EXCEL');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getExcelMiniClaCourseAuditAnalyz', 'ANON_RES', '/CourseController/getExcelMiniClaCourseAuditAnalyzeSalary.do');

INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('MINI_AUDIT_ANALYZE_SALARY_EXCEL','BUTTON','excel导出按钮',0,'miniClaCourseAuditAnalyzeSalarys',0,'MINI_AUDIT_ANALYZE_SALARY_EXCEL');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getExcelOtmClaCourseAuditAnalyz', 'ANON_RES', '/OtmCourseController/getExcelOtmClaCourseAuditAnalyzeSalary.do');

INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('OTM_AUDIT_ANALYZE_SALARY_EXCEL','BUTTON','excel导出按钮',0,'otmClaCourseAuditAnalyzeSalary',0,'OTM_AUDIT_ANALYZE_SALARY_EXCEL');


INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`)
VALUES ('subqueryMiniCourseConsumeb', '老师小班课时分布统计子查询', '学管主管看到当前校区所有学管', 'ROL0000000007', 'mc.BL_CAMPUS_ID in (select id from organization where orgLevel like \'${blCampusId}%\')', 'or', 'sql', 'FALSE');

INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`)
VALUES ('subqueryMiniCourseConsumec', '老师小班课时分布统计子查询', '班主任看到自己的数据', 'ROL0000000001', 'mc.STUDY_MANEGER_ID= \'${userId}\'', 'or', 'sql', 'FALSE');

INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`)
VALUES ('subqueryMiniCourseConsumed', '老师小班课时分布统计子查询', '老师看到自己的数据', 'ROL00000002', 'mcc.TEACHER_ID= \'${userId}\'', 'or', 'sql', 'FALSE');

INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`)
VALUES ('subqueryMiniCourseConsumea', '老师小班课时分布统计子查询', '走组织架构', NULL, 'mc.BL_CAMPUS_ID in (${eachOrganizationCampus})', 'or', 'sql', 'FALSE');

release/20180117
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('canUpdateIncomeDistribution', 'ANON_RES', '/ContractAction/canUpdateIncomeDistribution.do');

ALTER TABLE `course_hours_distribute_record`
ADD COLUMN `BL_CAMPUS_ID`  varchar(32) NULL COMMENT '校区' AFTER `CREATE_USER_ID`;

release/20180117 END
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getOneOnOneAutoRecogCount', 'ANON_RES', '/CourseController/getOneOnOneAutoRecogCount.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getOneOnOneAuditSummary', 'ANON_RES', '/CourseController/getOneOnOneAuditSummary.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('oooMiniCourseTeacherWithGradeOri', 'MENU', '老师课时年级分布(汇总)', '0', 'RES0000000084', '4', 'function/reportforms/oooMiniCourseConsumeTeacherWithGradeOrigin.html');

UPDATE resource SET RORDER=5 WHERE ID='miniClassCourseAudit';


INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG)
VALUES('ORI_TEACHER_GRADE_DISTRIBUTION_E','BUTTON','excel导出按钮',0,'oooMiniCourseTeacherWithGradeOri',0,'ORI_TEACHER_GRADE_DISTRIBUTION_E');

UPDATE resource SET RNAME='老师课时年级分布(明细)' WHERE ID='oooMiniCourseTeacherWithGrade';


UPDATE resource SET RNAME='老师课时年级分布(汇总)' ,RURL='function/reportforms/oooMiniCourseConsumeTeacherWithGrade.html' WHERE ID='oooMiniCourseTeacherWithGrade';
UPDATE resource SET RNAME='老师课时年级分布(明细)' ,RURL='function/reportforms/oooMiniCourseConsumeTeacherWithGradeOrigin.html' WHERE ID='oooMiniCourseTeacherWithGradeOri';