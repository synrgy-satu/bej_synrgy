package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.rekening.CheckExistRequest;
import com.example.finalProject_synrgy.entity.Rekening;
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

    public String checkIfRekeningExist(CheckExistRequest req) {
        validationService.validate(req);

        Rekening rekening = rekeningRepository.findByCardNumber(req.getCardNumber());

        if(rekening == null ||
                rekening.getExpiredDateMonth().equals(req.getMonth()) ||
                rekening.getExpiredDateYear().equals(req.getYear())
        ) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card Not Found");

        if(rekening.getUser() != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Card exist, but already used by other user");

        return "Card exist";
    }

}
