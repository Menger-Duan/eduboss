package com.eduboss.dialect;

import java.sql.Types;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQLDialectForBoss extends MySQL5InnoDBDialect {

	public MySQLDialectForBoss() {  
        super();  
        registerHibernateType(Types.LONGVARCHAR, 65535, "text");  
    } 
	
}
