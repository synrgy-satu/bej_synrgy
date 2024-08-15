package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.rekening.RekeningCheckRequest;
import com.example.finalProject_synrgy.dto.rekening.RekeningCreateRequest;
import com.example.finalProject_synrgy.entity.Rekening;

public interface RekeningService {
    String checkIfRekeningExist(RekeningCheckRequest req);

    Rekening create(RekeningCreateRequest req);

    Object read();

    Object readOne(Long rekeningNumber);

    public Rekening createRandom();
}
