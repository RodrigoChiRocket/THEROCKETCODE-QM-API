package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.AutoDescripcionDTO;
import com.qualitas.portal.fraudes.account.application.service.AutoDescripcionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/autodescripcion")
public class AutoDescripcionControlador {

    private static final Logger logger = LoggerFactory.getLogger(AutoDescripcionControlador.class);

    @Autowired
    private AutoDescripcionService autoDescripcionService;

    // Crear AutoDescripcion
    @PostMapping
    public ResponseEntity<AutoDescripcionDTO> crearAutoDescripcion(@RequestBody AutoDescripcionDTO autoDescripcionDTO) {
        logger.info("Creando AutoDescripcion con los datos: {}", autoDescripcionDTO);
        AutoDescripcionDTO nuevaAutoDescripcion = autoDescripcionService.crearAuto(autoDescripcionDTO);
        return new ResponseEntity<>(nuevaAutoDescripcion, HttpStatus.CREATED);
    }

    // Obtener AutoDescripcion por ID
    @GetMapping("/{id}")
    public ResponseEntity<AutoDescripcionDTO> obtenerAutoDescripcion(@PathVariable BigDecimal id) {
        logger.info("Obteniendo AutoDescripcion con ID: {}", id);
        AutoDescripcionDTO autoDescripcionDTO = autoDescripcionService.obtenerAuto(id);
        return ResponseEntity.ok(autoDescripcionDTO);
    }

    // Actualizar AutoDescripcion
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarAutoDescripcion(@PathVariable BigDecimal id, @RequestBody AutoDescripcionDTO autoDescripcionDTO) {
        logger.info("Actualizando AutoDescripcion con ID: {}", id);
        autoDescripcionService.actualizarAuto(id, autoDescripcionDTO);
        return new ResponseEntity<>("AutoDescripcion actualizado con éxito", HttpStatus.OK);
    }

    // Eliminar AutoDescripcion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutoDescripcion(@PathVariable BigDecimal id) {
        logger.info("Eliminando AutoDescripcion con ID: {}", id);
        autoDescripcionService.eliminarAuto(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todas las AutoDescripciones
    @GetMapping
    public ResponseEntity<List<AutoDescripcionDTO>> listarAutoDescripciones() {
        logger.info("Listando todas las AutoDescripciones");
        List<AutoDescripcionDTO> autoDescripciones = autoDescripcionService.listarAutos();
        return ResponseEntity.ok(autoDescripciones);
    }
}
