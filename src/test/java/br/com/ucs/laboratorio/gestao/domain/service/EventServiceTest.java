package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.EventRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EquipmentService equipmentService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 //TODO AJUSTAR TESTES DE CREATE E UPDATE
    @Test
    void testFindById_success() {
        Long id = 1L;
        EventModel event = new EventModel();

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        EventModel result = eventService.findById(id);

        assertEquals(event, result);
    }

    @Test
    void testFindById_notFound() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> eventService.findById(99L));
        assertEquals("Evento nao existe", ex.getMessage());
    }

    @Test
    void testFindAll_success() {
        List<EventModel> models = List.of(new EventModel(), new EventModel());
        List<EventResponse> responses = List.of(new EventResponse(), new EventResponse());

        when(eventRepository.findAll()).thenReturn(models);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapList(models, EventResponse.class)).thenReturn(responses);

            List<EventResponse> result = eventService.findAll();

            assertEquals(2, result.size());
        }
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        EventModel event = new EventModel();

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        eventService.delete(id);

        verify(eventRepository).delete(event);
    }

}
