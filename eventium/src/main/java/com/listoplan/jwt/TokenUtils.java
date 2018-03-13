package com.listoplan.jwt;

import java.util.Date;

import com.listoplan.jwt.Token.EstadoToken;
import com.listoplan.models.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class TokenUtils {
	
	//Time-to-live del token en segundos
	private static final int TTL=604800; //7 dias
	private static final String SECRET="sjksfdfd345jdij0345kdcv9";

	public static Token crearToken(Usuario usuario){
		if(usuario==null) {
			return new Token(null,EstadoToken.KO);
		}
		Long millisActual= System.currentTimeMillis();
		Date d= new Date(millisActual+TTL*1000);
		String jwt=Jwts.builder()
				.setSubject(Integer.toString(usuario.getId_usuario()))
				.setExpiration(d)
				.claim("email",usuario.getEmail())
				.claim("nombre",usuario.getNombre())
				.claim("apellido", usuario.getApellido())
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
		return new Token(jwt, EstadoToken.OK);
	}
	
	public static InfoSesion validarToken(String token){
		try{
			Jws<Claims> claimsJws= Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			int idUsuario= Integer.parseInt(claimsJws.getBody().getSubject());
			String email= (String) claimsJws.getBody().get("email");
			String nombre= (String) claimsJws.getBody().get("nombre");
			String apellido= (String) claimsJws.getBody().get("apellido");
			return new InfoSesion(idUsuario,email,nombre,apellido);
		}
		catch(SignatureException e){
			return null;
		}
		catch(ExpiredJwtException e){
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	

}
