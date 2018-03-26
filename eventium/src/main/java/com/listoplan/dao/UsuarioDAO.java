package com.listoplan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.listoplan.models.Usuario;
import com.listoplan.mysqlcontroller.MysqlManager;
import com.listoplan.jwt.Token;
import com.listoplan.jwt.TokenUtils;

public class UsuarioDAO {

	private static Logger logger= Logger.getLogger(UsuarioDAO.class);
	public static Token loginUsuario(String email, String contrasena) {
		String sql=String.format("SELECT id_usuario, nombre, apellido FROM listoplan.usuarios " + 
				"WHERE EMAIL='%s' AND ACTIVO=1 " + 
				"AND CONTRASENA =SHA2(CONCAT(SALT,'%s'),256);",email, contrasena);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
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
			logger.error("Error: ",e);
			return TokenUtils.crearToken(null);
		}
	}
	
	public static String crearUsuario(String email, String nombre, String apellido, String contrasena) {
		String status;
		//Comprobar que el usuario no existe
		String sql=String.format("SELECT count(*) as num FROM listoplan.usuarios WHERE email='%s';",email);
		try {
			ResultSet rs= MysqlManager.getInstance().query(sql);
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
			logger.error("Error: ",e);
			status= "Error: Se ha producido un error al registrar el usuario";
			
		}		
		return status;
	}
	
	public static Usuario getUsuarioPorId(int idUsuario) {
		String sql=String.format("SELECT email, nombre, apellido FROM listoplan.usuarios " + 
				"WHERE activo=1 AND id_usuario=%s;",idUsuario);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				String email=rs.getString("email");
				String nombre=rs.getString("nombre");
				String apellido=rs.getString("apellido");
				Usuario usuario= new Usuario(idUsuario,email,nombre,apellido);
				return usuario;
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return null;
	}
	
	public static ArrayList<Usuario> buscarUsuarios(String filtro) {
		ArrayList<Usuario> usuarios= new ArrayList<Usuario>();
		String sql=String.format("select id_usuario, email, nombre, apellido  from usuarios " + 
				"where (LOWER(email) like '%%%s%%' " + 
				"OR LOWER(CONCAT(nombre,apellido)) like '%%%s%%') AND activo=1;",filtro.toLowerCase(), filtro.toLowerCase());
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int idUsuario=rs.getInt("id_usuario");
				String email=rs.getString("email");
				String nombre=rs.getString("nombre");
				String apellido=rs.getString("apellido");
				Usuario usuario= new Usuario(idUsuario,nombre,apellido,email);
				usuarios.add(usuario);	
			}
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return usuarios;
	}
	
	
}
