package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.accountfraudesapi.response.Response;
import com.qualitas.portal.fraudes.account.dto.request.CredencialesDto;
import com.qualitas.portal.fraudes.account.dto.response.InicioSesionRespuestaDto;
import com.qualitas.portal.fraudes.account.model.Usuario;
import com.qualitas.portal.fraudes.account.service.AutenticacionService;
import com.qualitas.portal.fraudes.account.service.CodigoEmailService;
import com.qualitas.portal.fraudes.account.service.RestablecerContrasenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/auth")
public class AutenticacionControlador {

    @Autowired
    private RestablecerContrasenaService restablecerContrasenaService;

    @Autowired
    private CodigoEmailService codigoEmailService;

    @Autowired
    AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDto credencialesUsuario) {
        InicioSesionRespuestaDto inicioSesionRespuestaDto = autenticacionService.iniciarSesion(credencialesUsuario);
        return ResponseEntity.ok(inicioSesionRespuestaDto);
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

}
