package ua.profitsoft.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.profitsoft.model.DevBook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BookParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Integer> parseAndCount(File file, String attribute) {
        Map<String, Integer> localStats = new HashMap<>();

        try (var parser = objectMapper.createParser(file)) {
            var iterator = objectMapper.readValues(parser, DevBook.class);

            while (iterator.hasNext()) {
                DevBook book = iterator.next();
                updateStats(localStats, book, attribute);
            }
        } catch (IOException e) {
            System.err.println("Error parsing file " + file.getName() + ": " + e.getMessage());
        }

        return localStats;
    }

    private void updateStats(Map<String, Integer> stats, DevBook book, String attribute) {
        switch (attribute.toLowerCase()) {
            case "author" -> stats.merge(book.author(), 1, Integer::sum);
            case "year" -> stats.merge(String.valueOf(book.yearPublished()), 1, Integer::sum);
            case "tags" -> {
                if (book.tags() != null) {
                    for (String tag : book.tags()) {
                        stats.merge(tag, 1, Integer::sum);
                    }
                }
            }
            default -> throw new IllegalArgumentException("Unknown attribute: " + attribute);
        }
    }
}