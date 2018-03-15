--liquibase formatted sql

--changeset tangyuping:5079-1
--comment 学生列表->查询合同->修改合同权限配置
INSERT INTO resource VALUES('STUDENT_LIST_EDITCONTRACT','BUTTON','学生列表查询合同并修改',0,'BTN_OPEN_QRY_CONTRACT_PAGE',0,NULL,'STUDENT_LIST_EDITCONTRACT',NULL,NULL,NULL,NULL,NULL);

--changeset tangyuping:4914-1
--comment 批量更换课程年级
INSERT INTO resource (ID,RTYPE,RURL) VALUES ('batchChangeCourseGrade','ANON_RES','/CourseController/batchChangeCourseGrade.do');

-- changeset tangyuping:5266-1
-- comment 添加第一次考勤时间
ALTER TABLE mini_class_student_attendent ADD COLUMN FIRST_ATTENDENT_TIME VARCHAR(20);
ALTER TABLE otm_class_student_attendent ADD COLUMN FIRST_ATTENDENT_TIME VARCHAR(20);
ALTER TABLE course ADD COLUMN FIRST_ATTENDENT_TIME VARCHAR(20);


-- changeset tangyuping:4955-1
-- comment 过季小班清理
INSERT INTO resource (ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL) VALUE ('cleanOutExpiredMiniclass', 'MENU', '过季小班清理', 0, '7', 10, 'function/system/cleanOutExpiredMiniclass.html');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('cleanOutExpiredSmallClass', 'ANON_RES', '/SystemAction/cleanOutExpiredSmallClass.do');


-- changeset tangyuping:4955-2 endDelimiter:\$\$
-- comment 过季小班清理功能，存储过程每天6:15跑批
$$
CREATE PROCEDURE proc_clean_out_expired_miniclass(IN product_version VARCHAR(32), IN product_quarter varchar(32))
BEGIN
	DECLARE contractproductId VARCHAR(32);
	DECLARE contractId VARCHAR(32);
	DECLARE productId VARCHAR(32);
	DECLARE studentId VARCHAR(32);
	DECLARE paidQuantity INT;
	DECLARE consumeQuantity INT;
	DECLARE paidAmount DECIMAL(10,2);
	DECLARE promotionAmount DECIMAL(10,2);
	DECLARE consumeAmount DECIMAL(10,2);
	DECLARE productType VARCHAR(32);
	DECLARE blCampus VARCHAR(32);

	DECLARE fundsId VARCHAR(32);
	

	DECLARE cur1 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c  
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT <= cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';

	DECLARE cur2 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c   
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT > cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';


	 DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = true;

    SET @CURSOR_STOP_FLAG =false;
		OPEN cur1;
			FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount + promotionAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							);

				UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

				IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
						THEN
							UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
				END IF;
	
				END WHILE;
			FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
		CLOSE cur1;


		SET @CURSOR_STOP_FLAG =false;
		OPEN cur2;
			FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'TRANSFER_NORMAL_TO_ELECT_ACC',
								blCampus,
								NULL,
								'REAL',
								NOW()
							);

				SET fundsId = (SELECT CONCAT('FUN',( LPAD((select nextval('funds_change_history') ID), 10, 0))));

				INSERT INTO funds_change_history 
						(id,
						student_id,
						transaction_time,
						transaction_amount,
						channel,
						contract_id,
						CHARGE_USER_ID,
						remark)
				VALUES
						(
							fundsId,
							studentId,
							NOW(),
							0,
							'REFUND_MONEY',
							contractId,
							'112233',
							'过季小班产品清理退费到电子账户'
						);

					UPDATE studnet_acc_mv SET ELECTRONIC_ACCOUNT = ELECTRONIC_ACCOUNT + (paidAmount - consumeAmount) where STUDENT_ID = studentId;

					INSERT INTO `student_return_fee`
						(`ID`,
						`STUDENT_ID`,
						`CONTRACT_ID`,
						`CONTRACT_PRODUCT_ID`,
						`RETURN_NORMAL_AMOUNT`,
						`RETURN_PROMOTION_AMOUNT`,
						`RETURN_AMOUNT`,
						`RETURN_TYPE`,
						`account_name`,
						`account`,
						`RETURN_REASON`,
						`CREATE_USER_ID`,
						`CREATE_TIME`,
						`CAMPUS`,
						`FUNDS_CHANGE_ID`)
					VALUES 
						 ((SELECT CONCAT('STU',( LPAD((select nextval('student_return_fee') ID), 10, 0)))),
							 studentId,
							 contractId,
							 contractproductId,
							 paidAmount - consumeAmount,
							 0,
							 paidAmount - consumeAmount,
							 'DAT0000000250',
							 '小班产品过季清理',
							 '小班产品过季清理',	
							 '小班产品过季清理',
							 '112233',
								NOW(),
								blCampus,
								fundsId
						 );

				INSERT INTO electronic_account_change_log 
						(ID,
						STUDENT_MV_ID,
						CHANGE_TYPE,
						CHANGE_AMOUNT,
						AFTER_AMOUNT,
						CHANGE_TIME,
						CHANGE_USER_ID,REMARK
						)
				VALUES 
						((select replace(UUID(),'-','')),
							studentId,
							'A',
							paidAmount - consumeAmount,
							paidAmount - consumeAmount,
							NOW(),
							'112233',
							'小班过季产品清理'
						);


				INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								promotionAmount,
								0,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							)	;			

					UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

					IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
							THEN
								UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
					END IF;

				END WHILE;
			FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
		CLOSE cur2;

