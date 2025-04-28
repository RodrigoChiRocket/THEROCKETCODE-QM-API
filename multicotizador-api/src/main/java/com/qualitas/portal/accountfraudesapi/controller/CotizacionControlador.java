package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.dto.CotizacionDTO;
import com.qualitas.portal.fraudes.account.application.dto.request.CotizacionCompletaDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.CotizacionCompletaResponseDTO;
import com.qualitas.portal.fraudes.account.application.dto.response.PageDTO;
import com.qualitas.portal.fraudes.account.application.service.CotizacionService;
import com.qualitas.portal.fraudes.account.application.service.ExcelImportService;
import com.qualitas.portal.fraudes.account.domain.model.Cotizacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cotizacion")
public class CotizacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(CotizacionControlador.class);


    @Autowired
    private ExcelImportService excelImportService;
    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping("/completa-catalogo")
    public ResponseEntity<CotizacionCompletaResponseDTO> crearCotizacionCompletaCatalogo(@RequestBody CotizacionCompletaDTO cotizacionCompletaDTO) {
        logger.info("Creando cotización completa con los datos: {}", cotizacionCompletaDTO);
        CotizacionCompletaResponseDTO responseDTO = cotizacionService.crearCotizacionCompletaCatalogo(cotizacionCompletaDTO);

        // Crear un ScheduledExecutorService con un solo hilo
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger contador = new AtomicInteger();
        // Ejecutar la petición HTTP en otro hilo después de un pequeño retraso
        executorService.schedule(() -> {
            try {
                contador.getAndIncrement();
                System.out.println("Enviando petición HTTP a Multicotizador API: " + contador.get());
                // Create RestTemplate
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8000/multicotizador-api/cotizacion/completa";

                // Create request headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Create request entity with headers and body
                HttpEntity<CotizacionCompletaResponseDTO> request = new HttpEntity<>(responseDTO, headers);

                // Send POST request
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

                // Handle response
                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Response: " + response.getBody());
                } else {
                    logger.error("Request failed with status code: " + response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Error al enviar petición HTTP", e);
            } finally {
                executorService.shutdown();
            }
        }, 1, TimeUnit.SECONDS);

        // Retornar inmediatamente sin esperar la respuesta HTTP
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }




/*
    @PostMapping("/completa")
    public ResponseEntity<CotizacionCompletaResponseDTO> crearCotizacionCompleta(@RequestBody CotizacionCompletaDTO cotizacionCompletaDTO) {
        logger.info("Creando cotización completa con los datos: {}", cotizacionCompletaDTO);
        CotizacionCompletaResponseDTO responseDTO = cotizacionService.crearCotizacionCompleta(cotizacionCompletaDTO);

        // Crear un ScheduledExecutorService con un solo hilo
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger contador = new AtomicInteger();
        // Ejecutar la petición HTTP en otro hilo después de un pequeño retraso
        executorService.schedule(() -> {
            try {
                contador.getAndIncrement();
                System.out.println("Enviando petición HTTP a Multicotizador API: " + contador.get());
                // Create RestTemplate
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8000/cotizacion/";

                // Create request headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Create request entity with headers and body
                HttpEntity<CotizacionCompletaResponseDTO> request = new HttpEntity<>(responseDTO, headers);

                // Send POST request
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

                // Handle response
                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Response: " + response.getBody());
                } else {
                    logger.error("Request failed with status code: " + response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Error al enviar petición HTTP", e);
            } finally {
                executorService.shutdown();
            }
        }, 1, TimeUnit.SECONDS);

        // Retornar inmediatamente sin esperar la respuesta HTTP
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

 */


    @PostMapping("/completa")
    public ResponseEntity<CotizacionCompletaResponseDTO> crearCotizacionCompleta(@RequestBody CotizacionCompletaDTO cotizacionCompletaDTO) {
        logger.info("Creando cotización completa con los datos: {}", cotizacionCompletaDTO);
        CotizacionCompletaResponseDTO responseDTO = cotizacionService.crearCotizacionCompleta(cotizacionCompletaDTO);
        // Lista de endpoints
        List<String> endpoints = Arrays.asList(
                "http://localhost:8001/cotizacion/axa",
                "http://localhost:8002/cotizacion/gnp",
                "http://localhost:8003/cotizacion/hdi",
                "http://localhost:8004/cotizacion/chubb",
                "http://localhost:8005/cotizacion/mapfre"
        );
        logger.info("Iniciando envío paralelo a {} endpoints", endpoints.size());
        // Enviar a cada endpoint en paralelo
        List<CompletableFuture<Void>> futures = endpoints.stream()
                .map(endpoint -> CompletableFuture.runAsync(() -> {
                    long startTime = System.currentTimeMillis();
                    logger.info("[INICIO] Enviando cotización a {}", endpoint);
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<CotizacionCompletaResponseDTO> request = new HttpEntity<>(responseDTO, headers);
                        ResponseEntity<String> response = restTemplate.postForEntity(endpoint, request, String.class);
                        if (response.getStatusCode().is2xxSuccessful()) {
                            logger.info("[ÉXITO] Respuesta de {} en {} ms: {}",
                                    endpoint,
                                    (System.currentTimeMillis() - startTime),
                                    response.getBody());
                        } else {
                            logger.error("[ERROR] {} respondió con código: {}",
                                    endpoint,
                                    response.getStatusCode());
                        }
                    } catch (Exception e) {
                        logger.error("[FALLO] Error en {}: {}", endpoint, e.getMessage());
                    } finally {
                        logger.info("[FIN] Procesamiento completado para {}", endpoint);
                    }
                }))
                .collect(Collectors.toList());
        // Opcional: Esperar a que todas las peticiones terminen (solo para logs)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> logger.info("Todas las cotizaciones se han procesado en paralelo"))
                .exceptionally(ex -> {
                    logger.error("Algunas cotizaciones fallaron", ex);
                    return null;
                });
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }




    // Crear Cotización
    @PostMapping
    public ResponseEntity<CotizacionDTO> crearCotizacion(@RequestBody CotizacionDTO requestDTO) {
        logger.info("Creando cotización con los datos: {}", requestDTO);
        // Crear la cotización a través del servicio
        CotizacionDTO cotizacionDTO = cotizacionService.crearCotizacion(requestDTO);
        return new ResponseEntity<>(cotizacionDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionDTO> obtenerCotizacion(@PathVariable BigDecimal id) {
        logger.info("Obteniendo cotización con ID: {}", id);
        CotizacionDTO cotizacionDTO = cotizacionService.obtenerCotizacionPorId(id);
        return ResponseEntity.ok(cotizacionDTO);
    }
    // Actualizar Cotización (versión mejorada)
    @PutMapping("/{id}")
    public ResponseEntity<CotizacionDTO> actualizarCotizacion(
            @PathVariable BigDecimal id,
            @RequestBody CotizacionDTO requestDTO) {

        logger.info("Actualizando cotización con ID: {}", id);

        try {
            // Validar que el ID del path coincida con el del cuerpo si viene
            if (requestDTO.getiCotizacionId() != null && !requestDTO.getiCotizacionId().equals(id)) {
                throw new IllegalArgumentException("ID en el cuerpo no coincide con el ID en la URL");
            }

            CotizacionDTO cotizacionActualizada = cotizacionService.actualizarCotizacion(id, requestDTO);

            return ResponseEntity.ok(cotizacionActualizada);

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al actualizar cotización: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar cotización con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar Cotización
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCotizacion(@PathVariable BigDecimal id) {
        logger.info("Eliminando cotización con ID: {}", id);

        cotizacionService.eliminarCotizacion(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todas las Cotizaciones
    @GetMapping
    public ResponseEntity<List<CotizacionCompletaResponseDTO>> listarCotizaciones() {
        logger.info("Listando todas las cotizaciones");
        // Obtener todas las cotizaciones a través del servicio
        List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarTodasLasCotizaciones();
        return ResponseEntity.ok(cotizaciones);
    }


    @GetMapping("/paginadas-completas")
    public ResponseEntity<PageDTO<CotizacionCompletaResponseDTO>> listarCotizacionesCompletasPaginadas(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "5") int tamanioPagina) {

        // Validación de parámetros
        if (pagina < 1) {
            throw new IllegalArgumentException("El número de página debe ser mayor o igual a 1");
        }
        if (tamanioPagina < 1) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor o igual a 1");
        }

        int offset = (pagina - 1) * tamanioPagina;

        // Obtener datos paginados completos
        List<CotizacionCompletaResponseDTO> dtos = cotizacionService
                .listarCotizacionesCompletasPaginadas(offset, tamanioPagina);
        int total = cotizacionService.contarTotalCotizaciones();

        // Crear respuesta paginada
        PageDTO<CotizacionCompletaResponseDTO> respuesta = new PageDTO<>(
                dtos,
                pagina,
                tamanioPagina,
                total
        );

        return ResponseEntity.ok(respuesta);
    }


   /* @PostMapping(value = "/importar-excel-servlet", consumes = "multipart/form-data")
    public ResponseEntity<List<CotizacionCompletaResponseDTO>> importarExcelServlet(
            @RequestParam("archivo") Part archivo) {

        if (archivo.getSize() == 0) {
            return ResponseEntity.badRequest().body(null);
        }

        try (InputStream inputStream = archivo.getInputStream()) {
            List<CotizacionCompletaResponseDTO> resultado = excelImportService.procesarCSV(inputStream);
            return ResponseEntity.ok(resultado);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }*/



    private CotizacionDTO convertirADTO(Cotizacion cotizacion) {
        CotizacionDTO dto = new CotizacionDTO();
        dto.setiCotizacionId(cotizacion.getiCotizacionId());
        dto.setiCategoriaVehiculoClave(cotizacion.getiCategoriaVehiculoClave());
        dto.setiTipoSeguroClave(cotizacion.getiTipoSeguroClave());
        dto.setiUsoClave(cotizacion.getiUsoClave());
        dto.setiTipoAutoClave(cotizacion.getiTipoAutoClave());
        dto.setiAutoClave(cotizacion.getiAutoClave());
        dto.setiPersonaClave(cotizacion.getiPersonaClave());
        dto.setdFechaCreacion(cotizacion.getdFechaCreacion());
        dto.setiUsuarioCreacion(cotizacion.getiUsuarioCreacion());
        return dto;
    }

    @PostMapping(value = "/importar-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarExcel(@RequestParam("archivo") MultipartFile archivo) {
        try (InputStream inputStream = archivo.getInputStream()) {
            String filename = archivo.getOriginalFilename();

            if (filename != null && filename.endsWith(".xlsx")) {
                List<CotizacionCompletaResponseDTO> resultado = excelImportService.procesarExcel(inputStream);
                return ResponseEntity.ok(resultado);
            }
            //else if (filename != null && filename.endsWith(".csv")) {
              //  List<CotizacionCompletaResponseDTO> resultado = excelImportService.procesarCSV(inputStream);
            //return ResponseEntity.ok(resultado);
        //    }
        else {
                return ResponseEntity.badRequest().body("Formato no soportado. Use .xlsx o .csv");
            }
        } catch (IOException e) {
            return ResponseEntity.noContent().build();
        }
    }



    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView handleFileUpload(@RequestParam("archivo") MultipartFile file) {
        ModelAndView mav = new ModelAndView("resultado");

        if (!file.isEmpty()) {
            try {
                // Procesa el archivo (ej: guardarlo en disco)
                byte[] bytes = file.getBytes();
                String fileName = file.getOriginalFilename();
                // Guardar lógica aquí...

                mav.addObject("mensaje", "Archivo '" + fileName + "' subido correctamente");
            } catch (Exception e) {
                mav.addObject("mensaje", "Error al subir el archivo: " + e.getMessage());
            }
        } else {
            mav.addObject("mensaje", "El archivo está vacío");
        }

        return mav;
    }

    @PostMapping(value = "/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String handleUpload(@RequestParam("archivo") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "Error: Archivo vacío";
        }
        // Guardar el archivo en disco (ejemplo)
        Files.copy(file.getInputStream(), Paths.get("/tmp/" + file.getOriginalFilename()));
        return "Archivo subido: " + file.getOriginalFilename();
    }
    /*
    @PostMapping(value = "/importar-excel2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarExcel2(@RequestParam("archivo") MultipartFile archivo) {
        logger.info("Importando cotizaciones desde archivo Excel/CSV");

        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        try (InputStream inputStream = archivo.getInputStream()) {
            List<CotizacionCompletaResponseDTO> resultado = excelImportService.procesarCSV(inputStream);

            // Opcional: Guardar las cotizaciones en la base de datos
            // resultado.forEach(dto -> {
            //     CotizacionCompletaDTO requestDto = convertirResponseARequest(dto);
            //     cotizacionService.crearCotizacionCompleta(requestDto);
            // });

            return ResponseEntity.ok(resultado);
        } catch (IOException e) {
            logger.error("Error al procesar el archivo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al importar cotizaciones", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }*/

    // Método auxiliar para convertir de ResponseDTO a RequestDTO si necesitas guardar los datos
    private CotizacionCompletaDTO convertirResponseARequest(CotizacionCompletaResponseDTO responseDTO) {
        CotizacionCompletaDTO requestDTO = new CotizacionCompletaDTO();
        requestDTO.setPersona(responseDTO.getPersona());
        requestDTO.setAuto(responseDTO.getAuto());
        requestDTO.setCotizacion(responseDTO.getCotizacion());
        return requestDTO;
    }





    @GetMapping("/listar-y-enviar")
    public ResponseEntity<List<CotizacionCompletaResponseDTO>> listarYEnviarCotizaciones() {
        logger.info("Listando y enviando todas las cotizaciones");

        // 1. Obtener todas las cotizaciones
        List<CotizacionCompletaResponseDTO> cotizaciones = cotizacionService.listarTodasLasCotizaciones();

        // 2. Crear executor para enviar en segundo plano
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5); // Pool de 5 hilos

        // 3. Enviar cada cotización al endpoint externo
        cotizaciones.forEach(cotizacion -> {
            executorService.schedule(() -> {
                try {
                    logger.info("Enviando cotización ID: {} a endpoint externo",
                            cotizacion.getCotizacion().getiCotizacionId());

                    // Convertir ResponseDTO a RequestDTO
                    CotizacionCompletaDTO requestDTO = new CotizacionCompletaDTO();
                    requestDTO.setPersona(cotizacion.getPersona());
                    requestDTO.setAuto(cotizacion.getAuto());
                    requestDTO.setCotizacion(cotizacion.getCotizacion());

                    // Configurar RestTemplate
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    // Crear request
                    HttpEntity<CotizacionCompletaDTO> request = new HttpEntity<>(requestDTO, headers);

                    // Enviar POST
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            "http://localhost:8000/multicotizador-api/cotizacion/completa",
                            request,
                            String.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        logger.info("Cotización ID: {} enviada con éxito",
                                cotizacion.getCotizacion().getiCotizacionId());
                    } else {
                        logger.error("Error al enviar cotización ID: {}. Código: {}",
                                cotizacion.getCotizacion().getiCotizacionId(),
                                response.getStatusCode());
                    }
                } catch (Exception e) {
                    logger.error("Error al enviar cotización", e);
                }
            }, 100, TimeUnit.MILLISECONDS); // Pequeño delay entre envíos
        });

        // 4. Cerrar el executor después de un tiempo
        executorService.schedule(() -> {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }, 5, TimeUnit.SECONDS);

        // 5. Retornar la lista de cotizaciones
        return ResponseEntity.ok(cotizaciones);
    }

}