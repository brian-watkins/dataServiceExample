package com.liatrio.exercise.dataService.controller;

import com.liatrio.exercise.dataService.dto.ApiResponse;
import com.liatrio.exercise.dataService.dto.CreateItemRequest;
import com.liatrio.exercise.dataService.model.Item;
import com.liatrio.exercise.dataService.repository.CoreProcessItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    
    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Item>> getItemById(@PathVariable Long id) {
        return repository.findById(id)
                .map(item -> ResponseEntity.ok(ApiResponse.of(item)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Item>> createItem(@RequestBody CreateItemRequest request) {
        // Validate request
        if (request == null || request.name() == null || request.name().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Create new item
        Item newItem = new Item(null, request.name());
        Item savedItem = repository.save(newItem);
        
        // Build location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.id())
                .toUri();
        
        // Return response with location header
        return ResponseEntity
                .created(location)
                .body(ApiResponse.of(savedItem));
    }
}
