--liquibase formatted sql

--changeset 郭诗博:advance
--comment
UPDATE resource SET RURL='js/advance/usermanage.html' WHERE RNAME='用户管理'; -- 需要
--UPDATE resource SET RURL='function/system/usermanage.html' WHERE RNAME='用户管理';
UPDATE resource SET RURL='js/advance/organizationtreemanage.html' WHERE RNAME='组织架构';   -- 需要
--UPDATE resource SET RURL='function/system/organizationtreemanage.html' WHERE RNAME='组织架构';
UPDATE resource SET RURL='js/advance/jobManage.html' WHERE RNAME='职位管理';  -- 需要
--UPDATE resource SET RURL='function/system/jobManage.html' WHERE RNAME='职位管理';

-- SELECT * FROM  `user_job` WHERE JOB_SIGN='xqcwspr';
-- SELECT * FROM  `user_job` WHERE JOB_SIGN='fgszjcwspr';

DELETE FROM resource WHERE RNAME ='校区资金支出管理'; -- 需要
-- INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES ('procedureManage', 'MENU', '存储过程管理', '0', '7', '165', 'function/system/procedureManage.html', NULL, NULL, NULL, NULL, NULL, NULL);
DELETE FROM resource WHERE RNAME ='存储过程管理';  -- 需要

DELETE FROM resource WHERE RNAME ='工作日志总结表';  -- 需要
DELETE from role_resource WHERE resourceID not in (SELECT id from resource); -- 需要

DELETE FROM role_resource WHERE resourceID ='twoteacherclass'; -- 需要



INSERT INTO resource (ID,RTYPE,RURL) VALUES ('editUserForAdvance','ANON_RES','/SystemAction/editUserForAdvance.do'); -- 需要

alter table product
add live_Id varchar(32) comment '直播产品ID',
add total_amount decimal(10,2) comment '总额',
add start_time varchar(5) comment '开始时间',
add promotion_amount decimal(10,2) comment '优惠金额';

create table PRODUCT_BRANCH (id int auto_increment primary key ,PRODUCT_ID VARCHAR(32) COMMENT '产品ID',BRANCH_ID VARCHAR(32) COMMENT '分公司ID');


--changeset 郭诗博:#964
--comment
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getOtherProductForEcs', 'ANON_RES', '/ProductController/getOtherProductForEcs.do');  -- 需要

DROP TABLE if EXISTS `product_package`;
 CREATE TABLE `product_package`(
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `parent_product_id` varchar(32) DEFAULT NULL COMMENT '父级产品ID',
 `son_product_id` varchar(32) DEFAULT NULL COMMENT '子产品ID',
 `create_user_id` varchar(32)  DEFAULT NULL COMMENT '创建人',
 `create_time` varchar(20)  DEFAULT NULL COMMENT '创建时间',
 PRIMARY KEY (`id`)
 )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


UPDATE resource  SET RURL='function/contract/editContractNew.html?database=advance' WHERE RTYPE='MENU' AND RNAME='添加合同';

alter table intenet_pay add AUTH_CODE VARCHAR(18) comment '付款二维码';
ALTER TABLE `intenet_pay` ADD UNIQUE INDEX `reqsn_UNIQUE` (`reqsn` ASC);
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveBarCodeInfo', 'ANON_RES', 'IntenetPayController/saveBarCodeInfo.do');

UPDATE resource SET RURL='function/course/miniClassCourse.html?database=advance' WHERE RTYPE='MENU' AND RNAME='小班考勤+结算'; -- 需要


INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
VALUES ('PRINT_SIMPLE_CONTRACT', 'BUTTON', '打印简要合同', '0', '58', NULL, NULL, 'PRINT_SIMPLE_CONTRACT', NULL, NULL, NULL, NULL, NULL); -- 需要



INSERT INTO income_distribution (`bonus_staff_id`, `bonus_type`, `product_type`, `base_bonus_type`, `sub_bonus_type`, `amount`, `create_time`, `funds_change_id`, `student_return_id`, `bonus_staff_campus`, `contract_campus`)
  SELECT BONUS_STAFF_ID, BONUS_TYPE, PRODUCT_TYPE, 'USER', 'USER_USER', BONUS_AMOUNT, CREATE_TIME, FUNDS_CHANGE_ID, STUDENT_RETURN_ID , BONUS_STAFF_CAMPUS, CONTRACT_CAMPUS FROM contract_bonus WHERE BONUS_STAFF_ID IS NOT NULL;  -- 需要


