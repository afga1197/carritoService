package com.carritoService.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.carritoService.Dao.ClienteDao;
import com.carritoService.model.Cliente;
import com.carritoService.model.ClientesSeguridad;
import com.carritoService.service.ClienteService;

@Service
public class ClienteServiceImp implements ClienteService, UserDetailsService{

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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Cliente cliente = clienteDao.loadUserByUsername(username);
		return ClientesSeguridad.build(cliente);
	}

	@Override
	public boolean guardarClienteEnTXT(Cliente clienteNuevo) {
		return clienteDao.guardarClienteEnTXT(clienteNuevo);
	}

}