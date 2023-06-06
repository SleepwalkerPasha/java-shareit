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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemResponse;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Item item;

    private ItemRequest request;

    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("owner");
        owner.setEmail("email@mail.ru");

        user = new User();
        user.setId(2L);
        user.setName("user");
        user.setEmail("email111@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setOwner(user);
        item.setAvailable(true);

        ItemResponse itemResponse = new ItemResponse(1L, "item", "description", true, owner, 1L);

        request = new ItemRequest(1L, "need item", user, LocalDateTime.now(), List.of(itemResponse));
    }

    @Test
    void testAddRequest() throws Exception {
        when(itemRequestService.addRequest(anyLong(), any())).thenReturn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(request))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(request.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].id", is(item.getId()), Long.class));
    }

    @Test
    void testGetAllRequestsByUserId() throws Exception {
        when(itemRequestService.getAllRequestsByOwnerId(anyLong())).thenReturn(List.of(request));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", owner.getId().toString());
        mockMvc.perform(get("/requests")
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(request.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].id", is(item.getId()), Long.class));
    }

    @Test
    void testGetAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(request));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("text", "desc");
        requestParams.add("from", "0");
        requestParams.add("size", "5");
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());

        mockMvc.perform(get("/requests/all")
                        .headers(headers)
                        .params(requestParams)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(request.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].id", is(item.getId()), Long.class));
    }

    @Test
    void testGetRequestById() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong())).thenReturn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());
        mockMvc.perform(get("/requests/{requestId}", request.getId().toString())
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(request.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].id", is(item.getId()), Long.class));
    }
}