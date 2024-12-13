package de.neuefische.backend.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public record ReviewDTO(String author, String comment, @Min(1) @Max(5) int starNumber) {
}
