--liquibase formatted sql

--changeset duanmenrun:1728_1
--comment 网络分配过程时长
CREATE TABLE `customer_allocate_obtain`(  
  `id` VARCHAR(32) NOT NULL ,
  `customer_id` VARCHAR(32) NOT NULL COMMENT '客户id',
  `allocate_time` varchar(20) DEFAULT NULL COMMENT '分配时间',
  `obtain_time` varchar(20) DEFAULT NULL COMMENT '获取时间',
  `status` TINYINT DEFAULT NULL COMMENT '是否获取,0未获取，1已获取',
  `type` VARCHAR(32) DEFAULT NULL COMMENT '事件类型',
  PRIMARY KEY (`id`),
  INDEX `idx_customer_id` (`customer_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='客户分配获取时间表';


--changeset duanmenrun:1736_1-1
--comment 新建表格：直播订单基础信息
CREATE TABLE `live_payment_record`(  
  `id` VARCHAR(32) NOT NULL,
  `payment_date` VARCHAR(20) COMMENT '支付日期',
  `student_name` VARCHAR(64) COMMENT '学生姓名',
  `student_id` VARCHAR(32) COMMENT '学生id',
  `student_contact` VARCHAR(32) COMMENT '学生电话',
  `user_name` VARCHAR(64) COMMENT '签单人姓名',
  `user_employeeNo` VARCHAR(32) COMMENT '签单人工号',
  `user_contact` VARCHAR(15) COMMENT '签单人电话',
  `user_campusId` VARCHAR(32) COMMENT '合同校区',
  `total_amount` DECIMAL(10,2) COMMENT '总额',
  `paid_amount` DECIMAL(10,2) COMMENT '分成总额',
  `campus_achievement` DECIMAL(10,2) COMMENT '校区业绩',
  `user_achievement` DECIMAL(10,2) COMMENT '个人业绩',
  `order_num` VARCHAR(32) COMMENT '直播订单号',
  `course_name` VARCHAR(150) COMMENT '课程名称',
  `contact_type` VARCHAR(10) COMMENT '合同类型,新签，续费',
  `finance_type` VARCHAR(10) COMMENT '流水类型，现金流，退费，营收',
  PRIMARY KEY (`id`),
  INDEX `idx_student_id` (`student_id`),
  INDEX `idx_order_num` (`order_num`)
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci ;


--changeset duanmenrun:1736_2-1
--comment 新建表格：直播订单流水
CREATE TABLE `live_transfer_pay` (
	`id` VARCHAR(32) NOT NULL,
	`amount`  DECIMAL(10,2) COMMENT '金额',
	`retcode` VARCHAR (10) COMMENT '返回码SUCCESS/FAIL',
	`retmsg` VARCHAR (100) COMMENT '返回码说明',
	`live_type` VARCHAR (10) COMMENT '新签/续签',
	`finance_type` VARCHAR (10) COMMENT '缴费/退款',
	`create_time` VARCHAR (20),
	`modify_time` VARCHAR (20),
	`remark` VARCHAR (200) COMMENT '备注',
	`trxid` VARCHAR (20) COMMENT '通联流水',
	`chnltrxid` VARCHAR (50)  COMMENT '支付宝微信支付流水',
	`pay_type` VARCHAR (10) COMMENT '支付类型',
	`trxstatus` VARCHAR (10)  COMMENT '交易的状态',
	`title` VARCHAR (100)  COMMENT '支付标题',
	`pay_info` TEXT COMMENT '二维码信息',
	`finish_time` VARCHAR (20) ,
	`errmsg` VARCHAR (100) COMMENT '错误原因',
	`reqsn` VARCHAR (32) COMMENT '商户交易单号',
	`transaction_num` VARCHAR (32)COMMENT '直播交易号',
	`callback_url` TEXT COMMENT '直播回调地址',
	`status`  VARCHAR (5) COMMENT '交易进行状态',
	`pay_time` VARCHAR (20) COMMENT '支付成功',
	PRIMARY KEY (`id`),
	INDEX `idx_transaction_num` (`transaction_num`),
	INDEX `idx_reqsn` (`reqsn`)
)ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_general_ci;

--changeset duanmenrun:1736_3-1
--comment 新建表格：直播课程详情
CREATE TABLE `live_course_detail`(  
  `id` VARCHAR(32) NOT NULL ,
  `course_id` VARCHAR(32) NOT NULL COMMENT '直播课程id',
  `course_name` VARCHAR(150) DEFAULT NULL COMMENT '直播课程名称',
  `paid_amount` DECIMAL(10,2) COMMENT '金额',
  `pay_record_id` VARCHAR(32) NOT NULL COMMENT '直播流水id',
  PRIMARY KEY (`id`),
   INDEX `idx_pay_record_id` (`pay_record_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='直播课程';

ALTER TABLE live_payment_record CHANGE user_campusId order_campusId VARCHAR(32) COMMENT '合同校区';


--changeset duanmenrun:1836-1 endDelimiter:\$\$
--comment 资源回收，（解决校区回收到分公司时文案错误）
DROP PROCEDURE IF EXISTS `proc_customer_resource_back`;

$$
CREATE  PROCEDURE `proc_customer_resource_back`()
BEGIN
		DECLARE cus_id VARCHAR(32);
    DECLARE return_node VARCHAR(32);
		DECLARE nowDate CHAR(32);
		
		DECLARE deliverTarget VARCHAR(32);
   
    DECLARE gainCustomerId VARCHAR(32);
    
    DECLARE poolName VARCHAR(32);
    
    DECLARE nextPoolName VARCHAR(32);
	 DECLARE cur1 CURSOR FOR SELECT cus.id,RETURN_NOTE,NOW(),cus.DELEVER_TARGET AS deliverTarget,cus.poolName,cus.nextPoolName
			FROM 
			( 
				SELECT c.*,p.RETURN_NOTE,p.RECOVEY_PERIOD,p.`NAME` AS poolName,np.NAME AS nextPoolName  FROM customer c  INNER JOIN resource_pool p ON c.delever_target= p.organization_id
				
				INNER JOIN resource_pool np ON p.RETURN_NOTE = np.organization_id
			 WHERE c.DELEVER_TARGET IS NOT NULL AND c.DEAL_STATUS IN ('STAY_FOLLOW','FOLLOWING','INVALID')  AND p.`STATUS`='VALID' AND p.CYCLE_TYPE='1' ) cus WHERE TO_DAYS(NOW())-TO_DAYS
(cus.modify_time)>cus.recovey_period;
	 DECLARE cur2 CURSOR FOR SELECT cus.id,cus.orgId,NOW() AS nowDate,cus.DELEVER_TARGET AS deliverTarget,cus.poolName FROM 
			( 
				SELECT c.*,uj.return_cycle,og.id AS orgId,og.RESOURCE_POOL_NAME AS poolName FROM customer  c 
					INNER JOIN user_dept_job udj ON c.delever_target=udj.user_id
					INNER JOIN user_job uj ON udj.job_id = uj.id
					INNER JOIN USER u ON u.user_id=c.delever_target
	      	INNER JOIN organization o ON udj.DEPT_ID = o.id
          INNER JOIN organization og ON og.id = o.belong
					WHERE uj.is_customer_follow='0' AND udj.isMajorRole ='0' AND c.DELEVER_TARGET IS NOT NULL AND c.DEAL_STATUS IN ('STAY_FOLLOW','FOLLOWING') AND 
uj.CYCLE_TYPE='1' )cus
						WHERE TO_DAYS(NOW())-TO_DAYS(cus.modify_time)>cus.return_cycle;
 DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = TRUE;
    SET @CURSOR_STOP_FLAG =FALSE;
    
     
    OPEN cur1;
		FETCH cur1 INTO cus_id,return_node,nowDate,deliverTarget,poolName,nextPoolName;
			WHILE !@CURSOR_STOP_FLAG DO
				
          SET gainCustomerId = CONCAT("GAI0",(SELECT nextval('gain_customer')));
					UPDATE customer SET DELEVER_TARGET=return_node,MODIFY_TIME=nowDate,DELIVER_TYPE='CUSTOMER_RESOURCE_POOL',DEAL_STATUS='STAY_FOLLOW',VISIT_TYPE = 'NOT_COME' WHERE id=cus_id;
					INSERT INTO customer_dynamic_status (ID,CUSTOMER_ID,DYNAMIC_STATUS_TYPE,OCCOUR_TIME,DESCRIPTION,REFER_URL,REFERUSER_ID,RES_ENTRANCE,STATUS_NUM,TABLE_NAME,TABLE_ID,VISIT_FLAG,DELIVER_TARGET)VALUES(CONCAT("CUS00000",(SELECT nextval
('customer_dynamic_status'))),cus_id,'BACK',nowDate,CONCAT("系统回收到:",nextPoolName),NULL,NULL,NULL,0,'gain_customer',gainCustomerId,1,deliverTarget);
					
					INSERT INTO gain_customer(ID,CUS_ID,DELIVERFROM,DELIVERTARGET,REASON,CREATE_USER,CREATE_TIME) VALUES(gainCustomerId,cus_id,deliverTarget,return_node,'系统回收',NULL,nowDate);
				
			FETCH cur1 INTO cus_id,return_node,nowDate,deliverTarget,poolName,nextPoolName;
			END WHILE;
		CLOSE cur1;
 SET @CURSOR_STOP_FLAG =FALSE;
	OPEN cur2;
	FETCH cur2 INTO cus_id,return_node,nowDate,deliverTarget,poolName;
		WHILE !@CURSOR_STOP_FLAG DO
			
          SET gainCustomerId = CONCAT("GAI0",(SELECT nextval('gain_customer')));
					UPDATE customer SET DELEVER_TARGET=return_node,MODIFY_TIME=nowDate,DELIVER_TYPE='CUSTOMER_RESOURCE_POOL',DEAL_STATUS='STAY_FOLLOW',VISIT_TYPE = 'NOT_COME' WHERE id=cus_id;
					INSERT INTO customer_dynamic_status (ID,CUSTOMER_ID,DYNAMIC_STATUS_TYPE,OCCOUR_TIME,DESCRIPTION,REFER_URL,REFERUSER_ID,RES_ENTRANCE,STATUS_NUM,TABLE_NAME,TABLE_ID,VISIT_FLAG,DELIVER_TARGET)VALUES(CONCAT("CUS00000",(SELECT nextval
('customer_dynamic_status'))),cus_id,'BACK',nowDate,CONCAT("系统回收到:",poolName),NULL,NULL,NULL,0,'gain_customer',gainCustomerId,1,deliverTarget);
                    INSERT INTO gain_customer(ID,CUS_ID,DELIVERFROM,DELIVERTARGET,REASON,CREATE_USER,CREATE_TIME) VALUES(gainCustomerId,cus_id,deliverTarget,return_node,'系统回收',NULL,nowDate);
			
				FETCH cur2 INTO cus_id,return_node,nowDate,deliverTarget,poolName;
			END WHILE;
		CLOSE cur2;
END
$$

--changeset duanmenrun:1626——1
--comment 增加消息类型MSG_TYPE   app营主、咨询师获得新客户资源
INSERT INTO  `data_dict` (`ID`, `NAME`, `VALUE`, `DICT_ORDER`, `PARENT_ID`, `CATEGORY`, `CREATE_TIME`, `CREATE_USER_ID`, `STATE`, `IS_SYSTEM`) VALUES ('NEW_CUSTOMER_RESOURCE', '新客户资源', 'NEW_CUSTOMER_RESOURCE', '15', NULL, 'MSG_TYPE', NULL, NULL, '0', NULL);


--changeset duanmenrun:1940---2---1.2 endDelimiter:\$\$
--comment 修改营收凭证存储过程
DROP PROCEDURE IF EXISTS `proc_ods_month_income_campus`;
$$

CREATE  PROCEDURE `proc_ods_month_income_campus`(
IN in_count_date VARCHAR(10))
    SQL SECURITY INVOKER
BEGIN
			DELETE FROM ods_month_income_campus WHERE COUNT_DATE = in_count_date;
			INSERT INTO ods_month_income_campus(ID, GROUP_ID, BRENCH_ID, CAMPUS_ID, ONE_ON_ONE_REAL_AMOUNT, ONE_ON_ONE_PROMOTION_AMOUNT, ONE_ON_ONE_REAL_WASH_AMOUNT, ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
									SMALL_CLASS_REAL_AMOUNT, SMALL_CLASS_PROMOTION_AMOUNT, SMALL_CLASS_REAL_WASH_AMOUNT, SMALL_CLASS_PROMOTION_WASH_AMOUNT,
									TWO_TEACHER_REAL_AMOUNT, TWO_TEACHER_PROMOTION_AMOUNT, TWO_TEACHER_REAL_WASH_AMOUNT, TWO_TEACHER_PROMOTION_WASH_AMOUNT,
									LIVE_REAL_AMOUNT, LIVE_PROMOTION_AMOUNT, LIVE_REAL_WASH_AMOUNT, LIVE_PROMOTION_WASH_AMOUNT,
									ECS_CLASS_REAL_AMOUNT, ECS_CLASS_PROMOTION_AMOUNT, ECS_CLASS_REAL_WASH_AMOUNT, ECS_CLASS_PROMOTION_WASH_AMOUNT,
									OTM_CLASS_REAL_AMOUNT, OTM_CLASS_PROMOTION_AMOUNT, OTM_CLASS_REAL_WASH_AMOUNT, OTM_CLASS_PROMOTION_WASH_AMOUNT,
									OTHERS_REAL_AMOUNT, OTHERS_PROMOTION_AMOUNT, OTHERS_REAL_WASH_AMOUNT, OTHERS_PROMOTION_WASH_AMOUNT,
									LECTURE_REAL_AMOUNT, LECTURE_PROMOTION_AMOUNT, LECTURE_REAL_WASH_AMOUNT, LECTURE_PROMOTION_WASH_AMOUNT,
									IS_NORMAL_REAL_AMOUNT, IS_NORMAL_PROMOTION_AMOUNT, IS_NORMAL_HISTORY_WASH_AMOUNT,
									COUNT_DATE, MAPPING_DATE, EVIDENCE_AUDIT_STATUS,LIVE_NEW_REAL_AMOUNT,LIVE_NEW_REAL_DIVIDE,LIVE_RENEW_REAL_AMOUNT,LIVE_RENEW_REAL_DIVIDE)
			SELECT CONCAT(CAMPUS_ID, '_', COUNT_DATE) ID, GROUP_ID, BRENCH_ID, CAMPUS_ID, SUM(ONE_ON_ONE_REAL_AMOUNT), SUM(ONE_ON_ONE_PROMOTION_AMOUNT), SUM(ONE_ON_ONE_REAL_WASH_AMOUNT), SUM(ONE_ON_ONE_PROMOTION_WASH_AMOUNT),
						 SUM(SMALL_CLASS_REAL_AMOUNT), SUM(SMALL_CLASS_PROMOTION_AMOUNT), SUM(SMALL_CLASS_REAL_WASH_AMOUNT), SUM(SMALL_CLASS_PROMOTION_WASH_AMOUNT),
						 SUM(TWO_TEACHER_REAL_AMOUNT), SUM(TWO_TEACHER_PROMOTION_AMOUNT), SUM(TWO_TEACHER_REAL_WASH_AMOUNT), SUM(TWO_TEACHER_PROMOTION_WASH_AMOUNT),
						 SUM(LIVE_REAL_AMOUNT), SUM(LIVE_PROMOTION_AMOUNT), SUM(LIVE_REAL_WASH_AMOUNT), SUM(LIVE_PROMOTION_WASH_AMOUNT),
						 SUM(ECS_CLASS_REAL_AMOUNT), SUM(ECS_CLASS_PROMOTION_AMOUNT), SUM(ECS_CLASS_REAL_WASH_AMOUNT), SUM(ECS_CLASS_PROMOTION_WASH_AMOUNT),
						 SUM(OTM_CLASS_REAL_AMOUNT), SUM(OTM_CLASS_PROMOTION_AMOUNT), SUM(OTM_CLASS_REAL_WASH_AMOUNT), SUM(OTM_CLASS_PROMOTION_WASH_AMOUNT),
					   SUM(OTHERS_REAL_AMOUNT), SUM(OTHERS_PROMOTION_AMOUNT), SUM(OTHERS_REAL_WASH_AMOUNT), SUM(OTHERS_PROMOTION_WASH_AMOUNT),
						 SUM(LECTURE_REAL_AMOUNT), SUM(LECTURE_PROMOTION_AMOUNT), SUM(LECTURE_REAL_WASH_AMOUNT), SUM(LECTURE_PROMOTION_WASH_AMOUNT), 
             SUM(IS_NORMAL_REAL_AMOUNT), SUM(IS_NORMAL_PROMOTION_AMOUNT), SUM(IS_NORMAL_HISTORY_WASH_AMOUNT),
						 in_count_date, SUBSTRING(MAPPING_DATE FROM 1 FOR 10), 'NOT_AUDIT', SUM(LIVE_NEW_REAL_AMOUNT), SUM(LIVE_NEW_REAL_DIVIDE), SUM(LIVE_RENEW_REAL_AMOUNT), SUM(LIVE_RENEW_REAL_DIVIDE)
        FROM ods_month_income_student
        WHERE
            1=1
            AND COUNT_DATE = in_count_date
        GROUP BY GROUP_ID, BRENCH_ID, CAMPUS_ID;
END
$$

DROP PROCEDURE IF EXISTS `proc_ods_month_income_campus_by_campus`;
$$

CREATE PROCEDURE `proc_ods_month_income_campus_by_campus`(
IN in_count_date VARCHAR(10),IN in_campus_id VARCHAR(32))
    SQL SECURITY INVOKER
BEGIN
			DELETE FROM ods_month_income_campus WHERE COUNT_DATE = in_count_date AND CAMPUS_ID = in_campus_id;
				INSERT INTO ods_month_income_campus(ID, GROUP_ID, BRENCH_ID, CAMPUS_ID, ONE_ON_ONE_REAL_AMOUNT, ONE_ON_ONE_PROMOTION_AMOUNT, ONE_ON_ONE_REAL_WASH_AMOUNT, ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
									SMALL_CLASS_REAL_AMOUNT, SMALL_CLASS_PROMOTION_AMOUNT, SMALL_CLASS_REAL_WASH_AMOUNT, SMALL_CLASS_PROMOTION_WASH_AMOUNT,
									TWO_TEACHER_REAL_AMOUNT, TWO_TEACHER_PROMOTION_AMOUNT, TWO_TEACHER_REAL_WASH_AMOUNT, TWO_TEACHER_PROMOTION_WASH_AMOUNT,
									LIVE_REAL_AMOUNT, LIVE_PROMOTION_AMOUNT, LIVE_REAL_WASH_AMOUNT, LIVE_PROMOTION_WASH_AMOUNT,
									ECS_CLASS_REAL_AMOUNT, ECS_CLASS_PROMOTION_AMOUNT, ECS_CLASS_REAL_WASH_AMOUNT, ECS_CLASS_PROMOTION_WASH_AMOUNT,
									OTM_CLASS_REAL_AMOUNT, OTM_CLASS_PROMOTION_AMOUNT, OTM_CLASS_REAL_WASH_AMOUNT, OTM_CLASS_PROMOTION_WASH_AMOUNT,
									OTHERS_REAL_AMOUNT, OTHERS_PROMOTION_AMOUNT, OTHERS_REAL_WASH_AMOUNT, OTHERS_PROMOTION_WASH_AMOUNT,
									LECTURE_REAL_AMOUNT, LECTURE_PROMOTION_AMOUNT, LECTURE_REAL_WASH_AMOUNT, LECTURE_PROMOTION_WASH_AMOUNT,
									IS_NORMAL_REAL_AMOUNT, IS_NORMAL_PROMOTION_AMOUNT, IS_NORMAL_HISTORY_WASH_AMOUNT,
									COUNT_DATE, MAPPING_DATE, EVIDENCE_AUDIT_STATUS,LIVE_NEW_REAL_AMOUNT,LIVE_NEW_REAL_DIVIDE,LIVE_RENEW_REAL_AMOUNT,LIVE_RENEW_REAL_DIVIDE)
			SELECT CONCAT(CAMPUS_ID, '_', COUNT_DATE) ID, GROUP_ID, BRENCH_ID, CAMPUS_ID, SUM(ONE_ON_ONE_REAL_AMOUNT), SUM(ONE_ON_ONE_PROMOTION_AMOUNT), SUM(ONE_ON_ONE_REAL_WASH_AMOUNT), SUM(ONE_ON_ONE_PROMOTION_WASH_AMOUNT),
						 SUM(SMALL_CLASS_REAL_AMOUNT), SUM(SMALL_CLASS_PROMOTION_AMOUNT), SUM(SMALL_CLASS_REAL_WASH_AMOUNT), SUM(SMALL_CLASS_PROMOTION_WASH_AMOUNT),
						 SUM(TWO_TEACHER_REAL_AMOUNT), SUM(TWO_TEACHER_PROMOTION_AMOUNT), SUM(TWO_TEACHER_REAL_WASH_AMOUNT), SUM(TWO_TEACHER_PROMOTION_WASH_AMOUNT),
						 SUM(LIVE_REAL_AMOUNT), SUM(LIVE_PROMOTION_AMOUNT), SUM(LIVE_REAL_WASH_AMOUNT), SUM(LIVE_PROMOTION_WASH_AMOUNT),
						 SUM(ECS_CLASS_REAL_AMOUNT), SUM(ECS_CLASS_PROMOTION_AMOUNT), SUM(ECS_CLASS_REAL_WASH_AMOUNT), SUM(ECS_CLASS_PROMOTION_WASH_AMOUNT),
						 SUM(OTM_CLASS_REAL_AMOUNT), SUM(OTM_CLASS_PROMOTION_AMOUNT), SUM(OTM_CLASS_REAL_WASH_AMOUNT), SUM(OTM_CLASS_PROMOTION_WASH_AMOUNT),
					   SUM(OTHERS_REAL_AMOUNT), SUM(OTHERS_PROMOTION_AMOUNT), SUM(OTHERS_REAL_WASH_AMOUNT), SUM(OTHERS_PROMOTION_WASH_AMOUNT),
						 SUM(LECTURE_REAL_AMOUNT), SUM(LECTURE_PROMOTION_AMOUNT), SUM(LECTURE_REAL_WASH_AMOUNT), SUM(LECTURE_PROMOTION_WASH_AMOUNT), 
             SUM(IS_NORMAL_REAL_AMOUNT), SUM(IS_NORMAL_PROMOTION_AMOUNT), SUM(IS_NORMAL_HISTORY_WASH_AMOUNT),
						 in_count_date, SUBSTRING(MAPPING_DATE FROM 1 FOR 10), 'NOT_AUDIT', SUM(LIVE_NEW_REAL_AMOUNT), SUM(LIVE_NEW_REAL_DIVIDE), SUM(LIVE_RENEW_REAL_AMOUNT), SUM(LIVE_RENEW_REAL_DIVIDE)
        FROM ods_month_income_student
        WHERE
            1=1
            AND COUNT_DATE = in_count_date
						AND CAMPUS_ID = in_campus_id
        GROUP BY GROUP_ID, BRENCH_ID, CAMPUS_ID;
END
$$

DROP PROCEDURE IF EXISTS `proc_ods_month_income_student`;
$$

CREATE PROCEDURE `proc_ods_month_income_student`(
IN in_start_date VARCHAR(10),IN in_end_date VARCHAR(10),IN in_mapping_date VARCHAR(20))
    SQL SECURITY INVOKER
BEGIN
            DELETE FROM ods_month_income_student WHERE COUNT_DATE = in_end_date;
            INSERT INTO ods_month_income_student(GROUP_ID, BRENCH_ID, CAMPUS_ID, STUDENT_ID, ONE_ON_ONE_REAL_AMOUNT, ONE_ON_ONE_PROMOTION_AMOUNT, ONE_ON_ONE_REAL_WASH_AMOUNT, ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
                                    SMALL_CLASS_REAL_AMOUNT, SMALL_CLASS_PROMOTION_AMOUNT, SMALL_CLASS_REAL_WASH_AMOUNT, SMALL_CLASS_PROMOTION_WASH_AMOUNT,
                                    TWO_TEACHER_REAL_AMOUNT, TWO_TEACHER_PROMOTION_AMOUNT, TWO_TEACHER_REAL_WASH_AMOUNT, TWO_TEACHER_PROMOTION_WASH_AMOUNT,
                                    LIVE_NEW_REAL_AMOUNT,LIVE_NEW_REAL_DIVIDE,LIVE_RENEW_REAL_AMOUNT,LIVE_RENEW_REAL_DIVIDE,
                                    ECS_CLASS_REAL_AMOUNT, ECS_CLASS_PROMOTION_AMOUNT, ECS_CLASS_REAL_WASH_AMOUNT, ECS_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTM_CLASS_REAL_AMOUNT, OTM_CLASS_PROMOTION_AMOUNT, OTM_CLASS_REAL_WASH_AMOUNT, OTM_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTHERS_REAL_AMOUNT, OTHERS_PROMOTION_AMOUNT, OTHERS_REAL_WASH_AMOUNT, OTHERS_PROMOTION_WASH_AMOUNT,
                                    LECTURE_REAL_AMOUNT, LECTURE_PROMOTION_AMOUNT, LECTURE_REAL_WASH_AMOUNT, LECTURE_PROMOTION_WASH_AMOUNT,
                                    IS_NORMAL_REAL_AMOUNT, IS_NORMAL_PROMOTION_AMOUNT, IS_NORMAL_HISTORY_WASH_AMOUNT,
                                    COUNT_DATE, MAPPING_DATE, EVIDENCE_AUDIT_STATUS)
            SELECT
            org_group.id GROUP_ID, org_brench.id BRENCH_ID, o.id CAMPUS_ID, s.ID AS STUDENT_ID,
            SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= CONCAT(in_mapping_date) THEN -amount ELSE 0 END) AS ONE_ON_ONE_REAL_AMOUNT,
            SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= CONCAT(in_mapping_date) THEN -amount ELSE 0 END) AS ONE_ON_ONE_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount 
                                ELSE 0 END) AS ONE_ON_ONE_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS SMALL_CLASS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS SMALL_CLASS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS SMALL_CLASS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS SMALL_CLASS_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS TWO_TEACHER_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS TWO_TEACHER_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_PROMOTION_WASH_AMOUNT,

                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='NEW' THEN total_amount ELSE 0 END) AS LIVE_NEW_REAL_AMOUNT,
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='NEW' THEN paid_amount ELSE 0 END) AS LIVE_NEW_REAL_DIVIDE,
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='RENEW' THEN total_amount ELSE 0 END) AS LIVE_RENEW_REAL_AMOUNT,
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='RENEW' THEN paid_amount ELSE 0 END) AS LIVE_RENEW_REAL_DIVIDE,
                        
                        
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS ECS_CLASS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS ECS_CLASS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ECS_CLASS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ECS_CLASS_PROMOTION_WASH_AMOUNT,
                
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTM_CLASS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTM_CLASS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTM_CLASS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTM_CLASS_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTHERS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTHERS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTHERS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTHERS_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS LECTURE_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS LECTURE_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH'  AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS LECTURE_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS LECTURE_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS IS_NORMAL_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS IS_NORMAL_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'WASH' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS IS_NORMAL_HISTORY_WASH_AMOUNT,
                        in_end_date, in_mapping_date, 'NOT_AUDIT'
	FROM (
        SELECT  aa.CHARGE_TYPE,aa.CHARGE_PAY_TYPE,aa.TRANSACTION_TIME,aa.PAY_TIME,aa.PAY_TYPE,aa.PRODUCT_TYPE,aa.amount,aa.BL_CAMPUS_ID,aa.STUDENT_ID
		,''  liveContractType ,0 total_amount,0  paid_amount    
        FROM account_charge_records aa
		LEFT JOIN income_mapping_date imd 
                                ON aa.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
        WHERE
            1=1
            AND aa.amount > 0  
            AND (
                CHARGE_TYPE = 'NORMAL' 
                OR CHARGE_TYPE = 'IS_NORMAL_INCOME'
            )
                        AND ((aa.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND aa.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59')
                        AND aa.PAY_TIME <= in_mapping_date AND aa.CHARGE_PAY_TYPE = 'CHARGE') 
                      OR (aa.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND aa.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                AND aa.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND aa.PAY_TIME <= in_mapping_date  AND aa.CHARGE_PAY_TYPE = 'WASH')
                        OR (aa.TRANSACTION_TIME <= CONCAT(in_start_date, ' 00:00:00') AND aa.PAY_TIME > CONCAT(in_start_date, ' 00:00:00') 
                                AND aa.PAY_TIME < CONCAT(in_end_date, ' 23:59:59')))
                        AND imd.ACCOUNT_CHARGE_RECORD_ID IS NULL AND aa.PRODUCT_TYPE <> 'LIVE'  
        UNION ALL
             SELECT '' CHARGE_TYPE,'' CHARGE_PAY_TYPE,CONCAT(payment_date, ' 08:00:00') TRANSACTION_TIME,payment_date PAY_TIME,'' PAY_TYPE,'LIVE' PRODUCT_TYPE,0 amount,order_campusId  BL_CAMPUS_ID,student_id  STUDENT_ID 
             , contact_type  liveContractType,total_amount,paid_amount
             FROM live_payment_record
             WHERE finance_type = 'REVENUE'
             AND (payment_date >= in_start_date AND payment_date <= in_end_date)
             ) acr 
        INNER JOIN
            organization o 
                ON acr.BL_CAMPUS_ID = o.id
                INNER JOIN organization org_brench
                                ON o.parentID = org_brench.id
                INNER JOIN organization org_group
                                ON org_brench.parentID = org_group.id
        LEFT JOIN
            student s 
                ON acr.STUDENT_ID = s.ID
                
        
        GROUP BY
            o.id,
            s.ID;
                DELETE FROM income_mapping_date WHERE COUNT_MONTH = CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)); 
                INSERT INTO income_mapping_date SELECT ID, CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)), acr.BL_CAMPUS_ID FROM account_charge_records acr
                    LEFT JOIN income_mapping_date imd 
                                ON acr.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
                     WHERE 1=1
            AND acr.amount > 0  
            AND (
                CHARGE_TYPE = 'NORMAL' 
                OR CHARGE_TYPE = 'IS_NORMAL_INCOME'
            )
                        AND ((acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                            AND acr.PAY_TIME <= in_mapping_date AND acr.CHARGE_PAY_TYPE = 'CHARGE') 
                      OR (acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date  AND acr.CHARGE_PAY_TYPE = 'WASH')
                        OR (acr.TRANSACTION_TIME <= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME > CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME < CONCAT(in_end_date, ' 23:59:59')))
                        AND imd.ACCOUNT_CHARGE_RECORD_ID IS NULL;
