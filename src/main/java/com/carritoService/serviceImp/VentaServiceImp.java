package com.carritoService.serviceImp;

import java.util.List;
import com.carritoService.model.Venta;
import com.carritoService.Dao.VentaDao;
import org.springframework.stereotype.Service;
import com.carritoService.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VentaServiceImp implements VentaService {

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

	@Override
	public boolean guardarDetalleVenta(Venta venta) {
		return ventaDao.guardarDetalleVenta(venta);
	}

}