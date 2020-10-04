package com.carritoService.DaoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import com.carritoService.Dao.ClienteDao;
import com.carritoService.model.Cliente;
import com.carritoService.model.Conexion;
import com.carritoService.model.Rol;
import com.carritoService.model.RolNombre;

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
				String query = "INSERT INTO cliente (nombre, apellido, dni, telefono, email) VALUES(?,?,?,?,?)";
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
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
			}
			return idCliente;
		} catch (Exception e) {
			System.out.print("Error en la ejecucion del query " + e.getMessage());
			return 0;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexion: " + e.getMessage());
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
			System.out.print("Error en la ejecucion del query " + e.getMessage());
			return null;
		} finally {
			try {
				Conexion.getInstance().closeConnection(conectar);
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexion: " + e.getMessage());
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
			System.out.println(e.getMessage());
			return null;
		}
	}
}