package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.convertDTO.PersonaConvertDTO;
import com.qualitas.portal.fraudes.account.application.dto.AutoDTO;
import com.qualitas.portal.fraudes.account.application.dto.EliminarCotizacionesRequest;
import com.qualitas.portal.fraudes.account.application.dto.PersonaDTO;
import com.qualitas.portal.fraudes.account.application.dto.request.CotizacionCompletaDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.CotizacionCompletaResponseDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.PageDTO;
import com.qualitas.portal.fraudes.account.application.service.CatalogoDatosService;
import com.qualitas.portal.fraudes.account.application.service.CatalogoDatosService2;
import com.qualitas.portal.fraudes.account.application.service.CotizacionService;
import com.qualitas.portal.fraudes.account.application.service.PersonaService;
import com.qualitas.portal.fraudes.account.domain.enums.Sexo;
import com.qualitas.portal.fraudes.account.domain.model.CatalogoDatos;
import com.qualitas.portal.fraudes.account.domain.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/catalogo")
public class CatalogoDatosControlador {

    @Autowired
    private PersonaConvertDTO personaConvertDTO;

    @Autowired
    private CatalogoDatosService2 catalogoDatosService2;

    private final CatalogoDatosService catalogoDatosService;

    private final CotizacionService cotizacionService;

    private final PersonaService personaService;


    public CatalogoDatosControlador(CatalogoDatosService catalogoDatosService, CotizacionService cotizacionService, PersonaService personaService) {
        this.catalogoDatosService = catalogoDatosService;
        this.cotizacionService = cotizacionService;
        this.personaService = personaService;
    }


    @GetMapping("/paginadas-completas")
    public ResponseEntity<PageDTO<CotizacionCompletaResponseDTO>> listarCotizacionesCompletasPaginadas(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "5") int tamanioPagina) {

        // Validar parámetros de paginación
        pagina = Math.max(1, pagina);
        tamanioPagina = Math.max(1, tamanioPagina);

        int offset = (pagina - 1) * tamanioPagina;

        // Obtener datos paginados completos
        List<CotizacionCompletaResponseDTO> dtos = cotizacionService
                .listarCotizacionesCompletasPaginadas(offset, tamanioPagina);
        int total = cotizacionService.contarTotalCotizaciones();

        // Calcular el número total de páginas
        int totalPaginas = (int) Math.ceil((double) total / tamanioPagina);

        // Si la página solicitada es mayor que el total de páginas, devolver última página
        if (pagina > totalPaginas && totalPaginas > 0) {
            pagina = totalPaginas;
            offset = (pagina - 1) * tamanioPagina;
            dtos = cotizacionService.listarCotizacionesCompletasPaginadas(offset, tamanioPagina);
        }

        // Crear respuesta paginada
        PageDTO<CotizacionCompletaResponseDTO> respuesta = new PageDTO<>(
                dtos,
                pagina,
                tamanioPagina,
                total
        );

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCotizador(@PathVariable BigDecimal id) {

            cotizacionService.eliminarCotizacion(id); // Método void
            return ResponseEntity.ok("Cotización eliminada correctamente");

    }
    @PutMapping("/completa/{id}")
    public ResponseEntity<CotizacionCompletaResponseDTO> actualizarCotizacionCompleta(
            @PathVariable BigDecimal id,
            @RequestBody CotizacionCompletaDTO requestDTO) {



            CotizacionCompletaResponseDTO response = cotizacionService.actualizarCotizacionCompleta(id, requestDTO);
            return ResponseEntity.ok(response);

    }

    @DeleteMapping("/eliminar-cotizaciones")
    public ResponseEntity<?> eliminarCotizaciones(@RequestBody EliminarCotizacionesRequest request) {
        cotizacionService.eliminarListaCotizacion(new HashSet<>(request.getCotizacionesId()));
        return ResponseEntity.ok(("Se a eliminado correctamente"));
    }
    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarCatalogo(@RequestParam("archivo") MultipartFile archivo) {

            // 1. Procesar el Excel y obtener los datos
            List<CatalogoDatos> datosCatalogo = catalogoDatosService.procesarCatalogoExcel(archivo);



            // 2. Para cada registro del catálogo, crear una persona (cotización)
          List<CotizacionCompletaResponseDTO> personasCreadas = new ArrayList<>();
            for (CatalogoDatos datos : datosCatalogo) {
                CotizacionCompletaResponseDTO personaCreada = catalogoDatosService2.crearCotizacionCompletaDesdeCatalogo(datos);
                personasCreadas.add(personaCreada);
            }







            // 3. Retornar respuesta con las personas creadas
            return ResponseEntity.ok(personasCreadas);




    }
}
