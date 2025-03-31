package com.liatrio.exercise.dataService.controller;

import com.liatrio.exercise.dataService.dto.ApiResponse;
import com.liatrio.exercise.dataService.model.Item;
import com.liatrio.exercise.dataService.repository.CoreProcessItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coreProcess")
public class CoreProcessController {
    
    private final CoreProcessItemsRepository repository;
    
    @Autowired
    public CoreProcessController(CoreProcessItemsRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping("/items")
    public ApiResponse<List<Item>> getAllItems() {
        List<Item> items = repository.findAll();
        return ApiResponse.of(items);
    }
}
