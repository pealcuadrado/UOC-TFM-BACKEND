package com.listoplan.jwt;

public class InfoSesion {

		private int idUsuario;
		private String email;
		private String nombre;
		private String apellido;
		public InfoSesion(int idUsuario, String email, String nombre, String apellido) {
			this.idUsuario = idUsuario;
			this.email = email;
			this.nombre = nombre;
			this.apellido = apellido;
		}
		public int getIdUsuario() {
			return idUsuario;
		}
		public void setIdUsuario(int idUsuario) {
			this.idUsuario = idUsuario;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getApellido() {
			return apellido;
		}
		public void setApellido(String apellido) {
			this.apellido = apellido;
		}
		
		
}
