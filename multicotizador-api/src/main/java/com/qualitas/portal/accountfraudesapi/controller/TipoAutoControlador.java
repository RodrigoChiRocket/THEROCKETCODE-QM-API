package com.qualitas.portal.accountfraudesapi.controller;
import com.qualitas.portal.fraudes.account.application.dto.TipoAutoDTO;
import com.qualitas.portal.fraudes.account.application.service.TipoAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tipoauto")
public class TipoAutoControlador {
    @Autowired
    private TipoAutoService tipoAutoService;

    @PostMapping
    public TipoAutoDTO crearTipoAuto(@RequestBody TipoAutoDTO tipoAutoDTO) {
        return tipoAutoService.crearTipoAuto(tipoAutoDTO);
    }

    @GetMapping("/{id}")
    public TipoAutoDTO obtenerTipoAuto(@PathVariable BigDecimal id) {
        return tipoAutoService.obtenerTipoAuto(id);
    }

    @PutMapping("/{id}")
    public TipoAutoDTO actualizarTipoAuto(@PathVariable BigDecimal id, @RequestBody TipoAutoDTO tipoAutoDTO) {
        return tipoAutoService.actualizarTipoAuto(id, tipoAutoDTO);
    }

    @GetMapping
    public List<TipoAutoDTO> listarTiposAuto() {
        return tipoAutoService.listarTiposAuto();
    }

    @DeleteMapping("/{id}")
    public void eliminarTipoAuto(@PathVariable BigDecimal id) {
        tipoAutoService.eliminarTipoAuto(id);
    }
}
