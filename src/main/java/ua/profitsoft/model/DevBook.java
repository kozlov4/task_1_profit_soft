package ua.profitsoft.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DevBook(
        String title,
        String author,
        @JsonProperty("year_published") int yearPublished,
        List<String> tags
) {}