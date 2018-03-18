package com.listoplan.models;

import java.sql.Date;
import java.sql.Time;

public class Nota {

	private int idNota;
	private String titulo;
	private String contenido;
	private Date fechaModificacion;
	private Time horaModificacion;
	
	
	public Nota(int idNota, String titulo, String contenido, Date fechaModificacion, Time horaModificacion) {
		super();
		this.idNota = idNota;
		this.titulo = titulo;
		this.contenido = contenido;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
	}
	public int getIdNota() {
		return idNota;
	}
	public void setIdNota(int idNota) {
		this.idNota = idNota;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Time getHoraModificacion() {
		return horaModificacion;
	}
	public void setHoraModificacion(Time horaModificacion) {
		this.horaModificacion = horaModificacion;
	}
	
	
	
	
	
}
