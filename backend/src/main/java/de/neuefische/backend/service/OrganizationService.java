package de.neuefische.backend.service;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepo;

    public List<Organization> getAllOrganizations () {
    return organizationRepo.findAll();}
}
