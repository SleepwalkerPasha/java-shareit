package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.ItemBookingInfo;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class ItemBookingInfoTest {

    @Autowired
    private JacksonTester<ItemBookingInfo> json;

    @Test
    void testSerialize() throws IOException {
        ItemBookingInfo info = new ItemBookingInfo();
        info.setName("name");
        info.setDescription("need something");
        info.setAvailable(true);

        String expectedJson = "{\"id\":null,\"name\":\"name\",\"description\":\"need something\",\"available\":true,\"lastBooking\":null,\"nextBooking\":null,\"comments\":[]}";

        assertThat(json.write(info).getJson(), equalTo(expectedJson));
    }

    @Test
    void testDeserialize() throws IOException {
        String context = "{\"id\":null,\"name\":\"name\",\"description\":\"need something\",\"available\":true,\"lastBooking\":null,\"nextBooking\":null,\"comments\":[]}";

        ItemBookingInfo expectedInfo = new ItemBookingInfo();
        expectedInfo.setName("name");
        expectedInfo.setDescription("need something");
        expectedInfo.setAvailable(true);

        assertThat(json.parseObject(context), equalTo(expectedInfo));
    }
}
