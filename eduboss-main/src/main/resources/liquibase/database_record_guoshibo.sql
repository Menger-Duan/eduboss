--liquibase formatted sql

--changeset 郭诗博:5390-1
--comment 短信通知老师和学管师
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('notifyTeacherAndStudyManager','ANON_RES','/SmsController/notifyTeacherAndStudyManager.do');

--changeset 郭诗博:5390-2
--comment 复制单日课程前的检查findStudentOneOnOnePaiKeInfo
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('checkCopyCourse','ANON_RES','/CourseController/checkCopyCourse.do');


--changeset 郭诗博:5390-3
--comment 老师学生课程表 新增课程查询学生一对一信息
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findStudentOneOnOnePaiKeInfo','ANON_RES','/StudentController/findStudentOneOnOnePaiKeInfo.do');


--changeset 郭诗博:5532-1
--comment 判断是否超过剩余课时
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('checkAheadOfRemainingHour','ANON_RES','/CourseController/checkAheadOfRemainingHour.do');

--changeset 郭诗博:5507-1
--comment 报合同不需要看到小班产品
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
`p`.`ID` AS `id`,
`p`.`ID` AS `product_id`,
NULL AS `mini_class_id`,
NULL AS `LECTURE_ID`
FROM
`product` `p`
WHERE p.CATEGORY<>'SMALL_CLASS'
UNION ALL
SELECT
ifnull(
`mc`.`MINI_CLASS_ID`,
`p`.`ID`
) AS `id`,
`p`.`ID` AS `product_id`,
`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
NULL AS `LECTURE_ID`
FROM
(
`mini_class` `mc`
LEFT JOIN `product` `p` ON (
(`mc`.`PRODUCE_ID` = `p`.`ID`)
)
)
UNION ALL
SELECT
ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
`p`.`ID` AS `product_id`,
NULL AS `MINI_CLASS_ID`,
`lc`.`LECTURE_ID` AS `LECTURE_ID`
FROM
(
`lecture_class` `lc`
LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
) ;

--changeset 郭诗博:5507-2
--comment 报合同不需要看到已完结的小班
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` <> 'SMALL_CLASS'
	)
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);

--changeset 郭诗博:5680
--comment 查询faq 指引的页面名称
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findPageNameByFaqId','ANON_RES','/FAQController/findPageNameByFaqId.do');


--changeset 郭诗博:5507-3
--comment 报合同不需要看到已完结的小班(先不上)
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);




--changeset 郭诗博:5507-4
--comment 报合同不需要看到已完结的小班 (国庆后上)
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` <> 'SMALL_CLASS'
	)
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);


--changeset 郭诗博:#5673
--comment   学生学校管理需要进行新增、查看和修改权限的分离

INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('addNewSchoolBtn','BUTTON','新增学生学校按钮','','addNewSchoolBtn');
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='addNewSchoolBtn';

INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('studentSchoolImportBtn','BUTTON','批量导入学校按钮','','studentSchoolImportBtn');
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolImportBtn';

INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('studentSchoolUpdateBtn','BUTTON','修改学生学校按钮','','studentSchoolUpdateBtn');
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolUpdateBtn';

--changeset 郭诗博:#5673-1
--comment   学生学校管理需要进行新增、查看和修改权限的分离
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' ) as a SET PARENT_ID = a.id WHERE r.ID='addNewSchoolBtn';

UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolImportBtn';

UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolUpdateBtn';

--changeset 郭诗博:#5673-2 （复制粘贴就是犯罪）
--comment   学生学校管理需要进行新增、查看和修改权限的分离

UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' ) as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolImportBtn';

UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' ) as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolUpdateBtn';



--changeset 郭诗博:5507-5
--comment 报合同不需要看到已完结的小班 (国庆后上)（又不上）
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);

--changeset 郭诗博:#5825-1
--comment   学校
drop TABLE if exists school_temp;
CREATE TABLE `school_temp` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(128) DEFAULT NULL,
  `VALUE` varchar(128) DEFAULT NULL,
  `AUDIT_STATUS` int(11) NOT NULL DEFAULT '2' COMMENT 'UNAUDIT(3, "未审批") UNVALIDATE(2, "审批不通过")  VALIDATE(1, "已审核")',
  `audit_user_id` varchar(32) DEFAULT NULL,
  `contract_id` varchar(32) DEFAULT NULL,
  `student_id` varchar(32) DEFAULT NULL,
  `ORDER_VAL` int(11) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL,
  `CATEGORY` varchar(32) DEFAULT NULL,
  `SCHOOL_STATUS` varchar(32) DEFAULT NULL,
  `CREATE_USER_ID` varchar(32) DEFAULT NULL,
  `CREATE_TIME` varchar(20) DEFAULT NULL,
  `MODIFY_USER_ID` varchar(32) DEFAULT NULL,
  `MODIFY_TIME` varchar(20) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '学校地址',
  `CONTACT` varchar(128) DEFAULT NULL COMMENT '联系方式',
  `REMARK` varchar(200) DEFAULT NULL,
  `SCHOOL_LEADER` varchar(100) DEFAULT NULL COMMENT '学校负责人',
  `SCHOOL_LEADER_CONTACT` varchar(32) DEFAULT NULL COMMENT '学校负责人联系方式',
  PRIMARY KEY (`ID`),
  KEY `INDEX_NAME` (`NAME`),
  KEY `student_school_reference_id_fk1` (`PARENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `resource`
