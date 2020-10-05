package com.carritoService.controller;

import java.util.List;
import com.carritoService.model.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.carritoService.service.ProductoService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	private static final Logger logger = LogManager.getLogger("producto");

	@GetMapping("/listar")
	public ResponseEntity<List<Producto>> listar() {
		List<Producto> productos = productoService.obtenerProductos();
		return new ResponseEntity(productos, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Producto producto) {
		if (producto.getNombre() != null && producto.getPrecio() != 0) {
			if (productoService.guardarProducto(producto)) {
				String respuesta = "{\r\n  \"status\": \"200\",\r\n  \"message\": \"El producto fue creado satisfactoriamente\",\r\n  \"code\": \"200\",\r\n}";
				logger.debug(respuesta);
				return new ResponseEntity(respuesta, HttpStatus.OK);
			} else {
				String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"No se pudo registrar el producto satisfactoriamente\",\r\n  \"error\": \"Error en el registro.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte log de productos para mas informacion acerca del error.\"\r\n}";
				logger.info(respuesta);
				return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
			}
		} else {
			String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"Datos incompletos. Por favor ingrese todos los datos.\",\r\n  \"error\": \"Datos incompletos.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Dato imcompletos.\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody Producto producto) {
		if (producto.getNombre() != null && producto.getPrecio() != 0) {
			if (productoService.actualizar(producto)) {
				String respuesta = "{\r\n  \"status\": \"200\",\r\n  \"message\": \"El producto fue actualizado satisfactoriamente\",\r\n  \"code\": \"200\",\r\n}";
				logger.debug(respuesta);
				return new ResponseEntity(respuesta, HttpStatus.OK);
			} else {
				String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"No se pudo actualizar el producto satisfactoriamente\",\r\n  \"error\": \"Error en la actualizacion.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte log de productos para mas informacion acerca del error.\"\r\n}";
				logger.info(respuesta);
				return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
			}
		} else {
			String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"Datos incompletos. Por favor ingrese todos los datos.\",\r\n  \"error\": \"Datos incompletos.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Dato imcompletos.\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/borrar/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		if (productoService.borrar(id)) {
			String respuesta = "{\r\n  \"status\": \"200\",\r\n  \"message\": \"El producto fue eliminado satisfactoriamente\",\r\n  \"code\": \"200\",\r\n}";
			logger.debug(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.OK);
		} else {
			String respuesta = "{\r\n  \"status\": \"400\",\r\n  \"message\": \"No se pudo eliminar el producto satisfactoriamente\",\r\n  \"error\": \"Error al eliminar.\",\r\n  \"code\": \"400\",\r\n  \"BackEndMessage\": \"Consulte log de productos para mas informacion acerca del error.\"\r\n}";
			logger.info(respuesta);
			return new ResponseEntity(respuesta, HttpStatus.BAD_REQUEST);
		}
	}

}