package kz.sitehealthtrackerbackend.site_health_tracker_backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpConnectionUtil {
    private static final int CONNECTION_TIMEOUT = 5000; // 5sec

    public static HttpStatus getUrlConnectionStatus(String urlString) {
        log.info("Проверка url: {}", urlString);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpURLConnection urlConnection = null;
        try {
            URL siteUrl = new URL(urlString);
            urlConnection = (HttpURLConnection) siteUrl.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            status = HttpStatus.valueOf(urlConnection.getResponseCode());
        } catch (IOException e) {
            log.error("Проверка url {} выбросило исключение:", urlString, e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        log.info("Конец проверки url {}, статус: {}", urlString, status);
        return status;
    }
}