(`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES
('stuSchoolManage', 'MENU', '学生学校管理', '2', 'RES0000000082', '1', '#', NULL, NULL, NULL, NULL, NULL, NULL);

UPDATE resource SET PARENT_ID='stuSchoolManage' WHERE RURL='function/student/studentschoolmanage.html';

INSERT INTO `resource`
(`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES
('auditStuSchool', 'MENU', '待审核学生学校', '0', 'stuSchoolManage', '2', 'function/student/studentschoolTempManage.html', NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO resource (ID,RTYPE,RURL) VALUES ('getSchoolTempList','ANON_RES','/StudentController/getSchoolTempList.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findSchoolTempById','ANON_RES','/StudentController/findSchoolTempById.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('matchingStudentSchool','ANON_RES','/StudentController/matchingStudentSchool.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('saveNewSchool','ANON_RES','/StudentController/saveNewSchool.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('unvalidSchool','ANON_RES','/StudentController/unvalidSchool.do');


--changeset 郭诗博:#5825-2
--comment   学校
ALTER TABLE `contract`
ADD COLUMN `SCHOOL_TEMP`  varchar(32) NULL AFTER `SCHOOL`;

ALTER TABLE `contract`
ADD COLUMN `schoolOrTemp`  varchar(32) NULL AFTER `SCHOOL_TEMP`;

--changeset 郭诗博:#5825-3
--comment   学校
 ALTER TABLE `student`
ADD COLUMN `SCHOOL_TEMP`  varchar(32) NULL AFTER `EREA`;

ALTER TABLE `student`
ADD COLUMN `schoolOrTemp`  varchar(32) NULL AFTER `SCHOOL`;

--changeset 郭诗博:#5825-4
--comment   学校
UPDATE contract SET schoolOrTemp='school';
UPDATE student SET schoolOrTemp ='school';


--changeset 郭诗博:#5673-3
--comment   学生学校管理需要进行新增、查看和修改权限的分离
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='addNewSchoolBtn';
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolImportBtn';
UPDATE resource r ,(SELECT id FROM resource  WHERE RNAME ='学生学校管理' AND PARENT_ID='stuSchoolManage') as a SET PARENT_ID = a.id WHERE r.ID='studentSchoolUpdateBtn';

--changeset 郭诗博:#5908
--comment   删除系统信息界面添加一列显示删除人员姓名
ALTER TABLE `deletedata_log`
ADD COLUMN `operation_user_id`  varchar(32) NULL AFTER `reason`;


--changeset 郭诗博:#5890
--comment   把角色功能权限中的“修改按钮”名称改为“账务”
UPDATE resource SET rname='账务' WHERE rname='修改按钮' AND ID='BTN_EDIT_STUDENT';


--changeset 郭诗博:5507-6
--comment 报合同不需要看到已完结的小班 这次真的上了
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` <> 'SMALL_CLASS'
	)
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);

--changeset 郭诗博:5507-7
--comment 报合同不需要看到已完结的小班 （又不上）
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);

--changeset 郭诗博:#131
--comment   小班管理优化
INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('updateSmallClass','BUTTON','修改小班','','updateSmallClass');
UPDATE resource r ,(SELECT ID FROM resource WHERE RNAME='小班管理') as a SET PARENT_ID = a.id WHERE r.ID='updateSmallClass';


INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('onlyUpdateSomeThing','BUTTON','修改小班（校区权限）','','onlyUpdateSomeThing');
UPDATE resource r ,(SELECT ID FROM resource WHERE RNAME='小班管理') as a SET PARENT_ID = a.id WHERE r.ID='onlyUpdateSomeThing';


INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('deleteSmallClass','BUTTON','删除小班','','deleteSmallClass');
UPDATE resource r ,(SELECT ID FROM resource WHERE RNAME='小班管理') as a SET PARENT_ID = a.id WHERE r.ID='deleteSmallClass';


INSERT INTO resource (ID,RTYPE,RNAME,PARENT_ID,RTAG) VALUES('addSmallClass','BUTTON','增加小班','','addSmallClass');
UPDATE resource r ,(SELECT ID FROM resource WHERE RNAME='小班管理') as a SET PARENT_ID = a.id WHERE r.ID='addSmallClass';

--changeset 郭诗博:#131-1
--comment   小班管理优化
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('getRenkeHekuaxiaoquTeacher','ANON_RES','/TeacherSubjectController/getRenkeHekuaxiaoquTeacher.do');


--changeset 郭诗博:5507-8
--comment  精英班课堂的小班产品可以报。其他的小班产品不能报
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` <> 'SMALL_CLASS'
	)
UNION ALL
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` = 'SMALL_CLASS' AND `p`.`CLASS_TYPE_ID`='DAT0000000240'
	)
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);

--changeset 郭诗博:5507-9
--comment
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);


--changeset 郭诗博:5507-10
--comment  精英班课堂的小班产品可以报。其他的小班产品不能报
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` <> 'SMALL_CLASS'
	)
UNION ALL
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` = 'SMALL_CLASS' AND `p`.`CLASS_TYPE_ID`='DAT0000000240'
	)
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);


--changeset 郭诗博:5507-11
--comment
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);


--changeset 郭诗博:#175
--comment  小班管理优化，独立页面继承分公司建班
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES ('miniClassManagerBranch', 'MENU', '小班管理（分公司）', '0', 'RES0000000089', '2', 'function/course/smallClassCourseBranch.html', NULL, NULL, 'DAT0000000400', '小班管理（分公司）', NULL, NULL);


--changeset 郭诗博:#175-1
--comment  小班管理优化，独立页面继承分公司建班 （查询所属分公司的所有校区）
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('selectOrgOptionOfBranch','ANON_RES','/CommonAction/selectOrgOptionOfBranch.do');

--changeset 郭诗博:#175-2
--comment   新增 小班课程 页面
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES ('miniClassCourseScheduleForBranch', 'ANON_RES', '小班课程', '0', NULL, '0', 'function/course/miniClassCourseScheduleForBranch.html', '', NULL, NULL, NULL, NULL, NULL);

--changeset 郭诗博:#194
--comment   小班产品添加“期”字段
ALTER TABLE `product`
ADD COLUMN `SMALL_CLASS_PHASE_ID`  varchar(32) NULL AFTER `PRODUCT_VERSION_ID`;



--changeset 郭诗博:5507-12
--comment  精英班课堂的小班产品可以报。其他的小班产品不能报
drop view if exists product_choose_view;
CREATE VIEW `product_choose_view` AS
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` <> 'SMALL_CLASS'
	)
UNION ALL
SELECT
	`p`.`ID` AS `id`,
	`p`.`ID` AS `product_id`,
	NULL AS `mini_class_id`,
	NULL AS `LECTURE_ID`
FROM
	`product` `p`
WHERE
	(
		`p`.`CATEGORY` = 'SMALL_CLASS' AND `p`.`CLASS_TYPE_ID`='DAT0000000240'
	)
UNION ALL
	SELECT
		ifnull(
			`mc`.`MINI_CLASS_ID`,
			`p`.`ID`
		) AS `id`,
		`p`.`ID` AS `product_id`,
		`mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
		NULL AS `LECTURE_ID`
	FROM
		(
			`mini_class` `mc`
			LEFT JOIN `product` `p` ON (
				(`mc`.`PRODUCE_ID` = `p`.`ID`)
			)
		)
	WHERE
		mc.`STATUS` <> 'CONPELETE'
	UNION ALL
		SELECT
			ifnull(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
			`p`.`ID` AS `product_id`,
			NULL AS `MINI_CLASS_ID`,
			`lc`.`LECTURE_ID` AS `LECTURE_ID`
		FROM
			(
				`lecture_class` `lc`
				LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`))
			);


