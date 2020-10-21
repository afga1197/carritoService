package com.carritoService;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import com.carritoService.DaoImp.ProductoDaoImp;
import com.carritoService.model.Producto;

class ProductoTest {

	@Test
	void test() {
		ProductoDaoImp productoDaoImp = new ProductoDaoImp();
		Producto producto = new Producto();
		producto.setIdProducto(0);
		producto.setNombre("pasta");
		producto.setPrecio(14.70);
		assertTrue(productoDaoImp.actualizar(null));
	}

}
