package com.unla.grupo16.configurations.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    // configura el sv SMTP de gmail
    // para enviar mails desde la aplicacion
    // como y donde se conecta
    
    @Value("${MAIL_USERNAME}")
    private String username;

    @Value("${MAIL_PASSWORD}")
    private String password;

    @Bean
    public JavaMailSenderImpl enviarEmail() {
        
        // creo instancia de JavaMailSenderImpl
        JavaMailSenderImpl servicioCorreo = new JavaMailSenderImpl();
        // puerto y host para gmail
        servicioCorreo.setHost("smtp.gmail.com");
        servicioCorreo.setPort(587);
        // usa la pw de la cuenta de gmail
        servicioCorreo.setUsername(username);
        servicioCorreo.setPassword(password);

        // setea las propiedades de javamail 
        Properties props = servicioCorreo.getJavaMailProperties();
        // habilitar auth SMTP
        props.put("mail.smtp.auth", "true");
        // habilitar STARTTLS
        props.put("mail.smtp.starttls.enable", "true");
        // habilitar trust para el sv gmail
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // habilitar starttls obligatorio
        props.put("mail.smtp.starttls.required", "true");

        return servicioCorreo;
    }
}
