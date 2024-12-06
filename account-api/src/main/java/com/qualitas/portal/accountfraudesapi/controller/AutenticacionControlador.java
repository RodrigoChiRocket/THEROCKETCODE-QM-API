package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.request.CredencialesDto;
import com.qualitas.portal.fraudes.account.dto.response.InicioSesionRespuestaDto;
import com.qualitas.portal.fraudes.account.service.AutenticacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacionControlador {
    @Autowired
    AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDto credencialesUsuario) {
        InicioSesionRespuestaDto inicioSesionRespuestaDto = autenticacionService.iniciarSesion(credencialesUsuario);
        return ResponseEntity.ok(inicioSesionRespuestaDto);
    }
}
