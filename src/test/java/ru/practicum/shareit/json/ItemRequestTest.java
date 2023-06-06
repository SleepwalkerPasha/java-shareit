package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.model.ItemRequest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class ItemRequestTest {

    @Autowired
    private JacksonTester<ItemRequest> json;

    @Test
    void testSerialize() throws IOException {
        ItemRequest request = new ItemRequest();
        request.setDescription("need something");

        String expectedJson = "{\"id\":null,\"description\":\"need something\",\"requestor\":null,\"created\":null,\"items\":[]}";

        assertThat(json.write(request).getJson(), equalTo(expectedJson));
    }

    @Test
    void testDeserialize() throws IOException {
        String context = "{\"id\":null,\"description\":\"need something\",\"requestor\":null,\"created\":null,\"items\":[]}";

        ItemRequest expectedRequest = new ItemRequest();
        expectedRequest.setDescription("need something");

        assertThat(json.parseObject(context), equalTo(expectedRequest));
    }
}
