package com.carritoService.model;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class Conexion {

	private final String DATABASE = "carritoservice";
	private final String URL = "jdbc:mysql://localhost:3306/" + DATABASE;
	private final String USER = "root";
	private final String PASSWORD = "root";
	private static Conexion conexion;
	private BasicDataSource basicDataSource = null;

	private Conexion() {
		basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		basicDataSource.setUsername(USER);
		basicDataSource.setPassword(PASSWORD);
		basicDataSource.setUrl(URL);
	}

	public static Conexion getInstance() {
		if (conexion == null) {
			conexion = new Conexion();
			return conexion;
		} else {
			return conexion;
		}
	}

	public Connection getConnection() {
		try {
			return this.basicDataSource.getConnection();
		} catch (SQLException e) {
			System.err.print(e.getMessage());
			return null;
		}
	}

	public void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.print(e.getMessage());
		}
	}
}