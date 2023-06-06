package ru.practicum.shareit.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryImpl;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepositoryImpl itemRequestRepository;

    @Mock
    private ItemRepositoryImpl itemRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        UserDto ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        UserDto userDto = new UserDto(2L, "user", "adadad133@ya.ru");

        when(userRepository.getUserById(1L)).thenReturn(Optional.of(ownerDto));

        when(userRepository.getUserById(2L)).thenReturn(Optional.of(userDto));

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequestor(userDto);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto);
        itemDto.setItemRequest(itemRequestDto);


        when(itemRepository.getItem(1L)).thenReturn(Optional.of(itemDto));

        when(itemRepository.getItemsByItemRequestId(1L)).thenReturn(List.of(itemDto));

        when(itemRepository.getItemsInRequestIds(anyList())).thenReturn(List.of(itemDto));

        when(itemRequestRepository.getAllRequestsByUserId(anyLong())).thenReturn(List.of(itemRequestDto));

        when(itemRequestRepository.getAllRequests(anyLong(), any())).thenReturn(new PageImpl<>(List.of(itemRequestDto)));

        when(itemRequestRepository.getRequestById(anyLong())).thenReturn(Optional.of(itemRequestDto));
    }

    @Test
    void testAddRequest() {
        UserDto userDto3 = new UserDto(3L, "user 3", "sdfsdfsfsf@ya.ru");

        when(userRepository.getUserById(3L)).thenReturn(Optional.of(userDto3));

        String description = "хочу item";

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(description);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequestor(userDto3);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setId(2L);
        itemRequestDto.setDescription(description);

        when(itemRequestRepository.addRequest(any())).thenReturn(itemRequestDto);

        ItemRequest addedRequest = itemRequestService.addRequest(3L, itemRequest);

        assertThat(addedRequest.getId(), equalTo(2L));
        assertThat(addedRequest.getRequestor().getId(), equalTo(3L));

        verify(itemRequestRepository, times(1)).addRequest(any());
        verify(userRepository, times(1)).getUserById(3L);
    }

    @Test
    void testGetAllRequestsByOwnerId() {
        List<ItemRequest> allRequestsByOwnerId = itemRequestService.getAllRequestsByOwnerId(1L);

        assertThat(allRequestsByOwnerId.size(), equalTo(1));
        assertThat(allRequestsByOwnerId.get(0).getId(), equalTo(1L));
        assertThat(allRequestsByOwnerId.get(0).getRequestor().getId(), equalTo(2L));

        verify(userRepository, times(1)).getUserById(1L);
        verify(itemRequestRepository, times(1)).getAllRequestsByUserId(1L);
    }

    @Test
    void testGetAllRequests() {
        List<ItemRequest> allRequestsByOwnerId = itemRequestService.getAllRequests(2L, 0, 5);

        assertThat(allRequestsByOwnerId.size(), equalTo(1));
        assertThat(allRequestsByOwnerId.get(0).getId(), equalTo(1L));
        assertThat(allRequestsByOwnerId.get(0).getRequestor().getId(), equalTo(2L));

        verify(userRepository, times(1)).getUserById(2L);
        verify(itemRequestRepository, times(1)).getAllRequests(anyLong(), any());
        verify(itemRepository, times(1)).getItemsInRequestIds(List.of(1L));
    }

    @Test
    void testGetRequestById() {
        ItemRequest requestById = itemRequestService.getRequestById(2L, 1L);

        assertThat(requestById.getId(), equalTo(1L));
        assertThat(requestById.getRequestor().getId(), equalTo(2L));

        verify(userRepository, times(1)).getUserById(2L);
        verify(itemRequestRepository, times(1)).getRequestById(1L);
        verify(itemRepository, times(1)).getItemsByItemRequestId(1L);
    }
}