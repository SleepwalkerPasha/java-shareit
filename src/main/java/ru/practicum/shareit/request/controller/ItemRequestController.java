package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
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

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest addRequest(@RequestHeader(name = "X-Sharer-User-Id") long userId, @RequestBody ItemRequest request) {
        return itemRequestService.addRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequest> getAllRequestsByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllRequests(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                            @RequestParam(name = "from", defaultValue = "0", required = false) int from,
                                            @RequestParam(name = "size", required = false) int size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequest getRequestById(@RequestHeader(name = "X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
