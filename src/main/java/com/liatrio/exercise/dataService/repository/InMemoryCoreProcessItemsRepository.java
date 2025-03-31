package com.liatrio.exercise.dataService.repository;

import com.liatrio.exercise.dataService.model.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryCoreProcessItemsRepository implements CoreProcessItemsRepository {
    private final List<Item> items = new ArrayList<>();

    public InMemoryCoreProcessItemsRepository() {
        // Initialize with some data for testing
        items.add(new Item(1L, "Item 1"));
        items.add(new Item(2L, "Item 2"));
        items.add(new Item(3L, "Item 3"));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }
    
    @Override
    public Optional<Item> findById(Long id) {
        return items.stream()
                .filter(item -> item.id().equals(id))
                .findFirst();
    }
}
