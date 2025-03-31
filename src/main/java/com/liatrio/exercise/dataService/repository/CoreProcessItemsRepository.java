package com.liatrio.exercise.dataService.repository;

import com.liatrio.exercise.dataService.model.Item;

import java.util.List;
import java.util.Optional;

public interface CoreProcessItemsRepository {
    List<Item> findAll();
    Optional<Item> findById(Long id);
    Item save(Item item);
    void deleteById(Long id);
    Item update(Item item);
}
