package com.example.finalProject_synrgy.utils;

import com.example.finalProject_synrgy.dto.bca.BcaAccessToken;
import com.example.finalProject_synrgy.dto.bca.BcaSignature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
public class BcaUtils {
    private static String BCA_HOST = "sandbox.bca.co.id";

    private static String BCA_X_CLIENT_KEY = "a350eb10-401e-40bd-abaf-d9c77005595f";

    private static String BCA_X_CLIENT_SECRET = "58b10a4c-7b99-4085-b70c-1413034eca9f";

    private static String BCA_PRIVATE_KEY = "-----BEGIN PRIVATE KEY----- MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCe5ktDYufsqjv8HH78fzloRa6DjuDMMD8YxVCc3uQYOPxN2IWb2cNs/pG3NnO6cfuqnEvUgvougsUMhVUVl1n670pA2B2eiOQdsYPNxZsSUVPHUjg1ZHSWn5yNxMZ9MIZv1DhZVzdiRMtAw66jTyF9BleCcjq/oW3uHqKWGtepOJVHNtgLz9MGJTzEXb0uEOOK0uCuJK+VMMqKXlUhMZur2Bah99D6vuMnmpmCnNQEvVj4MqhFQHD4uzW6qq2IevwHVbk8wPOq1HByftBQW/fowdtJKfSRaUf6dShHBrdcHDWlu+5TOB7LcbUY7oKFDk8NGdrHQiRaKlgLfU2BGHVVAgMBAAECggEAUseq4fo+1N6CzX6S8TveTmIu3j6rAfUIigERVAgSUEQvvOZWBLFXzAp7IzVs6O7Eq0ctghKR/3UE7tbvUoY8zCupRUrRc2vhW07FWYfel5ZizO4adkZVLrsMNhcTSNjk0JGAoZp8Meeg86Z97nok+hs5r62OyZJx0KGJFiX5wB/ziNwBlrc5asxGk4DhwGsSILrpi+3/xTdTeUu1kAnSREHSEgunGW+VFE/lGXiG8gd254m6MW8nRlaKnSODlZd3+YRKbbRUAaZ/GyTdwMZZrptn9H+ZSRMl0Z9sS0pe8LmJM5eqiCjqCLaxeJ/nEQeJm5x3L1JQpGsnGVfZjDZowQKBgQDrevLyjZzFFJisTMBV1pe+KQuP0hF9SJqgzOXeMMMxnM93kC4obimgAcmJmiHUnWWLt1UGyut96kUZIkdZg3rj/rBrk2uKrnyrgdbhEtPtfWGJFSItLfcZcTBr+VoGWqqKEMhMFcVqxFTA8sY3Xzv6vzcrPtGtX0Q9PEj1Qo9A2QKBgQCsvv89kz98PYl+qx2oHZEXQcyEeGI6ibfDq98EYpoYjcflNSpzQm/QHNtLQLiJ9t5TW1gi/cNry24FiJTADw4O/6k3wIbuAIm7HsjUVwJYoeTtn98Q2Ymsksv4yu7K/F8qXb6ELIlYlXOV19ZERcJLKbbQ2yl88Ww7+i0reu0K3QKBgAn/45c3OkQINt+CNtyuSy1REuOdmQ6H6cEQUmaYDYHq1ciO/9bJrszTpppISE1+DZTcSSkLrupe62ZA1WTQt4Q9CYLX9MYj2Llzvws5wHQiUeT/V78xZ3/WFadQJGmGqh1Izyij+Akroym6ZX5udd6VBiO4/DBvjjdHexWnKOwpAoGBAJ5/0OnKhWGVhOa4UsnB9zKDqQeS/W4Alp/uvv3jCsikrljcY0rGFpm5IGz3wVq1LGEHWuMgO4JYcWaaXwGpzphsc/M3r5YI4FbUdCiAfSKdyNNO8Pkg4HV7a7OnX1rYHOlegkP8KTkiR5+hHnQeHZuhdqBDttlxGoIdlfxjGcPxAoGBALSkWh2WzX+rt5Q8jA2nPoj01Je2KXt1NkwWVddaYhI4M65Wsdi2qvi5nradXzOs2p2E/VuoJ0nDotPPxrzdygZ3at0teyGlYEAnfIJPacA3G9FD73smM/2CkVAbaMVt6kNZuVFypnEhMbf6A8SKbH0/N4QBo0GBAXH7OmA0hkSp -----END PRIVATE KEY-----";

