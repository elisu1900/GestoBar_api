package com.elias.GestoBar.service;

import com.elias.GestoBar.dto.restaurantTableDTO.RestaurantTableRequestDTO;
import com.elias.GestoBar.model.RestaurantTable;
import com.elias.GestoBar.repository.RestaurantTableRepository;
import java.util.Optional;
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

    public RestaurantTable createTable(RestaurantTableRequestDTO request) {
        Optional<RestaurantTable> existing = tableRepository.findByNumber(request.getNumber());
        if (existing.isPresent()) {
            RestaurantTable table = existing.get();
            if (Boolean.TRUE.equals(table.getIsActive())) {
                throw new IllegalArgumentException("An active table already exists with number " + request.getNumber());
            }
            table.setIsActive(true);
            table.setCapacity(request.getCapacity());
            return tableRepository.save(table);
        }
        return tableRepository.save(RestaurantTable.builder()
                .number(request.getNumber())
                .capacity(request.getCapacity())
                .isActive(true)
                .build());
    }

    public void deactivateTable(Integer tableId) {
        RestaurantTable table = getTableById(tableId);
        table.setIsActive(false);
        tableRepository.save(table);
    }
}