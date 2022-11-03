package com.example.warehouse.impl;

import com.example.warehouse.controller.MailService;

import com.example.warehouse.model.ReturnWithMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public ReturnWithMessage sendMail(Map<String, String> request) {
        String dest = request.get("dest");
        String subject = request.get("subject");
        String mailMessage = request.get("mailMessage");
        // Set required configs
        String from = "lordmarcus0000@gmail.com";
        String to = "lordmarcus0000@gmail.com";
        String host = "smtp.gmail.com";
        String port = "587";
        String user = "lordmarcus0000@gmail.com";
        String password = "raqeukwdxulyhfxv";
        // Set system properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.password", password);
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            // Imposto indirizzo Email
            message.setFrom(new InternetAddress(from, "Warehouse"));
            // Imposto indirizzo email destinatario
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(dest));
            // Oggetto dell'email
            message.setSubject(subject);
            // Messaggio dell'email
            message.setText(mailMessage);
            // Protocollo
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            // Invio l'email
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return new ReturnWithMessage(true, "Email inviata con successo!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Errore");
        } catch (AddressException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Errore");
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            return new ReturnWithMessage(false, "Errore");
        }
       // return null;
    }

}
