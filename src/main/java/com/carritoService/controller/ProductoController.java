package com.carritoService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carritoService.model.Producto;
import com.carritoService.service.ProductoService;

@RestController
@RequestMapping("/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping("/listar")
	public ResponseEntity<List<Producto>> listar() {
		List<Producto> productos = productoService.obtenerProductos();
		return new ResponseEntity(productos, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> save(@RequestBody Producto producto) {
		if (productoService.guardarProducto(producto)) {
			return new ResponseEntity("El producto fue creado satisfactoriamente", HttpStatus.OK);
		} else {
			return new ResponseEntity("El producto no se pudo crear", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Producto producto){
    	if (productoService.actualizar(producto)) {
    		return new ResponseEntity("producto actualizado", HttpStatus.OK);
    	} else {
    		return new ResponseEntity("producto no actualizado", HttpStatus.OK);
    	}
    }
    
	@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
    	if (productoService.borrar(id)) {
    		return new ResponseEntity("producto actualizado", HttpStatus.OK);
    	} else {
    		return new ResponseEntity("producto no actualizado", HttpStatus.OK);
    	}
    }

}