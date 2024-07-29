package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.rekening.CheckExistRequest;
import com.example.finalProject_synrgy.entity.Rekening;

public interface RekeningService {
    String checkIfRekeningExist(CheckExistRequest req);

    Rekening create(CheckExistRequest req);
}
