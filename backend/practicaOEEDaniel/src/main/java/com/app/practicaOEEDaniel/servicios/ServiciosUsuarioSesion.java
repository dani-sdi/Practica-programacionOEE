package com.app.practicaOEEDaniel.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.practicaOEEDaniel.almacenamiento.Sesion;
import com.app.practicaOEEDaniel.modelos.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class ServiciosUsuarioSesion implements IServiciosUsuario {
	public int proximoIdUsuario = 1;
	
	@Override
	public ResponseEntity<String> guardar(String nombre, String email, int edad, String pass, HttpServletRequest request, BCryptPasswordEncoder passCrpyt) {
		try {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			List<Usuario> usuarios = new ArrayList<Usuario>();
			
			if(usuarios != null) {
				Usuario usuario = usuarios.stream()
					.filter(user -> email.equals(user.getEmail()))
					.findAny()
					.orElse(null);
				if(usuario != null) {
					return new ResponseEntity<>("{\"message\":\"Este email ya está registrado\"}", HttpStatus.CONFLICT);
				}
			}
			Usuario usuario = new Usuario(String.valueOf(proximoIdUsuario),nombre,email,edad,passwordEncoder.encode(pass));
			Sesion.guardarUsuario(usuario, request);
			proximoIdUsuario++; //Se actualiza el valor. Adquiere el valor para el id del próximo usuario creado
			return new ResponseEntity<>(usuario.toString(), HttpStatus.OK);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al guardar el usuario \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@Override
	public ResponseEntity<String> listarUsuarios(HttpServletRequest request) {
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			usuarios =  Sesion.getUsuarios(request);
			return new ResponseEntity<>(usuarios.toString(), HttpStatus.OK);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al listar usuarios \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> borrarUsuario(String id, HttpServletRequest request) {
		boolean eliminado;
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			usuarios =  Sesion.getUsuarios(request);

			Usuario us = usuarios.stream()
					.filter(user -> id.equals(user.getId()))
					.findAny()
					.orElse(null);
			
			if(us == null) {
				return new ResponseEntity<>("{\"message\":\"El usuario no existe\"}", HttpStatus.NOT_FOUND);
			}
			
			eliminado = usuarios.removeIf(usuario -> id.equals(usuario.getId()));
			
			if (eliminado) {
				return new ResponseEntity<>("{\"message\": \"Usuario eliminado\"}", HttpStatus.OK);
			}else {
				return new ResponseEntity<>("{\"message\": \"No se ha eliminado ningún usuario\"}", HttpStatus.NOT_FOUND);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al borrar \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

	@Override
	public ResponseEntity<String> getUsuario(String id, HttpServletRequest request) {
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			usuarios =  Sesion.getUsuarios(request);
			if(usuarios == null) {
				return new ResponseEntity<>("{\"message\":\"No hay usuarios almacenados\"}", HttpStatus.NOT_FOUND);
			}
			Usuario usuario = usuarios.stream()
					.filter(user -> id.equals(user.getId()))
					.findAny()
					.orElse(null);
			
			if (usuario != null) {
				return new ResponseEntity<>(usuario.toString(), HttpStatus.OK);
			}else {
				return new ResponseEntity<>("{ \"message\": \"El usuario no existe\"}", HttpStatus.NOT_FOUND);
			}
		}catch(Exception ex) {
			return new ResponseEntity<>("{ \"message\": \" Error al seleccionar al usuario \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> modificar(String id, String nombre, String email, int edad, String pass, HttpServletRequest request) {
		try {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			usuarios =  Sesion.getUsuarios(request);
			
			if(usuarios == null) {
				return new ResponseEntity<>("{\"message\":\"No hay usuarios almacenados\"}", HttpStatus.NOT_FOUND);
			}
			
			Usuario usuario = usuarios.stream()
					.filter(user -> id.equals(user.getId()))
					.findAny()
					.orElse(null);
			
			if (usuario == null) {
				return new ResponseEntity<>("{\"message\":\"El usuario que intentas modificar no existe\"}", HttpStatus.NOT_FOUND);
			}
			
			if(usuarios != null) {
				Usuario hayEmailDuplicado = usuarios.stream()
					.filter(user -> email.equals(user.getEmail()) && id != user.getId())
					.findAny()
					.orElse(null);
				if(hayEmailDuplicado != null) {
					return new ResponseEntity<>("{\"message\":\"Ya existe un correo con este usuario\"}", HttpStatus.OK);
				}
			}
			
			usuario.setNombre(nombre);
			usuario.setEmail(email);
			usuario.setEdad(edad);
			usuario.setPass(pass);
			
			return new ResponseEntity<>(usuario.toString(), HttpStatus.OK);
		}catch(Exception ex) {
			return new ResponseEntity<>("{ \"message\": \" Error al modificar usuario \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public ResponseEntity<String> login(String email, String pass, HttpServletRequest request, BCryptPasswordEncoder passCrpyt) {
		
		try {
			boolean passCorrecta;
			List<Usuario> usuarios = new ArrayList<Usuario>();
			usuarios =  Sesion.getUsuarios(request);
			Usuario usuario = usuarios.stream()
					.filter(user -> email.equals(user.getEmail()))
					.findAny()
					.orElse(null);
			
			if (usuario == null) {
				return new ResponseEntity<>("{\"message\":\"No hay ningún usuario con este email\"}", HttpStatus.NOT_FOUND);
			}
			
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			passCorrecta = passwordEncoder.matches(pass,usuario.getPass());
			
			if (passCorrecta) {			
				String token = getJWTToken(email);
				usuario.setToken(token);						
				return new ResponseEntity<>(usuario.toString(), HttpStatus.OK);
			}else {
				return new ResponseEntity<>("{\"message\":\"Contraseña incorrecta\"}", HttpStatus.NOT_ACCEPTABLE);
			}
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al iniciar sesión \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String getJWTToken(String username) {
		String secretKey = "Dr10-0491";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
