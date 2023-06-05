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
import ru.practicum.shareit.item.model.BasicInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingInfo;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService service;

    @PostMapping
    public Item addItem(@RequestBody @Validated(BasicInfo.class) Item item,
                        @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody @Validated Item item,
                           @PathVariable long itemId,
                           @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.updateItem(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemBookingInfo getItem(@PathVariable long itemId, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemBookingInfo> getAllItems(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                             @RequestParam(name = "from", defaultValue = "0")
                                             @PositiveOrZero Integer from,
                                             @RequestParam(name = "size", defaultValue = "20")
                                             @Positive Integer size) {
        return service.getAllUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<Item> getItemsByDescription(@RequestParam(name = "text") String text,
                                            @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                            @RequestParam(name = "from", defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "20")
                                           @Positive Integer size) {
        return service.getItemsByDescription(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addCommentToItem(@PathVariable long itemId,
                                    @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                    @RequestBody @Validated Comment comment) {
        return service.addCommentToItem(itemId, userId, comment);
    }
}
