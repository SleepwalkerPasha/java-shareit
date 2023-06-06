package ru.practicum.shareit.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User owner;

    private User booker;

    private Item item;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Booking booking;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2023, 6, 30, 12, 0);
        endDate = LocalDateTime.of(2023, 7, 30, 12, 0);

        owner = new User();
        owner.setId(1L);
        owner.setName("user");
        owner.setEmail("email@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setOwner(owner);
        item.setAvailable(true);

        booker = new User();
        booker.setId(2L);
        booker.setName("user2");
        booker.setEmail("someemail@mail.ru");

        booking = new Booking(1L, startDate, endDate, item, booker, BookingStatus.WAITING);
    }

    @Test
    void testAddBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest(item.getId(), startDate, endDate);

        when(bookingService.addBooking(anyLong(), any())).thenReturn(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", booker.getId().toString());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void addBookingInvalidValidationStartAfterEndDate() throws Exception {
        startDate = LocalDateTime.now();
        endDate = LocalDateTime.now().minusDays(2);
        BookingRequest bookingRequest = new BookingRequest(item.getId(), startDate, endDate);

        when(bookingService.addBooking(anyLong(), any())).thenReturn(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", booker.getId().toString());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBookingInvalidValidationStartDateEqualsEndDate() throws Exception {
        startDate = LocalDateTime.now();
        endDate = startDate;
        BookingRequest bookingRequest = new BookingRequest(item.getId(), startDate, endDate);

        when(bookingService.addBooking(anyLong(), any())).thenReturn(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", booker.getId().toString());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBookingInvalidValidationStartBeforeNow() throws Exception {
        startDate = LocalDateTime.now().minusDays(2);
        BookingRequest bookingRequest = new BookingRequest(item.getId(), startDate, endDate);

        when(bookingService.addBooking(anyLong(), any())).thenReturn(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", booker.getId().toString());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testApproveBooking() throws Exception {
        booking = new Booking(1L, startDate, endDate, item, booker, BookingStatus.APPROVED);

        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", booker.getId().toString());
        mockMvc.perform(patch("/bookings/{bookingId}", booking.getId().toString())
                        .param("approved", "true")
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(booking);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", booker.getId().toString());
        mockMvc.perform(get("/bookings/{bookingId}", booking.getId().toString())
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
    }

    @Test
    void testGetBookingsByUserId() throws Exception {
        when(bookingService.getBookingsByUserId(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(booking));

        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("state", "WAITING");
        requestParams.add("from", "0");
        requestParams.add("size", "5");
        headers.set("X-Sharer-User-Id", booker.getId().toString());

        mockMvc.perform(get("/bookings")
                        .headers(headers)
                        .params(requestParams)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())));
    }

    @Test
    void testGetAllBookingsOfUser() throws Exception {
        when(bookingService.getBookingsByOwnerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(booking));

        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("state", "WAITING");
        requestParams.add("from", "0");
        requestParams.add("size", "5");
        headers.set("X-Sharer-User-Id", owner.getId().toString());

        mockMvc.perform(get("/bookings/owner")
                        .headers(headers)
                        .params(requestParams)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.owner.id", is(booking.getItem().getOwner().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus().toString())));
    }

}
