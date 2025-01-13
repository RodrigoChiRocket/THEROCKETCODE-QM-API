package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.EntidadDTO;
import com.qualitas.portal.fraudes.account.service.EntidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/entidad")
public class EntidadController {

    @Autowired
    private EntidadService entidadService;

    // Crear una nueva entidad
    @PostMapping
    public ResponseEntity<BigDecimal> crearEntidad(@RequestBody EntidadDTO entidadDTO) {
        BigDecimal id = entidadService.crearEntidad(entidadDTO);
        return ResponseEntity.ok(id); // Devuelve el ID generado
    }

    // Actualizar una entidad existente
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarEntidad(@PathVariable BigDecimal id, @RequestBody EntidadDTO entidadDTO) {
        entidadDTO.setiEntidaId(id); // Asegura que se actualiza la entidad correcta
        entidadService.actualizarEntidad(entidadDTO);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content al actualizar
    }

    // Obtener una entidad por su ID
    @GetMapping("/{id}")
    public ResponseEntity<EntidadDTO> obtenerEntidad(@PathVariable BigDecimal id) {
        EntidadDTO entidadDTO = entidadService.obtenerEntidad(id);
        return ResponseEntity.ok(entidadDTO);
    }

    // Listar todas las entidades
    @GetMapping("listar")
    public ResponseEntity<List<EntidadDTO>> listarEntidades() {
        List<EntidadDTO> entidades = entidadService.listarEntidades();
        return ResponseEntity.ok(entidades);
    }

    // Eliminar una entidad por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntidad(@PathVariable BigDecimal id) {
        entidadService.eliminarEntidad(id);
        return ResponseEntity.noContent().build(); // Retorna un 204 No Content al eliminar
    }
}