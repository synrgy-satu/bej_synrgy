package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.action.TransferReq;
import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.dto.schemas.ErrorSchema;
import com.example.finalProject_synrgy.dto.schemas.UserDataSchema;
import com.example.finalProject_synrgy.service.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(actionService.getInfoSaldo(principal), "Success Get Saldo"));
    }

    @PostMapping("transfer")
    public ResponseEntity<?> transfer(Principal principal,@RequestBody TransferReq req) {
        return ResponseEntity.ok(BaseResponse.success(actionService.transfer(principal, req), "Success Transfering Money"));
    }
}
