package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.service.QrisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Qris")
@RestController
@RequestMapping("v1/qris")
public class QrisController {
    @Autowired
    QrisService qrisService;

    @GetMapping("list")
    public ResponseEntity<?> getQrisList(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.readQrisList(principal), "Succes get Qris list"));
    }

    @GetMapping("{qrOrId}")
    public ResponseEntity<?> getQrisList(@PathVariable String qrOrId) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.readQris(qrOrId), "Succes get Qris information"));
    }

    @GetMapping("activate/{id}/{condition}")
    public ResponseEntity<?> getQrisList(Principal principal, String id,@PathVariable boolean condition) {
        return ResponseEntity.ok(BaseResponse.success(qrisService.activate(principal, id, condition), "Succes set Qris status"));
    }
}
