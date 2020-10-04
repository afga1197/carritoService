package com.carritoService.controller;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.carritoService.model.Cliente;
import com.carritoService.model.JWT;
import com.carritoService.model.Rol;
import com.carritoService.model.RolNombre;
import com.carritoService.security.JWTEntryPoint;
import com.carritoService.security.JWTProvider;
import com.carritoService.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTProvider jwtProvider;

	private static final Logger logger = LogManager.getLogger("login");

	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Cliente cliente) {
		if (!clienteService.existeUsuario(cliente.getDni())) {
			Cliente clienteNuevo = cliente;
			clienteNuevo.setConstrasenia(passwordEncoder.encode(cliente.getConstrasenia()));
			Set<Rol> roles = new HashSet<>();
			Rol rolUser = new Rol((RolNombre.ROLE_USER));
			roles.add(rolUser);
			if (clienteNuevo.getRoles().contains("admin")) {
				Rol rolAdmin = new Rol((RolNombre.ROLE_ADMIN));
				roles.add(rolAdmin);
			}
			clienteNuevo.setRoles(roles);
			if (clienteService.guardarCliente(clienteNuevo)) {
				if (clienteService.guardarClienteEnTXT(clienteNuevo)) {
					return new ResponseEntity("El usuario fue creado satisfactoriamente", HttpStatus.OK);
				} else {
					return new ResponseEntity(
							"El usuario fue creado pero no se pudieron almacenar la credenciales. Por favor comunicarse con soporte.",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity("El usuario no se pudo crear", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity("El usuario ya existe", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Cliente cliente) {
		try {
			Authentication authentication = authenticationManager
					.authenticate((Authentication) new UsernamePasswordAuthenticationToken(cliente.getUsuario(),
							cliente.getConstrasenia()));
			System.out.println("este es el authentication" + authentication);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtProvider.generarToken(authentication);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			JWT jwtDto = new JWT(jwt, userDetails.getUsername(), userDetails.getAuthorities());
			String log = "Usuario: " + cliente.getUsuario() + ". Logueo Exitoso.";
			logger.debug(log);
			return new ResponseEntity(jwtDto, HttpStatus.OK);
		} catch (BadCredentialsException e) {
			String log = "Usuario: " + cliente.getUsuario() + ". Datos incorrectos en el logueo";
			logger.info(log);
			return new ResponseEntity(HttpStatus.OK);
		} catch (InternalAuthenticationServiceException e) {
			String log = "Usuario: " + cliente.getUsuario() + ". El usuario no existe";
			logger.info(log);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			String log = "Usuario: " + cliente.getUsuario() + ". Error en " + e.getMessage();
			logger.error(log);
			return new ResponseEntity(HttpStatus.OK);
		}
	}

}