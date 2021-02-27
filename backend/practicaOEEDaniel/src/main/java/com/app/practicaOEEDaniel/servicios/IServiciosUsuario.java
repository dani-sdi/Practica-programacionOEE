package com.app.practicaOEEDaniel.servicios;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public interface IServiciosUsuario {
	
	public ResponseEntity<String> guardar(String nombre ,String email,int edad, String pass, HttpServletRequest request, BCryptPasswordEncoder passCrpyt);
	
	public ResponseEntity<String> listarUsuarios(HttpServletRequest request);
	
	public ResponseEntity<String> borrarUsuario(String id, HttpServletRequest request);
	
	public ResponseEntity<String> getUsuario(String id, HttpServletRequest request);
	
	public ResponseEntity<String> modificar(String id,String nombre , String email,int edad , String pass, HttpServletRequest request);
	
	public ResponseEntity<String> login(String email, String pass, HttpServletRequest request, BCryptPasswordEncoder passCrpyt);
	
	public BCryptPasswordEncoder passwordEncoder();

}