END
$$


DROP PROCEDURE IF EXISTS `proc_ods_month_income_student_by_campus`;
$$
CREATE PROCEDURE `proc_ods_month_income_student_by_campus`(
IN in_start_date VARCHAR(10),IN in_end_date VARCHAR(10),IN in_mapping_date VARCHAR(20),IN in_campus_id VARCHAR(32))
    SQL SECURITY INVOKER
BEGIN
            DELETE FROM ods_month_income_student WHERE COUNT_DATE = in_end_date AND CAMPUS_ID = in_campus_id;
            INSERT INTO ods_month_income_student(GROUP_ID, BRENCH_ID, CAMPUS_ID, STUDENT_ID, ONE_ON_ONE_REAL_AMOUNT, ONE_ON_ONE_PROMOTION_AMOUNT, ONE_ON_ONE_REAL_WASH_AMOUNT, ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
                                    SMALL_CLASS_REAL_AMOUNT, SMALL_CLASS_PROMOTION_AMOUNT, SMALL_CLASS_REAL_WASH_AMOUNT, SMALL_CLASS_PROMOTION_WASH_AMOUNT,
                                    TWO_TEACHER_REAL_AMOUNT, TWO_TEACHER_PROMOTION_AMOUNT, TWO_TEACHER_REAL_WASH_AMOUNT, TWO_TEACHER_PROMOTION_WASH_AMOUNT,
                                    LIVE_NEW_REAL_AMOUNT,LIVE_NEW_REAL_DIVIDE,LIVE_RENEW_REAL_AMOUNT,LIVE_RENEW_REAL_DIVIDE,
                                    ECS_CLASS_REAL_AMOUNT, ECS_CLASS_PROMOTION_AMOUNT, ECS_CLASS_REAL_WASH_AMOUNT, ECS_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTM_CLASS_REAL_AMOUNT, OTM_CLASS_PROMOTION_AMOUNT, OTM_CLASS_REAL_WASH_AMOUNT, OTM_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTHERS_REAL_AMOUNT, OTHERS_PROMOTION_AMOUNT, OTHERS_REAL_WASH_AMOUNT, OTHERS_PROMOTION_WASH_AMOUNT,
                                    LECTURE_REAL_AMOUNT, LECTURE_PROMOTION_AMOUNT, LECTURE_REAL_WASH_AMOUNT, LECTURE_PROMOTION_WASH_AMOUNT,
                                    IS_NORMAL_REAL_AMOUNT, IS_NORMAL_PROMOTION_AMOUNT, IS_NORMAL_HISTORY_WASH_AMOUNT,
                                    COUNT_DATE, MAPPING_DATE, EVIDENCE_AUDIT_STATUS)
            SELECT
            org_group.id GROUP_ID, org_brench.id BRENCH_ID, o.id CAMPUS_ID, s.ID AS STUDENT_ID,
            SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= CONCAT(in_mapping_date) THEN -amount ELSE 0 END) AS ONE_ON_ONE_REAL_AMOUNT,
            SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= CONCAT(in_mapping_date) THEN -amount ELSE 0 END) AS ONE_ON_ONE_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount 
                                ELSE 0 END) AS ONE_ON_ONE_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS SMALL_CLASS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS SMALL_CLASS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS SMALL_CLASS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS SMALL_CLASS_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS TWO_TEACHER_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS TWO_TEACHER_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_PROMOTION_WASH_AMOUNT,
                       
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='NEW' THEN total_amount ELSE 0 END) AS LIVE_NEW_REAL_AMOUNT,
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='NEW' THEN paid_amount ELSE 0 END) AS LIVE_NEW_REAL_DIVIDE,
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='RENEW' THEN total_amount ELSE 0 END) AS LIVE_RENEW_REAL_AMOUNT,
                        SUM(CASE WHEN PRODUCT_TYPE='LIVE' AND liveContractType ='RENEW' THEN paid_amount ELSE 0 END) AS LIVE_RENEW_REAL_DIVIDE,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS ECS_CLASS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS ECS_CLASS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ECS_CLASS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ECS_CLASS_PROMOTION_WASH_AMOUNT,
                
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTM_CLASS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTM_CLASS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTM_CLASS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTM_CLASS_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTHERS_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS OTHERS_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTHERS_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTHERS_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS LECTURE_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS LECTURE_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH'  AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS LECTURE_REAL_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS LECTURE_PROMOTION_WASH_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS IS_NORMAL_REAL_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS IS_NORMAL_PROMOTION_AMOUNT,
                        SUM(CASE WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'WASH' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = LAST_DAY(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), LAST_DAY(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS IS_NORMAL_HISTORY_WASH_AMOUNT,
                        in_end_date, in_mapping_date, 'NOT_AUDIT'
        FROM (
        SELECT  aa.CHARGE_TYPE,aa.CHARGE_PAY_TYPE,aa.TRANSACTION_TIME,aa.PAY_TIME,aa.PAY_TYPE,aa.PRODUCT_TYPE,aa.amount,aa.BL_CAMPUS_ID,aa.STUDENT_ID
		,''  liveContractType ,0 total_amount,0  paid_amount    
        FROM account_charge_records aa
		LEFT JOIN income_mapping_date imd 
                                ON aa.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
        WHERE
            1=1
            AND aa.amount > 0  
            AND aa.BL_CAMPUS_ID = in_campus_id
            AND (
                CHARGE_TYPE = 'NORMAL' 
                OR CHARGE_TYPE = 'IS_NORMAL_INCOME'
            )
                        AND ((aa.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND aa.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59')
                        AND aa.PAY_TIME <= in_mapping_date AND aa.CHARGE_PAY_TYPE = 'CHARGE') 
                      OR (aa.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND aa.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                AND aa.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND aa.PAY_TIME <= in_mapping_date  AND aa.CHARGE_PAY_TYPE = 'WASH')
                        OR (aa.TRANSACTION_TIME <= CONCAT(in_start_date, ' 00:00:00') AND aa.PAY_TIME > CONCAT(in_start_date, ' 00:00:00') 
                                AND aa.PAY_TIME < CONCAT(in_end_date, ' 23:59:59')))
                        AND imd.ACCOUNT_CHARGE_RECORD_ID IS NULL AND aa.PRODUCT_TYPE <> 'LIVE'  
        UNION ALL
             SELECT '' CHARGE_TYPE,'' CHARGE_PAY_TYPE,CONCAT(payment_date, ' 08:00:00') TRANSACTION_TIME,payment_date PAY_TIME,'' PAY_TYPE,'LIVE' PRODUCT_TYPE,0 amount,order_campusId  BL_CAMPUS_ID,student_id  STUDENT_ID 
             , contact_type  liveContractType,total_amount,paid_amount
             FROM live_payment_record
             WHERE order_campusId = in_campus_id AND finance_type = 'REVENUE'
             AND (payment_date >= in_start_date AND payment_date <= in_end_date)
             ) acr 
        INNER JOIN
            organization o 
                ON acr.BL_CAMPUS_ID = o.id
                INNER JOIN organization org_brench
                                ON o.parentID = org_brench.id
                INNER JOIN organization org_group
                                ON org_brench.parentID = org_group.id
        LEFT JOIN
            student s 
                ON acr.STUDENT_ID = s.ID
                
        
        GROUP BY
            o.id,
            s.ID;               
                DELETE FROM income_mapping_date WHERE COUNT_MONTH = CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)) AND BL_CAMPUS_ID = in_campus_id; 
                INSERT INTO income_mapping_date SELECT ID, CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)), acr.BL_CAMPUS_ID FROM account_charge_records acr
                        LEFT JOIN income_mapping_date imd 
                                ON acr.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
                     WHERE 1=1
            AND acr.amount > 0
                        AND acr.BL_CAMPUS_ID= in_campus_id
            AND (
                acr.CHARGE_TYPE = 'NORMAL' 
                OR acr.CHARGE_TYPE = 'IS_NORMAL_INCOME'
            )
                    AND ((acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59')
                        AND acr.PAY_TIME <= in_mapping_date AND acr.CHARGE_PAY_TYPE = 'CHARGE') 
                      OR (acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date  AND acr.CHARGE_PAY_TYPE = 'WASH')
                        OR (acr.TRANSACTION_TIME <= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME > CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME < CONCAT(in_end_date, ' 23:59:59')))
                        AND imd.ACCOUNT_CHARGE_RECORD_ID IS NULL;
