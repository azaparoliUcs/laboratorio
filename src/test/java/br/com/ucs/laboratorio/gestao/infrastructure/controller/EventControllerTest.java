package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.EventDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.EventResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.service.EventService;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EventDto eventDto;
    private EventResponse eventResponse;
    private EventModel event;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        objectMapper = new ObjectMapper();

        eventDto = new EventDto();
        eventDto.setObservation("Test Event");
        eventDto.setEventType(EventType.CALIBRATION);

        eventResponse = new EventResponse();
        eventResponse.setId(1L);
        eventResponse.setObservation("Test Event");
        eventDto.setEventType(EventType.CALIBRATION);

        event = new EventModel();
        event.setId(1L);
        event.setObservation("Test Event");
        eventDto.setEventType(EventType.CALIBRATION);
    }

    @Test
    void create_ShouldReturnEventResponse_WhenValidEventDto() throws Exception {
        when(eventService.create(any(EventDto.class))).thenReturn(eventResponse);

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isOk());

        verify(eventService).create(any(EventDto.class));
    }

    @Test
    void findById_ShouldReturnEventResponse_WhenEventExists() throws Exception {
        when(eventService.findById(1L)).thenReturn(event);

        mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk());

        verify(eventService).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfEventResponse() throws Exception {
        List<EventResponse> events = Arrays.asList(eventResponse);
        when(eventService.findAll()).thenReturn(events);

        mockMvc.perform(get("/event"))
                .andExpect(status().isOk());

        verify(eventService).findAll();
    }

    @Test
    void update_ShouldReturnUpdatedEventResponse() throws Exception {
        when(eventService.update(eq(1L), any(EventDto.class))).thenReturn(eventResponse);

        mockMvc.perform(put("/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isOk());

        verify(eventService).update(eq(1L), any(EventDto.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(eventService).delete(1L);

        mockMvc.perform(delete("/event/1"))
                .andExpect(status().isNoContent());

        verify(eventService).delete(1L);
    }

    @Test
    void findByEquipment_ShouldReturnEventsForEquipment() throws Exception {
        List<EventResponse> events = Arrays.asList(eventResponse);
        when(eventService.findByEquipmentId(1L)).thenReturn(events);

        mockMvc.perform(get("/event/equipment/1"))
                .andExpect(status().isOk());

        verify(eventService).findByEquipmentId(1L);
    }
}