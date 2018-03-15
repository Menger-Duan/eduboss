--liquibase formatted sql

--changeset yaoyuqi:607_1
--comment 添加合同修改
delete from resource where id ='getCurrentBranchTeacher';
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCurrentBranchTeacher', 'ANON_RES', '/TeacherSubjectController/getCurrentBranchTeacher.do');

--changeset yaoyuqi:610_1
--comment 小班班主任修改小班已结算课程学生状态
delete from resource where id ='changeMccStudentAttendance';
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('changeMccStudentAttendance', 'ANON_RES', '/CourseController/changeMccStudentAttendance.do');

--changeset yaoyuqi:638_1
--comment 个人业绩凭证需求
delete from resource where id ='getPersonPaymentFinaceBonus';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getPersonPaymentFinaceBonus', 'ANON_RES', 'OdsMonthPaymentRecieptController/getPersonPaymentFinaceBonus.do');
delete from resource where id ='exportPaymentRecieptReportExcel';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('exportPaymentRecieptReportExcel', 'ANON_RES', 'OdsMonthPaymentRecieptController/exportPaymentRecieptReportExcel.do');
delete from resource where id ='paymentRecieptPerson';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('paymentRecieptPerson', 'MENU', '个人业绩凭证', '0', 'RES0000000092', '101', 'function/reportforms/paymentRecieptPerson.html');

--changeset yaoyuqi:658_1
--comment 学生转校区后
alter table TRANSACTION_CAMPUS_RECORD add STUDY_MANAGER_ID VARCHAR(32);

--changeset yaoyuqi:670_1
--comment 复制新增一个“校区1对1课消（老师）”报表 MAR W4
delete from resource where id ='courseConsumeAnalyzeTeacher';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('courseConsumeAnalyzeTeacher', 'MENU', '校区1对1课消(老师)', '0', 'RES0000000093', '2', 'function/reportforms/courseConsumeAnalyzeTeacher.html');

--changeset yaoyuqi:3161
--comment 灰度发布
delete from resource where id ='saveGrayPublish';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveGrayPublish', 'ANON_RES', 'SystemAction/saveGrayPublish.do');

delete from resource where id ='getGrayPublish';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getGrayPublish', 'ANON_RES', 'SystemAction/getGrayPublish.do');

delete from resource where id ='getGrayPublishProject';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getGrayPublishProject', 'ANON_RES', 'SystemAction/getGrayPublishProject.do');

delete from resource where id ='getMethodByClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getMethodByClass', 'ANON_RES', 'SystemAction/getMethodByClass.do');

delete from resource where id ='grayPublish';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('grayPublish', 'MENU', '灰度发布管理', '0', '7', '101', 'function/system/grayPublish.html');

--changeset yaoyuqi:679-1
--comment 小班课程添加保存课程名称字段
alter table mini_class_course add COURSE_NAME varchar(100);

--changeset yaoyuqi:3162
--comment 灰度发布
delete from resource where id ='findSystemDegradeVoList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findSystemDegradeVoList', 'ANON_RES', 'SystemAction/findSystemDegradeVoList.do');
delete from resource where id ='saveSystemDegrade';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveSystemDegrade', 'ANON_RES', 'SystemAction/saveSystemDegrade.do');
delete from resource where id ='findSystemDegradeById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findSystemDegradeById', 'ANON_RES', 'SystemAction/findSystemDegradeById.do');
drop table if exists system_degrade;
CREATE TABLE system_degrade (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    projects varchar(2000),
    type varchar(20),
    status int,
    create_time timestamp default current_timestamp(),
    MODIFY_TIME timestamp default current_timestamp(),
    create_user_id varchar(32),
    modify_user_id varchar(32),
    remark varchar(300)

);

--changeset yaoyuqi:707-1
--comment 双师
delete from product_category where id ='TWO_TEACHER';
INSERT INTO `Product_Category` (`ID`, `NAME`, `CAT_LEVEL`, `PRODUCT_TYPE`, `CREATE_USER_ID`, `CREATE_TIME`, `MODIFY_USER_ID`, `MODIFY_TIME`) VALUES ('TWO_TEACHER', '双师', '0006', 'TWO_TEACHER', '112233', '2017-03-29 11:20:31', '112233', '2017-03-29 11:20:31');

--changeset yaoyuqi:707-2
--comment 双师
delete from resource where id ='findTwoTeacherClassList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassList', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassList.do');
delete from resource where id ='findTwoTeacheClassrById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacheClassrById', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacheClassrById.do');
delete from resource where id ='saveTwoTeacherClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveTwoTeacherClass', 'ANON_RES', 'TwoTeacherClassController/saveTwoTeacherClass.do');
delete from resource where id ='twoteacherclass';
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('twoteacherclass', 'MENU', '双师管理', '1', 'RES0000000010', '4', '#');
delete from resource where id ='twoteacherclassgroup';
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('twoteacherclassgroup', 'MENU', '双师管理（集团）', '0', 'twoteacherclass', '2', 'function/twoteacher/twoTeacherClass.html');
delete from resource where id ='twoteacherclassbrench';
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('twoteacherclassbrench', 'MENU', '双师管理（分公司）', '0', 'twoteacherclass', '2', 'function/twoteacher/twoTeacherClassTwo.html');



delete from resource where id ='findProductForTwoTeacherChoose';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findProductForTwoTeacherChoose', 'ANON_RES', 'ProductController/findProductForTwoTeacherChoose.do');

delete from resource where id ='findTwoTeacherClassCourseList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassCourseList', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassCourseList.do');
delete from resource where id ='findTwoTeacherClassStudentList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassStudentList', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassStudentList.do');
delete from resource where id ='findTwoTeacherClassTwoList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassTwoList', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassTwoList.do');


delete from resource where id ='twoTeacherClassCoursehtml';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('twoTeacherClassCoursehtml', 'ANON_RES', 'function/twoteacher/twoTeacherClassCourse.html');

delete from resource where id ='twoTeacherClassGeneration';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('twoTeacherClassGeneration', 'ANON_RES', 'function/twoteacher/twoTeacherClassGeneration.html');


delete from resource where id ='twoTeacherClassDetail';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('twoTeacherClassDetail', 'ANON_RES', 'function/twoteacher/twoTeacherClassDetail.html');


delete from resource where id ='saveTwoTeacherClassCourse';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveTwoTeacherClassCourse', 'ANON_RES', 'TwoTeacherClassController/saveTwoTeacherClassCourse.do');


delete from resource where id ='saveTwoTeacherClassTwo';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveTwoTeacherClassTwo', 'ANON_RES', 'TwoTeacherClassController/saveTwoTeacherClassTwo.do');


delete from resource where id ='findTwoTeacherClassTwoById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassTwoById', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassTwoById.do');

   drop table if exists TWO_TEACHER_CLASS;
