package kz.sitehealthtracker.site_health_tracker.utils;

import kz.sitehealthtracker.site_health_tracker.config.exception.BadRequestException;
import kz.sitehealthtracker.site_health_tracker.model.Site;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionUtil {
    private static final int CONNECTION_TIMEOUT = 5000; // 5sec

    public static HttpStatus getUrlConnectionStatus(String urlString) {
        System.out.println("Check url connection by url: " + urlString);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpURLConnection urlConnection = null;
        try {
            URL siteUrl = new URL(urlString);
            urlConnection = (HttpURLConnection) siteUrl.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            status = HttpStatus.valueOf(urlConnection.getResponseCode());
        } catch (IOException e) {
            System.out.printf("Checking url %s is failed: %s%n", urlString, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return status;
    }
}
