--changeset yaoyuqi:5314-1
--comment 小班课时查询方法
delete from resource where id='findMiniClassWillBuyHour';
INSERT INTO `resource` (`ID`, `RTYPE`,  `RURL`) VALUES ('findMiniClassWillBuyHour', 'ANON_RES', '/CourseController/findMiniClassWillBuyHour.do');
--changeset yaoyuqi:5832-1
--comment 凭证导出
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('exportPaymentRecieptMainExcel', 'ANON_RES', '/OdsMonthPaymentRecieptController/exportPaymentRecieptMainExcel.do');

--changeset yaoyuqi:INTENET_PAY-1
--comment 网络支付通道
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('网络支付订单', 'ANON_RES', 'IntenetPayController/sendPayDetailInfo.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('查询网络支付订单', 'ANON_RES', 'IntenetPayController/findPayInfo.do');

drop table if exists INTENET_PAY;
CREATE TABLE INTENET_PAY (
    ID int auto_increment PRIMARY KEY,
    AMOUNT DECIMAL(10 , 2 ) COMMENT '金额',
    retcode VARCHAR(10) COMMENT '支付状态',
    FUNDS_CHANGE_ID VARCHAR(32) COMMENT '收款记录ID',
    CREATE_TIME VARCHAR(20) COMMENT '创建时间',
    CREATE_USER VARCHAR(32) COMMENT '创建人',
    MODIFY_TIME VARCHAR(32) COMMENT '修改时间',
    MODIFY_USER VARCHAR(32) COMMENT '修改人',
    REMARK VARCHAR(200) COMMENT '备注',
    TRXID VARCHAR(20) COMMENT '通联流水',
    CHNLTRIXID VARCHAR(50) COMMENT '支付宝微信支付流水',
    PAY_TYPE VARCHAR(20) COMMENT '支付类型',
    TITLE VARCHAR(100) COMMENT '支付标题',
    trxstatus VARCHAR(10) COMMENT '订单状态',
    PAY_INFO TEXT COMMENT '二维码信息',
    FINISH_TIME VARCHAR(20) COMMENT '支付完成时间',
    ERRMSG VARCHAR(100) COMMENT '错误信息',
    retmsg varchar(100) COMMENT '返回信息',
    reqsn varchar(32) COMMENT '商户订单号',
    contract_id varchar(32) comment '合同id'
    );

--changeset yaoyuqi:INTENET_PAY-2
--comment 网络支付通道
  alter table intenet_pay add status varchar(10) comment 'BOSS订单状态';

--changeset yaoyuqi:INTENET_PAY-3
--comment 网络支付通道
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('confirmPaid', 'ANON_RES', 'IntenetPayController/confirmPaid.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findPayInfoByFundId', 'ANON_RES', 'IntenetPayController/findPayInfoByFundId.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('reflushPayStatus', 'ANON_RES', 'IntenetPayController/reflushPayStatus.do');

--changeset yaoyuqi:INTENET_PAY-4
--comment 网络支付通道
alter table organization add SYB_CUSID varchar(50) comment '商户号', add SYB_APPID varchar(50) comment '应用Id', add SYB_APPKEY varchar(100) comment '签名';

--changeset yaoyuqi:INTENET_PAY-5
--comment 网络支付通道
alter table intenet_pay add version int comment '锁版本';

--changeset yaoyuqi:INTENET_PAY-6
--comment 网络支付通道
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getBelongBranchByCampusId', 'ANON_RES', '/CommonAction/getBelongBranchByCampusId.do');

--changeset yaoyuqi:Promise_1
--comment 精英班修改
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCanTurnToClassOrganization', 'ANON_RES', '/PromiseClassController/getCanTurnToClassOrganization.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getPromiseClassByCampus', 'ANON_RES', '/PromiseClassController/getPromiseClassByCampus.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('turnPromiseClass', 'ANON_RES', '/PromiseClassController/turnPromiseClass.do');

--changeset yaoyuqi:320_1
--comment 凭证报表添加重置功能
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RTAG`) VALUES ('BTN_EVIDENCE_FLUSH', 'BUTTON', '凭证终审后刷新', '0', 'RES0000000092', '32', 'BTN_EVIDENCE_FLUSH');

--changeset yaoyuqi:340_1
--comment 现金流凭证优化

drop table if exists receipt_funds_change_record;

create table receipt_funds_change_record
(funds_change_id varchar(32) comment '收款记录Id',receipt_date varchar(20) comment '凭证日期',BL_CAMPUS_ID varchar(32) comment '校区',receipt_month varchar(20) comment '凭证月份');

alter table funds_change_history add receipt_status int default 1 comment '凭证状态  0为已记录，1为未记录',add receipt_date varchar(20) comment '凭证日期';


--changeset yaoyuqi:353_1
--comment 学生一对一课时科目状态
drop table if exists ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS;
create table ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS
(ID INT PRIMARY KEY AUTO_INCREMENT,
STUDENT_ID VARCHAR(32),
STUDENT_NAME VARCHAR(100),
CAMPUS_ID VARCHAR(32),
CAMPUS_NAME VARCHAR(100),
BRENCH_ID VARCHAR(32),
BRENCH_NAME VARCHAR(100),
STUDY_MANAGER_ID VARCHAR(32),
STUDY_MANAGER_NAME VARCHAR(100),
COUNT_DATE VARCHAR(20),
CHINESE_SUBJECT VARCHAR(32),
MATH_SUBJECT VARCHAR(32),
ENGLISH_SUBJECT VARCHAR(32),
PHYSICS_SUBJECT VARCHAR(32),

CHEMISTRY_SUBJECT VARCHAR(32),
BIOLOGY_SUBJECT VARCHAR(32),
GEOGRAPHY_SUBJECT VARCHAR(32),
POLITICS_SUBJECT VARCHAR(32),
HISTORY_SUBJECT VARCHAR(32),

GROWING VARCHAR(32),
INFO_TECHNOLOGY VARCHAR(32),
OTHER_ENGLISH VARCHAR(32),
OTHER_MATH VARCHAR(32),
OTHER VARCHAR(32),
KNOWLEDGE VARCHAR(32),
PSYCHOLOGY VARCHAR(32),
JANPANESE VARCHAR(32),
OTHER_CHINESE VARCHAR(32),
STUDENT_ONEONONE_STATUS VARCHAR(32),

KEY `Index_1` (`CAMPUS_ID`),
KEY `Index_2` (`BRENCH_ID`),
KEY `Index_3` (`COUNT_DATE`));

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getStudentSubjectStatusReport', 'ANON_RES', '/ReportAction/getStudentSubjectStatusReport.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('studentSubjectStatusReport', 'MENU', '学生科目状态统计', '0', 'RES0000000093', '13', 'function/reportforms/studentSubjectStatusReport.html');


--changeset yaoyuqi:353_3 endDelimiter:\$\$
--comment 学生一对一课时科目状态

DROP PROCEDURE IF EXISTS `proc_ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS`;

$$

CREATE  PROCEDURE `proc_ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS`(in countDate varchar(20))
BEGIN
	delete from ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS where count_date=countDate;
    INSERT INTO ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS
			(
			`STUDENT_ID`,
			`STUDENT_NAME`,
			`CAMPUS_ID`,
			`CAMPUS_NAME`,
			`BRENCH_ID`,
			`BRENCH_NAME`,
			`STUDY_MANAGER_ID`,
			`STUDY_MANAGER_NAME`,
			`COUNT_DATE`,
			`STUDENT_ONEONONE_STATUS`)
			select s.id studentId,s.name studentName,o.id campusId,o.name campusName,o2.id brenchId,o2.name brenchName,u.user_id studyManId,u.name studyManName
			,countDate,s.ONEONONE_STATUS
			from student s
			left join organization  o on s.BL_CAMPUS_ID=o.id
			left join organization o2 on o2.id = o.parentID
			left join user u on u.user_id = s.STUDY_MANEGER_ID;


   update ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS a set
	a.`CHINESE_SUBJECT` =func_get_student_subject_status(a.student_id,'DAT0000000019'),
	a.`MATH_SUBJECT` =func_get_student_subject_status(a.student_id,'DAT0000000018'),
	a.`ENGLISH_SUBJECT` = func_get_student_subject_status(a.student_id,'DAT0000000014'),
	a.`PHYSICS_SUBJECT` =func_get_student_subject_status(a.student_id,'DAT0000000015'),
	a.`CHEMISTRY_SUBJECT` = func_get_student_subject_status(a.student_id,'DAT0000000016'),
	a.`BIOLOGY_SUBJECT` = func_get_student_subject_status(a.student_id,'DAT0000000020'),
	a.`GEOGRAPHY_SUBJECT` = func_get_student_subject_status(a.student_id,'DAT0000000021'),
	a.`POLITICS_SUBJECT` = func_get_student_subject_status(a.student_id,'DAT0000000029'),
	a.`HISTORY_SUBJECT` = func_get_student_subject_status(a.student_id,'DAT0000000030'),
	a.`GROWING` = func_get_student_subject_status(a.student_id,'DAT0000000222'),
	a.`INFO_TECHNOLOGY` = func_get_student_subject_status(a.student_id,'DAT0000000221'),
	a.`OTHER_ENGLISH` = func_get_student_subject_status(a.student_id,'DAT0000000204'),
	a.`OTHER_MATH` = func_get_student_subject_status(a.student_id,'DAT0000000203'),
	a.`OTHER` = func_get_student_subject_status(a.student_id,'DAT0000000127'),
	a.`KNOWLEDGE` = func_get_student_subject_status(a.student_id,'DAT0000000126'),
	a.`PSYCHOLOGY` = func_get_student_subject_status(a.student_id,'DAT0000000125'),
	a.`JANPANESE` = func_get_student_subject_status(a.student_id,'DAT0000000252'),
	a.`OTHER_CHINESE` = func_get_student_subject_status(a.student_id,'DAT0000000343')
	where COUNT_DATE=countDate;

END
$$

--changeset yaoyuqi:User_syn_1
--comment 用户同步
alter table user add employee_No varchar(32) comment '员工号';
--changeset yaoyuqi:323_1
--comment 营收凭证审核添加课程汇总确认窗口
delete from resource where id ='findIncomeAuditRate';
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findIncomeAuditRate', 'ANON_RES', '/IncomeEvidenceController/findIncomeAuditRate.do');

--changeset yaoyuqi:422_1
--comment 校区人员查看小班报表优化 JAN W2
UPDATE `role_ql_config` SET `VALUE`='org.id in (${eachOrganizationCampus})' WHERE `ID`='miniCourseConsumeTeacherOrganizationAll';
UPDATE `role_ql_config` SET `VALUE`='org.id in (select id from organization where orgLevel like \'${blCampusId}%\')' WHERE `ID`='miniCourseConsumeTeacherStudydReadAll';


--changeset yaoyuqi:534_1
--comment 页面收款功能增加条形码扫码支付
alter table intenet_pay add AUTH_CODE VARCHAR(18) comment '付款二维码';
ALTER TABLE `intenet_pay` ADD UNIQUE INDEX `reqsn_UNIQUE` (`reqsn` ASC);
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveBarCodeInfo', 'ANON_RES', 'IntenetPayController/saveBarCodeInfo.do');

--changeset yaoyuqi:567_1
--comment 学生科目状态统计报表新增导出功能
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('exportStudentSubjectStatusReport', 'ANON_RES', 'ReportAction/exportStudentSubjectStatusReport.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RTAG`) VALUES ('STU_SUBJECT_STATUS_EXPORT', 'BUTTON', 'excel导出', '0', 'RES0000000093', '1', 'STU_SUBJECT_STATUS_EXPORT');

