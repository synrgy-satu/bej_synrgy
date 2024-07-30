package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.rekening.RekeningCheckRequest;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import com.example.finalProject_synrgy.repository.RekeningRepository;
import com.example.finalProject_synrgy.service.RekeningService;
import com.example.finalProject_synrgy.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Rekening create(RekeningCheckRequest req) {
        validationService.validate(req);
        Rekening rekening = new Rekening();

        if(rekeningRepository.existsByCardNumber(req.getCardNumber())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card already exist");
        rekening.setCardNumber(req.getCardNumber());
        rekening.setExpiredDateMonth(req.getMonth());
        rekening.setExpiredDateYear(req.getYear());
        rekening.setJenisRekening(JenisRekening.DEBIT);
        rekening.setBalance(4200000);

        return rekeningRepository.save(rekening);
    }

    public Object read() {
        return rekeningRepository.findAll();
    }

}
