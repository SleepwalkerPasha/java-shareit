package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public UserDto addUser(UserDto user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public UserDto updateUser(UserDto updatedUser) {
        return jpaUserRepository.save(updatedUser);
    }

    @Override
    public Optional<UserDto> getUserById(long userId) {
        return jpaUserRepository.findById(userId);
    }

    @Override
    public void deleteUser(long userId) {
        jpaUserRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return jpaUserRepository.findAll();
    }
}
