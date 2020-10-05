package com.carritoService.DaoImp;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.Logger;
import com.carritoService.model.Producto;
import com.carritoService.model.Conexion;
import com.carritoService.Dao.ProductoDao;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoDaoImp implements ProductoDao {

	private static final Logger logger = LogManager.getLogger("producto");
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
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
			}
			return productos;
		} catch (Exception e) {
			String log = "Error en la ejecucion del query, con excepcion en " + e.getMessage();
			logger.error(log);
			return null;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				String log = "Error al cerrar la conexion, con excepcion en " + e.getMessage();
				logger.error(log);
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
				String log = "Se almaceno el producto en base de datos";
				logger.debug(log);
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
				guardo = false;
			}
			return guardo;
		} catch (Exception e) {
			String log = "Error en la ejecucion del query, con excepcion en " + e.getMessage();
			logger.error(log);
			return false;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				String log = "Error al cerrar la conexion, con excepcion en " + e.getMessage();
				logger.error(log);
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
				String log = "Se actualizo el producto en base de datos";
				logger.debug(log);
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
				actualizo = false;
			}
			return actualizo;
		} catch (Exception e) {
			String log = "Error en la ejecucion del query, con excepcion en " + e.getMessage();
			logger.error(log);
			return false;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				String log = "Error al cerrar la conexion, con excepcion en " + e.getMessage();
				logger.error(log);
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
				String log = "Se elimino el producto de base de datos";
				logger.debug(log);
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
				borro = false;
			}
			return borro;
		} catch (Exception e) {
			String log = "Error en la ejecucion del query, con excepcion en " + e.getMessage();
			logger.error(log);
			return false;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				String log = "Error al cerrar la conexion, con excepcion en " + e.getMessage();
				logger.error(log);
			}
		}
	}

}