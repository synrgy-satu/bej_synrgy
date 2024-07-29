package com.example.finalProject_synrgy.controller;

import com.example.finalProject_synrgy.dto.schemas.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.finalProject_synrgy.dto.auth.*;
import com.example.finalProject_synrgy.dto.base.BaseResponse;
import com.example.finalProject_synrgy.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Tag(name = "Auth")
@RestController
@RequestMapping("v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Register user baru",description = "Mendaftarkan user baru, pastikan tidak ada field duplikasi dengan user yang sudah ada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDataSchema.class))},
                    description = "User berhasil terdaftar"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            )
    })
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.register(request), "Success Register User"));
    }

    @Operation(summary = "Dapatkan access token", description = "Dapatkan access token yang dapat digunakan dengan autentikasi bertipe 'Bearer'")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginSuccess.class))},
                    description = "Access token sudah terbuat"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Email tidak terdaftar"
            )
    })
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.login(request), "Success Login User"));
    }

//    @Hidden
//    @PostMapping("login/google")
//    public ResponseEntity<?> loginWithGoogle(@RequestParam MultiValueMap<String, String> parameters) throws IOException {
//        return ResponseEntity.ok(BaseResponse.success(authService.signWithGoogle(parameters), "Success Sign Google"));
//    }

//    @Hidden
//    @PostMapping("register/google")
//    public ResponseEntity<?> registerWithGoogle(@RequestBody RegisterRequest request) {
//        authService.register(request);
//        Object res = authService.sendEmailOtp(new EmailRequest(request.getEmailAddress()), "Register");
//        return ResponseEntity.ok(BaseResponse.success(res, "Success Register by Google"));
//    }

    @Operation(summary = "OTP", description = "Masukkan email untuk meminta otp")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StringDataSchema.class))},
                    description = "OTP terkirim"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Email tidak terdaftar"
            )
    })
    @PostMapping("otp")
    public ResponseEntity<?> sendEmailOtp(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.sendEmailOtp(req, "Register"), "Success Send OTP"));
    }

    @Operation(summary = "Aktivasi user", description = "Masukkan otp untuk aktivasi user")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StringDataSchema.class))},
                    description = "User aktif"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "OTP tidak terdaftar"
            )
    })
    @GetMapping("otp/{token}")
    public ResponseEntity<?> confirmOtp(@PathVariable(value = "token") String tokenOtp) {
        return ResponseEntity.ok(BaseResponse.success(authService.confirmOtp(tokenOtp), "Success Send OTP"));
    }

    @Operation(summary = "Forget password", description = "Masukkan email untuk meminta otp yang digunakan untuk mereset password")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StringDataSchema.class))},
                    description = "OTP terkirim"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Email tidak terdaftar"
            )
    })
    @PostMapping("password")
    public ResponseEntity<?> sendEmailForgetPassword(@RequestBody EmailRequest req) {
        return ResponseEntity.ok(BaseResponse.success(authService.sendEmailOtp(req, "Forget Password"), "Success Send OTP Forget Password"));
    }

    @Operation(summary = "Cek apakah otp valid")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StringDataSchema.class))},
                    description = "OTP valid"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "OTP tidak terdaftar"
            )
    })
    @PostMapping("password/otp")
    public ResponseEntity<?> checkValidOtp(@RequestBody OtpRequest otp) {
        return ResponseEntity.ok(BaseResponse.success(authService.checkOtpValid(otp), "Success Validate OTP"));
    }

    @Operation(summary = "Reset password", description = "Masukkan fields yang diminta untuk mengganti password")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StringDataSchema.class))},
                    description = "Berhasil reset password"
            ),
            @ApiResponse(responseCode = "400",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NullDataSchema.class))},
                    description = "Kesalahan dalam request, lihat message untuk rincian"
            ),
    })
    @PutMapping("password")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(BaseResponse.success(authService.resetPassword(request), "Success Reset Password"));
    }


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
    @GetMapping
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(authService.getCurrentUser(principal), "Success Get Current User Login"));
    }
}
