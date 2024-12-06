package com.qualitas.portal.accountfraudesapi.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class Response {

    private HttpStatus status;
    private final HashMap<String, Object> objetoRespuesta;

    public Response(){
        objetoRespuesta = new HashMap<>();
    }

    public Response codigoRespuesta(HttpStatus status){
        this.status = status;
        return this;
    }

    public Response agregarAtributo(String nombre, Object objeto){
        objetoRespuesta.putIfAbsent(nombre, objeto);
        return this;
    }

    public static Response crearRespuesta(){
        return new Response();
    }

    public ResponseEntity<HashMap<String, Object>> crear(){
        return ResponseEntity.status(status).body(objetoRespuesta);
    }

}
