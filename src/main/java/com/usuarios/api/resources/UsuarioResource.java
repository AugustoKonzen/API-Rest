package com.usuarios.api.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.api.repository.UsuarioRepository;
import com.usuarios.api.services.UsuarioService;

import lombok.AllArgsConstructor;

import com.usuarios.api.models.Usuario;

@RestController
@RequestMapping(value="/api/usuarios")
public class UsuarioResource {
	@Autowired
	private UsuarioService usuarioService;
	
	private PasswordEncoder encoder;
	
	public UsuarioResource(UsuarioService usuario, PasswordEncoder encoder) {
		this.usuarioService = usuario;
		this.encoder = encoder;
	}
	
	@GetMapping("/selectAll")
	public ResponseEntity<List<Usuario>> selectTodos() {
		List<Usuario> lista = usuarioService.findAll();
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Usuario> selectPorId(@PathVariable(value="id") long id) {
		Usuario user = usuarioService.findById(id);
		return ResponseEntity.ok().body(user);
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrar(@RequestBody @Valid Usuario user) {
		user.setSenha(encoder.encode(user.getSenha()));
		usuarioService.save(user);
		return ResponseEntity.ok("Usuário cadastrado com sucesso");
	}
	
	@GetMapping("/validarSenha")
	public ResponseEntity<Boolean> validarSenha(@RequestParam String email, @RequestParam String senha) {
		Optional<Usuario> optUsuario = usuarioService.validarSenha(email);
		if (optUsuario.get() == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
		
		Usuario user = optUsuario.get();
        boolean valido = encoder.matches(senha, user.getSenha());

        HttpStatus status = (valido) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valido);
	}
	
	@DeleteMapping("/remover")
	public ResponseEntity<String> remover(@RequestBody Usuario user) {
		usuarioService.delete(user);
		return ResponseEntity.ok("Usuário removido com sucesso");
	}
	
	@PutMapping("/alterar")
	public ResponseEntity<String> alterar(@RequestBody Usuario user) {
		usuarioService.save(user);
		return ResponseEntity.ok("Usuário alterado com sucesso");
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleException(MethodArgumentNotValidException e) {
		Map<String, String> erros = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			
			erros.put(fieldName, errorMessage);
		});
		return erros;
	}
}
