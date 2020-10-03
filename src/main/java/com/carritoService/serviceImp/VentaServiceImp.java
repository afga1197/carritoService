package com.carritoService.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carritoService.Dao.VentaDao;
import com.carritoService.model.Venta;
import com.carritoService.service.VentaService;

@Service
public class VentaServiceImp implements VentaService{

	@Autowired
	private VentaDao ventaDao;
	
	@Override
	public List<Venta> obtenerVentas() {
		return ventaDao.obtenerVentas();
	}

	@Override
	public boolean guardarVenta(Venta venta) {
		return ventaDao.guardarVenta(venta);
	}

}
