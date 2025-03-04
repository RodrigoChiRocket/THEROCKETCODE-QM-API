package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.CategoriaVehiculoDTO;
import com.qualitas.portal.fraudes.account.application.service.CategoriaVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/categoriavehiculo")
public class CategoriaVehiculoControlador {

    @Autowired
    private CategoriaVehiculoService categoriaVehiculoService;

    @PostMapping
    public CategoriaVehiculoDTO crearCategoriaVehiculo(@RequestBody CategoriaVehiculoDTO categoriaVehiculoDTO) {
        return categoriaVehiculoService.crearCategoriaVehiculo(categoriaVehiculoDTO);
    }

    @GetMapping("/{id}")
    public CategoriaVehiculoDTO obtenerCategoriaVehiculo(@PathVariable BigDecimal id) {
        return categoriaVehiculoService.obtenerCategoriaVehiculo(id);
    }

    @PutMapping("/{id}")
    public CategoriaVehiculoDTO actualizarCategoriaVehiculo(@PathVariable BigDecimal id, @RequestBody CategoriaVehiculoDTO categoriaVehiculoDTO) {
        return categoriaVehiculoService.actualizarCategoriaVehiculo(id, categoriaVehiculoDTO);
    }

    @GetMapping
    public List<CategoriaVehiculoDTO> listarCategoriasVehiculo() {
        return categoriaVehiculoService.listarCategoriasVehiculo();
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoriaVehiculo(@PathVariable BigDecimal id) {
        categoriaVehiculoService.eliminarCategoriaVehiculo(id);
    }
}