--changeset 郭诗博:#333
--comment  boss系统学生电子账户增加“充值”功能
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
 VALUES ('rechargeMoneyModal', 'ANON_RES', NULL, NULL, NULL, NULL, '/function/student/rechargeMoneyModal.html', NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('rechargeMoney','ANON_RES','/StudentController/rechargeMoney.do');

--changeset 郭诗博:#333-1
--comment  充值按钮权限控制
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
VALUES ('BTN_STU_ACCOUNT_RECHARGE', 'BUTTON', '学生电子账户充值', '0', 'studentAccountList', '0', NULL, 'STU_ACCOUNT_RECHARGE', NULL, NULL, NULL, NULL, NULL);



--changeset 郭诗博:#423
--comment
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('getStudentRechargeRecord','ANON_RES','/ContractAction/getStudentRechargeRecord.do');

--changeset 郭诗博:#423-1
--comment
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES ('electronicAccountRechargeRecord', 'MENU', '学生电子账户充值记录', '0', 'RES0000000009', '9', 'function/contract/electronicAccountRechargeRecord.html', NULL, NULL, NULL, NULL, NULL, NULL);

--changeset 郭诗博:#467
--comment 小班管理（分公司）中的小班课程状态需要单独配置权限
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
VALUES ('BRANCH_SMALL_CLASS_COURSE_STATUS', 'BUTTON', '小班（分公司）课程状态修改', '0', 'miniClassManagerBranch', NULL, NULL, 'BRANCH_SMALL_CLASS_COURSE_STATUS', NULL, NULL, NULL, NULL, NULL);

/*
--changeset 郭诗博:#766
--comment

DROP PROCEDURE IF EXISTS proc_update_customerImport;
DELIMITER $$
CREATE  PROCEDURE `proc_update_customerImport`(
)
    SQL SECURITY INVOKER
BEGIN

	DECLARE cusId VARCHAR(32);
	DECLARE cusTypeName VARCHAR(50);
	DECLARE cusOrgName VARCHAR(50);
	DECLARE resEntranceName VARCHAR(50);
	DECLARE contacts VARCHAR(32);
	DECLARE cusType VARCHAR(32);
	DECLARE cusOrg VARCHAR(32);
	DECLARE resEntrance VARCHAR(32);
	DECLARE cusStatus VARCHAR(10);
	DECLARE failReason VARCHAR(1000);
	DECLARE contactNum INT;
	DECLARE schoolId VARCHAR(32);
	DECLARE regionId VARCHAR(32);
	DECLARE regionName VARCHAR(32);
	DECLARE schoolName VARCHAR(50);
	DECLARE gradeName VARCHAR(50);
	DECLARE gradeId VARCHAR(32);
	DECLARE schoolLevelId VARCHAR(32);
	DECLARE schoolLevelName VARCHAR(50);


	DECLARE cur1 CURSOR FOR select ID,CUS_TYPE_NAME,CUS_ORG_NAME,RES_ENTRANCE_NAME,CONTACT,SCHOOL_NAME,SCHOOL_REGION_NAME,GRADE_NAME,SCHOOL_LEVEL from customer_import_transform where cus_status='0';

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = true;

    SET @CURSOR_STOP_FLAG =false;

    OPEN cur1;

		FETCH cur1 into cusId,cusTypeName,cusOrgName,resEntranceName,contacts,schoolName,regionName,gradeName,schoolLevelName;

			WHILE !@CURSOR_STOP_FLAG do


				IF cusTypeName <> '' AND EXISTS (SELECT ID from data_dict where `NAME`=cusTypeName and CATEGORY='RES_TYPE')

					THEN

						SET cusType = (SELECT ID from data_dict where `NAME`=cusTypeName and CATEGORY='RES_TYPE');

						IF cusOrgName <> '' AND EXISTS (SELECT ID from data_dict where name=cusOrgName and PARENT_ID=(SELECT ID from data_dict where `NAME`=cusTypeName and CATEGORY='RES_TYPE') and CATEGORY='CUS_ORG')

							THEN

									SET cusOrg=(SELECT ID from data_dict where name=cusOrgName and PARENT_ID=(SELECT ID from data_dict where `NAME`=cusTypeName and CATEGORY='RES_TYPE' ) and CATEGORY='CUS_ORG');

						ELSE

									SET cusOrg = NULL;

						END IF;

				ELSE

					SET cusType = NULL;

				END IF;

				IF resEntranceName <> ''  AND EXISTS (SELECT ID from data_dict where name=resEntranceName and CATEGORY='RES_ENTRANCE')

					 THEN

						SET resEntrance = (SELECT ID from data_dict where name=resEntranceName and CATEGORY='RES_ENTRANCE');

				ELSE

						SET resEntrance=NULL;

				END IF;

				IF gradeName <> ''  AND EXISTS (SELECT ID from data_dict where name=gradeName and CATEGORY = 'STUDENT_GRADE' )

					 THEN

						SET gradeId = (SELECT ID from data_dict where name=gradeName and CATEGORY = 'STUDENT_GRADE');

				ELSE

						SET gradeName=NULL;

				END IF;


				IF regionName <> ''  AND EXISTS (SELECT id from data_dict where name=regionName )

					 THEN
							SET regionId = (SELECT id from data_dict where name=regionName );
							IF 	schoolLevelName <> '' AND EXISTS (SELECT id from data_dict where name=schoolLevelName and CATEGORY = 'SCHOOL_LEVEL' )
							THEN
							SET schoolLevelId = (SELECT id from data_dict where name=schoolLevelName and CATEGORY = 'SCHOOL_LEVEL' );
									IF schoolName <> ''  AND EXISTS (select id from student_school where name=schoolName and city_id in (SELECT id from region where name=regionName ) and CATEGORY in (SELECT id from data_dict where name=schoolLevelName and CATEGORY='SCHOOL_LEVEL' ) )

									THEN

									SET schoolId = (select id from student_school where name=schoolName and city_id in (SELECT id from region where name=regionName ) and CATEGORY in (SELECT id from data_dict where name=schoolLevelName and CATEGORY='SCHOOL_LEVEL' ) );

								ELSE

									SET schoolName=NULL;
									SET schoolId=NULL;

								END IF;


							ELSE

								SET schoolLevelName = NULL;


								IF schoolName <> ''  AND EXISTS (select id from student_school where name=schoolName and PARENT_ID in (SELECT id from data_dict where name=regionName ))

									THEN

									SET schoolId = (select id from student_school where name=schoolName and PARENT_ID in (SELECT id from data_dict where name=regionName ));

								ELSE

									SET schoolName=NULL;
									SET schoolId=NULL;

								END IF;

							END IF;

				ELSE

						SET regionName=NULL;

				END IF;


				SET contactNum=(SELECT COUNT(1) from customer where CONTACT=contacts);

					IF contactNum>0

						THEN

								SET cusStatus='-1';

								SET failReason='联系方式重复;';

						ELSE

								SET cusStatus='0';

								SET failReason='';

					END IF;



				UPDATE customer_import_transform SET CUS_ORG=cusOrg,CUS_TYPE=cusType,RES_ENTRANCE=resEntrance,GRADE_ID=gradeId,SCHOOL_LEVEL=schoolLevelName,CUS_STATUS=cusStatus,FAIL_REASON=failReason,SCHOOL_ID=schoolId,SCHOOL_NAME=schoolName,SCHOOL_REGION_ID=regionId,SCHOOL_REGION_NAME=regionName,STUDENT_ID=(SELECT CONCAT("STU", (select nextval('student')))),CUS_ID=((SELECT CONCAT("CUS",(LPAD((SELECT nextval('customer') ID),12,0))))) where id=cusId;

			FETCH cur1 into cusId,cusTypeName,cusOrgName,resEntranceName,contacts,schoolName,regionName,gradeName,schoolLevelName;

			END WHILE;

		CLOSE cur1;

END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `proc_insertCustomer_fromImport`;
DELIMITER $$
CREATE  PROCEDURE `proc_insertCustomer_fromImport`(
)
    SQL SECURITY INVOKER
BEGIN
	DECLARE cusName VARCHAR(32);
	DECLARE contacts VARCHAR(32);
	DECLARE cusType VARCHAR(32);
	DECLARE cusOrg VARCHAR(32);
	DECLARE resEntrance VARCHAR(32);
	DECLARE deliverTarget VARCHAR(32);
	DECLARE deliverType VARCHAR(32);
	DECLARE dealStatus VARCHAR(32);
	DECLARE blSchool VARCHAR(32);
	DECLARE createTime VARCHAR(32);
	DECLARE createUser VARCHAR(32);
	DECLARE lastDeliverName VARCHAR(32);
	DECLARE remark VARCHAR(1000);
	DECLARE cusId VARCHAR(32);
	DECLARE customerId VARCHAR(32);
	DECLARE contactNum INT;

	DECLARE studentId VARCHAR(32);
	DECLARE studentName VARCHAR(50);
	DECLARE studentContact VARCHAR(50);
	DECLARE studentSchool VARCHAR(32);
	DECLARE studentGrade VARCHAR(32);
	DECLARE studentClasses VARCHAR(32);
	DECLARE stuFacherName VARCHAR(50);
	DECLARE stuFatherPhone VARCHAR(50);
	DECLARE stuMotherName VARCHAR(50);
	DECLARE stuMotherPhone VARCHAR(50);


	DECLARE cur2 CURSOR FOR select id,cus_name,contact,cus_type,cus_org,res_entrance,deliver_target,deliver_type,deliver_status,bl_school,create_time,create_user,last_deliver_name,remark,cus_id,
																 STUDENT_ID,STUDENT_NAME,STUDENT_CONTACT,SCHOOL_ID,GRADE_ID,CLASSES,FATHER_NAME,FATHE_PHONE,MOTHER_NAME,NOTHER_PHONE from customer_import_transform where cus_status='0';

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = true;

    SET @CURSOR_STOP_FLAG =false;
    OPEN cur2;
		FETCH cur2 into cusId,cusName,contacts,cusType,cusOrg,resEntrance,deliverTarget,deliverType,dealStatus,blSchool,createTime,createUser,lastDeliverName,remark,customerId,
										studentId,studentName,studentContact,studentSchool,studentGrade,studentClasses,stuFacherName,stuFatherPhone,stuMotherName,stuMotherPhone;
			WHILE !@CURSOR_STOP_FLAG do

			SET contactNum=(SELECT COUNT(1) from customer where CONTACT=contacts);
			IF contactNum>0
						THEN
									UPDATE customer_import_transform SET CUS_STATUS='-1',FAIL_REASON='联系方式重复' where id=cusId;
						ELSE
								INSERT INTO customer(
									ID,
									NAME,
									CUS_TYPE,
									CUS_ORG,
									CONTACT,
									RECORD_DATE,BL_SCHOOL,
									DEAL_STATUS,
									CREATE_TIME,
									CREATE_USER_ID,
									MODIFY_TIME,
									MODIFY_USER_ID,
									DELIVER_TYPE,
									DELEVER_TARGET,
									DELIVER_TIME,
									LAST_DELIVER_NAME,
									RECORD_USER_ID,
									BL_CAMPUS_ID,
									TRANSFER_FROM,
									REMARK)
						VALUES (customerId,
									cusName,
									cusType,
									cusOrg,
									contacts,
									createTime,
									blSchool,
									dealStatus,
									NOW(),
									createUser,
									createTime,
									createUser,
									deliverType,
									deliverTarget,
									createTime,
									lastDeliverName,
									createUser,
									blSchool,
									deliverTarget,
									remark);

					INSERT INTO student (
									ID,
									NAME,
									CONTACT,
									SCHOOL,
									FATHER_NAME,
									FATHER_PHONE,
									MOTHER_NAME,
									NOTHER_PHONE,
									CREATE_USER_ID,
									CREATE_TIME,
									GRADE_ID,
									CLASSES,
									BL_GOUP_ID,
									STUDENT_TYPE,
									schoolOrTemp)
					VALUES (studentId,
									studentName,
									studentContact,
									studentSchool,
									stuFacherName,
									stuFatherPhone,
									stuMotherName,
									stuMotherPhone,
									createUser,
									createTime,
									studentGrade,
									studentClasses,
									'000001',
									'POTENTIAL',
									'school');


					UPDATE customer_import_transform SET CUS_STATUS='1' where id=cusId;


				INSERT INTO customer_student_relation (
										CUSTOMER_ID,
										STUDENT_ID,
										CREATE_TIME,
										CREATE_USER_ID,
										CUSTOMER_STUDENT_STATUS)
							VALUES(customerId,
										studentId,
										createTime,
										createUser,
										'NORMAL');

				INSERT INTO customer_dynamic_status (ID,CUSTOMER_ID,
							DYNAMIC_STATUS_TYPE,
							OCCOUR_TIME,
							DESCRIPTION,
							REFERUSER_ID)
							VALUES (
							(select replace(UUID(),'-','')),
							customerId,
							'NEW',
							createTime,
							'新增客户',
							createUser);

			END IF;

			FETCH cur2 into cusId,cusName,contacts,cusType,cusOrg,resEntrance,deliverTarget,deliverType,dealStatus,blSchool,createTime,createUser,lastDeliverName,remark,customerId,studentId,studentName,studentContact,studentSchool,studentGrade,studentClasses,stuFacherName,stuFatherPhone,stuMotherName,stuMotherPhone;
			END WHILE;
		CLOSE cur2;
END $$
DELIMITER ;
*/



--changeset 郭诗博:#885
--comment 系统页面“精英班”名称改为“目标班”

UPDATE resource SET RNAME='目标班资金退回电子账户' WHERE ID='BTN_STU_ACCOUNT_ECS';

UPDATE resource SET RNAME='缩单(目标班)' WHERE ID='narrowBtn';

UPDATE resource SET RNAME='缩单(非目标班)' WHERE ID='NORMAL_NARROW_BTN';

UPDATE resource SET RNAME='目标班学生列表' WHERE ID='promiseStudentList';

UPDATE resource SET RNAME='目标班管理与报名' WHERE RNAME='精英班管理与报名';


UPDATE resource SET RNAME='目标班学生管理（班主任）' WHERE RNAME='精英班学生管理（班主任）';


UPDATE resource SET RNAME='目标班管理' WHERE RNAME='精英班管理';

--changeset 郭诗博:#918
--comment 小班与双师的产品与建班优化
ALTER TABLE `two_teacher_class`
ADD COLUMN `PHASE`  varchar(32) NULL AFTER `CLASS_TIME_LENGTH`;

--changeset 郭诗博:dsfdfeghdfgf33d
--comment
UPDATE refund_workflow set FIRST_BONUS_TYPE_CAMPUS='CAMPUS_CAMPUS' WHERE FIRST_REFUND_DUTY_AMOUNT_CAMPUS IS NOT NULL;

UPDATE refund_workflow set FIRST_BONUS_TYPE_PERSON='USER_USER' WHERE FIRST_REFUND_DUTY_AMOUNT_PERSON IS NOT NULL;