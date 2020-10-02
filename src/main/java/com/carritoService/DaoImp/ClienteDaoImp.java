package com.carritoService.DaoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.stereotype.Repository;
import com.carritoService.Dao.ClienteDao;
import com.carritoService.model.Cliente;
import com.carritoService.model.Conexion;

@Repository
public class ClienteDaoImp implements ClienteDao {

	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	@Override
	public boolean existeUsuario(long dni) {
		boolean existe = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "SELECT * FROM cliente WHERE dni=?";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setString(1, String.valueOf(dni));
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					existe = true;
				}
			} else {
				System.out.print("Error al conectarse a base de datos");
			}
			return existe;
		} catch (Exception e) {
			System.out.print("Error en la ejecucion del query" + e.getMessage());
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
	public boolean guardarCliente(Cliente cliente) {
		boolean guardo = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "INSERT INTO cliente (nombre, apellido, dni, telefono, email)  VALUES(?,?,?,?,?)";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setString(1, cliente.getNombre());
				preparedStatement.setString(2, cliente.getApellido());
				preparedStatement.setString(3, String.valueOf(cliente.getDni()));
				preparedStatement.setString(4, String.valueOf(cliente.getTelefono()));
				preparedStatement.setString(5, cliente.getEmail());
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

}