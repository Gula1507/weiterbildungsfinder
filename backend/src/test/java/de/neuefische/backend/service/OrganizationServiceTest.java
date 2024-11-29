package de.neuefische.backend.service;

import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private Organization testOrganization;

    @BeforeEach
    void setUp() {

        testOrganization = new Organization("1", "testname", "testhomepage",
                "testemail", "testaddress");
    }


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
        Organization testOrganization2 = new Organization("2", "testname2", "testhomepage2",
                "testemail2", "testaddress2");
        List<Organization> expected = new ArrayList<>(List.of(testOrganization, testOrganization2));
        when(mockedOrganisationRepo.findAll()).thenReturn(expected);
        List<Organization> actual = organizationService.getAllOrganizations();
        verify(mockedOrganisationRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void saveOrganizationFromDTO_shouldReturnOrganizationWithGeneratedId() {
        OrganizationDTO organizationDTO = new OrganizationDTO("testname", "testhomepage",
                "testemail", "testaddress");
        when(mockedOrganisationRepo.save(testOrganization)).thenReturn(testOrganization);
        when(mockedIdService.generateRandomId()).thenReturn("1");

        Organization organizationActual = organizationService.saveOrganizationFromDTO(organizationDTO);
        verify(mockedOrganisationRepo).save(testOrganization);
        verify(mockedIdService).generateRandomId();

        assertEquals(testOrganization, organizationActual);

    }

    @Test
    void getOrganizationDTObyId_shouldReturnOrganizationDTO() {
        when(mockedOrganisationRepo.findById("1")).thenReturn(Optional.of(testOrganization));
        OrganizationDTO result = organizationService.getOrganizationDTObyId("1");
        assertNotNull(result);
        verify(mockedOrganisationRepo).findById("1");
        assertEquals("testname", result.name());
        assertEquals("testhomepage", result.homepage());
        assertEquals("testemail", result.email());
        assertEquals("testaddress", result.address());
    }

    @Test
    void getOrganizationById_shouldThrowOrganizationNotFoundException() {
        when(mockedOrganisationRepo.findById("nonexistentId")).thenReturn(Optional.empty());
        Assertions.assertThrows(OrganizationNotFoundException.class, () ->
                organizationService.getOrganizationDTObyId("nonexistentId"));

    }

}