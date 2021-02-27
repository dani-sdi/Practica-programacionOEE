package com.app.practicaOEEDaniel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path= "/prueba")
public class ControladorPrueba {

	@RequestMapping(value = "/saludar")
	public @ResponseBody String guardar() {
		return "response: {message: \"hola\"}";
	}
}
