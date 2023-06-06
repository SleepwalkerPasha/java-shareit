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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingInfo;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("user");
        user.setEmail("email@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setOwner(user);
        item.setAvailable(true);
    }

    @Test
    void testAddItem() throws Exception {
        when(itemService.addItem(any(), anyLong())).thenReturn(item);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(item))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner.id", is(item.getOwner().getId()), Long.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item 1");
        item1.setDescription("description 1");
        item1.setOwner(user);
        item1.setAvailable(false);

        when(itemService.updateItem(any(), anyLong(), anyLong())).thenReturn(item1);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());
        mockMvc.perform(patch("/items/{itemId}", item.getId().toString())
                        .content(objectMapper.writeValueAsString(item1))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.owner.id", is(item1.getOwner().getId()), Long.class));
    }

    @Test
    void testGetItem() throws Exception {
        ItemBookingInfo itemBookingInfo = new ItemBookingInfo();
        itemBookingInfo.setId(1L);
        itemBookingInfo.setName(item.getName());
        itemBookingInfo.setDescription(item.getDescription());
        itemBookingInfo.setAvailable(item.getAvailable());

        when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemBookingInfo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());
        mockMvc.perform(get("/items/{itemId}", item.getId().toString())
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable()), Boolean.class));
    }

    @Test
    void testGetAllItems() throws Exception {
        ItemBookingInfo itemBookingInfo = new ItemBookingInfo();
        itemBookingInfo.setId(1L);
        itemBookingInfo.setName(item.getName());
        itemBookingInfo.setDescription(item.getDescription());
        itemBookingInfo.setAvailable(item.getAvailable());

        when(itemService.getAllUserItems(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemBookingInfo));

        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("state", "WAITING");
        requestParams.add("from", "0");
        requestParams.add("size", "5");
        headers.set("X-Sharer-User-Id", user.getId().toString());

        mockMvc.perform(get("/items")
                        .headers(headers)
                        .params(requestParams)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable()), Boolean.class));
    }

    @Test
    void testGetItemsByDescription() throws Exception {
        when(itemService.getItemsByDescription(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(List.of(item));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("text", "desc");
        requestParams.add("from", "0");
        requestParams.add("size", "5");
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());

        mockMvc.perform(get("/items/search")
                        .headers(headers)
                        .params(requestParams)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable()), Boolean.class));
    }

    @Test
    void testAddCommentToItem() throws Exception {
        Comment comment = new Comment();
        comment.setText("comment");

        Comment addedComment = new Comment(1L, comment.getText(), item, user.getName(), LocalDateTime.now());

        when(itemService.addCommentToItem(anyLong(), anyLong(), any())).thenReturn(addedComment);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());

        mockMvc.perform(post("/items/{itemId}/comment", item.getId().toString())
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(addedComment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(addedComment.getText())))
                .andExpect(jsonPath("$.item.id", is(addedComment.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(addedComment.getAuthorName())));
    }
}