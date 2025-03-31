package com.liatrio.exercise.dataService.repository;

import com.liatrio.exercise.dataService.model.Item;

import java.util.List;

public interface CoreProcessItemsRepository {
    List<Item> findAll();
}
