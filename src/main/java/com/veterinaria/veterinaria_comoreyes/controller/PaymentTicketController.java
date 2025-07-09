package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentTicketDTO;
import com.veterinaria.veterinaria_comoreyes.service.impl.GenerateTicketService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@RestController
@RequestMapping("/api/boletas")
public class PaymentTicketController {

    @Autowired
    private GenerateTicketService boletaService;

    @GetMapping("/{paymentId}")
    public void descargarBoleta(@PathVariable Long paymentId, HttpServletResponse response) throws Exception {
        boletaService.generarPdfBoleta(paymentId, response);
    }
}