END
$$


--changeset duanmenrun:1940————3--1
--comment 修改现金流凭证
ALTER TABLE ods_month_payment_receipt_main ADD COLUMN live_new_money DECIMAL(10,2) NULL DEFAULT 0 COMMENT '新签直播产品收款';

DROP TABLE IF EXISTS ods_month_payment_receipt_main_student;
CREATE TABLE ods_month_payment_receipt_main_student (
	id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT ,
	group_id VARCHAR (32) COMMENT '集团',
	group_name VARCHAR (100) COMMENT '集团名字',
	branch_id VARCHAR (32) COMMENT '分公司',
	branch_name VARCHAR (32)COMMENT '分公司名字',
	campus_id VARCHAR (32)COMMENT '校区',
	campus_name VARCHAR (32)COMMENT '校区名字',
	student_id VARCHAR (32)COMMENT '学生id',
	new_money DECIMAL(10,2) COMMENT '新签金额',
	re_money DECIMAL(10,2) COMMENT '续费金额',
	all_money DECIMAL(10,2) COMMENT '实收金额',
	wash_money DECIMAL(10,2) COMMENT '冲销金额',
	total_money DECIMAL(10,2) COMMENT '收款小计',
	bonus_money DECIMAL(10,2) COMMENT '业绩金额',
	refund_money DECIMAL(10,2) COMMENT '退费金额',
	special_refund_money DECIMAL(10,2) COMMENT '特殊退费',
	total_refund_money DECIMAL(10,2) COMMENT '总退费',
	refund_bonus_money DECIMAL(10,2) COMMENT '退费业绩',
	receipt_month VARCHAR (32)COMMENT '凭证月份',
	receipt_date VARCHAR (32)COMMENT '凭证日期',
	receipt_status VARCHAR (32)COMMENT '状态',
	total_finace DECIMAL(10,2) COMMENT '现金流合计',
	total_bonus DECIMAL(10,2) COMMENT '本月业绩合计',
	live_new_money DECIMAL(10,2) COMMENT '新签直播产品收款',
	PRIMARY KEY (id),
	INDEX idx_receipt_month_campus_id(receipt_month,campus_id)
)COMMENT='现金流凭证-学生'; 



--changeset duanmenrun:1940——4——1 endDelimiter:\$\$
--comment 修改现金流凭证存储过程

DROP PROCEDURE IF EXISTS `proc_update_ods_month_payment_receipt_main`;

$$
CREATE PROCEDURE `proc_update_ods_month_payment_receipt_main`(IN count_date_month VARCHAR(10),IN incampusId VARCHAR(32))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
  SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
 
 IF EXISTS (SELECT 1 FROM receipt_funds_change_record WHERE receipt_month<count_date_month) THEN 
	UPDATE funds_change_history fun LEFT JOIN receipt_funds_change_record re ON re.funds_change_id = fun.id
    SET fun.receipt_status=0,fun.receipt_date=re.receipt_date
    WHERE fun.receipt_status=1;
    DELETE FROM receipt_funds_change_record WHERE receipt_month<count_date_month;
 END IF;
 
