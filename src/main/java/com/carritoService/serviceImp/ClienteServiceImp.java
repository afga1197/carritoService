package com.carritoService.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.carritoService.Dao.ClienteDao;
import com.carritoService.model.Cliente;
import com.carritoService.service.ClienteService;

@Service
public class ClienteServiceImp implements ClienteService{

	@Autowired
	private ClienteDao clienteDao;
	
	@Override
	public boolean existeUsuario(long dni) {
		return clienteDao.existeUsuario(dni);
	}

	@Override
	public boolean guardarCliente(Cliente cliente) {
		return clienteDao.guardarCliente(cliente);
	}

}