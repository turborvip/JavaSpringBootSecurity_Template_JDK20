package com.turborvip.security.application.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

public interface GMailerService {

    Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory) throws IOException;


    void sendEmail(String to, String subject, String message) throws MessagingException, IOException;
}
