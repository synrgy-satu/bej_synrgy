package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.service.BcaService;
import com.example.finalProject_synrgy.utils.BcaUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BcaServiceImpl implements BcaService {
    @Override
    public Object isBackAccountNumberExists(String accountNumber) {
        if(!BcaUtils.isBackAccountNumberExists(accountNumber)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account number not found");

        return "Bank account number found";
    }
}
