package de.neuefische.backend.repository;

import de.neuefische.backend.model.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {

    boolean existsByName(String name);
}
