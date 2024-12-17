package de.neuefische.backend.organization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdServiceTest {
    private final IdService idService = new IdService();

    @Test
    void generateRandomId_shouldReturnValidUUID() {
        String generatedId = idService.generateRandomId();
        assertTrue(generatedId.matches("^[a-f0-9-]{36}$"));
    }

    @Test
    void generateRandomId_shouldGenerateUniqueIds() {
        String id1 = idService.generateRandomId();
        String id2 = idService.generateRandomId();
        assertNotEquals(id1, id2);
    }
}
