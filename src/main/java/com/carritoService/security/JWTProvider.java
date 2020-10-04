package com.carritoService.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.carritoService.model.ClientesSeguridad;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTProvider {
	/* crea el token y tambien valida si el token esta bien formado */

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private int expiration;

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
			System.out.println("token mal formado");
		} catch (UnsupportedJwtException e) {
			System.out.println("token no soportado");
		} catch (ExpiredJwtException e) {
			System.out.println("token expirado");
		} catch (IllegalArgumentException e) {
			System.out.println("token vacío");
		} catch (SignatureException e) {
			System.out.println("fail en la firma");
		}
		return false;
	}
}