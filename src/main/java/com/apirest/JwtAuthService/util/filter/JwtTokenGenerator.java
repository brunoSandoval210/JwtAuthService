package com.apirest.JwtAuthService.util.filter;

import com.apirest.JwtAuthService.controller.dtos.AuthLoginRequest;
import com.apirest.JwtAuthService.controller.dtos.AuthResponse;
import com.apirest.JwtAuthService.services.UserDetailServiceImpl;
import com.apirest.JwtAuthService.util.JwtUtils;
import com.apirest.JwtAuthService.util.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenGenerator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

            if ("/auth/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())){
                try{
                    AuthLoginRequest authLoginRequest = new ObjectMapper().readValue(request.getInputStream(), AuthLoginRequest.class);

                    Authentication authentication = authenticate(
                                    authLoginRequest.username(),
                                    authLoginRequest.password()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = jwtUtils.generateToken(authentication);

                    AuthResponse authResponse = new AuthResponse(
                            authLoginRequest.username(),
                            "Logeado correctamente",
                            token,
                            true
                    );

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);

                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
                catch (AuthenticationException e){
                    String message = e instanceof BadCredentialsException ?
                            "Usuario o contraseña incorrectos" :
                            "Error de autenticación";
                    sendErrorResponse(response, message, e.getMessage());
                }
                catch (Exception e) {
                    sendErrorResponse(response, "Error interno", e.getMessage());
                }
            } else {
                filterChain.doFilter(request, response);
            }
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (userDetails == null){
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    private void sendErrorResponse(HttpServletResponse response, String errorTitle, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                    errorTitle,
                    message,
                    false
                );
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
