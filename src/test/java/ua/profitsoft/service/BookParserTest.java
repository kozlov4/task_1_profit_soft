package ua.profitsoft.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookParserTest {

    @Test
    void testStandardFile() throws IOException {
        File tempFile = createTempFile("""
            [
              { "title": "B1", "author": "Author A", "year_published": 2020, "tags": ["Java"] },
              { "title": "B2", "author": "Author A", "year_published": 2021, "tags": ["Spring"] },
              { "title": "B3", "author": "Author B", "year_published": 2022, "tags": ["Java"] }
            ]
        """);

        BookParser parser = new BookParser();
        Map<String, Integer> stats = parser.parseAndCount(tempFile, "author");

        assertEquals(2, stats.get("Author A"));
        assertEquals(1, stats.get("Author B"));
        assertEquals(2, stats.size());
    }

    @Test
    void testEmptyJson() throws IOException {
        File tempFile = createTempFile("[]");

        BookParser parser = new BookParser();
        Map<String, Integer> stats = parser.parseAndCount(tempFile, "author");

        assertTrue(stats.isEmpty(), "Statistics should be empty for empty JSON");
    }

    @Test
    void testMissingAttributes() throws IOException {
        File tempFile = createTempFile("""
            [
              { "title": "No Author Book", "year_published": 2020, "tags": ["Java"] },
              { "title": "Normal Book", "author": "John", "year_published": 2021 }
            ]
        """);

        BookParser parser = new BookParser();
        Map<String, Integer> stats = parser.parseAndCount(tempFile, "author");

        assertEquals(1, stats.get("John"));
        assertEquals(1, stats.size());
    }

    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("test_books", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }
}