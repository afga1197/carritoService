package com.carritoService.controller;

import rx.Single;
import java.util.List;
import rx.schedulers.Schedulers;

import com.carritoService.model.ErrorInfo;
import com.carritoService.model.Venta;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.ResponseEntity;
import com.carritoService.service.VentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/venta")
public class VentaController {

	@Autowired
	private VentaService ventaService;
	
	@Autowired
	private ErrorInfo errorInfo;

	private static final Logger logger = LogManager.getLogger(Venta.class);

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listar")
	public ResponseEntity<List<Venta>> listar() {
		List<Venta> ventas = ventaService.obtenerVentas();
		errorInfo=new ErrorInfo(HttpStatus.OK, "Se consultaron las ventas", HttpStatus.OK.value(), "Consulta exitosa");
		logger.debug(errorInfo);
		return new ResponseEntity(ventas, HttpStatus.OK);
	}

	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Venta venta) {
		if (ventaService.guardarVenta(venta)) {
			errorInfo=new ErrorInfo(HttpStatus.OK, "La venta fue creada satisfactoriamente", HttpStatus.OK.value(), "Consulta exitosa");
			logger.debug(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.OK);
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "No se pudo registrar la venta satisfactoriamente", HttpStatus.BAD_REQUEST.value(), "Consulte el log ventas para mas informacion");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/crearDetalleVenta")
	public ResponseEntity<?> saveDetalleVenta(@RequestBody Venta venta) {
		if (ventaService.guardarDetalleVenta(venta)) {
			errorInfo=new ErrorInfo(HttpStatus.OK, "El detalle de la venta fue creada satisfactoriamente", HttpStatus.OK.value(), "Consulta exitosa");
			logger.debug(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.OK);
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "No se pudo registrar el detalle de la venta satisfactoriamente", HttpStatus.BAD_REQUEST.value(), "Consulte el log ventas para mas informacion");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscarIdVenta/{id}")
	public Single<ResponseEntity<?>> buscarVentaId(@PathVariable("id") int id){
		return ventaService.buscarPorId(id).subscribeOn(Schedulers.io())
                .map(s -> new ResponseEntity(s,HttpStatus.OK));
	}
	
	@GetMapping("/buscarIdCliente/{id}")
	public Single<ResponseEntity<?>> buscarVentaCliente(@PathVariable("id") int id){
		return ventaService.buscarPorCliente(id).subscribeOn(Schedulers.io())
				.map(s -> new ResponseEntity(s,HttpStatus.OK));
	}

}