END;
$$

-- changeset tangyuping:4955-3 endDelimiter:\$\$
-- comment 过季小班清理功能，存储过程每天6:15跑批
DROP PROCEDURE IF EXISTS proc_clean_out_expired_miniclass;
$$
CREATE PROCEDURE proc_clean_out_expired_miniclass(IN product_version VARCHAR(32), IN product_quarter varchar(32))
BEGIN
	DECLARE contractproductId VARCHAR(32);
	DECLARE contractId VARCHAR(32);
	DECLARE productId VARCHAR(32);
	DECLARE studentId VARCHAR(32);
	DECLARE paidQuantity INT;
	DECLARE consumeQuantity INT;
	DECLARE paidAmount DECIMAL(10,2);
	DECLARE promotionAmount DECIMAL(10,2);
	DECLARE consumeAmount DECIMAL(10,2);
	DECLARE productType VARCHAR(32);
	DECLARE blCampus VARCHAR(32);

	DECLARE fundsId VARCHAR(32);
	

	DECLARE cur1 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c  
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT <= cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';

	DECLARE cur2 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c   
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT > cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';


	 DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = true;

    SET @CURSOR_STOP_FLAG =false;
		OPEN cur1;
			FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount + promotionAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							);

				UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

				IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
						THEN
							UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
				END IF;

				FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
			
				END WHILE;
			
		CLOSE cur1;


		SET @CURSOR_STOP_FLAG = false;
		OPEN cur2;
			FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'TRANSFER_NORMAL_TO_ELECT_ACC',
								blCampus,
								NULL,
								'REAL',
								NOW()
							);

				

				INSERT INTO electronic_account_change_log 
						(ID,
						STUDENT_MV_ID,
						CHANGE_TYPE,
						CHANGE_AMOUNT,
						AFTER_AMOUNT,
						CHANGE_TIME,
						CHANGE_USER_ID,REMARK
						)
				VALUES 
						((select replace(UUID(),'-','')),
							studentId,
							'A',
							paidAmount - consumeAmount,
							paidAmount - consumeAmount,
							NOW(),
							'112233',
							'小班过季产品清理'
						);


				INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								promotionAmount,
								0,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							)	;			

					
					SET fundsId = (SELECT CONCAT('FUN',( LPAD((select nextval('funds_change_history') ID), 10, 0))));

					INSERT INTO funds_change_history 
							(id,
							student_id,
							transaction_time,
							transaction_amount,
							channel,
							contract_id,
							CHARGE_USER_ID,
							remark)
					VALUES
							(
								fundsId,
								studentId,
								NOW(),
								0,
								'REFUND_MONEY',
								contractId,
								'112233',
								'过季小班产品清理退费到电子账户'
							);

					UPDATE studnet_acc_mv SET ELECTRONIC_ACCOUNT = ELECTRONIC_ACCOUNT + (paidAmount - consumeAmount) where STUDENT_ID = studentId;

					INSERT INTO `student_return_fee`
						(`ID`,
						`STUDENT_ID`,
						`CONTRACT_ID`,
						`CONTRACT_PRODUCT_ID`,
						`RETURN_NORMAL_AMOUNT`,
						`RETURN_PROMOTION_AMOUNT`,
						`RETURN_AMOUNT`,
						`RETURN_TYPE`,
						`account_name`,
						`account`,
						`RETURN_REASON`,
						`CREATE_USER_ID`,
						`CREATE_TIME`,
						`CAMPUS`,
						`FUNDS_CHANGE_ID`)
					VALUES 
						 ((SELECT CONCAT('STU',( LPAD((select nextval('student_return_fee') ID), 10, 0)))),
							 studentId,
							 contractId,
							 contractproductId,
							 paidAmount - consumeAmount,
							 0,
							 paidAmount - consumeAmount,
							 'DAT0000000250',
							 '小班产品过季清理',
							 '小班产品过季清理',	
							 '小班产品过季清理',
							 '112233',
								NOW(),
								blCampus,
								fundsId
						 );

					UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

					IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
							THEN
								UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
					END IF;
	
					FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
		
				END WHILE;
			
		CLOSE cur2;

