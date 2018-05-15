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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.listoplan.dao.GrupoDAO;
import com.listoplan.dao.ListaDAO;
import com.listoplan.dao.ListaDAO.AmbitoLista;
import com.listoplan.dao.ListaDAO.TipoLista;
import com.listoplan.jwt.InfoSesion;
import com.listoplan.jwt.TokenUtils;
import com.listoplan.models.Lista;

@CrossOrigin
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
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		AmbitoLista an;
    		TipoLista tl;
    		int id;
    		if(ambito.equals("GRUPO")){
    			id=Integer.parseInt(idRequest);
    			an=AmbitoLista.GRUPO;
    		}
    		else if (ambito.equals("USUARIO")) {
    			id=is.getIdUsuario();
    			an=AmbitoLista.USUARIO;
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
    
    @RequestMapping(value="/listas/modificacion_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> modificarLista(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idLista=Integer.parseInt((String)resultado.get("idLista"));
    		String nombre=(String) resultado.get("nombre");
    		String descripcion=(String) resultado.get("descripcion");
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoLista an;
    		int id=is.getIdUsuario();;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
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
    @RequestMapping(value="/listas/desactivacion_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> desactivarLista(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoLista an;
    		int id=is.getIdUsuario();
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
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
    
    @RequestMapping(value="/listas/nuevo_item", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> nuevoItem(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String idRequest=(String) resultado.get("id");
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		String nombre=(String) resultado.get("nombre");
    		String valor=(String) resultado.get("valor");
    		int orden= Integer.parseInt((String) resultado.get("orden"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		if(ambito.equals("GRUPO") && !GrupoDAO.esMiembroGrupo(is.getIdUsuario(), Integer.parseInt(idRequest))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		AmbitoLista an;
    		int id=is.getIdUsuario();;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= ListaDAO.crearItem(idLista, nombre, valor, orden);
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
    
    @RequestMapping(value="/listas/modificacion_item", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> modificarItem(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String idRequest=(String) resultado.get("id");
    		int idItem=Integer.parseInt((String) resultado.get("idItem"));
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		String nombre=(String) resultado.get("nombre");
    		String valor=(String) resultado.get("valor");
    		int orden= Integer.parseInt((String) resultado.get("orden"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		if(ambito.equals("GRUPO") && !GrupoDAO.esMiembroGrupo(is.getIdUsuario(), Integer.parseInt(idRequest))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		AmbitoLista an;
    		int id=is.getIdUsuario();;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= ListaDAO.modificarItem(idItem, idLista, nombre, valor, orden);
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
    
    @RequestMapping(value="/listas/eliminacion_item", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> eliminarItem(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		String idRequest=(String) resultado.get("id");
    		int idItem=Integer.parseInt((String) resultado.get("idItem"));
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		if(ambito.equals("GRUPO") && !GrupoDAO.esMiembroGrupo(is.getIdUsuario(), Integer.parseInt(idRequest))){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		AmbitoLista an;
    		int id=is.getIdUsuario();;
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= ListaDAO.eliminarItem(idItem, idLista);
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
    
    @RequestMapping(value="/listas/detalle_lista_usuario/{idLista}", method= RequestMethod.GET)
    public ResponseEntity<Lista> detalleListaUsuario(@PathVariable String idLista, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<Lista>(HttpStatus.UNAUTHORIZED);
    		if(!ListaDAO.esPropietarioLista(is.getIdUsuario(),Integer.parseInt(idLista),AmbitoLista.USUARIO)){
    			return new ResponseEntity<Lista>(HttpStatus.FORBIDDEN);
    		}
    		Lista lista= ListaDAO.getListaPorId(Integer.parseInt(idLista));
    		return new ResponseEntity<Lista>(lista, HttpStatus.OK);
    }
    
    @RequestMapping(value="/listas/detalle_lista_grupo/{idLista}", method= RequestMethod.GET)
    public ResponseEntity<Lista> detalleListaGrupo(@PathVariable String idLista, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<Lista>(HttpStatus.UNAUTHORIZED);
    		if(!ListaDAO.esPropietarioLista(is.getIdUsuario(),Integer.parseInt(idLista),AmbitoLista.GRUPO)){
    			return new ResponseEntity<Lista>(HttpStatus.FORBIDDEN);
    		}
    		Lista lista= ListaDAO.getListaPorId(Integer.parseInt(idLista));
    		return new ResponseEntity<Lista>(lista, HttpStatus.OK);
    }
    
    @RequestMapping(value="/listas/listas_usuario/", method= RequestMethod.GET)
    public ResponseEntity<ArrayList<Lista>> listasUsuario(@RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<ArrayList<Lista>>(HttpStatus.UNAUTHORIZED);
    		ArrayList<Lista> lista= ListaDAO.getListasUsuario(is.getIdUsuario());
    		return new ResponseEntity<ArrayList<Lista>>(lista, HttpStatus.OK);
    }
    
    @RequestMapping(value="/listas/listas_grupo/{idGrupo}", method= RequestMethod.GET)
    public ResponseEntity<ArrayList<Lista>> listasGrupo(@PathVariable String idGrupo, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<ArrayList<Lista>>(HttpStatus.UNAUTHORIZED);
    		ArrayList<Lista> lista= ListaDAO.getListasGrupo(Integer.parseInt(idGrupo));
    		return new ResponseEntity<ArrayList<Lista>>(lista, HttpStatus.OK);
    }
    
    @RequestMapping(value="/listas/comparticion_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> comparticionLista(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int compartida=Integer.parseInt((String) resultado.get("compartida"));
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		int idRequest=Integer.parseInt((String) resultado.get("id"));
    		String ambito=((String) resultado.get("ambito")).toUpperCase();
    		AmbitoLista an;
    		int id=is.getIdUsuario();
    		if(ambito.equals("GRUPO")){
    			an=AmbitoLista.GRUPO;
    			
    		}
    		else if (ambito.equals("USUARIO")) {
    			an=AmbitoLista.USUARIO;
    			
    		}
    		else {
    			HashMap<String,String> res = new HashMap<String,String>();
    			res.put("status","Ámbito no válido, debe ser USUARIO o GRUPO");
    			res.put("resultado","KO");
    			return new ResponseEntity<HashMap<String,String>>(res,HttpStatus.BAD_REQUEST);
    		}
    		if(!ListaDAO.esPropietarioLista(id,idLista,an)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= ListaDAO.modificarComparticionLista(idLista, compartida);
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
    
    @RequestMapping(value="/listas/duplicacion_lista", method= RequestMethod.POST)
    public ResponseEntity<HashMap<String,String>> duplicarListaCompartida(@RequestBody String data, @RequestHeader String token) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<HashMap<String,String>>(HttpStatus.UNAUTHORIZED);
    		JsonParser jp = JsonParserFactory.getJsonParser();
    		Map<String, Object> resultado = jp.parseMap(data);
    		int idLista=Integer.parseInt((String) resultado.get("idLista"));
    		int idRequest=Integer.parseInt((String) resultado.get("id"));
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
    		if(!ListaDAO.esListaCompartida(idLista)){
    			return new ResponseEntity<HashMap<String,String>>(HttpStatus.FORBIDDEN);
    		}
    		String status= ListaDAO.duplicarListaCompartida(id, idLista, an);
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
    
    @RequestMapping(value="/listas/listas_compartidas", method= RequestMethod.GET)
    public ResponseEntity<ArrayList<Lista>> listasCompartidas(@RequestHeader String token, @RequestParam String filtro) {
    		InfoSesion is=TokenUtils.validarToken(token);
    		if(is==null) return new ResponseEntity<ArrayList<Lista>>(HttpStatus.UNAUTHORIZED);
    		ArrayList<Lista> lista= ListaDAO.getListasCompartidas(filtro);
    		return new ResponseEntity<ArrayList<Lista>>(lista, HttpStatus.OK);
    }
}
