package com.elias.GestoBar.controller;

import com.elias.GestoBar.dto.balanceDTO.DailyBalanceResponseDTO;
import com.elias.GestoBar.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    //GET /api/balace/daily
    @GetMapping("/daily")
    public ResponseEntity<DailyBalanceResponseDTO> getDailyBalance() {
        return ResponseEntity.ok(balanceService.getDailyBalance());
    }

    //POST /api/balance/reset
    @PostMapping("/reset")
    public ResponseEntity<Void> resetDay() {
        balanceService.resetDay();
        return ResponseEntity.noContent().build();
    }
}