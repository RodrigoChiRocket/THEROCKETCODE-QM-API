package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.CoberturaDTO;
import com.qualitas.portal.fraudes.account.service.CoberturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/cobertura")
public class CoberturaControlador {
    @Autowired
    private CoberturaService coberturaService;

    // Crear una nueva cobertura
    @PostMapping
    public ResponseEntity<BigDecimal> crearCobertura(@RequestBody CoberturaDTO coberturaDTO) {
        BigDecimal id = coberturaService.crearCobertura(coberturaDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED); // Retorna el ID de la nueva cobertura
    }

    // Obtener una cobertura por ID
    @GetMapping("/{id}")
    public ResponseEntity<CoberturaDTO> obtenerCobertura(@PathVariable BigDecimal id) {
        CoberturaDTO coberturaDTO = coberturaService.obtenerCobertura(id);
        return new ResponseEntity<>(coberturaDTO, HttpStatus.OK);
    }

    // Actualizar una cobertura existente
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarCobertura(@PathVariable BigDecimal id, @RequestBody CoberturaDTO coberturaDTO) {
        coberturaDTO.setiCobertID(id); // Aseguramos que el ID del DTO coincida con el de la URL
        coberturaService.actualizarCobertura(coberturaDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No retorna contenido tras la actualización exitosa
    }

    // Eliminar una cobertura por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCobertura(@PathVariable BigDecimal id) {
        coberturaService.eliminarCobertura(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No retorna contenido tras la eliminación exitosa
    }

    // Listar todas las coberturas
    @GetMapping("/listar")
    public ResponseEntity<List<CoberturaDTO>> listarCoberturas() {
        List<CoberturaDTO> coberturas = coberturaService.listarCobertura();
        return new ResponseEntity<>(coberturas, HttpStatus.OK);
    }
}