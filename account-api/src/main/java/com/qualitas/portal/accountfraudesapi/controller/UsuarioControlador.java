package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.accountfraudesapi.response.Response;
import com.qualitas.portal.fraudes.account.model.Usuario;
import com.qualitas.portal.fraudes.account.service.AutenticacionService;
import com.qualitas.portal.fraudes.account.service.UsuarioService;
import com.qualitas.portal.fraudes.account.util.email.EnvioCorreo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    private UsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger(AutenticacionControlador.class);

    @Autowired
    AutenticacionService autenticacionService;

    @Autowired
    EnvioCorreo enviarCorreo;


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {


        // Si no existe, registrar el usuario
        BigDecimal iUsuaID = usuarioService.crearUsuario(usuario);

        return Response.crearRespuesta()
                .codigoRespuesta(HttpStatus.CREATED)
                .agregarAtributo("iUsuaID", iUsuaID)
                .crear();
    }



    //Prueba de envio de correo
    @GetMapping("/enviarCorreoTest")
    public ResponseEntity<String> enviarCorreoTest() {

        String email= "rodrigorafaelchipacheco@gmail.com";
        String nombre="Rafael";
        String contraseña= "contar";
        logger.info("Enviando correo a: {}", email);


        // Enviar correo
        boolean result = enviarCorreo.enviarCorreo(email, nombre, contraseña);

        if (result) {
            logger.info("Correo enviado exitosamente a: {}", email);
            return ResponseEntity.ok("Correo enviado exitosamente.");
        } else {
            logger.error("Error al enviar el correo a: {}", email);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo.");
        }
    }

}