IF incampusId IS NOT NULL AND incampusId<>'' 
THEN
	DELETE FROM receipt_funds_change_record WHERE receipt_month=count_date_month AND bl_campus_id=incampusId;
    DELETE FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND campus_id=incampusId;
    INSERT INTO `ods_month_payment_receipt_main`
    (`id`,
    `group_id`,
    `branch_id`,
    `campus_id`,
    `group_name`,
    `branch_name`,
    `campus_name`,
    `new_money`,
    `re_money`,
    `all_money`,
    `wash_money`,
    `total_money`,
    `bonus_money`,
    `refund_money`,
    `special_refund_money`,
    `total_refund_money`,
    `refund_bonus_money`,
    `receipt_month`,
    `receipt_date`,
    `receipt_status`,
    `total_finace`,
    `total_bonus`,
    live_new_money
    )
    SELECT CONCAT(campusId,REPLACE(count_date_month,'-','')),groupId,branchId,campusId,groupName,branchName,campusName,SUM(newMoney),SUM(reMoney),SUM(allMoney),SUM(washMoney),SUM(totalMoney),SUM(bonusMoney),SUM(refundMoney),SUM(special_return_money),SUM(totalRefundMoney),SUM(refundBonusMoney),
    count_date_month,receiptDate,'NOT_AUDIT',SUM(totalMoney-totalRefundMoney),SUM(bonusMoney-refundBonusMoney),SUM(live_new_money)
     FROM (
    SELECT o3.id groupId,o2.id branchId,o.id campusId,
    o3.name groupName,o2.name branchName,o.name campusName,
    SUM(CASE WHEN c.CONTRACT_TYPE='NEW_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) newMoney,
    SUM(CASE WHEN c.CONTRACT_TYPE='RE_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) reMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) allMoney,
    SUM(CASE WHEN  fun.FUNDS_PAY_TYPE='WASH' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) washMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='WASH' THEN  -fun.TRANSACTION_AMOUNT ELSE 0 END) totalMoney,
    0 bonusMoney,
    0 refundMoney,
    0 AS special_return_money,
    0 totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM funds_change_history fun
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON fun.fund_campus_id =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1)) AND o.id=incampusId AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') 
    AND (fun.AUDIT_STATUS='VALIDATE' OR fun.FUNDS_PAY_TYPE='WASH')  AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
    AND fun.CONTRACT_ID IS NOT NULL GROUP BY o.id
    UNION
    ALL
    SELECT o3.id groupId,o2.id branchId,o.id campusId,
    o3.name groupName,o2.name branchName,o.name campusName,
    0 newMoney,
    0 reMoney,
    0 allMoney,
    0 washMoney,
    0 totalMoney,
    0 bonusMoney,
    SUM(-fun.TRANSACTION_AMOUNT) refundMoney,   
    SUM(IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) AS special_return_money,
    SUM(-fun.TRANSACTION_AMOUNT + IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM student_return_fee srf
    LEFT JOIN funds_change_history fun ON fun.id=srf.FUNDS_CHANGE_ID
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON srf.CAMPUS =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND o.id=incampusId AND fun.CHANNEL ='REFUND_MONEY'  
            AND NOT EXISTS (SELECT 1 FROM contract_product cp WHERE cp.type = 'LIVE' AND cp.contract_id = fun.CONTRACT_ID ) GROUP BY o.id
            
     UNION ALL       
            
     SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
     0 newMoney,
     0 reMoney,
     0 allMoney,
     0 washMoney,
     SUM(d.paid_amount) totalMoney,
     SUM(d.campus_achievement) bonusMoney,
     0 refundMoney,
     0 AS special_return_money,
     0 totalRefundMoney,
     0 refundBonusMoney,
     SUM(d.paid_amount) live_new_money
     FROM  live_payment_record d
     LEFT JOIN organization o ON d.order_campusId =o.id
     LEFT JOIN organization o2 ON o2.id=o.parentId
     LEFT JOIN organization o3 ON o3.id=o2.parentId
     WHERE  o.id=incampusId AND d.payment_date  LIKE CONCAT(count_date_month,'%') AND d.contact_type='NEW' AND d.finance_type='INCOME'
             GROUP BY o.id) a GROUP BY a.campusId  ORDER BY a.branchName;
    
    UPDATE ods_month_payment_receipt_main om LEFT JOIN (SELECT receipt_main_id,IFNULL(SUM(amount),0) amount FROM ods_payment_receipt_modify WHERE receipt_main_id=CONCAT(incampusId,REPLACE(count_date_month,'-','')) GROUP BY receipt_main_id) s ON s.receipt_main_id=om.id
    SET om.modify_money=s.amount,om.after_modify_money=om.total_finace+s.amount
    WHERE om.campus_id=incampusId AND om.receipt_month=count_date_month AND EXISTS(SELECT 1 FROM ods_payment_receipt_modify WHERE receipt_main_id=CONCAT(incampusId,REPLACE(count_date_month,'-','')) );
    
    UPDATE ods_month_payment_receipt_main om
        LEFT JOIN
        (SELECT 
            cb.ORGANIZATIONID,
                SUM(CASE
                    WHEN fun.CHANNEL IN ('CASH' , 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN cb.amount
                    ELSE 0
                END) bonusMoney,
                SUM(CASE
                    WHEN fun.CHANNEL = 'REFUND_MONEY' THEN cb.amount
                    ELSE 0
                END) refundBonusMoney
        FROM
            income_distribution cb
        LEFT JOIN funds_change_history fun ON cb.FUNDS_CHANGE_ID = fun.ID
        WHERE
            cb.ORGANIZATIONID IS NOT NULL  AND cb.base_bonus_type='CAMPUS'
            
            AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
                AND (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1))
        GROUP BY cb.ORGANIZATIONID) s ON s.ORGANIZATIONID = om.campus_id 
    SET 
        om.bonus_money = IFNULL(s.bonusMoney, 0) + om.bonus_money,
        om.refund_bonus_money = IFNULL(s.refundBonusMoney, 0),
        om.total_bonus = IFNULL(s.bonusMoney - s.refundBonusMoney, 0) + om.total_bonus
    WHERE om.campus_id=incampusId AND om.receipt_month=count_date_month;
ELSE
	DELETE FROM receipt_funds_change_record WHERE receipt_month=count_date_month;
    DELETE FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month;
    INSERT INTO `ods_month_payment_receipt_main`
    (`id`,
    `group_id`,
    `branch_id`,
    `campus_id`,
    `group_name`,
    `branch_name`,
    `campus_name`,
    `new_money`,
    `re_money`,
    `all_money`,
    `wash_money`,
    `total_money`,
    `bonus_money`,
    `refund_money`,
    `special_refund_money`,
    `total_refund_money`,
    `refund_bonus_money`,
    `receipt_month`,
    `receipt_date`,
    `receipt_status`,
    `total_finace`, 
    `total_bonus`,
    live_new_money)
    SELECT CONCAT(campusId,REPLACE(count_date_month,'-','')),groupId,branchId,campusId,groupName,branchName,campusName,SUM(newMoney),SUM(reMoney),SUM(allMoney),SUM(washMoney),SUM(totalMoney),SUM(bonusMoney),SUM(refundMoney),SUM(special_return_money),SUM(totalRefundMoney),SUM(refundBonusMoney),
    count_date_month,receiptDate,'NOT_AUDIT',SUM(totalMoney-totalRefundMoney),SUM(bonusMoney-refundBonusMoney),SUM(live_new_money)
     FROM (
    SELECT o3.id groupId,o2.id branchId,o.id campusId,
    o3.name groupName,o2.name branchName,o.name campusName,
    SUM(CASE WHEN c.CONTRACT_TYPE='NEW_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) newMoney,
    SUM(CASE WHEN c.CONTRACT_TYPE='RE_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) reMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) allMoney,
    SUM(CASE WHEN  fun.FUNDS_PAY_TYPE='WASH' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) washMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='WASH' THEN  -fun.TRANSACTION_AMOUNT ELSE 0 END) totalMoney,
    0 bonusMoney,
    0 refundMoney,
    0 AS special_return_money,
    0 totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM funds_change_history fun
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON fun.fund_campus_id =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1)) AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') 
    AND (fun.AUDIT_STATUS='VALIDATE' OR fun.FUNDS_PAY_TYPE='WASH')  AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
    AND fun.CONTRACT_ID IS NOT NULL GROUP BY o.id
    UNION
    ALL
    SELECT o3.id groupId,o2.id branchId,o.id campusId,
    o3.name groupName,o2.name branchName,o.name campusName,
    0 newMoney,
    0 reMoney,
    0 allMoney,
    0 washMoney,
    0 totalMoney,
    0 bonusMoney,
    SUM(-fun.TRANSACTION_AMOUNT) refundMoney,   
    SUM(IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) AS special_return_money,
    SUM(-fun.TRANSACTION_AMOUNT + IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM student_return_fee srf
    LEFT JOIN funds_change_history fun ON fun.id=srf.FUNDS_CHANGE_ID
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON srf.CAMPUS =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND fun.CHANNEL ='REFUND_MONEY'  
	 AND NOT EXISTS (SELECT 1 FROM contract_product cp WHERE cp.type = 'LIVE' AND cp.contract_id = fun.CONTRACT_ID ) GROUP BY o.id
	UNION ALL       
            
     SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
     0 newMoney,
     0 reMoney,
     0 allMoney,
     0 washMoney,
     SUM(d.paid_amount) totalMoney,
     SUM(d.campus_achievement) bonusMoney,
     0 refundMoney,
     0 AS special_return_money,
     0 totalRefundMoney,
     0 refundBonusMoney,
     SUM(d.paid_amount) live_new_money
     FROM  live_payment_record d
     LEFT JOIN organization o ON d.order_campusId =o.id
     LEFT JOIN organization o2 ON o2.id=o.parentId
     LEFT JOIN organization o3 ON o3.id=o2.parentId
     WHERE  d.payment_date  LIKE CONCAT(count_date_month,'%') AND d.contact_type='NEW' AND d.finance_type='INCOME'
             GROUP BY o.id ) a GROUP BY a.campusId  ORDER BY a.branchName;
    UPDATE ods_month_payment_receipt_main om
        LEFT JOIN
        (SELECT 
            cb.ORGANIZATIONID,
                SUM(CASE
                    WHEN fun.CHANNEL IN ('CASH' , 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN cb.amount
                    ELSE 0
                END) bonusMoney,
                SUM(CASE
                    WHEN fun.CHANNEL = 'REFUND_MONEY' THEN cb.amount
                    ELSE 0
                END) refundBonusMoney
        FROM
            income_distribution cb
        LEFT JOIN funds_change_history fun ON cb.FUNDS_CHANGE_ID = fun.ID
        WHERE
            cb.ORGANIZATIONID IS NOT NULL  AND cb.base_bonus_type='CAMPUS'
            
            AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
			AND (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1))
        GROUP BY cb.ORGANIZATIONID) s ON s.ORGANIZATIONID = om.campus_id 
    SET 
        om.bonus_money = IFNULL(s.bonusMoney, 0) + om.bonus_money,
        om.refund_bonus_money = IFNULL(s.refundBonusMoney, 0),
        om.total_bonus = IFNULL(s.bonusMoney - s.refundBonusMoney, 0) + om.total_bonus
    WHERE om.receipt_month=count_date_month AND s.ORGANIZATIONID IS NOT NULL;
END IF;
END
$$


DROP PROCEDURE IF EXISTS `proc_update_ods_month_payment_receipt_main_flush`;
$$
CREATE PROCEDURE `proc_update_ods_month_payment_receipt_main_flush`(IN count_date_month VARCHAR(10))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
 SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
 
	
    DELETE FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status='NOT_AUDIT';
    INSERT INTO `ods_month_payment_receipt_main`
    (`id`,
    `group_id`,
    `branch_id`,
    `campus_id`,
    `group_name`,
    `branch_name`,
    `campus_name`,
    `new_money`,
    `re_money`,
    `all_money`,
    `wash_money`,
    `total_money`,
    `bonus_money`,
    `refund_money`,
    `special_refund_money`,
    `total_refund_money`,
    `refund_bonus_money`,
    `receipt_month`,
    `receipt_date`,
    `receipt_status`,
    `total_finace`,
    `total_bonus`,
    live_new_money)
    SELECT CONCAT(campusId,REPLACE(count_date_month,'-','')),groupId,branchId,campusId,groupName,branchName,campusName,SUM(newMoney),SUM(reMoney),SUM(allMoney),SUM(washMoney),SUM(totalMoney),SUM(bonusMoney),SUM(refundMoney),SUM(special_return_money),SUM(totalRefundMoney),SUM(refundBonusMoney),
    count_date_month,receiptDate,'NOT_AUDIT',SUM(totalMoney-totalRefundMoney),SUM(bonusMoney-refundBonusMoney),SUM(live_new_money)
     FROM (
    SELECT o3.id groupId,o2.id branchId,o.id campusId,
    o3.name groupName,o2.name branchName,o.name campusName,
    SUM(CASE WHEN c.CONTRACT_TYPE='NEW_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) newMoney,
    SUM(CASE WHEN c.CONTRACT_TYPE='RE_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) reMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) allMoney,
    SUM(CASE WHEN  fun.FUNDS_PAY_TYPE='WASH' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) washMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='WASH' THEN  -fun.TRANSACTION_AMOUNT ELSE 0 END) totalMoney,
    0 bonusMoney,
    0 refundMoney,
    0 AS special_return_money,
    0 totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM funds_change_history fun
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON fun.fund_campus_id =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1)) 
    AND o.id NOT IN(SELECT campus_id FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status<>'NOT_AUDIT') 
    AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') 
    AND (fun.AUDIT_STATUS='VALIDATE' OR fun.FUNDS_PAY_TYPE='WASH')  AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
    AND fun.CONTRACT_ID IS NOT NULL GROUP BY o.id
    UNION
    ALL
    SELECT o3.id groupId,o2.id branchId,o.id campusId,
    o3.name groupName,o2.name branchName,o.name campusName,
    0 newMoney,
    0 reMoney,
    0 allMoney,
    0 washMoney,
    0 totalMoney,
    0 bonusMoney,
    SUM(-fun.TRANSACTION_AMOUNT) refundMoney,   
    SUM(IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) AS special_return_money,
    SUM(-fun.TRANSACTION_AMOUNT + IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM student_return_fee srf
    LEFT JOIN funds_change_history fun ON fun.id=srf.FUNDS_CHANGE_ID
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON srf.CAMPUS =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND o.id NOT IN(SELECT campus_id FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status<>'NOT_AUDIT') 
		AND fun.CHANNEL ='REFUND_MONEY'  
		 AND NOT EXISTS (SELECT 1 FROM contract_product cp WHERE cp.type = 'LIVE' AND cp.contract_id = fun.CONTRACT_ID )
        GROUP BY o.id
     
     UNION ALL       
            
     SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
     0 newMoney,
     0 reMoney,
     0 allMoney,
     0 washMoney,
     SUM(d.paid_amount) totalMoney,
     SUM(d.campus_achievement) bonusMoney,
     0 refundMoney,
     0 AS special_return_money,
     0 totalRefundMoney,
     0 refundBonusMoney,
     SUM(d.paid_amount) live_new_money
     FROM  live_payment_record d
     LEFT JOIN organization o ON d.order_campusId =o.id
     LEFT JOIN organization o2 ON o2.id=o.parentId
     LEFT JOIN organization o3 ON o3.id=o2.parentId
     WHERE  d.payment_date  LIKE CONCAT(count_date_month,'%') AND d.contact_type='NEW' AND d.finance_type='INCOME'
	AND o.id NOT IN(SELECT campus_id FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status<>'NOT_AUDIT') 
             GROUP BY o.id
        
        ) a GROUP BY a.campusId  ORDER BY a.branchName;
    
    UPDATE ods_month_payment_receipt_main om
        LEFT JOIN
        (SELECT 
            cb.ORGANIZATIONID,
                SUM(CASE
                    WHEN fun.CHANNEL IN ('CASH' , 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN cb.amount
                    ELSE 0
                END) bonusMoney,
                SUM(CASE
                    WHEN fun.CHANNEL = 'REFUND_MONEY' THEN cb.amount
                    ELSE 0
                END) refundBonusMoney
        FROM
            income_distribution cb
        LEFT JOIN funds_change_history fun ON cb.FUNDS_CHANGE_ID = fun.ID
        WHERE
            cb.ORGANIZATIONID IS NOT NULL  AND cb.base_bonus_type='CAMPUS'
            
            AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
                AND (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1))
        GROUP BY cb.ORGANIZATIONID) s ON s.ORGANIZATIONID = om.campus_id 
    SET 
        om.bonus_money = IFNULL(s.bonusMoney, 0) + om.bonus_money,
        om.refund_bonus_money = IFNULL(s.refundBonusMoney, 0),
        om.total_bonus = IFNULL(s.bonusMoney - s.refundBonusMoney, 0) + om.total_bonus
    WHERE om.receipt_month=count_date_month AND om.receipt_status='NOT_AUDIT';
END
$$

--changeset duanmenrun:1940——4——2.1 endDelimiter:\$\$
--comment 新增现金流凭证存储过程

DROP PROCEDURE IF EXISTS `proc_update_ods_month_payment_receipt_main_student`;
$$
CREATE PROCEDURE `proc_update_ods_month_payment_receipt_main_student`(IN count_date_month VARCHAR(10),IN incampusId VARCHAR(32))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
  SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
 
