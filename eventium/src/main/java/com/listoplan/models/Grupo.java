package com.listoplan.models;

public class Grupo {
	private int idGrupo;
	private String nombre;
	
	public Grupo(int idGrupo, String nombre) {
		this.idGrupo = idGrupo;
		this.nombre = nombre;
	}
	public int getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
