package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.MovimientoReservaDTO;
import com.qualitas.portal.fraudes.account.service.MovimientoReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/movimiento-reserva")
public class MovimientoReservaControlador {

    @Autowired
    private MovimientoReservaService movimientoReservaService;

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoReservaDTO> obtenerMovimientoReserva(@PathVariable BigDecimal id) {
        MovimientoReservaDTO movimientoReserva = movimientoReservaService.obtenerMovimientoReserva(id);
        if (movimientoReserva != null) {
            return ResponseEntity.ok(movimientoReserva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