INSERT INTO income_distribution (`organizationId`, `bonus_type`, `product_type`, `base_bonus_type`, `sub_bonus_type`, `amount`, `create_time`, `funds_change_id`, `student_return_id`, `bonus_staff_campus`, `contract_campus`)
  SELECT ORGANIZATIONID, BONUS_TYPE, PRODUCT_TYPE, 'CAMPUS', 'CAMPUS_CAMPUS', CAMPUS_AMOUNT, CREATE_TIME, FUNDS_CHANGE_ID, STUDENT_RETURN_ID, BONUS_STAFF_CAMPUS, CONTRACT_CAMPUS FROM contract_bonus WHERE ORGANIZATIONID IS NOT NULL;  -- 需要



UPDATE resource SET RURL = 'function/contract/listContract.html?database=advance' WHERE RTYPE='MENU' AND RNAME='合同列表';


INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`)
VALUES ('advanceOfcontractDetailModal', 'ANON_RES', NULL, NULL, NULL, NULL, 'function/contract/advance/contractDetailModal.html', NULL, NULL, NULL, NULL, NULL, NULL); -- 需要




alter table finance_brench add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';
alter table finance_campus add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';
alter table finance_user add ONLINE_AMOUNT decimal(10,2) default 0.00 comment '线上业绩',add LINE_AMOUNT decimal(10,2) default 0.00 comment '线下业绩';


UPDATE resource SET RURL = 'function/contract/listContract.html' WHERE RTYPE='MENU' AND RNAME='合同列表'; -- 需要


ALTER TABLE mini_class ADD COLUMN CHANNEL_TYPE VARCHAR(32) NOT NULL DEFAULT 'OFFLINE' COMMENT '销售渠道：OFFLINE线下，ON_LINE线上';

ALTER TABLE `mini_class`
ADD COLUMN `TEXTBOOK_ID`  int(11) NULL COMMENT '教材id' AFTER `CHANNEL_TYPE`,
ADD COLUMN `TEXTBOOK_NAME`  varchar(32) NULL COMMENT '教材名字' AFTER `TEXTBOOK_ID`;



INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTextBookInfoList', 'ANON_RES', '/ProductController/getTextBookInfoList.do');--需要

INSERT INTO resource(ID, RTYPE, RURL) VALUE('setDeleteStudent', 'ANON_RES', '/CustomerAction/setDeleteStudent.do');

ALTER TABLE `customer_student_relation`
ADD COLUMN `IS_DELETED`  tinyint(1) NULL DEFAULT 0 AFTER `CUSTOMER_STUDENT_STATUS`;


INSERT INTO data_dict(ID,NAME,VALUE,DICT_ORDER,CATEGORY,STATE,IS_SYSTEM)VALUES('TRANSFER','转介绍','TRANSFER','8','RES_ENTRANCE','0','1');

-- 培优上线sql
ALTER TABLE `account_charge_records`
ADD COLUMN `TWO_TEACHER_STUDENT_ATTENDENT`  int(32) NULL COMMENT '双师考勤记录id' AFTER `MINI_CLASS_COURSE_ID`;

ALTER TABLE `two_teacher_class_student_attendent`
ADD COLUMN `CLASS_TWO_ID`  int(11) NULL COMMENT '辅班id' AFTER `TWO_CLASS_COURSE_ID`,
ADD COLUMN `ATTENDANCE_PIC_NAME`  varchar(50) NULL COMMENT '考勤图片地址' AFTER `version`;

ALTER TABLE `two_teacher_class_student_attendent`
ADD COLUMN `COURSE_STATUS`  varchar(32) NULL COMMENT '课程状态' AFTER `MODIFY_USER_ID`;

ALTER TABLE `two_teacher_class_student_attendent`
ADD COLUMN `AUDIT_STATUS`  varchar(32) NULL COMMENT '审批状态' AFTER `COURSE_STATUS`;


ALTER TABLE `student_school`
ADD COLUMN `LOG`  varchar(255) NULL COMMENT '学校经度' AFTER `ADDRESS`,
ADD COLUMN `LAT`  varchar(255) NULL COMMENT '学校维度' AFTER `LOG`;

ALTER TABLE `organization`
ADD COLUMN `state_of_emergency`  varchar(32) NULL DEFAULT 'NORMAL' COMMENT '组织架构紧急状态' AFTER `area_id`;

CREATE TABLE `mini_class_inventory` (
  `ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `MINI_CLASS_ID` char(32) NOT NULL COMMENT '小班ID',
	`MAX_QUANTIY` int(11) DEFAULT NULL COMMENT '可报名额',
  `LEFT_QUANTITY` int(11) DEFAULT NULL COMMENT '剩余名额',
	`CREATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MODIFY_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
	UNIQUE KEY `UQ_MINI_CLASS_ID` (`mini_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小班库存表';