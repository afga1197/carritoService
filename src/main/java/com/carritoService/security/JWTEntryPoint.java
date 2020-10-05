package com.carritoService.security;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Component
public class JWTEntryPoint implements AuthenticationEntryPoint{

	private static final Logger logger = LogManager.getLogger("seguridad");
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		logger.error("Error de autorizacion");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no autorizado");
	}

}