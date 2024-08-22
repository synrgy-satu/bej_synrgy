package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.rekening.BriefInformationResponse;
import com.example.finalProject_synrgy.dto.rekening.RekeningCheckRequest;
import com.example.finalProject_synrgy.dto.rekening.RekeningCreateRequest;
import com.example.finalProject_synrgy.entity.Qris;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.enums.JenisRekening;
import com.example.finalProject_synrgy.repository.RekeningRepository;
import com.example.finalProject_synrgy.service.QrisService;
import com.example.finalProject_synrgy.service.RekeningService;
import com.example.finalProject_synrgy.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Random;

@Service
public class RekeningServiceImpl implements RekeningService {

    @Autowired
    RekeningRepository rekeningRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    QrisService qrisService;

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
        rekening.setName(req.getName().trim().replaceAll("[0-9]", "").toUpperCase());

        if(req.getYear() <= Calendar.getInstance().get(Calendar.YEAR)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please put a future year");
        rekening.setExpiredDateYear(req.getYear());
        rekening.setExpiredDateMonth(req.getMonth());

        rekening.setJenisRekening(req.getJenisRekening());
        rekening.setBalance(req.getBalance());

        rekening = rekeningRepository.save(rekening);
        Qris qris = qrisService.generateQrisRekening(rekening);
        rekening.setQris(qris);

        return rekeningRepository.save(rekening);
    }

    public Rekening createRandom() {
        Rekening rekening = new Rekening();

        Random random = new Random();

        Long generatedCardNum;
        do {
            generatedCardNum = 4444444444440000L + random.nextInt(9999);
        } while (rekeningRepository.existsByRekeningNumber(generatedCardNum));
        rekening.setCardNumber(generatedCardNum);

        Long generatedRekNum;
        do {
            generatedRekNum = 4444444400L + random.nextInt(99);
        } while (rekeningRepository.existsByRekeningNumber(generatedRekNum));
        rekening.setRekeningNumber(generatedRekNum);

        String[] firstName = {
                "Arif",
                "Budi",
                "Dian",
                "Eko",
                "Fajar",
                "Gita",
                "Hadi",
                "Indra",
                "Joko",
                "Lestari"
        };

        String[] lastName = {
                "Pratama",
                "Wibowo",
                "Setiawan",
                "Nugroho",
                "Putri",
                "Sari",
                "Santoso",
                "Susanto",
                "Yulianto",
                "Wijaya"
        };

        String generatedName = firstName[random.nextInt(10)] + " " + lastName[random.nextInt(10)];
        rekening.setName(generatedName.trim().replaceAll("[0-9]", "").toUpperCase());

        rekening.setExpiredDateYear(Calendar.getInstance().get(Calendar.YEAR) + 1 + random.nextInt(5));
        rekening.setExpiredDateMonth(1 + random.nextInt(12));

        rekening.setJenisRekening(JenisRekening.values()[random.nextInt(JenisRekening.values().length)]);
        rekening.setBalance(random.nextInt(11) * 100000);

        rekening = rekeningRepository.save(rekening);
        Qris qris = qrisService.generateQrisRekening(rekening);
        rekening.setQris(qris);

        return rekeningRepository.save(rekening);
    }

    public Object read() {
        return rekeningRepository.findAll();
    }

    public Object readOne(Long rekeningNumber) {
        BriefInformationResponse res = new BriefInformationResponse();

        Rekening rekening = rekeningRepository.findByRekeningNumber(rekeningNumber);

        if(rekening == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rekening not found");

        res.setRekeningNumber(rekening.getRekeningNumber());

        if(rekening.getUser() == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user associated with this rekening");
        res.setName(rekening.getUser().getUsername());

        return res;
    }

}
