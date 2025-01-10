package de.neuefische.backend.organization.model;


import jakarta.validation.Valid;
import lombok.With;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@With
public record Organization(String id,
        Long apiId,
        String name,
        String homepage,
        String email,
        String address,
        List<@Valid Review> reviews,
        double averageRating,
        List<Course> courses) {

    public Organization(String id, OrganizationDTO organizationDTO) {
        this(id,
                null,
                organizationDTO.name(),
                organizationDTO.homepage(),
                organizationDTO.email(),
                organizationDTO.address(),
                organizationDTO.reviews() != null ? organizationDTO.reviews() : new ArrayList<>(),
                organizationDTO.averageRating(),
                organizationDTO.courses() != null ? organizationDTO.courses() : new ArrayList<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
    }
