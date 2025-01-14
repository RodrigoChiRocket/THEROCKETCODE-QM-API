package com.qualitas.portal.accountfraudesapi.controller;
import com.qualitas.portal.fraudes.account.dao.DUADao;
import com.qualitas.portal.fraudes.account.dto.DUADTO;
import com.qualitas.portal.fraudes.account.model.DUA;
import com.qualitas.portal.fraudes.account.service.DUAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dua")
public class DUAControlador {

    @Autowired
    private DUAService duaService;
    @GetMapping("/{id}")
    public ResponseEntity<DUADTO> obtenerDUA(@PathVariable BigDecimal id) {
        return ResponseEntity.ok(duaService.obtenerDUA(id));
    }

}
