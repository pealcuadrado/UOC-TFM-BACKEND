package com.listoplan.models;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Lista {
	private int idLista;
	private String nombre;
	private String descripcion;
	private String tipoLista;
	private Date fecha;
	private Time hora;
	private ArrayList<ItemLista> items;
	private int compartida;
	
	
	public Lista(int idLista, String nombre, String descripcion, String tipoLista, Date fecha, Time hora,
			ArrayList<ItemLista> items, int compartida) {
		super();
		this.idLista = idLista;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipoLista = tipoLista;
		this.fecha = fecha;
		this.hora = hora;
		this.items = items;
		this.compartida=compartida;
	}
	
	public int getIdLista() {
		return idLista;
	}
	public void setIdLista(int idLista) {
		this.idLista = idLista;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipoLista() {
		return tipoLista;
	}
	public void setTipoLista(String tipoLista) {
		this.tipoLista = tipoLista;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Time getHora() {
		return hora;
	}
	public void setHora(Time hora) {
		this.hora = hora;
	}
	public ArrayList<ItemLista> getItems() {
		return items;
	}
	public void setItems(ArrayList<ItemLista> items) {
		this.items = items;
	}

	public int getCompartida() {
		return compartida;
	}

	public void setCompartida(int compartida) {
		this.compartida = compartida;
	}
	
	
	
}
