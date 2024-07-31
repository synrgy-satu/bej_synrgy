package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.infosaldo.InfoSaldoRequest;
import com.example.finalProject_synrgy.dto.infosaldo.InfoSaldoResponse;

public interface InfoSaldoService {
    InfoSaldoResponse getInfoSaldo(String username);
}