END;
$$

-- changeset tangyuping:4955-4
-- comment 删除多余的触发器
DROP TRIGGER IF EXISTS funds_change_history_after_ins_day;
DROP TRIGGER IF EXISTS  contract_product_after_ins_student_day;
DROP TRIGGER IF EXISTS contract_product_after_upd_student_day;
DROP TRIGGER IF EXISTS contract_product_after_del_student_day;

-- changeset tangyuping:4955-5 endDelimiter:\$\$
-- comment 过季小班清理功能，存储过程每天6:15跑批,避免funds_charge_history主键冲突
DROP PROCEDURE IF EXISTS proc_clean_out_expired_miniclass;
$$
CREATE PROCEDURE proc_clean_out_expired_miniclass(IN product_version VARCHAR(32), IN product_quarter varchar(32))
BEGIN
	DECLARE contractproductId VARCHAR(32);
	DECLARE contractId VARCHAR(32);
	DECLARE productId VARCHAR(32);
	DECLARE studentId VARCHAR(32);
	DECLARE paidQuantity INT;
	DECLARE consumeQuantity INT;
	DECLARE paidAmount DECIMAL(10,2);
	DECLARE promotionAmount DECIMAL(10,2);
	DECLARE consumeAmount DECIMAL(10,2);
	DECLARE productType VARCHAR(32);
	DECLARE blCampus VARCHAR(32);

	DECLARE fundsId VARCHAR(32);
	

	DECLARE cur1 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c  
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT <= cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';

	DECLARE cur2 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c   
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT > cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';


	 DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = true;

    SET @CURSOR_STOP_FLAG =false;
		OPEN cur1;
			FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount + promotionAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							);

				UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

				IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
						THEN
							UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
				END IF;

				FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
			
				END WHILE;
			
		CLOSE cur1;


		SET @CURSOR_STOP_FLAG = false;
		OPEN cur2;
			FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'TRANSFER_NORMAL_TO_ELECT_ACC',
								blCampus,
								NULL,
								'REAL',
								NOW()
							);

				

				INSERT INTO electronic_account_change_log 
						(ID,
						STUDENT_MV_ID,
						CHANGE_TYPE,
						CHANGE_AMOUNT,
						AFTER_AMOUNT,
						CHANGE_TIME,
						CHANGE_USER_ID,REMARK
						)
				VALUES 
						((select replace(UUID(),'-','')),
							studentId,
							'A',
							paidAmount - consumeAmount,
							paidAmount - consumeAmount,
							NOW(),
							'112233',
							'小班过季产品清理'
						);


				INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								promotionAmount,
								0,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							)	;			

					
					SET fundsId = (SELECT CONCAT('FUNC',( LPAD((select nextval('funds_change_history') ID), 9, 0))));

					INSERT INTO funds_change_history 
							(id,
							student_id,
							transaction_time,
							transaction_amount,
							channel,
							contract_id,
							CHARGE_USER_ID,
							remark)
					VALUES
							(
								fundsId,
								studentId,
								NOW(),
								0,
								'REFUND_MONEY',
								contractId,
								'112233',
								'过季小班产品清理退费到电子账户'
							);

					UPDATE studnet_acc_mv SET ELECTRONIC_ACCOUNT = ELECTRONIC_ACCOUNT + (paidAmount - consumeAmount) where STUDENT_ID = studentId;

					INSERT INTO `student_return_fee`
						(`ID`,
						`STUDENT_ID`,
						`CONTRACT_ID`,
						`CONTRACT_PRODUCT_ID`,
						`RETURN_NORMAL_AMOUNT`,
						`RETURN_PROMOTION_AMOUNT`,
						`RETURN_AMOUNT`,
						`RETURN_TYPE`,
						`account_name`,
						`account`,
						`RETURN_REASON`,
						`CREATE_USER_ID`,
						`CREATE_TIME`,
						`CAMPUS`,
						`FUNDS_CHANGE_ID`)
					VALUES 
						 ((SELECT CONCAT('STU',( LPAD((select nextval('student_return_fee') ID), 10, 0)))),
							 studentId,
							 contractId,
							 contractproductId,
							 paidAmount - consumeAmount,
							 0,
							 paidAmount - consumeAmount,
							 'DAT0000000250',
							 '小班产品过季清理',
							 '小班产品过季清理',	
							 '小班产品过季清理',
							 '112233',
								NOW(),
								blCampus,
								fundsId
						 );

					UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

					IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
							THEN
								UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
					END IF;
	
					FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
		
				END WHILE;
			
		CLOSE cur2;

