
package com.example.gestionscolaire.authentication.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		String message = null;
		final Exception exception = (Exception) request.getAttribute("exception");
		if (exception != null) {
			log.info(exception.toString());
			message = exception.getMessage();
			log.error("Unauthorized error: {}", message);
		} else {
			if (authException.getCause() != null) {
				message = authException.getCause().toString() + " " + authException.getMessage();
			} else {
				message = authException.getMessage();
			}
			log.error("Unauthorized error: {}", authException.getMessage());
		}
		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED,messageSource.getMessage("security.unauthorized", null, LocaleContextHolder.getLocale()));
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,message);
	}
	
}
