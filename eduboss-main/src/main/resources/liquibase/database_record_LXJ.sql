--liquibase formatted sql

--changeset yaoyuqi:904_1
--comment 1对1教师星级优化
UPDATE teacher_version SET TEACHER_LEVEL = 'NORAMAL_H3' WHERE TEACHER_LEVEL IN ('NORMAL_STAR_SIXTH', 'NORMAL_STAR_SEVENTH', 'NORMAL_GOLD_MEDAL');

--changeset lixuejun:901-1 endDelimiter:\$\$
--comment 学生剩余资金报表取数优化
DROP PROCEDURE IF EXISTS `proc_update_ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING`;

$$
CREATE PROCEDURE `proc_update_ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING`(
IN in_target_date varchar(10))
    SQL SECURITY INVOKER
BEGIN

            DELETE FROM `ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING` WHERE COUNT_DATE = in_target_date;    

            INSERT INTO `ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING` (
                    GROUP_ID,
                    BRANCH_ID,
                    CAMPUS_ID,
                    COUNT_DATE,
                    STUDY_MANAGER_ID,
                    STUDENT_ID,

                    ONE_ON_ONE_REMAINING_HOUR,
                    ONE_ON_ONE_REMAINING_FUNDING,
                    ONE_ON_ONE_REMAINING_GIFTED_HOUR,
                    ONE_ON_ONE_REMAINING_GIFTED_FUNDING,
                    ONE_ON_ONE_REAL_REMAINING_HOUR,
                    ONE_ON_ONE_REAL_REMAINING_FUNDING,
                 
                              OTM_CLASS_REMAINING_HOUR,
                              OTM_CLASS_REMAINING_FUNDING,
                              OTM_CLASS_REMAINING_GIFTED_HOUR,
                              OTM_CLASS_REMAINING_GIFTED_FUNDING,
                              OTM_CLASS_REAL_REMAINING_HOUR,
                              OTM_CLASS_REAL_REMAINING_FUNDING,
                    
                    SMALL_CLASS_REMAINING_HOUR,
                    SMALL_CLASS_REMAINING_FUNDING,
                    SMALL_CLASS_REMAINING_GIFTED_HOUR,
                    SMALL_CLASS_REMAINING_GIFTED_FUNDING,
                    SMALL_CLASS_REAL_REMAINING_HOUR,
                    SMALL_CLASS_REAL_REMAINING_FUNDING,
                   
                    PROMISED_CLASS_REAL_REMAINING_FUNDING,
                    PROMISED_CLASS_REMAINING_GIFTED_FUNDING,
                    PROMISED_CLASS_REMAINING_FUNDING,
                    
                    LECTURE_REAL_REMAINING_FUNDING,
                    LECTURE_REMAINING_GIFTED_FUNDING,
                    LECTURE_REMAINING_FUNDING,
                    

                    OTHER_REMAINING_FUNDING,
                                        OTHER_REMAINING_GIFTED_FUNDING,
                                        OTHER_REAL_REMAINING_FUNDING,

                    ACTUAL_REMAINING_FUNDING,
                    GIFTED_REMAINING_FUNDING,
                    TOTAL_REMAINING_FUNDING,





                              ELECTRONIC_ACCOUNT
                )

              SELECT
                org_group.ID as GROUP_ID,
                org_branch.ID as BRANCH_ID,
                stu.BL_CAMPUS_ID as CAMPUS_ID,
                in_target_date,
                stu.STUDY_MANEGER_ID as STUDY_MANAGER_ID,
                con.STUDENT_ID as STUDENT_ID,

                convert(SUM(case when TYPE = 'ONE_ON_ONE_COURSE' and  (CP.STATUS<>'CLOSE_PRODUCT'and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN (cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT)/cp.PRICE ELSE (cp.PAID_AMOUNT-cp.CONSUME_AMOUNT)/cp.PRICE END) else 0 end),decimal(10,2)) as ONE_ON_ONE_REMAINING_HOUR,
                SUM(case when TYPE = 'ONE_ON_ONE_COURSE' and  (CP.STATUS<>'CLOSE_PRODUCT'and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end) as ONE_ON_ONE_REMAINING_FUNDING,
                convert(SUM(case when TYPE = 'ONE_ON_ONE_COURSE'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT)/cp.price else 0 end ),decimal(10,2)) as ONE_ON_ONE_REMAINING_GIFTED_HOUR,
                SUM(case when TYPE = 'ONE_ON_ONE_COURSE'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT) else 0 end ) as ONE_ON_ONE_REMAINING_GIFTED_FUNDING,
                convert(SUM(case when TYPE = 'ONE_ON_ONE_COURSE'   and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT)/cp.PRICE else 0 end ),decimal(10,2)) as ONE_ON_ONE_REAL_REMAINING_HOUR,
                SUM(case when TYPE = 'ONE_ON_ONE_COURSE'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT) else 0 end ) as ONE_ON_ONE_REAL_REMAINING_FUNDING,
                
                convert(SUM(case when TYPE = 'ONE_ON_MANY'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END)/cp.PRICE else 0 end ),decimal(10,2)) as OTM_CLASS_REMAINING_HOUR,
                SUM(case when TYPE = 'ONE_ON_MANY'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end) as OTM_CLASS_REMAINING_FUNDING,
                convert(SUM(case when TYPE = 'ONE_ON_MANY' and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT)/cp.price else 0 end ),decimal(10,2)) as OTM_CLASS_REMAINING_GIFTED_HOUR,
                SUM(case when TYPE = 'ONE_ON_MANY'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT) else 0 end ) as OTM_CLASS_REMAINING_GIFTED_FUNDING,
                                convert(SUM(case when TYPE = 'ONE_ON_MANY'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT)/cp.PRICE else 0 end ),decimal(10,2)) as OTM_CLASS_REAL_REMAINING_HOUR,
                SUM(case when TYPE = 'ONE_ON_MANY'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT) else 0 end ) as OTM_CLASS_REAL_REMAINING_FUNDING,
               
                convert(SUM(case when TYPE = 'SMALL_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END)/cp.PRICE else 0 end ),decimal(10,2)) as SMALL_CLASS_REMAINING_HOUR,
                SUM(case when TYPE = 'SMALL_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end) as SMALL_CLASS_REMAINING_FUNDING,
                convert(SUM(case when TYPE = 'SMALL_CLASS' and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT)/cp.price else 0 end ),decimal(10,2)) as SMALL_CLASS_REMAINING_GIFTED_HOUR,
                SUM(case when TYPE = 'SMALL_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT) else 0 end ) as SMALL_CLASS_REMAINING_GIFTED_FUNDING,
                convert(SUM(case when TYPE = 'SMALL_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT)/cp.PRICE else 0 end ),decimal(10,2)) as SMALL_CLASS_REAL_REMAINING_HOUR,
                SUM(case when TYPE = 'SMALL_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT) else 0 end ) as SMALL_CLASS_REAL_REMAINING_FUNDING,
                
                SUM(case when TYPE = 'ECS_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end ) as PROMISED_CLASS_REMAINING_FUNDING,
                SUM(case when TYPE = 'ECS_CLASS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT) else 0 end ) as PROMISED_CLASS_REMAINING_GIFTED_FUNDING,
                SUM(case when TYPE = 'ECS_CLASS' and (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT) else 0 end ) as PROMISED_CLASS_REAL_REMAINING_FUNDING ,
                
                SUM(case when TYPE = 'LECTURE'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end ) as LECTURE_REMAINING_FUNDING,
                SUM(case when TYPE = 'LECTURE'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT) else 0 end ) as LECTURE_REMAINING_GIFTED_FUNDING,
                SUM(case when TYPE = 'LECTURE' and (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT) else 0 end ) as LECTURE_REAL_REMAINING_FUNDING ,
                
                                SUM(case when TYPE = 'OTHERS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (CASE when cp.PAID_STATUS='PAID' THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT ELSE cp.PAID_AMOUNT-cp.CONSUME_AMOUNT END) else 0 end ) as OTHER_REMAINING_FUNDING,
                SUM(case when TYPE = 'OTHERS'  and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PROMOTION_AMOUNT - cp.PROMOTION_CONSUME_AMOUNT) else 0 end ) as OTHER_REMAINING_GIFTED_FUNDING,
                SUM(case when TYPE = 'OTHERS' and (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then (cp.PAID_AMOUNT - cp.REAL_CONSUME_AMOUNT) else 0 end ) as OTHER_REAL_REMAINING_FUNDING ,
                               
                SUM(case when cp.PAID_AMOUNT-cp.CONSUME_AMOUNT>0   and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') then cp.PAID_AMOUNT-cp.CONSUME_AMOUNT else 0 end ) 
                                    + CASE WHEN sam.ELECTRONIC_ACCOUNT>0 then sam.ELECTRONIC_ACCOUNT ELSE 0 END as ACTUAL_REMAINING_FUNDING,
               
                SUM(CASE when cp.PAID_STATUS='PAID'   and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') and cp.PAID_AMOUNT-cp.CONSUME_AMOUNT>=0 THEN cp.PROMOTION_AMOUNT
                                when cp.PAID_STATUS='PAID'and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED') and cp.PAID_AMOUNT-cp.CONSUME_AMOUNT<0 then cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT
                                ELSE 0 END) as GIFTED_REMAINING_FUNDING,
                                
                sum(CASE when cp.PAID_STATUS='PAID'   and  (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED' )
                                    THEN cp.PAID_AMOUNT+cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT
                                when  cp.PAID_STATUS='PAYING'and (CP.STATUS<>'CLOSE_PRODUCT' and CP.STATUS <> 'REFUNDED' AND cp.`STATUS`<>'ENDED')
                 then cp.PAID_AMOUNT-cp.CONSUME_AMOUNT
                 ELSE 0 END) + CASE WHEN sam.ELECTRONIC_ACCOUNT>0 then sam.ELECTRONIC_ACCOUNT ELSE 0 END  as TOTAL_REMAINING_FUNDING,


                                CASE WHEN sam.ELECTRONIC_ACCOUNT>0 then sam.ELECTRONIC_ACCOUNT ELSE 0 END as ELECTRONIC_ACCOUNT

            FROM `contract_product` cp
            INNER JOIN `contract` con ON cp.CONTRACT_ID = con.ID
            INNER JOIN `student` stu ON stu.ID = con.STUDENT_ID
                        INNER JOIN studnet_acc_mv sam on stu.ID=sam.STUDENT_ID
            INNER JOIN `organization` org_campus on org_campus.ID = stu.BL_CAMPUS_ID
            INNER JOIN `organization` org_branch on org_campus.parentID = org_branch.id
            INNER JOIN `organization` org_group on org_branch.parentID = org_group.id
            GROUP BY GROUP_ID, BRANCH_ID, CAMPUS_ID, STUDY_MANAGER_ID, STUDENT_ID;


             update ODS_DAY_REPORT_STUDENT_SURPLUS_FUNDING ods
            left join (
            select student_id,sum(amount) amount from (SELECT 
                c.ID AS id,
                c.STUDENT_ID AS STUDENT_ID,
                (c.PAID_AMOUNT - SUM(cp.PAID_AMOUNT)) AS AMOUNT
            FROM
                contract c
                LEFT JOIN contract_product cp ON c.ID = cp.CONTRACT_ID
            WHERE
                c.CONTRACT_STATUS = 'NORMAL'
            GROUP BY c.ID , c.STUDENT_ID) a where a.amount>0 group by student_id) s on ods.STUDENT_ID=s.student_id
            set ods.UN_DISTRIBUTION_AMOUNT=s.amount
            where COUNT_DATE = in_target_date  and s.amount>0;
END
$$

--changeset lixuejun:1098_1
--comment 【合同列表】-【银联支付数据管理】-【已导入数据】页面优化
UPDATE role_ql_config SET `VALUE` = 'ppd.blCampus.id in (select id from Organization organization where ${foreachOrganizationHql})' WHERE ID = 'posPayDataList';

--changeset lixuejun:1098_2
--comment 【合同列表】-【银联支付数据管理】-【已导入数据】页面优化
UPDATE pos_machine_manage pmm, pos_machine pm SET pmm.POS_TYPE = pm.POS_TYPE WHERE pmm.POS_NUMBER = pm.POS_NUMBER AND pm.POS_TYPE IS NOT NULL;

--changeset lixuejun:1136_1
--comment 退费流程审批添加职位判断
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('checkAuditAuthority', 'ANON_RES', '/RefundWorkflowController/checkAuditAuthority.do');

--changeset lixuejun:1137_1
--comment 邮件对接优化
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('delMailAddrByDisableUsers', 'ANON_RES', '/Mail/delMailAddrByDisableUsers.do');

--changeset lixuejun:1510_3
--comment 学生课程表、老师课程表、老师学生课表查询三个页面加入双师课程信息
alter table `course_conflict` add column class_two_id  int(11) DEFAULT NULL COMMENT '双师辅班编号';

--changeset lixuejun:1510_4 endDelimiter:\$\$
--comment 学生课程表、老师课程表、老师学生课表查询三个页面加入双师课程信息
DROP TRIGGER IF EXISTS `two_teacher_class_course_ins`;
$$
CREATE TRIGGER `two_teacher_class_course_ins` AFTER INSERT ON `two_teacher_class_course` FOR EACH ROW BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);
DECLARE CONFLICT_CURSOR cursor for select ttcs.STUDENT_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student ttcs 
    left join two_teacher_class_two ttct on ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID  
    left join student s on ttcs.STUDENT_ID = s.ID where ttc.CLASS_TWO_ID = NEW.CLASS_ID;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;
select TEACHER_ID into CONFLICT_TEACHER_ID from two_teacher_class_two where CLASS_ID = NEW.CLASS_ID;

OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID,CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR; 

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_upd`;
$$
CREATE TRIGGER `two_teacher_class_course_upd` AFTER UPDATE ON `two_teacher_class_course` FOR EACH ROW BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);
DECLARE OLD_CLASS_TWO_ID int(11);
DECLARE CONFLICT_CURSOR cursor for select ttcs.STUDENT_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student ttcs 
    left join two_teacher_class_two ttct on ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID  
    left join student s on ttcs.STUDENT_ID = s.ID where ttc.CLASS_TWO_ID = NEW.CLASS_ID;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;
select TEACHER_ID into CONFLICT_TEACHER_ID from two_teacher_class_two where CLASS_ID = NEW.CLASS_ID;
select CLASS_TWO_ID into OLD_CLASS_TWO_ID from two_teacher_class_two where CLASS_ID = OLD.CLASS_ID;

delete from course_conflict where course_id = OLD.COURSE_ID and class_two_id = OLD_CLASS_TWO_ID;

OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID,CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR;  

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_del`;
$$
CREATE TRIGGER `two_teacher_class_course_del` AFTER DELETE ON `two_teacher_class_course` FOR EACH ROW BEGIN
DECLARE OLD_CLASS_TWO_ID int(11);
select CLASS_TWO_ID into OLD_CLASS_TWO_ID from two_teacher_class_two where CLASS_ID = OLD.CLASS_ID;

delete from course_conflict where course_id = OLD.COURSE_ID and class_two_id = OLD_CLASS_TWO_ID;

END
$$

--changeset lixuejun:1510_5
--comment 学生课程表、老师课程表、老师学生课表查询三个页面加入双师课程信息
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getTwoTeacherCourseScheduleList', 'ANON_RES', '/TwoTeacherClassController/getTwoTeacherCourseScheduleList.do');

--changeset lixuejun:1510_6 endDelimiter:\$\$
--comment 学生课程表、老师课程表、老师学生课表查询三个页面加入双师课程信息
DROP TRIGGER IF EXISTS `two_teacher_class_course_ins`;
$$
CREATE TRIGGER `two_teacher_class_course_ins` AFTER INSERT ON `two_teacher_class_course` FOR EACH ROW BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);
DECLARE CONFLICT_CURSOR cursor for select ttcs.STUDENT_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student ttcs 
    left join two_teacher_class_two ttct on ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID  
    left join student s on ttcs.STUDENT_ID = s.ID where ttc.CLASS_ID = NEW.CLASS_ID;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;
select TEACHER_ID into CONFLICT_TEACHER_ID from two_teacher_class_two where CLASS_ID = NEW.CLASS_ID;

OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID,CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR; 

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_upd`;
$$
CREATE TRIGGER `two_teacher_class_course_upd` AFTER UPDATE ON `two_teacher_class_course` FOR EACH ROW BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);
DECLARE OLD_CLASS_TWO_ID int(11);
DECLARE CONFLICT_CURSOR cursor for select ttcs.STUDENT_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student ttcs 
    left join two_teacher_class_two ttct on ttcs.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join two_teacher_class ttc on ttct.CLASS_ID = ttc.CLASS_ID  
    left join student s on ttcs.STUDENT_ID = s.ID where ttc.CLASS_ID = NEW.CLASS_ID;
DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;
select TEACHER_ID into CONFLICT_TEACHER_ID from two_teacher_class_two where CLASS_ID = NEW.CLASS_ID;
select CLASS_TWO_ID into OLD_CLASS_TWO_ID from two_teacher_class_two where CLASS_ID = OLD.CLASS_ID;

delete from course_conflict where course_id = OLD.COURSE_ID and class_two_id = OLD_CLASS_TWO_ID;

OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID,CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR;  

END
$$

--changeset lixuejun:1519_1
--comment 双师课程汇总页面增加计算课时报表导出
update resource set HAS_CHILDREN = 1 WHERE ID = 'twoTeacherCourseCollect';
insert into resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE('mainClassExcel', 'BUTTON', '主讲老师课时汇总', 0, 'twoTeacherCourseCollect', 1, 'TWO_TEACHER_MAIN_TOEXCEL_BTN');
insert into resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE('auxiliaryClassExcel', 'BUTTON', '辅导老师课时汇总', 0, 'twoTeacherCourseCollect', 2, 'WO_TEACHER_AUXILIARY_TOEXCEL_BTN');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('exportTwoTeacherMain', 'ANON_RES', '/ReportAction/exportTwoTeacherMainClassesReport.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('exportTwoTeacherAuxiliary', 'ANON_RES', '/ReportAction/exportTwoTeacherAuxiliaryClassesReport.do');

--changeset lixuejun:1510_7 endDelimiter:\$\$
--comment 学生课程表、老师课程表、老师学生课表查询三个页面加入双师课程信息
DROP TRIGGER IF EXISTS `two_teacher_class_course_ins`;
$$
CREATE TRIGGER `two_teacher_class_course_ins` AFTER INSERT ON `two_teacher_class_course` FOR EACH ROW BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);

DECLARE CONFLICT_CURSOR cursor for select distinct ttcsa.STUDENT_ID, ttct.TEACHER_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class_two ttct on ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONFLICT_CURSOR2 cursor for select distinct ttcsa.STUDENT_ID, ttc.TEACHER_ID, s.BL_CAMPUS_ID, ttc.CLASS_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class ttc on ttcsa.CLASS_TWO_ID = NEW.CLASS_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;



OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR; 

SET @CONFLICT_CURSOR_STOP=false;

OPEN CONFLICT_CURSOR2;
    conflict_two_teacher_class_course2:LOOP
        FETCH CONFLICT_CURSOR2 into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course2;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR2; 

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_upd`;
$$
CREATE TRIGGER `two_teacher_class_course_upd` AFTER UPDATE ON `two_teacher_class_course` FOR EACH ROW BEGIN 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);

DECLARE CONFLICT_CURSOR cursor for select distinct ttcsa.STUDENT_ID, ttct.TEACHER_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class_two ttct on ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONFLICT_CURSOR2 cursor for select distinct ttcsa.STUDENT_ID, ttc.TEACHER_ID, s.BL_CAMPUS_ID, ttc.CLASS_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class ttc on ttcsa.CLASS_TWO_ID = NEW.CLASS_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;

delete from course_conflict where course_id = OLD.COURSE_ID;

OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR; 

SET @CONFLICT_CURSOR_STOP=false;

OPEN CONFLICT_CURSOR2;
    conflict_two_teacher_class_course2:LOOP
        FETCH CONFLICT_CURSOR2 into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course2;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR2; 

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_del`;
$$
CREATE TRIGGER `two_teacher_class_course_del` AFTER DELETE ON `two_teacher_class_course` FOR EACH ROW BEGIN
delete from course_conflict where course_id = OLD.COURSE_ID;
END
$$

--changeset lixuejun:1510_8 endDelimiter:\$\$
--comment 学生课程表、老师课程表、老师学生课表查询三个页面加入双师课程信息
DROP TRIGGER IF EXISTS `two_teacher_class_course_ins`;
$$
CREATE TRIGGER `two_teacher_class_course_ins` AFTER INSERT ON `two_teacher_class_course` FOR EACH ROW BEGIN 
 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);

DECLARE CONFLICT_CURSOR cursor for select distinct ttcsa.STUDENT_ID, ttct.TEACHER_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class_two ttct on ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONFLICT_CURSOR2 cursor for select distinct ttcsa.STUDENT_ID, ttc.TEACHER_ID, s.BL_CAMPUS_ID, ttc.CLASS_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class ttc on ttc.CLASS_ID = NEW.CLASS_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;



OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR; 

SET @CONFLICT_CURSOR_STOP=false;

OPEN CONFLICT_CURSOR2;
    conflict_two_teacher_class_course2:LOOP
        FETCH CONFLICT_CURSOR2 into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course2;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR2; 

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_upd`;
$$
CREATE TRIGGER `two_teacher_class_course_upd` AFTER UPDATE ON `two_teacher_class_course` FOR EACH ROW BEGIN 
DECLARE CONFLICT_STUDENT_ID varchar(32);
DECLARE CONFLICT_BL_CAMPUS_ID varchar(32);
DECLARE CONFLICT_CLASS_TWO_ID int(11);
DECLARE CONFLICT_TEACHER_ID varchar(32);

DECLARE CONFLICT_CURSOR cursor for select distinct ttcsa.STUDENT_ID, ttct.TEACHER_ID, s.BL_CAMPUS_ID, ttct.CLASS_TWO_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class_two ttct on ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONFLICT_CURSOR2 cursor for select distinct ttcsa.STUDENT_ID, ttc.TEACHER_ID, s.BL_CAMPUS_ID, ttc.CLASS_ID from two_teacher_class_student_attendent ttcsa 
    left join two_teacher_class ttc on ttc.CLASS_ID = NEW.CLASS_ID 
    left join student s on ttcsa.STUDENT_ID = s.ID where ttcsa.TWO_CLASS_COURSE_ID = NEW.COURSE_ID;

DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET @CONFLICT_CURSOR_STOP=true;
SET @CONFLICT_CURSOR_STOP=false;

delete from course_conflict where course_id = OLD.COURSE_ID;

OPEN CONFLICT_CURSOR;
    conflict_two_teacher_class_course:LOOP
        FETCH CONFLICT_CURSOR into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR; 

SET @CONFLICT_CURSOR_STOP=false;

OPEN CONFLICT_CURSOR2;
    conflict_two_teacher_class_course2:LOOP
        FETCH CONFLICT_CURSOR2 into CONFLICT_STUDENT_ID, CONFLICT_TEACHER_ID, CONFLICT_BL_CAMPUS_ID, CONFLICT_CLASS_TWO_ID;
        IF @CONFLICT_CURSOR_STOP THEN
            LEAVE conflict_two_teacher_class_course2;
        END IF;
        insert into course_conflict(course_id,start_time,end_time,teacher_id,student_id,bl_campus_id, class_two_id) values(
            NEW.COURSE_ID,
            concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_TIME,1,2),substr(NEW.COURSE_TIME,4,2)),
      concat(replace(NEW.COURSE_DATE,'-',''),substr(NEW.COURSE_END_TIME,1,2),substr(NEW.COURSE_END_TIME,4,2)),
            CONFLICT_TEACHER_ID,
            CONFLICT_STUDENT_ID,
            CONFLICT_BL_CAMPUS_ID,
            CONFLICT_CLASS_TWO_ID
        );

    END LOOP;
CLOSE CONFLICT_CURSOR2; 

END
$$


DROP TRIGGER IF EXISTS `two_teacher_class_course_del`;
$$
CREATE TRIGGER `two_teacher_class_course_del` AFTER DELETE ON `two_teacher_class_course` FOR EACH ROW BEGIN
delete from course_conflict where course_id = OLD.COURSE_ID;
END
$$

--changeset lixuejun:1600_1 endDelimiter:\$\$
--comment 营业收入统计、营收凭证，直播累计规则优化
DROP PROCEDURE IF EXISTS `proc_ods_month_income_student`;

$$
CREATE PROCEDURE `proc_ods_month_income_student`(
IN in_start_date varchar(10),IN in_end_date varchar(10),IN in_mapping_date varchar(20))
    SQL SECURITY INVOKER
BEGIN
            DELETE FROM ods_month_income_student WHERE COUNT_DATE = in_end_date;
            INSERT INTO ods_month_income_student(GROUP_ID, BRENCH_ID, CAMPUS_ID, STUDENT_ID, ONE_ON_ONE_REAL_AMOUNT, ONE_ON_ONE_PROMOTION_AMOUNT, ONE_ON_ONE_REAL_WASH_AMOUNT, ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
                                    SMALL_CLASS_REAL_AMOUNT, SMALL_CLASS_PROMOTION_AMOUNT, SMALL_CLASS_REAL_WASH_AMOUNT, SMALL_CLASS_PROMOTION_WASH_AMOUNT,
                                    TWO_TEACHER_REAL_AMOUNT, TWO_TEACHER_PROMOTION_AMOUNT, TWO_TEACHER_REAL_WASH_AMOUNT, TWO_TEACHER_PROMOTION_WASH_AMOUNT,
                                    LIVE_REAL_AMOUNT, LIVE_PROMOTION_AMOUNT, LIVE_REAL_WASH_AMOUNT, LIVE_PROMOTION_WASH_AMOUNT,
                                    ECS_CLASS_REAL_AMOUNT, ECS_CLASS_PROMOTION_AMOUNT, ECS_CLASS_REAL_WASH_AMOUNT, ECS_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTM_CLASS_REAL_AMOUNT, OTM_CLASS_PROMOTION_AMOUNT, OTM_CLASS_REAL_WASH_AMOUNT, OTM_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTHERS_REAL_AMOUNT, OTHERS_PROMOTION_AMOUNT, OTHERS_REAL_WASH_AMOUNT, OTHERS_PROMOTION_WASH_AMOUNT,
                                    LECTURE_REAL_AMOUNT, LECTURE_PROMOTION_AMOUNT, LECTURE_REAL_WASH_AMOUNT, LECTURE_PROMOTION_WASH_AMOUNT,
                                    IS_NORMAL_REAL_AMOUNT, IS_NORMAL_PROMOTION_AMOUNT, IS_NORMAL_HISTORY_WASH_AMOUNT,
                                    COUNT_DATE, MAPPING_DATE, EVIDENCE_AUDIT_STATUS)
            SELECT
            org_group.id GROUP_ID, org_brench.id BRENCH_ID, o.id CAMPUS_ID, s.ID as STUDENT_ID,
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount 
                                ELSE 0 END) AS ONE_ON_ONE_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS SMALL_CLASS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_PROMOTION_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -(amount/2.0) ELSE 0 END) AS LIVE_REAL_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -(amount/2.0) ELSE 0 END) AS LIVE_PROMOTION_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -(amount/2.0)
                                ELSE 0 END) AS LIVE_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -(amount/2.0)
                                ELSE 0 END) AS LIVE_PROMOTION_WASH_AMOUNT,

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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ECS_CLASS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTM_CLASS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTHERS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS LECTURE_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS IS_NORMAL_HISTORY_WASH_AMOUNT,
                        in_end_date, in_mapping_date, 'NOT_AUDIT'
        from account_charge_records acr  
        inner join
            organization o 
                on acr.BL_CAMPUS_ID = o.id
                inner join organization org_brench
                                on o.parentID = org_brench.id
                inner join organization org_group
                                on org_brench.parentID = org_group.id
        left join
            student s 
                on acr.STUDENT_ID = s.ID
                left join income_mapping_date imd 
                                on acr.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
        WHERE
            1=1
            AND acr.amount > 0  
            AND (
                CHARGE_TYPE = 'NORMAL' 
                or CHARGE_TYPE = 'IS_NORMAL_INCOME'
            )
                        AND ((acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59')
                        AND acr.PAY_TIME <= in_mapping_date AND acr.CHARGE_PAY_TYPE = 'CHARGE') 
                      OR (acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date  AND acr.CHARGE_PAY_TYPE = 'WASH')
                        OR (acr.TRANSACTION_TIME <= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME > CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME < CONCAT(in_end_date, ' 23:59:59')))
                        AND imd.ACCOUNT_CHARGE_RECORD_ID IS NULL
        group by
            o.id,
            s.ID;

                DELETE FROM income_mapping_date WHERE COUNT_MONTH = CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)); 
                INSERT INTO income_mapping_date SELECT ID, CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)), acr.BL_CAMPUS_ID from account_charge_records acr
                    left join income_mapping_date imd 
                                on acr.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
                     WHERE 1=1
            AND acr.amount > 0  
            AND (
                CHARGE_TYPE = 'NORMAL' 
                or CHARGE_TYPE = 'IS_NORMAL_INCOME'
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
IN in_start_date varchar(10),IN in_end_date varchar(10),IN in_mapping_date varchar(20),IN in_campus_id varchar(32))
    SQL SECURITY INVOKER
