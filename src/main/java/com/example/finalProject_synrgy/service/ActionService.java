package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.action.TransferReq;
import com.example.finalProject_synrgy.dto.infosaldo.InfoSaldoResponse;

import java.security.Principal;

public interface ActionService {
    Object getInfoSaldo(Principal principal);

    Object transfer(Principal principal, TransferReq req);
}
