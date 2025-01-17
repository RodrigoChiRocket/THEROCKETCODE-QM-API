package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.TrazaDTO;
import com.qualitas.portal.fraudes.account.service.TrazoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/traza")
public class TrazaControlador {
    @Autowired
    private TrazoService trazoService;

    // Endpoint para obtener las trazas de un siniestro específico
    @GetMapping("/siniestro/{siniestroId}")
    public List<TrazaDTO> obtenerTrazasPorSiniestro(@PathVariable Long siniestroId) {
        return trazoService.obtenerTrazasPorSiniestro(siniestroId);
    }
}
