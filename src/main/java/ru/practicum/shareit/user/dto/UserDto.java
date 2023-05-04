package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    String name;

    String email;

    List<ItemDto> items = new ArrayList<>();

    public void addItem(ItemDto item) {
        items.add(item);
    }

    public void updateItem(ItemDto itemDto) {
        Optional<ItemDto> dtoOptional = getItemById(itemDto.getId());
        if (dtoOptional.isEmpty())
            throw new NotFoundException("нет итема в списке с таким id " + itemDto.getId());
        items.set(items.indexOf(dtoOptional.get()), itemDto);
    }

    public Optional<ItemDto> getItemById(long id) {
        return items.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    public List<ItemDto> getItems() {
        return items;
    }
}
