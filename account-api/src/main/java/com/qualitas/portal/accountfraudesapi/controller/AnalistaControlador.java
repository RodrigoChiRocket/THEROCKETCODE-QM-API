package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.AnalistaDTO;
import com.qualitas.portal.fraudes.account.service.AnalistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/analista")
public class AnalistaControlador {

    private final AnalistaService analistaService;



    @Autowired
    public AnalistaControlador(AnalistaService analistaService) {
        this.analistaService = analistaService;
    }


    @GetMapping
    public String obtenerSaludo() {
        return analistaService.checkout();
    }
    // Endpoint para crear un analista
    @PostMapping
    public ResponseEntity<BigDecimal> crearAnalista(@RequestBody AnalistaDTO analistaDTO) {

        BigDecimal id = analistaService.crearAnalista(analistaDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED); // Retorna el ID del nuevo analista con un código 201 (CREADO)

    }
    @GetMapping("/{id}")
    public ResponseEntity<AnalistaDTO> obtenerAnalista(@PathVariable BigDecimal id){
        AnalistaDTO analistaDTO= analistaService.obtenerAnalista(id);
        return analistaDTO != null ? ResponseEntity.ok(analistaDTO) : ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarAnalista(@PathVariable BigDecimal id, @RequestBody AnalistaDTO analistaDTO){
        analistaDTO.setiAnalisID(id);

        analistaService.actualizarAnalista(analistaDTO);

        return new ResponseEntity<>("Analista actualizado con exito", HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAnalista(@PathVariable BigDecimal id){
        analistaService.eliminarAnalista(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/listar")
    public  ResponseEntity<List<AnalistaDTO>> listarAnalistas(){
        List<AnalistaDTO> analistaDTOS = analistaService.listarAnalistas();
        return ResponseEntity.ok(analistaDTOS);
    }

}
