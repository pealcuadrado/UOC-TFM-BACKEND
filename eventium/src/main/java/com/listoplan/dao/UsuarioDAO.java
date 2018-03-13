package com.listoplan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.listoplan.models.Usuario;
import com.listoplan.mysqlcontroller.MysqlManager;
import com.listoplan.jwt.Token;
import com.listoplan.jwt.TokenUtils;

public class UsuarioDAO {

	public static Token loginUsuario(String email, String contrasena) {
		String sql=String.format("SELECT id_usuario, nombre, apellido FROM listoplan.usuarios " + 
				"WHERE EMAIL='%s' " + 
				"AND CONTRASENA =SHA2(CONCAT(SALT,'%s'),256);",email, contrasena);
		ResultSet rs = MysqlManager.getInstance().query(sql);
		try {
			if(rs.next()) {
				int idUsuario=rs.getInt("id_usuario");
				String nombre=rs.getString("nombre");
				String apellido=rs.getString("apellido");
				Usuario usuarioLogeado= new Usuario(idUsuario,email,nombre,apellido);
				return TokenUtils.crearToken(usuarioLogeado);
				
			} else {
				return TokenUtils.crearToken(null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return TokenUtils.crearToken(null);
		}
	}
	
	public static String crearUsuario(String email, String nombre, String apellido, String contrasena) {
		String status;
		//Comprobar que el usuario no existe
		String sql=String.format("SELECT count(*) as num FROM listoplan.usuarios WHERE email='%s';",email);
		ResultSet rs= MysqlManager.getInstance().query(sql);
		try {
			rs.next();
			if(rs.getInt("num") > 0) {
				status= "Error: El usuario ya existe";
			}
			else {
				//Crear usuario
				sql=String.format("CALL usuarios_crear('%s','%s','%s','%s');", email,nombre,apellido,contrasena);
				MysqlManager.getInstance().execute(sql);
				status="Usuario creado correctamente";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			status= "Error: Se ha producido un error al registrar el usuario";
			
		}		
		return status;
	}
	
	public static Usuario getUsuarioPorId(int idUsuario) {
		String sql=String.format("SELECT email, nombre, apellido FROM listoplan.usuarios " + 
				"WHERE id_usuario=%s;",idUsuario);
		ResultSet rs = MysqlManager.getInstance().query(sql);
		try {
			if(rs.next()) {
				String email=rs.getString("email");
				String nombre=rs.getString("nombre");
				String apellido=rs.getString("apellido");
				Usuario usuario= new Usuario(idUsuario,email,nombre,apellido);
				return usuario;
				
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}