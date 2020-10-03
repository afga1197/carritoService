package com.carritoService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.carritoService.model.Venta;
import com.carritoService.service.VentaService;

@RestController
@RequestMapping("/venta")
public class VentaController {
	
	@Autowired
	private VentaService ventaService;
	
	@GetMapping("/listar")
	public ResponseEntity<List<Venta>> listar() {
		List<Venta> ventas = ventaService.obtenerVentas();
		return new ResponseEntity(ventas, HttpStatus.OK);
	}
	
	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Venta venta) {
		if (ventaService.guardarVenta(venta)) {
			return new ResponseEntity("El producto fue creado satisfactoriamente", HttpStatus.OK);
		} else {
			return new ResponseEntity("El producto no se pudo crear", HttpStatus.BAD_REQUEST);
		}
	}
	
}