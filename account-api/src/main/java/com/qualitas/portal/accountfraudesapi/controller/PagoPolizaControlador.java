package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.PagoPolizaDTO;
import com.qualitas.portal.fraudes.account.service.PagoPolizaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pagoPoliza")
public class PagoPolizaControlador {
    @Autowired
    private PagoPolizaService pagoPolizaService;

    // Obtener un pago de póliza por ID (retorna DTO)
    @GetMapping("/{id}")
    public ResponseEntity<PagoPolizaDTO> obtenerPagoPoliza(@PathVariable BigDecimal id) {
        PagoPolizaDTO pagoPoliza = pagoPolizaService.obtenerPagoPolizaPorId(id);
        return pagoPoliza != null ? ResponseEntity.ok(pagoPoliza) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo pago de póliza (entrada y salida como DTO)
    @PostMapping
    public ResponseEntity<BigDecimal> crearPagoPoliza(@RequestBody PagoPolizaDTO pagoPolizaDTO) {
        PagoPolizaDTO nuevoPago = pagoPolizaService.crearPagoPoliza(pagoPolizaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago.getiPagoPolizaId());
    }

    @GetMapping("/siniestro/{id}")
    public ResponseEntity<List<PagoPolizaDTO>> obtenerPagosPolizaPorSinistro(@PathVariable BigDecimal id) {
        List<PagoPolizaDTO> pagos = pagoPolizaService.obtenerPagosPolizaPorSinistro(id);
        return ResponseEntity.ok(pagos);
    }


    // Actualizar un pago de póliza (entrada como DTO)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPagoPoliza(@PathVariable BigDecimal id, @RequestBody PagoPolizaDTO pagoPolizaDTO) {
        pagoPolizaDTO.setiPagoPolizaId(id);
        pagoPolizaService.actualizarPagoPoliza(pagoPolizaDTO);
        return ResponseEntity.ok("Pago de póliza actualizado con éxito");
    }

    // Eliminar un pago de póliza por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPagoPoliza(@PathVariable BigDecimal id) {
        pagoPolizaService.eliminarPagoPoliza(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los pagos de póliza (retorna lista de DTOs)
    @GetMapping("/listar")
    public ResponseEntity<List<PagoPolizaDTO>> listarPagosPoliza() {
        List<PagoPolizaDTO> pagos = pagoPolizaService.listarPagosPoliza();
        return ResponseEntity.ok(pagos);
    }

    // Verificar si existe un pago de póliza por endoso
    @GetMapping("/existe/endoso/{endoso}")
    public ResponseEntity<Boolean> existePagoPolizaPorEndoso(@PathVariable String endoso) {
        boolean existe = pagoPolizaService.existePagoPolizaPorEndoso(endoso);
        return ResponseEntity.ok(existe);
    }

    // Verificar si existe un pago de póliza por ID
    @GetMapping("/existe/id/{id}")
    public ResponseEntity<Boolean> existePagoPolizaPorId(@PathVariable BigDecimal id) {
        boolean existe = pagoPolizaService.existePagoPolizaPorId(id);
        return ResponseEntity.ok(existe);
    }

}
