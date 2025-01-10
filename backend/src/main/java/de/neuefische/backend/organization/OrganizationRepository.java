package de.neuefische.backend.organization;

import de.neuefische.backend.organization.model.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {

    boolean existsByName(String name);
    boolean existsByApiId(Long apiId);
}
