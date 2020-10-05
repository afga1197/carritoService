package com.carritoService.Dao;

import java.util.List;
import com.carritoService.model.Venta;

public interface VentaDao {

	List<Venta> obtenerVentas();
	boolean guardarVenta(Venta venta);
	boolean guardarDetalleVenta(Venta venta);

}