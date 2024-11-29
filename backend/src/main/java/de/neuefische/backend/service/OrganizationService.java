package de.neuefische.backend.service;

import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepo;
    private final IdService idService;

    public List<Organization> getAllOrganizations() {
        return organizationRepo.findAll();
    }

    public Organization saveOrganizationFromDTO(OrganizationDTO organizationDTO) {
        Organization organization = new Organization(
                idService.generateRandomId(),
                organizationDTO.name(), organizationDTO.homepage(),
                organizationDTO.email(), organizationDTO.address());
        return organizationRepo.save(organization);
    }

    public OrganizationDTO getOrganizationDTObyId(String id) {
        Organization organization = organizationRepo.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(id));
        return new OrganizationDTO(organization.name(), organization.homepage(), organization.email(),
                organization.address());
    }
}
