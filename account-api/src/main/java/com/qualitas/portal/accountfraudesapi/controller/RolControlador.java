package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.accountfraudesapi.response.Response;
import com.qualitas.portal.fraudes.account.dto.request.RolDto;
import com.qualitas.portal.fraudes.account.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/roles")
public class RolControlador {
    @Autowired
    private RolService rolService;

    @PostMapping("/crear")
    @PreAuthorize("hasAuthority('POST')")
    public ResponseEntity<?> crearRol(@RequestBody RolDto rolDto) {
        BigDecimal iRolId = rolService.crearRol(rolDto.getvRolNombre(), rolDto.getPermisosId());

        return Response.crearRespuesta()
                .codigoRespuesta(HttpStatus.CREATED)
                .agregarAtributo("iRolId", iRolId)
                .crear();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GET')")
    public ResponseEntity<?> roles() {
        return Response.crearRespuesta()
                .codigoRespuesta(HttpStatus.OK)
                .agregarAtributo("roles", rolService.roles())
                .crear();
    }
}
