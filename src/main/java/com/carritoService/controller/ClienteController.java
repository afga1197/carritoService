package com.carritoService.controller;

import java.util.Set;
import java.util.HashSet;
import com.carritoService.model.JWT;
import com.carritoService.model.Rol;
import org.apache.logging.log4j.Logger;
import com.carritoService.model.Cliente;
import com.carritoService.model.RolNombre;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.carritoService.security.JWTProvider;
import com.carritoService.service.ClienteService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

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

	private static final Logger logger = LogManager.getLogger(ClienteController.class);

	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Cliente cliente) {
		if (cliente.getApellido() != null && cliente.getConstrasenia() != null && cliente.getDni() != 0
				&& cliente.getEmail() != null && cliente.getNombre() != null && cliente.getTelefono() != 0
				&& cliente.getUsuario() != null) {
			if (!clienteService.existeUsuario(cliente.getDni())) {
				Cliente clienteNuevo = cliente;
				clienteNuevo.setConstrasenia(passwordEncoder.encode(cliente.getConstrasenia()));
				Set<Rol> roles = new HashSet<>();
				Rol rolUser = new Rol((RolNombre.ROLE_USER));
				roles.add(rolUser);
				clienteNuevo.setRoles(roles);
				if (clienteService.guardarCliente(clienteNuevo)) {
					if (clienteService.guardarClienteEnTXT(clienteNuevo)) {
						String respuesta="{\r\n  \"status\": \"200\",\r\n  \"message\": \"El usuario fue creado satisfactoriamente\",\r\n  \"code\": \"200\"\r\n}";
						logger.debug(respuesta);
						return new ResponseEntity(respuesta, HttpStatus.OK);
					} else {
						String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"El usuario fue creado pero no se pudieron almacenar la credenciales. Por favor comunicarse con soporte.\",\r\n  \"error\": \"No se pudieron almacenar credenciales\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte el log del cliente.\"\r\n}";
						logger.info(respuesta);
						return new ResponseEntity(respuesta,HttpStatus.BAD_REQUEST);
					}
				} else {
					String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"El usuario no se pudo almacenar en base de datos\",\r\n  \"error\": \"El usuario no se pudo almacenar en base de datos\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte el log del cliente.\"\r\n}";
					logger.info(respuesta);
					return new ResponseEntity(respuesta,HttpStatus.BAD_REQUEST);
				}
			} else {
				String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"El cliente ya existe\",\r\n  \"error\": \"El cliente ya existe\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"El cliente ya existe\"\r\n}";
				logger.info(respuesta);
				return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
			}
		} else {
			String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"Datos incompletos. Por favor ingrese todos los datos\",\r\n  \"error\": \"Datos incompletos\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Datos imcompletos\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Cliente cliente) {
		if (cliente.getUsuario() != null && cliente.getConstrasenia() != null) {
			try {
				Authentication authentication = authenticationManager
						.authenticate((Authentication) new UsernamePasswordAuthenticationToken(cliente.getUsuario(),
								cliente.getConstrasenia()));
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
				String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"Datos incorrectos en el logueo.\",\r\n  \"error\": \""+e.getCause() +"\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \""+e.getMessage()+"\"\r\n}";
				return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
			} catch (InternalAuthenticationServiceException e) {
				String log = "Usuario: " + cliente.getUsuario() + ". El usuario no existe";
				logger.info(log);
				String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"El usuario no existe.\",\r\n  \"error\": \""+e.getCause() +"\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \""+e.getMessage()+"\"\r\n}";
				return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				String log = "Usuario: " + cliente.getUsuario() + ". Error en " + e.getMessage();
				logger.error(log);
				String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"Error en "+e.getMessage()+"\",\r\n  \"error\": \""+e.getCause() +"\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \""+e.getMessage()+"\"\r\n}";
				return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
			}
		} else {
			String respuesta="{\r\n  \"status\": \"400\",\r\n  \"message\": \"Datos incompletos. Por favor ingrese todos los datos\",\r\n  \"error\": \"Datos incompletos\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Dato imcompletos\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}
	}

}