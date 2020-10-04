package com.carritoService.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JWTEntryPoint implements AuthenticationEntryPoint{

	private static final Logger logger = LogManager.getLogger(JWTEntryPoint.class.getName());
	
	/*Rechaza las peticiones no autenticadas*/
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		logger.error("Error de autorizacion");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no autorizado");
	}

}