package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.TemplateDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TemplateResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.CategoryModel;
import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodCalibrationType;
import br.com.ucs.laboratorio.gestao.domain.type.PeriodMaintenanceType;
import br.com.ucs.laboratorio.gestao.infrastructure.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.TemplateRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_success() {
        TemplateDto dto = new TemplateDto();
        dto.setCategoryId(1L);

        CategoryModel category = new CategoryModel();
        TemplateModel template = new TemplateModel();
        TemplateModel saved = new TemplateModel();
        TemplateResponse response = new TemplateResponse();

        when(categoryService.findById(1L)).thenReturn(category);
        when(modelMapper.map(dto, TemplateModel.class)).thenReturn(template);
        when(templateRepository.save(template)).thenReturn(saved);
        when(modelMapper.map(saved, TemplateResponse.class)).thenReturn(response);

        TemplateResponse result = templateService.create(dto);

        assertEquals(response, result);
        assertEquals(category, template.getCategory());
    }

    @Test
    void testFindById_success() {
        Long id = 1L;
        TemplateModel template = new TemplateModel();

        when(templateRepository.findById(id)).thenReturn(Optional.of(template));

        TemplateModel result = templateService.findById(id);

        assertEquals(template, result);
    }

    @Test
    void testFindById_notFound() {
        when(templateRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> templateService.findById(100L));
        assertEquals("Modelo nao existe", ex.getMessage());
    }

    @Test
    void testFindAll_success() {
        List<TemplateModel> models = List.of(new TemplateModel(), new TemplateModel());
        List<TemplateResponse> responses = List.of(new TemplateResponse(), new TemplateResponse());

        when(templateRepository.findAll()).thenReturn(models);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapList(models, TemplateResponse.class)).thenReturn(responses);

            List<TemplateResponse> result = templateService.findAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        TemplateModel template = new TemplateModel();

        when(templateRepository.findById(id)).thenReturn(Optional.of(template));

        templateService.delete(id);

        verify(templateRepository).delete(template);
    }

    @Test
    void testUpdate_success() {
        Long id = 1L;
        TemplateDto dto = new TemplateDto();
        dto.setDescription("Nova descrição");
        dto.setPeriodCalibrationType(PeriodCalibrationType.FIVE_YEARS);
        dto.setPeriodMaintenanceType(PeriodMaintenanceType.ONE_YEAR);

        TemplateModel template = new TemplateModel();
        TemplateModel saved = new TemplateModel();
        TemplateResponse response = new TemplateResponse();

        when(templateRepository.findById(id)).thenReturn(Optional.of(template));
        when(templateRepository.save(template)).thenReturn(saved);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapObject(saved, TemplateResponse.class)).thenReturn(response);

            TemplateResponse result = templateService.update(id, dto);

            assertEquals(response, result);
            assertEquals("Nova descrição", template.getDescription());
            assertEquals(PeriodCalibrationType.FIVE_YEARS, template.getPeriodCalibrationType());
            assertEquals(PeriodMaintenanceType.ONE_YEAR, template.getPeriodMaintenanceType());
        }
    }
}
