package com.example.finalProject_synrgy.dto.bca;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BcaSignature {
    String timestamp;
    String signature;
}
