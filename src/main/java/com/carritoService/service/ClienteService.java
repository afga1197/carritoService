package com.carritoService.service;

import com.carritoService.model.Cliente;

public interface ClienteService {

	boolean existeUsuario(long dni);

	boolean guardarCliente(Cliente cliente);

	boolean guardarClienteEnTXT(Cliente clienteNuevo);

}