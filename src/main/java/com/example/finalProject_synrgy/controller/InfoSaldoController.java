package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.infosaldo.InfoSaldoRequest;
import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.service.InfoSaldoService;
import com.example.finalProject_synrgy.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/amount")

public class InfoSaldoController {
    private final InfoSaldoService infoSaldoService;

    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public ResponseEntity<?> getInfoSaldo(
            @Valid
            @RequestHeader(name = "Authorization") String token
    ) {

        String jwt = token.substring("Bearer ".length());
        String username = jwtUtil.getUsername(jwt);
        return ResponseEntity.ok(BaseResponse.success(infoSaldoService.getInfoSaldo(username), "Nomor Rekening ditemukan"));
    }



}
