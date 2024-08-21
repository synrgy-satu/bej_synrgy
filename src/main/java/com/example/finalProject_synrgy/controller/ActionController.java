package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.action.PayQrisReq;
import com.example.finalProject_synrgy.dto.action.TransferReq;
import com.example.finalProject_synrgy.dto.action.AddCardReq;
import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.service.ActionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Action")
@RestController
@RequestMapping("v1/action")
public class ActionController {
    @Autowired
    ActionService actionService;

    @GetMapping("/checkbalance")
    public ResponseEntity<?> checkBalance(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(actionService.getInfoSaldo(principal), "Success Get Balance"));
    }

    @GetMapping("/checkbalance/total")
    public ResponseEntity<?> checkBalanceTotal(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(actionService.getInfoSaldoTotal(principal), "Success Get Balance Total"));
    }

    @PostMapping("transfer")
    public ResponseEntity<?> transfer(Principal principal,@RequestBody TransferReq req) {
        return ResponseEntity.ok(BaseResponse.success(actionService.transfer(principal, req), "Success Transfering Money"));
    }

    @PostMapping("addcard")
    public ResponseEntity<?> addCard(Principal principal,@RequestBody AddCardReq req) {
        return ResponseEntity.ok(BaseResponse.success(actionService.addCard(principal, req), "Success adding card"));
    }

    @GetMapping("checkqris/{qrisCode}")
    public ResponseEntity<?> getQrisInformation(@PathVariable String qrisCode) {
        return ResponseEntity.ok(BaseResponse.success(actionService.checkQris(qrisCode), "Succes get Qris information"));
    }

    @PostMapping("bayarqris")
    public ResponseEntity<?> getQrisInformation(Principal principal, @RequestBody PayQrisReq req) {
        return ResponseEntity.ok(BaseResponse.success(actionService.payQris(principal, req), "Succes Paying Qris"));
    }
}
