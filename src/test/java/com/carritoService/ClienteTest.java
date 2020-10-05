package com.carritoService;

import org.junit.jupiter.api.Test;
import com.carritoService.model.Cliente;
import com.carritoService.DaoImp.ClienteDaoImp;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClienteTest {

	@Test
	void test() {
		ClienteDaoImp clienteDaoImp = new ClienteDaoImp();
		Cliente cliente = new Cliente();
		cliente.setApellido("alfaro");
		cliente.setConstrasenia("12345");
		cliente.setDni(41516228);
		cliente.setEmail("alfaroquintanasofia@hotmail.com");
		cliente.setNombre("sofia");
		cliente.setTelefono(2528192);
		cliente.setUsuario("alfaroquintanasofia");
		clienteDaoImp.guardarCliente(cliente);
	}
	
	@Test
	void test1() {
		ClienteDaoImp clienteDaoImp = new ClienteDaoImp();
		Cliente cliente = new Cliente();
		cliente.setConstrasenia("12345");
		cliente.setUsuario("afga97");
		clienteDaoImp.loadUserByUsername(cliente.getUsuario());
	}

}