END;
$$
-- changeset tangyuping:4955-6 endDelimiter:\$\$
-- comment 过季小班清理功能，存储过程每天6:15跑批,不需要添加扣费记录
DROP PROCEDURE IF EXISTS proc_clean_out_expired_miniclass;
$$
CREATE PROCEDURE proc_clean_out_expired_miniclass(IN product_version VARCHAR(32), IN product_quarter varchar(32))
BEGIN
	DECLARE contractproductId VARCHAR(32);
	DECLARE contractId VARCHAR(32);
	DECLARE productId VARCHAR(32);
	DECLARE studentId VARCHAR(32);
	DECLARE paidQuantity INT;
	DECLARE consumeQuantity INT;
	DECLARE paidAmount DECIMAL(10,2);
	DECLARE promotionAmount DECIMAL(10,2);
	DECLARE consumeAmount DECIMAL(10,2);
	DECLARE productType VARCHAR(32);
	DECLARE blCampus VARCHAR(32);

	DECLARE fundsId VARCHAR(32);
	

	DECLARE cur1 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c  
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT <= cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';

	DECLARE cur2 CURSOR FOR select cp.id,cp.CONTRACT_ID,cp.PRODUCT_ID,c.STUDENT_ID,cp.QUANTITY,cp.CONSUME_QUANTITY,cp.PAID_AMOUNT,cp.PROMOTION_AMOUNT,cp.CONSUME_AMOUNT,cp.TYPE,c.BL_CAMPUS_ID  from contract_product cp,product p,contract c   
													where cp.PRODUCT_ID = p.id and cp.CONTRACT_ID = c.id and  cp.`STATUS` = 'NORMAL' AND cp.PAID_STATUS = 'PAID' AND cp.TYPE = 'SMALL_CLASS' 
													and p.PRODUCT_VERSION_ID = product_version and p.PRODUCT_QUARTER_ID = product_quarter and cp.PAID_AMOUNT > cp.CONSUME_AMOUNT and cp.`STATUS`<>'ENDED';


	 DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CURSOR_STOP_FLAG = true;

    SET @CURSOR_STOP_FLAG =false;
		OPEN cur1;
			FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount + promotionAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							);

				UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

				IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
						THEN
							UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
				END IF;

				FETCH cur1 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
			
				END WHILE;
			
		CLOSE cur1;


		SET @CURSOR_STOP_FLAG = false;
		OPEN cur2;
			FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
				WHILE !@CURSOR_STOP_FLAG do	
					
					INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								paidAmount - consumeAmount,
								paidQuantity - consumeQuantity,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'TRANSFER_NORMAL_TO_ELECT_ACC',
								blCampus,
								NULL,
								'REAL',
								NOW()
							);				

				INSERT INTO electronic_account_change_log 
						(ID,
						STUDENT_MV_ID,
						CHANGE_TYPE,
						CHANGE_AMOUNT,
						AFTER_AMOUNT,
						CHANGE_TIME,
						CHANGE_USER_ID,REMARK
						)
				VALUES 
						((select replace(UUID(),'-','')),
							studentId,
							'A',
							paidAmount - consumeAmount,
							paidAmount - consumeAmount,
							NOW(),
							'112233',
							'小班过季产品清理'
						);


				INSERT INTO `account_charge_records`
							(`ID`,
							`CONTRACT_PRODUCT_ID`,
							`CONTRACT_ID`,
							`AMOUNT`,
							`QUANTITY`,
							`PAY_TIME`,
							`PRODUCT_ID`,
							`OPERATE_USER_ID`,
							`STUDENT_ID`,
							`PRODUCT_TYPE`,
							`CHARGE_TYPE`,
							`BL_CAMPUS_ID`,
							`TRANSACTION_ID`,
							`PAY_TYPE`,
							`TRANSACTION_TIME`) 
					VALUES 
							((select replace(UUID(),'-','')),
								contractproductId,
								contractId,
								promotionAmount,
								0,
								NOW(),
								productId,
								'112233',
								studentId,
								productType,
								'IS_NORMAL_INCOME',
								blCampus,
								(select replace(UUID(),'-','')),
								'PROMOTION',
								NOW()
							)	;					

					UPDATE studnet_acc_mv SET ELECTRONIC_ACCOUNT = ELECTRONIC_ACCOUNT + (paidAmount - consumeAmount) where STUDENT_ID = studentId;		

					UPDATE contract_product SET STATUS = 'CLOSE_PRODUCT' where id = contractproductId;

					IF NOT EXISTS (select id from contract_product where CONTRACT_ID = contractId and (STATUS='NORMAL' or status='STARTED'))
							THEN
								UPDATE contract SET CONTRACT_STATUS = 'FINISHED' where id = contractId;
					END IF;
	
					FETCH cur2 into contractproductId, contractId, productId, studentId, paidQuantity, consumeQuantity, paidAmount, promotionAmount, consumeAmount, productType, blCampus;
		
				END WHILE;
			
		CLOSE cur2;

