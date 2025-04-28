package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.AutoDTO;
import com.qualitas.portal.fraudes.account.application.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auto")
public class AutoControlador {

    private static final Logger logger = LoggerFactory.getLogger(AutoControlador.class);

    @Autowired
    private AutoService autoService;

    // Crear Auto
    @PostMapping
    public ResponseEntity<AutoDTO> crearAuto(@RequestBody AutoDTO requestDTO) {
        logger.info("Creando auto con los datos: {}", requestDTO);
        AutoDTO autoDTO = autoService.crearAuto(requestDTO);
        return new ResponseEntity<>(autoDTO, HttpStatus.CREATED);
    }

    // Obtener Auto por ID
    @GetMapping("/{id}")
    public ResponseEntity<AutoDTO> obtenerAuto(@PathVariable BigDecimal id) {
        logger.info("Obteniendo auto con ID: {}", id);
        AutoDTO responseDTO = autoService.obtenerAuto(id);
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar Auto
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAuto(@PathVariable BigDecimal id, @RequestBody AutoDTO requestDTO) {
        try {
            logger.info("Actualizando auto con ID: {}", id);

            // Validación básica
            if (requestDTO == null) {
                return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede estar vacío");
            }

            AutoDTO autoActualizado = autoService.actualizarAuto(id, requestDTO);
            return ResponseEntity.ok(autoActualizado);

        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error al actualizar auto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar Auto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAuto(@PathVariable BigDecimal id) {
        logger.info("Eliminando auto con ID: {}", id);
        autoService.eliminarAuto(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los Autos
    @GetMapping
    public ResponseEntity<List<AutoDTO>> listarAutos() {
        logger.info("Listando todos los autos");
        List<AutoDTO> responseDTOs = autoService.listarAutos();
        return ResponseEntity.ok(responseDTOs);
    }
}
