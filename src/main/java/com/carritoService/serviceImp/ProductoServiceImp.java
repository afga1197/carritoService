package com.carritoService.serviceImp;

import java.util.List;
import com.carritoService.model.Producto;
import com.carritoService.Dao.ProductoDao;
import org.springframework.stereotype.Service;
import com.carritoService.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProductoServiceImp implements ProductoService {

	@Autowired
	private ProductoDao productoDao;

	@Override
	public List<Producto> obtenerProductos() {
		return productoDao.obtenerProductos();
	}

	@Override
	public boolean guardarProducto(Producto producto) {
		return productoDao.guardarProducto(producto);
	}

	@Override
	public boolean actualizar(Producto producto) {
		return productoDao.actualizar(producto);
	}

	@Override
	public boolean borrar(int id) {
		return productoDao.borrar(id);
	}

}