END;
$$

-- changeset tangyuping:5331-1
-- comment 删除考勤记录中第一次考勤时间，添加在课程表中
ALTER TABLE mini_class_student_attendent DROP COLUMN FIRST_ATTENDENT_TIME;
ALTER TABLE otm_class_student_attendent DROP COLUMN FIRST_ATTENDENT_TIME;
ALTER TABLE mini_class_course ADD COLUMN FIRST_ATTENDENT_TIME VARCHAR(20);
ALTER TABLE otm_class_course ADD COLUMN FIRST_ATTENDENT_TIME VARCHAR(20);

-- changeset tangyuping:5301-1
-- comment 产品表添加字段
ALTER TABLE PRODUCT ADD MAX_PROMOTION_DISCOUNT DECIMAL(4,2) DEFAULT NULL;

-- changeset tangyuping:5306-1
-- comment 添加一对一本周课表取消课程权限
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('ONE_ON_ONE_CANCEL_COURSE','BUTTON','取消一对一课程',0,'RES0000000003',0,'CANCEL_ONE_COURSE');

-- changeset tangyuping:5368-1
-- comment 小班，一对多课程取消
ALTER TABLE mini_class ADD COLUMN CANCELED_HOURS DECIMAL(9,2) DEFAULT 0;
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('SMALL_CLASS_CANCEL_COURSE','BUTTON','取消小班课程',0,'RES0000000002',0,'CANCEL_SMALL_CLASS_COURSE');
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('OTM_CLASS_CANCEL_COURSE','BUTTON','取消一对多课程',0,'otmClassCourseManage',0,'CANCEL_OTM_CLASS_COURSE');

