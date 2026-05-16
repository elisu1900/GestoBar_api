package com.elias.GestoBar.controller;

import com.elias.GestoBar.dto.restaurantTableDTO.RestaurantTableRequestDTO;
import com.elias.GestoBar.dto.restaurantTableDTO.RestaurantTableResponseDTO;
import com.elias.GestoBar.mapper.RestaurantTableMapper;
import com.elias.GestoBar.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;
    private final RestaurantTableMapper tableMapper;

    //GET /api/tables
    @GetMapping
    public ResponseEntity<List<RestaurantTableResponseDTO>> getAllTables() {
        List<RestaurantTableResponseDTO> tables = tableService.getAllActiveTables()
                .stream()
                .map(tableMapper::toResponse)
                .toList();
        return ResponseEntity.ok(tables);
    }
    //GET /api/tables/tablesId
    @GetMapping("/{tableId}")
    public ResponseEntity<RestaurantTableResponseDTO> getTableById(@PathVariable Integer tableId) {
        return ResponseEntity.ok(tableMapper.toResponse(tableService.getTableById(tableId)));
    }

    // POST /api/tables
    @PostMapping
    public ResponseEntity<RestaurantTableResponseDTO> createTable(@RequestBody @Valid RestaurantTableRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tableMapper.toResponse(tableService.createTable(request)));
    }

    // PATCH /api/tables/{tableId}/deactivate
    @PatchMapping("/{tableId}/deactivate")
    public ResponseEntity<Void> deactivateTable(@PathVariable Integer tableId) {
        tableService.deactivateTable(tableId);
        return ResponseEntity.noContent().build();
    }
}
