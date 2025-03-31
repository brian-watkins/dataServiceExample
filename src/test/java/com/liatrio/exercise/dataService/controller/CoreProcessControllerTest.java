package com.liatrio.exercise.dataService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liatrio.exercise.dataService.dto.CreateItemRequest;
import com.liatrio.exercise.dataService.model.Item;
import com.liatrio.exercise.dataService.repository.CoreProcessItemsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

@SuppressWarnings("deprecation")
@WebMvcTest(CoreProcessController.class)
class CoreProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CoreProcessItemsRepository repository;
    
    @Test
    void getAllItems_ShouldReturnListOfItems() throws Exception {
        // Given
        List<Item> items = List.of(
                new Item(1L, "Item 1"),
                new Item(2L, "Item 2"),
                new Item(3L, "Item 3")
        );
        when(repository.findAll()).thenReturn(items);

        // When/Then
        mockMvc.perform(get("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Item 1")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].name", is("Item 2")))
                .andExpect(jsonPath("$.data[2].id", is(3)))
                .andExpect(jsonPath("$.data[2].name", is("Item 3")));
    }
    
    @Test
    void createItem_ShouldReturnCreatedItemWithLocationHeader() throws Exception {
        // Given
        CreateItemRequest createRequest = new CreateItemRequest("New Item");
        Item createdItem = new Item(4L, "New Item");
        when(repository.save(any(Item.class))).thenReturn(createdItem);
        
        // When/Then
        mockMvc.perform(post("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/api/coreProcess/items/4"))
                .andExpect(jsonPath("$.data.id", is(4)))
                .andExpect(jsonPath("$.data.name", is("New Item")))
                .andExpect(jsonPath("$.timestamp").isNumber());
    }
    
    @Test
    void createItem_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateItemRequest invalidRequest = new CreateItemRequest(null);
        
        // When/Then
        mockMvc.perform(post("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getItemById_ShouldReturnSingleItem() throws Exception {
        // Given
        Item item = new Item(1L, "Item 1");
        when(repository.findById(eq(1L))).thenReturn(java.util.Optional.of(item));
        
        // When/Then
        mockMvc.perform(get("/api/coreProcess/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Item 1")))
                .andExpect(jsonPath("$.timestamp").isNumber());
    }
    
    @Test
    void getItemById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        
        // When/Then
        mockMvc.perform(get("/api/coreProcess/items/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
