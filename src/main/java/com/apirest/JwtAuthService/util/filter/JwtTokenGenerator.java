package com.apirest.JwtAuthService.util.filter;

import com.apirest.JwtAuthService.controller.dtos.auth.AuthLoginRequest;
import com.apirest.JwtAuthService.controller.dtos.auth.AuthResponse;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.services.exception.UserNotFoundException;
import com.apirest.JwtAuthService.services.impl.UserDetailServiceImpl;
import com.apirest.JwtAuthService.util.JwtUtils;
import com.apirest.JwtAuthService.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
                } catch (BadCredentialsException e) {
                    sendErrorResponse(response, ErrorCodeEnum.BAD_CREDENTIALS, e.getMessage());
                } catch (UserNotFoundException e) {
                    sendErrorResponse(response, ErrorCodeEnum.USER_NOT_FOUND, e.getMessage());
                } catch (Exception e) {
                    sendErrorResponse(response, ErrorCodeEnum.INTERNAL_SERVER_ERROR, e.getMessage());
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

    private void sendErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCode, String detail)
            throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.name(),
                detail,
                errorCode.getCode()
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(determineHttpStatus(errorCode).value());
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }

    private HttpStatus determineHttpStatus(ErrorCodeEnum errorCode) {
        return switch (errorCode) {
            case ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case INVALID_TOKEN, EXPIRED_TOKEN, BAD_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
            case USER_NOT_FOUND, INVALID_ROLES -> HttpStatus.NOT_FOUND;
            case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case INVALID_REQUEST, MISSING_REQUIRED_FIELDS -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

}