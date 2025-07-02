package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dao.TemplateDao;
import br.com.ucs.laboratorio.gestao.domain.dto.TemplateDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TemplateResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TemplateDao templateDao;

    @InjectMocks
    private TemplateService templateService;

    private TemplateDto templateDto;
    private TemplateModel templateModel;
    private TemplateResponse templateResponse;
    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
        templateDto = new TemplateDto();
        templateDto.setCategoryId(1L);
        templateDto.setDescription("Template Test");
        templateDto.setPeriodMaintenanceType(PeriodMaintenanceType.ONE_YEAR);
        templateDto.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);

        categoryModel = new CategoryModel();
        categoryModel.setId(1L);
        categoryModel.setName("Category Test");

        templateModel = new TemplateModel();
        templateModel.setId(1L);
        templateModel.setDescription("Template Test");
        templateModel.setPeriodMaintenanceType(PeriodMaintenanceType.ONE_YEAR);
        templateModel.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);
        templateModel.setCategory(categoryModel);

        templateResponse = new TemplateResponse();
        templateResponse.setId(1L);
        templateResponse.setDescription("Template Test");
        templateResponse.setPeriodMaintenanceType(PeriodMaintenanceType.ONE_YEAR);
        templateResponse.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);
    }

    @Test
    void create_ShouldCreateTemplate_WhenValidData() {
        // Given
        when(categoryService.findById(1L)).thenReturn(categoryModel);
        when(modelMapper.map(templateDto, TemplateModel.class)).thenReturn(templateModel);
        when(templateRepository.save(any(TemplateModel.class))).thenReturn(templateModel);
        when(modelMapper.map(templateModel, TemplateResponse.class)).thenReturn(templateResponse);

        // When
        TemplateResponse result = templateService.create(templateDto);

        // Then
        assertNotNull(result);
        assertEquals(templateResponse.getId(), result.getId());
        assertEquals(templateResponse.getDescription(), result.getDescription());
        assertEquals(templateResponse.getPeriodMaintenanceType(), result.getPeriodMaintenanceType());
        assertEquals(templateResponse.getPeriodCalibrationType(), result.getPeriodCalibrationType());

        verify(categoryService).findById(1L);
        verify(templateRepository).save(any(TemplateModel.class));
        verify(modelMapper, times(2)).map(any(), any());
    }

    @Test
    void findById_ShouldReturnTemplate_WhenExists() {
        // Given
        when(templateRepository.findById(1L)).thenReturn(Optional.of(templateModel));

        // When
        TemplateModel result = templateService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(templateModel.getId(), result.getId());
        assertEquals(templateModel.getDescription(), result.getDescription());
        verify(templateRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowBusinessException_WhenNotExists() {
        // Given
        when(templateRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> templateService.findById(1L));
        assertEquals("Modelo nao existe", exception.getMessage());
        verify(templateRepository).findById(1L);
    }

    @Test
    void findAll_ShouldReturnAllTemplates() {
        // Given
        List<TemplateModel> templates = Arrays.asList(templateModel);
        when(templateRepository.findAll()).thenReturn(templates);

        // When
        List<TemplateResponse> result = templateService.findAll();

        // Then
        assertNotNull(result);
        verify(templateRepository).findAll();
    }

    @Test
    void delete_ShouldDeleteTemplate_WhenExists() {
        // Given
        when(templateRepository.findById(1L)).thenReturn(Optional.of(templateModel));

        // When
        templateService.delete(1L);

        // Then
        verify(templateRepository).findById(1L);
        verify(templateRepository).delete(templateModel);
    }

    @Test
    void delete_ShouldThrowBusinessException_WhenNotExists() {
        // Given
        when(templateRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> templateService.delete(1L));
        assertEquals("Modelo nao existe", exception.getMessage());
        verify(templateRepository).findById(1L);
        verify(templateRepository, never()).delete(any());
    }

    @Test
    void update_ShouldUpdateTemplate_WhenValidData() {
        // Given
        TemplateDto updateDto = new TemplateDto();
        updateDto.setDescription("Updated Template");
        updateDto.setPeriodMaintenanceType(PeriodMaintenanceType.ONE_YEAR);
        updateDto.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);

        when(templateRepository.findById(1L)).thenReturn(Optional.of(templateModel));
        when(templateRepository.save(any(TemplateModel.class))).thenReturn(templateModel);

        // When
        TemplateResponse result = templateService.update(1L, updateDto);

        // Then
        assertNotNull(result);
        verify(templateRepository).findById(1L);
        verify(templateRepository).save(templateModel);

        // Verify that the template fields were updated
        assertEquals("Updated Template", templateModel.getDescription());
        assertEquals(PeriodMaintenanceType.ONE_YEAR, templateModel.getPeriodMaintenanceType());
        assertEquals(PeriodCalibrationType.FIVE_YEARS, templateModel.getPeriodCalibrationType());
    }

    @Test
    void update_ShouldThrowBusinessException_WhenTemplateNotExists() {
        // Given
        when(templateRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> templateService.update(1L, templateDto));
        assertEquals("Modelo nao existe", exception.getMessage());
        verify(templateRepository).findById(1L);
        verify(templateRepository, never()).save(any());
    }

    @Test
    void filterTemplates_ShouldReturnFilteredTemplates_WhenValidFilters() {
        // Given
        String brand = "Brand Test";
        TemplateType type = TemplateType.ANALOG;
        Long categoryId = 1L;

        List<TemplateModel> filteredTemplates = Arrays.asList(templateModel);
        when(templateDao.filterTemplates(brand, type, categoryId)).thenReturn(filteredTemplates);

        // When
        List<TemplateResponse> result = templateService.filterTemplates(brand, type, categoryId);

        // Then
        assertNotNull(result);
        verify(templateDao).filterTemplates(brand, type, categoryId);
    }

    @Test
    void filterTemplates_ShouldReturnEmptyList_WhenNoTemplatesMatch() {
        // Given
        String brand = "Nonexistent Brand";
        TemplateType type = TemplateType.ANALOG;
        Long categoryId = 999L;

        when(templateDao.filterTemplates(brand, type, categoryId)).thenReturn(Arrays.asList());

        // When
        List<TemplateResponse> result = templateService.filterTemplates(brand, type, categoryId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(templateDao).filterTemplates(brand, type, categoryId);
    }

    @Test
    void filterTemplates_ShouldHandleNullParameters() {
        // Given
        when(templateDao.filterTemplates(null, null, null)).thenReturn(Arrays.asList(templateModel));

        // When
        List<TemplateResponse> result = templateService.filterTemplates(null, null, null);

        // Then
        assertNotNull(result);
        verify(templateDao).filterTemplates(null, null, null);
    }
}