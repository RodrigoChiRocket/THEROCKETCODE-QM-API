package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.AutoMarcaDTO;
import com.qualitas.portal.fraudes.account.application.service.AutoMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("automarca")
public class AutoMarcaControlador {


    @Autowired
    private AutoMarcaService autoMarcaService;

    @PostMapping
    public AutoMarcaDTO crearAutoMarca(@RequestBody AutoMarcaDTO autoMarcaDTO) {
        return autoMarcaService.crearAutoMarca(autoMarcaDTO);
    }

    @GetMapping("/{id}")
    public AutoMarcaDTO obtenerAutoMarca(@PathVariable BigDecimal id) {
        return autoMarcaService.obtenerAutoMarca(id);
    }

    @PutMapping("/{id}")
    public AutoMarcaDTO actualizarAutoMarca(@PathVariable BigDecimal id, @RequestBody AutoMarcaDTO autoMarcaDTO) {
        return autoMarcaService.actualizarAutoMarca(id, autoMarcaDTO);
    }

    @GetMapping
    public List<AutoMarcaDTO> listarAutoMarcas() {
        return autoMarcaService.listarAutoMarcas();
    }
    @DeleteMapping("/{id}")
    public void eliminarAutoMarca(@PathVariable BigDecimal id) {
        autoMarcaService.eliminarAutoMarca(id);
    }
}