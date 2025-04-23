package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.response.EstadisticasCoberturaDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.EstadisticasTiempoEjecucionDTO;
import com.qualitas.portal.fraudes.account.application.service.DashBoardService;
import com.qualitas.portal.fraudes.account.application.service.RutinaCargaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Cambio crucial aquí
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
@RestController
public class DashboardControlador {


    @Autowired
    private  DashBoardService dashBoardService;

    @Autowired
    private RutinaCargaService rutinaCargaService;

    @GetMapping("/estadisticas-carga-datos")
    public ResponseEntity<EstadisticasCoberturaDTO> obtenerEstadisticasCoberturas() {
        EstadisticasCoberturaDTO estadisticas = dashBoardService.obtenerEstadisticasCoberturasMesActual();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/estadisticas/por-mes-simple")
    public ResponseEntity<List<Map<String, Object>>> obtenerEstadisticasPorMesSimple() {
        List<Map<String, Object>> datos = dashBoardService.obtenerConteoResultadosPorMes();
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/estadisticas-grafica-tiempo")
    public EstadisticasTiempoEjecucionDTO obtenerEstadisticasCompletas() {
        return rutinaCargaService.obtenerEstadisticasTiemposEjecucion();
    }
}
