package com.listoplan.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.listoplan.dao.GrupoDAO;
import com.listoplan.dao.ListaDAO;
import com.listoplan.dao.ListaDAO.AmbitoLista;
import com.listoplan.dao.ListaDAO.TipoLista;
import com.listoplan.jwt.InfoSesion;
import com.listoplan.jwt.TokenUtils;

@RestController
public class ListasController {
    @RequestMapping(value="/listas/nueva_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> nuevaLista(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String idRequest=(String) resultado.get("id");
    		String nombre=(String) resultado.get("nombre");
    		String descripcion=(String) resultado.get("descripcion");
    		String tipoLista= (String) resultado.get("tipoLista");
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		if(ambito.equals("GRUPO") && !GrupoDAO.esMiembroGrupo(is.getIdUsuario(), Integer.parseInt(idRequest))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		}
    		AmbitoLista an;
    		TipoLista tl;
    		int id;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    			id=Integer.parseInt(idRequest);
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    			id=is.getIdUsuario();
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		tl= ListaDAO.mapearTipoLista(tipoLista);
    		if(tl==null) {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Error: El tipo de lista indicado debe ser ORDENADA, CHECKLIST o REPARTICION");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		String status= ListaDAO.crearLista(id, nombre, descripcion,tl, an);
    		HashMap<String, String> respuesta = new HashMap<String,String>();
    		if(status.contains("Error")){
    			respuesta.put("status", status);
    			respuesta.put("resultado", "KO");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    		}else {
       		respuesta.put("status", status);
    			respuesta.put("resultado", "OK");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.OK);
    		}
    }
    
    @RequestMapping(value="/listas/modificar_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> modificarLista(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idRequest=Integer.parseInt((String)resultado.get("id"));
    		int idLista=Integer.parseInt((String)resultado.get("idLista"));
    		String nombre=(String) resultado.get("nombre");
    		String descripcion=(String) resultado.get("descripcion");
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoLista an;
    		int id;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    			id=idRequest;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    			id=is.getIdUsuario();
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		}
    		String status= ListaDAO.modificarLista(idLista, nombre, descripcion);
    		HashMap<String, String> respuesta = new HashMap<String,String>();
    		if(status.contains("Error")){
    			respuesta.put("status", status);
    			respuesta.put("resultado", "KO");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    		}else {
       		respuesta.put("status", status);
    			respuesta.put("resultado", "OK");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.OK);
    		}
    }
    @RequestMapping(value="/listas/desactivar_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> desactivarNota(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idRequest=Integer.parseInt((String) resultado.get("id"));
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoLista an;
    		int id;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    			id=idRequest;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    			id=is.getIdUsuario();
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		}
    		String status= ListaDAO.desactivarLista(idLista);
    		HashMap<String, String> respuesta = new HashMap<String,String>();
    		if(status.contains("Error")){
    			respuesta.put("status", status);
    			respuesta.put("resultado", "KO");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    		}else {
       		respuesta.put("status", status);
    			respuesta.put("resultado", "OK");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.OK);
    		}
    }
}
