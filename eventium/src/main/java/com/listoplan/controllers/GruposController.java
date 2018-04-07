package com.listoplan.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.listoplan.dao.GrupoDAO;
import com.listoplan.jwt.InfoSesion;
import com.listoplan.jwt.TokenUtils;
import com.listoplan.models.Grupo;
import com.listoplan.models.Usuario;

@CrossOrigin
@RestController
public class GruposController {
    @RequestMapping(value="/grupos/nuevo_grupo", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> nuevoGrupo(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String status= GrupoDAO.crearGrupo(is.getIdUsuario(), (String) resultado.get("nombreGrupo"));
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
    
    @RequestMapping(value="/grupos/modificacion_grupo", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> modificarGrupo(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		if(!GrupoDAO.esAdminGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= GrupoDAO.modificarGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")),(String) resultado.get("nombreGrupo"));
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
    
    @RequestMapping(value="/grupos/desactivacion_grupo", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> desactivarGrupo(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		if(!GrupoDAO.esAdminGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= GrupoDAO.desactivarGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")));
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
    
    @RequestMapping(value="/grupos/vinculacion_usuario_grupo", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> vincularUsuarioGrupo(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		if(!GrupoDAO.esAdminGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= GrupoDAO.vincularUsuarioAGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")),
    				Integer.parseInt((String) resultado.get("idUsuario")),Integer.parseInt((String) resultado.get("esAdministrador")));
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
    
    @RequestMapping(value="/grupos/desvinculacion_usuario_grupo", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> desvincularUsuarioGrupo(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		if(!GrupoDAO.esAdminGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= GrupoDAO.desvincularUsuarioDeGrupo(is.getIdUsuario(), Integer.parseInt((String) resultado.get("idGrupo")),
    				Integer.parseInt((String) resultado.get("idUsuario")));
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
    
    @RequestMapping(value="/grupos/informacion_grupo/{idGrupo}", method=RequestMethod.GET)
	    public ResponseEntity<Grupo> informacionGrupo(@PathVariable int idGrupo, @RequestHeader String token){
	    	if(TokenUtils.validarToken(token)==null) return new ResponseEntity<Grupo>(HttpStatus.UNAUTHORIZED);
	    	Grupo grupo= GrupoDAO.getGrupoPorId(idGrupo);
	    	if(grupo==null) {
	    		return new ResponseEntity<Grupo>(HttpStatus.BAD_REQUEST);
	    	}else {
	    		return new ResponseEntity<Grupo>(grupo,HttpStatus.OK);
	    	}
    }
    
    @RequestMapping(value="/grupos/grupos_usuario/{idUsuario}", method=RequestMethod.GET)
	    public ResponseEntity<ArrayList<Grupo>> gruposUsuario(@PathVariable int idUsuario, @RequestHeader String token){
	    	if(TokenUtils.validarToken(token)==null) return new ResponseEntity<ArrayList<Grupo>>(HttpStatus.UNAUTHORIZED);
	    	ArrayList<Grupo> grupos= GrupoDAO.getGruposUsuario(idUsuario);
	    	if(grupos==null) {
	    		return new ResponseEntity<ArrayList<Grupo>>(HttpStatus.BAD_REQUEST);
	    	}else {
	    		return new ResponseEntity<ArrayList<Grupo>>(grupos,HttpStatus.OK);
	    	}
    	}
    
    @RequestMapping(value="/grupos/usuarios_grupo/{idGrupo}", method=RequestMethod.GET)
    public ResponseEntity<ArrayList<Usuario>> usuariosGrupo(@PathVariable int idGrupo, @RequestHeader String token){
    	if(TokenUtils.validarToken(token)==null) return new ResponseEntity<ArrayList<Usuario>>(HttpStatus.UNAUTHORIZED);
    	ArrayList<Usuario> usuarios= GrupoDAO.getUsuariosGrupo(idGrupo);
    	if(usuarios==null) {
    		return new ResponseEntity<ArrayList<Usuario>>(HttpStatus.BAD_REQUEST);
    	}else {
    		return new ResponseEntity<ArrayList<Usuario>>(usuarios,HttpStatus.OK);
    	}
	}

}
