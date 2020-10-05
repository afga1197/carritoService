package com.carritoService.controller;

import java.util.List;
import com.carritoService.model.Venta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.carritoService.service.VentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/venta")
public class VentaController {

	@Autowired
	private VentaService ventaService;

	private static final Logger logger = LogManager.getLogger("venta");

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/listar")
	public ResponseEntity<List<Venta>> listar() {
		List<Venta> ventas = ventaService.obtenerVentas();
		return new ResponseEntity(ventas, HttpStatus.OK);
	}

	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Venta venta) {
		if (ventaService.guardarVenta(venta)) {
			String respuesta = "{\r\n  \"status\": \"200\",\r\n  \"message\": \"La venta fue creada satisfactoriamente\",\r\n  \"code\": \"200\",\r\n}";
			logger.debug(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.OK);
		} else {
			String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"No se pudo registrar la venta satisfactoriamente\",\r\n  \"error\": \"Error en el registro.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte log de ventas para mas informacion acerca del error.\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/crearDetalleVenta")
	public ResponseEntity<?> saveDetalleVenta(@RequestBody Venta venta) {
		if (ventaService.guardarDetalleVenta(venta)) {
			String respuesta = "{\r\n  \"status\": \"200\",\r\n  \"message\": \"El detalle de la venta fue creada satisfactoriamente\",\r\n  \"code\": \"200\",\r\n}";
			logger.debug(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.OK);
		} else {
			String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"No se pudo registrar el detalle de la venta satisfactoriamente\",\r\n  \"error\": \"Error en el registro.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte log de ventas para mas informacion acerca del error.\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}
	}

}