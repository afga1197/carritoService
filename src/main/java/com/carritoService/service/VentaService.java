package com.carritoService.service;

import java.util.List;

import com.carritoService.model.Venta;

public interface VentaService {

	List<Venta> obtenerVentas();

	boolean guardarVenta(Venta venta);

}
