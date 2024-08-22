package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.entity.Qris;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.oauth2.User;
import com.example.finalProject_synrgy.repository.QrisRepository;
import com.example.finalProject_synrgy.repository.UserRepository;
import com.example.finalProject_synrgy.service.QrisService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Service
public class QrisServiceImpl implements QrisService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private QrisRepository qrisRepository;

    @Override
    public Object generateQris(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(user.getRekenings().get(0).getRekeningNumber().toString()+","+user.getFullName(),
                    BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToPath(bitMatrix, "png",
                    Paths.get((SystemUtils.IS_OS_LINUX ? "" : ".")+"/data/"+user.getUsername()+".png"));
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, t.getMessage());
        }

        return null;
    }

    @Override
    public Qris generateQrisRekening(Rekening rekening) {
        Qris qris = new Qris();

        qris.setName("QRIS " + rekening.getName());
        qris.setRekening(rekening);
        qris = qrisRepository.save(qris);
        qris.setEncodedQrCode(qris.getId().toString());
        qris.setImageUrl("https://satu.cekrek.shop/static/"+qris.getId().toString()+".png");
        qris = qrisRepository.save(qris);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(qris.getEncodedQrCode(),
                    BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToPath(bitMatrix, "png",
                    Paths.get((SystemUtils.IS_OS_LINUX ? "~/static" : ".")+"/qris/"+qris.getEncodedQrCode()+".png"));
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, t.getMessage());
        }

        return qris;
    }
}
