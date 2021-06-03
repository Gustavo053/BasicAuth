package br.com.security.demo.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public String sendMessage(String message) {
        JSONObject json = new JSONObject();
        json.put("message", message);

        return json.toString();
    }
}
