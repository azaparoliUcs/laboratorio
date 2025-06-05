package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.service.EventService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        EventDto dto = new EventDto();
        EventResponse response = new EventResponse();

        when(eventService.create(dto)).thenReturn(response);

        var result = eventController.create(dto);
        assertEquals(response, result.getBody());
    }

    @Test
    void testFindById() {
        try (var mocked = mockStatic(MapperUtil.class)) {
            when(eventService.findById(1L)).thenReturn(new EventModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(EventResponse.class))).thenReturn(new EventResponse());

            var result = eventController.findById(1L);
            assertNotNull(result.getBody());
        }
    }

    @Test
    void testFindAll() {
        when(eventService.findAll()).thenReturn(List.of(new EventResponse()));
        var result = eventController.findAll();
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testUpdate() {
        EventDto dto = new EventDto();
        when(eventService.update(1L, dto)).thenReturn(new EventResponse());
        var result = eventController.update(1L, dto);
        assertNotNull(result.getBody());
    }

    @Test
    void testDelete() {
        var result = eventController.delete(1L);
        assertEquals(204, result.getStatusCodeValue());
        verify(eventService).delete(1L);
    }
}
