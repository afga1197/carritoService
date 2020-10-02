package com.carritoService.Dao;

import com.carritoService.model.Cliente;

public interface ClienteDao {

	boolean existeUsuario(long dni);

	boolean guardarCliente(Cliente cliente);

}