CREATE TABLE `TWO_TEACHER_CLASS` (
   `CLASS_ID` int auto_increment,
   `PRODUCE_ID` varchar(32) DEFAULT NULL COMMENT '产品',
   `NAME` varchar(32) DEFAULT NULL COMMENT '名称',
   `BL_CAMPUS_ID` varchar(32) DEFAULT NULL COMMENT '所属分公司',
   `SUBJECT` varchar(32) DEFAULT NULL COMMENT '科目',
   `TEACHER_ID` varchar(32) DEFAULT NULL COMMENT '老师',
   `START_DATE` varchar(20) DEFAULT NULL COMMENT '开始日期',
   `CLASS_TIME` varchar(32) DEFAULT NULL COMMENT '上课时间',
   `EVERY_COURSE_CLASS_NUM` decimal(9,2) DEFAULT NULL COMMENT '课时',
   `TOTAL_CLASS_HOURS` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '总课时',
   `STATUS` varchar(32) DEFAULT NULL COMMENT '状态',
   `PEOPLE_QUANTITY` int(11) DEFAULT '1' COMMENT '计划招生人数',
   `REMARK` varchar(512) DEFAULT NULL COMMENT '备注',
   `CREATE_TIME` varchar(20) DEFAULT NULL COMMENT '创建时间',
   `CREATE_USER_ID` varchar(32) DEFAULT NULL COMMENT '创建人',
   `MODIFY_TIME` varchar(20) DEFAULT NULL COMMENT '修改时间',
   `MODIFY_USER_ID` varchar(32) DEFAULT NULL COMMENT '修改人',
   `UNIT_PRICE` decimal(9,2) DEFAULT NULL COMMENT '单价',
   `CLASS_TIME_LENGTH` int(11) DEFAULT NULL COMMENT '课时时长',
   PRIMARY KEY (`CLASS_ID`),
   KEY `IDX_BL_TE_PR` (`BL_CAMPUS_ID`,`TEACHER_ID`,`PRODUCE_ID`),
   KEY `IDX_ST_BL_PR` (`START_DATE`,`BL_CAMPUS_ID`,`PRODUCE_ID`),
   KEY `IDX_PR_BL_CR` (`PRODUCE_ID`,`BL_CAMPUS_ID`,`CREATE_TIME`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='双师主班';

   drop table if exists TWO_TEACHER_CLASS_COURSE;
CREATE TABLE `TWO_TEACHER_CLASS_COURSE` (
   `COURSE_ID` int auto_increment,
   `CLASS_ID` int NOT NULL COMMENT '班级id',
   `COURSE_NAME` varchar(100) DEFAULT NULL COMMENT '名称',
   `COURSE_TIME` varchar(20) DEFAULT NULL COMMENT '上课时间',
   `COURSE_MINUTES` decimal(10,0) DEFAULT NULL COMMENT '课时时长',
   `COURSE_STATUS` varchar(32) DEFAULT NULL COMMENT '课程状态',
   `COURSE_DATE` varchar(10) DEFAULT NULL COMMENT '课程日期',
   `COURSE_HOURS` decimal(9,2) DEFAULT NULL COMMENT '课时',
   `COURSE_END_TIME` varchar(20) DEFAULT NULL COMMENT '结束时间',
   `ATTENDANCE_PIC_NAME` varchar(50) DEFAULT NULL COMMENT '考勤图片',
   `AUDIT_STATUS` varchar(32) DEFAULT NULL COMMENT '课程审批状态',
   `TEACHING_ATTEND_TIME` varchar(20) DEFAULT NULL COMMENT '老师考勤时间',
   `STUDY_MANAGER_CHARGE_TIME` varchar(20) DEFAULT NULL COMMENT '扣费时间',
   `FIRST_ATTENDENT_TIME` varchar(20) DEFAULT NULL COMMENT '第一次考勤时间',
   `CREATE_TIME` varchar(20) DEFAULT NULL COMMENT '创建时间',
   `CREATE_USER_ID` varchar(32) DEFAULT NULL COMMENT '创建人',
   `MODIFY_TIME` varchar(20) DEFAULT NULL COMMENT '修改时间',
   `MODIFY_USER_ID` varchar(32) DEFAULT NULL COMMENT '修改人',
   PRIMARY KEY (`COURSE_ID`),
   KEY `IDX_COURSE_STATUS` (`COURSE_STATUS`),
   KEY `IDX_MINI_CLASS_ID_COURSE_DATE` (`CLASS_ID`,`COURSE_DATE`),
   KEY `IDX_AUDIT_STATUS` (`AUDIT_STATUS`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='双师课程';

   drop table if exists TWO_TEACHER_CLASS_TWO;
drop table if exists TWO_TEACHER_CLASS_TWO;
CREATE TABLE `TWO_TEACHER_CLASS_TWO` (
	`CLASS_TWO_ID` int auto_increment,
   `CLASS_ID` int NOT NULL COMMENT '主班ID',
   `NAME` varchar(32) DEFAULT NULL COMMENT '名称',
   `BL_CAMPUS_ID` varchar(32) DEFAULT NULL COMMENT '所属校区',
   `CLASS_ROOM_ID` varchar(32) DEFAULT NULL COMMENT '教室',
   `TEACHER_ID` varchar(32) DEFAULT NULL COMMENT '老师',
   `PEOPLE_QUANTITY` int(11) DEFAULT '1' COMMENT '计划招生人数',
   `STATUS` varchar(32) DEFAULT NULL COMMENT '状态',
   `REMARK` varchar(512) DEFAULT NULL COMMENT '备注',
   `QQ` varchar(32) DEFAULT NULL COMMENT 'qq群',
   `WEB_CHAT` varchar(32) DEFAULT NULL COMMENT '微信群',
   `CREATE_TIME` varchar(20) DEFAULT NULL COMMENT '创建时间',
   `CREATE_USER_ID` varchar(32) DEFAULT NULL COMMENT '创建人',
   `MODIFY_TIME` varchar(20) DEFAULT NULL COMMENT '修改时间',
   `MODIFY_USER_ID` varchar(32) DEFAULT NULL COMMENT '修改人',
   PRIMARY KEY (`CLASS_TWO_ID`),
   KEY `IDX_BL_TE_PR` (`BL_CAMPUS_ID`,`TEACHER_ID`,`CLASS_ID`),
   KEY `IDX_PR_BL_CR` (`CLASS_ID`,`BL_CAMPUS_ID`,`CREATE_TIME`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='双师辅班';


   drop table if exists TWO_TEACHER_CLASS_STUDENT;
 CREATE TABLE `TWO_TEACHER_CLASS_STUDENT` (
   `ID` int auto_increment,
   `CLASS_TWO_ID` int NOT NULL COMMENT '副班ID',
   `STUDENT_ID` varchar(32) NOT NULL COMMENT '学生',
   `CONTRACT_PRODUCT_ID` varchar(32) DEFAULT NULL COMMENT '合同产品',
   `FIRST_SCHOOL_TIME` varchar(20) DEFAULT NULL COMMENT '第一次上课时间',
   `CREATE_TIME` varchar(20) DEFAULT NULL COMMENT '创建时间',
   `CREATE_USER_ID` varchar(32) DEFAULT NULL COMMENT '创建人',
   `MODIFY_TIME` varchar(20) DEFAULT NULL COMMENT '修改时间',
   `MODIFY_USER_ID` varchar(32) DEFAULT NULL COMMENT '修改人',
   PRIMARY KEY (`ID`),
   KEY `INDEX_FIRST_SCHOOL_TIME` (`FIRST_SCHOOL_TIME`),
   KEY `CREATE_TIME_idx` (`CREATE_TIME`),
   KEY `IDX_CONTRACT_PRODUCT_ID_CREATE_TIME` (`CONTRACT_PRODUCT_ID`,`CREATE_TIME`),
   KEY `IDX_STUDENT_ID_MINI_CLASS_ID` (`STUDENT_ID`,`CLASS_TWO_ID`),
   KEY `IDX_ST_CO_MI` (`STUDENT_ID`,`CONTRACT_PRODUCT_ID`,`CLASS_TWO_ID`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='双师学生';

  drop table if exists TWO_TEACHER_CLASS_STUDENT_ATTENDENT;
 CREATE TABLE `TWO_TEACHER_CLASS_STUDENT_ATTENDENT` (
   `ID` int auto_increment,
   `TWO_CLASS_COURSE_ID` varchar(32) NOT NULL,
   `STUDENT_ID` varchar(32) DEFAULT NULL,
   `COURSE_DATE_TIME` varchar(20) DEFAULT NULL,
   `ATTENDENT_USER_ID` varchar(32) DEFAULT NULL,
   `CHARGE_STATUS` varchar(32) DEFAULT NULL,
   `CREATE_TIME` varchar(20) DEFAULT NULL,
   `CREATE_USER_ID` varchar(32) DEFAULT NULL,
   `MODIFY_TIME` varchar(20) DEFAULT NULL,
   `MODIFY_USER_ID` varchar(32) DEFAULT NULL,
   `ATTENDENT_STATUS` varchar(32) DEFAULT NULL,
   `HAS_TEACHER_ATTENDANCE` varchar(12) DEFAULT NULL,
   `ABSENT_REMARK` varchar(32) DEFAULT NULL,
   `SUPPLEMENT_DATE` varchar(10) DEFAULT NULL,
   `version` int(11) DEFAULT '0',
   PRIMARY KEY (`ID`),
   KEY `idx_student_attendent_STUDENT_ID` (`STUDENT_ID`(16)),
   KEY `IDX_TW_ST_CR` (`TWO_CLASS_COURSE_ID`,`STUDENT_ID`,`CREATE_TIME`),
   KEY `IDX_ATTENDENT_STATUS_CHARGE_STATUS` (`ATTENDENT_STATUS`,`CHARGE_STATUS`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='双师考勤';


 drop table if exists TWO_TEACHER_BRENCH;
 CREATE TABLE TWO_TEACHER_BRENCH(
 ID INT AUTO_INCREMENT PRIMARY KEY,
 CLASS_ID INT NOT NULL,
 BRENCH_ID varchar(32) NOT NULL,
 KEY `IDX_CLASS_ID` (`CLASS_ID`),
 KEY `IDX_BRENCH_ID` (`BRENCH_ID`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='双师分公司关联表';



CREATE
     OR REPLACE ALGORITHM = UNDEFINED
    SQL SECURITY INVOKER
VIEW `product_choose_view` AS
    SELECT
        `p`.`ID` AS `id`,
        `p`.`ID` AS `product_id`,
        NULL AS `mini_class_id`,
        NULL AS `LECTURE_ID`,
        null as two_class_id
    FROM
        `product` `p`
    WHERE
        (`p`.`CATEGORY` <> 'SMALL_CLASS')
    UNION ALL SELECT
        `p`.`ID` AS `id`,
        `p`.`ID` AS `product_id`,
        NULL AS `mini_class_id`,
        NULL AS `LECTURE_ID`,
        null as two_class_id
    FROM
        `product` `p`
    WHERE
        ((`p`.`CATEGORY` = 'SMALL_CLASS')
            AND (`p`.`CLASS_TYPE_ID` = 'DAT0000000240'))
    UNION ALL SELECT
        IFNULL(`mc`.`MINI_CLASS_ID`, `p`.`ID`) AS `id`,
        `p`.`ID` AS `product_id`,
        `mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
        NULL AS `LECTURE_ID`,
        null as two_class_id
    FROM
        (`mini_class` `mc`
        LEFT JOIN `product` `p` ON ((`mc`.`PRODUCE_ID` = `p`.`ID`)))
    WHERE
        (`mc`.`STATUS` <> 'CONPELETE')
    UNION ALL SELECT
        IFNULL(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
        `p`.`ID` AS `product_id`,
        NULL AS `MINI_CLASS_ID`,
        `lc`.`LECTURE_ID` AS `LECTURE_ID`,
        null as two_class_id
    FROM
        (`lecture_class` `lc`
        LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`)))

	union all select
		tctt.CLASS_TWO_ID,
        tctc.PRODUCE_ID as product_id,
        null as `MINI_CLASS_ID`,
        NULL AS `LECTURE_ID`,
        tctt.CLASS_TWO_ID as two_class_id
        from two_teacher_class_two tctt
        left join two_teacher_class tctc on tctt.class_id=tctc.class_id;


 --changeset yaoyuqi:707-3
--comment 双师
 delete from data_dict where id ='TWO_TEACHER';
 INSERT INTO `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `CATEGORY`, `STATE`, `IS_SYSTEM`) VALUES ('TWO_TEACHER', '双师', 'TWO_TEACHER', '7', 'PRODUCT_TYPE', '0', '1');

--changeset yaoyuqi:707-4
--comment 双师
delete from resource where id ='deleteClassCourse';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('deleteClassCourse', 'ANON_RES', 'TwoTeacherClassController/deleteClassCourse.do');

--changeset yaoyuqi:707-5
--comment 双师
delete from resource where id ='findTwoTeacherClassCourseById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassCourseById', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassCourseById.do');
delete from resource where id ='saveTwoTeacherClassCourse';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveTwoTeacherClassCourse', 'ANON_RES', 'TwoTeacherClassController/saveTwoTeacherClassCourse.do');
delete from resource where id ='twoClassAttendentDetail';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('twoClassAttendentDetail', 'ANON_RES', 'function/twoteacher/twoClassAttendentDetail.html');
delete from resource where id ='findTwoTeacherClassAttendentList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoTeacherClassAttendentList', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherClassAttendentList.do');

--changeset yaoyuqi:707-6
--comment 双师
delete from resource where id ='saveMultTwoTeacherClassCourse';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveMultTwoTeacherClassCourse', 'ANON_RES', 'TwoTeacherClassController/saveMultTwoTeacherClassCourse.do');

--changeset yaoyuqi:756
--comment 报表禁用
delete from role_resource where resourceId='realTime.call';
delete from role_resource where resourceId='realTime.analysis';
delete from resource where id='realTime.call';
delete from resource where id='realTime.analysis';

--changeset yaoyuqi:846-1
--comment 精英班产品添加字段支撑新类型精英班产品
delete from  data_dict where id ='fixedhours';
delete from  data_dict where id ='fixedmoney';
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `PARENT_ID`, `CATEGORY`, `CREATE_TIME`, `CREATE_USER_ID`, `STATE`, `IS_SYSTEM`) VALUES ('fixedhours', '固定课时', 'fixedhours', '15', 'ECS_CLASS', 'COURSE_SERIES', '2017-04-19 14:24:34', '112233', '0', '1');
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `PARENT_ID`, `CATEGORY`, `CREATE_TIME`, `CREATE_USER_ID`, `STATE`, `IS_SYSTEM`) VALUES ('fixedmoney', '固定总价', 'fixedmoney', '16', 'ECS_CLASS', 'COURSE_SERIES', '2017-04-19 14:24:34', '112233', '0', '1');
update data_dict set parent_id='SMALL_CLASS' where category='COURSE_SERIES' and PARENT_ID is null;


--changeset yaoyuqi:846-2 endDelimiter:\$\$
--comment 精英班产品添加字段支撑新类型精英班产品
DROP procedure IF EXISTS `proc_autoTotal_ECS_CLASS`;

$$

CREATE PROCEDURE `proc_autoTotal_ECS_CLASS`(
IN nyear int(11),IN nMonth int(11),IN yearMonth varchar(10),IN lastDate varchar(10))
    SQL SECURITY INVOKER
begin

	declare productYear int;

    if nMonth<8
    then set productYear=nyear;
    else set productYear=nyear+1;
    end if;

    INSERT INTO `promise_class_record`
            (`ID`,`STUDENT_ID`,`CLASS_STATUS`,`CLASS_YEAR`,`CLASS_MONTH`,`CHARGE_HOURS`,`CHARGE_AMOUNT`,`CREATE_TIME`,`CLASS_DATE`,`contract_product_id`)
        select replace(uuid(),'-',''),mcs.STUDENT_ID,'0',nyear,nMonth,sum(mcc.COURSE_HOURS) COURSE_HOURS ,
            func_getEcsContractChargeMoney(cp.id,nyear,nMonth) as chargeMoney,
            now(),lastDate as date,concat(cp.id,'') contractId
        from
            mini_class mc
            left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
            left join contract_product cp on mcs.CONTRACT_PRODUCT_ID=cp.id
            left join product p on p.id=cp.PRODUCT_ID
            left join contract c on c.id=cp.CONTRACT_ID
            left join data_dict dd on dd.id=p.PRODUCT_VERSION_ID
            left join mini_class_course mcc on mcc.MINI_CLASS_ID=mc.MINI_CLASS_ID
            left join mini_class_student_attendent  mcsa on mcsa.MINI_CLASS_COURSE_ID=mcc.MINI_CLASS_COURSE_ID  and mcsa.STUDENT_ID=c.STUDENT_ID
             where cp.type='ECS_CLASS' and (cp.PAID_STATUS='PAID' or cp.PAID_STATUS='PAYING')
             and mcsa.CHARGE_STATUS='CHARGED'  and (cp.STATUS='NORMAL' or cp.STATUS='STARTED')
             and mcc.COURSE_DATE like concat(yearMonth,'%')  and p.COURSE_SERIES_ID='fixedmoney'
            group by mcs.STUDENT_ID,cp.ID;


        INSERT INTO `promise_class_detail_record`
            (`ID`,
            `PROMISE_CLASS_RECORD_ID`,
            `CLASS_TYPE`,
            `CLASS_SUBJECT`,
            `COURSE_HOURS`,
            `TEACHER`,
            `CREATE_TIME`)
            select replace(uuid(),'-','') as id,
            (select id from promise_class_record pp where pp.contract_product_id =mcs.contract_product_id
            and pp.id not in(select PROMISE_CLASS_RECORD_ID from promise_class_detail_record) limit 1) as promise_record_id,
            '0',
            mcc.SUBJECT subjectId,sum(mcc.COURSE_HOURS) COURSE_HOURS,mcc.TEACHER_ID as teacherid,now() as createTime

            from
                        mini_class mc  left join mini_class_student mcs on mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID
                        left join contract_product cp on mcs.CONTRACT_PRODUCT_ID=cp.id
                         left join product p on p.id=cp.PRODUCT_ID
                        left join contract c on c.id=cp.CONTRACT_ID
                        left join data_dict dd on dd.id=p.PRODUCT_VERSION_ID
                        left join mini_class_course mcc on mcc.MINI_CLASS_ID=mc.MINI_CLASS_ID
                         left join mini_class_student_attendent  mcsa on mcsa.MINI_CLASS_COURSE_ID=mcc.MINI_CLASS_COURSE_ID  and mcsa.STUDENT_ID=c.STUDENT_ID
                         where cp.type='ECS_CLASS' and (cp.PAID_STATUS='PAID' or (cp.PAID_STATUS='PAYING' AND cp.PAID_AMOUNT-cp.CONSUME_AMOUNT>0))
                         and mcsa.CHARGE_STATUS='CHARGED'  and (cp.STATUS='NORMAL' or cp.STATUS='STARTED')
                         and mcc.COURSE_DATE like concat(yearMonth,'%')  and p.COURSE_SERIES_ID='fixedmoney'
            group by mcs.CONTRACT_PRODUCT_ID,mcc.teacher_id,mcc.SUBJECT;


    INSERT INTO `promise_class_record`
     (`ID`,`STUDENT_ID`,`CLASS_STATUS`,`CLASS_YEAR`,`CLASS_MONTH`,`CHARGE_HOURS`,`CHARGE_AMOUNT`,`CREATE_TIME`,`CLASS_DATE`,`contract_product_id`)
      select replace(uuid(),'-',''),c.STUDENT_ID,'0',nyear,nMonth,0,
            func_getEcsContractChargeMoney(cp.id,nyear,nMonth) as chargeMoney,
            now(),lastDate as date,concat(cp.id,'') contractId
            from contract_product cp
            left join product p on p.id=cp.PRODUCT_ID
            left join data_dict dd on dd.ID=p.PRODUCT_VERSION_ID
            left join contract c on c.id =cp.CONTRACT_ID
            where  cp.TYPE='ECS_CLASS' and (cp.CONSUME_AMOUNT>0 or cp.CONSUME_QUANTITY>0) and (cp.STATUS='NORMAL' or cp.STATUS='STARTED')
            and  dd.name=concat(productYear,'')
            and cp.CREATE_TIME <=lastDate  and p.COURSE_SERIES_ID='fixedmoney'
            AND cp.id not in(select pc.contract_product_id from promise_class_record pc where pc.CLASS_YEAR=concat(nyear,'') and pc.CLASS_MONTH=concat(nMonth,'')  and pc.CONTRACT_PRODUCT_ID is not null);
end
$$


--changeset yaoyuqi:846-3 endDelimiter:\$\$
--comment 精英班产品添加字段支撑新类型精英班产品3
DROP function IF EXISTS `func_getEcsContractChargeMoney`;

$$

CREATE  FUNCTION `func_getEcsContractChargeMoney`(
conProId varchar(32),nyear int(11),nmonth int(11)) RETURNS double
    SQL SECURITY INVOKER
BEGIN
select CAST(case when dd.NAME=nyear and nmonth<7 then (cp.plan_amount*(1- p.PROMISE_CLASS_DISCOUNT)-cp.CONSUME_AMOUNT)/(8-nmonth)
            when dd.NAME=nyear+1 and nmonth>9 then (cp.plan_amount*(1- p.PROMISE_CLASS_DISCOUNT)-cp.CONSUME_AMOUNT)/(20-nmonth)
            when dd.NAME=nyear and nmonth=7  then (cp.plan_amount*(1- p.PROMISE_CLASS_DISCOUNT)-cp.CONSUME_AMOUNT)
            else 0 end AS decimal(10,2)) into @sumCourseHour from
    contract_product cp
    left join product p on p.id=cp.PRODUCT_ID
    left join data_dict dd on dd.id=p.PRODUCT_VERSION_ID
where cp.id=conProId;
RETURN @sumCourseHour;
END
$$

--changeset yaoyuqi:707-7
--comment 双师
ALTER TABLE contract_record ADD TWO_TEACHER_AMOUNT DECIMAL(10,2) default 0.00;

--changeset yaoyuqi:707-8
--comment 双师
CREATE
     OR REPLACE ALGORITHM = UNDEFINED
    SQL SECURITY INVOKER
VIEW `product_choose_view` AS
    SELECT
        `p`.`ID` AS `id`,
        `p`.`ID` AS `product_id`,
        NULL AS `mini_class_id`,
        NULL AS `LECTURE_ID`,
        null as two_class_id
    FROM
        `product` `p`
    WHERE
        (`p`.`CATEGORY` <> 'SMALL_CLASS' and `p`.`CATEGORY` <> 'TWO_TEACHER' )
    UNION ALL SELECT
        `p`.`ID` AS `id`,
        `p`.`ID` AS `product_id`,
        NULL AS `mini_class_id`,
        NULL AS `LECTURE_ID`,
        null as two_class_id
    FROM
        `product` `p`
    WHERE
        ((`p`.`CATEGORY` = 'SMALL_CLASS')
            AND (`p`.`CLASS_TYPE_ID` = 'DAT0000000240'))
    UNION ALL SELECT
        IFNULL(`mc`.`MINI_CLASS_ID`, `p`.`ID`) AS `id`,
        `p`.`ID` AS `product_id`,
        `mc`.`MINI_CLASS_ID` AS `MINI_CLASS_ID`,
        NULL AS `LECTURE_ID`,
        null as two_class_id
    FROM
        (`mini_class` `mc`
        LEFT JOIN `product` `p` ON ((`mc`.`PRODUCE_ID` = `p`.`ID`)))
    WHERE
        (`mc`.`STATUS` <> 'CONPELETE')
    UNION ALL SELECT
        IFNULL(`lc`.`LECTURE_ID`, `p`.`ID`) AS `id`,
        `p`.`ID` AS `product_id`,
        NULL AS `MINI_CLASS_ID`,
        `lc`.`LECTURE_ID` AS `LECTURE_ID`,
        null as two_class_id
    FROM
        (`lecture_class` `lc`
        LEFT JOIN `product` `p` ON ((`lc`.`PRODUCT` = `p`.`ID`)))

	union all select
		tctt.CLASS_TWO_ID,
        tctc.PRODUCE_ID as product_id,
        null as `MINI_CLASS_ID`,
        NULL AS `LECTURE_ID`,
        tctt.CLASS_TWO_ID as two_class_id
        from two_teacher_class_two tctt
        left join two_teacher_class tctc on tctt.class_id=tctc.class_id;

--changeset yaoyuqi:707-9
--comment 双师
    delete from resource where id ='getTwoClassStuRemain';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoClassStuRemain', 'ANON_RES', 'TwoTeacherClassController/getTwoClassStuRemain.do');
    delete from resource where id ='checkAllowAddStudent4Class';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('checkAllowAddStudent4Class', 'ANON_RES', 'TwoTeacherClassController/checkAllowAddStudent4Class.do');
    delete from resource where id ='changeStudentTwoClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('changeStudentTwoClass', 'ANON_RES', 'TwoTeacherClassController/changeStudentTwoClass.do');
    delete from resource where id ='findTwoClassForChangeClassSelect';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoClassForChangeClassSelect', 'ANON_RES', 'TwoTeacherClassController/findTwoClassForChangeClassSelect.do');

--changeset yaoyuqi:707-10
--comment 双师
delete from resource where id ='getClassCourseTimeInfo';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getClassCourseTimeInfo', 'ANON_RES', 'TwoTeacherClassController/getClassCourseTimeInfo.do');

--changeset yaoyuqi:875-1
--comment 精英班
UPDATE `data_dict` SET `NAME`='高考目标班' WHERE `ID`='DAT0000000371';
UPDATE `data_dict` SET `NAME`='中考目标班' WHERE `ID`='DAT0000000370';
UPDATE `data_dict` SET `NAME`='小升初目标班' WHERE `ID`='DAT0000000368';
update data_dict set name ='目标班VIP服务费' where id ='DAT0000000432';

--changeset yaoyuqi:693-2
--comment 易宝支付
drop table if exists yee_pay_info;
CREATE TABLE `yee_pay_info` (
   `ID` varchar(32) NOT NULL,
   `PAY_CODE` varchar(32) DEFAULT NULL  COMMENT '支付参考号',
   `MONEY` decimal(10,2) DEFAULT NULL  COMMENT '支付金额',
   `POS_CODE` varchar(32) DEFAULT NULL  COMMENT 'POS机号',
   `RESULT` varchar(32) DEFAULT NULL  COMMENT '结果',
   `STATUS` varchar(32) DEFAULT NULL  COMMENT '状态',
   `FAILD_REASON` varchar(100) DEFAULT NULL  COMMENT '失败原因',
   `FUND_CHARGE_ID` varchar(32) DEFAULT NULL  COMMENT '收款ID',
   `CONTRACT_ID` varchar(32) DEFAULT NULL  COMMENT '合同ID',
   `BUS_CODE` varchar(50) DEFAULT NULL  COMMENT '商户',
   `TRANSACTION_TIME` timestamp  COMMENT '支付时间',
   `CARD_NO` varchar(50) DEFAULT NULL  COMMENT '手续费',
   `REAL_MONEY` decimal(10,2) DEFAULT NULL  COMMENT '手续费',
	`counter_fee` decimal(10,2) COMMENT '手续费',
  `type` varchar(10) comment '保留字段，后续用来扩展支付宝微信',
	`remark` varchar(500),
   `CREATE_TIME` timestamp,
   `CREATE_USER` varchar(32) DEFAULT NULL,
	`MODIFY_TIME` timestamp,
	`modify_user` varchar(32),
   PRIMARY KEY (`ID`),
   KEY `IDX_FUND_CHARGE_ID_CREATE_TIME` (`FUND_CHARGE_ID`,`CREATE_TIME`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='易宝POS机待支付流水表';


--changeset yaoyuqi:876-1
--comment twoteacher
delete from data_dict where id ='increaseClass';
delete from data_dict where id ='bestClass';
delete from data_dict where id ='fourth';
delete from data_dict where id ='TWO_TEACHER_CLASS_FOR_OTHER';
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `PARENT_ID`, `CATEGORY`, `CREATE_TIME`, `CREATE_USER_ID`, `STATE`) VALUES ('increaseClass', '提高班', 'increaseClass', '1', 'TWO_TEACHER', 'CLASS_TYPE', '2017-04-12 14:45:00', 'USE0000018100', '0');
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `PARENT_ID`, `CATEGORY`, `CREATE_TIME`, `CREATE_USER_ID`, `STATE`) VALUES ('bestClass', '尖子班', 'bestClass', '1', 'TWO_TEACHER', 'CLASS_TYPE', '2017-04-12 14:45:00', 'USE0000018100', '0');
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `CATEGORY`, `CREATE_TIME`, `CREATE_USER_ID`, `STATE`) VALUES ('fourth', '第四期', 'fourth', '1', 'SMALL_CLASS_PHASE', '2016-11-19 09:15:05', 'USE0000011031', '0');
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `CATEGORY`, `STATE`, `IS_SYSTEM`) VALUES ('TWO_TEACHER_CLASS_FOR_OTHER', '双师', 'TWO_TEACHER_CLASS_FOR_OTHER', '2', 'OTHER_OF_CATEGORY', '0', '1');

--changeset yaoyuqi:878-1
--comment twoteacher
delete from resource where id ='turnClass';
delete from resource where id ='deleteClass';
delete from resource where id ='addClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('turnClass', 'BUTTON', '转班', 'twoteacherclassbrench', 'turnClass');
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('deleteClass', 'BUTTON', '删除', 'twoteacherclassbrench', 'deleteClass');
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('addClass', 'BUTTON', '添加', 'twoteacherclassbrench', 'addClass');


--changeset yaoyuqi:878-2
--comment twoteacher
delete from resource where id ='modifyClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('modifyClass', 'BUTTON', '修改', 'twoteacherclassbrench', 'modifyClass');

--changeset yaoyuqi:878-3
--comment twoteacher
delete from resource where id ='findTwoByContractProductId';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findTwoByContractProductId', 'ANON_RES', 'TwoTeacherClassController/findTwoTeacherTwoByContractProductId.do');

--changeset yaoyuqi:878-4
--comment twoteacher
delete from resource where id ='findClassWillBuyHour';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findClassWillBuyHour', 'ANON_RES', 'TwoTeacherClassController/findClassWillBuyHour.do');


--changeset yaoyuqi:693-110
--comment boss系统对接易宝支付渠道功能
alter table pos_machine_manage add POS_TYPE varchar(32);
alter table pos_machine add POS_TYPE varchar(32);

--changeset yaoyuqi:902
--comment 小班，双师课程埋点
alter table mini_class_course add COURSE_NUM INT default 0 COMMENT '课程在班级中的序号';
alter table two_teacher_class_course add COURSE_NUM INT default 0 COMMENT '课程在班级中的序号';

--changeset yaoyuqi:live
--comment 直播产品
alter table product
add live_Id varchar(32) comment '直播产品ID',
add total_amount decimal(10,2) comment '总额',
add start_time varchar(5) comment '开始时间',
add promotion_amount decimal(10,2) comment '优惠金额';

delete FROM product_category where id ='LIVE';
INSERT INTO `PRODUCT_CATEGORY` (`ID`, `NAME`, `CAT_LEVEL`, `PRODUCT_TYPE`, `CREATE_USER_ID`, `CREATE_TIME`, `MODIFY_USER_ID`, `MODIFY_TIME`) VALUES ('LIVE', '直播', '0007', 'LIVE', '112233', '2017-05-15 11:20:31', '112233', '2017-05-15 11:20:31');

alter table product
add teacher_id varchar(32) comment '老师';

create table PRODUCT_BRANCH (id int auto_increment primary key ,PRODUCT_ID VARCHAR(32) COMMENT '产品ID',BRANCH_ID VARCHAR(32) COMMENT '分公司ID');

delete from resource where id ='saveLiveProduct';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveLiveProduct', 'ANON_RES', 'ProductController/saveLiveProduct.do');


delete from resource where id ='liveDetailModal';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('liveDetailModal', 'ANON_RES', 'function/contract/liveDetailModal.html');


--changeset yaoyuqi:live-1
--comment 直播产品
INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`) VALUES ('ROL0000000030_1', '课程产品管理', '只能看见本人所属分公司的数据', 'sqlRestriction_organization.id=exists (select 1  from product_branch  where product_id={alias}.id and branch_id=\'${blBrench}\')', 'or', 'criteria', 'TRUE');
UPDATE `role_ql_config` SET `JOINER`='or' WHERE `ID`='ROL0000000030';

delete from resource where id ='saveContractAndLive';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveContractAndLive', 'ANON_RES', 'ContractAction/saveContractAndLive.do');


--changeset yaoyuqi:946
--comment 合同列表打印预览添加“打印简要合同”功能
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('simpleContractPrint', 'ANON_RES', 'function/contract/contractSimplePrintModal.html');
INSERT INTO `RESOURCE` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RTAG`) VALUES ('PRINT_SIMPLE_CONTRACT', 'BUTTON', '打印简要合同', '0', '58', 'PRINT_SIMPLE_CONTRACT');

--changeset yaoyuqi:live-2
--comment 直播产品
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='402881e647ce90330147ce94ef900002';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='402881e647ce90330147ce94ef900003';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='402881e647ce90330147ce94ef900004';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='402881e647ce90330147ce94ef900005';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='402881e647ce90330147ce94ef900006';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='402881e647ce90330147ce94ef900007';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000265' WHERE `ID`='402881e647ce90330147ce94ef900008';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000265' WHERE `ID`='402881e647ce90330147ce94ef900009';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000265' WHERE `ID`='402881e647ce90330147ce94ef900010';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000266' WHERE `ID`='402881e647ce90330147ce94ef900011';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000266' WHERE `ID`='402881e647ce90330147ce94ef900012';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000266' WHERE `ID`='402881e647ce90330147ce94ef900013';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000264' WHERE `ID`='DAT0000000393';
UPDATE `data_dict` SET `PARENT_ID`='DAT0000000266' WHERE `ID`='DAT0000000409';


--changeset yaoyuqi:live-3
--comment 直播产品
INSERT INTO `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `CATEGORY`, `STATE`, `IS_SYSTEM`) VALUES ('LIVE', '直播', 'LIVE', '8', 'PRODUCT_TYPE', '0', '1');

--changeset yaoyuqi:live-4
--comment 直播产品
delete from data_dict where id ='freeClass';
delete from data_dict where id ='longClass';
delete from data_dict where id ='shortClass';
INSERT INTO `data_dict` (`ID`,`NAME`,`VALUE`,`DICT_ORDER`,`PARENT_ID`,`CATEGORY`,`ICON`,`DATA`,`REMARK`,`CREATE_TIME`,`CREATE_USER_ID`,`STATE`,`IS_SYSTEM`) VALUES ('freeClass','免费公开课','freeClass',2,'LIVE','CLASS_TYPE',NULL,NULL,NULL,'2017-04-12 14:45:00','USE0000018100','0','0');
INSERT INTO `data_dict` (`ID`,`NAME`,`VALUE`,`DICT_ORDER`,`PARENT_ID`,`CATEGORY`,`ICON`,`DATA`,`REMARK`,`CREATE_TIME`,`CREATE_USER_ID`,`STATE`,`IS_SYSTEM`) VALUES ('longClass','长期课','longClass',3,'LIVE','CLASS_TYPE',NULL,NULL,NULL,'2017-04-12 14:45:00','USE0000018100','0','0');
INSERT INTO `data_dict` (`ID`,`NAME`,`VALUE`,`DICT_ORDER`,`PARENT_ID`,`CATEGORY`,`ICON`,`DATA`,`REMARK`,`CREATE_TIME`,`CREATE_USER_ID`,`STATE`,`IS_SYSTEM`) VALUES ('shortClass','短期课','shortClass',1,'LIVE','CLASS_TYPE',NULL,NULL,NULL,'2017-04-12 14:45:00','USE0000018100','0','0');


--changeset yaoyuqi:959
--comment boss首页和报表中心优化，现金流区分线下和线上
alter table staff_bonus_day add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';
alter table staff_bonus_month add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';
alter table staff_bonus_week add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';

--changeset yaoyuqi:finance_amount
--comment 线上线下现金流
alter table finance_brench add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';
alter table finance_campus add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';
alter table finance_user add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';

--changeset yaoyuqi:959-1
--comment boss首页和报表中心优化，现金流区分线下和线上
update finance_brench set LINE_AMOUNT=count_paid_total_amount-ONLINE_AMOUNT;
update finance_campus set LINE_AMOUNT=count_paid_total_amount-ONLINE_AMOUNT;
update finance_user set LINE_AMOUNT=count_paid_total_amount-ONLINE_AMOUNT;
update staff_bonus_day set LINE_AMOUNT=AMOUNT-ONLINE_AMOUNT;
update staff_bonus_month set LINE_AMOUNT=AMOUNT-ONLINE_AMOUNT;
update staff_bonus_week set LINE_AMOUNT=AMOUNT-ONLINE_AMOUNT;


--changeset yaoyuqi:949 endDelimiter:\$\$
--comment 学生科目状态统计只需显示购买了一对一产品的学生
DROP procedure IF EXISTS `proc_ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS`;

$$

CREATE PROCEDURE `proc_ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS`(
IN countDate varchar(20))
    SQL SECURITY INVOKER
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
			left join user u on u.user_id = s.STUDY_MANEGER_ID
            where s.ONEONONE_STATUS is not null;


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

 		UPDATE ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS a SET
 	a.`CHINESE_SUBJECT_HOURS` =SPLIT_STR(a.CHINESE_SUBJECT,'/', 1),
	a.`MATH_SUBJECT_HOURS` =SPLIT_STR(a.MATH_SUBJECT,'/', 1),
 	a.`ENGLISH_SUBJECT_HOURS` = SPLIT_STR(a.ENGLISH_SUBJECT,'/', 1),
 	a.`PHYSICS_SUBJECT_HOURS` =SPLIT_STR(a.PHYSICS_SUBJECT,'/', 1),
 	a.`CHEMISTRY_SUBJECT_HOURS` = SPLIT_STR(a.CHEMISTRY_SUBJECT,'/', 1),
 	a.`BIOLOGY_SUBJECT_HOURS` = SPLIT_STR(a.BIOLOGY_SUBJECT,'/', 1),
 	a.`GEOGRAPHY_SUBJECT_HOURS` = SPLIT_STR(a.GEOGRAPHY_SUBJECT,'/', 1),
	a.`POLITICS_SUBJECT_HOURS` = SPLIT_STR(a.POLITICS_SUBJECT,'/', 1),
 	a.`HISTORY_SUBJECT_HOURS` = SPLIT_STR(a.HISTORY_SUBJECT,'/', 1),
 	a.`GROWING_HOURS` = SPLIT_STR(a.GROWING,'/', 1),
	a.`INFO_TECHNOLOGY_HOURS` = SPLIT_STR(a.INFO_TECHNOLOGY,'/', 1),
	a.`OTHER_ENGLISH_HOURS` = SPLIT_STR(a.OTHER_ENGLISH,'/', 1),
	a.`OTHER_MATH_HOURS` = SPLIT_STR(a.OTHER_MATH,'/', 1),
	a.`OTHER_HOURS` = SPLIT_STR(a.OTHER,'/', 1),
	a.`KNOWLEDGE_HOURS` = SPLIT_STR(a.KNOWLEDGE,'/', 1),
 	a.`PSYCHOLOGY_HOURS` = SPLIT_STR(a.PSYCHOLOGY,'/', 1),
 	a.`JANPANESE_HOURS` = SPLIT_STR(a.JANPANESE,'/', 1),
 	a.`OTHER_CHINESE_HOURS` = SPLIT_STR(a.OTHER_CHINESE,'/', 1)
 	where COUNT_DATE=countDate;

 	 UPDATE ODS_DAY_ONE_ON_ONE_SUBJECT_STATUS a SET
 	a.`CHINESE_SUBJECT` = SPLIT_STR(a.CHINESE_SUBJECT,'/', 2),
 	a.`MATH_SUBJECT` =SPLIT_STR(a.MATH_SUBJECT,'/', 2),
 	a.`ENGLISH_SUBJECT` = SPLIT_STR(a.ENGLISH_SUBJECT,'/', 2),
 	a.`PHYSICS_SUBJECT` =SPLIT_STR(a.PHYSICS_SUBJECT,'/', 2),
 	a.`CHEMISTRY_SUBJECT` = SPLIT_STR(a.CHEMISTRY_SUBJECT,'/', 2),
 	a.`BIOLOGY_SUBJECT` = SPLIT_STR(a.BIOLOGY_SUBJECT,'/', 2),
 	a.`GEOGRAPHY_SUBJECT` = SPLIT_STR(a.GEOGRAPHY_SUBJECT,'/', 2),
 	a.`POLITICS_SUBJECT` = SPLIT_STR(a.POLITICS_SUBJECT,'/', 2),
 	a.`HISTORY_SUBJECT` = SPLIT_STR(a.HISTORY_SUBJECT,'/', 2),
 	a.`GROWING` = SPLIT_STR(a.GROWING,'/', 2),
 	a.`INFO_TECHNOLOGY` = SPLIT_STR(a.INFO_TECHNOLOGY,'/', 2),
 	a.`OTHER_ENGLISH` = SPLIT_STR(a.OTHER_ENGLISH,'/', 2),
 	a.`OTHER_MATH` = SPLIT_STR(a.OTHER_MATH,'/', 2),
 	a.`OTHER` = SPLIT_STR(a.OTHER,'/', 2),
 	a.`KNOWLEDGE` = SPLIT_STR(a.KNOWLEDGE,'/', 2),
 	a.`PSYCHOLOGY` = SPLIT_STR(a.PSYCHOLOGY,'/', 2),
 	a.`JANPANESE` = SPLIT_STR(a.JANPANESE,'/', 2),
 	a.`OTHER_CHINESE` = SPLIT_STR(a.OTHER_CHINESE,'/', 2)
 	where COUNT_DATE=countDate;

END
$$

--changeset yaoyuqi:972-1
--comment 双师管理（集团）数据查询权限优化
delete from `role_ql_config` where id ='twoTeacherGroupList';
INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`) VALUES ('twoTeacherGroupList', '双师集团', '能看见归属的组织架构数据', 'b.brenchId.id in (${belongOrganization})', 'or', 'hql', 'TRUE');

--changeset yaoyuqi:983-1
--comment 目标班优化
CREATE TABLE promise_class_subject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mini_class_id VARCHAR(32) comment '小班ID',
    subject_id VARCHAR(32) comment '科目',
    course_hours DECIMAL(10 , 2 ) comment '课时数',
    QUARTER_ID varchar(32) comment '季度',
    promise_student_id varchar(32) comment '精英班',
    create_time DATETIME,
    create_user_id VARCHAR(32),
    modify_time DATETIME,
    modify_user_id VARCHAR(32)
);

delete from resource where id ='savePromiseSubject';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('savePromiseSubject', 'ANON_RES', 'PromiseClassController/savePromiseSubject.do');
delete from resource where id ='deleteStudentFromClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('deleteStudentFromClass', 'ANON_RES', 'PromiseClassController/deleteStudentFromClass.do');
delete from resource where id ='findMiniClassTeacherForSelect';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findMiniClassTeacherForSelect', 'ANON_RES', 'PromiseClassController/findMiniClassTeacherForSelect.do');
delete from resource where id ='findMiniClassByPromiseSubjectId';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findMiniClassByPromiseSubjectId', 'ANON_RES', 'PromiseClassController/findMiniClassByPromiseSubjectId.do');
delete from resource where id ='getPromiseSubjectList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getPromiseSubjectList', 'ANON_RES', 'PromiseClassController/getPromiseSubjectList.do');
delete from resource where id ='savePromiseToMiniClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('savePromiseToMiniClass', 'ANON_RES', 'PromiseClassController/savePromiseToMiniClass.do');

--changeset yaoyuqi:983-2
--comment 目标班优化
delete from resource where id ='findPromiseStudentInfoById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findPromiseStudentInfoById', 'ANON_RES', 'PromiseClassController/findStudentInfoByPromiseStudentId.do');

--changeset yaoyuqi:1048-2
--comment 小班新增销售渠道及优化
alter table mini_class add ONLINE_SALE int default 1 comment '线上销售',add CAMPUS_SALE int default 0 comment '校区销售',add CAMPUS_CONTACT varchar(32) comment '校区联系电话',add on_shelves int default 1 comment '是否上架';
update mini_class set CAMPUS_SALE=0;

drop table if exists mini_class_change_sale_type_log;
create table mini_class_change_sale_type_log(id int auto_increment primary key,MINI_CLASS_ID varchar(32),old_Type varchar(50) comment '旧类型',new_type varchar(50) comment '新类型',remark varchar(500) comment '备注',CREATE_USER_ID varchar(32),CREATE_TIME timestamp);

delete from resource where id ='changeMiniClassSaleType';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('changeMiniClassSaleType', 'ANON_RES', 'CourseController/changeMiniClassSaleType.do');


--changeset yaoyuqi:1064
--comment 目标班目标结果设定增加审批环节
drop table if exists promise_class_end_audit;
CREATE TABLE promise_class_end_audit (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promise_class_student_id VARCHAR(32) comment '目标班学生ID',
    AUDIT_STATUS INT DEFAULT 0 Comment '审批状态0开始审批，1审批完成',
    CREATE_USER_ID VARCHAR(32),
    CREATE_TIME VARCHAR(20),
    MODIFY_USER_ID VARCHAR(32),
    MODIFY_TIME VARCHAR(20),
    remark VARCHAR(500),
	KEY `idx_student_id` (`promise_class_student_id`),
    key `idx_status` (`AUDIT_STATUS`)
);

delete from resource where id ='startAuditPromiseStudent';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('startAuditPromiseStudent', 'ANON_RES', 'PromiseClassController/startAuditPromiseStudent.do');
delete from resource where id ='confirmAuditPromiseStudent';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('confirmAuditPromiseStudent', 'ANON_RES', 'PromiseClassController/confirmAuditPromiseStudent.do');


--changeset yaoyuqi:1064-1
--comment 目标班目标结果设定增加审批环节
INSERT INTO `role` (`id`, `name`, `roleCode`, `ROLE_LEVEL`, `BREND_MANAGER`, `ROLE_SIGN`, `CREATE_TIME`, `MODIFY_TIME`) VALUES ('brenchPromiseUser', '分公司目标班负责人', null, '2', '1', 'fgsmbbfzr', '2017-06-12 11:12:57', '2017-06-12 11:12:57');
INSERT INTO `role` (`id`, `name`, `roleCode`, `ROLE_LEVEL`, `BREND_MANAGER`, `ROLE_SIGN`, `CREATE_TIME`, `MODIFY_TIME`) VALUES ('campusPromiseUser', '校区目标班负责人', null, '3', '2', 'xqmbbfzr', '2017-06-12 11:12:57', '2017-06-12 11:12:57');
INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`) VALUES ('getPromiseStudentList', '目标班学生管理', '班主任看到自己班的学生', '', 'promiseClass.head_teacher.userId =\'${userId}\'', 'or', 'hql', 'TRUE');
INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`) VALUES ('getPromiseStudentList1', '目标班学生管理', '分公司目标班负责人', 'brenchPromiseUser', 'promiseClass.pSchool.id in (${eachOrganizationBrench})', 'or', 'hql', 'FALSE');
INSERT INTO `role_ql_config` (`ID`, `NAME`, `DESCRIPTION`, `ROLE_ID`, `VALUE`, `JOINER`, `TYPE`, `IS_OTHER_ROLE`) VALUES ('getPromiseStudentList2', '目标班学生管理', '校区目标班负责人', 'campusPromiseUser', 'promiseClass.pSchool.id in (${eachOrganizationCampus})', 'or', 'hql', 'FALSE');

--changeset yaoyuqi:1064-2
--comment 目标班目标结果设定增加审批环节
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('promiseAuditEnd', 'BUTTON', '目标班结课审批按钮', 'RES0000000019', 'promiseAuditEnd');

--changeset yaoyuqi:1078
--comment 收款记录
ALTER TABLE FUNDS_CHANGE_HISTORY ADD FUNDS_CHANGE_TYPE VARCHAR(32) default 'HUMAN' comment '支付类型，人工或者系统';


--changeset yaoyuqi:1048-3
--comment 小班新增销售渠道及优化
delete from resource where id ='checkOnlineSaleBrench';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('checkOnlineSaleBrench', 'ANON_RES', 'CourseController/checkOnlineSaleBrench.do');

--changeset yaoyuqi:1133
--comment 课程模板
delete from resource where id ='saveCourseModal';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveCourseModal', 'ANON_RES', 'CourseModalController/saveCourseModal.do');
delete from resource where id ='getCourseModalList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCourseModalList', 'ANON_RES', 'CourseModalController/getCourseModalList.do');
delete from resource where id ='findModalByModalId';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findModalByModalId', 'ANON_RES', 'CourseModalController/findModalByModalId.do');


--changeset yaoyuqi:1139
--comment 双师管理（分公司）增加筛选项
delete from resource where id ='getLoginUserMainTeacherList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getLoginUserMainTeacherList', 'ANON_RES', 'TwoTeacherClassController/getLoginUserMainTeacherList.do');
delete from resource where id ='getLoginUserTwoTeacherList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getLoginUserTwoTeacherList', 'ANON_RES', 'TwoTeacherClassController/getLoginUserTwoTeacherList.do');

--changeset yaoyuqi:1169
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
delete from resource where id ='getEcsContractInfo';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getEcsContractInfo', 'ANON_RES', 'PromiseClassController/getEcsContractInfo.do');
delete from resource where id ='getEcsContractChargeInfo';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getEcsContractChargeInfo', 'ANON_RES', 'PromiseClassController/getEcsContractChargeInfo.do');


--changeset yaoyuqi:1169-1
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
alter table promise_class_student add audit_remark varchar(500)COMMENT '审批意见',add return_type varchar(32)COMMENT '退费类型',add AUDIT_STATUS varchar(32)COMMENT '审批状态';

drop table if EXISTS promise_audit_log;
CREATE TABLE `promise_audit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `AUDIT_STATUS` varchar(32) DEFAULT NULL COMMENT '审批状态',
  `return_type` varchar(32) DEFAULT NULL COMMENT '退费类型',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `create_time` varchar(20) DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10086 DEFAULT CHARSET=utf8

--changeset yaoyuqi:1169-2
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
delete from resource where id ='savaPromiseReturnAuditInfo';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('savaPromiseReturnAuditInfo', 'ANON_RES', 'PromiseClassController/savaPromiseReturnAuditInfo.do');

--changeset yaoyuqi:1172
--comment 目标班学生课程规划，报班管理，添加小班弹框优化
delete from resource where id ='getCampusByLoginUserNew';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getCampusByLoginUserNew', 'ANON_RES', 'CommonAction/getCampusByLoginUserNew.do');

--changeset yaoyuqi:1169-3
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
delete from resource where id ='getEcsContractChargeList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getEcsContractChargeList', 'ANON_RES', 'PromiseClassController/getEcsContractChargeList.do');

--changeset yaoyuqi:1169-4
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
alter table promise_class_student add old_audit_remark varchar(500)COMMENT '旧审批意见';

--changeset yaoyuqi:1169-5
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
delete from resource where id ='getPromiseSubjectDetailList';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getPromiseSubjectDetailList', 'ANON_RES', 'PromiseClassController/getPromiseSubjectDetailList.do');

--changeset yaoyuqi:1169-6
--comment 目标班学生管理，学生详情页改造，新增中途退费和审批
delete from resource where id ='promiseMidReturnBtn';
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('promiseMidReturnBtn', 'BUTTON', '中途退费审批', 'RES0000000019', 'promiseMidReturnBtn');


--changeset yaoyuqi:newUi
--comment 新ui路径
ALTER TABLE resource add new_url varchar(200) COMMENT '新UI路由';

--changeset yaoyuqi:1271
--comment 小班学生版本
alter table mini_class_student add version int default 0 COMMENT '版本';

--changeset yaoyuqi:1133-1
--comment 课程模板
drop table if exists course_modal;
CREATE TABLE course_modal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    modal_name VARCHAR(200) COMMENT '模板名字',
    tech_num INT COMMENT '讲数',
    product_year VARCHAR(32) COMMENT '年份',
    product_season VARCHAR(32) COMMENT '季节',
    branch_id VARCHAR(32) COMMENT '分公司',
    course_week varchar(32) Comment '上课星期',
    course_phase varchar(32) Comment '上课期数',
    version INT comment '版本',
    create_time VARCHAR(20),
    create_user VARCHAR(32),
    modify_time VARCHAR(20),
    modify_user VARCHAR(32)
);

ALTER TABLE course_modal AUTO_INCREMENT=10086;


drop table if exists course_modal_date;
CREATE TABLE course_modal_date (
    id INT AUTO_INCREMENT PRIMARY KEY,
    modal_id INT not null COMMENT '模板ID',
    COURSE_DATE varchar(20) COMMENT '课程日期',
    version INT comment '版本',
    create_time VARCHAR(20),
    create_user VARCHAR(32),
    modify_time VARCHAR(20),
    modify_user VARCHAR(32)
);
ALTER TABLE course_modal_date AUTO_INCREMENT=10010;

delete from resource where id ='deleteCourseModal';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('deleteCourseModal', 'ANON_RES', 'CourseModalController/deleteCourseModal.do');


--changeset yaoyuqi:1133-2
--comment 课程模板
delete from resource where id ='getSmallClassListOnModal';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('getSmallClassListOnModal', 'ANON_RES', 'SmallClassController/getSmallClassListOnModal.do');

--changeset yaoyuqi:1133-3
--comment 课程模板周几
drop table if EXISTS course_modal_week;
create table course_modal_week (id int auto_increment primary key ,modal_id int ,course_week varchar(32));

--changeset yaoyuqi:1133-4
--comment 教室
delete from resource where id ='getAllCurrentClassroom';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('getAllCurrentClassroom', 'ANON_RES', 'ClassroomManageController/getAllCurrentClassroom.do');

delete from resource where id ='getTeacherListForSelect';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('getTeacherListForSelect', 'ANON_RES', 'TeacherSubjectController/getTeacherListForSelect.do');

--changeset yaoyuqi:1133-5
--comment 小班字段
alter table mini_class add `modal_id` int(11) DEFAULT NULL COMMENT '模版ID',
  add `is_modal` int(11) DEFAULT '0' COMMENT '0未排课，1自由排课，2模版排课';
update mini_class mc set mc.is_modal=1 where EXISTS(select 1 from mini_class_course c where c.mini_class_id = mc.MINI_CLASS_ID)

--changeset yaoyuqi:1133-6
--comment 小班字段
delete from resource where id ='clearAllClassCourse';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('clearAllClassCourse', 'ANON_RES', 'SmallClassController/clearAllClassCourse.do');

delete from resource where id ='saveEditClassModal';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('saveEditClassModal', 'ANON_RES', 'SmallClassController/saveEditClassModal.do');

--changeset yaoyuqi:1133-7
--comment 菜单
delete from resource where id ='courseModalMenu';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('courseModalMenu', 'MENU', '校历与课程模板管理', '0', 'RES0000000010', '21', null);

--changeset yaoyuqi:1133-8
--comment 模板后缀名称
alter table course_modal add suffix_name varchar(100) COMMENT '后缀名称';

--changeset yaoyuqi:1133-9
--comment 菜单
delete from resource where id ='courseModalSchedule';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`) VALUES ('courseModalSchedule', 'MENU', '课程模板排课', '0', 'RES0000000010', '22', null);

--changeset yaoyuqi:1133-10
--comment 按老师查询班级接口
delete from resource where id ='getSmallClassListByTeacher';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) values ('getSmallClassListByTeacher', 'ANON_RES', 'SmallClassController/getSmallClassListByTeacher.do');


--changeset yaoyuqi:verify_code
--comment 验证码
drop table if EXISTS system_verify_code;
CREATE TABLE system_verify_code (
	id INT auto_increment PRIMARY KEY,
	verify_CODE VARCHAR (10)COMMENT '验证码',
	STATUS INT COMMENT '状态',
	phone_num varchar(11) COMMENT '手机号',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`MODIFY_TIME` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	CREATE_USER VARCHAR (32),
	MODIFY_USER VARCHAR (32)
);

delete from resource where id ='sendVerifyCode';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('sendVerifyCode', 'ANON_RES', 'CommonAction/sendVerifyCode.do');
delete from resource where id ='verifyUserCode';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('verifyUserCode', 'ANON_RES', 'CommonAction/verifyUserCode.do');



--changeset yaoyuqi:1310
--comment 课程管理双师管理，增加双师课程汇总
delete from resource where id ='getTwoTeacherCourseTotal';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherCourseTotal', 'ANON_RES', 'ReportAction/getTwoTeacherCourseTotal.do');
delete from resource where id ='twoTeacherCourseCollect';
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`, `PARENT_ID`, `RNAME`) VALUES ('twoTeacherCourseCollect', 'MENU', 'function/realtimereport/twoTeacherCourseCollect.html', 'twoteacherclass', '双师课程汇总');


--changeset yaoyuqi:1378
--comment boss旧版校历与课程模板管理页面、课程模板排课页面增加跳转提示
update resource set rurl='404.html' where id ='courseModalMenu';
update resource set rurl='404.html' where id ='courseModalSchedule';

--changeset yaoyuqi:1440
--comment boss小班课程进行角色权限控制
delete from resource where id ='modify_mini_course';
insert into resource (id,rtype,rname,parent_id,rorder,rtag) values ('modify_mini_course','BUTTON','小班课程修改','RES0000000002',9,'modify_mini_course');

--changeset yaoyuqi:1440-1
--comment boss小班课程进行角色权限控制
update resource set PARENT_ID='RES0000000089',RORDER=17 where id ='modify_mini_course';

--changeset yaoyuqi:1453
--comment boss小班课程进行角色权限控制
delete from role_resource where resourceID='SMALL_CLASS_CANCEL_COURSE';
delete from resource where ID='SMALL_CLASS_CANCEL_COURSE';

--changeset yaoyuqi:1258
--comment 目标班学生管理（班主任）、目标班履行详情、课程规划优化，及扣费规则修改
alter table promise_class_subject add consume_course_hours decimal(10,2) default 0  COMMENT '消耗课时';

update promise_class_subject p ,(
select pcs.id ,ifnull(sum(acr.QUANTITY),0) quantity from promise_class_subject pcs
left join promise_class_student stu on pcs.promise_student_id = stu.ID
left join account_charge_records acr on acr.MINI_CLASS_ID=pcs.mini_class_id and acr.STUDENT_ID = stu.STUDENT_ID
where pcs.mini_class_id is not null and acr.CHARGE_PAY_TYPE ='CHARGE' AND acr.IS_WASHED = 'FALSE'
group by pcs.id) pp set p.consume_course_hours=pp.quantity where p.id = pp.id ;

delete from resource where id ='getStudentMiniClassAttendent';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getStudentMiniClassAttendent', 'ANON_RES', 'SmallClassController/getStudentMiniClassAttendent.do');


--changeset yaoyuqi:1258-1
--comment 目标班学生管理（班主任）、目标班履行详情、课程规划优化，及扣费规则修改
delete from resource where id ='getMiniClassCourseDateInfo';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getMiniClassCourseDateInfo', 'ANON_RES', 'SmallClassController/getMiniClassCourseDateInfo.do');

--changeset yaoyuqi:1544
--comment 数据结转处理
alter table contract add pub_pay_contract int DEFAULT 0 COMMENT '0不是公帐合同，1公帐合同';
alter table funds_change_history add pub_pay_contract int DEFAULT 0 COMMENT '0不是公帐合同，1公帐合同';

CREATE TABLE student_acc_info (
	id INT auto_increment PRIMARY KEY,
	student_id VARCHAR (32) UNIQUE NOT NULL,
	account_amount DECIMAL (10, 2),
	version int,
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`MODIFY_TIME` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE student_acc_info_log (
	id INT auto_increment PRIMARY KEY,
	student_id VARCHAR (32) NOT NULL COMMENT '学生id',
	change_amount DECIMAL (10, 2)  COMMENT '变更金额',
	AFTER_AMOUNT DECIMAL (10, 2)  COMMENT '变更后金额',
	type varchar(10)  COMMENT '类型',
  log_status INT  COMMENT '状态' DEFAULT 0,
  from_student_id varchar(32) COMMENT '用于学生转账',
	sign_staff_id varchar(32) COMMENT '签单人Id',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`MODIFY_TIME` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	create_user_id varchar(32),
	modify_user_id varchar(32),
  remark varchar(100)
);


delete from resource where id ='getPublicFinanceContractAnalyze';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getPublicFinanceContractAnalyze', 'ANON_RES', 'ReportAction/getPublicFinanceContractAnalyze.do');


delete from resource where id ='financeAnalyzePub';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`,`RNAME`,`HAS_CHILDREN`,`PARENT_ID`,`RORDER`) VALUES ('financeAnalyzePub', 'MENU', 'function/reportforms/financeAnalyzePublic.html','实收现金流(公帐)',0,'RES0000000092',2);



--changeset yaoyuqi:1492
--comment 小班“允许超额人数”叠加到招生人数上，并新增是否满班状态
update mini_class set RECRUIT_STUDENT_STATUS='ING';

--changeset yaoyuqi:1551
--comment 双师主班课程修改权限优化
alter table TWO_TEACHER_CLASS_COURSE add teacher_id varchar(32) COMMENT '老师';
update TWO_TEACHER_CLASS_COURSE ttcc left join two_teacher_class ttc on ttc.CLASS_ID=ttcc.class_id set ttcc.teacher_id =ttc.TEACHER_ID;

--changeset yaoyuqi:1552
--comment 学生跟进回访优化需求
delete from resource where id ='getStudentFollowUpById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getStudentFollowUpById', 'ANON_RES', 'StudentController/getStudentFollowUpById.do');
delete from resource where id ='delStudentFollowUpById';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('delStudentFollowUpById', 'ANON_RES', 'StudentController/delStudentFollowUpById.do');

--changeset yaoyuqi:1570
--comment 校历课程模板，新增开始时间字段，以及相关页面优化
alter table course_modal add CLASS_TIME VARCHAR(32) COMMENT '上课时间';

--changeset yaoyuqi:1619
--comment 小班修改关联产品时，增加总课时数匹配判断
delete from resource where id ='getMiniClassCanChangeProduct';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getMiniClassCanChangeProduct', 'ANON_RES', 'SmallClassController/getMiniClassCanChangeProduct.do');

--changeset yaoyuqi:1582
--comment 目标班产品单科全科----alter table product add single_subject varchar(20) NOT NULL DEFAULT 'ALL_SUBJECT' COMMENT '单科/全科';
alter table product add single_subject varchar(20) NOT NULL DEFAULT 'ALL_SUBJECT' COMMENT '单科/全科';

--changeset yaoyuqi:1550
--comment 运营指标营收新增双师跟直播数据----alter table income_count_brench add twoTeacherRealAmount decimal(10,2) DEFAULT 0 COMMENT '双师实际金额',add twoTeacherPromotionAmount decimal(10,2) DEFAULT 0 COMMENT '双师优惠金额',add twoTeacherQuantity decimal(10,2) DEFAULT 0 COMMENT '双师课时',add liveRealAmount decimal(10,2) DEFAULT 0 COMMENT '直播实际金额',add livePromotionAmount decimal(10,2) DEFAULT 0 COMMENT '直播优惠金额',add liveQuantity decimal(10,2) DEFAULT 0 COMMENT '直播课时';alter table income_count_campus add twoTeacherRealAmount decimal(10,2) DEFAULT 0 COMMENT '双师实际金额',add twoTeacherPromotionAmount decimal(10,2) DEFAULT 0 COMMENT '双师优惠金额',add twoTeacherQuantity decimal(10,2) DEFAULT 0 COMMENT '双师课时',add liveRealAmount decimal(10,2) DEFAULT 0 COMMENT '直播实际金额',add livePromotionAmount decimal(10,2) DEFAULT 0 COMMENT '直播优惠金额',add liveQuantity decimal(10,2) DEFAULT 0 COMMENT '直播课时';
alter table income_count_brench
add twoTeacherRealAmount decimal(10,2) DEFAULT 0 COMMENT '双师实际金额',
add twoTeacherPromotionAmount decimal(10,2) DEFAULT 0 COMMENT '双师优惠金额',
add twoTeacherQuantity decimal(10,2) DEFAULT 0 COMMENT '双师课时',
add liveRealAmount decimal(10,2) DEFAULT 0 COMMENT '直播实际金额',
add livePromotionAmount decimal(10,2) DEFAULT 0 COMMENT '直播优惠金额',
add liveQuantity decimal(10,2) DEFAULT 0 COMMENT '直播课时';

alter table income_count_campus
add twoTeacherRealAmount decimal(10,2) DEFAULT 0 COMMENT '双师实际金额',
add twoTeacherPromotionAmount decimal(10,2) DEFAULT 0 COMMENT '双师优惠金额',
add twoTeacherQuantity decimal(10,2) DEFAULT 0 COMMENT '双师课时',
add liveRealAmount decimal(10,2) DEFAULT 0 COMMENT '直播实际金额',
add livePromotionAmount decimal(10,2) DEFAULT 0 COMMENT '直播优惠金额',
add liveQuantity decimal(10,2) DEFAULT 0 COMMENT '直播课时';

--changeset yaoyuqi:1630
--comment 培优营收凭证打印
delete from resource where id ='incomeDetailAdvance';
insert into resource (id,rtype,rurl) values ('incomeDetailAdvance','ANON_RES','function/reportforms/incomeEvidenceDetailModalAdvance.html');


--changeset yaoyuqi:app1027newmenu
--comment App  新菜单
delete from resource where id ='attendentByCard';
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RURL,CREATE_TIME,CREATE_USER)
VALUES('attendentByCard','APP','考勤打卡',0,-1,-1,'#','2017-10-27 23:53:22','112233');



--changeset yaoyuqi:1582-1
--comment 目标班产品单科全科----ALTER TABLE `product` CHANGE COLUMN `single_subject` `single_subject` VARCHAR(20) NULL DEFAULT 'ALL_SUBJECT' COMMENT '单科/全科' ;
ALTER TABLE `product` CHANGE COLUMN `single_subject` `single_subject` VARCHAR(20) NULL DEFAULT 'ALL_SUBJECT' COMMENT '单科/全科' ;

--changeset yaoyuqi:1755
--comment 小班转班优化
delete from resource where id ='changeMiniClassSelectList';
insert into resource (id,rtype,rurl) values ('changeMiniClassSelectList','ANON_RES','CourseController/changeMiniClassSelectList.do');
delete from resource where id ='findAllEnableMiniClassCourse';
insert into resource (id,rtype,rurl) values ('findAllEnableMiniClassCourse','ANON_RES','CourseController/findAllEnableMiniClassCourse.do');

--changeset yaoyuqi:1743
--comment 合同管理中，扣费记录和退费记录导出提高限额 ALTER TABLE `student_return_fee` DROP INDEX  ,ADD INDEX `idx_campus` (`CAMPUS` ASC);
ALTER TABLE `student_return_fee` ADD INDEX `idx_campus` (`CAMPUS` ASC);

--changeset yaoyuqi:1743-1
--comment 合同管理中，扣费记录和退费记录导出提高限额
delete from role_ql_config where name ='扣费记录' and type='sql';
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql1', '扣费记录', '会计走组织架构', 'ROL0000000061', 'acr.bl_campus_id in (select id from Organization organization where  ${foreachOrganization})', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql2', '扣费记录', '会计走组织架构', 'ROL0000000021', 'acr.bl_campus_id in (select id from Organization organization where  ${foreachOrganization})', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql3', '扣费记录', '只能看见本人所属校区的数据', '', 'acr.bl_campus_id in (select id from Organization where orgLevel like \'${blCampusId}%\')', 'or', 'sql', 'TRUE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql4', '扣费记录', '只能看见自己带的学生的合同的扣费记录', 'ROL0000000001', 's.study_manager_id = \'${userId}\'', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql5', '扣费记录', '只能看见自己签单的合同的扣费记录', 'ROL0000000001', 'c.sign_staff_id = \'${userId}\'', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql6', '扣费记录', '只能看见自己签单的合同的扣费记录', '4028818d46e1abf10146e1abf3fd0001',  'c.sign_staff_id = \'${userId}\'', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql7', '扣费记录', '校区管理学管', 'specialRole', '(s.id like \'0%\' or s.id like \'%5\') and $(isForbidden)', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql8', '扣费记录', '校区管理主任', 'specialRole1', '(s.id like \'0%\' or s.id like \'%5\') and $(isForbidden)', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql9', '扣费记录', '校区管理经理', 'specialRole2', '(s.id like \'0%\' or s.id like \'%5\') and $(isForbidden)', 'or', 'sql', 'FALSE');

--changeset yaoyuqi:1743-2
--comment 合同管理中，扣费记录和退费记录导出提高限额
delete from role_ql_config where id ='listChargeSql4';
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql4', '扣费记录', '只能看见自己带的学生的合同的扣费记录', 'ROL0000000001', 's.STUDY_MANEGER_ID = \'${userId}\'', 'or', 'sql', 'FALSE');
delete from role_ql_config where id ='listChargeSql5';
delete from role_ql_config where id ='listChargeSql6';
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql5', '扣费记录', '只能看见自己签单的合同的扣费记录', 'ROL0000000001', 'con.sign_staff_id = \'${userId}\'', 'or', 'sql', 'FALSE');
insert into role_ql_config(id,name,description,role_id,value,joiner,type,is_other_role) VALUES ('listChargeSql6', '扣费记录', '只能看见自己签单的合同的扣费记录', '4028818d46e1abf10146e1abf3fd0001',  'con.sign_staff_id = \'${userId}\'', 'or', 'sql', 'FALSE');


--changeset yaoyuqi:1978
--comment boss组织架构板块改造
delete from resource where id ='saveHrmsOrgToBoss';
insert into resource (id,rtype,rurl) values ('saveHrmsOrgToBoss','ANON_RES','MessageReceiveController/saveHrmsOrgToBoss.do');
delete from resource where id ='cacelHrmsOrgInBoss';
insert into resource (id,rtype,rurl) values ('cacelHrmsOrgInBoss','ANON_RES','MessageReceiveController/cacelHrmsOrgInBoss.do');
delete from resource where id ='getBossOrganizationList';
insert into resource (id,rtype,rurl) values ('getBossOrganizationList','ANON_RES','MessageReceiveController/getBossOrganizationList.do');

--changeset yaoyuqi:761930
--comment 双师优化
delete from resource where id ='findCourseByClassTwoId';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findCourseByClassTwoId', 'ANON_RES', 'TwoTeacherClassController/findCourseByClassTwoId.do');