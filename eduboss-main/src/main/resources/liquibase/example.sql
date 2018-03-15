--liquibase formatted sql
--文档:http://www.liquibase.org/documentation/sql_format.html
--文件第一行必须是--liquibase formatted sql

--changeset author:id
--comment author,id,sql文件名组合必须唯一
DROP TABLE IF EXISTS test1;
create table test1(
  id int primary key,
  name varchar(255)
)

--changeset 学邦科技:1
INSERT INTO test1 VALUES (1, 'test');
--rollback DELETE FROM test1 WHERE id=1;

--changeset 学邦科技:2 endDelimiter:\$\$
--comment 存储过程、函数、触发器，必须加上endDelimiter
DROP PROCEDURE IF EXISTS sayHelloWorld;
$$
CREATE PROCEDURE sayHelloWorld()
BEGIN
    SELECT 'Hello World From a MySql Database!';
END
$$

