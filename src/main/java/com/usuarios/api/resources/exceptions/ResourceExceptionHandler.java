package com.usuarios.api.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.usuarios.api.services.exceptions.EntityNotFound;

@ControllerAdvice
public class ResourceExceptionHandler {
	@ExceptionHandler(EntityNotFound.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFound e, HttpServletRequest request) {
		StandardError erro = new StandardError();
		erro.setTimestamp(Instant.now());
		erro.setStatus(HttpStatus.NOT_FOUND.value());
		erro.setError("Resource not found");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
}
