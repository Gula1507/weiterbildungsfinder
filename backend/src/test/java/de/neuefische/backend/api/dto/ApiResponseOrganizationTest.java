package de.neuefische.backend.api.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ApiResponseOrganizationTest {

    @Test
    void equals_shouldReturnTrueForEqualIds() {
        long id = 123;
        ApiResponseOrganization apiResponseOrganization1 = new ApiResponseOrganization(id, "testname1", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        ApiResponseOrganization apiResponseOrganization2 = new ApiResponseOrganization(id, "testname2", "testhomepage2"
                , "testmail2", new ApiResponseAddress("testStreet2", new ApiResponseAddressDetails("112", "Hamburg")));
        assertEquals(apiResponseOrganization1, apiResponseOrganization2);
    }

    @Test
    void equals_shouldReturnFalseForNotEqualIds() {
        long id1 = 123;
        long id2 = 456;
        ApiResponseOrganization apiResponseOrganization1 = new ApiResponseOrganization(id1, "testname1", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        ApiResponseOrganization apiResponseOrganization2 = new ApiResponseOrganization(id2, "testname2", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        assertNotEquals(apiResponseOrganization1, apiResponseOrganization2);
    }

    @Test
    void equals_shouldReturnTrue_forSameObject() {
        long id1 = 123;
        ApiResponseOrganization apiResponseOrganization = new ApiResponseOrganization(id1, "testname2", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        assertEquals(apiResponseOrganization, apiResponseOrganization);
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        long id1 = 123;
        ApiResponseOrganization apiResponseOrganization = new ApiResponseOrganization(id1, "testname2", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        assertNotEquals(null, apiResponseOrganization);
    }
}