IF incampusId IS NOT NULL AND incampusId<>'' 
THEN
    DELETE FROM ods_month_payment_receipt_main_student WHERE receipt_month=count_date_month AND campus_id=incampusId;
    INSERT INTO `ods_month_payment_receipt_main_student`
    (
    `group_id`,
    `branch_id`,
    `campus_id`,
    student_id,
    `group_name`,
    `branch_name`,
    `campus_name`,
    `new_money`,
    `re_money`,
    `all_money`,
    `wash_money`,
    `total_money`,
    `bonus_money`,
    `refund_money`,
    `special_refund_money`,
    `total_refund_money`,
    `refund_bonus_money`,
    `receipt_month`,
    `receipt_date`,
    `receipt_status`,
    `total_finace`,
    `total_bonus`,
    live_new_money
    )
    SELECT groupId,branchId,campusId,student_id,groupName,branchName,campusName,SUM(newMoney),SUM(reMoney),SUM(allMoney),SUM(washMoney),SUM(totalMoney),SUM(bonusMoney),SUM(refundMoney),SUM(special_return_money),SUM(totalRefundMoney),SUM(refundBonusMoney),
    count_date_month,receiptDate,'NOT_AUDIT',SUM(totalMoney-totalRefundMoney),SUM(bonusMoney-refundBonusMoney),SUM(live_new_money)
     FROM (
    SELECT o3.id groupId,o2.id branchId,o.id campusId,fun.STUDENT_ID student_id,
    o3.name groupName,o2.name branchName,o.name campusName,
    SUM(CASE WHEN c.CONTRACT_TYPE='NEW_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) newMoney,
    SUM(CASE WHEN c.CONTRACT_TYPE='RE_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) reMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) allMoney,
    SUM(CASE WHEN  fun.FUNDS_PAY_TYPE='WASH' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) washMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='WASH' THEN  -fun.TRANSACTION_AMOUNT ELSE 0 END) totalMoney,
    0 bonusMoney,
    0 refundMoney,
    0 AS special_return_money,
    0 totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM funds_change_history fun
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON fun.fund_campus_id =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1)) AND o.id=incampusId AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') 
    AND (fun.AUDIT_STATUS='VALIDATE' OR fun.FUNDS_PAY_TYPE='WASH')  AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
    AND fun.CONTRACT_ID IS NOT NULL GROUP BY o.id,fun.STUDENT_ID
    UNION
    ALL
    SELECT o3.id groupId,o2.id branchId,o.id campusId,fun.STUDENT_ID student_id,
    o3.name groupName,o2.name branchName,o.name campusName,
    0 newMoney,
    0 reMoney,
    0 allMoney,
    0 washMoney,
    0 totalMoney,
    0 bonusMoney,
    SUM(-fun.TRANSACTION_AMOUNT) refundMoney,   
    SUM(IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) AS special_return_money,
    SUM(-fun.TRANSACTION_AMOUNT + IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM student_return_fee srf
    LEFT JOIN funds_change_history fun ON fun.id=srf.FUNDS_CHANGE_ID
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON srf.CAMPUS =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND o.id=incampusId AND fun.CHANNEL ='REFUND_MONEY'  
            AND NOT EXISTS (SELECT 1 FROM contract_product cp WHERE cp.type = 'LIVE' AND cp.contract_id = fun.CONTRACT_ID ) GROUP BY o.id,fun.STUDENT_ID
            
     UNION ALL       
            
     SELECT o3.id groupId,o2.id branchId,o.id campusId,d.student_id student_id,
     o3.name groupName,o2.name branchName,o.name campusName,
     0 newMoney,
     0 reMoney,
     0 allMoney,
     0 washMoney,
     SUM(d.paid_amount) totalMoney,
     SUM(d.campus_achievement) bonusMoney,
     0 refundMoney,
     0 AS special_return_money,
     0 totalRefundMoney,
     0 refundBonusMoney,
     SUM(d.paid_amount) live_new_money
     FROM  live_payment_record d
     LEFT JOIN organization o ON d.order_campusId =o.id
     LEFT JOIN organization o2 ON o2.id=o.parentId
     LEFT JOIN organization o3 ON o3.id=o2.parentId
     WHERE  o.id=incampusId AND d.payment_date  LIKE CONCAT(count_date_month,'%') AND d.contact_type='NEW' AND d.finance_type='INCOME'
             GROUP BY o.id,d.student_id ) a GROUP BY a.campusId,a.student_id  ORDER BY a.branchName;
    
    UPDATE ods_month_payment_receipt_main_student om
        LEFT JOIN
        (SELECT 
            cb.ORGANIZATIONID,fun.STUDENT_ID,
                SUM(CASE
                    WHEN fun.CHANNEL IN ('CASH' , 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN cb.amount
                    ELSE 0
                END) bonusMoney,
                SUM(CASE
                    WHEN fun.CHANNEL = 'REFUND_MONEY' THEN cb.amount
                    ELSE 0
                END) refundBonusMoney
        FROM
            income_distribution cb
        LEFT JOIN funds_change_history fun ON cb.FUNDS_CHANGE_ID = fun.ID
        WHERE
            cb.ORGANIZATIONID IS NOT NULL  AND cb.base_bonus_type='CAMPUS'
            
            AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
                AND (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1))
        GROUP BY cb.ORGANIZATIONID,fun.STUDENT_ID) s ON s.ORGANIZATIONID = om.campus_id AND s.STUDENT_ID = om.student_id
    SET 
        om.bonus_money = IFNULL(s.bonusMoney, 0) + om.bonus_money,
        om.refund_bonus_money = IFNULL(s.refundBonusMoney, 0),
        om.total_bonus = IFNULL(s.bonusMoney - s.refundBonusMoney, 0) + om.total_bonus
    WHERE om.campus_id=incampusId AND om.receipt_month=count_date_month;
ELSE
    DELETE FROM ods_month_payment_receipt_main_student WHERE receipt_month=count_date_month;
    INSERT INTO `ods_month_payment_receipt_main_student`
    (
    `group_id`,
    `branch_id`,
    `campus_id`,
    student_id,
    `group_name`,
    `branch_name`,
    `campus_name`,
    `new_money`,
    `re_money`,
    `all_money`,
    `wash_money`,
    `total_money`,
    `bonus_money`,
    `refund_money`,
    `special_refund_money`,
    `total_refund_money`,
    `refund_bonus_money`,
    `receipt_month`,
    `receipt_date`,
    `receipt_status`,
    `total_finace`, 
    `total_bonus`,
    live_new_money)
    SELECT groupId,branchId,campusId,student_id,groupName,branchName,campusName,SUM(newMoney),SUM(reMoney),SUM(allMoney),SUM(washMoney),SUM(totalMoney),SUM(bonusMoney),SUM(refundMoney),SUM(special_return_money),SUM(totalRefundMoney),SUM(refundBonusMoney),
    count_date_month,receiptDate,'NOT_AUDIT',SUM(totalMoney-totalRefundMoney),SUM(bonusMoney-refundBonusMoney),SUM(live_new_money)
     FROM (
    SELECT o3.id groupId,o2.id branchId,o.id campusId,fun.STUDENT_ID student_id,
    o3.name groupName,o2.name branchName,o.name campusName,
    SUM(CASE WHEN c.CONTRACT_TYPE='NEW_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) newMoney,
    SUM(CASE WHEN c.CONTRACT_TYPE='RE_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) reMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) allMoney,
    SUM(CASE WHEN  fun.FUNDS_PAY_TYPE='WASH' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) washMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='WASH' THEN  -fun.TRANSACTION_AMOUNT ELSE 0 END) totalMoney,
    0 bonusMoney,
    0 refundMoney,
    0 AS special_return_money,
    0 totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM funds_change_history fun
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON fun.fund_campus_id =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1)) AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') 
    AND (fun.AUDIT_STATUS='VALIDATE' OR fun.FUNDS_PAY_TYPE='WASH')  AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
    AND fun.CONTRACT_ID IS NOT NULL GROUP BY o.id,fun.STUDENT_ID
    UNION
    ALL
    SELECT o3.id groupId,o2.id branchId,o.id campusId,fun.STUDENT_ID student_id,
    o3.name groupName,o2.name branchName,o.name campusName,
    0 newMoney,
    0 reMoney,
    0 allMoney,
    0 washMoney,
    0 totalMoney,
    0 bonusMoney,
    SUM(-fun.TRANSACTION_AMOUNT) refundMoney,   
    SUM(IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) AS special_return_money,
    SUM(-fun.TRANSACTION_AMOUNT + IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM student_return_fee srf
    LEFT JOIN funds_change_history fun ON fun.id=srf.FUNDS_CHANGE_ID
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON srf.CAMPUS =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND fun.CHANNEL ='REFUND_MONEY'  
	 AND NOT EXISTS (SELECT 1 FROM contract_product cp WHERE cp.type = 'LIVE' AND cp.contract_id = fun.CONTRACT_ID ) GROUP BY o.id,fun.STUDENT_ID 
	UNION ALL       
            
     SELECT o3.id groupId,o2.id branchId,o.id campusId,d.student_id student_id,
     o3.name groupName,o2.name branchName,o.name campusName,
     0 newMoney,
     0 reMoney,
     0 allMoney,
     0 washMoney,
     SUM(d.paid_amount) totalMoney,
     SUM(d.campus_achievement) bonusMoney,
     0 refundMoney,
     0 AS special_return_money,
     0 totalRefundMoney,
     0 refundBonusMoney,
     SUM(d.paid_amount) live_new_money
     FROM  live_payment_record d
     LEFT JOIN organization o ON d.order_campusId =o.id
     LEFT JOIN organization o2 ON o2.id=o.parentId
     LEFT JOIN organization o3 ON o3.id=o2.parentId
     WHERE  d.payment_date  LIKE CONCAT(count_date_month,'%') AND d.contact_type='NEW' AND d.finance_type='INCOME'
             GROUP BY o.id,d.student_id ) a GROUP BY a.campusId,a.student_id  ORDER BY a.branchName;
    UPDATE ods_month_payment_receipt_main_student om
        LEFT JOIN
        (SELECT 
            cb.ORGANIZATIONID,fun.STUDENT_ID,
                SUM(CASE
                    WHEN fun.CHANNEL IN ('CASH' , 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN cb.amount
                    ELSE 0
                END) bonusMoney,
                SUM(CASE
                    WHEN fun.CHANNEL = 'REFUND_MONEY' THEN cb.amount
                    ELSE 0
                END) refundBonusMoney
        FROM
            income_distribution cb
        LEFT JOIN funds_change_history fun ON cb.FUNDS_CHANGE_ID = fun.ID
        WHERE
            cb.ORGANIZATIONID IS NOT NULL  AND cb.base_bonus_type='CAMPUS'
            
            AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
			AND (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1))
        GROUP BY cb.ORGANIZATIONID,fun.STUDENT_ID) s ON s.ORGANIZATIONID = om.campus_id AND s.STUDENT_ID = om.student_id
    SET 
        om.bonus_money = IFNULL(s.bonusMoney, 0) + om.bonus_money,
        om.refund_bonus_money = IFNULL(s.refundBonusMoney, 0),
        om.total_bonus = IFNULL(s.bonusMoney - s.refundBonusMoney, 0) + om.total_bonus
    WHERE om.receipt_month=count_date_month AND s.ORGANIZATIONID IS NOT NULL;
END IF;
END
$$

