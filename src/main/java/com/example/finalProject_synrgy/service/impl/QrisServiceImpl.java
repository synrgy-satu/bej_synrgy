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
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@Service
public class QrisServiceImpl implements QrisService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private QrisRepository qrisRepository;

    @Value("${cloud.static.directory.qris}")
    private String qrisFolder;

    @Override
    public Object generateQris(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(user.getRekenings().get(0).getRekeningNumber().toString()+","+user.getFullName(),
                    BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToPath(bitMatrix, "png",
                    Paths.get((SystemUtils.IS_OS_LINUX ? qrisFolder : "./data")+"/"+user.getUsername()+".png"));
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
        qris.setImageUrl("https://satu.cekrek.shop/qris/"+qris.getId().toString()+".png");
        qris.setOwner(rekening.getName());
        qris.setActive(true);
        Random random = new Random();
        Long generatedNmid;
        do {
            generatedNmid = 10000000L + random.nextInt(99999999);
        } while (qrisRepository.existsByNmid(generatedNmid));
        qris.setNmid(generatedNmid);

        Calendar date = Calendar.getInstance();
        date.add(Calendar.YEAR, 1);
        qris.setActiveUntil(date.getTime());

        qris = qrisRepository.save(qris);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(qris.getEncodedQrCode(),
                    BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToPath(bitMatrix, "png",
                    Paths.get((SystemUtils.IS_OS_LINUX ? qrisFolder : "./data")+"/"+qris.getEncodedQrCode()+".png"));
        } catch (Throwable t) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, t.getMessage());
        }

        return qris;
    }

    public Object readQrisList(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        List<Qris> qrises = new ArrayList<>();

        user.getRekenings().forEach(rekening -> {
            qrises.add(rekening.getQris());
        });

        return qrises;
    }

    public Object readQris(String id) {
        Optional<Qris> qrisOptional = qrisRepository.findById(UUID.fromString(id));
        if(!qrisOptional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "QRIS doesn't exist");
        Qris qris = qrisOptional.get();
        qris.setOwner(qris.getRekening().getUser().getFullName());

        return qris;
    }

    public Object activate(Principal principal, String id, boolean condition) {
        Optional<Qris> qrisOptional = qrisRepository.findById(UUID.fromString(id));
        if(!qrisOptional.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "QRIS doesn't exist");
        Qris qris = qrisOptional.get();

        if(!userRepository.findByUsername(principal.getName()).equals(qris.getRekening().getUser()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This qris does not belongs to authenticated user");

        qris.setActive(condition);

        return qrisRepository.save(qris);
    }
}
