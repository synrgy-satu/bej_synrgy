package com.example.finalProject_synrgy.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> GetValue(String json, String key) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode specificVariableNode = rootNode.get(key);
            if (specificVariableNode != null) {
                return Optional.ofNullable(specificVariableNode.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
