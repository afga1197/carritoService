package com.carritoService.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carritoService.controller.ClienteController;
import com.carritoService.serviceImp.ClienteServiceImp;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JWTFiltroToken extends OncePerRequestFilter {

	@Autowired
	JWTProvider jwtProvider;

	@Autowired
	ClienteServiceImp clienteServiceImp;
	
	private static final Logger logger = LogManager.getLogger(ClienteController.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = getToken(request);
			if (token != null && jwtProvider.validarToken(token)) {
				String nombreUsuario = jwtProvider.obtenerUsuariodeltoken(token);
				UserDetails userDetails = clienteServiceImp.loadUserByUsername(nombreUsuario);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			logger.error("Error del metodo doFilter, con excepcion en: " + e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer"))
			return header.replace("Bearer ", "");
		return null;
	}

}