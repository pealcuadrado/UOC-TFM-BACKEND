package com.listoplan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.listoplan.mysqlcontroller.MysqlManager;

public class ListaDAO {
	public enum AmbitoLista {USUARIO,GRUPO};
	public enum TipoLista {ORDENADA,CHECKLIST,REPARTICION};
	
	private static int getTipoLista(TipoLista tipoLista) {
		int tl=0;
		if (tipoLista==TipoLista.ORDENADA) tl=1;
		else if (tipoLista==TipoLista.CHECKLIST) tl=2;
		else if (tipoLista==TipoLista.REPARTICION) tl=3;
		return tl;
	}
	public static TipoLista mapearTipoLista(String tipoLista) {
		TipoLista tl=null;
		if (tipoLista.equals("ORDENADA")) tl=TipoLista.ORDENADA;
		else if (tipoLista.equals("CHECKLIST")) tl=TipoLista.CHECKLIST;
		else if (tipoLista.equals("REPARTICION")) tl=TipoLista.REPARTICION;
		return tl;
	}
	
	public static String crearLista(int id, String nombre, String descripcion, TipoLista tipoLista, AmbitoLista ambitoNota) {
		String status;
		String ambito;
		int tl=0;
		if(ambitoNota==AmbitoLista.USUARIO) ambito="USUARIO";
		else ambito="GRUPO";
		
		tl=getTipoLista(tipoLista);

		String sql=String.format("CALL listas_crear('%s','%s','%s','%s','%s');", id,nombre,descripcion,Integer.toString(tl),ambito);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La lista se ha creado correctamente";
		}
		catch(Exception e) {
			e.printStackTrace();
			status="Error: Se ha producido un error al crear la lista";
		}
		return status;
	}
	
	public static String modificarLista(int idLista, String nombre, String descripcion) {
		String status;
		String sql=String.format("CALL listas_modificar('%s','%s','%s');", idLista,nombre,descripcion);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La lista se ha modificado correctamente";
		}
		catch(Exception e) {
			e.printStackTrace();
			status="Error: Se ha producido un error al modificar la lista";
		}
		return status;
	}
	
	public static String desactivarLista(int idLista) {
		String status;
		String sql=String.format("CALL listas_desactivar('%s');", idLista);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La lista se ha desactivado correctamente";
		}
		catch(Exception e) {
			e.printStackTrace();
			status="Error: Se ha producido un error al desactivar la lista";
		}
		return status;
	}
	
	public static boolean esPropietarioLista(int id, int idLista, AmbitoLista ambito){
		String sql;
		if (ambito==AmbitoLista.USUARIO) {
			sql=String.format("select count(*) as num from usuario_listas " + 
				"		WHERE FK_ID_USUARIO='%s' " + 
				"		AND FK_ID_LISTA='%s' ",id, idLista);
		}
		else if (ambito==AmbitoLista.GRUPO) {
			sql=String.format("		select count(*) as num from grupo_listas " + 
					"		WHERE FK_ID_GRUPO='%s' " + 
					"		AND FK_ID_LISTA='%s' ",id, idLista);
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
			e.printStackTrace();
			return false;
		}
	}
}
