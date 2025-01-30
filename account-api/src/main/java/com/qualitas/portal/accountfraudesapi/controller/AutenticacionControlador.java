package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.accountfraudesapi.configuracion.security.CustomUserDetailsService;
import com.qualitas.portal.accountfraudesapi.response.Response;
import com.qualitas.portal.accountfraudesapi.util.jwt.JwtTokenUtil;

import com.qualitas.portal.fraudes.account.application.dto.request.CredencialesDto;
import com.qualitas.portal.fraudes.account.application.service.AutenticacionService;
import com.qualitas.portal.fraudes.account.application.service.CodigoEmailService;
import com.qualitas.portal.fraudes.account.application.service.RestablecerContrasenaService;
import com.qualitas.portal.fraudes.account.domain.model.Usuario;
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
@RequestMapping("/auth")
public class AutenticacionControlador {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RestablecerContrasenaService restablecerContrasenaService;

    @Autowired
    private CodigoEmailService codigoEmailService;

    @Autowired
    AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDto credencialesUsuario) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credencialesUsuario.getvUsuario(),
                            credencialesUsuario.getvContrasena())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(credencialesUsuario.getvUsuario());

            String token = jwtTokenUtil.generateToken(userDetails.getUsername(),
                    userDetails.getAuthorities());

            // Devolver el token
                return Response.crearRespuesta()
                        .codigoRespuesta(HttpStatus.OK)
                        .agregarAtributo("token", token)
                        .crear();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        BigDecimal iUsuaID = autenticacionService.registrarUsuario(usuario);

        return Response.crearRespuesta()
                .codigoRespuesta(HttpStatus.CREATED)
                .agregarAtributo("iUsuaID", iUsuaID)
                .crear();
    }






    // Endpoint para solicitar el restablecimiento de contraseña (genera y envía el código)
    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarRestablecimiento(@RequestParam String email) {
        codigoEmailService.generarCodigoParaRestablecimiento(email);
        return ResponseEntity.ok("Código de restablecimiento enviado al correo.");
    }


    @PostMapping("/restablecer")
    public ResponseEntity<String> restablecerContrasena(@RequestParam String email,
                                                        @RequestParam String nuevaContrasena,
                                                        @RequestParam String codigo) {

        boolean esValido = codigoEmailService.verificarCodigoRestablecimiento(email, codigo);
        try {
            // Llama al servicio para restablecer la contraseña
            restablecerContrasenaService.restablecerContrasena(email, nuevaContrasena, codigo);
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping("/check")
    public String check() {
        return "OK";
    }

}
