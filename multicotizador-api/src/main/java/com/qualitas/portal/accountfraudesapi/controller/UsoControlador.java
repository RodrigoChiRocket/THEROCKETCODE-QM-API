package com.qualitas.portal.accountfraudesapi.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.qualitas.portal.fraudes.account.application.dto.UsoDTO;
import com.qualitas.portal.fraudes.account.application.service.UsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/uso")
public class UsoControlador {

    @Autowired
    private UsoService usoService;

    @PostMapping
    public UsoDTO crearUso(@RequestBody UsoDTO usoDTO) {
        return usoService.crearUso(usoDTO);
    }

    @GetMapping("/{id}")
    public UsoDTO obtenerUso(@PathVariable BigDecimal id) {
        return usoService.obtenerUso(id);
    }

    @PutMapping("/{id}")
    public UsoDTO actualizarUso(@PathVariable BigDecimal id, @RequestBody UsoDTO usoDTO) {
        return usoService.actualizarUso(id, usoDTO);
    }

    @GetMapping
    public List<UsoDTO> listarUsos() {
        return usoService.listarUsos();
    }
}
