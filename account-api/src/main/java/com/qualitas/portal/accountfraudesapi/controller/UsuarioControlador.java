package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    private UsuarioService usuarioService;

}