-- changeset tangyuping:5368-2 endDelimiter:\$\$
-- comment 小班，一对多课程取消 修改触发器

DROP TRIGGER IF EXISTS mini_class_course_upd;
$$
CREATE TRIGGER mini_class_course_upd
AFTER UPDATE ON `mini_class_course`
FOR EACH ROW
BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CURSOR cursor for select mcs.STUDENT_ID,s.BL_CAMPUS_ID from mini_class_student mcs left join student s on mcs.STUDENT_ID = s.ID where MINI_CLASS_ID = NEW.MINI_CLASS_ID and mcs.FIRST_SCHOOL_TIME <= NEW.COURSE_DATE;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;
SET @CONFLICT_TEACHER_ID = NEW.TEACHER_ID;

delete from course_conflict where course_id = OLD.MINI_CLASS_COURSE_ID and mini_class_id = OLD.MINI_CLASS_ID;

OPEN CONFLICT_CURSOR;
	conflict_mini_class_course:LOOP
		FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID,CONFLICT_BL_CAMPUS_ID;
		IF @CONFLICT_CURSOR_STOP THEN
			LEAVE conflict_mini_class_course;
		END IF;
		IF new.COURSE_STATUS <> 'CANCEL' THEN
			insert into course_conflict(course_id,mini_class_id,start_time,end_time,teacher_id,student_id,bl_campus_id) values(
				NEW.MINI_CLASS_COURSE_ID,
				NEW.MINI_CLASS_ID,
				concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
				concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
				@CONFLICT_TEACHER_ID,
				CONFLICT_STUDENT_ID,
				CONFLICT_BL_CAMPUS_ID 
			);
	END IF;

	END LOOP;
CLOSE CONFLICT_CURSOR; 

END
$$

-- changeset tangyuping:5368-3 endDelimiter:\$\$
-- comment 小班，一对多课程取消 修改触发器

DROP TRIGGER IF EXISTS course_after_upd;
$$
CREATE TRIGGER course_after_upd
AFTER UPDATE ON `course`
FOR EACH ROW
BEGIN      

	SET @OLD_ONE_ON_ONE_ARRANGED_HOUR = 0 ;

    	SELECT
    		IFNULL(ONE_ON_ONE_ARRANGED_HOUR, 0)
    	FROM
    		STUDNET_ACC_MV
    	WHERE
    		STUDENT_ID = NEW.STUDENT_ID
    	INTO
    		@OLD_ONE_ON_ONE_ARRANGED_HOUR ;
    	SET @NEW_ONE_ON_ONE_ARRANGED_HOUR = @OLD_ONE_ON_ONE_ARRANGED_HOUR + NEW.PLAN_HOURS - OLD.PLAN_HOURS;

    	UPDATE STUDNET_ACC_MV SET
    		ONE_ON_ONE_ARRANGED_HOUR =  @NEW_ONE_ON_ONE_ARRANGED_HOUR
    	WHERE STUDENT_ID = NEW.STUDENT_ID ;

   
    SET @CONFLICT_BL_CAMPUS_ID = null;

    IF NOT EXISTS (select 1 from course_conflict where course_id = NEW.COURSE_ID) THEN
                                     select BL_CAMPUS_ID into @CONFLICT_BL_CAMPUS_ID from student where ID = NEW.STUDENT_ID;
                                     insert into course_conflict(course_id,mini_class_id,start_time,end_time,teacher_id,student_id,bl_campus_id) values(
    			NEW.COURSE_ID,
    			null,
    			concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
                                              concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,9,2),substr(NEW.COURSE_TIME,12,2)),
    			NEW.TEACHER_ID,
    			NEW.STUDENT_ID,
    			@CONFLICT_BL_CAMPUS_ID
    		);
    ELSE
                                     update course_conflict set
                                              START_TIME = concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
                                              END_TIME = concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,9,2),substr(NEW.COURSE_TIME,12,2)),
                                              TEACHER_ID = NEW.TEACHER_ID,
                                              STUDENT_ID = NEW.STUDENT_ID
                                              where COURSE_ID = OLD.COURSE_ID and MINI_CLASS_ID is null;
    END IF;

    IF NEW.COURSE_STATUS = 'TEACHER_LEAVE' or NEW.COURSE_STATUS = 'STUDENT_LEAVE' or NEW.COURSE_STATUS = 'CANCEL' THEN
        delete from course_conflict where course_id = OLD.COURSE_ID and MINI_CLASS_ID is null;
    END IF;