DROP PROCEDURE IF EXISTS `proc_update_ods_month_payment_receipt_main_student_flush`;
$$
CREATE PROCEDURE `proc_update_ods_month_payment_receipt_main_student_flush`(IN count_date_month VARCHAR(10))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
 SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
 
	
    DELETE FROM ods_month_payment_receipt_main_student WHERE receipt_month=count_date_month AND receipt_status='NOT_AUDIT';
    INSERT INTO `ods_month_payment_receipt_main_student`
    (
    `group_id`,
    `branch_id`,
    `campus_id`,
    student_id,
    `group_name`,
    `branch_name`,
    `campus_name`,
    `new_money`,
    `re_money`,
    `all_money`,
    `wash_money`,
    `total_money`,
    `bonus_money`,
    `refund_money`,
    `special_refund_money`,
    `total_refund_money`,
    `refund_bonus_money`,
    `receipt_month`,
    `receipt_date`,
    `receipt_status`,
    `total_finace`,
    `total_bonus`,
    live_new_money)
    SELECT groupId,branchId,campusId,student_id,groupName,branchName,campusName,SUM(newMoney),SUM(reMoney),SUM(allMoney),SUM(washMoney),SUM(totalMoney),SUM(bonusMoney),SUM(refundMoney),SUM(special_return_money),SUM(totalRefundMoney),SUM(refundBonusMoney),
    count_date_month,receiptDate,'NOT_AUDIT',SUM(totalMoney-totalRefundMoney),SUM(bonusMoney-refundBonusMoney),SUM(live_new_money)
     FROM (
    SELECT o3.id groupId,o2.id branchId,o.id campusId,fun.STUDENT_ID student_id,
    o3.name groupName,o2.name branchName,o.name campusName,
    SUM(CASE WHEN c.CONTRACT_TYPE='NEW_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) newMoney,
    SUM(CASE WHEN c.CONTRACT_TYPE='RE_CONTRACT' AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) reMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) allMoney,
    SUM(CASE WHEN  fun.FUNDS_PAY_TYPE='WASH' THEN  fun.TRANSACTION_AMOUNT ELSE 0 END) washMoney,
    SUM(CASE WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='RECEIPT' THEN  fun.TRANSACTION_AMOUNT WHEN fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') AND fun.FUNDS_PAY_TYPE='WASH' THEN  -fun.TRANSACTION_AMOUNT ELSE 0 END) totalMoney,
    0 bonusMoney,
    0 refundMoney,
    0 AS special_return_money,
    0 totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM funds_change_history fun
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON fun.fund_campus_id =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1)) 
    AND o.id NOT IN(SELECT campus_id FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status<>'NOT_AUDIT') 
    AND fun.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') 
    AND (fun.AUDIT_STATUS='VALIDATE' OR fun.FUNDS_PAY_TYPE='WASH')  AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
    AND fun.CONTRACT_ID IS NOT NULL GROUP BY o.id,fun.STUDENT_ID
    UNION
    ALL
    SELECT o3.id groupId,o2.id branchId,o.id campusId,fun.STUDENT_ID student_id,
    o3.name groupName,o2.name branchName,o.name campusName,
    0 newMoney,
    0 reMoney,
    0 allMoney,
    0 washMoney,
    0 totalMoney,
    0 bonusMoney,
    SUM(-fun.TRANSACTION_AMOUNT) refundMoney,   
    SUM(IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) AS special_return_money,
    SUM(-fun.TRANSACTION_AMOUNT + IFNULL(srf.RETURN_SPECIAL_AMOUNT,0)) totalRefundMoney,
    0 refundBonusMoney,
    0 live_new_money
    FROM student_return_fee srf
    LEFT JOIN funds_change_history fun ON fun.id=srf.FUNDS_CHANGE_ID
    LEFT JOIN contract c ON c.ID=fun.CONTRACT_ID
    LEFT JOIN organization o ON srf.CAMPUS =o.id
    LEFT JOIN organization o2 ON o2.id=o.parentId
    LEFT JOIN organization o3 ON o3.id=o2.parentId
    WHERE fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND o.id NOT IN(SELECT campus_id FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status<>'NOT_AUDIT') 
		AND fun.CHANNEL ='REFUND_MONEY'  
		 AND NOT EXISTS (SELECT 1 FROM contract_product cp WHERE cp.type = 'LIVE' AND cp.contract_id = fun.CONTRACT_ID )
        GROUP BY o.id,fun.STUDENT_ID
     
     UNION ALL       
            
     SELECT o3.id groupId,o2.id branchId,o.id campusId,d.student_id student_id,
     o3.name groupName,o2.name branchName,o.name campusName,
     0 newMoney,
     0 reMoney,
     0 allMoney,
     0 washMoney,
     SUM(d.paid_amount) totalMoney,
     SUM(d.campus_achievement) bonusMoney,
     0 refundMoney,
     0 AS special_return_money,
     0 totalRefundMoney,
     0 refundBonusMoney,
     SUM(d.paid_amount) live_new_money
     FROM  live_payment_record d
     LEFT JOIN organization o ON d.order_campusId =o.id
     LEFT JOIN organization o2 ON o2.id=o.parentId
     LEFT JOIN organization o3 ON o3.id=o2.parentId
     WHERE   d.payment_date  LIKE CONCAT(count_date_month,'%') AND d.contact_type='NEW' AND d.finance_type='INCOME'
	AND o.id NOT IN(SELECT campus_id FROM ods_month_payment_receipt_main WHERE receipt_month=count_date_month AND receipt_status<>'NOT_AUDIT') 
             GROUP BY o.id,d.student_id
        
        ) a GROUP BY a.campusId,a.student_id  ORDER BY a.branchName;
    
    UPDATE ods_month_payment_receipt_main_student om
        LEFT JOIN
        (SELECT 
            cb.ORGANIZATIONID,fun.STUDENT_ID,
                SUM(CASE
                    WHEN fun.CHANNEL IN ('CASH' , 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN cb.amount
                    ELSE 0
                END) bonusMoney,
                SUM(CASE
                    WHEN fun.CHANNEL = 'REFUND_MONEY' THEN cb.amount
                    ELSE 0
                END) refundBonusMoney
        FROM
            income_distribution cb
        LEFT JOIN funds_change_history fun ON cb.FUNDS_CHANGE_ID = fun.ID
        WHERE
            cb.ORGANIZATIONID IS NOT NULL  AND cb.base_bonus_type='CAMPUS'
            
            AND NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fun.CONTRACT_ID )
                AND (fun.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') OR (fun.receipt_time LIKE CONCAT(count_date_month,'%') AND fun.receipt_status=1))
        GROUP BY cb.ORGANIZATIONID,fun.STUDENT_ID) s ON s.ORGANIZATIONID = om.campus_id AND s.STUDENT_ID = om.student_id
    SET 
        om.bonus_money = IFNULL(s.bonusMoney, 0) + om.bonus_money,
        om.refund_bonus_money = IFNULL(s.refundBonusMoney, 0),
        om.total_bonus = IFNULL(s.bonusMoney - s.refundBonusMoney, 0) + om.total_bonus
    WHERE om.receipt_month=count_date_month AND om.receipt_status='NOT_AUDIT';
END
$$


--changeset duanmenrun:1940————5--1
--comment 校区业绩凭证
DROP TABLE IF EXISTS ods_month_campus_achievement_main;
CREATE TABLE ods_month_campus_achievement_main (
		id VARCHAR(50) NOT NULL,
		group_id VARCHAR (32) COMMENT '集团',
		group_name VARCHAR (100) COMMENT '集团名字',
		branch_id VARCHAR (32) COMMENT '分公司',
		branch_name VARCHAR (32)COMMENT '分公司名字',
		campus_id VARCHAR (32)COMMENT '校区',
		campus_name VARCHAR (32)COMMENT '校区名字',
		campus_amount_new DECIMAL(10,2) COMMENT '星火新签合同业绩',
		campus_amount_re DECIMAL(10,2) COMMENT '星火续费合同业绩',
		all_money DECIMAL(10,2) COMMENT '星火收款业绩',
		refund_amount DECIMAL(10,2) COMMENT '星火退费业绩',
		total_money DECIMAL(10,2) COMMENT '星火校区总业绩',
		live_income_new DECIMAL(10,2) COMMENT '直播新签业绩',
		live_income_renew DECIMAL(10,2) COMMENT '直播续费业绩',
		live_refund_new DECIMAL(10,2) COMMENT '直播新签退费责任业绩',
		live_refund_renew DECIMAL(10,2) COMMENT '直播续费退费责任业绩',
		live_total_money DECIMAL(10,2) COMMENT '直播合作业绩',
		total_income_money DECIMAL(10,2) COMMENT '总收款业绩',
		total_refund_money DECIMAL(10,2) COMMENT '总退费责任',
		total_bonus DECIMAL(10,2) COMMENT '本月业绩合计',
		receipt_month VARCHAR (32)COMMENT '凭证月份',
		receipt_date VARCHAR (32)COMMENT '凭证日期',
		receipt_status VARCHAR (32)COMMENT '状态',
		remark VARCHAR (500)COMMENT '备注',
		modify_time VARCHAR (32)COMMENT '修改时间',
		modify_user VARCHAR (32)COMMENT '修改人',
		modify_money DECIMAL(10,2)COMMENT '调整金额',
		after_modify_money DECIMAL(10,2)COMMENT '调整后金额',
		CURRENT_AUDIT_USER VARCHAR (32),
		CURRENT_AUDIT_TIME VARCHAR (32),
		CAMPUS_CONFIRM_USER VARCHAR (32),
		CAMPUS_CONFIRM_TIME VARCHAR (32),
		FINANCE_FIRST_AUDIT_USER VARCHAR (32),
		FINANCE_FIRST_AUDIT_TIME VARCHAR (32),
		BRENCH_CONFIRM_USER VARCHAR (32),
		BRENCH_CONFIRM_TIME VARCHAR (32),
		FINANCE_END_AUDIT_USER VARCHAR (32),
		FINANCE_END_AUDIT_TIME VARCHAR (32),
		PRIMARY KEY (id),
		INDEX idx_receipt_month_campus_id(receipt_month,campus_id)
	)COMMENT='校区业绩凭证主表'; 


DROP TABLE IF EXISTS ods_month_campus_achievement_modify;
CREATE TABLE `ods_month_campus_achievement_modify` (
		`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT ,
		`type` VARCHAR (32)COMMENT '调整类型',
		`amount` DECIMAL(10,2)COMMENT '调整金额',
		`remark` VARCHAR (500)COMMENT '备注',
		`create_user` VARCHAR (32)COMMENT '创建人',
		`create_time` VARCHAR (32)COMMENT '创建时间',
		`modify_user` VARCHAR (32)COMMENT '修改人',
		`modify_time` VARCHAR (32)COMMENT '修改时间',
		`achievement_main_id` VARCHAR (50)COMMENT '主表Id',
		PRIMARY KEY (id),
		INDEX idx_achievement_main_id(achievement_main_id)
	)COMMENT='校区业绩凭证调整'; 

DROP TABLE IF EXISTS ods_month_campus_achievement_main_student;
CREATE TABLE `ods_month_campus_achievement_main_student` (
	`id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT ,
	`group_id` VARCHAR (32)COMMENT '集团',
	`branch_id` VARCHAR (32)COMMENT '分公司',
	`campus_id` VARCHAR (32)COMMENT '校区',
	`student_id` VARCHAR (32)COMMENT '学生',
	`school_id` VARCHAR (32)COMMENT '学校',
	`grade_id` VARCHAR (32)COMMENT '年级',
	`contract_type` VARCHAR (32)COMMENT '合同类型',
	`contract_id` VARCHAR (32)COMMENT '合同ID',
	`contract_create_time` VARCHAR (32)COMMENT '合同创建时间',
	`contract_status` VARCHAR (32)COMMENT '合同状态',
	`contract_paid_status` VARCHAR (32)COMMENT '合同支付状态',
	`un_distribution_amount` DECIMAL(10,2)COMMENT '待分配资金',
	`transaction_amount` DECIMAL(10,2)COMMENT '金额',
	`transaction_time` VARCHAR (32)COMMENT '收款/退费时间',
	`transaction_campus` VARCHAR (32)COMMENT '校区',
	`un_distribution_bonus` DECIMAL(10,2)COMMENT '待分配业绩',
	`bonus_amount` DECIMAL(10,2)COMMENT '业绩金额',
	`bonus_type` VARCHAR (32)COMMENT '业绩类型',
	`bonus_staff_id` VARCHAR (32)COMMENT '业绩人',
	`bonus_dept_id` VARCHAR (32)COMMENT '业绩人部门',
	`receipt_month` VARCHAR (32)COMMENT '凭证月份',
	`receipt_date` VARCHAR (32)COMMENT '凭证日期',
	`receipt_status` VARCHAR (32)COMMENT '凭证状态',
	`main_id` VARCHAR (50)COMMENT '凭证主表ID',
	`funds_change_id` VARCHAR (32),
	`PRODUCT_TYPE` VARCHAR (32),
	PRIMARY KEY (id),
	INDEX idx_receipt_month_campus_id(receipt_month,campus_id)
)COMMENT='校区业绩凭证-学生'; 

--changeset duanmenrun:1940--6--1 endDelimiter:\$\$
--comment 新增校区业绩凭证存储过程
DROP PROCEDURE IF EXISTS `proc_update_ods_month_campus_achievement_main`;
$$
CREATE  PROCEDURE `proc_update_ods_month_campus_achievement_main`(IN count_date_month VARCHAR(10),IN incampusId VARCHAR(32))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
  SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
 
IF incampusId IS NOT NULL AND incampusId<>'' 
THEN
    DELETE FROM ods_month_campus_achievement_main WHERE receipt_month=count_date_month AND campus_id=incampusId;
    INSERT INTO ods_month_campus_achievement_main
    (   id,
	group_id,
	group_name,
	branch_id,
	branch_name,
	campus_id,
	campus_name,
	campus_amount_new,
	campus_amount_re,
	all_money,
	refund_amount,
	total_money,
	live_income_new,
	live_income_renew,
	live_refund_new,
	live_refund_renew,
	live_total_money,
	total_income_money,
	total_refund_money,
	total_bonus,
	receipt_month,
	receipt_date,
	receipt_status)
    SELECT CONCAT(campusId,'_',REPLACE(count_date_month,'-','')),groupId,groupName,branchId,branchName,campusId,campusName,
    SUM(campusAmountNew),SUM(campusAmountRe),SUM(allMoney),SUM(refundAmount),SUM(totalMoney),
    SUM(liveIncomeNew),SUM(liveIncomeRenew),SUM(liveRefundNew),SUM(liveRefundRenew),SUM(liveTotalMoney),
    SUM(totalIncomeMoney),SUM(totalRefundMoney),SUM(totalBonus),count_date_month,receiptDate,'NOT_AUDIT'
     FROM (
	SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
	SUM( CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT'  AND fch.CHANNEL IN ( 'CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER' )  THEN f.amount  ELSE 0  END ) AS campusAmountNew,
	SUM( CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT'  AND fch.CHANNEL IN ('CASH','POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN f.amount ELSE 0  END ) AS campusAmountRe,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount ELSE 0 END) allMoney,
	SUM( CASE WHEN fch.CHANNEL = 'REFUND_MONEY'  THEN f.amount  ELSE 0  END ) AS refundAmount,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount WHEN  fch.CHANNEL = 'REFUND_MONEY'  THEN -f.amount ELSE 0 END) totalMoney,
	0 liveIncomeNew,0 liveIncomeRenew,0 liveRefundNew,0 liveRefundRenew,0 liveTotalMoney,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount ELSE 0 END) totalIncomeMoney,
	SUM( CASE WHEN fch.CHANNEL = 'REFUND_MONEY'  THEN f.amount  ELSE 0  END ) AS totalRefundMoney,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount WHEN  fch.CHANNEL = 'REFUND_MONEY'  THEN -f.amount ELSE 0 END) totalBonus
	
	FROM income_distribution  f
	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID
	LEFT JOIN contract c ON c.ID=fch.CONTRACT_ID
	LEFT JOIN organization o ON o.id=f.organizationId
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE fch.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND o.id = incampusId AND f.base_bonus_type='CAMPUS'  
	AND  fch.CHANNEL IN('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' ) 
	AND f.product_type <> 'LIVE'  GROUP BY o.id
	UNION ALL
	
	SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
	0 campusAmountNew,0 campusAmountRe,0 allMoney,0 refundAmount,0 totalMoney,
	SUM( CASE WHEN pd.contact_type = 'NEW'  AND pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS liveIncomeNew,
	SUM( CASE WHEN pd.contact_type = 'RENEW'  AND pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS liveIncomeRenew,
	SUM( CASE WHEN pd.contact_type = 'NEW'  AND pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS liveRefundNew,
	SUM( CASE WHEN pd.contact_type = 'RENEW'  AND pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS liveRefundRenew,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement WHEN pd.finance_type = 'REFUND' THEN -pd.campus_achievement ELSE 0  END ) AS liveTotalMoney,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS totalIncomeMoney,
	SUM( CASE WHEN pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS totalRefundMoney,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement WHEN pd.finance_type = 'REFUND' THEN -pd.campus_achievement ELSE 0  END ) AS totalBonus
	
	FROM live_payment_record pd  
	LEFT JOIN organization o ON o.id = pd.order_campusId	
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE pd.payment_date LIKE CONCAT(count_date_month,'%') AND o.id = incampusId AND finance_type IN ('INCOME','REFUND')
	GROUP BY o.id
	) a GROUP BY a.campusId  ORDER BY a.branchName;
    
    UPDATE ods_month_campus_achievement_main om 
    SET om.modify_money=(SELECT IFNULL(SUM(amount),0) FROM ods_month_campus_achievement_modify WHERE achievement_main_id=CONCAT(incampusId,'_',REPLACE(count_date_month,'-',''))),
    om.after_modify_money=om.total_bonus+(SELECT IFNULL(SUM(amount),0) FROM ods_month_campus_achievement_modify WHERE achievement_main_id=CONCAT(incampusId,'_',REPLACE(count_date_month,'-','')))
    WHERE om.campus_id = incampusId AND om.receipt_month=count_date_month ;
     
ELSE
    DELETE FROM ods_month_campus_achievement_main WHERE receipt_month=count_date_month;
    INSERT INTO ods_month_campus_achievement_main
    (   id,
	group_id,
	group_name,
	branch_id,
	branch_name,
	campus_id,
	campus_name,
	campus_amount_new,
	campus_amount_re,
	all_money,
	refund_amount,
	total_money,
	live_income_new,
	live_income_renew,
	live_refund_new,
	live_refund_renew,
	live_total_money,
	total_income_money,
	total_refund_money,
	total_bonus,
	receipt_month,
	receipt_date,
	receipt_status,
	modify_money,
	after_modify_money)
    SELECT CONCAT(campusId,'_',REPLACE(count_date_month,'-','')),groupId,groupName,branchId,branchName,campusId,campusName,
    SUM(campusAmountNew),SUM(campusAmountRe),SUM(allMoney),SUM(refundAmount),SUM(totalMoney),
    SUM(liveIncomeNew),SUM(liveIncomeRenew),SUM(liveRefundNew),SUM(liveRefundRenew),SUM(liveTotalMoney),
    SUM(totalIncomeMoney),SUM(totalRefundMoney),SUM(totalBonus),count_date_month,receiptDate,'NOT_AUDIT',0,SUM(totalBonus)
     FROM (
	SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
	SUM( CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT'  AND fch.CHANNEL IN ( 'CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER' )  THEN f.amount  ELSE 0  END ) AS campusAmountNew,
	SUM( CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT'  AND fch.CHANNEL IN ('CASH','POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN f.amount ELSE 0  END ) AS campusAmountRe,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount ELSE 0 END) allMoney,
	SUM( CASE WHEN fch.CHANNEL = 'REFUND_MONEY'  THEN f.amount  ELSE 0  END ) AS refundAmount,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount WHEN  fch.CHANNEL = 'REFUND_MONEY'  THEN -f.amount ELSE 0 END) totalMoney,
	0 liveIncomeNew,0 liveIncomeRenew,0 liveRefundNew,0 liveRefundRenew,0 liveTotalMoney,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount ELSE 0 END) totalIncomeMoney,
	SUM( CASE WHEN fch.CHANNEL = 'REFUND_MONEY'  THEN f.amount  ELSE 0  END ) AS totalRefundMoney,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount WHEN  fch.CHANNEL = 'REFUND_MONEY'  THEN -f.amount ELSE 0 END) totalBonus
	
	FROM income_distribution  f
	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID
	LEFT JOIN contract c ON c.ID=fch.CONTRACT_ID
	LEFT JOIN organization o ON o.id=f.organizationId
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE fch.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND f.base_bonus_type='CAMPUS'  
	AND  fch.CHANNEL IN('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' ) 
	AND f.product_type <> 'LIVE'  GROUP BY o.id
	UNION ALL
	
	SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
	0 campusAmountNew,0 campusAmountRe,0 allMoney,0 refundAmount,0 totalMoney,
	SUM( CASE WHEN pd.contact_type = 'NEW'  AND pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS liveIncomeNew,
	SUM( CASE WHEN pd.contact_type = 'RENEW'  AND pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS liveIncomeRenew,
	SUM( CASE WHEN pd.contact_type = 'NEW'  AND pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS liveRefundNew,
	SUM( CASE WHEN pd.contact_type = 'RENEW'  AND pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS liveRefundRenew,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement WHEN pd.finance_type = 'REFUND' THEN -pd.campus_achievement ELSE 0  END ) AS liveTotalMoney,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS totalIncomeMoney,
	SUM( CASE WHEN pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS totalRefundMoney,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement WHEN pd.finance_type = 'REFUND' THEN -pd.campus_achievement ELSE 0  END ) AS totalBonus
	
	FROM live_payment_record pd  
	LEFT JOIN organization o ON o.id = pd.order_campusId	
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE pd.payment_date LIKE CONCAT(count_date_month,'%') AND finance_type IN ('INCOME','REFUND')
	GROUP BY o.id
	) a GROUP BY a.campusId  ORDER BY a.branchName;
  
END IF;
END
$$


DROP PROCEDURE IF EXISTS `proc_update_ods_month_campus_achievement_main_flush`;
$$
CREATE  PROCEDURE `proc_update_ods_month_campus_achievement_main_flush`(IN count_date_month VARCHAR(10))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
  SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
 
    DELETE FROM ods_month_campus_achievement_main WHERE receipt_month=count_date_month AND receipt_status='NOT_AUDIT';
    INSERT INTO ods_month_campus_achievement_main
    (   id,
	group_id,
	group_name,
	branch_id,
	branch_name,
	campus_id,
	campus_name,
	campus_amount_new,
	campus_amount_re,
	all_money,
	refund_amount,
	total_money,
	live_income_new,
	live_income_renew,
	live_refund_new,
	live_refund_renew,
	live_total_money,
	total_income_money,
	total_refund_money,
	total_bonus,
	receipt_month,
	receipt_date,
	receipt_status,
	modify_money,
	after_modify_money)
    SELECT CONCAT(campusId,'_',REPLACE(count_date_month,'-','')),groupId,groupName,branchId,branchName,campusId,campusName,
    SUM(campusAmountNew),SUM(campusAmountRe),SUM(allMoney),SUM(refundAmount),SUM(totalMoney),
    SUM(liveIncomeNew),SUM(liveIncomeRenew),SUM(liveRefundNew),SUM(liveRefundRenew),SUM(liveTotalMoney),
    SUM(totalIncomeMoney),SUM(totalRefundMoney),SUM(totalBonus),count_date_month,receiptDate,'NOT_AUDIT',0,SUM(totalBonus)
     FROM (
	SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
	SUM( CASE WHEN c.CONTRACT_TYPE = 'NEW_CONTRACT'  AND fch.CHANNEL IN ( 'CASH', 'POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER' )  THEN f.amount  ELSE 0  END ) AS campusAmountNew,
	SUM( CASE WHEN c.CONTRACT_TYPE = 'RE_CONTRACT'  AND fch.CHANNEL IN ('CASH','POS', 'WEB_CHART_PAY', 'ALI_PAY', 'BANK_TRANSFER') THEN f.amount ELSE 0  END ) AS campusAmountRe,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount ELSE 0 END) allMoney,
	SUM( CASE WHEN fch.CHANNEL = 'REFUND_MONEY'  THEN f.amount  ELSE 0  END ) AS refundAmount,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount WHEN  fch.CHANNEL = 'REFUND_MONEY'  THEN -f.amount ELSE 0 END) totalMoney,
	0 liveIncomeNew,0 liveIncomeRenew,0 liveRefundNew,0 liveRefundRenew,0 liveTotalMoney,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount ELSE 0 END) totalIncomeMoney,
	SUM( CASE WHEN fch.CHANNEL = 'REFUND_MONEY'  THEN f.amount  ELSE 0  END ) AS totalRefundMoney,
	SUM( CASE WHEN fch.CHANNEL IN('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER') THEN  f.amount WHEN  fch.CHANNEL = 'REFUND_MONEY'  THEN -f.amount ELSE 0 END) totalBonus
	
	FROM income_distribution  f
	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID
	LEFT JOIN contract c ON c.ID=fch.CONTRACT_ID
	LEFT JOIN organization o ON o.id=f.organizationId
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE fch.TRANSACTION_TIME LIKE CONCAT(count_date_month,'%') AND f.base_bonus_type='CAMPUS'  
	AND  fch.CHANNEL IN('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' ) 
	AND o.id  NOT IN(SELECT campus_id FROM ods_month_campus_achievement_main WHERE receipt_month=count_date_month AND receipt_status <> 'NOT_AUDIT')
	AND f.product_type <> 'LIVE'  GROUP BY o.id
	UNION ALL
	
	SELECT o3.id groupId,o2.id branchId,o.id campusId,o3.name groupName,o2.name branchName,o.name campusName,
	0 campusAmountNew,0 campusAmountRe,0 allMoney,0 refundAmount,0 totalMoney,
	SUM( CASE WHEN pd.contact_type = 'NEW'  AND pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS liveIncomeNew,
	SUM( CASE WHEN pd.contact_type = 'RENEW'  AND pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS liveIncomeRenew,
	SUM( CASE WHEN pd.contact_type = 'NEW'  AND pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS liveRefundNew,
	SUM( CASE WHEN pd.contact_type = 'RENEW'  AND pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS liveRefundRenew,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement WHEN pd.finance_type = 'REFUND' THEN -pd.campus_achievement ELSE 0  END ) AS liveTotalMoney,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement ELSE 0  END ) AS totalIncomeMoney,
	SUM( CASE WHEN pd.finance_type = 'REFUND' THEN pd.campus_achievement ELSE 0  END ) AS totalRefundMoney,
	SUM( CASE WHEN pd.finance_type = 'INCOME' THEN pd.campus_achievement WHEN pd.finance_type = 'REFUND' THEN -pd.campus_achievement ELSE 0  END ) AS totalBonus
	
	FROM live_payment_record pd  
	LEFT JOIN organization o ON o.id = pd.order_campusId	
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE pd.payment_date LIKE CONCAT(count_date_month,'%') AND finance_type IN ('INCOME','REFUND')
	AND o.id NOT IN(SELECT campus_id FROM ods_month_campus_achievement_main WHERE receipt_month=count_date_month AND receipt_status <> 'NOT_AUDIT')
	GROUP BY o.id
	) a GROUP BY a.campusId  ORDER BY a.branchName;
END
$$


--changeset duanmenrun:1940---6.1 endDelimiter:\$\$
--comment 新增校区业绩凭证-学生详情存储过程

DROP PROCEDURE IF EXISTS `proc_ods_month_campus_achievement_main_student`;
$$
CREATE  PROCEDURE `proc_ods_month_campus_achievement_main_student`(IN yearMonth VARCHAR(7),IN campusId VARCHAR(32))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
  SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
    IF campusId IS NOT NULL AND campusId<>'' THEN
        DELETE FROM ods_month_campus_achievement_main_student WHERE receipt_month=yearMonth AND campus_Id=campusId;
        INSERT INTO `ods_month_campus_achievement_main_student`
        (
        `group_id`,
        `branch_id`,
        `campus_id`,
        `student_id`,
        `school_id`,
        `grade_id`,
        `contract_type`,
        `contract_id`,
        `contract_create_time`,
        `contract_status`,
        `contract_paid_status`,
        `un_distribution_amount`,
        `transaction_amount`,
        `transaction_time`,
        `transaction_campus`,
        `un_distribution_bonus`,
        `bonus_amount`,
        `bonus_type`,
        `bonus_staff_id`,
        `bonus_dept_id`,
        `receipt_month`, 
        `receipt_date`,
        `receipt_status`,`main_id`,`funds_change_id`,`PRODUCT_TYPE`)
        SELECT 
            o3.id groupId,
            o2.id branchId,
            o.id campusId,
            s.id studentId,
            s.school schoolId ,
            s.grade_id gradeId,
            c.CONTRACT_TYPE,
            c.ID,
            c.CREATE_TIME,
            c.CONTRACT_STATUS,
            c.PAID_STATUS,
            CASE WHEN NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fch.CONTRACT_ID ) THEN 
            (SELECT 
                    c.PAID_AMOUNT - SUM(PAID_AMOUNT)
                FROM
                    contract_product
                WHERE
                    contract_id = c.ID) ELSE 0 END,
            CASE WHEN NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fch.CONTRACT_ID ) THEN         
            fch.TRANSACTION_AMOUNT ELSE 0 END,
            fch.TRANSACTION_TIME,
            fch.fund_campus_id,
            fch.user_not_assigned_amount,
            SUM(f.amount ) BONUS_AMOUNT,
            f.BONUS_TYPE,
            c.CREATE_USER_ID,
            f.organizationId,
            yearMonth,
            receiptDate,
            'NOT_AUDIT',
            CONCAT(o.id,'_',REPLACE(yearMonth,'-','')),fch.id,f.product_type
        FROM income_distribution  f
	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID
	LEFT JOIN contract c ON c.ID=fch.CONTRACT_ID
        LEFT JOIN student s ON c.STUDENT_ID = s.ID
	LEFT JOIN organization o ON o.id=f.organizationId
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE fch.TRANSACTION_TIME LIKE CONCAT(yearMonth,'%') AND o.id = campusId AND f.base_bonus_type='CAMPUS'  
	AND  fch.CHANNEL IN('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' ) 
	AND f.product_type <> 'LIVE'  GROUP BY fch.id ,o.id, f.product_type;
	
		
	INSERT INTO `ods_month_campus_achievement_main_student`
        (
        `group_id`,
        `branch_id`,
        `campus_id`,
        `student_id`,
        `school_id`,
        `grade_id`,
        `contract_type`,
        `contract_id`,
        `contract_create_time`,
        `contract_status`,
        `contract_paid_status`,
        `un_distribution_amount`,
        `transaction_amount`,
        `transaction_time`,
        `transaction_campus`,
        `un_distribution_bonus`,
        `bonus_amount`,
        `bonus_type`,
        `bonus_staff_id`,
        `bonus_dept_id`,
        `receipt_month`, 
        `receipt_date`,
        `receipt_status`,`main_id`,`funds_change_id`,`PRODUCT_TYPE`)
        SELECT 
            o3.id groupId,
            o2.id branchId,
            o.id campusId,
            s.id studentId,
            s.school schoolId ,
            s.grade_id gradeId,
            'LIVE_CONTRACT',
            NULL,
            NULL,
            NULL,
            NULL,
            0,
            CASE WHEN pd.finance_type='INCOME' AND pd.contact_type = 'NEW' THEN  pd.campus_achievement ELSE 0 END,
            pd.payment_date,
            pd.order_campusId,
            0,
            pd.campus_achievement,
            CASE WHEN pd.finance_type = 'INCOME' THEN 'NORMAL' WHEN pd.finance_type = 'REFUND' THEN 'FEEDBACK_REFUND' ELSE ''  END,
            (SELECT user_id FROM USER WHERE employee_No = pd.user_employeeNo  LIMIT 1),
            pd.order_campusId,
            yearMonth,
            receiptDate,
            'NOT_AUDIT',
            CONCAT(o.id,'_',REPLACE(yearMonth,'-','')),NULL,'LIVE'
        FROM live_payment_record pd  
        LEFT JOIN student s ON pd.student_id = s.ID
	LEFT JOIN organization o ON o.id = pd.order_campusId	
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE pd.payment_date LIKE CONCAT(yearMonth,'%') AND o.id = campusId AND finance_type IN ('INCOME','REFUND');
		
    ELSE
        DELETE FROM ods_month_campus_achievement_main_student WHERE receipt_month=yearMonth;
        INSERT INTO `ods_month_campus_achievement_main_student`
        (
        `group_id`,
        `branch_id`,
        `campus_id`,
        `student_id`,
        `school_id`,
        `grade_id`,
        `contract_type`,
        `contract_id`,
        `contract_create_time`,
        `contract_status`,
        `contract_paid_status`,
        `un_distribution_amount`,
        `transaction_amount`,
        `transaction_time`,
        `transaction_campus`,
        `un_distribution_bonus`,
        `bonus_amount`,
        `bonus_type`,
        `bonus_staff_id`,
        `bonus_dept_id`,
        `receipt_month`, 
        `receipt_date`,
        `receipt_status`,`main_id`,`funds_change_id`,`PRODUCT_TYPE`)
        SELECT 
            o3.id groupId,
            o2.id branchId,
            o.id campusId,
            s.id studentId,
            s.school schoolId ,
            s.grade_id gradeId,
            c.CONTRACT_TYPE,
            c.ID,
            c.CREATE_TIME,
            c.CONTRACT_STATUS,
            c.PAID_STATUS,
            CASE WHEN NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fch.CONTRACT_ID ) THEN 
            (SELECT 
                    c.PAID_AMOUNT - SUM(PAID_AMOUNT)
                FROM
                    contract_product
                WHERE
                    contract_id = c.ID) ELSE 0 END,
            CASE WHEN NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fch.CONTRACT_ID ) THEN         
            fch.TRANSACTION_AMOUNT ELSE 0 END,
            fch.TRANSACTION_TIME,
            fch.fund_campus_id,
            fch.user_not_assigned_amount,
            SUM(f.amount ) BONUS_AMOUNT,
            f.BONUS_TYPE,
            c.CREATE_USER_ID,
            f.organizationId,
            yearMonth,
            receiptDate,
            'NOT_AUDIT',
            CONCAT(o.id,'_',REPLACE(yearMonth,'-','')),fch.id,f.product_type
        FROM income_distribution  f
	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID
	LEFT JOIN contract c ON c.ID=fch.CONTRACT_ID
        LEFT JOIN student s ON c.STUDENT_ID = s.ID
	LEFT JOIN organization o ON o.id=f.organizationId
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE fch.TRANSACTION_TIME LIKE CONCAT(yearMonth,'%') AND f.base_bonus_type='CAMPUS'  
	AND  fch.CHANNEL IN('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' ) 
	AND f.product_type <> 'LIVE'  GROUP BY fch.id ,o.id, f.product_type;
	
		
	INSERT INTO `ods_month_campus_achievement_main_student`
        (
        `group_id`,
        `branch_id`,
        `campus_id`,
        `student_id`,
        `school_id`,
        `grade_id`,
        `contract_type`,
        `contract_id`,
        `contract_create_time`,
        `contract_status`,
        `contract_paid_status`,
        `un_distribution_amount`,
        `transaction_amount`,
        `transaction_time`,
        `transaction_campus`,
        `un_distribution_bonus`,
        `bonus_amount`,
        `bonus_type`,
        `bonus_staff_id`,
        `bonus_dept_id`,
        `receipt_month`, 
        `receipt_date`,
        `receipt_status`,`main_id`,`funds_change_id`,`PRODUCT_TYPE`)
        SELECT 
            o3.id groupId,
            o2.id branchId,
            o.id campusId,
            s.id studentId,
            s.school schoolId ,
            s.grade_id gradeId,
            'LIVE_CONTRACT',
            NULL,
            NULL,
            NULL,
            NULL,
            0,
            CASE WHEN pd.finance_type='INCOME' AND pd.contact_type = 'NEW' THEN  pd.campus_achievement ELSE 0 END,
            pd.payment_date,
            pd.order_campusId,
            0,
            pd.campus_achievement,
            CASE WHEN pd.finance_type = 'INCOME' THEN 'NORMAL' WHEN pd.finance_type = 'REFUND' THEN 'FEEDBACK_REFUND' ELSE ''  END,
            (SELECT user_id FROM USER WHERE employee_No = pd.user_employeeNo  LIMIT 1),
            pd.order_campusId,
            yearMonth,
            receiptDate,
            'NOT_AUDIT',
            CONCAT(o.id,'_',REPLACE(yearMonth,'-','')),NULL,'LIVE'
        FROM live_payment_record pd  
        LEFT JOIN student s ON pd.student_id = s.ID
	LEFT JOIN organization o ON o.id = pd.order_campusId	
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE pd.payment_date LIKE CONCAT(yearMonth,'%') AND finance_type IN ('INCOME','REFUND');
    END IF;
