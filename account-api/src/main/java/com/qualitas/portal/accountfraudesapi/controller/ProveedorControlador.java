package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.ProveedorDTO;
import com.qualitas.portal.fraudes.account.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/proveedor")
@Validated
public class ProveedorControlador {
    @Autowired
    private ProveedorService proveedorService;



    // Obtener un proveedor por ID (retorna DTO)
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> obtenerProveedor(@PathVariable BigDecimal id) {
        ProveedorDTO proveedor = proveedorService.obtenerProveedor(id);
        return proveedor != null ? ResponseEntity.ok(proveedor) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo proveedor (entrada y salida como DTO)
    @PostMapping
    public ResponseEntity<BigDecimal> crearProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        BigDecimal proveedorId = proveedorService.crearProveedor(proveedorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorId);
    }

    // Actualizar un proveedor (entrada como DTO)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProveedor(@PathVariable BigDecimal id, @RequestBody ProveedorDTO proveedorDTO) {
        // Establecer el ID en el DTO antes de pasarlo al servicio
        proveedorDTO.setiProveeID(id);
        proveedorService.actualizarProveedor(proveedorDTO);
        return ResponseEntity.ok("Proveedor actualizado con éxito");
    }

    // Eliminar un proveedor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable BigDecimal id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los proveedores (retorna lista de DTOs)
    @GetMapping("/listar")
    public ResponseEntity<List<ProveedorDTO>> listarProveedores() {
        List<ProveedorDTO> proveedores = proveedorService.listarProveedores();
        return ResponseEntity.ok(proveedores);
    }
}