    private static BcaSignature mainSignature = null;

    private static BcaAccessToken accessToken = null;

    public static BcaSignature getMainSignature() {
        if(mainSignature != null) return mainSignature;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CLIENT-KEY", BCA_X_CLIENT_KEY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String timestamp = ZonedDateTime.now().format(formatter);
        headers.set("X-TIMESTAMP", timestamp);
        headers.set("Private_Key", BCA_PRIVATE_KEY);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("https://"+ BCA_HOST +"/api/v1/utilities/signature-auth",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Optional<String> signature = JsonUtils.GetValue(response.getBody(), "signature");

        signature.ifPresent(s -> mainSignature = new BcaSignature(timestamp, s));

        return mainSignature;
    }

    public static String getAccessToken() {
        if(accessToken != null && accessToken.getExpired_date().isAfter(ZonedDateTime.now())) return accessToken.getAccessToken();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-CLIENT-KEY", BCA_X_CLIENT_KEY);
        headers.set("X-TIMESTAMP", getMainSignature().getTimestamp());
        headers.set("X-SIGNATURE", getMainSignature().getSignature());

        JSONObject body = new JSONObject();
        body.put("grantType", "client_credentials");

        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange("https://"+ BCA_HOST +"/openapi/v1.0/access-token/b2b",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Optional<String> _accessToken = JsonUtils.GetValue(response.getBody(), "accessToken");

        _accessToken.ifPresent(s -> accessToken = new BcaAccessToken(_accessToken.get(), ZonedDateTime.now().plusSeconds(900)));

        return accessToken.getAccessToken();
    }

    public static Boolean isBackAccountNumberExists(String accountNumber) {
        String endpoint = "/openapi/v1.0/balance-inquiry";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());
        headers.set("X-PARTNER-ID", "KBBABCINDO");
        headers.set("X-TIMESTAMP", getMainSignature().getTimestamp());
        headers.set("CHANNEL-ID", "95051");
        headers.set("X-SIGNATURE", getEndpointSignature(accountNumber, endpoint));

        JSONObject body = new JSONObject();
        body.put("partnerReferenceNo", "2020102900000000000001");
        body.put("accountNo", accountNumber);
        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

        log.info(httpEntity.toString());

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange("https://" + BCA_HOST + endpoint,
                    HttpMethod.POST,
                    httpEntity,
                    String.class);
        } catch (Throwable t) {
            log.info(t.getMessage());
            return false;
        }
        Optional<String> responseMessage = JsonUtils.GetValue(response.getBody(), "responseMessage");
        return responseMessage.get().equals("Successful");
    }

    public static String getEndpointSignature(String accountNumber, String endpoint) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("accesstoken", getAccessToken());
        headers.set("HttpMethod", "POST");
        headers.set("X-TIMESTAMP", getMainSignature().getTimestamp());
        headers.set("X-CLIENT-SECRET", BCA_X_CLIENT_SECRET);
        headers.set("EndpoinUrl", endpoint);

        JSONObject body = new JSONObject();
        body.put("partnerReferenceNo", "2020102900000000000001");
        body.put("accountNo", accountNumber);

        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange("https://"+ BCA_HOST +"/api/v1/utilities/signature-service",
                HttpMethod.POST,
                httpEntity,
                String.class);

        Optional<String> signature = JsonUtils.GetValue(response.getBody(), "signature");

        return signature.orElse(null);
    }
}
