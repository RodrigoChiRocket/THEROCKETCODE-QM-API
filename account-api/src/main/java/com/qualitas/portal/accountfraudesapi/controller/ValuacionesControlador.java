package com.qualitas.portal.accountfraudesapi.controller;


import com.qualitas.portal.fraudes.account.dto.ValuacionesDTO;
import com.qualitas.portal.fraudes.account.service.ValuacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/valuaciones")
@Validated
public class ValuacionesControlador {

    @Autowired
    private ValuacionesService valuacionesService;

    // Obtener una valuación por ID (retorna DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ValuacionesDTO> obtenerValuaciones(@PathVariable BigDecimal id) {
        ValuacionesDTO valuaciones = valuacionesService.obtenerValuaciones(id);
        return valuaciones != null ? ResponseEntity.ok(valuaciones) : ResponseEntity.notFound().build();
    }

    // Crear una nueva valuación (entrada y salida como DTO)
    @PostMapping
    public ResponseEntity<BigDecimal> crearValuaciones(@RequestBody ValuacionesDTO valuacionesDTO) {
        BigDecimal valuacionesId = valuacionesService.crearValuaciones(valuacionesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(valuacionesId);
    }

    // Actualizar una valuación (entrada como DTO)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarValuaciones(@PathVariable BigDecimal id, @RequestBody ValuacionesDTO valuacionesDTO) {
        valuacionesDTO.setiValuacID(id);
        valuacionesService.actualizarValuaciones(valuacionesDTO);
        return ResponseEntity.ok("Valuación actualizada con éxito");
    }

    // Eliminar una valuación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarValuaciones(@PathVariable BigDecimal id) {
        valuacionesService.eliminarValuaciones(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todas las valuaciones (retorna lista de DTOs)
    @GetMapping("/listar")
    public ResponseEntity<List<ValuacionesDTO>> listarValuaciones() {
        List<ValuacionesDTO> valuaciones = valuacionesService.listarValuaciones();
        return ResponseEntity.ok(valuaciones);
    }
}
