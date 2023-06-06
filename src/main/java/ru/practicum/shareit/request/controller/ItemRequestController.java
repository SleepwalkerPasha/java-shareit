package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest addRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                  @Validated @RequestBody ItemRequest request) {
        return itemRequestService.addRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequest> getAllRequestsByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllRequestsByOwnerId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                            @RequestParam(name = "from", defaultValue = "0")
                                            @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "20")
                                            @Positive Integer size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequest getRequestById(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                      @PathVariable long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