BEGIN
            DELETE FROM ods_month_income_student WHERE COUNT_DATE = in_end_date AND CAMPUS_ID = in_campus_id;
            INSERT INTO ods_month_income_student(GROUP_ID, BRENCH_ID, CAMPUS_ID, STUDENT_ID, ONE_ON_ONE_REAL_AMOUNT, ONE_ON_ONE_PROMOTION_AMOUNT, ONE_ON_ONE_REAL_WASH_AMOUNT, ONE_ON_ONE_PROMOTION_WASH_AMOUNT,
                                    SMALL_CLASS_REAL_AMOUNT, SMALL_CLASS_PROMOTION_AMOUNT, SMALL_CLASS_REAL_WASH_AMOUNT, SMALL_CLASS_PROMOTION_WASH_AMOUNT,
                                    TWO_TEACHER_REAL_AMOUNT, TWO_TEACHER_PROMOTION_AMOUNT, TWO_TEACHER_REAL_WASH_AMOUNT, TWO_TEACHER_PROMOTION_WASH_AMOUNT,
                                    LIVE_REAL_AMOUNT, LIVE_PROMOTION_AMOUNT, LIVE_REAL_WASH_AMOUNT, LIVE_PROMOTION_WASH_AMOUNT,
                                    ECS_CLASS_REAL_AMOUNT, ECS_CLASS_PROMOTION_AMOUNT, ECS_CLASS_REAL_WASH_AMOUNT, ECS_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTM_CLASS_REAL_AMOUNT, OTM_CLASS_PROMOTION_AMOUNT, OTM_CLASS_REAL_WASH_AMOUNT, OTM_CLASS_PROMOTION_WASH_AMOUNT,
                                    OTHERS_REAL_AMOUNT, OTHERS_PROMOTION_AMOUNT, OTHERS_REAL_WASH_AMOUNT, OTHERS_PROMOTION_WASH_AMOUNT,
                                    LECTURE_REAL_AMOUNT, LECTURE_PROMOTION_AMOUNT, LECTURE_REAL_WASH_AMOUNT, LECTURE_PROMOTION_WASH_AMOUNT,
                                    IS_NORMAL_REAL_AMOUNT, IS_NORMAL_PROMOTION_AMOUNT, IS_NORMAL_HISTORY_WASH_AMOUNT,
                                    COUNT_DATE, MAPPING_DATE, EVIDENCE_AUDIT_STATUS)
            SELECT
            org_group.id GROUP_ID, org_brench.id BRENCH_ID, o.id CAMPUS_ID, s.ID as STUDENT_ID,
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount 
                                ELSE 0 END) AS ONE_ON_ONE_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_ONE_COURSE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS SMALL_CLASS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'SMALL_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'TWO_TEACHER' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS TWO_TEACHER_PROMOTION_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' 
                                        AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -(amount/2.0) ELSE 0 END) AS LIVE_REAL_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' 
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') AND acr.PAY_TIME <= in_mapping_date THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION'
                                AND acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                        AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date THEN -amount ELSE 0 END) AS LIVE_PROMOTION_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -(amount/2.0)
                                ELSE 0 END) AS LIVE_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount/2.0 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LIVE' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -(amount/2.0)
                                ELSE 0 END) AS LIVE_PROMOTION_WASH_AMOUNT,

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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS ECS_CLASS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ECS_CLASS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTM_CLASS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'ONE_ON_MANY' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS OTHERS_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'WASH' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'OTHERS' AND CHARGE_PAY_TYPE = 'CHARGE' AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'REAL' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS LECTURE_REAL_WASH_AMOUNT,

                        SUM(CASE WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'WASH'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'NORMAL' AND PRODUCT_TYPE = 'LECTURE' AND CHARGE_PAY_TYPE = 'CHARGE'  AND PAY_TYPE = 'PROMOTION' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
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
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN amount 
                                WHEN CHARGE_TYPE = 'IS_NORMAL_INCOME' AND CHARGE_PAY_TYPE = 'CHARGE' AND acr.TRANSACTION_TIME < CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME > CONCAT(IFNULL(SUBSTR((SELECT MAPPING_DATE FROM ods_month_income_student WHERE COUNT_DATE = last_day(acr.TRANSACTION_TIME) AND STUDENT_ID = s.ID LIMIT 1) FROM 1 FOR 10), last_day(acr.TRANSACTION_TIME)), ' 00:00:00') THEN -amount
                                ELSE 0 END) AS IS_NORMAL_HISTORY_WASH_AMOUNT,
                        in_end_date, in_mapping_date, 'NOT_AUDIT'
        from account_charge_records acr  
        inner join
            organization o 
                on acr.BL_CAMPUS_ID = o.id
                inner join organization org_brench
                                on o.parentID = org_brench.id
                inner join organization org_group
                                on org_brench.parentID = org_group.id
        left join
            student s 
                on acr.STUDENT_ID = s.ID
                left join income_mapping_date imd 
                                on acr.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
        WHERE
            1=1
            AND acr.amount > 0
                        AND o.id = in_campus_id
            AND (
                CHARGE_TYPE = 'NORMAL' 
                or CHARGE_TYPE = 'IS_NORMAL_INCOME'
            )
                        AND ((acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59')
                        AND acr.PAY_TIME <= in_mapping_date AND acr.CHARGE_PAY_TYPE = 'CHARGE') 
                      OR (acr.TRANSACTION_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.TRANSACTION_TIME <= CONCAT(in_end_date, ' 23:59:59') 
                                AND acr.PAY_TIME >= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME <= in_mapping_date  AND acr.CHARGE_PAY_TYPE = 'WASH')
                        OR (acr.TRANSACTION_TIME <= CONCAT(in_start_date, ' 00:00:00') AND acr.PAY_TIME > CONCAT(in_start_date, ' 00:00:00') 
                                AND acr.PAY_TIME < CONCAT(in_end_date, ' 23:59:59')))
                        AND imd.ACCOUNT_CHARGE_RECORD_ID IS NULL
        group by
            o.id,
            s.ID;

                DELETE FROM income_mapping_date WHERE COUNT_MONTH = CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)) AND BL_CAMPUS_ID = in_campus_id; 
                INSERT INTO income_mapping_date SELECT ID, CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2)), acr.BL_CAMPUS_ID from account_charge_records acr
                        left join income_mapping_date imd 
                                on acr.id = imd.ACCOUNT_CHARGE_RECORD_ID AND imd.COUNT_MONTH != CONCAT(SUBSTR(in_end_date FROM 1 FOR 4), SUBSTR(in_end_date FROM 6 FOR 2))
                     WHERE 1=1
            AND acr.amount > 0
                        AND acr.BL_CAMPUS_ID= in_campus_id
            AND (
                acr.CHARGE_TYPE = 'NORMAL' 
                or acr.CHARGE_TYPE = 'IS_NORMAL_INCOME'
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

--changeset lixuejun:1607_2
--comment 呼入电话连接boss系统
CREATE TABLE `disabled_customer` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `contact` varchar(11) NOT NULL COMMENT '联系方式',
  `remark` varchar(100) NOT NULL COMMENT '备注',
    `udpate_enabled` tinyint(1) DEFAULT 0 COMMENT '0未更新为有效，1已更新为有效',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_CONTACT` (`contact`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='无效客户表';

--changeset lixuejun:1607_3
--comment 呼入电话连接boss系统
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getDisCusForJqGrid', 'ANON_RES', '/CustomerAction/getDisCusForJqGrid.do');

--changeset lixuejun:1505_1
--comment 直播更改客户手机号同步修改
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getVerification', 'ANON_RES', '/CommonAction/getVerification.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('checkVerification', 'ANON_RES', '/CommonAction/checkVerification.do');

--changeset lixuejun:1751_1
--comment BOSS成绩管理
DROP TABLE IF EXISTS achievement_benchmark;
DROP TABLE IF EXISTS achievement_comparison;
DROP TABLE IF EXISTS achievement_pro_standard;
DROP TABLE IF EXISTS achievement_template;
DROP TABLE IF EXISTS achievement_template_subject;
DROP TABLE IF EXISTS achievement_template_grade;
DROP TABLE IF EXISTS achievement_category;
DROP TABLE IF EXISTS achievement_category_score;
DROP TABLE IF EXISTS student_achievement;
DROP TABLE IF EXISTS student_achievement_subject;

CREATE TABLE achievement_benchmark
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    achievement_comparison_id INTEGER NOT NULL COMMENT '成绩对比记录编号',
    student_achievement_id INTEGER NOT NULL COMMENT '基准学生成绩编号',
    benchmark_subject_id INTEGER NOT NULL COMMENT '基准学生成绩科目编号',
    benchmark_achievement_type VARCHAR(32) COMMENT '基准成绩类型',
    benchmark_category VARCHAR(32) NOT NULL COMMENT '基准成绩类别',
    compare_subject_id INTEGER COMMENT '对比学生成绩科目编号',
    compare_achievement_type VARCHAR(32) COMMENT '对比成绩类型',
    compare_category VARCHAR(32) COMMENT '对比成绩类别',
    standard_category VARCHAR(32) COMMENT '进步类别',
    benchmark_type VARCHAR(32) COMMENT '基准判定类型',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_COMPARISION_STUDENT_SUBJECT_ID` (`achievement_comparison_id`,`student_achievement_id`, `benchmark_subject_id`)
) COMMENT='成绩基准科目'
;


