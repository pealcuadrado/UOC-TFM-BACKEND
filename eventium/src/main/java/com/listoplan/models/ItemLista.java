package com.listoplan.models;

public class ItemLista {
	private int idItem;
	private String nombre;
	private String valor;
	private int item;
	
	public ItemLista(int idItem, String nombre, String valor, int item) {
		this.idItem = idItem;
		this.nombre = nombre;
		this.valor = valor;
		this.item = item;
	}

	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}
	
	
	
}
