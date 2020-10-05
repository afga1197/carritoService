package com.carritoService;

import org.junit.jupiter.api.Test;
import com.carritoService.DaoImp.ProductoDaoImp;
import com.carritoService.model.Producto;

class VentaTest {

	@Test
	void test() {
		ProductoDaoImp productoDaoImp = new ProductoDaoImp();
		Producto producto = null;
		productoDaoImp.actualizar(producto);
	}

}
