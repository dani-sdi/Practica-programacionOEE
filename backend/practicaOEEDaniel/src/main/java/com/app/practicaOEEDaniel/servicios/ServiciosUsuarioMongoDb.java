package com.app.practicaOEEDaniel.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.practicaOEEDaniel.almacenamiento.MongoDB;
import com.app.practicaOEEDaniel.almacenamiento.Sesion;
import com.app.practicaOEEDaniel.modelos.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class ServiciosUsuarioMongoDb implements IServiciosUsuario {
	
	@Autowired
	MongoDB repository;
	

	@Override
	public ResponseEntity<String> guardar(String nombre, String email, int edad, String pass,
		HttpServletRequest request, BCryptPasswordEncoder passCrpyt) {
		try {
			//Comprobamos si existe un usuario con el email introducido.
			Usuario user = repository.findUsuarioByEmail(email);
			if (user != null) {
				return new ResponseEntity<>("{\"message\":\"Este email ya está registrado\"}", HttpStatus.CONFLICT);
			}
			
			//Encriptamos la contraseña antes de ser guardada
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			Usuario usuario = new Usuario(nombre,email,edad,passwordEncoder.encode(pass));
			usuario = repository.save(usuario);
			return new ResponseEntity<>(usuario.toString(), HttpStatus.OK);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al guardar el usuario \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<String> listarUsuarios(HttpServletRequest request) {
		try {
			List<Usuario> usuarios = repository.findAll();
			System.out.println(usuarios);
			return new ResponseEntity<>(usuarios.toString(), HttpStatus.OK);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al listar usuarios \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> borrarUsuario(String id, HttpServletRequest request) {	
		try {
			//Antes de borrar el usuario comprobamos que existe.
			Usuario usuario = repository.findById(id).orElse(null);
			if(usuario == null) {
				return new ResponseEntity<>("{\"message\":\"El usuario no existe\"}", HttpStatus.NOT_FOUND);
			}
			
			repository.delete(usuario);
			return new ResponseEntity<>("{\"message\": \"Usuario eliminado\"}", HttpStatus.OK);
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al borrar usuario \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> getUsuario(String id, HttpServletRequest request) {
		try {
			Usuario usuario = repository.findById(id).orElse(null);
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
	public ResponseEntity<String> modificar(String id, String nombre, String email, int edad, String pass,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> login(String email, String pass, HttpServletRequest request,
			BCryptPasswordEncoder passCrpyt) {

		try {
			boolean passCorrecta;
			
			//Comprobamos si el usuario existe.
			Usuario usuario = repository.findUsuarioByEmail(email);
			if (usuario == null) {
				return new ResponseEntity<>("{\"message\":\"No hay ningún usuario con este email\"}", HttpStatus.NOT_FOUND);
			}

			//Se cimprueba si las contraseñas coinciden utilizando BCrypt.
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			passCorrecta = passwordEncoder.matches(pass,usuario.getPass());
			
			
			if (passCorrecta) {			
				String token = getJWTToken(email); //Generamos un token para este usuario.
				usuario.setToken(token); //Aasignamos el token para futuras peticiones.
				return new ResponseEntity<>(usuario.toString(), HttpStatus.OK);
			}else {
				return new ResponseEntity<>("{\"message\":\"Contraseña incorrecta\"}", HttpStatus.NOT_ACCEPTABLE);
			}
			
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<>("{ \"message\": \" Error al iniciar sesión \"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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

}
