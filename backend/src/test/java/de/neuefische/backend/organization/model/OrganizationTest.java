package de.neuefische.backend.organization.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OrganizationTest {

    @Test
    void equals_shouldReturnTrueForEqualNames() {

        Organization organization1 = new Organization("12", 12L,"testname", "testhomepage", "testmail", "address",
                new ArrayList<>(), 0.0, new ArrayList<>());
        Organization organization2 = new Organization("12", 13L,"testname", "testhomepage2", "testmai2", "address2",
                new ArrayList<>(), 0.0, new ArrayList<>());
        assertEquals(organization1, organization2);
    }

    @Test
    void equals_shouldReturnFalseForNotEqualNames() {

        Organization organization1 = new Organization("12", 13L, "testname","testhomepage", "testmail", "address",
                new ArrayList<>(), 0.0,new ArrayList<>());
        Organization organization2 = new Organization("12", 13L,"testname2", "testhomepage2", "testmai2", "address2",
                new ArrayList<>(), 0.0,new ArrayList<>());
        assertNotEquals(organization1, organization2);

    }
}