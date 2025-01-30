package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.PersonaDTO;
import com.qualitas.portal.fraudes.account.application.service.PersonaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/persona")
public class PersonaControlador {

    private static final Logger logger = LoggerFactory.getLogger(PersonaControlador.class);

    @Autowired
    private PersonaService personaService;

    // Crear Persona
    @PostMapping
    public ResponseEntity<PersonaDTO> crearPersona(@RequestBody PersonaDTO requestDTO) {
        logger.info("Creando persona con los datos: {}", requestDTO);
        // Convertir requestDTO a PersonaDTO dentro del servicio y luego crear la persona
        PersonaDTO personaDTO = personaService.crearPersona(requestDTO);
        return new ResponseEntity<>(personaDTO, HttpStatus.CREATED);
    }

    // Obtener Persona por ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> obtenerPersona(@PathVariable BigDecimal id) {
        logger.info("Obteniendo persona con ID: {}", id);
        // Obtener PersonaDTO desde el servicio
        PersonaDTO responseDTO = personaService.obtenerPersona(id);
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar Persona
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPersona(@PathVariable BigDecimal id, @RequestBody PersonaDTO requestDTO) {
        logger.info("Actualizando persona con ID: {}", id);
        // Actualizar persona en el servicio usando el DTO recibido
        personaService.actualizarPersona(id, requestDTO);
        return new ResponseEntity<>("Persona actualizada con éxito", HttpStatus.OK);
    }

    // Eliminar Persona
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable BigDecimal id) {
        logger.info("Eliminando persona con ID: {}", id);
        // Eliminar la persona a través del servicio
        personaService.eliminarPersona(id);
        return ResponseEntity.noContent().build(); // Retorna 204 (No Content) al eliminar exitosamente
    }

    // Listar todas las Personas
    @GetMapping
    public ResponseEntity<List<PersonaDTO>> listarPersonas() {
        logger.info("Listando todas las personas");
        List<PersonaDTO> responseDTOs = personaService.listarPersonas();
        return ResponseEntity.ok(responseDTOs);
    }
}
