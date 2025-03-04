package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.ResultadoConsultaDTO;
import com.qualitas.portal.fraudes.account.application.service.ResultadoConsultaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/resultado-consulta")
public class ResultadoConsultaControlador {

    private static final Logger logger = LoggerFactory.getLogger(ResultadoConsultaControlador.class);

    @Autowired
    private ResultadoConsultaService resultadoConsultaService;

    // Crear ResultadoConsulta
    @PostMapping
    public ResponseEntity<ResultadoConsultaDTO> crearResultadoConsulta(@RequestBody ResultadoConsultaDTO requestDTO) {
        logger.info("Creando resultado de consulta con los datos: {}", requestDTO);
        ResultadoConsultaDTO resultadoDTO = resultadoConsultaService.crearResultadoConsulta(requestDTO);
        return new ResponseEntity<>(resultadoDTO, HttpStatus.CREATED);
    }

    // Obtener ResultadoConsulta por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResultadoConsultaDTO> obtenerResultadoConsulta(@PathVariable BigDecimal id) {
        logger.info("Obteniendo resultado de consulta con ID: {}", id);
        ResultadoConsultaDTO responseDTO = resultadoConsultaService.obtenerResultadoConsulta(id);
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar ResultadoConsulta
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarResultadoConsulta(@PathVariable BigDecimal id, @RequestBody ResultadoConsultaDTO requestDTO) {
        logger.info("Actualizando resultado de consulta con ID: {}", id);
        resultadoConsultaService.actualizarResultadoConsulta(id, requestDTO);
        return new ResponseEntity<>("Resultado de consulta actualizado con éxito", HttpStatus.OK);
    }

    // Eliminar ResultadoConsulta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResultadoConsulta(@PathVariable BigDecimal id) {
        logger.info("Eliminando resultado de consulta con ID: {}", id);
        resultadoConsultaService.eliminarResultadoConsulta(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los ResultadosConsulta
    @GetMapping
    public ResponseEntity<List<ResultadoConsultaDTO>> listarResultadosConsulta() {
        logger.info("Listando todos los resultados de consulta");
        List<ResultadoConsultaDTO> responseDTOs = resultadoConsultaService.listarResultadosConsulta();
        return ResponseEntity.ok(responseDTOs);
    }
}
