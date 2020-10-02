package com.carritoService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.carritoService.model.Cliente;
import com.carritoService.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Cliente cliente) {
		if (!clienteService.existeUsuario(cliente.getDni())) {
			if (clienteService.guardarCliente(cliente)) {
				return new ResponseEntity("El usuario fue creado satisfactoriamente", HttpStatus.OK);
			}else {
				return new ResponseEntity("El usuario no se pudo crear", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity("El usuario ya existe", HttpStatus.BAD_REQUEST);
		}
	}

}