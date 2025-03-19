package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.CotizacionDTO;
import com.qualitas.portal.fraudes.account.application.dto.request.CotizacionCompletaDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.CotizacionCompletaResponseDTO;
import com.qualitas.portal.fraudes.account.application.service.CotizacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cotizacion")
public class CotizacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(CotizacionControlador.class);

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping("/completa")
    public ResponseEntity<CotizacionCompletaResponseDTO> crearCotizacionCompleta(@RequestBody CotizacionCompletaDTO cotizacionCompletaDTO) {
        logger.info("Creando cotización completa con los datos: {}", cotizacionCompletaDTO);
        CotizacionCompletaResponseDTO responseDTO = cotizacionService.crearCotizacionCompleta(cotizacionCompletaDTO);

        // Crear un ScheduledExecutorService con un solo hilo
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger contador = new AtomicInteger();
        // Ejecutar la petición HTTP en otro hilo después de un pequeño retraso
        executorService.schedule(() -> {
            try {
                contador.getAndIncrement();
                System.out.println("Enviando petición HTTP a Multicotizador API: " + contador.get());
                // Create RestTemplate
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8000/multicotizador-api/cotizacion/completa";

                // Create request headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Create request entity with headers and body
                HttpEntity<CotizacionCompletaResponseDTO> request = new HttpEntity<>(responseDTO, headers);

                // Send POST request
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

                // Handle response
                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Response: " + response.getBody());
                } else {
                    logger.error("Request failed with status code: " + response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Error al enviar petición HTTP", e);
            } finally {
                executorService.shutdown();
            }
        }, 1, TimeUnit.SECONDS);

        // Retornar inmediatamente sin esperar la respuesta HTTP
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