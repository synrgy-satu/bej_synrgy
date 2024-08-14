package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.dto.rekening.RekeningCheckRequest;
import com.example.finalProject_synrgy.dto.rekening.RekeningCreateRequest;
import com.example.finalProject_synrgy.dto.schemas.NullDataSchema;
import com.example.finalProject_synrgy.dto.schemas.StringDataSchema;
import com.example.finalProject_synrgy.service.RekeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rekening")
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
            @ApiResponse(responseCode = "404",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kartu tidak ditemukan"
            ),
            @ApiResponse(responseCode = "409",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kartu sudah terhubung dengan user lain"
            )
    })
    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody RekeningCheckRequest req) {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.checkIfRekeningExist(req), "Success checking card"));
    }

    @Operation(summary = "Membuat kartu (hanya untuk debugging)", description = "Jenis rekening (SAVER/PRIORITAS/EDU)")
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody RekeningCreateRequest req) {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.create(req), "Success Create Card"));
    }

    @Operation(summary = "Membuat kartu random (hanya untuk debugging)", description = "Jenis rekening (SAVER/PRIORITAS/EDU)")
    @PostMapping("random")
    public ResponseEntity<?> createRandom() {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.createRandom(), "Success Create Card"));
    }

    @Operation(summary = "Lihat kartu terdaftar (hanya untuk debugging)")
    @GetMapping()
    public ResponseEntity<?> read() {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.read(), "Success Read Card"));
    }

    @Operation(summary = "Lihat informasi rekening")
    @GetMapping("{rekeningNumber}")
    public ResponseEntity<?> getCardName(@PathVariable Long rekeningNumber) {
        return ResponseEntity.ok(BaseResponse.success(rekeningService.readOne(rekeningNumber), "Success Read Card"));
    }
}
