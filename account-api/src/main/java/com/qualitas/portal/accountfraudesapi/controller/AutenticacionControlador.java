package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.accountfraudesapi.response.Response;
import com.qualitas.portal.fraudes.account.dto.request.CredencialesDto;
import com.qualitas.portal.fraudes.account.dto.response.InicioSesionRespuestaDto;
import com.qualitas.portal.fraudes.account.model.Usuario;
import com.qualitas.portal.fraudes.account.service.AutenticacionService;
import com.qualitas.portal.fraudes.account.util.email.EnvioCorreo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/auth")
public class AutenticacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(AutenticacionControlador.class);

    @Autowired
    AutenticacionService autenticacionService;

    @Autowired
    EnvioCorreo enviarCorreo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDto credencialesUsuario) {
        InicioSesionRespuestaDto inicioSesionRespuestaDto = autenticacionService.iniciarSesion(credencialesUsuario);
        return ResponseEntity.ok(inicioSesionRespuestaDto);
    }


}
