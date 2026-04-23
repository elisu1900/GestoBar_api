package com.elias.GestoBar.controller;

import com.elias.GestoBar.dto.restaurantTableDTO.RestaurantTableResponseDTO;
import com.elias.GestoBar.mapper.RestaurantTableMapper;
import com.elias.GestoBar.model.RestaurantTable;
import com.elias.GestoBar.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;
    private final RestaurantTableMapper tableMapper;

    @GetMapping
    public ResponseEntity<List<RestaurantTableResponseDTO>> getAllTables() {
        List<RestaurantTableResponseDTO> tables = tableService.getAllActiveTables()
                .stream()
                .map(tableMapper::toResponse)
                .toList();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<RestaurantTableResponseDTO> getTableById(@PathVariable Integer tableId) {
        return ResponseEntity.ok(tableMapper.toResponse(tableService.getTableById(tableId)));
    }
}
