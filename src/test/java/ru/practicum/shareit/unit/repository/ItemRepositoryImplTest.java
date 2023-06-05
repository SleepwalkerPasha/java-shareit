package ru.practicum.shareit.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.item.repository.JpaItemRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRepositoryImplTest {

    @InjectMocks
    private ItemRepositoryImpl itemRepository;

    @Mock
    private JpaItemRepository jpaItemRepository;

    private UserDto ownerDto;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto);
    }

    @Test
    void testUpdateItem() {
        ItemDto updatedItem = new ItemDto();
        updatedItem.setId(1L);
        updatedItem.setName("item 1");
        updatedItem.setDescription("description 1");
        updatedItem.setAvailable(false);
        updatedItem.setOwner(ownerDto);

        when(jpaItemRepository.save(any())).thenReturn(updatedItem);

        ItemDto updateItem = itemRepository.updateItem(updatedItem);

        assertThat(updateItem, equalTo(updatedItem));
    }

    @Test
    void testGetUserItemsByUserId() {
        when(jpaItemRepository.findAllUserItemsByUserId(anyLong())).thenReturn(List.of(itemDto));
        when(jpaItemRepository.findAllUserItemsByUserId(anyLong(), any())).thenReturn(new PageImpl<>(List.of(itemDto)));

        Page<ItemDto> pageItems = itemRepository.getUserItemsByUserId(ownerDto.getId(), PageRequest.of(0, 5));
        List<ItemDto> items = itemRepository.getUserItemsByUserId(ownerDto.getId());

        assertThat(items.size(), equalTo(1));
        assertThat(pageItems.getSize(), equalTo(1));
        assertThat(items.get(0), equalTo(itemDto));
    }

    @Test
    void testGetItemsByDescription() {
        when(jpaItemRepository.findAllItemsBySubstring(anyString())).thenReturn(List.of(itemDto));
        when(jpaItemRepository.findAllItemsBySubstring(anyString(), any())).thenReturn(new PageImpl<>(List.of(itemDto)));

        Page<ItemDto> pageItems = itemRepository.getItemsByDescription("desc", PageRequest.of(0, 5));
        List<ItemDto> items = itemRepository.getItemsByDescription("desc");

        assertThat(items.size(), equalTo(1));
        assertThat(pageItems.getSize(), equalTo(1));
        assertThat(items.get(0), equalTo(itemDto));
    }
}