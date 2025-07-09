package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentTicketDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Care;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.HeadquarterVetService;
import com.veterinaria.veterinaria_comoreyes.entity.Payment;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.repository.PaymentRepository;
import com.veterinaria.veterinaria_comoreyes.util.NumberToLetterConverter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GenerateTicketService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TemplateEngine templateEngine;

    public PaymentTicketDTO generarDatosBoleta(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

        Care care = payment.getCare();
        Client client = care.getAnimal().getClient();
        HeadquarterVetService hvs = care.getHeadquarterVetService();

        // Usamos BigDecimal para precisión en cálculos monetarios
        BigDecimal precioServicio = BigDecimal.valueOf(hvs.getVeterinaryService().getPrice());
        BigDecimal subtotal = precioServicio.divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
        BigDecimal igv = subtotal.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igv);

        // Format the payment ID with leading zeros
        String formattedPaymentId = String.format("%06d", paymentId);
        String numeroBoleta = "000-" + formattedPaymentId;

        return new PaymentTicketDTO(
                numeroBoleta,
                payment.getPaymentDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                client.getName() + " " + client.getLastName(),
                client.getDni(),
                care.getAnimal().getName(),
                care.getAnimal().getBreed().getSpecie().getName(),
                care.getAnimal().getBreed().getName(),
                hvs.getVeterinaryService().getName(),
                precioServicio.doubleValue(),
                subtotal.doubleValue(),
                igv.doubleValue(),
                total.doubleValue(),
                NumberToLetterConverter.convert(total.doubleValue()),
                payment.getPaymentMethod().getName(),
                care.getEmployee().getName(),
                hvs.getHeadquarter().getAddress(),
                hvs.getHeadquarter().getPhone(),
                "20123456789");
    }

    public void generarPdfBoleta(Long paymentId, HttpServletResponse response) throws Exception {
        PaymentTicketDTO boleta = generarDatosBoleta(paymentId);

        Context context = new Context();
        context.setVariable("ticket", boleta);

        String html = templateEngine.process("boleta", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=boleta_" + boleta.numeroBoleta() + ".pdf");

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(response.getOutputStream());
    }
}