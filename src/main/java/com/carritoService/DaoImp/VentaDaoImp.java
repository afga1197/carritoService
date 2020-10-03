package com.carritoService.DaoImp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.carritoService.Dao.VentaDao;
import com.carritoService.model.Conexion;
import com.carritoService.model.Producto;
import com.carritoService.model.Venta;

@Repository
public class VentaDaoImp implements VentaDao{

	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	@Override
	public List<Venta> obtenerVentas() {
		/*
		 * Connection conectar = null; ArrayList<Venta> ventas = new ArrayList<>(); try
		 * { conectar = Conexion.getInstance().getConnection(); if (conectar != null) {
		 * String query = "SELECT * FROM producto"; preparedStatement =
		 * conectar.prepareStatement(query); resultSet =
		 * preparedStatement.executeQuery(); while (resultSet.next()) { Producto
		 * producto = new Producto();
		 * producto.setIdProducto(resultSet.getInt("idProducto"));
		 * producto.setNombre(resultSet.getString("nombre"));
		 * producto.setPrecio(resultSet.getDouble("precio")); productos.add(producto); }
		 * } else { System.out.print("Error al conectarse a base de datos"); } return
		 * productos; } catch (Exception e) {
		 * System.out.print("Error en la ejecucion del query" + e.getMessage()); return
		 * null; } finally { try { Conexion.getInstance().closeConnection(conectar); }
		 * catch (Exception e) { System.out.println("Error al cerrar la conexion: " +
		 * e.getMessage()); } }
		 */
		return null;
	}

	@Override
	public boolean guardarVenta(Venta venta) {
		boolean guardo = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "INSERT INTO venta (fecha, idCliente) VALUES(?,?)";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setDate(1, obtenerFecha());
				preparedStatement.setDouble(2, venta.getIdCliente());
				preparedStatement.executeUpdate();
				guardo = true;
			} else {
				guardo = false;
			}
			return guardo;
		} catch (Exception e) {
			System.out.print("Error en la ejecucion del query " + e.getMessage());
			return false;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexion: " + e.getMessage());
			}
		}
	}

	private Date obtenerFecha() {
		java.util.Date d = new java.util.Date();
		java.sql.Date date2 = new java.sql.Date(d.getTime());
		return date2;
	}

}
