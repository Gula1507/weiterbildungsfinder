package de.neuefische.backend.organization;

import de.neuefische.backend.api.service.ArbeitsagenturApiService;
import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.organization.model.Organization;
import de.neuefische.backend.organization.model.OrganizationDTO;
import de.neuefische.backend.organization.model.Review;
import de.neuefische.backend.organization.model.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class OrganizationService {
    private final MongoTemplate mongoTemplate;
    private final OrganizationRepository organizationRepo;
    private final IdService idService;
    private final ArbeitsagenturApiService apiService;
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);


    public Page<Organization> getAllOrganizations(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(Sort.by(new Sort.Order(Sort.Direction.ASC, "name")))
                                 .with(pageable);
        if (!search.isEmpty()) {
            String sanitizedSearch = Pattern.quote(search);
            query.addCriteria(Criteria.where("name")
                                      .regex(sanitizedSearch, "i"));
        }
        query.collation(Collation.of("en")
                                 .strength(Collation.ComparisonLevel.secondary()));
        List<Organization> sortedOrganizations = mongoTemplate.find(query, Organization.class);
        return PageableExecutionUtils.getPage(sortedOrganizations, pageable, organizationRepo::count);
    }

    public List<Organization> refreshOrganizationsFromApi() {
        List<Organization> apiOrganizations = apiService.loadAllOrganizations();
        List<Organization> savedApiOrganizations = new ArrayList<>();
        for (Organization apiOrganization : apiOrganizations) {
            if (!organizationRepo.existsByApiId(apiOrganization.apiId())) {
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
        String id = idService.generateRandomId();
        Organization organization = new Organization(id, organizationDTO);
        return organizationRepo.save(organization);
    }

    public Organization getOrganizationById(String id) {
        return organizationRepo.findById(id).orElseThrow(() -> new OrganizationNotFoundException(id));
    }

    public Organization updateOrganizationFromDTO(String id, OrganizationDTO organizationDTO) {
        if (!organizationRepo.existsById(id)) {
            throw new OrganizationNotFoundException(id);
        }
        return organizationRepo.save(new Organization(id, organizationDTO));
    }

    public void deleteOrganizationById(String id) {
        if (!organizationRepo.existsById(id)) {
            throw new OrganizationNotFoundException(id);
        } else {
            organizationRepo.deleteById(id);
        }
    }

    public Organization addReviewToOrganization(String organizationId, ReviewDTO reviewDTO) {
        String currentlyLoggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Review review = new Review(idService.generateRandomId(), currentlyLoggedInUserName, reviewDTO.comment(),
                reviewDTO.starNumber());
        Organization organization = organizationRepo.findById(organizationId)
                                                    .orElseThrow(
                                                            () -> new OrganizationNotFoundException(organizationId));
        List<Review> reviews = new ArrayList<>(organization.reviews());
        reviews.add(review);
        double averageRating = reviews.stream()
                                      .mapToInt(Review::starNumber)
                                      .average()
                                      .orElse(0.0);

        Organization actualisedOrganization = organization.withReviews(reviews)
                                                          .withAverageRating(averageRating);
        organizationRepo.save(actualisedOrganization);
        return actualisedOrganization;
    }

}

