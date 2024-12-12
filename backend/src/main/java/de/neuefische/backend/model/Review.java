package de.neuefische.backend.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record Review(String comment, @Min(1) @Max(5) int starNumber) {

}
