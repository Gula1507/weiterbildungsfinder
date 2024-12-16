package de.neuefische.backend.service;

import de.neuefische.backend.api.service.ArbeitsagenturApiService;
import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.model.Review;
import de.neuefische.backend.model.ReviewDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

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
    MongoTemplate mockedMongoTemplate = mock(MongoTemplate.class);

    OrganizationService organizationService = new OrganizationService(mockedMongoTemplate, mockedOrganisationRepo,
            mockedIdService, mockedApiService);
    private Organization testOrganization;

    @BeforeEach
    void setUp() {
        testOrganization = new Organization("1", "testname", "testhomepage", "testemail", "testaddress",
                new ArrayList<>(), 0.0);
    }


    @Test
    void getAllOrganizations_returnsEmptyPage_whenRepositoryHasNoOrganizations() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Organization> expected = new PageImpl<>(List.of(), pageable, 0);

        when(mockedMongoTemplate.find(any(Query.class), eq(Organization.class))).thenReturn(List.of());
        when(mockedOrganisationRepo.count()).thenReturn(0L);
        Page<Organization> actual = organizationService.getAllOrganizations(0, 10, "");

        verify(mockedMongoTemplate).find(any(Query.class), eq(Organization.class));
        assertEquals(expected, actual);
    }

    @Test
    void getAllOrganizations_ShouldReturnAllOrganizations() {
        Organization testOrganizationA = new Organization("1", "A-name", "testhomepage", "testemail", "testaddress",
                new ArrayList<>(), 0.0);
        Organization testOrganizationSmallA = new Organization("1", "a-name", "testhomepage", "testemail",
                "testaddress", new ArrayList<>(), 0.0);
        Organization testOrganizationB = new Organization("1", "B-name", "testhomepage", "testemail", "testaddress",
                new ArrayList<>(), 0.0);

        List<Organization> sortedOrganizations = List.of(testOrganizationA, testOrganizationSmallA, testOrganizationB);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Organization> expected = new PageImpl<>(sortedOrganizations, pageable, sortedOrganizations.size());

        when(mockedMongoTemplate.find(any(Query.class), eq(Organization.class))).thenReturn(sortedOrganizations);

        Page<Organization> actual = organizationService.getAllOrganizations(0, 10, "");

        verify(mockedMongoTemplate).find(any(Query.class), eq(Organization.class));

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
        OrganizationDTO organizationDTO = new OrganizationDTO("testname", "testhomepage", "testemail", "testaddress",
                new ArrayList<>(), 0.0);
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
                , "Updated Address", new ArrayList<>(), 0.0);
        String id = "1";
        Organization expectedOrganization = new Organization(id, "Updated Name", "Updated Homepage",
                "updated@email" + ".com", "Updated" + " Address", new ArrayList<>(), 0.0);
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
                , "Updated Address", new ArrayList<>(), 0.0);
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

    @Test
    void addReviewToOrganization_shouldThrowException_whenOrganizationNotFound() {
        ReviewDTO reviewDTO = new ReviewDTO("testauthor", "testcomment", 5);
        String id = "123";
        when(mockedIdService.generateRandomId()).thenReturn("123");
        when(mockedOrganisationRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(OrganizationNotFoundException.class, () -> organizationService.addReviewToOrganization(id,
                reviewDTO));

    }

    @Test
    void addReviewToOrganization_shouldReturnOrganizationWithActualizedReviews_whenOrganizationExist() {
        ReviewDTO reviewDTO = new ReviewDTO("testauthor", "testcomment", 3);
        List<Review> reviews = new ArrayList<>(List.of(new Review("123", "testauthor", "testcomment", 3)));
        when(mockedIdService.generateRandomId()).thenReturn("123");
        when(mockedOrganisationRepo.findById(testOrganization.id())).thenReturn(Optional.ofNullable(testOrganization));

        Organization actual = organizationService.addReviewToOrganization(testOrganization.id(), reviewDTO);
        Organization expected = testOrganization.withReviews(reviews);

        verify(mockedIdService).generateRandomId();
        verify(mockedOrganisationRepo).save(actual);
        verify(mockedOrganisationRepo).findById(testOrganization.id());
        assertEquals(expected, actual);

    }
}