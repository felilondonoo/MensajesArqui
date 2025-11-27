package com.example.message.demo.controller;

import com.example.message.demo.dtos.NotificacionPagoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final JavaMailSender mailSender;

    @Value("${correo.from}")
    private String from;

    public NotificacionController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/pago-aprobado")
    public ResponseEntity<Void> notificarPagoAprobado(@RequestBody NotificacionPagoDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            System.out.println("No hay email en la notificación, no se envía correo.");
            return ResponseEntity.badRequest().build();
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(dto.getEmail());
        message.setSubject("Pago aprobado: " + (dto.getTitulo() != null ? dto.getTitulo() : "Tu compra"));

        String cuerpo = """
                Hola %s,

                Gracias por tu compra. Tu pago ha sido APROBADO.

                Detalle de tu pedido:
                - Producto/servicio: %s
                - Cantidad: %s
                - Monto BD: %s %s
                - Estado en sistema: %s

                Información de pago (Mercado Pago):
                - Payment ID: %s
                - Estado pago: %s
                - Tipo de pago: %s
                - Monto MP: %s
                - Merchant Order ID: %s
                - Preference ID: %s
                - External Reference: %s

                Conserva este correo como comprobante.
                """.formatted(
                safe(dto.getNombre()),
                safe(dto.getTitulo()),
                dto.getCantidad() != null ? dto.getCantidad() : 1,
                dto.getMonto() != null ? dto.getMonto() : "",
                safe(dto.getMoneda()),
                safe(dto.getEstado()),
                safe(dto.getPaymentId()),
                safe(dto.getPaymentStatus()),
                safe(dto.getPaymentType()),
                safe(dto.getTransactionAmountMP()),
                safe(dto.getMerchantOrderId()),
                safe(dto.getPreferenceId()),
                safe(dto.getExternalReference())
        );

        message.setText(cuerpo);

        mailSender.send(message);
        System.out.println("Correo enviado a " + dto.getEmail());

        return ResponseEntity.ok().build();
    }

    private String safe(String v) {
        return v != null ? v : "";
    }
}

