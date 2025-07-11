package com.veterinaria.veterinaria_comoreyes.external.reports.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfGenerator {
    
    private final SpringTemplateEngine templateEngine;
    
    public PdfGenerator(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    public byte[] generatePdf(String templateName, Map<String, Object> data) throws IOException {
        try {
            Context context = new Context();
            context.setVariables(data);
            
            String html = templateEngine.process(templateName, context);
            
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(html);
                renderer.layout();
                renderer.createPDF(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new IOException("Error al generar PDF desde la plantilla: " + templateName, e);
        }
    }
}