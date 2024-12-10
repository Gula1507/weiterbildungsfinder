package de.neuefische.backend.service;

import de.neuefische.backend.api.service.ArbeitsagenturApiService;
import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationService {
    private final MongoTemplate mongoTemplate;
    private final OrganizationRepository organizationRepo;
    private final IdService idService;
    private final ArbeitsagenturApiService apiService;
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);


    public Page<Organization> getAllOrganizations(int page, int size) {
        Pageable pageable;
        pageable = PageRequest.of(page, size);
        Query query = new Query().with(Sort.by(new Sort.Order(Sort.Direction.ASC, "name"))).with(pageable);
        query.collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary()));
        List<Organization> sortedOrganizations = mongoTemplate.find(query, Organization.class);

        return PageableExecutionUtils.getPage(sortedOrganizations, pageable, organizationRepo::count);
    }

    public List<Organization> refreshOrganizationsFromApi() {
        List<Organization> apiOrganizations = apiService.loadAllOrganizations();
        List<Organization> savedApiOrganizations = new ArrayList<>();
        for (Organization apiOrganization : apiOrganizations) {
            if (!organizationRepo.existsByName(apiOrganization.name())) {
                organizationRepo.save(apiOrganization);
                savedApiOrganizations.add(apiOrganization);
            }
        }
        if (savedApiOrganizations.isEmpty()) {
            logger.info("No new organizations found in the API to save.");
        } else {
            logger.info("{} new organizations saved from API.", savedApiOrganizations.size());
        }
        return savedApiOrganizations;
    }

    public Organization saveOrganizationFromDTO(OrganizationDTO organizationDTO) {
        Organization organization = new Organization(idService.generateRandomId(), organizationDTO.name(),
                organizationDTO.homepage(), organizationDTO.email(), organizationDTO.address());
        return organizationRepo.save(organization);
    }

    public OrganizationDTO getOrganizationDTObyId(String id) {
        Organization organization =
                organizationRepo.findById(id).orElseThrow(() -> new OrganizationNotFoundException(id));
        return new OrganizationDTO(organization.name(), organization.homepage(), organization.email(),
                organization.address());
    }

    public Organization updateOrganizationFromDTO(String id, @Valid OrganizationDTO organizationDTO) {
        if (organizationRepo.existsById(id)) {
            Organization updatedOrganization = new Organization(id, organizationDTO.name(),
                    organizationDTO.homepage(), organizationDTO.email(), organizationDTO.address());
            return organizationRepo.save(updatedOrganization);
        } else
            throw new OrganizationNotFoundException(id);
    }

    public void deleteOrganizationById(String id) {
        if (!organizationRepo.existsById(id)) {
            throw new OrganizationNotFoundException(id);
        } else {
            organizationRepo.deleteById(id);
        }
    }
}

