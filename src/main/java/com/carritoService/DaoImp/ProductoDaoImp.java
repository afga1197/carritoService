package com.carritoService.DaoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.carritoService.Dao.ProductoDao;
import com.carritoService.model.Conexion;
import com.carritoService.model.Producto;

@Repository
public class ProductoDaoImp implements ProductoDao {

	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	@Override
	public List<Producto> obtenerProductos() {
		Connection conectar = null;
		ArrayList<Producto> productos = new ArrayList<>();
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "SELECT * FROM producto";
				preparedStatement = conectar.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Producto producto = new Producto();
					producto.setIdProducto(resultSet.getInt("idProducto"));
					producto.setNombre(resultSet.getString("nombre"));
					producto.setPrecio(resultSet.getDouble("precio"));
					productos.add(producto);
				}
			} else {
				System.out.print("Error al conectarse a base de datos");
			}
			return productos;
		} catch (Exception e) {
			System.out.print("Error en la ejecucion del query" + e.getMessage());
			return null;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexion: " + e.getMessage());
			}
		}
	}

	@Override
	public boolean guardarProducto(Producto producto) {
		boolean guardo = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "INSERT INTO producto (nombre, precio) VALUES(?,?)";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setString(1, producto.getNombre());
				preparedStatement.setDouble(2, producto.getPrecio());
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

	@Override
	public boolean actualizar(Producto producto) {
		boolean actualizo = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "UPDATE producto SET nombre=?, precio=? WHERE idProducto=?";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setString(1, producto.getNombre());
				preparedStatement.setDouble(2, producto.getPrecio());
				preparedStatement.setInt(3, producto.getIdProducto());
				preparedStatement.executeUpdate();
				actualizo = true;
			} else {
				actualizo = false;
			}
			return actualizo;
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

	@Override
	public boolean borrar(int id) {
		boolean borro = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "DELETE FROM producto WHERE idProducto=?";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();
				borro = true;
			} else {
				borro = false;
			}
			return borro;
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

}