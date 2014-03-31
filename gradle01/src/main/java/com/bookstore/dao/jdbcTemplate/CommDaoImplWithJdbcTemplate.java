package com.bookstore.dao.jdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.bookstore.dao.CommDao;

public class CommDaoImplWithJdbcTemplate<T> implements CommDao <T> {
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private final Class<T> type;
	private final Field[] fields;
	
	public CommDaoImplWithJdbcTemplate(Class<T> type) {
		this.type = type;
		this.fields = type.getDeclaredFields();
	}
	
	protected T convertToObject(SqlRowSet rs) {
		T obj = null;
		
		try {
			obj = type.getConstructor((Class<?>[]) null).newInstance((Object[]) null);
			
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				boolean fieldTypeIsDate = fields[i].getType().getSimpleName().equals("Date");
				boolean fieldTypeIsEnum = fields[i].getType().isEnum();
				boolean fieldTypeIsInteger = (fields[i].getType() == Integer.class);
				String fieldType;
				if (fieldTypeIsDate) {
					fieldType = "Timestamp";
				} else if (fieldTypeIsEnum) {
					fieldType = "int";
				} else if (fieldTypeIsInteger) {
					fieldType = "Object";
				} else {
					fieldType = fields[i].getType().getSimpleName();
				}
				
				Method get = rs.getClass().getMethod("get" + fieldType.substring(0, 1).toUpperCase() + fieldType.substring(1), String.class);
				Object getValue = get.invoke(rs, fieldName);
				if (fieldTypeIsDate) {
					getValue = new java.util.Date(((Timestamp) getValue).getTime());
				} else if (fieldTypeIsEnum) {
					getValue = fields[i].getType().getMethod("valueOf", int.class).invoke(null, getValue);
				}
				
				Method set = type.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), fields[i].getType());
				set.invoke(obj, getValue);
			}
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}
	
	private String getSqlAdd() {
		StringBuilder sql = new StringBuilder("insert ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null).invoke(null, (Object[]) null));
			sql.append(" (");
			
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i].getName());
				if (i < fields.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(") values(");
			for (int i = 0; i < fields.length; i++) {
				sql.append("?");
				if (i < fields.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(")");
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}
	
	public void add(final T obj) {
		ArrayList<Object> args = new ArrayList<Object>();
		try {
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				boolean fieldTypeIsDate = fields[i].getType().getSimpleName().equals("Date");
				boolean fieldTypeIsEnum = fields[i].getType().isEnum();
				
				Method get = type.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), (Class<?>[]) null);
				Object getValue = get.invoke(obj, (Object[]) null);
				if (fieldTypeIsDate) {
					getValue = new java.sql.Timestamp(((Date) getValue).getTime());
				} else if (fieldTypeIsEnum) {
					getValue = fields[i].getType().getMethod("intValue", (Class<?>[]) null).invoke(getValue, (Object[]) null);
				}
				
				args.add(getValue);
			}
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		this.jdbcTemplate.update(getSqlAdd(), args.toArray());
	}
	
	private String getSqlGetById() {
		StringBuilder sql = new StringBuilder("select ");
		try {
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i].getName());
				if (i < fields.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(" from ");
			sql.append(type.getMethod("getTableName", (Class<?>[]) null).invoke(null, (Object[]) null));
			sql.append(" where id = ?");
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}
	
	public T getById(Object id) {
		SqlRowSet rs = this.jdbcTemplate.queryForRowSet(getSqlGetById(), id);
		
		rs.next();
		
		return convertToObject(rs);
	}
	
	private String getSqlUpdate() {
		StringBuilder sql = new StringBuilder("update ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null).invoke(null, (Object[]) null));
			sql.append(" set ");
			
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i].getName());
				sql.append(" = ?");
				if (i < fields.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(" where id = ?");
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}
	
	public void update(final T obj) {
		ArrayList<Object> args = new ArrayList<Object>();
		try {
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				boolean fieldTypeIsDate = fields[i].getType().getSimpleName().equals("Date");
				boolean fieldTypeIsEnum = fields[i].getType().isEnum();
				
				Method get = type.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), (Class<?>[]) null);
				Object getValue = get.invoke(obj, (Object[]) null);
				if (fieldTypeIsDate) {
					getValue = new java.sql.Timestamp(((Date) getValue).getTime());
				} else if (fieldTypeIsEnum) {
					getValue = fields[i].getType().getMethod("intValue", (Class<?>[]) null).invoke(getValue, (Object[]) null);
				}
				
				args.add(getValue);
			}
			
			Method get = type.getMethod("getId", (Class<?>[]) null);
			Object getValue = get.invoke(obj, (Object[]) null);
			args.add(getValue);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		this.jdbcTemplate.update(getSqlUpdate(), args.toArray());
	}
	
	private String getSqlGetAll() {
		StringBuilder sql = new StringBuilder("select ");
		try {
			for (int i = 0; i < fields.length; i++) {
				sql.append(fields[i].getName());
				if (i < fields.length - 1) {
					sql.append(", ");
				}
			}
			
			sql.append(" from ");
			sql.append(type.getMethod("getTableName", (Class<?>[]) null).invoke(null, (Object[]) null));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}
	
	public List<T> getAll() {
		SqlRowSet rs = this.jdbcTemplate.queryForRowSet(getSqlGetAll());
		
		List<T> objs = new ArrayList<>();
		while(rs.next()) {
			objs.add(convertToObject(rs));
		}
		
		return objs;
	}
	
	private String getSqlCountAll() {
		StringBuilder sql = new StringBuilder("select count(*) from ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null).invoke(null, (Object[]) null));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}
	
	public int countAll() {
		return this.jdbcTemplate.queryForObject(getSqlCountAll(), Integer.class);
	}
	
	private String getSqlDeleteAll() {
		StringBuilder sql = new StringBuilder("delete from ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null).invoke(null, (Object[]) null));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update(getSqlDeleteAll());
	}
}
