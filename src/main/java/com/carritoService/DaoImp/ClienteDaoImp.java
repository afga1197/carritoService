package com.carritoService.DaoImp;

import java.io.File;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.Connection;
import java.io.IOException;
import java.io.BufferedWriter;
import java.sql.PreparedStatement;
import com.carritoService.model.Rol;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.Logger;
import com.carritoService.model.Cliente;
import com.carritoService.model.Conexion;
import com.carritoService.Dao.ClienteDao;
import com.carritoService.model.RolNombre;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteDaoImp implements ClienteDao {

	private static final Logger logger = LogManager.getLogger("cliente");
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
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
			}
			return existe;
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
	public boolean guardarCliente(Cliente cliente) {
		boolean guardo = false;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "INSERT INTO cliente (nombre, apellido, dni, telefono, email) VALUES(?,?,?,?,?)";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setString(1, cliente.getNombre());
				preparedStatement.setString(2, cliente.getApellido());
				preparedStatement.setString(3, String.valueOf(cliente.getDni()));
				preparedStatement.setString(4, String.valueOf(cliente.getTelefono()));
				preparedStatement.setString(5, cliente.getEmail());
				preparedStatement.executeUpdate();
				guardo = true;
				String log = "Se almaceno el cliente en la base de datos";
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
	public boolean guardarClienteEnTXT(Cliente cliente) {
		int idCliente = obtenerIdCliente(cliente);
		try {
			if (idCliente != 0) {
				String ruta = "C:/Users/andre/Desktop/credenciales.txt";
				String contenido = idCliente + ", " + cliente.getUsuario() + ", " + cliente.getConstrasenia() + "\n";
				File file = new File(ruta);
				if (!file.exists()) {
					file.createNewFile();
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(contenido);
					bw.close();
				} else {
					FileWriter fw = new FileWriter(file, true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(contenido);
					bw.close();
				}
				String log = "Se almacenaron las credenciales del usuario";
				logger.debug(log);
				return true;
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
				return false;
			}
		} catch (IOException e) {
			String log = "Error en la escritura del archivo, con excepcion en " + e.getMessage();
			logger.error(log);
			return false;
		}
	}

	private int obtenerIdCliente(Cliente cliente) {
		int idCliente = 0;
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "SELECT idCliente FROM cliente WHERE dni=?";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setString(1, String.valueOf(cliente.getDni()));
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					idCliente = resultSet.getInt("idCliente");
				}
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
			}
			return idCliente;
		} catch (Exception e) {
			String log = "Error en la ejecucion del query, con excepcion en " + e.getMessage();
			logger.error(log);
			return 0;
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
	public Cliente loadUserByUsername(String username) {
		Cliente cliente = obtenerCredenciales(username);
		Connection conectar = null;
		try {
			conectar = Conexion.getInstance().getConnection();
			if (conectar != null) {
				String query = "SELECT * FROM cliente WHERE idCliente=?";
				preparedStatement = conectar.prepareStatement(query);
				preparedStatement.setInt(1, cliente.getIdCliente());
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					cliente.setNombre(resultSet.getString("nombre"));
					cliente.setApellido(resultSet.getString("apellido"));
					cliente.setDni(Long.parseLong(resultSet.getString("dni")));
					cliente.setTelefono(Long.parseLong(resultSet.getString("telefono")));
					cliente.setEmail(resultSet.getString("email"));
				}
			} else {
				String log = "Error al conectarse a la base de datos, consulte el log de conexion para mas informacion";
				logger.error(log);
			}
			Set<Rol> roles = new HashSet<>();
			Rol rolUser = new Rol((RolNombre.ROLE_USER));
			roles.add(rolUser);
			if (cliente.getUsuario().equals("admin")) {
				Rol rolAdmin = new Rol((RolNombre.ROLE_ADMIN));
				roles.add(rolAdmin);
			}
			cliente.setRoles(roles);
			return cliente;
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

	private Cliente obtenerCredenciales(String username) {
		File file = new File("C:/Users/andre/Desktop/credenciales.txt");
		Scanner scanner;
		try {
			Cliente cliente = new Cliente();
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String linea = scanner.nextLine();
				String[] partes = linea.split(", ");
				String parte1 = partes[0];
				String parte2 = partes[1];
				String parte3 = partes[2];
				if (username.equals(parte2)) {
					cliente.setIdCliente(Integer.parseInt(parte1));
					cliente.setUsuario(username);
					cliente.setConstrasenia(parte3);
					break;
				}
			}
			scanner.close();
			return cliente;
		} catch (FileNotFoundException e) {
			String log = "Error en la lectura del archivo TXT, con excepcion en " + e.getMessage();
			logger.error(log);
			return null;
		}
	}
}