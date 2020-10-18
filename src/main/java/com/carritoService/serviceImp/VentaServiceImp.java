package com.carritoService.serviceImp;

import rx.Single;
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

	@Override
	public Single<List<Venta>> buscarPorId(int id) {
		return Single.create(singleSubscriber -> {
			List<Venta> ventasId = ventaDao.buscarPorId(id);
			singleSubscriber.onSuccess(ventasId);
		});
	}

	@Override
	public Single<List<Venta>> buscarPorCliente(int id) {
		return Single.create(singleSubcriber ->{
			List<Venta> ventasCliente = ventaDao.buscarPorCliente(id);
			singleSubcriber.onSuccess(ventasCliente);
		});
	}
}