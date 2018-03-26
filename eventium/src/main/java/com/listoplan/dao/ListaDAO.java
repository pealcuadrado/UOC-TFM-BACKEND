package com.listoplan.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.listoplan.models.ItemLista;
import com.listoplan.models.Lista;
import com.listoplan.mysqlcontroller.MysqlManager;

public class ListaDAO {
	private static Logger logger= Logger.getLogger(ListaDAO.class);
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
			logger.error("Error: ",e);
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
			logger.error("Error: ",e);
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
			logger.error("Error: ",e);
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
			logger.error("Error: ",e);
			return false;
		}
	}
	
	public static String crearItem(int idLista, String nombre, String valor, int orden) {
		String status;
		String sql=String.format("CALL listas_crear_item('%s','%s','%s','%s');", idLista,nombre,valor,orden);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El item se ha creado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al crear el item";
		}
		return status;
	}
	
	public static String modificarItem(int idItem, int idLista, String nombre, String valor, int orden) {
		String status;
		String sql=String.format("CALL listas_modificar_item('%s','%s','%s','%s','%s');",idItem, idLista,nombre,valor,orden);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El item se ha modificado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al modificar el item";
		}
		return status;
	}
	
	public static String eliminarItem(int idItem, int idLista) {
		String status;
		String sql=String.format("CALL listas_eliminar_item('%s','%s');",idItem, idLista);
		try {
			MysqlManager.getInstance().execute(sql);
			status="El item se ha eliminado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al eliminar el item";
		}
		return status;
	}
	
	
	public static Lista getListaPorId(int idLista) {
		
		String sqlItems=String.format("SELECT ID_ITEM, NOMBRE_ITEM, VALOR_ITEM, ORDEN FROM listoplan.lista_item\n" + 
				"where fk_id_lista='%s'\n" + 
				"order by orden asc;",idLista);
		
		String sqlListas=String.format("select id_lista, nombre, descripcion, tipo_lista, FECHA_MODIFICACION, compartida from listas l " + 
				"join tm_tipo_lista tl on l.fk_id_tipo=tl.ID_TIPO_LISTA " + 
				"where activo=1 " + 
				"and id_lista='%s' " + 
				"order by fecha_modificacion desc;",idLista);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sqlItems);
			ArrayList<ItemLista> items = new ArrayList<ItemLista>();
			while(rs.next()) {
				int idItem=rs.getInt("id_item");
				String nombre=rs.getString("nombre_item");
				String valor=rs.getString("valor_item");
				int orden=rs.getInt("orden");
				ItemLista item= new ItemLista(idItem, nombre, valor, orden);
				items.add(item);
				
			}
			rs = MysqlManager.getInstance().query(sqlListas);
			if(rs.next()) {
				String nombre=rs.getString("nombre");
				String descripcion=rs.getString("descripcion");
				String tipoLista=rs.getString("tipo_lista");
				Date fecha=rs.getDate("fecha_modificacion");
				Time hora=rs.getTime("fecha_modificacion");
				int compartida=rs.getInt("Compartida");
				Lista lista= new Lista(idLista, nombre, descripcion, tipoLista, fecha, hora, items,compartida);
				return lista;
				
			} else {
				return null;
			}
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
	}
	
	public static ArrayList<Lista> getListasUsuario(int idUsuario) {
		ArrayList<Lista> listas= new ArrayList<Lista>();
		//No se retorna todo el contenido de la nota, solo los primeros 20 carácteres
		String sql=String.format("select ID_LISTA, NOMBRE, DESCRIPCION, "
				+ "fecha_modificacion, compartida " + 
				"from usuario_listas ul " + 
				"join listas l on ul.FK_ID_LISTA=l.ID_LISTA " + 
				"WHERE activo=1 " + 
				"AND fk_id_usuario=%s " + 
				"order by FECHA_MODIFICACION desc;",idUsuario);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int idLista=rs.getInt("id_lista");
				String nombre=rs.getString("nombre");
				String descripcion=rs.getString("descripcion");
				Date fecha=rs.getDate("fecha_modificacion");
				Time hora=rs.getTime("fecha_modificacion");
				int compartida=rs.getInt("Compartida");
				Lista lista= new Lista(idLista, nombre, descripcion, null,fecha, hora,null, compartida);
				listas.add(lista);
				
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return listas;
	}
	
	public static ArrayList<Lista> getListasGrupo(int idGrupo) {
		ArrayList<Lista> listas= new ArrayList<Lista>();
		String sql=String.format("select ID_LISTA, NOMBRE, DESCRIPCION, "
				+ "fecha_modificacion, compartida " + 
				"from grupo_listas gl " + 
				"join grupos g on gl.FK_ID_LISTA=g.ID_LISTA " + 
				"WHERE activo=1 " + 
				"AND fk_id_grupo=%s " + 
				"order by FECHA_MODIFICACION desc;",idGrupo);
		try {
			ResultSet rs = MysqlManager.getInstance().query(sql);
			while(rs.next()) {
				int idLista=rs.getInt("id_lista");
				String nombre=rs.getString("nombre");
				String descripcion=rs.getString("descripcion");
				Date fecha=rs.getDate("fecha_modificacion");
				Time hora=rs.getTime("fecha_modificacion");
				int compartida=rs.getInt("Compartida");
				Lista lista= new Lista(idLista, nombre, descripcion, null,fecha, hora,null,compartida);
				listas.add(lista);
				
			} 
		} catch (SQLException e) {
			logger.error("Error: ",e);
			return null;
		}
		return listas;
	}
	
	public static String modificarComparticionLista(int idLista, int compartida) {
		String status;
		String sql=String.format("CALL listas_compartir(%s,%s);",idLista, compartida);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La visibilidad de la lista se ha modificado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al modificar la visibilidad de la lista";
		}
		return status;
	}
	
	public static String duplicarListaCompartida(int id, int idLista, AmbitoLista ambitoLista) {
		String status;
		String sql=String.format("CALL listas_duplicar('%s','%s','%s');",id, idLista, ambitoLista);
		try {
			MysqlManager.getInstance().execute(sql);
			status="La lista se ha copiado correctamente";
		}
		catch(Exception e) {
			logger.error("Error: ",e);
			status="Error: Se ha producido un error al copiar la lista";
		}
		return status;
	}
	
	public static boolean esListaCompartida(int idLista){
		String sql=String.format("select count(*) as num from listas " + 
				"		WHERE ACTIVO=1 AND COMPARTIDA=1 AND ID_LISTA='%s'", idLista);
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
	
}
