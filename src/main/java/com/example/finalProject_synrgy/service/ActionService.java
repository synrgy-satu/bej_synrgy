package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.action.PayQrisReq;
import com.example.finalProject_synrgy.dto.action.TransferReq;
import com.example.finalProject_synrgy.dto.infosaldo.InfoSaldoResponse;
import com.example.finalProject_synrgy.entity.Qris;

import java.security.Principal;

public interface ActionService {
    Object getInfoSaldo(Principal principal);

    Object transfer(Principal principal, TransferReq req);

    Object payQris(Principal principal, PayQrisReq req);

    Qris checkQris(String targetQris);
}
