package com.bookstore.dao.sqlExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bookstore.dao.CommDao;
import com.bookstore.dao.sqlExecutor.SqlExecutor.ExecuteSelectQuery;
import com.bookstore.dao.sqlExecutor.SqlExecutor.ExecuteUpdateQuery;

public class CommDaoImplWithSqlExecutor<T> implements CommDao<T> {
	@Autowired
	protected SqlExecutor sqlExecutor;

	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}

	public void setSqlExecutor(SqlExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}

	private final Class<T> type;
	private final Field[] fields;

	public CommDaoImplWithSqlExecutor(Class<T> type) {
		this.type = type;
		this.fields = type.getDeclaredFields();
	}

	protected T convertToObject(ResultSet rs) {
		T obj = null;

		try {
			obj = type.getConstructor((Class<?>[]) null).newInstance(
					(Object[]) null);

			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				boolean fieldTypeIsDate = fields[i].getType().getSimpleName()
						.equals("Date");
				boolean fieldTypeIsEnum = fields[i].getType().isEnum();
				String fieldType;
				if (fieldTypeIsDate) {
					fieldType = "Timestamp";
				} else if (fieldTypeIsEnum) {
					fieldType = "int";
				} else {
					fieldType = fields[i].getType().getSimpleName();
				}

				Method get = rs.getClass().getMethod(
						"get" + fieldType.substring(0, 1).toUpperCase()
								+ fieldType.substring(1), String.class);
				Object getValue = get.invoke(rs, fieldName);
				if (fieldTypeIsDate) {
					getValue = new java.util.Date(
							((Timestamp) getValue).getTime());
				} else if (fieldTypeIsEnum) {
					getValue = fields[i].getType()
							.getMethod("valueOf", int.class)
							.invoke(null, getValue);
				}

				Method set = type.getMethod(
						"set" + fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1), fields[i].getType());
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
			sql.append(type.getMethod("getTableName", (Class<?>[]) null)
					.invoke(null, (Object[]) null));
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
		this.sqlExecutor.execute(new ExecuteUpdateQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement st = conn.prepareStatement(getSqlAdd());
				try {
					for (int i = 0; i < fields.length; i++) {
						String fieldName = fields[i].getName();
						boolean fieldTypeIsDate = fields[i].getType()
								.getSimpleName().equals("Date");
						boolean fieldTypeIsEnum = fields[i].getType().isEnum();
						Class<?> fieldType;
						if (fieldTypeIsDate) {
							fieldType = Timestamp.class;
						} else if (fieldTypeIsEnum) {
							fieldType = int.class;
						} else {
							fieldType = fields[i].getType();
						}
						String filedTypeName = fieldType.getSimpleName();

						Method get = type.getMethod("get"
								+ fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1), (Class<?>[]) null);
						Object getValue = get.invoke(obj, (Object[]) null);
						if (fieldTypeIsDate) {
							getValue = new java.sql.Timestamp(((Date) getValue)
									.getTime());
						} else if (fieldTypeIsEnum) {
							getValue = fields[i].getType()
									.getMethod("intValue", (Class<?>[]) null)
									.invoke(getValue, (Object[]) null);
						}

						Class<?>[] parameterTypes = { int.class, fieldType };
						Method set = st.getClass().getMethod(
								"set"
										+ filedTypeName.substring(0, 1)
												.toUpperCase()
										+ filedTypeName.substring(1),
								parameterTypes);
						Object[] args = { i + 1, getValue };
						set.invoke(st, args);
					}
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
				return st;
			}
		});
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

	@SuppressWarnings("unchecked")
	public T getById(final Object id) {
		return (T) this.sqlExecutor.execute(new ExecuteSelectQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement st = conn.prepareStatement(getSqlGetById());
				st.setObject(1, id);
				return st;
			}

			@Override
			public T parseResultSet(ResultSet rs) throws SQLException {
				rs.next();
				return convertToObject(rs);
			}
		});
	}

	private String getSqlCountAll() {
		StringBuilder sql = new StringBuilder("select count(*) from ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null)
					.invoke(null, (Object[]) null));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}

	private String getSqlUpdate() {
		StringBuilder sql = new StringBuilder("update ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null)
					.invoke(null, (Object[]) null));
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
		this.sqlExecutor.execute(new ExecuteUpdateQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement st = conn.prepareStatement(getSqlUpdate());
				try {
					for (int i = 0; i < fields.length; i++) {
						String fieldName = fields[i].getName();
						boolean fieldTypeIsDate = fields[i].getType()
								.getSimpleName().equals("Date");
						boolean fieldTypeIsEnum = fields[i].getType().isEnum();
						Class<?> fieldType;
						if (fieldTypeIsDate) {
							fieldType = Timestamp.class;
						} else if (fieldTypeIsEnum) {
							fieldType = int.class;
						} else {
							fieldType = fields[i].getType();
						}
						String filedTypeName = fieldType.getSimpleName();

						Method get = type.getMethod("get"
								+ fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1), (Class<?>[]) null);
						Object getValue = get.invoke(obj, (Object[]) null);
						if (fieldTypeIsDate) {
							getValue = new java.sql.Timestamp(((Date) getValue)
									.getTime());
						} else if (fieldTypeIsEnum) {
							getValue = fields[i].getType()
									.getMethod("intValue", (Class<?>[]) null)
									.invoke(getValue, (Object[]) null);
						}

						Class<?>[] parameterTypes = { int.class, fieldType };
						Method set = st.getClass().getMethod(
								"set"
										+ filedTypeName.substring(0, 1)
												.toUpperCase()
										+ filedTypeName.substring(1),
								parameterTypes);
						Object[] args = { i + 1, getValue };
						set.invoke(st, args);
					}

					Method get = type.getMethod("getId", (Class<?>[]) null);
					Object getValue = get.invoke(obj, (Object[]) null);
					Class<?>[] parameterTypes = { int.class, int.class };
					Method set = st.getClass().getMethod("setInt",
							parameterTypes);
					Object[] args = { fields.length + 1, getValue };
					set.invoke(st, args);
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
				return st;
			}
		});
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
			sql.append(type.getMethod("getTableName", (Class<?>[]) null)
					.invoke(null, (Object[]) null));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return (List<T>) this.sqlExecutor.execute(new ExecuteSelectQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement st = conn
						.prepareStatement(getSqlGetAll());
				return st;
			}

			@Override
			public Object parseResultSet(ResultSet rs) throws SQLException {
				List<T> objs = new ArrayList<>();
				while (rs.next()) {
					objs.add(convertToObject(rs));
				}

				return objs;
			}
		});
	}

	public int countAll() {
		return (Integer) this.sqlExecutor.execute(new ExecuteSelectQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement st = conn.prepareStatement(getSqlCountAll());
				return st;
			}

			@Override
			public Object parseResultSet(ResultSet rs) throws SQLException {
				rs.next();
				return rs.getInt(1);
			}
		});
	}

	private String getSqlDeleteAll() {
		StringBuilder sql = new StringBuilder("delete from ");
		try {
			sql.append(type.getMethod("getTableName", (Class<?>[]) null)
					.invoke(null, (Object[]) null));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}

	public void deleteAll() {
		this.sqlExecutor.execute(new ExecuteUpdateQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement st = conn.prepareStatement(getSqlDeleteAll());
				return st;
			}
		});
	}
}