END
$$

DROP PROCEDURE IF EXISTS `proc_ods_month_campus_achievement_main_student_flush`;
$$
CREATE PROCEDURE `proc_ods_month_campus_achievement_main_student_flush`(IN yearMonth VARCHAR(7))
BEGIN
DECLARE receiptDate VARCHAR(20);
IF DAY(NOW())<=8 THEN
 SET receiptDate=CURDATE();
ELSE
  SET receiptDate=CONCAT(SUBSTR(CURRENT_DATE(),1,7),'-08');
 END IF;
        DELETE FROM ods_month_campus_achievement_main_student WHERE receipt_month=yearMonth AND receipt_status='NOT_AUDIT';
        INSERT INTO `ods_month_campus_achievement_main_student`
        (
        `group_id`,
        `branch_id`,
        `campus_id`,
        `student_id`,
        `school_id`,
        `grade_id`,
        `contract_type`,
        `contract_id`,
        `contract_create_time`,
        `contract_status`,
        `contract_paid_status`,
        `un_distribution_amount`,
        `transaction_amount`,
        `transaction_time`,
        `transaction_campus`,
        `un_distribution_bonus`,
        `bonus_amount`,
        `bonus_type`,
        `bonus_staff_id`,
        `bonus_dept_id`,
        `receipt_month`, 
        `receipt_date`,
        `receipt_status`,`main_id`,`funds_change_id`,`PRODUCT_TYPE`)
        SELECT 
            o3.id groupId,
            o2.id branchId,
            o.id campusId,
            s.id studentId,
            s.school schoolId ,
            s.grade_id gradeId,
            c.CONTRACT_TYPE,
            c.ID,
            c.CREATE_TIME,
            c.CONTRACT_STATUS,
            c.PAID_STATUS,
            CASE WHEN NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fch.CONTRACT_ID ) THEN 
            (SELECT 
                    c.PAID_AMOUNT - SUM(PAID_AMOUNT)
                FROM
                    contract_product
                WHERE
                    contract_id = c.ID) ELSE 0 END,
            CASE WHEN NOT EXISTS (SELECT 1 FROM contract WHERE CONTRACT_TYPE = 'LIVE_CONTRACT' AND id = fch.CONTRACT_ID ) THEN         
            fch.TRANSACTION_AMOUNT ELSE 0 END,
            fch.TRANSACTION_TIME,
            fch.fund_campus_id,
            fch.user_not_assigned_amount,
            SUM(f.amount ) BONUS_AMOUNT,
            f.BONUS_TYPE,
            c.CREATE_USER_ID,
            f.organizationId,
            yearMonth,
            receiptDate,
            'NOT_AUDIT',
            CONCAT(o.id,'_',REPLACE(yearMonth,'-','')),fch.id,f.product_type
        FROM income_distribution  f
	INNER JOIN funds_change_history fch ON f.funds_change_id=fch.ID
	LEFT JOIN contract c ON c.ID=fch.CONTRACT_ID
        LEFT JOIN student s ON c.STUDENT_ID = s.ID
	LEFT JOIN organization o ON o.id=f.organizationId
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE fch.TRANSACTION_TIME LIKE CONCAT(yearMonth,'%') AND f.base_bonus_type='CAMPUS'  
	AND  fch.CHANNEL IN('CASH','POS','REFUND_MONEY','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY' ) 
	AND o.id NOT IN(SELECT campus_id FROM ods_month_campus_achievement_main_student WHERE receipt_month=yearMonth AND receipt_status <> 'NOT_AUDIT')
	AND f.product_type <> 'LIVE'  GROUP BY fch.id ,o.id, f.product_type;
	
		
	INSERT INTO `ods_month_campus_achievement_main_student`
        (
        `group_id`,
        `branch_id`,
        `campus_id`,
        `student_id`,
        `school_id`,
        `grade_id`,
        `contract_type`,
        `contract_id`,
        `contract_create_time`,
        `contract_status`,
        `contract_paid_status`,
        `un_distribution_amount`,
        `transaction_amount`,
        `transaction_time`,
        `transaction_campus`,
        `un_distribution_bonus`,
        `bonus_amount`,
        `bonus_type`,
        `bonus_staff_id`,
        `bonus_dept_id`,
        `receipt_month`, 
        `receipt_date`,
        `receipt_status`,`main_id`,`funds_change_id`,`PRODUCT_TYPE`)
        SELECT 
            o3.id groupId,
            o2.id branchId,
            o.id campusId,
            s.id studentId,
            s.school schoolId ,
            s.grade_id gradeId,
            'LIVE_CONTRACT',
            NULL,
            NULL,
            NULL,
            NULL,
            0,
            CASE WHEN pd.finance_type='INCOME' AND pd.contact_type = 'NEW' THEN  pd.campus_achievement ELSE 0 END,
            pd.payment_date,
            pd.order_campusId,
            0,
            pd.campus_achievement,
            CASE WHEN pd.finance_type = 'INCOME' THEN 'NORMAL' WHEN pd.finance_type = 'REFUND' THEN 'FEEDBACK_REFUND' ELSE ''  END,
            (SELECT user_id FROM USER WHERE employee_No = pd.user_employeeNo  LIMIT 1),
            pd.order_campusId,
            yearMonth,
            receiptDate,
            'NOT_AUDIT',
            CONCAT(o.id,'_',REPLACE(yearMonth,'-','')),NULL,'LIVE'
        FROM live_payment_record pd  
        LEFT JOIN student s ON pd.student_id = s.ID
	LEFT JOIN organization o ON o.id = pd.order_campusId	
	LEFT JOIN organization o2 ON o2.id=o.parentId
	LEFT JOIN organization o3 ON o3.id=o2.parentId
	WHERE pd.payment_date LIKE CONCAT(yearMonth,'%') AND finance_type IN ('INCOME','REFUND')
	AND o.id NOT IN(SELECT campus_id FROM ods_month_campus_achievement_main_student WHERE receipt_month=yearMonth AND receipt_status <> 'NOT_AUDIT');
