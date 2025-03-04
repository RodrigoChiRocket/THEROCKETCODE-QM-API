package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.response.UsuarioRespuesta;
import com.qualitas.portal.fraudes.account.application.service.UsuarioService;
import com.qualitas.portal.fraudes.account.util.email.EnvioCorreo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.qualitas.portal.accountfraudesapi.response.Response;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnvioCorreo envioCorreo;
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GET')")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable BigDecimal id) {
        UsuarioRespuesta usuario = usuarioService.obtenerUsuarioPorId(id);
        return Response.crearRespuesta()
                .codigoRespuesta(HttpStatus.OK)
                .agregarAtributo("usuario", usuario)
                .crear();
    }

    @GetMapping("/enviarCorreo")
    public ResponseEntity<String> enviarCorreo() {
        try {
            // Asegúrate de usar una dirección de correo válida y un mensaje adecuado
            envioCorreo.enviarCorreo("destinatario@dominio.com", "Asunto del correo", "Cuerpo del correo");

            return ResponseEntity.status(HttpStatus.CREATED).body("Correo enviado correctamente.");
        } catch (Exception e) {
            // Si hay un error al enviar el correo, respondemos con un error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo: " + e.getMessage());
        }
    }
}
