package de.neuefische.backend.service;

import de.neuefische.backend.api.service.ArbeitsagenturApiService;
import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    OrganizationRepository mockedOrganisationRepo = mock(OrganizationRepository.class);
    IdService mockedIdService = mock(IdService.class);
    ArbeitsagenturApiService mockedApiService = mock(ArbeitsagenturApiService.class);
    OrganizationService organizationService = new OrganizationService(mockedOrganisationRepo, mockedIdService,
            mockedApiService);
    private Organization testOrganization;

    @BeforeEach
    void setUp() {

        testOrganization = new Organization("1", "testname", "testhomepage", "testemail", "testaddress");
    }


    @Test
    void getAllOrganizations_returnsEmptyList_whenRepositoryHasNoOrganizations() {
        Page<Organization> expected = Page.empty();
        Pageable pageable = PageRequest.of(0, 10);
        when(mockedOrganisationRepo.findAll(pageable)).thenReturn(expected);

        Page<Organization> actual = organizationService.getAllOrganizations(0, 10);
        verify(mockedOrganisationRepo).findAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void getAllOrganizations_ShouldReturnAllOrganizations() {
        Organization testOrganization2 = new Organization("2", "testname2", "testhomepage2", "testemail2",
                "testaddress2");
        Page<Organization> expected = new PageImpl<>(List.of(testOrganization, testOrganization2));

        Pageable pageable = PageRequest.of(0, 10);
        when(mockedOrganisationRepo.findAll(pageable)).thenReturn(expected);

        Page<Organization> actual = organizationService.getAllOrganizations(0, 10);
        verify(mockedOrganisationRepo).findAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void refreshOrganizationsFromApi_shouldSaveOrganizationFromApi_ifApiOrganizationNotExistInRepo() {
        List<Organization> expected = new ArrayList<>(List.of(testOrganization));
        when(mockedApiService.loadAllOrganizations()).thenReturn(expected);
        when(mockedOrganisationRepo.existsByName(testOrganization.name())).thenReturn(false);
        when(mockedOrganisationRepo.save(testOrganization)).thenReturn(testOrganization);

        List<Organization> actual = organizationService.refreshOrganizationsFromApi();

        verify(mockedOrganisationRepo).save(testOrganization);
        verify(mockedOrganisationRepo).existsByName(testOrganization.name());
        assertEquals(expected, actual);
    }

    @Test
    void saveApiOrganizations_shouldNotSaveOrganizationIfItAlreadyExistsInRepo() {
        List<Organization> expected = new ArrayList<>(List.of(testOrganization));
        when(mockedApiService.loadAllOrganizations()).thenReturn(expected);
        when(mockedOrganisationRepo.existsByName(testOrganization.name())).thenReturn(true);

        List<Organization> actual = organizationService.refreshOrganizationsFromApi();

        verify(mockedOrganisationRepo).existsByName(testOrganization.name());
        verify(mockedOrganisationRepo, times(0)).save(testOrganization);
        Assertions.assertTrue(actual.isEmpty());

    }

    @Test
    void saveOrganizationFromDTO_shouldReturnOrganizationWithGeneratedId() {
        OrganizationDTO organizationDTO = new OrganizationDTO("testname", "testhomepage", "testemail", "testaddress");
        when(mockedOrganisationRepo.save(testOrganization)).thenReturn(testOrganization);
        when(mockedIdService.generateRandomId()).thenReturn("1");
        when(mockedOrganisationRepo.existsByName("testname2")).thenReturn(false);

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
        Assertions.assertThrows(OrganizationNotFoundException.class,
                () -> organizationService.getOrganizationDTObyId("nonexistentId"));
    }

    @Test
    void updateOrganizationFromDTO_shouldReturnUpdatedOrganization_whenOrganizationExists() {
        OrganizationDTO organizationDTO = new OrganizationDTO("Updated Name", "Updated Homepage", "updated@email.com"
                , "Updated Address");
        String id = "1";
        Organization expectedOrganization = new Organization(id, "Updated Name", "Updated Homepage",
                "updated@email" + ".com", "Updated" + " Address");
        when(mockedOrganisationRepo.existsById(id)).thenReturn(true);
        when(mockedOrganisationRepo.save(any(Organization.class))).thenReturn(expectedOrganization);

        Organization actualOrganization = organizationService.updateOrganizationFromDTO(id, organizationDTO);

        verify(mockedOrganisationRepo).existsById(id);
        verify(mockedOrganisationRepo).save(any(Organization.class));
        assertEquals(expectedOrganization, actualOrganization);
    }

    @Test
    void updateOrganizationFromDTO_shouldThrowException_whenOrganizationNotExist() {
        OrganizationDTO organizationDTO = new OrganizationDTO("Updated Name", "Updated Homepage", "updated@email.com"
                , "Updated Address");
        String id = "999";
        when(mockedOrganisationRepo.existsById(id)).thenReturn(false);

        assertThrows(OrganizationNotFoundException.class, () -> organizationService.updateOrganizationFromDTO(id,
                organizationDTO));
        verify(mockedOrganisationRepo).existsById(id);
    }

    @Test
    void deleteById_shouldThrowException_whenOrganizationNotExist() {
        when(mockedOrganisationRepo.existsById("999")).thenReturn(false);
        assertThrows(OrganizationNotFoundException.class, () -> organizationService.deleteOrganizationById("999"));
        verify(mockedOrganisationRepo).existsById("999");
    }

    @Test
    void deleteById_shouldDeleteOrganization_whenExists() {
        when(mockedOrganisationRepo.existsById("1")).thenReturn(true);
        organizationService.deleteOrganizationById("1");
        verify(mockedOrganisationRepo).deleteById("1");
        when(mockedOrganisationRepo.existsById("1")).thenReturn(false);
        assertFalse(mockedOrganisationRepo.existsById("1"));

    }

}