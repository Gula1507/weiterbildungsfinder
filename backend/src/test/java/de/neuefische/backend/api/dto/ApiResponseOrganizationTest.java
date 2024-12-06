package de.neuefische.backend.api.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ApiResponseOrganizationTest {

    @Test
    void equals_shouldReturnTrueForEqualNames() {
        long id = 123;
        ApiResponseOrganization apiResponseOrganization1 = new ApiResponseOrganization(id, "testname", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        ApiResponseOrganization apiResponseOrganization2 = new ApiResponseOrganization(id, "testname", "testhomepage2"
                , "testmail2", new ApiResponseAddress("testStreet2", new ApiResponseAddressDetails("112", "Hamburg")));
        assertEquals(apiResponseOrganization1, apiResponseOrganization2);
    }

    @Test
    void equals_shouldReturnFalseForNotEqualNames() {
        long id = 123;
        ApiResponseOrganization apiResponseOrganization1 = new ApiResponseOrganization(id, "testname", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        ApiResponseOrganization apiResponseOrganization2 = new ApiResponseOrganization(id, "testname2", "testhomepage"
                , "testmail", new ApiResponseAddress("testStreet", new ApiResponseAddressDetails("11", "Berlin")));
        assertNotEquals(apiResponseOrganization1, apiResponseOrganization2);
    }
}