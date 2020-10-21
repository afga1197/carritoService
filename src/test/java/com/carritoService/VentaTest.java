package com.carritoService;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import com.carritoService.DaoImp.ProductoDaoImp;
import com.carritoService.DaoImp.VentaDaoImp;
import com.carritoService.model.Producto;
import com.carritoService.model.Venta;

class VentaTest {

	@Test
	void test() {
		VentaDaoImp ventaDaoImpl = new VentaDaoImp();
		Venta venta = null;
		assertTrue(ventaDaoImpl.guardarVenta(venta));
	}

}
