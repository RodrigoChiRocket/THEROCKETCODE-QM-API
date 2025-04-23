package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.RutinaCargaDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.CotizacionCompletaResponseDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.EstadisticasTiempoEjecucionDTO;
import com.qualitas.portal.fraudes.account.application.service.CotizacionService;
import com.qualitas.portal.fraudes.account.application.service.ResultadoCotizacionService;
import com.qualitas.portal.fraudes.account.application.service.RutinaCargaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "*")

@RequestMapping("/rutina-carga")
public class RutinaCargaController {

    private static final Logger logger = LoggerFactory.getLogger(RutinaCargaController.class);

    private final Map<String, LocalDateTime> ultimasEjecuciones = new ConcurrentHashMap<>();

    @Autowired
    private RutinaCargaService rutinaCargaService;

    @Autowired
    private CotizacionService cotizacionService;


@Autowired
private ResultadoCotizacionService resultadoCotizacionService;

    // Método programado que se ejecuta cada minuto para verificar las rutinas




    // Endpoints REST para gestión manual de rutinas
    @PostMapping
    public ResponseEntity<RutinaCargaDTO> crearRutina(@RequestBody RutinaCargaDTO rutinaCargaDTO) {
        logger.info("Creando nueva rutina para portal: {}", rutinaCargaDTO.getvPortal());
        RutinaCargaDTO nuevaRutina = rutinaCargaService.crearRutina(rutinaCargaDTO);
        return new ResponseEntity<>(nuevaRutina, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaCargaDTO> obtenerRutina(@PathVariable BigDecimal id) {
        logger.info("Obteniendo rutina con ID: {}", id);
        RutinaCargaDTO rutina = rutinaCargaService.obtenerRutina(id);
        return ResponseEntity.ok(rutina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRutina(@PathVariable BigDecimal id, @RequestBody RutinaCargaDTO rutinaCargaDTO) {
        logger.info("Actualizando rutina ID: {}", id);
        rutinaCargaService.actualizarRutina(id, rutinaCargaDTO);
        return ResponseEntity.ok("Rutina actualizada exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRutina(@PathVariable BigDecimal id) {
        logger.info("Eliminando rutina ID: {}", id);
        rutinaCargaService.eliminarRutina(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RutinaCargaDTO>> listarRutinas() {
        logger.info("Listando todas las rutinas de carga");
        List<RutinaCargaDTO> rutinas = rutinaCargaService.listarRutinas();
        return ResponseEntity.ok(rutinas);
    }

    @PatchMapping("/{id}/datos-obtenidos")
    public ResponseEntity<Void> actualizarDatosObtenidos(
            @PathVariable BigDecimal id,
            @RequestParam Integer nuevosDatos) {
        logger.info("Actualizando datos obtenidos para rutina ID: {}", id);
        rutinaCargaService.actualizarDatosObtenidos(id, nuevosDatos);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/programacion")
    public ResponseEntity<Void> actualizarProgramacion(
            @PathVariable BigDecimal id,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime nuevaProgramacion) {
        logger.info("Actualizando programación para rutina ID: {}", id);
        rutinaCargaService.actualizarProgramacion(id, nuevaProgramacion);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/habilitado")
    public ResponseEntity<Void> actualizarHabilitado(
            @PathVariable BigDecimal id,
            @RequestParam("valor") Integer habilitado) {
        logger.info("Actualizando estado habilitado para rutina ID: {}", id);
        rutinaCargaService.actualizarHabilitado(id, habilitado);
        return ResponseEntity.noContent().build();
    }












    /**
     * Endpoint para registrar el inicio de ejecución de una rutina
     * @param rutinaId ID de la rutina a iniciar
     * @return Respuesta HTTP 200 si fue exitoso
     */
    @PostMapping("/{rutinaId}/iniciar")
    public ResponseEntity<Void> iniciarEjecucion(@PathVariable BigDecimal rutinaId) {
        rutinaCargaService.registrarInicioEjecucion(rutinaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para registrar el fin de ejecución de una rutina
     * @param rutinaId ID de la rutina a finalizar
     * @return Respuesta HTTP 200 si fue exitoso
     */
    @PostMapping("/{rutinaId}/finalizar")
    public ResponseEntity<Void> finalizarEjecucion(@PathVariable BigDecimal rutinaId) {
        rutinaCargaService.registrarFinEjecucion(rutinaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para obtener el tiempo de ejecución de una rutina
     * @param rutinaId ID de la rutina a consultar
     * @return Tiempo de ejecución en milisegundos o 404 si no está disponible
     */
    @GetMapping("/{rutinaId}/tiempo")
    public ResponseEntity<Long> obtenerTiempoEjecucion(@PathVariable BigDecimal rutinaId) {
        Long tiempo = rutinaCargaService.obtenerTiempoEjecucion(rutinaId);
        return tiempo != null ? ResponseEntity.ok(tiempo) : ResponseEntity.notFound().build();
    }






    @GetMapping("/enviar/chubb")
    public ResponseEntity<?> enviarDirectoChubb() {
        logger.info("[MANUAL] Solicitado envío MANUAL a Chubb");
        try {
            // Registrar inicio
            BigDecimal rutinaId = new BigDecimal(1);
            rutinaCargaService.registrarInicioEjecucion(rutinaId);

            List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarCotizacionesCompletas();
            logger.info("[MANUAL] Obtenidas {} cotizaciones para envío manual", cotizaciones.size());

            // Enviar las cotizaciones a Chubb (pero no usamos esta respuesta)
            ResponseEntity<String> response = enviarCotizaciones("Chubb", "http://localhost:8004/cotizacion/chubb", cotizaciones);

            // Verificar y procesar completadas
            rutinaCargaService.verificarYProcesarCotizacionesCompletadas(rutinaId);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("[MANUAL] Envío a Chubb COMPLETADO exitosamente");
            } else {
                logger.warn("[MANUAL] Envío a Chubb completado con estado {}", response.getStatusCode());
            }

            // Devolver la lista de cotizaciones en lugar de la respuesta del envío
            return ResponseEntity.ok(cotizaciones);

        } catch (Exception e) {
            logger.error("[MANUAL] Error en envío manual a Chubb: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en envío manual: " + e.getMessage());
        }
    }

    @GetMapping("/enviar/mapfre")
    public ResponseEntity<String> enviarDirectoMapfre() {
        BigDecimal rutinaId = new BigDecimal(2);
        rutinaCargaService.registrarInicioEjecucion(rutinaId);

        List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarCotizacionesCompletas();
        ResponseEntity<String> response = enviarCotizaciones("Mapfre", "http://localhost:8005/cotizacion/mapfre", cotizaciones);

        rutinaCargaService.verificarYProcesarCotizacionesCompletadas(rutinaId);
        return response;
    }

    @GetMapping("/enviar/axa")
    public ResponseEntity<String> enviarDirectoaxa() {
        BigDecimal rutinaId = new BigDecimal(4);
        rutinaCargaService.registrarInicioEjecucion(rutinaId);

        List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarCotizacionesCompletas();
        ResponseEntity<String> response = enviarCotizaciones("axa", "http://localhost:8001/cotizacion/axa", cotizaciones);

        rutinaCargaService.verificarYProcesarCotizacionesCompletadas(rutinaId);
        return response;
    }

    @GetMapping("/enviar/gnp")
    public ResponseEntity<String> enviarDirectognp() {
        BigDecimal rutinaId = new BigDecimal(3);
        rutinaCargaService.registrarInicioEjecucion(rutinaId);

        List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarCotizacionesCompletas();
        ResponseEntity<String> response = enviarCotizaciones("Mapfre", "http://localhost:8002/cotizacion/gnp", cotizaciones);

        rutinaCargaService.verificarYProcesarCotizacionesCompletadas(rutinaId);
        return response;
    }

    @GetMapping("/enviar/hdi")
    public ResponseEntity<String> enviarDirectoHDI() {
        BigDecimal rutinaId = new BigDecimal(5);
        rutinaCargaService.registrarInicioEjecucion(rutinaId);

        List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarCotizacionesCompletas();
        ResponseEntity<String> response = enviarCotizaciones("HDI", "http://localhost:8003/cotizacion/hdi", cotizaciones);

        rutinaCargaService.verificarYProcesarCotizacionesCompletadas(rutinaId);
        return response;
    }




    private ResponseEntity<String> enviarCotizaciones(String portal, String endpoint,
                                                      List<CotizacionCompletaResponseDTO> cotizaciones) {
        logger.info("[ENVIO] Preparando envío a {} ({} cotizaciones)", portal, cotizaciones.size());

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(15000);
            restTemplate.setRequestFactory(factory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Aseguradora-Destino", portal);
            logger.debug("[ENVIO] Headers configurados: {}", headers);

            HttpEntity<List<CotizacionCompletaResponseDTO>> request = new HttpEntity<>(cotizaciones, headers);
            logger.info("[ENVIO] Intentando conectar con {}...", endpoint);

            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, request, String.class);
            long elapsedTime = System.currentTimeMillis() - startTime;

            logger.info("[ENVIO] Respuesta recibida de {} en {} ms. Estado: {}",
                    portal, elapsedTime, response.getStatusCode());
            logger.debug("[ENVIO] Cuerpo respuesta: {}", response.getBody());

            return response;
        } catch (Exception e) {
            logger.error("[ENVIO] ERROR al enviar a {}: {}", portal, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar a " + portal + ": " + e.getMessage());
        }
    }






    @GetMapping("/{rutinaId}/verificar")
    public ResponseEntity<Boolean> tieneCotizacionesCompletadas(@PathVariable BigDecimal rutinaId) {
        boolean existe = rutinaCargaService.existeCotizacionCompletadaPorRutina(rutinaId);

        if (existe) {
            logger.info("Cotizaciones completadas encontradas. Registrando fin de ejecución...");

            // 1. Registrar fin de ejecución
            rutinaCargaService.registrarFinEjecucion(rutinaId);

            // 2. Actualizar el tiempo de ejecución en la base de datos
            rutinaCargaService.actualizarTiempoEjecucion(rutinaId);

            logger.info("Fin de ejecución registrado correctamente");
        }

        return ResponseEntity.ok(existe);
    }




    @GetMapping("/{rutinaId}/tiempo-ejecucion")
    public ResponseEntity<Map<String, Object>> obtenerTiempoEjecucionDetallado(@PathVariable BigDecimal rutinaId) {

            Map<String, Object> tiempoEjecucion = rutinaCargaService.obtenerTiempoEjecucionDetallado(rutinaId);

            return ResponseEntity.ok(tiempoEjecucion);


    }








    @DeleteMapping("/eliminar-completados/{rutinaCargaClave}")
    public ResponseEntity<?> eliminarResultadosCompletados(@PathVariable BigDecimal rutinaCargaClave) {
        boolean eliminados = resultadoCotizacionService.eliminarResultadosCompletadosPorRutina(rutinaCargaClave);

        if(eliminados) {
            return ResponseEntity.ok().body("Registros completados eliminados correctamente");
        }
        return ResponseEntity.ok().body("No se encontraron registros completados para eliminar");
    }
}