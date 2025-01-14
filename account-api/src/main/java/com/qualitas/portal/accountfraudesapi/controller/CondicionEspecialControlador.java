package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.CondicionEspecialDTO;
import com.qualitas.portal.fraudes.account.service.CondicionEspecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/condicionesespeciales")
public class CondicionEspecialControlador {

    @Autowired
    private CondicionEspecialService condicionEspecialService;

    @GetMapping("/{id}")
    public CondicionEspecialDTO obtenerCondicionEspecial(@PathVariable BigDecimal id) {
        return condicionEspecialService.obtenerCondicionEspecial(id);
    }

}
