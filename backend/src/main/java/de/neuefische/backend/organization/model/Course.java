package de.neuefische.backend.organization.model;

import de.neuefische.backend.api.dto.CourseType;
import lombok.With;

import java.util.Objects;

@With
public record Course(
        String id,
        Long apiCourseId,
        String courseName,
        String courseContent,
        String courseDegree,
        String educationVoucher,
        CourseType courseType,
        Long apiOrganizationId
) {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return Objects.equals(apiCourseId, course.apiCourseId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(apiCourseId);
    }
}


