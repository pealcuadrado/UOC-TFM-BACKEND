package com.listoplan.dao;

import java.sql.ResultSet;

import java.sql.SQLException;

import com.listoplan.models.Nota;
import com.listoplan.mysqlcontroller.MysqlManager;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import org.apache.log4j.Logger;


public class NotaDAO {
	private static Logger logger= Logger.getLogger(NotaDAO.class);
	public enum AmbitoNota {USUARIO,GRUPO};
	
	public static String crearNota(int id, String titulo, String contenido, AmbitoNota ambitoNota) {
		String status;
		String ambito;
		if(ambitoNota==AmbitoNota.USUARIO) ambito="USUARIO";
		else ambito="GRUPO";
		String sql=String.format("CALL notas_crear('%s','%s','%s','%s');", id,titulo,contenido,ambito);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La nota se ha creado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al crear la nota";
		}
		return status;
	}
	
	public static String modificarNota(int idNota, String titulo, String contenido) {
		String status;
		String sql=String.format("CALL notas_modificar('%s','%s','%s');", idNota,titulo,contenido);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La nota se ha modificado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al modificar la nota";
		}
		return status;
	}
	
	public static String desactivarNota(int id) {
		String status;
		String sql=String.format("CALL notas_desactivar('%s');", id);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La nota se ha desactivado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al desactivar la nota";
		}
		return status;
	}
	
	public static boolean esPropietarioNota(int id, int idNota, AmbitoNota ambito){
		String sql;
		if (ambito==AmbitoNota.USUARIO) {
			sql=String.format("		select count(*) as num from usuario_notas " + 
				"		WHERE FK_ID_USUARIO='%s' " + 
				"		AND FK_ID_NOTA='%s' ",id, idNota);
		}
		else if (ambito==AmbitoNota.GRUPO) {
			sql=String.format("		select count(*) as num from grupo_notas gn " + 
					"		join usuarios_grupos ug " + 
					"		ON ug.FK_ID_GRUPO=gn.FK_ID_GRUPO "+
					"		WHERE FK_ID_USUARIO='%s' " + 
					"		AND FK_ID_NOTA='%s' ",id, idNota);
		}
		else {
			return false;
		}
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
	
	public static Nota getNotaPorId(int idNota) {
		String sql=String.format("select titulo,contenido,fecha_modificacion from notas " + 
				"where activo=1 and id_nota=%s;",idNota);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			if(rs.next()) {
				String titulo=rs.getString("titulo");
				String contenido=rs.getString("contenido");
				Date fecha=rs.getDate("fecha_modificacion");
				Time hora=rs.getTime("fecha_modificacion");
				Nota nota= new Nota(idNota, titulo, contenido, fecha, hora);
				return nota;
				
			} else {
				return null;
			}
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
	}
	
	public static ArrayList<Nota> getNotasUsuario(int idUsuario) {
		ArrayList<Nota> notas= new ArrayList<Nota>();
		//No se retorna todo el contenido de la nota, solo los primeros 20 carácteres
		String sql=String.format("select id_nota, titulo, if(length(contenido) > 20 " + 
				", concat(substr(contenido,1,100),'...'), substr(contenido,1,100)) " + 
				"as contenido, fecha_modificacion from usuario_notas un " + 
				"join notas n on n.id_nota=un.fk_id_nota " + 
				"where activo=1 and fk_id_usuario=%s order by fecha_modificacion desc;",idUsuario);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int idNota=rs.getInt("id_nota");
				String titulo=rs.getString("titulo");
				String contenido=rs.getString("contenido");
				Date fecha=rs.getDate("fecha_modificacion");
				Time hora=rs.getTime("fecha_modificacion");
				Nota nota= new Nota(idNota, titulo, contenido, fecha, hora);
				notas.add(nota);
				
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return notas;
	}

	public static ArrayList<Nota> getNotasGrupo(int idGrupo) {
		ArrayList<Nota> notas= new ArrayList<Nota>();
		//No se retorna todo el contenido de la nota, solo los primeros 20 carácteres
		String sql=String.format("select id_nota, titulo, if(length(contenido) > 20 " + 
				", concat(substr(contenido,1,20),'...'), substr(contenido,1,20)) " + 
				"as contenido, fecha_modificacion from grupo_notas gn " + 
				"join notas n on n.id_nota=gn.fk_id_nota " + 
				"where activo=1 and fk_id_grupo=%s order by fecha_modificacion desc;",idGrupo);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int idNota=rs.getInt("id_nota");
				String titulo=rs.getString("titulo");
				String contenido=rs.getString("contenido");
				Date fecha=rs.getDate("fecha_modificacion");
				Time hora=rs.getTime("fecha_modificacion");
				Nota nota= new Nota(idNota, titulo, contenido, fecha, hora);
				notas.add(nota);
				
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return notas;
	}
}
