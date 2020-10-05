package com.carritoService.DaoImp;

import java.sql.Date;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.carritoService.model.Venta;
import com.carritoService.Dao.VentaDao;
import org.apache.logging.log4j.Logger;
import com.carritoService.model.Conexion;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;

@Repository
public class VentaDaoImp implements VentaDao {

	private static final Logger logger = LogManager.getLogger("venta");
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	@Override
	public List<Venta> obtenerVentas() {
		Connection conectar = null;
		ArrayList<Venta> ventas = new ArrayList<>();
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "SELECT dv.idDetalleVenta, dv.idProducto, v.idVenta, v.fecha, v.idCliente FROM detalleventa AS dv INNER JOIN venta AS v ON dv.idVenta=v.idVenta";
				preparedStatement = conectar.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Venta venta = new Venta();
					venta.setFecha(resultSet.getDate("fecha"));
					venta.setIdCliente(resultSet.getInt("idCliente"));
					venta.setIdDetalleVenta(resultSet.getInt("idDetalleVenta"));
					venta.setIdProducto(resultSet.getInt("idProducto"));
					venta.setIdVenta(resultSet.getInt("idVenta"));
					ventas.add(venta);
				}
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
			}
			return ventas;
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
				String log = "Se almaceno la venta en base de datos";
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

	private Date obtenerFecha() {
		java.util.Date d = new java.util.Date();
		java.sql.Date date2 = new java.sql.Date(d.getTime());
		return date2;
	}

	@Override
	public boolean guardarDetalleVenta(Venta venta) {
		boolean guardo = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "INSERT INTO detalleventa (idProducto, idVenta) VALUES(?,?)";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setInt(1, venta.getIdProducto());
				preparedStatement.setInt(2, venta.getIdVenta());
				preparedStatement.executeUpdate();
				guardo = true;
				String log = "Se almaceno el detalle de la venta en base de datos";
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

}