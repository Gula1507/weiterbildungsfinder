package de.neuefische.backend.service;

import de.neuefische.backend.api.service.ArbeitsagenturApiService;
import de.neuefische.backend.exception.OrganizationNotFoundException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.repository.OrganizationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepo;
    private final IdService idService;
    private final ArbeitsagenturApiService apiService;

    public List<Organization> getAllOrganizations() {
        List<Organization> allOrganizations = new ArrayList<>();
        allOrganizations.addAll(organizationRepo.findAll());
        allOrganizations.addAll(apiService.loadAllOrganizations());
        return allOrganizations;
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

    public Organization updateOrganizationFromDTO(String id, @Valid OrganizationDTO organizationDTO) {
        if (organizationRepo.existsById(id)) {
            Organization updatedOrganization = new Organization(id, organizationDTO.name(), organizationDTO.homepage(),
                    organizationDTO.email(),
                    organizationDTO.address());
            return organizationRepo.save(updatedOrganization);
        } else throw new OrganizationNotFoundException(id);
    }

    public void deleteOrganizationById(String id) {
        if (!organizationRepo.existsById(id)) {
            throw new OrganizationNotFoundException(id);
        } else {
            organizationRepo.deleteById(id);
        }
    }
}

