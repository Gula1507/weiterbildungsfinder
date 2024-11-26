package de.neuefische.backend.service;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    OrganizationService organizationService = new OrganizationService(organizationRepository);

    @Test
    void getAllOrganizations_returnsEmptyList_whenRepositoryIsEmpty() {
        List<Organization> expected = List.of();
        when(organizationRepository.findAll()).thenReturn((List.of()));
        List<Organization> actual = organizationService.getAllOrganizations();
        verify(organizationRepository).findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllOrganizations_shouldReturnAllInputOrganizations() {
        Organization organization1 = new Organization(1, "testname1", "testhomepage1");
        Organization organization2 = new Organization(1, "testname2", "testhomepage2");
        List<Organization> expected = new ArrayList<>(List.of(organization1, organization2));
        when(organizationRepository.findAll()).thenReturn(expected);
        List<Organization> actual = organizationService.getAllOrganizations();
        verify(organizationRepository).findAll();
        Assertions.assertEquals(expected, actual);
    }
}