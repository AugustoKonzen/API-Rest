package com.usuarios.api.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usuarios.api.models.Usuario;
import com.usuarios.api.repository.UsuarioRepository;
import com.usuarios.api.services.exceptions.EntityNotFound;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuario;
	
	public Usuario findById(Long id) {
		return usuario.findById(id).orElseThrow(() -> new EntityNotFound("Usuário " + id + " Não encontrado"));
	}
	
	public List<Usuario> findAll() {
		return usuario.findAll();
	}
	
	public Usuario save(Usuario user) {
		return usuario.save(user);
	}
	
	public void delete(Usuario user) {
		usuario.delete(user);
	}
	
	public Optional<Usuario> login(String email) {
		return usuario.findByEmail(email);
	}
}
