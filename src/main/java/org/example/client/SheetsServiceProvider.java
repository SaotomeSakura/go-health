package org.example.client;

import com.google.api.services.sheets.v4.Sheets;

public interface SheetsServiceProvider {
    Sheets getSheetsService();
}

