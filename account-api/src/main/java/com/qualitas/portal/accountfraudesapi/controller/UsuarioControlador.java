package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.util.email.EnvioCorreo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private EnvioCorreo envioCorreo;

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
