package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.UserRequest;
import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.dto.rekening.CheckExistRequest;
import com.example.finalProject_synrgy.dto.schemas.NullDataSchema;
import com.example.finalProject_synrgy.dto.schemas.StringDataSchema;
import com.example.finalProject_synrgy.dto.schemas.UserDataSchema;
import com.example.finalProject_synrgy.service.RekeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/card")
public class RekeningController {

    @Autowired
    RekeningService rekeningService;

    @Operation(summary = "Cek apakah kartu valid")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StringDataSchema.class))},
                    description = "Berhasil cek"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
            @ApiResponse(responseCode = "409",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kartu sudah terhubung dengan user lain"
            )
    })
    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody CheckExistRequest req) {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.checkIfRekeningExist(req), "Success checking card"));
    }

    @Operation(summary = "Membuat kartu (hanya untuk debugging)")
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CheckExistRequest req) {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.create(req), "Success Create Card"));
    }
}
