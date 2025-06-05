package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.CategoryDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.CategoryResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.infrastructure.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.CategoryRepository;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_success() {
        Long id = 1L;
        CategoryModel model = new CategoryModel();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(model));

        CategoryModel result = categoryService.findById(id);

        assertEquals(model, result);
        verify(categoryRepository).findById(id);
    }

    @Test
    void testFindById_notFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> categoryService.findById(1L));
        assertEquals("Categoria nao existe", exception.getMessage());
    }

    @Test
    void testCreate_success() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Test Category");

        CategoryModel savedModel = CategoryModel.builder().name("Test Category").build();
        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(savedModel);
        when(modelMapper.map(savedModel, CategoryResponse.class)).thenReturn(response);

        CategoryResponse result = categoryService.create(dto);

        assertEquals(response, result);
        verify(categoryRepository).save(any(CategoryModel.class));
    }

    @Test
    void testFindAll_success() {
        List<CategoryModel> models = Arrays.asList(new CategoryModel(), new CategoryModel());
        List<CategoryResponse> responses = Arrays.asList(new CategoryResponse(), new CategoryResponse());

        when(categoryRepository.findAll()).thenReturn(models);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapList(models, CategoryResponse.class)).thenReturn(responses);

            List<CategoryResponse> result = categoryService.findAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        categoryService.delete(id);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void testUpdate_success() {
        Long id = 1L;
        CategoryDto dto = new CategoryDto();
        dto.setName("Updated");

        CategoryModel model = new CategoryModel();
        model.setName("Old");

        CategoryModel updatedModel = new CategoryModel();
        updatedModel.setName("Updated");

        CategoryResponse response = new CategoryResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(model));
        when(categoryRepository.save(model)).thenReturn(updatedModel);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapObject(updatedModel, CategoryResponse.class)).thenReturn(response);

            CategoryResponse result = categoryService.update(id, dto);

            assertEquals(response, result);
            assertEquals("Updated", model.getName());
        }
    }
}
