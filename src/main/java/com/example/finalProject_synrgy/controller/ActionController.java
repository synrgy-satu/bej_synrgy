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

    @Operation(summary = "Get Current user", description = "Gunakan autentikasi bearer untuk mendapatkan detail data user yang terautentikasi")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDataSchema.class))},
                    description = "Berhasil mengambil data user yang terautentikasi"
            ),
            @ApiResponse(responseCode = "401",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSchema.class))},
                    description = "Access token salah"
            )
    })
    @GetMapping("/checkbalance")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(actionService.getInfoSaldo(principal), "Success Get Saldo"));
    }

    @PostMapping("transfer")
    public ResponseEntity<?> tranfer(Principal principal,@RequestBody TransferReq req) {
        return ResponseEntity.ok(BaseResponse.success(actionService.transfer(principal, req), "Success Transfering Money"));
    }
}
