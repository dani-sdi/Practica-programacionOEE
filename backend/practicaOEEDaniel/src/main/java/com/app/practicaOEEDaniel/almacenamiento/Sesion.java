package com.app.practicaOEEDaniel.almacenamiento;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.app.practicaOEEDaniel.modelos.Usuario;

@Controller
public class Sesion {	
	public static boolean crearSesion(Model model,Usuario usuario, HttpSession session) {
		try {
			List<Usuario> usuarios = (List<Usuario>) session.getAttribute("usuarios");
			if(usuarios == null) {
				usuarios = new ArrayList<Usuario>();
			}
			model.addAttribute("usuarios", usuarios);			
			return true;
		}catch(Exception ex) {
			System.out.println(ex.toString());
			return false;
		}	
	}
	
	public static boolean guardarUsuario(Usuario usuario, HttpServletRequest  request) {
		System.out.println("Hola mundo desde la sesion");
		try {
			List<Usuario> usuarios = (List<Usuario>) request.getSession().getAttribute("usuarios");
			if(usuarios == null) {
				usuarios = new ArrayList<Usuario>();
			}
			usuarios.add(usuario);	
			request.getSession().setAttribute("usuarios", usuarios);	
			System.out.println(request.getSession());
			return true;
		}catch(Exception ex) {
			System.out.println(ex.toString());
			return false;
		}	
	}
	
	public static List<Usuario> getUsuarios(HttpServletRequest  request){
		List<Usuario> usuarios = (ArrayList<Usuario>) request.getSession().getAttribute("usuarios");
		System.out.println(request.getSession());
		return usuarios;
	}
}
