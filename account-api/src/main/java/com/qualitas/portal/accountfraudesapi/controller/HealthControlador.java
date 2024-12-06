package com.qualitas.portal.accountfraudesapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthControlador {

        @RequestMapping("/check")
        public String check() {
            return "OK";
        }
}