CREATE TABLE achievement_comparison
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  student_id VARCHAR(32) NOT NULL COMMENT '学生编号',
    school_year VARCHAR(32) NOT NULL COMMENT '学年',
    semester VARCHAR(32) NOT NULL COMMENT '学期',
    subject_ids VARCHAR(250) COMMENT '在读一对一科目编号',
    subject_names VARCHAR(250) COMMENT '在读一对一科目名称',
    comparative_achievement_id INTEGER COMMENT '对比成绩',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_STUDENT_SCHOOL_YEAR_SEMESTER` (`student_id`, `school_year`,`semester`)
)  COMMENT='成绩对比记录'
;

CREATE TABLE achievement_pro_standard
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(32) NOT NULL COMMENT '名称',
    standard_category VARCHAR(32) NOT NULL COMMENT '标准类别',
  greater_type VARCHAR(32) COMMENT '大于类型',
    score_between_start INTEGER COMMENT '分数相差起',
    less_type VARCHAR(32) COMMENT '小于类型',
    score_between_end INTEGER COMMENT '分数相差止',
    class_ranking_btw_start INTEGER COMMENT '班级排名相差起',
    class_ranking_btw_end INTEGER COMMENT '班级排名相差止',
    grade_ranking_btw_start INTEGER COMMENT '年级排名相差起',
    grade_ranking_btw_end INTEGER COMMENT '年级排名相差止',
    evaluation_list VARCHAR(10) COMMENT '评级列表',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id)
)  COMMENT='成绩进步标准'
;

CREATE TABLE achievement_template
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    city_id INTEGER NOT NULL COMMENT '城市编号',
    effective_date VARCHAR(10) COMMENT '生效日期',
    achievement_version TINYINT NOT NULL COMMENT '模板版本',
    is_current_version TINYINT NOT NULL DEFAULT 1 COMMENT '是否当前版本1:否，0：是',
    is_deleted TINYINT NOT NULL DEFAULT 1 COMMENT '是否删除1:否，0：是',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id)
)  COMMENT='成绩模板'
;

CREATE TABLE achievement_template_subject
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    template_grade_id INTEGER NOT NULL COMMENT '成绩模板年级编号',
    subject_id VARCHAR(32) NOT NULL COMMENT '科目编号',
    total_score INTEGER NOT NULL COMMENT '总分',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_GRADE_SUBJECT_ID` (`template_grade_id`,`subject_id`)
)  COMMENT='成绩模板科目'
;

