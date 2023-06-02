package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.Item;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class ItemTest {

    @Autowired
    private JacksonTester<Item> json;

    @Test
    void testSerialize() throws IOException {
        Item item = new Item();
        item.setName("name");
        item.setDescription("need something");
        item.setAvailable(true);

        String expectedJson = "{\"id\":null,\"name\":\"name\",\"description\":\"need something\",\"available\":true,\"owner\":null,\"requestId\":null,\"comments\":[]}";

        assertThat(json.write(item).getJson(), equalTo(expectedJson));
    }

    @Test
    void testDeserialize() throws IOException {
        String context = "{\"id\":null,\"name\":\"name\",\"description\":\"need something\",\"available\":true,\"owner\":null,\"requestId\":null,\"comments\":[]}";

        Item expectedItem = new Item();
        expectedItem.setName("name");
        expectedItem.setDescription("need something");
        expectedItem.setAvailable(true);

        assertThat(json.parseObject(context), equalTo(expectedItem));
    }

}
