package de.neuefische.backend.repository;

import de.neuefische.backend.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {
    boolean existsByName(String name);

    @NonNull
    Page<Organization> findAll(@NonNull Pageable pageable);
}
