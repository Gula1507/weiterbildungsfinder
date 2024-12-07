package de.neuefische.backend.api.service;

import de.neuefische.backend.api.dto.ApiResponse;
import de.neuefische.backend.api.dto.ApiResponseCourseOffer;
import de.neuefische.backend.api.dto.ApiResponseDetails;
import de.neuefische.backend.api.dto.ApiResponseOrganization;
import de.neuefische.backend.api.exception.ApiResponseException;
import de.neuefische.backend.model.Organization;
import de.neuefische.backend.service.IdService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArbeitsagenturApiService {

    private final RestClient restClient;
    private final IdService idService;

    public ArbeitsagenturApiService(RestClient.Builder builder, IdService idService) {
        this.restClient =
                builder.baseUrl("https://rest.arbeitsagentur.de/infosysbub/wbsuche/pc/v2/bildungsangebot").build();
        this.idService = idService;
    }

    public List<Organization> loadAllOrganizations() {
        List<Organization> apiOrganizations = new ArrayList<>();
        String urlPage = "?page=0&size=20";

        while (urlPage != null) {
            try {
                ApiResponse response =
                        restClient.get().uri(urlPage).header("X-API-Key", "infosysbub-wbsuche").retrieve().body(ApiResponse.class);

                if (response == null || response.responseContent() == null || response.responseContent().details() == null) {
                    System.out.println("Fehler: responseContent ist null für die Seite " + urlPage);
                    break;
                }

                List<ApiResponseDetails> details = response.responseContent().details();

                List<ApiResponseOrganization> apiResponseOrganizations =
                        details.stream().map(ApiResponseDetails::courseOffer).map(ApiResponseCourseOffer::apiResponseOrganization).distinct().toList();

                apiOrganizations.addAll(convertApiOrganizationsToOrganizations(apiResponseOrganizations));
                urlPage = getNextPageUrl(response);
            } catch (Exception e) {
                throw new ApiResponseException();
            }
        }

        return apiOrganizations.stream().distinct().toList();
    }

    public List<Organization> convertApiOrganizationsToOrganizations(List<ApiResponseOrganization> apiResponseOrganizations) {

        return apiResponseOrganizations.stream().map(a -> new Organization(idService.generateRandomId(), a.name(),
                a.homepage(), a.email(),
                a.address().addressDetails().postalCode() + " " + a.address().addressDetails().city() + ", " + a.address().streetAndHomeNumber())).toList();
    }

    public String getNextPageUrl(ApiResponse apiResponse) {
        if (apiResponse != null && apiResponse.page() != null && apiResponse.page().number() < 10) {
            return "?page=" + (apiResponse.page().number() + 1) + "&size=20";
        }
        return null;
    }
}
