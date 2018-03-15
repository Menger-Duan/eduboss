--liquibase formatted sql

--changeset 宋直乘:5077-1
INSERT INTO `resource` (`ID`, `RTYPE`, `RNAME`, `HAS_CHILDREN`, `PARENT_ID`, `RORDER`, `RTAG`) VALUES ('BTN_STUDENT_ACCOUNT_SHOW', 'BUTTON', '账务', '0', 'promiseStudentList', '0', 'SHOW_STUDENT_ACCOUNT_INFO');