package com.unla.grupo16.services.interfaces;

import com.unla.grupo16.models.entities.Turno;

import jakarta.mail.MessagingException;

public interface IEmailService {

    // OK
    public void sendEmail(String para, Turno turno) throws MessagingException;
}
