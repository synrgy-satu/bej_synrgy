package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.entity.Qris;
import com.example.finalProject_synrgy.entity.Rekening;

import java.security.Principal;

public interface QrisService {
    public Object generateQris(Principal principal);

    public Qris generateQrisRekening(Rekening rekening);

    public Object readQrisList(Principal principal);

    public Object readQris(String id);

    public Object activate(Principal principal, String id, boolean active);
}
