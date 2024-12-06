package de.neuefische.backend.model;


import java.util.Objects;

public record Organization(
        String id,
        String name,
        String homepage,
        String email,
        String address
) {
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
