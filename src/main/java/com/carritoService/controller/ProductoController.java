package com.carritoService.controller;

import java.util.List;
import org.apache.logging.log4j.Logger;

import com.carritoService.model.ErrorInfo;
import com.carritoService.model.Producto;
import org.springframework.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.ResponseEntity;
import com.carritoService.service.ProductoService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ErrorInfo errorInfo;
	
	private static final Logger logger = LogManager.getLogger(Producto.class);

	@GetMapping("/listar")
	public ResponseEntity<List<Producto>> listar() {
		List<Producto> productos = productoService.obtenerProductos();
		errorInfo=new ErrorInfo(HttpStatus.OK, "Se consultaron los productos en la base de datos", HttpStatus.OK.value(), "Consulta exitosa");
		logger.debug(errorInfo);
		return new ResponseEntity(productos, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Producto producto) {
		if (producto.getNombre() != null && producto.getPrecio() != 0) {
			if (productoService.guardarProducto(producto)) {
				errorInfo=new ErrorInfo(HttpStatus.OK, "El producto fue creado satisfactoriamente", HttpStatus.OK.value(), "Operacion exitosa");
				logger.debug(errorInfo);
				return new ResponseEntity(errorInfo, HttpStatus.OK);
			} else {
				errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "No se pudo registrar el producto satisfactoriamente", HttpStatus.BAD_REQUEST.value(), "Consulte el log productos para mas informacion");
				logger.info(errorInfo);
				return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
			}
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "Datos incompletos. Por favor ingrese todos los datos.", HttpStatus.BAD_REQUEST.value(), "Datos imcompletos.");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody Producto producto) {
		if (producto.getNombre() != null && producto.getPrecio() != 0) {
			if (productoService.actualizar(producto)) {
				errorInfo=new ErrorInfo(HttpStatus.OK, "El producto fue actualizado satisfactoriamente", HttpStatus.OK.value(), "Operacion exitosa");
				logger.debug(errorInfo);
				return new ResponseEntity(errorInfo, HttpStatus.OK);
			} else {
				errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "No se pudo actualizar el producto satisfactoriamente", HttpStatus.BAD_REQUEST.value(), "Consulte el log productos para mas informacion.");
				logger.info(errorInfo);
				return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
			}
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "Datos incompletos. Por favor ingrese todos los datos.", HttpStatus.BAD_REQUEST.value(), "Datos imcompletos.");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/borrar/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		if (productoService.borrar(id)) {
			errorInfo=new ErrorInfo(HttpStatus.OK, "El producto fue eliminado satisfactoriamente", HttpStatus.OK.value(), "Operacion exitosa");
			logger.debug(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.OK);
		} else {
			errorInfo=new ErrorInfo(HttpStatus.BAD_REQUEST, "No se pudo eliminar el producto satisfactoriamente.", HttpStatus.BAD_REQUEST.value(), "Consulte el log productos para mas informacion.");
			logger.info(errorInfo);
			return new ResponseEntity(errorInfo, HttpStatus.BAD_REQUEST);
		}
	}

}