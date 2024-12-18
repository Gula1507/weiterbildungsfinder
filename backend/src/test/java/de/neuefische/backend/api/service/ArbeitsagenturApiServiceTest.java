package de.neuefische.backend.api.service;

import de.neuefische.backend.api.dto.*;
import de.neuefische.backend.organization.IdService;
import de.neuefische.backend.organization.model.Course;
import de.neuefische.backend.organization.model.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.assertions.Assertions.assertNull;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArbeitsagenturApiServiceTest {
    IdService idService = new IdService();
    ArbeitsagenturApiService apiService = new ArbeitsagenturApiService(RestClient.builder(), idService);

    @Test
    void convertApiOrganizationsToOrganizations_returnsEmptyList_whenNoApiResponseOrganization() {
        List<ApiResponseOrganization> apiResponseOrganizations = List.of();

        List<Organization> organizations = apiService.convertApiOrganizationsToOrganizations(apiResponseOrganizations);
        assertTrue(organizations.isEmpty());
    }

    @Test
    void convertApiOrganizationsToOrganizations_returnsList_whenApiResponseOrganizationsAreProvided() {
        ApiResponseAddressDetails addressDetails = new ApiResponseAddressDetails("99084", "Erfurt");
        ApiResponseAddress address = new ApiResponseAddress("Juri-Gagarin-Ring 152", addressDetails);

        ApiResponseOrganization apiResponseOrganization = new ApiResponseOrganization(1L, "Test Organization", "https"
                                                                                                               +
                                                                                                               "://example.com", "email@example.com", address);

        List<ApiResponseOrganization> apiResponseOrganizations = List.of(apiResponseOrganization);

        List<Organization> organizations = apiService.convertApiOrganizationsToOrganizations(apiResponseOrganizations);

        assertNotNull(organizations);
        assertEquals(1, organizations.size());
        assertEquals("Test Organization", organizations.getFirst().name());
        assertEquals("Juri-Gagarin-Ring 152, 99084 Erfurt", organizations.getFirst().address());

    }

    @Test
    void getNextPageUrl_returnsNull_whenPageIsNull() {

        ApiResponse apiResponse = mock(ApiResponse.class);
        when(apiResponse.page()).thenReturn(null);

        String nextPageUrl = apiService.getNextPageUrl(apiResponse);

        assertNull(nextPageUrl);
    }

    @Test
    void getNextPageUrl_returnsCorrectUrl_whenPageNumberIsLessThan10() {

        ApiResponsePage page = mock(ApiResponsePage.class);
        when(page.number()).thenReturn(1);
        ApiResponse apiResponse = mock(ApiResponse.class);
        when(apiResponse.page()).thenReturn(page);
        when(apiResponse.page().totalPages()).thenReturn(10);

        String nextPageUrl = apiService.getNextPageUrl(apiResponse);
        assertEquals("?page=2&size=20", nextPageUrl);
    }

    @Test
    void getNextPageUrl_returnsNull_whenResponseIsNull() {

        ApiResponse apiResponse = null;

        String result = apiService.getNextPageUrl(apiResponse);

        assertNull(result);
    }

    @Test
    void addCoursesToOrganizations() {
        Organization organization1 = new Organization("1", 120L, "testname1", "testpage1",
                "testemail1", "testadress1"
                , new ArrayList<>(), 0.00, new ArrayList<>());
        Organization organization2 = new Organization("2", 1202L, "testname2", "testpage2",
                "testemail2",
                "testadress2", new ArrayList<>(), 0.00, new ArrayList<>());
        List<Organization> organizationsWithoutCourses = new ArrayList<>(List.of(organization1, organization2));

        List<Course> courses = new ArrayList<>(List.of(new Course(10L, "course1",
                "content1", "certificate1", "yes",
                new CourseType("weiterbildung"), 10L)));

        Organization organizationWithCourses = new Organization("2", 1202L, "testname2",
                "testpage2", "testemail2",
                "testadress2", new ArrayList<>(), 0.00, courses);

        List<Organization> expected = new ArrayList<>(List.of(organization1, organizationWithCourses));

        List<Organization> actual = apiService.addCoursesToOrganizations(organizationsWithoutCourses, courses);

        assertEquals(expected, actual);
    }
}
