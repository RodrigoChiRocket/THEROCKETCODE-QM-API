package com.qualitas.portal.accountfraudesapi.controller;


import com.qualitas.portal.fraudes.account.application.service.ResultadoCotizacionService;
import com.qualitas.portal.fraudes.account.domain.dto.ResultadoCotizacionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/resultados-cotizacion")
public class ResultadoCotizacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(ResultadoCotizacionControlador.class);

    @Autowired
    private ResultadoCotizacionService resultadoCotizacionService;

    @PostMapping
    public ResponseEntity<ResultadoCotizacionDTO> crearResultadoCotizacion(@RequestBody ResultadoCotizacionDTO dto) {
        // Loguear los datos del request
        logger.info("Recibiendo solicitud para crear un resultado de cotización. Datos recibidos:" + dto);


        // Procesar la solicitud
        ResultadoCotizacionDTO resultadoDTO = resultadoCotizacionService.crearResultadoCotizacion(dto);

        // Loguear el resultado creado
        logger.info("Resultado de cotización creado: {}", resultadoDTO);
        return new ResponseEntity<>(resultadoDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoCotizacionDTO> obtenerResultadoCotizacion(@PathVariable BigDecimal id) {
        logger.info("Recibiendo solicitud para obtener resultado de cotización con ID: {}", id);
        ResultadoCotizacionDTO resultadoDTO = resultadoCotizacionService.obtenerResultadoCotizacion(id);
        return ResponseEntity.ok(resultadoDTO);
    }

    @GetMapping
    public ResponseEntity<List<ResultadoCotizacionDTO>> listarResultadosCotizacion() {
        logger.info("Recibiendo solicitud para listar todos los resultados de cotización");
        List<ResultadoCotizacionDTO> resultados = resultadoCotizacionService.listarResultadosCotizacion();
        logger.info("Número de resultados de cotización encontrados: {}", resultados.size());
        return ResponseEntity.ok(resultados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoCotizacionDTO> actualizarResultadoCotizacion(
            @PathVariable BigDecimal id, @RequestBody ResultadoCotizacionDTO dto) {
        logger.info("Recibiendo solicitud para actualizar resultado de cotización con ID: {}. Datos recibidos: {}", id, dto);
        ResultadoCotizacionDTO resultadoDTO = resultadoCotizacionService.actualizarResultadoCotizacion(id, dto);
        logger.info("Resultado de cotización actualizado: {}", resultadoDTO);
        return ResponseEntity.ok(resultadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResultadoCotizacion(@PathVariable BigDecimal id) {
        logger.info("Recibiendo solicitud para eliminar resultado de cotización con ID: {}", id);
        resultadoCotizacionService.eliminarResultadoCotizacion(id);
        logger.info("Resultado de cotización con ID: {} eliminado", id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/por-cotizacion-clave/{cotizacionClave}")
    public ResponseEntity<List<ResultadoCotizacionDTO>> obtenerResultadoPorCotizacionClave(@PathVariable BigDecimal cotizacionClave) {
        logger.info("Recibiendo solicitud para obtener resultado de cotización por clave: {}", cotizacionClave);
        List<ResultadoCotizacionDTO> resultados = resultadoCotizacionService.obtenerResultadoPorCotizacionClave(cotizacionClave);
        if (!resultados.isEmpty()) {
            logger.info("Resultados de cotización encontrados: {}", resultados);
            return ResponseEntity.ok(resultados);
        } else {
            logger.warn("No se encontraron resultados de cotización con clave: {}", cotizacionClave);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar/{cotizacionClave}/{nombreSeguro}")
    public ResponseEntity<List<ResultadoCotizacionDTO>> buscarPorNombreSeguroYCotizacionClave(
            @PathVariable BigDecimal cotizacionClave,
            @PathVariable String nombreSeguro
    ) {
        logger.info("Recibiendo solicitud para buscar resultados por clave de cotización: {} y nombre de seguro: {}", cotizacionClave, nombreSeguro);

        // Llamar al servicio
        List<ResultadoCotizacionDTO> resultados = resultadoCotizacionService.buscarPorNombreSeguroYCotizacionClave(nombreSeguro, cotizacionClave);

        if (!resultados.isEmpty()) {
            logger.info("Resultados encontrados: {}", resultados);
            return ResponseEntity.ok(resultados);
        } else {
            logger.warn("No se encontraron resultados para clave de cotización: {} y nombre de seguro: {}", cotizacionClave, nombreSeguro);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/verificar-estado/{cotizacionClave}")
    public ResponseEntity<Boolean> verificarEstadoCotizacion(@PathVariable BigDecimal cotizacionClave) {
        logger.info("Recibiendo solicitud para verificar estado de cotización con clave: {}", cotizacionClave);
        boolean resultado = resultadoCotizacionService.verificarEstadoCotizacion(cotizacionClave);
        logger.info("Resultado de verificación: {}", resultado);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ResultadoCotizacionDTO>> filtrarResultadosCotizacion(
            @RequestParam(required = false) BigDecimal cotizacionClave,
            @RequestParam(required = false) String nombreSeguro,
            @RequestParam(required = false) String nombreCobertura) {
        logger.info("Recibiendo solicitud para filtrar resultados de cotización. Clave: {}, Seguro: {}, Cobertura: {}",
                cotizacionClave, nombreSeguro, nombreCobertura);

        List<ResultadoCotizacionDTO> resultados = resultadoCotizacionService.filtrarResultadosCotizacion(
                cotizacionClave, nombreSeguro, nombreCobertura);

        if (!resultados.isEmpty()) {
            logger.info("Resultados encontrados: {}", resultados);
            return ResponseEntity.ok(resultados);
        } else {
            logger.warn("No se encontraron resultados con los filtros proporcionados");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contar-por-rutina/{rutinaClave}")
    public ResponseEntity<Integer> contarRegistrosPorRutina(@PathVariable BigDecimal rutinaClave) {
        logger.info("Recibiendo solicitud para contar registros por rutina: {}", rutinaClave);
        int total = resultadoCotizacionService.contarRegistrosPorRutina(rutinaClave);
        logger.info("Total de registros encontrados: {}", total);
        return ResponseEntity.ok(total);
    }



    

}