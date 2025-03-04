package com.qualitas.portal.accountfraudesapi.controller;
import com.qualitas.portal.fraudes.account.application.dto.TipoSeguroDTO;
import com.qualitas.portal.fraudes.account.application.service.TipoSeguroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tiposeguro")
public class TipoSeguroControlador {

    @Autowired
    private TipoSeguroService tipoSeguroService;

    @PostMapping
    public TipoSeguroDTO crearTipoSeguro(@RequestBody TipoSeguroDTO tipoSeguroDTO) {
        return tipoSeguroService.crearTipoSeguro(tipoSeguroDTO);
    }

    @GetMapping("/{id}")
    public TipoSeguroDTO obtenerTipoSeguro(@PathVariable BigDecimal id) {
        return tipoSeguroService.obtenerTipoSeguro(id);
    }

    @PutMapping("/{id}")
    public TipoSeguroDTO actualizarTipoSeguro(@PathVariable BigDecimal id, @RequestBody TipoSeguroDTO tipoSeguroDTO) {
        return tipoSeguroService.actualizarTipoSeguro(id, tipoSeguroDTO);
    }

    @GetMapping
    public List<TipoSeguroDTO> listarTiposSeguro() {
        return tipoSeguroService.listarTiposSeguro();
    }

    @DeleteMapping("/{id}")
    public void eliminarTipoSeguro(@PathVariable BigDecimal id) {
        tipoSeguroService.eliminarTipoSeguro(id);
    }
}
