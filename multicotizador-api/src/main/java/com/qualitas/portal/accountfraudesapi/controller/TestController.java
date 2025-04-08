package com.qualitas.portal.accountfraudesapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired(required = false)
    private MultipartResolver multipartResolver;


    @PostMapping(path = "/importar-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> handleFileUpload(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("status", "error");
                response.put("message", "Por favor seleccione un archivo");
                return ResponseEntity.badRequest().body(response);
            }

            // Procesar el archivo aquí
            String fileName = file.getOriginalFilename();
            long size = file.getSize();

            response.put("status", "success");
            response.put("filename", fileName);
            response.put("size", size);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al procesar el archivo: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/multipart-check")
    public ResponseEntity<String> checkMultipartConfig() {
        if (multipartResolver != null) {
            return ResponseEntity.ok("Multipart configurado correctamente");
        }
        return ResponseEntity.status(500)
                .body("Multipart NO está configurado");
    }
}