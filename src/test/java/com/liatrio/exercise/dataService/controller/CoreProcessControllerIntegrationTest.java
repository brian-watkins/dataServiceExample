package com.liatrio.exercise.dataService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liatrio.exercise.dataService.dto.CreateItemRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoreProcessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllItems_ShouldReturnItemsFromRepository() throws Exception {
        mockMvc.perform(get("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$.timestamp").isNumber());
    }
    
    @Test
    void deleteItem_ShouldDeleteExistingItem() throws Exception {
        // First, create a new item to ensure it exists
        CreateItemRequest createRequest = new CreateItemRequest("Test Item For Delete");
        
        String locationHeader = mockMvc.perform(post("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        
        // Extract ID from location header
        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        
        // Delete the item
        mockMvc.perform(delete("/api/coreProcess/items/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        // Verify item is deleted by trying to get it (should return 404)
        mockMvc.perform(get("/api/coreProcess/items/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deleteItem_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/coreProcess/items/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createItem_ShouldCreateNewItemAndReturnLocationHeader() throws Exception {
        // Given
        CreateItemRequest createRequest = new CreateItemRequest("Test Item Created");
        
        // When/Then
        mockMvc.perform(post("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.name", is("Test Item Created")))
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
    void getItemById_ShouldReturnExistingItem() throws Exception {
        // First, create a new item to ensure it exists
        CreateItemRequest createRequest = new CreateItemRequest("Test Item For Get");
        
        String locationHeader = mockMvc.perform(post("/api/coreProcess/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");
        
        // Extract ID from location header
        String id = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        
        // Now fetch the item
        mockMvc.perform(get("/api/coreProcess/items/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.name", is("Test Item For Get")))
                .andExpect(jsonPath("$.timestamp").isNumber());
    }
    
    @Test
    void getItemById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/coreProcess/items/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
