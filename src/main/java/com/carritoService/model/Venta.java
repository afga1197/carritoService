package com.carritoService.model;

import java.sql.Date;

public class Venta {

	private int idVenta;
	private int idDetalleVenta;
	private int idCliente;
	private int idProducto;
	private Date fecha;

	public Venta(int idVenta, int idDetalleVenta, int idCliente, int idProducto, Date fecha) {
		this.idVenta = idVenta;
		this.idDetalleVenta = idDetalleVenta;
		this.idCliente = idCliente;
		this.idProducto = idProducto;
		this.fecha = fecha;
	}

	public Venta(int idVenta, int idCliente, Date fecha) {
		this.idVenta = idVenta;
		this.idCliente = idCliente;
		this.fecha = fecha;
	}

	public Venta(int idVenta, int idDetalleVenta, int idProducto) {
		this.idVenta = idVenta;
		this.idDetalleVenta = idDetalleVenta;
		this.idProducto = idProducto;
	}

	public int getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(int idVenta) {
		this.idVenta = idVenta;
	}

	public int getIdDetalleVenta() {
		return idDetalleVenta;
	}

	public void setIdDetalleVenta(int idDetalleVenta) {
		this.idDetalleVenta = idDetalleVenta;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}