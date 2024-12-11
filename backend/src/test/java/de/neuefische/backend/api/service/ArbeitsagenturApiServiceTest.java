package de.neuefische.backend.api.service;

import de.neuefische.backend.api.dto.*;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.service.IdService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

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
        when(page.number()).thenReturn(5);
        ApiResponse apiResponse = mock(ApiResponse.class);
        when(apiResponse.page()).thenReturn(page);

        String nextPageUrl = apiService.getNextPageUrl(apiResponse);
        assertEquals("?page=6&size=20", nextPageUrl);
    }

    @Test
    void getNextPageUrl_returnsNull_whenResponseIsNull() {

        ApiResponse apiResponse = null;

        String result = apiService.getNextPageUrl(apiResponse);

        assertNull(result);
    }
}
