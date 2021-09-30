package com.usuarios.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTValidationFilter extends BasicAuthenticationFilter {
	
	public static final String header = "Authorization";
    public static final String prefixo = "Bearer ";

	public JWTValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String atributo = request.getHeader(header);
		
		if (atributo == null) {
			chain.doFilter(request, response);
			return;
		}
		
		if (!atributo.startsWith(prefixo)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = atributo.replace(prefixo, "");
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		String usuario = JWT.require(Algorithm.HMAC512(JWTAuthenticationFilter.senhaToken)).build().verify(token).getSubject();
		
		if (usuario == null) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
	}

}
