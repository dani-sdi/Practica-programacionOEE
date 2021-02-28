package com.app.practicaOEEDaniel.controladores;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.practicaOEEDaniel.modelos.Usuario;
import com.app.practicaOEEDaniel.servicios.ServiciosUsuarioMongoDb;
import com.app.practicaOEEDaniel.servicios.ServiciosUsuarioSesion;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping(path= "/usuarios")
public class ControladorUsuarios{
	
	private byte tipoMemoria = 1;
	private final byte SESSION = 0; // Memoria volatil.
	private final byte MONGODB = 1; // Memoria permanente.
			
	@Autowired
	BCryptPasswordEncoder passCrpyt;
	
	@Autowired
	private HttpServletRequest request;
	
	private ServiciosUsuarioSesion usuarioSesion = new ServiciosUsuarioSesion();
	
	@Autowired
	ServiciosUsuarioMongoDb usuarioMongoDb;
	

	/**
	 * Guarda nuevos usuarios.
	 */
	@PostMapping("/guardar")
	public @ResponseBody ResponseEntity<String> guardar(@RequestBody Usuario usuario) {
		ResponseEntity<String> respuesta = null;
		
		switch(tipoMemoria) {
			case SESSION:
				System.out.println("Datos sesion:" + usuario.getNombre() + usuario.getEmail());
				respuesta = usuarioSesion.guardar(usuario.getNombre(), usuario.getEmail(), usuario.getEdad(), usuario.getPass(), request, passCrpyt);
				break;
			case MONGODB:
				System.out.println("Datos mongo:" + usuario.getNombre() + usuario.getEmail());
				respuesta = usuarioMongoDb.guardar(usuario.getNombre(), usuario.getEmail(), usuario.getEdad(), usuario.getPass(), request, passCrpyt);
				break;
		}	
		return respuesta;
	}
	

	/**
	 * Lista los usuarios guardados.
	 */
	@GetMapping("/listar")
	public @ResponseBody ResponseEntity<String> listarUsuarios() {
		ResponseEntity<String> respuesta = null;
		switch(tipoMemoria) {
			case SESSION:
				respuesta = usuarioSesion.listarUsuarios(request);
				break;
			case MONGODB:
				respuesta = usuarioMongoDb.listarUsuarios(request);
				break;
		}	
		return respuesta;
	}
	
	/**
	 * Borra un usuario del array o de la base de datos.
	 */
	@RequestMapping(value = "/borrar", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> borrarUsuario(@RequestParam("id") String id) {
		ResponseEntity<String> respuesta = null;
		switch(tipoMemoria) {
			case SESSION:
				respuesta = usuarioSesion.borrarUsuario(id,request);
				break;
			case MONGODB:
				respuesta = usuarioMongoDb.borrarUsuario(id,request);
				break;
		}
		return respuesta;
	}
	
	/**
	 * Devuelve la información de un usuario en formato json
	 */
	@RequestMapping(value = "/usuario", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> getUsuario(@RequestParam("id") String id) {
		ResponseEntity<String> respuesta = null;
		switch(tipoMemoria) {
			case SESSION:
				respuesta = usuarioSesion.getUsuario(id,request);
				break;
			case MONGODB:
				respuesta = usuarioMongoDb.getUsuario(id,request);
				break;
		}
		return respuesta;
	}
	
	/**
	 * Modifica los datos de un usuario.
	 */
	@RequestMapping(value = "/modificar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> modificar(@RequestParam("id") String id,
														@RequestParam(value="nombre") String nombre , 
														@RequestParam(value="email") String email,
														@RequestParam(value="edad") int edad , 
														@RequestParam(value="pass") String pass){
		ResponseEntity<String> respuesta = null;
		switch(tipoMemoria) {
			case SESSION:
				respuesta = usuarioSesion.modificar(id, nombre, email, edad, pass, request);
				break;
			case MONGODB:
				respuesta = usuarioMongoDb.modificar(id, nombre, email, edad, pass, request);
				break;
		}
		return respuesta;

	}
	
	/**
	 * Realiza el login de un usuario en el backoffice.
	 */
	@PostMapping("/login")
	public @ResponseBody ResponseEntity<String> login(@RequestBody Usuario usuario){		
		ResponseEntity<String> respuesta = null;
		switch(tipoMemoria) {
			case SESSION:
				respuesta = usuarioSesion.login(usuario.getEmail(), usuario.getPass(), request, passCrpyt);	
				break;
			case MONGODB:
				respuesta = usuarioMongoDb.login(usuario.getEmail(), usuario.getPass(), request, passCrpyt);
				break;
		}	
		return respuesta;
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
}
