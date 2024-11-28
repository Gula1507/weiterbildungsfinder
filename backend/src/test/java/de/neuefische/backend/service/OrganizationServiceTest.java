package de.neuefische.backend.service;

import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    OrganizationRepository mockedOrganisationRepo = mock(OrganizationRepository.class);
    IdService mockedIdService = mock(IdService.class);
    OrganizationService organizationService = new OrganizationService(mockedOrganisationRepo, mockedIdService);

    @Test
    void getAllOrganizations_returnsEmptyList_whenRepositoryIsEmpty() {
        List<Organization> expected = List.of();
        when(mockedOrganisationRepo.findAll()).thenReturn((List.of()));
        List<Organization> actual = organizationService.getAllOrganizations();
        verify(mockedOrganisationRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getAllOrganizations_shouldReturnAllInputOrganizations() {
        Organization organization1 = new Organization("1", "testname1", "testhomepage1");
        Organization organization2 = new Organization("2", "testname2", "testhomepage2");
        List<Organization> expected = new ArrayList<>(List.of(organization1, organization2));
        when(mockedOrganisationRepo.findAll()).thenReturn(expected);
        List<Organization> actual = organizationService.getAllOrganizations();
        verify(mockedOrganisationRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void saveOrganizationFromDTO_shouldReturnOrganizationWithGeneratedId() {
        OrganizationDTO organizationDTO = new OrganizationDTO("testname", "testhomepage");
        Organization expected = new Organization("123", "testname", "testhomepage");
        when(mockedOrganisationRepo.save(expected)).thenReturn(expected);
        when(mockedIdService.generateRandomId()).thenReturn("123");

        Organization organizationActual = organizationService.saveOrganizationFromDTO(organizationDTO);
        verify(mockedOrganisationRepo).save(expected);
        verify(mockedIdService).generateRandomId();

        assertEquals(expected, organizationActual);

    }

    @Test
    void getOrganizationDTObyId_shouldReturnOrganizationDTO() {
        Organization organization = new Organization("123abc", "Test Organization", "https://testpage.com");
        when(mockedOrganisationRepo.findById("123abc")).thenReturn(Optional.of(organization));
        OrganizationDTO result = organizationService.getOrganizationDTObyId("123abc");
        assertNotNull(result);
        verify(mockedOrganisationRepo).findById("123abc");
        assertEquals("Test Organization", result.name());
        assertEquals("https://testpage.com", result.homepage());
    }

    @Test
    void getOrganizationById_shouldThrowOrganizationNotFoundException() {
        when(mockedOrganisationRepo.findById("nonexistentId")).thenReturn(Optional.empty());
        Assertions.assertThrows(OrganizationNotFoundException.class, () ->
                organizationService.getOrganizationDTObyId("nonexistentId"));

    }

}