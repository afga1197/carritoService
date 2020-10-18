package com.carritoService.service;

import rx.Single;
import java.util.List;
import com.carritoService.model.Venta;

public interface VentaService {

	List<Venta> obtenerVentas();
	boolean guardarVenta(Venta venta);
	boolean guardarDetalleVenta(Venta venta);
	Single<List<Venta>> buscarPorId(int id);
	Single<List<Venta>> buscarPorCliente(int id);
	
}