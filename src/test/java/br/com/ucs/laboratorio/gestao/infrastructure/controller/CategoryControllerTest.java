package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.CategoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.CategoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.domain.service.CategoryService;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        CategoryDto dto = new CategoryDto();
        CategoryResponse response = new CategoryResponse();

        when(categoryService.create(dto)).thenReturn(response);

        var result = categoryController.create(dto);

        assertEquals(response, result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        CategoryResponse response = new CategoryResponse();

        try (var mocked = mockStatic(MapperUtil.class)) {
            when(categoryService.findById(id)).thenReturn(new CategoryModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(CategoryResponse.class))).thenReturn(response);

            var result = categoryController.findById(id);

            assertEquals(response, result.getBody());
        }
    }

    @Test
    void testFindAll() {
        List<CategoryResponse> list = List.of(new CategoryResponse());
        when(categoryService.findAll()).thenReturn(list);

        var result = categoryController.findAll();

        assertEquals(list, result.getBody());
    }

    @Test
    void testUpdate() {
        CategoryDto dto = new CategoryDto();
        CategoryResponse response = new CategoryResponse();

        when(categoryService.update(1L, dto)).thenReturn(response);

        var result = categoryController.update(1L, dto);

        assertEquals(response, result.getBody());
    }

    @Test
    void testDelete() {
        var result = categoryController.delete(1L);

        assertEquals(204, result.getStatusCodeValue());
        verify(categoryService).delete(1L);
    }
}
