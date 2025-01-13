package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.TipoProveedorDTO;
import com.qualitas.portal.fraudes.account.service.TipoProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tipoProveedor")
public class TipoProveedorController {

    @Autowired
    private TipoProveedorService tipoProveedorService;

    // Obtener un tipo de proveedor por ID (retorna DTO)
    @GetMapping("/{id}")
    public ResponseEntity<TipoProveedorDTO> obtenerTipoProveedor(@PathVariable BigDecimal id) {
        TipoProveedorDTO tipoProveedor = tipoProveedorService.obtenerTipoProveedor(id);
        return tipoProveedor != null ? ResponseEntity.ok(tipoProveedor) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo tipo de proveedor (entrada y salida como DTO)
    @PostMapping
    public ResponseEntity<Void> crearTipoProveedor(@RequestBody TipoProveedorDTO tipoProveedorDTO) {
        tipoProveedorService.crearTipoProveedor(tipoProveedorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Actualizar un tipo de proveedor (entrada como DTO)
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTipoProveedor(@PathVariable BigDecimal id, @RequestBody TipoProveedorDTO tipoProveedorDTO) {
        // Establecer el ID en el DTO antes de pasarlo al servicio
        tipoProveedorDTO.setiTProveeId(id);
        tipoProveedorService.actualizarTipoProveedor(tipoProveedorDTO);
        return ResponseEntity.ok("Tipo de proveedor actualizado con éxito");
    }

    // Eliminar un tipo de proveedor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoProveedor(@PathVariable BigDecimal id) {
        tipoProveedorService.eliminarTipoProveedor(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los tipos de proveedor (retorna lista de DTOs)
    @GetMapping("/listar")
    public ResponseEntity<List<TipoProveedorDTO>> listarTiposProveedor() {
        List<TipoProveedorDTO> tiposProveedor = tipoProveedorService.listarTiposProveedor();
        return ResponseEntity.ok(tiposProveedor);
    }
}
