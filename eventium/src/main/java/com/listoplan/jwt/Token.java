package com.listoplan.jwt;

public class Token {
	public enum EstadoToken{OK, KO};
	
	private String token;
	private EstadoToken estadoToken;
	
	protected Token(String token, EstadoToken estadoToken){
		this.token=token;
		this.estadoToken=estadoToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public EstadoToken getEstadoToken() {
		return estadoToken;
	}

	public void setEstadoToken(EstadoToken estadoToken) {
		this.estadoToken = estadoToken;
	}
	
	
}
