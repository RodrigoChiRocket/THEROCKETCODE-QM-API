package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.accountfraudesapi.configuracion.security.CustomUserDetailsService;
import com.qualitas.portal.accountfraudesapi.response.Response;
import com.qualitas.portal.accountfraudesapi.util.jwt.JwtTokenUtil;
import com.qualitas.portal.fraudes.account.application.dto.request.ActualizarContrasenaRequest;
import com.qualitas.portal.fraudes.account.application.dto.request.LoginRequestDTO;
import com.qualitas.portal.fraudes.account.application.dto.request.PasswordResetRequestDTO;
import com.qualitas.portal.fraudes.account.application.dto.request.RegisterRequestDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.AuthResponseDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.PasswordResetTokenDTO;
import com.qualitas.portal.fraudes.account.application.service.AutenticacionService;
import com.qualitas.portal.fraudes.account.application.service.CodigoEmailService;
import com.qualitas.portal.fraudes.account.application.service.PasswordResetService;
import com.qualitas.portal.fraudes.account.application.service.RestablecerContrasenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AutenticacionControlador {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private CodigoEmailService codigoEmailService;

    @Autowired
    private AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Obtener información del usuario desde el servicio
            AuthResponseDTO authResponse = autenticacionService.autenticarUsuario(loginRequest);

            // Generar token JWT en el controlador con los datos adicionales
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtTokenUtil.generateToken(
                    userDetails.getUsername(),
                    authResponse.getRole(),
                    authResponse.getUsername()
            );

            authResponse.setToken(token);

            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.OK)
                    .agregarAtributo("token", authResponse.getToken())
                    .agregarAtributo("userId", authResponse.getUserId())
                    .agregarAtributo("username", authResponse.getUsername())
                    .agregarAtributo("role", authResponse.getRole())
                    .crear();

        } catch (BadCredentialsException e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.UNAUTHORIZED)
                    .agregarAtributo("mensaje", "Credenciales inválidas")
                    .crear();
        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.INTERNAL_SERVER_ERROR)
                    .agregarAtributo("mensaje", "Error durante la autenticación")
                    .crear();
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            // Registrar el nuevo usuario
            BigDecimal userId = autenticacionService.registrarUsuario(registerRequest);

            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.CREATED)
                    .agregarAtributo("userId", userId)
                    .agregarAtributo("mensaje", "Usuario registrado exitosamente")
                    .crear();

        } catch (RuntimeException e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.BAD_REQUEST)
                    .agregarAtributo("mensaje", e.getMessage())
                    .crear();
        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.INTERNAL_SERVER_ERROR)
                    .agregarAtributo("mensaje", "Error al registrar el usuario")
                    .crear();
        }
    }

    @PatchMapping("/actualizar-contrasena")
    public ResponseEntity<?> actualizarContrasena(@RequestBody ActualizarContrasenaRequest request) {
        try {
            autenticacionService.actualizarContrasenaPorEmail(request.getEmail(), request.getNuevaContrasena());

            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.OK)
                    .agregarAtributo("mensaje", "Contraseña actualizada exitosamente")
                    .crear();

        } catch (RuntimeException e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.BAD_REQUEST)
                    .agregarAtributo("mensaje", e.getMessage())
                    .crear();
        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.INTERNAL_SERVER_ERROR)
                    .agregarAtributo("mensaje", "Error al actualizar la contraseña")
                    .crear();
        }
    }


    @PostMapping("/solicitar-restablecimiento")
    public ResponseEntity<?> solicitarRestablecimiento(@RequestParam String email) {
        try {
            codigoEmailService.generarCodigoParaRestablecimiento(email);
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.OK)
                    .agregarAtributo("mensaje", "Código de restablecimiento enviado al correo")
                    .crear();
        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.BAD_REQUEST)
                    .agregarAtributo("mensaje", e.getMessage())
                    .crear();
        }
    }


    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        try {
            PasswordResetTokenDTO tokenDTO = passwordResetService.generarPasswordResetToken(email);

            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.OK)
                    .agregarAtributo("mensaje", "Se ha enviado un enlace de restablecimiento a tu correo")
                    .agregarAtributo("expira", tokenDTO.getExpiration())
                    .crear();

        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.BAD_REQUEST)
                    .agregarAtributo("mensaje", e.getMessage())
                    .crear();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDTO request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getEmail(), request.getNewPassword());

            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.OK)
                    .agregarAtributo("mensaje", "Contraseña actualizada exitosamente")
                    .crear();

        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.BAD_REQUEST)
                    .agregarAtributo("mensaje", e.getMessage())
                    .crear();
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token, @RequestParam String email) {
        try {
            boolean isValid = passwordResetService.validarToken(token, email);

            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.OK)
                    .agregarAtributo("valido", isValid)
                    .crear();

        } catch (Exception e) {
            return Response.crearRespuesta()
                    .codigoRespuesta(HttpStatus.BAD_REQUEST)
                    .agregarAtributo("mensaje", e.getMessage())
                    .crear();
        }
    }

    }


