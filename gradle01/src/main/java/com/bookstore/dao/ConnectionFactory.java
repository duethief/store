package com.bookstore.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private String connectionString;
	private String driverName;
	private String username;
	private String password;
	
	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public ConnectionFactory() {
	}
	
	public ConnectionFactory(String connectionString, String driverName, String username, String password) {
		this.connectionString = connectionString;
		this.driverName = driverName;
		this.username = username;
		this.password = password;
	}
	
	public void init() {
		try {
			Class.forName(this.driverName).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(this.connectionString, this.username, this.password);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
