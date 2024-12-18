package de.neuefische.backend.organization;

import de.neuefische.backend.organization.model.Organization;
import de.neuefische.backend.organization.model.OrganizationDTO;
import de.neuefische.backend.organization.model.ReviewDTO;
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
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false, defaultValue = "") String search) {
        return organizationService.getAllOrganizations(page, size, search);
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
    public Organization updateOrganization(@PathVariable String id,
                                           @Valid @RequestBody OrganizationDTO organizationDTO) {
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/reviews")
    public Organization addReview(@PathVariable String id, @RequestBody @Valid ReviewDTO reviewDTO) {
        return organizationService.addReviewToOrganization(id, reviewDTO);
    }

}
