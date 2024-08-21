package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Notification")
@RestController
@RequestMapping("v1/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("{size}/{pagenum}")
    public ResponseEntity<?> checkBalance(Principal principal, @PathVariable int size, @PathVariable int pagenum) {
        return ResponseEntity.ok(BaseResponse.success(notificationService.get(principal, size, pagenum), "Success Get Balance"));
    }
}
