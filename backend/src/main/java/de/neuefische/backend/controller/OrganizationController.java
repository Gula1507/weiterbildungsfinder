package de.neuefische.backend.controller;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.model.OrganizationDTO;
import de.neuefische.backend.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public Page<Organization> getAllOrganizations(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return organizationService.getAllOrganizations(page, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Organization addOrganization(@Valid @RequestBody OrganizationDTO organizationDTO) {
        return organizationService.saveOrganizationFromDTO(organizationDTO);
    }

    @GetMapping("/{id}")
    public OrganizationDTO getOrganizationById(@PathVariable String id) {
        return organizationService.getOrganizationDTObyId(id);
    }

    @PutMapping("/{id}")
    public Organization updateOrganization(@PathVariable String id, @Valid @RequestBody OrganizationDTO organizationDTO) {
        return organizationService.updateOrganizationFromDTO(id, organizationDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganizationById(id);
    }

    @PutMapping("/refresh")
    public List<Organization> refreshOrganizationsFromApi() {
        return organizationService.refreshOrganizationsFromApi();
    }
}
