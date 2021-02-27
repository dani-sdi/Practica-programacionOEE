package com.app.practicaOEEDaniel.almacenamiento;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.practicaOEEDaniel.modelos.Usuario;

@Repository
public interface MongoDB extends MongoRepository<Usuario, String> {
	public Usuario findUsuarioByEmail(String email);
}