CREATE TABLE achievement_template_grade
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    achievement_template_id INTEGER NOT NULL COMMENT '成绩模板编号',
    grade_id VARCHAR(32) NOT NULL COMMENT '年级',
    achievement_type_list VARCHAR(50) NOT NULL COMMENT '成绩类型列表',
    subject_num TINYINT NOT NULL DEFAULT 0 COMMENT '科目数量',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_TEMPLATE_GRADE_ID` (`achievement_template_id`,`grade_id`)
)  COMMENT='成绩模板年级'
;


CREATE TABLE achievement_category
(
  id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(20) NOT NULL COMMENT  '名称',
    class_ranking_start INTEGER NOT NULL DEFAULT 0 COMMENT '班级排名起',
    class_ranking_end INTEGER NOT NULL DEFAULT 0 COMMENT '班级名次止',
    grade_ranking_start INTEGER NOT NULL DEFAULT 0 COMMENT '年级排名起',
    grade_ranking_end INTEGER NOT NULL DEFAULT 0 COMMENT '年级排名止',
    evaluation_list VARCHAR(10) COMMENT '评级列表',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_NAME` (`name`)
)  COMMENT='成绩类别'
;

CREATE TABLE achievement_category_score
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    category_id VARCHAR(32) NOT NULL COMMENT '成绩类别',
    total_score INTEGER NOT NULL DEFAULT 0 COMMENT '总分',
    score_start INTEGER NOT NULL DEFAULT 0 COMMENT '分数起',
    score_end INTEGER COMMENT '分数止',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_CATEGORY_TOTAL_SCORE` (`category_id`,`total_score`)
)  COMMENT='成绩类别分数关联表'
;

CREATE TABLE student_achievement
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    achievement_template_id INTEGER COMMENT '成绩模板编号',
    student_id VARCHAR(32) NOT NULL COMMENT '学生编号',
    school_year VARCHAR(32) NOT NULL COMMENT '学年',
    semester VARCHAR(32) NOT NULL COMMENT '学期',
    examination_type VARCHAR(32) NOT NULL COMMENT '考试类型',
    city_id INTEGER NOT NULL COMMENT '城市',
    grade_id VARCHAR(32) COMMENT '年级',
    examination_date VARCHAR(10) NOT NULL COMMENT '考试日期',
    achievement_type VARCHAR(32) COMMENT '成绩类型',
    achievement_evidence VARCHAR(50) COMMENT '成绩凭证',
  total_score DECIMAL(5,2) COMMENT '总分',
    total_class_ranking INTEGER COMMENT '总分班级排名',
    total_grade_ranking INTEGER COMMENT '总分年级排名',
    total_evaluation_type VARCHAR(32) COMMENT '总分评级',
    is_deleted TINYINT NOT NULL DEFAULT 1 COMMENT '是否删除1:否，0：是',
    subject_num TINYINT NOT NULL DEFAULT 0 COMMENT '科目数量',
    template_subject_num TINYINT NOT NULL DEFAULT 0 COMMENT '模板科目数量',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id)
    #UNIQUE KEY `UQ_YEAR_SEMESTER_TYPE_CITY_GRADE_STUDENT` (`school_year`,`semester`,`examination_type`,`city_id`,`grade_id`, `student_id`)
)  COMMENT='学生成绩'
;


CREATE TABLE student_achievement_subject
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    student_achievement_id INTEGER NOT NULL COMMENT '学生成绩编号',
    subject_id VARCHAR(32) NOT NULL COMMENT '科目',
    score DECIMAL(5,2) COMMENT '分数',
    class_ranking INTEGER COMMENT '班级排名',
    grade_ranking INTEGER COMMENT '年级排名',
    evaluation_type VARCHAR(32) COMMENT '评级',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_ACHIEVEMENT_SUBJECT` (`student_achievement_id`,`subject_id`)
)  COMMENT='学生成绩科目'
;

