package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.CotizacionDTO;
import com.qualitas.portal.fraudes.account.application.dto.request.CotizacionCompletaDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.CotizacionCompletaResponseDTO;
import com.qualitas.portal.fraudes.account.application.service.CotizacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cotizacion")
public class CotizacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(CotizacionControlador.class);

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping("/completa")
    public ResponseEntity<CotizacionCompletaResponseDTO> crearCotizacionCompleta(@RequestBody CotizacionCompletaDTO cotizacionCompletaDTO) {
        logger.info("Creando cotización completa con los datos: {}", cotizacionCompletaDTO);
        CotizacionCompletaResponseDTO responseDTO = cotizacionService.crearCotizacionCompleta(cotizacionCompletaDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Crear Cotización
    @PostMapping
    public ResponseEntity<CotizacionDTO> crearCotizacion(@RequestBody CotizacionDTO requestDTO) {
        logger.info("Creando cotización con los datos: {}", requestDTO);
        // Crear la cotización a través del servicio
        CotizacionDTO cotizacionDTO = cotizacionService.crearCotizacion(requestDTO);
        return new ResponseEntity<>(cotizacionDTO, HttpStatus.CREATED);
    }

    // Obtener Cotización por ID
    @GetMapping("/{id}")
    public ResponseEntity<CotizacionDTO> obtenerCotizacion(@PathVariable BigDecimal id) {
        logger.info("Obteniendo cotización con ID: {}", id);
        // Obtener la cotización a través del servicio
        CotizacionDTO cotizacionDTO = cotizacionService.obtenerCotizacionPorId(id);
        return ResponseEntity.ok(cotizacionDTO);
    }

    // Actualizar Cotización
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCotizacion(@PathVariable BigDecimal id, @RequestBody CotizacionDTO requestDTO) {
        logger.info("Actualizando cotización con ID: {}", id);
        // Actualizar la cotización a través del servicio
        requestDTO.setiCotizacionId(id); // Asegurar que el ID sea el correcto
        cotizacionService.actualizarCotizacion(requestDTO);
        return new ResponseEntity<>("Cotización actualizada con éxito", HttpStatus.OK);
    }

    // Eliminar Cotización
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCotizacion(@PathVariable BigDecimal id) {
        logger.info("Eliminando cotización con ID: {}", id);
        // Eliminar la cotización a través del servicio
        cotizacionService.eliminarCotizacion(id);
        return ResponseEntity.noContent().build(); // Retorna 204 (No Content) al eliminar exitosamente
    }

    // Listar todas las Cotizaciones
    @GetMapping
    public ResponseEntity<List<CotizacionDTO>> listarCotizaciones() {
        logger.info("Listando todas las cotizaciones");
        // Obtener todas las cotizaciones a través del servicio
        List<CotizacionDTO> cotizaciones = cotizacionService.listarTodasLasCotizaciones();
        return ResponseEntity.ok(cotizaciones);
    }
}