package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dto.UserDto;

public interface JpaUserRepository extends JpaRepository<UserDto, Long> {
}
