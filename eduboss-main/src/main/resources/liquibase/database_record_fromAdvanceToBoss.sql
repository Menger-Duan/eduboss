--liquibase formatted sql

--changeset 肖金旺: 20170603
--comment 从培优到星火的sql
ALTER TABLE `product`
ADD COLUMN `TEXTBOOK_ID`  int(11) NULL COMMENT '教材id' AFTER `MAX_PROMOTION_DISCOUNT`,
ADD COLUMN `TEXTBOOK_NAME`  varchar(32) NULL DEFAULT '' COMMENT '教材名字' AFTER `TEXTBOOK_ID`;

-- INSERT INTO resource(ID, RTYPE, RURL) VALUE('getTextBookInfoList', 'ANON_RES', '/ProductController/getTextBookInfoList.do');nuat有
INSERT INTO role_resource (roleID,resourceID)VALUES('ROL0000000146','getTextBookInfoList');

-- ALTER TABLE `teacher_subject`
-- ADD COLUMN `UPDATE_TIME`  varchar(32) NULL AFTER `CREATE_USER_ID`,
-- ADD COLUMN `UPDATE_USER_ID`  varchar(32) NULL AFTER `UPDATE_TIME`;

-- INSERT INTO resource(ID, RTYPE, RURL) VALUE('inactivePromotion', 'ANON_RES', '/PromotionController/inactivePromotion.do');
-- INSERT INTO resource(ID, RTYPE, RURL) VALUE('activePromotion', 'ANON_RES', '/PromotionController/activePromotion.do');

--changeset guoshibo:1148
--comment 双师管理（分公司）双师辅班添加学生退班功能
delete from resource where id ='deleteTwoTeacherClass';
INSERT INTO  `resource` (`ID`, `RTYPE`, `RNAME`, `PARENT_ID`, `RTAG`) VALUES ('deleteTwoTeacherClass', 'BUTTON', '退班', 'twoteacherclassbrench', 'deleteTwoTeacherClass');