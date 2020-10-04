package com.carritoService.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.carritoService.service.ClienteService;
import com.carritoService.serviceImp.ClienteServiceImp;

public class JWTFiltroToken extends OncePerRequestFilter{
	
	@Autowired
    JWTProvider jwtProvider;

	@Autowired
	ClienteServiceImp clienteServiceImp;
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
            String token = getToken(request);
            if(token != null && jwtProvider.validarToken(token)) {
            	String nombreUsuario = jwtProvider.obtenerUsuariodeltoken(token);
            	UserDetails userDetails = clienteServiceImp.loadUserByUsername(nombreUsuario);
            	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
            logger.error("fail en el método doFilter " + e.getMessage());
        }
        filterChain.doFilter(request, response);
	}
	
	private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }

}
