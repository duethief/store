package com.bookstore.utils;

import org.hibernate.Session;

public interface HibernateAction {
	Object doProcess(Session session);
}
