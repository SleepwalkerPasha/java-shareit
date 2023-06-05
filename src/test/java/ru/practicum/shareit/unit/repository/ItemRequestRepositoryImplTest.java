package ru.practicum.shareit.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryImpl;
import ru.practicum.shareit.request.repository.JpaItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestRepositoryImplTest {

    @InjectMocks
    private ItemRequestRepositoryImpl itemRequestRepository;

    @Mock
    private JpaItemRequestRepository jpaItemRepository;

    private ItemRequestDto itemRequestDto;

    private UserDto userDto;

    private UserDto ownerDto;

    @BeforeEach
    void setUp() {
        ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        userDto = new UserDto(2L, "user", "adadad133@ya.ru");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequestor(userDto);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description");
    }

    @Test
    void testGetAllRequests() {
        when(jpaItemRepository.findAllItemRequests(anyLong())).thenReturn(List.of(itemRequestDto));
        when(jpaItemRepository.findAllItemRequests(anyLong(), any())).thenReturn(new PageImpl<>(List.of(itemRequestDto)));

        List<ItemRequestDto> requests = itemRequestRepository.getAllRequests(ownerDto.getId());
        Page<ItemRequestDto> pageRequests = itemRequestRepository.getAllRequests(ownerDto.getId(), PageRequest.of(0, 5));

        assertThat(pageRequests.getSize(), equalTo(1));
        assertThat(requests.size(), equalTo(1));
        assertThat(requests.get(0), equalTo(itemRequestDto));
    }

    @Test
    void testGetAllRequestsByUserId() {
        when(jpaItemRepository.findAllByRequestor_IdOrderByCreatedDesc(anyLong())).thenReturn(List.of(itemRequestDto));

        List<ItemRequestDto> allRequestsByUserId = itemRequestRepository.getAllRequestsByUserId(userDto.getId());

        assertThat(allRequestsByUserId.size(), equalTo(1));
        assertThat(allRequestsByUserId.get(0), equalTo(itemRequestDto));
    }
}