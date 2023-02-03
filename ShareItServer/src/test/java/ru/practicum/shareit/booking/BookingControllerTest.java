package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    private static BookingDtoFromUser bookingDtoFromUser;

    private static BookingDtoToUser bookingDtoToUser;

    @BeforeAll
    static void setUp() {
        bookingDtoFromUser = new BookingDtoFromUser();
        bookingDtoFromUser.setStart(LocalDateTime.MAX);
        bookingDtoFromUser.setEnd(LocalDateTime.MAX);
        bookingDtoFromUser.setItemId(1L);

        bookingDtoToUser = new BookingDtoToUser();
        bookingDtoToUser.setId(5L);
        bookingDtoToUser.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createNewBooking() throws Exception {
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void getBadRequestWhenCreateNewBookingWithoutHeader() throws Exception {
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.updateStatus(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }

    @Test
    void getBadRequestWhenUpdateBookingWithoutHeader() throws Exception {
        when(bookingService.updateStatus(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBadRequestWhenUpdateBookingWithoutParam() throws Exception {
        when(bookingService.updateStatus(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findBooking() throws Exception {
        when(bookingService.findBooking(anyLong(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)));
    }

    @Test
    void getBadRequestWhenFindBookingWithoutHeader() throws Exception {
        when(bookingService.findBooking(anyLong(), anyLong())).thenReturn(bookingDtoToUser);
        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBadRequestWhenFindAllBookingsByItemOwnerWithoutHeader() throws Exception {
        when(bookingService.findAllBookingsByItemOwner(anyLong(), anyString(), any())).thenReturn(new ArrayList<>());
        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(bookingDtoFromUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}