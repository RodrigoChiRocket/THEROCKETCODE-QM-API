package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.ReglaAlertadaDTO;
import com.qualitas.portal.fraudes.account.service.ReglasAlertadasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/reglas-alertadas")
public class ReglasAlertadasControlador {

    private final ReglasAlertadasService reglasAlertadasService;

    @Autowired
    public ReglasAlertadasControlador(ReglasAlertadasService reglasAlertadasService) {
        this.reglasAlertadasService = reglasAlertadasService;
    }

    // Endpoint para crear una regla alertada
    @PostMapping
    public ResponseEntity<BigDecimal> crearReglaAlertada(@RequestBody ReglaAlertadaDTO reglaAlertadaDTO) {
        BigDecimal id = reglasAlertadasService.crearReglaAlertada(reglaAlertadaDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED); // Retorna el ID de la nueva regla alertada con un código 201 (CREADO)
    }

    // Endpoint para obtener una regla alertada por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ReglaAlertadaDTO> obtenerReglaAlertada(@PathVariable BigDecimal id) {
        ReglaAlertadaDTO reglaAlertadaDTO = reglasAlertadasService.obtenerReglaAlertada(id);
        return reglaAlertadaDTO != null ? ResponseEntity.ok(reglaAlertadaDTO) : ResponseEntity.notFound().build();
    }

    // Endpoint para actualizar una regla alertada
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarReglaAlertada(@PathVariable BigDecimal id, @RequestBody ReglaAlertadaDTO reglaAlertadaDTO) {
        reglaAlertadaDTO.setiRAlertaID(id);
        reglasAlertadasService.actualizarReglaAlertada(reglaAlertadaDTO);
        return new ResponseEntity<>("Regla alertada actualizada con éxito", HttpStatus.OK);
    }

    // Endpoint para eliminar una regla alertada
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReglaAlertada(@PathVariable BigDecimal id) {
        reglasAlertadasService.eliminarReglaAlertada(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para listar todas las reglas alertadas
    @GetMapping("/listar")
    public ResponseEntity<List<ReglaAlertadaDTO>> listarReglasAlertadas() {
        List<ReglaAlertadaDTO> reglaAlertadaDTOS = reglasAlertadasService.listarReglasAlertadas();
        return ResponseEntity.ok(reglaAlertadaDTOS);
    }
}
