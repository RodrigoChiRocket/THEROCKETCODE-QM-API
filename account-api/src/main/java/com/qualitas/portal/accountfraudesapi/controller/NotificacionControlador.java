package com.qualitas.portal.accountfraudesapi.controller;

import com.qualitas.portal.fraudes.account.dto.NotificionDTO;
import com.qualitas.portal.fraudes.account.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionControlador {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/{id}")
    public NotificionDTO obtenerNotificacion(@PathVariable BigDecimal id) {
        return notificacionService.obtenerNotificacion(id);
    }
}
