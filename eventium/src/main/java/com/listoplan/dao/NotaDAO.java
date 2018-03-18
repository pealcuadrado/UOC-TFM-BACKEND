package com.listoplan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.listoplan.mysqlcontroller.MysqlManager;


public class NotaDAO {
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			sql=String.format("		select count(*) as num from grupo_notas " + 
					"		WHERE FK_ID_GRUPO='%s' " + 
					"		AND FK_ID_NOTA='%s' ",id, idNota);
		}
		else {
			return false;
		}
		ResultSet rs = MysqlManager.getInstance().query(sql);
		try {
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
			e.printStackTrace();
			return false;
		}
	}

}
