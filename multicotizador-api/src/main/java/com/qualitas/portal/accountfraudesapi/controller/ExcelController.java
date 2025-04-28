package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.application.service.ExcelReaderService;
import com.qualitas.portal.fraudes.account.domain.model.Vehiculo;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/vehiculos")
public class ExcelController {

    private final ExcelReaderService excelReaderService;

    @Autowired
    public ExcelController(ExcelReaderService excelReaderService) {
        this.excelReaderService = excelReaderService;
    }

    @PostMapping(value = "/probar-archivo")
    public ResponseEntity<Map<String, Object>> probarSubidaArchivo(HttpServletRequest rawRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Verificar si ya es una solicitud multipart
            MultipartHttpServletRequest multipartRequest;
            if (rawRequest instanceof MultipartHttpServletRequest) {
                multipartRequest = (MultipartHttpServletRequest) rawRequest;
            } else {
                // Intentar convertir
                multipartRequest = new DefaultMultipartHttpServletRequest(rawRequest);
            }

            MultipartFile archivo = multipartRequest.getFile("archivo");

            if (archivo == null) {
                response.put("status", "error");
                response.put("message", "No se encontró el parámetro 'archivo' en la solicitud");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (archivo.isEmpty()) {
                response.put("status", "error");
                response.put("message", "El archivo está vacío");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            response.put("status", "success");
            response.put("filename", archivo.getOriginalFilename());
            response.put("size", archivo.getSize());
            response.put("contentType", archivo.getContentType());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al procesar el archivo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/importar-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> procesarExcel(
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> respuesta = new HashMap<>();

        if (archivo == null || archivo.isEmpty()) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Debe seleccionar un archivo Excel");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        try {
            // Procesar el archivo Excel
            List<Vehiculo> vehiculos = excelReaderService.procesarExcel(archivo);

            if (vehiculos == null || vehiculos.isEmpty()) {
                respuesta.put("estado", "error");
                respuesta.put("mensaje", "El archivo no contiene datos válidos");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }

            respuesta.put("estado", "éxito");
            respuesta.put("datos", vehiculos);
            respuesta.put("total", vehiculos.size());
            respuesta.put("mensaje", "Archivo procesado correctamente");

            return new ResponseEntity<>(respuesta, HttpStatus.OK);

        } catch (Exception e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Error al procesar el archivo: " + e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "/probar-archivo2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> probarSubidaArchivo2(
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> response = new HashMap<>();

        if (archivo == null) {
            response.put("status", "error");
            response.put("message", "No se encontró el parámetro 'archivo'");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (archivo.isEmpty()) {
            response.put("status", "error");
            response.put("message", "El archivo está vacío");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("status", "success");
        response.put("filename", archivo.getOriginalFilename());
        response.put("size", archivo.getSize());
        response.put("contentType", archivo.getContentType());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/diagnostico-multipart")
    public ResponseEntity<Map<String, Object>> diagnosticoMultipart(HttpServletRequest request) {
        Map<String, Object> resultado = new HashMap<>();

        resultado.put("contentType", request.getContentType());
        resultado.put("metodo", request.getMethod());
        resultado.put("esMultipart", ServletFileUpload.isMultipartContent(request));

        try {
            if (request instanceof MultipartHttpServletRequest) {
                resultado.put("archivos", ((MultipartHttpServletRequest) request).getFileMap().keySet());
            }
        } catch (Exception e) {
            resultado.put("error", e.getMessage());
        }

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }



    @PostMapping(value = "/leer-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> leerExcel(
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> respuesta = new HashMap<>();

        if (archivo == null || archivo.isEmpty()) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Debe subir un archivo Excel");
            return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
        }

        try {
            // Leer el archivo Excel
            Workbook workbook = new XSSFWorkbook(archivo.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Primera hoja

            // Obtener cabeceras (primera fila)
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // Procesar filas de datos
            List<Map<String, Object>> datosExcel = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, Object> fila = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    switch (cell.getCellType()) {
                        case STRING:
                            fila.put(headers.get(j), cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            fila.put(headers.get(j), cell.getNumericCellValue());
                            break;
                        default:
                            fila.put(headers.get(j), "");
                    }
                }
                datosExcel.add(fila);
            }

            workbook.close();

            // Respuesta exitosa
            respuesta.put("estado", "éxito");
            respuesta.put("cabeceras", headers);
            respuesta.put("datos", datosExcel);
            respuesta.put("totalRegistros", datosExcel.size());

            return new ResponseEntity<>(respuesta, HttpStatus.OK);

        } catch (Exception e) {
            respuesta.put("estado", "error");
            respuesta.put("mensaje", "Error al leer el Excel: " + e.getMessage());
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}