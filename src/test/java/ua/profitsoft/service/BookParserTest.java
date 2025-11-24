package ua.profitsoft.service;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookParserTest {

    @Test
    void testParseAndCount() throws IOException {
        File tempFile = File.createTempFile("test_books", ".json");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("""
                [
                  { "title": "B1", "author": "Author A", "year_published": 2020, "tags": ["Java"] },
                  { "title": "B2", "author": "Author A", "year_published": 2021, "tags": ["Spring"] },
                  { "title": "B3", "author": "Author B", "year_published": 2022, "tags": ["Java"] }
                ]
            """);
        }

        BookParser parser = new BookParser();
        Map<String, Integer> statsAuthors = parser.parseAndCount(tempFile, "author");
        Map<String, Integer> statsTags = parser.parseAndCount(tempFile, "tags");


        assertEquals(2, statsAuthors.get("Author A"));
        assertEquals(1, statsAuthors.get("Author B"));

        assertEquals(2, statsTags.get("Java"));
        assertEquals(1, statsTags.get("Spring"));
    }
}