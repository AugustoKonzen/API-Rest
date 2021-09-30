package com.usuarios.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usuarios.api.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Usuario findById(long id);
	
	public Optional<Usuario> findByEmail(String email);
}
