package de.neuefische.backend.organization.model;

import de.neuefische.backend.api.dto.CourseType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CourseTest {

    @Test
    void equals_shouldReturnTrueForEqualApiCourseId() {
        Course course1 = new Course("1", 23L, "coursename", "coursecontent", "certificat", "voucher", new CourseType(
                "weiterbildung"), 100L);
        Course course2 = course1.withId("2").withCourseName("coursename2");
        assertEquals(course1, course2);
    }

    @Test
    void equals_shouldReturnFalseForNotEqualApiCourseId() {
        Course course1 = new Course("1", 23L, "coursename", "coursecontent", "certificat", "voucher", new CourseType(
                "weiterbildung"), 100L);
        Course course2 = course1.withApiCourseId(55L);
        assertNotEquals(course1, course2);

    }
}
