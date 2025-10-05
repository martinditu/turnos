package com.unla.grupo16.services.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.unla.grupo16.models.entities.Turno;
import com.unla.grupo16.services.interfaces.IEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    // OK
    @Override
    public void sendEmail(String para, Turno turno) throws MessagingException {

        Context context = new Context();
        context.setVariable("cliente", turno.getCliente());
        context.setVariable("servicio", turno.getServicio());
        context.setVariable("turno", turno);

        String htmlContent = templateEngine.process("email/email-template.html", context);

        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(para);
        helper.setSubject("Confirmacion de turno - " + turno.getServicio().getNombre());
        helper.setText(htmlContent, true);

        javaMailSender.send(mensaje);
    }
}
