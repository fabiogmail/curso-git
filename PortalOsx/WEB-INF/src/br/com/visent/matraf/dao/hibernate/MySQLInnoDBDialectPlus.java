package br.com.visent.matraf.dao.hibernate;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.HSQLDialect;

public class MySQLInnoDBDialectPlus extends HSQLDialect{
	
	public MySQLInnoDBDialectPlus(){
		super();
		registerHibernateType(Types.DECIMAL, Hibernate.DOUBLE.getName() );
	}
}
 