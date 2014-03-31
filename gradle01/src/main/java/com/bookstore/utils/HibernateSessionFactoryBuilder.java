package com.bookstore.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.beans.factory.FactoryBean;

public class HibernateSessionFactoryBuilder implements FactoryBean<SessionFactory>{
	String filename;
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public HibernateSessionFactoryBuilder() {}
	
	@Override
	public SessionFactory getObject() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure(filename);
		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
		serviceRegistryBuilder.applySettings(cfg.getProperties());
		return	cfg.configure().buildSessionFactory(serviceRegistryBuilder.build());
	}

	@Override
	public Class<?> getObjectType() {
		return SessionFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
	public String[] getSchemaQuery() {
		Configuration cfg = new Configuration();
		cfg.configure(filename);
		return cfg.generateSchemaCreationScript(new MySQL5InnoDBDialect());
	}
}
