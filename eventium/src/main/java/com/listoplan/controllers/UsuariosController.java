package com.listoplan.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.listoplan.dao.UsuarioDAO;
import com.listoplan.jwt.Token;
import com.listoplan.jwt.Token.EstadoToken;
import com.listoplan.jwt.TokenUtils;
import com.listoplan.models.Usuario;

import org.springframework.boot.json.JsonParser;


@RestController
public class UsuariosController {
	
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<Token> loginUsuario(@RequestBody String data) {
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		Token token=UsuarioDAO.loginUsuario((String) resultado.get("email"), (String) resultado.get("contrasena"));
    		if(token.getEstadoToken()==EstadoToken.KO) {
    			return new ResponseEntity<Token>(token, HttpStatus.UNAUTHORIZED);
    		}else {
    			return new ResponseEntity<Token>(token, HttpStatus.OK);
    		}
    }
    
    @RequestMapping(value="/nuevo_usuario", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> nuevoUsuario(@RequestBody String data) {
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String status= UsuarioDAO.crearUsuario((String) resultado.get("email"), (String) resultado.get("nombre"), 
    				(String) resultado.get("apellido"), (String) resultado.get("contrasena"));
    		HashMap<String, String> respuesta = new HashMap<String,String>();
    		if(status.contains("Error")){
    			respuesta.put("status", status);
    			respuesta.put("resultado", "KO");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.CONFLICT);
    		}else {
       		respuesta.put("status", status);
    			respuesta.put("resultado", "OK");
    			return new ResponseEntity<HashMap<String,String>>(respuesta, HttpStatus.OK);
    		}
    }
    
    @RequestMapping(value="/usuario/{idUsuario}", method=RequestMethod.GET)
    public ResponseEntity<Usuario> informacionUsuario(@PathVariable int idUsuario, @RequestHeader String token){
    	if(TokenUtils.validarToken(token)==null) return new ResponseEntity<Usuario>(HttpStatus.UNAUTHORIZED);
    	Usuario usuario= UsuarioDAO.getUsuarioPorId(idUsuario);
    	if(usuario==null) {
    		return new ResponseEntity<Usuario>(HttpStatus.BAD_REQUEST);
    	}else {
    		return new ResponseEntity<Usuario>(usuario,HttpStatus.OK);
    	}
    		
    }
}
