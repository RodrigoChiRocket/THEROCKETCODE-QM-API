package com.qualitas.portal.accountfraudesapi.controller;


import com.qualitas.portal.fraudes.account.dto.ConductorDTO;
import com.qualitas.portal.fraudes.account.service.ConductorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/conductor")
public class ConductorController {

    @Autowired
    private ConductorService conductorService;



    // Obtener un conductor por ID (retorna DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ConductorDTO> obtenerConductor(@PathVariable BigDecimal id) {
        ConductorDTO conductor = conductorService.obtenerConductor(id);
        return conductor != null ? ResponseEntity.ok(conductor) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo conductor (entrada y salida como DTO)
    @PostMapping
    public ResponseEntity<Void> crearConductor(@RequestBody ConductorDTO conductorDTO) {
        conductorService.crearConductor(conductorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Actualizar un conductor (entrada como DTO)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarConductor(@PathVariable BigDecimal id, @RequestBody ConductorDTO conductorDTO) {
        // Establecer el ID en el DTO antes de pasarlo al servicio
        conductorDTO.setiConducId(id);
        conductorService.actualizarConductor(conductorDTO);
        return ResponseEntity.ok("Conductor actualizado con éxito");
    }

    // Eliminar un conductor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConductor(@PathVariable BigDecimal id) {
        conductorService.eliminarConductor(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los conductores (retorna lista de DTOs)
    @GetMapping("/listar")
    public ResponseEntity<List<ConductorDTO>> listarConductores() {
        List<ConductorDTO> conductores = conductorService.listarConductores();
        return ResponseEntity.ok(conductores);
    }
}
