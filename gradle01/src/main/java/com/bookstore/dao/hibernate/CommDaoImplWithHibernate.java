package com.bookstore.dao.hibernate;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import com.bookstore.dao.CommDao;
import com.bookstore.utils.HibernateAction;
import com.bookstore.utils.HibernateSqlExecutor;

public class CommDaoImplWithHibernate<T> implements CommDao<T> {
	@Autowired
	protected SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private final Class<T> type;
	
	public CommDaoImplWithHibernate(Class<T> type) {
		this.type = type;
	}

	@Override
	public void add(T obj) {
		Session session = sessionFactory.getCurrentSession();
		session.save(obj);
		Field[] fields = type.getDeclaredFields();
		for(Field field : fields) {
			Annotation[] annots = field.getAnnotations();
			for(Annotation annot : annots) {
				if(annot.annotationType() == Id.class) {
					try {
						field.setAccessible(true);
						field.set(obj, session.save(obj));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getById(Object id) {
		Session session = sessionFactory.getCurrentSession();
		return (T) session.get(type, (Serializable) id);
	}

	@Override
	public void update(T obj) {
		Session session = sessionFactory.getCurrentSession();
		session.update(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(type).list();
	}

	@Override
	public int countAll() {
		Session session = sessionFactory.getCurrentSession();
		Long count = (Long) session.createCriteria(type).setProjection(Projections.rowCount()).uniqueResult();
		if(count == null) {
			return 0;
		}
		return count.intValue();
	}

	@Override
	public void deleteAll() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<T> result = session.createCriteria(type).list();
		for(T r : result) {
			session.delete(r);
		}
	}
}
