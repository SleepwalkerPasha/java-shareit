package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.Comment;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class CommentTest {

    @Autowired
    private JacksonTester<Comment> json;

    @Test
    void testSerialize() throws IOException {
        Comment comment = new Comment();
        comment.setText("comment 1");

        String expectedJson = "{\"id\":null,\"text\":\"comment 1\",\"item\":null,\"authorName\":null,\"created\":null}";

        assertThat(json.write(comment).getJson(), equalTo(expectedJson));
    }

    @Test
    void testDeserialize() throws IOException {
        String context = "{\"id\":null,\"text\":\"comment 1\",\"item\":null,\"authorName\":null,\"created\":null}";

        Comment comment = new Comment();
        comment.setText("comment 1");

        assertThat(json.parseObject(context), equalTo(comment));
    }
}
