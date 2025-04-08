package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.AutoMarcaDTO;
import com.qualitas.portal.fraudes.account.application.service.AutoMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("automarca")
@CrossOrigin(origins = "*")
public class AutoMarcaControlador {

    @Autowired
    private AutoMarcaService autoMarcaService;

    @PostMapping
    public ResponseEntity<AutoMarcaDTO> crearAutoMarca(@RequestBody AutoMarcaDTO autoMarcaDTO) {
        return ResponseEntity.ok(autoMarcaService.crearAutoMarca(autoMarcaDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoMarcaDTO> obtenerAutoMarca(@PathVariable BigDecimal id) {
        return ResponseEntity.ok(autoMarcaService.obtenerAutoMarca(id));
    }

    @GetMapping("/buscar-por-nombre/{vNombre}")
    public ResponseEntity<AutoMarcaDTO> obtenerAutoMarcaPorNombre(@PathVariable String vNombre) {
        return ResponseEntity.ok(autoMarcaService.obtenerAutoMarcaPorNombre(vNombre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutoMarcaDTO> actualizarAutoMarca(@PathVariable BigDecimal id,
                                                            @RequestBody AutoMarcaDTO autoMarcaDTO) {
        return ResponseEntity.ok(autoMarcaService.actualizarAutoMarca(id, autoMarcaDTO));
    }

    @GetMapping
    public ResponseEntity<List<AutoMarcaDTO>> listarAutoMarcas() {
        return ResponseEntity.ok(autoMarcaService.listarAutoMarcas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAutoMarca(@PathVariable BigDecimal id) {
        autoMarcaService.eliminarAutoMarca(id);
        return ResponseEntity.noContent().build();
    }
}