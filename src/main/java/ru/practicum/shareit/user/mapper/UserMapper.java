package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null)
            user.setId(userDto.getId());
        if (userDto.getName() != null)
            user.setName(userDto.getName());
        if (userDto.getEmail() != null)
            user.setEmail(userDto.getEmail());
        if (userDto.getItems() != null)
            user.setItems(userDto.getItems().stream().map(ItemMapper::toItem).collect(Collectors.toList()));
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        if (user.getId() != null)
            userDto.setId(user.getId());
        if (user.getName() != null)
            userDto.setName(user.getName());
        if (user.getEmail() != null)
            userDto.setEmail(user.getEmail());
        if (user.getItems() != null)
            userDto.setItems(user.getItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList()));
        return userDto;
    }
}
