package com.carritoService.service;

import java.util.List;

import com.carritoService.model.Producto;

public interface ProductoService {

	List<Producto> obtenerProductos();

	boolean guardarProducto(Producto producto);

	boolean actualizar(Producto producto);

	boolean borrar(int id);

}
