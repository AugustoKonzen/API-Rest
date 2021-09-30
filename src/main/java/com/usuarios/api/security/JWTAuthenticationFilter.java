package com.usuarios.api.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuarios.api.data.DetalhesUsuario;
import com.usuarios.api.models.Usuario;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	public static final int tempoToken = 600_000;
	public static final String senhaToken = "39bcb7b8-08b8-4bea-b2c7-8ac0357974f4";
	
	private final AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuário", e);
		}
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		DetalhesUsuario usuarioData = (DetalhesUsuario) authResult.getPrincipal();
		
		String token = JWT.create().withSubject(usuarioData.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + tempoToken)).sign(Algorithm.HMAC512(senhaToken));
		
		response.getWriter().write(token);
		response.getWriter().flush();
	}
}
