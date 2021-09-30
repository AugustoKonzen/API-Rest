package com.usuarios.api.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.usuarios.api.data.DetalhesUsuario;
import com.usuarios.api.models.Usuario;
import com.usuarios.api.repository.UsuarioRepository;

@Component
public class DetalhesUsuarioService implements UserDetailsService {
	
	private final UsuarioRepository repository;

	public DetalhesUsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário " + username + " não encontrado");
		}
		return new DetalhesUsuario(usuario);
	}

}
