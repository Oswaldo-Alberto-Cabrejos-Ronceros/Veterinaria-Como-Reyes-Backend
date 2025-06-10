package com.veterinaria.veterinaria_comoreyes.external.mercadoPago.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import com.veterinaria.veterinaria_comoreyes.service.ICareService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class MerPagoServiceImpl implements IMerPagoService {

    @Value("${codigo.mercadoPago}")
    private String mercadoLibreToken;

    private final IAppointmentService appointmentService;
    private final ICareService careService;

    public MerPagoServiceImpl(IAppointmentService appointmentService, ICareService careService) {
        this.appointmentService = appointmentService;
        this.careService = careService;
    }


    @Override
    public String createPreference(UserBuyerDTO userBuyerDTO) {
        if (userBuyerDTO == null) {
            throw new IllegalArgumentException("JSON vacío o inválido.");
        }

        try {
            MercadoPagoConfig.setAccessToken(mercadoLibreToken);

            // Construir el item
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(userBuyerDTO.getTitle())
                    .quantity(userBuyerDTO.getQuantity())
                    .unitPrice(BigDecimal.valueOf(userBuyerDTO.getUnitPrice()))
                    .currencyId("PEN")
                    .build();

            // Construir URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://tu-sitio/success")
                    .pending("https://tu-sitio/pending")
                    .failure("https://tu-sitio/failure")
                    .build();

            // Construir la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(itemRequest))
                    .backUrls(backUrls)
                    .externalReference(String.valueOf(userBuyerDTO.getIdOrderPayment()))
                    .build();

            // Crear la preferencia
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getInitPoint();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la preferencia: " + e.getMessage(), e);
        }
    }

    @Override
    public String createPreferenceByIdAppointment(Long idAppointment) {
        UserBuyerDTO userBuyerDTO = appointmentService.getInfoForPaymentMerPago(idAppointment);
        return createPreference(userBuyerDTO);
    }

    @Override
    public String createPreferenceByIdCare(Long idCare) {
        UserBuyerDTO userBuyerDTO = careService.getInfoForPaymentMerPago(idCare);
        return createPreference(userBuyerDTO);
    }
}