END
$$

--changeset duanmenrun:1940——7--1
--comment 校区业绩凭证资源

INSERT INTO `resource`
(`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RURL`, `RTAG`, `RCONTENT`, `STATE_TYPE`, `TITLE`, `create_time`, `create_user`) VALUES
('campusAchievementMain', 'MENU', '校区业绩凭证', '0', 'RES0000000092', '199', 'function/reportforms/campusAchievementMain.html', NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findCampusAchievementMainByMonth','ANON_RES','OdsMonthCampusAchievementController/findCampusAchievementMainByMonth.do');
                                             

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('flushCampusAchievementById','ANON_RES','OdsMonthCampusAchievementController/flushCampusAchievementById.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findAchievementMainInfoById','ANON_RES','OdsMonthCampusAchievementController/findAchievementMainInfoById.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('auditCampusAchievement','ANON_RES','OdsMonthCampusAchievementController/auditCampusAchievement.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('rollbackCampusAchievement','ANON_RES','OdsMonthCampusAchievementController/rollbackCampusAchievement.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('updateAchievementMainInfo','ANON_RES','OdsMonthCampusAchievementController/updateAchievementMainInfo.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findAchievementStudentByMonth','ANON_RES','OdsMonthCampusAchievementController/findAchievementMainStudentByMonth.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findOdsMonthAchievementPrintById','ANON_RES','OdsMonthCampusAchievementController/findOdsMonthCampusAchievementMainPrintById.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('deleteAchievementModifyInfo','ANON_RES','OdsMonthCampusAchievementController/deleteAchievementModifyInfo.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('saveAchievementModifyInfo','ANON_RES','OdsMonthCampusAchievementController/saveAchievementModifyInfo.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('findModifyInfoByMainId','ANON_RES','OdsMonthCampusAchievementController/findModifyInfoByMainId.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('campusAchievementDetailModal','ANON_RES','function/reportforms/campusAchievementDetailModal.html');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('exportCampusAchievementMainExcel','ANON_RES','OdsMonthCampusAchievementController/exportCampusAchievementMainExcel.do');
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('exportAchievementStudentExcel','ANON_RES','OdsMonthCampusAchievementController/exportCampusAchievementMainStudentExcel.do');

INSERT INTO resource (ID,RTYPE,RURL) VALUES ('loadCampusFundsAuditRate','ANON_RES','OdsMonthCampusAchievementController/loadCampusFundsAuditRate.do');


--changeset duanmenrun:1940——8--1
--comment 新增直播同步标识
DROP TABLE IF EXISTS live_sync_sign;
CREATE TABLE `live_sync_sign` (
		`id` VARCHAR (50),
		`record_id` VARCHAR (32),
		PRIMARY KEY (id)
	)COMMENT='直播同步标识'; 

--changeset duanmenrun:1940——9
--comment 直播流水role_ql_config
INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_1','直播合作流水','校区主任看到自己校区的','ROL0000000008','r.order_campusId in (select id  from organization  where orgLevel like \'${blCampusId}%\')','or','sql','FALSE');


INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_2','直播合作流水','校区主任要看到自己签单的，无论是不是本校区','ROL0000000008','u.USER_ID =\'${userId}\'','or','sql','FALSE');


INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_3','直播合作流水','会计走组织架构','ROL0000000061','r.order_campusId in (select id  from organization o  where ${foreachOrganization} )','or','sql','FALSE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_4','直播合作流水','咨询师只能看见自己签单的','4028818d46e1abf10146e1abf3fd0001','u.USER_ID =\'${userId}\'','or','sql','FALSE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_5','直播合作流水','财务走组织架构','ROL0000000105','r.order_campusId in (select id  from organization o  where ${foreachOrganization} )','or','sql','FALSE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_6','直播合作流水','只能看见本人所属校区的数据',NULL,'r.order_campusId in (select id  from organization  where orgLevel like \'${blCampusId}%\')','or','sql','TRUE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_7','直播合作流水','只能看见本人所属校区的数据','','r.order_campusId in (select id  from organization  where ${foreachOrganization})','or','sql','TRUE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_8','直播合作流水','过滤掉系统基本权限','ROL0000000146','1=2','or','sql','FALSE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_9','直播合作流水','学管师只能看见自己签单的','ROL0000000001','u.USER_ID =\'${userId}\'','or','sql','FALSE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_10','直播合作流水','学管主管只能看到自己签单的','ROL0000000007','u.USER_ID =\'${userId}\'','or','sql','FALSE');

INSERT INTO role_ql_config (ID,NAME,DESCRIPTION,ROLE_ID,VALUE,JOINER,TYPE,IS_OTHER_ROLE) 
VALUES ('getLivePaymentRecordList_11','直播合作流水','老师看不见所有合同','ROL00000002','1=0','or','sql','FALSE');


--changeset duanmenrun:2007--1
--comment  live_transfer_pay新增字段索引

ALTER TABLE live_transfer_pay ADD COLUMN campus_Id varchar(32) NULL  COMMENT '校区id';
ALTER TABLE live_transfer_pay ADD INDEX idx_trxid ( `trxid` );
