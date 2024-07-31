package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.auth.RegisterRequest;
import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.service.AuthService;
import com.example.finalProject_synrgy.service.BcaService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@Tag(name = "BCA")
@RestController
@RequestMapping("v1/bca")
public class BcaController {
    @Autowired
    private BcaService bcaService;

    @Operation(summary = "Cek apakah rekening valid")
    @PostMapping("/{accountNumber}")
    public ResponseEntity<?> register(@PathVariable(value = "accountNumber") String accountNumber) {
        return ResponseEntity.ok(BaseResponse.success(bcaService.isBackAccountNumberExists(accountNumber), "Success checking bank account number"));
    }
}
