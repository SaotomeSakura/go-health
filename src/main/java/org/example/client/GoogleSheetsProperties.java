package org.example.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.sheets")
@Data
public class GoogleSheetsProperties {
    private String credentialsPath;
    private String applicationName;
}

