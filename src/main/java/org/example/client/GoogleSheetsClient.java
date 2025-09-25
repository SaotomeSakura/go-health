package org.example.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.GoogleSheetsClientException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Initializes and provides access to the Google Sheets API client.
 * Loads credentials from the classpath and builds an authenticated {@link Sheets} service.
 * Used by repository or service layers to interact with Google Sheets.
 *
 * <p>Configuration properties:</p>
 * <ul>
 *     <li><code>google.sheets.credentials-path</code>: Path to the service account credentials file</li>
 *     <li><code>google.sheets.application-name</code>: Application name for Google Sheets API</li>
 * </ul>
 */
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class GoogleSheetsClient implements SheetsServiceProvider {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Sheets sheetsService;

    private final GoogleSheetsProperties properties;


//    @Value("${google.sheets.credentials-path:credentials.json}")
//    private String credentialsPath;
//
//    @Value("${google.sheets.application-name}")
//    private String applicationName;

    /**
     * Initializes the Google Sheets service after dependency injection.
     * Called automatically by Spring after construction.
     *
     * @throws GoogleSheetsClientException if initialization fails
     */
    @PostConstruct
    public void init() throws GoogleSheetsClientException {
        log.info("Credentials path: {}", properties.getCredentialsPath());
        log.info("Application name: {}", properties.getApplicationName());
        this.sheetsService = buildSheetsService(
                properties.getCredentialsPath(),
                properties.getApplicationName()
        );

    }


    /**
     * Builds and configures the Google Sheets API client.
     *
     * @param path the path to the credentials file
     * @param appName the application name to register with the API
     * @return an authenticated {@link Sheets} client
     * @throws GoogleSheetsClientException if client setup fails
     */
    private Sheets buildSheetsService(String path, String appName) throws GoogleSheetsClientException {
        try {
            log.info("Initializing Google Sheets client...");
            InputStream in = new ClassPathResource(path).getInputStream();

            GoogleCredential credential = GoogleCredential.fromStream(in)
                    .createScoped(List.of(SheetsScopes.SPREADSHEETS));

            return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                    .setApplicationName(appName)
                    .build();
        } catch (Exception e) {
            log.error("Google Sheets client initialization failed: {}", e.getMessage(), e);
            throw new GoogleSheetsClientException("Failed to initialize Sheets service", e);
        }

    }

    /**
     * Indicates whether the Sheets client has been successfully initialized.
     *
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return sheetsService != null;
    }

}

