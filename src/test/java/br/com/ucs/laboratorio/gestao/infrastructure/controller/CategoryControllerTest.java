package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import br.com.ucs.laboratorio.gestao.domain.dto.CategoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.CategoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.domain.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CategoryDto categoryDto;
    private CategoryResponse categoryResponse;
    private CategoryModel category;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();

        categoryDto = new CategoryDto();
        categoryDto.setName("Equipamentos Eletrônicos");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Equipamentos Eletrônicos");

        category = new CategoryModel();
        category.setId(1L);
        category.setName("Equipamentos Eletrônicos");
    }

    @Test
    void shouldCreateCategorySuccessfully() throws Exception {
        when(categoryService.create(any(CategoryDto.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk());

        verify(categoryService).create(any(CategoryDto.class));
    }

    @Test
    void shouldFindCategoryByIdSuccessfully() throws Exception {
        try (MockedStatic<MapperUtil> mapperUtil = mockStatic(MapperUtil.class)) {
            when(categoryService.findById(1L)).thenReturn(category);
            mapperUtil.when(() -> MapperUtil.mapObject(category, CategoryResponse.class))
                    .thenReturn(categoryResponse);

            mockMvc.perform(get("/category/1"))
                    .andExpect(status().isOk());

            verify(categoryService).findById(1L);
        }
    }

    @Test
    void shouldFindAllCategoriesSuccessfully() throws Exception {
        List<CategoryResponse> categories = Arrays.asList(categoryResponse);
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk());

        verify(categoryService).findAll();
    }

    @Test
    void shouldUpdateCategorySuccessfully() throws Exception {
        when(categoryService.update(eq(1L), any(CategoryDto.class))).thenReturn(categoryResponse);

        mockMvc.perform(put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk());

        verify(categoryService).update(eq(1L), any(CategoryDto.class));
    }

    @Test
    void shouldDeleteCategorySuccessfully() throws Exception {
        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1L);
    }
}