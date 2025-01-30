package com.qualitas.portal.accountfraudesapi.controller;


import com.qualitas.portal.fraudes.account.application.dto.AutoModeloDTO;
import com.qualitas.portal.fraudes.account.application.service.AutoModeloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/automodelo")
public class AutoModeloControlador {

    private static final Logger logger = LoggerFactory.getLogger(AutoModeloControlador.class);

    @Autowired
    private AutoModeloService autoModeloService;

    // Crear AutoModelo
    @PostMapping
    public ResponseEntity<AutoModeloDTO> crearAutoModelo(@RequestBody AutoModeloDTO autoModeloDTO) {
        logger.info("Creando AutoModelo con los datos: {}", autoModeloDTO);
        AutoModeloDTO nuevoAutoModelo = autoModeloService.crearAuto(autoModeloDTO);
        return new ResponseEntity<>(nuevoAutoModelo, HttpStatus.CREATED);
    }

    // Obtener AutoModelo por ID
    @GetMapping("/{id}")
    public ResponseEntity<AutoModeloDTO> obtenerAutoModelo(@PathVariable BigDecimal id) {
        logger.info("Obteniendo AutoModelo con ID: {}", id);
        AutoModeloDTO autoModeloDTO = autoModeloService.obtenerAuto(id);
        return ResponseEntity.ok(autoModeloDTO);
    }

    // Actualizar AutoModelo
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarAutoModelo(@PathVariable BigDecimal id, @RequestBody AutoModeloDTO autoModeloDTO) {
        logger.info("Actualizando AutoModelo con ID: {}", id);
        autoModeloService.actualizarAuto(id, autoModeloDTO);
        return new ResponseEntity<>("AutoModelo actualizado con éxito", HttpStatus.OK);
    }

    // Eliminar AutoModelo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutoModelo(@PathVariable BigDecimal id) {
        logger.info("Eliminando AutoModelo con ID: {}", id);
        autoModeloService.eliminarAuto(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los AutoModelos
    @GetMapping
    public ResponseEntity<List<AutoModeloDTO>> listarAutoModelos() {
        logger.info("Listando todos los AutoModelos");
        List<AutoModeloDTO> autoModelos = autoModeloService.listarAutos();
        return ResponseEntity.ok(autoModelos);
    }
}
