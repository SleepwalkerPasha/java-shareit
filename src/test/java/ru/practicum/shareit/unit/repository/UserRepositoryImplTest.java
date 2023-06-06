package ru.practicum.shareit.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.JpaUserRepository;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRepositoryImplTest {

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private JpaUserRepository jpaUserRepository;

    private UserDto userDto;

    private UserDto ownerDto;

    @BeforeEach
    void setUp() {
        ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        userDto = new UserDto(2L, "user", "adadad133@ya.ru");
    }

    @Test
    void testAddUser() {
        when(jpaUserRepository.save(any())).thenReturn(ownerDto);

        UserDto userDto1 = userRepository.addUser(ownerDto);

        assertThat(userDto1, equalTo(ownerDto));
    }

    @Test
    void testUpdateUser() {
        ownerDto = new UserDto(1L, "owner 1", "email211@mail.ru");

        when(jpaUserRepository.save(any())).thenReturn(ownerDto);

        UserDto userDto1 = userRepository.updateUser(ownerDto);

        assertThat(userDto1, equalTo(ownerDto));
    }

    @Test
    void testGetUserById() {
        when(jpaUserRepository.findById(ownerDto.getId())).thenReturn(Optional.of(ownerDto));

        Optional<UserDto> byId = userRepository.getUserById(ownerDto.getId());

        assertThat(byId.isPresent(), is(true));
        assertThat(byId.get(), equalTo(ownerDto));
    }

    @Test
    void testDeleteUser() {
        when(jpaUserRepository.findById(ownerDto.getId())).thenReturn(Optional.empty());

        userRepository.deleteUser(ownerDto.getId());

        Optional<UserDto> byId = userRepository.getUserById(ownerDto.getId());

        assertThat(byId.isEmpty(), is(true));
    }

    @Test
    void testGetAll() {
        when(jpaUserRepository.findAll()).thenReturn(List.of(ownerDto, userDto));

        List<UserDto> all = userRepository.getAll();

        assertThat(all.size(), equalTo(2));
        assertThat(all.get(0), equalTo(ownerDto));
        assertThat(all.get(1), equalTo(userDto));
    }
}