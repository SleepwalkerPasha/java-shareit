package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        if (user.getId() != null) {
            userDto.setId(user.getId());
        }
        if (user.getName() != null) {
            userDto.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userDto.setEmail(user.getEmail());
        }
        return userDto;
    }
}
