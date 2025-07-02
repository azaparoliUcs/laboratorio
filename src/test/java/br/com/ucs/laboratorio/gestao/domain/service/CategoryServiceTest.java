package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.CategoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.CategoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.CategoryRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryModel categoryModel;
    private CategoryDto categoryDto;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        categoryModel = CategoryModel.builder()
                .id(1L)
                .name("Categoria Teste")
                .build();

        categoryDto = new CategoryDto();
        categoryDto.setName("Categoria Teste");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Categoria Teste");
    }

    @Test
    void findById_WhenCategoryExists_ShouldReturnCategory() {
        // Given
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryModel));

        // When
        CategoryModel result = categoryService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(categoryModel.getId(), result.getId());
        assertEquals(categoryModel.getName(), result.getName());
        verify(categoryRepository).findById(id);
    }

    @Test
    void findById_WhenCategoryDoesNotExist_ShouldThrowBusinessException() {
        // Given
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> categoryService.findById(id));
        assertEquals("Categoria nao existe", exception.getMessage());
        verify(categoryRepository).findById(id);
    }

    @Test
    void create_ShouldCreateCategoryAndReturnResponse() {
        // Given
        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(categoryModel);
        when(modelMapper.map(categoryModel, CategoryResponse.class)).thenReturn(categoryResponse);

        // When
        CategoryResponse result = categoryService.create(categoryDto);

        // Then
        assertNotNull(result);
        assertEquals(categoryResponse.getId(), result.getId());
        assertEquals(categoryResponse.getName(), result.getName());
        verify(categoryRepository).save(any(CategoryModel.class));
        verify(modelMapper).map(categoryModel, CategoryResponse.class);
    }

    @Test
    void findAll_ShouldReturnListOfCategoryResponses() {
        // Given
        List<CategoryModel> categoryList = Arrays.asList(categoryModel);
        List<CategoryResponse> expectedResponses = Arrays.asList(categoryResponse);

        when(categoryRepository.findAll()).thenReturn(categoryList);

        try (MockedStatic<MapperUtil> mockedMapperUtil = mockStatic(MapperUtil.class)) {
            mockedMapperUtil.when(() -> MapperUtil.mapList(categoryList, CategoryResponse.class))
                    .thenReturn(expectedResponses);

            // When
            List<CategoryResponse> result = categoryService.findAll();

            // Then
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
            assertEquals(expectedResponses.get(0).getId(), result.get(0).getId());
            verify(categoryRepository).findAll();
        }
    }

    @Test
    void delete_ShouldDeleteCategory() {
        // Given
        Long id = 1L;

        // When
        categoryService.delete(id);

        // Then
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void update_WhenCategoryExists_ShouldUpdateAndReturnResponse() {
        // Given
        Long id = 1L;
        String newName = "Categoria Atualizada";
        categoryDto.setName(newName);

        CategoryModel updatedCategory = CategoryModel.builder()
                .id(id)
                .name(newName)
                .build();

        CategoryResponse updatedResponse = new CategoryResponse();
        updatedResponse.setId(id);
        updatedResponse.setName(newName);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryModel));
        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(updatedCategory);

        try (MockedStatic<MapperUtil> mockedMapperUtil = mockStatic(MapperUtil.class)) {
            mockedMapperUtil.when(() -> MapperUtil.mapObject(updatedCategory, CategoryResponse.class))
                    .thenReturn(updatedResponse);

            // When
            CategoryResponse result = categoryService.update(id, categoryDto);

            // Then
            assertNotNull(result);
            assertEquals(updatedResponse.getId(), result.getId());
            assertEquals(newName, result.getName());
            verify(categoryRepository).findById(id);
            verify(categoryRepository).save(any(CategoryModel.class));
        }
    }

    @Test
    void update_WhenCategoryDoesNotExist_ShouldThrowBusinessException() {
        // Given
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> categoryService.update(id, categoryDto));
        assertEquals("Categoria nao existe", exception.getMessage());
        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any(CategoryModel.class));
    }

    @Test
    void create_WhenNameIsProvided_ShouldBuildCategoryCorrectly() {
        // Given
        String categoryName = "Nova Categoria";
        categoryDto.setName(categoryName);

        CategoryModel newCategory = CategoryModel.builder()
                .name(categoryName)
                .build();
        newCategory.setId(2L);

        CategoryResponse newResponse = new CategoryResponse();
        newResponse.setId(2L);
        newResponse.setName(categoryName);

        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(newCategory);
        when(modelMapper.map(newCategory, CategoryResponse.class)).thenReturn(newResponse);

        // When
        CategoryResponse result = categoryService.create(categoryDto);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(categoryName, result.getName());
        verify(categoryRepository).save(argThat(category ->
                category.getName().equals(categoryName)));
    }
}