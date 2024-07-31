package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.rekening.RekeningCheckRequest;
import com.example.finalProject_synrgy.entity.Rekening;

public interface RekeningService {
    String checkIfRekeningExist(RekeningCheckRequest req);

    Rekening create(RekeningCheckRequest req);

    Object read();
}
