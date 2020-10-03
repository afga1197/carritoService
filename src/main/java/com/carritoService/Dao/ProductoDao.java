package com.carritoService.Dao;

import java.util.List;

import com.carritoService.model.Producto;

public interface ProductoDao {

	List<Producto> obtenerProductos();

	boolean guardarProducto(Producto producto);

	boolean actualizar(Producto producto);

	boolean borrar(int id);

}
