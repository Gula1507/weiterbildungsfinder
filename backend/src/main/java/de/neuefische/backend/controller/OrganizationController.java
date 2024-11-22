package de.neuefische.backend.controller;

import de.neuefische.backend.model.Organization;
import de.neuefische.backend.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }
}
