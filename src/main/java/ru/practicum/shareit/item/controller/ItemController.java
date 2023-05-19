package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.BasicInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public Item addItem(@RequestBody @Validated(BasicInfo.class) Item item, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody @Validated Item item, @PathVariable long itemId, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.updateItem(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable long itemId, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        Item item = service.getItem(itemId, userId);
        return item;
    }

    @GetMapping
    public List<Item> getAllItems(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemByDescription(@RequestParam(name = "text") String text, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.getItemByDescription(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addCommentToItem(@PathVariable long itemId, @RequestHeader(name = "X-Sharer-User-Id") long userId, @RequestBody Comment comment) {
        return service.addCommentToItem(itemId, userId, comment);
    }
}
