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
import com.listoplan.dao.NotaDAO;
import com.listoplan.dao.NotaDAO.AmbitoNota;
import com.listoplan.jwt.InfoSesion;
import com.listoplan.jwt.TokenUtils;

@RestController
public class NotasController {
    @RequestMapping(value="/nueva_nota", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> nuevaNota(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String idRequest=(String) resultado.get("id");
    		String titulo=(String) resultado.get("titulo");
    		String contenido=(String) resultado.get("contenido");
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		if(ambito.equals("GRUPO") && !GrupoDAO.esMiembroGrupo(is.getIdUsuario(), Integer.parseInt(idRequest))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		}
    		AmbitoNota an;
    		int id;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoNota.GRUPO;
    			id=Integer.parseInt(idRequest);
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoNota.USUARIO;
    			id=is.getIdUsuario();
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		String status= NotaDAO.crearNota(id, titulo, contenido, an);
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
    
    @RequestMapping(value="/modificar_nota", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> modificarNota(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idRequest=Integer.parseInt((String) resultado.get("id"));
    		int idNota=Integer.parseInt((String) resultado.get("idNota"));
    		String titulo=(String) resultado.get("titulo");
    		String contenido=(String) resultado.get("contenido");
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoNota an;
    		int id;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoNota.GRUPO;
    			id=idRequest;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoNota.USUARIO;
    			id=is.getIdUsuario();
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!NotaDAO.esPropietarioNota(id,idNota,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		}
    		String status= NotaDAO.modificarNota(idNota, titulo, contenido);
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
    @RequestMapping(value="/desactivar_nota", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> desactivarNota(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idRequest=Integer.parseInt((String) resultado.get("id"));
    		int idNota=Integer.parseInt((String) resultado.get("idNota"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoNota an;
    		int id;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoNota.GRUPO;
    			id=idRequest;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoNota.USUARIO;
    			id=is.getIdUsuario();
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!NotaDAO.esPropietarioNota(id,idNota,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		}
    		String status= NotaDAO.desactivarNota(idNota);
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
