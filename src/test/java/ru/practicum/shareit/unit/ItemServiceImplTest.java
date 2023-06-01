package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepositoryImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingInfo;
import ru.practicum.shareit.item.repository.CommentRepositoryImpl;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private BookingRepositoryImpl bookingRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private ItemRepositoryImpl itemRepository;

    @Mock
    private ItemRequestRepositoryImpl itemRequestRepository;

    @Mock
    private CommentRepositoryImpl commentRepository;


    @BeforeEach
    void setUp() {
        UserDto ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        UserDto userDto = new UserDto(2L, "user", "adadad133@ya.ru");

        when(userRepository.getUserById(1L)).thenReturn(Optional.of(ownerDto));

        when(userRepository.getUserById(2L)).thenReturn(Optional.of(userDto));

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto);

        ItemDto newItem = new ItemDto();
        newItem.setId(1L);
        newItem.setName("newItem");
        newItem.setDescription("description 2");
        newItem.setAvailable(false);
        newItem.setOwner(ownerDto);

        CommentDto comment = new CommentDto();
        comment.setId(1L);
        comment.setAuthorDto(userDto);
        comment.setText("text");
        comment.setItemDto(itemDto);
        comment.setCreated(LocalDateTime.now());

        LocalDateTime startDate = LocalDateTime.of(2023, 4, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 30, 12, 0);

        BookingDto bookingDtoApproved = new BookingDto();
        bookingDtoApproved.setId(1L);
        bookingDtoApproved.setStatus(BookingStatus.APPROVED);
        bookingDtoApproved.setItem(itemDto);
        bookingDtoApproved.setBooker(userDto);
        bookingDtoApproved.setStartDate(startDate);
        bookingDtoApproved.setEndDate(endDate);

        when(itemRepository.getItem(1L)).thenReturn(Optional.of(itemDto));

        when(itemRepository.updateItem(any())).thenReturn(itemDto);

        when(itemRepository.getUserItemsByUserId(anyLong())).thenReturn(List.of(itemDto));

        when(commentRepository.getCommentsByItemId(1L)).thenReturn(List.of(comment));

        when(commentRepository.getCommentsInItemIds(anyList())).thenReturn(List.of(comment));

        when(bookingRepository.getApprovedBookingsByItemId(anyLong())).thenReturn(List.of(bookingDtoApproved));

        when(bookingRepository.getApprovedBookingsInItems(anyList())).thenReturn(List.of(bookingDtoApproved));

        when(itemRepository.getItemsByDescription(anyString())).thenReturn(List.of(itemDto));

        when(bookingRepository.getBookingsByItemIdAndUserId(anyLong(), anyLong())).thenReturn(List.of(bookingDtoApproved));
    }

    @Test
    void testAddItem() {
        UserDto ownerDto2 = new UserDto(3L, "user", "adadad133@ya.ru");

        when(userRepository.getUserById(3L)).thenReturn(Optional.of(ownerDto2));

        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto2);

        when(itemRepository.addItem(any())).thenReturn(itemDto);

        Item item = itemService.addItem(ItemMapper.toItem(itemDto), 3L);

        assertThat(item.getId(), equalTo(2L));
        assertThat(item.getOwner().getId(), equalTo(3L));

        verify(userRepository, times(1)).getUserById(3L);
        verify(itemRepository, times(1)).addItem(any());
        verify(itemRequestRepository, times(0)).getRequestById(anyLong());
    }

    @Test
    void testUpdateItem() {
        String name = "newItem";
        String description = "description 2";
        Item newItem = new Item();
        newItem.setName(name);
        newItem.setDescription(description);
        newItem.setAvailable(false);

        Item item = itemService.updateItem(newItem, 1L, 1L);

        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo(name));
        assertThat(item.getDescription(), equalTo(description));

        verify(itemRepository, times(1)).getItem(anyLong());
        verify(userRepository, times(1)).getUserById(anyLong());
        verify(itemRepository, times(1)).updateItem(any());
    }

    @Test
    void testGetItem() {
        ItemBookingInfo item = itemService.getItem(1L, 1L);

        assertThat(item.getId(), equalTo(1L));

        verify(itemRepository, times(1)).getItem(1L);
        verify(commentRepository, times(1)).getCommentsByItemId(1L);
        verify(bookingRepository, times(1)).getApprovedBookingsByItemId(1L);
    }

    @Test
    void testGetAllUserItems() {
        List<ItemBookingInfo> allUserItems = itemService.getAllUserItems(1L, null, null);

        assertThat(allUserItems.size(), equalTo(1));
        assertThat(allUserItems.get(0).getId(), equalTo(1L));

        verify(itemRepository, times(1)).getUserItemsByUserId(anyLong());
        verify(itemRepository, times(0)).getUserItemsByUserId(anyLong(), any());
        verify(bookingRepository, times(1)).getApprovedBookingsInItems(anyList());
        verify(commentRepository, times(1)).getCommentsInItemIds(anyList());
    }

    @Test
    void testGetItemByDescription() {
        List<Item> allUserItems = itemService.getItemsByDescription("desc", 2L, null, null);

        assertThat(allUserItems.size(), equalTo(1));
        assertThat(allUserItems.get(0).getId(), equalTo(1L));

        verify(itemRepository, times(1)).getItemsByDescription(anyString());
        verify(itemRepository, times(0)).getItemsByDescription(anyString(), any());
    }

    @Test
    void testAddCommentToItem() {
        String text = "comment text";
        Comment comment = new Comment();
        comment.setText(text);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(2L);
        commentDto.setText(text);
        commentDto.setCreated(LocalDateTime.now());

        when(commentRepository.addComment(any())).thenReturn(commentDto);

        Comment comment1 = itemService.addCommentToItem(1L, 2L, comment);

        assertThat(comment1.getId(), equalTo(2L));
        assertThat(comment1.getText(), equalTo(text));

        verify(itemRepository, times(1)).getItem(anyLong());
        verify(bookingRepository, times(1)).getBookingsByItemIdAndUserId(anyLong(), anyLong());
        verify(commentRepository, times(1)).addComment(any());
    }
}