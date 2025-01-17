package com.qualitas.portal.accountfraudesapi.controller;


import com.qualitas.portal.fraudes.account.dto.DocumentoDTO;
import com.qualitas.portal.fraudes.account.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/documento")
public class DocumentoControlador {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> obtenerDocumento(@PathVariable BigDecimal id) {
        // Llamamos al servicio para obtener el documento convertido a DTO
        DocumentoDTO documentoDTO = documentoService.obtenerDocumento(id);
        return ResponseEntity.ok(documentoDTO);
    }
}