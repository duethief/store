package com.bookstore.dao.queryDSL;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bookstore.dao.CommDao;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;

public class CommDaoImplWithQueryDSL<T> implements CommDao<T> {
	@PersistenceContext
	protected EntityManager em;
	
	private final Class<T> type;
	protected final EntityPathBase<T> q;
	
	@SuppressWarnings("unchecked")
	public CommDaoImplWithQueryDSL(Class<T> type) {
		this.type = type;
		Class<?> qClass = null;
		EntityPathBase<T> q = null;
		
		try {
			qClass = Class.forName(type.getPackage().getName() + ".Q" + type.getSimpleName());
		} catch (ClassNotFoundException e) {
			new RuntimeException(e);
		}
		
		Field[] fields = qClass.getFields();
		for(Field field : fields) {
			if(Modifier.isStatic(field.getModifiers())) {
				try {
					q = (EntityPathBase<T>) field.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					new RuntimeException(e);
				}
			}
		}
		this.q = q;
	}
	
	@Override
	public void add(T obj) {
		em.persist(obj);
	}

	@Override
	public T getById(Object id) {
		return (T) em.find(type, id);
	}

	@Override
	public void update(T obj) {
		em.persist(obj);
	}

	@Override
	public List<T> getAll() {
		return new JPAQuery(em).from(q).list(q);
	}

	@Override
	public int countAll() {
		return new JPAQuery(em).from(q).uniqueResult(q.count()).intValue();
	}

	@Override
	public void deleteAll() {
		new JPADeleteClause(em, q).execute();
	}

}