END
$$

-- changeset tangyuping:5368-4 endDelimiter:\$\$
-- comment 小班，一对多课程取消 修改触发器

DROP TRIGGER IF EXISTS otm_class_course_upd;
$$
CREATE TRIGGER otm_class_course_upd
AFTER UPDATE ON `otm_class_course`
FOR EACH ROW
BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CURSOR cursor for select ocs.STUDENT_ID,s.BL_CAMPUS_ID from otm_class_student ocs left join student s on ocs.STUDENT_ID = s.ID where OTM_CLASS_ID = NEW.OTM_CLASS_ID;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;
SET @CONFLICT_TEACHER_ID = NEW.TEACHER_ID;

delete from course_conflict where course_id = OLD.OTM_CLASS_COURSE_ID and otm_class_id = OLD.OTM_CLASS_ID;

OPEN CONFLICT_CURSOR;
	conflict_otm_class_course:LOOP
		FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID,CONFLICT_BL_CAMPUS_ID;
		IF @CONFLICT_CURSOR_STOP THEN
			LEAVE conflict_otm_class_course;
		END IF;
		IF new.COURSE_STATUS <> 'CANCEL' THEN
				insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id,otm_class_id) values(
					NEW.OTM_CLASS_COURSE_ID,
					concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
					concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
					@CONFLICT_TEACHER_ID,
					CONFLICT_STUDENT_ID,
					CONFLICT_BL_CAMPUS_ID,
					NEW.OTM_CLASS_ID
				);
		END IF;
	END LOOP;
CLOSE CONFLICT_CURSOR;  
END
$$

-- changeset tangyuping:5310-1
-- comment 学生详情修改学生信息权限
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('BTN_EDIT_STUDENT_INFO','BUTTON','修改学生信息',0,'89',0,'EDIT_STUDENT_INFO');
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('BTN_ONE_EDIT_STUDENT_INFO','BUTTON','修改学生信息',0,'oneOnOneStudentList',0,'EDIT_ONE_STUDENT_INFO');
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('BTN_OTM_EDIT_STUDENT_INFO','BUTTON','修改学生信息',0,'otmStudentList',0,'EDIT_OTM_STUDENT_INFO');
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('BTN_PROMISE_EDIT_STUDENT_INFO','BUTTON','修改学生信息',0,'promiseStudentList',0,'EDIT_PROMISE_STUDENT_INFO');
INSERT INTO resource (ID,RTYPE,RNAME,HAS_CHILDREN,PARENT_ID,RORDER,RTAG) VALUES('BTN_SMALLCLASS_EDIT_STUDENT_INFO','BUTTON','修改学生信息',0,'smallStudentList',0,'EDIT_SMALLCLASS_STUDENT_INFO');


-- changeset tangyuping:5307-1
-- comment 设置课程表里面的审批状态
UPDATE course SET AUDIT_STATUS = 'UNAUDIT' where AUDIT_STATUS IS NULL AND COURSE_STATUS <> 'CHARGED';
UPDATE mini_class_course SET AUDIT_STATUS = 'UNAUDIT' where AUDIT_STATUS IS NULL AND COURSE_STATUS <> 'CHARGED';
UPDATE otm_class_course SET AUDIT_STATUS = 'UNAUDIT' where AUDIT_STATUS IS NULL AND COURSE_STATUS <> 'CHARGED';


-- changeset tangyuping:5320-1
-- comment 添加产品高级权限
UPDATE resource SET RNAME ='产品基本维护权限'  WHERE ID = 'BINS_EDIT_PRODUCT';
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUES ('TOP_BINS_EDIT_PRODUCT','BUTTON','产品高级维护权限',0,'66',0,'TOP_EDIT_PRODUCT');


