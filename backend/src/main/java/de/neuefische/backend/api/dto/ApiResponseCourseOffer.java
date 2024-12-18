package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseCourseOffer(
        @JsonProperty("id")
        Long courseId,

        @JsonProperty("titel")
        String courseName,

        @JsonProperty("inhalt")
       String courseContent,

        @JsonProperty("abschlussbezeichnung")
        String courseDegree,

        @JsonProperty ("foerderung")
        String educationVoucher,

        @JsonProperty("bildungsanbieter")
        ApiResponseOrganization apiResponseOrganization,

        @JsonProperty ("angebotstyp")
        CourseType courseType



) {
}