insert into achievement_category(`name`, class_ranking_start, class_ranking_end, grade_ranking_start, grade_ranking_end, evaluation_list, version, create_user_id, modify_user_id)
    value ('A类', 0, 5, 0, 30, 'A', 0, '112233', '112233');
insert into achievement_category(`name`, class_ranking_start, class_ranking_end, grade_ranking_start, grade_ranking_end, evaluation_list, version, create_user_id, modify_user_id)
    value ('B类', 5, 15, 30, 100, 'B', 0, '112233', '112233');
insert into achievement_category(`name`, class_ranking_start, class_ranking_end, grade_ranking_start, grade_ranking_end, evaluation_list, version, create_user_id, modify_user_id)
    value ('C类', 15, 30, 100, 150, 'C', 0, '112233', '112233');
insert into achievement_category(`name`, class_ranking_start, class_ranking_end, grade_ranking_start, grade_ranking_end, evaluation_list, version, create_user_id, modify_user_id)
    value ('D类', 30, 40, 150, 300, 'D', 0, '112233', '112233');
insert into achievement_category(`name`, class_ranking_start, class_ranking_end, grade_ranking_start, grade_ranking_end, evaluation_list, version, create_user_id, modify_user_id)
    value ('E类', 40, 999, 300, 9999, 'E', 0, '112233', '112233');

insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(1, 100, 90, 999, '112233', '112233');
insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(1, 150, 130, 999, '112233', '112233');

insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(2, 100, 70, 90, '112233', '112233');
insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(2, 150, 100, 130, '112233', '112233');

insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(3, 100, 50, 70, '112233', '112233');
insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(3, 150, 80, 100, '112233', '112233');

insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(4, 100, 40, 50, '112233', '112233');
insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(4, 150, 60, 80, '112233', '112233');

insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(5, 100, 0, 40, '112233', '112233');
insert into achievement_category_score(category_id, total_score, score_start, score_end, create_user_id, modify_user_id) 
    value(5, 150, 0, 60, '112233', '112233');

insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('A类', 'PROGRESS', 'GREATER_THAN', 0, null, null, null, -1, null, -1, null, 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('A类', 'FLAT', 'GREATER_THAN_EQUAL', 0, 'LESS_THAN_EQUAL', 0, 0, 0, 0, 0, 'A', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('A类', 'BACKWARD', null, null, 'LESS_THAN', 1, 1, null, 1, null, 'B,C,D,E', 0, '112233', '112233');

insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('B类', 'PROGRESS', 'GREATER_THAN_EQUAL', 5, null, null, null, -3, null, -3, 'A', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('B类', 'FLAT', 'GREATER_THAN_EQUAL', 0, 'LESS_THAN', 5, -2, 0, -2, 0, 'B', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('B类', 'BACKWARD', null, null, 'LESS_THAN', 0, 1, null, 1, null, 'C,D,E', 0, '112233', '112233');

insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('C类', 'PROGRESS', 'GREATER_THAN_EQUAL', 8, null, null, null, -5, null, -5, 'A,B', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('C类', 'FLAT', 'GREATER_THAN_EQUAL', 0, 'LESS_THAN', 8, -5, 0, -5, 0, 'C', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('C类', 'BACKWARD', null, null, 'LESS_THAN', 0, 1, null, 1, null, 'D,E', 0, '112233', '112233');

insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('D类', 'PROGRESS', 'GREATER_THAN_EQUAL', 8, null, null, null, -5, null, -5, 'A,B,C', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('D类', 'FLAT', 'GREATER_THAN_EQUAL', 0, 'LESS_THAN', 8, -5, 0, -5, 0, 'D', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('D类', 'BACKWARD', null, null, 'LESS_THAN', 0, 1, null, 1, null, 'E', 0, '112233', '112233');

insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('E类', 'PROGRESS', 'GREATER_THAN_EQUAL', 8, null, null, null, -5, null, -5, 'A,B,C,D', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('E类', 'FLAT', 'GREATER_THAN_EQUAL', 0, 'LESS_THAN', 8, -5, 0, -5, 0, 'E', 0, '112233', '112233');
insert into achievement_pro_standard(`name`, standard_category, greater_type, score_between_start, less_type, score_between_end, class_ranking_btw_start, class_ranking_btw_end, grade_ranking_btw_start, grade_ranking_btw_end, evaluation_list, version, create_user_id, modify_user_id)
    value('E类', 'BACKWARD', null, null, 'LESS_THAN', 0, 1, null, 1, null, null, 0, '112233', '112233');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAllAchievementCategory', 'ANON_RES', '/AchievementController/listAllAchievementCategory.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('updateAchievementCategory', 'ANON_RES', '/AchievementController/updateAchievementCategory.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAllAchievementProStandard', 'ANON_RES', '/AchievementController/listAllAchievementProStandard.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('updateAchievementProStandard', 'ANON_RES', '/AchievementController/updateAchievementProStandard.do');


INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listTemplateCityIdsByProvinceId', 'ANON_RES', '/AchievementController/listTemplateCityIdsByProvinceId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('initAchievementTemplateByCitys', 'ANON_RES', '/AchievementController/initAchievementTemplateByCitys.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findPageAchievementTemplate', 'ANON_RES', '/AchievementController/findPageAchievementTemplate.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveFullAchievementTemplate', 'ANON_RES', '/AchievementController/saveFullAchievementTemplate.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAchievementTemplateByCityId', 'ANON_RES', '/AchievementController/listAchievementTemplateByCityId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAllTemplateGradeByTemplateId', 'ANON_RES', '/AchievementController/listAllTemplateGradeByTemplateId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('deleteAchievementTemplateById', 'ANON_RES', '/AchievementController/deleteAchievementTemplateById.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findMostConsistentTemplateGrade', 'ANON_RES', '/AchievementController/findMostConsistentTemplateGrade.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveFullStudentAchievement', 'ANON_RES', '/StudentAchievementController/saveFullStudentAchievement.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findPageStudentAchievement', 'ANON_RES', '/StudentAchievementController/findPageStudentAchievement.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAllAchSubjectByAchievementId', 'ANON_RES', '/StudentAchievementController/listAllAchievementSubjectByAchievementId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getStudentAchSelectByStudentId', 'ANON_RES', '/StudentAchievementController/getStudentAchievementSelectByStudentId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('getSubjectsByBenchmark', 'ANON_RES', '/StudentAchievementController/getSubjectsByBenchmark.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findStuCityGradeVoByStudentId', 'ANON_RES', '/StudentAchievementController/findStudentCityGradeVoByStudentId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('deleteStuAchByAchievementId', 'ANON_RES', '/StudentAchievementController/deleteStudentAchievementByAchievementId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('saveFullAchComparison', 'ANON_RES', '/StudentAchievementController/saveFullAchievementComparison.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findAchComparisonDetailById', 'ANON_RES', '/StudentAchievementController/findAchievementComparisonDetailById.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAllAchComparisonByStuId', 'ANON_RES', '/StudentAchievementController/listAllAchievementComparisonByStudentId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listAllAchBenchmarkByComId', 'ANON_RES', '/StudentAchievementController/listAllAchievementBenchmarkByComparisonId.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('updateStandardCatForAchBen', 'ANON_RES', '/StudentAchievementController/updateStandardCategoryForAchievementBenchmark.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findStudentAchievementById', 'ANON_RES', '/StudentAchievementController/findStudentAchievementById.do');

INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findLastAchComparisonDetail', 'ANON_RES', '/StudentAchievementController/findLastAchievementComparisonDetail.do');
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RURL, new_url) VALUE ('studentScoreManage', 'MENU', '成绩模板管理', 0, 'RES0000000011', 1, '404.html', '/StudentScoreTemplate');
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE ('ACHIEVEMENT_CITY_BTN', 'BUTTON', '选择城市', 0, 'studentScoreManage', 1, 'ACHIEVEMENT_CITY_BTN');
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE ('ACHIEVEMENT_CATEGORY_BTN', 'BUTTON', '学生类别设置', 0, 'studentScoreManage', 2, 'ACHIEVEMENT_CATEGORY_BTN');
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE ('ACHIEVEMENT_PRO_STANDARD_BTN', 'BUTTON', '学生进步类别设置', 0, 'studentScoreManage', 3, 'ACHIEVEMENT_PRO_STANDARD_BTN');

--changeset lixuejun:1751_2
--comment BOSS成绩管理
update achievement_pro_standard set score_between_end = 0 where id = 3;

--changeset lixuejun:1751_3
--comment BOSS成绩管理
DROP TABLE IF EXISTS achievement_benchmark;
CREATE TABLE achievement_benchmark
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    achievement_comparison_id INTEGER NOT NULL COMMENT '成绩对比记录编号',
    student_achievement_id INTEGER NOT NULL COMMENT '基准学生成绩编号',
    benchmark_subject_id INTEGER NOT NULL COMMENT '基准学生成绩科目编号',
    benchmark_achievement_type VARCHAR(32) COMMENT '基准成绩类型',
    benchmark_category VARCHAR(32) NOT NULL COMMENT '基准成绩类别',
    compare_subject_id INTEGER COMMENT '对比学生成绩科目编号',
    compare_achievement_type VARCHAR(32) COMMENT '对比成绩类型',
    compare_category VARCHAR(32) COMMENT '对比成绩类别',
    standard_category VARCHAR(32) COMMENT '进步类别',
    benchmark_type VARCHAR(32) COMMENT '基准判定类型',
    read_only_desc varchar(200) default null COMMENT '仅作记录描述',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_COMPARISION_STUDENT_SUBJECT_ID` (`achievement_comparison_id`,`student_achievement_id`, `benchmark_subject_id`)
) COMMENT='成绩基准科目'
;

DROP TABLE IF EXISTS achievement_benchmark_log;
CREATE TABLE achievement_benchmark_log
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    achievement_benchmark_id INTEGER NOT NULL COMMENT '成绩基准科目编号',
    standard_category_ori VARCHAR(32) COMMENT '原进步类别',
    standard_category_tar VARCHAR(32) COMMENT '目标进步类别',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id)
) COMMENT='成绩基准科目修改日志'
;

--changeset lixuejun:1751_4 endDelimiter:\$\$
--comment BOSS成绩管理
DROP PROCEDURE IF EXISTS `proc_updat_current_ach_template`;

$$
CREATE PROCEDURE `proc_updat_current_ach_template`(
IN in_current_date varchar(10))
    SQL SECURITY INVOKER
BEGIN
            DECLARE tmp_id INT;
            DECLARE tmp_city_id VARCHAR(32);

            DECLARE done INT DEFAULT FALSE;  
            -- 游标
            DECLARE cur_db_tb CURSOR 
                FOR 
                SELECT id, city_id FROM achievement_template WHERE effective_date = in_current_date;
            -- 将结束标志绑定到游标
            DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
            DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = true;

             -- 打开游标
            OPEN cur_db_tb;
                -- 提取游标里的数据，这里只有一个，多个的话也一样；
                FETCH cur_db_tb INTO tmp_id, tmp_city_id;
                WHILE !done do
                    UPDATE achievement_template SET is_current_version = 1 WHERE city_id = tmp_city_id AND is_current_version = 0;
                    UPDATE achievement_template SET is_current_version = 0 WHERE id = tmp_id;
                    FETCH cur_db_tb INTO tmp_id, tmp_city_id;
                END WHILE;
            -- 关闭游标
            CLOSE cur_db_tb;
        COMMIT;
END
$$

--changeset lixuejun:1751_5
--comment BOSS成绩管理
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('checkHashRelatedAchBenchmark', 'ANON_RES', '/StudentAchievementController/checkHashRelatedAchievementBenchmark.do');

--changeset lixuejun:1751_6
--comment BOSS成绩管理
DROP TABLE IF EXISTS achievement_comparison;
CREATE TABLE achievement_comparison
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  student_id VARCHAR(32) NOT NULL COMMENT '学生编号',
    school_year VARCHAR(32) NOT NULL COMMENT '学年',
    semester VARCHAR(32) NOT NULL COMMENT '学期',
    subject_ids VARCHAR(250) COMMENT '在读一对一科目编号',
    subject_names VARCHAR(250) COMMENT '在读一对一科目名称',
    comparative_achievement_id INTEGER COMMENT '对比成绩',
    is_deleted TINYINT NOT NULL DEFAULT 1 COMMENT '是否删除1:否，0：是',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id),
    UNIQUE KEY `UQ_STUDENT_SCHOOL_YEAR_SEMESTER` (`student_id`, `school_year`,`semester`)
)  COMMENT='成绩对比记录'
;

--changeset lixuejun:1751_7
--comment BOSS成绩管理
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE ('ACHIEVEMENT_MANAGE_BTN', 'BUTTON', '成绩管理(新)', 0, '89', 10, 'ACHIEVEMENT_MANAGE_BTN');

--changeset lixuejun:1751_8
--comment BOSS成绩管理
DROP TABLE IF EXISTS achievement_comparison;
CREATE TABLE achievement_comparison
(
    id INTEGER unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  student_id VARCHAR(32) NOT NULL COMMENT '学生编号',
    school_year VARCHAR(32) NOT NULL COMMENT '学年',
    semester VARCHAR(32) NOT NULL COMMENT '学期',
    subject_ids VARCHAR(250) COMMENT '在读一对一科目编号',
    subject_names VARCHAR(250) COMMENT '在读一对一科目名称',
    comparative_achievement_id INTEGER COMMENT '对比成绩',
    is_deleted TINYINT NOT NULL DEFAULT 1 COMMENT '是否删除1:否，0：是',
    version INTEGER DEFAULT NULL COMMENT '版本',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user_id VARCHAR(32) COMMENT '创建人编号',
    modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    modify_user_id VARCHAR(32) COMMENT '修改人编号',
    PRIMARY KEY (id)
)  COMMENT='成绩对比记录'
;

--changeset lixuejun:1751_9
--comment BOSS成绩管理
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('listTemplateEffDateByCityId', 'ANON_RES', '/AchievementController/listTemplateEffectiveDateByCityId.do');

--changeset lixuejun:1751_10
--comment BOSS成绩管理
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('findPageAchTemplateByCityId', 'ANON_RES', '/AchievementController/findPageAchievementTemplateByCityId.do');

--changeset lixuejun:1505_2
--comment 直播更改客户手机号同步修改
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('checkContactUsed', 'ANON_RES', '/StudentController/checkContactUsed.do');

--changeset lixuejun:1751_11
--comment BOSS成绩管理
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('deleteStuAchievementEvidence', 'ANON_RES', '/StudentAchievementController/deleteStudentAchievementEvidence.do');

--changeset lixuejun:1751_12
--comment BOSS成绩管理
UPDATE resource SET HAS_CHILDREN = 1 WHERE ID = 'ACHIEVEMENT_MANAGE_BTN';
INSERT INTO resource(ID, RTYPE, RNAME, HAS_CHILDREN, PARENT_ID, RORDER, RTAG) VALUE ('MODIFY_COMPARISON_BTN', 'BUTTON', '成绩管理(新)', 0, 'ACHIEVEMENT_MANAGE_BTN', 1, 'MODIFY_COMPARISON_BTN');

--changeset lixuejun:1751_13
--comment BOSS成绩管理
DROP PROCEDURE IF EXISTS `proc_updat_current_ach_template`;

--changeset lixuejun:1751_14
--comment BOSS成绩管理
UPDATE resource set RNAME = '对比成绩修改' where ID = 'MODIFY_COMPARISON_BTN';

--changeset lixuejun:1751_15
--comment BOSS成绩管理
alter table student_achievement_subject add column `total_score` int(11) COMMENT '满分' after `score`;

update student_achievement_subject sas, student_achievement sa,  
        achievement_template_grade atg, achievement_template_subject ats set sas.total_score = ats.total_score
    where 1=1 and sas.student_achievement_id = sa.id
        and sa.achievement_template_id = atg.achievement_template_id and sa.grade_id = atg.grade_id
        and ats.template_grade_id = atg.id and ats.subject_id = sas.subject_id;

--changeset lixuejun:1751_16
--comment BOSS成绩管理
alter table achievement_template add index `IDX_CITY_DELETE_CURRENT`(city_id, is_deleted, is_current_version);
alter table achievement_benchmark add index `IDX_BENCHMARK_SUBJECT`(benchmark_subject_id);
alter table achievement_benchmark add index `IDX_COMPARE_SUBJECT`(compare_subject_id);

--changeset lixuejun:1751_17
--comment BOSS成绩管理
alter table student_achievement add index `IDX_PAGE_SEARCH`(student_id, examination_date, is_deleted, school_year, semester, examination_type);
alter table achievement_template_subject add index `IDX_SUBJECT`(subject_id);
alter table achievement_template_grade add index `IDX_GRADE`(grade_id);
alter table achievement_comparison add index `IDX_COMPARATIVE_ACHIEVEMENT`(comparative_achievement_id);
alter table achievement_benchmark add index `IDX_STUDENT_ACHIEVEMENT`(student_achievement_id);
alter table achievement_comparison add index `IDX_STU_DEL_SCH_YEAR_SEMESTER`(student_id, school_year, semester, is_deleted);
alter table achievement_pro_standard add index `IDX_NAME`(name);

--changeset lixuejun:1751_18
--comment BOSS成绩管理
update student_achievement_subject sas, student_achievement sa,  
        achievement_template_grade atg, achievement_template_subject ats set sas.total_score = ats.total_score
    where 1=1 and sas.student_achievement_id = sa.id
        and sa.achievement_template_id = atg.achievement_template_id and sa.grade_id = atg.grade_id
        and ats.template_grade_id = atg.id and ats.subject_id = sas.subject_id;
        
--changeset lixuejun:1751_19
--comment BOSS成绩管理    
update resource set RNAME = '进步判定修改' where ID = 'MODIFY_COMPARISON_BTN';

--changeset lixuejun:1997_1
--comment BOSS成绩管理    
alter table mini_class add column course_start_time varchar(20) DEFAULT NULL COMMENT '对应在线开始上课报读时间';

alter table mini_class add column coures_end_time varchar(20) DEFAULT NULL COMMENT '对应在线开始上课报读时间';

update mini_class set course_start_time = START_DATE, coures_end_time = END_DATE where 1=1;

--changeset lixuejun:1997_2
--comment 小班数据表添加“首次上课时间”字段提供给在线报读使用
UPDATE mini_class SET START_DATE=SUBSTR(START_DATE,1,10) WHERE START_DATE IS NOT NULL;
UPDATE mini_class SET END_DATE=SUBSTR(END_DATE,1,10) WHERE END_DATE IS NOT NULL;

--changeset lixuejun:691515_1
--comment 所有学生都需要默认初始化一个基准成绩和对比成绩，并前端展示
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('initAchievementComparisons', 'ANON_RES', '/StudentAchievementController/initAchievementComparisons.do');
INSERT INTO `resource` (`ID`, `RTYPE`, `RURL`) VALUES ('initAchComparisonByStudentId', 'ANON_RES', '/StudentAchievementController/initAchievementComparisonByStudentId.do');