package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.AutoDescripcionDTO;
import com.qualitas.portal.fraudes.account.application.service.AutoDescripcionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.qualitas.portal.fraudes.account.application.dto.RutinaCargaDTO;
import com.qualitas.portal.fraudes.account.application.service.RutinaCargaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/rutina-carga")
public class RutinaCargaController {

    private static final Logger logger = LoggerFactory.getLogger(RutinaCargaController.class);

    @Autowired
    private RutinaCargaService rutinaCargaService;

    // Crear RutinaCarga
    @PostMapping
    public ResponseEntity<RutinaCargaDTO> crearRutina(@RequestBody RutinaCargaDTO rutinaCargaDTO) {
        logger.info("Creando RutinaCarga con los datos: {}", rutinaCargaDTO);
        RutinaCargaDTO nuevaRutina = rutinaCargaService.crearRutina(rutinaCargaDTO);
        return new ResponseEntity<>(nuevaRutina, HttpStatus.CREATED);
    }

    // Obtener RutinaCarga por ID
    @GetMapping("/{id}")
    public ResponseEntity<RutinaCargaDTO> obtenerRutina(@PathVariable BigDecimal id) {
        logger.info("Obteniendo RutinaCarga con ID: {}", id);
        RutinaCargaDTO rutinaCargaDTO = rutinaCargaService.obtenerRutina(id);
        return ResponseEntity.ok(rutinaCargaDTO);
    }

    // Actualizar RutinaCarga
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRutina(@PathVariable BigDecimal id, @RequestBody RutinaCargaDTO rutinaCargaDTO) {
        logger.info("Actualizando RutinaCarga con ID: {}", id);
        rutinaCargaService.actualizarRutina(id, rutinaCargaDTO);
        return new ResponseEntity<>("RutinaCarga actualizada con éxito", HttpStatus.OK);
    }

    // Eliminar RutinaCarga
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable BigDecimal id) {
        logger.info("Eliminando RutinaCarga con ID: {}", id);
        rutinaCargaService.eliminarRutina(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todas las RutinaCargas
    @GetMapping
    public ResponseEntity<List<RutinaCargaDTO>> listarRutinas() {
        logger.info("Listando todas las RutinaCargas");
        List<RutinaCargaDTO> rutinas = rutinaCargaService.listarRutinas();
        return ResponseEntity.ok(rutinas);
    }
}
