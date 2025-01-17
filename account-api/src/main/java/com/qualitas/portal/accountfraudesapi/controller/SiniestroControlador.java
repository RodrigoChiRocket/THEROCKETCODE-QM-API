package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.SiniestroDTO;
import com.qualitas.portal.fraudes.account.dto.response.SinestroResponse;
import com.qualitas.portal.fraudes.account.dto.response.SiniestroDetalleRespuestaDTO;
import com.qualitas.portal.fraudes.account.service.SiniestroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/siniestro")
public class SiniestroControlador {



    @Autowired
    private SiniestroService siniestroService;

    // Endpoint para crear un siniestro
    @PostMapping
    public ResponseEntity<BigDecimal> crearSiniestro(@RequestBody SiniestroDTO siniestroDTO) {
        BigDecimal id = siniestroService.crearSiniestro(siniestroDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED); // Retorna el ID del nuevo siniestro con un código 201 (CREADO)
    }

    // Endpoint para obtener un siniestro por ID
    @GetMapping("/{id}")
    public ResponseEntity<SiniestroDTO> obtenerSiniestro(@PathVariable BigDecimal id) {
        SiniestroDTO siniestroDTO = siniestroService.obtenerSiniestro(id);
        return siniestroDTO != null ? ResponseEntity.ok(siniestroDTO) : ResponseEntity.notFound().build();
    }

    // Endpoint para actualizar un siniestro
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarSiniestro(@PathVariable BigDecimal id, @RequestBody SiniestroDTO siniestroDTO) {

        siniestroService.actualizarSiniestro(siniestroDTO, id);
        return new ResponseEntity<>("Siniestro actualizado con éxito", HttpStatus.OK);
    }

    // Endpoint para eliminar un siniestro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSiniestro(@PathVariable BigDecimal id) {
        siniestroService.eliminarSiniestro(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para listar todos los siniestros
    @GetMapping("/listar")
    public ResponseEntity<List<SiniestroDTO>> listarSiniestros() {
        List<SiniestroDTO> siniestros = siniestroService.listarSiniestros();
        return ResponseEntity.ok(siniestros);
    }

    // Endpoint para obtener detalles de un siniestro específico
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<SiniestroDetalleRespuestaDTO>> obtenerSiniestroDetalle(@PathVariable BigDecimal id) {
        List<SiniestroDetalleRespuestaDTO> detalles = (List<SiniestroDetalleRespuestaDTO>) siniestroService.obtenerSiniestroDetalle(id);
        return ResponseEntity.ok(detalles);
    }

}
