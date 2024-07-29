package com.example.finalProject_synrgy.service;

import com.example.finalProject_synrgy.dto.rekening.CheckExistRequest;

public interface RekeningService {
    String checkIfRekeningExist(CheckExistRequest req);
}
