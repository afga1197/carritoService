package com.carritoService.controller;

import java.util.Set;
import java.util.HashSet;
import com.carritoService.model.JWT;
import com.carritoService.model.Rol;
import org.apache.logging.log4j.Logger;
import com.carritoService.model.Cliente;
import com.carritoService.model.ErrorInfo;
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
	private ClienteService clienteService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTProvider jwtProvider;
	
	@Autowired
	private ErrorInfo errorInfo;

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
						errorInfo=new ErrorInfo(HttpStatus.OK, "El usuario fue creado satisfactoriamente", HttpStatus.OK.value(), "Proceso exitoso");
						logger.debug(errorInfo);
						return new ResponseEntity(errorInfo, HttpStatus.OK);
					} else {
						errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "El usuario fue creado pero no se pudieron almacenar la credenciales. Por favor comunicarse con soporte", HttpStatus.BAD_REQUEST.value(), "Consulte el log cliente para mas informacion");
						logger.info(errorInfo);
						return new ResponseEntity(errorInfo,HttpStatus.BAD_REQUEST);
					}
				} else {
					errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "El usuario no se pudo almacenar en base de datos", HttpStatus.BAD_REQUEST.value(), "Consulte el log cliente para mas informacion");
					logger.info(errorInfo);
					return new ResponseEntity(errorInfo,HttpStatus.BAD_REQUEST);
				}
			} else {
				errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "El cliente ya existe", HttpStatus.BAD_REQUEST.value(), "El cliente ya existe");
				logger.info(errorInfo);
				return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
			}
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "Datos incompletos. Por favor ingrese todos los datos", HttpStatus.BAD_REQUEST.value(), "Datos imcompletos");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
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
				errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "Datos incorrectos en el logueo", HttpStatus.BAD_REQUEST.value(), e.getMessage());
				return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
			} catch (InternalAuthenticationServiceException e) {
				String log = "Usuario: " + cliente.getUsuario() + ". El usuario no existe";
				logger.info(log);
				errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "El usuario no existe", HttpStatus.BAD_REQUEST.value(), e.getMessage());
				return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				String log = "Usuario: " + cliente.getUsuario() + ". Error en " + e.getMessage();
				logger.error(log);
				errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "Error en "+e.getMessage(), HttpStatus.BAD_REQUEST.value(), e.getMessage());
				return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
			}
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "Datos incompletos. Por favor ingrese todos los datos", HttpStatus.BAD_REQUEST.value(), "Datos imcompletos");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}

}