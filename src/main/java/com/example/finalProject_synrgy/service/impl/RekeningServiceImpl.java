package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.rekening.RekeningCheckRequest;
import com.example.finalProject_synrgy.dto.rekening.RekeningCreateRequest;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import com.example.finalProject_synrgy.repository.RekeningRepository;
import com.example.finalProject_synrgy.service.RekeningService;
import com.example.finalProject_synrgy.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Service
public class RekeningServiceImpl implements RekeningService {

    @Autowired
    RekeningRepository rekeningRepository;

    @Autowired
    ValidationService validationService;

    public String checkIfRekeningExist(RekeningCheckRequest req) {
        validationService.validate(req);

        Rekening rekening = rekeningRepository.findByCardNumber(req.getCardNumber());

        if(rekening == null || rekening.getExpiredDateMonth() != req.getMonth() || rekening.getExpiredDateYear() != req.getYear()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card Not Found");

        if(rekening.getUser() != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Card exist, but already used by other user");

        return "Card exist";
    }

    public Rekening create(RekeningCreateRequest req) {
        validationService.validate(req);
        Rekening rekening = new Rekening();

        if(rekeningRepository.existsByCardNumber(req.getCardNumber())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card already exist");
        rekening.setCardNumber(req.getCardNumber());
        Random random = new Random();
        Long generatedRekNum;
        do {
            generatedRekNum = 1111111100L + 1 + random.nextInt(99);
        } while (rekeningRepository.existsByRekeningNumber(generatedRekNum));
        rekening.setRekeningNumber(generatedRekNum);
        rekening.setExpiredDateMonth(req.getMonth());
        rekening.setExpiredDateYear(req.getYear());
        rekening.setJenisRekening(req.getJenisRekening());
        rekening.setBalance(req.getBalance());

        return rekeningRepository.save(rekening);
    }

    public Object read() {
        return rekeningRepository.findAll();
    }

}
