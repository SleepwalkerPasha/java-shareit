package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("email@mail.ru");
        userDto.setName("Name");

        when(userRepository.addUser(any())).thenReturn(userDto);

        when(userRepository.getUserById(anyLong())).thenReturn(Optional.of(userDto));

        when(userRepository.getAll()).thenReturn(List.of(userDto));


        userService.addUser(UserMapper.toUser(userDto));
    }

    @Test
    void testAddUser() {
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setEmail("newemail@mail.ru");
        userDto.setName("new name");

        when(userRepository.addUser(any())).thenReturn(userDto);

        User addedUser = userService.addUser(UserMapper.toUser(userDto));
        assertThat(addedUser.getId(), equalTo(2L));
    }

    @Test
    void testUpdateUser() {
        String newName = "new name";
        UserDto info = new UserDto();
        info.setId(1L);
        info.setName(newName);
        info.setEmail("email@mail.ru");

        when(userRepository.updateUser(any(), anyLong())).thenReturn(info);

        User updatedUser = userService.updateUser(UserMapper.toUser(info), info.getId());
        assertThat(updatedUser.getId(), equalTo(1L));
        assertThat(updatedUser.getName(), equalTo(newName));
    }

    @Test
    void testGetUserById() {
        Optional<User> userById = userService.getUserById(1L);

        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get().getId(), equalTo(1L));
    }

    @Test
    @Rollback
    void testDeleteUserById() {
        userService.deleteUserById(1L);
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserById(1L), "нет такого юзера");
    }

    @Test
    void testGetAll() {
        List<User> all = userService.getAll();

        assertThat(all.size(), equalTo(1));
        assertThat(all.get(0).getId(), equalTo(1L));
    }
}