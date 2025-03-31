package com.liatrio.exercise.dataService.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("deprecation")
@WebMvcTest(CoreProcessController.class)
class CoreProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
