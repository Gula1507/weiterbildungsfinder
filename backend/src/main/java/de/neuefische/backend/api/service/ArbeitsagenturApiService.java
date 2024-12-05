package de.neuefische.backend.api.service;

import de.neuefische.backend.api.dto.*;
import de.neuefische.backend.api.exception.ApiResponseException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.service.IdService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsagenturApiService {

    private final RestClient restClient;

    public ArbeitsagenturApiService(RestClient.Builder builder) {
        this.restClient =
                builder.baseUrl("https://rest.arbeitsagentur.de/infosysbub/wbsuche/pc/v2/bildungsangebot").build();
    }

    public List<Organization> loadAllOrganizations() {
        List<Organization> apiOrganizations = new ArrayList<>();
        String urlPage = "?page=0&size=20";

        while (urlPage != null) {
            Optional<ApiResponse> optionalResponse =
                    Optional.ofNullable(restClient.get().uri(urlPage).header("X-API" + "-Key", "infosysbub-wbsuche").retrieve().body(ApiResponse.class));
            ApiResponse response = optionalResponse.orElse(null);
            urlPage = getNextPageUrl(response);

            List<ApiResponseDetails> details =
                    optionalResponse.map(ApiResponse::responseContent).map(ApiResponseContent::details).orElseThrow(ApiResponseException::new);

            List<ApiResponseOrganization> apiResponseOrganizations =
                    details.stream().map(ApiResponseDetails::courseOffer).map(ApiResponseCourseOffer::apiResponseOrganization).distinct().toList();
            apiOrganizations.addAll(getOrganizations(apiResponseOrganizations));

        }
        return apiOrganizations.stream().distinct().toList();

    }

    private List<Organization> getOrganizations(List<ApiResponseOrganization> apiResponseOrganizations) {
        IdService idService = new IdService();
        return apiResponseOrganizations.stream().map(a -> new Organization(idService.generateRandomId(), a.name(),
                a.homepage(), a.email(),
                a.address().addressDetails().postalCode() + " " + a.address().addressDetails().city() + ", " + a.address().streetAndHomeNumber())).toList();
    }

    private String getNextPageUrl(ApiResponse apiResponse) {
        if (apiResponse != null && apiResponse.page() != null && apiResponse.page().number() < 10) {
            return "?page=" + (apiResponse.page().number() + 1) + "&size=20";
        }
        return null;
    }
}
