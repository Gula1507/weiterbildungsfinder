package de.neuefische.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OrganizationTest {

    @Test
    void equals_shouldReturnTrueForEqualNames() {

        Organization organization1 = new Organization("12", "testname", "testhomepage", "testmail", "address");
        Organization organization2 = new Organization("12", "testname", "testhomepage2", "testmai2", "address2");
        assertEquals(organization1, organization2);
    }

    @Test
    void equals_shouldReturnFalseForNotEqualNames() {

        Organization organization1 = new Organization("12", "testname", "testhomepage", "testmail", "address");
        Organization organization2 = new Organization("12", "testname2", "testhomepage2", "testmai2", "address2");
        assertNotEquals(organization1, organization2);

    }
}