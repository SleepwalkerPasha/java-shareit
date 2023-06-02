package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.model.BookingRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class BookingRequestTest {

    @Autowired
    private JacksonTester<BookingRequest> json;

    @Test
    void testSerialize() throws IOException {
        LocalDateTime startDate = LocalDateTime.of(2023, 6, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 7, 30, 12, 0);

        BookingRequest request = new BookingRequest();
        request.setItemId(1L);
        request.setStart(startDate);
        request.setEnd(endDate);

        String expectedJson = String.format("{\"itemId\":1,\"start\":\"%s\",\"end\":\"%s\"}",
                startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        assertThat(json.write(request).getJson(), equalTo(expectedJson));
    }

    @Test
    void testDeserialize() throws IOException {
        LocalDateTime startDate = LocalDateTime.of(2023, 6, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 7, 30, 12, 0);

        String context = String.format("{\"itemId\":1,\"start\":\"%s\",\"end\":\"%s\"}",
                startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        BookingRequest expectedRequest = new BookingRequest();
        expectedRequest.setItemId(1L);
        expectedRequest.setStart(startDate);
        expectedRequest.setEnd(endDate);

        assertThat(json.parseObject(context), equalTo(expectedRequest));
    }
}
