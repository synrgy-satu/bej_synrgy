package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.service.BcaService;
import com.example.finalProject_synrgy.utils.BcaUtils;
import com.example.finalProject_synrgy.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
public class BcaServiceImpl implements BcaService {
    @Value("${BCA_HOST}")
    private String BCA_HOST;

    @Override
    public Object isBackAccountNumberExists(String accountNumber) {
        String endpoint = "/openapi/v1.0/balance-inquiry";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(BcaUtils.getAccessToken());
        headers.set("X-PARTNER-ID", "KBBABCINDO");
        headers.set("X-TIMESTAMP", BcaUtils.getMainSignature().getTimestamp());
        headers.set("CHANNEL-ID", "95051");
        headers.set("X-SIGNATURE", BcaUtils.getEndpointSignature(accountNumber, endpoint));

        JSONObject body = new JSONObject();
        body.put("partnerReferenceNo", "2020102900000000000001");
        body.put("accountNo", accountNumber);
        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange("https://" + BCA_HOST + endpoint,
                    HttpMethod.POST,
                    httpEntity,
                    String.class);
        } catch (Throwable t) {
            log.error(t.getMessage());

            if(t.getMessage().charAt(0) == '4') {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account number not found");
            }

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server couldn't access BCA");
        }

        return "Bank account number found";
    }
}
