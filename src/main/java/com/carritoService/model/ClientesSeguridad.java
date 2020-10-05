package com.carritoService.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ClientesSeguridad implements UserDetails {

	private String apellido;
	private String constrasenia;
	private long dni;
	private String email;
	private int idCliente;
	private String nombre;
	private long telefono;
	private String usuario;
	private Collection<? extends GrantedAuthority> auhorities;

	public ClientesSeguridad(String apellido, String constrasenia, long dni, String email, int idCliente, String nombre,
			long telefono, String usuario, Collection<? extends GrantedAuthority> auhorities) {
		this.apellido = apellido;
		this.constrasenia = constrasenia;
		this.dni = dni;
		this.email = email;
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.telefono = telefono;
		this.usuario = usuario;
		this.auhorities = auhorities;
	}

	public static ClientesSeguridad build(Cliente cliente) {
		List<GrantedAuthority> authorities = cliente.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.getRolNombre().name())).collect(Collectors.toList());
		return new ClientesSeguridad(cliente.getApellido(), cliente.getConstrasenia(), cliente.getDni(), cliente.getEmail(), cliente.getIdCliente(), cliente.getNombre(), cliente.getTelefono(), cliente.getUsuario(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return auhorities;
	}

	@Override
	public String getPassword() {
		return constrasenia;
	}

	@Override
	public String getUsername() {
		return usuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}