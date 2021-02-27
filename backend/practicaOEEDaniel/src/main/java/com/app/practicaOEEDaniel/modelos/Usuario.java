package com.app.practicaOEEDaniel.modelos;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario implements Serializable{
	@Id
	private String id;
	private String nombre;
	private String email;
	private int edad;
	private String pass;
	private byte rol;
	private boolean activo;
	private String token;

	
	public Usuario(String id, String nombre, String email, int edad, String pass) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.edad = edad;
		this.pass = pass;
	}
	
	public Usuario(String nombre, String email, int edad, String pass) {
		this.nombre = nombre;
		this.email = email;
		this.edad = edad;
		this.pass = pass;
	}
	
	public Usuario() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public byte getRol() {
		return rol;
	}

	public void setRol(byte rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		//Por motivos de seguridad, el campo pass no se pasa
		String result = ("{ \"id\" : \"" + this.getId() + "\", \"nombre\" : \"" + this.getNombre() + "\", \"email\" :\"" + this.getEmail() + "\", \"edad\" :\"" + String.valueOf(this.getEdad()) + "\" , \"token\" : \"" + this.getToken() + "\"}"); 
		return result;
	}
	
	
	
}
