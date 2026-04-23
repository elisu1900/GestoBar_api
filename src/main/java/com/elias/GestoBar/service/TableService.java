package com.elias.GestoBar.service;
import com.elias.GestoBar.model.RestaurantTable;
import com.elias.GestoBar.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantTableRepository tableRepository;

    public List<RestaurantTable> getAllActiveTables() {
        return tableRepository.findByIsActiveTrue();
    }

    public RestaurantTable getTableById(Integer tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));
    }
}