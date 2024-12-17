package de.neuefische.backend.organization.model;

import de.neuefische.backend.api.dto.CourseType;

public record Course(
        Long courseId,
        String courseName,
        String courseContent,
        String courseDegree,
        String educationVoucher,
        CourseType courseType,
        Long apiOrganizationId
) {
}
