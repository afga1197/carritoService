package com.carritoService.Dao;

import org.springframework.security.core.userdetails.UserDetails;

import com.carritoService.model.Cliente;

public interface ClienteDao {

	boolean existeUsuario(long dni);

	boolean guardarCliente(Cliente cliente);

	boolean guardarClienteEnTXT(Cliente clienteNuevo);

	Cliente loadUserByUsername(String username);

}