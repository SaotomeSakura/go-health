package org.example.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.core.io.ClassPathResource;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheetsClient {

    private static final String APPLICATION_NAME = "Ticket Tracker";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        InputStream in = new ClassPathResource("credentials.json").getInputStream();

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(List.of(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

    }
}

