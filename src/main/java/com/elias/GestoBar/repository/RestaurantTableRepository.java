package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    List<RestaurantTable> findBytableId(Integer tableId);

    List<RestaurantTable> findByIsActiveTrue();

    List<RestaurantTable> findByIsActiveFalse();

    List<RestaurantTable> findByCapacity(Integer capacity);

    java.util.Optional<RestaurantTable> findByNumber(Integer number);
}
