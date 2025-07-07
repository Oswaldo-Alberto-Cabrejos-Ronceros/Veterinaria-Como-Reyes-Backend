package com.veterinaria.veterinaria_comoreyes.external.email.notifications;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailAppointmentNotification {

    private final JavaMailSender mailSender;

    @Value("${app.baseUrl}") // Necesitas agregar esto en application.properties
    private String baseUrl;

    @Autowired
    public EmailAppointmentNotification(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentReminder(AppointmentNotificationDTO appointment) {
        if (appointment.getOwnerEmail() == null || appointment.getOwnerEmail().isEmpty()) {
            throw new IllegalArgumentException("Email del cliente no puede ser nulo o vacío");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("icaclinicaregional@gmail.com");
            helper.setTo(appointment.getOwnerEmail());
            helper.setSubject("✨ Recordatorio de cita - Veterinaria Como Reyes ✨");

            String htmlContent = generateHtmlContent(appointment);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }

    private String generateHtmlContent(AppointmentNotificationDTO appointment) {
        String confirmUrl = baseUrl + "/api/appointments/" + appointment.getAppointmentId() + "/confirm";

        String htmlTemplate = """
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Recordatorio de Cita - Veterinaria Como Reyes</title>
    <style>
        body {
            font-family: 'Montserrat', 'Arial', sans-serif;
            background-color: #fafafa;
            color: #555;
            margin: 0;
            padding: 0;
            line-height: 1.6;
        }
        .header {
            background: linear-gradient(135deg, #ff9a9e 0%%, #fad0c4 100%%);
            padding: 25px;
            color: white;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .logo-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 15px;
        }
        .header img {
            height: 60px;
            margin-right: 15px;
            filter: drop-shadow(0 2px 4px rgba(0,0,0,0.1));
        }
        .header h2 {
            margin: 0;
            font-weight: 300;
            letter-spacing: 1px;
        }
        .container {
            padding: 40px;
            background-color: white;
            margin: 30px auto;
            max-width: 600px;
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            border: 1px solid #f0f0f0;
        }
        .title {
            color: #ff6b88;
            font-size: 24px;
            margin-bottom: 25px;
            font-weight: 500;
            text-align: center;
            letter-spacing: 0.5px;
        }
        .info {
            line-height: 1.8;
            font-size: 15px;
        }
        .info strong {
            color: #ff6b88;
            font-weight: 500;
        }
        .divider {
            height: 1px;
            background: linear-gradient(to right, transparent, #ff9a9e, transparent);
            margin: 25px 0;
        }
        .button-container {
            text-align: center;
            margin: 30px 0;
        }
        .button {
            background: linear-gradient(to right, #ff6b88, #ff8e9e);
            color: white;
            padding: 14px 28px;
            text-decoration: none;
            border-radius: 30px;
            display: inline-block;
            font-weight: 500;
            letter-spacing: 0.5px;
            box-shadow: 0 4px 15px rgba(255,107,136,0.3);
            transition: all 0.3s ease;
        }
        .button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(255,107,136,0.4);
        }
        .footer {
            text-align: center;
            margin-top: 40px;
            font-size: 12px;
            color: #aaa;
            line-height: 1.5;
        }
        .appointment-details {
            background-color: #fff9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
            border-left: 4px solid #ff6b88;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo-container">
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnKiygKTnhtKC3f-BLIiAdN3rXWmDAHk8ZUA&s" alt="Logo">
            <h2>Clínica Veterinaria Como Reyes</h2>
        </div>
    </div>
    <div class="container">
        <div class="title">Recordatorio de Cita</div>
        <div class="info">
            Hola <strong>%s</strong>,<br><br>
            Te recordamos que tienes una cita programada con nosotros:
        </div>
        <div class="appointment-details">
            <strong>🐾 Mascota:</strong> %s<br>
            <strong>🏥 Servicio:</strong> %s<br>
            <strong>📅 Fecha:</strong> %s<br>
            <strong>⏰ Hora:</strong> %s<br>
            <strong>📍 Sede:</strong> %s<br>
        </div>
        <div class="divider"></div>
        <div class="info">
            Por favor confirma tu asistencia haciendo clic en el siguiente botón:
        </div>
        <div class="button-container">
            <a href="%s" class="button">Confirmar Asistencia</a>
        </div>
        <div class="info">
            Si no puedes asistir, te agradeceríamos que nos avises con anticipación para poder reagendar.
        </div>
        <div class="footer">
            © 2025 Clínica Veterinaria Como Reyes<br>
            <small>Todos los derechos reservados</small>
        </div>
    </div>
</body>
</html>
""";

        return String.format(
                htmlTemplate,
                appointment.getOwnerName(),
                appointment.getPetName(),
                appointment.getVetServiceName(),
                appointment.getFormattedDate(),
                appointment.getFormattedTime(),
                appointment.getHeadquarterName(),
                confirmUrl
        );
    }

}