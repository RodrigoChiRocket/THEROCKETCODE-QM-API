package com.qualitas.portal.accountfraudesapi.controller;
import com.qualitas.portal.fraudes.account.dto.EtiquetaSiseDTO;
import com.qualitas.portal.fraudes.account.service.EtiquetaSiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;



@RestController
@RequestMapping("/etiquetasise")
public class EtiquetaSiseControlador {
    @Autowired
    private EtiquetaSiseService etiquetaSiseService;

    @GetMapping("/{id}")
    public EtiquetaSiseDTO obtenerEtiquetaSise(@PathVariable BigDecimal id) {
        return etiquetaSiseService.obtenerEtiquetaSise(id);
    }
}
