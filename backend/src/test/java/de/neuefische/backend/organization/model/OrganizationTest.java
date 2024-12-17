package de.neuefische.backend.organization.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OrganizationTest {

    @Test
    void equals_shouldReturnTrueForEqualNames() {

        Organization organization1 = new Organization("12", "testname", "testhomepage", "testmail", "address",
                new ArrayList<>(), 0.0);
        Organization organization2 = new Organization("12", "testname", "testhomepage2", "testmai2", "address2",
                new ArrayList<>(), 0.0);
        assertEquals(organization1, organization2);
    }

    @Test
    void equals_shouldReturnFalseForNotEqualNames() {

        Organization organization1 = new Organization("12", "testname", "testhomepage", "testmail", "address",
                new ArrayList<>(), 0.0);
        Organization organization2 = new Organization("12", "testname2", "testhomepage2", "testmai2", "address2",
                new ArrayList<>(), 0.0);
        assertNotEquals(organization1, organization2);

    }
}