package com.listoplan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.listoplan.models.Grupo;
import com.listoplan.models.Usuario;
import com.listoplan.mysqlcontroller.MysqlManager;

public class GrupoDAO {
	private static Logger logger=Logger.getLogger(GrupoDAO.class);
	public static String crearGrupo(int idUsuario, String nombreGrupo) {
		String status;
		String sql=String.format("CALL grupos_crear('%s','%s');", idUsuario,nombreGrupo);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El grupo se ha creado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al crear el grupo";
		}
		return status;
	}
	
	public static String modificarGrupo(int idUsuario,int idGrupo, String nombreGrupo) {
		String status;
		String sql=String.format("CALL grupos_modificar('%s','%s');", idGrupo,nombreGrupo);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El grupo se ha modificado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al modificar el grupo";
		}
		return status;
	}
	
	public static String desactivarGrupo(int idUsuario, int idGrupo) {
		String status;
		String sql=String.format("CALL grupos_desactivar('%s');", idGrupo);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El grupo se ha desactivado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al desactivar el grupo";
		}
		return status;
	}
	
	public static String vincularUsuarioAGrupo(int idUsuario, int idGrupo, int idUsuarioAVincular, int esAdministrador) {
		String status;
		String sql=String.format("CALL grupos_vincular_usuario(%s,%s,%s);", idGrupo,idUsuarioAVincular,esAdministrador);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El usuario ha sido vinculado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al vincular el usuario";
		}
		return status;
	}
	
	public static String desvincularUsuarioDeGrupo(int idUsuario, int idGrupo, int idUsuarioADesvincular) {
		String status;
		String sql=String.format("CALL grupos_desvincular_usuario(%s,%s);", idGrupo,idUsuarioADesvincular);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El usuario ha sido desvinculado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al desvincular el usuario";
		}
		return status;
	}
	
	public static boolean esAdminGrupo(int idUsuario, int idGrupo){
		String sql=String.format("		select count(*) as num from usuarios_grupos " + 
				"		WHERE FK_ID_USUARIO='%s' " + 
				"		AND FK_ID_GRUPO='%s' " + 
				"		AND ADMINISTRADOR=1;",idUsuario, idGrupo);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			if(rs.next()) {
				if(rs.getInt("num") > 0) {
					return true;
				}else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return false;
		}
	}
	
	public static boolean esMiembroGrupo(int idUsuario, int idGrupo){
		String sql=String.format("		select count(*) as num from usuarios_grupos " + 
				"		WHERE FK_ID_USUARIO='%s' " + 
				"		AND FK_ID_GRUPO='%s' ",idUsuario, idGrupo);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			if(rs.next()) {
				if(rs.getInt("num") > 0) {
					return true;
				}else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return false;
		}
	}
	
	public static Grupo getGrupoPorId(int idGrupo) {
		String sql=String.format("SELECT nombre FROM listoplan.grupos " + 
				"WHERE activo=1 and id_grupo=%s;",idGrupo);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			if(rs.next()) {
				String nombre=rs.getString("nombre");
				Grupo grupo= new Grupo(idGrupo,nombre);
				return grupo;
				
			} else {
				return null;
			}
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
	}
	
	public static ArrayList<Grupo> getGruposUsuario(int idUsuario) {
		ArrayList<Grupo> listadoGrupos = new ArrayList<Grupo>();
		String sql=String.format("select id_grupo, nombre from usuarios_grupos ug " + 
				"join grupos g on g.id_grupo=ug.fk_id_grupo " + 
				"where activo=1 " + 
				"and fk_id_usuario=%s;",idUsuario);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int id_grupo=rs.getInt("id_grupo");
				String nombre=rs.getString("nombre");
				Grupo grupo= new Grupo(id_grupo,nombre);
				listadoGrupos.add(grupo);
				
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return listadoGrupos;
	}
	
	public static ArrayList<Usuario> getUsuariosGrupo(int idGrupo) {
		ArrayList<Usuario> listadoUsuarios = new ArrayList<Usuario>();
		String sql=String.format("select id_usuario, email, nombre, apellido  from usuarios_grupos ug " + 
				"join usuarios u on u.id_usuario=ug.fk_id_usuario " + 
				"where activo=1 " + 
				"and fk_id_grupo=%s",idGrupo);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int id_usuario=rs.getInt("id_usuario");
				String email=rs.getString("email");
				String nombre=rs.getString("nombre");
				String apellido=rs.getString("apellido");
				Usuario usuario= new Usuario(id_usuario,nombre,apellido, email);
				listadoUsuarios.add(usuario);
				
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return listadoUsuarios;
	}

}
