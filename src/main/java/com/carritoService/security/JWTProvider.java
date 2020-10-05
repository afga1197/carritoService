package com.carritoService.security;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.Logger;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;
import com.carritoService.model.ClientesSeguridad;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JWTProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private int expiration;

	private static final Logger logger = LogManager.getLogger("seguridad");

	public String generarToken(Authentication authentication) {
		ClientesSeguridad clienteSeguridad = (ClientesSeguridad) authentication.getPrincipal();
		long time = System.currentTimeMillis();
		return Jwts.builder().setSubject(clienteSeguridad.getUsername()).setIssuedAt(new Date(time))
				.setExpiration(new Date(time + (expiration * 250))).signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public String obtenerUsuariodeltoken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validarToken(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException e) {
			String log = "Token mal formado, con excepcion en: " + e.getMessage();
			logger.error(log);
		} catch (UnsupportedJwtException e) {
			String log = "Token no soportado, con excepcion en: " + e.getMessage();
			logger.error(log);
		} catch (ExpiredJwtException e) {
			String log = "Token expirado, con excepcion en: " + e.getMessage();
			logger.error(log);
		} catch (IllegalArgumentException e) {
			String log = "Token vacio, con excepcion en: " + e.getMessage();
			logger.error(log);
		} catch (SignatureException e) {
			String log = "Fallo en la firma, con excepcion en: " + e.getMessage();
			logger.error(log);
		}
		return false;
	}
}